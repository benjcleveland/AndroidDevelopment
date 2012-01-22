package edu.washington.cleveb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TaskContentProvider extends ContentProvider {

	private static final String TASK_TABLE_NAME = "task_table";
	private static final String DATABASE_NAME = "tasks";
	private static final int DATABASE_VERSION = 1; 
	
	public static final String AUTHORITY = "edu.washington.cleveb.TaskContentProvider";
	
	private static DatabaseHelper databaseHelp;
	
	// create the content provider
	@Override
	public boolean onCreate() {
		// create a database helper
		databaseHelp = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION );
		return (databaseHelp == null) ? false : true;
	}

	// query the content provider
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = databaseHelp.getReadableDatabase();
		qb.setTables(TASK_TABLE_NAME);
		
		// Create a cursor from the query
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	// add a task to the database
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = databaseHelp.getWritableDatabase();
		long rowId = db.insert(TASK_TABLE_NAME, "name", values);
		
		if( rowId > 0){	// successfully inserted the task into the database
			Uri taskUri = ContentUris.withAppendedId(Tasks.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(taskUri, null);
			return taskUri;
		}
		return null;
	}

	// delete the task
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = databaseHelp.getWritableDatabase();
		
		int count = db.delete(TASK_TABLE_NAME, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// update the task
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = databaseHelp.getWritableDatabase();
		
		int count = db.update(TASK_TABLE_NAME, values, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// database helper class used to store all the tasks
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// create the task database
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TASK_TABLE_NAME  + " (" + Tasks.TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Tasks.TASK_NAME + " VARCHAR);");
		}

		// upcrate the database as needed
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
			onCreate(db);
		}
	}
}
