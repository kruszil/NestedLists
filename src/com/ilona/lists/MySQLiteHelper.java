package com.ilona.lists;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_LIST_ITEMS = "listItems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CHECKED = "checked";
	public static final String COLUMN_PARENT_ID = "parent_id";

	private static final String DATABASE_NAME = "lists.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_LIST_ITEMS_CREATE = "create table "
			+ TABLE_LIST_ITEMS
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_NAME
			+ " text not null, "
			+ COLUMN_CHECKED
			+ " integer not null, "
			+ COLUMN_PARENT_ID + " integer not null);";

	public static final String TABLE_LISTS = "lists";
	public static final String COLUMN_ID_LISTS = "_id";
	public static final String COLUMN_NAME_LISTS = "name";
	public static final String COLUMN_TIME_STAMP = "timeStamp";

	private static final String TABLE_LISTS_CREATE = "create table "
			+ TABLE_LISTS + "(" + COLUMN_ID_LISTS
			+ " integer primary key autoincrement, " + COLUMN_NAME_LISTS
			+ " text not null, " + COLUMN_TIME_STAMP
			+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_LIST_ITEMS_CREATE);
		database.execSQL(TABLE_LISTS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_LIST_ITEMS
				+ ";");
		db.execSQL("drop table if exists " + TABLE_LISTS + ";");
		onCreate(db);
	}

}
