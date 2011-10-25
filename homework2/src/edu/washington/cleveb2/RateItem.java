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
	
	private static final String TEXT_KEY = "text";
	private static final String RATING_KEY = "joke_rating";
	private static final String POSITION_KEY = "position";
	
	private OnRatingBarChangeListener ratingListener = new RatingBar.OnRatingBarChangeListener() {
		
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			
			// save the rating
			resultIntent.putExtra(RATING_KEY, rating);
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
			quoteTextView.setText(extras.getString(TEXT_KEY));
			
			quoteRating = (RatingBar) findViewById( R.id.quote_rating_bar );
			quoteRating.setRating(extras.getFloat(RATING_KEY));
			
			// setup the quote rating handler
			quoteRating.setOnRatingBarChangeListener( ratingListener );
			
			// make sure we save the position
			resultIntent.putExtra(POSITION_KEY, (int)extras.getInt(POSITION_KEY));
			resultIntent.putExtra(RATING_KEY, extras.getFloat(RATING_KEY));
		}
	}
}
