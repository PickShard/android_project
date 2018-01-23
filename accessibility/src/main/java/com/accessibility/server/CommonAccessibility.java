package com.accessibility.server;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.QQHandler;
import com.accessibility.R;
import com.accessibility.utils.Log;
import com.accessibility.utils.MyApplication;
import com.accessibility.utils.Utils;


import static com.accessibility.utils.Utils.printNode;
import static com.accessibility.utils.Utils.toast;

/**
 * @描述： 请填写描述信息
 * @作者： SuCheng
 * @时间： 2017/6/12 15:44
 */


public class CommonAccessibility extends AccessibilityService {

    private QQHandler qqHandler;
    private View floatview;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (TextUtils.equals(intent.getStringExtra("Action"), "stop")) {
            MyApplication.setEnabled(false);
            onDestroy();
        } else {
            MyApplication.setEnabled(true);
            Utils.toast("服务开启成功", false);

            final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            final WindowManager.LayoutParams wl = new WindowManager.LayoutParams();

            wl.type = WindowManager.LayoutParams.TYPE_PHONE;
            wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //wl.gravity = Gravity.RIGHT;

            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wl.format = PixelFormat.RGBA_8888;

            wl.windowAnimations = android.R.anim.fade_in;



            final Point point = new Point();
            wm.getDefaultDisplay().getRealSize(point);

            final View floatview = View.inflate(this, R.layout.float_window_layout, null);

            floatview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            v.animate().scaleX(1.5f).scaleY(1.5f).setDuration(300).start();
                            wm.updateViewLayout(floatview,wl);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float rawY = event.getRawY()- point.y/2 - v.getHeight()/2;
                            float rawX = point.x/2 - event.getRawX();
                            wl.y = (int)rawY;
                            wl.x = -(int)rawX;
                            wm.updateViewLayout(floatview,wl);
                            break;
                        default:
                            v.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                    }

                    return false;
                }
            });

            wm.addView(floatview, wl);
        }
//        AccessibilityServiceInfo info = getServiceInfo();
//        info.packageNames = new String[]{"com.tencent.mobileqq"};
//        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
//        setServiceInfo(info);
        return super.onStartCommand(intent, flags, startId);
    }

    AccessibilityNodeInfo source;
    int accpetEvent;    //下一个接受的事件


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!MyApplication.isEnabled()) {
            return;
        }

        Log.d(AccessibilityEvent.eventTypeToString(event.getEventType()));

//        if (accpetEvent !=0 && event.getEventType() != accpetEvent) {
//            return;
//        }

        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            Utils.toast("无法获取窗口组件,请重启目标程序", false);
            return;
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            printNode(root, 1);
            //findNode(root, 1);
        }

        if (TextUtils.equals(event.getPackageName().toString(), "com.tencent.mobileqq")) {
            if (qqHandler == null) {
                qqHandler = new QQHandler();
            }
            qqHandler.onEvent(this, root);
        }
    }

    public void findNode(AccessibilityNodeInfo node, int top) {

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                if (TextUtils.equals(child.getViewIdResourceName(), "com.tencent.mobileqq:id/qb_troop_list_view")) {


                }
                findNode(child, top + 1);
            }
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
