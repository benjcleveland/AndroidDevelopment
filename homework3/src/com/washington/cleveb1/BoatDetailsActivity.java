package com.washington.cleveb1;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class BoatDetailsActivity extends Activity {

	private static final String TAG = "BoatDetailsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		
		// set the content view to the boat details
		setContentView(R.layout.boat_details);
		
		// get the info from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			ImageView picture = (ImageView) findViewById(R.id.boat_picture);
			TextView price = (TextView) findViewById(R.id.price);
			TextView description = (TextView) findViewById(R.id.description);
			
			description.setText( extras.getString("desc"));
			price.setText( extras.getString("price"));
			
			try {
				picture.setImageBitmap( assetToBitmap( extras.getString("picture")));
			} catch (IOException e) {
				Log.e(TAG, "Received an exception tyring to set the bitmap", e);
			}
		}
	}
	
	// convert an asset to a bitmap - would be nice to put this in its own class or something
	private Bitmap assetToBitmap(String asset_name) throws IOException {
		InputStream istr = getAssets().open(asset_name);
		Bitmap bmap = BitmapFactory.decodeStream(istr);
		return bmap;
	}
}
