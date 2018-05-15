package com.MhMohamed.PillUp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MedicationContract {

    public static final String CONTENT_AUTHORITY = "com.MhMohamed.PillUp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MEDICATION= "medication";

    public static final class MedicationEntry implements BaseColumns {

        public static final String TABLE_NAME = "medication";


        public static final String COLUMN__NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DOSAGE= "dosage";
        public static final String COLUMN_DOSAGE_TIME= "dosage_time";
        public static final String COLUMN_PRODUCTION_DATE= "production_date";
        public static final String COLUMN_EXPIRY_DATE= "expiry_date";
        public static final String COLUMN_EXPIRED= "expired";

        public static final int FLAG_TRUE = 1;
        public static final int FLAG_FALSE = 0;


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;


        public static Uri builMedicationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildMedicationUpdateUri() {
            return CONTENT_URI.buildUpon().appendPath("med").appendPath("update")
                    .build();
        }

        public static final String sMedicationByNameSelection =
                MedicationContract.MedicationEntry.TABLE_NAME +
                        "." + MedicationEntry.COLUMN__NAME + " = ? ";

        public static String getMedicationNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
