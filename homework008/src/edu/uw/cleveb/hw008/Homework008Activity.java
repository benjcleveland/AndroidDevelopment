package edu.uw.cleveb.hw008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Homework008Activity extends Activity {

	private static TextView current_condition;
	private static TextView current_temp;
	private static TextView current_temp_celcius;
	private static WebView forecast;
	private static EditText zipcode;
	private static TextView title;
	private static LinearLayout display_layout;
	private static LinearLayout progress_layout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		current_condition = (TextView) findViewById(R.id.current_condition);
		current_temp = (TextView) findViewById(R.id.current_temp);
		current_temp_celcius = (TextView) findViewById(R.id.current_temp_celsius);
		forecast = (WebView) findViewById(R.id.webview);
		zipcode = (EditText) findViewById(R.id.zip_code_text);
		title = (TextView) findViewById(R.id.title);
		display_layout = (LinearLayout) findViewById(R.id.linearLayoutDisplay);
		progress_layout = (LinearLayout) findViewById(R.id.linearLayoutProgress);

		zipcode.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// update the weather display
					displayWeather(zipcode.getText().toString());
					// hide the softkeyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(zipcode.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

		// fill in the weather for the default zip code
		displayWeather(zipcode.getText().toString());
	}

	// updates the screen to display the weather from the given zipcode
	private void displayWeather(String zipcode) {
		// hide the display layout
		display_layout.setVisibility(LinearLayout.GONE);
		// show the progress layout
		progress_layout.setVisibility(LinearLayout.VISIBLE);

		// get the weather in json format
		String query = String
				.format("http://query.yahooapis.com/v1/public/yql?format=json&q=select%%20*%%20from%%20rss%%20where%%20url=%%27http://xml.weather.yahoo.com/forecastrss/%s_f.xml%%27",
						zipcode);
		Log.v("test", query);
		LoadWeatherTask task = new LoadWeatherTask();
		task.execute(new String[] { query });
	}

	// convert the given value from fahrenheit to celsius
	private String convertFahrenheitToCelsius(int fahrenheit) {
		return Integer.toString((int) ((5.0 / 9.0) * (fahrenheit - 32)));
	}

	// Ansyc task for getting the web page source
	private class LoadWeatherTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page_source = "";

			// loop through all the given urls - there should only be one
			for (String url : params) {
				// create the client and the request
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);

				Log.v("test", url);
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
		protected void onPostExecute(String result) {

			try {
				// convert the string to a JSON object
				JSONObject jsonObject = new JSONObject(result);

				JSONObject item = jsonObject.getJSONObject("query")
						.getJSONObject("results").getJSONObject("item");
				JSONObject condition = item.getJSONObject("condition");
				JSONArray forcast_array = item.getJSONArray("forecast");
				Log.i("here", condition.getString("temp"));

				// update the title, includes the location
				title.setText(item.getString("title"));

				// update the condition eg. cloudy
				current_condition.setText(condition.getString("text"));

				// update the current temperature
				current_temp.setText(condition.getString("temp"));

				// convert from fahrenhite to celsius
				String celsius = convertFahrenheitToCelsius(condition
						.getInt("temp"));
				current_temp_celcius.setText(celsius);

				// this will load the webview of the description
				forecast.loadData(
						item.getString("description").split("<br />")[0],
						"text/html", null);

				LayoutInflater inflater = getLayoutInflater();
				TableLayout forecast_table = (TableLayout) findViewById(R.id.forcast_table);

				// fill in the forcast table
				for (int i = 0; i < forcast_array.length(); ++i) {
					// get the json object out of the array
					JSONObject day_forecast = forcast_array.getJSONObject(i);

					TableRow forecast_row = (TableRow) forecast_table
							.getChildAt(i);
					if (forecast_row == null) {
						// inflate a new row layout
						forecast_row = (TableRow) inflater.inflate(
								R.layout.forecast_row, null);

						// change the background color
						if (i % 2 == 0)
							forecast_row.setBackgroundColor(Color.GRAY);
						else
							forecast_row.setBackgroundColor(Color.LTGRAY);

						// add the table row to the table
						forecast_table.addView(forecast_row);
					}

					// get all the resources that we need
					TextView day = (TextView) forecast_row
							.findViewById(R.id.forecast_day);
					TextView text = (TextView) forecast_row
							.findViewById(R.id.forecast_text);
					TextView high = (TextView) forecast_row
							.findViewById(R.id.forecast_high);
					TextView high_cel = (TextView) forecast_row
							.findViewById(R.id.forecast_high_cel);
					TextView low = (TextView) forecast_row
							.findViewById(R.id.forecast_low);
					TextView low_cel = (TextView) forecast_row
							.findViewById(R.id.forecast_low_cel);

					// fill in the data from the json object
					day.setText(day_forecast.getString("day"));
					text.setText(day_forecast.getString("text"));
					high.setText(day_forecast.getString("high"));

					// convert to celsius - another way of doing this is
					// creating another http query
					celsius = convertFahrenheitToCelsius(day_forecast
							.getInt("high"));
					high_cel.setText(celsius);

					low.setText(day_forecast.getString("low"));

					// convert to celsius
					celsius = convertFahrenheitToCelsius(day_forecast
							.getInt("low"));
					low_cel.setText(celsius);
				}

			} catch (Exception e) {
				Toast.makeText(getBaseContext(),
						"No information for the given zip code",
						Toast.LENGTH_SHORT).show();
			}

			// hide the progress layout
			progress_layout.setVisibility(LinearLayout.GONE);

			// show the display layout
			display_layout.setVisibility(LinearLayout.VISIBLE);

		}
	}
}