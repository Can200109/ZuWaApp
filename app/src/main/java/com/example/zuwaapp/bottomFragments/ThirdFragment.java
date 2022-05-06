package com.example.zuwaapp.bottomFragments;

import static android.app.Activity.RESULT_OK;
import static com.example.zuwaapp.Constant.ADD_PRODUCT;
import static com.example.zuwaapp.Constant.PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_URL;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.util.GlideLoadImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yds.library.MultiImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ThirdFragment extends Fragment {
    private MultiImageView multiImageView;

    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private EditText Title, describe ,price ,RvPrice;
    private Spinner fenLei;
    private Button button;
    private String type;
    private TextView tvAdd,tvClear;
    private static ThirdFragment pf;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ADD_PRODUCT:
                    Result<Product> addResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if (addResult.getCode()==200){
                        Product product = addResult.getData();
                        uploadImage(uriList, product.getProductId(),product.getPhoneNumber());
                        Toast.makeText(getContext(),"添加成功",Toast.LENGTH_LONG).show();
                        Title.setText(null);
                        describe.setText(null);
                        price.setText(null);
                        RvPrice.setText(null);
                        uriList.clear();
                        multiImageView.setImagesData(uriList);
                        multiImageView.setMultiImageLoader(new GlideLoadImage());
                    }else {
                        Toast.makeText(getContext(),"添加失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.jiahao,
                container,
                false);

        initView(view);
        fenLei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) fenLei.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Title.getText().toString()==null||"".equals(Title.getText().toString())||describe.getText().toString() == null||"".equals(describe.getText().toString()) ||price.getText().toString() == null ||"".equals(price.getText().toString())|| RvPrice.getText().toString() ==null||"".equals(RvPrice.getText().toString())){
                    Toast.makeText(getContext(),"*.* 请确认所有项都已经填过了 *.*",Toast.LENGTH_LONG).show();
                }else {
                    String title1 = Title.getText().toString();
                    String describe1 = describe.getText().toString();
                    String price1 =  price.getText().toString();
                    Double price2 = Double.parseDouble(price1);
                    String RVprice1 = RvPrice.getText().toString();
                    Double RVprice2 = Double.parseDouble(RVprice1);


//                Product product = new Product(title1,describe1,price2,RVprice2,type, "12345678910");
                    Product product = new Product(title1,describe1,price2,RVprice2,type, PHONENUMBER);
                    (new Method()).addProduct(product,handler);
                }




            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriList.clear();
                multiImageView.setImagesData(uriList);
                multiImageView.setMultiImageLoader(new GlideLoadImage());
            }
        });
        return view;
    }
    private void initView(View view){
        uriList.clear();
        tvAdd = view.findViewById(R.id.add_photo);
        tvClear = view.findViewById(R.id.clear_photo);
        Title = view.findViewById(R.id.push_title);
        describe = view.findViewById(R.id.zhengwen);
        fenLei = view.findViewById(R.id.fenlei);
        button = view.findViewById(R.id.button);
        price = view.findViewById(R.id.push_price);
        RvPrice = view.findViewById(R.id.push_RVprice);
        multiImageView = view.findViewById(R.id.image_multi);

    }
    private void pickImage(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"},
                Constant.REQUEST_IMAGE
        );
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            uriList.add(uri);
            /**
             *将选择的图片显示到界面上的ivPhoto控件
             * */
            multiImageView.setImagesData(uriList);
            multiImageView.setMultiImageLoader(new GlideLoadImage());

        }
    }

    public void uploadImage(List<Uri> uriList, String productId, String phoneNumber){
        List<String> urlList = getUrlList(uriList);
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.addFormDataPart("productId",productId);
        multipartBodyBuilder.addFormDataPart("phoneNumber",phoneNumber);
        multipartBodyBuilder.setType(MultipartBody.FORM);
        for (String url:urlList) {
            File imageFile = new File(url);
            multipartBodyBuilder.addFormDataPart("file",
                    imageFile.getName(),
                    RequestBody.create(MediaType.parse("application/octet-stream"), imageFile)
            );
        }
            //创建请求体对象
            RequestBody requestBody = multipartBodyBuilder.build();
            //创建请求对象
        Request request = new Request.Builder()
                .url(PRODUCT_URL + "uploadPhoto")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        //发起异步请求，不需要手动创建子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp","请求失败时执行");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("okhttp","请求成功时执行");
                Log.e("异步请求的结果",response.body().string());
                Log.e("onResponse",Thread.currentThread().getName());//如果需要将数据显示到界面，使用Handler实现
            }
        });
    }
    public List<String> getUrlList(List<Uri> uriList) {
        List<String> urlList = new ArrayList<>();
        for (Uri uri:uriList){
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(uri,
                    null,null,null,null);
            if (cursor.moveToFirst()) {
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                urlList.add(imagePath);
            }
        }
        return urlList;
    }
}
