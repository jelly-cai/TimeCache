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
        String createSql = "create table if not exists" + DATABASE_TABLE; // 表名
        createSql += "(id INTEGER PRIMARY KEY AUTOINCREMENT,";
        createSql += "key text unique,";
        createSql += "value text,";
        createSql += "savetime text,";
        createSql += "cachetime integer)";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}