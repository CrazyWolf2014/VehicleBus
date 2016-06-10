package com.ifoer.webservice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.ksoap2.serialization.SoapObject;

public class SoapObjectUtil {
    public static <T> T soapToPojo(Class clazz, SoapObject soapObject) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Field[] fields = clazz.getDeclaredFields();
        Object obj = clazz.newInstance();
        for (Field f : fields) {
            String method = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
            if (hasMethod(method, clazz.getMethods())) {
                Vector<SoapObject> object = soapObject.getPropertySafely(f.getName());
                if (object instanceof List) {
                    Vector<SoapObject> soapObjects = object;
                    List objectList = new ArrayList();
                    Iterator it = soapObjects.iterator();
                    while (it.hasNext()) {
                        SoapObject soapObject2 = (SoapObject) it.next();
                        try {
                            objectList.add(soapToPojo(Class.forName(soapObject2.getName()), soapObject2));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    clazz.getMethod(method, new Class[]{f.getType()}).invoke(obj, new Object[]{objectList});
                } else {
                    try {
                        Method method2 = clazz.getMethod(method, new Class[]{f.getType()});
                        Object[] objArr = new Object[1];
                        objArr[0] = soapObject.getPropertySafely(f.getName());
                        method2.invoke(obj, objArr);
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
        return obj;
    }

    private static boolean hasMethod(String methodName, Method[] method) {
        for (Method m : method) {
            if (methodName.equals(m.getName())) {
                return true;
            }
        }
        return false;
    }
}
