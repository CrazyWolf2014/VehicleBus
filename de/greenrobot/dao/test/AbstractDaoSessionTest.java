package de.greenrobot.dao.test;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;

public abstract class AbstractDaoSessionTest<A extends Application, T extends AbstractDaoMaster, S extends AbstractDaoSession> extends DbTest<A> {
    protected T daoMaster;
    private final Class<T> daoMasterClass;
    protected S daoSession;

    public AbstractDaoSessionTest(Class<T> daoMasterClass) {
        this(daoMasterClass, true);
    }

    public AbstractDaoSessionTest(Class<T> daoMasterClass, boolean inMemory) {
        super(inMemory);
        this.daoMasterClass = daoMasterClass;
    }

    public AbstractDaoSessionTest(Class<A> appClass, Class<T> daoMasterClass, boolean inMemory) {
        super(appClass, inMemory);
        this.daoMasterClass = daoMasterClass;
    }

    protected void setUp() {
        super.setUp();
        try {
            this.daoMaster = (AbstractDaoMaster) this.daoMasterClass.getConstructor(new Class[]{SQLiteDatabase.class}).newInstance(new Object[]{this.db});
            this.daoMasterClass.getMethod("createAllTables", new Class[]{SQLiteDatabase.class, Boolean.TYPE}).invoke(null, new Object[]{this.db, Boolean.valueOf(false)});
            this.daoSession = this.daoMaster.newSession();
        } catch (Exception e) {
            throw new RuntimeException("Could not prepare DAO session test", e);
        }
    }
}
