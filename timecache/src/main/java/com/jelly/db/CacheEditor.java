package com.jelly.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 批量操作
 * Created by JellyCai on 2018/3/8.
 */

public class CacheEditor implements Editor{

    private Map<String,Object> cacheMap;
    private TimeCacheDbUtil timeCacheDbUtil;
    private ThreadPoolExecutor executor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    public CacheEditor(TimeCacheDbUtil timeCacheDbUtil){
        cacheMap = new ConcurrentHashMap<>();
        this.timeCacheDbUtil = timeCacheDbUtil;
    }

    @Override
    public Editor addCache(String key,String value){
        cacheMap.put(key,value);
        return this;
    }

    @Override
    public Editor addCache(String key,Integer value){
        cacheMap.put(key,value);
        return this;
    }

    @Override
    public Editor addCache(String key,Double value){
        cacheMap.put(key,value);
        return this;
    }

    @Override
    public Editor addCache(String key,Float value){
        cacheMap.put(key,value);
        return this;
    }

    @Override
    public Editor addCache(String key,Object value){
        cacheMap.put(key,value);
        return this;
    }

    @Override
    public Editor removeCache(String key){
        cacheMap.remove(key);
        return this;
    }

    @Override
    public void apply() {
        synchronized (this){
            if(executor == null){
                executor = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(10));
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    commit();
                }
            });
        }
    }

    @Override
    public boolean commit(){
        return timeCacheDbUtil.addBatchCache(cacheMap);
    }

}
