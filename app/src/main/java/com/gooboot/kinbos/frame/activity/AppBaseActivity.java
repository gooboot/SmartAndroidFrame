package com.gooboot.kinbos.frame.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gooboot.kinbos.frame.R;

public class AppBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_base);
    }
}
