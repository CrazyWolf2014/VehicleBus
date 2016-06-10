package com.cnmobi.im.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static MySQLiteOpenHelper instance = null;
    private static final String name = "cnmobiIm.db";
    private static final int version = 2;

    private MySQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySQLiteOpenHelper(context);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i("MySQLiteOpenHelper", "onCreate");
        db.execSQL("CREATE TABLE IF NOT EXISTS message(id integer primary key autoincrement,jId varchar(50),time varchar(15),content text,direction varchar(3),type varchar(10),duration integer(2),filePath varchar(100),ownerJid varchar(50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS rider(jId varchar(50),name varchar(15),logoUri varchar(100),online varchar(1),signature varchar(50),type varchar(6),ownerJid varchar(50),constraint pk_rider primary key (jId,ownerJid))");
        db.execSQL("CREATE TABLE IF NOT EXISTS chatroom(jId varchar(50),name varchar(15),occupants integer,description varchar(50),subject varchar(50),createrJid varchar(50),ownerJid varchar(50),constraint pk_chatroom primary key (jId,ownerJid))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("MySQLiteOpenHelper", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS message");
        db.execSQL("DROP TABLE IF EXISTS rider");
        onCreate(db);
    }
}
