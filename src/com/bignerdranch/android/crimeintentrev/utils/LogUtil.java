package com.bignerdranch.android.crimeintentrev.utils;

import android.util.Log;

/**
 * 日志工具类
 * @author jacrylodai
 *
 */
public class LogUtil {
		
	private static final int VERBOSE = 0;
	
	private static final int DEBUG = 1;
	
	private static final int INFO = 2;
	
	private static final int WARN = 3;
	
	private static final int ERROR = 4;
	
	private static final int LEVEL_NO_INFO_SHOW = 5;
	
	//当前的日志级别
	private static final int CURR_LEVEL = VERBOSE;

	public static void v(String tag,String msg){
		
		if(CURR_LEVEL <= VERBOSE){
			Log.v(tag, msg);
		}
	}

	public static void v(String tag,String msg,Throwable tr){
		
		if(CURR_LEVEL <= VERBOSE){
			Log.v(tag, msg,tr);
		}
	}
	
	public static void d(String tag,String msg){
		
		if(CURR_LEVEL <= DEBUG){
			Log.d(tag, msg);
		}
	}

	public static void d(String tag,String msg,Throwable tr){
		
		if(CURR_LEVEL <= DEBUG){
			Log.d(tag, msg,tr);
		}
	}

	public static void i(String tag,String msg){
		
		if(CURR_LEVEL <= INFO){
			Log.i(tag, msg);
		}
	}

	public static void i(String tag,String msg,Throwable tr){
		
		if(CURR_LEVEL <= INFO){
			Log.i(tag, msg,tr);
		}
	}

	public static void w(String tag,String msg){
		
		if(CURR_LEVEL <= WARN){
			Log.w(tag, msg);
		}
	}

	public static void w(String tag,String msg,Throwable tr){
		
		if(CURR_LEVEL <= WARN){
			Log.w(tag, msg,tr);
		}
	}

	public static void e(String tag,String msg){
		
		if(CURR_LEVEL <= ERROR){
			Log.e(tag, msg);
		}
	}

	public static void e(String tag,String msg,Throwable tr){
		
		if(CURR_LEVEL <= ERROR){
			Log.e(tag, msg ,tr);
		}
	}
	
}
