package com.MhMohamed.PillUp.widget;

/**
 * Created by Mohamed on 3/7/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.MhMohamed.PillUp.data.MedicationContract;
import com.MhMohamed.PillUp.models.Medication;
import com.example.www.medicationReminder.R;

import java.util.ArrayList;
import java.util.List;

public class MedicationWidgetFactory implements RemoteViewsService.RemoteViewsFactory {



    private Context mContext;
    private Cursor cursor;
    private String columnNames[];
    private List<Medication> medicationList;

    public MedicationWidgetFactory(Context applicationContext, Intent intent) {

        mContext = applicationContext;


    }

    @Override
    public void onCreate() {

        cursor = mContext.getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI,
                null, null, null, null);

        medicationList = new ArrayList<>();


        if (cursor != null) {
            while (cursor.moveToNext()) {
                Medication medication = new Medication();
                medication.setName(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN__NAME)));
                medication.setDosage(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE)));
                medication.setDosageTime(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE_TIME)));

                medicationList.add(medication);
            }
        }


    }

    @Override
    public void onDataSetChanged() {


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {


        String ingTxt = "";
        int i = 0;
        for (Medication medication : medicationList) {
            ingTxt += +(++i) + "- \"" + medication.getName() + " . " +
                    medication.getDosage() + " \" . " +
                    medication.getDosageTime() + " \n\n";
        }


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.titleTv, mContext.getString(R.string.app_name));
        rv.setTextViewText(R.id.contentTv, ingTxt);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

