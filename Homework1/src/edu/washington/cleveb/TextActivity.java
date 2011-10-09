package edu.washington.cleveb;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextActivity extends Activity {
    private Button quitButton;
    private TextView randomTextView;
    
    // this will handle the quit button click
    private View.OnClickListener quitListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			// create an intent that will launch the OS home screen
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			
			// start the intent
			startActivity( i );
			
			// finish this activity so when the app is restarted it will be on the home activity
			finish();	
		}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // set the content view
        setContentView( R.layout.textlayout );
    
        // get the string array
        Resources res = getResources();
        String[] random_text = res.getStringArray( R.array.random_strings );
    
        // get the text view
        randomTextView = (TextView) findViewById( R.id.random_text );
    
        // get a random index
        Random rand = new Random();
        
        // set the text to a random value
        randomTextView.setText( random_text[rand.nextInt(random_text.length)] );
        
        // setup the quit button
        quitButton = (Button) findViewById( R.id.quit_button );
        quitButton.setOnClickListener( quitListener );
    }
}
