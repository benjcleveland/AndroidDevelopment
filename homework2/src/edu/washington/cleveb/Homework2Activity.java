package edu.washington.cleveb;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Homework2Activity extends Activity {
 
	private ListView listview;
	private Button add_text_button;
	private ArrayAdapter<String> list_adapter;
	private ArrayList<String> list_array = new ArrayList<String>();
	
	// handler for adding text
	private View.OnClickListener addTextListener = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// get text from the text field
			EditText text = (EditText) findViewById( R.id.edit_text );
			
			// add the text to the list
			add_to_list( text.getText().toString() );
			
			// clear the text box 
			text.setText("");
		}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        // initialize the list array
        list_array.addAll( Arrays.asList(getResources().getStringArray( R.array.text_string_list)));
    
        // setup the list adapter
        list_adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, list_array);
        
        // fill in the values for the list view
        listview = (ListView) findViewById( R.id.itemlistview );

        listview.setAdapter(list_adapter);
        
        // setup the add text button
        add_text_button = (Button) findViewById( R.id.add_text_button );
        add_text_button.setOnClickListener( addTextListener );
    }
    
    // adds a string to the list view
    private void add_to_list( String str )
    {
    	// make sure there is text before trying to add it (we don't want to add blank lines!)
    	if( str.length() > 0 )
    	{
    		// add the string to the array
    		list_array.add( str );
    		
    		// notify the listview that the list has been changed
    		list_adapter.notifyDataSetChanged();
    	}
    }
}