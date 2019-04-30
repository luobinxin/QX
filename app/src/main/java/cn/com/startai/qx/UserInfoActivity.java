package cn.com.startai.qx;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.List;

import cn.com.startai.fssdk.FSUploadCallback;
import cn.com.startai.fssdk.StartaiUploaderManager;
import cn.com.startai.fssdk.db.entity.UploadBean;
import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.channel.mqtt.busi.IThirdAccountType;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.db.bean.UserBean;


public class UserInfoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, View.OnClickListener {

    private TextView tvHead;
    private TextView tvNickName;
    private TakePhoto takePhoto;
    private Uri imageUri;
    private InvokeParam invokeParam;
    private TextView tvMobile;
    private TextView tvWebo;
    private TextView tvAlipay;
    private TextView tvWechat;
    private TextView tvFacebook;
    private TextView tvGoogle;
    private TextView tvEmail;
    private TextView tvQQ;
    private UserBean userInfo;


    @Override
    protected int getLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("用户信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initListener();

        initHeadCacheFile();
    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);
        switch (action) {

            case EventAction
                    .ACTION_GET_USERINFO_RESULT:

                GetUserInfo.Resp resp1 = (GetUserInfo.Resp) eventBean.getObject1();

                onGetUserInfoResult(resp1);

                break;

            case EventAction
                    .ACTION_UPDATE_USERINFO_RESULT:

                UpdateUserInfo.Resp resp2 = (UpdateUserInfo.Resp) eventBean.getObject1();

                onUpdateUserInfoResult(resp2);
                break;
        }
    }

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


    public void onGetUserInfoResult(final GetUserInfo.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "查询用户信息成功");
            loadUserInfoFromLocal();
        } else {
            TAndL.TL(getApplicationContext(), "查询用户信息失败 " + resp.getContent().getErrmsg());
        }
    }

    public void onUpdateUserInfoResult(final UpdateUserInfo.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "更新用户信息成功");
            loadUserInfoFromLocal();
        } else {
            TAndL.TL(getApplicationContext(), "更新用户信息失败 " + resp.getContent().getErrmsg());
        }
    }

    private void initHeadCacheFile() {
        //拍照存储路径
        File file = new File(Environment.getExternalStorageDirectory(), "/QXSDK/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfoFromLocal();
    }

    private void loadUserInfoFromLocal() {

        resetThirdInfos();
        userInfo = QX.getInstance().getQxUserManager().getUser();
        if (userInfo != null) {
            TAndL.L("AccountActivity.userInfo = " + userInfo);

            tvNickName.setText("昵称:" + (TextUtils.isEmpty(userInfo.getNickName()) ? "未设置" : userInfo.getNickName()));
            loadHeadImage(userInfo.getHeadPic());

            String mobile = userInfo.getMobile();
            if (!TextUtils.isEmpty(mobile)) {
                tvMobile.setText("手机：" + mobile);
            }

            String email = userInfo.getEmail();
            if (!TextUtils.isEmpty(email)) {
                tvEmail.setText("邮箱：" + email);
            }


            List<GetUserInfo.Resp.ContentBean.ThirdInfosBean> thirdInfoList = userInfo.getThirdInfoList();
            if (thirdInfoList != null) {


                for (GetUserInfo.Resp.ContentBean.ThirdInfosBean thirdInfosBean : thirdInfoList) {

                    int type = thirdInfosBean.getType();
                    switch (type) {
                        case IThirdAccountType.THIRD_ALIPAY:
                            tvAlipay.setText("支付宝：" + thirdInfosBean.getNickName());
                            tvAlipay.setTag(true);
                            break;
                        case IThirdAccountType.THIRD_FACEBOOK:
                            tvFacebook.setText("Facebook：" + thirdInfosBean.getNickName());
                            tvFacebook.setTag(true);
                            break;
                        case IThirdAccountType.THIRD_QQ:
                            tvQQ.setText("QQ：" + thirdInfosBean.getNickName());
                            tvQQ.setTag(true);
                            break;
                        case IThirdAccountType.THIRD_GOOGLE:
                            tvGoogle.setText("Google:" + thirdInfosBean.getNickName());
                            tvGoogle.setTag(true);
                            break;
                        case IThirdAccountType.THIRD_WECHAT:
                            tvWechat.setText("Facebook:" + thirdInfosBean.getNickName());
                            tvWechat.setTag(true);
                            break;
                    }

                }
            }
        }

    }

    private void resetThirdInfos() {
        tvEmail.setText("邮箱：");
        tvMobile.setText("手机：");
        tvQQ.setText("QQ：");
        tvWebo.setText("微博：");
        tvWechat.setText("Wechat:");
        tvAlipay.setText("支付宝：");
        tvGoogle.setText("Google：");
        tvFacebook.setText("Facebook：");

    }

    private void loadHeadImage(String url) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .error(R.drawable.ic_account_box_black_48dp)
                .placeholder(R.drawable.ic_account_box_black_48dp);
        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    /**
                     * The method that will be called when the resource load has finished.
                     *
                     * @param resource   the loaded resource.
                     * @param transition
                     */
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        resource.setBounds(0, 0, 156, 156);
                        tvHead.setCompoundDrawables(null, null, resource, null);

                    }

                });
    }

    private void initListener() {

        tvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登出
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("设置头像")
                        .setSingleChoiceItems(new String[]{"拍照", "从相册中选择"}, -1, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0:
                                        getTakePhoto().onPickFromCaptureWithCrop(imageUri, getCropOptions());
                                        break;
                                    case 1:
                                        getTakePhoto().onPickFromGalleryWithCrop(imageUri, getCropOptions());
                                        break;

                                }

                            }
                        })
                        .create().show();


            }
        });

        tvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, UpdateNickNameActivity.class));
            }
        });

        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(UserInfoActivity.this, BindMobileOrEmailNumActivity.class);
                intent1.putExtra("isMobile", true);
                startActivity(intent1);
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(UserInfoActivity.this, BindMobileOrEmailNumActivity.class);
                intent2.putExtra("isMobile", false);
                startActivity(intent2);

            }
        });

        tvWechat.setOnClickListener(this);
        tvAlipay.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvWebo.setOnClickListener(this);
        tvGoogle.setOnClickListener(this);
        tvFacebook.setOnClickListener(this);


    }

    private void bindWechat() {

    }

    private void unbindWechat() {

    }


    private void initView() {
        tvHead = (TextView) findViewById(R.id.tv_userinfo_head);
        tvNickName = (TextView) findViewById(R.id.tv_userinfo_nickname);

        tvMobile = (TextView) findViewById(R.id.tv_userinfo_mobile);
        tvWebo = (TextView) findViewById(R.id.tv_userinfo_webo);
        tvAlipay = (TextView) findViewById(R.id.tv_userinfo_alipay);
        tvWechat = (TextView) findViewById(R.id.tv_userinfo_wechat);

        tvFacebook = (TextView) findViewById(R.id.tv_userinfo_facebook);
        tvGoogle = (TextView) findViewById(R.id.tv_userinfo_google);
        tvEmail = (TextView) findViewById(R.id.tv_userinfo_email);
        tvQQ = (TextView) findViewById(R.id.tv_userinfo_qq);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getTakePhoto().onSaveInstanceState(outState);
    }


    @Override
    public void takeSuccess(TResult result) {
        String originalPath = result.getImage().getOriginalPath();
        Log.d(TAG, "takeSuccess " + originalPath);

        startUploadHeadPic(result.getImage().getOriginalPath());

    }

    private void startUploadHeadPic(String localPath) {

        UploadBean uploadBean = new UploadBean();
        uploadBean.setLocalPath(localPath);
        StartaiUploaderManager.getInstance().startUpload(uploadBean, new FSUploadCallback() {
            @Override
            public void onStart(UploadBean entity) {

                TAndL.TL(getApplicationContext(), "正在上传头像");

            }

            @Override
            public void onSuccess(UploadBean entity) {

                TAndL.TL(getApplicationContext(), "头像上传成功");

                //更新云端用户信息
                UpdateUserInfo.Req req = new UpdateUserInfo.Req();
                req.setHeadPic(entity.getHttpDownloadUrl());
                getQXBusi().updateUserInfo(req, callListener);
            }

            @Override
            public void onFailure(UploadBean entity, int statusCode) {
                TAndL.TL(getApplicationContext(), "头像上传失败，请重试");
            }

            @Override
            public void onProgress(UploadBean entity) {
                TAndL.TL(getApplicationContext(), "正在上传..." + entity.getProgress());
            }

            @Override
            public void onWaiting(UploadBean entity) {
                TAndL.TL(getApplicationContext(), "onWaiting");
            }

            @Override
            public void onPause(UploadBean entity) {
                TAndL.TL(getApplicationContext(), "onPause");
            }
        });


    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.d(TAG, "takeFail ");
    }

    @Override
    public void takeCancel() {
        Log.d(TAG, "takeCancel ");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * 裁剪参数
     *
     * @return
     */
    private CropOptions getCropOptions() {

        int height = 512;
        int width = 512;
        boolean withWonCrop = true;

        CropOptions.Builder builder = new CropOptions.Builder();

        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        TextView textView = null;
        String tag = "";
        int type = 0;
        switch (v.getId()) {
            case R.id.tv_userinfo_webo:
                textView = tvWebo;
                tag = "微博";

                break;
            case R.id.tv_userinfo_qq:
                textView = tvQQ;
                tag = "QQ";
                type = IThirdAccountType.THIRD_QQ;
                break;
            case R.id.tv_userinfo_wechat:
                tag = "微信";
                textView = this.tvWechat;
                type = IThirdAccountType.THIRD_WECHAT;
                break;
            case R.id.tv_userinfo_alipay:
                tag = "支付宝";
                textView = tvAlipay;
                type = IThirdAccountType.THIRD_ALIPAY;
                break;
            case R.id.tv_userinfo_google:
                tag = "Google";
                textView = tvGoogle;
                type = IThirdAccountType.THIRD_GOOGLE;
                break;
            case R.id.tv_userinfo_facebook:
                tag = "Facebook";
                textView = tvFacebook;
                type = IThirdAccountType.THIRD_FACEBOOK;

                break;

        }
        if (textView != null) {

            Boolean isBind = false;
            if (textView.getTag() != null) {
                isBind = (Boolean) textView.getTag();
            }
            TAndL.L("isBind = " + isBind);
            if (isBind) {
                if (userInfo.isOnlyOneThindBind()) {
                    createDialog("提示", "至少保留一个绑定账号", null, null, null, null).show();
                } else {

                    final int finalType = type;
                    createDialog("提示", "解除" + tag + "绑定", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unBindThird(finalType);
                        }
                    }, "取消", null).show();
                }
            } else {
                bindThird(type);
            }
        }
    }

    private void bindThird(int type) {
        switch (type) {
            case IThirdAccountType.THIRD_WECHAT:
                toBindWechat();
                break;
            case IThirdAccountType.THIRD_ALIPAY:
                toBindAlipay();
                break;
            case IThirdAccountType.THIRD_QQ:
                toBindQQ();
                break;
            case IThirdAccountType.THIRD_GOOGLE:
                toBindGoogle();
                break;
            case IThirdAccountType.THIRD_FACEBOOK:
                toBindFacebook();
                break;

        }
    }

    private void toBindFacebook() {

    }

    private void toBindGoogle() {

    }

    private void toBindQQ() {

    }

    private void toBindAlipay() {

    }

    private void toBindWechat() {

    }

    private void unBindThird(int type) {
        UnBindThirdAccount.Req req = new UnBindThirdAccount.Req(type);
        getQXBusi().unBindThirdAccount(req, callListener);
    }
}
