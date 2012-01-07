package edu.uw.cleveb.hw7;

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

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewSourceActivity extends Activity {
	private static final String URL = "url";
	private static TextView source;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewsource_layout);
		
		// get the neccessary views
		source = (TextView) findViewById( R.id.sourceview);
		
		// get the url from the intent
		Bundle extras = getIntent().getExtras();
		if( extras != null )
		{
			source.setText(getPageSource(extras.getString(URL)));
		}
	}
	
	// get the source for the given url
	private String getPageSource( String url )
	{
		String page_source = "";
		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response= client.execute(request);
			
			HttpEntity entity = response.getEntity();
			if( entity != null)
			{
				InputStream instream = entity.getContent();
				BufferedReader br = new BufferedReader( new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line = br.readLine()) != null)
				{
					sb.append(line + "\n");
				}
				instream.close();
				page_source = sb.toString();
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return page_source;
	}
}
