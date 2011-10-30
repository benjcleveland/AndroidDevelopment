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
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, price INTEGER, preview VARCHAR, description VARCHAR)");
		
		// add entries to the database
		
		// TODO - modify this to use the strings.xml file
		
		db.execSQL( "insert into boats (title, price, preview, description) VALUES ('awesome boat', 150000, 'powerboat.png', '2011 Avalon AMBASSADOR 2011 AVALON Ambassador , This is priced with a 150HP Honda and a black powder coated tandem EZ loader trailer with disk brakes.')");
		db.execSQL( "insert into boats (title, price, preview, description) VALUES ('An even better boat awesome boat', 20000, 'sailboat.png', '2011 Avalon Eagle Family 2011 AVALON Eagle Family, Priced with a 15HP honda engine and an ez loader trailer')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE boats");
	}
}
