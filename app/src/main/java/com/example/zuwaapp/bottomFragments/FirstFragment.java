package com.example.zuwaapp.bottomFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuwaapp.CitySelect.CityPickerActivity;
import com.example.zuwaapp.CitySelect.bean.LocateState;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.ResultActivity;
import com.example.zuwaapp.activity.SearchActivity;
import com.example.zuwaapp.activity.SearchPage;
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.buttonActivity.TwoRow;
import com.example.zuwaapp.buttonActivity.VerticalSortActivity;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.fragment.FragmentFive;
import com.example.zuwaapp.fragment.FragmentFour;
import com.example.zuwaapp.fragment.FragmentOne;
import com.example.zuwaapp.fragment.FragmentSix;
import com.example.zuwaapp.fragment.FragmentThree;
import com.example.zuwaapp.fragment.FragmentTwo;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.zuwaapp.Constant.FIND_ALL;
import static com.example.zuwaapp.Constant.LOAD_MORE;
import static com.example.zuwaapp.Constant.REFRESH;
import static com.example.zuwaapp.Constant.getRandomThreeInfoList;


public class FirstFragment extends Fragment {
    private int locateState = LocateState.LOCATING;
    private TextView btn;


    private final int REQUEST_CODE=1;
    private TextView btnSearch,btnCode;
    private EditText edtSearch;
    private Button btnClothes,btnComputer,btnPhone, btnPhoto,btnVr,btnDesk,btnSport,btnMore,btnEar,btnWatch;


    private ViewPager mViewPager;
    private List<ImageView> images;
    //???????????????
    private int currentItem;
    //?????????????????????
    private int oldPosition = 0;
    //???????????????id
    private int[] imageIds = new int[]{R.mipmap.ban1,R.mipmap.ban2,R.mipmap.ban3};
    private ViewPagerAdapter adapter;
    //??????????????????
    private ScheduledExecutorService scheduledExecutorService;
    //??????
    private SmartRefreshLayout refreshLayout;


    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler  = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_ALL:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
                        List<Product> ramData = new Constant().getRandomThreeInfoList(data,6);
//                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_LONG).show();
                        for(Product product:ramData){
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_LONG).show();
                    }
                    break;
                case REFRESH:
                    productList.clear();
                    new Method().findAllProduct(handler);
                    refreshLayout.finishRefresh();
                    break;
                case LOAD_MORE:
                    new Method().findAllProduct(handler);
                    refreshLayout.finishLoadMore();
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
        }
        View view = inflater.inflate(R.layout.shouye,container,false);

        initviews(view);
        ZXingLibrary.initDisplayOpinion(this.getContext());
        //????????????????????????
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchPage.class);
                startActivity(intent);

            }
        });
        //????????????????????????
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("??????","??????????????????");
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);


            }
        });
        //?????????????????????
        getImage();
        //???????????????
        setView(view);


        //??????????????????
        productList.clear();
        new Method().findAllProduct(handler);
        RecyclerView Rview = view.findViewById(R.id.listRecommend);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(productList);
        Rview.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                //???????????????
                //?????????????????????????????????  ??????   ??????   ??????    ??????    ??????   ??????
                //???????????????????????????2022????????????????????????????????????id???phone????????????????????????????????????????????????????????????
                intent.setClass(getActivity(), GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //????????????
        setListener();


        //???????????????????????????
        //?????????adapter?????????????????????????????????id???????????????????????????????????????????????????????????????????????????????????????
        //????????????????????????????????????????????????????????????????????????????????????bug????????????
        btnClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnClothes.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnComputer.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnPhone.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnDesk.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnPhoto.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnSport.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        btnVr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnVr.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        btnEar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnEar.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);

            }
        });

        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TwoRow.class);
                Bundle bundle = new Bundle();
                String text = btnWatch.getText().toString();
                bundle.putString("text",text);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), VerticalSortActivity.class);
                startActivity(intent);
            }
        });



        //????????????

        btn=view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getContext(), CityPickerActivity.class);
                startActivityForResult(intent,100);
                // getActivity().finish();
                //   startActivity(new Intent(getActivity(), CityPickerActivity.class));
            }

        });



        return view;
    }



    //?????????????????????
    private void getImage() {

    }

    //UI???????????????
    private void setView(View view) {
        //???????????????
        images = new ArrayList<ImageView>();
        for(int i = 0;i<imageIds.length;i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //???????????????

        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
    //??????????????????
    private class ViewPagerAdapter extends PagerAdapter {
        //?????????????????????????????????
        @Override
        public int getCount() {
            return images.size();
        }
        //??????instantiateItem??????????????????key??????????????????????????????????????????
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(images.get(position));
        }
        //??????????????????????????????????????????????????????????????????????????????????????????

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

    }
    //???????????????????????????????????????
    public void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),2,2, TimeUnit.SECONDS );
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem+1) % imageIds.length;
            mHandler.sendEmptyMessage(0);//??????????????????


        }
    }
    //?????????????????????????????????
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    mViewPager.setCurrentItem(currentItem);
                    break;
            }
        }
    };
    //????????????????????????????????????????????????
    public void onStop() {
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //???????????????????????????
        if(requestCode == 100){
            if(requestCode==100){
                String result=data.getStringExtra("name");
                btn.setText(result);
            }
        }

        switch (requestCode) {
            case REQUEST_CODE: {
                //??????????????????
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    Intent intent = new Intent();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        intent.setClass(getActivity(), ResultActivity.class);
                        intent.putExtra("result",result);
                        startActivity(intent);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        intent.setClass(getActivity(),ResultActivity.class);
                        intent.putExtra("result","?????????????????????");
                        startActivity(intent);
                    }
                }
            }

            break;


        }
    }





    private void setListener() {
        //??????????????????
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //??????????????????????????????????????????????????????
                new Thread() {
                    @Override
                    public void run() {
                        //??????????????????????????????
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //????????????????????????????????????
                        Message message = new Message();
                        message.what = REFRESH;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });

        //????????????????????????
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Thread() {
                    @Override
                    public void run() {
                        //???????????????????????????
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Message message = new Message();
                        message.what = LOAD_MORE;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });
    }






    //?????????????????????
    private void initviews(View view) {
        mViewPager = view.findViewById(R.id.viewpager);
        btnSearch = view.findViewById(R.id.btn_search);
        btnCode = view.findViewById(R.id.btn_code);
//        edtSearch = view.findViewById(R.id.edt_search);
        btnClothes = view.findViewById(R.id.btn_clothes);
        btnComputer = view.findViewById(R.id.btn_computer);
        btnPhone = view.findViewById(R.id.btn_phone);
        btnDesk = view.findViewById(R.id.btn_desk);
        btnPhoto = view.findViewById(R.id.btn_photo);
        btnSport = view.findViewById(R.id.btn_sport);
        btnVr = view.findViewById(R.id.btn_vr);
        btnMore = view.findViewById(R.id.btn_more);
        btnEar = view.findViewById(R.id.btn_ear);
        btnWatch = view.findViewById(R.id.btn_watch);
        refreshLayout = view.findViewById(R.id.srl);

    }
}