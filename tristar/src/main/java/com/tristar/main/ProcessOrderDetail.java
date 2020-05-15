package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.CodeAndTitle;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.Tristar;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.voicerecorder.VoiceRecorder;
import com.tristar.webutils.WebServiceConsumer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;


@SuppressWarnings("ALL")
@SuppressLint("CutPasteId")
public class ProcessOrderDetail extends Activity implements OnClickListener,
		OnLongClickListener {
	private static final int PICK_FROM_FILE = 2;
	public static boolean Validate_Clear_ImageSession = false;
	private static int RESULT_CODE = -1;
	public double latitude, longitude;
	Button btnrecord, btnfinal, btnprev, btn_serveaudio,
			btnpdf, btnAddaddress, btntakepic, search;
	String phone1, phone2, phone3, phone, finalphone;
	String processAdd, processDueDate, processPriority, processServee;
	TextView back, address, processAddress, processDue, processPrior,
			processSer, processSelect, finalstatus_lblhome, phone_no, btn_specialInstruction;
	DataBaseHelper database;
	ProcessAddressForServer processOrderToDisplayInDetailView;
	ArrayList<Tristar> detail;
	ArrayList<CodeAndTitle> code;
	ArrayList<DiligenceForProcess> diligence;
	PreviousDiligence dell = null;
	ArrayList<DiligenceForProcess> arrayListProcessDiligence;
	int processOrderID;
	Context context;
	ImageView image, audioIcon;
	String selectedProcess, mapview;
	String specialMessage;
	boolean isAttach = false;
	TextView Txt_ReceivedDate, Txt_Receivendtime;
	Bundle extra;
	CheckBox chk_entity;
	EditText edt_agentforservice, edt_agenttitle;
	LinearLayout EntityLinear, Special_Instruction_Parent, FinalStatus_lblHome_Parent;
	ExifInterface exif;
	// Get Image from library
	private Uri fileUri;
	private GPSTracker gps;
	private TextView textView_email;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_order_detail_view);
		context = this;
		dell = new PreviousDiligence();
		diligence = new ArrayList<DiligenceForProcess>();
		database = DataBaseHelper.getInstance();
		processOrderToDisplayInDetailView = new ProcessAddressForServer();
		SessionData.getInstance().clearAttachments();
		SessionData.getInstance().setDiligenceAttachment(0);
		SessionData.getInstance().setFinalstatusAttachment(0);

		getGps();


		extra = getIntent().getExtras();

		if (extra != null) {
			processOrderID = extra.getInt("processOrderID");
			mapview = extra.getString("mapview");
		}
		EntityLinear = (LinearLayout) findViewById(R.id.linearentity);
		Special_Instruction_Parent = (LinearLayout) findViewById(R.id.special_instruction_parent);
		FinalStatus_lblHome_Parent = (LinearLayout) findViewById(R.id.finalstatus_lblhome_parent);
		chk_entity = (CheckBox) findViewById(R.id.check_entity);
		edt_agentforservice = (EditText) findViewById(R.id.edt_agentforservice);
		edt_agenttitle = (EditText) findViewById(R.id.edt_agenttitle);
		edt_agentforservice.setEnabled(false);
		edt_agenttitle.setEnabled(false);

		btn_serveaudio = (Button) findViewById(R.id.btn_serveaudio);
		finalstatus_lblhome = (TextView) findViewById(R.id.finalstatus_lblhome);
		processAddress = (TextView) findViewById(R.id.txt_address);
		processDue = (TextView) findViewById(R.id.txt_duedate);
		processPrior = (TextView) findViewById(R.id.txt_priority);
		processSer = (TextView) findViewById(R.id.txt_servee);
		phone_no = (TextView) findViewById(R.id.textView_phone);

		Txt_ReceivedDate = (TextView) findViewById(R.id.txt_recieveddate);
		Txt_Receivendtime = (TextView) findViewById(R.id.txt_recievedtime);


		processSelect = (TextView) findViewById(R.id.lbl_category);
		address = (TextView) findViewById(R.id.txt_address);
		btnrecord = (Button) findViewById(R.id.btnRecord);
		btnfinal = (Button) findViewById(R.id.btnfinal);
		btnpdf = (Button) findViewById(R.id.btnpdfview);
		btnAddaddress = (Button) findViewById(R.id.btnAddaddress);
		btntakepic = (Button) findViewById(R.id.btntake_pic);
		search = (Button) findViewById(R.id.button1_search);

		audioIcon = (ImageView) findViewById(R.id.audio_icon);
		btnprev = (Button) findViewById(R.id.btnprev);
		btn_specialInstruction = (TextView) findViewById(R.id.btnspecial);
		back = (TextView) findViewById(R.id.textviewbackpro);
		textView_email = (TextView) findViewById(R.id.textView_email);
		image = (ImageView) findViewById(R.id.imageButtonbackpro);

		btnrecord.setOnClickListener(this);
		back.setOnClickListener(this);
		image.setOnClickListener(this);
		btnfinal.setOnClickListener(this);
		btnprev.setOnClickListener(this);
		address.setOnLongClickListener(this);
		address.setOnClickListener(this);
		btnAddaddress.setOnClickListener(this);
		btntakepic.setOnClickListener(this);
		btn_serveaudio.setOnClickListener(this);
		textView_email.setOnClickListener(this);
		phone_no.setOnClickListener(this);
		SessionData.getInstance().setCity("");
		SessionData.getInstance().setState("");
		SessionData.getInstance().setZip("");
		SessionData.getInstance().setStreet("");
		SessionData.getInstance().setTheAddressType(0);
		btnpdf.setOnClickListener(this);
		search.setOnClickListener(this);

		initialilizeViews();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (SessionData.getInstance().getAudioData() != null) {
			audioIcon.setVisibility(View.VISIBLE);
		} else {
			audioIcon.setVisibility(View.GONE);
		}
	}

	@SuppressLint("SetTextI18n")
	public void initialilizeViews() {


		if (SessionData.getInstance().isAudio_on()) {
			btn_serveaudio.setVisibility(View.VISIBLE);
		} else {
			btn_serveaudio.setVisibility(View.GONE);
		}
		try {
			processOrderToDisplayInDetailView = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
			SessionData.getInstance().setProcessorderid(processOrderID);
			Log.d("processOrderID", "" + processOrderID);

			if (processOrderToDisplayInDetailView.getWorkorder().length() == 0) {
				processSelect.setText("N/A");
			} else {
				processSelect.setText(processOrderToDisplayInDetailView
						.getWorkorder());
				SessionData.getInstance().setImageworkorder(processOrderToDisplayInDetailView
						.getWorkorder());
			}

			if(processOrderToDisplayInDetailView.getAddressFormattedNewLine1().length()!=0){
				processAddress.setText(processOrderToDisplayInDetailView.getAddressFormattedNewLine1()+"\n"+processOrderToDisplayInDetailView.getAddressFormattedNewLine2());

			}

			if (processOrderToDisplayInDetailView.getAddressFormattedForDisplay().length() == 0) {
			//	processAddress.setText("N/A");
				finalstatus_lblhome.setText("");
			} else {
				String[] array = processOrderToDisplayInDetailView.getAddressFormattedForDisplay().split(":");
				Log.d("processOrderToDisplayInDetailView"," = "+array);
				if (array.length > 2) {
					if(processOrderToDisplayInDetailView.getAddressFormattedNewLine1().length()==0) {
							processAddress.setText(array[1].trim() + ": " + array[2].trim());
					}
				} else {
					if(processOrderToDisplayInDetailView.getAddressFormattedNewLine1().length()==0) {
							processAddress.setText(array[1].trim());
					}
				}

				boolean addresstype = Pattern.compile(Pattern.quote("Home"), Pattern.CASE_INSENSITIVE)
						.matcher(array[0].trim()).find();
				if (addresstype) {
					FinalStatus_lblHome_Parent.setBackground(ContextCompat.getDrawable(ProcessOrderDetail.this, R.drawable.greenbackground));
					finalstatus_lblhome.setText(array[0].trim());
				} else {
					boolean addresstype1 = Pattern.compile(Pattern.quote("Business"), Pattern.CASE_INSENSITIVE)
							.matcher(array[0].trim()).find();
					if (addresstype1) {
						FinalStatus_lblHome_Parent.setBackground(ContextCompat.getDrawable(ProcessOrderDetail.this, R.drawable.bluebackground));
						finalstatus_lblhome.setText(array[0].trim());
					} else {
						finalstatus_lblhome.setText(array[0].trim() + ": ");
					}
				}
				SessionData.getInstance().setPreviousdiligenceAddress(
						array[1].trim());

			}

			if (processOrderToDisplayInDetailView.getDueDate().length() == 0) {
				processDue.setText("N/A");
			} else {
				String date = processOrderToDisplayInDetailView.getDueDate()
						.substring(0, 10);

				String duedate = getFormattedDateOnly(date);
				processDue.setText(duedate);
			}
			if (processOrderToDisplayInDetailView.getPriorityTitle().length() == 0) {
				processPrior.setText("N/A");
			} else {
				processPrior.setText(processOrderToDisplayInDetailView
						.getPriorityTitle());
			}
			if (processOrderToDisplayInDetailView.getServee().length() == 0) {
				processSer.setText("N/A");
			} else {
				processSer.setText(processOrderToDisplayInDetailView
						.getServee());
			}
			if (processOrderToDisplayInDetailView.getTimeReceived() == null) {
				Txt_Receivendtime.setText("N/A");
			} else {
				Txt_Receivendtime.setText(processOrderToDisplayInDetailView
						.getTimeReceived());
			}
			if (processOrderToDisplayInDetailView.getDateReceived() == null) {
				Txt_ReceivedDate.setText("N/A");
			} else {
				Txt_ReceivedDate.setText(processOrderToDisplayInDetailView
						.getDateReceived());
			}
			if (processOrderToDisplayInDetailView.getPhone().length() == 0) {
				phone_no.setText("");
			} else {

				phone = processOrderToDisplayInDetailView
						.getPhone();
				phone1 = phone.substring(0, 3);
				phone2 = phone.substring(3, 6);
				phone3 = phone.substring(6, 10);
				Log.d("split phone", "" + phone1);

				phone_no.setText("(" + phone1 + ") " + phone2 + "-" + phone3);
			}

			if (processOrderToDisplayInDetailView.getAgentForServiceRelationShipToServee().length() != 0) {
				edt_agentforservice.setText(processOrderToDisplayInDetailView
						.getAgentForServiceRelationShipToServee());
				Log.d("service", "" + processOrderToDisplayInDetailView
						.getAgentForServiceRelationShipToServee());

			}
			if (processOrderToDisplayInDetailView.getAuthorizedAgent()
					.length() != 0) {
				edt_agenttitle.setText(processOrderToDisplayInDetailView
						.getAuthorizedAgent());
				Log.d("title", "" + processOrderToDisplayInDetailView
						.getAuthorizedAgent());
			}

			new Mytask().execute();


			if (processOrderToDisplayInDetailView.isEntity() == false) {
				EntityLinear.setVisibility(View.GONE);
				chk_entity.setChecked(false);
				SessionData.getInstance().setEntityChecked(1);
				SessionData.getInstance().setAgentservice("");
				SessionData.getInstance().setAgenttitle("");
			} else {
				chk_entity.setChecked(true);
				SessionData.getInstance().setEntityChecked(0);
				String service = edt_agentforservice.getText().toString();
				String title = edt_agenttitle.getText().toString();
				SessionData.getInstance().setAgentservice(service);
				Log.d("agentservice", "" + service);
				Log.d("agenttitle", "" + title);
				SessionData.getInstance().setAgenttitle(title);
			}

			chk_entity.setClickable(false);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btnrecord) {
			finish();
			RecordDiligence._lineItem = 0;
			Validate_Clear_ImageSession = true;
			Intent submit = new Intent(ProcessOrderDetail.this,
					RecordDiligence.class);
			submit.putExtra("processOrderID", processOrderID);
			startActivity(submit);
		} else if (v == textView_email) {




		}else if (v == back) {
			finish();
			Intent submit = new Intent(ProcessOrderDetail.this,
					ProcessOrder.class);
			if (mapview != null && mapview.length() != 0) {
				submit.putExtra("mapview", mapview);
			}
			startActivity(submit);

		} else if (v == image) {
			finish();
			Intent submit = new Intent(ProcessOrderDetail.this,
					ProcessOrder.class);
			if (mapview != null && mapview.length() != 0) {
				submit.putExtra("mapview", mapview);
			}
			startActivity(submit);
		} else if (v == btnfinal) {
			finish();
			Validate_Clear_ImageSession = true;
			Intent submit = new Intent(ProcessOrderDetail.this,
					FinalStatus.class);
			submit.putExtra("processOrderID", processOrderID);
			startActivity(submit);
		} else if (v == btntakepic) {

			if (!isDeviceSupportCamera()) {
				Toast.makeText(getApplicationContext(),
						"Sorry! Your device doesn't support camera",
						Toast.LENGTH_LONG).show();
				SessionData.getInstance().setImageData(null);

			} else {

				getGps();
				if (gps.canGetLocation()) {
					isAttach = true;
					BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.TAKE_PICTURE;
					BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.TAKE_PICTURE;
					SessionData.getInstance().setImagelat(latitude);
					SessionData.getInstance().setImagelong(longitude);

					Intent Go_To_SurfaceViewCamera = new Intent(ProcessOrderDetail.this,
							BaseFileIncluder.class);
					Go_To_SurfaceViewCamera.putExtra("processOrderID", processOrderID);
					Go_To_SurfaceViewCamera.putExtra("activityId", 5);
					Go_To_SurfaceViewCamera.putExtra("Flag", "ProcessOrderDetails");

					startActivity(Go_To_SurfaceViewCamera);
//				finish();
					// captureImage();
				}

//			finish();
//			Intent attach_white = new Intent(ProcessOrderDetail.this, BaseFileIncluder.class);
//			startActivity(attach_white);
			}
		} else if (v == search) {
			BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.CHOOSE_IMAGE;
			SessionData.getInstance().setImageData(null);
			fileUri = null;
			showFileChooser();

		} else if (v == btnprev) {

			if (checkInternetConenction()) {

				new previousdiligence().execute();

			} else {
				Toast.makeText(ProcessOrderDetail.this, "No Internet Connection", Toast.LENGTH_LONG).show();
			}

		} else if (v == btn_serveaudio) {
			finish();
			Intent intent = new Intent(ProcessOrderDetail.this,
					VoiceRecorder.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
		} else if (v == btnpdf) {

			new MyAstask().execute();

		} else if (v == btnAddaddress) {
			SessionData.getInstance().setAddaddressLineItem(
					processOrderToDisplayInDetailView.getLineItem());
			SessionData.getInstance().setAddaddressWorkorder(
					processOrderToDisplayInDetailView.getWorkorder());
			Intent intent = new Intent(ProcessOrderDetail.this,
					ProcessOrderAddAddress.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
		} else if (v == address) {
			double lat = Double.parseDouble(processOrderToDisplayInDetailView
					.getLatitude());
			double lon = Double.parseDouble(processOrderToDisplayInDetailView
					.getLongitude());

			String uriBegin = "geo:" + lat + "," + lon;
			String uriString = uriBegin + "?q=" + processOrderToDisplayInDetailView.getAddressFormattedForGoogle();
			Uri uri = Uri.parse(uriString);

			Log.d("URI", "" + uriString);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} else if (phone_no == v) {
			if (processOrderToDisplayInDetailView.getPhone().length() != 0) {
				phonedialog();
			}
		} else if (v == chk_entity) {
			if (chk_entity.isChecked()) {
				edt_agentforservice.setEnabled(true);
				edt_agenttitle.setEnabled(true);
				if (processOrderToDisplayInDetailView.getAgentForServiceRelationShipToServee().length() != 0) {
					edt_agentforservice.setText(processOrderToDisplayInDetailView
							.getAgentForServiceRelationShipToServee());
					Log.d("service", "" + processOrderToDisplayInDetailView
							.getAgentForServiceRelationShipToServee());

				}
				if (processOrderToDisplayInDetailView.getAuthorizedAgent()
						.length() != 0) {
					edt_agenttitle.setText(processOrderToDisplayInDetailView
							.getAuthorizedAgent());
					Log.d("title", "" + processOrderToDisplayInDetailView
							.getAuthorizedAgent());
				}
			} else {
				edt_agentforservice.setEnabled(false);
				edt_agenttitle.setEnabled(false);

				edt_agentforservice.setText("");

				edt_agenttitle.setText("");

			}
		}

	}

	private void showFileChooser() {

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
		super.onActivityResult(requestCode, resultCode, data);
		RESULT_CODE = requestCode;

		if (RESULT_CODE == PICK_FROM_FILE) {
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

					Intent Go_To_BaseFileIncluder = new Intent(ProcessOrderDetail.this, BaseFileIncluder.class);
					Go_To_BaseFileIncluder.putExtra("processOrderID", processOrderID);
					startActivity(Go_To_BaseFileIncluder);

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ProcessOrderDetail.this,
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


	private boolean isDeviceSupportCamera() {
		return (getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA));
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

	public void getGps() {
		gps = new GPSTracker(ProcessOrderDetail.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}
	}

	private boolean checkInternetConenction() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@SuppressLint("SetTextI18n")
	private void phonedialog() {

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.phone_dialog);
		TextView phone = (TextView) dialog.findViewById(R.id.phoneno);
		phone.setText("(" + phone1 + ")" + phone2 + "-" + phone3);

		Button cancel = (Button) dialog.findViewById(R.id.btn_yes);
		Button call = (Button) dialog.findViewById(R.id.btn_no);

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
			}
		});
		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + processOrderToDisplayInDetailView
							.getPhone().toString()));
					startActivity(callIntent);
				} catch (Exception exception) {
					//Logger.log(exception);
				}

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public boolean onLongClick(View v) {
		if (v == address) {
			Intent addressOptions = new Intent(ProcessOrderDetail.this,
					AddressOptions.class);
			addressOptions.putExtra("address", processOrderToDisplayInDetailView.getAddressFormattedForGoogle());
			addressOptions.putExtra("Latitude",
					processOrderToDisplayInDetailView.getLatitude());
			addressOptions.putExtra("Longitude",
					processOrderToDisplayInDetailView.getLongitude());
			startActivity(addressOptions);
		}
		return false;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
			default:
				super.onMenuItemSelected(featureId, item);
				break;
		}
		return true;

	}

	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(ProcessOrderDetail.this, ProcessOrder.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@SuppressLint("SimpleDateFormat")
	private String getFormattedDateOnly(String date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObject = null;
		try {
			dateObject = dateFormat.parse(date);
		} catch (Exception e) {
			dateObject = new Date();
		}
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		return dateFormat.format(dateObject);
	}

	private class Mytask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(ProcessOrderDetail.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				specialMessage = WebServiceConsumer.getInstance()
						.GetSpecialInstructions(
								sessionId,
								processOrderToDisplayInDetailView
										.getWorkorder());

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				specialMessage = null;
			} catch (Exception e) {
				e.printStackTrace();
				specialMessage = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			ProgressBar.dismiss();

			if (specialMessage == null) {

				Special_Instruction_Parent.setVisibility(View.GONE);

			} else {
				Special_Instruction_Parent.setVisibility(View.VISIBLE);
				btn_specialInstruction.setText(specialMessage);
			}
		}
	}

	private class MyAstask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(ProcessOrderDetail.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String session = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				code = WebServiceConsumer.getInstance().getAttachedPDFList(
						session,
						processOrderToDisplayInDetailView.getWorkorder());
				SessionData.getInstance().setWorklistid(
						processOrderToDisplayInDetailView.getWorkorder());
			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				code = null;
			} catch (Exception e) {
				e.printStackTrace();
				code = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (code != null && code.size() > 0) {
				Log.d("The detail Array Size", "" + code.size());
				SessionData.getInstance().setDetail(code);
				Intent intent = new Intent(ProcessOrderDetail.this,
						ViewPdf.class);
				//	intent.putExtra("processOrderID", processOrderID);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"This Workorder has no PDF list", Toast.LENGTH_LONG)
						.show();
			}
			ProgressBar.dismiss();
			super.onPostExecute(result);
		}
	}

	private class previousdiligence extends AsyncTask<Void, Void, Void>

	{
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(ProcessOrderDetail.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				String session = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				arrayListProcessDiligence = WebServiceConsumer.getInstance()
						.getDiligenceForProcess(
								session,
								processOrderToDisplayInDetailView
										.getWorkorder(),
								processOrderToDisplayInDetailView
										.getAddressLineItem());

				SessionData.getInstance().setWorklistid(
						processOrderToDisplayInDetailView.getWorkorder());

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				arrayListProcessDiligence = null;
			} catch (Exception e) {
				e.printStackTrace();
				arrayListProcessDiligence = null;
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			if (arrayListProcessDiligence != null
					&& arrayListProcessDiligence.size() > 0) {

				//	SessionData.getInstance().setArrayListProcessDiligence(arrayListProcessDiligence);
				for (DiligenceForProcess diligence : arrayListProcessDiligence) {
					try {
						database.insertReturnDiligencefromServer(diligence);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Intent submit = new Intent(ProcessOrderDetail.this,
						PreviousDiligence.class);
				submit.putExtra("processOrderID", processOrderID);
				startActivity(submit);
			} else {
				Toast.makeText(getApplicationContext(), "No diligence found",
						Toast.LENGTH_LONG).show();
			}
			ProgressBar.dismiss();
			super.onPostExecute(result);

		}
	}

}