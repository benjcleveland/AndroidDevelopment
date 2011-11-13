package com.washington.cleveb4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class LanguageDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 8;
	private static final String DATABASE_NAME = "LanguageDatabase";
	private static final String DATABASE_CREATE = "CREATE TABLE phrase (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, spanish VARCHAR, english VARCHAR, FOREIGN KEY ('category') REFERNCES category('_ID')";
	private static final String TABLE2_CREATE = "CREATE TABLE types (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, language VARCHAR)";
	private static final String TABLE3_CREATE = "CREATE TABLE category (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, language VARCHAR, FOREIGN KEY ('language') REFERENCES phrase (" + BaseColumns._ID + "))";
	
	public LanguageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// "IF NOT EXISTS"
		db.execSQL(DATABASE_CREATE);
		db.execSQL(TABLE2_CREATE);
		db.execSQL(TABLE3_CREATE);
		
		// fill in some initial data
		// this is only for testing
		db.execSQL("insert into phrase (category, spanish, english) values ('medical questions', 'spanish words', 'english words')" );
		db.execSQL("insert into phrase (category, spanish, english) values ('Greeting questions', 'spanish words1á', 'english words2')" );
		db.execSQL("insert into phrase (category, spanish, english) values ('Greeting questions', 'spanish words3é', 'english words3')" );
		db.execSQL("insert into phrase (category, spanish, english) values ('Greeting questions', 'spanish words4úó', 'english words4')" );
		
		// update the types table
		db.execSQL("insert into types (language) values ('English')" );
		db.execSQL("insert into types (language) values ('Español')" );
		
		db.execSQL("insert into category (language, name) values ('English', 'Greeting Questions')");
		db.execSQL("insert into category (language, name) values ('Español', 'Preguntas Saludo')");
		db.execSQL("insert into category (language, name) values ('English', 'Medical Questions')");
		db.execSQL("insert into category (language, name) values ('Español', 'Algunas de las preguntas')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LanguageDatabase.class.getName(), "Upgrading database from " + oldVersion + " to " + newVersion );
		// clear out the database
		db.execSQL("DROP TABLE IF EXISTS phrase" );
		db.execSQL("DROP TABLE IF EXISTS types");
		db.execSQL("DROP TABLE IF EXISTS category");
		// create a new one
		onCreate(db);
	}

}
