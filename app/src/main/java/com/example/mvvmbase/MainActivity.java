package com.example.mvvmbase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hdl.CrashExceptioner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashExceptioner.init(this);

    }
}