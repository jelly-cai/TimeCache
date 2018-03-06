package com.jelly.timecache.timecache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jelly.timecache.TimeCache;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Button saveInteger;
    private Button getInteger;

    private Button saveDouble;
    private Button getDouble;

    private Button saveObject;
    private Button getObject;

    private Button saveTime;
    private Button getTime;

    private Button saveExists;
    private Button getExists;

    private Button btnClearCache;

    private  int i = 1;
    private TimeCache timeCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveInteger = findViewById(R.id.btn_save_integer);
        getInteger = findViewById(R.id.btn_get_integer);

        saveDouble = findViewById(R.id.btn_save_double);
        getDouble = findViewById(R.id.btn_get_double);

        saveObject = findViewById(R.id.btn_save_object);
        getObject = findViewById(R.id.btn_get_object);

        saveTime = findViewById(R.id.btn_save_time);
        getTime = findViewById(R.id.btn_get_time);

        saveExists = findViewById(R.id.btn_save_exists);
        getExists = findViewById(R.id.btn_get_exists);

        btnClearCache = findViewById(R.id.btn_clear_cache);

        timeCache = TimeCache.getTimeCache(getApplicationContext());

        saveInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCache.put("test",i);
                i++;
            }
        });

        getInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ToastUtils.showMessage(MainActivity.this, timeCache.getInteger("test") + "");
            }
        });

        saveDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCache.put("test1",1.12);
            }
        });

        getDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showMessage(MainActivity.this,timeCache.getDouble("test1") + "");
            }
        });

        saveObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Man man = new Man();
                man.setName("JellyCai");
                man.setAge(23);
                timeCache.put("test2",man);
            }
        });

        getObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Man man = timeCache.get("test2",Man.class);
                ToastUtils.showMessage(MainActivity.this,"man-name:" + man.getName() + "man-age:" + man.getAge());
            }
        });

        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCache.setCacheTime(1, TimeUnit.SECONDS);
                timeCache.put("test3","JellyCai");
            }
        });

        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showMessage(MainActivity.this,timeCache.getString("test3"));
            }
        });

        saveExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!timeCache.isExists("test4",String.class)){
                    timeCache.put("test4","test4");
                }
            }
        });

        getExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timeCache.isExists("test4",String.class)){
                    ToastUtils.showMessage(MainActivity.this,timeCache.getString("test4"));
                }
            }
        });

        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCache.clearCache();
            }
        });

    }

}
