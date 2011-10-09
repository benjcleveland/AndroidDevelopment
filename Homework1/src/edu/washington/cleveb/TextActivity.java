package edu.washington.cleveb;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextActivity extends Activity {
    private Button quitButton;
    private TextView randomTextView;
    
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
        quitButton.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// put the task in the background so we go back to the home screen
				moveTaskToBack(true);
				// finish this activity so when the app is restarted it will be on the home activity
				finish();
			}
		});
    }
}
