package com.cnlaunch.x431pro.module.config;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.golo.InterfaceUrl;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.config.action.ConfigAction;
import com.cnlaunch.x431pro.module.config.db.ConfigDaoMaster;
import com.cnlaunch.x431pro.module.config.db.ConfigDaoMaster.DevOpenHelper;
import com.cnlaunch.x431pro.module.config.db.ConfigDaoSession;
import com.cnlaunch.x431pro.module.config.db.ConfigInfo;
import com.cnlaunch.x431pro.module.config.db.ConfigInfoDao;
import com.cnlaunch.x431pro.module.config.db.ConfigInfoDao.Properties;
import com.cnlaunch.x431pro.module.config.model.ConfigUrl;
import com.cnlaunch.x431pro.module.config.model.GetIpAreaResponse;
import com.cnlaunch.x431pro.utils.Tools;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.chart.TimeChart;
import org.xmlpull.v1.XmlPullParser;

public class ConfigDBManager {
    private static ConfigDBManager instance;
    private String DB_NAME;
    private ConfigDaoMaster configDaoMaster;
    private ConfigInfoDao configInfoDao;
    private ConfigDaoSession configSession;
    private Context mContext;
    private final String tag;
    private String timeKey;

    /* renamed from: com.cnlaunch.x431pro.module.config.ConfigDBManager.1 */
    class C01371 implements Runnable {
        private final /* synthetic */ List val$list;

        C01371(List list) {
            this.val$list = list;
        }

        public void run() {
            NLog.m916d(ConfigDBManager.this.tag, "saveConfig size: " + this.val$list.size());
            for (ConfigUrl bean : this.val$list) {
                QueryBuilder<ConfigInfo> qb = ConfigDBManager.this.configInfoDao.queryBuilder();
                qb.where(Properties.Key.eq(bean.getKey()), new WhereCondition[0]);
                List<ConfigInfo> listOld = qb.list();
                if (listOld.isEmpty()) {
                    ConfigInfo info = new ConfigInfo();
                    info.setKey(bean.getKey());
                    info.setValue(bean.getValue());
                    ConfigDBManager.this.configInfoDao.insert(info);
                } else {
                    NLog.m916d(ConfigDBManager.this.tag, "ConfigInfo size: " + listOld.size() + " Key: " + bean.getKey());
                    ConfigInfo infoOld = (ConfigInfo) listOld.get(0);
                    infoOld.setKey(bean.getKey());
                    infoOld.setValue(bean.getValue());
                    ConfigDBManager.this.configInfoDao.update(infoOld);
                }
            }
            NLog.m916d(ConfigDBManager.this.tag, "saveConfig OK!");
        }
    }

    public static ConfigDBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ConfigDBManager.class) {
                if (instance == null) {
                    instance = new ConfigDBManager(context);
                }
            }
        }
        return instance;
    }

    private ConfigDBManager(Context context) {
        this.tag = ConfigDBManager.class.getSimpleName();
        this.timeKey = "configCacheTime";
        this.DB_NAME = "config_db";
        this.mContext = context;
        this.configDaoMaster = new ConfigDaoMaster(new DevOpenHelper(context, this.DB_NAME, null).getWritableDatabase());
        this.configSession = this.configDaoMaster.newSession();
        this.configInfoDao = this.configSession.getConfigInfoDao();
    }

    public ConfigInfoDao getConfigInfoDao() {
        return this.configInfoDao;
    }

    public void setConfigInfoDao(ConfigInfoDao configInfoDao) {
        this.configInfoDao = configInfoDao;
    }

    public ConfigDaoSession getConfigSession() {
        return this.configSession;
    }

    public void setConfigSession(ConfigDaoSession configSession) {
        this.configSession = configSession;
    }

    public boolean isUpdateConfig() {
        long timeout = MySharedPreferences.getLong(this.mContext, this.timeKey, System.currentTimeMillis());
        long now = System.currentTimeMillis();
        if (this.configInfoDao.count() <= 0 || now - timeout >= TimeChart.DAY) {
            return true;
        }
        return false;
    }

    public String getUrlByKey(String key) throws HttpException {
        if (getInstance(this.mContext).isUpdateConfig()) {
            UpdateConfig();
        }
        if (TextUtils.isEmpty(key)) {
            throw new HttpException("ConfigDBManager getUrlByKey key is not null.");
        }
        String url = XmlPullParser.NO_NAMESPACE;
        QueryBuilder<ConfigInfo> qb = this.configInfoDao.queryBuilder();
        qb.where(Properties.Key.eq(key), new WhereCondition[0]);
        List<ConfigInfo> list = qb.list();
        if (list.isEmpty()) {
            return url;
        }
        return ((ConfigInfo) list.get(0)).getValue();
    }

    public boolean saveConfig(List<ConfigUrl> list) {
        if (list != null) {
            try {
                if (!list.isEmpty()) {
                    this.configSession.runInTx(new C01371(list));
                    MySharedPreferences.setLong(this.mContext, this.timeKey, System.currentTimeMillis());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        NLog.m917e(this.tag, "put the config fail, list is null.");
        return false;
    }

    public void UpdateConfig() throws HttpException {
        GetIpAreaResponse res;
        ConfigAction action = new ConfigAction(this.mContext);
        String configurl = Constants.CONFIG_URL_US;
        String configlan = Contact.RELATION_BACKNAME;
        NLog.m916d("Config", new StringBuilder(Constants.ISRELEASE_KEY).append(MySharedPreferences.getBooleanValue(this.mContext, Constants.ISRELEASE_KEY, true)).toString());
        if (MySharedPreferences.getBooleanValue(this.mContext, Constants.ISRELEASE_KEY, true)) {
            String ip = action.getIp();
            if (Tools.checkIpSuccess(ip)) {
                res = action.getIpArea(ip);
                if (res == null || res.getCode() != 0) {
                    configurl = Constants.CONFIG_URL_US;
                    configlan = Contact.RELATION_FRIEND;
                    NLog.m916d("Config", "\u6ca1\u6709\u5f97\u5230\u76f8\u5e94  \u9ed8\u8ba4\u56fd\u5185");
                } else {
                    NLog.m916d("Config", "res" + res.toString());
                    String country_id = res.getData().getCountry_id();
                    NLog.m916d("Config", "country_id" + country_id);
                    MySharedPreferences.setString(this.mContext, Constants.CurrentCountry, country_id);
                    if ("CN".equals(country_id)) {
                        configurl = Constants.CONFIG_URL_CN;
                        configlan = Contact.RELATION_FRIEND;
                    } else {
                        configurl = Constants.CONFIG_URL_US;
                        configlan = Contact.RELATION_BACKNAME;
                    }
                }
            } else {
                configurl = Constants.CONFIG_URL_US;
                configlan = Contact.RELATION_BACKNAME;
                NLog.m916d("Config", "2---->\u53d1\u5e03\u73af\u5883\uff1a\u6d77\u5916");
            }
        } else {
            configurl = Constants.CONFIG_URL_US;
            configlan = Contact.RELATION_BACKNAME;
            NLog.m916d("Config", "3---->\u53d1\u5e03\u73af\u5883\uff1a\u6d77\u5916");
        }
        res = action.getConfigService(configurl, "0.0.0", Contact.RELATION_BACKNAME);
        if (res.getCode() == 0) {
            getInstance(this.mContext).saveConfig(res.getData().getUrls());
        }
    }

    public List<InterfaceUrl> getInterfaceUrlList(List<ConfigUrl> list) {
        List<InterfaceUrl> result = new ArrayList();
        if (list != null) {
            for (ConfigUrl tmp : list) {
                InterfaceUrl item = new InterfaceUrl();
                item.setKey(tmp.getKey());
                item.setValue(tmp.getValue());
                result.add(item);
            }
        }
        return result;
    }
}
