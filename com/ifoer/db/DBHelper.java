package com.ifoer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String CAR_AREA = "car_area";
    private static final String CAR_ICON = "car_icon";
    private static final String CAR_LOGO = "car_logo";
    private static final String CAR_VERSION = "car_version";
    public static final String CIRCLE_CHATINFO_TABLE = "circle_chatinfo_table";
    private static final boolean f1222D = false;
    private static final String DBNAME = "expedition_car.db";
    private static final int DBVERSION = 5;
    private static final String DIAGNOSTIC_REPORT = "diagnostic_report";
    private static final String DOWNLOAD = "download";
    private static final String DRAFTS = "drafts";
    private static final String LANGUAGE = "language";
    public static final String OPERATING_RECORD_TABLE = "operating_record_table";
    private static final String PAYKEY = "payKey";
    private static final String SERIALNO = "serialNo";
    private static final String SHOPPING_CAR = "shopping_car";
    private static final String UPGRADE = "upgrade";
    private static final String diagnosticReportSql = "create table diagnostic_report(_id integer primary key autoincrement,reportName text,creationTime text,serialNo text,reportPath text,type text)";
    private static final String download = "create table download(_id integer primary key autoincrement,numbering text,versionNo text)";
    private static final String draftsSql = "create table drafts(_id integer primary key autoincrement,name text,serviceCycle text,phoneNum text,serialNo text,password text)";
    private static final String payKeySql = "create table payKey (_id integer primary key autoincrement,cc text,serialNo text,orderSN text,paykey text,status text)";
    private static final String serialNo = "create table serialNo(_id integer primary key autoincrement,cc text null,serialNo text)";
    private static final String upgradeSql = "create table upgrade (_id integer primary key autoincrement,serialNo text,carName text,softId text,lanName text,maxVersion text)";
    private String carAreaTableSql;
    private String carIcon;
    private String carLogo;
    private String carVersionSql;
    private String circleChatTableSql;
    private String languageSql;
    private String operatingRecordTableSql;
    private String shoppingCar;

    public DBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        this.carAreaTableSql = "create table car_area(_id integer,areaId integer primary key,areaName varchar(10))";
        this.carVersionSql = "create table car_version(_id integer primary key,serialNo text,carId text,areaId integer null,versionNo text,versionDir text,lanName varchar(10))";
        this.languageSql = "create table language(_id integer,lanId integer primary key,lanName varchar(10))";
        this.carLogo = "create table car_logo(_id integer primary key,serialNo text,carId text,carLogoPath text,lanName varchar(10) null)";
        this.shoppingCar = "create table shopping_car(_id integer primary key autoincrement,cc text,softName text,softId text,version text,price text,currencyId text,serialNo text,buyType text,totalPrice text,date text)";
        this.carIcon = "create table car_icon(_id integer primary key autoincrement,softPackageId text,softId integer null,zhName text,enName text,icon text,iconPath text null,areaId integer,szhName text,senName text)";
        this.operatingRecordTableSql = "create table operating_record_table(_id integer primary key autoincrement,serialNumber text,testTime text,testSite text,textCar text)";
        this.circleChatTableSql = "create table circle_chatinfo_table(_id integer primary key autoincrement,roomID text,userName text,cc text,fromName text,fromCC text,time text,message text,mySend boolean,image boolean,toName text ,toCC text)";
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.carAreaTableSql);
        db.execSQL(this.carVersionSql);
        db.execSQL(this.languageSql);
        db.execSQL(this.carLogo);
        db.execSQL(this.shoppingCar);
        db.execSQL(this.carIcon);
        db.execSQL(serialNo);
        db.execSQL(this.circleChatTableSql);
        db.execSQL(diagnosticReportSql);
        db.execSQL(draftsSql);
        db.execSQL(this.operatingRecordTableSql);
        db.execSQL(download);
        db.execSQL(payKeySql);
        db.execSQL(upgradeSql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists car_area");
        db.execSQL("drop table if exists car_version");
        db.execSQL("drop table if exists language");
        db.execSQL("drop table if exists car_logo");
        db.execSQL("drop table if exists shopping_car");
        db.execSQL("drop table if exists car_icon");
        db.execSQL("drop table if exists serialNo");
        db.execSQL("drop table if exists circle_chatinfo_table");
        db.execSQL("drop table if exists operating_record_table");
        db.execSQL("drop table if exists diagnostic_report");
        db.execSQL("drop table if exists drafts");
        db.execSQL("drop table if exists download");
        db.execSQL("drop table if exists payKey");
        db.execSQL("drop table if exists upgrade");
        onCreate(db);
    }
}
