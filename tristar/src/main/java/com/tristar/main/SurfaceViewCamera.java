package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.utils.SessionData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class SurfaceViewCamera extends Activity implements SurfaceHolder.Callback {
	public static boolean CAPTUREDIMAGE = false;
	TextView testView;
	Camera camera;
	TextView btn_capture;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	TextView cancel, surface;
	PictureCallback rawCallback;
	//	ToggleButton tButton;
	SwitchCompat tButton;
	ShutterCallback shutterCallback;
	PictureCallback jpegCallback;
	TextView workorder, Date, lat, longi;
	int processOrderID;
	private boolean hasFlash;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.capture_activity);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);


		if (getIntent().getExtras() != null) {
			processOrderID = getIntent().getExtras().getInt("processOrderID");
		}

		cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.TAKE_PICTURE) {
					BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SURFACE_CAMERA;
					Intent intent = new Intent(SurfaceViewCamera.this, ProcessOrderDetail.class);
					intent.putExtra("processOrderID", processOrderID);
					startActivity(intent);

				} else {
					BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SURFACE_CAMERA;
					Intent intent = new Intent(SurfaceViewCamera.this, RawFileIncluder.class);
					intent.putExtra("processOrderID", processOrderID);
					startActivity(intent);
					finish();
				}
			}
		});

		surfaceHolder = surfaceView.getHolder();
		workorder = (TextView) findViewById(R.id.workorder);
		Date = (TextView) findViewById(R.id.date);
		lat = (TextView) findViewById(R.id.lat);
		btn_capture = (TextView) findViewById(R.id.btn_capture);
		btn_capture.setClickable(true);
//		tButton = (ToggleButton)findViewById(R.id.toggleButton1);
		tButton = (SwitchCompat) findViewById(R.id.toggleButton1);
		longi = (TextView) findViewById(R.id.longi);
		String date = new SimpleDateFormat("MM/dd/yy hh:mm a",
				Locale.getDefault()).format(new Date());
		Date.setText(date);
		workorder.setText(SessionData.getInstance().getImageworkorder());

		lat.setText(String.valueOf(SessionData.getInstance().getImagelat()));

		longi.setText(String.valueOf(SessionData.getInstance().getImagelong()));

		SessionData.getInstance().setCapturedimage(null);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		surfaceHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		jpegCallback = new PictureCallback() {
			@SuppressLint("ShowToast")
			public void onPictureTaken(byte[] data, Camera camera) {
//				FileOutputStream fos = null;
//				try {
//					outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
//					outStream.write(data);
//					outStream.close();


//					Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
//
//					int rotationAngle = 90;
//
//					Matrix matrix = new Matrix();
//					matrix.setRotate(rotationAngle, (float) bitmap.getWidth(),
//							(float) bitmap.getHeight() / 2);
//					Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//							bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//
//					bitmap = rotatedBitmap;
//					Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//
//					 Canvas newCanvas = new Canvas(mutableBitmap);
//
//					   newCanvas.drawBitmap(mutableBitmap, 0, 0, null);
//					  // newCanvas.drawColor(Color.WHITE);
//					   Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
//					   paintText.setStyle(Paint.Style.STROKE);
//					   paintText.setColor(Color.WHITE);
//				         Path mPath = new Path();
//				         RectF mRectF = new RectF(0, 160, bitmap.getWidth(), 0);
//				         mPath.addRect(mRectF, Path.Direction.CCW);
//				         paintText.setStrokeWidth(160);
//				         paintText.setStyle(Paint.Style.STROKE);
//				         newCanvas.drawPath(mPath, paintText);
//
//				         paintText.setColor(Color.BLACK);
//
//						    paintText.setTextSize(100);
//						    paintText.setStyle(Style.FILL);
//						    paintText.setShadowLayer(10f, 10f, 10f, Color.WHITE);
//
//						    String Workorder = "Order # :";
//						    String Date = "Date :";
//						    String Lat = "Latitude :";
//						    String Long = "Longitude :";
//						    Rect rectText = new Rect();
//
//						    paintText.getTextBounds(Workorder, 0, Workorder.length(), rectText);
//
//						    newCanvas.drawText(Workorder,
//						      0, rectText.height(), paintText);
//						    newCanvas.drawText(Date,
//								      0, 200, paintText);
//
//						    newCanvas.drawText(Lat,
//						    		bitmap.getWidth()- 1300, rectText.height(), paintText);
//
//						    newCanvas.drawText(Long,
//						    		bitmap.getWidth() - 1300, 200, paintText);
//
//
//				         ByteArrayOutputStream bos = new ByteArrayOutputStream();
//						    mutableBitmap.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
//						    byte[] bitmapdata = bos.toByteArray();
//
//						    //write the bytes in file
//						     fos = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
//						    fos.write(bitmapdata);
//						    fos.flush();
//						    fos.close();
				Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally {
//				}
				Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();
				//refreshCamera();
				SessionData.getInstance().setImage(1);
				CAPTUREDIMAGE = true;
				SessionData.getInstance().setCapturedimage(data);
				BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SURFACE_CAMERA;

				Intent intent = new Intent(SurfaceViewCamera.this, RawFileIncluder.class);
				startActivity(intent);
				finish();
			}
		};
	}

	public void captureImage(View v) throws IOException {
		btn_capture.setClickable(false);
		//take the picture
		camera.takePicture(null, null, jpegCallback);
	}

	public void refreshCamera() {
		if (surfaceHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here
		// start preview with new settings
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.

		final Parameters param;
		param = camera.getParameters();


		camera.setParameters(param);


//		param.setGpsLatitude(SessionData.getInstance().getImagelat());
//		param.setGpsLongitude(SessionData.getInstance().getImagelong());


		hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (!hasFlash) {
			tButton.setVisibility(View.GONE);
		} else {
			param.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
											 boolean isChecked) {
					List<String> flashModes = param.getSupportedFlashModes();

					if (isChecked) {

						if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
							param.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}
						/* tvStateofToggleButton.setText("ON"); */
					} else {

						param.setFlashMode(Parameters.FLASH_MODE_OFF);
					}
					camera.setParameters(param);
					camera.startPreview();
					/* tvStateofToggleButton.setText("OFF"); */
				}

			});
		}

		refreshCamera();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// open the camera
			camera = Camera.open();
		} catch (RuntimeException e) {
			// check for exceptions
			System.err.println(e);
			return;
		}
		Camera.Parameters param;
		param = camera.getParameters();


		// modify parameter
		//param.setPreviewSize(352, 288);
		camera.setParameters(param);
		camera.setDisplayOrientation(90);
		Camera.Parameters params = camera.getParameters();
//		   surfaceView.getLayoutParams().width=params.getPreviewSize().height;
//		   surfaceView.getLayoutParams().height=params.getPreviewSize().width;


		List<Camera.Size> sizes = params.getSupportedPreviewSizes();
		Camera.Size cs = sizes.get(0);
		params.setPreviewSize(cs.width, cs.height);

		params.setGpsLatitude(SessionData.getInstance().getImagelat());
		params.setGpsLongitude(SessionData.getInstance().getImagelong());


		try {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
			camera.setParameters(params);
//			if(param.isZoomSupported()){
//			    maxZoomLevel = param.getMaxZoom();
//			    Log.d("maxZoomLevel", "" + maxZoomLevel);
//
//				zoomControls.setIsZoomInEnabled(true);
//				zoomControls.setIsZoomOutEnabled(true);
//
//
//			        zoomControls.setOnZoomInClickListener(new OnClickListener(){
//			            public void onClick(View v){
//			                    if(currentZoomLevel < maxZoomLevel){
//			                        currentZoomLevel++;
//			                        camera.startSmoothZoom(currentZoomLevel);
//			                        Log.d("currentZoomLevel", "" + currentZoomLevel);
//			                    }
//			            }
//			        });
//
//			    zoomControls.setOnZoomOutClickListener(new OnClickListener(){
//			            public void onClick(View v){
//			                    if(currentZoomLevel > 0){
//			                        currentZoomLevel--;
//			                        camera.startSmoothZoom(currentZoomLevel);
//			                        Log.d("currentZoomLevel", "" + currentZoomLevel);
//			                    }
//			            }
//			        });
//			   }
//			   else
//			   {
//			     zoomControls.setVisibility(View.GONE);
//			   }
		} catch (Exception e) {
			// check for exceptions
			System.err.println(e);
			return;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// stop preview and release camera
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	@Override
	public void onBackPressed() {

		if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.TAKE_PICTURE) {
			BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SURFACE_CAMERA;
			Intent intent = new Intent(SurfaceViewCamera.this, ProcessOrderDetail.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
		} else {
			BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SURFACE_CAMERA;
			Intent intent = new Intent(SurfaceViewCamera.this, RawFileIncluder.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
			finish();
		}

		super.onBackPressed();
	}
}
