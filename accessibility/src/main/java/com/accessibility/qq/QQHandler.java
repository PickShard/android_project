package com.accessibility.qq;

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

public class QQHandler {


    private QQStatusChecker qqStatusChecher;
    private AccessibilityNodeInfo lastRoot;
    private List<String> mIds;

    public static int rote = 1; //操作速率
    private int status;     //当前状态


    private boolean canNext = true;


    public QQHandler(CommonAccessibility server) {
        qqStatusChecher = QQStatusChecker.getInstance();
        TaskStack.currTask = TaskStack.TASK_REGISTER_QQ;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 10) {
                canNext = true;
                handleEvent();
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
        status = qqStatusChecher.checkStatus(mIds);
        action(lastRoot, status, TaskStack.currTask);
    }


    public void action(AccessibilityNodeInfo root, int status ,int task) {

        Log.d("333333333333333333333333333");

        if (task == TaskStack.TASK_REGISTER_QQ){

            if (status == QQStatusChecker.STATUS_LOGIN){

                AccessibilityUtil.clickViewByText(root,Constant.QQ_LOGIN_REGISTER_TEXT);
                sendAction(10,800);
            }else  if (status == QQStatusChecker.STATUS_REGISTER_TEL){

                ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
                Log.d(EditText.class.getName());
                AccessibilityUtil.getViewsByType(root, EditText.class.getName(), nodeInfos);
                AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfo,"13008843475");
                AccessibilityUtil.clickViewByText(root,Constant.QQ_REGISTER_NEXT_TEXT);

                sendAction(10,800);
            }else if (status == QQStatusChecker.STATUS_REGISTER_CODE){

                ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
                AccessibilityUtil.getViewsByType(root, EditText.class.getName(), nodeInfos);

                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(0),"0");
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(1),"1");
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(2),"2");
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(3),"3");
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(4),"4");
                AccessibilityUtil.inputText(MyApplication.getMyApplication(),nodeInfos.get(5),"5");

                sendAction(10,800);

            }else if (status == QQStatusChecker.STATUS_REGISTER_BUNDLE){

                AccessibilityUtil.clickViewByText(root,Constant.QQ_REGISTER_REGISTER);
                sendAction(10,800);

            }else if (status == QQStatusChecker.STATUS_REGISTER_SEND_SMS){

                AccessibilityUtil.clickViewByText(root,Constant.QQ_REGISTER_SEND_SMS);
                sendAction(10,800);
            }

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
}
