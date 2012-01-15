package edu.uw.cleveb.hw7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewSourceActivity extends Activity {
	private static final String URL = "url";
	private static final String TYPE = "type";

	private static TextView source;
	private static ProgressBar progress_bar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewsource_layout);

		// get the neccessary views
		source = (TextView) findViewById(R.id.sourceview);
		progress_bar = (ProgressBar) findViewById( R.id.progress_bar );
		// get the url from the intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			switch (extras.getInt(TYPE)) {
			case R.id.source:
				// set the text to loading
				source.setText( R.string.loading_text );
				
				// create a thread to download the page source
				LoadWebPageSourceTask task = new LoadWebPageSourceTask();
				task.execute(new String[] { extras.getString(URL) });
				break;
			case R.id.cookies:
				// hide the progress bar
				progress_bar.setVisibility(ProgressBar.GONE);
				// display the cookie for this url
				source.setText(CookieManager.getInstance().getCookie(
						extras.getString(URL)));
				break;
			}
		}
	}

	// Ansyc task for getting the web page source
	private class LoadWebPageSourceTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page_source = "";
			
			// loop through all the given urls - there should only be one
			for (String url : params) {
				// create the client and the request 
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);
				
				try {
					// execute the request
					HttpResponse response = client.execute(request);

					// get the response
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						// create a string with the results
						InputStream instream = entity.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(instream), 8192);
						String line = "";
						StringBuilder sb = new StringBuilder();
						while ((line = br.readLine()) != null) {
							// append the line
							sb.append(line + "\n");
						}
						instream.close();
						page_source += sb.toString();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// return the source as a string 
			return page_source;
		}

		// update the UI now that we have the data we want
		@Override
		protected void onPostExecute( String result )
		{
			// display the results to the user (page source)
			source.setText(result);

			// hide the progress bar
			progress_bar.setVisibility(ProgressBar.GONE);
		}
	}
}
