package edu.washington.cleveb;

import edu.washington.cleveb.hw009.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class homework009Activity extends ListActivity {
	private static SimpleCursorAdapter cursorAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ContentValues values = new ContentValues();
		values.put(Tasks.TASK_NAME, "this is a test");
		getContentResolver().insert(Tasks.CONTENT_URI, values);

		Cursor c = managedQuery(Tasks.CONTENT_URI, new String[] {
				Tasks.TASK_ID, Tasks.TASK_NAME }, null, null, null);
		c.moveToFirst();
		cursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, c,
				new String[] { Tasks.TASK_NAME },
				new int[] { android.R.id.text1 });

		// set the list adapter
		setListAdapter(cursorAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// delete this item 
		Cursor c = cursorAdapter.getCursor();
		String name = c.getString(c.getColumnIndex(Tasks.TASK_NAME));
		getContentResolver().delete(Tasks.CONTENT_URI, Tasks.TASK_NAME + " = '" + name +"'", null);
		super.onListItemClick(l, v, position, id);
	}
}