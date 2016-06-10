package com.cnlaunch.x431pro.module.config.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import org.xmlpull.v1.XmlPullParser;

public class ConfigInfoDao extends AbstractDao<ConfigInfo, Long> {
    public static final String TABLENAME = "CONFIG_INFO";

    public static class Properties {
        public static final Property Id;
        public static final Property Key;
        public static final Property Value;

        static {
            Id = new Property(0, Long.class, LocaleUtil.INDONESIAN, true, "_id");
            Key = new Property(1, String.class, SharedPref.KEY, false, "KEY");
            Value = new Property(2, String.class, SharedPref.VALUE, false, "VALUE");
        }
    }

    public ConfigInfoDao(DaoConfig config) {
        super(config);
    }

    public ConfigInfoDao(DaoConfig config, ConfigDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : XmlPullParser.NO_NAMESPACE) + "'CONFIG_INFO' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'KEY' TEXT NOT NULL ," + "'VALUE' TEXT NOT NULL );");
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : XmlPullParser.NO_NAMESPACE) + "'CONFIG_INFO'");
    }

    protected void bindValues(SQLiteStatement stmt, ConfigInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getKey());
        stmt.bindString(3, entity.getValue());
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public ConfigInfo readEntity(Cursor cursor, int offset) {
        return new ConfigInfo(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.getString(offset + 1), cursor.getString(offset + 2));
    }

    public void readEntity(Cursor cursor, ConfigInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setKey(cursor.getString(offset + 1));
        entity.setValue(cursor.getString(offset + 2));
    }

    protected Long updateKeyAfterInsert(ConfigInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(ConfigInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
