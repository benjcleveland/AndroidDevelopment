package com.washington.cleveb1;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class BoatDetailsActivity extends Activity {

	private static final String PREVIEW = "preview";
	private static final String DESCRIPTION = "description";
	private static final String PRICE = "price";
	private static final String TITLE = "title";
	private static final String TAG = "BoatDetailsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		
		// change the font and color of the title text
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// set the content view to the boat details
		setContentView(R.layout.boat_details);
		// change the title text
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_text);
		
		// get the info from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			ImageView picture = (ImageView) findViewById(R.id.boat_picture);
			TextView price = (TextView) findViewById(R.id.price);
			TextView description = (TextView) findViewById(R.id.description);
			TextView title =  (TextView) findViewById(R.id.details_title);
			
			// set the values of the text
			description.setText( extras.getString(DESCRIPTION));
			price.setText( extras.getString(PRICE));
			title.setText( extras.getString(TITLE));
			
			try {
				picture.setImageBitmap( assetToBitmap( extras.getString(PREVIEW)));
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
