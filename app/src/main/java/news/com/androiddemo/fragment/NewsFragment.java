package news.com.androiddemo.fragment;

import android.app.Activity;
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
import java.util.logging.LogRecord;

import news.com.androiddemo.R;
import news.com.androiddemo.adapter.NewsAdapter;
import news.com.androiddemo.bean.NewsEntity;
import news.com.androiddemo.tool.Constants;

/**
 * Created by chimingqun on 2015-6-15.
 */
public class NewsFragment extends Fragment{
    Activity activity;
    ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
    ListView mListView;
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
    public void setUserVisibleHint(boolean isVisibleToUser){
        if(isVisibleToUser){
            if(newsList!=null && newsList.size()!=0){
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
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment,null);
        mListView = (ListView)view.findViewById(R.id.mListView);
        TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
        detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
        item_textview.setText(text);
        return view;
    }
    private void initData(){
        newsList = Constants.getNewsList();
    }

}
