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
	 * �����ʽintent�Ƿ���Activity��Ӧ�����У�����true�����ܰ�ȫ���������û�У�����false
	 * @param context
	 * @param intent ��ʽintent
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
