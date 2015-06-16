package news.com.androiddemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import news.com.androiddemo.adapter.NewsFragmentPagerAdapter;
import news.com.androiddemo.bean.NewsClassify;
import news.com.androiddemo.fragment.NewsFragment;
import news.com.androiddemo.tool.BaseTools;
import news.com.androiddemo.tool.Constants;


public class MainActivity extends FragmentActivity {

    /**新闻类列表*/
    private ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPaper;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth/7;
        newsClassify = Constants.getData();
        initView();
        initFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initView(){
        mViewPaper = (ViewPager) findViewById(R.id.mViewPaper);
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

            Bundle data = new Bundle();
            data.putString("text",newsClassify.get(0).getTitle());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        NewsFragmentPagerAdapter mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
         mViewPaper.setCurrentItem(0);
         mViewPaper.setAdapter(mAdapter);
       // mViewPaper.setOnPageChangeListener(pageListener);

    }
}
