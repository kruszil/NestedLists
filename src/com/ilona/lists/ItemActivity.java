package com.ilona.lists;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ItemActivity extends Activity {

	private DataSource datasource;
	private Item item;
	private Lists list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		datasource = new DataSource(getApplicationContext());
		datasource.open();

		// changing actionBar title to the name of chosen list.
		ActionBar actionBar = getActionBar();
		long listId = (Long) getIntent().getExtras().get("list_id");
		list = datasource.getList(listId);
		actionBar.setTitle(list.getName().toString());

		long id = (Long) getIntent().getExtras().get("item_id");
		item = datasource.getListItem(id);
		TextView textView = (TextView) findViewById(R.id.item_name);
		textView.setText(item.getName());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item, menu);
		return true;
	}

	public void editItem(View view) {

		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();

		Intent intent = new Intent(getApplicationContext(),
				ItemEditActivity.class);
		intent.putExtra("item_id", item.getId());
		intent.putExtra("list_id", list.getId());
		startActivityForResult(intent, 1);

	}

	public void deleteItem(View view) {
		datasource = new DataSource(getApplicationContext());
		datasource.open();
		if (datasource.deleteListItem(item)) {
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
		} else {
			Log.i("delete", "Delete operation hasn't been successful");
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
		}

	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Log.i("EDIT", "result ok");

			}
		}

	}
}
