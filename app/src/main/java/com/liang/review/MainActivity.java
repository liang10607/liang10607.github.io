package com.liang.review;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.LruCache;

import com.liang.review.ui.main.MainFragment;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        String ss = "";
        LruCache<String,String>  lruCache =new LruCache<>(40);
        HashMap hashMap =new HashMap();
        LinkedHashMap linkedHashMap =new LinkedHashMap<>();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}