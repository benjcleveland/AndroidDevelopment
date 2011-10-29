package com.washington.cleveb1;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class BoatListActivity extends ListActivity {
    private SQLiteDatabase database;
	private CursorAdapter dataSource;
	
	private static final String fields[] = { "title", "price", BaseColumns._ID };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        BoatDatabase boatdb = new BoatDatabase( this );
        database = boatdb.getWritableDatabase();
        
        Cursor data = database.query("boats", fields, null, null, null, null, null);
     
        dataSource = new SimpleCursorAdapter( this, R.layout.list_row, data, fields, new int[] { R.id.title, R.id.cost} );
        
        setListAdapter(dataSource);
        
    }
}