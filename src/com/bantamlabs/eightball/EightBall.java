package com.bantamlabs.eightball;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

public class EightBall extends Activity {
	protected EightBallView eightBallView;
	protected SensorManager sensorMgr;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Real code
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		// Simulator code for Open Intents, comment out for deploy
		/*
		Hardware.mContentResolver = getContentResolver();
		sensorMgr = (SensorManager) new SensorManagerSimulator(sensorMgr);
		Intent intent = new Intent(Intent.ACTION_VIEW, Hardware.Preferences.CONTENT_URI); 
		startActivity(intent);
		SensorManagerSimulator.connectSimulator();
		 */
		
        eightBallView = new EightBallView(this);
        setContentView(eightBallView);
    }
    
    @Override
	protected void onResume() {
		super.onResume();

        sensorMgr.registerListener(eightBallView, SensorManager.SENSOR_ACCELEROMETER | SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
        sensorMgr.unregisterListener(eightBallView);
	}
}