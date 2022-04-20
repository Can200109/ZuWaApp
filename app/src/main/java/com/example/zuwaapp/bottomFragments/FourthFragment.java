package com.example.zuwaapp.bottomFragments;

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PRODUCTTYPE;
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
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class FourthFragment extends Fragment {
    private List<Product> waterList1 = new ArrayList<>();
    private List<Product> waterList2 = new ArrayList<>();
    private ProductAdapter waterFullAdapter;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler  = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_PRODUCT_BY_PRODUCTTYPE:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        //data为product的数组
                        List<Product> data = findResult.getData();
//                        Toast.makeText(getContext(),"查找成功",Toast.LENGTH_LONG).show();
                        for(int i = 0; i<data.size(); i++){
                            if(i<4){
                                waterList1.add(data.get(i));
                            }else {
                                waterList2.add(data.get(i));
                            }
                        }
                        waterFullAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.xiaoxi,
                container,
                false);

        //默认
        waterList1.clear();
        waterList2.clear();
        //调用线程获取数据
        new Method().findProductByProductType("节日礼服",handler);

        //第一个瀑布流的数据
        RecyclerView Rview1 = view.findViewById(R.id.waterfull1);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        Rview1.setLayoutManager(layoutManager);
        waterFullAdapter = new ProductAdapter(waterList1);
        Rview1.setAdapter(waterFullAdapter);
        waterFullAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                intent.setClass(getContext(), GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",waterList1.get(position).getPhoneNumber());
                bundle.putString("id",waterList1.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //第二个瀑布流的数据
        RecyclerView Rview2 = view.findViewById(R.id.waterfull2);
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview2.setLayoutManager(layoutManager2);
        waterFullAdapter = new ProductAdapter(waterList2);
        Rview2.setAdapter(waterFullAdapter);
        waterFullAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                intent.setClass(getContext(), GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",waterList2.get(position).getPhoneNumber());
                bundle.putString("id",waterList2.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


        return view;
    }

}
