package com.example.inappbrowser;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SecondFragment hello = new SecondFragment();
        fragmentTransaction.add(R.id.framelayout, hello, "HELLO");
        fragmentTransaction.commit();
    }



}
