package com.accessibility.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accessibility.R;
import com.accessibility.server.CommonAccessibility;
import com.accessibility.utils.AccessibilityUtil;

public class    MainActivity extends AppCompatActivity {

    private EditText input_msg;
    private EditText delay;
    private Button openServer;
    private boolean opened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_msg = (EditText) findViewById(R.id.input_msg);
        delay = (EditText) findViewById(R.id.delay);
        openServer = (Button) findViewById(R.id.openServer);
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.openServer:

                opened = AccessibilityUtil.checkAccessibilityEnabled(this, CommonAccessibility.class.getName());
                if (opened) {

                    Intent intent = new Intent(this,CommonAccessibility.class);
                    intent.putExtra("enabled",true);
                    startService(intent);

                } else {
                    AccessibilityUtil.goAccess(this);
                }

                break;
            case R.id.applist:
                Intent intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                break;
            case R.id.stopServer:

                intent = new Intent(this,CommonAccessibility.class);
                intent.putExtra("enabled",false);
                startService(intent);
                break;
        }
    }
}
