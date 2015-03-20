package com.iflytek.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.call.CallActivity;
import com.iflytek.sms.SMSActivity;
import com.iflytek.util.Util;

public class MainActivity extends Activity {

	final String TAG = "MainActivity";
	
	Context mContext;

	/** 模拟来电 */
	Button mCallButton;
	/** 模拟来信 */
	Button mSmsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		mContext = this;

		mCallButton = (Button) findViewById(R.id.call);
		mCallButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Util.isSupportSimulateCall()) {
					Util.startActivity(mContext, CallActivity.class);
				}else {
					Toast.makeText(mContext, "Android 4.4及以上不支持来电模拟。", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 来电弹窗
		mSmsButton = (Button) findViewById(R.id.sms);
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
}
