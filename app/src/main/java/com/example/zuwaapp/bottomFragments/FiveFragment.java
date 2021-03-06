package com.example.zuwaapp.bottomFragments;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.usercenterActivity.MyCollect;
import com.example.zuwaapp.usercenterActivity.MyCoupon;
import com.example.zuwaapp.usercenterActivity.MyRelease;
import com.example.zuwaapp.usercenterActivity.MyRent;
import com.example.zuwaapp.usercenterActivity.UserSetting;
import com.example.zuwaapp.usercenterActivity.orderType.MyNoAssess;
import com.example.zuwaapp.usercenterActivity.orderType.MyNoGoods;
import com.example.zuwaapp.usercenterActivity.orderType.MyNoHarvest;
import com.example.zuwaapp.usercenterActivity.orderType.MyNoPayment;
import com.example.zuwaapp.usercenterActivity.orderType.MyNoReturn;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

public class FiveFragment extends Fragment {
    private ImageView ivUserPhoto;
    private TextView userNick, userPhoneNumber;
    private Button userSetting, myRelease, myCollect, myBorrow, myCoupon, myNoPayment, myNoGoods, myNoHarvest, myNoReturn, myNoAssess;

    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
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
                        User user = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_LONG).show();
                        userPhoneNumber.setText(user.getPhoneNumber());
                        userNick.setText(user.getUserName());
                        String userPhoto = user.getUserPhoto();
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto;
                            Log.e("????????? : ", url);
                            Glide.with(getActivity())
                                    .load(url)
                                    .placeholder(R.drawable.loading)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(ivUserPhoto);
                        }

                    }
                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wode,
                container,
                false);

        uriList.clear();

        ivUserPhoto = view.findViewById(R.id.user_photo);
        userNick = view.findViewById(R.id.user_nick);
        userPhoneNumber = view.findViewById(R.id.user_phoneNumber);
        userSetting = view.findViewById(R.id.user_setting);
        myRelease = view.findViewById(R.id.my_release);
        myCollect = view.findViewById(R.id.my_collect);
        myBorrow = view.findViewById(R.id.my_borrow);
        myCoupon = view.findViewById(R.id.my_coupon);
        myNoPayment = view.findViewById(R.id.my_no_payment);
        myNoGoods = view.findViewById(R.id.my_no_goods);
        myNoHarvest = view.findViewById(R.id.my_no_harvest);
        myNoReturn = view.findViewById(R.id.my_no_return);
        myNoAssess = view.findViewById(R.id.my_no_assess);

        //??????handler
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

        //???????????????????????????????????????????????????
        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSetting.class);
                startActivity(intent);
            }
        });

        userNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSetting.class);
                startActivity(intent);
            }
        });

        userPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSetting.class);
                startActivity(intent);
            }
        });


        //??????????????????????????????
        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSetting.class);
                startActivity(intent);
            }
        });

        //??????????????????
        myRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyRelease.class);
                startActivity(intent);
            }
        });

        //??????????????????
        myCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCollect.class);
                startActivity(intent);

            }
        });

        //??????????????????
        myBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyRent.class);
                startActivity(intent);
            }
        });

        //???????????????
        myCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCoupon.class);
                startActivity(intent);

            }
        });

        /**
         * ????????????????????????????????????????????????.??????????????????????????????????????????????????????????????????????????????
         * 2022/5/1
         * x_heng
         * ??????????????????????????????
         * **/

        //?????????
        myNoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNoPayment.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","?????????");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        //?????????
        myNoGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNoGoods.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","?????????");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        //?????????
        myNoHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNoHarvest.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","?????????");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        //?????????
        myNoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNoReturn.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","?????????");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        //?????????
        myNoAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyNoAssess.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","?????????");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        /**
         * ??????????????????????????????????????????
         * **/


        return view;
    }

}

