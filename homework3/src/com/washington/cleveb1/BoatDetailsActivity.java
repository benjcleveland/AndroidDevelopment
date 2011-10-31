package com.washington.cleveb1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BoatDetailsActivity extends Activity {

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
			TextView description = (TextView) findViewById(R.id.description);
			
			description.setText( extras.getString("desc"));
		}
	}
}
