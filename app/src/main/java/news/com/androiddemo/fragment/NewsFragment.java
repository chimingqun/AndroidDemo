package news.com.androiddemo.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import android.os.Handler;
import android.widget.Toast;

import java.util.logging.LogRecord;

import news.com.androiddemo.MainActivity;
import news.com.androiddemo.R;
import news.com.androiddemo.adapter.NewsAdapter;
import news.com.androiddemo.bean.NewsEntity;
import news.com.androiddemo.tool.Constants;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by chimingqun on 2015-6-15.
 */
public class NewsFragment extends Fragment{
    Activity activity;
    ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
    PullToRefreshListView mListView;
    NewsAdapter mAdapter;
    String text;
    ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;


    @Override
    public void onCreate(Bundle savedInstanceState){
         Bundle args = getArguments();
        text = args !=null? args.getString("text"):"";

        initData();
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Activity activity){
        this.activity = activity;
        super.onAttach(activity);
    }
    @Override
    /**fragement可见时，再进行数据加载，避免开始初始大量数据*/
    public void setUserVisibleHint(boolean isVisibleToUser){
        if(isVisibleToUser){
            if(newsList!=null && newsList.size()!=0){
                //向handler发送消息，加载新闻列表
                handler.obtainMessage(SET_NEWSLIST).sendToTarget();
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(2);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                    }
                }).start();
            }
        }else{
            //bu caozuo
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    //加载新闻列表
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SET_NEWSLIST:
                    detail_loading.setVisibility(View.GONE);
                    mAdapter = new NewsAdapter(activity,newsList);
                    mListView.setAdapter(mAdapter);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    //创建对应的视图
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment,null);
        mListView = (PullToRefreshListView)view.findViewById(R.id.mListView);
        TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
        detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
        item_textview.setText(text);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){
            @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView){
                new GetDataTask().execute();
            }
        });
        mListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener(){
            @Override
                    public void onLastItemVisible(){
                Toast.makeText(activity,"End of List!",Toast.LENGTH_SHORT).show();
            }

        });
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true,false);
        startLabels.setReleaseLabel("下拉刷新");
        startLabels.setRefreshingLabel("正在载入...");
        startLabels.setReleaseLabel("放开刷新...");
        return view;
    }
    private void initData(){
        newsList = Constants.getNewsList();
    }

    private class GetDataTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params){
            try{
                Thread.sleep(2000);
                //后台获取数据代码
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return "GetData";
        }
        @Override
        protected void onPostExecute(String result){
            mAdapter.notifyDataSetChanged();;
            mListView.onRefreshComplete();
            super.onPostExecute(result);
        }


    }



}
