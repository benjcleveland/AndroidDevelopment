package com.washington.cleveb4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class LanguageDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "LanguageDatabase";
	private static final String DATABASE_CREATE = "CREATE TABLE language (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, spanish VARCHAR, english VARCHAR)";
	
	
	public LanguageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// "IF NOT EXISTS"
		db.execSQL(DATABASE_CREATE);

		// fill in some initial data
		// this is only for testing
		db.execSQL("insert into language (category, spanish, english) values ('medical questions', 'spanish words', 'english words')" );
		db.execSQL("insert into language (category, spanish, english) values ('Greeting questions', 'spanish words1', 'english words2')" );
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}
