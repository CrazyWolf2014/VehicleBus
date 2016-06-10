package cn.sharesdk.framework.p001b.p002a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: cn.sharesdk.framework.b.a.a */
public class C0036a extends SQLiteOpenHelper {
    public C0036a(Context context) {
        super(context.getApplicationContext(), "sharesdk.db", null, 1);
    }

    public synchronized void close() {
        super.close();
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(" create table  message(_id integer primary key autoincrement,post_time timestamp not null, message_data text not null);");
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        super.onOpen(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
