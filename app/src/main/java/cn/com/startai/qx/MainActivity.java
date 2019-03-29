package cn.com.startai.qx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.event.IOnCallListener;
import cn.com.startai.qxsdk.global.QXError;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login.Req req = new Login.Req();
        req.setUname("");
        req.setPwd("");


        QX.getInstance().getQxBusi().login(req, new IOnCallListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(QXError error) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        App.exitApp();

    }
}
