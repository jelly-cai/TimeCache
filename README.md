# TimeCache
### 简介
TimeCache是在Android上基于SqlLite的键值对缓存工具
### 使用
    Gradle
        compile 'com.jelly:timecache:1.2.0
### 具体使用
#### 快速开始
##### 操作单个数据
>TimeCache提供了基本数据类型的put方法

    //获取TimeCache对象
    TimeCache timeCache = TimeCache.newTimeCache(getApplicationContext());
    //存储单个数据
    timeCache.put("key",i);
    //根据键取出数据
    timeCache.getInteger("key");
    //根据键取出数据，不判断时间
    timeCache.getIntegerNoTime("key");
##### 设置缓存时间
    //第二个参数为时间单位
    timeCache.setCacheTime(1, TimeUnit.SECONDS);
##### 判断缓存数据是否存在
    timeCache.isExists("key")
##### 删除某个缓存
    timeCache.remove("key")
##### 清空缓存
    timeCache.clearCache();
##### 批量操作数据
>批量操作数据，先通过TimeCache对象获取CacheEditor批量操作对象，通过CacheEditor的add方法添加缓存到内存中，调用CacheEditor的commit方法同步提交数据，或者使用apply方法异步提交数据

    //获取CacheEditor操作对象
    CacheEditor editor = timeCache.getEditor();
    //添加数据，数据有多种类型
    editor.addCache("key1","value1");
    editor.addCache("key2ppip",3);
    editor.addCache("key3",1.12);
    editor.addCache("key4",1.22f);
    //同步提交数据(异步提交数据:editor.apply();)
    editor.commit();

