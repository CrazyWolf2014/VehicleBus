package com.cnlaunch.x431pro.module.config.db;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;
import java.util.Map;

public class ConfigDaoSession extends AbstractDaoSession {
    private final ConfigInfoDao configInfoDao;
    private final DaoConfig configInfoDaoConfig;

    public ConfigDaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        this.configInfoDaoConfig = ((DaoConfig) daoConfigMap.get(ConfigInfoDao.class)).clone();
        this.configInfoDaoConfig.initIdentityScope(type);
        this.configInfoDao = new ConfigInfoDao(this.configInfoDaoConfig, this);
        registerDao(ConfigInfo.class, this.configInfoDao);
    }

    public void clear() {
        this.configInfoDaoConfig.getIdentityScope().clear();
    }

    public ConfigInfoDao getConfigInfoDao() {
        return this.configInfoDao;
    }
}
