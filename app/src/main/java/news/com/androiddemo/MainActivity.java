package news.com.androiddemo;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import news.com.androiddemo.adapter.NewsFragmentPagerAdapter;
import news.com.androiddemo.bean.NewsClassify;
import news.com.androiddemo.fragment.NewsFragment;
import news.com.androiddemo.tool.BaseTools;
import news.com.androiddemo.tool.Constants;
import news.com.androiddemo.view.ColumnHorizontalScrollView;
import news.com.androiddemo.view.DrawerView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.zdp.aseo.content.AseoZdpAseo;


public class MainActivity extends FragmentActivity {
    /**自定义HorizontalScrollView*/
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_columns;
    private ViewPager mViewPaper;
    private ImageView button_more_columns;
    /**新闻类列表*/
    private ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
    /**当前选中栏目*/
    private int columnSelectIndex = 0;
    /**左阴影*/
    public ImageView shade_left;
    /**右阴影*/
    public ImageView shade_right;

    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    protected SlidingMenu side_drawer;
    /**head头部的中间loading*/
    private ProgressBar top_progress;
    /**head头部中间的刷新按钮*/
    private ImageView top_refresh;
    /** head 头部 的左侧菜单 按钮*/
    private ImageView top_head;
    /** head 头部 的右侧菜单 按钮*/
    private ImageView top_more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth/7;//一个item宽度为屏幕的1/7
        ///newsClassify = Constants.getData();
        initView();
       /// initFragment();
        initSlidingMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void initSlidingMenu(){
        side_drawer = new DrawerView(this).initSlidingMenu();
    }
    /**初始化layout控件*/
    private void initView(){
        //AseoZdpAseo.initTimer(this,30);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout)findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout)findViewById(R.id.ll_more_columns);
        rl_columns = (RelativeLayout)findViewById(R.id.rl_column);
        button_more_columns = (ImageView)findViewById(R.id.button_more_columns);
        mViewPaper = (ViewPager) findViewById(R.id.mViewPaper);
        shade_left = (ImageView)findViewById(R.id.shade_left);
        shade_right = (ImageView)findViewById(R.id.shade_right);
        top_head = (ImageView)findViewById(R.id.top_head);
        top_more = (ImageView)findViewById(R.id.top_more);
        top_refresh = (ImageView)findViewById(R.id.top_refresh);
        top_progress = (ProgressBar)findViewById(R.id.top_progress);
        button_more_columns.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                //打开category 菜单添加增删column
            }

        });
        top_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(side_drawer.isMenuShowing()){
                    side_drawer.showContent();
                }else{
                    side_drawer.showMenu();
                }
            }
        });
        top_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(side_drawer.isSecondaryMenuShowing()){
                    side_drawer.showContent();
                }else{
                    side_drawer.showSecondaryMenu();
                }
            }
        });
        setChangelView();
    }
    /** 当栏目项发生变化时候调用*/
    private void setChangelView(){
        initColumnData();
        initTabColumn();
        initFragment();
    }
    /**获取Column栏目数据*/
    private void initColumnData(){
        newsClassify = Constants.getData();
    }

    @Override
    public void onBackPressed(){
        //返回键的处理
        //AseoZdpAseo.initPush(this);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //AseoZdpAseo.initFinalTimer(this);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**初始化Column栏目项*/
    private void initTabColumn(){
        mRadioGroup_content.removeAllViews();
        int count = newsClassify.size();
        mColumnHorizontalScrollView.setParam(this,mScreenWidth,mRadioGroup_content,shade_left,shade_right,ll_more_columns,rl_columns);
        for(int i =0;i<count;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this,R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.radio_button_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5,5,5,5);
            columnTextView.setId(i);
            columnTextView.setText(newsClassify.get(i).getTitle());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex ==i){
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if(localView!=v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPaper.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(),newsClassify.get(v.getId()).getTitle(),Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView,i,params);

        }
    }
    /**选中的Column里面的Tab*/
    private void selectTab(int tab_position){
        columnSelectIndex = tab_position;
        for(int i=0;i<mRadioGroup_content.getChildCount();i++){
            View checkView = mRadioGroup_content.getChildAt(tab_position);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l+k/2 -mScreenWidth/2;
            mColumnHorizontalScrollView.smoothScrollTo(i2,0);
        }
        //判断是否选中
        for(int j=0;j<mRadioGroup_content.getChildCount();j++){
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean isCheck;
            if(j ==tab_position){
                isCheck = true;
            }else{
                isCheck = false;
            }
            checkView.setSelected(isCheck);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initFragment(){
        int count = newsClassify.size();

        for(int i=0;i<count;i++){
            Bundle data = new Bundle();
            data.putString("text",newsClassify.get(i).getTitle());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }


        NewsFragmentPagerAdapter mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        /// mViewPaper.setCurrentItem(0);

         mViewPaper.setAdapter(mAdapter);
         mViewPaper.setOnPageChangeListener(pageListener);
    }
    /**ViewPager 切换监听*/
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                mViewPaper.setCurrentItem(position);
                selectTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
