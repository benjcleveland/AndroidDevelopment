package edu.washington.cleveb.homework010;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Homework010Activity extends Activity implements
		SensorEventListener {

	private static final String TAG = Homework010Activity.class.getSimpleName();
	private static Sensor mOrientationSensor;
	private static SensorManager mSensorManager;
	private static CompassView mCompassView;

	private static TextView mOrientationText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.e(TAG, "starting loading it works!!");
		// get the sensor manager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// get the orienation sensor
		// use mSensorManager.getOrientation() instead?
		mOrientationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mOrientationText = (TextView) findViewById(R.id.orientationText);
		mCompassView = (CompassView) findViewById(R.id.compassView1);

		// hide debug info
		mOrientationText.setVisibility(TextView.GONE);

		Log.v(TAG, "done loading");
		Log.v(TAG, "test tesst test");
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "on resume");
		// register the sensor listener
		mSensorManager.registerListener(this, mOrientationSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.v(TAG, "on pause");
		// unregister the sensor listener
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	// handle the on sensor changed event
	public void onSensorChanged(SensorEvent event) {
		// display the values for the orientation sensor on the screen
		StringBuilder sb = new StringBuilder();
		sb.append("azimuth: ");
		sb.append(event.values[0]);
		sb.append("\npitch: ");
		sb.append(event.values[1]);
		sb.append("\nroll: ");
		sb.append(event.values[2]);
		// update the text on the screen
		mOrientationText.setText(sb.toString());
		mCompassView.setRot(event.values[0]);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}