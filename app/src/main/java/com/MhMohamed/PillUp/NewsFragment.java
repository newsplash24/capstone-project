package com.MhMohamed.PillUp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.MhMohamed.PillUp.adapters.NewsListAdapter;
import com.MhMohamed.PillUp.models.New;
import com.MhMohamed.PillUp.utils.Constants;
import com.MhMohamed.PillUp.utils.Utility;
import com.example.www.medicationReminder.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    static List<New> newsList;
    private DialogHandler dialogHandler;
    private NewsListAdapter newsListAdapter;
    private int mPosition;
    private final String SELECTED_KEY="selectedPosition";
    private Tracker mTracker;

    @BindView(R.id.newsRecyclerView)
    RecyclerView newsRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public interface DialogHandler {
        void showDialog(String msg);
        void dismissDialog();

    }

    public NewsFragment() {
        // Required empty public constructor
        newsList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("ANALYTICS", "Setting screen name: " + NewsFragment.class);
        mTracker.setScreenName("Image~" + NewsFragment.class);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, rootView);

        if (getActivity() instanceof DialogHandler) {
            dialogHandler = (DialogHandler) getActivity();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(linearLayoutManager);

        newsListAdapter = new NewsListAdapter(getActivity());
        newsRecyclerView.setAdapter(newsListAdapter);

        new FetchWeatherTask().execute(Constants.NEWS);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchWeatherTask().execute(Constants.NEWS);
                showSnackbarMessage("Refreshing List...", rootView);

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Refresh")
                        .build());
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY))
        {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);

        }


        if(mPosition != GridView.INVALID_POSITION )
        {
            newsRecyclerView.smoothScrollToPosition(mPosition);
        }


        return rootView;
    }



    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showSnackbarMessage(String msg, View rooView) {
        Snackbar.make(rooView, msg, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mPosition);

            super.onSaveInstanceState(outState);

    }

    public class FetchWeatherTask extends AsyncTask<String, Void, List<New>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newsList.clear();
            dialogHandler.showDialog("Loading News......");
        }

        @Override
        protected List<New> doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String service = params[0];
            URL requestUrl = Utility.buildUrl(service);

            try {
                String response = Utility
                        .getResponseFromHttpUrl(requestUrl);

                JSONArray jsonArray = new JSONArray(response);

                for (int i =0; i< jsonArray.length(); i++) {

                    JSONObject newObject = jsonArray.getJSONObject(i);

                    New aNew = new New();
                    aNew.setTitle(newObject.getString("title"));
                    aNew.setContent(newObject.getString("content"));
                    aNew.setImageUrl(newObject.getString("img"));

                    newsList.add(aNew);
                }

                return newsList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<New> newsData) {
            dialogHandler.dismissDialog();
            if (newsData != null) {
                newsListAdapter.setNews(newsData);
                newsListAdapter.notifyDataSetChanged();
                updateRefreshingUI();
            }
        }
    }

}
