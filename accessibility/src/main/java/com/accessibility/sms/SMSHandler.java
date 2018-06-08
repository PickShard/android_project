package com.accessibility.sms;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import com.accessibility.TaskStack;
import com.accessibility.server.CommonAccessibility;
import com.accessibility.utils.AccessibilityUtil;
import com.accessibility.utils.Constant;
import com.accessibility.utils.Log;
import com.accessibility.app.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by sc on 2017/6/20.
 */

public class SMSHandler {

    private SMSStatusChecker smsStatusChecker;
    private List<Integer> taskList = new ArrayList<>();

    public static int mDelay = 1500;
    private int status;
    private AccessibilityNodeInfo lastRoot;
    private List<String> mIds;

    private Map<String, Integer> listMap = new ArrayMap<>();
    private boolean canNext = true;


    public SMSHandler(CommonAccessibility server) {
        smsStatusChecker = SMSStatusChecker.getInstance();
        taskList.add(TaskStack.TASK_REGISTER_QQ);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 10 ) {
                canNext = true;
                handleEvent();
            }else if (msg.what == 20){
                AccessibilityUtil.clickViewByID(lastRoot,Constant.SMS_HOME_BT);
            }
        }
    };

    public void onEvent(AccessibilityNodeInfo root, List<String> ids) {

        lastRoot = root;
        mIds = ids;

        if (canNext){
            handleEvent();
        }
    }

    private void handleEvent() {
        Log.d("222222222222222222222");

        if (lastRoot == null || mIds == null){
            return;
        }

        canNext = false;
        status = smsStatusChecker.checkStatus(mIds);
        conduct(lastRoot, status, taskList.get(0));
    }

    public void conduct(AccessibilityNodeInfo root, int status, int task) {

        if (task >= TaskStack.TASK_REGISTER_QQ) {
            action(root, status);
        }

    }


    public void action(AccessibilityNodeInfo root, int status) {

        Log.d("333333333333333333333333333");

        if (status == SMSStatusChecker.STATUS_SEND){
            Log.d("44444444444444444444444444444");
            AccessibilityUtil.clickViewByID(root,Constant.SMS_SEND_BT);
            sendAction(20,800);
        }

    }

    public void sendAction(int action, int delay) {
        mDelay = delay;
        handler.sendEmptyMessageDelayed(action, delay);
    }
}
