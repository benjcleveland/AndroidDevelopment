package com.washington.cleveb1;

import java.io.IOException;
import java.io.InputStream;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

public class BoatListActivity extends ListActivity {
    private SQLiteDatabase database;
	private CursorAdapter dataSource;
	
	private static final String fields[] = { "title", "price", "preview", BaseColumns._ID };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        BoatDatabase boatdb = new BoatDatabase( this );
        database = boatdb.getWritableDatabase();
        
        Cursor data = database.query("boats", fields, null, null, null, null, null);
     
        // create a simple cursor adapter
        dataSource = new PictureAdapter( this, R.layout.list_row, data, fields, new int[] { R.id.title, R.id.cost} );
        
        // connect the database to the list
        setListAdapter(dataSource);
    }
    
    
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
					// e.printStackTrace();
				}
			}
			
			return row;
		}
		
		// convert an asset to a bitmap
		private Bitmap assetToBitmap( String asset_name) throws IOException
		{
			InputStream istr = getAssets().open( asset_name);
			Bitmap bmap = BitmapFactory.decodeStream(istr);
			return bmap;
		}
    }
}