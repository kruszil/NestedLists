package com.ilona.lists;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ItemsViewActivity extends Activity {
	private DataSource datasource;
	private ArrayAdapter<Item> arrayAdapter;
	private String[] fromColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_CHECKED };
	private String[] toValues = { "", "", "" };
	private SimpleCursorAdapter simpleCursorAdapter;
	private List<Item> myShoppingListItems;
	private Long parentId;
	private Lists list;
	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_items_view);

		datasource = new DataSource(this);
		datasource.open();

		// changing actionBar title to the name of chosen list.
		ActionBar actionBar = getActionBar();
		id = getIntent().getExtras().getLong("id");
		list = datasource.getList(id);
		actionBar.setTitle(list.getName().toString());

		parentId = getIntent().getExtras().getLong("parentId");
		myShoppingListItems = datasource.getAllListItems(parentId);

		arrayAdapter = new ItemArrayAdapter(this,
				R.layout.activity_list_item_view, myShoppingListItems,
				parentId);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(arrayAdapter);

	}

	public void addItem(View view) {
		EditText enterItem = (EditText) findViewById(R.id.edit_message);
		if (!(enterItem.getText().toString().isEmpty())) {
			datasource.addListItem(enterItem.getText().toString(),
					parentId);
			enterItem.setText("");
		}
		arrayAdapter.notifyDataSetChanged();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.items_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				Log.i("SHOP", "result ok");
				arrayAdapter.notifyDataSetChanged();
			}

		}
	}

}
