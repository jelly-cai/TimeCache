package com.jelly.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author YC
 * 2018/3/2 0002.
 */

public class TimeCacheDbUtil {

    public static final String TAG = "TimeCacheDbUtil";

    private Context context;
    private static TimeCacheDbHelper dbHelper;
    private SQLiteDatabase db;
    private AtomicInteger openCounter = new AtomicInteger();
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
     * 设置缓存时间
     * @param cacheTime 缓存时间
     */
    public void setCacheTime(Long cacheTime) {
        this.CACHE_TIME = cacheTime;
    }

    /**
     * 获得写数据的数据库操作的对象
     */
    private synchronized void getWritableDatabase(){
        initDbHelper();
        if(openCounter.incrementAndGet() == 1){
            db = dbHelper.getWritableDatabase();
        }
    }

    /**
     * 获得读数据的数据库操作的对象
     */
    private synchronized void getReadableDatabase(){
        initDbHelper();
        if(openCounter.incrementAndGet() == 1){
            db = dbHelper.getWritableDatabase();
        }
    }

    /**
     * 关闭数据库连接
     */
    private void closeDataBase(){
        if(openCounter.incrementAndGet() == 0){
            db.close();
        }
    }

    /**
     * 新增缓存,如果缓存存在，先删除再新增
     * @param key 键
     * @param value 值
     * @return Boolean 是否添加成功
     */
    public boolean addCache(String key,Object value){
        getWritableDatabase();

        deleteCache(key,db); //清除已存在缓存

        ContentValues increase = new ContentValues();
        increase.put(TimeCacheDbHelper.KEY_FIELD,key);
        String values = value.toString();//判断是否为基本数据类型
        increase.put(TimeCacheDbHelper.VALUE_FIELD,values);
        increase.put(TimeCacheDbHelper.SAVE_TIME_FIELD, System.currentTimeMillis());
        increase.put(TimeCacheDbHelper.CACHE_TIME_FIELD, CACHE_TIME);
        long rowID = db.insert(TimeCacheDbHelper.DATABASE_TABLE, null, increase);

        closeDataBase();

        if(rowID == -1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 批量存入
     * @param map key -valuer
     */
    public boolean addBatchCache(Map<String,Object> map){
        Thread t = Thread.currentThread();
        String name = t.getName();
        Log.d(TAG, "addBatchCache: " + name);
        getWritableDatabase();
        deleteBatch(map.keySet(),db);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ")
                .append(TimeCacheDbHelper.DATABASE_TABLE)
                .append(" ( ").append(TimeCacheDbHelper.KEY_FIELD)
                .append(",").append(TimeCacheDbHelper.VALUE_FIELD)
                .append(",").append(TimeCacheDbHelper.SAVE_TIME_FIELD)
                .append(",").append(TimeCacheDbHelper.CACHE_TIME_FIELD).append(" ) ")
                .append("VALUES(?,?,?,?)");
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement(sql.toString());
        for(Map.Entry<String,Object> entry:map.entrySet()){
            Object valuer = entry.getValue();
            String valuers = valuer.toString();
            stmt.bindString(1,entry.getKey());
            stmt.bindString(2,valuers);
            stmt.bindString(3,System.currentTimeMillis() + "");
            stmt.bindString(4,CACHE_TIME + "");
            stmt.execute();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        closeDataBase();
        return true;
    }

    /**
     * 根据key获取值
     * @param key 键
     * @return T
     */
    public String  getCacheByKey (String key){
        getReadableDatabase();
        Cursor cur = null;
        try{
            cur =  db.query(TimeCacheDbHelper.DATABASE_TABLE, null, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{ key }, null, null, null);
            if(cur != null && cur.getCount() > 0){
                cur.moveToNext();
                long cacheTime = cur.getLong(cur.getColumnIndex(TimeCacheDbHelper.CACHE_TIME_FIELD));
                long saveTime = Long.parseLong(cur.getString(cur.getColumnIndex(TimeCacheDbHelper.SAVE_TIME_FIELD)));
                long curTime = System.currentTimeMillis();
                String value = cur.getString(cur.getColumnIndex(TimeCacheDbHelper.VALUE_FIELD));
                if((curTime - saveTime) < cacheTime){
                    //在保存时间内
                    return value;
                }
                return null;
            }
            cur.close();
        }finally {
            if(cur != null){
                cur.close();
            }
        }
        closeDataBase();
        return null;
    }

    /**
     * 根据key获取值
     * @param key 键
     * @return T
     */
    public String  getCacheByKeyNoTime (String key){
        getReadableDatabase();
        Cursor cur = null;
        try {
            cur =  db.query(TimeCacheDbHelper.DATABASE_TABLE, null, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{ key }, null, null, null);
            if(cur != null && cur.getCount() > 0){
                cur.moveToNext();
                return cur.getString(cur.getColumnIndex(TimeCacheDbHelper.VALUE_FIELD));
            }
            cur.close();
        }finally {
            if(cur != null){
                cur.close();
            }
        }
        closeDataBase();
        return null;
    }

    /**
     * 批量清空keys 不关闭连接
     * @param keys 键集合
     */
    private void deleteBatch(Set<String> keys,SQLiteDatabase db){
        if(keys == null || keys.size() == 0){
            throw new IllegalArgumentException("keys值不能为空和大小不能为0");
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("delete from ")
                .append(TimeCacheDbHelper.DATABASE_TABLE)
                .append(" where ").append(TimeCacheDbHelper.KEY_FIELD)
                .append(" IN ")
                .append("(");
        for(String key : keys){
            buffer.append("\"" + key + "\"").append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        db.execSQL(buffer.toString());
    }

    /**
     * 批量清空keys
     * @param keys 键集合
     */
    public boolean deleteBatch(Set<String> keys){
        getReadableDatabase();
        deleteBatch(keys,db);
        closeDataBase();
        return true;
    }

    /**
     * 删除key对应的缓存，会关闭数据库连接
     * @param key 键
     * @return Boolean 是否删除成功
     */
    public boolean deleteCache(String key){
        getWritableDatabase();
        int status = db.delete(TimeCacheDbHelper.DATABASE_TABLE, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{key});
        closeDataBase();
        if(status == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 删除key对应的缓存,不会关闭数据库连接
     * @param key 键
     * @param db 数据库对象
     */
    private void deleteCache(String key, SQLiteDatabase db){
        db.delete(TimeCacheDbHelper.DATABASE_TABLE, TimeCacheDbHelper.KEY_FIELD + " = ?", new String[]{key});
    }

    /**
     * 是否存在Key
     * @param key 键
     * @return Boolean 是否存在
     */
    public boolean isExists(String key){
        if(getCacheByKey(key) == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 清空所有缓存
     */
    public void cleanCache(){
        getWritableDatabase();
        db.execSQL("delete from "+TimeCacheDbHelper.DATABASE_TABLE);
        closeDataBase();
    }

}
