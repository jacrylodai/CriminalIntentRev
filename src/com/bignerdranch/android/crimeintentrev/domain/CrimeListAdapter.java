package com.bignerdranch.android.crimeintentrev.domain;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.android.crimeintentrev.R;

public class CrimeListAdapter extends ArrayAdapter<Crime>{

	private int resource;
	
	public CrimeListAdapter(Context context, int resource, List<Crime> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Crime crime = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(
					resource, parent, false);
		}
		
		TextView tvItemCrimeCrimeTitle = 
				(TextView)convertView.findViewById(R.id.tv_item_crime_crime_title);
		tvItemCrimeCrimeTitle.setText(crime.getCrimeTitle());
		
		TextView tvItemCrimeCrimeDate = 
				(TextView)convertView.findViewById(R.id.tv_item_crime_crime_date);
		Date crimeDate = crime.getCrimeDate();
		
		String crimeDateStr = DateFormat.format("yyyy-MM-dd", crimeDate).toString();
		tvItemCrimeCrimeDate.setText(crimeDateStr);
		
		CheckBox cbItemCrimeCrimeSolved = 
				(CheckBox)convertView.findViewById(R.id.cb_item_crime_crime_solved);
		cbItemCrimeCrimeSolved.setChecked(crime.isCrimeSolved());
		
		return convertView;
	}

}
