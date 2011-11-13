package com.washington.cleveb4;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class Homework4Activity extends ListActivity {
	
	private static final String CATEGORY_TABLE = "category";
	private static final String PHRASE_TABLE = "phrase";
	private SQLiteDatabase langdb;
	private CursorAdapter dataSource = null;
	private CursorAdapter categorySource = null;
	private CursorAdapter languageSource = null;
	
	private Spinner categorySpinner;
	private Spinner languageSpinner;
	
	private OnItemSelectedListener category_listener = new OnItemSelectedListener()
	{

		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
				long id) {
			// get the current cursor
			Cursor c = categorySource.getCursor();
			
			Log.v("hw4", "cursor value" + c.getString(c.getColumnIndex("name")) );
			
			String selection = "category_number = " + c.getString(c.getColumnIndex("number")) +"";
			
			// we need to update the listview selection
			Cursor new_cursor = langdb.query(PHRASE_TABLE, new String[] {"english", "spanish", "_ID"}, selection, null, null, null, null);
			
			// update the listview cursor
			dataSource.changeCursor(new_cursor);
			
			// notify the listview the data changed
			dataSource.notifyDataSetChanged();
		}

		public void onNothingSelected(AdapterView<?> parentView) {
			// TODO Auto-generated method stub
			
		}
	};

	private OnItemSelectedListener language_listener = new OnItemSelectedListener()
	{

		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
				long id) {
			// get the current cursor
			Cursor c = languageSource.getCursor();
			
			Log.v("hw4", "cursor value" + c.getString(c.getColumnIndex("language")) );
			
			// update the language
			changeLanguage(c.getString(c.getColumnIndex("language")));
		}

		public void onNothingSelected(AdapterView<?> parentView) {
			// TODO Auto-generated method stub
			
		}
	};
	
	// change the primary language
	private void changeLanguage(String lang)
	{
		int[] order;
		Log.v("hw4", "language = " + lang );
		if( lang.equals("English") )
		{
			order = new int[]{ android.R.id.text1, android.R.id.text2 };
		}
		else
		{
			order = new int[] { android.R.id.text2, android.R.id.text1 };
		}
		
		// change the order of the languages
		dataSource = new SimpleCursorAdapter( getBaseContext(), android.R.layout.simple_list_item_2, dataSource.getCursor(), new String[] {"english", "spanish" }, order );
		setListAdapter(dataSource);
		
		// update the category cursor
		Cursor categoryCursor = langdb.query(CATEGORY_TABLE, new String[] {"_ID", "name", "number" },	"language = '" + lang +"'", null, null, null, null);
		categorySource.changeCursor(categoryCursor);
		categorySource.notifyDataSetChanged();
	}
	
	
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
        Cursor category_data = langdb.query(CATEGORY_TABLE, new String[]{"_ID", "name", "number"}, "language = 'English'", null, null, null, null);
        
        categorySource = new SimpleCursorAdapter( this, android.R.layout.simple_spinner_item, category_data, new String[] {"name"}, new int[]{android.R.id.text1});
        
        // set the adapter for the spinner
        categorySpinner.setAdapter( categorySource );
        
        // set the click listener
        categorySpinner.setOnItemSelectedListener( category_listener );
        
        // setup the language spinner
        languageSpinner = (Spinner) findViewById( R.id.language_spinner );
        Cursor language_data = langdb.query("types", new String[] {"_ID", "language" }, null, null, null, null, null);
        
        // create the spinner adapter
        languageSource = new SimpleCursorAdapter( this, android.R.layout.simple_spinner_item, language_data, new String[]{"language"}, new int[]{android.R.id.text1});
        languageSpinner.setAdapter(languageSource);
        
        languageSpinner.setOnItemSelectedListener( language_listener );
        
        // get the cursor for the list view
        Cursor data = langdb.query(PHRASE_TABLE, new String[] {"english", "spanish", "_ID"}, "category_number = 1", null, null, null, null);
    
        dataSource = new SimpleCursorAdapter( this, android.R.layout.simple_list_item_2, data, new String[] {"english", "spanish" }, new int[]{android.R.id.text1, android.R.id.text2 } );
        
        // connect the database to the list
        setListAdapter(dataSource);
    }
    
    // make sure we clean up everything correctly
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	
    	if( langdb != null)
    	{
    		langdb.close();
    	}
    	
    	Cursor c = dataSource.getCursor();
    	if( c != null)
    	{
    		c.close();
    	}
    	
    	c = categorySource.getCursor();
    	if( c != null )
    	{
    		c.close();
    	}
    	
    	c = languageSource.getCursor();
    	if( c != null )
    	{
    		c.close();
    	}
    }
}