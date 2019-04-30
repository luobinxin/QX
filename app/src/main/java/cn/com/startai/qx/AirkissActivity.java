package cn.com.startai.qx;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.plugin.exdevice.jni.C2JavaExDevice;
import com.tencent.mm.plugin.exdevice.jni.Java2CExDevice;

import cn.com.startai.airkisssender.StartaiAirkissManager;
import cn.com.startai.helper.TAndL;


/**
 * airkiss 配网界面
 */
public class AirkissActivity extends AppCompatActivity {


    private TextView tvSSID;

    private EditText etPwd;

    private Button btSend;
    private Button btCancel;

    private long t;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airkiss);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("Airkiss配网");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSSID = findViewById(R.id.tv_ssid);
        etPwd = findViewById(R.id.et_password);
        btSend = findViewById(R.id.bt_send);
        btCancel = findViewById(R.id.bt_cancel);

        tvLog = findViewById(R.id.tv_log);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());


        StartaiAirkissManager.getInstance().setAirKissListener(airkissListener);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((Boolean) btSend.getTag()) {
                    //处理5g网络
                    appendLog("暂不支持5G网络");
                    return;
                }


                String ssid = tvSSID.getText().toString();// ssid
                String pwd = etPwd.getText().toString();//wifi密码
                String aesKey = "";
                int processPeroid = 0;
                int datePeroid = 5;
                int timeout = 90 * 1000; //超时时长


                StartaiAirkissManager.getInstance().startAirKiss(pwd, ssid, aesKey.getBytes(), timeout, processPeroid, datePeroid);


                t = System.currentTimeMillis();
                appendLog("\n开始配置... \nssid = " + ssid + " \npwd = " + pwd + "\n");


            }
        });


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopAirkiss();


                appendLog("\n停止配置");

            }
        });


    }

    C2JavaExDevice.OnAirKissListener airkissListener = new C2JavaExDevice.OnAirKissListener() {
        @Override
        public void onAirKissSuccess() {
            appendLog("配置成功 用时 " + ((System.currentTimeMillis() - t) / 1000) + " s\n");
        }

        @Override
        public void onAirKissFailed(int error) {
            appendLog("配置失败 errorCode = " + error + "\n");
            StartaiAirkissManager.getInstance().stopAirKiss();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        unregisterReceiver(mReceiver);
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    onWifiChanged(wifiInfo);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 处理在配网过程中wifi切换
     *
     * @param info
     */
    private void onWifiChanged(WifiInfo info) {
        if (info == null) {
            tvSSID.setText("");
            btSend.setEnabled(false);

            stopAirkiss();
            appendLog("网络断开，配网已经停止");

        } else {

            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }

            tvSSID.setText(ssid);

            btSend.setEnabled(true);

            //5G网络判断
            btSend.setTag(Boolean.FALSE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequence = info.getFrequency();
                if (frequence > 4900 && frequence < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    btSend.setTag(Boolean.TRUE);
                }
            }
        }
    }

    private void stopAirkiss() {

        StartaiAirkissManager.getInstance().stopAirKiss();

    }


    private String TAG = this.getClass().getSimpleName();


    private void appendLog(final String log) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TAndL.TL(getApplicationContext(), log);
                CharSequence text = tvLog.getText();
                tvLog.setText(log + "\n" + text);
                tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

            }
        });

    }

}

