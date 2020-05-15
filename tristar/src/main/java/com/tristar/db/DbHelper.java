package com.tristar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


@SuppressWarnings("ALL")
public class DbHelper extends SQLiteOpenHelper {
	static String DATABASE_NAME="userdata";
	public static final String TABLE_NAME="user";
	public static final String KEY_FNAME="fname";
	public static final String KEY_LNAME="lname";
	public static final String KEY_LPASS ="lpass";
	public static final String KEY_LCOMPANYNAME ="lcompanyname";
	public static final String KEY_ID="id";
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, 3);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT,"+KEY_LPASS+" TEXT, "+KEY_LCOMPANYNAME+" TEXT)";
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);

	}

}
