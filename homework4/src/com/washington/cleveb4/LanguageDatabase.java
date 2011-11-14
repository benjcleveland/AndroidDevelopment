package com.washington.cleveb4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LanguageDatabase extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 18;
	private static final String DATABASE_NAME = "LanguageDatabase.db";
	//private static final String DATABASE_CREATE = "CREATE TABLE phrase (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, category_number INTEGER, spanish VARCHAR, english VARCHAR, FOREIGN KEY ('category_number')  REFERENCES category('number'))";
	//private static final String TABLE3_CREATE = "CREATE TABLE category (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, language VARCHAR, number INTEGER)";
	
	private static final String DB_PATH = "/data/data/com.washington.cleveb4/databases/";
	
	private static final String CATEGORY_TABLE = "category";
	private static final String PHRASE_TABLE = "phrase";
	
	private final Context mContext;
	private SQLiteDatabase mDatabase;
	
	public LanguageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
		// create the database	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// "IF NOT EXISTS"

		//db.execSQL(TABLE3_CREATE);
		//db.execSQL(DATABASE_CREATE);	
		
		// fill in some initial data
		// this is only for testing
		/*
		db.execSQL("insert into phrase (category_number, spanish, english) values (1, '¿Cómo estás?', 'How are you?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (1, '¿Cuál es tu nombre?', 'What is your name?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (1, '¿Cuántos años tienes?', 'How old are you?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (1, '¿Qué hace usted para vivir?', 'What do you do for a living?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (1, '¿De dónde eres?', 'Where are you from?')" );
		
		
		db.execSQL("insert into phrase (category_number, spanish, english) values (2, '¿Qué pasa doctor?', \"What's up doc?\")" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (2, '¿Qué está mal con el pie?', 'What is wrong with my foot?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (2, 'Necesito una ambulancia!', 'I need an ambulance!')" );
		
		db.execSQL("insert into phrase (category_number, spanish, english) values (3, '¿Cuál es el puntaje en el juego?', 'What is the score to the game?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (3, '¿Quieres jugar al fútbol?', 'Do you want to play soccer?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (3, '¿Cuál es tu deporte favorito?', 'What is your favorite sport?')" );
		db.execSQL("insert into phrase (category_number, spanish, english) values (3, '¿Eres bueno en el béisbol?', 'Are you good at baseball?')" );
		
		
		db.execSQL("insert into category (language, name, number) values ('English', 'Greeting Questions', 1)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Preguntas Saludo', 1)");
		db.execSQL("insert into category (language, name, number) values ('English', 'Medical Questions', 2)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Algunas de las preguntas', 2)");
		db.execSQL("insert into category (language, name, number) values ('English', 'Sports Questions', 3)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Preguntas Deportes', 3)");
		*/
		//this.getReadableDatabase();
		
		// copy the database
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LanguageDatabase.class.getName(), "Upgrading database from " + oldVersion + " to " + newVersion );
		// clear out the database
		db.execSQL("DROP TABLE IF EXISTS phrase" );
		db.execSQL("DROP TABLE IF EXISTS category");
		
		// create a new one
		onCreate(db);
	}
	
	private void copyDatabase() throws IOException
	{
		InputStream input = mContext.getAssets().open(DATABASE_NAME);
	
		// output path
		String output_path = DB_PATH + DATABASE_NAME;
		
		OutputStream output = new FileOutputStream( output_path );
		
		byte[] buffer = new byte[1024];
		int length;
		// read the input file and create the database
		while( (length = input.read(buffer)) > 0)
		{
			output.write(buffer, 0, length);
		}
		Log.v(LanguageDatabase.class.getName(), "copied the database!!");
		
		// cleanup
		output.flush();
		output.close();
		input.close();
	}
	

	// open the database
	public void openDatabase()
	{
		try
		{
			mDatabase = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch(SQLiteException e)
		{
			// the database doesn't exist - so create it then open
			try {
				// this will create the path to the database
				getReadableDatabase();
				copyDatabase();
				mDatabase = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
			} catch (IOException e1) {
				Log.e(LanguageDatabase.class.getName(), "Error trying to copy the database", e1);
			}
		}
	}
	
	// close the database
	public void closeDatabase()
	{
		if( mDatabase != null)
		{
			mDatabase.close();
		}
	}
	
	// return the category cursor
	public Cursor getCategoryCursor(String lang)
	{
		String selection = "language = '" + lang + "'";
		return mDatabase.query(CATEGORY_TABLE, new String[] {"_ID", "name", "number" },	selection, null, null, null, null);
	}
	
	// return the language cursor
	public Cursor getLanguageCursor()
	{
		return mDatabase.query(CATEGORY_TABLE, new String[] {"_ID", "language" }, null, null, "language", null, null);
	}
	
	public Cursor getPhraseCursor(int number)
	{
		String selection = "category_number = " + number;
		
		return mDatabase.query(PHRASE_TABLE, new String[] {"english", "spanish", "_ID"}, selection, null, null, null, null);
	}
}
