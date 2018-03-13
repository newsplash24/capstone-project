package com.MhMohamed.PillUp;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.MhMohamed.PillUp.data.MedicationContract;
import com.example.www.medicationReminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpiryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ContentValues[] expiredList;
    private String[] columnNames;
    private final String mDateFormat = "dd/MM/yyyy";
    private final Uri mUri = MedicationContract.MedicationEntry.CONTENT_URI;
    private int DETAIL_LOADER = 0;


    private static final String[] DETAIL_COLUMNS = {

            MedicationContract.MedicationEntry.TABLE_NAME + "." + MedicationContract.MedicationEntry._ID,
            MedicationContract.MedicationEntry.COLUMN__NAME,
            MedicationContract.MedicationEntry.COLUMN_DOSAGE,
            MedicationContract.MedicationEntry.COLUMN_PRODUCTION_DATE,
            MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE,
            MedicationContract.MedicationEntry.COLUMN_EXPIRED,
            MedicationContract.MedicationEntry.COLUMN_TYPE
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    MedicationContract.MedicationEntry.COLUMN_EXPIRED +"=? ",
                    new String[]{"1"},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/helveticaneueltcom_thex.ttf");
        Typeface tfc = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeueLT_Arabic_55_Roman.ttf");


        hName.setTypeface(tf); hPDate.setTypeface(tf);  hEDate.setTypeface(tf);


        if(data.getCount() >0) {
            header.setVisibility(View.VISIBLE);
            hello.setVisibility(View.GONE);
            while (data.moveToNext()) {
                View tableRow = LayoutInflater.from(getContext()).inflate(R.layout.table_item, null, false);
                TextView item_name = (TextView) tableRow.findViewById(R.id.item_name);
                TextView item_PDate = (TextView) tableRow.findViewById(R.id.item_PDate);
                TextView item_EDate = (TextView) tableRow.findViewById(R.id.item_EDate);

                item_name.setText(data.getString(data.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_TYPE)));
                item_PDate.setText(data.getString(data.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_PRODUCTION_DATE)));
                item_EDate.setText(data.getString(data.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE)));

                tableLayout.addView(tableRow);
            }
        }
        else
        {
            header.setVisibility(View.GONE);
            hello.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public ExpiryFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.hello)
    TextView hello;

    @BindView(R.id.tableLayout)
    TableLayout tableLayout;

    @BindView(R.id.table)
    RelativeLayout table;

    @BindView(R.id.header)
    TableRow header;

    @BindView(R.id.hName)
    TextView hName;

    @BindView(R.id.hPDate)
    TextView hPDate;

    @BindView(R.id.hEDate)
    TextView hEDate;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView = inflater.inflate(R.layout.fragment_expiry, container, false);

        ButterKnife.bind(this, rootView);


        expiredList = null;
        columnNames = null;

        checkExpired();
        getExpired();

        return rootView;
    }

    void checkExpired() {
        Cursor cursor = getContext().getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            int expIndx = cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE);
            long currTime = System.currentTimeMillis();
            while (cursor.moveToNext()) {

                int dosageTimeIndx = cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE_TIME);
                String dosageTime = cursor.getString(dosageTimeIndx);

                String expDate = cursor.getString(expIndx);
                long expDateMillis = getDateMillis(expDate);
                if (currTime > expDateMillis) {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MedicationContract.MedicationEntry.COLUMN_EXPIRED, 1);
                    long id = cursor.getLong(cursor.getColumnIndex(MedicationContract.MedicationEntry._ID));
                    int i = getContext().getContentResolver().update(MedicationContract.MedicationEntry.buildMedicationUpdateUri(), newValues, MedicationContract.MedicationEntry._ID + "=?",
                            new String[]{id + ""}
                    );
                }
            }


        }
    }

    void getExpired(){

//        Cursor cursor = getContext().getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI,
//                null, MedicationContract.MedicationEntry.COLUMN_EXPIRED +"=? ", new String[]{"1"}, null);
////        Toast.makeText(MainActivity.this, "Size = "+cursor.getCount(), Toast.LENGTH_SHORT).show();
//        columnNames = cursor.getColumnNames();
//        expiredList = new ContentValues[cursor.getCount()];
//        int c = 0;

//        while (cursor.moveToNext() && c < columnNames.length) {
//            expiredList[c] = new ContentValues();
//            for (String col : columnNames) {
//
//                    if (col.equals(MedicationEntry.COLUMN_EXPIRED))
//                        expiredList[c].put(col, cursor.getInt(cursor.getColumnIndex(col)));
////                        Toast.makeText(MainActivity.this, col + " = "+cursor.getInt(cursor.getColumnIndex(col)), Toast.LENGTH_SHORT).show();
//                    else {
//
//                        expiredList[c].put(col, cursor.getString(cursor.getColumnIndex(col)));
////                        Toast.makeText(MainActivity.this,col+ " = "+cursor.getString(cursor.getColumnIndex(col)), Toast.LENGTH_SHORT).show();
//                    }
//            }
//
//            c++;
//        }
//
//        String result = "";
//        for(ContentValues cv : expiredList) {
//            for (String col : columnNames) {
//                result += cv.get(col) + "   ";
//            }
//            result += System.getProperty("line.separator");
//        }
//        hello.setText("looool");
//        hello.setText(result);


//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/helveticaneueltcom_thex.ttf");
//        Typeface tfc = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeueLT_Arabic_55_Roman.ttf");
//
//
//        hName.setTypeface(tf); hPDate.setTypeface(tf);  hEDate.setTypeface(tf);
//
//
//        if(cursor.getCount() >0) {
//            header.setVisibility(View.VISIBLE);
//            hello.setVisibility(View.GONE);
//            while (cursor.moveToNext()) {
//                View tableRow = LayoutInflater.from(getContext()).inflate(R.layout.table_item, null, false);
//                TextView item_name = (TextView) tableRow.findViewById(R.id.item_name);
//                TextView item_PDate = (TextView) tableRow.findViewById(R.id.item_PDate);
//                TextView item_EDate = (TextView) tableRow.findViewById(R.id.item_EDate);
//
//                item_name.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_TYPE)));
//                item_PDate.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_PRODUCTION_DATE)));
//                item_EDate.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE)));
//
//                tableLayout.addView(tableRow);
//            }
//        }
//        else
//        {
//            header.setVisibility(View.GONE);
//            hello.setVisibility(View.VISIBLE);
//        }


    }

    public long getDateMillis(String toParse) {

        SimpleDateFormat formatter = new SimpleDateFormat(mDateFormat);
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }


}
