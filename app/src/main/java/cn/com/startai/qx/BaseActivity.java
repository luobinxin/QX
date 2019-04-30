package cn.com.startai.qx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.com.startai.helper.TAndL;
import cn.com.startai.helper.TimeCount;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qx.utils.SLog;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.QXBusiManager;
import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.QXError;


/**
 * Created by Robin on 2018/10/10.
 * qq: 419109715 彬影
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG = "";
    static ArrayList<Activity> activityArrayList = new ArrayList<>();

    protected String action;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAndL.L(TAG + " onCreate");
        setContentView(getLayout());
        TAG = this.getClass().getSimpleName();
        init();
        activityArrayList.add(this);
        SLog.d(TAG, this.getClass().getSimpleName() + " added to activitylist" + " size = " + activityArrayList.size());

    }

    TimeCount timeCount;

    protected void initTimeCount(Button button) {
        timeCount = new TimeCount(60 * 1000, 1000, button);
    }

    protected void startTimeCount() {
        timeCount.start();
    }

    protected void releaseTimeCount() {
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
    }

    @Override
    protected void onResume() {
        TAndL.L(TAG + " onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        TAndL.L(TAG + " onPause");
        super.onPause();
    }

    @Override
    protected void onStart() {
        TAndL.L(TAG + " onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        TAndL.L(TAG + " onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    protected IQXBusi getQXBusi() {
        IQXBusi qx = QX.getInstance().getQxBusiManager();
        return qx;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TAndL.L(TAG + " onDestroy");
        activityArrayList.remove(this);
        releaseTimeCount();
        SLog.d(TAG, this.getClass().getSimpleName() + " remove from activitylist" + " size = " + activityArrayList.size());
    }

    protected abstract int getLayout();

    public abstract void init();

    protected void exitApp() {
        finishAllActivity();
        System.exit(0);

    }

    protected void finishAllActivity() {
        for (Activity activity : activityArrayList) {
            SLog.d(TAG, activity.getClass().getSimpleName() + " finish");
            activity.finish();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean eventBean) {
        action = eventBean.getAction();
        TAndL.L(TAG + " action = " + action);
    }


    protected IQXCallListener callListener = new IQXCallListener() {
        @Override
        public void onSuccess() {
            TAndL.TL(getApplicationContext(), "消息发送成功");
        }

        @Override
        public void onFailed(QXError error) {
            TAndL.TL(getApplicationContext(), "消息发送失败 " + error.getErrcode() + " " + error.getErrmsg());
        }
    };

    protected void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }
    }

    protected boolean isLogin() {
        UserBean user = QXUserManager.getInstance().getUser();
        return user != null;
    }

    protected void toLogin() {
        startActivity(LoginActivity.class);
        finish();
    }

    protected void checkLoginAndToLogin() {
        if (!isLogin()) {
            toLogin();
        }
    }

    protected void startActivity(Class activity) {


        startActivity(new Intent(this, activity));
    }

    public AlertDialog createDialog(String title, String msg, String posMsg, DialogInterface.OnClickListener posListener, String nagMsg, DialogInterface.OnClickListener negListener) {

        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posMsg, posListener)
                .setNegativeButton(nagMsg, negListener)
                .create();
    }
}
