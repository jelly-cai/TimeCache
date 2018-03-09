package com.jelly.db;


/**
 * Created by JellyCai on 2018/3/9.
 */

public interface Editor {


    /**
     * 添加缓存
     * @param key
     * @param value 字符串
     * @return
     */
    Editor addCache(String key,String value);

    /**
     * 添加缓存
     * @param key
     * @param value 整型
     * @return
     */
    Editor addCache(String key,Integer value);

    /**
     * 添加缓存
     * @param key
     * @param value Double
     * @return
     */
    Editor addCache(String key,Double value);

    /**
     * 添加缓存
     * @param key
     * @param value Float
     * @return
     */
    Editor addCache(String key,Float value);

    /**
     * 添加缓存
     * @param key
     * @param value 对象
     * @return
     */
    Editor addCache(String key,Object value);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    Editor removeCache(String key);

    /**
     * 同步提交本次缓存
     * @return
     */
    boolean commit();

    /**
     * 异步提交本次缓存
     */
    void apply();

}
