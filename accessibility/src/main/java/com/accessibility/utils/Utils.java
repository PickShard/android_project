package com.accessibility.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * Created by sc on 2017/6/20.
 */

public class Utils {

    public static void toast(String msg,boolean showLong){
        Context context = MyApplication.getMyApplication();
        Toast.makeText(context,msg,showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void printNode(AccessibilityNodeInfo node, int top) {

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {
                if ( child.getText() != null) {
                    Log.d(child.getClassName().toString() + " id:" + child.getViewIdResourceName() + " index:" + (top) +" text:"+child.getText().toString());
                }else {
                    Log.d(child.getClassName().toString() + " " + child.getViewIdResourceName() + " index:" + (top));
                }
                printNode(child, top + 1);
            }
        }
    }

    public static void openApp(Context context,String packageName) {

        PackageManager packageManager = context.getPackageManager();

        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);

        context.startActivity(launchIntentForPackage);
    }

}
