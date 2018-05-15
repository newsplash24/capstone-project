package com.MhMohamed.PillUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.MhMohamed.PillUp.R;
import com.MhMohamed.PillUp.models.New;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsActivity extends AppCompatActivity {

    @BindView(R.id.articleTv)
    TextView articleTv;

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        New aNew = (New)bundle.getSerializable("article");

        articleTv.setText(aNew.getContent());

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




    }
}
