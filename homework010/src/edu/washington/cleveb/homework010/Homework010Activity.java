package edu.washington.cleveb.homework010;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationListener;
import android.widget.TextView;

public class Homework010Activity extends Activity implements SensorEventListener {
	
	private static Sensor mOrientationSensor;
	private static SensorManager mSensorManager;
	
	private static TextView mOrientationText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get the senor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        // get the orienation sensor
        // use mSensorManager.getOrientation() instead?
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); 
        mOrientationText = (TextView) findViewById(R.id.orientationText);
    }

    @Override
	protected void onResume() {
    	// register the sensor listener
    	mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// unregister the sensor listener
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
	
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
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}