package com.accessibility.sms;

import com.accessibility.utils.Constant;

import java.util.List;

/**
 * 微信状态检查器
 * Created by sc on 2018/3/28.
 */

public class SMSStatusChecker {

    public static int STATUS_SEND = 1000; // 短信发送界面


    private SMSStatusChecker() {
    }

    private static SMSStatusChecker mthis;

    public static SMSStatusChecker getInstance() {
        if (mthis == null)
            mthis = new SMSStatusChecker();
        return mthis;
    }


    public int checkStatus(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        if (ids.contains(Constant.SMS_HOME_BT) && ids.contains(Constant.SMS_SEND_BT)) {
            return STATUS_SEND;

        }
        return 0;
    }
}
