package com.jelly.timecache;

import android.content.Context;

import com.jelly.db.TimeCacheDbUtil;

/**
 * 时间缓存操作
 * Created by JellyCai on 2018/3/5.
 */

public class TimeCache {

    /**
     * 数据库操作
     */
    private TimeCacheDbUtil timeCacheDbUtil;
    private static TimeCache timeCache;

    public static TimeCache getInstance(Context context){
        if(timeCache == null){
            timeCache = new TimeCache(context);
        }
        return timeCache;
    }

    private TimeCache(Context context){
        if(timeCacheDbUtil == null){
            timeCacheDbUtil = new TimeCacheDbUtil(context);
        }
    }

    public void put(String key,String value){
        timeCacheDbUtil.addCache(key,value);
    }

    public void put(String key,int value){
        put(key,String.valueOf(value));
    }

    public void put(String key,double value){
        put(key,String.valueOf(value));
    }

    public void put(String key,float value){
        put(key,String.valueOf(value));
    }

    public <T> T getValue(String key,Class<T> c){
        return (T) timeCacheDbUtil.getCacheByKey(key,c);
    }

}
