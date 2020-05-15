package com.tristar.main;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.tristar.db.DataBaseHelper;
import com.tristar.object.ReturnRouteTaskStatusObject;
import com.tristar.object.ReturnRouteTasksObject;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.SimpleScannerActivity;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.SubmitWebServiceConsumer;
import com.tristar.webutils.WebServiceConsumer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("ALL")
public class RouteTrackerApp extends Activity implements OnClickListener {
	TextView back;
	Button scan, scanLocation, cancel, submit;
	EditText Job_Scanner, location_scanner;
	String str_jobScanner = "", stradd;
	ArrayAdapter<String> CompetitionAdapter;
	ArrayAdapter<String> StatusAdapter;
	Spinner select_Type, Select_job;
	int taskCode;
	String serverCode;
	String sessionid;
	String submitServerCode, submitCustomer;
	int submitTaskcode, submitStatusCode;
	String SubmitJob;

	ArrayList<String> submitWorkOrder;
	private Class<?> mClss;
	private static final int ZBAR_CAMERA_PERMISSION = 1;

	String submitDate;

	final Context context = this;
	DataBaseHelper database;
	public String sessionIDForJobTrack;
	ArrayList<ReturnRouteTasksObject> ReturnRouteTask;
	ArrayList<ReturnRouteTaskStatusObject> ReturnRouteStatusTask;

	String routeSubmit;

	private static final int ZBAR_SCANNER_REQUEST = 0;
	public static final int ZBAR_OR_SCANNER_REQUEST = 1;
	List<String> Type_list = new ArrayList<String>();
	List<String> job_list = new ArrayList<String>();

	ArrayList<String> jobscan_list = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.route_tracker_app);
		back = (TextView) findViewById(R.id.txt_backrouter);
		scan = (Button) findViewById(R.id.btn_scan_job);
		submit = (Button) findViewById(R.id.btn_submit_route);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
			String[] perms = {"android.permission.CAMERA"};

			int permsRequestCode = 200;

			requestPermissions(perms, permsRequestCode);
		}
		// scan = (Button) findViewById(R.id.btn_scan_job);
		// scanLocation = (Button) findViewById(R.id.btn_scan_location);
		cancel = (Button) findViewById(R.id.btncancel);
		Job_Scanner = (EditText) findViewById(R.id.edt_scan);
		location_scanner = (EditText) findViewById(R.id.edt_location);
		select_Type = (Spinner) findViewById(R.id.spinner_type);
		Select_job = (Spinner) findViewById(R.id.spinner_status);

		// Type_list.add("Select Job Type");
		// Type_list.add("Pickup");
		// Type_list.add("Delivery");
		// Type_list.add("Signature");
		CompetitionAdapter = new ArrayAdapter<String>(RouteTrackerApp.this,
				R.layout.spinner_row, Type_list);
		new Mytask().execute();

		CompetitionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_Type.setAdapter(CompetitionAdapter);
		select_Type.setSelection(SessionData.getInstance().getType());
		select_Type
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int position, long id) {

						taskCode = Integer.parseInt(ReturnRouteTask.get(
								position).getCode());
						SessionData.getInstance().setType(position);
						Log.d("taskCode", "" + taskCode);
						new MyStatus().execute();

						// if (position == 1) {
						// List<String> job_list = new ArrayList<String>();
						//
						// job_list.add("Pick up Papers");
						// job_list.add("Nothing available for pickup");
						//
						// StatusAdapter = new ArrayAdapter<String>(
						// RouteTrackerApp.this, R.layout.spinner_row,
						// job_list);
						//
						// StatusAdapter
						// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// Select_job.setAdapter(StatusAdapter);
						// } else if (position == 2) {
						// List<String> job_list = new ArrayList<String>();
						//
						// job_list.add("Delivered to Court");
						// job_list.add("Delivered to client");
						// job_list.add("Delivered to third party");
						//
						// StatusAdapter = new ArrayAdapter<String>(
						// RouteTrackerApp.this, R.layout.spinner_row,
						// job_list);
						//
						// StatusAdapter
						// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// Select_job.setAdapter(StatusAdapter);
						// } else if (position == 3) {
						// List<String> job_list = new ArrayList<String>();
						//
						// job_list.add("Signature received");
						// job_list.add("Left for signature");
						//
						// StatusAdapter = new ArrayAdapter<String>(
						// RouteTrackerApp.this, R.layout.spinner_row,
						// job_list);
						//
						// StatusAdapter
						// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// Select_job.setAdapter(StatusAdapter);
						// } else {
						// List<String> job_list = new ArrayList<String>();
						//
						// StatusAdapter = new ArrayAdapter<String>(
						// RouteTrackerApp.this, R.layout.spinner_row,
						// job_list);
						//
						// StatusAdapter
						// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// Select_job.setAdapter(StatusAdapter);
						// }

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		Select_job
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int position, long id) {

						submitStatusCode = Integer
								.parseInt(ReturnRouteStatusTask.get(position)
										.getCode());
						SessionData.getInstance().setStatus(position);
						Log.d("submitStatusCode", "" + submitStatusCode);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// scanLocation.setOnClickListener(this);
		back.setOnClickListener(this);
		scan.setOnClickListener(this);
		cancel.setOnClickListener(this);
		submit.setOnClickListener(this);


//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null) {
//			if (SessionData.getInstance().getScanner_result() == 1){
//				String scan_value = bundle.getString("value");
//
//				Job_Scanner.setText(scan_value);
//				Log.d("Job_Scanner",""+Job_Scanner.getText().toString());
//			}
//		}



		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
				String scan_value = bundle.getString("value");
			String job_scan;
					if (SessionData.getInstance().getScanner_value().toString().equalsIgnoreCase("job")){
						if (SessionData.getInstance().getFinal_scanjobresult().length() != 0){
							job_scan = SessionData.getInstance().getFinal_scanjobresult() +"\n" +scan_value;
						}else {
							job_scan = scan_value;
						}
						SessionData.getInstance().setFinal_scanjobresult(job_scan);
						Log.d("Job_Scanner",""+SessionData.getInstance().getFinal_scanjobresult());
					}
		}

		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null){
				String scan_value = bundle1.getString("value1");
				if (SessionData.getInstance().getScanner_value().toString().equalsIgnoreCase("location")){
					SessionData.getInstance().setFinal_locationresult(scan_value);
					Log.d("location_Scanner",""+SessionData.getInstance().getFinal_locationresult());
				}
		}

		Job_Scanner.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SessionData.getInstance().setFinal_scanjobresult(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		location_scanner.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SessionData.getInstance().setFinal_locationresult(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


	}

	private class Mytask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				ReturnRouteTask = WebServiceConsumer.getInstance()
						.getReturnAppOptions(sessionId);

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				ReturnRouteTask = null;
			} catch (Exception e) {
				e.printStackTrace();
				ReturnRouteTask = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (ReturnRouteTask != null) {
				Type_list = new ArrayList<String>();
				for (int i = 0; ReturnRouteTask.size() > i; i++) {
					Type_list.add(ReturnRouteTask.get(i).getTitle());
				}

				CompetitionAdapter = new ArrayAdapter<String>(
						RouteTrackerApp.this, R.layout.spinner_row, Type_list);
				CompetitionAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				select_Type.setAdapter(CompetitionAdapter);
				select_Type.setSelection(SessionData.getInstance().getType());
			}
		}
	}

	private class MyStatus extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				ReturnRouteStatusTask = WebServiceConsumer.getInstance()
						.getReturnRouteTaskStatus(sessionId, taskCode);

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				ReturnRouteStatusTask = null;
			} catch (Exception e) {
				e.printStackTrace();
				ReturnRouteStatusTask = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (ReturnRouteStatusTask != null) {
				job_list = new ArrayList<String>();
				for (int i = 0; ReturnRouteStatusTask.size() > i; i++) {
					job_list.add(ReturnRouteStatusTask.get(i).getTitle());
				}

				StatusAdapter = new ArrayAdapter<String>(RouteTrackerApp.this,
						R.layout.spinner_row, job_list);

				StatusAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Select_job.setAdapter(StatusAdapter);
				Select_job.setSelection(SessionData.getInstance().getStatus());
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null) {
//			if (SessionData.getInstance().getScanner_result() == 1){
//				String scan_value = bundle.getString("value");
//
//				Job_Scanner.setText(scan_value);
//				Log.d("Job_Scanner",""+Job_Scanner.getText().toString());
//			}
//		}

		if (SessionData.getInstance().getScanner_result() == 1){

			Job_Scanner.setText(SessionData.getInstance().getFinal_scanjobresult());
			location_scanner.setText(SessionData.getInstance().getFinal_locationresult());
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == back) {

			// finish();
			Intent submit = new Intent(RouteTrackerApp.this, ListCategory.class);
			startActivity(submit);
		} else if (v == scan) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_scan_route_tracker);

			TextView jobscan = (TextView) dialog.findViewById(R.id.job_scan);
			TextView locationscan = (TextView) dialog
					.findViewById(R.id.location_scan);
			TextView cancel = (TextView) dialog.findViewById(R.id.cancel_scan);

			jobscan.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					str_jobScanner = Job_Scanner.getText().toString();
					if (isCameraAvailable()) {

						SessionData.getInstance().setScanjobresult("");

						SessionData.getInstance().setScanner(1);
						SessionData.getInstance().setScanner_loca(0);
						SessionData.getInstance().setScanner_value("job");

						SessionData.getInstance().setScanner_result(1);
						launchActivity(SimpleScannerActivity.class);

						/*Intent intent = new Intent(getApplicationContext(),
								ZBarScannerActivity.class);

						startActivityForResult(intent, ZBAR_SCANNER_REQUEST);*/
					} else {
						Toast.makeText(getApplicationContext(),
								"Rear Facing Camera Unavailable",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			locationscan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (isCameraAvailable()) {

						SessionData.getInstance().setScanner(0);
						SessionData.getInstance().setScanner_loca(1);
						SessionData.getInstance().setScanner_value("location");

						SessionData.getInstance().setScanner_result(1);

						launchActivity(SimpleScannerActivity.class);
						/*Intent intent = new Intent(getApplicationContext(),
								ZBarScannerActivity.class);

						startActivityForResult(intent, ZBAR_OR_SCANNER_REQUEST);*/
					} else {
						Toast.makeText(getApplicationContext(),
								"Rear Facing Camera Unavailable",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();

		}
		// finish();
		//
		// Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		// startActivityForResult(intent, 0);

		else if (v == submit) {
			// initializeSubmit();
			SubmitJob = Job_Scanner.getText().toString();
			SubmitJob = SubmitJob.replace("\n",",");
			submitCustomer = location_scanner.getText().toString();

			if (SubmitJob != null) {
				submitWorkOrder = new ArrayList<String>(Arrays.asList(SubmitJob
						.split(",")));

			}

			@SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			submitDate = dateFormat.format(cal.getTime());

			Log.d("Date", "" + submitDate);

			if (submitCustomer.length() == 0) {
				new CustomAlertDialog(RouteTrackerApp.this,
						"Location field shoud not be empty").show();
			} else if (SubmitJob.length() == 0) {
				new CustomAlertDialog(RouteTrackerApp.this,
						"Jobs field shoud not be empty").show();
			} else {
				new Submittask().execute();
			}

		}

		else if (v == cancel) {

			// finish();
			Intent submit = new Intent(RouteTrackerApp.this, ListCategory.class);
			startActivity(submit);
		}

	}

	private class Submittask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(RouteTrackerApp.this,
					"Processing, please wait...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				for(int i = 0; submitWorkOrder.size()>i;i++){
					String workorder = submitWorkOrder.get(i);
					String sessionId = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					Log.d("WorkOrder_job",""+workorder);
					Log.d("WorkOrder_location",""+submitCustomer);
					Log.d("taskCode",""+taskCode);
					Log.d("submitStatusCode",""+submitStatusCode);
					Log.d("submitDate",""+submitDate);
					routeSubmit = SubmitWebServiceConsumer.getInstance()
							.SubmitJobTrack(sessionId, workorder,
									taskCode, submitStatusCode, submitCustomer,
									submitDate);
				}



			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				routeSubmit = null;
			} catch (Exception e) {
				e.printStackTrace();
				routeSubmit = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			ProgressBar.dismiss();

			if (routeSubmit != null) {
				// Toast.makeText(getApplicationContext(), routeSubmit,
				// Toast.LENGTH_LONG).show();

				Intent submit = new Intent(RouteTrackerApp.this,
						ListCategory.class);

				new CustomAlertDialog(RouteTrackerApp.this, routeSubmit, submit)
						.show();
			}
		}
	}

	// private void initializeSubmit() {
	// // TODO Auto-generated method stub
	// new AsyncTask<Void, Void, Void>() {
	// // String errorString = null;
	// protected void onPreExecute() {
	// ProgressBar.showCommonProgressDialog(
	// RouteTrackerApp.this, "Loading...");
	//
	// };
	// @Override
	// protected Void doInBackground(Void... params) {
	//
	// try {
	//
	//
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// return null;
	// }
	//
	//
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// ProgressBar.dismiss();
	// new CustomAlertDialog(RouteTrackerApp.this,
	// "Submit Successfully").show();
	//
	// super.onPostExecute(result);
	// }
	// };
	// }


	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ZBAR_SCANNER_REQUEST:
				// case ZBAR_QR_SCANNER_REQUEST:
				if (resultCode == RESULT_OK) {
					// stradd=(data.getStringExtra(ZBarConstants.SCAN_RESULT));
					str_jobScanner = str_jobScanner.concat(data
							.getStringExtra(ZBarConstants.SCAN_RESULT) + "\n");
					Job_Scanner.setText(str_jobScanner);

					// Toast.makeText(this, "Scan Result = " +
					// data.getStringExtra(ZBarConstants.SCAN_RESULT),
					// Toast.LENGTH_SHORT).show();
				} else if (resultCode == RESULT_CANCELED && data != null) {
					String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
					if (!TextUtils.isEmpty(error)) {
						Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case ZBAR_OR_SCANNER_REQUEST:
				// case ZBAR_QR_SCANNER_REQUEST:
				if (resultCode == RESULT_OK) {
					location_scanner.setText(data
							.getStringExtra(ZBarConstants.SCAN_RESULT));

					// Toast.makeText(this, "Scan Result = " +
					// data.getStringExtra(ZBarConstants.SCAN_RESULT),
					// Toast.LENGTH_SHORT).show();
				} else if (resultCode == RESULT_CANCELED && data != null) {
					String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
					if (!TextUtils.isEmpty(error)) {
						Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
					}
				}
				break;
		}
	}*/

	// public void onActivityResult(int requestCode, int resultCode, Intent
	// intent) {
	// if (requestCode == 0) {
	// if (resultCode == RESULT_OK) {
	// String contents = intent.getStringExtra("SCAN_RESULT");
	// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	// Log.i("xZing", "contents: "+contents+" format: "+format);
	// // Handle successful scan
	// } else if (resultCode == RESULT_CANCELED) {
	// Log.i("xZing", "Cancelled");
	// // Handle cancel
	// }
	// }
	// }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent submit = new Intent(RouteTrackerApp.this, ListCategory.class);
		startActivity(submit);
		super.onBackPressed();
	}

	public void launchActivity(Class<?> clss) {
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			mClss = clss;
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
		} else {
			Intent intent = new Intent(this, clss);
			startActivity(intent);
		}
	}

}
