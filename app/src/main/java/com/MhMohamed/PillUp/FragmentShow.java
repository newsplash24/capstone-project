package com.MhMohamed.PillUp;

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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.MhMohamed.PillUp.data.MedicationContract;
import com.MhMohamed.PillUp.R;


import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentShow extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Uri mUri = MedicationContract.MedicationEntry.CONTENT_URI;
    private int SHOW_LOADER = 1;


    private static final String[] DETAIL_COLUMNS = {

            MedicationContract.MedicationEntry.TABLE_NAME + "." + MedicationContract.MedicationEntry._ID,
            MedicationContract.MedicationEntry.COLUMN__NAME,
            MedicationContract.MedicationEntry.COLUMN_DOSAGE,
            MedicationContract.MedicationEntry.COLUMN_PRODUCTION_DATE,
            MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE,
            MedicationContract.MedicationEntry.COLUMN_EXPIRED,
            MedicationContract.MedicationEntry.COLUMN_TYPE
    };

    TextView out;
    String mDateFormat = "dd/MM/yyyy";
    String pDate = "", eDate = "";

    @BindView(R.id.tableLayout)
    TableLayout tableLayout;

    @BindView(R.id.hName)
    TextView hName;

    @BindView(R.id.hDosage)
    TextView hDosage;

    @BindView(R.id.hPDate)
    TextView hPDate;

    @BindView(R.id.hEDate)
    TextView hEDate;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helveticaneueltcom_thex.ttf");
        Typeface tfc = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLT_Arabic_55_Roman.ttf");


        hName.setTypeface(tf); hPDate.setTypeface(tf);  hEDate.setTypeface(tf); hDosage.setTypeface(tf);

        Cursor cursor = getActivity().getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI,
                null, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                View tableRow = getActivity().getLayoutInflater().inflate(R.layout.table_show_item, null, false);
                TextView item_name = (TextView) tableRow.findViewById(R.id.item_name);
                TextView item_dosage = (TextView) tableRow.findViewById(R.id.item_dosage);
                TextView item_PDate = (TextView) tableRow.findViewById(R.id.item_PDate);
                TextView item_EDate = (TextView) tableRow.findViewById(R.id.item_EDate);

                item_name.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_TYPE)));
                item_dosage.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_DOSAGE)));
                item_PDate.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_PRODUCTION_DATE)));
                item_EDate.setText(cursor.getString(cursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_EXPIRY_DATE)));

                tableLayout.addView(tableRow);
            }
        } else {
            Toast.makeText(getContext(), "Please Add Some Medications!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public FragmentShow() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SHOW_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show, container, false);


        ButterKnife.bind(this, root);

        return root;
    }
}
