package com.crazy.panassistant.actvity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crazy.panassistant.business.comm.BaiDuOpenAPI;

import android.os.Bundle;

import android.app.TabActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ListActivity extends TabActivity {

	private static final String all_files = "全部";

	private static final String docs = "文档";

	private static final String pictures = "图片";

	private static final String music = "音乐";
	private BaiDuOpenAPI baiDuOpenAPI = new BaiDuOpenAPI();

	private String[] names = new String[] { "虎头", "弄玉", "李清照", "李白" };
	private int[] imageIds = new int[] { R.drawable.tiger, R.drawable.nongyu,
			R.drawable.qingzhao, R.drawable.libai };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost = getTabHost();
		// 设置使用TabHost布局
		LayoutInflater.from(this).inflate(R.layout.activity_list,
				tabHost.getTabContentView(), true);
		// 添加第一个标签页(全文见列表)
		tabHost.addTab(tabHost.newTabSpec("all'").setIndicator(all_files)
				.setContent(R.id.all));
		// 添加第二个标签页（文档）
		tabHost.addTab(tabHost.newTabSpec(docs).setIndicator(docs)
				.setContent(R.id.docs));
		// 添加第三个标签页（音乐）
		tabHost.addTab(tabHost.newTabSpec("music").setIndicator(music)
				.setContent(R.id.music));
		// 添加第四个标签页(图片)
		tabHost.addTab(tabHost.newTabSpec(pictures).setIndicator(pictures)
				.setContent(R.id.pictures));
		baiDuOpenAPI.list(this);
		// 创建一个List集合，List集合的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("header", imageIds[i]);
			listItem.put("personName", names[i]);
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.activity_list,
				new String[] { "personName", "header" }, new int[] { R.id.name,
						R.id.header });
		ListView list = (ListView) findViewById(R.id.list_all);
		// 为ListView设置Adapter
		list.setAdapter(simpleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

}
