package com.washington.cleveb4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class LanguageDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 10;
	private static final String DATABASE_NAME = "LanguageDatabase";
	private static final String DATABASE_CREATE = "CREATE TABLE phrase (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, category_number INTEGER, spanish VARCHAR, english VARCHAR, FOREIGN KEY ('category_number')  REFERENCES category('number'))";
	private static final String TABLE2_CREATE = "CREATE TABLE types (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, language VARCHAR)";
	private static final String TABLE3_CREATE = "CREATE TABLE category (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, language VARCHAR, number INTEGER, FOREIGN KEY ('language') REFERENCES phrase (" + BaseColumns._ID + "))";
	
	public LanguageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// "IF NOT EXISTS"
	
		db.execSQL(TABLE2_CREATE);
		db.execSQL(TABLE3_CREATE);
		db.execSQL(DATABASE_CREATE);	
		
		// fill in some initial data
		// this is only for testing
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
		
		// update the types table
		db.execSQL("insert into types (language) values ('English')" );
		db.execSQL("insert into types (language) values ('Español')" );
		
		db.execSQL("insert into category (language, name, number) values ('English', 'Greeting Questions', 1)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Preguntas Saludo', 1)");
		db.execSQL("insert into category (language, name, number) values ('English', 'Medical Questions', 2)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Algunas de las preguntas', 2)");
		db.execSQL("insert into category (language, name, number) values ('English', 'Sports Questions', 3)");
		db.execSQL("insert into category (language, name, number) values ('Español', 'Preguntas Deportes', 3)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LanguageDatabase.class.getName(), "Upgrading database from " + oldVersion + " to " + newVersion );
		// clear out the database
		db.execSQL("DROP TABLE IF EXISTS phrase" );
		db.execSQL("DROP TABLE IF EXISTS types");
		db.execSQL("DROP TABLE IF EXISTS category");
		// create a new one
		onCreate(db);
	}

}
