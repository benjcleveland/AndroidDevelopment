package edu.washington.cleveb2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.washington.cleveb2.R;

public class Homework2Activity extends Activity {

	private ListView listview;
	private Button add_text_button;
	private ArrayAdapter<String> list_adapter;
	private ArrayList<HashMap<String, Object>> list_array = new ArrayList<HashMap<String, Object>>();

	// handler for adding text
	private View.OnClickListener addTextListener = new View.OnClickListener() {

		public void onClick(View arg0) {
			// get text from the text field
			EditText text = (EditText) findViewById(R.id.edit_text);

			// add the text to the list
			// the initial rating should be 0
			add_to_list(text.getText().toString(), 0);

			// notify the listview that the list has been changed
			list_adapter.notifyDataSetChanged();

			// clear the text box
			text.setText("");

			// hide the keyboard now that the user is done
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		// get the list view
		listview = (ListView) findViewById(R.id.itemlistview);

		// get the string array
		for( String str : getResources().getStringArray( R.array.text_string_list ) ) 
		{
			// initialize the array with values
			add_to_list( str, 0 );
		}

		// set the list view adapter
		listview.setAdapter(new ratingListAdapter(list_array, this));
		
		// setup the add text button
		add_text_button = (Button) findViewById(R.id.add_text_button);
		add_text_button.setOnClickListener(addTextListener);
	}

	// adds a string to the list view
	private void add_to_list(String str, Integer rating) {
		// make sure there is text before trying to add it (we don't want to add
		// blank lines!)
		if (str.length() > 0) {
			
			// create a hash map for the object
			HashMap<String, Object> hm;
			hm = new HashMap<String, Object>();
			hm.put("text", str);
			hm.put("rating", rating);
			list_array.add(hm);
			
		}
	}

	private class ratingListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> List;

		private LayoutInflater mInflater;

		public ratingListAdapter(ArrayList<HashMap<String, Object>> list,
				Context context) {
			List = list;
			mInflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_rating, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.textView1);
				holder.rating = (RatingBar) convertView.findViewById(R.id.ratingBar1);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.text.setText((String) List.get(position).get("text"));
			holder.rating.setRating((Integer) List.get(position).get("rating"));
			// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 :
			// mIcon2);

			return convertView;

		}

		class ViewHolder {
			TextView text;
			RatingBar rating;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return List.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return List.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}

}
