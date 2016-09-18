package com.bignerdranch.android.crimeintentrev.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.DatePicker;

import com.bignerdranch.android.crimeintentrev.R;
import com.bignerdranch.android.crimeintentrev.utils.LogUtil;

public class DatePickerDialogFragment extends DialogFragment{
	
	private static final String TAG = "DatePickerDialogFragment";

	public static final String EXTRA_CRIME_DATE = 
			"com.bignerdranch.android.crimeintentrev.extra_crime_date";
	
	private DatePicker datePicker;
	
	private Date crimeDate;
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		crimeDate = (Date) getArguments().getSerializable(EXTRA_CRIME_DATE);
		LogUtil.d(TAG, "onCreateDialog--->> init crimeDate:"+crimeDate);
		
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_dialog_date_picker, null);
		
		datePicker = (DatePicker) view.findViewById(R.id.dp_date_picker_date_picker);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(crimeDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
				crimeDate = calendar.getTime();
				LogUtil.d(TAG, "you choose date:"+calendar.getTime());
				
				getArguments().putSerializable(EXTRA_CRIME_DATE, crimeDate);
			}
		});
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(R.string.title_date_picker);
		dialog.setView(view);
		dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				setResult(Activity.RESULT_OK);
			}
		});
		dialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				setResult(Activity.RESULT_CANCELED);
			}
		});
		
		return dialog.create();
	}
	
	private void setResult(int resultCode){
		
		Fragment targetFragment = getTargetFragment();
		if(targetFragment == null){
			return;
		}
		
		int requestCode = getTargetRequestCode();
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_CRIME_DATE, crimeDate);
		
		targetFragment.onActivityResult(requestCode, resultCode, intent);
	}
	
	public static final DatePickerDialogFragment newInstance(Date crimeDate){
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_CRIME_DATE, crimeDate);
		
		DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
		datePickerDialogFragment.setArguments(bundle);
		return datePickerDialogFragment;
	}
	
	
}
