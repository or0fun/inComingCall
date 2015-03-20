package com.iflytek.util;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Date;

public class Util {

	/**
	 * 启动Activity
	 * @param context
	 * @return 
	 */
	public static void startActivity(Context context, Class<?> name) {
		Intent intent = new Intent(context, name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		context.startActivity(intent);
	}
	/**
	 * 启动Activity
	 * @param context
	 * @param packageName
     * @param activityName
	 * @param actionName
	 */
	public static void startActivity(Context context, String packageName, String activityName, String actionName) {

		Intent intent = new Intent( );   
        ComponentName comp = new ComponentName(packageName, activityName);  
        intent.setComponent(comp);   
        intent.setAction(actionName); 
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent); 
	}
	/**
	 * 插入短信到收件箱
	 * 
	 * @param address
	 * @param read
	 * @param body
	 * @param date
	 * @param person
	 * @return
	 */
	public static Uri insertSMSToInbox(Context context, String address, int read, String body,
			long date, String person) {
		return insertSMS(context, Uri.parse("content://sms/inbox"), address, 1, read,
				body, date, person);
	}
	/**
	 * 插入短信
	 * @param address
	 * @param body
	 * @param person
	 * @return
	 */
	public static Uri insertSMSToInbox(Context context, String address, String body, String person) {
		return insertSMS(context, Uri.parse("content://sms/inbox"), address, 1, 0, body,
				new Date().getTime(), person);
	}

	/**
	 * 插入短信
	 * 
	 * @param smsBox
	 * @param address
	 * @param type
	 * @param read
	 * @param body
	 * @param date
	 * @param person
	 * @return
	 */
	public static Uri insertSMS(Context context, Uri smsBox, String address, int type, int read,
			String body, long date, String person) {
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("type", type);
		values.put("read", read);
		values.put("body", body);
		values.put("date", date);
		values.put("person", person);
		Uri uri = context.getContentResolver()
				.insert(smsBox, values);
		return uri;
	}
	/**
	 * 是否支持模拟来电
	 * @return
	 */
	public static boolean isSupportSimulateCall() {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			return false;
		};
		return true;
	}

}
