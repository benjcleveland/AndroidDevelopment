package edu.washington.cleveb;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Tasks implements BaseColumns {
	private Tasks() {
	}
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + TaskContentProvider.AUTHORITY + "/tasks");
	
	public static final String TASK_ID = "_id";
	public static final String TASK_NAME = "name";
}
