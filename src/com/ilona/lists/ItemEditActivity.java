package com.ilona.lists;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ItemEditActivity extends Activity {

	private DataSource datasource;
	private Item item;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_edit);
		datasource = new DataSource(getApplicationContext());
		datasource.open();

		// changing actionBar title to the name of chosen list.
		ActionBar actionBar = getActionBar();
		long listId = (Long) getIntent().getExtras().get("list_id");
		Lists list = datasource.getList(listId);
		actionBar.setTitle(list.getName().toString());

		editText = (EditText) findViewById(R.id.edit_field);
		long id = (Long) getIntent().getExtras().get("item_id");
		item = datasource.getListItem(id);
		editText.setText(item.getName());
		item.setName(editText.getText().toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_edit, menu);
		return true;
	}

	public void confirmEdit(View view) {
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		item.setName(editText.getEditableText().toString());
		item = datasource.updateListItem(item);
		finish();
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

}
