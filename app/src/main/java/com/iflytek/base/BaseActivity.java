package com.iflytek.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iflytek.test.R;
import com.iflytek.util.Constant;
import com.iflytek.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity {

	protected Context mContext;
	protected static final int PICK_CONTACT = 1;
	protected PopupWindow mPopupWindow;
	/** 历史号码 */
	protected ImageButton mHistoryBtn;
	/** 输入区域 */
	protected View mInputLayout;
	/** 显示历史列表 */
	protected ListView mListView;
	/** 号码输入框 */
	protected EditText mNumberEditText;
	/** 选择联系人 */
	protected Button mSelectContactBtn;

	protected List<String> mNumbers;
	protected List<String> mSelectedNumbers;

	/** 最多显示数目 */
	protected int maxShowItemNum = 5;

	protected SharedPreferencesUtil mSharedPreferencesUtil;
	protected DropdownAdapter mAdapter;
	// 缓存号码
	public String mNumberString = "";
	/** 最后保存的号码 */
	public String mLastNumberString = "";
	/** 是否已启动 */
	protected boolean mIsStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(this);
		mNumbers = mSharedPreferencesUtil.readSharedPreferences(
				Constant.SAVED_NAME, Constant.SAVED_HISTORY_KEY);
		mSelectedNumbers = new ArrayList<String>();
		// 历史记录列表
		mAdapter = new DropdownAdapter(this, mNumbers);

		mListView = new ListView(this);
		mListView.setBackgroundColor(Color.LTGRAY);
		mListView.setAdapter(mAdapter);

		// 输入区域
		mInputLayout = (View) findViewById(R.id.inputLayout);
		// 选择联系人
		mSelectContactBtn = (Button) findViewById(R.id.select);
		mSelectContactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startPickContact();
			}
		});
		// 输入号码
		mNumberEditText = (EditText) findViewById(R.id.number);
		mNumberEditText.setText(readNumber());
		mNumberEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
				mNumberString = text.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mSelectedNumbers.clear();
				int len = mNumbers.size();
				for (int i = 0; i < len; i++) {
					if (mNumbers.get(i).startsWith(s.toString())) {
						mSelectedNumbers.add(mNumbers.get(i));
					}
				}
				mAdapter.setList(mSelectedNumbers);
				showPopupWindow(mSelectedNumbers);
			}
		});
		mNumberEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					hidePopupWindow();
				} else {

				}
				return false;
			}
		});
		mNumberEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if (hasFocus) {
					hidePopupWindow();
				}
			}
		});
		// 下拉历史记录
		mHistoryBtn = (ImageButton) findViewById(R.id.history);
		mHistoryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatePopupWindow(mNumbers);
				mAdapter.setList(mNumbers);
				if (mPopupWindow.isShowing()) {
					hidePopupWindow();
				} else {
					showPopupWindow(mNumbers);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsStarted = false;
		saveNumber(mNumberEditText.getText().toString());
		mSharedPreferencesUtil.saveSharedPreferences(Constant.SAVED_NAME,
				Constant.SAVED_HISTORY_KEY, mNumbers);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mIsStarted = true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_CONTACT: {
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getContentResolver().query(contactData, null, null,
						null, null);
				c.moveToFirst();
				String phoneNum = getContactPhone(c);
				mNumberString = phoneNum;
				mNumberEditText.setText(mNumberString);

				break;
			}
		}
		}
	}

	/**
	 * 选择联系人
	 */
	protected void startPickContact() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);
	}

	/**
	 * 显示下拉框
	 * 
	 * @param list
	 */
	protected void showPopupWindow(List<String> list) {
		if (false == mIsStarted) {
			return;
		}
		CreatePopupWindow(list);
		if (list.size() == 0) {
			hidePopupWindow();
		} else {
			if (false == mPopupWindow.isShowing()) {
				mPopupWindow.showAsDropDown(mInputLayout);
			}
		}
	}

	/**
	 * 隐藏下拉框
	 */
	protected void hidePopupWindow() {
		if (null != mPopupWindow && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 创建实例
	 * 
	 * @param list
	 */
	protected void CreatePopupWindow(List<String> list) {
		if (null == mPopupWindow) {
			mPopupWindow = new PopupWindow(this);
			mPopupWindow.setContentView(mListView);
			mPopupWindow.setWidth(mInputLayout.getWidth());
			mPopupWindow.setFocusable(false);
			mPopupWindow.setOutsideTouchable(true);
		}

		setPopupWindowHeight(list);
	}

	/**
	 * 设置高度
	 * 
	 * @param list
	 */
	protected void setPopupWindowHeight(List<String> list) {
		if (list.size() < maxShowItemNum) {
			mPopupWindow.setHeight(list.size() * mInputLayout.getHeight());
		} else {
			mPopupWindow.setHeight(maxShowItemNum * mInputLayout.getHeight());
		}
	}

	/**
	 * 读取上次输入框的号码
	 * 
	 * @return
	 */
	protected String readNumber() {
		mNumberString = mSharedPreferencesUtil.readSharedPreferences(
				Constant.SAVED_NAME, Constant.SAVED_KEY, "");
		return mNumberString;
	}

	/**
	 * 保存输入框号码
	 * 
	 * @param num
	 * @return
	 */
	protected boolean saveNumber(String num) {
		return mSharedPreferencesUtil.saveSharedPreferences(
				Constant.SAVED_NAME, Constant.SAVED_KEY, num);
	}

	/**
	 * 保存输入框号码
	 * 
	 * @param content
	 * @return
	 */
	protected boolean saveContent(String content) {
		return mSharedPreferencesUtil.saveSharedPreferences(
				Constant.SAVED_NAME, Constant.SAVED_CONTENT, content);
	}

	/**
	 * 读取输入框号码
	 *
	 * @return
	 */
	protected String readContent() {
		return mSharedPreferencesUtil.readSharedPreferences(
				Constant.SAVED_NAME, Constant.SAVED_CONTENT,
				getString(R.string.inputContent));
	}

	/**
	 * 获取联系人电话
	 * 
	 * @param cursor
	 * @return
	 */
	private String getContactPhone(Cursor cursor) {
		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人的电话号码的cursor;
			Cursor phones = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones.getColumnIndex(Phone.NUMBER);
					int typeindex = phones.getColumnIndex(Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);
					switch (phone_type) {
					case Phone.TYPE_MOBILE:
						if (null != phoneNumber
								&& phoneNumber.trim().length() > 0) {
							phoneResult = phoneNumber;
							break;
						}
					default:
						if (null != phoneNumber
								&& phoneNumber.trim().length() > 0) {
							phoneResult = phoneNumber;
						}

					}
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult;
	}

	/** 用于显示popupWindow内容的适配器 */
	public class DropdownAdapter extends BaseAdapter {

		public DropdownAdapter(Context context, List<String> list) {
			this.context = context;
			this.list = list;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.list_row, null);
			close = (ImageButton) convertView.findViewById(R.id.close_row);
			content = (TextView) convertView.findViewById(R.id.text_row);
			final String editContent = list.get(position);
			content.setText(editContent);
			// 点击选择
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mNumberEditText.setText(editContent);
					mPopupWindow.dismiss();
				}
			});
			// 删除
			close.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mNumbers.remove(position);
					mAdapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public void setList(List<String> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		private Context context;
		private LayoutInflater layoutInflater;
		private List<String> list;
		private TextView content;
		private ImageButton close;
	}
}
