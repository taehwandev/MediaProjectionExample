package net.thdev.mediaprojectionexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MainActivity extends Activity implements SurfaceHolder.Callback {
	
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	
	MediaProjectionManager projectionManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        
        surfaceView = new SurfaceView(this);
        holder = surfaceView.getHolder();
		holder.addCallback(this);
		setContentView(surfaceView);
		
		projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		
		startActivityForResult(projectionManager.createScreenCaptureIntent(), 100);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i("TAG", "resultCode " + resultCode);
    	if (requestCode == 100) {
    		MediaProjection projection = projectionManager.getMediaProjection(resultCode, data);
    		Log.i("TAG", "projection " + projection);
    		VirtualDisplay virtualDisplay = null;
    		if (projection != null) {
    			virtualDisplay = projection.createVirtualDisplay("test", 1280, 720, DisplayMetrics.DENSITY_MEDIUM, 0, holder.getSurface(), null, null);
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
