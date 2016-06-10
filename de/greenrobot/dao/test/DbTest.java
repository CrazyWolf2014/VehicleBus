package de.greenrobot.dao.test;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import de.greenrobot.dao.DbUtils;
import java.util.Random;

public abstract class DbTest<T extends Application> extends ApplicationTestCase<T> {
    protected SQLiteDatabase db;
    protected final boolean inMemory;
    protected Random random;

    public DbTest() {
        this(true);
    }

    public DbTest(boolean inMemory) {
        this(Application.class, inMemory);
    }

    public DbTest(Class<T> appClass, boolean inMemory) {
        super(appClass);
        this.inMemory = inMemory;
        this.random = new Random();
    }

    protected void setUp() {
        try {
            super.setUp();
            createApplication();
            setUpDb();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setUpDb() {
        if (this.inMemory) {
            this.db = SQLiteDatabase.create(null);
            return;
        }
        getApplication().deleteDatabase("test-db");
        this.db = getApplication().openOrCreateDatabase("test-db", 0, null);
    }

    protected void tearDown() throws Exception {
        this.db.close();
        if (!this.inMemory) {
            getApplication().deleteDatabase("test-db");
        }
        super.tearDown();
    }

    protected void logTableDump(String tablename) {
        DbUtils.logTableDump(this.db, tablename);
    }
}
