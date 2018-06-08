package com.accessibility.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by sc on 2018/4/17.
 */

public class FloatView extends FrameLayout {

    public FloatView(@NonNull Context context) {
        super(context);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void init() {
        //final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//
//            final WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
//
//            wl.type = WindowManager.LayoutParams.TYPE_PHONE;
//            wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//            //wl.gravity = Gravity.RIGHT;
//
//            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            wl.format = PixelFormat.RGBA_8888;
//
//            wl.windowAnimations = android.R.anim.fade_in;
//
//
//
//            final Point point = new Point();
//            wm.getDefaultDisplay().getRealSize(point);
//
//            final View floatview = View.inflate(this, R.layout.float_window_layout, null);
//
//            floatview.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            v.animate().scaleX(1.5f).scaleY(1.5f).setDuration(300).start();
//                            wm.updateViewLayout(floatview,wl);
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            float rawY = event.getRawY()- point.y/2 - v.getHeight()/2;
//                            float rawX = point.x/2 - event.getRawX();
//                            wl.y = (int)rawY;
//                            wl.x = -(int)rawX;
//                            wm.updateViewLayout(floatview,wl);
//                            break;
//                        default:
//                            v.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
//                            break;
//                    }
//
//                    return false;
//                }
//            });
//
//            wm.addView(floatview, wl);
    }
//        AccessibilityServiceInfo info = getServiceInfo();
//        info.packageNames = new String[]{"com.tencent.mobileqq"};
//        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
//        setServiceInfo(info);
}
