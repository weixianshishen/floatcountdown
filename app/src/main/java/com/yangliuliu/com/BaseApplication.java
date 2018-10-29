package com.yangliuliu.com;

import android.app.Application;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

/**
 * Created by togo1 on 2018/10/23.
 */

public class BaseApplication extends Application {

    private static final String TAG  = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(30);


        FloatWindow
                .with(getApplicationContext())
                .setView(textView)
                .setWidth(Screen.width, 0.53f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.125f)
                .setX(100)
                .setY(Screen.height, 0.35f)
                .setMoveType(MoveType.active)
                .setMoveStyle(500, new BounceInterpolator())
                .setViewStateListener(mViewStateListener)
                .setDesktopShow(true)
                .build();


        PgyCrashManager.register();
        PgyerCrashObservable.get().attach(new PgyerObserver() {
            @Override
            public void receivedCrash(Thread thread, Throwable throwable) {

            }
        });

    }



    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };


}
