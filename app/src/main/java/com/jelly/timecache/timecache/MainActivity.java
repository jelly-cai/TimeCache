package com.jelly.timecache.timecache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jelly.timecache.TimeCache;


public class MainActivity extends AppCompatActivity {

    private Button saveInteger;
    private Button getInteger;

    private Button saveDouble;
    private Button getDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveInteger = findViewById(R.id.btn_save_integer);
        getInteger = findViewById(R.id.btn_get_integer);

        saveDouble = findViewById(R.id.btn_save_double);
        getDouble = findViewById(R.id.btn_get_double);

        saveInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeCache.getInstance(MainActivity.this).put("test",1);
            }
        });

        getInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, TimeCache.getInstance(MainActivity.this).getValue("test",Integer.class) + "",Toast.LENGTH_LONG).show();
            }
        });

        saveDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeCache.getInstance(MainActivity.this).put("test1",1.12);
            }
        });

        getDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,TimeCache.getInstance(MainActivity.this).getValue("test1",Double.class) + "",Toast.LENGTH_LONG).show();

            }
        });

    }

}
