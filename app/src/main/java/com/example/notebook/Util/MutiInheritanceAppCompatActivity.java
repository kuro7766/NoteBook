package com.example.notebook.Util;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MutiInheritanceAppCompatActivity extends AppCompatActivity{
    public TextView TextView(@IdRes int id){
        return findViewById(id);
    }
//    Button
}
