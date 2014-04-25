package com.ilona.lists;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ItemArrayAdapter extends ArrayAdapter<Item> {

	Context context;
	int layoutResourceId;
	List<Item> data = null;
	private DataSource datasource;
	private long parentId;

	public ItemArrayAdapter(Context context, int layoutResourceId,
			List<Item> data, long parentId) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.parentId = parentId;
		datasource = new DataSource(context);
		datasource.open();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ListItemHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ListItemHolder();
			holder.name = (TextView) row.findViewById(R.id.list_item_name);
			holder.checked = (CheckBox) row
					.findViewById(R.id.list_item_checked);
			holder.optionsButton = (Button) row.findViewById(R.id.options);

			row.setTag(holder);
		} else {
			holder = (ListItemHolder) row.getTag();
		}

		Item listItem = data.get(position);
		holder.id = Long.valueOf(listItem.getId());
		holder.name = (TextView) row.findViewById(R.id.list_item_name);
		holder.name.setText(listItem.getName());
		holder.checked = (CheckBox) row.findViewById(R.id.list_item_checked);
		holder.checked.setChecked(listItem.isChecked());
		holder.checked.setTag(listItem);
		holder.optionsButton.setTag(listItem);
		holder.optionsButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Button button = (Button) view;
				Item item = (Item) button.getTag();
				Intent intent = new Intent(context, ItemActivity.class);
				intent.putExtra("item_id", item.getId());
				intent.putExtra("list_id", parentId);
				((ItemsViewActivity) context).startActivityForResult(intent, 1);
			}
		});

		// listening to single list item on click
		holder.checked.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CheckBox check = (CheckBox) view;
				Item updatedItem = (Item) check.getTag();
				updatedItem.setChecked(check.isChecked());
				datasource.updateListItem(updatedItem);
			}
		});

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public void notifyDataSetChanged() {
		data = datasource.getAllListItems(parentId);
		super.notifyDataSetChanged();
	}

	static class ListItemHolder {
		long id;
		TextView name;
		CheckBox checked;
		Button optionsButton;
	}

}