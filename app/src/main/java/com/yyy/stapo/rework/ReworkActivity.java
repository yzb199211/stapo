package com.yyy.stapo.rework;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.yyy.stapo.R;
import com.yyy.stapo.dialog.JudgeDialog;
import com.yyy.stapo.dialog.LoadingDialog;
import com.yyy.stapo.interfaces.ResponseListener;
import com.yyy.stapo.login.LoginBean;
import com.yyy.stapo.util.NetConfig;
import com.yyy.stapo.util.NetParams;
import com.yyy.stapo.util.NetUtil;
import com.yyy.stapo.util.SharedPreferencesHelper;
import com.yyy.stapo.util.StringUtil;
import com.yyy.stapo.util.Toasts;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectChangeListener;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.view.OptionsPickerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReworkActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right2)
    ImageView ivRight2;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_sbarCode)
    EditText etSbarCode;
    @BindView(R.id.et_time)
    EditText etTime;
    @BindView(R.id.tv_worker)
    TextView tvWorker;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sbarcode)
    TextView tvSbarcode;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_check_worker)
    TextView tvCheckWorker;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.tv_error_length)
    TextView tvErrorLength;
    @BindView(R.id.tv_error_num)
    TextView tvErrorNum;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    OptionsPickerView workerPick;
    int iRecNo = 0;
    String workerId = "";
    String userId = "";

    List<ReworkWorkerBean.TablesBean> workers;
    SharedPreferencesHelper preferencesHelper;
    JudgeDialog submitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rework);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        userId = (String) preferencesHelper.getSharedPreference("userid", "");
        initView();
    }

    private void initView() {
        workers = new ArrayList<>();
        ivBack.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        ivRight2.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        tvTitle.setText("修补");
        initEdit();
    }

    private void initEdit() {
//        etTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSbarCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSbarCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    hintKeyBoard();
                    String code = etSbarCode.getText().toString();
                    clearData();
                    getData(code);
                }
                return false;
            }
        });
    }

    private void clearData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llContent.setVisibility(View.GONE);
                iRecNo = 0;
                tvCode.setText("");
                tvName.setText("");
                tvSbarcode.setText("");
                tvType.setText("");
                tvCheckWorker.setText("");
                tvLength.setText("");
                tvErrorLength.setText("");
                tvErrorNum.setText("");
                if (workerPick != null)
                    workerPick.setSelectOptions(0);
                workerId = "";
                tvWorker.setText("");
                etTime.setText("");
            }
        });
    }

    private void initPick() {
        workerPick = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                tvWorker.setText(workers.get(options1).getPickerViewText());
                workerId = workers.get(options1).getSCode();
            }
        })
                .setTitleText("修补人员选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        workerPick.setPicker(workers);//一级选择器
//        pvCustom.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
        Dialog mDialog = workerPick.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            workerPick.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
                //当显示只有一列是需要设置window宽度，防止两边有空隙；
                WindowManager.LayoutParams winParams;
                winParams = dialogWindow.getAttributes();
                winParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialogWindow.setAttributes(winParams);
            }
        }
        workerPick.show();
    }

    /*获取条码数据*/
    private void getData(String code) {
        LoadingDialog.showDialogForLoading(this);
        etSbarCode.setText("");
        new NetUtil(getParams(code), NetConfig.url + NetConfig.Pda_Method, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    Gson gson = new Gson();
                    String data = StringUtil.getRepleaceData(string);
                    ReworkBean reworkBean = gson.fromJson(data, ReworkBean.class);
                    if (reworkBean.isSuccess()) {
                        List<ReworkBean.TablesBean> list = reworkBean.getTables().get(0);
                        if (list.size() > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setView(list);
                                }
                            });

                        } else loadFail("条码不存在");
                    } else {
                        loadFail(reworkBean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadFail("数据解析错误");
                }

            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                loadFail("网络错误");
            }
        });
    }

    /*加载条码数据*/
    private void setView(List<ReworkBean.TablesBean> list) {
        llContent.setVisibility(View.VISIBLE);
        iRecNo = list.get(0).getIRecNo();
        tvCode.setText(TextUtils.isEmpty(list.get(0).getSCode()) ? "产品编号：" : "产品编号：" + list.get(0).getSCode());
        tvName.setText(TextUtils.isEmpty(list.get(0).getSName()) ? "产品名称：" : "产品名称：" + list.get(0).getSName());
        tvSbarcode.setText(TextUtils.isEmpty(list.get(0).getSBarcode()) ? "产品条码：" : "产品条码：" + list.get(0).getSBarcode());
        tvType.setText(TextUtils.isEmpty(list.get(0).getSFlowerCode()) ? "花本型号：" : "花本型号：" + list.get(0).getSFlowerCode());
        tvCheckWorker.setText(TextUtils.isEmpty(list.get(0).getSUserName()) ? "验布人员：" : "验布人员" + list.get(0).getSUserName());
        tvLength.setText("毛米数：" + list.get(0).getFQty());
        tvErrorLength.setText("疵点米数：" + list.get(0).getFFlawQty());
        tvErrorNum.setText("疵点数：" + list.get(0).getIFlawCount());
        LoadingDialog.cancelDialogForLoading();
    }

    /*获取条码参数*/
    private List<NetParams> getParams(String code) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", "GetBarcodeFromSDOrderDDVatNoDReelNoWithRepair"));
        params.add(new NetParams("db", "HxSiTaiBao2019ULT"));
        params.add(new NetParams("callback", ""));
        params.add(new NetParams("sBarCode", code));
        return params;
    }

    /*获取修复人员数据*/
    private void getWorker() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getWorkerParams(), NetConfig.url + NetConfig.Pda_Method, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    Gson gson = new Gson();
                    ReworkWorkerBean reworkWorkerBean = gson.fromJson(StringUtil.getRepleaceData(string), ReworkWorkerBean.class);
                    if (reworkWorkerBean.isSuccess()) {
                        if (reworkWorkerBean.getTables().get(0).size() > 0) {
                            workers.clear();
                            workers.addAll(reworkWorkerBean.getTables().get(0));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.cancelDialogForLoading();
                                    initPick();
                                }
                            });
                        } else {
                            loadFail("无修补人员数据");
                        }
                    } else {
                        loadFail(reworkWorkerBean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadFail("数据解析错误");
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                loadFail("网络错误");
            }
        });
    }

    /*获取修补人员参数*/
    private List<NetParams> getWorkerParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", "GetRepairPerson"));
        params.add(new NetParams("db", "HxSiTaiBao2019ULT"));
        params.add(new NetParams("callback", ""));
        return params;
    }

    /*提交数据*/
    private void submit(Float time) {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(submitParams(time), NetConfig.url + NetConfig.Pda_Method, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    Gson gson = new Gson();

                    LoginBean bean = gson.fromJson(StringUtil.getRepleaceData(string), LoginBean.class);
                    if (bean.isSuccess()) {
                        clearData();
                        loadFail("提交成功");
                    } else {
                        loadFail(bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadFail("数据解析错误");
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                loadFail("网络错误");
            }
        });
    }

    private List<NetParams> submitParams(Float time) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", "SaveRepairMessage"));
        params.add(new NetParams("callback", ""));
        params.add(new NetParams("sRepairPerson", workerId));
        params.add(new NetParams("db", "HxSiTaiBao2019ULT"));
        params.add(new NetParams("iRepairCost", time * 60 + ""));
        params.add(new NetParams("iRecNo", iRecNo + ""));
        params.add(new NetParams("sUserID", userId));
        return params;
    }

    /*隐藏键盘*/
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @OnClick({R.id.tv_worker, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_worker:
                if (workers.size() > 0) {
                    workerPick.show();
                } else {
                    getWorker();
                }
                break;
            case R.id.tv_submit:

                Float time = 0.0f;
                if (!StringUtil.isNumber(etTime.getText().toString())) {
                    Toasts.showShort(ReworkActivity.this, "修补时间不符合规则");
                    return;
                }

                time = Float.parseFloat(etTime.getText().toString());
                if (time < 0.1) {
                    Toasts.showShort(ReworkActivity.this, "修补时间应大于0.1小时");
                    return;
                }

                if (TextUtils.isEmpty(workerId)) {
                    Toasts.showShort(ReworkActivity.this, "请选择修补人员");
                    return;
                }
                isSubmit(time);

                break;
            default:
                break;
        }
    }

    private void isSubmit(Float time) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("修补时间：" + etTime.getText().toString() + "小时");
        buffer.append("\n");
        buffer.append("修补人员：" + tvWorker.getText().toString());
        buffer.append("\n");
        buffer.append(tvSbarcode.getText().toString());
        buffer.append("\n");
        buffer.append("是否确定提交？");
        String title = buffer.toString();

        if (submitDialog == null)
            submitDialog = new JudgeDialog(this, R.style.JudgeDialog, title, new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm)
                        submit(time);
                }
            });
        else {
            submitDialog.setContent(title);
        }

        submitDialog.show();
    }

    private void loadFail(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                Toasts.showShort(ReworkActivity.this, message);
            }
        });
    }
}
