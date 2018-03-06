package com.jelly.timecache.timecache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jelly.timecache.TimeCache;


public class MainActivity extends AppCompatActivity {

    private Button saveInteger;
    private Button getInteger;

    private Button saveDouble;
    private Button getDouble;

    private Button saveObject;
    private Button getObject;

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
               ToastUtils.showMessage(MainActivity.this, TimeCache.getTimeCache(MainActivity.this).getValue("test",Integer.class) + "");
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
                ToastUtils.showMessage(MainActivity.this,TimeCache.getTimeCache(MainActivity.this).getValue("test1",Double.class) + "");
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
                Man man = TimeCache.getTimeCache(MainActivity.this).getValue("test2",Man.class);
                ToastUtils.showMessage(MainActivity.this,"man-name:" + man.getName() + "man-age:" + man.getAge());
            }
        });

    }

}
