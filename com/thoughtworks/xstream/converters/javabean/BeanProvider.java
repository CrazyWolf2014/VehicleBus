package com.thoughtworks.xstream.converters.javabean;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BeanProvider implements JavaBeanProvider {
    protected static final Object[] NO_PARAMS;
    protected PropertyDictionary propertyDictionary;

    public interface Visitor extends com.thoughtworks.xstream.converters.javabean.JavaBeanProvider.Visitor {
    }

    static {
        NO_PARAMS = new Object[0];
    }

    public BeanProvider() {
        this(new PropertyDictionary(new NativePropertySorter()));
    }

    public BeanProvider(Comparator propertyNameComparator) {
        this(new PropertyDictionary(new ComparingPropertySorter(propertyNameComparator)));
    }

    public BeanProvider(PropertyDictionary propertyDictionary) {
        this.propertyDictionary = propertyDictionary;
    }

    public Object newInstance(Class type) {
        try {
            return getDefaultConstrutor(type).newInstance(NO_PARAMS);
        } catch (InstantiationException e) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e2);
        } catch (InvocationTargetException e3) {
            if (e3.getTargetException() instanceof RuntimeException) {
                throw ((RuntimeException) e3.getTargetException());
            } else if (e3.getTargetException() instanceof Error) {
                throw ((Error) e3.getTargetException());
            } else {
                throw new ObjectAccessException("Constructor for " + type.getName() + " threw an exception", e3);
            }
        }
    }

    public void visitSerializableProperties(Object object, com.thoughtworks.xstream.converters.javabean.JavaBeanProvider.Visitor visitor) {
        PropertyDescriptor[] propertyDescriptors = getSerializableProperties(object);
        int i = 0;
        while (i < propertyDescriptors.length) {
            PropertyDescriptor property = propertyDescriptors[i];
            try {
                Method readMethod = property.getReadMethod();
                String name = property.getName();
                Class definedIn = readMethod.getDeclaringClass();
                if (visitor.shouldVisit(name, definedIn)) {
                    visitor.visit(name, property.getPropertyType(), definedIn, readMethod.invoke(object, new Object[0]));
                }
                i++;
            } catch (IllegalArgumentException e) {
                throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), e);
            } catch (IllegalAccessException e2) {
                throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), e2);
            } catch (InvocationTargetException e3) {
                throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), e3);
            }
        }
    }

    public void writeProperty(Object object, String propertyName, Object value) {
        PropertyDescriptor property = getProperty(propertyName, object.getClass());
        try {
            property.getWriteMethod().invoke(object, new Object[]{value});
        } catch (IllegalArgumentException e) {
            throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), e2);
        } catch (InvocationTargetException e3) {
            throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), e3);
        }
    }

    public Class getPropertyType(Object object, String name) {
        return getProperty(name, object.getClass()).getPropertyType();
    }

    public boolean propertyDefinedInClass(String name, Class type) {
        return getProperty(name, type) != null;
    }

    public boolean canInstantiate(Class type) {
        return getDefaultConstrutor(type) != null;
    }

    protected Constructor getDefaultConstrutor(Class type) {
        Constructor[] constructors = type.getConstructors();
        for (Constructor c : constructors) {
            if (c.getParameterTypes().length == 0 && Modifier.isPublic(c.getModifiers())) {
                return c;
            }
        }
        return null;
    }

    protected PropertyDescriptor[] getSerializableProperties(Object object) {
        List result = new ArrayList();
        Iterator iter = this.propertyDictionary.propertiesFor(object.getClass());
        while (iter.hasNext()) {
            PropertyDescriptor descriptor = (PropertyDescriptor) iter.next();
            if (canStreamProperty(descriptor)) {
                result.add(descriptor);
            }
        }
        return (PropertyDescriptor[]) result.toArray(new PropertyDescriptor[result.size()]);
    }

    protected boolean canStreamProperty(PropertyDescriptor descriptor) {
        return (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) ? false : true;
    }

    public boolean propertyWriteable(String name, Class type) {
        return getProperty(name, type).getWriteMethod() != null;
    }

    protected PropertyDescriptor getProperty(String name, Class type) {
        return this.propertyDictionary.propertyDescriptor(type, name);
    }
}
