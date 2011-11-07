package com.washington.cleveb1;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class BoatDatabase extends SQLiteOpenHelper  {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "BoatSBuyer";
	private static final String DATABASE_CREATE = "CREATE TABLE boats (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, price INTEGER, preview VARCHAR, description VARCHAR)";
	
	private Context context;
	
	public BoatDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	// create the database
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//"IF NOT EXISTS"
		db.execSQL( DATABASE_CREATE );

		// fill in all the initial data
		// get all the resource arrays
		Resources res = context.getResources();
		String[] titles = res.getStringArray(R.array.boat_titles);
		String[] previews = res.getStringArray(R.array.boat_pictures);
		String[] descriptions = res.getStringArray(R.array.boat_description);
		int[] prices = res.getIntArray(R.array.boat_prices);
		
		// add all the boat to the database
		for( int i = 0; i < titles.length; ++i )
		{
			String sql = "insert into boats (title, price, preview, description) values ('"
					+ titles[i] + "', " + prices[i] + ", '" + previews[i] + "', \"" + descriptions[i] 
					+ "\")";
			db.execSQL(sql);
		}
	}

	// upgrade the database if needed
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BoatDatabase.class.getName(), "Upgrading database from " + oldVersion + " to " + newVersion );
		// clear out the database
		db.execSQL("DROP TABLE IF EXISTS boats");
		// create a new one
		onCreate(db);
	}
}
