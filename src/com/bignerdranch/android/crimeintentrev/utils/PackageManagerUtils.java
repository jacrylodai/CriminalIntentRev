package com.bignerdranch.android.crimeintentrev.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class PackageManagerUtils {
	
	private static final String TAG = "PackageManagerUtils";
	
	/**
	 * 检测隐式intent是否有Activity响应，如有，返回true，就能安全启动，如果没有，返回false
	 * @param context
	 * @param intent 隐式intent
	 * @return
	 */
	public static boolean isIntentLaunchSafe(Context context,Intent intent){
		
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfosList = pm.queryIntentActivities(intent, 0);
		int size = resolveInfosList.size();
		Log.d(TAG, "resolveInfo size:"+size);
		
		if(size>0){
			return true;
		}else{
			return false;
		}
	}

}
