package com.accessibility.wx;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.TaskStack;
import com.accessibility.server.CommonAccessibility;
import com.accessibility.utils.AccessibilityUtil;
import com.accessibility.utils.Constant;
import com.accessibility.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by sc on 2017/6/20.
 */

public class WXHandler {

    private WXStatusChecker wxStatusChecher;
    private List<Integer> taskList = new ArrayList<>();

    public static int mDelay = 1500;
    private int status;
    private AccessibilityNodeInfo lastRoot;
    private List<String> mIds;
    private long eventTime;

    private Map<String, Integer> listMap = new ArrayMap<>();
    private boolean canNext = true;


    public WXHandler(CommonAccessibility server) {
        wxStatusChecher = WXStatusChecker.getInstance();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 10 && lastRoot != null && mIds != null) {
                canNext = true;
                handleEvent(lastRoot, mIds);
            }
        }
    };

    public void onEvent(AccessibilityNodeInfo root, List<String> ids) {

        lastRoot = root;
        mIds = ids;

        if (canNext){
            handleEvent(lastRoot,mIds);
        }
    }

    private void handleEvent(AccessibilityNodeInfo root, List<String> ids) {
        canNext = false;

        status = wxStatusChecher.checkStatus(ids);

        conduct(root, status, taskList.get(0));
    }

    public void conduct(AccessibilityNodeInfo root, int status, int task) {

        if (task >= TaskStack.TASK_SEND_COLLECT) {
            action(root, status);
        }

    }


    public void action(AccessibilityNodeInfo root, int status) {

        if (status == WXStatusChecker.STATUS_CHAT) {

            AccessibilityUtil.clickViewByID(root, Constant.WX_ID_MORE);
            handler.sendEmptyMessageDelayed(10, 1500);

        } else if (status == WXStatusChecker.STATUS_CHAT_MORE) {

            AccessibilityNodeInfo nodeInfo = AccessibilityUtil.findViewByText(root, "我的收藏");
            if (nodeInfo == null) return;
            nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            handler.sendEmptyMessageDelayed(10, 1500);
            Integer scrollCout = listMap.get(Constant.WX_COLLECT_LISTVIEW_SCROLL_COUT);
            listMap.put(Constant.WX_LISTVIEW_SCROLL_COUT_TEMP,scrollCout);

        } else if (status == WXStatusChecker.STATUS_COLLECT) {

            AccessibilityNodeInfo viewByID = AccessibilityUtil.findViewByID(root, Constant.WX_ID_COLLECT_LISTVIEW);
            handleListView(viewByID);

        } else if (status == WXStatusChecker.STATUS_COLLECT_DIALOG) {

            AccessibilityUtil.clickViewByID(root, Constant.WX_ID_SEND);
            handler.sendEmptyMessageDelayed(10, 3000);

        } else if (status == WXStatusChecker.STATUS_CHAT_KEYBORD) {

            AccessibilityUtil.clickViewByID(root, Constant.WX_ID_CHAT_KEYBORD);
            handler.sendEmptyMessageDelayed(10, 1000);
        }

    }

    private AccessibilityNodeInfo lastClickChild;
    private AccessibilityNodeInfo lastItem;
    public void handleListView(AccessibilityNodeInfo listview) {

        Integer position = listMap.get(Constant.WX_COLLECT_LISTVIEW_POSITON);
        Integer scrollCout = listMap.get(Constant.WX_LISTVIEW_SCROLL_COUT_TEMP);

        int childCount = listview.getChildCount();

        if (position == null) position = 0;
        if (scrollCout == null) scrollCout = 0;

        if (scrollCout > 0) {

            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            listMap.put(Constant.WX_LISTVIEW_SCROLL_COUT_TEMP, --scrollCout);
            sendAction(10, 1500);

        } else if (position >= childCount) {
            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            listMap.put(Constant.WX_COLLECT_LISTVIEW_POSITON, 0);

            scrollCout = listMap.get(Constant.WX_COLLECT_LISTVIEW_SCROLL_COUT);
            if (scrollCout == null) scrollCout = 0;
            listMap.put(Constant.WX_COLLECT_LISTVIEW_SCROLL_COUT, ++scrollCout);

            sendAction(10, 1500);

        } else if (position < childCount) {

            AccessibilityNodeInfo child = listview.getChild(position);

            if (!compare(lastItem,child)){
                lastItem = child;
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            listMap.put(Constant.WX_COLLECT_LISTVIEW_POSITON, ++position);
            sendAction(10, 1000);
        }
    }

    public void sendMessage(AccessibilityNodeInfo info, int arg1, int arg2, int what, int delay) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = info;
        handler.sendMessageDelayed(message, delay);
    }

    public void sendAction(int action, int delay) {
        mDelay = delay;
        handler.sendEmptyMessageDelayed(action, delay);
    }


    public boolean compare(AccessibilityNodeInfo node1, AccessibilityNodeInfo node2) {

        List<String> ids1 = Utils.getIds(node1, new ArrayList<String>(), Constant.MODE_ALL);
        List<String> ids2 = Utils.getIds(node2, new ArrayList<String>(), Constant.MODE_ALL);

        return ids1.containsAll(ids2);
    }
}
