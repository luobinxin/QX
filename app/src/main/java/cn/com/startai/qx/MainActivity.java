package cn.com.startai.qx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.com.startai.qxsdk.QX;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        QX.release();
        App.exitApp();

    }
}
