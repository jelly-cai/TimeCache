package com.jelly.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Author YC
 * 2018/3/2 0002.
 */

public class TimeCacheDbUtil {

    public static final String TAG = "TimeCacheDbUtil";

    private Context context;
    private static TimeCacheDbHelper dbHelper;
    /**
     * 默认保存一周,时间单位为毫秒
     */
    private Long CACHE_TIME = 7 * 24 * 60 * 60 * 1000L;

    public TimeCacheDbUtil(Context context){
        this.context = context;
    }

    /**
     * 初始化DbHelper
     */
    private void initDbHelper() {
        if(dbHelper == null){
            dbHelper = new TimeCacheDbHelper(context);
        }
    }

    /**
     * 获得写数据的数据库操作的对象
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        initDbHelper();
        return dbHelper.getWritableDatabase();
    }

    /**
     * 获得读数据的数据库操作的对象
     * @return
     */
    private SQLiteDatabase getReadableDatabase(){
        initDbHelper();
        return dbHelper.getWritableDatabase();
    }

    /**
     * 新增缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public long addCache(String key,Object value){
        SQLiteDatabase db = getWritableDatabase();
        deleteCache(key,db);
        ContentValues increase = new ContentValues();
        increase.put(TimeCacheDbHelper.KEY_FIELD,key);
        String values = value.getClass().isPrimitive() ? value + "" : new Gson().toJson(value);//判断是否为基本数据类型
        increase.put(TimeCacheDbHelper.VALUE_FIELD,values);
        increase.put(TimeCacheDbHelper.SAVE_TIME_FIELD, System.currentTimeMillis());
        increase.put(TimeCacheDbHelper.CACHE_TIME_FIELD, CACHE_TIME);
        long status = db.insert(TimeCacheDbHelper.DATABASE_TABLE, null, increase);
        db.close();
        return status;
    }

    /**
     * 根据key获取值
     * @param key
     * @param clazz
     * @return
     */
    public <T> T  getCacheByKey (String key,Class<T> clazz){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur =  db.query(TimeCacheDbHelper.DATABASE_TABLE, null, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{ key }, null, null, null);
        if(cur != null && cur.getCount() > 0){
            cur.moveToNext();
            long cacheTime = cur.getLong(cur.getColumnIndex(TimeCacheDbHelper.CACHE_TIME_FIELD));
            long saveTime = Long.parseLong(cur.getString(cur.getColumnIndex(TimeCacheDbHelper.SAVE_TIME_FIELD)));
            long perTime = System.currentTimeMillis();
            String value = cur.getString(cur.getColumnIndex(TimeCacheDbHelper.VALUE_FIELD));
            if((perTime - saveTime) < cacheTime){
                //在保存时间内
                return new Gson().fromJson(value,clazz);
            }
            return null;
        }
        cur.close();
        db.close();
        return null;
    }

    /**
     * 删除key对应的缓存，会关闭数据库连接
     * @param key
     * @return
     */
    public long deleteCache(String key){
        SQLiteDatabase db= getWritableDatabase();
        int status = db.delete(TimeCacheDbHelper.DATABASE_TABLE, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{key});
        db.close();
        return status;
    }

    /**
     * 删除key对应的缓存,不会关闭数据库连接
     * @param key
     * @param db 数据库对象
     * @return
     */
    public long deleteCache(String key, SQLiteDatabase db){
        int status = db.delete(TimeCacheDbHelper.DATABASE_TABLE, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{key});
        return status;
    }

    /**
     * 更新Cache
     * @param key
     * @param isUpdataTime 是否更新返
     * */
    public void updataCache(String key,Object value,boolean isUpdataTime){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cvtime = new ContentValues();
        if(isUpdataTime){
            cvtime.put("savetime", System.currentTimeMillis());
        }
        cvtime.put("cachetime", CACHE_TIME);
        String values = value.getClass().isPrimitive() ? value+"":new Gson().toJson(value);//判断是否为基本数据类型
        cvtime.put("value",value.toString());
        int status = db.update(TimeCacheDbHelper.DATABASE_TABLE, cvtime, "key=?", new String[] {key});
        db.close();
    }

    /**
     * 清空所有缓存
     */
    public void cleanCache(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+TimeCacheDbHelper.DATABASE_TABLE);
        db.close();
    }
}
