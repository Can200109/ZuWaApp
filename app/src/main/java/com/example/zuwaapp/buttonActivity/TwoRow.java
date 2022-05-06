package com.example.zuwaapp.buttonActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.adapter.RentAdapter;
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

public class TwoRow extends AppCompatActivity {
    private TextView text, twoRowType1, twoRowType2;
    private Button twoRowReturn;

    private List<Product> productList = new ArrayList<>();
    private List<Product> waterList1 = new ArrayList<>();
    private List<Product> waterList2 = new ArrayList<>();
    private RentAdapter listAdapter;
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
                            if(i<1){
                                productList.add(data.get(i));
                            }else if(i>=1&&i<5){
                                waterList1.add(data.get(i));
                            }else {
                                waterList2.add(data.get(i));
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                        waterFullAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TwoRow.this,"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_row);
        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        text.setText(bundle.getString("text"));
        Log.e("节日礼服0", bundle.getString("text"));
        //写入两个waterType, 简单粗暴的if else
        if("节日礼服".equals(bundle.getString("text"))){
            Log.e("节日礼服", text.toString());
            twoRowType1.setText("精品礼服");
            twoRowType2.setText("热搜礼服");
        }else if ("电脑设备".equals(bundle.getString("text"))){
            twoRowType1.setText("游戏电脑");
            twoRowType2.setText("品牌电脑");
        }else if ("唯美相机".equals(bundle.getString("text"))){
            twoRowType1.setText("微单相机");
            twoRowType2.setText("全景相机");
        }
        else if ("精品手机".equals(bundle.getString("text"))){
            twoRowType1.setText("爆款机型");
            twoRowType2.setText("热租机型");
        }
        else if ("耳机乐器".equals(bundle.getString("text"))){
            twoRowType1.setText("低音降噪");
            twoRowType2.setText("3D环绕");
        }
        else if ("手表饰品".equals(bundle.getString("text"))){
            twoRowType1.setText("真卡西欧");
            twoRowType2.setText("劳力士水鬼");
        }
        else if ("体感VR".equals(bundle.getString("text"))){
            twoRowType1.setText("3D全景");
            twoRowType2.setText("模拟人生");
        }
        else if ("聚会用品".equals(bundle.getString("text"))){
            twoRowType1.setText("K歌必备");
            twoRowType2.setText("基础设备");
        }
        else if ("运动健身".equals(bundle.getString("text"))){
            twoRowType1.setText("运动器材");
            twoRowType2.setText("动感单车");
        }

        //返回键
        twoRowReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //产品
        //推荐列表，要前三个
        productList.clear();
        waterList1.clear();
        waterList2.clear();
        //调用线程获取数据
        new Method().findProductByProductType(bundle.getString("text"),handler);
        //绑定list
        ListView listView = findViewById(R.id.two_row_list);
        listAdapter = new RentAdapter(productList,R.layout.two_row_list,TwoRow.this);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(TwoRow.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        //第一个瀑布流的数据
        RecyclerView Rview1 = findViewById(R.id.two_row_waterfull1);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview1.setLayoutManager(layoutManager);
        waterFullAdapter = new ProductAdapter(waterList1);
        Rview1.setAdapter(waterFullAdapter);
        waterFullAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                intent.setClass(TwoRow.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",waterList1.get(position).getPhoneNumber());
                bundle.putString("id",waterList1.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //第二个瀑布流的数据
        RecyclerView Rview2 = findViewById(R.id.two_row_waterfull2);
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview2.setLayoutManager(layoutManager2);
        waterFullAdapter = new ProductAdapter(waterList2);
        Rview2.setAdapter(waterFullAdapter);
        waterFullAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                intent.setClass(TwoRow.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",waterList2.get(position).getPhoneNumber());
                bundle.putString("id",waterList2.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


    }
    public void init(){
        text = findViewById(R.id.btn_text);
        twoRowReturn = findViewById(R.id.two_row_return);
        twoRowType1 = findViewById(R.id.two_row_type1);
        twoRowType2 = findViewById(R.id.two_row_type2);
    }

}