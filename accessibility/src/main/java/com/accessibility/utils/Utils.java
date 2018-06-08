package com.accessibility.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.accessibility.app.MyApplication;

import java.util.List;

/**
 * Created by sc on 2017/6/20.
 */

public class Utils {

    public static void toast(String msg, boolean showLong) {
        Context context = MyApplication.getMyApplication();
        Toast.makeText(context, msg, showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void printNode(AccessibilityNodeInfo node, int top) {

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                String className = child.getClassName().toString();
                String resourceName = child.getViewIdResourceName();
                CharSequence text = child.getText();

                String logStr = className + " id:" + resourceName + " index:" + top;
                if (!TextUtils.isEmpty(text)) {
                    logStr += " text:" + text.toString();
                }

                Log.d(logStr);

                printNode(child, top + 1);
            }
        }
    }

    public static List<String> getIds(AccessibilityNodeInfo node, List<String> ids, String mode) {

        if (node == null) {
            return ids;
        }
        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                String resourceName = child.getViewIdResourceName();
                CharSequence text = child.getText();

                if (!TextUtils.isEmpty(text))
                    ids.add(text.toString());
                if (!TextUtils.isEmpty(resourceName))
                    ids.add(resourceName);

                getIds(child, ids, mode);
            }
        }

        return ids;
    }

    public static void openApp(Context context, String packageName) {

        PackageManager packageManager = context.getPackageManager();

        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);

        context.startActivity(launchIntentForPackage);
    }
}
