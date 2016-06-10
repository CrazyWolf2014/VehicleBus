package org.codehaus.jackson.map.introspect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BasicBeanDescription extends BeanDescription {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected TypeBindings _bindings;
    protected final AnnotatedClass _classInfo;

    public BasicBeanDescription(JavaType type, AnnotatedClass ac, AnnotationIntrospector ai) {
        super(type);
        this._classInfo = ac;
        this._annotationIntrospector = ai;
    }

    public boolean hasKnownClassAnnotations() {
        return this._classInfo.hasAnnotations();
    }

    public Annotations getClassAnnotations() {
        return this._classInfo.getAnnotations();
    }

    public TypeBindings bindingsForBeanType() {
        if (this._bindings == null) {
            this._bindings = new TypeBindings(this._type);
        }
        return this._bindings;
    }

    public AnnotatedClass getClassInfo() {
        return this._classInfo;
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._classInfo.findMethod(name, paramTypes);
    }

    public Object instantiateBean(boolean fixAccess) {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        if (fixAccess) {
            ac.fixAccess();
        }
        try {
            return ac.getAnnotated().newInstance(new Object[0]);
        } catch (Throwable e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            } else if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            } else {
                throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
            }
        }
    }

    public LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> visibilityChecker, Collection<String> ignoredProperties) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap();
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 0) {
                String propName = this._annotationIntrospector.findGettablePropertyName(am);
                if (propName == null) {
                    propName = am.getName();
                    if (propName.startsWith("get")) {
                        if (visibilityChecker.isGetterVisible(am)) {
                            propName = okNameForGetter(am, propName);
                        } else {
                            continue;
                        }
                    } else if (visibilityChecker.isIsGetterVisible(am)) {
                        propName = okNameForIsGetter(am, propName);
                    } else {
                        continue;
                    }
                    if (propName == null) {
                        continue;
                    } else if (this._annotationIntrospector.hasAnyGetterAnnotation(am)) {
                    }
                } else if (propName.length() == 0) {
                    propName = okNameForAnyGetter(am, am.getName());
                    if (propName == null) {
                        propName = am.getName();
                    }
                }
                if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                    AnnotatedMethod old = (AnnotatedMethod) results.put(propName, am);
                    if (old != null) {
                        String oldDesc = old.getFullName();
                        throw new IllegalArgumentException("Conflicting getter definitions for property \"" + propName + "\": " + oldDesc + " vs " + am.getFullName());
                    }
                }
            }
        }
        return results;
    }

    public AnnotatedMethod findJsonValueMethod() {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAsValueAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with active 'as-value' annotation (" + found.getName() + "(), " + am.getName() + ")");
                } else if (ClassUtil.hasGetterSignature(am.getAnnotated())) {
                    found = am;
                } else {
                    throw new IllegalArgumentException("Method " + am.getName() + "() marked with an 'as-value' annotation, but does not have valid getter signature (non-static, takes no args, returns a value)");
                }
            }
        }
        return found;
    }

    public Constructor<?> findDefaultConstructor() {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        return ac.getAnnotated();
    }

    public List<AnnotatedConstructor> getConstructors() {
        return this._classInfo.getConstructors();
    }

    public List<AnnotatedMethod> getFactoryMethods() {
        List<AnnotatedMethod> candidates = this._classInfo.getStaticMethods();
        if (candidates.isEmpty()) {
            return candidates;
        }
        ArrayList<AnnotatedMethod> result = new ArrayList();
        for (AnnotatedMethod am : candidates) {
            if (isFactoryMethod(am)) {
                result.add(am);
            }
        }
        return result;
    }

    public Constructor<?> findSingleArgConstructor(Class<?>... argTypes) {
        for (AnnotatedConstructor ac : this._classInfo.getConstructors()) {
            if (ac.getParameterCount() == 1) {
                Class<?> actArg = ac.getParameterClass(0);
                for (Class<?> expArg : argTypes) {
                    if (expArg == actArg) {
                        return ac.getAnnotated();
                    }
                }
                continue;
            }
        }
        return null;
    }

    public Method findFactoryMethod(Class<?>... expArgTypes) {
        for (AnnotatedMethod am : this._classInfo.getStaticMethods()) {
            if (isFactoryMethod(am)) {
                Class<?> actualArgType = am.getParameterClass(0);
                for (Class<?> expArgType : expArgTypes) {
                    if (actualArgType.isAssignableFrom(expArgType)) {
                        return am.getAnnotated();
                    }
                }
                continue;
            }
        }
        return null;
    }

    protected boolean isFactoryMethod(AnnotatedMethod am) {
        if (!getBeanClass().isAssignableFrom(am.getRawType())) {
            return false;
        }
        if (this._annotationIntrospector.hasCreatorAnnotation(am)) {
            return true;
        }
        if ("valueOf".equals(am.getName())) {
            return true;
        }
        return false;
    }

    public List<String> findCreatorPropertyNames() {
        List<String> names = null;
        int i = 0;
        while (i < 2) {
            for (AnnotatedWithParams creator : i == 0 ? getConstructors() : getFactoryMethods()) {
                int argCount = creator.getParameterCount();
                if (argCount >= 1) {
                    String name = this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(0));
                    if (name != null) {
                        if (names == null) {
                            names = new ArrayList();
                        }
                        names.add(name);
                        for (int p = 1; p < argCount; p++) {
                            names.add(this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(p)));
                        }
                    }
                }
            }
            i++;
        }
        if (names == null) {
            return Collections.emptyList();
        }
        return names;
    }

    public LinkedHashMap<String, AnnotatedField> findSerializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, true);
    }

    public Inclusion findSerializationInclusion(Inclusion defValue) {
        return this._annotationIntrospector.findSerializationInclusion(this._classInfo, defValue);
    }

    public LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> vchecker) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap();
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 1) {
                String propName = this._annotationIntrospector.findSettablePropertyName(am);
                if (propName != null) {
                    if (propName.length() == 0) {
                        propName = okNameForSetter(am);
                        if (propName == null) {
                            propName = am.getName();
                        }
                    }
                } else if (vchecker.isSetterVisible(am)) {
                    propName = okNameForSetter(am);
                    if (propName == null) {
                    }
                } else {
                    continue;
                }
                AnnotatedMethod old = (AnnotatedMethod) results.put(propName, am);
                if (old == null) {
                    continue;
                } else if (old.getDeclaringClass() == am.getDeclaringClass()) {
                    String oldDesc = old.getFullName();
                    throw new IllegalArgumentException("Conflicting setter definitions for property \"" + propName + "\": " + oldDesc + " vs " + am.getFullName());
                } else {
                    results.put(propName, old);
                }
            }
        }
        return results;
    }

    public AnnotatedMethod findAnySetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAnySetterAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with 'any-setter' annotation (" + found.getName() + "(), " + am.getName() + ")");
                }
                int pcount = am.getParameterCount();
                if (pcount != 2) {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): takes " + pcount + " parameters, should take 2");
                }
                Class<?> type = am.getParameterClass(0);
                if (type == String.class || type == Object.class) {
                    found = am;
                } else {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): first argument not of type String or Object, but " + type.getName());
                }
            }
        }
        return found;
    }

    public AnnotatedMethod findAnyGetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAnyGetterAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with 'any-getter' annotation (" + found.getName() + "(), " + am.getName() + ")");
                }
                if (Map.class.isAssignableFrom(am.getRawType())) {
                    found = am;
                } else {
                    throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + am.getName() + "(): return type is not instance of java.util.Map");
                }
            }
        }
        return found;
    }

    public Map<String, AnnotatedMember> findBackReferenceProperties() {
        ReferenceProperty prop;
        HashMap<String, AnnotatedMember> result = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 1) {
                prop = this._annotationIntrospector.findReferenceType(am);
                if (prop != null && prop.isBackReference()) {
                    if (result == null) {
                        result = new HashMap();
                    }
                    if (result.put(prop.getName(), am) != null) {
                        throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
                    }
                }
            }
        }
        for (AnnotatedField af : this._classInfo.fields()) {
            prop = this._annotationIntrospector.findReferenceType(af);
            if (prop != null && prop.isBackReference()) {
                if (result == null) {
                    result = new HashMap();
                }
                if (result.put(prop.getName(), af) != null) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
                }
            }
        }
        return result;
    }

    public LinkedHashMap<String, AnnotatedField> findDeserializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, false);
    }

    public String okNameForAnyGetter(AnnotatedMethod am, String name) {
        String str = okNameForIsGetter(am, name);
        if (str == null) {
            return okNameForGetter(am, name);
        }
        return str;
    }

    public String okNameForGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("get")) {
            return null;
        }
        if ("getCallbacks".equals(name)) {
            if (isCglibGetCallbacks(am)) {
                return null;
            }
        } else if ("getMetaClass".equals(name) && isGroovyMetaClassGetter(am)) {
            return null;
        }
        return mangleGetterName(am, name.substring(3));
    }

    public String okNameForIsGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("is")) {
            return null;
        }
        Class<?> rt = am.getRawType();
        if (rt == Boolean.class || rt == Boolean.TYPE) {
            return mangleGetterName(am, name.substring(2));
        }
        return null;
    }

    protected String mangleGetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    protected boolean isCglibGetCallbacks(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || !rt.isArray()) {
            return false;
        }
        Package pkg = rt.getComponentType().getPackage();
        if (pkg == null) {
            return false;
        }
        String pname = pkg.getName();
        if (pname.startsWith("net.sf.cglib") || pname.startsWith("org.hibernate.repackage.cglib")) {
            return true;
        }
        return false;
    }

    protected boolean isGroovyMetaClassSetter(AnnotatedMethod am) {
        Package pkg = am.getParameterClass(0).getPackage();
        if (pkg == null || !pkg.getName().startsWith("groovy.lang")) {
            return false;
        }
        return true;
    }

    protected boolean isGroovyMetaClassGetter(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || rt.isArray()) {
            return false;
        }
        Package pkg = rt.getPackage();
        if (pkg == null || !pkg.getName().startsWith("groovy.lang")) {
            return false;
        }
        return true;
    }

    public String okNameForSetter(AnnotatedMethod am) {
        String name = am.getName();
        if (!name.startsWith("set")) {
            return null;
        }
        name = mangleSetterName(am, name.substring(3));
        if (name == null) {
            return null;
        }
        if ("metaClass".equals(name) && isGroovyMetaClassSetter(am)) {
            return null;
        }
        return name;
    }

    protected String mangleSetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    public LinkedHashMap<String, AnnotatedField> _findPropertyFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties, boolean forSerialization) {
        LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap();
        for (AnnotatedField af : this._classInfo.fields()) {
            String propName = forSerialization ? this._annotationIntrospector.findSerializablePropertyName(af) : this._annotationIntrospector.findDeserializablePropertyName(af);
            if (propName != null) {
                if (propName.length() == 0) {
                    propName = af.getName();
                }
            } else if (vchecker.isFieldVisible(af)) {
                propName = af.getName();
            } else {
                continue;
            }
            if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                AnnotatedField old = (AnnotatedField) results.put(propName, af);
                if (old != null && old.getDeclaringClass() == af.getDeclaringClass()) {
                    throw new IllegalArgumentException("Multiple fields representing property \"" + propName + "\": " + old.getFullName() + " vs " + af.getFullName());
                }
            }
        }
        return results;
    }

    public static String manglePropertyName(String basename) {
        int len = basename.length();
        if (len == 0) {
            return null;
        }
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            char upper = basename.charAt(i);
            char lower = Character.toLowerCase(upper);
            if (upper == lower) {
                break;
            }
            if (sb == null) {
                sb = new StringBuilder(basename);
            }
            sb.setCharAt(i, lower);
        }
        if (sb != null) {
            return sb.toString();
        }
        return basename;
    }

    public static String descFor(AnnotatedElement elem) {
        if (elem instanceof Class) {
            return "class " + ((Class) elem).getName();
        }
        if (elem instanceof Method) {
            Method m = (Method) elem;
            return "method " + m.getName() + " (from class " + m.getDeclaringClass().getName() + ")";
        } else if (!(elem instanceof Constructor)) {
            return "unknown type [" + elem.getClass() + "]";
        } else {
            return "constructor() (from class " + ((Constructor) elem).getDeclaringClass().getName() + ")";
        }
    }
}
