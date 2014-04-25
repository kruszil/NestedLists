package com.ilona.lists;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumnsItems = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_CHECKED,
			MySQLiteHelper.COLUMN_PARENT_ID };
	private String[] allColumnsLists = { MySQLiteHelper.COLUMN_ID_LISTS,
			MySQLiteHelper.COLUMN_NAME_LISTS };

	public DataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * adds String name and parentId to table of List Items and returns
	 * it as listItem
	 * 
	 * @param name
	 *            , parentId
	 * @return
	 */
	public Item addListItem(String name, long parentId) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_CHECKED, 0);
		values.put(MySQLiteHelper.COLUMN_PARENT_ID, parentId);

		long id = database.insert(MySQLiteHelper.TABLE_LIST_ITEMS,
				null, values);

		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_LIST_ITEMS, allColumnsItems,
				MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		Item item = cursorToListItem(cursor);
		cursor.close();
		return item;
	}

	/**
	 * updates ListItem in database.
	 * 
	 * @param item
	 * @return
	 */
	public Item updateListItem(Item item) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, item.getName());
		values.put(MySQLiteHelper.COLUMN_CHECKED, item.isChecked() ? 1 : 0);

		database.update(MySQLiteHelper.TABLE_LIST_ITEMS, values,
				MySQLiteHelper.COLUMN_ID + " = " + item.getId(), null);

		return item;
	}

	/**
	 * deletes ListItem from database
	 * 
	 * @param item
	 */
	public boolean deleteListItem(Item item) {
		return database.delete(MySQLiteHelper.TABLE_LIST_ITEMS,
				MySQLiteHelper.COLUMN_ID + " = " + item.getId(), null) == 0 ? true
				: false;
	}

	/**
	 * deletes all ListItems with the same parentId from database
	 * 
	 * @param parentId
	 */
	public boolean deleteListItems(long parentId) {
		return database.delete(MySQLiteHelper.TABLE_LIST_ITEMS,
				MySQLiteHelper.COLUMN_PARENT_ID + " = " + parentId, null) == 0 ? true
				: false;
	}

	/**
	 * returns list of all ListItem from ListItems table
	 */
	public List<Item> getAllListItems(long parentId) {
		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_LIST_ITEMS, allColumnsItems,
				MySQLiteHelper.COLUMN_PARENT_ID + " = " + parentId, null, null,
				null, null);

		List<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			do {
				Item item = cursorToListItem(cursor);
				items.add(item);
			} while (cursor.moveToNext());
		}

		cursor.close();
		return items;
	}

	public Item getListItem(long id) {
		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_LIST_ITEMS, allColumnsItems,
				MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);

		if (cursor.moveToFirst()) {
			Item item = cursorToListItem(cursor);
			cursor.close();
			return item;
		}
		cursor.close();
		return null;
	}

	/**
	 * returns cursor to all list items
	 * 
	 * @return
	 */
	public Cursor getCursorAllListItems() {
		return database.query(MySQLiteHelper.TABLE_LIST_ITEMS,
				allColumnsItems, null, null, null, null, null);
	}

	/**
	 * converts cursor to list item
	 * 
	 * @param cursor
	 * @return
	 */
	private Item cursorToListItem(Cursor cursor) {
		return new Item(cursor.getLong(0), cursor.getString(1),
				cursor.getInt(2));
	}

	/**
	 * adds String name to table of Lists and returns it as item of Lists
	 * 
	 * @param name
	 * @return
	 */
	public Lists addList(String name) {

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME_LISTS, name);
		long id = database.insert(MySQLiteHelper.TABLE_LISTS, null,
				values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_LISTS,
				allColumnsLists, MySQLiteHelper.COLUMN_ID_LISTS + " = " + id,
				null, null, null, null);
		cursor.moveToFirst();
		Lists lists = cursorToList(cursor);
		cursor.close();
		return lists;

	}

	/**
	 * updates Lists in database.
	 * 
	 * @param list
	 * @return
	 */
	public Lists updateList(Lists list) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME_LISTS, list.getName());

		database.update(MySQLiteHelper.TABLE_LISTS, values,
				MySQLiteHelper.COLUMN_ID_LISTS + " = " + list.getId(), null);

		return list;
	}

	// delete one list from list of lists
	public boolean deleteList(long id) {
		// delete list only if delete items has been successful
		if (deleteListItems(id)) {
			return database.delete(MySQLiteHelper.TABLE_LISTS,
					MySQLiteHelper.COLUMN_ID_LISTS + "=" + id, null) == 0 ? true
					: false;
		}
		return false;

	}

	/**
	 * returns list of all lists from Lists table
	 */
	public List<Lists> getAllLists() {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LISTS,
				allColumnsLists, null, null, null, null,
				MySQLiteHelper.COLUMN_TIME_STAMP + " DESC");

		List<Lists> lists = new ArrayList<Lists>();
		if (cursor.moveToFirst()) {
			do {
				Lists list = cursorToList(cursor);
				lists.add(list);
			} while (cursor.moveToNext());
		}

		cursor.close();
		return lists;
	}

	public Lists getList(long id) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LISTS,
				allColumnsLists, MySQLiteHelper.COLUMN_ID_LISTS + " = " + id,
				null, null, null, null);

		if (cursor.moveToFirst()) {
			Lists list = cursorToList(cursor);
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}

	/**
	 * returns cursor to all lists in List
	 * 
	 * @return
	 */
	public Cursor getCursorAllLists() {
		return database.query(MySQLiteHelper.TABLE_LISTS,
				allColumnsLists, null, null, null, null, null);
	}

	/**
	 * converts cursor to list in Lists
	 * 
	 * @param cursor
	 * @return
	 */
	private Lists cursorToList(Cursor cursor) {
		return new Lists(cursor.getLong(0), cursor.getString(1));
	}

}
