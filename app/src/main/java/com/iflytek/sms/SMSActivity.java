package com.iflytek.sms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.iflytek.base.BaseActivity;
import com.iflytek.call.CallActivity;
import com.iflytek.test.R;
import com.iflytek.util.Util;

public class SMSActivity extends BaseActivity {

	final String TAG = "SMSActivity";
	/** 模拟来信 */
	Button mIncomingSmsButton;
	/** 模拟来信 */
	EditText mSmsContentEditText;
	/** 模拟来信 */
	String mSmsContent;
	/** 进入模拟来电 */
	Button mCallButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.sms_layout);
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.sms_title));  

		mIncomingSmsButton = (Button)findViewById(R.id.send);
		mIncomingSmsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Util.insertSMSToInbox(mContext, mNumberString, mSmsContent, "测试");
			}
		});

		mSmsContent = readContent();
		mSmsContentEditText = (EditText)findViewById(R.id.content);
		mSmsContentEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				mSmsContent = arg0.toString();
				saveContent(mSmsContent);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		mSmsContentEditText.setText(mSmsContent);
		
		mCallButton = (Button)findViewById(R.id.call);
		mCallButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Util.startActivity(mContext, CallActivity.class);
			}
		});
	}	
	
	@Override
	protected void onPause() {
		super.onPause();
	}
}
