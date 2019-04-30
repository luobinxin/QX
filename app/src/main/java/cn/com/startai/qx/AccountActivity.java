package cn.com.startai.qx;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.db.bean.UserBean;


/**
 * 个人中心
 */
public class AccountActivity extends BaseActivity {

    private TextView btLogout;
    private TextView tvAboutUs;
    private TextView tvUpdatePwd;
    private TextView tvCheckUpdate;
    private TextView tvNickName;
    private ImageView ivSet;
    private ImageView ivHead;
    private UserBean userInfo;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            getQXBusi().getUserInfo(callListener);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_account;
    }

    @Override
    public void init() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("我的");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initview();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfoFromLocal();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {

            case EventAction.ACTION_LOGOUT_RESULT:
                int result = (int) eventBean.getObject1();
                if (result == 1) {
                    finishAllActivity();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                }

                break;

            case EventAction
                    .ACTION_GET_USERINFO_RESULT:

                GetUserInfo.Resp resp = (GetUserInfo.Resp) eventBean.getObject1();

                onGetUserInfoResult(resp);

                break;

            case EventAction.ACTION_GET_LATEST_VERSION_RESULT:

                GetLatestAppVersion.Resp resp2 = (GetLatestAppVersion.Resp) eventBean.getObject1();
                onGetLatestVersionResult(resp2);
                break;
        }

    }

    private void loadUserInfoFromLocal() {

        userInfo = QX.getInstance().getQxUserManager().getUser();
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.getNickName())) {
                tvNickName.setText("昵称未设置");
            } else {
                tvNickName.setText(userInfo.getNickName());
            }
            int isHavePwd = userInfo.getIsHavePwd();
            if (isHavePwd == 1) {
                tvUpdatePwd.setText("修改密码");
            } else {
                tvUpdatePwd.setText("设置登录密码");
            }

            RequestOptions options = new RequestOptions()
                    .circleCrop()
                    .error(R.drawable.ic_account_box_black_48dp)
                    .placeholder(R.drawable.ic_account_box_black_48dp);
            Glide.with(getApplicationContext())
                    .load(userInfo.getHeadPic())
                    .apply(options)
                    .into(ivHead);

        } else {
            getQXBusi().getUserInfo(callListener);
        }

    }

    public void onGetUserInfoResult(final GetUserInfo.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "查询用户信息成功");
            loadUserInfoFromLocal();
        } else {
            TAndL.TL(getApplicationContext(), "查询用户信息失败 " + resp.getContent().getErrmsg());
        }

    }

    private void initListener() {


        ivSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登出
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("提示")
                        .setMessage("退出登录")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getQXBusi().logout();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();


            }
        });

        tvCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQXBusi().getLatestVersion(callListener);

            }
        });


        tvUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UpdatePasswordActivity.class);
                if (userInfo != null) {
                    intent.putExtra("isHavePwd", userInfo.getIsHavePwd());
                }
                startActivity(intent);

            }
        });

    }

    public void onGetLatestVersionResult(GetLatestAppVersion.Resp resp) {


        if (resp != null) {
//            TAndL.TL(getApplicationContext(), "检查结果 " + resp.getResult());
            toUpdate(resp.getContent());
        }
    }


    private void toUpdate(final GetLatestAppVersion.Resp.ContentBean contentBean) {

//        if (!getApplicationInfo().packageName.equals(contentBean.getPackageName())) {
//            return;
//        }

        int versionCode = contentBean.getVersionCode();
        int currCode = AppUtils.getAppInfo().getVersionCode();
        if (currCode >= versionCode) {
            TAndL.TL(getApplicationContext(), "已经是最新版本了 " + AppUtils.getAppInfo().getVersionName());
        } else {
            new AlertDialog.Builder(AccountActivity.this)
                    .setTitle("检测到新版本 " + contentBean.getVersionName())
                    .setMessage(contentBean.getUpdateLog())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new AlertDialog.Builder(AccountActivity.this)
                                    .setTitle("下载文件")
                                    .setSingleChoiceItems(new String[]{"浏览器下载", "应用内下载", "应用市场下载"}, -1, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            switch (which) {
                                                case 0:
                                                    gotoBrowserDownload(contentBean.getUpdateUrl());
                                                    break;
                                                case 1:
                                                    TAndL.TL(getApplicationContext(), "应用内下载 开发中...");
                                                    break;
                                                case 2:
                                                    TAndL.TL(getApplicationContext(), "应用市场下载 开发中...");
                                                    break;
                                            }
                                        }
                                    })
                                    .show();


                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        }
    }

    private void gotoBrowserDownload(String downloadUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(downloadUrl);
        intent.setData(content_url);
        startActivity(intent);
    }


    public void onUnActiviteResult(UpdateUserInfo.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {

            TAndL.TL(getApplicationContext(), "注销成功 ");
            finishAllActivity();
            System.exit(0);
        } else {
            TAndL.TL(getApplicationContext(), "注销失败 ");
        }

    }


    private void initview() {


        btLogout = (TextView) findViewById(R.id.bt_account_exit_login);

        tvAboutUs = (TextView) findViewById(R.id.tv_account_abouttus);
        tvNickName = (TextView) findViewById(R.id.tv_account_nickname);
        tvCheckUpdate = (TextView) findViewById(R.id.tv_account_app_update);
        tvUpdatePwd = (TextView) findViewById(R.id.tv_account_pwd);
        ivSet = (ImageView) findViewById(R.id.iv_account_set);
        ivHead = (ImageView) findViewById(R.id.iv_account_head);


    }

}
