package com.infoshare.bowlingscorecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class xxxscoreStats extends Activity {

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.statistics_layout);
		//
		Intent intent = getIntent();
		//
		_iTotalBowlingSessions= intent.getIntExtra("TotalBowlingSessions", 0);
		String sTotalBowlingSessions = "Total Bowling Sessions: " + _iTotalBowlingSessions;
		TextView tvTotalBowlingSessions  = (TextView)findViewById(R.id.tvTotalBowlingSessions);
		tvTotalBowlingSessions.setText(sTotalBowlingSessions);

		_iTotalNoOfGames = intent.getIntExtra("TotalNoOfGames", 0);
		String sTotalGames = "Total No of Games: " + _iTotalNoOfGames;
		TextView tvTotalNoOfGames  = (TextView)findViewById(R.id.tvTotalGames);
		tvTotalNoOfGames.setText(sTotalGames);

		_iSeasonTotal = intent.getIntExtra("SeasonTotal", 0);
		String sSeasonTotal = "Season Total: " + _iSeasonTotal;
		TextView tvSeasonTotal  = (TextView)findViewById(R.id.tvSeasonTotal);
		tvSeasonTotal.setText(sSeasonTotal);

		_iSeasonAverage = intent.getIntExtra("SeasonAverage", 0);
		String sSeasonAverage = "Season Average: " + _iSeasonAverage;
		TextView tvSeasonAverage  = (TextView)findViewById(R.id.tvSeasonAvg);
		tvSeasonAverage.setText(sSeasonAverage);
		
		_iHighestGame = intent.getIntExtra("HighestGame", 0);
		_sHighestGameDate = intent.getStringExtra("HighestGameDate");
		String sHighestGame = "High SingleGame: " + _iHighestGame + "  ( " + _sHighestGameDate + " )";
		TextView tvHighestGame  = (TextView)findViewById(R.id.tvHighGame);
		tvHighestGame.setText(sHighestGame);
		
		_iHighestTriple = intent.getIntExtra("HighestTriple", 0);
		_sHighestTripleDate = intent.getStringExtra("HighestTripleDate");
		String sHighestTriple = "High Triple Games: " + _iHighestTriple + "  ( " + _sHighestTripleDate + " )";
		TextView tvHighestTriple  = (TextView)findViewById(R.id.tvHighTriple);
		tvHighestTriple.setText(sHighestTriple);
		
		_iLowestGame = intent.getIntExtra("LowestGame", 0);
		_sLowestGameDate = intent.getStringExtra("LowestGameDate");
		String sLowestGame = "Low Single Game: " + _iLowestGame + "  ( " + _sLowestGameDate + " )";
		TextView tvLowestGame  = (TextView)findViewById(R.id.tvLowGame);
		tvLowestGame.setText(sLowestGame);
		
		_iLowestTriple = intent.getIntExtra("LowestTriple", 0);
		_sLowestTripleDate = intent.getStringExtra("LowestTripleDate");
		String sLowestTriple = "Low Triple Games: " + _iLowestTriple + "  ( " + _sLowestTripleDate + " )";
		TextView tvLowestTriple  = (TextView)findViewById(R.id.tvLowTriple);
		tvLowestTriple.setText(sLowestTriple);
		//
	}

}
