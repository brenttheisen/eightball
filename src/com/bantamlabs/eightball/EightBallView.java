package com.bantamlabs.eightball;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.view.View;

public class EightBallView extends View implements SensorListener {
	protected static final float SHAKE_VIOLENCE = 0.5f;
	protected static final int ANSWER_COUNT = 3;
	
	protected final Paint backgroundPaint = new Paint();
	protected final Paint messagePaint = new Paint();
	
	private Bitmap bitmap;
	private Random random = new Random();
	
	private float previousAcceleroX = 0;
	private float previousAcceleroY = 0;
	private float previousAcceleroZ = 0;
	
	private boolean shaking = false;
	private boolean hasBeenShaked = false;
	
	private String message;

	public EightBallView(Context context) {
		super(context);
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.eight_ball);
		
		backgroundPaint.setARGB(255, 255, 255, 255);
		messagePaint.setARGB(255, 0, 0, 0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

		// Calculate position based on accelerometer
//		int x = getDimensionPosition(acceleroX, bitmap.getWidth(), getWidth());
//		int y = getDimensionPosition(acceleroY, bitmap.getHeight(), getHeight());

		canvas.drawBitmap(bitmap, (getWidth() / 2) - (bitmap.getWidth() / 2), (getHeight() / 2) - (bitmap.getHeight() / 2), null);
		
		if(message != null) {
			canvas.drawText(message, 5, 25, messagePaint);
		}
    }
/*	
	protected int getDimensionPosition(float acceleroValue, int bitmapLength, int viewLength) {
		int positionValue = Math.round(acceleroValue / (SensorManager.GRAVITY_EARTH / 100));
		if(positionValue < 0)
			positionValue = -positionValue;
		else
			positionValue += viewLength / 2;
		
		return positionValue;
	}
*/
	public void onSensorChanged(int i, float[] af) {
		float acceleroX = af[SensorManager.DATA_X];
		float acceleroY = af[SensorManager.DATA_Y];
		float acceleroZ = af[SensorManager.DATA_Z];

		float seed = 0;
		boolean setAnswer = false;
		synchronized(this) {
			if(acceleroX - previousAcceleroX > SHAKE_VIOLENCE || 
					acceleroY - previousAcceleroY > SHAKE_VIOLENCE || 
					acceleroZ - previousAcceleroZ > SHAKE_VIOLENCE) {
				shaking = true;
				hasBeenShaked = true;
			} else if(hasBeenShaked) {
				shaking = false;
				seed = acceleroX + previousAcceleroX + 
						acceleroY + previousAcceleroY + 
						acceleroZ + previousAcceleroZ;
				setAnswer = true;
				hasBeenShaked = false;
			}
		}
		
		if(setAnswer) {
			random.setSeed(Math.round(seed));
			int randomInt = random.nextInt();
			int answerID = (randomInt % ANSWER_COUNT) + 1;
			int resourceID = getResources().getIdentifier("com.bantamlabs.eightball:string/answer_" + answerID, null, null);
			message = getResources().getString(resourceID);
		}

		this.postInvalidate();
	}

	public void onAccuracyChanged(int i, int j) {
		// Don't care
	}
}
