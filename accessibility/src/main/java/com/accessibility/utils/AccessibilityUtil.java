package com.accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

public class AccessibilityUtil {

    public static AccessibilityManager getAccessibilityManager(Context context) {
        return (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }


    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    public static boolean checkAccessibilityEnabled(Context context, String serviceName) {

        List<AccessibilityServiceInfo> accessibilityServices = getAccessibilityManager(context)
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getResolveInfo().serviceInfo.name.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前往开启辅助服务界面
     */
    public static void goAccess(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static List<AccessibilityNodeInfo> getViewsByType(AccessibilityNodeInfo node,String className,List<AccessibilityNodeInfo> nodes) {


        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                String resourceName = child.getClassName().toString();

                if (TextUtils.equals(resourceName, className)) {
                    nodes.add(child);
                }

                getViewsByType(child, className,nodes);
            }
        }

        return nodes;
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    /**
     * 模拟返回操作
     */
    public static void performBackClick(AccessibilityService service) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(GLOBAL_ACTION_BACK);
    }

    /**
     * 模拟下滑操作
     */
    public static void performScrollBackward(AccessibilityService service) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    /**
     * 模拟上滑操作
     */
    public static void performScrollForward(AccessibilityService service) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        service.performGlobalAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }


    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @return View
     */
    public static AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo root, String text) {

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            return nodeInfoList.get(0);
        }
        return null;
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo findViewByID(AccessibilityNodeInfo root, String id) {

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            return nodeInfoList.get(0);
        }
        return null;
    }

    public static void clickTextViewByText(AccessibilityService service, String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        performAction(nodeInfoList);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void clickTextViewByID(AccessibilityService service, String id) {

        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        performAction(nodeInfoList);
    }


    public static void clickViewByID(AccessibilityNodeInfo root, String id) {

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByViewId(id);
        performAction(nodeInfoList);
    }

    public static void clickViewByText(AccessibilityNodeInfo root, String text) {

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByText(text);
        performAction(nodeInfoList);
    }

    public static void clickItemByID(AccessibilityNodeInfo root, String id, int item) {

        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByViewId(id);

        if (nodeInfoList != null && !nodeInfoList.isEmpty() && nodeInfoList.size() > item) {
            nodeInfoList.get(item).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    public static void performAction(List<AccessibilityNodeInfo> nodeInfoList) {

        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }


    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    public static void inputText(Context context, AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }


}
