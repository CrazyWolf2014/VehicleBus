package com.hp.hpl.sparta;

import java.util.Hashtable;

public class Sparta {
    private static CacheFactory cacheFactory_;
    private static Internment internment_;

    public interface Cache {
        Object get(Object obj);

        Object put(Object obj, Object obj2);

        int size();
    }

    public interface CacheFactory {
        Cache create();
    }

    public interface Internment {
        String intern(String str);
    }

    /* renamed from: com.hp.hpl.sparta.Sparta.1 */
    class C10641 implements Internment {
        private final Hashtable strings_;

        C10641() {
            this.strings_ = new Hashtable();
        }

        public String intern(String str) {
            String str2 = (String) this.strings_.get(str);
            if (str2 != null) {
                return str2;
            }
            this.strings_.put(str, str);
            return str;
        }
    }

    /* renamed from: com.hp.hpl.sparta.Sparta.2 */
    class C10652 implements CacheFactory {
        C10652() {
        }

        public Cache create() {
            return new HashtableCache(null);
        }
    }

    private static class HashtableCache extends Hashtable implements Cache {
        private HashtableCache() {
        }

        HashtableCache(C10641 c10641) {
            this();
        }
    }

    static {
        internment_ = new C10641();
        cacheFactory_ = new C10652();
    }

    public static String intern(String str) {
        return internment_.intern(str);
    }

    static Cache newCache() {
        return cacheFactory_.create();
    }

    public static void setCacheFactory(CacheFactory cacheFactory) {
        cacheFactory_ = cacheFactory;
    }

    public static void setInternment(Internment internment) {
        internment_ = internment;
    }
}
