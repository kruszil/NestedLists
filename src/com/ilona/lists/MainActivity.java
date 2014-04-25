package com.ilona.lists;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DataSource datasource;
	private ArrayAdapter<Lists> arrayAdapter;
	private List<Lists> myLists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Nested Lists");

		datasource = new DataSource(this);
		datasource.open();

		// inflating footerView for footer.xml:
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TextView footerView = (TextView) inflater.inflate(R.layout.footer,
				null, false);

		myLists = datasource.getAllLists();

		arrayAdapter = new ListArrayAdapter(this, R.layout.activity_list,
				myLists);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(arrayAdapter);

		// add a footerView to the ListView:
		listView.addFooterView(footerView);

		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((TextView) v).getText().equals("Edit")) {
					((TextView) v).setText("Done");
					((ListArrayAdapter) arrayAdapter).setEditModeEnabled(true);
					arrayAdapter.notifyDataSetChanged();
				} else if (((TextView) v).getText().equals("Done")) {
					((TextView) v).setText("Edit");
					((ListArrayAdapter) arrayAdapter).setEditModeEnabled(false);
					arrayAdapter.notifyDataSetChanged();
				}

			}
		});

	}

	public void addList(View view) {
		EditText enterList = (EditText) findViewById(R.id.enter_listName);
		if (!(enterList.getText().toString().isEmpty())) {
			datasource.addList(enterList.getText().toString());
			enterList.setText("");
		}

		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
