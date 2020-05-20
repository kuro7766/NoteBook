package com.example.notebook.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.notebook.Entity.Processor;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context context;
    protected static Processor processor=new Processor();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
    }
    protected void initView(){

    }
    protected void initData(){

    }
}
