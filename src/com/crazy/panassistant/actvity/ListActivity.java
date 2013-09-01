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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ListActivity extends TabActivity {

	private static final String all_files = "ȫ��";

	private static final String docs = "�ĵ�";

	private static final String pictures = "ͼƬ";

	private static final String music = "����";
	private BaiDuOpenAPI baiDuOpenAPI = new BaiDuOpenAPI();

	private String[] names = new String[] { "��ͷ", "Ū��", "������", "���" };
	private int[] imageIds = new int[] { R.drawable.tiger, R.drawable.nongyu,
			R.drawable.qingzhao, R.drawable.libai };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost = getTabHost();
		// ����ʹ��TabHost����
		LayoutInflater.from(this).inflate(R.layout.activity_list,
				tabHost.getTabContentView(), true);
		// ��ӵ�һ����ǩҳ(ȫ�ļ��б�)
		tabHost.addTab(tabHost.newTabSpec("all'").setIndicator(all_files)
				.setContent(R.id.all));
		// ��ӵڶ�����ǩҳ���ĵ���
		tabHost.addTab(tabHost.newTabSpec(docs).setIndicator(docs)
				.setContent(R.id.docs));
		// ��ӵ�������ǩҳ�����֣�
		tabHost.addTab(tabHost.newTabSpec("music").setIndicator(music)
				.setContent(R.id.music));
		// ��ӵ��ĸ���ǩҳ(ͼƬ)
		tabHost.addTab(tabHost.newTabSpec(pictures).setIndicator(pictures)
				.setContent(R.id.pictures));
		baiDuOpenAPI.list(this);
		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("header", imageIds[i]);
			listItem.put("personName", names[i]);
			listItems.add(listItem);
		}
		// ����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.activity_list,
				new String[] { "personName", "header" }, new int[] { R.id.name,
						R.id.header });
		ListView list = (ListView) findViewById(R.id.list_all);
		// ΪListView����Adapter
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	private List<String> getData() {

		List<String> data = new ArrayList<String>();
		data.add("��������1");
		data.add("��������2");
		data.add("��������3");
		data.add("��������4");
		data.add("��������5");
		data.add("��������6");
		data.add("��������7");
		data.add("��������8");
		return data;
	}

}
