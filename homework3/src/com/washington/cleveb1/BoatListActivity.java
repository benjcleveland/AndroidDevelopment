package com.washington.cleveb1;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class BoatListActivity extends ListActivity {
    private SQLiteDatabase database;
	private CursorAdapter dataSource;
	
	private Dialog mSplashScreen;
	
	private static final String fields[] = { "title", "price", "description",  "preview", BaseColumns._ID };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // show the splash screen
        showSplashScreen();

        setContentView(R.layout.main);
        
        BoatDatabase boatdb = new BoatDatabase( this );
        database = boatdb.getWritableDatabase();
        
        Cursor data = database.query("boats", fields, null, null, null, null, null);
    
        // create a simple cursor adapter
        dataSource = new PictureAdapter( this, R.layout.list_row, data, fields, new int[] { R.id.title, R.id.cost, R.id.description} );

        // connect the database to the list
        setListAdapter(dataSource);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	// TODO - how do we know which entry in the database this is?
    	Cursor c = dataSource.getCursor();
    	int i = c.getPosition();
    	String s = c.getString(c.getColumnIndex("preview"));
    	
    	// start intent to handle new activity
    }
    
    // display the splash screen
    private void showSplashScreen(){
    	mSplashScreen = new Dialog( this, R.style.splashscreen );
    	
    	mSplashScreen.setContentView(R.layout.splashscreen);
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
    
    
    // extended the simple cursor adapter so we can add the image to the listview
    private class PictureAdapter extends SimpleCursorAdapter 
    {
    	private Cursor mCursor;
		public PictureAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			mCursor = c;
			// TODO Auto-generated constructor stub
		}
    	
		public View getView( int position, View convertView, ViewGroup parent )
		{
			View row = super.getView(position, convertView, parent);
			ImageView icon = (ImageView) row.findViewById(R.id.preview);

			TextView price = (TextView) row.findViewById(R.id.cost);
			
			int prices = mCursor.getInt(mCursor.getColumnIndex("price"));
			price.setText("$" + NumberFormat.getInstance().format(prices));
			
			// get the name of the image
			String filename = mCursor.getString(mCursor.getColumnIndex("preview"));
			
			if( filename != null )
			{
				// load the bitmap into the image view
				try {
					Bitmap bmap = assetToBitmap(filename);
					icon.setImageBitmap(bmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// don't do anything for now - the default item will be displayed
					// e.printStackTrace();
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