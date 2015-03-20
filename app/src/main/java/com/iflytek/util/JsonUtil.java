package com.iflytek.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	final String JSON_PARAM_NUMBER = "number";
	private static JsonUtil mInstance = new JsonUtil();

	private JsonUtil() {
	};

	public static JsonUtil getInstance() {
		return mInstance;
	}

	/**
	 * 转化成json格式的字符串 保存
	 * 
	 * @return
	 */
	public String listToJson(List<String> list) {
		JSONArray jsonArray = new JSONArray();
		if (null != list) {
			int len = list.size();
			for (int i = 0; i < len; i++) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put(JSON_PARAM_NUMBER, list.get(i));
					jsonArray.put(jsonObject);
				} catch (JSONException e) {
				}
			}
		}
		return jsonArray.toString();
	}

	/**
	 * 读取 json格式的字符串转成contact list
	 * 
	 * @return
	 */
	public List<String> jsonToList(String json) {
		List<String> list = new ArrayList<String>();
		if (null != json && json.length() > 0) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(json);
				if (null != jsonArray && jsonArray.length() > 0) {
					int len = jsonArray.length();
					for (int i = 0; i < len; i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (null != jsonObject) {
							try {
								list.add(jsonObject
										.getString(JSON_PARAM_NUMBER));
							} catch (JSONException e1) {
							}
						}
					}
					//Collections.sort(list);
				}
			} catch (JSONException e1) {
			}
		}
		return list;
	}
}
