package edu.washington.cleveb;

import edu.washington.cleveb.hw009.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class homework009Activity extends ListActivity implements
		OnClickListener {
	private static SimpleCursorAdapter cursorAdapter;
	private static Button addButton;
	private static EditText newTaskText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// query the content provider for all the tasks
		Cursor c = managedQuery(Tasks.CONTENT_URI, new String[] {
				Tasks.TASK_ID, Tasks.TASK_NAME }, null, null, null);
		c.moveToFirst();

		// create a simple cursor adapter
		cursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, c,
				new String[] { Tasks.TASK_NAME },
				new int[] { android.R.id.text1 });

		// set the list adapter
		setListAdapter(cursorAdapter);

		// save the edit text view
		newTaskText = (EditText) findViewById(R.id.task_text);

		// get the button
		addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(this);
	}

	// clicking on the item will delete it
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final CharSequence[] items = { "Edit", "Delete" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick an Item");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Cursor c = cursorAdapter.getCursor();
				int id = c.getInt(c.getColumnIndex(Tasks.TASK_ID));

				switch (which) {
				case 0:
					// create an intent to edit the task
					Intent intent = new Intent(getBaseContext(), EditTask.class);
					intent.putExtra(Tasks.TASK_ID, id);
					startActivity(intent);
					break;
				case 1:
					// delete this item
					getContentResolver().delete(Tasks.CONTENT_URI,
							Tasks.TASK_ID + " = " + id, null);
					break;
				}
			}
		});

		// show the dialog
		builder.create().show();
		super.onListItemClick(l, v, position, id);
	}

	// handle adding a task to the list
	public void onClick(View v) {
		String task = newTaskText.getText().toString();

		// make sure there is a string
		if (task.equals("") == false) {
			// add the task
			ContentValues values = new ContentValues();
			values.put(Tasks.TASK_NAME, task);
			getContentResolver().insert(Tasks.CONTENT_URI, values);

			// hide the on screen keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(newTaskText.getWindowToken(), 0);

			// clear the edit text
			newTaskText.setText("");
		}
	}
}