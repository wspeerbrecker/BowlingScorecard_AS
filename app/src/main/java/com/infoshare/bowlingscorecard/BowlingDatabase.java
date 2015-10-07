package com.infoshare.bowlingscorecard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by wspeerbrecker on 2015-10-01.
 */
public class BowlingDatabase extends SQLiteOpenHelper {
    private static final String TAG = BowlingDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "scorecard.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables {
        String SCORECARD = "scorecard";
    }

    private static final String DATABASE_CREATE_SQL =
            "create table " + Tables.SCORECARD
                    + " (" + BaseColumns._ID + " integer primary key autoincrement, "
                    + BowlingContract.ScorecardColumns.SCORECARD_SEASONID + " integer not null, "
                    + BowlingContract.ScorecardColumns.SCORECARD_BOWLING_DATE + " text not null, "
                    + BowlingContract.ScorecardColumns.SCORECARD_GAME1 + " integer not null, "
                    + BowlingContract.ScorecardColumns.SCORECARD_GAME2 + " integer not null, "
                    + BowlingContract.ScorecardColumns.SCORECARD_GAME3 + " integer not null,"
                    + BowlingContract.ScorecardColumns.SCORECARD_TOTAL + " integer not null,"
                    + BowlingContract.ScorecardColumns.SCORECARD_AVERAGE + " integer not null"

                    // Rest  of creation:
                    + ");";

    public BowlingDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading application's database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data!");

        int version = oldVersion;
        if (version == 1) {
            version = 2;
        }
        if (version!= DATABASE_VERSION) {
            // Drop the old tables.
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SCORECARD);
            // Recreate new database.
            onCreate(db);
        }
    }
    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
