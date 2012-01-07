package edu.uw.cleveb.hw7;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Homework007Activity extends Activity {
	
	static WebView webview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get the web view
        webview = (WebView) findViewById(R.id.webview);
        
        // enable javascript
        webview.getSettings().setJavaScriptEnabled(true);
        
        // load the default url
        webview.loadUrl("http://www.google.com");
        
        // use our webview client
        webview.setWebViewClient(new myWebViewClient());
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