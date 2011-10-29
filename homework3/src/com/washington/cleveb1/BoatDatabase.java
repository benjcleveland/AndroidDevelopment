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
		
		//"IF NOT EXISTS"
		db.execSQL("CREATE TABLE boats (" 
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, price INTEGER, preview VARCHAR)");
		
		// add entries to the database
		db.execSQL( "insert into boats (title, price, preview) VALUES ('awesome boat', 150000, 'powerboat.png')");
		db.execSQL( "insert into boats (title, price, preview) VALUES ('An even better boat awesome boat', 20000, 'sailboat.png')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
