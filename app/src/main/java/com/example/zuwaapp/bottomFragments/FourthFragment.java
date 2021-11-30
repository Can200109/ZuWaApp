package com.example.zuwaapp.bottomFragments;

import static com.example.zuwaapp.Constant.FIND_RENT;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.Glace2Activity;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.SearchActivity;
import com.example.zuwaapp.activity.SecondActivity;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class FourthFragment extends Fragment {

    private RentAdapter MePushAdapter;
    private List<Product> productList = new ArrayList<>();
    private ImageButton button;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_RENT:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
                        for (Product product:data){
                            productList.add(product);
                        }
                        MePushAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"无数据",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    private static FourthFragment mf;
    public static FourthFragment getMseeageFragemnt(){
        if(mf == null){
            mf = new FourthFragment();
        }
        return mf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.xiaoxi,
                container,
                false);
        button = view.findViewById(R.id.btn_search_zujie);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = "";
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("context",context);
                Log.e("内容",context);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //先查出全部列表，然后根据条件添加
        productList.clear();
        new Method().findRents("12345678910",1,handler);
//        new Method().findRents(Constant.PHONENUMBER,1,handler);

        ListView listView = view.findViewById(R.id.WoZuJieList);
        MePushAdapter = new RentAdapter(productList,R.layout.me_push_layout,getContext());
        listView.setAdapter(MePushAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("FirstA","点了事件"+position);
                Intent intent = new Intent();
                intent.setClass(getContext(), Glace2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });



        return view;
    }

}
