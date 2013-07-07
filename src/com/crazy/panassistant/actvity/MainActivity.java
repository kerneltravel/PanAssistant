package com.crazy.panassistant.actvity;

import com.crazy.panassistant.business.comm.BaiDuOpenAPI;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private BaiDuOpenAPI baiDuOpenAPI;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		baiDuOpenAPI = new BaiDuOpenAPI();
		baiDuOpenAPI.login(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// baidu.LogOut();// 销毁activity对象后退出百度
	}

}
