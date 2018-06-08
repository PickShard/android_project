package com.accessibility.server;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.qq.QQHandler;
import com.accessibility.R;
import com.accessibility.sms.SMSHandler;
import com.accessibility.wx.WXHandler;
import com.accessibility.utils.Constant;
import com.accessibility.utils.Log;
import com.accessibility.app.MyApplication;
import com.accessibility.utils.Utils;


import java.util.ArrayList;
import java.util.List;

import static com.accessibility.utils.Utils.toast;

/**
 * @描述： 请填写描述信息
 * @作者： SuCheng
 * @时间： 2017/6/12 15:44
 */


public class CommonAccessibility extends AccessibilityService {

    private static boolean mEnabled = false;


    private QQHandler qqHandler;
    private WXHandler wxHandler;
    private SMSHandler smsHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("onStartCommand");
        setEnabled(intent.getBooleanExtra("enabled", false));

        AccessibilityServiceInfo info = getServiceInfo();
        info.packageNames = new String[]{Constant.PACKAGENAME_QQ,Constant.PACKAGENAME_MMS};
        setServiceInfo(info);

        return super.onStartCommand(intent, flags, startId);
    }

    public static void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (mEnabled) {
            Utils.toast("服务已开启", false);
        } else {
            Utils.toast("服务已关闭", false);
        }
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(AccessibilityEvent.eventTypeToString(event.getEventType()));

        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            return;
        }

        Utils.printNode(root, 0);

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED || !mEnabled) {
            return;
        }

        List<String> ids = Utils.getIds(root, new ArrayList<String>(), Constant.MODE_ALL);


        if (TextUtils.equals(event.getPackageName().toString(), getString(R.string.qq_package))) {

            if (qqHandler == null) {
                qqHandler = new QQHandler(this);
            }
            qqHandler.onEvent(root, ids);
        } else if (TextUtils.equals(event.getPackageName().toString(), getString(R.string.wx_package))) {
            if (wxHandler == null) {
                wxHandler = new WXHandler(this);
            }
            wxHandler.onEvent(root, ids);
        } else if (TextUtils.equals(event.getPackageName().toString(), Constant.PACKAGENAME_MMS)) {
            if (smsHandler == null) {
                smsHandler = new SMSHandler(this);
            }
            smsHandler.onEvent(root, ids);
        }

    }


    @Override
    public boolean onUnbind(Intent intent) {
        Utils.toast("辅助功能已关闭", false);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("==============", "onDestroy");
        toast("服务已经停止", false);
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("==============", "onRebind");
        super.onRebind(intent);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d("==============", "onKeyEvent");
        return super.onKeyEvent(event);
    }

    @Override
    protected boolean onGesture(int gestureId) {
        Log.d("==============", "onGesture");
        return super.onGesture(gestureId);

    }


    @Override
    public void onInterrupt() {

    }
}
