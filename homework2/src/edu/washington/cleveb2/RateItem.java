package edu.washington.cleveb2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateItem extends Activity{

	private TextView quoteTextView;
	private RatingBar quoteRating;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_item);
		
		// get the info from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			quoteTextView = (TextView) findViewById( R.id.quote_text );
			quoteTextView.setText(extras.getString("QUOTE_TEXT"));
			
			quoteRating = (RatingBar) findViewById( R.id.quote_rating_bar );
			quoteRating.setRating(extras.getFloat("QUOTE_RATING"));
			
			Intent resultIntent = new Intent();
			resultIntent.putExtra("POSITION", (int)extras.getInt("POSITION"));
			resultIntent.putExtra("QUOTE_RATING", (float)5);
			setResult(1, resultIntent);
		}
	}
}
