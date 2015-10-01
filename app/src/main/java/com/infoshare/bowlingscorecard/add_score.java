package com.infoshare.bowlingscorecard;

import java.util.Calendar;

import com.infoshare.bowlingscorecard.DatePickerFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class add_score extends Activity {

	private Button btnDate;
	private EditText evScore1, evScore2, evScore3;
	private TextView tvTotal, tvAverage;
	private TextView tvCancelBtn, tvSaveBtn;
	private long lRowID;
	
    private int mYear;
    private int mMonth;
    private int mDay;
    
    public String sYear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_score_layout);
		Intent intent = getIntent();
		
		lRowID = intent.getLongExtra("RowID", 0);
		Integer iSeasonID = intent.getIntExtra("SeasonID", 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String sBowlersName = sharedPreferences.getString("BowlersName", "Not Entered");
		TextView tvBName = (TextView)findViewById(R.id.tvBName);
		tvBName.setText("Name: " + sBowlersName);

		String sBowlingDate = intent.getStringExtra("BowlingDate");
		btnDate = (Button)findViewById(R.id.btnDate);
		btnDate.setText(sBowlingDate);
		
		Integer iScore1 = intent.getIntExtra("Score1", 0);
		evScore1 = (EditText)findViewById(R.id.editTextScore1);
		evScore1.setRawInputType(Configuration.KEYBOARD_QWERTY);
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
        .showSoftInput(evScore1, InputMethodManager.SHOW_FORCED);
		evScore1.requestFocus();
		if (iScore1 != 0)
		{ evScore1.setText(iScore1.toString()); }	
		
		Integer iScore2 = intent.getIntExtra("Score2", 0);
		evScore2 = (EditText)findViewById(R.id.editTextScore2);
		if (iScore2 != 0)
		{evScore2.setText(iScore2.toString());	}
		
		Integer iScore3 = intent.getIntExtra("Score3", 0);
		evScore3 = (EditText)findViewById(R.id.editTextScore3);
		if (iScore3 != 0)
		{evScore3.setText(iScore3.toString()); }
		
		Integer iTotalValue = intent.getIntExtra("Total", 0);
		tvTotal = (TextView)findViewById(R.id.tvTotalValue);
		tvTotal.setText(iTotalValue.toString());
		
		Integer iAvgValue = intent.getIntExtra("Average", 0);
		tvAverage = (TextView)findViewById(R.id.tvAverageValue);
		tvAverage.setText(iAvgValue.toString());
		//
		setupScoreChanges();
		setupSaveButton();
		setupCancelButton();
		//setupDeleteButton();
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if (lRowID == 0)
        {
            String sMonth = Integer.toString(mMonth+1);
            if (sMonth.length() == 1) sMonth = "0" + sMonth;
            String sDay = Integer.toString(mDay);
            if (sDay.length() == 1) sDay = "0" + sDay;
            //
    		String sDate = Integer.toString(mYear) + "-" + sMonth + "-" + sDay;
			btnDate.setText(sDate);
        }
	}

	private void setupScoreChanges() {
		
		evScore1.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				CalculateTotalAndAverage();
			}
			
		});

		evScore2.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				CalculateTotalAndAverage();
			}
			
		});
		
		evScore3.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				CalculateTotalAndAverage();
			}
		
		});
	}

	 // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

		@Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    //
                    String sMonth = Integer.toString(monthOfYear+1);
                    if (sMonth.length() == 1) sMonth = "0" + sMonth;
                    String sDay = Integer.toString(dayOfMonth);
                    if (sDay.length() == 1) sDay = "0" + sDay;
                    //
            		String sBowlingDate = Integer.toString(year) + "-" + sMonth + "-" + sDay;
            		btnDate.setText(sBowlingDate);
                }

            };
 
	public void showDatePickerDialog(View v) {
    	
        DialogFragment newFragment = new DatePickerFragment(mDateSetListener);
        newFragment.show(getFragmentManager(), "datePicker");

	}
   
	private void CalculateTotalAndAverage() {

		tvTotal = (TextView)findViewById(R.id.tvTotalValue);
		int iTotalValue = 0;
		String sSc1 = evScore1.getText().toString();
		if (sSc1.length() != 0)
		{ iTotalValue += Integer.parseInt(sSc1); }
		String sSc2 = evScore2.getText().toString();
		if (sSc2.length() != 0)
		{ iTotalValue += Integer.parseInt(sSc2); }
		String sSc3 = evScore3.getText().toString();
		if (sSc3.length() != 0)
		{ iTotalValue += Integer.parseInt(sSc3); }
		//
		tvTotal.setText(Integer.toString(iTotalValue));
		//
		// Calculate the average for games entered.
		tvAverage = (TextView)findViewById(R.id.tvAverageValue);
		int iAverage = 0;
		if (iTotalValue != 0)
		{ iAverage = iTotalValue / 3; }
		tvAverage.setText(Integer.toString(iAverage));
		
	}
	private void setupSaveButton() {

		TextView tvSave = (TextView) findViewById(R.id.tvSaveBtn);

		tvSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String sDate = btnDate.getText().toString();
				if (sDate == ""){ 
					Toast.makeText(v.getContext(), "Unable to save the data.  Bowling Data must be entered.", Toast.LENGTH_LONG).show();
					return;
				}
				Integer iScore1 = 0;
				Integer iScore2 = 0;
				Integer iScore3 = 0;
				EditText evScore1 = (EditText) findViewById(R.id.editTextScore1);
				if (evScore1.getText().toString().length() != 0)
				{
					//					Toast.makeText(v.getContext(), "Unable to save the data.  Game 1 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
					//					return;
					//				}
					iScore1 = Integer.parseInt(evScore1.getText().toString());
					if ((iScore1 <= 0) || (iScore1 > 450)){
						Toast.makeText(v.getContext(), "Unable to save the data.  Game 1 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
						return;
					}
				}
				EditText evScore2 = (EditText) findViewById(R.id.editTextScore2);
				if (evScore2.getText().toString().length() != 0)
				{
					//					Toast.makeText(v.getContext(), "Unable to save the data.  Game 2 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
					//					return;
					//				}
					iScore2 = Integer.parseInt(evScore2.getText().toString());
					if ((iScore2 <= 0) || (iScore2 > 450)){
						Toast.makeText(v.getContext(), "Unable to save the data.  Game 2 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
						return;
					}
				}
				EditText evScore3 = (EditText) findViewById(R.id.editTextScore3);
				if (evScore3.getText().toString().length() != 0)
				{
					//					Toast.makeText(v.getContext(), "Unable to save the data.  Game 3 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
					//					return;
					//				}
					iScore3 = Integer.parseInt(evScore3.getText().toString());
					if ((iScore3 <= 0) || (iScore3 > 450)){
						Toast.makeText(v.getContext(), "Unable to save the data.  Game 3 must be between 1 and 450 points.", Toast.LENGTH_LONG).show();
						return;
					}
				}
				//
				CalculateTotalAndAverage();
				Integer iTotal = Integer.parseInt(tvTotal.getText().toString());
				Integer iAverage = Integer.parseInt(tvAverage.getText().toString());
				//
				Intent intent = new Intent();
				intent.putExtra("BowlingDate", sDate);
				intent.putExtra("Score1", iScore1);
				intent.putExtra("Score2", iScore2);
				intent.putExtra("Score3", iScore3);
				intent.putExtra("Total", iTotal);
				intent.putExtra("Average", iAverage);
				intent.putExtra("RowID", lRowID);
				if (lRowID == 0)
				{
					intent.putExtra("ActionIND", 'I');
				}
				else {
					intent.putExtra("ActionIND", 'U');
				}
				//
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}
	private void setupCancelButton() {

		TextView tvCancel = (TextView) findViewById(R.id.tvCancelBtn);

		tvCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					
					setResult(Activity.RESULT_CANCELED, intent);
					finish();
					
				}
			});
	}
//	private void setupDeleteButton() {
//
//		Button btnDelete = (Button) findViewById(R.id.btnDelete);
//
//		if (lRowID == 0)
//		{
//			btnDelete.setEnabled(false);
//			btnDelete.setVisibility(View.INVISIBLE);
//		}
//		else
//		{
//			btnDelete.setEnabled(true);
//			btnDelete.setVisibility(View.VISIBLE);
//		}
//
//		btnDelete.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent();
//
//				intent.putExtra("ActionIND", 'D');
//				intent.putExtra("RowID", lRowID);
//				setResult(Activity.RESULT_OK, intent);
//				finish();
//
//			}
//		});
//}
	
}




