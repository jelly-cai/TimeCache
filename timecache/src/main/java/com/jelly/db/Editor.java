package com.jelly.db;


/**
 * Created by JellyCai on 2018/3/9.
 */

public interface Editor {


    /**
     * 添加缓存
     * @param key 键
     * @param value 字符串
     * @return Editor
     */
    Editor addCache(String key,String value);

    /**
     * 添加缓存
     * @param key 键
     * @param value 整型
     * @return Editor
     */
    Editor addCache(String key,Integer value);

    /**
     * 添加缓存
     * @param key 键
     * @param value Double
     * @return Editor
     */
    Editor addCache(String key,Double value);

    /**
     * 添加缓存
     * @param key 键
     * @param value Float
     * @return Editor
     */
    Editor addCache(String key,Float value);

    /**
     * 添加缓存
     * @param key 键
     * @param value 对象
     * @return Editor
     */
    Editor addCache(String key,Object value);

    /**
     * 删除缓存
     * @param key 键
     * @return Editor
     */
    Editor removeCache(String key);

    /**
     * 同步提交本次缓存
     * @return Boolean 是否成功
     */
    boolean commit();

    /**
     * 异步提交本次缓存
     */
    void apply();

}
