package com.washington.cleveb1;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BoatDatabase extends SQLiteOpenHelper  {

	private Context context;
	
	public BoatDatabase(Context context)
	{
		super(context, "BoatSailer", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//"IF NOT EXISTS"
		db.execSQL("CREATE TABLE boats (" 
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, price INTEGER, preview VARCHAR, description VARCHAR)");

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
					+ titles[i] + "', " + prices[i] + ", '" + previews[i] + "', '" + descriptions[i] 
					+ "')";
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE boats");
	}
}
