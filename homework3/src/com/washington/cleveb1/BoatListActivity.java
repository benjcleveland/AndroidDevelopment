package com.washington.cleveb1;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BoatListActivity extends ActionBarActivity {
    
	private static final int BOAT_SEARCH = 0;
	// constants
	private static final String PREVIEW = "preview";
	private static final String DESCRIPTION = "description";
	private static final String PRICE = "price";
	private static final String TITLE = "title";
	private static final String MAXIMUM = "maximum";
	private static final String MINIMUM = "minimum";
		
	private SQLiteDatabase database;
	private CursorAdapter dataSource;
	
	private Dialog mSplashScreen;
	
	private static final String fields[] = { TITLE, PRICE, DESCRIPTION,  PREVIEW, BaseColumns._ID };
	
	private static final String TAG = "BoatListActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	showSplashScreen();
    	super.onCreate(savedInstanceState);
        
        // show the splash screen
        //showSplashScreen();
        
        setContentView(R.layout.main);
        
        BoatDatabase boatdb = new BoatDatabase( this );
        database = boatdb.getWritableDatabase();
        
        Cursor data = database.query("boats", fields, null, null, null, null, "price");
    
        // create a simple cursor adapter
        dataSource = new PictureAdapter( this, R.layout.list_row, data, fields, new int[] { R.id.title, R.id.cost, R.id.description} );

        // connect the database to the list
        setListAdapter(dataSource);
    }
    
    // handle when the user clicks on the list view
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	Cursor c = dataSource.getCursor();
    	
    	// start intent to handle new activity
    	Intent intent = new Intent(getBaseContext(), BoatDetailsActivity.class );
    	
    	// pass the information to the other activity
    	intent.putExtra(DESCRIPTION, c.getString(c.getColumnIndex(DESCRIPTION)));
    	intent.putExtra(PREVIEW, c.getString(c.getColumnIndex(PREVIEW)));
    	intent.putExtra(TITLE, c.getString(c.getColumnIndex(TITLE)));

    	int price = c.getInt(c.getColumnIndex(PRICE));
    	intent.putExtra(PRICE, convertDollar( price ) );
    	
    	// start the other activity
    	startActivity(intent);
    }
    
    // this is so the splashscreen is not displayed when the user rotates the phone
    @Override
    public void onConfigurationChanged( Configuration newConfig )
    {
    	super.onConfigurationChanged(newConfig);
    	setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
    
    // handle any button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_search:
                
            	// start the search activity
                Intent intent = new Intent( getBaseContext(), BoatSearchActivity.class);
                
                startActivityForResult(intent, BOAT_SEARCH);
                
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // handle the results from activities
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if( resultCode == Activity.RESULT_OK)
    	{
    		if( requestCode == BOAT_SEARCH)
    		{
    			String min = data.getStringExtra(MINIMUM);
    			String max = data.getStringExtra(MAXIMUM);
    			
    			Log.v(TAG, "min = " + min +max);
    			
    			// convert to an integer
    			String minimum = dollarConvert( min);
    			String maximum = dollarConvert( max );
    			String search = null;
    			
    			// build the search query
    			if( minimum != null )
    			{
    				search = "price > " + minimum;
    			}
    			if( maximum != null)
    			{
    				if( search != null)
    					search = search + " and price < " +maximum;
    				else
    					search = "price < " + maximum;
    			}
    			
    			Cursor new_cursor = database.query("boats", null, search, null, null, null, "price");
    			
    			// deal with the case the search returned nothing
    			if( new_cursor.getCount() == 0 )
    			{
    				// notify the user the search did not return any results
    				// TODO - this does not work for some reason
    				Toast.makeText(getApplicationContext(), "Search did not return any results, displaying last search results", Toast.LENGTH_LONG);
    				
    				// close this cursor
    				new_cursor.close();
    			}
    			else
    			{
					// change the cursor
					dataSource.changeCursor(new_cursor);

					// notify the data set changed
					dataSource.notifyDataSetChanged();
    			}
    		}
    	}
    }

    // display the splash screen
    private void showSplashScreen(){
    	mSplashScreen = new Dialog( this, R.style.page_background );
    	
    	mSplashScreen.setContentView(R.layout.splashscreen);
    	mSplashScreen.setCancelable(false);
    	mSplashScreen.show();
    	
    	// make the splash screen go away after a set amount of time
    	final Handler handler = new Handler();
    	handler.postDelayed( new Runnable()
    	{
    		public void run() {
    			if( mSplashScreen != null)
    			{
    				mSplashScreen.dismiss();
    			}
    		}
    	}, 3000);
    }
    
    // make sure we clean everything up correctly
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	
    	// close the database
    	if( database != null)
    	{
    		database.close();
    	}
    	
    	// close the cursor
    	Cursor c = dataSource.getCursor();
    	if( c != null)
    	{
    		c.close();
    	}
    }
	
    // convert a dollar number to a string
	private String convertDollar( int number )
	{
		return NumberFormat.getCurrencyInstance().format(number);
	}
	
	// convert a dollar string to int
	private String dollarConvert( String dollar )
	{
		String ret = null;
		try {
			 ret = NumberFormat.getCurrencyInstance().parse(dollar).toString();
		} catch (ParseException e) {
			Log.v(TAG, "Error trying to convert dollar to an int");
		}
		return ret;
	}
	
    // extended the simple cursor adapter so we can add the image to the listview
    private class PictureAdapter extends SimpleCursorAdapter 
    {
		public PictureAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
    	
		public View getView( int position, View convertView, ViewGroup parent )
		{
			View row = super.getView(position, convertView, parent);
			ImageView icon = (ImageView) row.findViewById(R.id.preview);
			
			TextView price = (TextView) row.findViewById(R.id.cost);
			Cursor mCursor = getCursor();
			
			int prices = mCursor.getInt(mCursor.getColumnIndex(PRICE));
			// this will put a $ in front of the price text
			price.setText( convertDollar( prices ));
			
			// get the name of the image
			String filename = mCursor.getString(mCursor.getColumnIndex(PREVIEW));
			
			if( filename != null )
			{
				// load the bitmap into the image view
				try {
					Bitmap bmap = assetToBitmap(filename);
					icon.setImageBitmap(bmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// don't do anything for now - the default item will be displayed
					Log.e(TAG, "Received an exception setting bitmap", e);
				}
			}
			
			return row;
		}
		
		// convert an asset to a bitmap
		private Bitmap assetToBitmap(String asset_name) throws IOException {
			InputStream istr = getAssets().open(asset_name);
			Bitmap bmap = BitmapFactory.decodeStream(istr);
			return bmap;
		}
    }
}