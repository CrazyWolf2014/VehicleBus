package com.ifoer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.amap.mapapi.location.LocationManagerProxy;
import com.car.result.DiagSoftIdListResult;
import com.cnlaunch.x431pro.common.Constants;
import com.cnmobi.im.dto.Msg;
import com.ifoer.dbentity.Car;
import com.ifoer.dbentity.CarLogo;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.dbentity.Language;
import com.ifoer.dbentity.SerialNumber;
import com.ifoer.dbentity.ShoppingCar;
import com.ifoer.dbentity.Version;
import com.ifoer.entity.DiagSoftIdDTO;
import com.ifoer.entity.Drafts;
import com.ifoer.entity.OperatingRecordInfo;
import com.ifoer.entity.OrderDetail;
import com.ifoer.entity.PackageDetailDto;
import com.ifoer.entity.PayKeyAll;
import com.ifoer.entity.Report;
import com.ifoer.mine.Contact;
import com.ifoer.util.Common;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class DBDao {
    private static final String CAR_AREA = "car_area";
    private static final String CAR_ICON = "car_icon";
    private static final String CAR_LOGO = "car_logo";
    private static final String CAR_VERSION = "car_version";
    private static final boolean f1221D = false;
    private static final String DIAGNOSTIC_REPORT = "diagnostic_report";
    private static final String DOWNLOAD = "download";
    private static final String DRAFTS = "drafts";
    private static final String LANGUAGE = "language";
    public static final String OPERATING_RECORD_TABLE = "operating_record_table";
    private static final String PAYKEY = "payKey";
    private static final String SERIALNO = "serialNo";
    private static final String SHOPPING_CAR = "shopping_car";
    private static final String UPGRADE = "upgrade";
    private static DBDao dao;
    private Context context;

    static {
        dao = null;
    }

    public DBDao(Context context) {
        this.context = context;
    }

    public static DBDao getInstance(Context context) {
        if (dao == null) {
            dao = new DBDao(context);
        }
        return dao;
    }

    public SQLiteDatabase getConnection() {
        return new DBHelper(this.context).getWritableDatabase();
    }

    public void deleteRecord(String serialNo, String carName, String maxVersion, SQLiteDatabase db) {
    }

    public void updateMaxVersionNo(String serialNo, String carName, String lanName, String maxVersionNo, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put("maxVersion", maxVersionNo);
            db.update(UPGRADE, values, "serialNo=? and carName=? COLLATE NOCASE  and lanName=?", new String[]{serialNo, carName, lanName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExistSoftId(SQLiteDatabase db) {
        boolean isExist = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(softId) softId from upgrade", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast() && Integer.parseInt(cursor.getString(cursor.getColumnIndex("softId"))) > 0) {
                isExist = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExist;
    }

    public void updateUpgrade(SQLiteDatabase db) {
        List<Map<String, String>> list = new ArrayList();
        Cursor cursor = null;
        cursor = db.rawQuery("select softPackageId,a.softId from car_icon a,upgrade b where a.softPackageId=b.carName COLLATE NOCASE", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                Map<String, String> map = new HashMap();
                map.put("softPackageId", cursor.getString(cursor.getColumnIndex("softPackageId")));
                map.put("softId", cursor.getString(cursor.getColumnIndex("softId")));
                list.add(map);
                cursor.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (list.size() > 0) {
            for (Map<String, String> map2 : list) {
                ContentValues cvalues = new ContentValues();
                cvalues.put("softId", ((String) map2.get("softId")).toString());
                db.update(UPGRADE, cvalues, "carName=? COLLATE NOCASE", new String[]{((String) map2.get("softPackageId")).toString()});
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public String getMaxVersion(String serialNo, String carName, String lanName, SQLiteDatabase db) {
        Cursor cursor = null;
        String maxOldVersion = null;
        try {
            cursor = db.rawQuery("select maxVersion from upgrade where serialNo=? and carName=? COLLATE NOCASE and lanName=?", new String[]{serialNo, carName, lanName});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maxOldVersion = cursor.getString(cursor.getColumnIndex("maxVersion"));
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxOldVersion;
    }

    public String queryMaxVersion(String serialNo, String softId, String lanName, SQLiteDatabase db) {
        Cursor cursor = null;
        String maxOldVersion = null;
        try {
            cursor = db.rawQuery("select maxVersion from upgrade where serialNo=? and softId=? and lanName=?", new String[]{serialNo, softId, lanName});
            if (cursor.moveToFirst()) {
                maxOldVersion = cursor.getString(cursor.getColumnIndex("maxVersion"));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxOldVersion;
    }

    public boolean isHasData(SQLiteDatabase db) {
        boolean isThereData = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from upgrade", null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) <= 0) {
                    isThereData = f1221D;
                } else {
                    isThereData = true;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isThereData;
    }

    public void insertToUpgrade(String serialNo, String carName, String softId, String maxVersion, String lanName, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(SERIALNO, serialNo);
            values.put("carName", carName);
            values.put("softId", softId);
            values.put("maxVersion", maxVersion);
            values.put("lanName", lanName);
            db.insert(UPGRADE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteUpgradeVersionInfo(String serialNo, String carName, String maxVersion, SQLiteDatabase db) {
        try {
            String[] whereArgs = new String[]{serialNo, carName, maxVersion};
            return db.delete(UPGRADE, "serialNo=? and carName=? and maxVersion=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void addToUpgrade(SQLiteDatabase db) {
        List<CarVersionInfo> infoList = new ArrayList();
        Cursor cursor = null;
        cursor = db.rawQuery("select serialNo,carId, max(versionNo) versionNo,lanName from car_version group by carId,lanName,serialNo", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                CarVersionInfo info = new CarVersionInfo();
                info.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                info.setCarId(cursor.getString(cursor.getColumnIndex("carId")));
                info.setLanName(cursor.getString(cursor.getColumnIndex("lanName")));
                info.setVersionNo(cursor.getString(cursor.getColumnIndex("versionNo")));
                infoList.add(info);
                cursor.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (infoList.size() > 0) {
            for (CarVersionInfo info2 : infoList) {
                if (!isThereRecord(info2, db)) {
                    ContentValues values = new ContentValues();
                    values.put(SERIALNO, info2.getSerialNo());
                    values.put("carName", info2.getCarId());
                    values.put("lanName", info2.getLanName());
                    values.put("maxVersion", info2.getVersionNo());
                    db.insert(UPGRADE, null, values);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public boolean isThereRecord(CarVersionInfo info, SQLiteDatabase db) {
        try {
            Cursor cursor = db.rawQuery("select count(*) from upgrade where serialNo=? and carName=? COLLATE NOCASE and lanName=? and maxVersion=?", new String[]{info.getSerialNo(), info.getCarId(), info.getLanName(), info.getVersionNo()});
            if (!cursor.moveToFirst()) {
                return f1221D;
            }
            if (cursor.getInt(0) <= 0) {
                return f1221D;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return f1221D;
        }
    }

    public void updatePayKey(String paykey, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(LocationManagerProxy.KEY_STATUS_CHANGED, Contact.RELATION_FRIEND);
            db.update(PAYKEY, values, "paykey=?", new String[]{paykey});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPayKey(String cc, String serialNo, String orderSN, String paykey, String status, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(MultipleAddresses.CC, cc);
            values.put(SERIALNO, serialNo);
            values.put("orderSN", orderSN);
            values.put("paykey", paykey);
            values.put(LocationManagerProxy.KEY_STATUS_CHANGED, status);
            db.insert(PAYKEY, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> queryPayKey(String cc, SQLiteDatabase db) {
        List<String> list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select paykey  from payKey where cc=? and status=?", new String[]{cc, Contact.RELATION_ASK});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex("paykey")));
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public List<PayKeyAll> queryPayKeyAll(SQLiteDatabase db) {
        List<PayKeyAll> list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from payKey where status=?", new String[]{Contact.RELATION_ASK});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PayKeyAll payKeyAll = new PayKeyAll();
                payKeyAll.setCc(cursor.getString(cursor.getColumnIndex(MultipleAddresses.CC)));
                payKeyAll.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                payKeyAll.setOrderSN(cursor.getString(cursor.getColumnIndex("orderSN")));
                payKeyAll.setPaykey(cursor.getString(cursor.getColumnIndex("paykey")));
                payKeyAll.setStatus(cursor.getString(cursor.getColumnIndex(LocationManagerProxy.KEY_STATUS_CHANGED)));
                list.add(payKeyAll);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public int deleteDynamicLibrary(String serialNo, String carId, String versionNo, SQLiteDatabase db) {
        try {
            String[] whereArgs = new String[]{serialNo, carId, versionNo};
            return db.delete(CAR_VERSION, "serialNo=? and carId=? COLLATE NOCASE and versionNo=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteDrafts(int id, SQLiteDatabase db) {
        try {
            String[] whereArgs = new String[]{String.valueOf(id)};
            return db.delete(DRAFTS, "_id=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long addDrafts(String name, String serviceCycle, String phoneNum, String serialNo, String password, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("serviceCycle", serviceCycle);
            values.put("phoneNum", phoneNum);
            values.put(SERIALNO, serialNo);
            values.put("password", password);
            return db.insert(DRAFTS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Drafts> queryDrafts(SQLiteDatabase db) {
        List<Drafts> lists = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select *  from drafts order by _id desc", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Drafts drafts = new Drafts();
                drafts.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                drafts.setServiceCycle(cursor.getString(cursor.getColumnIndex("serviceCycle")));
                drafts.setName(cursor.getString(cursor.getColumnIndex("name")));
                drafts.setPhoneNum(cursor.getString(cursor.getColumnIndex("phoneNum")));
                drafts.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                drafts.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                lists.add(drafts);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }

    public int updateDrafts(int id, String name, String phoneNum, String serialNo, String password, String serviceCycle, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("phoneNum", phoneNum);
            values.put(SERIALNO, serialNo);
            values.put("password", password);
            values.put("serviceCycle", serviceCycle);
            String[] whereArgs = new String[]{String.valueOf(id)};
            return db.update(DRAFTS, values, "_id=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int querySoftId(String softPackageId, SQLiteDatabase db) {
        Cursor cursor = null;
        int softId = 0;
        try {
            cursor = db.rawQuery("select softId  from car_icon where softPackageId=? COLLATE NOCASE", new String[]{softPackageId});
            if (cursor.moveToFirst()) {
                String sId = cursor.getString(cursor.getColumnIndex("softId"));
                if (sId != null) {
                    softId = Integer.parseInt(sId);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return softId;
    }

    public int UpdateReport(String id, String reportName, String reportPath, SQLiteDatabase db) {
        int result = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("reportName", reportName);
            values.put("reportPath", reportPath);
            result = db.update(DIAGNOSTIC_REPORT, values, "_id=?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addReport(String reportName, String creationTime, String serialNo, String reportPath, String type, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put("reportName", reportName);
            values.put("creationTime", creationTime);
            values.put(SERIALNO, serialNo);
            values.put("reportPath", reportPath);
            values.put(SharedPref.TYPE, type);
            db.insert(DIAGNOSTIC_REPORT, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteReport(int id, SQLiteDatabase db) {
        try {
            String[] whereArgs = new String[]{String.valueOf(id)};
            return db.delete(DIAGNOSTIC_REPORT, "_id=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Report> queryReport(String cc, String type, SQLiteDatabase db) {
        List<Report> lists = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select *  from diagnostic_report where type=? order by creationTime desc", new String[]{type});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Report report = new Report();
                report.setId(new StringBuilder(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))).toString());
                report.setReportName(cursor.getString(cursor.getColumnIndex("reportName")));
                report.setCreationTime(cursor.getString(cursor.getColumnIndex("creationTime")));
                report.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                String reportPath = cursor.getString(cursor.getColumnIndex("reportPath"));
                String filePath = reportPath.substring(reportPath.indexOf(EntityCapsManager.ELEMENT, reportPath.indexOf(EntityCapsManager.ELEMENT) + 1), reportPath.length());
                String sdPath = Common.getSdCardPath();
                report.setReportPath(reportPath);
                lists.add(report);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }

    public List<Report> queryRemoteDiagReport(String type, SQLiteDatabase db) {
        List<Report> lists = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select *  from diagnostic_report where type=? order by creationTime desc", new String[]{type});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Report report = new Report();
                report.setId(new StringBuilder(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))).toString());
                report.setReportName(cursor.getString(cursor.getColumnIndex("reportName")));
                report.setCreationTime(cursor.getString(cursor.getColumnIndex("creationTime")));
                report.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                String reportPath = cursor.getString(cursor.getColumnIndex("reportPath"));
                String filePath = reportPath.substring(reportPath.indexOf(EntityCapsManager.ELEMENT, reportPath.indexOf(EntityCapsManager.ELEMENT) + 1), reportPath.length());
                String sdPath = Common.getSdCardPath();
                report.setReportPath(reportPath);
                lists.add(report);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }

    public void deleteAllTable(SQLiteDatabase db) {
        new DBHelper(this.context).onUpgrade(db, 1, 2);
    }

    public synchronized List<OrderDetail> getIcons(List<OrderDetail> details, SQLiteDatabase db) {
        List<OrderDetail> lists;
        lists = new ArrayList();
        try {
            for (OrderDetail detail : details) {
                Cursor cursor = db.rawQuery("select icon,enName  from car_icon where softId=?", new String[]{new StringBuilder(String.valueOf(detail.getSoftId())).toString()});
                if (cursor.moveToFirst()) {
                    detail.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                    detail.setSoftPackageID(cursor.getString(cursor.getColumnIndex("enName")));
                }
                lists.add(detail);
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    public synchronized List<PackageDetailDto> getIcon(List<PackageDetailDto> list, SQLiteDatabase db) {
        List<PackageDetailDto> lists = new ArrayList();
        try {
            for (PackageDetailDto detail : list) {
                Cursor cursor = db.rawQuery("select icon,enName  from car_icon where softId=?", new String[]{new StringBuilder(String.valueOf(detail.getSoftId())).toString()});
                if (cursor.moveToFirst()) {
                    detail.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                    detail.setSoftPackageID(cursor.getString(cursor.getColumnIndex("enName")));
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isThereData(SQLiteDatabase db) {
        boolean isThereData = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from car_icon", null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) <= 0) {
                    isThereData = f1221D;
                } else {
                    isThereData = true;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isThereData;
    }

    public void updateCarIcon(DiagSoftIdListResult result, SQLiteDatabase db) {
        try {
            for (DiagSoftIdDTO dto : result.getDiagSoftIdList()) {
                ContentValues values = new ContentValues();
                values.put("softId", dto.getSoftId());
                String[] whereArgs = new String[]{dto.getSoftPackageId()};
                db.update(CAR_ICON, values, "softPackageId=? COLLATE NOCASE", whereArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertToCarIcon(List<List<HashMap<String, Object>>> lists, SQLiteDatabase db) {
        try {
            for (List<HashMap<String, Object>> list : lists) {
                for (HashMap<String, Object> map : list) {
                    ContentValues values = new ContentValues();
                    values.put("softPackageId", map.get("softPackageId").toString());
                    values.put("zhName", map.get("name_zh").toString());
                    values.put("enName", map.get("name").toString());
                    values.put("icon", map.get("icon").toString());
                    values.put("areaId", Integer.valueOf(Integer.parseInt(map.get("areaId").toString())));
                    values.put("senName", this.context.getString(Integer.parseInt(map.get("sname").toString())));
                    values.put("szhName", map.get("sname_zh").toString());
                    db.insert(CAR_ICON, null, values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<HashMap<String, Object>> query(String areaId, String serialNo, String lanName, SQLiteDatabase db) {
        List<HashMap<String, Object>> list;
        list = new ArrayList();
        List<HashMap<String, Object>> list1 = new ArrayList();
        Cursor cursor = null;
        String sql = XmlPullParser.NO_NAMESPACE;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            sql = "select car_icon.*,(select count(*) from car_version where car_version.carId=car_icon.softPackageId COLLATE NOCASE and serialNo=" + serialNo + " and lanName='" + lanName + "') flag from " + CAR_ICON + " where areaId=? order by car_icon.szhName asc";
        } else {
            sql = "select car_icon.*,(select count(*) from car_version where car_version.carId=car_icon.softPackageId COLLATE NOCASE and serialNo=" + serialNo + " and lanName='" + lanName + "') flag from " + CAR_ICON + " where areaId=? order by car_icon.senName asc";
        }
        cursor = db.rawQuery(sql, new String[]{areaId});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HashMap<String, Object> maps = new HashMap();
            maps.put("softPackageId", cursor.getString(cursor.getColumnIndex("softPackageId")));
            maps.put("softId", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("softId"))));
            maps.put("name_zh", cursor.getString(cursor.getColumnIndex("zhName")));
            maps.put("name", cursor.getString(cursor.getColumnIndex("enName")));
            maps.put("icon", cursor.getString(cursor.getColumnIndex("icon")));
            maps.put("iconPath", cursor.getString(cursor.getColumnIndex("iconPath")));
            maps.put("areaId", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("areaId"))));
            maps.put("flag", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("flag"))));
            if (cursor.getInt(cursor.getColumnIndex("flag")) > 0) {
                list1.add(maps);
            } else {
                try {
                    list.add(maps);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            cursor.moveToNext();
        }
        if (list1.size() > 0) {
            for (int i = 0; i < list1.size(); i++) {
                list.add(i, (HashMap) list1.get(i));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public boolean isClearData(SQLiteDatabase db) {
        boolean isClear = f1221D;
        Cursor vCursor = null;
        Cursor lCursor = null;
        int vCount = 0;
        int lCount = 0;
        try {
            vCursor = db.rawQuery("select count(*)  from car_version", null);
            if (vCursor.moveToFirst()) {
                vCount = vCursor.getInt(0);
                if (vCount <= 0) {
                    isClear = true;
                } else {
                    isClear = f1221D;
                }
            }
            lCursor = db.rawQuery("select count(*)  from car_logo", null);
            if (lCursor.moveToFirst()) {
                lCount = lCursor.getInt(0);
            }
            if (vCount <= 0 && lCount <= 0) {
                isClear = true;
            }
            if (vCursor != null) {
                vCursor.close();
            }
            if (lCursor != null) {
                lCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (vCursor != null) {
                vCursor.close();
            }
            if (lCursor != null) {
                lCursor.close();
            }
        } catch (Throwable th) {
            if (vCursor != null) {
                vCursor.close();
            }
            if (lCursor != null) {
                lCursor.close();
            }
        }
        return isClear;
    }

    public void addToCarVer(SerialNumber serialNumber, SQLiteDatabase db) {
        try {
            for (Car car : serialNumber.getCarList()) {
                String serialNum = serialNumber.getSerialNum();
                String carId = car.getName();
                int areaId = car.getAreaId();
                List<Version> versions = car.getVersions();
                List<CarLogo> carLogos = car.getCarLogos();
                for (int i = 0; i < versions.size(); i++) {
                    Log.i("add Car", "versions.size()" + versions.size());
                    String versionNo = ((Version) versions.get(i)).getVersionName();
                    String versionDir = ((Version) versions.get(i)).getVersionPath();
                    List<Language> languages = ((Version) versions.get(i)).getLanguage();
                    for (int j = 0; j < languages.size(); j++) {
                        String lanName = ((Language) languages.get(j)).getLanName();
                        if (!isExist(carId, serialNum, versionNo, lanName, db)) {
                            ContentValues values = new ContentValues();
                            values.put("carId", carId);
                            values.put(SERIALNO, serialNum);
                            values.put("areaId", Integer.valueOf(areaId));
                            values.put("versionNo", versionNo);
                            values.put("versionDir", versionDir);
                            values.put("lanName", lanName);
                            db.insert(CAR_VERSION, null, values);
                        }
                        String maxOldVersion = getMaxVersion(serialNum, carId, lanName, db);
                        if (maxOldVersion != null) {
                            if (Double.parseDouble(maxOldVersion.split("V")[1]) < Double.parseDouble(versionNo.split("V")[1])) {
                                updateMaxVersionNo(serialNum, carId, lanName, versionNo, db);
                            }
                        } else {
                            insertToUpgrade(serialNum, carId, new StringBuilder(String.valueOf(querySoftId(carId, db))).toString(), versionNo, lanName, db);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(List<SerialNumber> serialNumList, SQLiteDatabase db) {
        try {
            for (SerialNumber sn : serialNumList) {
                String serialNo = sn.getSerialNum();
                List<Car> cars = sn.getCarList();
                if (cars.size() > 0 && cars != null) {
                    for (Car car : cars) {
                        String carName = car.getName();
                        int areaId = car.getAreaId();
                        List<Version> versions = car.getVersions();
                        List<CarLogo> carLogos = car.getCarLogos();
                        if (versions.size() > 0) {
                            for (Version version : versions) {
                                String versionNo = version.getVersionName();
                                String versionPath = version.getVersionPath();
                                List<Language> languages = version.getLanguage();
                                if (languages.size() > 0) {
                                    for (Language lan : languages) {
                                        String lanName = lan.getLanName();
                                        if (!isExist(carName, serialNo, versionNo, lanName, db)) {
                                            ContentValues values = new ContentValues();
                                            values.put("carId", carName);
                                            values.put(SERIALNO, serialNo);
                                            values.put("areaId", Integer.valueOf(areaId));
                                            values.put("versionNo", versionNo);
                                            values.put("versionDir", versionPath);
                                            values.put("lanName", lanName);
                                            db.insert(CAR_VERSION, null, values);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String carId, String serialNo, String versionNo, String lanName, SQLiteDatabase db) {
        boolean isExit = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from car_version where carId=? COLLATE NOCASE and versionNo=? and lanName=? and serialNo=?", new String[]{carId, versionNo, lanName, serialNo});
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                isExit = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExit;
    }

    public boolean isExistIcon(String carId, String serialNo, String carLogoPath, String lanName, SQLiteDatabase db) {
        boolean isExit = f1221D;
        Cursor cursor = null;
        if (lanName == null) {
            lanName = XmlPullParser.NO_NAMESPACE;
        }
        if (carId == null) {
            carId = XmlPullParser.NO_NAMESPACE;
        }
        if (serialNo == null) {
            serialNo = XmlPullParser.NO_NAMESPACE;
        }
        if (carLogoPath == null) {
            carLogoPath = XmlPullParser.NO_NAMESPACE;
        }
        try {
            cursor = db.rawQuery("select count(*)  from car_logo where carId=? COLLATE NOCASE and carLogoPath=? and lanName=? and serialNo=?", new String[]{carId, carLogoPath, lanName, serialNo});
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                isExit = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExit;
    }

    public boolean isDownload(String carId, String serialNo, String lanName, SQLiteDatabase db) {
        boolean isDownload = f1221D;
        Cursor cursor = null;
        try {
            String tempSql = XmlPullParser.NO_NAMESPACE;
            if (lanName.equals(Constants.DEFAULT_LANGUAGE) || lanName.equals("ZH") || lanName.equals("CN")) {
                tempSql = XmlPullParser.NO_NAMESPACE;
            } else {
                tempSql = " or lanName='EN' and carId='" + carId + "' COLLATE NOCASE and serialNo='" + serialNo + "'";
            }
            cursor = db.rawQuery("select count(*)  from car_version where carId=? COLLATE NOCASE and serialNo=? and lanName=? " + tempSql + " COLLATE NOCASE", new String[]{carId, serialNo, lanName});
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                isDownload = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isDownload;
    }

    public ArrayList<CarVersionInfo> queryCarVersion(String carId, String lanName, String serialNo, SQLiteDatabase db) {
        ArrayList<CarVersionInfo> infos = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select carId,versionNo,versionDir,lanName from car_version where carId=? COLLATE NOCASE and lanName='" + lanName + "' and serialNo=? order by versionNo desc", new String[]{carId, serialNo});
            cursor.moveToFirst();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CarVersionInfo info = new CarVersionInfo();
                info.setCarId(cursor.getString(cursor.getColumnIndex("carId")));
                info.setVersionNo(cursor.getString(cursor.getColumnIndex("versionNo")));
                info.setVersionDir(Common.getSdCardPath() + new StringBuilder(Constants.des_password).append(cursor.getString(cursor.getColumnIndex("versionDir")).split(Constants.des_password)[1]).toString());
                info.setLanName(cursor.getString(cursor.getColumnIndex("lanName")));
                infos.add(info);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return infos;
    }

    public List<ShoppingCar> getShoppingList(String cc, SQLiteDatabase db) {
        List<ShoppingCar> lists = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from  shopping_car where cc=? order by serialNo", new String[]{cc});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ShoppingCar shopCar = new ShoppingCar();
                shopCar.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                shopCar.setCc(cursor.getString(cursor.getColumnIndex(MultipleAddresses.CC)));
                shopCar.setSoftName(cursor.getString(cursor.getColumnIndex("softName")));
                shopCar.setSoftId(cursor.getString(cursor.getColumnIndex("softId")));
                shopCar.setVersion(cursor.getString(cursor.getColumnIndex(AlixDefine.VERSION)));
                shopCar.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                shopCar.setCurrencyId(cursor.getString(cursor.getColumnIndex("currencyId")));
                shopCar.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIALNO)));
                shopCar.setBuyType(cursor.getString(cursor.getColumnIndex("buyType")));
                shopCar.setTotalPrice(cursor.getString(cursor.getColumnIndex("totalPrice")));
                shopCar.setDate(cursor.getString(cursor.getColumnIndex(Msg.DATE)));
                lists.add(shopCar);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }

    public synchronized boolean addShoppingCarBefore(ShoppingCar shopCar, SQLiteDatabase db) {
        boolean isExist;
        isExist = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from shopping_car where cc=? and serialNo=? and softId=?", new String[]{shopCar.getCc(), shopCar.getSerialNo(), shopCar.getSoftId()});
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                isExist = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExist;
    }

    public synchronized boolean addShoppingCar(ShoppingCar shopCar, SQLiteDatabase db) {
        boolean isSuccess;
        isSuccess = f1221D;
        try {
            ContentValues values = new ContentValues();
            values.put(MultipleAddresses.CC, shopCar.getCc());
            values.put("softName", shopCar.getSoftName());
            values.put("softId", shopCar.getSoftId());
            values.put(AlixDefine.VERSION, shopCar.getVersion());
            values.put("price", shopCar.getPrice());
            values.put("currencyId", shopCar.getCurrencyId());
            values.put(SERIALNO, shopCar.getSerialNo());
            values.put("buyType", shopCar.getBuyType());
            values.put("totalPrice ", shopCar.getTotalPrice());
            values.put("date ", new SimpleDateFormat("yyyy/MM/dd,HH:mm").format(new Date()));
            if (db.insert(SHOPPING_CAR, null, values) >= 0) {
                isSuccess = true;
            }
        } catch (Exception e) {
            isSuccess = f1221D;
            e.printStackTrace();
        }
        return isSuccess;
    }

    public void deleteShoppingCar(List<ShoppingCar> lists, SQLiteDatabase db) {
        int i = 0;
        while (i < lists.size()) {
            try {
                int id = ((ShoppingCar) lists.get(i)).getId();
                db.delete(SHOPPING_CAR, "_id=?", new String[]{String.valueOf(id)});
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public synchronized List<String> querytAllSerialNo(SQLiteDatabase db) {
        List<String> list;
        list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select serialNo from serialNo order by _id desc", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String serialNo = cursor.getString(cursor.getColumnIndex(SERIALNO));
                if (serialNo != null && !serialNo.equals(XmlPullParser.NO_NAMESPACE) && serialNo.length() == 12 && serialNo.substring(0, 5).equals(MySharedPreferences.getStringValue(this.context, "PORT_START"))) {
                    list.add(serialNo);
                }
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void addSerialNo(List<HashMap<String, String>> list, SQLiteDatabase db) {
        try {
            for (HashMap<String, String> map : list) {
                String cc;
                String serialNo = ((String) map.get(SERIALNO)).toString();
                if (map.get(MultipleAddresses.CC) == null) {
                    cc = XmlPullParser.NO_NAMESPACE;
                } else {
                    cc = ((String) map.get(MultipleAddresses.CC)).toString();
                }
                if (isExistsSerialNo(serialNo, db)) {
                    ContentValues values = new ContentValues();
                    values.put(MultipleAddresses.CC, cc);
                    db.update(SERIALNO, values, "serialNo=?", new String[]{serialNo});
                } else {
                    ContentValues conValues = new ContentValues();
                    conValues.put(MultipleAddresses.CC, cc);
                    conValues.put(SERIALNO, serialNo);
                    db.insert(SERIALNO, null, conValues);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isExistsSerialNo(String serialNo, SQLiteDatabase db) {
        boolean isExists;
        isExists = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from serialNo where serialNo=?", new String[]{serialNo});
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                isExists = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExists;
    }

    public boolean isClearSerialNo(SQLiteDatabase db) {
        boolean isClear = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from serialNo", null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) <= 0) {
                    isClear = true;
                } else {
                    isClear = f1221D;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isClear;
    }

    public synchronized boolean addOperatingRecord(OperatingRecordInfo recordInfo, SQLiteDatabase db) {
        boolean isSuccess;
        isSuccess = f1221D;
        try {
            ContentValues values = new ContentValues();
            values.put("serialNumber", recordInfo.getSerialNumber());
            values.put("testTime", recordInfo.getTestTime());
            values.put("testSite", recordInfo.getTestSite());
            values.put("textCar", recordInfo.getTestCar());
            if (db.insert(OPERATING_RECORD_TABLE, null, values) >= 0) {
                isSuccess = true;
            }
        } catch (Exception e) {
            isSuccess = f1221D;
            e.printStackTrace();
        }
        return isSuccess;
    }

    public synchronized List<OperatingRecordInfo> getOperatingRecord(String CC, SQLiteDatabase db) {
        List<OperatingRecordInfo> list;
        list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select record._id,record.serialNumber,record.testTime,record.testSite,record.textCar from serialNo serial,operating_record_table record where serial.serialNo=record.serialNumber and serial.cc=? order by record.testTime desc", new String[]{CC});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OperatingRecordInfo recordInfo = new OperatingRecordInfo();
                recordInfo.setRecordId(cursor.getInt(cursor.getColumnIndex("_id")));
                recordInfo.setSerialNumber(cursor.getString(cursor.getColumnIndex("serialNumber")));
                recordInfo.setTestTime(cursor.getString(cursor.getColumnIndex("testTime")));
                recordInfo.setTestSite(cursor.getString(cursor.getColumnIndex("testSite")));
                recordInfo.setTestCar(cursor.getString(cursor.getColumnIndex("textCar")));
                list.add(recordInfo);
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public synchronized String getDownloadVersion(SQLiteDatabase db) {
        String version;
        version = XmlPullParser.NO_NAMESPACE;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from download where numbering=?", new String[]{"001"});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                version = cursor.getString(cursor.getColumnIndex("versionNo"));
                cursor.moveToNext();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return version;
    }

    public synchronized long setDownloadVersion(SQLiteDatabase db, String version) {
        long row;
        row = -1;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from download where numbering=?", new String[]{"001"});
            ContentValues downloadValue;
            if (cursor.getCount() == 1) {
                downloadValue = new ContentValues();
                downloadValue.put("versionNo", version);
                row = (long) db.update(DOWNLOAD, downloadValue, "numbering=?", new String[]{"001"});
            } else if (cursor.getCount() == 0) {
                downloadValue = new ContentValues();
                downloadValue.put("numbering", "001");
                downloadValue.put("versionNo", version);
                row = db.insert(DOWNLOAD, null, downloadValue);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return row;
    }

    public boolean isClearDownloadVersion(SQLiteDatabase db) {
        boolean isClear = f1221D;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*)  from download", null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) <= 0) {
                    isClear = true;
                } else {
                    isClear = f1221D;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isClear;
    }

    public String queryCarName(SQLiteDatabase db, String softPackageId, String country) {
        String carName = XmlPullParser.NO_NAMESPACE;
        Cursor cursor = db.rawQuery("select zhName,enName  from car_icon where softPackageId='" + softPackageId + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            if (country.equals("CN") || country.equals("ZH")) {
                carName = cursor.getString(cursor.getColumnIndex("zhName"));
            } else {
                carName = cursor.getString(cursor.getColumnIndex("enName"));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return carName;
    }
}
