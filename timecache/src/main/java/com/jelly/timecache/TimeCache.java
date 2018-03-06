package com.jelly.timecache;

import android.content.Context;
import android.text.TextUtils;

import com.jelly.db.TimeCacheDbUtil;

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

    public static TimeCache getTimeCache(Context context){
        return new TimeCache(context);
    }

    private TimeCache(Context context){
        if(timeCacheDbUtil == null){
            timeCacheDbUtil = new TimeCacheDbUtil(context);
        }
    }

    /**
     * 存数据
     * @param key
     * @param value
     * @return
     */
    public long put(String key,String value){
        return timeCacheDbUtil.addCache(key,value);
    }

    public long put(String key,Integer value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,Double value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,Float value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,Object o){
        return timeCacheDbUtil.addCache(key,o);
    }

    /**
     * 取数据
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> c){
        return timeCacheDbUtil.getCacheByKey(key,c);
    }

    /**
     * 取出字符串缓存数据
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getString(String key,String defaultValue){
        String result = get(key,String.class);
        return TextUtils.isEmpty(result) ? defaultValue : result;
    }

    /**
     * 取出字符串缓存数据，默认值为""
     * @param key
     * @return
     */
    public String getString(String key){
        return getString(key,"");
    }

    /**
     * 取出整型数据
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Integer getInteger(String key,Integer defaultValue){
        Integer result = get(key,Integer.class);
        return result == null ? defaultValue : result;
    }

    /**
     * 取出整型数据
     * @param key
     * @return
     */
    public Integer getInteger(String key){
        return getInteger(key,0);
    }

    /**
     * 获取double数据
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Double getDouble(String key,double defaultValue){
        Double result = get(key,Double.class);
        return result == null ? defaultValue : result;
    }

    /**
     * 获得Double数据
     * @param key
     * @return
     */
    public Double getDouble(String key){
        return getDouble(key,0);
    }

    /**
     * 获得float数据
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Float getFloat(String key,float defaultValue){
        Float result = get(key,Float.class);
        return result == null ? defaultValue : result;
    }

    /**
     * 获得Float数据
     * @param key
     * @return
     */
    public float getFloat(String key){
        return getFloat(key,0);
    }

    /**
     * 删除数据
     * @param key
     * @return
     */
    public long remove(String key){
        return timeCacheDbUtil.deleteCache(key);
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
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    public <T> boolean isExists(String key,Class<T> c){
        return timeCacheDbUtil.isExists(key,c);
    }

    /**
     * 设置缓存时间，单位为天
     * @param time
     */
    public void setCacheTime(long time){
        setCacheTime(time,TimeUnit.DAYS);
    }

}
