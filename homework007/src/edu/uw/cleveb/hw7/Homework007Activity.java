package edu.uw.cleveb.hw7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Homework007Activity extends Activity {
	
	private static WebView webview;
	private static Button goButton;
	private static EditText url;
	
	private static TextView source;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get all the necessary views
        webview = (WebView) findViewById(R.id.webview);
        goButton = (Button) findViewById(R.id.go_button);
        url = (EditText) findViewById(R.id.url_text);
        source = (TextView) findViewById(R.id.sourceview);
        
        // enable javascript
        webview.getSettings().setJavaScriptEnabled(true);
        
        // load the default url
        webview.loadUrl("http://www.google.com");
        
        // use our webview client
        webview.setWebViewClient(new myWebViewClient());
        
        // change the go button on click handler
        goButton.setOnClickListener(new OnClickListener()
        {
        	// load the url when clicked
        	public void onClick( View view )
        	{
        		openUrl();
        	}
        });
        
        // this will allow the user to hit enter while in the edit text and not have to hit the go button
        url.setOnKeyListener(new OnKeyListener()
        {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( keyCode == KeyEvent.KEYCODE_ENTER)
				{
					openUrl();
					return true;
				}
				return false;
			}
        });
    }
    
    // open the url that has been entered in the url edit text
    private void openUrl()
    {
    	// don't worry if the user has not entered anything into the url edit text 
    	webview.loadUrl(url.getText().toString());

		// hide the onscreen keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(url.getWindowToken(), 0);
    }
    
    // override the back button so we can navigate back
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {
    	if((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
    	{
    		webview.goBack();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    // create/inflate the options menu
    @Override
    public boolean onCreateOptionsMenu( Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
	// handle the menu input
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle the item selection
		switch (item.getItemId()) {
		case R.id.source:
			// get and display the source to the user
			webview.setVisibility(WebView.GONE);
			source.setVisibility(TextView.VISIBLE);
			
			String source_text = "Here is the source!";
			try
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(webview.getUrl()));
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
					source_text = sb.toString();
				}
			}
			catch( IOException e )
			{
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			source.setText(source_text);
			
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

    // override the webview client so we can click on links and not open the default browser
    private class myWebViewClient extends WebViewClient
    {
    	@Override
    	public boolean shouldOverrideUrlLoading( WebView view, String url)
    	{
    		view.loadUrl(url);
			return true;
    	}
    }
}