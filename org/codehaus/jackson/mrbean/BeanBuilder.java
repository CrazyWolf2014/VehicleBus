package org.codehaus.jackson.mrbean;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.MethodVisitor;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;

public class BeanBuilder {
    protected Map<String, Property> _beanProperties;
    protected final Class<?> _implementedType;
    protected LinkedHashMap<String, Method> _unsupportedMethods;

    private static class Property {
        protected final String _fieldName;
        protected Method _getter;
        protected final String _name;
        protected Method _setter;

        public Property(String name) {
            this._name = name;
            this._fieldName = "_" + name;
        }

        public String getName() {
            return this._name;
        }

        public void setGetter(Method m) {
            this._getter = m;
        }

        public void setSetter(Method m) {
            this._setter = m;
        }

        public Method getGetter() {
            return this._getter;
        }

        public Method getSetter() {
            return this._setter;
        }

        public String getFieldName() {
            return this._fieldName;
        }

        public boolean hasConcreteGetter() {
            return this._getter != null && BeanUtil.isConcrete(this._getter);
        }

        public boolean hasConcreteSetter() {
            return this._setter != null && BeanUtil.isConcrete(this._setter);
        }

        private TypeDescription getterType() {
            return new TypeDescription(TypeFactory.type(this._getter.getGenericReturnType(), this._getter.getDeclaringClass()));
        }

        private TypeDescription setterType() {
            return new TypeDescription(TypeFactory.type(this._setter.getGenericParameterTypes()[0], this._setter.getDeclaringClass()));
        }

        public TypeDescription selectType() {
            if (this._getter == null) {
                return setterType();
            }
            if (this._setter == null) {
                return getterType();
            }
            TypeDescription st = setterType();
            TypeDescription gt = getterType();
            TypeDescription specificType = TypeDescription.moreSpecificType(st, gt);
            if (specificType != null) {
                return specificType;
            }
            throw new IllegalArgumentException("Invalid property '" + getName() + "': incompatible types for getter/setter (" + gt + " vs " + st + ")");
        }
    }

    private static class TypeDescription {
        private final Type _asmType;
        private JavaType _jacksonType;

        public TypeDescription(JavaType type) {
            this._jacksonType = type;
            this._asmType = Type.getType(type.getRawClass());
        }

        public Class<?> getRawClass() {
            return this._jacksonType.getRawClass();
        }

        public String erasedSignature() {
            return this._jacksonType.getErasedSignature();
        }

        public String genericSignature() {
            return this._jacksonType.getGenericSignature();
        }

        public boolean hasGenerics() {
            return this._jacksonType.hasGenericTypes();
        }

        public int getLoadOpcode() {
            return this._asmType.getOpcode(21);
        }

        public int getReturnOpcode() {
            return this._asmType.getOpcode(Opcodes.IRETURN);
        }

        public String toString() {
            return this._jacksonType.toString();
        }

        public static TypeDescription moreSpecificType(TypeDescription desc1, TypeDescription desc2) {
            Class<?> c1 = desc1.getRawClass();
            Class<?> c2 = desc2.getRawClass();
            if (c1.isAssignableFrom(c2)) {
                return desc2;
            }
            if (c2.isAssignableFrom(c1)) {
                return desc1;
            }
            return null;
        }
    }

    public BeanBuilder(Class<?> implType) {
        this._beanProperties = new LinkedHashMap();
        this._unsupportedMethods = new LinkedHashMap();
        this._implementedType = implType;
    }

    public BeanBuilder implement(boolean failOnUnrecognized) {
        ArrayList<Class<?>> implTypes = new ArrayList();
        implTypes.add(this._implementedType);
        BeanUtil.findSuperTypes(this._implementedType, Object.class, implTypes);
        Iterator it = implTypes.iterator();
        while (it.hasNext()) {
            for (Method m : ((Class) it.next()).getDeclaredMethods()) {
                String methodName = m.getName();
                int argCount = m.getParameterTypes().length;
                if (argCount == 0) {
                    if (methodName.startsWith("get") || (methodName.startsWith("is") && returnsBoolean(m))) {
                        addGetter(m);
                    }
                    if (!(BeanUtil.isConcrete(m) || this._unsupportedMethods.containsKey(methodName))) {
                        if (failOnUnrecognized) {
                            this._unsupportedMethods.put(methodName, m);
                        } else {
                            throw new IllegalArgumentException("Unrecognized abstract method '" + methodName + "' (not a getter or setter) -- to avoid exception, disable AbstractTypeMaterializer.Feature.FAIL_ON_UNMATERIALIZED_METHOD");
                        }
                    }
                }
                if (argCount == 1 && methodName.startsWith("set")) {
                    addSetter(m);
                }
                if (failOnUnrecognized) {
                    this._unsupportedMethods.put(methodName, m);
                } else {
                    throw new IllegalArgumentException("Unrecognized abstract method '" + methodName + "' (not a getter or setter) -- to avoid exception, disable AbstractTypeMaterializer.Feature.FAIL_ON_UNMATERIALIZED_METHOD");
                }
            }
        }
        return this;
    }

    public byte[] build(String className) {
        String superName;
        ClassWriter cw = new ClassWriter(1);
        String internalClass = getInternalClassName(className);
        String implName = getInternalClassName(this._implementedType.getName());
        if (this._implementedType.isInterface()) {
            superName = "java/lang/Object";
            cw.visit(49, 33, internalClass, null, superName, new String[]{implName});
        } else {
            superName = implName;
            cw.visit(49, 33, internalClass, null, implName, null);
        }
        cw.visitSource(className + ".java", null);
        generateDefaultConstructor(cw, superName);
        for (Property prop : this._beanProperties.values()) {
            TypeDescription type = prop.selectType();
            createField(cw, prop, type);
            if (!prop.hasConcreteGetter()) {
                createGetter(cw, internalClass, prop, type);
            }
            if (!prop.hasConcreteSetter()) {
                createSetter(cw, internalClass, prop, type);
            }
        }
        for (Method createUnimplementedMethod : this._unsupportedMethods.values()) {
            createUnimplementedMethod(cw, internalClass, createUnimplementedMethod);
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    private static String getPropertyName(String methodName) {
        String body = methodName.substring(methodName.startsWith("is") ? 2 : 3);
        StringBuilder sb = new StringBuilder(body);
        sb.setCharAt(0, Character.toLowerCase(body.charAt(0)));
        return sb.toString();
    }

    private static String buildGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String buildSetterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String getInternalClassName(String className) {
        return className.replace(".", FilePathGenerator.ANDROID_DIR_SEP);
    }

    private void addGetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getGetter() == null) {
            prop.setGetter(m);
        }
    }

    private void addSetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getSetter() == null) {
            prop.setSetter(m);
        }
    }

    private Property findProperty(String propName) {
        Property prop = (Property) this._beanProperties.get(propName);
        if (prop != null) {
            return prop;
        }
        prop = new Property(propName);
        this._beanProperties.put(propName, prop);
        return prop;
    }

    private static final boolean returnsBoolean(Method m) {
        Class<?> rt = m.getReturnType();
        return rt == Boolean.class || rt == Boolean.TYPE;
    }

    private static void generateDefaultConstructor(ClassWriter cw, String superName) {
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createField(ClassWriter cw, Property prop, TypeDescription type) {
        String sig;
        if (type.hasGenerics()) {
            sig = type.genericSignature();
        } else {
            sig = null;
        }
        ClassWriter classWriter = cw;
        classWriter.visitField(1, prop.getFieldName(), type.erasedSignature(), sig, null).visitEnd();
    }

    private static void createSetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        String sig;
        Method setter = prop.getSetter();
        if (setter != null) {
            desc = Type.getMethodDescriptor(setter);
            methodName = setter.getName();
        } else {
            desc = "(" + propertyType.erasedSignature() + ")V";
            methodName = buildSetterName(prop.getName());
        }
        if (propertyType.hasGenerics()) {
            sig = "(" + propertyType.genericSignature() + ")V";
        } else {
            sig = null;
        }
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitVarInsn(propertyType.getLoadOpcode(), 1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createGetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        String sig;
        Method getter = prop.getGetter();
        if (getter != null) {
            desc = Type.getMethodDescriptor(getter);
            methodName = getter.getName();
        } else {
            desc = "()" + propertyType.erasedSignature();
            methodName = buildGetterName(prop.getName());
        }
        if (propertyType.hasGenerics()) {
            sig = "()" + propertyType.genericSignature();
        } else {
            sig = null;
        }
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(propertyType.getReturnOpcode());
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createUnimplementedMethod(ClassWriter cw, String internalClassName, Method method) {
        String exceptionName = getInternalClassName(UnsupportedOperationException.class.getName());
        String sig = Type.getMethodDescriptor(method);
        String name = method.getName();
        MethodVisitor mv = cw.visitMethod(1, name, sig, null, null);
        mv.visitTypeInsn(Opcodes.NEW, exceptionName);
        mv.visitInsn(89);
        mv.visitLdcInsn("Unimplemented method '" + name + "' (not a setter/getter, could not materialize)");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionName, "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
}
