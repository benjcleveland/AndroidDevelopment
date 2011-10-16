package edu.washington.cleveb;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Homework2Activity extends Activity {
 
	private ListView listview;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        // fill in the values for the list view
       /* listview = (ListView) findViewById( R.id.itemlistview );
        
        Resources res = getResources();
        
        listview.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, res.getStringArray(R.array.text_string_list)));
        */
    }
}