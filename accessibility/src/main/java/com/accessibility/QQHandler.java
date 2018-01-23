package com.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.server.CommonAccessibility;
import com.accessibility.utils.AccessibilityUtil;
import com.accessibility.utils.Log;
import com.accessibility.utils.MyApplication;
import com.accessibility.utils.Utils;

import java.util.List;


/**
 * Created by sc on 2017/6/20.
 */

public class QQHandler {
    private CommonAccessibility mServer;
    public static boolean accpetEvent = true;
    public static int delay = 1500;
    public static String input = "测试";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.obj != null) {
                AccessibilityNodeInfo obj = (AccessibilityNodeInfo) msg.obj;
                obj.performAction(msg.arg1);
            } else {
                mServer.performGlobalAction(msg.what);
            }
            accpetEvent = true;
        }
    };

    private List<AccessibilityNodeInfo> nodeInfos;
    private boolean scrolled;
    private int count;

    public void onEvent(CommonAccessibility server, AccessibilityNodeInfo root) {

        if (!accpetEvent) {
            return;
        }

        accpetEvent = false;

        if (mServer == null) {
            mServer = server;
        }

//        nodeInfos = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/recent_chat_list");  //消息列表
//
//        if (!nodeInfos.isEmpty()) {
//
//
//            accpetEvent = true;
//            return;
//        }
//        nodeInfos = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/elv_buddies");  //好友列表
//
//        if (!nodeInfos.isEmpty()) {
//            if (nodeInfos.get(0).isVisibleToUser()){
//                findNode(root,1);
//                return;
//            }
//        }

        nodeInfos = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/qb_troop_list_view");  //群列表

        if (!nodeInfos.isEmpty()) {

            if (!nodeInfos.get(0).isVisibleToUser()) {
                findNode(root, 1);
                return;
            }

            List<AccessibilityNodeInfo> item = nodeInfos.get(0).findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/text1");

            if (!item.isEmpty()) {

                if (item.size() - 1 == count && !item.get(count).isVisibleToUser()) {
                    Log.d("scrolled");

                    sendMessage(nodeInfos.get(0), AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    count = 0;
                } else {

                    sendMessage(item.get(count).getParent(), AccessibilityNodeInfo.ACTION_CLICK);
                    count++;
                }

            } else {
                int childCount = nodeInfos.get(0).getChildCount();
                sendMessage(nodeInfos.get(0).getChild(childCount - 1), AccessibilityNodeInfo.ACTION_CLICK);
            }
            return;
        }

        nodeInfos = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/listView1");   //聊天窗口

        if (!nodeInfos.isEmpty()) {
            Utils.printNode(nodeInfos.get(0), 1);

            List<AccessibilityNodeInfo> item = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/input");

            if (!item.isEmpty()) {
                AccessibilityUtil.inputText(mServer, item.get(0), input);

                List<AccessibilityNodeInfo> sendButtons = root.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/fun_btn");
                if (!sendButtons.isEmpty()) {
                    //sendButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK); //发送消息
                }

                handler.sendEmptyMessageDelayed(AccessibilityService.GLOBAL_ACTION_BACK, delay);
            }
            return;
        }

        nodeInfos = root.findAccessibilityNodeInfosByViewId("android:id/tabhost");   //聊天窗口

        if (!nodeInfos.isEmpty()) {
            Utils.printNode(nodeInfos.get(0), 1);

            List<AccessibilityNodeInfo> item = root.findAccessibilityNodeInfosByViewId("android:id/tabs");

            if (!item.isEmpty()) {

                AccessibilityNodeInfo info = item.get(0);
                AccessibilityNodeInfo child = info.getChild(1);
                sendMessage(child, AccessibilityNodeInfo.ACTION_CLICK);
            }
            return;
        }

        accpetEvent = true;
    }

    public void sendMessage(AccessibilityNodeInfo info, int arg) {
        Message message = handler.obtainMessage();
        message.arg1 = arg;
        message.obj = info;
        handler.sendMessageDelayed(message, delay);
    }

    public void findNode(AccessibilityNodeInfo node, int top) {

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                if (TextUtils.equals(child.getClassName().toString(), "android.widget.HorizontalScrollView")) {
                    sendMessage(child.getChild(1), AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                }

                findNode(child, top + 1);
            }
        }
    }
}
