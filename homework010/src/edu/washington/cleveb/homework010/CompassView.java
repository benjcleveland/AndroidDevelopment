package edu.washington.cleveb.homework010;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CompassView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = CompassView.class.getSimpleName();
	private static drawingThread mDrawingThread;
	
	private static float mRotation;
	private static ArrayList<Float> mRotationList;
	
	
	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.v(TAG,"compass view constructor");
		mRotation = 0;
		mRotationList = new ArrayList<Float>();
		getHolder().addCallback(this);
		mDrawingThread = new drawingThread(getHolder());
	}

	// set the rotation of the arrow
	public void setRot( float rotation )
	{
		float total = 0;
		if( mRotationList.size() > 20 )
		{
			mRotationList.remove(0);
		}
		mRotationList.add(rotation);
		
		for( float val : mRotationList )
		{
			total += val;
		}
		mRotation = total / mRotationList.size();
		mRotation = rotation;
	}
	
	private class drawingThread extends AsyncTask<String, Void, String> {
		private SurfaceHolder mSurfaceHolder;
		private Path mArrow = new Path();
		private Paint mPaint = new Paint();
		private int mWidth;
		private int mHeight;
		private Matrix mCompassMatrix;
		private Bitmap mCompassBitmap;
		
		public drawingThread(SurfaceHolder surfaceHolder)
		{
			mSurfaceHolder = surfaceHolder;
			// initialize our arrow
			mArrow.moveTo(0, -50);
            mArrow.lineTo(-20, 60);
            mArrow.lineTo(0, 50);
            mArrow.lineTo(20, 60);
            mArrow.close();
            
            mPaint.setColor(Color.RED);
     	
            // load the compass bitmap
            mCompassBitmap = BitmapFactory.decodeStream(getContext().getResources().openRawResource(R.raw.compass));
		       
            Log.v(TAG, Integer.toString(mHeight) + " " + Integer.toString(mWidth));
        }
		
		@Override
		protected String doInBackground(String... params) {
			Canvas canvas = null;
	
			while(true){
				
				update();
				canvas = mSurfaceHolder.lockCanvas();
				synchronized(mSurfaceHolder)
				{
					draw( canvas );
				}
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}

		// draw the view
		private void draw( Canvas canvas) {
			canvas.drawColor(Color.LTGRAY);
			canvas.drawPath(mArrow, mPaint);
			canvas.drawBitmap(mCompassBitmap, mCompassMatrix, null);
			canvas.restore();
		}
		
		// update the compass position
		private void update()
		{
			mCompassMatrix = new Matrix();
			
			// create the compass rotation matrix
			mCompassMatrix.postTranslate(-mCompassBitmap.getWidth()/2, -mCompassBitmap.getHeight()/2 );
			mCompassMatrix.postRotate(mRotation);
			mCompassMatrix.postTranslate(mWidth/2, mHeight/2);
			
		}
		
		// set the width and height of the view
		public void setRect( int height, int width)
		{
			mHeight = height;
			mWidth = width;
			
			Matrix trans = new Matrix();
			trans.setTranslate(mWidth/2, mHeight/2 - mCompassBitmap.getHeight());
			mArrow.transform(trans);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mDrawingThread.setRect(holder.getSurfaceFrame().height(), holder.getSurfaceFrame().width());
	
		mDrawingThread.execute(new String());
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mDrawingThread.cancel(false);
	}
}
