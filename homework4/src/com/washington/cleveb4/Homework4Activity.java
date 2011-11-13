package com.washington.cleveb4;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class Homework4Activity extends ListActivity {
	
	private SQLiteDatabase langdb;
	private CursorAdapter dataSource;
	private CursorAdapter categorySource;
	
	private Spinner categorySpinner;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get the database
        LanguageDatabase langhelper = new LanguageDatabase( this );
        langdb = langhelper.getWritableDatabase();
        
        // get the spinner
        categorySpinner = (Spinner) findViewById( R.id.category_spinner );
        
        // get the cursor for the category spinner
        Cursor category_data = langdb.query("language", new String[] {"_id","category"}, null, null, null, null, null);
        categorySource = new SimpleCursorAdapter( this, android.R.layout.simple_spinner_item, category_data, new String[] {"category"}, new int[]{android.R.id.text1});
        
        // set the adapter for the spinner
        categorySpinner.setAdapter( categorySource );
        
        // get the cursor for the list view
        Cursor data = langdb.query("language", null, "category = 'medical questions'", null, null, null, null);
    
        
        dataSource = new SimpleCursorAdapter( this, android.R.layout.simple_list_item_2, data, new String[] {"english", "spanish" }, new int[]{android.R.id.text1, android.R.id.text2 } );
        
        // connect the database to the list
        setListAdapter(dataSource);
    }
}