package com.crazy.panassistant.actvity;

import com.crazy.panassistant.business.comm.BaiDuOpenAPI;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Menu;
import android.view.WindowManager;

public class LoadActivity extends Activity {
	private static final int LOAD_DISPLAY_TIME = 1500;
	private BaiDuOpenAPI baiDuOpenAPI = new BaiDuOpenAPI();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.activity_load);
		baiDuOpenAPI.login(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.load, menu);
		return true;
	}

}
