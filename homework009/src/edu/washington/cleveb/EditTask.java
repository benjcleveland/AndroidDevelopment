package edu.washington.cleveb;

import edu.washington.cleveb.hw009.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class EditTask extends Activity implements OnClickListener {

	private static EditText taskText;
	private static Button updateButton;
	private static int task_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task_layout);

		taskText = (EditText) findViewById(R.id.editText);
		updateButton = (Button) findViewById(R.id.updateButton); 
		
		// get the data from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			task_id = extras.getInt(Tasks.TASK_ID);
			// get the current task name
			Cursor c = managedQuery(Tasks.CONTENT_URI, new String[] {
					Tasks.TASK_ID, Tasks.TASK_NAME }, Tasks.TASK_ID + " = "
					+ task_id, null, null);
			c.moveToFirst();
			
			taskText.setText(c.getString(c.getColumnIndex(Tasks.TASK_NAME)));
		}
		updateButton.setOnClickListener(this);
	}

	// update the task and finish
	public void onClick(View v) {
	String task = taskText.getText().toString();
		
		// make sure there is a string
		if( task.equals("") == false )
		{
			// add the task
			ContentValues values = new ContentValues();
			values.put(Tasks.TASK_NAME, task);
			getContentResolver().update(Tasks.CONTENT_URI, values, Tasks.TASK_ID + "=" + task_id, null);
			
			// hide the on screen keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(taskText.getWindowToken(), 0);
			
			// finish the activity
			finish();
		}
	}

}
