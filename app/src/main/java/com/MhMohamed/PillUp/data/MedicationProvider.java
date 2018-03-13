package com.MhMohamed.PillUp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MedicationProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MedicationDbHelper mOpenHelper;

    static final int MEDICATION = 100;
    static final int MEDICATION_ITEM = 200;
    static final int MEDICATION_UPDATE = 700;



    private static final String sMedicationByNameSelection =
            MedicationContract.MedicationEntry.TABLE_NAME +
                    "." + MedicationContract.MedicationEntry.COLUMN__NAME + " = ? ";

    private static final String sMedicationSelection =
            MedicationContract.MedicationEntry.TABLE_NAME +
                    "." + MedicationContract.MedicationEntry._ID + " = ? ";

    private Cursor getMedicationByName(Uri uri, String[] projection) {
        String MedicationName = MedicationContract.MedicationEntry.getMedicationNameFromUri(uri);

        String[] selectionArgs;
        String selection = sMedicationByNameSelection;
        selectionArgs = new String[]{MedicationName};

        Cursor cursor = mOpenHelper.getReadableDatabase().query(MedicationContract.MedicationEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        getContext().getContentResolver().notifyChange(uri, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MedicationContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MedicationContract.PATH_MEDICATION, MEDICATION);
        matcher.addURI(authority, MedicationContract.PATH_MEDICATION + "/#", MEDICATION_ITEM);
        matcher.addURI(authority, MedicationContract.PATH_MEDICATION + "/*/update", MEDICATION_UPDATE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MedicationDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case MEDICATION:
                return MedicationContract.MedicationEntry.CONTENT_TYPE;

            case MEDICATION_ITEM:
                return MedicationContract.MedicationEntry.CONTENT_ITEM_TYPE;

            case MEDICATION_UPDATE:
                return MedicationContract.MedicationEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MEDICATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MedicationContract.MedicationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            case MEDICATION_ITEM: {
                String m_id = uri.getLastPathSegment();
                selectionArgs = new String[]{m_id};

                retCursor = mOpenHelper.getReadableDatabase().query(
                        MedicationContract.MedicationEntry.TABLE_NAME,
                        projection,
                        sMedicationSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MEDICATION: {
                long _id = db.insert(MedicationContract.MedicationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MedicationContract.MedicationEntry.builMedicationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;

        if (null == selection) selection = "1";

        switch (match) {

            case MEDICATION: {
                rowDeleted = db.delete(MedicationContract.MedicationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (rowDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MEDICATION_UPDATE:
                rowsUpdated = db.update(MedicationContract.MedicationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String table_name;
        switch (match) {
            case MEDICATION: {
                table_name = MedicationContract.MedicationEntry.TABLE_NAME;
                break;
            }

            default:
                return super.bulkInsert(uri, values);
        }

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(table_name, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}