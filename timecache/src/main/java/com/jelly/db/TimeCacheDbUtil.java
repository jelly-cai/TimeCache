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
    private static SQLiteDatabase db ;
    private static TimeCacheDbUtil dbUtil;
    private static TimeCacheDbHelper dbHelper;
    private Long CACHE_TIME = 7 * 24 * 60L;//默认保存一周

    public TimeCacheDbUtil(Context context){
        this.context = context;
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if(dbHelper == null){
            dbHelper = new TimeCacheDbHelper(context);
        }
        return dbHelper.getWritableDatabase();
    }

    public void addCache(String key,Object value){
        db = getSqLiteDatabase();
        ContentValues increase = new ContentValues();
        increase.put("key",key);
        String values = value.getClass().isPrimitive() ? value+"":new Gson().toJson(value);//判断是否为基本数据类型
        increase.put("value",value.toString());
        increase.put("savetime", System.currentTimeMillis());
        increase.put("cachetime", CACHE_TIME);
        long status = db.insert(TimeCacheDbHelper.DATABASE_TABLE, null, increase);
        db.close();
    }

    /**
     * 根据key获取值
     * @param key
     * @param clazz
     * @return
     */
    public Object  getCacheByKey (String key,Class clazz){
        db = getSqLiteDatabase();
        Cursor cur =  db.query(TimeCacheDbHelper.DATABASE_TABLE, null, "key=?", new String[]{key}, null, null, null);
        if(cur != null && cur.getCount()>0){
                cur.moveToNext();
                long cacheTime = cur.getLong(cur.getColumnIndex("cachetime"));
                long saveTime = Long.parseLong(cur.getString(cur.getColumnIndex("savetime")));
                long perTime = System.currentTimeMillis();
                String value = cur.getString(cur.getColumnIndex("value"));
                if((perTime - saveTime) / (24*60) < cacheTime){//在保存时间内
                    return new Gson().fromJson(value,clazz);
                }
                return null;
        }
        cur.close();
        db.close();
        return null;
    }

    /**
     * 清空key
     * @param key
     * @return
     */
    public void deleteCache(String key){
        db= getSqLiteDatabase();
        int status = db.delete(TimeCacheDbHelper.DATABASE_TABLE, "key = ?", new String[]{key});
        db.close();
    }

    /**
     * 更新Cache
     * @param key
     * @param isUpdataTime 是否更新返
     * */
    public void updataCache(String key,Object value,boolean isUpdataTime){
        db = getSqLiteDatabase();
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
        db = getSqLiteDatabase();
        db.execSQL("delete from "+TimeCacheDbHelper.DATABASE_TABLE);
        db.close();
    }
}
