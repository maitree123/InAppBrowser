package com.example.inappbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BasicActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView captionField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        captionField = (TextView) findViewById(R.id.captionField);
        captionField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BasicActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
