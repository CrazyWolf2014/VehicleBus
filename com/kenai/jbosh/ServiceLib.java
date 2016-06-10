package com.kenai.jbosh;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ServiceLib {
    private static final Logger LOG;

    static {
        LOG = Logger.getLogger(ServiceLib.class.getName());
    }

    private ServiceLib() {
    }

    static <T> T loadService(Class<T> cls) {
        for (String attemptLoad : loadServicesImplementations(cls)) {
            T attemptLoad2 = attemptLoad(cls, attemptLoad);
            if (attemptLoad2 != null) {
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.finest("Selected " + cls.getSimpleName() + " implementation: " + attemptLoad2.getClass().getName());
                }
                return attemptLoad2;
            }
        }
        throw new IllegalStateException("Could not load " + cls.getName() + " implementation");
    }

    private static List<String> loadServicesImplementations(Class cls) {
        Closeable inputStreamReader;
        Closeable bufferedReader;
        Throwable e;
        Closeable closeable = null;
        List<String> arrayList = new ArrayList();
        String property = System.getProperty(cls.getName());
        if (property != null) {
            arrayList.add(property);
        }
        URL resource = ServiceLib.class.getClassLoader().getResource("META-INF/services/" + cls.getName());
        try {
            Closeable openStream = resource.openStream();
            try {
                inputStreamReader = new InputStreamReader(openStream);
                try {
                    bufferedReader = new BufferedReader(inputStreamReader);
                    while (true) {
                        try {
                            property = bufferedReader.readLine();
                            if (property == null) {
                                break;
                            } else if (!property.matches("\\s*(#.*)?")) {
                                arrayList.add(property.trim());
                            }
                        } catch (IOException e2) {
                            e = e2;
                            closeable = inputStreamReader;
                            inputStreamReader = openStream;
                        } catch (Throwable th) {
                            e = th;
                            closeable = inputStreamReader;
                            inputStreamReader = openStream;
                        }
                    }
                    finalClose(bufferedReader);
                    finalClose(inputStreamReader);
                    finalClose(openStream);
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = null;
                    closeable = inputStreamReader;
                    inputStreamReader = openStream;
                    try {
                        LOG.log(Level.WARNING, "Could not load services descriptor: " + resource.toString(), e);
                        finalClose(bufferedReader);
                        finalClose(closeable);
                        finalClose(inputStreamReader);
                        return arrayList;
                    } catch (Throwable th2) {
                        e = th2;
                        finalClose(bufferedReader);
                        finalClose(closeable);
                        finalClose(inputStreamReader);
                        throw e;
                    }
                } catch (Throwable th3) {
                    e = th3;
                    bufferedReader = null;
                    closeable = inputStreamReader;
                    inputStreamReader = openStream;
                    finalClose(bufferedReader);
                    finalClose(closeable);
                    finalClose(inputStreamReader);
                    throw e;
                }
            } catch (IOException e4) {
                e = e4;
                bufferedReader = null;
                inputStreamReader = openStream;
                LOG.log(Level.WARNING, "Could not load services descriptor: " + resource.toString(), e);
                finalClose(bufferedReader);
                finalClose(closeable);
                finalClose(inputStreamReader);
                return arrayList;
            } catch (Throwable th4) {
                e = th4;
                bufferedReader = null;
                inputStreamReader = openStream;
                finalClose(bufferedReader);
                finalClose(closeable);
                finalClose(inputStreamReader);
                throw e;
            }
        } catch (IOException e5) {
            e = e5;
            bufferedReader = null;
            inputStreamReader = null;
            LOG.log(Level.WARNING, "Could not load services descriptor: " + resource.toString(), e);
            finalClose(bufferedReader);
            finalClose(closeable);
            finalClose(inputStreamReader);
            return arrayList;
        } catch (Throwable th5) {
            e = th5;
            bufferedReader = null;
            inputStreamReader = null;
            finalClose(bufferedReader);
            finalClose(closeable);
            finalClose(inputStreamReader);
            throw e;
        }
        return arrayList;
    }

    private static <T> T attemptLoad(Class<T> cls, String str) {
        Throwable e;
        Level level;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Attempting service load: " + str);
        }
        try {
            Class cls2 = Class.forName(str);
            if (cls.isAssignableFrom(cls2)) {
                return cls.cast(cls2.newInstance());
            }
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(cls2.getName() + " is not assignable to " + cls.getName());
            }
            return null;
        } catch (ClassNotFoundException e2) {
            e = e2;
            level = Level.FINEST;
            LOG.log(level, "Could not load " + cls.getSimpleName() + " instance: " + str, e);
            return null;
        } catch (InstantiationException e3) {
            e = e3;
            level = Level.WARNING;
            LOG.log(level, "Could not load " + cls.getSimpleName() + " instance: " + str, e);
            return null;
        } catch (IllegalAccessException e4) {
            e = e4;
            level = Level.WARNING;
            LOG.log(level, "Could not load " + cls.getSimpleName() + " instance: " + str, e);
            return null;
        }
    }

    private static void finalClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                LOG.log(Level.FINEST, "Could not close: " + closeable, e);
            }
        }
    }
}
