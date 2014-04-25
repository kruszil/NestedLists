package com.ilona.lists;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ListArrayAdapter extends ArrayAdapter<Lists> {

	Context context;
	int layoutResourceId;
	List<Lists> data = null;
	private DataSource datasource;
	private boolean isEditModeEnabled = false;

	public void setEditModeEnabled(boolean isEditModeEnabled) {
		this.isEditModeEnabled = isEditModeEnabled;
	}

	public ListArrayAdapter(Context context, int layoutResourceId,
			List<Lists> data) {

		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;

		datasource = new DataSource(context);
		datasource.open();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ListHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ListHolder();
			holder.name = (TextView) row.findViewById(R.id.list_name);
			holder.editName = (EditText) row.findViewById(R.id.edit_list_field);
			holder.deleteButton = (Button) row.findViewById(R.id.delete_list);

			row.setTag(holder);
		} else {
			holder = (ListHolder) row.getTag();
		}

		Lists list = data.get(position);

		holder.id = list.getId();

		holder.name = (TextView) row.findViewById(R.id.list_name);
		holder.name.setTag(list.getId());
		holder.name.setText(list.getName());
		holder.name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ItemsViewActivity.class);
				intent.putExtra("parentId", (Long) v.getTag());
				intent.putExtra("id", (Long) v.getTag());
				context.startActivity(intent);
			}
		});

		holder.editName = (EditText) row.findViewById(R.id.edit_list_field);
		holder.editName.setTag(list);
		holder.editName.setText(list.getName());

		holder.editName.setOnKeyListener(new EditText.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

				EditText editText = (EditText) arg0;
				Lists list = (Lists) editText.getTag();
				list.setName(editText.getEditableText().toString());
				list = datasource.updateList(list);
				editText.setTag(list);

				return false;
			}
		});

		holder.deleteButton = (Button) row.findViewById(R.id.delete_list);
		holder.deleteButton.setTag(list);
		holder.deleteButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Button button = (Button) view;
				Lists list = (Lists) button.getTag();
				datasource.deleteList(list.getId());
				remove(list);
			}
		});

		if (isEditModeEnabled == false) {
			holder.deleteButton.setVisibility(View.GONE);
			holder.editName.setVisibility(View.GONE);
			holder.name.setVisibility(View.VISIBLE);
		} else {
			holder.deleteButton.setVisibility(View.VISIBLE);
			holder.editName.setVisibility(View.VISIBLE);
			holder.name.setVisibility(View.GONE);
		}

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public void notifyDataSetChanged() {
		data = datasource.getAllLists();
		super.notifyDataSetChanged();
	}

	static class ListHolder {
		long id;
		TextView name;
		EditText editName;
		Button deleteButton;
	}

}
