package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

public class DependencyInjectionFactory {

    /* renamed from: com.thoughtworks.xstream.core.util.DependencyInjectionFactory.1 */
    static class C08941 implements Comparator {
        C08941() {
        }

        public int compare(Object o1, Object o2) {
            return ((Constructor) o2).getParameterTypes().length - ((Constructor) o1).getParameterTypes().length;
        }
    }

    private static class TypedValue {
        final Class type;
        final Object value;

        public TypedValue(Class type, Object value) {
            this.type = type;
            this.value = value;
        }

        public String toString() {
            return this.type.getName() + ":" + this.value;
        }
    }

    public static Object newInstance(Class type, Object[] dependencies) {
        return newInstance(type, dependencies, null);
    }

    public static Object newInstance(Class type, Object[] dependencies, BitSet usedDependencies) {
        int i;
        Object instance;
        if (dependencies != null) {
            int length = dependencies.length;
            if (r0 > 63) {
                throw new IllegalArgumentException("More than 63 arguments are not supported");
            }
        }
        Constructor bestMatchingCtor = null;
        ArrayList matchingDependencies = new ArrayList();
        List possibleMatchingDependencies = null;
        long usedDeps = 0;
        long possibleUsedDeps = 0;
        if (dependencies != null && dependencies.length > 0) {
            Constructor[] ctors = type.getConstructors();
            length = ctors.length;
            if (r0 > 1) {
                Arrays.sort(ctors, new C08941());
            }
            Object typedDependencies = new TypedValue[dependencies.length];
            i = 0;
            while (true) {
                length = dependencies.length;
                if (i >= r0) {
                    break;
                }
                Object dependency = dependencies[i];
                Class depType = dependency.getClass();
                if (depType.isPrimitive()) {
                    depType = Primitives.box(depType);
                } else if (depType == TypedNull.class) {
                    depType = ((TypedNull) dependency).getType();
                    dependency = null;
                }
                typedDependencies[i] = new TypedValue(depType, dependency);
                i++;
            }
            Constructor possibleCtor = null;
            int arity = Integer.MAX_VALUE;
            i = 0;
            while (bestMatchingCtor == null) {
                length = ctors.length;
                if (i >= r0) {
                    break;
                }
                Constructor constructor = ctors[i];
                Class[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length <= dependencies.length) {
                    if (parameterTypes.length != 0) {
                        length = parameterTypes.length;
                        if (arity > r0) {
                            if (possibleCtor == null) {
                                arity = parameterTypes.length;
                            }
                        }
                        int j = 0;
                        while (true) {
                            length = parameterTypes.length;
                            if (j >= r0) {
                                break;
                            }
                            if (parameterTypes[j].isPrimitive()) {
                                parameterTypes[j] = Primitives.box(parameterTypes[j]);
                            }
                            j++;
                        }
                        matchingDependencies.clear();
                        usedDeps = 0;
                        j = 0;
                        int k = 0;
                        while (true) {
                            length = parameterTypes.length;
                            if (j >= r0) {
                                break;
                            }
                            if ((parameterTypes.length + k) - j > typedDependencies.length) {
                                break;
                            }
                            if (parameterTypes[j].isAssignableFrom(typedDependencies[k].type)) {
                                matchingDependencies.add(typedDependencies[k].value);
                                usedDeps |= 1 << k;
                                j++;
                                length = parameterTypes.length;
                                if (j == r0) {
                                    break;
                                }
                            }
                            k++;
                        }
                        bestMatchingCtor = constructor;
                        if (bestMatchingCtor == null) {
                            boolean possible = true;
                            TypedValue[] deps = new TypedValue[typedDependencies.length];
                            System.arraycopy(typedDependencies, 0, deps, 0, deps.length);
                            matchingDependencies.clear();
                            usedDeps = 0;
                            j = 0;
                            while (true) {
                                length = parameterTypes.length;
                                if (j >= r0) {
                                    break;
                                }
                                int assignable = -1;
                                for (k = 0; k < deps.length; k++) {
                                    if (deps[k] != null) {
                                        if (deps[k].type == parameterTypes[j]) {
                                            assignable = k;
                                            break;
                                        }
                                        if (parameterTypes[j].isAssignableFrom(deps[k].type)) {
                                            if (assignable >= 0) {
                                                if (deps[assignable].type != deps[k].type) {
                                                    if (!deps[assignable].type.isAssignableFrom(deps[k].type)) {
                                                    }
                                                }
                                            }
                                            assignable = k;
                                        }
                                    }
                                }
                                if (assignable < 0) {
                                    break;
                                }
                                matchingDependencies.add(deps[assignable].value);
                                usedDeps |= 1 << assignable;
                                deps[assignable] = null;
                                j++;
                            }
                            possible = false;
                            if (possible && (possibleCtor == null || usedDeps < possibleUsedDeps)) {
                                possibleCtor = constructor;
                                possibleMatchingDependencies = (List) matchingDependencies.clone();
                                possibleUsedDeps = usedDeps;
                            }
                        }
                    } else if (possibleCtor == null) {
                        bestMatchingCtor = constructor;
                    }
                }
                i++;
            }
            if (bestMatchingCtor == null) {
                if (possibleCtor == null) {
                    throw new ObjectAccessException("Cannot construct " + type.getName() + ", none of the dependencies match any constructor's parameters");
                }
                bestMatchingCtor = possibleCtor;
                matchingDependencies.clear();
                matchingDependencies.addAll(possibleMatchingDependencies);
                usedDeps = possibleUsedDeps;
            }
        }
        if (bestMatchingCtor == null) {
            try {
                instance = type.newInstance();
            } catch (InstantiationException e) {
                throw new ObjectAccessException("Cannot construct " + type.getName(), e);
            } catch (IllegalAccessException e2) {
                throw new ObjectAccessException("Cannot construct " + type.getName(), e2);
            } catch (InvocationTargetException e3) {
                throw new ObjectAccessException("Cannot construct " + type.getName(), e3);
            } catch (SecurityException e4) {
                throw new ObjectAccessException("Cannot construct " + type.getName(), e4);
            } catch (ExceptionInInitializerError e5) {
                throw new ObjectAccessException("Cannot construct " + type.getName(), e5);
            }
        }
        instance = bestMatchingCtor.newInstance(matchingDependencies.toArray());
        if (usedDependencies != null) {
            usedDependencies.clear();
            i = 0;
            long l = 1;
            while (l < usedDeps) {
                if ((usedDeps & l) > 0) {
                    usedDependencies.set(i);
                }
                l <<= 1;
                i++;
            }
        }
        return instance;
    }
}
