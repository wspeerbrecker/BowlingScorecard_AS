package com.infoshare.bowlingscorecard;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private OnDateSetListener mDateSetListener;
	
	public DatePickerFragment(OnDateSetListener callback) {

		mDateSetListener = (OnDateSetListener) callback;

	}
	
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current date as the default date in the picker
	final Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);

	// Create a new instance of DatePickerDialog and return it
	return new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
}

public void onDateSet(DatePicker view, int year, int month, int day) {
// Do something with the date chosen by the user
	}

}

