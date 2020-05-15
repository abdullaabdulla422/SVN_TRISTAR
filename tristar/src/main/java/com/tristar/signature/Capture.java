package com.tristar.signature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.geo.utils.GPSTracker;
import com.tristar.main.CourtService;
import com.tristar.main.R;
import com.tristar.object.AddressForServer;

import com.tristar.utils.SessionData;

@SuppressWarnings("ALL")
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class Capture extends Activity implements OnClickListener {

	public LinearLayout mContent, btnsShow1, btnsShow2;
	public SignaturePanel mSignature;
	TextView textCancel;

	public Button btnClear, btnSaveSignature, mNewSignature;
	public static String tempDir;
	public int count = 1;
	public String current = null;

	private byte[] signatureData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signature);

		new ContextWrapper(getApplicationContext());
		if (SessionData.getInstance().getImageData() != null)
			signatureData = SessionData.getInstance().getImageData().clone();

		textCancel = (TextView) findViewById(R.id.textViewbacktodeliveryPOD);
		btnsShow1 = (LinearLayout) findViewById(R.id.linearLayoutSignButtons1);
		btnsShow2 = (LinearLayout) findViewById(R.id.linearLayoutSignButtons2);

		mContent = (LinearLayout) findViewById(R.id.linearLayoutSign);
		mNewSignature = (Button) findViewById(R.id.newSign);

		btnClear = (Button) findViewById(R.id.clear);
		btnSaveSignature = (Button) findViewById(R.id.getsign);

		textCancel.setOnClickListener(this);

		btnClear.setOnClickListener(this);
		btnSaveSignature.setOnClickListener(this);
		mNewSignature.setOnClickListener(this);

		if (signatureData == null) {
			btnsShow1.setVisibility(View.GONE);
			btnsShow2.setVisibility(View.VISIBLE);
			btnSaveSignature.setEnabled(false);
		} else {
			btnsShow1.setVisibility(View.VISIBLE);
			btnsShow2.setVisibility(View.GONE);
		}
		mSignature = new SignaturePanel(this, null);
		if (signatureData != null) {
			final Paint paint = new Paint();
			final Bitmap mBitmap = BitmapFactory.decodeByteArray(signatureData,
					0, signatureData.length);
			mContent.setBackground(new Drawable() {

				@Override
				public void setColorFilter(ColorFilter cf) {

				}

				@Override
				public void setAlpha(int alpha) {

				}

				@Override
				public int getOpacity() {
					return 0;
				}

				@Override
				public void draw(Canvas canvas) {
					canvas.drawBitmap(mBitmap, 0, 0, paint);

				}
			});
		} else {
			mContent.addView(mSignature, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		((TextView) findViewById(R.id.txt_court_job_title))
				.setText(((AddressForServer) CourtService.selectedAddressServer)
						.getWorkorder());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public class SignaturePanel extends View {
		private static final float STROKE_WIDTH = 2f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		public Bitmap mBitmap;
		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public SignaturePanel(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);

		}

		public void save(View v) {

			mBitmap = Bitmap.createBitmap(mContent.getWidth(),
					mContent.getHeight(), Bitmap.Config.RGB_565);

			Canvas canvas = new Canvas(mBitmap);

			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				v.draw(canvas);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				try {
					SessionData.getInstance().setImageData(
							setExifMetaData(stream));
				} catch (Exception e) {
					SessionData.getInstance()
							.setImageData(stream.toByteArray());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void clear() {
			mContent.setBackgroundResource(R.drawable.backgroundtristarr);
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float eventX = event.getX();
			float eventY = event.getY();
			btnSaveSignature.setEnabled(true);

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) {
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					expandDirtyRect(historicalX, historicalY);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;

			default:
				debug("Ignored touch event: " + event.toString());
				return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string) {
		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == textCancel) {
			finish();
		} else if (v == btnClear) {
			mSignature.clear();
			btnSaveSignature.setEnabled(false);
		} else if (v == btnSaveSignature) {
			mContent.setDrawingCacheEnabled(true);
			mSignature.save(mContent);
			Toast.makeText(Capture.this, "Signature saved successfully",
					Toast.LENGTH_LONG).show();
			finish();
		} else if (v == mNewSignature) {
			mSignature.clear();
			btnSaveSignature.setEnabled(false);
			if (mContent.getChildCount() == 0) {
				mContent.addView(mSignature, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
			}
			btnsShow1.setVisibility(View.GONE);
			btnsShow2.setVisibility(View.VISIBLE);
		}
	}

	private byte[] setExifMetaData(ByteArrayOutputStream stream)
			throws IOException {
		String path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		path = path + "/WinServe_Data/signature";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
		new File(path).mkdirs();
		path += "/sign_" + format.format(date) + ".jpg";

		FileOutputStream fout = new FileOutputStream(path);
		fout.write(stream.toByteArray());
		fout.flush();
		fout.close();

		ExifInterface newexif = new ExifInterface(path);

		newexif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "0");

		GPSTracker gps = new GPSTracker(this);
		double latitude = 0D, longitude = 0D;
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}

		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		newexif.setAttribute(ExifInterface.TAG_DATETIME, format.format(date));
		newexif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude + "");
		newexif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "" + longitude);

		newexif.saveAttributes();

		FileInputStream inStream = new FileInputStream(path);
		byte[] buffer = new byte[inStream.available()];
		inStream.read(buffer);
		inStream.close();
		return buffer;
	}
}