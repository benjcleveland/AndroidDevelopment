package edu.uw.cleveb.hw7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class Homework007Activity extends Activity {

	private static final String URL = "url";
	private static final String TYPE = "type";

	private static WebView webview;
	private static Button goButton;
	private static EditText url;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Activity activity = this;

		// for kicks let display the progress
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.main);

		// get all the necessary views
		webview = (WebView) findViewById(R.id.webview);
		goButton = (Button) findViewById(R.id.go_button);
		url = (EditText) findViewById(R.id.url_text);

		// enable javascript
		webview.getSettings().setJavaScriptEnabled(true);

		// enable multi touch
		webview.getSettings().setBuiltInZoomControls(true);

		// enable the ability to update the progress bar
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// update the progress bar
				activity.setProgress(progress * 100);
			}
		});

		url.setText(R.string.default_url);
		
		webview.requestFocus();
		
		// load the default url
		webview.loadUrl(getString(R.string.default_url));
		
		// use our webview client
		webview.setWebViewClient(new myWebViewClient());

		// change the go button on click handler
		goButton.setOnClickListener(new OnClickListener() {
			// load the url when clicked
			public void onClick(View view) {
				openUrl();
			}
		});

		// this will allow the user to hit enter while in the edit text and not
		// have to hit the go button
		url.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					openUrl();
					return true;
				}
				return false;
			}
		});
	}

	// open the url that has been entered in the url edit text
	private void openUrl() {
		// don't worry if the user has not entered anything into the url edit
		// text
		webview.loadUrl(url.getText().toString());
		
		// hide the onscreen keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(url.getWindowToken(), 0);
	
		webview.requestFocus();
	}

	// override the back button so we can navigate back
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// create/inflate the options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		case R.id.cookies:
			// create an intent to start the view source activity
			Intent intent = new Intent(getBaseContext(),
					ViewSourceActivity.class);

			// pass the current url to the page source activity
			intent.putExtra(URL, webview.getUrl());
			intent.putExtra(TYPE, item.getItemId());

			// start the activty
			startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	// override the webview client so we can click on links and not open the
	// default browser
	private class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}