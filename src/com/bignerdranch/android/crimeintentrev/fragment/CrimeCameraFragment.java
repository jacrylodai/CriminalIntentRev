package com.bignerdranch.android.crimeintentrev.fragment;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.crimeintentrev.R;

public class CrimeCameraFragment extends Fragment {
	
	private static final String TAG = "CrimeCameraFragment";
	
	public static final String EXTRA_PICTURE_FILENAME = 
			"com.bignerdranch.android.crimeintentrev.extraPictureFileName";

	private Camera mCamera;
	
	private SurfaceView svCameraView;
	
	private View flCrimeCameraBlock;
	
	private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			flCrimeCameraBlock.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			String fileName = UUID.randomUUID() + ".jpeg";
			
			boolean isSuccess = true;
			
			BufferedOutputStream bos = null;
			try {
				FileOutputStream fos = 
						getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
				bos = new BufferedOutputStream(fos);
				bos.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				Log.d(TAG, "Error save picture", e);
				isSuccess = false;
			} finally{
				if(bos != null){
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
						Log.d(TAG, "Error closing picture file", e);
						isSuccess = false;
					}
				}
			}
			
			if(isSuccess){
				Log.d(TAG, "Success save picture to file:"+fileName);
				
				Intent intent = new Intent();
				intent.putExtra(EXTRA_PICTURE_FILENAME, fileName);
				getActivity().setResult(Activity.RESULT_OK, intent);
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			
			getActivity().finish();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_crime_camera, container,false);
		
		svCameraView = 
				(SurfaceView) view.findViewById(R.id.sv_crime_camera_camera_view);
		SurfaceHolder surfaceHolder = svCameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {

				Log.d(TAG, "surfaceDestroyed");
				if(mCamera != null){
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {

				Log.d(TAG, "surfaceCreated");
				try{
					if(mCamera != null){
						mCamera.setPreviewDisplay(holder);
					}
				}catch(IOException ex){
					Log.d(TAG, "Error setting up preview display", ex);
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {

				Log.d(TAG, "surfaceChanged");
				if(mCamera == null){
					return;
				}
				
				Camera.Parameters parameters = mCamera.getParameters();
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), 
						width, height);
				parameters.setPreviewSize(s.width, s.height);
				
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(),width,height);
				parameters.setPictureSize(s.width, s.height);
				
				mCamera.setParameters(parameters);
				try{
					mCamera.startPreview();
				}catch(Exception ex){
					Log.d(TAG, "Could not start preview",ex);
					if(mCamera !=null){
						mCamera.release();
						mCamera = null;
					}
				}
			}
		});
		
		Button buttonTakePicture = 
				(Button) view.findViewById(R.id.button_crime_camera_take_picture);
		buttonTakePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCamera != null){
					mCamera.takePicture(shutterCallback, null ,jpegCallback);
				}
			}
		});
		
		flCrimeCameraBlock = view.findViewById(R.id.fl_crime_camera_block);
		flCrimeCameraBlock.setVisibility(View.INVISIBLE);
		
		return view;
	}
	
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
			mCamera = Camera.open();
		}else{
			mCamera = Camera.open(0);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
	
	private Size getBestSupportedSize(List<Size> sizeList,int width,int height){
		
		Log.d(TAG, "getBestSize");
		
		Size largestSize = null;
		int largestArea = 0;
		for(Size size:sizeList){
			Log.d(TAG, "size--> width:"+width+"  height:"+height);
			int area = size.width * size.height;
			if(area > largestArea){
				largestSize = size;
				largestArea = area;
			}
		}
		return largestSize;
	}
	
}
