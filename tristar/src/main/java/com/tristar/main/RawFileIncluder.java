package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tristar.geo.utils.GPSTracker;
import com.tristar.utils.SessionData;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.tristar.main.SurfaceViewCamera.CAPTUREDIMAGE;

@SuppressWarnings("ALL")
@SuppressLint("ResourceAsColor")
public class RawFileIncluder extends Activity implements SurfaceHolder.Callback, OnClickListener {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int REQUEST_CAMERA = 1;
	public static final String IMAGE_DIRECTORY_NAME = "WinServe_Data/Camera";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100,
			LIBRARY_IMAGE_REQUEST_CODE = 101;
	private static final int FILECHOOSER_RESULTCODE = 2888;
	private static final int PICK_FROM_FILE = 2;
	private static final int CAMERA_REQUEST = 1888;
	private static int RESULT_CODE = -1;
	private static int SPLASH_TIME_OUT = 100;
	public double latitude, longitude;
	RelativeLayout rll;
	Button take_picture, search, btn_cancel;
	byte[] data;
	String Workorder, Date, Lat, Long;
	int processOrderID;
	GPSTracker gps;
	Bundle extra;
	ExifInterface exif;
	File mediaFile, medialatlongfile;
	Bitmap bitmap;
	private Uri fileUri;

	@SuppressLint("Recycle")
	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = {"_data"};
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachment_options);

//		 if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//				String[] perms = {"android.permission.CAMERA"};
//
//				int permsRequestCode = 200;
//
//				requestPermissions(perms, permsRequestCode);
//	        }

//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (getIntent().getExtras() != null) {
			processOrderID = getIntent().getExtras().getInt("processOrderID");
		}
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				IMAGE_DIRECTORY_NAME);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {

				return;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		medialatlongfile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG__" + timeStamp + ".jpg");

		btn_cancel = (Button) findViewById(R.id.button3_cancel);
		take_picture = (Button) findViewById(R.id.button2_take_picture);
		search = (Button) findViewById(R.id.button1_search);
		btn_cancel.setOnClickListener(this);
		take_picture.setOnClickListener(this);
		search.setOnClickListener(this);


		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			SessionData.getInstance().setImageData(null);
			finish();
		}

		if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.TAKE_PICTURE) {
			onClick(take_picture);
		}

		int getImage = SessionData.getInstance().getImage();

		if (SessionData.getInstance().getImage() == 1 || CAPTUREDIMAGE) {
			String date = new SimpleDateFormat("MM/dd/yy hh:mm a",
					Locale.getDefault()).format(new Date());
			Workorder = "Order # : "
					+ SessionData.getInstance().getImageworkorder();
			Date = "Date : " + date;
			data = SessionData.getInstance().getCapturedimage();
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");

			// ByteArrayOutputStream bos = new ByteArrayOutputStream();

			// BitmapFactory.Options opts = new BitmapFactory.Options();
			// opts.inJustDecodeBounds = false;
			// opts.inPreferredConfig = Config.RGB_565;
			// opts.inDither = true;
			// Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data
			// .length);
			// bitmap.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/,
			// bos);
			// byte[] bitmapdata = bos.toByteArray();

			// write the bytes in file
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(mediaFile);
				fos.write(data);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			addImageToSessionData();
			// Intent intent = new Intent(RawFileIncluder.this,
			// BaseFileIncluder.class);
			// startActivity(intent);

			new Handler().postDelayed(new Runnable() {

				/*
				 * Showing splash screen with a timer. This will be useful when
				 * you want to show case your app logo / company
				 */

				@Override
				public void run() {
					// This method will be executed once the timer is over
					RawFileIncluder.this.onBackPressed();

				}
			}, SPLASH_TIME_OUT);

		}

	}

	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CAMERA: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					Toast.makeText(getApplicationContext(), "Permission granted",
							Toast.LENGTH_SHORT).show();
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Toast.makeText(getApplicationContext(), "Permission denied",
							Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		RESULT_CODE = requestCode;

		if (RESULT_CODE == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				addImageToSessionData();
				finish();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (RESULT_CODE == PICK_FROM_FILE) {
			if (resultCode == RESULT_OK) {
				try {

					fileUri = Uri.parse("file://"
							+ Environment
							.getExternalStoragePublicDirectory(getPath(
									this, data.getData())));
					fileUri = Uri.fromFile(new File(getPath(this,
							data.getData())));
					if (fileUri != null) {
						String path = fileUri.toString();
						if (path.toLowerCase().startsWith("file://")) {
							path = (new File(URI.create(path)))
									.getAbsolutePath();
						}

					}
					addImageToSessionDataImage();

					finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(RawFileIncluder.this,
							"Exception in choosing file", Toast.LENGTH_LONG)
							.show();
				}

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"User cancelled image selection", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to Choose image", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private void captureImage() {

		Intent inten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		inten.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		inten.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				Orientation.LEFT_RIGHT);
		startActivityForResult(inten, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				IMAGE_DIRECTORY_NAME);

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {

				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());

		if (type == MEDIA_TYPE_IMAGE) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	private boolean isDeviceSupportCamera() {
		return (getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA));
	}

	private void addImageToSessionData() {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			//
			// bitmap = BitmapFactory.decodeByteArray(data , 0, data
			// .length,options);
			bitmap = BitmapFactory.decodeFile(mediaFile.getPath(), options);

			Log.d("File name", "" + mediaFile.getPath());

			exif = new ExifInterface(mediaFile.getPath());
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
					String.valueOf(SessionData.getInstance().getImagelat()));
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
					String.valueOf(SessionData.getInstance().getImagelong()));

			// exif.saveAttributes();
			bitmap = Bitmap.createScaledBitmap(bitmap, 400, 300, true);
			int rotationAngle = 90;

			Matrix matrix = new Matrix();
			matrix.setRotate(rotationAngle, (float) 300, (float) 400 / 2);

			Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 400, 300,
					matrix, true);

			getGps();
			bitmap = rotatedBitmap;
			Lat = "Latitude : " + SessionData.getInstance().getImagelat();
			Long = "Longitude : " + SessionData.getInstance().getImagelong();

			String lati = String.valueOf(Lat);
			String longi = String.valueOf(Long);

			Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			Canvas newCanvas = new Canvas(mutableBitmap);

			newCanvas.drawBitmap(mutableBitmap, 0.0f, 0.0f, null);
			// newCanvas.drawColor(Color.WHITE);
			Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintText.setStyle(Paint.Style.STROKE);
			paintText.setColor(Color.WHITE);
			Path mPath = new Path();

			RectF mRectF = new RectF(0, 20, bitmap.getWidth(), 0);
			mPath.addRect(mRectF, Path.Direction.CCW);
			paintText.setStrokeWidth(20);
			paintText.setStyle(Paint.Style.STROKE);
			newCanvas.drawPath(mPath, paintText);

			paintText.setColor(Color.BLACK);

			paintText.setTextSize(11);
			paintText.setStyle(Style.FILL);
			paintText.setShadowLayer(10f, 10f, 10f, Color.WHITE);

			Rect rectText = new Rect();

			paintText.getTextBounds(Workorder, 0, Workorder.length(), rectText);

			newCanvas.drawText(Workorder, 0, rectText.height(), paintText);
			newCanvas.drawText(Date, 0, 25, paintText);

			newCanvas.drawText(Lat, bitmap.getWidth() - 113, rectText.height(),
					paintText);

			newCanvas.drawText(Long, bitmap.getWidth() - 125, 25, paintText);

			// File mediaStorageDir = new File(
			// Environment
			// .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
			// IMAGE_DIRECTORY_NAME);

			// Convert bitmap to byte array

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] bytearrays = stream.toByteArray();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(medialatlongfile);
				fos.write(bytearrays);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// InputStream is = new ByteArrayInputStream(bytearrays);

			TiffOutputSet outputSet = null;

			IImageMetadata metadata = Sanselan.getMetadata(new File(mediaFile
					.getPath()));
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			if (null != jpegMetadata) {
				TiffImageMetadata exif = jpegMetadata.getExif();
				if (null != exif) {
					outputSet = exif.getOutputSet();
				}
			}
			if (null != outputSet) {
				stream.flush();
				stream.close();
				stream = new ByteArrayOutputStream();
				ExifRewriter ER = new ExifRewriter();
				ER.updateExifMetadataLossless(bytearrays, stream, outputSet);
				bytearrays = stream.toByteArray();

			}

			// mediaFile = new File(mediaFile.getPath());
			// exif = new ExifInterface(mediaFile.getPath());
			//
			// exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
			// String.valueOf(latitude));
			// exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
			// String.valueOf(longitude));
			// exif
			// .getAttribute(ExifInterface.TAG_ORIENTATION);
			// exif
			// .getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			// exif
			// .getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			//
			// Log.d("lat:long", "" + latitude+":"+longitude);
			// exif.saveAttributes();
			SessionData.getInstance().setImageData(bytearrays);
			CAPTUREDIMAGE = false;

		} catch (NullPointerException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, "General ex : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

	@Override
	public void onClick(View v) {

		if (v == search) {

			SessionData.getInstance().setImageData(null);
			fileUri = null;
			showFileChooser();

		}
		if (v == take_picture) {

			getGps();
			if (gps.canGetLocation()) {

				SessionData.getInstance().setImagelat(latitude);
				SessionData.getInstance().setImagelong(longitude);

				Intent intent = new Intent(RawFileIncluder.this,
						SurfaceViewCamera.class);
				intent.putExtra("processOrderID", processOrderID);
				startActivity(intent);
				finish();
			}
		}

		if (v == btn_cancel) {
			SessionData.getInstance().setImageData(null);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	private void addImageToSessionDataImage() {
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap bitmap = BitmapFactory
					.decodeFile(fileUri.getPath(), options);

			exif = new ExifInterface(fileUri.getPath());
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
					String.valueOf(latitude));
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
					String.valueOf(longitude));
			String orientString = exif
					.getAttribute(ExifInterface.TAG_ORIENTATION);

			int orientation = orientString != null ? Integer
					.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
			int rotationAngle = 0;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
				rotationAngle = 90;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
				rotationAngle = 180;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
				rotationAngle = 270;

			Matrix matrix = new Matrix();
			matrix.setRotate(rotationAngle, (float) bitmap.getWidth(),
					(float) bitmap.getHeight());
			Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap = rotatedBitmap;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

			byte[] bytearrays = stream.toByteArray();
			stream.flush();
			stream.close();
			SessionData.getInstance().setImageData(bytearrays);

		} catch (NullPointerException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, "General ex : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void showFileChooser() {

		// Intent intent = new Intent();

		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");

		try {
			startActivityForResult(intent, PICK_FROM_FILE);
			// startActivityForResult(
			// Intent.createChooser(intent, "Complete action using"),
			// PICK_FROM_FILE);

		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void getGps() {
		gps = new GPSTracker(RawFileIncluder.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	public class GPS {
		private StringBuilder sb = new StringBuilder(20);

		/**
		 * returns ref for latitude which is S or N.
		 *
		 * @param latitude
		 * @return S or N
		 */
		public String latitudeRef(double latitude) {
			return latitude < 0.0d ? "S" : "N";
		}

		/**
		 * returns ref for latitude which is S or N.
		 *
		 * @param latitude
		 * @return S or N
		 */
		public String longitudeRef(double longitude) {
			return longitude < 0.0d ? "W" : "E";
		}

		/**
		 * convert latitude into DMS (degree minute second) format. For instance<br/>
		 * -79.948862 becomes<br/>
		 * 79/1,56/1,55903/1000<br/>
		 * It works for latitude and longitude<br/>
		 *
		 * @param latitude could be longitude.
		 * @return
		 */
		synchronized public final String convert(double latitude) {
			latitude = Math.abs(latitude);
			int degree = (int) latitude;
			latitude *= 60;
			latitude -= (degree * 60.0d);
			int minute = (int) latitude;
			latitude *= 60;
			latitude -= (minute * 60.0d);
			int second = (int) (latitude * 1000.0d);

			sb.setLength(0);
			sb.append(degree);
			sb.append("/1,");
			sb.append(minute);
			sb.append("/1,");
			sb.append(second);
			sb.append("/1000,");
			return sb.toString();
		}
	}
}