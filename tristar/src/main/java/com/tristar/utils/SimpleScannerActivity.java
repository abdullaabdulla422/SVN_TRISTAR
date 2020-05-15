package com.tristar.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.main.BaseFileIncluder;
import com.tristar.main.ListCategory;
import com.tristar.main.R;
import com.tristar.main.RecordDiligence;
import com.tristar.main.RouteTrackerApp;
import com.tristar.main.Scanned_Value_Adapter;
import com.tristar.object.ProcessAddressForServer;

import java.util.ArrayList;

import me.dm7.barcodescanner.core.CameraPreview;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class SimpleScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler, ZBarConstants {
	TextView cancel, Done;
	LinearLayout Scan_result;
	String JobValues;
	EditText Scanned_val;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	ArrayList<ProcessAddressForServer> processOrderListArray;
	String Message;
	ViewGroup contentFrame;
	private ZBarScannerView mScannerView;
	private CameraPreview mPreview;

	@Override
	public void onBackPressed() {
		SessionData.getInstance().setValidate_record_Deligence_CheckBox(false);
		SessionData.getInstance().setValidate_record_Deligence_Scanned_result(2);
		SessionData.getInstance().getScanned_Workorder().clear();
		SessionData.getInstance().getScanned_Item_Process_ID().clear();
		Intent intent = new Intent(SimpleScannerActivity.this, ListCategory.class);
		startActivity(intent);
		finish();

	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_simple_scanner);
		setupToolbar();
		if (!isCameraAvailable()) {
			// Cancel request if there is no rear-facing camera.
			cancelRequest();
			return;
		}
		BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SCANNER;

		contentFrame = (ViewGroup) findViewById(R.id.camera_preview);
		mScannerView = new ZBarScannerView(this);
		contentFrame.addView(mScannerView);
		processOrderListArray = new ArrayList<ProcessAddressForServer>();
		cancel = (TextView) findViewById(R.id.cancel);
		Done = (TextView) findViewById(R.id.done);
		Scanned_val = (EditText) findViewById(R.id.scan_value);
		Scan_result = (LinearLayout) findViewById(R.id.scanned_layout);
		Scanned_val.setFocusable(false);

		Done.setVisibility(View.GONE);
		Scan_result.setVisibility(View.GONE);

		Done.setClickable(true);

		Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
		cancel.startAnimation(animation2);

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SessionData.getInstance().setValidate_record_Deligence_CheckBox(false);
				SessionData.getInstance().setValidate_record_Deligence_Scanned_result(2);
				SessionData.getInstance().getScanned_Workorder().clear();
				SessionData.getInstance().getScanned_Item_Process_ID().clear();
				Intent intent = new Intent(SimpleScannerActivity.this, ListCategory.class);
				startActivity(intent);
				finish();
			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Message = bundle.getString("Message");
			if (Message.length() != 0) {
				Toast.makeText(this, Message, Toast.LENGTH_LONG).show();
			} else {
			}
		}
		if (SessionData.getInstance().isValidate_record_Deligence_CheckBox()
				|| SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 0) {
			Done.setVisibility(View.VISIBLE);

		} else {
			if (SessionData.getInstance().getScanner_activity() == 1) {
				Scan_result.setVisibility(View.GONE);
				Done.setVisibility(View.GONE);
			} else if (SessionData.getInstance().getScanner() == 1) {
				Scan_result.setVisibility(View.VISIBLE);
				Done.setVisibility(View.VISIBLE);
			} else if (SessionData.getInstance().getScanner_loca() == 1) {
				Scan_result.setVisibility(View.GONE);
				Done.setVisibility(View.GONE);
			}

		}



		Done.startAnimation(animation2);

		Done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Done.setText("Done");
				if (SessionData.getInstance().getScanned_Workorder().size() == 0
						&& SessionData.getInstance().isValidate_record_Deligence_CheckBox()){
					Toast.makeText(SimpleScannerActivity.this, "No work order Scanned", Toast.LENGTH_SHORT).show();
				}else {
				if (SessionData.getInstance().isValidate_record_Deligence_CheckBox()
						|| SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 0) {
					SessionData.getInstance().setValidate_record_Deligence_CheckBox(false);
					SessionData.getInstance().setValidate_record_Deligence_Scanned_result(2);
					Done.setClickable(false);
					showDialog();
				} else {
					JobValues = SessionData.getInstance().getScanjobresult();
					Intent intent = new Intent(SimpleScannerActivity.this, RouteTrackerApp.class);
					intent.putExtra("value", JobValues);
					startActivity(intent);
					finish();
				}}
			}
		});

	}

	@SuppressLint("SetTextI18n")
	private void showDialog() {

		final Dialog dialog = new Dialog(SimpleScannerActivity.this);
		dialog.setContentView(R.layout.scanned_value_dialog);
		TextView txt_Title = (TextView) dialog.findViewById(R.id.txt_Title);
		ListView list_details = (ListView) dialog.findViewById(R.id.list_details);
		Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
		Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
		txt_Title.setText("Scanned Workorders");

		if (SessionData.getInstance().getScanned_Workorder() == null ||
				SessionData.getInstance().getScanned_Workorder().size() == 0){

		}else {
			Scanned_Value_Adapter adapter = new Scanned_Value_Adapter(SessionData.getInstance().getScanned_Workorder(), this);
			list_details.setAdapter(adapter);

		}
		btn_yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(SessionData.getInstance().getScanned_Workorder().size()!=0){
					Intent processdetail = new Intent(SimpleScannerActivity.this,
							RecordDiligence.class);
					startActivity(processdetail);
				}else{
					Toast.makeText(SimpleScannerActivity.this,"No Workorders Scanned",Toast.LENGTH_SHORT).show();
				}


			}
		});

		btn_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Done.setClickable(true);
				SessionData.getInstance().getScanned_Workorder().clear();
				SessionData.getInstance().getScanned_Item_Process_ID().clear();
				SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
				dialog.dismiss();


			}
		});
		dialog.show();


//		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
//		aBuilder.setTitle("Scanned Workorders");
//		Scanned_Value_Adapter adapter = new Scanned_Value_Adapter(SessionData.getInstance().getScanned_Workorder(), this);
//		aBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//
//		aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				RecordDiligence._lineItem = 0;
//				Intent processdetail = new Intent(SimpleScannerActivity.this,
//						RecordDiligence.class);
//				startActivity(processdetail);
//			}
//		});	aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//
//		aBuilder.show();
	}

	@Override
	public void onResume() {
		super.onResume();
		mScannerView.setResultHandler(this);
		mScannerView.startCamera();
	}

	@Override
	public void onPause() {
		super.onPause();
		mScannerView.stopCamera();
	}

	public void cancelRequest() {
		Intent dataIntent = new Intent();
		dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
		setResult(Activity.RESULT_CANCELED, dataIntent);
		finish();
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	public void handleResult(final Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

		// Note:
		// * Wait 2 seconds to resume the preview.
		// * On older devices continuously stopping and resuming camera preview can result in freezing the app.
		// * I don't know why this is the case but I don't have the time to figure out.
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
				if (SessionData.getInstance().isValidate_record_Deligence_CheckBox()
						|| SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 0) {
					String Scanned_result = rawResult.getContents();
					if (rawResult.getContents() != null && rawResult.getContents().length() != 0) {
						SessionData.getInstance().setValidate_record_Deligence_Scanned_result(1);
						Intent intent = new Intent(SimpleScannerActivity.this, ListCategory.class);
						intent.putExtra("value", rawResult.getContents());
						contentFrame.removeAllViews();
						rawResult.setContents(null);
						startActivity(intent);
					} else {
						return;
					}

				} else {
					if (SessionData.getInstance().getScanner_activity() == 1) {
						SessionData.getInstance().setScanner_activity_result(1);
						Intent intent = new Intent(SimpleScannerActivity.this, ListCategory.class);
						intent.putExtra("value", rawResult.getContents());
						startActivity(intent);
						finish();
					} else if (SessionData.getInstance().getScanner() == 1) {

						String scan_value = rawResult.getContents();
						String job_scan;

						if (SessionData.getInstance().getScanjobresult().length() != 0) {
							job_scan = SessionData.getInstance().getScanjobresult() + "\n" + scan_value;
						} else {
							job_scan = scan_value;
						}
						SessionData.getInstance().setScanjobresult(job_scan);
						Scanned_val.setText(SessionData.getInstance().getScanjobresult());
						Log.d("Job_Scanner ", "With in Sanner " + SessionData.getInstance().getScanjobresult());

						mScannerView.setResultHandler(SimpleScannerActivity.this);
						mScannerView.startCamera();
						//SessionData.getInstance().setScanner_result(1);
                    /*Intent intent = new Intent(SimpleScannerActivity.this,RouteTrackerApp.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();*/
					} else if (SessionData.getInstance().getScanner_loca() == 1) {
						Intent intent = new Intent(SimpleScannerActivity.this, RouteTrackerApp.class);
						intent.putExtra("value1", rawResult.getContents());
						startActivity(intent);
						finish();
					}
                /*if (Sessiondata.getInstance().getScanner_partreceipt() == 1){
                    Intent intent = new Intent(SimpleScannerActivity.this,PartReceiptActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_partreceiving() == 2){
                    Intent intent = new Intent(SimpleScannerActivity.this,PartsReceivingDetailsActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_replace() == 3){
                    Intent intent = new Intent(SimpleScannerActivity.this,ReplacePartsActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_counting1() == 4){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value1", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_counting2() == 5){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value2", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_inventory() == 6){
                    Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }*/

				}
			}
		}, 1000);
	}
}
