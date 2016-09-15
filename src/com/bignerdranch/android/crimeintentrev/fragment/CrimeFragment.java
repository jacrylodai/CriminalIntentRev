package com.bignerdranch.android.crimeintentrev.fragment;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bignerdranch.android.crimeintentrev.R;
import com.bignerdranch.android.crimeintentrev.activity.CrimeCameraActivity;
import com.bignerdranch.android.crimeintentrev.domain.Crime;
import com.bignerdranch.android.crimeintentrev.domain.Photo;
import com.bignerdranch.android.crimeintentrev.utils.CrimeLab;
import com.bignerdranch.android.crimeintentrev.utils.PictureUtils;

public class CrimeFragment extends Fragment{
	
	private static final String TAG = "CrimeFragment";
	
	public static final String EXTRA_CRIME_ID = 
			"com.bignerdranch.android.crimeintentrev.extra_crime_id";
	
	private static final String TAG_DATE_PICKER = "tagDatePicker";
	
	private static final String TAG_SHOW_IMAGE = "tagShowImage";
	
	private static final int REQUEST_CODE_DATE_PICKER = 1;

	private static final int REQUEST_CODE_TAKE_PICTURE = 2;

	private Crime crime;
	
	private EditText etCrimeCrimeTitle;
	
	private Button buttonCrimeCrimeDate;
	
	private CheckBox cbCrimeCrimeSolved;
	
	private ImageButton ibCrimeTakePicture;
	
	private ImageView ivCrimePhoto;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		
		Log.d(TAG, "onCreate");
		
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		crime = CrimeLab.getInstance(getActivity()).getCrimeById(crimeId);
		
		Log.d(TAG, crime.getCrimeTitle());
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		Log.d(TAG, "onCreateView");
		
		View view = inflater.inflate(R.layout.fragment_crime, container, false);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		etCrimeCrimeTitle = (EditText) view.findViewById(R.id.et_crime_crime_title);
		etCrimeCrimeTitle.setText(crime.getCrimeTitle());
		etCrimeCrimeTitle.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String content = s.toString();
				
				crime.setCrimeTitle(content);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		buttonCrimeCrimeDate = (Button) view.findViewById(R.id.button_crime_crime_date);
		
		updateUICrimeDate();
		buttonCrimeCrimeDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				FragmentManager fragmentManager = CrimeFragment.this.getActivity()
						.getSupportFragmentManager();
				
				DatePickerDialogFragment datePickerDialogFragment = 
						DatePickerDialogFragment.newInstance(crime.getCrimeDate());
				datePickerDialogFragment.setTargetFragment(CrimeFragment.this
						, REQUEST_CODE_DATE_PICKER);
				datePickerDialogFragment.show(fragmentManager, TAG_DATE_PICKER);
			}
		});
		
		cbCrimeCrimeSolved = (CheckBox)view.findViewById(R.id.cb_crime_crime_solved);
		cbCrimeCrimeSolved.setChecked(crime.isCrimeSolved());
		cbCrimeCrimeSolved.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				crime.setCrimeSolved(isChecked);
			}
			
		});
		
		ibCrimeTakePicture = (ImageButton) view.findViewById(R.id.ib_crime_take_picture);
		ibCrimeTakePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(),CrimeCameraActivity.class);
				startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
			}
		});
		
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				Camera.getNumberOfCameras()>0;
				
		if(!hasCamera){
			ibCrimeTakePicture.setEnabled(false);
		}
		
		ivCrimePhoto = (ImageView) view.findViewById(R.id.iv_crime_photo);
		ivCrimePhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Photo photo = crime.getPhoto();
				if(photo == null){
					return;
				}
				
				String fileName = photo.getFileName();
				String filePath = 
						getActivity().getFileStreamPath(fileName).getAbsolutePath();
				ImageFragment imageFragment = ImageFragment.newInstance(filePath);
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				imageFragment.show(fm, TAG_SHOW_IMAGE);
			}
		});
		
		return view;
	}
	
	public static final CrimeFragment newInstance(UUID crimeId){
		
		Bundle args = new Bundle();
		args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment crimeFragment = new CrimeFragment();
		crimeFragment.setArguments(args);
		
		return crimeFragment;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUEST_CODE_DATE_PICKER:
			if(resultCode == Activity.RESULT_OK){
				Date crimeDate = (Date) data.getSerializableExtra(
						DatePickerDialogFragment.EXTRA_CRIME_DATE);
				crime.setCrimeDate(crimeDate);
				updateUICrimeDate();
			}
			break;

		case REQUEST_CODE_TAKE_PICTURE:
			
			if(resultCode == Activity.RESULT_OK){
				
				String fileName = 
						data.getStringExtra(CrimeCameraFragment.EXTRA_PICTURE_FILENAME);
				Photo photo = new Photo();
				photo.setFileName(fileName);
				crime.setPhoto(photo);
				
				showPhoto();
			}
			break;
			
		default:
			break;
		}
	}
	
	public void updateUICrimeDate(){
		
		Date crimeDate = crime.getCrimeDate();
		String crimeDateStr = DateFormat.getMediumDateFormat(getActivity()).format(crimeDate);
		buttonCrimeCrimeDate.setText(crimeDateStr);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			
			if(NavUtils.getParentActivityName(getActivity()) != null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(ivCrimePhoto);
	}
	
	private void showPhoto(){
		
		Photo photo = crime.getPhoto();
		if(photo == null){
			return;
		}
		
		BitmapDrawable bitmapDrawable = null;
		String filePath = 
				getActivity().getFileStreamPath(photo.getFileName()).getAbsolutePath();
		bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), filePath);
		ivCrimePhoto.setImageDrawable(bitmapDrawable);		
	}
	
}