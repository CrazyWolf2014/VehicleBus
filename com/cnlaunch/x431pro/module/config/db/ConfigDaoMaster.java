package com.cnlaunch.x431pro.module.config.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class ConfigDaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    public static abstract class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, ConfigDaoMaster.SCHEMA_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version 1");
            ConfigDaoMaster.createAllTables(db, false);
        }
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            ConfigDaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }

    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ConfigInfoDao.createTable(db, ifNotExists);
    }

    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ConfigInfoDao.dropTable(db, ifExists);
    }

    public ConfigDaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ConfigInfoDao.class);
    }

    public ConfigDaoSession newSession() {
        return new ConfigDaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
    }

    public ConfigDaoSession newSession(IdentityScopeType type) {
        return new ConfigDaoSession(this.db, type, this.daoConfigMap);
    }
}
