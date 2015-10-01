// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.infoshare.bowlingscorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class BowlingDBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = BowlingDBAdapter.class.getSimpleName();
			// "BowlingDBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	/*
	 * CHANGE 1:
	 */
	// TODO: Setup your fields here:
	public static final String KEY_SEASONID = "SeasonID";
	public static final String KEY_BOWLING_DATE = "BowlingDate";
	public static final String KEY_GAME1 = "Game1";
	public static final String KEY_GAME2 = "Game2";
	public static final String KEY_GAME3 = "Game3";
	public static final String KEY_TOTAL = "Total";
	public static final String KEY_AVERAGE = "Average";
	
	// TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
	public static final int COL_SEASONID = 1;
	public static final int COL_BOWLING_DATE = 2;
	public static final int COL_GAME1 = 3;
	public static final int COL_GAME2 = 4;
	public static final int COL_GAME3 = 5;
	public static final int COL_TOTAL = 6;
	public static final int COL_AVERAGE = 7;

	
	public static final String[] ALL_KEYS = new String[] 
			{KEY_ROWID, KEY_SEASONID, KEY_BOWLING_DATE, KEY_GAME1, KEY_GAME2, KEY_GAME3, KEY_TOTAL, KEY_AVERAGE};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_NAME = "BowlingDb";
	public static final String DATABASE_TABLE = "Scorecard";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 3;

	interface Tables {
		String SCORECARD = "Scorecard";
	}

	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_SEASONID + " integer not null, "
			+ KEY_BOWLING_DATE + " text not null, "
			+ KEY_GAME1 + " integer not null, "
			+ KEY_GAME2 + " integer not null, "
			+ KEY_GAME3 + " integer not null,"
			+ KEY_TOTAL + " integer not null,"
			+ KEY_AVERAGE + " integer not null"
			
			// Rest  of creation:
			+ ");";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public BowlingDBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public BowlingDBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(int seasonid, String bowlingdate, int game1, int game2, int game3, int total, int average) {
		/*
		 * CHANGE 3:
		 */		
		// TODO: Update data in the row with new fields.
		// TODO: Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SEASONID, seasonid);
		initialValues.put(KEY_BOWLING_DATE, bowlingdate);
		initialValues.put(KEY_GAME1, game1);
		initialValues.put(KEY_GAME2, game2);
		initialValues.put(KEY_GAME3, game3);
		initialValues.put(KEY_TOTAL, total);
		initialValues.put(KEY_AVERAGE, average);
		
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	// Add a new set of values to the database.
	public long insertRowwithValues(ContentValues values) {

		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, values);
	}
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	// Delete a row from the database, by rowId (primary key)
	public int deleteRowwithValues(String where, String[] selectionArgs) {

		return db.delete(DATABASE_TABLE, where, null);
	}
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		String sOrderBy = KEY_BOWLING_DATE + " DESC";
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, sOrderBy, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, int seasonid, String bowlingdate, int game1, int game2, int game3, int total, int average) {
		String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
		// TODO: Update data in the row with new fields.
		// TODO: Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_SEASONID, seasonid);
		newValues.put(KEY_BOWLING_DATE, bowlingdate);
		newValues.put(KEY_GAME1, game1);
		newValues.put(KEY_GAME2, game2);
		newValues.put(KEY_GAME3, game3);
		newValues.put(KEY_TOTAL, total);
		newValues.put(KEY_AVERAGE, average);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}


	// Change an existing row to be equal to new data.
	public int updateRowwithValues(ContentValues newValues, String where, String[] selectionArgs) {

		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, selectionArgs); // != 0;
	}
	// Change an existing row to be equal to new data.
	public boolean updateAverage(long rowId, int average) {
		String where = KEY_ROWID + "=" + rowId;

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_AVERAGE, average);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	public static boolean deleteDatabase(Context ctx)
	{
		ctx.deleteDatabase(DATABASE_NAME);
		return true;
	}
	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
