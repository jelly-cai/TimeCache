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

    private static SQLiteDatabase db ;

    private static TimeCacheDbUtil dbUtil;

    private static TimeCacheDbHelper dbHelper;

    public static Long  CACHETIME= 10L;//默认保存10分钟

    public static TimeCacheDbUtil getInstance(Context context) {
        if (null == dbUtil) {
            dbUtil = new TimeCacheDbUtil();
        }
        if (null == dbHelper) {
            dbHelper = new TimeCacheDbHelper(context);
        }
        return dbUtil;
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (null == db) {
            db = dbHelper.getWritableDatabase();
        }
        return db;
    }

    public void addCache(String key,Object value){
        db = getSqLiteDatabase();
        ContentValues increase = new ContentValues();
        increase.put("key",key);
        String values = value.getClass().isPrimitive() ? value+"":new Gson().toJson(value);//判断是否为基本数据类型
        increase.put("value",value.toString());
        increase.put("savetime", System.currentTimeMillis());
        increase.put("cachetime",CACHETIME);
        long status = db.insert(TimeCacheDbHelper.DATABASE_TABLE, null, increase);
        Log.i("", "--db==>addCache"  + status );
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
                long cachetime = cur.getLong(cur.getColumnIndex("cachetime"));
                long savetime = Long.parseLong(cur.getString(cur.getColumnIndex("savetime")));
                long perTime = System.currentTimeMillis();
                String value = cur.getString(cur.getColumnIndex("value"));
                if((perTime - savetime)/(24*60) > cachetime){//在保存时间内
                    Log.i("", "--db==>query"  + new Gson().fromJson(value,clazz) );
                    return new Gson().fromJson(value,clazz);
                }
                throw  new NullException("没有找到相应的值");
        }
        cur.close();
        db.close();
        throw  new NullException("没有找到相应的值");
    }

    /**
     * 清空key
     * @param key
     * @return
     */
    public void deleteCache(String key){
        db= getSqLiteDatabase();
        int status = db.delete(TimeCacheDbHelper.DATABASE_TABLE, "key = ?", new String[]{key});
        Log.i("", "--db==>deleteCache"  + status );
        db.close();
    }

    /**
     * 更新Cache
     * @param key
     * @param isUpdataTime 是否更新返     */
    public void updataCache(String key,Object value,boolean isUpdataTime){
        db = getSqLiteDatabase();
        ContentValues cvtime = new ContentValues();
        if(isUpdataTime){
            cvtime.put("savetime", System.currentTimeMillis());
        }
        cvtime.put("cachetime", CACHETIME);
        String values = value.getClass().isPrimitive() ? value+"":new Gson().toJson(value);//判断是否为基本数据类型
        cvtime.put("value",value.toString());
        int status = db.update(TimeCacheDbHelper.DATABASE_TABLE, cvtime, "key=?", new String[] {key});
        db.close();
        Log.i("", "--db==>updataCache"  + status );
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
