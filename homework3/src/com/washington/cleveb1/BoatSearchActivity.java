package com.washington.cleveb1;

import java.text.NumberFormat;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

// search activity
public class BoatSearchActivity extends Activity {

	private static final String MAXIMUM = "maximum";
	private static final String MINIMUM = "minimum";
	
	private ArrayList<String> min_list = new ArrayList<String>();
	private ArrayList<String> max_list = new ArrayList<String>();
	
	private Spinner min_spinner;
	private Spinner max_spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// set the content view
		setContentView( R.layout.search_layout );
	
		int base = 25000;
		int incr = 25000;
		min_list.add("No min");
		max_list.add("No max");
		// this could be done in the strings.xml with an array but I am lazy... (don't want to enter all the numbers)
		for( int i = 0; i < 150; ++i)
		{
			String number = "$" + NumberFormat.getInstance().format(base + incr*i);
			min_list.add( number );
			max_list.add( number );
		}
		
		// setup the spinners
		min_spinner = (Spinner) findViewById( R.id.min_spinner );
		
		ArrayAdapter<String> min_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, min_list);
		min_spinner.setAdapter(min_adapter);
		
		// setup the max spinner
		max_spinner = (Spinner) findViewById( R.id.max_spinnner );
		
		ArrayAdapter<String> max_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, max_list);
		max_spinner.setAdapter(max_adapter);
		
		// setup the button
		Button search_button = (Button) findViewById( R.id.search_button );
		search_button.setOnClickListener( searchListener );
	}

	// handle the search button clicks
	private View.OnClickListener searchListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			
			// get the data from the spinners and pass it back to the calling activity
			Log.v("activity","seleced = " + min_spinner.getSelectedItem());
			
			Intent resultIntent = new Intent();
			
			// add data to the result intent
			resultIntent.putExtra(MINIMUM, (String) min_spinner.getSelectedItem());
			resultIntent.putExtra(MAXIMUM, (String) max_spinner.getSelectedItem());
			
			setResult(Activity.RESULT_OK, resultIntent);
			
			// finish the activity
			finish();
		}
	};
}
