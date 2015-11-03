package com.example.zhaoxuanli.softwidgetdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private long start = 0;
    private long end = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        start = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        end = System.currentTimeMillis();
        System.out.println("View加载时间:"+(end-start)+" ms");
    }
}
