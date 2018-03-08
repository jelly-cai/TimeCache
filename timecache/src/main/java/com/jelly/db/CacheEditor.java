package com.jelly.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/3/8.
 */

public class CacheEditor {

    private Map<String,Object> cacheMap;
    private TimeCacheDbUtil timeCacheDbUtil;

    public CacheEditor(TimeCacheDbUtil timeCacheDbUtil){
        cacheMap = new ConcurrentHashMap<>();
        this.timeCacheDbUtil = timeCacheDbUtil;
    }

    public void addCache(String key,String value){
        cacheMap.put(key,value);
    }

    public void addCache(String key,Integer value){
        cacheMap.put(key,value);
    }

    public void addCache(String key,Double value){
        cacheMap.put(key,value);
    }

    public void addCache(String key,Float value){
        cacheMap.put(key,value);
    }

    public void addCache(String key,Object value){
        cacheMap.put(key,value);
    }

    public void removeCache(String key){
        cacheMap.remove(key);
    }

    /**
     * 提交本次缓存
     */
    public void commit(){
        timeCacheDbUtil.addBatchCache(cacheMap);
    }

}
