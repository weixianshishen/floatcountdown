package com.yangliuliu.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnOpen;
    private Button mBtnClose;
    private Button mBtnIntroduce;
    private Button mBtnSupport;
    private static final String TAG = "FloatWindow";
    private TextView mTvTime;
    private TextView mTvCopy;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTvTime.setText(format());
            mTvTime.setTextColor(getResources().getColor(R.color.colorWhite));
            mTvTime.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mHandler.sendEmptyMessageDelayed(1, 100);

        }
    };
    private IFloatWindow iFloatWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnOpen = (Button) findViewById(R.id.btn_open);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnIntroduce = (Button) findViewById(R.id.btn_introduce);
        mTvCopy = (TextView) findViewById(R.id.tv_copy);
        mBtnSupport = (Button) findViewById(R.id.btn_support);
        mBtnOpen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnIntroduce.setOnClickListener(this);
        mBtnSupport.setOnClickListener(this);
        mTvCopy.setOnClickListener(this);


        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制更新
                .setUserCanRetry(true)         //失败后是否提示重新下载
                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                .register();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
                iFloatWindow = FloatWindow.get();

                if (iFloatWindow != null) {
                    iFloatWindow.show();
                    mHandler.removeMessages(1);
                    mHandler.sendEmptyMessage(1);
                    mTvTime = (TextView) FloatWindow.get().getView();
                }


                break;

            case R.id.btn_close:
                if (iFloatWindow != null) {
                    iFloatWindow.hide();
                }
                break;
            case R.id.btn_introduce:
                new AlertDialog.Builder(this)
                        .setTitle("请手动修改系统权限")
                        .setMessage("【手机参考路径】\n设置->所有应用->找到此应用->权限管理->显示悬浮窗选择为允许\n【一键直达修改界面按钮,如无法点击请手动修改】")
                        .setPositiveButton("一键直达设置界面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getAppDetailSettingIntent();
                            }
                        })
                        .create().show();

                break;

            case R.id.tv_copy:

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(mTvCopy.getText());
                Toast.makeText(this, "复制成功" + mTvCopy.getText(), Toast.LENGTH_LONG).show();

                break;

            case R.id.btn_support:

                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);

                break;

        }
    }

    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
        System.exit(0);
    }


    private String format() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        return format.format(System.currentTimeMillis());
    }


}
