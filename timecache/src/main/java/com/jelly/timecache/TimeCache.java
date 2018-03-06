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

    public static TimeCache getTimeCache(Context context){
        return new TimeCache(context);
    }

    private TimeCache(Context context){
        if(timeCacheDbUtil == null){
            timeCacheDbUtil = new TimeCacheDbUtil(context);
        }
    }

    public long put(String key,String value){
        return timeCacheDbUtil.addCache(key,value);
    }

    public long put(String key,int value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,double value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,float value){
        return put(key,String.valueOf(value));
    }

    public long put(String key,Object o){
        return timeCacheDbUtil.addCache(key,o);
    }

    public <T> T getValue(String key,Class<T> c){
        return timeCacheDbUtil.getCacheByKey(key,c);
    }

}
