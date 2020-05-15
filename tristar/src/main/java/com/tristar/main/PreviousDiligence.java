package com.tristar.main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tristar.adapters.DiligenceAdapter;
import com.tristar.db.DataBaseHelper;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.SubmitDiligence;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;

@SuppressWarnings("ALL")
public class PreviousDiligence extends Activity implements View.OnClickListener {

	TextView back, Previous_diligences, txt_adresline;
	ImageView image;
	DataBaseHelper database;
	ListView listview_previousdiligence;
	int processOrderID;
	ProcessAddressForServer processOrderToDisplay;
	ArrayList<DiligenceForProcess> arrayListProcessDiligence;
	ArrayList<SubmitDiligence> locallyAddedDiligencesArray;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.previous_diligence);
		listview_previousdiligence = (ListView) findViewById(R.id.listViewpreviousdiligence);
		back = (TextView) findViewById(R.id.txt_processback);

		Previous_diligences = (TextView) findViewById(R.id.Previous_diligences);
		image = (ImageView) findViewById(R.id.imageButtonback);
		database = DataBaseHelper.getInstance();
		processOrderID = getIntent().getExtras().getInt("processOrderID");
		locallyAddedDiligencesArray = new ArrayList<SubmitDiligence>();
		arrayListProcessDiligence = new ArrayList<DiligenceForProcess>();
	//	arrayListProcessDiligence = SessionData.getInstance().getArrayListProcessDiligence();
		try {
			processOrderToDisplay = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		back.setOnClickListener(this);
		image.setOnClickListener(this);

		viewDidLoad();
		setValuesforViewsAndListViewAdapter();

	}

	@Override
	public void onClick(View v) {
		if (v == back || v == image) {
			finish();
			Intent intent = new Intent(PreviousDiligence.this,
					ProcessOrderDetail.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
		}

	}

	public void setValuesforViewsAndListViewAdapter() {
		listview_previousdiligence.setAdapter(new DiligenceAdapter(this,
				arrayListProcessDiligence, processOrderID));
		listview_previousdiligence.setDividerHeight(1);
		if (locallyAddedDiligencesArray.size() == 0
				&& arrayListProcessDiligence.size() == 0) {
			Intent detailView = new Intent(PreviousDiligence.this,
					ProcessOrderDetail.class);
			detailView.putExtra("processOrderID", processOrderID);
			new CustomAlertDialog(PreviousDiligence.this,
					"No Diligences to show!", detailView).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (locallyAddedDiligencesArray.size() == 0
				&& arrayListProcessDiligence.size() == 0) {
			Intent detailView = new Intent(PreviousDiligence.this,
					ProcessOrderDetail.class);
			detailView.putExtra("processOrderID", processOrderID);
			new CustomAlertDialog(PreviousDiligence.this,
					"No Diligences to show!", detailView).show();
		}
	}

	@SuppressLint("LongLogTag")
	public void viewDidLoad() {
		try {
			if (processOrderToDisplay.getWorkorder().length() == 0) {
				Previous_diligences.setText("N/A");
			} else {
				Previous_diligences.setText(processOrderToDisplay
						.getWorkorder());

			}

			arrayListProcessDiligence = database
					.getReturnDiligenceValuesFromDBToDisplay(
							processOrderToDisplay.getWorkorder(),
							processOrderToDisplay.getAddressLineItem());

			Log.d("Address Line item", String.valueOf(arrayListProcessDiligence
					.get(0).getAddressLineItem()));
			Log.d("Report", arrayListProcessDiligence.get(0).getReport());
			Log.d("Report", arrayListProcessDiligence.get(0).getReport());

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Inside Previous Diligence", "Error in fetching data from db");
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		Intent attach = new Intent(PreviousDiligence.this,
				ProcessOrderDetail.class);
		attach.putExtra("processOrderID", processOrderID);
		startActivity(attach);
	}
}