package com.example.zuwaapp.method;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Collect;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.zuwaapp.Constant.ADD_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.ADD_PRODUCT;
import static com.example.zuwaapp.Constant.COLLECT_URL;
import static com.example.zuwaapp.Constant.DELETE;
import static com.example.zuwaapp.Constant.FIND_ALL;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.LOGIN;
import static com.example.zuwaapp.Constant.MODIFY;
import static com.example.zuwaapp.Constant.PRODUCT_URL;
import static com.example.zuwaapp.Constant.USER_URL;

public class Method {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    //此处用于接收线程中的数据
    /*private Handler handler0 = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case LOGIN:
                    *//*****************************************************
                     * 验证码登录时，
                     * 如果数据库中有数据就返回"账户不存在，注册并登录成功。
                     * "没数据就返回"登录成功"
                     * 安卓前端可根据此实现未注册用户进入先跳转到填写信息页面注册用户直接跳转到首页
                     * 由于在普通类中不存在getApplicationContext（）所以先把方法注释掉，如有需要请自行取用。
                     * *************************************************//*
                    Toast.makeText(getApplicationContext(), msg.obj.toString(),Toast.LENGTH_LONG).show();

                    break;
                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        findUserByPhoneNumberResult.getData().getPhoneNumber();
                    }else {
                        //Toast.makeText(getApplicationContext(),"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                case MODIFY:
                    Result<User> modifyResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (modifyResult.getCode()==200){
                        *//**此方法更新数据会返回更新后的数据如需取用请参考下边例子
                         * Result<>类型都是此格式取用数据
                         *
                         * *//*
                        modifyResult.getData().getPhoneNumber();
                    }
                    break;
                case DELETE:
                    Result<User> deleteResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Product>>>(){}.getType());
                    if (deleteResult.getCode()==200){
                        //Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                    }
                    break;
                case ADD_PRODUCT:
                    Result<Product> addResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Product>>>(){}.getType());
                    if (addResult.getCode()==200){
                        addResult.getData().getPhoneNumber();
                        //Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_LONG).show();
                    }else {
                        //Toast.makeText(getApplicationContext(),"添加失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                case FIND_ALL:
                    //findProductMethod(getContext(),msg);
                    break;
                case FIND_PRODUCT_BY_PHONENUMBER:
                    //findProductMethod(getContext(),msg);
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        findProductById.getData().getPhoneNumber();
                    }
                    break;


            }
        }
    };*/
    public void findProductMethod(Context context,Message msg){
        Result<List<Product>> findProductByPhoneNumber = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Product>>>(){}.getType());
        if (findProductByPhoneNumber.getCode()==200){
            Toast.makeText(context,"查找成功",Toast.LENGTH_LONG).show();
            List<Product> products = findProductByPhoneNumber.getData();
        }else {
            Toast.makeText(context,"查找失败",Toast.LENGTH_LONG).show();
        }
    }
    //登录功能，接受前端传过来的电话号码，实现登录，同事创建用户存到数据库中
    public void login(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(USER_URL + "login")
                .post(formBody)//设置请求方式为post请求
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,LOGIN,handler);
    }
    /*************************************************************************
     * 如果是修改先调用findUserByPhoneNumber()，先查找出来在editText中显示出来。然后点击修改完成的时候调用editUser更新数据
     * 如果是第一次注册登录，的话直接调用editUser()。
     * **********************************************************************/
    public void findUserByPhoneNumber(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(USER_URL + "findUserByPhoneNumber")
                .post(formBody)//设置请求方式为post请求
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,FIND_USER_BY_PHONENUMBER,handler);
    }


    public void editUser(@NonNull User user, Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",user.getPhoneNumber())//电话号码暂不可以修改，编辑页面电话号码弄成TextView
                .add("userName",user.getUserName())
                .add("userPassword", user.getUserPassword())
                .build();
        Request request = new Request.Builder()
                .url(USER_URL + "editUser")
                .post(formBody)//设置请求方式为post请求
                .build();
        Call call = okHttpClient.newCall(request);
       enqueue(call,MODIFY,handler);
        Log.e("okhttp","异步请求已发送" );

    }
    public void deleteUser(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(USER_URL+"deleteUser")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,DELETE,handler);
    }
    /**
     * 添加商品
     * 也可以
     * 修改商品信息
     * */
    public void addProduct(@NonNull Product product, Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",product.getPhoneNumber())
                .add("productName",product.getProductName())
                .add("productDescribe", product.getProductDescribe())
                .add("productType", product.getProductType())
                .add("productPrice",product.getProductPrice()+"")
                .add("productDeposit",product.getProductDeposit()+"")
                .add("productCount",product.getProductCount()+"")
                .add("productRent",product.getProductRent()+"")
                .add("rentPhoneNumber",product.getRentPhoneNumber())
                .build();
        Request request = new Request.Builder()
                .url(Constant.PRODUCT_URL+"addProduct")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,ADD_PRODUCT,handler);
    }
    public void findAllProduct(Handler handler){
        Request request = new Request.Builder()
                .url(Constant.PRODUCT_URL+"findAllProducts")
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,FIND_ALL,handler);
    }
    public void findProductByPhoneNumber(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"findProductByPhoneNumber")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,FIND_PRODUCT_BY_PHONENUMBER,handler);
    }
    public void addRentPhone(String phoneNumber,Product product,Handler handler){
        product.setProductRent(1);
//        product.setRentPhoneNumber(Constant.PHONENUMBER);
        product.setRentPhoneNumber("12345678910");
        FormBody formBody = new FormBody.Builder()
                .add("productId",product.getProductId())
                .add("productRent",product.getProductRent()+"")
                .add("rentPhoneNumber",product.getRentPhoneNumber())
                .add("productPhoto",product.getProductPhoto())
                .add("phoneNumber",phoneNumber)
                .add("productName",product.getProductName())
                .add("productDescribe", product.getProductDescribe())
                .add("productType", product.getProductType())
                .add("productPrice",product.getProductPrice()+"")
                .add("productDeposit",product.getProductDeposit()+"")
                .add("productCount",product.getProductCount()+"")
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"addProduct")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,ADD_BY_PHONENUMBER,handler);
    }


    /*
     *根据商品id查找商品信息
     *  */
    public void findProductById(String productId,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("productId",productId+"")
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"findProductById")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,FIND_PRODUCT_BY_ID,handler);
    }

    /**
     *product中id的值不能为空，和手机号必须有。其他的值随便有没有都可以
     * */
    public void deleteProduct(Product product,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("productId",product.getProductId())
                .add("phoneNumber",product.getPhoneNumber())
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"deleteProduct")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.DELETE_PRODUCT,handler);
    }
    public void findProductByProductType(String type,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("productType",type)
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"findProductByProductType")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.FIND_PRODUCT_BY_PRODUCTTYPE,handler);
    }
    public void findRents(String rentPhoneNumber, int productRent,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("rentPhoneNumber",rentPhoneNumber)
                .add("productRent",productRent+"")
                .build();
        Request request = new Request.Builder()
                .url(PRODUCT_URL+"findRents")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.FIND_RENT,handler);
    }
    /**
     * 注意!!!!!!!!!!!!!!!!!!!!!!
     * 此处电话号码为当前登录用户的电话号码并不是商品中的电话号
     * 返回值为Collect对象
     * */
    public void addCollect(String phoneNumber,String productId,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .add("productId",productId)
                .build();
        Request request = new Request.Builder()
                .url(COLLECT_URL+"addCollect")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.ADD_COLLECT,handler);
    }
    /**
     * 该方法可以没有返回值
     * */
    public void deleteCollect(String phoneNumber,String productId,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .add("productId",productId)
                .build();
        Request request = new Request.Builder()
                .url(COLLECT_URL+"deleteCollect")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.DELETE_COLLECT,handler);
    }
    /**
     * 返回的是JSON格式的Collect数组
     * 根据之前的查找进行解析
     * */
    public void findCollectByPhoneNumber(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(COLLECT_URL+"findCollectByPhoneNumber")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.FIND_COLLECT,handler);
    }

    public void findCollectColorByPhoneNumber(String phoneNumber,Handler handler){
        FormBody formBody = new FormBody.Builder()
                .add("phoneNumber",phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url(COLLECT_URL+"findCollectByPhoneNumber")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        enqueue(call,Constant.FIND_COLLECT_COLOR,handler);
    }

    public void enqueue(Call call,int constant,Handler handler){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp","请求失败执行");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = new Message();
                message.what = constant;
                message.obj = str;
                Log.e("okhttp","请求成功执行");
                Log.e("异步请求的结果",str);
                Log.e("onResponse",Thread.currentThread().getName());
                //如果需要将数据显示到界面，使用Handler实现
                handler.sendMessage(message);
            }
        });
    }
    /**
     * 访问服务器的图片
     * */


}
