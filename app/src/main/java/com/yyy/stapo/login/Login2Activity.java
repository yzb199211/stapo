package com.yyy.stapo.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyy.stapo.BaseActivity;
import com.yyy.stapo.R;
import com.yyy.stapo.dialog.LoadingDialog;
import com.yyy.stapo.interfaces.ResponseListener;
import com.yyy.stapo.rework.ReworkActivity;
import com.yyy.stapo.util.NetConfig;
import com.yyy.stapo.util.NetParams;
import com.yyy.stapo.util.NetUtil;
import com.yyy.stapo.util.PxUtil;
import com.yyy.stapo.util.SharedPreferencesHelper;
import com.yyy.stapo.util.StringUtil;
import com.yyy.stapo.util.Toasts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login2Activity extends BaseActivity {
    private final String TAG = "LoginActivity";
    @BindView(R.id.iv_login_header)
    ImageView ivLoginHeader;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_sweep)
    TextView btnSweep;
    private static final int REQUEST_CODE_SCAN = 11;
    SharedPreferencesHelper preferencesHelper;
    String userid;
    String password;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        url = NetConfig.url + NetConfig.Login_Method;
        initView();
    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivLoginHeader.getLayoutParams();
        params.height = PxUtil.getHeight(this);
        ivLoginHeader.setLayoutParams(params);
        String name = (String) preferencesHelper.getSharedPreference("userid", "");
        etUser.setText(name);
        etUser.setSelection(name.length());
        etPwd.setText((String) preferencesHelper.getSharedPreference("pwd", ""));
        btnSweep.setVisibility(View.GONE);
    }


    @OnClick({R.id.btn_login, R.id.btn_sweep})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_login:
                try {
                    isNone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_sweep:
//                intent.setClass(LoginActivity.this, MainActivity.class);
////                intent.setClass(LoginActivity.this, OutputListActivity.class);
//                startActivity(intent);
//                permission(intent);
                break;
        }
    }


    /**
     * 判断url,用户名和密码是否为空
     */
    private void isNone() throws Exception {

        Log.e(TAG, url);

        userid = etUser.getText().toString();
        password = etPwd.getText().toString();

        if (TextUtils.isEmpty(userid)) {
            Toasts.showShort(Login2Activity.this, "请输入用户名");
            return;
        }
//        if (TextUtils.isEmpty(password)) {
//            Toasts.showShort(LoginActivity.this, "请输入密码");
//            return;
//        }
        getContact();
//        NetRxUtil.doRequestByRetrofit(NetRequestBody.getMultipartBody(getParams()));
    }

    /**
     * 获取数据
     */
    private void getContact() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initData(StringUtil.getRepleaceData(string));
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasts.showShort(Login2Activity.this, "无法连接服务器");
                            LoadingDialog.cancelDialogForLoading();
                        }
                    });
                }
            }

            @Override
            public void onFail(IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.cancelDialogForLoading();
                        Toasts.showLong(Login2Activity.this, e.getMessage());
                    }
                });

            }
        });
    }

    /**
     * 设置参数
     *
     * @return
     */
    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("userid", userid));
        list.add(new NetParams("password", password));
        list.add(new NetParams("db", "HxSiTaiBao2019ULT"));
        list.add(new NetParams("callback", ""));
        return list;
    }

    /**
     * 返回数据处理
     *
     * @param response
     */
    private void initData(String response) throws Exception {

        Gson gson = new Gson();
        Log.e(TAG, response);
        LoginBean model = gson.fromJson(response, LoginBean.class);
        if (model.isSuccess()) {
            preferencesHelper.put("userid", userid);
            preferencesHelper.put("pwd", password);
            Intent intent = new Intent();
            intent.setClass(Login2Activity.this, ReworkActivity.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.cancelDialogForLoading();
                    startActivity(intent);
                    finish();
                }
            });


        } else
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, model.getMessage());
                    LoadingDialog.cancelDialogForLoading();
                    Toasts.showLong(Login2Activity.this, model.getMessage());
                }
            });
    }


}
