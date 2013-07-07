package com.crazy.panassistant.actvity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Menu;
import android.view.WindowManager;

public class LoadActivity extends Activity {
	private static final int LOAD_DISPLAY_TIME = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.activity_load);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// Go to main activity, and finish load activity
				Intent mainIntent = new Intent(LoadActivity.this,
						MainActivity.class);
				LoadActivity.this.startActivity(mainIntent);
				LoadActivity.this.finish();
			}
		}, LOAD_DISPLAY_TIME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load, menu);
		return true;
	}

}
