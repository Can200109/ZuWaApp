package com.example.zuwaapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.adapter.VerticalProductAdapter;
import com.example.zuwaapp.buttonActivity.TwoRow;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PRODUCTTYPE;

public class MultiplexingFragment extends Fragment {
    private String name;
    private TextView mText,verticalType1,verticalType2,verticalType3;

    private List<Product> productList = new ArrayList<>();
    private RentAdapter listAdapter;

    private List<Product> waterList1 = new ArrayList<>();
    private List<Product> waterList2 = new ArrayList<>();
    private List<Product> waterList3 = new ArrayList<>();
    private VerticalProductAdapter waterFullAdapter;

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
                        //data???product?????????
                        List<Product> data = findResult.getData();
//                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_LONG).show();
                        for(int i = 0; i<data.size(); i++){
                            if(i==0){
                                productList.add(data.get(i));
                            } else if(i<5&&i>0){
                                waterList1.add(data.get(i));
                            }else if(4<i&&i<9){
                                waterList2.add(data.get(i));
                            }else {
                                waterList3.add(data.get(i));
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                        waterFullAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



    public MultiplexingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("string");
        }
    }

    //??????fragment
    public static Fragment getMultiplexing(String string, String string2) {
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("string", string);
        multiplexingFragment.setArguments(bundle);
        return multiplexingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiplexing, container, false);

        verticalType1 = view.findViewById(R.id.vertical_type1);
        verticalType2 = view.findViewById(R.id.vertical_type2);
        verticalType3 = view.findViewById(R.id.vertical_type3);


        //??????
        waterList1.clear();
        waterList2.clear();
        //????????????????????????
        new Method().findProductByProductType(name,handler);

        //???????????????????????????
        RecyclerView Rview1 = view.findViewById(R.id.sort_waterfull1);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview1.setLayoutManager(layoutManager);
        waterFullAdapter = new VerticalProductAdapter(waterList1);
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

        //???????????????????????????
        RecyclerView Rview2 = view.findViewById(R.id.sort_waterfull2);
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview2.setLayoutManager(layoutManager2);
        waterFullAdapter = new VerticalProductAdapter(waterList2);
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

        //???san?????????????????????
        RecyclerView Rview3 = view.findViewById(R.id.sort_waterfull3);
        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview3.setLayoutManager(layoutManager3);
        waterFullAdapter = new VerticalProductAdapter(waterList3);
        Rview3.setAdapter(waterFullAdapter);
        waterFullAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                intent.setClass(getContext(), GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",waterList3.get(position).getPhoneNumber());
                bundle.putString("id",waterList3.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


        //??????????????????????????????
        ListView listView = view.findViewById(R.id.vertical_list);
        listAdapter = new RentAdapter(productList,R.layout.rubbish_img,getContext());

        listView.setAdapter(listAdapter);


        //??????????????????????????????????????????????????????
        if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("??????vr".equals(name)){
            verticalType1.setText("??????VR");
            verticalType2.setText("??????VR");
            verticalType3.setText("??????VR");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("K?????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("?????????");
            verticalType3.setText("?????????");
        }else if("????????????".equals(name)){
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("?????????");
        }else{
            verticalType1.setText("????????????");
            verticalType2.setText("????????????");
            verticalType3.setText("????????????");
        }

        return view;
    }


}
