package com.example.zuwaapp.alipay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.PayFailActivity;
import com.example.zuwaapp.activity.RentGlaceActivity;
import com.example.zuwaapp.activity.SuccessActivity;
import com.example.zuwaapp.adapter.RentAdapter;
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

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;


/**
 * 这个页面加了一点操作
 * **/
public class PayActivity extends AppCompatActivity {
    private String productId,productNumber;

    private ImageView ownerPhoto;
    private RentAdapter RentGlaceAdapter;
    private TextView userName, userPhone, userLocation, ownerName, dayPrice, mortPrice, totalPrice;
    private ListView productList1;
    private List<Product> productList = new ArrayList<>();
    private Button rentPayment, back;
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
                        User user = findUserByPhoneNumberResult.getData();
                        if(Constant.PHONENUMBER.equals(user.getPhoneNumber())){
                            userPhone.setText(user.getPhoneNumber());
                            userName.setText(user.getUserName());
                        }else {
                            ownerName.setText(user.getUserName());
                            String userPhoto = user.getUserPhoto();
                            if (userPhoto!=null){
                                String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto;
                                Log.e("图片在 : ", url);
                                Glide.with(PayActivity.this)
                                        .load(url)
                                        .placeholder(R.drawable.loading)
                                        .centerCrop()
                                        .dontAnimate()
                                        .into(ownerPhoto);
                            }
                        }
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        dayPrice.setText(product.getProductPrice()+"");
                        mortPrice.setText(product.getProductDeposit()+"");
                        totalPrice.setText(""+(product.getProductPrice()*30+product.getProductDeposit()));
                        productNumber = product.getPhoneNumber();

                        new Method().findUserByPhoneNumber(product.getPhoneNumber(),handler);

                        productList.add(product);
                        RentGlaceAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };



    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2021000118655951";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "2088622957274260";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "xmxwao5071@sandbox.com";

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
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQChYRdH2m1MJfMS4W5herVz0UU6E4N+Q9VZEaLVFJxOsXQuVo/lQMxB2IH1NSzGdOMXM39wMfyqCmLuysz4VFZAV71mGUTQXO75NT8nfN6q/xn8euLxjQBqELn+eLHh/uL57OGJRAsvomUIQFf5nSTJCqy4NTHJ3Tvfky4nwoeL4f3T3xJBg/trS9hhDnjQoVqqZquAhxrFi3qy39vLe0Ozb9B+7UIT69rz+wYYveLp2TSjkEBR0EynqcZahIBmkTZUTTyBz6XMjkqj2Bj4oIEr3uLyd3e1ICnpfNu+dR7hAh7MnIaT7xTdUYTJw8EjLrh/p3m3Jo7V7eijbOlWcqCrAgMBAAECggEAf+XW8ZdnMAaTks8fEBqUXGnql5CHkjZoi+KyTnW7O5LgJSlteZ/VQgo5VQEDr3VL1UN3KJA1Uv5QUjhgIh+VDAF9lcFJDGna658LolwQjS0Ih1VNL2s0fNsszUUSla3QbYsTZBqNUv89W0DDeIzWbo8LPCLMhtPTc+nNlq1Rh8YaMCJ+bhCvcRA7swQNmxHd+j59QBtbxwicViUtbrtRh4o3JaJrV2R5HZv1VodBqoWXEi43HT5w70sRkofEZ/dSl+wJUp+5Ynv3k54PPnKP2ygkCTFPKzW+shths39GOPsyJD2IJjm7LgFLb3QnxG+BSzTbgLtEqBe2DPqUSNdF4QKBgQDrG1BwFhYLKDEpaPKIDfeMa9kog2CA9IWYw+WL7gvJi3F5UFC1UT9jOYl+/MgMRjZxGtwD+/L3v7OZ5+ZXK6qXY/dH3w/+tc4ga8xjObRiR3+PQi4Vv3ckrTLBFRQPu5CEG6hqfVI/wjAZKYWK7ufjZr9CDjAxB26n/2fZ7RZErwKBgQCvuHmVughmCAAGY7IHM1FWyk99mAd9tfPJiEtXyndDacOithDiMlIrlBem6sSwFmaa9ifbrNb5AXQCs/yAuhB2mwQIjfa8jDTPCfGh7f41LmwRGrOvOGkxhCkK/mK5+djHCH1xYHnFq56uypzZDDMW0F1j7qQ0+tqpIIl11YoaxQKBgQCPuaLy4o2XRHrJVd7uTZuHvGaE3cuhCdqiCR/K6ztJenVkNDUiowidIA9uEho5ZrLhcgoOZXei/Q5Oh/XIVGl1IyeISFT+4SWhrETGZABiFFNmwI5rvSCYy0i7wKcC9ybhnAbZEsO1NoEw4LFCvcoEuksxlnrffiunh/t0vftfAQKBgQClfS38pcRtGxGy8sdiYqEzBLuSQ9CiQu8CmPqkBokFkyDtJ1yFOvEKG6Ge0LFGSQJ+5F6rU37t6cFxHkMqlJEWsm09BT4fWkgNyIapgzMKsNqTzTr2RUFEofvpX6RpE/VllQhW844bbMK4SRcojoW68adSixhY0PuHlp3+vIohJQKBgFjKYXElmKvNfcg1lhFt4YJ1d2Z1kfp11vet/A2mMwzoxOUTodGdJOJ3TeOlXd04kMARLr81ueP9XFMx4iCnlsCKK8UnlAWPiQWxijdfbSxTDaqRbDiEikklc9GR67J+jK+L4WjApsd32+2swRGTTipItLib7QpnB1OeifSux9yK";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

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
                        //showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
                        //跳转到成功页面，需要把商品id传过去
                        Intent intent = new Intent(PayActivity.this, SuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId",productId);
                        bundle.putString("productNumber",productNumber);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //showAlert(PayActivity.this, getString(R.string.pay_failed) + payResult);
                        //如果支付失败，则跳转到失败页面，然后加入待付款
                        Intent intent1 = new Intent(PayActivity.this, PayFailActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("productId",productId);
                        bundle1.putString("productNumber",productNumber);
                        intent1.putExtra("bundle",bundle1);
                        startActivity(intent1);
                        finish();
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
                        showAlert(PayActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(PayActivity.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        init();

//        needPrice = findViewById(R.id.need_price);
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("bundle1");
//        if(!bundle.isEmpty()){
//            price = bundle.getString("price");
//            productId = bundle.getString("productId");
//            Log.e("接收到的productId",""+productId);
//
//            Log.e("price的价格是",""+price);
//            needPrice.setText(bundle.getString("price"));
//        }
//
//        Button button = findViewById(R.id.button_pay);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                payV2(v);
//            }
//        });

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


//        (new Method()).findUserByPhoneNumber("12345678910",handler);


        //获取到商品列表(这里的电话号是商品发布者的电话号)
        (new Method()).findProductById(bundle.getString("id"),handler);
        productId = bundle.getString("id");
        Log.e("productId...id",bundle.getString("id")+"");

        //获取到当前用户的userName，phone，地点的话先传默认的
        //这里先默认一个用户
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

        productList1 = findViewById(R.id.ownRentProduct);
        RentGlaceAdapter = new RentAdapter(productList,R.layout.rent_product_layout,PayActivity.this);
        productList1.setAdapter(RentGlaceAdapter);
        //点击列表可以进入详情
        productList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                finish();
            }
        });

        //点击按钮付款
        rentPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payV2(v);
            }
        });

    }


    /**
     * 支付宝支付业务示例
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);

        /**
         * 价格在这里设计，价格在这里设计
         *
         * **/

        Log.e("支付步骤","走到了这里"+totalPrice.getText().toString());
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,totalPrice.getText().toString());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务示例
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            showAlert(this, getString(R.string.error_auth_missing_partner_appid_rsa_private_target_id));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * 获取支付宝 SDK 版本号。
     */
    public void showSdkVersion(View v) {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        showAlert(this, getString(R.string.alipay_sdk_version_is) + version);
    }



    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            new AlertDialog.Builder(ctx)
                    .setMessage(info)
                    .setPositiveButton(R.string.confirm, null)
                    .setOnDismissListener(onDismiss)
                    .show();
        }
    }

    private static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    private static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (String key: bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
        }
        return sb.toString();
    }

    private void init(){

        back = findViewById(R.id.detail_glace_back);
        userName = findViewById(R.id.buyer_name);
        userPhone = findViewById(R.id.buyer_phone);
        userLocation = findViewById(R.id.buyer_address);
        dayPrice = findViewById(R.id.day_price);
        mortPrice = findViewById(R.id.mort_price);
        totalPrice = findViewById(R.id.total_price);
        rentPayment = findViewById(R.id.rent_payment);
        ownerPhoto = findViewById(R.id.owner_photo);
        ownerName = findViewById(R.id.owner_name);
    }

}