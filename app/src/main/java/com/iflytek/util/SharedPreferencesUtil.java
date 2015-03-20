package com.iflytek.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	private static SharedPreferencesUtil mInstance = null;
	private Context mContext = null;
	private SharedPreferencesUtil(Context context) {
		mContext = context;
	}
	public static SharedPreferencesUtil getInstance(Context context) {
		if (null == mInstance) {
			synchronized (SharedPreferencesUtil.class) {
				if (null == mInstance) {
					mInstance = new SharedPreferencesUtil(context);
				}
			}
		}
		return mInstance;
	}
	//保存号码接口
	public boolean saveSharedPreferences(String name, String key, String value) {
		Editor sharedata = mContext.getSharedPreferences(name, 0).edit();
		sharedata.putString(key, value);
		return sharedata.commit();
	}

	//读取号码接口
	public String readSharedPreferences(String name, String key,
			String defaultvalue) {
		SharedPreferences sharedata = mContext.getSharedPreferences(name, 0);
		String data = sharedata.getString(key, defaultvalue);
		return data;
	}

	// 保存list数据
	public boolean saveSharedPreferences(String name, String key,
			List<String> numbers) {
		SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor sharedata = sp.edit();
		sharedata.putString(key, JsonUtil.getInstance().listToJson(numbers));
		return sharedata.commit();
	}

	// 保存list数据
	public List<String> readSharedPreferences(String name, String key) {
		SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		return JsonUtil.getInstance().jsonToList(sp.getString(key, ""));
	}
}
