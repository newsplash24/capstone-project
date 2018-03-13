package com.MhMohamed.PillUp;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.MhMohamed.PillUp.adapters.NewsListAdapter;
import com.MhMohamed.PillUp.data.MedicationContract;
import com.MhMohamed.PillUp.models.Medication;
import com.MhMohamed.PillUp.widget.MedicationWidget;
import com.example.www.medicationReminder.R;
import com.MhMohamed.PillUp.models.New;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.DialogHandler,
        NewsListAdapter.NewsItemClickhandler {


    private Menu menu;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Update widget? ", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Cursor cursor = MainActivity.this.getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI,
                                        null, null, null, null);

                                List<Medication> medicationList = new ArrayList<>();


                                if (cursor != null) {
                                    if (cursor.getCount() <=0) {
                                        Toast.makeText(MainActivity.this, "Please Add Some Medications First!", Toast.LENGTH_SHORT).show();
                                    }
                                    while (cursor.moveToNext()) {
                                        Medication medication = new Medication();
                                        medication.setName(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN__NAME)));
                                        medication.setDosage(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE)));
                                        medication.setDosageTime(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE_TIME)));

                                        medicationList.add(medication);
                                    }
                                }

                                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(MainActivity.this, MedicationWidget.class));
                                MedicationWidget.refreshWidget(MainActivity.this, medicationList,
                                        appWidgetManager, appWidgetIds);

                                Toast.makeText(MainActivity.this, "Widget Refreshed!", Toast.LENGTH_SHORT).show();

                            }
                        }).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_news);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new NewsFragment()
        ).commit();

//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        MenuItem menuItem = menu.getItem(R.id.nav_expired);
//
//        this.onNavigationItemSelected(menuItem);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id == R.id.nav_insert){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new FragmentAdd()
                   ).addToBackStack("ADD_FRAGMENT").commit();
        }

        else if (id == R.id.nav_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new FragmentShow()
            ).addToBackStack("SHOW_FRAGMENT").commit();

        } else if (id == R.id.nav_expired) {
           getSupportFragmentManager().beginTransaction().replace(R.id.content, new ExpiryFragment()
           ).addToBackStack("EXPIRY_FRAGMENT").commit();

       } else if (id == R.id.nav_news) {
           getSupportFragmentManager().beginTransaction().replace(R.id.content, new NewsFragment()
           ).addToBackStack("NEWS_FRAGMENT").commit();
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void showDialog(String msg) {
        if (pDialog != null) {
            pDialog.setMessage(msg);
            pDialog.show();
        }

    }

    @Override
    public void dismissDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }

    }

    @Override
    public void onNewsClicked(View view, int position) {

        New aNew = NewsFragment.newsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", aNew);

        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);


    }
}
