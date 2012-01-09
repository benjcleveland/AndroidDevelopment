package edu.uw.cleveb.hw008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Homework008Activity extends Activity {
	
	private static TextView current_condition;
	private static TextView current_temp;
	private static TextView current_temp_celcius;
	private static WebView forecast;
	private static EditText zipcode;
	private static TextView title;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        current_condition = (TextView)findViewById(R.id.current_condition);
        current_temp = (TextView)findViewById(R.id.current_temp);
        current_temp_celcius = (TextView)findViewById(R.id.current_temp_celsius);
        forecast = (WebView) findViewById(R.id.webview);
        zipcode = (EditText) findViewById(R.id.zip_code_text);
        title = (TextView) findViewById(R.id.title);
       
        zipcode.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( keyCode == KeyEvent.KEYCODE_ENTER){
					// update the weather display
					displayWeather( zipcode.getText().toString());
					// hide the softkeyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(zipcode.getWindowToken(), 0);
					return true;
				}
				return false;
			}
        });
        // fill in the weather for the default zip code
        displayWeather("98105");
    }
    
    // updates the screen to display the weather from the given zipcode
    private void displayWeather( String zipcode )
    {
    	// get the weather in json format
    	String query = String.format("http://query.yahooapis.com/v1/public/yql?format=json&q=select%%20*%%20from%%20rss%%20where%%20url=%%27http://xml.weather.yahoo.com/forecastrss/%s_f.xml%%27", zipcode);
        String weather = readWeather( query ); 
        
        //Log.e("here", weather);
        
        try
        {
        	// convert the string to a JSON object
        	JSONObject jsonObject = new JSONObject( weather );
        	
        	JSONObject item = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("item");
        	JSONObject condition = item.getJSONObject("condition");
        	JSONArray forcast_array = item.getJSONArray("forecast");
        	//Log.i("here", condition.getString("temp"));
        	
        	// update the title, includes the location
        	title.setText( item.getString("title"));
        	
        	// update the condition eg. cloudy 
        	current_condition.setText(condition.getString("text"));
        	
        	// update the current temperature
        	current_temp.setText(condition.getString("temp"));
        	
        	// convert from fahrenhite to celsius
        	int celsius = (int) ((5.0/9.0)* ((condition.getInt("temp")) - 32));
        	current_temp_celcius.setText(Integer.toString(celsius));
        	
        	// this will load the webview of the description 
        	forecast.loadData(item.getString("description").split("<br />")[0], "text/html", null);
        	
        	LayoutInflater inflater = getLayoutInflater();
        	TableLayout forecast_table = (TableLayout) findViewById(R.id.forcast_table);
        	
        	// fill in the forcast table
        	for( int i = 0; i < forcast_array.length(); ++i )
        	{
        		// get the json object out of the array
        		JSONObject day_forecast = forcast_array.getJSONObject(i);
        		
        		TableRow forecast_row = (TableRow) forecast_table.getChildAt(i);
        		if( forecast_row == null)
        		{
        			// inflate a new row layout
        			forecast_row = (TableRow) inflater.inflate(R.layout.forecast_row, null);
        				
        			// add the table row to the table
        			forecast_table.addView(forecast_row);
        		}
        			
        		// get all the resources that we need
        		TextView day = (TextView) forecast_row.findViewById(R.id.forecast_day);
        		TextView text = (TextView) forecast_row.findViewById(R.id.forecast_text);
        		TextView high = (TextView) forecast_row.findViewById(R.id.forecast_high);
        		TextView low = (TextView) forecast_row.findViewById(R.id.forecast_low);
        		
        		// fill in the data from the json object
        		day.setText(day_forecast.getString("day"));
        		text.setText(day_forecast.getString("text"));
        		high.setText(day_forecast.getString("high"));
        		low.setText(day_forecast.getString("low"));
        	}
        	
        } catch( Exception e )
        {
        	e.printStackTrace();
        }
    }
    
    // get the source for the given url
 	private String readWeather(String url) {
 		String page_source = "";
 		try {
 			HttpClient client = new DefaultHttpClient();
 			HttpGet request = new HttpGet();
 			request.setURI(new URI(url));
 			HttpResponse response = client.execute(request);

 			HttpEntity entity = response.getEntity();
 			if (entity != null) {
 				InputStream instream = entity.getContent();
 				BufferedReader br = new BufferedReader(new InputStreamReader(
 						instream));
 				StringBuilder sb = new StringBuilder();
 				String line = "";
 				while ((line = br.readLine()) != null) {
 					sb.append(line + "\n");
 				}
 				instream.close();
 				page_source = sb.toString();
 			}
 		} catch (IOException e) {
 			e.printStackTrace();
 		} catch (URISyntaxException e) {
 			e.printStackTrace();
 		}
 		return page_source;
 	}
}