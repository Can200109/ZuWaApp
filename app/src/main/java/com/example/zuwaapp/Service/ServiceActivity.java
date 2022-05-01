package com.example.zuwaapp.Service;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.MyFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class ServiceActivity extends AppCompatActivity implements MyScrollView.OnScrollListener{
    private Button back;
    private TabLayout tabLayout1;
    private ViewPager2 vp3;
    //  private DatagramSocket ButterKnife;
    /**
     * 顶部固定的TabViewLayout
     */
    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    private LinearLayout mTabViewLayout;

    /**
     * 要悬浮在顶部的View的子View
     */
    private LinearLayout mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        SystemUtil.setStatusBarColor(this, Color.parseColor("#00CDCD"));

        setContentView(R.layout.service_helpcentre);


        MyScrollView mMyScrollView = (MyScrollView) findViewById(R.id.my_scrollview);
        mTabViewLayout = (LinearLayout) findViewById(R.id.ll_tabView);
        mTopTabViewLayout = (LinearLayout) findViewById(R.id.ll_tabTopView);
        mTopView = (LinearLayout) findViewById(R.id.tv_topView);
        back = findViewById(R.id.help_center_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //滑动监听
        mMyScrollView.setOnScrollListener(this);


        tabLayout1 = findViewById(R.id.tablayout1);
        vp3 = findViewById(R.id.vp3);
        //viewpager2响应滑动效果
        vp3.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //设置标签排列效果
        //如果标签少 按比例平均分配
        tabLayout1.setTabMode(TabLayout.MODE_FIXED);
        //准备fragment数据源
        List<Fragment> fragments1 = setData();
        //实例化适配器对象
        MyFragmentAdapter adapter = new MyFragmentAdapter(
                fragments1, this
        );
        //绑定适配器
        vp3.setAdapter(adapter);
        //关联TabLayout和ViewPager2
        TabLayoutMediator mediator = new TabLayoutMediator(
                tabLayout1,
                vp3,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setIcon(R.drawable.shixiang);
                                tab.setText("常见问题");
                                break;
                            case 1:
                                tab.setIcon(R.drawable.qiandai);
                                tab.setText("支付问题");
                                break;
                            case 2:
                                tab.setIcon(R.drawable.diannao);
                                tab.setText("设备维修");
                                break;
                            case 3:
                                tab.setIcon(R.drawable.anquansuo);
                                tab.setText("租赁保障");
                                break;
                        }
                    }
                }
        );
        mediator.attach();

    }

    private List<Fragment> setData() {
        List<Fragment> fragments1 = new ArrayList<>();
        fragments1.add(new FirstFragment());
        fragments1.add(new SecondFragment());
        fragments1.add(new ThirdFragment());
        fragments1.add(new FourthFragment());
        return fragments1;
    }



    @Override
    public void onScroll(int scrollY) {
        int mHeight = mTabViewLayout.getTop();
        //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
        if (scrollY > 0 && scrollY >= mHeight) {
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
            }

        } else {
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
            }

        }
    }
}
//    public void onScroll(int scrollY) {
//        int mHeight = mTabViewLayout.getTop();
//        if (scrollY > 0 && scrollY >= mHeight) {
//            if (mTopView.getParent() != mTopTabViewLayout) {
//                mTabViewLayout.removeView(mTopView);
//                mTopTabViewLayout.addView(mTopView);
//            } else {
//                if (mTopView.getParent() != mTopTabViewLayout) {
//                    mTopTabViewLayout.removeView(mTopView);
//                    mTabViewLayout.addView(mTopView);
//                }
//            }
//        }
//    }



  /*  //标签中的三个ImageView对象
    private Map<String, ImageView> iconMap = new HashMap<>();
    //标签中的三个TextView对象
    private Map<String, TextView> titleMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_helpcentre);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取FragmentTabHost的引用
        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        //初始化
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//作用：用来动态的添加、显示、替换、移除某一个Fragment
                android.R.id.tabcontent);//显示内容页面的布局id值

        //创建标签的TabSpc对象
        TabHost.TabSpec tabSpec1 = fragmentTabHost.newTabSpec("tab1")
                //    .setIndicator("推荐");//标签指示器：标签显示的文字
                .setIndicator(getTabSpecView("tab1","常见问题",R.drawable.my));
        TabHost.TabSpec tabSpec2 = fragmentTabHost.newTabSpec("tab2")
                // .setIndicator("联系人");//标签指示器：标签显示的文字
                .setIndicator(getTabSpecView("tab2","百货",R.drawable.my));
        TabHost.TabSpec tabSpec3 = fragmentTabHost.newTabSpec("tab3")
                // .setIndicator("我的");//标签指示器：标签显示的文字
                .setIndicator(getTabSpecView("tab3","食品",R.drawable.my));
        TabHost.TabSpec tabSpec4 = fragmentTabHost.newTabSpec("tab4")
                // .setIndicator("我的");//标签指示器：标签显示的文字
                .setIndicator(getTabSpecView("tab4","食品",R.drawable.my));

        //添加到FragmentTabHost
        //Class类型的参数：类名.class  对象名.getClass()  Class.forName("类名")
        fragmentTabHost.addTab(tabSpec1,
                FirstFragment.class,
                null);//需要给Fragment传递数据时，可以通过Bundle对象传递
        fragmentTabHost.addTab(tabSpec2,
                SecondFragment.class,
                null);
        fragmentTabHost.addTab(tabSpec3,
                ThirdFragment.class,
                null);
        fragmentTabHost.addTab(tabSpec4,
                FourthFragment.class,
                null);

        //设置默认选中的标签
        fragmentTabHost.setCurrentTab(0);
        //   iconMap.get("tab1").setBackgroundResource(R.drawable.message);
        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.red));

        //标签切换事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            //切换标签时执行
            public void onTabChanged(String tabId) {
                //将选中的标签修改为绿色，其他的标签修改为黑色
                switch (tabId){
                    case "tab1"://选中消息标签
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.red));
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab2"://选中联系人标签
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.red));
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab3"://选中我的标签
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.red));
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab4"://选中我的标签
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.red));
                        break;
                }
            }
        });
    }

    //加载标签的布局文件生成相应的视图对象
    public View getTabSpecView(String tag, String title,int drawable){
//        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.tab_spec_layout,null);
        //获取到标签布局中ImageView和TextView的引用
        ImageView icon = view.findViewById(R.id.iv_icon);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        iconMap.put(tag,icon);
        titleMap.put(tag,tvTitle);
        //设置控件显示的内容
        tvTitle.setText(title);
        icon.setBackgroundResource(drawable);//相当于XML布局文件中的src属性
        return view;
    }
}*/
