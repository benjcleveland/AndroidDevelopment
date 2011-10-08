package edu.washington.cleveb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Homework1Activity extends Activity {

	private Button textButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the content view
        setContentView( R.layout.main );

        // create the text button
        textButton = (Button) findViewById(R.id.activity_switcher);
        textButton.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent( Homework1Activity.this, TextActivity.class);
				Homework1Activity.this.startActivity( myIntent );
			}
		});
        
    }    
}

