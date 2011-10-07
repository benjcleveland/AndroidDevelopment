package edu.washington.cleveb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Homework1Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView( this );
        tv.setText( "Hello, Android\n I like turtles!" );
        
        setContentView( tv );
    }
}