package com.MhMohamed.PillUp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MhMohamed.PillUp.NewsFragment;
import com.MhMohamed.PillUp.R;
import com.MhMohamed.PillUp.models.New;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mohamed on 3/13/2018.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsCardViewHolder>{

    private NewsFragment.DialogHandler dialogDismisser;
    private NewsItemClickhandler newsItemClickhandler;
    private Context mContext;
    private Boolean toastShown;
    private List<New> news;

    public interface NewsItemClickhandler {
        void onNewsClicked(View view, int position);
    }



    public NewsListAdapter (Context context){
        dialogDismisser = (NewsFragment.DialogHandler) context;
        newsItemClickhandler = (NewsItemClickhandler) context;
        toastShown = false;
        mContext = context;
    }

    @Override
    public NewsCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_article, viewGroup, false);
        NewsCardViewHolder item = new NewsCardViewHolder(v);
        return item;
    }

    @Override
    public void onBindViewHolder(NewsCardViewHolder holder, int position) {

        if (news == null) {
            holder.thumb.setImageResource(R.mipmap.ic_launcher);
            return;
        }

        if (!haveNetworkConnection()) {
            holder.thumb.setImageResource(R.mipmap.ic_launcher);
        } else {

            New aNew = news.get(position);

            String url = aNew.getImageUrl();


            loadImageOnline(mContext, holder.thumb, url);

            holder.title.setText(aNew.getTitle());

            Log.v("NEWS_URL", url);
        }

    }


    @Override
    public int getItemCount() {
        if (news == null) {
            return 10;
        } else {
            return news.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public  class NewsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView thumb;
        NewsCardViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
            thumb = (ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            newsItemClickhandler.onNewsClicked(view, getPosition());
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    private void loadImageOnline(final Context context, final ImageView img, final String url ){

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.thumb)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        if(dialogDismisser != null) {
                            dialogDismisser.dismissDialog();

                        }
                    }

                    @Override
                    public void onError() {
                        Log.v("Picasso","Could not fetch image");

                        if(!toastShown)
                            Toast.makeText(mContext, "Cannot fetch posters. No internet connection!", Toast.LENGTH_LONG).show();
                        toastShown = true;
                    }
                });

    }

    public void setNews(List<New> news) {
        this.news = news;
    }

    public List<New> getNews() {
        return news;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

