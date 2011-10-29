package com.washington.cleveb1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BoatDatabase extends SQLiteOpenHelper  {

	public BoatDatabase(Context context)
	{
		super(context, "CursorDemo", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS boats (" 
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, price INTEGER)");
		
		db.execSQL( "insert into boats (title, price) VALUES ('awesome boat', 1)");
		db.execSQL( "insert into boats (title, price) VALUES ('An even better boat awesome boat', 2)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
