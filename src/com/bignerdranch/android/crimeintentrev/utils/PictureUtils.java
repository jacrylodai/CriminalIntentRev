package com.bignerdranch.android.crimeintentrev.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	
	private static final String TAG = "PictureUtils";

	/**
	 * 图片压缩加载到BitmapDrawable，把图片尺寸和屏幕尺寸进行对比，进行压缩
	 * @param activity
	 * @param filePath
	 * @return
	 */
	public static BitmapDrawable getScaledDrawable(Activity activity,String filePath){
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		float desWidth = display.getWidth();
		float desHeight = display.getHeight();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if( (srcWidth > desWidth) || (srcHeight > desHeight) ){
			int widthSampleSize = Math.round(srcWidth/desWidth);
			Log.d(TAG, "srcWidth:"+srcWidth+"--desWidth:"+desWidth
					+"--widthSampleSize:"+widthSampleSize);
			
			int heightSampleSize = Math.round(srcHeight/desHeight);
			Log.d(TAG, "srcHeight:"+srcHeight+"--desHeight:"+desHeight
					+"--heightSampleSize:"+heightSampleSize);
			
			if(widthSampleSize > heightSampleSize){
				inSampleSize = widthSampleSize;
			}else{
				inSampleSize = heightSampleSize;
			}
		}
		
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return new BitmapDrawable(activity.getResources(), bitmap);
	}
	
	/**
	 * 主动释放imageView中的图片占用的内存空间
	 * @param imageView
	 */
	public static void cleanImageView(ImageView imageView){
		
		Drawable drawable = imageView.getDrawable();
		if(drawable instanceof BitmapDrawable){
			BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
			bitmapDrawable.getBitmap().recycle();
			imageView.setImageDrawable(null);
		}
	}
	
}
