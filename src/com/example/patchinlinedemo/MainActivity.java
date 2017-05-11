package com.example.patchinlinedemo;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Field;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	private CheckBox mUsePatchCheck;
	private Button mShowMsgButton;
	private TextView mTextViewPatch;
	private Button mExitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mUsePatchCheck = (CheckBox)findViewById(R.id.usePatch);
		mUsePatchCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SharedPreferences sp = getSharedPreferences(MainApplication.SHARED_PREFERENCE_USE_PATCH, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean(MainApplication.KEY_USE_PATCH, arg1).commit();
			}
		});
		SharedPreferences sp = getSharedPreferences(MainApplication.SHARED_PREFERENCE_USE_PATCH, Context.MODE_PRIVATE);
    	boolean willUsePatch = sp.getBoolean(MainApplication.KEY_USE_PATCH, false);
    	mUsePatchCheck.setChecked(willUsePatch);
    	
		mShowMsgButton = (Button)findViewById(R.id.buttonShow);
		mShowMsgButton.setOnClickListener(this);
		
		mTextViewPatch = (TextView)findViewById(R.id.showResult);
		mTextViewPatch.setText("");
		
		mExitButton = (Button)findViewById(R.id.buttonExit);
		mExitButton.setOnClickListener(this);
		
		Log.d("PatchInlineDemo", "MainActivity onCreate");
		//testInline();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		switch(viewId) {
			case R.id.buttonShow:
				Log.d("PatchInlineDemo", "Click button Show");
				mTextViewPatch.setText(BugObject.fun());
				new Thread(new Runnable() {
					@Override
					public void run() {
						Log.d("PatchInlineDemo", "start run");
						for (int i = 0; i < 50000; i++) {
							if (i % 10000 == 1) {
								Log.d("PatchInlineDemo", "running i=" + i);
							}
							testInline();
						}
						Log.d("PatchInlineDemo", "end run");
					}
				}).start();
				break;
			case R.id.buttonExit:
				Log.d("PatchInlineDemo", "Click button Exit");
				System.exit(0);
				break;
			default:
				break;
		}
	}

	String testInline() {
		//android.util.Log.d("PatchInlineDemo", "enter testInline");
		return getObject();
	}

	String getObject() {
		//android.util.Log.d("PatchInlineDemo", "enter getObject");
		return BugObject.fun();
	}
}
