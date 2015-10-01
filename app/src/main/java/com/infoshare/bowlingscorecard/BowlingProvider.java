package com.infoshare.bowlingscorecard;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wspeerbrecker on 2015-09-29.
 */
public class BowlingProvider extends ContentProvider {

    private BowlingDatabase mDBHelper;

    private static String TAG = BowlingProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int SCORECARD = 100;
    private static final int SCORECARD_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BowlingContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "Scorecard", SCORECARD);
        matcher.addURI(authority, "Scorecard/*", SCORECARD_ID);

        return matcher;
    }
    public boolean onCreate(){
        mDBHelper = new BowlingDatabase(getContext());
        return true;
    }
    private void deleteDatabase(){
        mDBHelper.close();
        BowlingDatabase.deleteDatabase(getContext());
        mDBHelper = new BowlingDatabase(getContext());
    }
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case SCORECARD:
                return BowlingContract.Scorecard.CONTENT_TYPE;
            case SCORECARD_ID:
                return BowlingContract.Scorecard.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        //
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BowlingDatabase.Tables.SCORECARD);
        //
        switch (match)
        {
            case SCORECARD:
                break;
            case SCORECARD_ID:
                String id = BowlingContract.Scorecard.getScorecardId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.v(TAG, "Insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        //
        switch (match)
        {
            case SCORECARD:
                long recordid = db.insertOrThrow(BowlingDatabase.Tables.SCORECARD, null, values);
                return BowlingContract.Scorecard.buildScorecardUri(String.valueOf(recordid));
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        Log.v(TAG, "Update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        //
        String selectionCriteria = selection;
        switch (match)
        {
            case SCORECARD:
                // do nothing
                break;
            case SCORECARD_ID:
                String id = BowlingContract.Scorecard.getScorecardId(uri);
                // not used but sample.
                selectionCriteria = BaseColumns._ID +"=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return db.update(BowlingDatabase.Tables.SCORECARD, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.v(TAG, "Delete(uri=" + uri + ", selection=" + selection + ", Args=" + selectionArgs);
        if (uri.equals(BowlingContract.Scorecard.CONTENT_URI)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        //
        String selectionCriteria = selection;
        switch (match)
        {
            case SCORECARD_ID:
                String id = BowlingContract.Scorecard.getScorecardId(uri);
                // not used but sample.
                selectionCriteria = BaseColumns._ID +"=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(BowlingDatabase.Tables.SCORECARD, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

}
