package com.jelly.timecache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jelly.db.CacheEditor;
import com.jelly.db.TimeCacheDbUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 时间缓存操作
 * Created by JellyCai on 2018/3/5.
 */

public class TimeCache {

    /**
     * 数据库操作
     */
    private TimeCacheDbUtil timeCacheDbUtil;
    private CacheEditor editor;

    public static TimeCache newTimeCache(Context context){
        return new TimeCache(context);
    }

    private TimeCache(Context context){
        if(timeCacheDbUtil == null){
            timeCacheDbUtil = new TimeCacheDbUtil(context);
        }
    }

    /**
     * 获得批量操作缓存对象
     * @return CacheEditor
     */
    public CacheEditor getEditor(){
        if(editor == null){
            editor = new CacheEditor(timeCacheDbUtil);
        }
        return editor;
    }

    /**
     * 存数据
     * @param key 键
     * @param value 值
     * @return boolean 是否成功
     */
    public boolean put(String key,String value){
        return timeCacheDbUtil.addCache(key,value);
    }

    public boolean put(String key,Integer value){
        return put(key,String.valueOf(value));
    }

    public boolean put(String key,Double value){
        return put(key,String.valueOf(value));
    }

    public boolean put(String key,Float value){
        return put(key,String.valueOf(value));
    }

    /**
     * 取数据
     * @param key 键
     * @return T
     */
    private String get(String key){
        return timeCacheDbUtil.getCacheByKey(key);
    }

    /**
     * 取出字符串缓存数据
     * @param key 键
     * @param defaultValue 默认值
     * @return String
     */
    public String getString(String key,String defaultValue){
        String result = get(key);
        return TextUtils.isEmpty(result) ? defaultValue : result;
    }

    /**
     * 取出字符串缓存数据，默认值为""
     * @param key 键
     * @return String
     */
    public String getString(String key){
        return getString(key,"");
    }

    /**
     * 取出整型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return Integer
     */
    public Integer getInteger(String key,Integer defaultValue){
        String result = get(key);
        if(TextUtils.isEmpty(result)){
            return defaultValue;
        }else{
            return Integer.parseInt(get(key));
        }
    }

    /**
     * 取出整型数据
     * @param key 键
     * @return Integer
     */
    public Integer getInteger(String key){
        return getInteger(key,0);
    }

    /**
     * 获取double数据
     * @param key 键
     * @param defaultValue 默认值
     * @return Double
     */
    public Double getDouble(String key,double defaultValue){
        String result = get(key);
        if(TextUtils.isEmpty(result)){
            return defaultValue;
        }else{
            return Double.parseDouble(get(key));
        }
    }

    /**
     * 获得Double数据
     * @param key 键
     * @return Double
     */
    public Double getDouble(String key){
        return getDouble(key,0);
    }

    /**
     * 获得float数据
     * @param key 键
     * @param defaultValue 默认值
     * @return Float
     */
    public Float getFloat(String key,float defaultValue){
        String result = get(key);
        if(TextUtils.isEmpty(result)){
            return defaultValue;
        }else{
            return Float.parseFloat(result);
        }
    }

    /**
     * 获得Float数据
     * @param key 键
     * @return Float
     */
    public float getFloat(String key){
        return getFloat(key,0);
    }

    /**
     * 删除缓存数据
     * @param key 键
     * @return Boolean是否删除成功
     */
    public boolean remove(String key){
        return timeCacheDbUtil.deleteCache(key);
    }

    /**
     * 批量删除缓存
     * @param keys 键集合
     */
    public boolean remove(Set<String> keys){
        return timeCacheDbUtil.deleteBatch(keys);
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        timeCacheDbUtil.cleanCache();
    }


    /**
     * 设置缓存时间
     * @param time 缓存时间
     * @param timeUnit {@link TimeUnit}(TimeUnit.DAYS,TimeUnit.HOURS,TimeUnit.MINUTES,TimeUnit.SECONDS,TimeUnit.MILLISECONDS)
     */
    public void setCacheTime(long time,Enum timeUnit){
        if(timeUnit == TimeUnit.DAYS){
            timeCacheDbUtil.setCacheTime(time * 24 * 60 * 60 * 1000);
        }else if(timeUnit == TimeUnit.HOURS){
            timeCacheDbUtil.setCacheTime(time * 60 * 60 * 1000);
        }else if(timeUnit == TimeUnit.MINUTES){
            timeCacheDbUtil.setCacheTime(time * 60 * 1000);
        }else if(timeUnit == TimeUnit.SECONDS){
            timeCacheDbUtil.setCacheTime(time * 1000);
        }else if(timeUnit == TimeUnit.MILLISECONDS){
            timeCacheDbUtil.setCacheTime(time);
        }
    }

    /**
     * key是否存在
     * @param key 键
     * @return Boolean 是否存在
     */
    public boolean isExists(String key){
        return timeCacheDbUtil.isExists(key);
    }

    /**
     * 设置缓存时间，单位为天
     * @param time 缓存时间
     */
    public void setCacheTime(long time){
        setCacheTime(time,TimeUnit.DAYS);
    }

}
