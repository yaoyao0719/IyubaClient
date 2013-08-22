/**
 * 
 */
package com.iyuba.iyubaclient;

import com.iyuba.iyubaclient.util.AutoScrollTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author yao
 * 
 */
public class MainTopLeftDailog extends Activity {
	private LinearLayout layout, writeiyu, writeblog, report;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_top_left_dialog);
		layout = (LinearLayout) findViewById(R.id.main_dialog_layout);
		writeiyu = (LinearLayout) findViewById(R.id.writeiyu);
		writeblog = (LinearLayout) findViewById(R.id.writeblog);
		report = (LinearLayout) findViewById(R.id.report);
		writeiyu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainTopLeftDailog.this,
						PublishMoodActivity.class);
				startActivity(intent);
				finish();
			}
		});
		writeblog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainTopLeftDailog.this,
						PublishBlogActivity.class);
				startActivity(intent);
				finish();
			}
		});
		report.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainTopLeftDailog.this,
						POIListActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
