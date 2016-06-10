package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public final class Lookup {
    public static final int HOST_NOT_FOUND = 3;
    public static final int SUCCESSFUL = 0;
    public static final int TRY_AGAIN = 2;
    public static final int TYPE_NOT_FOUND = 4;
    public static final int UNRECOVERABLE = 1;
    private static Map defaultCaches;
    private static int defaultNdots;
    private static Resolver defaultResolver;
    private static Name[] defaultSearchPath;
    private static final Name[] noAliases;
    private List aliases;
    private Record[] answers;
    private boolean badresponse;
    private String badresponse_error;
    private Cache cache;
    private int credibility;
    private int dclass;
    private boolean done;
    private boolean doneCurrent;
    private String error;
    private boolean foundAlias;
    private int iterations;
    private Name name;
    private boolean nametoolong;
    private boolean networkerror;
    private boolean nxdomain;
    private boolean referral;
    private Resolver resolver;
    private int result;
    private Name[] searchPath;
    private boolean temporary_cache;
    private boolean timedout;
    private int type;
    private boolean verbose;

    static {
        noAliases = new Name[SUCCESSFUL];
        refreshDefault();
    }

    public static synchronized void refreshDefault() {
        synchronized (Lookup.class) {
            try {
                defaultResolver = new ExtendedResolver();
                defaultSearchPath = ResolverConfig.getCurrentConfig().searchPath();
                defaultCaches = new HashMap();
                defaultNdots = ResolverConfig.getCurrentConfig().ndots();
            } catch (UnknownHostException e) {
                throw new RuntimeException("Failed to initialize resolver");
            }
        }
    }

    public static synchronized Resolver getDefaultResolver() {
        Resolver resolver;
        synchronized (Lookup.class) {
            resolver = defaultResolver;
        }
        return resolver;
    }

    public static synchronized void setDefaultResolver(Resolver resolver) {
        synchronized (Lookup.class) {
            defaultResolver = resolver;
        }
    }

    public static synchronized Cache getDefaultCache(int i) {
        Cache cache;
        synchronized (Lookup.class) {
            DClass.check(i);
            cache = (Cache) defaultCaches.get(Mnemonic.toInteger(i));
            if (cache == null) {
                cache = new Cache(i);
                defaultCaches.put(Mnemonic.toInteger(i), cache);
            }
        }
        return cache;
    }

    public static synchronized void setDefaultCache(Cache cache, int i) {
        synchronized (Lookup.class) {
            DClass.check(i);
            defaultCaches.put(Mnemonic.toInteger(i), cache);
        }
    }

    public static synchronized Name[] getDefaultSearchPath() {
        Name[] nameArr;
        synchronized (Lookup.class) {
            nameArr = defaultSearchPath;
        }
        return nameArr;
    }

    public static synchronized void setDefaultSearchPath(Name[] nameArr) {
        synchronized (Lookup.class) {
            defaultSearchPath = nameArr;
        }
    }

    public static synchronized void setDefaultSearchPath(String[] strArr) throws TextParseException {
        synchronized (Lookup.class) {
            if (strArr == null) {
                defaultSearchPath = null;
            } else {
                Name[] nameArr = new Name[strArr.length];
                for (int i = SUCCESSFUL; i < strArr.length; i += UNRECOVERABLE) {
                    nameArr[i] = Name.fromString(strArr[i], Name.root);
                }
                defaultSearchPath = nameArr;
            }
        }
    }

    private final void reset() {
        this.iterations = SUCCESSFUL;
        this.foundAlias = false;
        this.done = false;
        this.doneCurrent = false;
        this.aliases = null;
        this.answers = null;
        this.result = -1;
        this.error = null;
        this.nxdomain = false;
        this.badresponse = false;
        this.badresponse_error = null;
        this.networkerror = false;
        this.timedout = false;
        this.nametoolong = false;
        this.referral = false;
        if (this.temporary_cache) {
            this.cache.clearCache();
        }
    }

    public Lookup(Name name, int i, int i2) {
        Type.check(i);
        DClass.check(i2);
        if (Type.isRR(i) || i == KEYRecord.PROTOCOL_ANY) {
            this.name = name;
            this.type = i;
            this.dclass = i2;
            synchronized (Lookup.class) {
                this.resolver = getDefaultResolver();
                this.searchPath = getDefaultSearchPath();
                this.cache = getDefaultCache(i2);
            }
            this.credibility = HOST_NOT_FOUND;
            this.verbose = Options.check("verbose");
            this.result = -1;
            return;
        }
        throw new IllegalArgumentException("Cannot query for meta-types other than ANY");
    }

    public Lookup(Name name, int i) {
        this(name, i, (int) UNRECOVERABLE);
    }

    public Lookup(Name name) {
        this(name, (int) UNRECOVERABLE, (int) UNRECOVERABLE);
    }

    public Lookup(String str, int i, int i2) throws TextParseException {
        this(Name.fromString(str), i, i2);
    }

    public Lookup(String str, int i) throws TextParseException {
        this(Name.fromString(str), i, (int) UNRECOVERABLE);
    }

    public Lookup(String str) throws TextParseException {
        this(Name.fromString(str), (int) UNRECOVERABLE, (int) UNRECOVERABLE);
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public void setSearchPath(Name[] nameArr) {
        this.searchPath = nameArr;
    }

    public void setSearchPath(String[] strArr) throws TextParseException {
        if (strArr == null) {
            this.searchPath = null;
            return;
        }
        Name[] nameArr = new Name[strArr.length];
        for (int i = SUCCESSFUL; i < strArr.length; i += UNRECOVERABLE) {
            nameArr[i] = Name.fromString(strArr[i], Name.root);
        }
        this.searchPath = nameArr;
    }

    public void setCache(Cache cache) {
        if (cache == null) {
            this.cache = new Cache(this.dclass);
            this.temporary_cache = true;
            return;
        }
        this.cache = cache;
        this.temporary_cache = false;
    }

    public void setNdots(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Illegal ndots value: " + i);
        }
        defaultNdots = i;
    }

    public void setCredibility(int i) {
        this.credibility = i;
    }

    private void follow(Name name, Name name2) {
        this.foundAlias = true;
        this.badresponse = false;
        this.networkerror = false;
        this.timedout = false;
        this.nxdomain = false;
        this.referral = false;
        this.iterations += UNRECOVERABLE;
        if (this.iterations >= 6 || name.equals(name2)) {
            this.result = UNRECOVERABLE;
            this.error = "CNAME loop";
            this.done = true;
            return;
        }
        if (this.aliases == null) {
            this.aliases = new ArrayList();
        }
        this.aliases.add(name2);
        lookup(name);
    }

    private void processResponse(Name name, SetResponse setResponse) {
        if (setResponse.isSuccessful()) {
            RRset[] answers = setResponse.answers();
            List arrayList = new ArrayList();
            for (int i = SUCCESSFUL; i < answers.length; i += UNRECOVERABLE) {
                Iterator rrs = answers[i].rrs();
                while (rrs.hasNext()) {
                    arrayList.add(rrs.next());
                }
            }
            this.result = SUCCESSFUL;
            this.answers = (Record[]) arrayList.toArray(new Record[arrayList.size()]);
            this.done = true;
        } else if (setResponse.isNXDOMAIN()) {
            this.nxdomain = true;
            this.doneCurrent = true;
            if (this.iterations > 0) {
                this.result = HOST_NOT_FOUND;
                this.done = true;
            }
        } else if (setResponse.isNXRRSET()) {
            this.result = TYPE_NOT_FOUND;
            this.answers = null;
            this.done = true;
        } else if (setResponse.isCNAME()) {
            follow(setResponse.getCNAME().getTarget(), name);
        } else if (setResponse.isDNAME()) {
            try {
                follow(name.fromDNAME(setResponse.getDNAME()), name);
            } catch (NameTooLongException e) {
                this.result = UNRECOVERABLE;
                this.error = "Invalid DNAME target";
                this.done = true;
            }
        } else if (setResponse.isDelegation()) {
            this.referral = true;
        }
    }

    private void lookup(Name name) {
        SetResponse lookupRecords = this.cache.lookupRecords(name, this.type, this.credibility);
        if (this.verbose) {
            System.err.println("lookup " + name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + Type.string(this.type));
            System.err.println(lookupRecords);
        }
        processResponse(name, lookupRecords);
        if (!this.done && !this.doneCurrent) {
            Message newQuery = Message.newQuery(Record.newRecord(name, this.type, this.dclass));
            try {
                Message send = this.resolver.send(newQuery);
                int rcode = send.getHeader().getRcode();
                if (rcode != 0 && rcode != HOST_NOT_FOUND) {
                    this.badresponse = true;
                    this.badresponse_error = Rcode.string(rcode);
                } else if (newQuery.getQuestion().equals(send.getQuestion())) {
                    lookupRecords = this.cache.addMessage(send);
                    if (lookupRecords == null) {
                        lookupRecords = this.cache.lookupRecords(name, this.type, this.credibility);
                    }
                    if (this.verbose) {
                        System.err.println("queried " + name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + Type.string(this.type));
                        System.err.println(lookupRecords);
                    }
                    processResponse(name, lookupRecords);
                } else {
                    this.badresponse = true;
                    this.badresponse_error = "response does not match query";
                }
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    this.timedout = true;
                } else {
                    this.networkerror = true;
                }
            }
        }
    }

    private void resolve(Name name, Name name2) {
        this.doneCurrent = false;
        if (name2 != null) {
            try {
                name = Name.concatenate(name, name2);
            } catch (NameTooLongException e) {
                this.nametoolong = true;
                return;
            }
        }
        lookup(name);
    }

    public Record[] run() {
        if (this.done) {
            reset();
        }
        if (!this.name.isAbsolute()) {
            if (this.searchPath != null) {
                if (this.name.labels() > defaultNdots) {
                    resolve(this.name, Name.root);
                }
                if (!this.done) {
                    int i = SUCCESSFUL;
                    while (i < this.searchPath.length) {
                        resolve(this.name, this.searchPath[i]);
                        if (!this.done) {
                            if (this.foundAlias) {
                                break;
                            }
                            i += UNRECOVERABLE;
                        } else {
                            return this.answers;
                        }
                    }
                }
                return this.answers;
            }
            resolve(this.name, Name.root);
        } else {
            resolve(this.name, null);
        }
        if (!this.done) {
            if (this.badresponse) {
                this.result = TRY_AGAIN;
                this.error = this.badresponse_error;
                this.done = true;
            } else if (this.timedout) {
                this.result = TRY_AGAIN;
                this.error = "timed out";
                this.done = true;
            } else if (this.networkerror) {
                this.result = TRY_AGAIN;
                this.error = "network error";
                this.done = true;
            } else if (this.nxdomain) {
                this.result = HOST_NOT_FOUND;
                this.done = true;
            } else if (this.referral) {
                this.result = UNRECOVERABLE;
                this.error = "referral";
                this.done = true;
            } else if (this.nametoolong) {
                this.result = UNRECOVERABLE;
                this.error = "name too long";
                this.done = true;
            }
        }
        return this.answers;
    }

    private void checkDone() {
        if (!this.done || this.result == -1) {
            StringBuffer stringBuffer = new StringBuffer("Lookup of " + this.name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            if (this.dclass != UNRECOVERABLE) {
                stringBuffer.append(DClass.string(this.dclass) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
            stringBuffer.append(Type.string(this.type) + " isn't done");
            throw new IllegalStateException(stringBuffer.toString());
        }
    }

    public Record[] getAnswers() {
        checkDone();
        return this.answers;
    }

    public Name[] getAliases() {
        checkDone();
        if (this.aliases == null) {
            return noAliases;
        }
        return (Name[]) this.aliases.toArray(new Name[this.aliases.size()]);
    }

    public int getResult() {
        checkDone();
        return this.result;
    }

    public String getErrorString() {
        checkDone();
        if (this.error != null) {
            return this.error;
        }
        switch (this.result) {
            case SUCCESSFUL /*0*/:
                return "successful";
            case UNRECOVERABLE /*1*/:
                return "unrecoverable error";
            case TRY_AGAIN /*2*/:
                return "try again";
            case HOST_NOT_FOUND /*3*/:
                return "host not found";
            case TYPE_NOT_FOUND /*4*/:
                return "type not found";
            default:
                throw new IllegalStateException("unknown result");
        }
    }
}
