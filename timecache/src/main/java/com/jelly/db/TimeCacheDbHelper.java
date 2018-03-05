package com.jelly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * Created by JellyCai on 2018/3/2.
 */

public class TimeCacheDbHelper extends SQLiteOpenHelper {
    /**
     * 数据库名
     */
    private static final String DATABASE_NAME = "timecache";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;
    /**
     * 缓存表名
     */
    public static final String DATABASE_TABLE = "cache";
    /**
     * id字段自动增长
     */
    public static final String ID = "id";
    /**
     * 缓存的key
     */
    public static final String KEY = "key";
    /**
     * 缓存的value
     */
    public static final String VALUE = "value";
    /**
     * 保存的时间
     */
    public static final String SAVETIME = "savetime";
    /**
     * 缓存时间
     */
    public static final String CACHE_TIME = "cachetime";

    /**
     * 建表语句
     */
    private static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
            + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY + " TEXT,"
            + VALUE + " TEXT,"
            + SAVETIME + " TEXT,"
            + CACHE_TIME + " INTEGER)";

    public TimeCacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}