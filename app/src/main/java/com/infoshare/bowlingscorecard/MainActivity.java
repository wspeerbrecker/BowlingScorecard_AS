package com.infoshare.bowlingscorecard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//import com.infoshare.samplecode.splash;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.FrameLayout.LayoutParams;
import android.widget.TabHost.TabSpec;

public class MainActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<List<Scorecard>> {

//    BowlingDBAdapter BowlingDB;
    private String sCSVFilename;
    ListView lvScores;

    // Overall Statistics.
    private int _iTotalNoOfGames = 0;
    private int _iTotalBowlingSessions = 0;
    private int _iSeasonAverage = 0;
    private int _iSeasonTotal = 0;
    private int _iHighestGame = 0;
    private String _sHighestGameDate;
    private int _iHighestTriple = 0;
    private String _sHighestTripleDate;
    private int _iLowestGame = 999;
    private String _sLowestGameDate;
    private int _iLowestTriple = 999;
    private String _sLowestTripleDate;

    private ScorecardCustomAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Scorecard> mScorecard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Scorecard");
        tabHost.addTab(spec1);

        TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Statistics");
        tabHost.addTab(spec2);

        TabSpec spec3 = tabHost.newTabSpec("tab3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Settings");
        tabHost.addTab(spec3);

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
                                            @Override
                                            public void onTabChanged(String tabId) {
                                                if (tabId == "1") {
                                                    //destroy earth
                                                }
                                                if (tabId == "tab2") {
                                                    // Calculate the Overall Statistics.
                                                    calculateStats();
                                                    //
                                                    displayStats();
                                                }
                                                if (tabId == "tab3") {
                                                    loadSavedPreferences();
                                                }
                                            }
                                        }
        );

        BowlingDatabase bowlingDB = new BowlingDatabase(getApplicationContext());
        //
        mContentResolver = getContentResolver();
        mAdapter = new ScorecardCustomAdapter(this);
        //
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        //
        ListView myList = (ListView) findViewById(R.id.lvScores);
        myList.setAdapter(mAdapter);
        //
        registertClickAddButton();
        registerOnClickListView();
        registertClickSavePrefLabel();
        registertClickSettingsAboutLabel();

        EditText etBName = (EditText) findViewById(R.id.etBowlersName);
        //if (etBName.getText() == "") {
        //	tabHost.setCurrentTab(2);
        //}
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Scorecard>> loader, List<Scorecard> data) {
        mAdapter.setData(data);
        mScorecard = data;
        calculateSeasonAverage();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Scorecard>> loader) {

        mAdapter.setData(null);
    }

    @Override
    public android.support.v4.content.Loader<List<Scorecard>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getContentResolver();
        return new ScorecardListLoader(this, BowlingContract.URI_TABLE, mContentResolver);
    }

    private void registerOnClickListView() {

        final ListView myList = (ListView) findViewById(R.id.lvScores);
        //
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long lID) {

                if (mScorecard != null) {
                    Scorecard scorecard = mScorecard.get(position);
                    //
                    long lRowID = scorecard.getid();
                    int iSeasonID = Integer.parseInt(scorecard.getSeasonId());
                    String sBowlingDate = scorecard.getBowlingDate();
                    int iScore1 = Integer.parseInt(scorecard.getGame1());
                    int iScore2 = Integer.parseInt(scorecard.getGame2());
                    int iScore3 = Integer.parseInt(scorecard.getGame3());
                    int iTotal = Integer.parseInt(scorecard.getSeriesTotal());
                    int iAverage = Integer.parseInt(scorecard.getSeriesAverage());
                    //
                    // pass to Add Score screen.
                    Intent addScoreIntent = new Intent(parent.getContext(), AddEditScorecard.class);

                    final int result = 1;
                    addScoreIntent.putExtra("RowID", lRowID);
                    addScoreIntent.putExtra("SeasonID", iSeasonID);
                    addScoreIntent.putExtra("BowlingDate", sBowlingDate);
                    addScoreIntent.putExtra("Score1", iScore1);
                    addScoreIntent.putExtra("Score2", iScore2);
                    addScoreIntent.putExtra("Score3", iScore3);
                    addScoreIntent.putExtra("Total", iTotal);
                    addScoreIntent.putExtra("Average", iAverage);
                    //
                    startActivityForResult(addScoreIntent, result);
                    if (result == RESULT_OK) {
                        myList.setAdapter(mAdapter);
                    }
                }

            }
        });

    }

    private void registertClickAddButton() {

        Button btn = (Button) findViewById(R.id.btnAdd);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onClick_AddRecord();

            }
        });
    }

    private void registertClickSavePrefLabel() {

        TextView tvSavePref = (TextView) findViewById(R.id.tvSavePref);

        tvSavePref.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText etLeagueNm = (EditText) findViewById(R.id.etLeagueName);
                savePreferences("LeagueName", etLeagueNm.getText().toString());
                EditText etLeagueSeason = (EditText) findViewById(R.id.etLeagueSeason);
                savePreferences("LeagueSeason", etLeagueSeason.getText().toString());
                EditText etBName = (EditText) findViewById(R.id.etBowlersName);
                savePreferences("BowlersName", etBName.getText().toString());
                //
                Toast.makeText(getApplicationContext(), "The settings have been saved.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registertClickSettingsAboutLabel() {

        TextView tvAbout = (TextView) findViewById(R.id.tvAbout);

        tvAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onClick_About();

            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        sCSVFilename = "MyBowlingScorecard.csv";
        if (id == R.id.action_ExportData) {

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                onClick_ExportData();
                Toast.makeText(this, "Exported data to filename: " + sCSVFilename, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "ERROR: Unable to exported data.  External storage not mounted. ", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_RestoreData) {
            //
            File myFile = new File(Environment.getExternalStorageDirectory() + "/" + sCSVFilename);
            // Check if backup file exists.
            if (!myFile.exists()) {
                Toast.makeText(this, "ERROR: Unable to restore the bowling score data.  File: " + sCSVFilename + " does not exist.", Toast.LENGTH_LONG).show();
                return true;
            } else {
                onClick_RestoreData();

//                Cursor cursor = BowlingDB.getAllRows();
                //displayRecordSet(cursor);
                Toast.makeText(this, "Restored data from filename: " + sCSVFilename, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_DeleteAllData) {
            onClick_DeleteAllData();

            Toast.makeText(this, "All bowling scores have been deleted from the database.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_about) {
            onClick_About();
            return true;
        }
        if (id == R.id.action_exit) {
            super.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick_ExportData() {

        File myFile;
        //
        myFile = new File(Environment.getExternalStorageDirectory() + "/" + sCSVFilename);
        try {
            // Delete file if exists.
            if (myFile.exists()) myFile.delete();
            //
            myFile.createNewFile();
            //
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            //
            if (mScorecard != null) {
                for (Scorecard scorecard : mScorecard) {
                    String sDate =  scorecard.getBowlingDate();
                    //
                    myOutWriter.append(scorecard.getSeasonId() + ";" // = "SeasonID";
                            + scorecard.getBowlingDate() + ";" // = "BowlingDate";
                            + scorecard.getGame1() + ";" // = "Game1";
                            + scorecard.getGame2() + ";" // = "Game2";
                            + scorecard.getGame3() + ";" // = "Game3";
                            + scorecard.getSeriesTotal() + ";" // = "Total";
                            + scorecard.getSeasonAverage() + ";"); // = "Average";
                    myOutWriter.append("\n");
                    //
                }
                //
            }
            myOutWriter.close();
            fOut.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void onClick_RestoreData() {
        // TODO Auto-generated method stub
        try {
            File myFile = new File(Environment.getExternalStorageDirectory() + "/" + sCSVFilename);
            FileReader fr = new FileReader(myFile);
            //
            // Delete all of the data from the database.
            mContentResolver.delete(BowlingContract.Scorecard.CONTENT_URI, null, null);
            //
            BufferedReader buffer = new BufferedReader(fr);
            String line = "";
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] columns = line.split(";");
                    if (columns.length != 7) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    //
                    int iSeasonID = Integer.parseInt(columns[0]);
                    String sBowlingDate = columns[1].toString();
                    int iScore1 = Integer.parseInt(columns[2]);
                    int iScore2 = Integer.parseInt(columns[3]);
                    int iScore3 = Integer.parseInt(columns[4]);
                    int iTotal = Integer.parseInt(columns[5]);
                    int iAverage = Integer.parseInt(columns[6]);
                    //
                 //   mContentResolver = MainActivity.this.getContentResolver();
                    ContentValues values = new ContentValues();
                    Uri uri;
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_SEASONID, 1);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_BOWLING_DATE, sBowlingDate);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_GAME1, iScore1);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_GAME2, iScore2);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_GAME3, iScore3);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_TOTAL, iTotal);
                    values.put(BowlingContract.ScorecardColumns.SCORECARD_AVERAGE, iAverage);
                    //
                    Uri returned = mContentResolver.insert(BowlingContract.URI_TABLE,values);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void onClick_DeleteAllData() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Delete All Scores in Database?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Delete All Scores")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                        // Delete all of the data from the database.
                        mContentResolver.delete(BowlingContract.Scorecard.CONTENT_URI, null, null);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void onClick_About() {

        Intent about = new Intent(this, about.class);

        final int result = 1;
        startActivityForResult(about, result);

    }

    public void onClick_AddRecord() {

        Intent addScoreIntent = new Intent(this, AddEditScorecard.class);

        final int result = 1;
        startActivityForResult(addScoreIntent, result);

    }

    private void calculateStats() {
        //
        // Store the total number of bowling sessions.
        _iTotalBowlingSessions = mScorecard.size();
        _iTotalNoOfGames = 0;
        int iTotalScore = 0;
        //
        if (mScorecard != null) {
            for (Scorecard scorecard : mScorecard) {
                String sDate =  scorecard.getBowlingDate();
                //
                // Determine the Highest Game
                int iGame1 =  Integer.parseInt(scorecard.getGame1());
                if (iGame1 != 0) {
                    _iTotalNoOfGames += 1;
                }
                iTotalScore += iGame1;
                if (iGame1 > _iHighestGame) {
                    _iHighestGame = iGame1;
                    _sHighestGameDate = sDate;
                }
                if (iGame1 < _iLowestGame) {
                    _iLowestGame = iGame1;
                    _sLowestGameDate = sDate;
                }
                int iGame2 = Integer.parseInt(scorecard.getGame2());
                if (iGame2 != 0) {
                    _iTotalNoOfGames += 1;
                }
                iTotalScore += iGame2;
                if (iGame2 > _iHighestGame) {
                    _iHighestGame = iGame2;
                    _sHighestGameDate = sDate;
                }
                if (iGame2 < _iLowestGame) {
                    _iLowestGame = iGame2;
                    _sLowestGameDate = sDate;
                }
                int iGame3 = Integer.parseInt(scorecard.getGame3());
                if (iGame3 != 0) {
                    _iTotalNoOfGames += 1;
                }
                iTotalScore += iGame3;
                if (iGame3 > _iHighestGame) {
                    _iHighestGame = iGame3;
                    _sHighestGameDate = sDate;
                }
                if (iGame3 < _iLowestGame) {
                    _iLowestGame = iGame3;
                    _sLowestGameDate = sDate;
                }
                //
                int iTriple = Integer.parseInt(scorecard.getSeriesTotal());
                if (iTriple > _iHighestTriple) {
                    _iHighestTriple = iTriple;
                    _sHighestTripleDate = sDate;
                }
                if (iTriple < _iLowestTriple) {
                    _iLowestTriple = iTriple;
                    _sLowestTripleDate = sDate;
                }
                // Store the total averages for all games.
                int iAvg = Integer.parseInt(scorecard.getSeriesAverage());
                //
                // Change Average to Rolling Average.

                //
            }
            //
            // Calculate the overall average.
            _iSeasonAverage = iTotalScore / _iTotalNoOfGames;
            _iSeasonTotal = iTotalScore;
        }

    }

    private void displayStats() {
        //
        String sTotalBowlingSessions = "Total Bowling Sessions: " + _iTotalBowlingSessions;
        TextView tvTotalBowlingSessions = (TextView) findViewById(R.id.tvTotalBowlingSessions);
        tvTotalBowlingSessions.setText(sTotalBowlingSessions);

        String sTotalGames = "Total No. of Games: " + _iTotalNoOfGames;
        TextView tvTotalNoOfGames = (TextView) findViewById(R.id.tvTotalGames);
        tvTotalNoOfGames.setText(sTotalGames);

        String sSeasonTotal = "Season Total: " + _iSeasonTotal;
        TextView tvSeasonTotal = (TextView) findViewById(R.id.tvSeasonTotal);
        tvSeasonTotal.setText(sSeasonTotal);

        String sSeasonAverage = "Season Average: " + _iSeasonAverage;
        TextView tvSeasonAverage = (TextView) findViewById(R.id.tvSeasonAverage);
        tvSeasonAverage.setText(sSeasonAverage);

        String sHighestGame = "High Single Game: " + _iHighestGame + "  ( " + _sHighestGameDate + " )";
        TextView tvHighestGame = (TextView) findViewById(R.id.tvHighGame);
        tvHighestGame.setText(sHighestGame);

        String sHighestTriple = "High Triple Games: " + _iHighestTriple + "  ( " + _sHighestTripleDate + " )";
        TextView tvHighestTriple = (TextView) findViewById(R.id.tvHighTriple);
        tvHighestTriple.setText(sHighestTriple);

        String sLowestGame = "Low Single Game: " + _iLowestGame + "  ( " + _sLowestGameDate + " )";
        TextView tvLowestGame = (TextView) findViewById(R.id.tvLowGame);
        tvLowestGame.setText(sLowestGame);

        String sLowestTriple = "Low Triple Games: " + _iLowestTriple + "  ( " + _sLowestTripleDate + " )";
        TextView tvLowestTriple = (TextView) findViewById(R.id.tvLowTriple);
        tvLowestTriple.setText(sLowestTriple);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            char cActionIND = data.getCharExtra("ActionIND", 'N');
            long lRowID = data.getLongExtra("RowID", 0);
            int iSeasonID = data.getIntExtra("SeasonID", 0);
            String sDate = data.getStringExtra("BowlingDate");
            int iScore1 = data.getIntExtra("Score1", 0);
            int iScore2 = data.getIntExtra("Score2", 0);
            int iScore3 = data.getIntExtra("Score3", 0);
            int iTotal = data.getIntExtra("Total", 0);
            int iAverage = data.getIntExtra("Average", 0);

            switch (cActionIND) {
                case 'I':
//                    lRowID = BowlingDB.insertRow(iSeasonID, sDate, iScore1, iScore2, iScore3, iTotal, iAverage);
                    Toast.makeText(this, "Scores have been added - " + lRowID, Toast.LENGTH_LONG).show();
                    break;
                case 'U':
//                    BowlingDB.updateRow(lRowID, iSeasonID, sDate, iScore1, iScore2, iScore3, iTotal, iAverage);
                    Toast.makeText(this, "Scores have been updated - " + lRowID, Toast.LENGTH_LONG).show();
                    break;
                case 'D':
//                    BowlingDB.deleteRow(lRowID);
                    Toast.makeText(this, "Scores have been deleted - " + lRowID, Toast.LENGTH_LONG).show();
                    break;
            }
//            Cursor cursor = BowlingDB.getAllRows();
            //
            //displayRecordSet(cursor);
        } else {
            Toast.makeText(this, "Adding/Updating Score has been cancelled", Toast.LENGTH_LONG).show();
        }
    }

    private void calculateSeasonAverage() {
        //
        int iTotalGames = 0;
        int iTotalScores = 0;

        if (mScorecard != null) {
            for (Scorecard scorecard : mScorecard) {
                iTotalGames += 3;
                //
                long lRowID = 0; //cursor.getLong(BowlingDBAdapter.COL_ROWID);
                iTotalScores += Integer.parseInt(scorecard.getSeriesTotal());
                int iSeasonAverage = iTotalScores / iTotalGames;
                //
                scorecard.setSeasonAverage(String.valueOf(iSeasonAverage));
                //
            }
        }
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //
        String sLeagueName = sharedPreferences.getString("LeagueName", "");
        EditText etLeagueNm = (EditText) findViewById(R.id.etLeagueName);
        etLeagueNm.setText(sLeagueName);
        //
        String sLeagueSeason = sharedPreferences.getString("LeagueSeason", "");
        EditText etLeagueSeason = (EditText) findViewById(R.id.etLeagueSeason);
        etLeagueSeason.setText(sLeagueSeason);
        //
        String sBowlersName = sharedPreferences.getString("BowlersName", "");
        EditText etBName = (EditText) findViewById(R.id.etBowlersName);
        etBName.setText(sBowlersName);
        //
    }

    private void savePreferences(String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
