package edu.washington.cleveb2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class RateItem extends Activity{

	private TextView quoteTextView;
	private RatingBar quoteRating;
	private Intent resultIntent = new Intent();
	
	private OnRatingBarChangeListener ratingListener = new RatingBar.OnRatingBarChangeListener() {
		
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			
			// save the rating
			resultIntent.putExtra("QUOTE_RATING", rating);
			// setup the return data
			setResult(Activity.RESULT_OK, resultIntent);
			
			// end the activity
			finish();
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the layout for this activity
		setContentView(R.layout.rate_item);
		
		// get the info from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			quoteTextView = (TextView) findViewById( R.id.quote_text );
			quoteTextView.setText(extras.getString("QUOTE_TEXT"));
			
			quoteRating = (RatingBar) findViewById( R.id.quote_rating_bar );
			quoteRating.setRating(extras.getFloat("QUOTE_RATING"));
			
			// setup the quote rating handler
			quoteRating.setOnRatingBarChangeListener( ratingListener );
			
			// make sure we save the position
			resultIntent.putExtra("POSITION", (int)extras.getInt("POSITION"));
			resultIntent.putExtra("QUOTE_RATING", extras.getFloat("QUOTE_RATING"));
		}
	}
}
