package com.infoshare.bowlingscorecard;

//import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wspeerbrecker on 2015-09-30.
 */
public class ScorecardListLoader extends AsyncTaskLoader<List<Scorecard>> {
    private static final String LOG_TAG=ScorecardListLoader.class.getSimpleName();
    private List<Scorecard> mScorecard;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public ScorecardListLoader(Context context, Uri uri, ContentResolver contentResolver){
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public List<Scorecard> loadInBackground() {

        String[] projection = {BaseColumns._ID, BowlingContract.ScorecardColumns.SCORECARD_SEASONID, BowlingContract.ScorecardColumns.SCORECARD_BOWLING_DATE,
                BowlingContract.ScorecardColumns.SCORECARD_GAME1, BowlingContract.ScorecardColumns.SCORECARD_GAME2, BowlingContract.ScorecardColumns.SCORECARD_GAME3,
                BowlingContract.ScorecardColumns.SCORECARD_TOTAL, BowlingContract.ScorecardColumns.SCORECARD_AVERAGE
        };
        List<Scorecard> entries = new ArrayList<Scorecard>();

        mCursor = mContentResolver.query(BowlingContract.URI_TABLE, projection, null, null, BowlingContract.ScorecardColumns.SCORECARD_BOWLING_DATE + " DESC");
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String seasonId = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_SEASONID));
                    String bowlingDate = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_BOWLING_DATE));
                    String game1 = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_GAME1));
                    String game2 = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_GAME2));
                    String game3 = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_GAME3));
                    String seriesTotal = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_TOTAL));
                    String seriesAverage  = mCursor.getString(mCursor.getColumnIndex(BowlingContract.ScorecardColumns.SCORECARD_AVERAGE));
                    //
                    Scorecard scorecard = new Scorecard(_id, seasonId, bowlingDate, game1, game2, game3, seriesTotal, seriesAverage);
                    entries.add(scorecard);
                } while (mCursor.moveToNext());

            }
        }
        return entries;

    }

    @Override
    public void deliverResult(List<Scorecard> data) {

        if (isReset()) {
            if (data != null) {
                mCursor.close();
            }
        }
        List<Scorecard> oldScorecard = mScorecard;
        if (mScorecard == null || mScorecard.size() == 0){
            Log.d(LOG_TAG, "*** No Data Returned ***");
        }
        mScorecard = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        if (oldScorecard != null && oldScorecard != data){
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {

        if (mScorecard != null){
            deliverResult(mScorecard);
        }

        if (takeContentChanged() || mScorecard == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {

        onStartLoading();
        if (mCursor != null){
            mCursor.close();
        }
        mScorecard = null;
    }

    @Override
    public void onCanceled(List<Scorecard> data) {
        super.onCanceled(data);
        if (mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
