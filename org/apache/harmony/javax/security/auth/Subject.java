package org.apache.harmony.javax.security.auth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public final class Subject implements Serializable {
    private static final AuthPermission _AS;
    private static final AuthPermission _AS_PRIVILEGED;
    private static final AuthPermission _PRINCIPALS;
    private static final AuthPermission _PRIVATE_CREDENTIALS;
    private static final AuthPermission _PUBLIC_CREDENTIALS;
    private static final AuthPermission _READ_ONLY;
    private static final AuthPermission _SUBJECT;
    private static final long serialVersionUID = -8308522755600156056L;
    private final Set<Principal> principals;
    private transient SecureSet<Object> privateCredentials;
    private transient SecureSet<Object> publicCredentials;
    private boolean readOnly;

    /* renamed from: org.apache.harmony.javax.security.auth.Subject.1 */
    static class C09221 implements PrivilegedAction {
        final /* synthetic */ SubjectDomainCombiner val$combiner;
        final /* synthetic */ AccessControlContext val$context;

        C09221(AccessControlContext accessControlContext, SubjectDomainCombiner subjectDomainCombiner) {
            this.val$context = accessControlContext;
            this.val$combiner = subjectDomainCombiner;
        }

        public Object run() {
            return new AccessControlContext(this.val$context, this.val$combiner);
        }
    }

    /* renamed from: org.apache.harmony.javax.security.auth.Subject.2 */
    static class C09232 implements PrivilegedAction<AccessControlContext> {
        final /* synthetic */ SubjectDomainCombiner val$combiner;
        final /* synthetic */ AccessControlContext val$context;

        C09232(AccessControlContext accessControlContext, SubjectDomainCombiner subjectDomainCombiner) {
            this.val$context = accessControlContext;
            this.val$combiner = subjectDomainCombiner;
        }

        public AccessControlContext run() {
            return new AccessControlContext(this.val$context, this.val$combiner);
        }
    }

    /* renamed from: org.apache.harmony.javax.security.auth.Subject.3 */
    static class C09243 implements PrivilegedAction<DomainCombiner> {
        final /* synthetic */ AccessControlContext val$context;

        C09243(AccessControlContext accessControlContext) {
            this.val$context = accessControlContext;
        }

        public DomainCombiner run() {
            return this.val$context.getDomainCombiner();
        }
    }

    private final class SecureSet<SST> extends AbstractSet<SST> implements Serializable {
        private static final int SET_Principal = 0;
        private static final int SET_PrivCred = 1;
        private static final int SET_PubCred = 2;
        private static final long serialVersionUID = 7911754171111800359L;
        private LinkedList<SST> elements;
        private transient AuthPermission permission;
        private int setType;

        /* renamed from: org.apache.harmony.javax.security.auth.Subject.SecureSet.2 */
        class C09252 extends AbstractSet<E> {
            private LinkedList<E> elements;
            final /* synthetic */ Class val$c;

            C09252(Class cls) {
                this.val$c = cls;
                this.elements = new LinkedList();
            }

            public boolean add(E e) {
                if (!this.val$c.isAssignableFrom(e.getClass())) {
                    throw new IllegalArgumentException("auth.0C " + this.val$c.getName());
                } else if (this.elements.contains(e)) {
                    return false;
                } else {
                    this.elements.add(e);
                    return true;
                }
            }

            public Iterator<E> iterator() {
                return this.elements.iterator();
            }

            public boolean retainAll(Collection<?> collection) {
                if (collection != null) {
                    return super.retainAll(collection);
                }
                throw new NullPointerException();
            }

            public int size() {
                return this.elements.size();
            }
        }

        private class SecureIterator implements Iterator<SST> {
            protected Iterator<SST> iterator;

            protected SecureIterator(Iterator<SST> it) {
                this.iterator = it;
            }

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public SST next() {
                return this.iterator.next();
            }

            public void remove() {
                Subject.this.checkState();
                Subject.checkPermission(SecureSet.this.permission);
                this.iterator.remove();
            }
        }

        /* renamed from: org.apache.harmony.javax.security.auth.Subject.SecureSet.1 */
        class C11471 extends SecureIterator {
            C11471(Iterator it) {
                super(it);
            }

            public SST next() {
                SST next = this.iterator.next();
                Subject.checkPermission(new PrivateCredentialPermission(next.getClass().getName(), Subject.this.principals));
                return next;
            }
        }

        protected SecureSet(AuthPermission authPermission) {
            this.permission = authPermission;
            this.elements = new LinkedList();
        }

        protected SecureSet(Subject subject, AuthPermission authPermission, Collection<? extends SST> collection) {
            this(authPermission);
            Object obj = collection.getClass().getClassLoader() == null ? SET_PrivCred : null;
            for (Object next : collection) {
                verifyElement(next);
                if (obj != null || !this.elements.contains(next)) {
                    this.elements.add(next);
                }
            }
        }

        private void verifyElement(Object obj) {
            if (obj == null) {
                throw new NullPointerException();
            } else if (this.permission == Subject._PRINCIPALS && !Principal.class.isAssignableFrom(obj.getClass())) {
                throw new IllegalArgumentException("auth.0B");
            }
        }

        public boolean add(SST sst) {
            verifyElement(sst);
            Subject.this.checkState();
            Subject.checkPermission(this.permission);
            if (this.elements.contains(sst)) {
                return false;
            }
            this.elements.add(sst);
            return true;
        }

        public Iterator<SST> iterator() {
            return this.permission == Subject._PRIVATE_CREDENTIALS ? new C11471(this.elements.iterator()) : new SecureIterator(this.elements.iterator());
        }

        public boolean retainAll(Collection<?> collection) {
            if (collection != null) {
                return super.retainAll(collection);
            }
            throw new NullPointerException();
        }

        public int size() {
            return this.elements.size();
        }

        protected final <E> Set<E> get(Class<E> cls) {
            if (cls == null) {
                throw new NullPointerException();
            }
            Set c09252 = new C09252(cls);
            Iterator it = iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (cls.isAssignableFrom(next.getClass())) {
                    c09252.add(cls.cast(next));
                }
            }
            return c09252;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            switch (this.setType) {
                case SET_Principal /*0*/:
                    this.permission = Subject._PRINCIPALS;
                    break;
                case SET_PrivCred /*1*/:
                    this.permission = Subject._PRIVATE_CREDENTIALS;
                    break;
                case SET_PubCred /*2*/:
                    this.permission = Subject._PUBLIC_CREDENTIALS;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            Iterator it = this.elements.iterator();
            while (it.hasNext()) {
                verifyElement(it.next());
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            if (this.permission == Subject._PRIVATE_CREDENTIALS) {
                Iterator it = iterator();
                while (it.hasNext()) {
                    it.next();
                }
                this.setType = SET_PrivCred;
            } else if (this.permission == Subject._PRINCIPALS) {
                this.setType = SET_Principal;
            } else {
                this.setType = SET_PubCred;
            }
            objectOutputStream.defaultWriteObject();
        }
    }

    static {
        _AS = new AuthPermission("doAs");
        _AS_PRIVILEGED = new AuthPermission("doAsPrivileged");
        _SUBJECT = new AuthPermission("getSubject");
        _PRINCIPALS = new AuthPermission("modifyPrincipals");
        _PRIVATE_CREDENTIALS = new AuthPermission("modifyPrivateCredentials");
        _PUBLIC_CREDENTIALS = new AuthPermission("modifyPublicCredentials");
        _READ_ONLY = new AuthPermission("setReadOnly");
    }

    public Subject() {
        this.principals = new SecureSet(_PRINCIPALS);
        this.publicCredentials = new SecureSet(_PUBLIC_CREDENTIALS);
        this.privateCredentials = new SecureSet(_PRIVATE_CREDENTIALS);
        this.readOnly = false;
    }

    public Subject(boolean z, Set<? extends Principal> set, Set<?> set2, Set<?> set3) {
        if (set == null || set2 == null || set3 == null) {
            throw new NullPointerException();
        }
        this.principals = new SecureSet(this, _PRINCIPALS, set);
        this.publicCredentials = new SecureSet(this, _PUBLIC_CREDENTIALS, set2);
        this.privateCredentials = new SecureSet(this, _PRIVATE_CREDENTIALS, set3);
        this.readOnly = z;
    }

    public static Object doAs(Subject subject, PrivilegedAction privilegedAction) {
        checkPermission(_AS);
        return doAs_PrivilegedAction(subject, privilegedAction, AccessController.getContext());
    }

    public static Object doAsPrivileged(Subject subject, PrivilegedAction privilegedAction, AccessControlContext accessControlContext) {
        checkPermission(_AS_PRIVILEGED);
        if (accessControlContext == null) {
            return doAs_PrivilegedAction(subject, privilegedAction, new AccessControlContext(new ProtectionDomain[0]));
        }
        return doAs_PrivilegedAction(subject, privilegedAction, accessControlContext);
    }

    private static Object doAs_PrivilegedAction(Subject subject, PrivilegedAction privilegedAction, AccessControlContext accessControlContext) {
        SubjectDomainCombiner subjectDomainCombiner;
        if (subject == null) {
            subjectDomainCombiner = null;
        } else {
            subjectDomainCombiner = new SubjectDomainCombiner(subject);
        }
        return AccessController.doPrivileged(privilegedAction, (AccessControlContext) AccessController.doPrivileged(new C09221(accessControlContext, subjectDomainCombiner)));
    }

    public static Object doAs(Subject subject, PrivilegedExceptionAction privilegedExceptionAction) throws PrivilegedActionException {
        checkPermission(_AS);
        return doAs_PrivilegedExceptionAction(subject, privilegedExceptionAction, AccessController.getContext());
    }

    public static Object doAsPrivileged(Subject subject, PrivilegedExceptionAction privilegedExceptionAction, AccessControlContext accessControlContext) throws PrivilegedActionException {
        checkPermission(_AS_PRIVILEGED);
        if (accessControlContext == null) {
            return doAs_PrivilegedExceptionAction(subject, privilegedExceptionAction, new AccessControlContext(new ProtectionDomain[0]));
        }
        return doAs_PrivilegedExceptionAction(subject, privilegedExceptionAction, accessControlContext);
    }

    private static Object doAs_PrivilegedExceptionAction(Subject subject, PrivilegedExceptionAction privilegedExceptionAction, AccessControlContext accessControlContext) throws PrivilegedActionException {
        SubjectDomainCombiner subjectDomainCombiner;
        if (subject == null) {
            subjectDomainCombiner = null;
        } else {
            subjectDomainCombiner = new SubjectDomainCombiner(subject);
        }
        return AccessController.doPrivileged(privilegedExceptionAction, (AccessControlContext) AccessController.doPrivileged(new C09232(accessControlContext, subjectDomainCombiner)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Subject subject = (Subject) obj;
        if (this.principals.equals(subject.principals) && this.publicCredentials.equals(subject.publicCredentials) && this.privateCredentials.equals(subject.privateCredentials)) {
            return true;
        }
        return false;
    }

    public Set<Principal> getPrincipals() {
        return this.principals;
    }

    public <T extends Principal> Set<T> getPrincipals(Class<T> cls) {
        return ((SecureSet) this.principals).get(cls);
    }

    public Set<Object> getPrivateCredentials() {
        return this.privateCredentials;
    }

    public <T> Set<T> getPrivateCredentials(Class<T> cls) {
        return this.privateCredentials.get(cls);
    }

    public Set<Object> getPublicCredentials() {
        return this.publicCredentials;
    }

    public <T> Set<T> getPublicCredentials(Class<T> cls) {
        return this.publicCredentials.get(cls);
    }

    public int hashCode() {
        return (this.principals.hashCode() + this.privateCredentials.hashCode()) + this.publicCredentials.hashCode();
    }

    public void setReadOnly() {
        checkPermission(_READ_ONLY);
        this.readOnly = true;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Subject:\n");
        for (Object append : this.principals) {
            stringBuilder.append("\tPrincipal: ");
            stringBuilder.append(append);
            stringBuilder.append('\n');
        }
        Iterator it = this.publicCredentials.iterator();
        while (it.hasNext()) {
            stringBuilder.append("\tPublic Credential: ");
            stringBuilder.append(it.next());
            stringBuilder.append('\n');
        }
        int length = stringBuilder.length() - 1;
        Iterator it2 = this.privateCredentials.iterator();
        while (it2.hasNext()) {
            try {
                stringBuilder.append("\tPrivate Credential: ");
                stringBuilder.append(it2.next());
                stringBuilder.append('\n');
            } catch (SecurityException e) {
                stringBuilder.delete(length, stringBuilder.length());
                stringBuilder.append("\tPrivate Credentials: no accessible information\n");
            }
        }
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.publicCredentials = new SecureSet(_PUBLIC_CREDENTIALS);
        this.privateCredentials = new SecureSet(_PRIVATE_CREDENTIALS);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    public static Subject getSubject(AccessControlContext accessControlContext) {
        checkPermission(_SUBJECT);
        if (accessControlContext == null) {
            throw new NullPointerException("auth.09");
        }
        DomainCombiner domainCombiner = (DomainCombiner) AccessController.doPrivileged(new C09243(accessControlContext));
        if (domainCombiner == null || !(domainCombiner instanceof SubjectDomainCombiner)) {
            return null;
        }
        return ((SubjectDomainCombiner) domainCombiner).getSubject();
    }

    private static void checkPermission(Permission permission) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(permission);
        }
    }

    private void checkState() {
        if (this.readOnly) {
            throw new IllegalStateException("auth.0A");
        }
    }
}
