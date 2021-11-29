package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.ADD_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.alipay.AuthResult;
import com.example.zuwaapp.alipay.OrderInfoUtil2_0;
import com.example.zuwaapp.alipay.PayResult;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RentGlaceActivity extends AppCompatActivity {
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2021002197635045";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "2088532870584123";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "租蛙工作室";

    public static final String PARTNER = "租蛙工作室";


    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC2El+2YMz6WeWKZ2IAeMotqH8AH9XkSng1onq9cOGGhEqTy8pryzjV/PyshO7BFMJVXslynzrBzLm2huEQWMTrFzwJw7jzNan3q635kbL4j+H2wyB7cXmDuMgX2ET/upivuID4/zf6lonVUkxDqBTxGohEPJogNl8Iwgedi380eaqMwuWD2150I1EOXEncAk4AZTcgG1+ojtr0tgHTn0i6R4ddowp45ovd2nwQqdqCNxHOQxx9CiFYf+hs5xr0zajibMCj3pSJndOzgF5GlSlvCh3y/VaiqFRwZgKI2retGQ5cLS/s1bGMvuAfDmIZHKd35EDrJo3XZdiPn3MAdw4PAgMBAAECggEADgsmxMt8pAbRFLbasUyf05u+iATsm5o+4Wwx6XDOhHCpy/KZH21iaecEmk36mHwAbVNNWID9qDshS/nlXDxsm/GD2QD2wBOwk4Dzf1+EnXWDqWPtmnfrNoTHMkelMtByvIMPt6T/7V01LnYVmdZaHyq7vf+qp7dp5yQIMFe1LRfgBNLkUbxtDQgZ4ZjOrVgP6KR1y5JjTJhFSkcrkEz798qvLpoDy292Ptn/6H9J3EaPSvhtRgozHYyupZ36epJ/uG1PJBfYLyg8tG27bUeXlfeflqGq3Fk9Alll3VHkii4Pu/0LAzStMQwE1wwDEuOwzJQQmaA6KldNmrFURendiQKBgQDee+gzoQZUDd2V4mrCfAlnriXW0vK5akELHkaWVYlK3wK1jBDajzqZM/USNT7OzreJx0pNk7U27JOZ0rvW8UGagnQ1b0TKqC3tie8+YIsjO/TbX8T3E9dY0FtIYlsz79pl7SKSKBXZU6asmHQi54ohGrb/FftJ1b1ZqgCB2r2iiwKBgQDRf/gpbBZhe271ehntkI1SQZ41Tc7Qu9GkJ9iDEXrO3wi1MN7+Dq28j6aT6ozoX92L5l8/HM/EgLJph3M5c8XgJELo0ulIsL93UmniNwQoDSQR/t/GG+cLjEVCcd34hKVqso9M+wDziJfguKUt+uPSkWWDLX+/pdGe2lSzgKwHDQKBgFLthIOaBa3W7T0tA/YyHZ9Tvi7wuiB6hnwKcO0gPOh3b58T8kRFFuZ8A2EiD6MuUiM88WkxIV6zl+DCN9nKW1WoQoCYVN/lCBl+THrHqZUciAwtClG+R4qHPz8fyjb1tEZBuZ0Y6+vxzjKPMNrByy7DsmoqW1wOG2IMh9vfGMg5AoGBAKtPL0f/4JU579O/UnBWYZk/mvUPeOxqiNTo2DzlED0VV/OB0PaCXkLwXFBb3Xku2uTJLl1XW/XdHwb4AZqbNtVAEY582NHd5dLWi0q1nip6BGLKvYUrz9Q7kW7x2YacQP9sn41dOrvpl/fkRP3CSIUo649Gp025dB2nfteBgioJAoGBAMvkwypGe9bosHPRlaWxq6r/7Suc332d6hddJuBDQDqXwjyQBP9sBOUFxuWImM3RaOhXzHUUoGm4JaWvalzMbKI1sQWhyh1b66qCm931ANwm+SoVBNtZxIeylZm8Z6HOZLcxw9ALF+iJQIJ4FVrCjG/vTe/7MbijbDSzzjWhTmnm";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private ImageButton back;
    private RentAdapter RentGlaceAdapter;
    private TextView userName, userPhone, userLocation, price;
    private ListView productList1;
    private List<Product> productList = new ArrayList<>();
    private Button rent;
    private String id;
    private Product product;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userPhone.setText(findUserByPhoneNumberResult.getData().getPhoneNumber());
                        userName.setText(findUserByPhoneNumberResult.getData().getUserName());
                        Log.e("id",findUserByPhoneNumberResult.getData().getPhoneNumber());
                        Log.e("昵称",findUserByPhoneNumberResult.getData().getUserName());
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        price.setText("合计："+(product.getProductPrice()+product.getProductDeposit()));
                        productList.add(product);
                        RentGlaceAdapter.notifyDataSetChanged();

                    }
                    break;
                        //下面那个分支似乎没什么用
                case ADD_BY_PHONENUMBER:
                    Result<Product> addPhone = gson.fromJson(msg.obj.toString(), new TypeToken<Result<Product>>(){}.getType());
                    if (addPhone.getCode() == 200) {


                    } else {
                        Toast.makeText(RentGlaceActivity.this,"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_glace);

        back = findViewById(R.id.rentown_back);
        userName = findViewById(R.id.ownRenttv_user);
        userPhone = findViewById(R.id.ownRentv_phone);
        userLocation = findViewById(R.id.ownRenttv_location);
        price = findViewById(R.id.price);
        rent = findViewById(R.id.rent);

        //携带数据跳过来
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        //点击返回键返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取到当前用户的userName，phone，地点的话先传默认的
        //这里先默认一个用户
          (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);
//        (new Method()).findUserByPhoneNumber("12345678910",handler);


        //获取到商品列表(这里的电话号是商品发布者的电话号)
        (new Method()).findProductById(bundle.getString("id"),handler);
        //title用于记录商品名称
        id = bundle.getString("id");
        productList1 = findViewById(R.id.ownRentProduct);
        RentGlaceAdapter = new RentAdapter(productList,R.layout.me_push_layout,RentGlaceActivity.this);
        productList1.setAdapter(RentGlaceAdapter);
        //点击列表可以进入详情
        productList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(RentGlaceActivity.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //价格获取
        //在handler里

        //点击按钮添加
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //改变collection，同时将号码加到rentPhoneNumber里
                //查出这个商品的数据，在handler里改
                (new Method()).addRentPhone(bundle.getString("phone"),product,handler);
                //弹出支付页
                payV2(view);
                Intent intent1 = new Intent(RentGlaceActivity.this,SuccessActivity.class);
                startActivity(intent1);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(getActivity(), getString(R.string.pay_success));
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(getActivity(), getString(R.string.pay_failed));
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(getActivity(), getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(getActivity(), getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务
     * payV2
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            getActivity().finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        Log.e("orderParam", orderParam);
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        Log.e("privateKey", privateKey);
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        Log.e("sign", sign);
        final String orderInfo = orderParam + "&" + sign;
        Log.e("orderInfo", orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Activity getActivity() {
        return  RentGlaceActivity.this;
    }

    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }

}