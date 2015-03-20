package com.iflytek.call;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iflytek.sms.SMSActivity;
import com.iflytek.test.R;
import com.iflytek.base.BaseActivity;
import com.iflytek.util.Constant;
import com.iflytek.util.Util;

public class CallActivity extends BaseActivity {

	final String TAG = "MainActivity";

	/** 挂断 */
	Button mIdleBtn;
	/** 来电 弹窗 */
	Button mRingingBtn;
	/** 进入模拟来信 */
	Button mSmsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.call_layout); 
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.call_title));

		// 挂断 隐藏来电弹窗
		mIdleBtn = (Button) findViewById(R.id.idle);
		mIdleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLastNumberString.length() > 0) {
					inComingCall(Constant.IDLE, mLastNumberString);
				}
			}
		});
		// 来电弹窗
		mRingingBtn = (Button) findViewById(R.id.ringing);
		mRingingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mNumberString = mNumberEditText.getText().toString();
				mLastNumberString = mNumberString;
				inComingCall(Constant.RINGING, mNumberString);
				if (null != mNumbers) {
					if (mNumbers.indexOf(mNumberString) == -1) {
						mNumbers.add(mNumberString);
						mAdapter.setList(mNumbers);
					}
				}
			}
		});
		mSmsButton = (Button)findViewById(R.id.sms);
		mSmsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Util.startActivity(mContext, SMSActivity.class);
			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 模拟发送来电广播
	 * 
	 * @param state
	 * @param number
	 */
	private void inComingCall(String state, String number) {
		if (mNumberString != null && mNumberString.length() > 0) {
			Intent intent = new Intent();
			intent.setAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
			// 可以模拟任意号码"13486377381"
			// "13511026174" 顺丰快递员
			intent.putExtra(TelephonyManager.EXTRA_INCOMING_NUMBER, number);
			// 振铃(RINGING),摘机(OFFHOOK)和空闲(IDLE)
			intent.putExtra(TelephonyManager.EXTRA_STATE, state);
			sendBroadcast(intent);
		}
	}

}
