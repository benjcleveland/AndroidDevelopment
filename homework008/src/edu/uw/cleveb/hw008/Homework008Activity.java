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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Homework008Activity extends Activity {
	
	private static TextView current_condition;
	private static TextView current_temp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        current_condition = (TextView)findViewById(R.id.current_condition);
        current_temp = (TextView)findViewById(R.id.current_temp);
        
        Log.e("here", "this is a test");
        // get the weather in json format
        String weather = readWeather( "http://query.yahooapis.com/v1/public/yql?format=json&q=select%20*%20from%20rss%20where%20url=%27http://xml.weather.yahoo.com/forecastrss/98057_f.xml%27");
        Log.e("here", weather);
        
        try
        {
        	JSONObject jsonObject = new JSONObject( weather );
        	
        	JSONObject condition = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("item").getJSONObject("condition");
        	Log.i("here", condition.getString("temp"));
        	
        	current_condition.setText(condition.getString("text"));
        	current_temp.setText(condition.getString("temp"));
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