package com.bignerdranch.android.crimeintentrev.fragment;

import com.bignerdranch.android.crimeintentrev.utils.PictureUtils;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment{
	
	private static final String EXTRA_IMAGE_PATH = 
			"com.bignerdranch.android.crimeintentrev.extraImagePath";
	
	private ImageView imageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		String filePath = getArguments().getString(EXTRA_IMAGE_PATH);
		BitmapDrawable bitmapDrawable = 
				PictureUtils.getScaledDrawable(getActivity(), filePath);
		
		imageView = new ImageView(getActivity());
		imageView.setImageDrawable(bitmapDrawable);
		return imageView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(imageView);
	}

	public static ImageFragment newInstance(String filePath){
		
		Bundle args = new Bundle();
		args.putString(EXTRA_IMAGE_PATH, filePath);
		
		ImageFragment imageFragment = new ImageFragment();
		imageFragment.setArguments(args);
		imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return imageFragment;
	}
	
}
