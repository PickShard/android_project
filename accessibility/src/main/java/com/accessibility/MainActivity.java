package com.accessibility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accessibility.server.CommonAccessibility;
import com.accessibility.utils.AccessibilityUtil;
import com.accessibility.utils.Constant;
import com.accessibility.utils.Log;
import com.accessibility.utils.Utils;

import java.util.List;

import static com.accessibility.utils.Utils.openApp;

public class MainActivity extends AppCompatActivity {

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

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.openServer:

                opened = AccessibilityUtil.checkAccessibilityEnabled(this, CommonAccessibility.class.getName());
                if (opened) {

                    if (TextUtils.isEmpty(delay.getText().toString())) {
                        Utils.toast(delay.getHint().toString(), false);
                    } else if (TextUtils.isEmpty(input_msg.getText().toString())) {
                        Utils.toast(input_msg.getHint().toString(), false);
                    } else {
                        QQHandler.delay = Integer.valueOf(delay.getText().toString());
                        QQHandler.input = input_msg.getText().toString();
                        QQHandler.accpetEvent = true;
                        Intent intent = new Intent(this, CommonAccessibility.class);
                        intent.putExtra("Action","start");
                        startService(intent);

                        Utils.openApp(this,Constant.PACKAGENAME_QQ);
                    }
                } else {
                    AccessibilityUtil.goAccess(this);
                }

                break;
            case R.id.applist:
                Intent intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                break;
            case R.id.stopServer:
                Log.d("stopService");
                intent = new Intent(this, CommonAccessibility.class);
                intent.putExtra("Action","stop");
                startService(intent);
                break;
        }
    }
}
