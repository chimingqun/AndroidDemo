package news.com.androiddemo.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import news.com.androiddemo.MainActivity;
import news.com.androiddemo.R;
import news.com.androiddemo.bean.NewsEntity;
import news.com.androiddemo.tool.Constants;
import news.com.androiddemo.tool.Options;

/**
 * Created by chimingqun on 2015-6-15.
 */
public class NewsAdapter extends BaseAdapter{

    ArrayList<NewsEntity> newsList;
    Activity activity;
    LayoutInflater inflater = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    /**pop up more option windows*/
    private PopupWindow popupWindow;
    public NewsAdapter(Activity activity,ArrayList<NewsEntity> newsList){
        this.activity = activity;
        this.newsList = newsList;
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
        //initPopWindow();
    }

    @Override
    public int getCount(){
        return newsList == null?0:newsList.size();
    }
    @Override
    public NewsEntity getItem(int position){
        if(newsList != null&& newsList.size() !=0){
            return newsList.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position){
        return position;

    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        //加载一次，然后缓存起来，提高效率ViewHolder
        ViewHolder mHolder;
        View view = convertView;
        if(view == null){
            //实例化layout文件
            view = inflater.inflate(R.layout.list_item,null);
            mHolder = new ViewHolder();
            mHolder.item_layout = (RelativeLayout)view.findViewById(R.id.item_layout);
            mHolder.comment_layout = (RelativeLayout)view.findViewById(R.id.comment_layout);
            mHolder.item_title = (TextView)view.findViewById(R.id.item_title);
            mHolder.item_source = (TextView)view.findViewById(R.id.item_source);
            mHolder.list_item_local = (TextView)view.findViewById(R.id.list_item_local);
            mHolder.comment_count = (TextView)view.findViewById(R.id.comment_count);
            mHolder.publish_time = (TextView)view.findViewById(R.id.publish_time);
            mHolder.item_abstract = (TextView)view.findViewById(R.id.item_abstract);
            mHolder.alt_mark = (ImageView)view.findViewById(R.id.alt_mark);
            mHolder.right_image = (ImageView)view.findViewById(R.id.right_image);
            mHolder.item_image_layout = (LinearLayout)view.findViewById(R.id.item_image_layout);
            mHolder.item_image_0 = (ImageView)view.findViewById(R.id.item_image_0);
            mHolder.item_image_1 = (ImageView)view.findViewById(R.id.item_image_1);
            mHolder.item_image_2 = (ImageView)view.findViewById(R.id.item_image_2);
            mHolder.large_image = (ImageView)view.findViewById(R.id.large_image);
            mHolder.popicon = (ImageView)view.findViewById(R.id.popicon);
            mHolder.comment_content = (TextView)view.findViewById(R.id.comment_content);
            mHolder.right_padding_view = (View)view.findViewById(R.id.right_padding_view);
            view.setTag(mHolder);
        }else{
            //直接调用缓存
            mHolder = (ViewHolder)view.getTag();
        }
        NewsEntity news = getItem(position);
        mHolder.item_title.setText(news.getTitle());
        mHolder.item_source.setText(news.getSource());
        mHolder.comment_count.setText("评论"+news.getCommentNum());
        mHolder.publish_time.setText(news.getPublishTime()+"小时前");
        List<String> imgUrlList = news.getPicList();
        mHolder.popicon.setVisibility(View.VISIBLE);
        mHolder.comment_count.setVisibility(View.VISIBLE);
        mHolder.right_padding_view.setVisibility(View.VISIBLE);
        if(imgUrlList !=null&&imgUrlList.size()!=0){
            if (imgUrlList.size()==1){
                mHolder.item_image_layout.setVisibility(View.GONE);
                //是否大图，是大图显示大图view
                if(news.getIsLarge()){
                    mHolder.large_image.setVisibility(View.VISIBLE);
                    mHolder.right_image.setVisibility(View.GONE);
                    imageLoader.displayImage(imgUrlList.get(0),mHolder.large_image,options);
                    mHolder.popicon.setVisibility(View.GONE);
                    mHolder.right_padding_view.setVisibility(View.GONE);
                }else{//显示小图布局
                    mHolder.large_image.setVisibility(View.GONE);
                    mHolder.right_image.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(imgUrlList.get(0),mHolder.right_image,options);
                }

            }else{//多图布局
                mHolder.large_image.setVisibility(View.GONE);
                mHolder.right_image.setVisibility(View.GONE);
                mHolder.item_image_layout.setVisibility(View.VISIBLE);
                imageLoader.displayImage(imgUrlList.get(0),mHolder.item_image_0,options);
                imageLoader.displayImage(imgUrlList.get(1),mHolder.item_image_1,options);
                imageLoader.displayImage(imgUrlList.get(2),mHolder.item_image_2,options);

            }
        }else{//没有图片的新闻
            mHolder.right_image.setVisibility(View.GONE);
            mHolder.item_image_layout.setVisibility(View.GONE);
        }
        int markResID = getAltMarkResID(news.getMark(),news.getCollectStatus());
        if(markResID !=-1){
            mHolder.alt_mark.setVisibility(View.VISIBLE);
            mHolder.alt_mark.setImageResource(markResID);
        }else{
            mHolder.alt_mark.setVisibility(View.GONE);
        }
        //判断新闻内容是否为空
        if(!TextUtils.isEmpty(news.getNewsAbstract())){
            mHolder.item_abstract.setVisibility(View.VISIBLE);
            mHolder.item_abstract.setText(news.getNewsAbstract());
        }else{
            mHolder.item_abstract.setVisibility(View.GONE);
        }
        //判断新闻是否特殊标记的，推广等，为空是新闻
        if(!TextUtils.isEmpty(news.getLocal())){
            mHolder.list_item_local.setVisibility(View.VISIBLE);
            mHolder.list_item_local.setText(news.getLocal());
        }else{
            mHolder.list_item_local.setVisibility(View.GONE);
        }
        //判断评论字段是否为空，不为空显示对应布局
        if(!TextUtils.isEmpty(news.getComment())){
            mHolder.comment_layout.setVisibility(View.VISIBLE);
            mHolder.comment_content.setText(news.getComment());
        }else{
            mHolder.comment_layout.setVisibility(View.GONE);
        }
        //判断新闻是否已读
        if(!news.getReadStatus()){
            mHolder.item_layout.setSelected(true);
        }else{
            mHolder.item_layout.setSelected(false);
        }
          //设置+按钮点击效果
        //mHolder.popicon.setOnClickListener(new popAction(position));
        return view;
    }



    //获取position对应的数据


    static class ViewHolder {
        RelativeLayout item_layout;
        //title
        TextView item_title;
        //图片源
        TextView item_source;
        //类似推广之类的标签
        TextView list_item_local;
        //评论数量
        TextView comment_count;
        //发布时间
        TextView publish_time;
        //新闻摘要
        TextView item_abstract;
        //右上方TAG标记图片
        ImageView alt_mark;
        //右边图片
        ImageView right_image;
        //3张图片布局
        LinearLayout item_image_layout; //3张图片时候的布局
        ImageView item_image_0;
        ImageView item_image_1;
        ImageView item_image_2;
        //大图的图片的话布局
        ImageView large_image;
        //pop按钮
        ImageView popicon;
        //评论布局
        RelativeLayout comment_layout;
        TextView comment_content;
        //paddingview
        View right_padding_view;
    }
    public int getAltMarkResID(int mark,boolean isfavor){
        if(isfavor){
            return R.mipmap.ic_mark_favor;
        }
        switch (mark){
            //R文件里都是资源id
            case Constants.mark_recom:
                return R.mipmap.ic_mark_recommend;
            case Constants.mark_hot:
                return R.mipmap.ic_mark_hot;
            case Constants.mark_frist:
                return R.mipmap.ic_mark_first;
            case Constants.mark_exclusive:
                return R.mipmap.ic_mark_exclusive;
            case Constants.mark_favor:
                return R.mipmap.ic_mark_favor;
            default:
                break;
        }
        return -1;
    }
//    private void initPopWindow(){
//        View popView = inflater(R.layout.list)
//    }
}
