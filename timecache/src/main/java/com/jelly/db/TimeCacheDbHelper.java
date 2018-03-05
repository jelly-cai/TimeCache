package com.jelly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/3/2.
 */

public class TimeCacheDbHelper extends SQLiteOpenHelper{

    private static  final String DATABASE_NAME ="timecache";

    private static  final int VERSION = 1;

    public static final String DATABASE_TABLE ="cache";

    public TimeCacheDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder cacheBuffer = new StringBuilder();
        cacheBuffer.append("create table if not exists" + " "
                + DATABASE_TABLE); // 表名
        cacheBuffer.append("(");
        cacheBuffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
        cacheBuffer.append("key varchar(60) unique,");
        cacheBuffer.append("value text,");
        cacheBuffer.append("savetime varchar(13),");
        cacheBuffer.append("cachetime int(8)");
        cacheBuffer.append(")");
        db.execSQL(cacheBuffer.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}