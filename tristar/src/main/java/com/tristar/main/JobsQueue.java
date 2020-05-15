package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.DiligencePhrase;
import com.tristar.object.MannerOfService;
import com.tristar.object.PODAttachments;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.SubmitCourtPOD;
import com.tristar.object.SubmitDeliveryPOD;
import com.tristar.object.SubmitDiligence;
import com.tristar.object.SubmitFinalStatus;
import com.tristar.object.SubmitPickupPOD;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.SubmitWebServiceConsumer;
import com.tristar.webutils.WebServiceConsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("ALL")
public class JobsQueue extends Activity implements OnClickListener {
	static final int DATE_DIALOG_ID = 999;
	private static TextView selectedTextView;
	public ArrayList<DiligencePhrase> listStatusValues;
	public ArrayList<String> Diligence = new ArrayList<>();
	public ArrayList<String> mannerofservice;
	public ArrayList<String> mannerofserviceCode;
	public ArrayList<String> wheelMenuWeight;
	public ArrayList<String> wheelMenuHeight;
	public ArrayList<String> wheelMenuRace;
	public ArrayList<String> wheelMenuHair;
	public ArrayList<String> wheelMenuEye;
	public ArrayList<String> wheelMenuAge;
	public ArrayList<String> relationship;
	ListView listView;
	TextView txtJobsDdit;
	TextView txt_sync;
	View lblCategory, imgCategory;
	DataBaseHelper database;
	ListCategory listCategory = new ListCategory();
	int court = 0;
	String sessionId;
	String serverCode;
	Button ok;
	int jobsInQueue = 0;
	ArrayList<byte[]> attachedFilesData;
	byte[] audiofile;
	ArrayList<SubmitDiligence> submitImage = new ArrayList<SubmitDiligence>();
	ArrayList<SubmitCourtPOD> podImage = new ArrayList<SubmitCourtPOD>();
	ArrayList<PODAttachments> deliverypodImage = new ArrayList<PODAttachments>();
	ArrayList<PODAttachments> courtpodImage = new ArrayList<PODAttachments>();
	ArrayList<ProcessAddressForServer> processOrderListArray = new ArrayList<ProcessAddressForServer>();
	ArrayList<CourtAddressForServer> courtServiceListArray = new ArrayList<CourtAddressForServer>();
	ArrayList<AddressForServer> pickupServiceListArray = new ArrayList<AddressForServer>();
	ArrayList<AddressForServer> deliveryServiceListArray = new ArrayList<AddressForServer>();
	ArrayList<Object> jobList;
	//Record Diligence Dialog
	TextView RD_date;
	TextView RD_textAddress;
	EditText RD_report;
	TextView RD_workorder;
	LinearLayout RD_layout_special_instruction;
	Button RD_edt_image;
	TextView RD_audio;
	TextView RD_image;
	TextView RD_latlang;
	TextView RD_set_time;
	Button Rd_btn_Save;
	CheckBox RD_timeCheckBox;
	SubmitDiligence RD_address;
	String[] RD_deligience_specialreport = {""};
	//Court_Services
	TextView CS_name;
	TextView CS_addresss;
	TextView CS_workorder;
	TextView CS_duedate;
	TextView CS_priority;
	TextView CS_instructions;
	TextView CS_documents;
	TextView CS_date;
	TextView CS_Time;
	CheckBox CS_chk_time;
	EditText CS_comments;
	EditText CS_feeadvance;
	TextView CS_casename;
	TextView CS_caseNumber;
	EditText CS_weight;
	EditText CS_pieces;
	EditText CS_waittime;
	TextView CS_image;
	Button CS_btn_save;
	boolean CS_is_updated;
	//Pickup_Service
	TextView PS_workorder;
	TextView PS_casename;
	TextView PS_casenumber;
	TextView PS_jobinstructions;
	TextView PS_addressinstructions;
	TextView PS_addresss;
	TextView PS_duedate;
	TextView PS_priority;
	TextView PS_duetime;
	TextView PS_date;
	TextView PS_time;
	EditText PS_comments;
	Button PS_btn_Save;

	//Delivery_Service
	SubmitPickupPOD PS_submitPickupPOD;
	boolean PS_isSubmitSucess;
	TextView DS_workorder;
	TextView DS_addresss;
	TextView DS_job_instructions;
	TextView DS_address_instructions;
	TextView DS_Casename;
	TextView DS_Casenumber;
	TextView DS_duedate;
	TextView DS_priority;
	TextView DS_duetime;
	TextView DS_date;
	TextView DS_time;
	EditText DS_comments;
	EditText DS_feeadvance;
	EditText DS_weight;
	EditText DS_pieces;
	EditText DS_waittime;
	EditText DS_Receivedby;
	TextView DS_image;
	Button DS_btn_save;

	//FinalStatus
	SubmitDeliveryPOD DS_submitDeliveryPOD;
	boolean DS_isSubmitSucess;
	ArrayList<MannerOfService> listofManner;
	ArrayList<ProcessAddressForServer> processOrderlist;
	TextView FS_workorder;
	CheckBox FS_Entity;
	TextView FS_service;
	TextView FS_name;
	TextView FS_title;
	TextView FS_txtaddress;
	TextView FS_mannerofService;
	EditText FS_leftWith;
	EditText FS_relationship;
	TextView FS_servedDate;
	TextView FS_servedTime;
	RadioButton FS_male;
	RadioButton FS_female;
	CheckBox FS_inuniform;
	CheckBox FS_military;
	CheckBox FS_police;
	TextView FS_age;
	TextView FS_hair;
	TextView FS_skin;
	TextView FS_Weight;
	TextView FS_Height;
	TextView FS_Eye;
	EditText FS_distingusihing;
	EditText FS_reports;
	TextView FS_gps;
	TextView FS_signature;
	ImageView FS_img_finalstatus_relationship;
	LinearLayout FS_layout_manner_of_services;
	LinearLayout FS_layout_relationship;
	LinearLayout FS_layout_age;
	LinearLayout FS_layout_hair;
	LinearLayout FS_layout_race;
	LinearLayout FS_layout_weight;
	LinearLayout FS_layout_height;
	LinearLayout FS_layout_eye;
	Button FS_btn_save;
	boolean FS_is_updated;


	//dialog object
	Object item;


	private boolean editMode = false;
	private int year;
	private int month;
	private int day;
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int yearSelected,
							  int monthOfYear, int dayOfMonth) {
			year = yearSelected;
			month = monthOfYear;
			day = dayOfMonth;

			Calendar datetime = Calendar.getInstance();
			datetime.set(Calendar.DATE, dayOfMonth);
			datetime.set(Calendar.MONTH, monthOfYear);
			datetime.set(Calendar.YEAR, yearSelected);

			SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
			String dateFormatted = dateformat.format(datetime.getTime());
			selectedTextView.setText(dateFormatted);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jobsqueue);
		editMode = false;
		database = DataBaseHelper.getInstance(JobsQueue.this);

		initializeControlls();
		setListeners();
		processList();
	}

	private void initializeControlls() {
		txtJobsDdit = (TextView) findViewById(R.id.txt_jobs_edit);
		txt_sync = (TextView) findViewById(R.id.txt_sync);

		lblCategory = findViewById(R.id.lbl_category);
		imgCategory = findViewById(R.id.img_category);

		listView = (ListView) findViewById(R.id.list_jobs);


		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

	}

	private void setListeners() {
		txtJobsDdit.setOnClickListener(this);
		imgCategory.setOnClickListener(this);
		txt_sync.setOnClickListener(this);
		findViewById(R.id.tab_category).setOnClickListener(this);
	}

	private void processList() {
		jobList = new ArrayList<Object>();
		{
			ArrayList<ProcessAddressForServer> list = database
					.getFinalStatusToSubmit();
			if (list != null && list.size() > 0) {
				jobList.add("Process Order");
				jobList.addAll(list);

			}
		}
		{
			ArrayList<CourtAddressForServer> list = database
					.getSubmitCourtOrder();
			if (list != null && list.size() > 0) {
				jobList.add("Court Services");
				jobList.addAll(list);
			}
		}
		{
			ArrayList<SubmitDiligence> list = database
					.getSubmitDiligencesValuesFromDBForUploadingToServer();
			if (list != null && list.size() > 0) {
				jobList.add("Record Diligence");
				jobList.addAll(list);
			}
		}
		{
			ArrayList<SubmitStatusList> list = database.getSubmitStatusValuesFromDBToDisplay();
			if (list != null && list.size() > 0) {
				jobList.add("Court Status");
				jobList.addAll(list);
			}
		}

		{
			ArrayList<SubmitStatusList> list = database.getSubmitPickupStatusValuesFromDBToDisplay();
			if (list != null && list.size() > 0) {
				jobList.add("Pickup Status");
				jobList.addAll(list);
			}
		}

		{
			ArrayList<SubmitStatusList> list = database.getSubmitDeliveryStatusValuesFromDBToDisplay();
			if (list != null && list.size() > 0) {
				jobList.add("Delivery Status");
				jobList.addAll(list);
			}
		}

		ArrayList<AddressForServer> list = database.getSubmitPickupOrder();
		if (list != null && list.size() > 0) {
			jobList.add("Pickup Services");
			jobList.addAll(list);
		}
		list = database.getSubmitDeliveryOrder();
		if (list != null && list.size() > 0) {
			jobList.add("Delivery Services");
			jobList.addAll(list);
		}

		if (jobList.size() > 0) {
			JobAdapter adapter = new JobAdapter();
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onClick(View v) {

		if (v == findViewById(R.id.tab_category) || v == imgCategory) {
			finish();
			Intent category = new Intent(JobsQueue.this, ListCategory.class);
			startActivity(category);
		} else if (txtJobsDdit == v) {
			editMode = !editMode;
			if (editMode) {
				txtJobsDdit.setText("Done");
			} else {
				txtJobsDdit.setText("Edit");
			}
			JobAdapter adapter = new JobAdapter();
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		} else if (v == txt_sync) {
            SessionData.getInstance().setSynchandler(1);
			new CustomAlertDialog(this, getResources().getString(
					R.string.alert_sync), CustomAlertDialog.SYNC).show();
		}
	}

	private void deleteRecordFromTable(int index) {
		item = jobList.get(index);
		try {
			if (item instanceof ProcessAddressForServer) {
				ProcessAddressForServer address = (ProcessAddressForServer) item;
				database.deleteFinalStausTableAfterSync(address.getLineItem());
				database.deleteFinalStatusAttachementTable(address
						.getLineItem());
			} else if (item instanceof CourtAddressForServer) {
				CourtAddressForServer address = (CourtAddressForServer) item;
				database.deleteSubmitCourtPODTableAfterSync(address
						.getCourtOpenAddressID());
				database.deleteSubmitCourtPODAttachementTableFromQueue(address
						.getWorkorder());
			} else if (item instanceof SubmitDiligence) {
				SubmitDiligence address = (SubmitDiligence) item;
				database.deleteSubmitDiligence(address.getLineItem());
				database.updateSubmitDiligenceAttachmentsTableAfterSync(address
						.getLineItem());
			} else if (item instanceof SubmitStatusList) {
				SubmitStatusList address = (SubmitStatusList) item;
				database.deleteSubmitStatus(address.getLineitem());
				database.updateSubmitStatusTable(address.getLineitem());
				database.deleteSubmitPickupStatus(address.getLineitem());
				database.updateSubmitPickupStatusTable(address.getLineitem());
				database.deleteSubmitDeliveryStatus(address.getLineitem());
				database.updateSubmitDileveryStatusTable(address.getLineitem());

			} else if (item instanceof AddressForServer) {
				AddressForServer address = (AddressForServer) item;
				if (address.TYPE == AddressForServer.PICKUP_SERVICE) {
					database.deleteSubmitPickupPODTableAfterSync(address
							.getJobID());
					database.deleteSubmitPickupPODAttachementTableFromQueue(address
							.getWorkorder());
				} else if (address.TYPE == AddressForServer.DELIVERY_SERVICE) {
					database.deleteSubmitDeliveryPODTableAfterSync(address
							.getJobID());
					database.deleteSubmitDeliveryPODAttachementTableFromQueue(address
							.getWorkorder());
				}
			}

			if (index > 0) {
				jobList.remove(item);
				if (jobList.get(index - 1) instanceof String) {
					if (jobList.size() == index
							|| jobList.get(index) instanceof String)
						jobList.remove(index - 1);
				}
				JobAdapter adapter = new JobAdapter();
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getFinal_Staus_Manner_Value(int processOrderID) {
		listofManner = new ArrayList<MannerOfService>();
		mannerofservice = new ArrayList<>();
		processOrderlist = new ArrayList<ProcessAddressForServer>();
		try {
			ProcessAddressForServer procesAddress = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(15);
			listofManner = database.getMannerOfServiceByStateCode(procesAddress
					.getCourtStateCode());

//			btn_manner.setText(listofManner.get(0).getTitle());

//		;	manner_code = listofManner.get(0).getCode();
//			edt_Mannerservice.setTag(manner_code);

			mannerofserviceCode = new ArrayList<>();

			for (int i = 0; i < listofManner.size(); i++) {
				MannerOfService manner = listofManner.get(i);
				mannerofservice.add(manner.getTitle());
				mannerofserviceCode.add(manner.getCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void intialize() {

		//Manner_of_services


		// weight_wheel
		wheelMenuWeight = new ArrayList<String>();
		wheelMenuWeight.add("Under 100 Lbs");
		wheelMenuWeight.add("100-120 Lbs");
		wheelMenuWeight.add("121-140 Lbs");
		wheelMenuWeight.add("141-160 Lbs");
		wheelMenuWeight.add("161-180 Lbs");
		wheelMenuWeight.add("181-200 Lbs");
		wheelMenuWeight.add("201-220 Lbs");
		wheelMenuWeight.add("221-240 Lbs");
		wheelMenuWeight.add("241-260 Lbs");
		wheelMenuWeight.add("261-280 Lbs");
		wheelMenuWeight.add("281-300 Lbs");
		wheelMenuWeight.add("Over 300 Lbs");

		// Height_wheel
		wheelMenuHeight = new ArrayList<String>();
		wheelMenuHeight.add("Under 4 Feet");
		wheelMenuHeight.add("4'0 - 4'6");
		wheelMenuHeight.add("4'7 - 5'0");
		wheelMenuHeight.add("5'1 - 5'6");
		wheelMenuHeight.add("5'7 - 6'0");
		wheelMenuHeight.add("6'1 - 6'6");
		wheelMenuHeight.add("Over 6'6");
		wheelMenuHeight.add("Seated");

		// Race_wheel
		wheelMenuRace = new ArrayList<String>();
		wheelMenuRace.add("White");
		wheelMenuRace.add("African American");
		wheelMenuRace.add("Latino");
		wheelMenuRace.add("Asian");
		wheelMenuRace.add("Middle Eastern");
		wheelMenuRace.add("Native American");
		wheelMenuRace.add("Other");

		// Hair_wheel
		wheelMenuHair = new ArrayList<String>();
		wheelMenuHair.add("Black");
		wheelMenuHair.add("Brown");
		wheelMenuHair.add("Blond");
		wheelMenuHair.add("Auburn");
		wheelMenuHair.add("Chestnut");
		wheelMenuHair.add("Red");
		wheelMenuHair.add("Gray/White");
		wheelMenuHair.add("Bald");

		// Eye _wheel
		wheelMenuEye = new ArrayList<String>();
		wheelMenuEye.add("Brown");
		wheelMenuEye.add("Hazel");
		wheelMenuEye.add("Green");
		wheelMenuEye.add("Grey");
		wheelMenuEye.add("Blue");
		wheelMenuEye.add("Amber");

		// Age _wheel
		wheelMenuAge = new ArrayList<String>();
		wheelMenuAge.add("Under 18");
		wheelMenuAge.add("18 - 25");
		wheelMenuAge.add("26 - 30");
		wheelMenuAge.add("31 - 35");
		wheelMenuAge.add("36 - 40");
		wheelMenuAge.add("41 - 45");
		wheelMenuAge.add("46 - 50");
		wheelMenuAge.add("51 - 55");
		wheelMenuAge.add("56 - 60");
		wheelMenuAge.add("Over 60");

		// relationship _wheel
		relationship = new ArrayList<>();
		relationship.add("Administrative Assistant");
		relationship.add("Administrator");
		relationship.add("Agent for Service");
		relationship.add("Assistant Manager");
		relationship.add("Co-Resident");
		relationship.add("Corporate Officer");
		relationship.add("Custodian of Record");
		relationship.add("Director");
		relationship.add("Employee");
		relationship.add("GATE GUARD");
		relationship.add("Legal Assistant");
		relationship.add("Liason Officer");
		relationship.add("Manager");
		relationship.add("Member Services Rep");
		relationship.add("Owner");
		relationship.add("Parent and/or Guardian");
		relationship.add("Payroll Department");
		relationship.add("Person-In-Charge");
		relationship.add("President");
		relationship.add("Process Specialist");
		relationship.add("Receptionist");
		relationship.add("REGISTERED AGENT, pursuant to NRS 14.020");
		relationship.add("Sibling");
		relationship.add("Spouse");


	}

	private void Final_Status_dialogmanner() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Manner of Service");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, mannerofservice);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_mannerofService.getText().toString()), true);

		final String[] select = new String[1];
		final String[] selectmanner = new String[1];
		if (FS_mannerofService.getText().toString().length() != 0) {
			select[0] = FS_mannerofService.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
				selectmanner[0] = mannerofserviceCode.get(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_mannerofService.setText(select[0]);
				FS_mannerofService.setTag(selectmanner[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	private void dialogweight() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Weight");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuWeight);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_Weight.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_Weight.getText().toString().length() != 0) {
			select[0] = FS_Weight.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_Weight.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});


		dialog.show();
	}

	private void dialogheight() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Height");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuHeight);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_Height.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_Height.getText().toString().length() != 0) {
			select[0] = FS_Height.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_Height.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}


	private void dialograce() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Race");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuRace);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_skin.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_skin.getText().toString().length() != 0) {
			select[0] = FS_skin.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_skin.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	private void dialoghair() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Hair");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuHair);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_hair.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_hair.getText().toString().length() != 0) {
			select[0] = FS_hair.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_hair.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	private void dialogeye() {
		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Eye");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuEye);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_Eye.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_Eye.getText().toString().length() != 0) {
			select[0] = FS_Eye.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_Eye.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});


		dialog.show();

	}


	private void dialogAge() {

		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Age");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, wheelMenuAge);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_age.getText().toString()), true);

		final String[] select = new String[1];
		if (FS_age.getText().toString().length() != 0) {
			select[0] = FS_age.getText().toString();
		}

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FS_age.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	private void dialogrelationship() {

		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Relationship");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, relationship);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(FS_relationship.getText().toString()), true);

		final String[] select = new String[1];
		//	final String[] selectmanner = new String[1];
		if (FS_relationship.getText().toString().length() != 0) {
			select[0] = FS_relationship.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
				//selectmanner[0]=mannerofserviceCode.get(position);
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				FS_relationship.setText(select[0]);
				//edt_relatioship.setTag(selectmanner[0]);
				dialog.dismiss();

			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}


	@SuppressLint("SetTextI18n")
	private void Dialog(final int index) {
		// TODO Auto-generated method stub
		item = jobList.get(index);
		try {

			if (item instanceof ProcessAddressForServer) {
				final ProcessAddressForServer address = (ProcessAddressForServer) item;

				final Dialog dialog = new Dialog(JobsQueue.this);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.job_queue_final_status);

				ImageView close = (ImageView) dialog.findViewById(R.id.close);
				ImageView upload = (ImageView) dialog.findViewById(R.id.upload);

				close.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				upload.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						initializeUpload();

						dialog.dismiss();

					}

					private void initializeUpload() {
						// TODO Auto-generated method stub

						new AsyncTask<Void, Void, Void>() {
							// String errorString = null;
							protected void onPreExecute() {
								ProgressBar.showCommonProgressDialog(
										JobsQueue.this, "Uploading...");

							}

							@Override
							protected Void doInBackground(Void... params) {

								try {
									// ProcessAddressForServer processOrderList
									// = new ProcessAddressForServer>();
									// processOrderList =
									// database.getFinalStatusToUploadToServer();
									String sessionId = WebServiceConsumer
											.getInstance()
											.signOn(TristarConstants.SOAP_ADDRESS,
													SessionData.getInstance()
															.getUsername(),
													SessionData.getInstance()
															.getPassword());

									SessionData.getInstance().setSubfinalStatus(1);
									serverCode = SubmitWebServiceConsumer
											.getInstance().SubmitFinalStatus(
													sessionId, address);

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// errorString = initialUploadWebServiceCall();

								return null;
							}

							// private String initialUploadWebServiceCall() {
							// // TODO Auto-generated method stub
							// String errorString = null;
							// ArrayList<Object> jobList = new
							// ArrayList<Object>();
							// {
							// ArrayList<ProcessAddressForServer> list =
							// database
							// .getFinalStatusToSubmit();
							// if (list != null && list.size() > 0) {
							// jobList.addAll(list);
							// }
							// }
							// return errorString;
							// }

							@Override
							protected void onPostExecute(Void result) {
								ProgressBar.dismiss();

								if (isServerCodeAccepted(serverCode)) {

									Log.d("Server Code", "" + serverCode);
									database.deleteFinalStausTableAfterSync(address
											.getLineItem());
									try {
										database.deleteFinalStatusAttachementTable(address
												.getLineItem());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									jobList.remove(item);
									if (jobList.get(index - 1) instanceof String) {
										if (jobList.size() == index
												|| jobList.get(index) instanceof String)
											jobList.remove(index - 1);
									}
									JobAdapter adapter = new JobAdapter();
									adapter.notifyDataSetChanged();
									listView.setAdapter(adapter);
									if (submitImage.size() == 0) {
										new CustomAlertDialog(JobsQueue.this,
												"Uploaded Successfully").show();
										database.DeleteFinalStatusAfteruploadTableBy(address.getAddressLineItem());
										// listCategory.initialSync();
									} else {
										uploadImage();
									}

								} else {
									new CustomAlertDialog(JobsQueue.this,
											"Invoking initial sync failed with error code : "
													+ serverCode).show();
								}

								// if (errorString != null) {
								// database.deleteCategoryReleatedTableInDB();
								// new CustomAlertDialog(JobsQueue.this,
								// "Invoking initial sync failed with error code : "
								// + errorString).show();
								// }
								//

								// final Dialog dialog = new
								// Dialog(JobsQueue.this);
								// dialog.getWindow().setBackgroundDrawable(
								// new ColorDrawable(Color.TRANSPARENT));
								// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								// dialog.setContentView(R.layout.alert_upload_final_status);
								// ok = (Button) findViewById(R.id.btn_ok);
								// ok.setOnClickListener(new OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// //deleteRecordFromTable(position);
								// }
								// });
								//
								super.onPostExecute(result);
							}

							private void uploadImage() {

								new AsyncTask<Void, Void, Void>() {
									// String errorString = null;
									protected void onPreExecute() {
										ProgressBar.showCommonProgressDialog(
												JobsQueue.this, "Uploading...");

									}

									@Override
									protected Void doInBackground(
											Void... params) {

										try {
											// ProcessAddressForServer
											// processOrderList
											// = new ProcessAddressForServer>();
											// processOrderList =
											// database.getFinalStatusToUploadToServer();
											for (SubmitDiligence submitDiligence : submitImage) {

												Log.d("attachment upload", ""
														+ submitImage.size());
												String sessionID = WebServiceConsumer
														.getInstance()
														.signOn(TristarConstants.SOAP_ADDRESS,
																SessionData
																		.getInstance()
																		.getUsername(),
																SessionData
																		.getInstance()
																		.getPassword());
												serverCode = SubmitWebServiceConsumer.getInstance()
														.SubmitFinalAttachments(sessionID, submitDiligence);

											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										// errorString =
										// initialUploadWebServiceCall();

										return null;
									}

									// private String
									// initialUploadWebServiceCall() {
									// // TODO Auto-generated method stub
									// String errorString = null;
									// ArrayList<Object> jobList = new
									// ArrayList<Object>();
									// {
									// ArrayList<ProcessAddressForServer> list =
									// database
									// .getFinalStatusToSubmit();
									// if (list != null && list.size() > 0) {
									// jobList.addAll(list);
									// }
									// }
									// return errorString;
									// }

									@Override
									protected void onPostExecute(Void result) {
										ProgressBar.dismiss();

										if (isServerCodeAccepted(serverCode)) {
											database.deletesFinalStatementAttachment(address
													.getLineItem());
											database.DeleteFinalStatusAfteruploadTableBy(address.
													getAddressLineItem());


											new CustomAlertDialog(
													JobsQueue.this,
													"Uploaded Successfully")
													.show();

											submitImage.clear();

											// listCategory.initialSync();
										} else {

											new CustomAlertDialog(
													JobsQueue.this,
													"Invoking initial sync failed with error code : "
															+ serverCode)
													.show();
										}

										// if (errorString != null) {
										// database.deleteCategoryReleatedTableInDB();
										// new CustomAlertDialog(JobsQueue.this,
										// "Invoking initial sync failed with error code : "
										// + errorString).show();
										// }
										//

										// final Dialog dialog = new
										// Dialog(JobsQueue.this);
										// dialog.getWindow().setBackgroundDrawable(
										// new
										// ColorDrawable(Color.TRANSPARENT));
										// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
										// dialog.setContentView(R.layout.alert_upload_final_status);
										// ok = (Button)
										// findViewById(R.id.btn_ok);
										// ok.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										// //deleteRecordFromTable(position);
										// }
										// });
										//
										super.onPostExecute(result);
									}
								}.execute();
							}

						}.execute();

					}
				});

				FS_workorder = (TextView) dialog
						.findViewById(R.id.final_workorder);

				FS_workorder.setText(address.getWorkorder());

				FS_Entity = (CheckBox) dialog
						.findViewById(R.id.finalstatus_entity);

				if (address.isEntity()) {
					FS_Entity.setChecked(true);
				} else {
					FS_Entity.setChecked(false);
				}

				FS_service = (TextView) dialog
						.findViewById(R.id.finalstatus_service);

				FS_service.setText(address.getAuthorizedAgent());

				FS_name = (TextView) dialog
						.findViewById(R.id.finalstatus_name);

				FS_name.setText(address.getServee());

				FS_title = (TextView) dialog
						.findViewById(R.id.finalstatus_Title);

				FS_title.setText(address.getAuthorizedAgentTitle());

				FS_txtaddress = (TextView) dialog
						.findViewById(R.id.finalstatus_address);

				processOrderListArray = database
						.getprocessOrderValuesFromTabletoDisplayInListView();
				Log.d("Address", "" + processOrderListArray.size());
				for (int i = 0; i < processOrderListArray.size(); i++) {
					Log.d("Address", ""
							+ processOrderListArray.get(i).getAddressLineItem());
					if (processOrderListArray.get(i).getAddressLineItem() == address
							.getAddressLineItem()) {
						FS_txtaddress.setText(processOrderListArray.get(i)
								.getAddressFormattedForDisplay());
					}

				}

				Log.d("Addressssss", "" + address.getAddressLineItem());

				FS_mannerofService = (TextView) dialog
						.findViewById(R.id.finalstatus_mannerofservice);

				if (address.getMannerofServicecode().contains("A")) {
					FS_mannerofService.setText("Personal Service");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("B")) {
					FS_mannerofService.setText("Substituted Service");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("C")) {
					FS_mannerofService.setText("Was Not Served");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("D")) {
					FS_mannerofService.setText("Mail And Acknowledgment");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("E")) {
					FS_mannerofService.setText("By Other Means");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("F")) {
					FS_mannerofService.setText("Posting");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("G")) {
					FS_mannerofService.setText("By Mail");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("H")) {
					FS_mannerofService.setText("By EMail");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("I")) {
					FS_mannerofService.setText("OverNight Delivery");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("J")) {
					FS_mannerofService.setText("Messenger");
					FS_mannerofService.setTag(address.getMannerofServicecode());

				} else if (address.getMannerofServicecode().contains("K")) {
					FS_mannerofService.setText("Fax");
					FS_mannerofService.setTag(address.getMannerofServicecode());
				}

				FS_leftWith = (EditText) dialog
						.findViewById(R.id.finalstatus_leftwith);

				FS_leftWith.setText(address.getLeftWith());

				FS_relationship = (EditText) dialog
						.findViewById(R.id.finalstatus_relationship);
				FS_img_finalstatus_relationship = (ImageView) dialog
						.findViewById(R.id.img_finalstatus_relationship);

				FS_relationship.setText(address.getRelation());

				FS_servedDate = (TextView) dialog
						.findViewById(R.id.finalstatus_date);

				FS_servedDate.setText(address.getServeDate());

				FS_servedTime = (TextView) dialog
						.findViewById(R.id.finalstatus_time);

				FS_servedTime.setText(address.getServeTime());

				FS_male = (RadioButton) dialog.findViewById(R.id.male);
				FS_female = (RadioButton) dialog
						.findViewById(R.id.female);

				if (address.isServeeIsMale()) {

					FS_male.setChecked(true);
					FS_female.setChecked(false);

				} else {

					FS_male.setChecked(false);
					FS_female.setChecked(true);
				}


				FS_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked){
							FS_male.setChecked(true);
							FS_female.setChecked(false);
						}
					}
				});
				FS_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked){
							FS_male.setChecked(false);
							FS_female.setChecked(true);
						}
					}
				});

				FS_inuniform = (CheckBox) dialog
						.findViewById(R.id.finalstatus_in_uniform);

				if (address.isInuniform()) {

					FS_inuniform.setChecked(true);
				} else {
					FS_inuniform.setChecked(false);

				}

				FS_military = (CheckBox) dialog
						.findViewById(R.id.finalstatus_military);

				if (address.isMilitary()) {

					FS_military.setChecked(true);
				} else {
					FS_military.setChecked(false);

				}

				FS_police = (CheckBox) dialog
						.findViewById(R.id.finalstatus_police);

				if (address.isPolice()) {

					FS_police.setChecked(true);
				} else {
					FS_police.setChecked(false);

				}

				FS_age = (TextView) dialog
						.findViewById(R.id.finalstatus_age);

				FS_age.setText(address.getAge());

				FS_hair = (TextView) dialog
						.findViewById(R.id.finalstatus_hair);

				FS_hair.setText(address.getHair());

				FS_skin = (TextView) dialog
						.findViewById(R.id.finalstatus_race);

				FS_skin.setText(address.getSkin());

				FS_Weight = (TextView) dialog
						.findViewById(R.id.finalstatus_weight);

				FS_Weight.setText(address.getWeight());

				FS_Height = (TextView) dialog
						.findViewById(R.id.finalstatus_height);

				FS_Height.setText(address.getHeight());

				FS_Eye = (TextView) dialog
						.findViewById(R.id.finalstatus_eye);

				FS_Eye.setText(address.getEyes());

				FS_distingusihing = (EditText) dialog
						.findViewById(R.id.finalstatus_marks);

				FS_distingusihing.setText(address.getMarks());

				FS_reports = (EditText) dialog
						.findViewById(R.id.finalstatus_report);

				FS_reports.setText(address.getReport());

				FS_gps = (TextView) dialog
						.findViewById(R.id.finalstatus_gps);

				FS_signature = (TextView) dialog
						.findViewById(R.id.finalstatus_signature);

				FS_layout_manner_of_services = (LinearLayout) dialog.findViewById(R.id.layout_manner_of_services);
				FS_layout_relationship = (LinearLayout) dialog.findViewById(R.id.layout_relationship);
				FS_layout_age = (LinearLayout) dialog.findViewById(R.id.layout_age);
				FS_layout_hair = (LinearLayout) dialog.findViewById(R.id.layout_hair);
				FS_layout_race = (LinearLayout) dialog.findViewById(R.id.layout_race);
				FS_layout_weight = (LinearLayout) dialog.findViewById(R.id.layout_weight);
				FS_layout_height = (LinearLayout) dialog.findViewById(R.id.layout_height);
				FS_layout_eye = (LinearLayout) dialog.findViewById(R.id.layout_eye);
				FS_btn_save = (Button) dialog.findViewById(R.id.btn_save);

				FS_servedDate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedTextView = (TextView) v;
						showDialog(DATE_DIALOG_ID);
					}
				});

				FS_servedTime.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.timepicker_dialog);
						final TimePicker tp = (TimePicker) dialog.findViewById(R.id.tp);
						Button set = (Button) dialog.findViewById(R.id.btn_set);
						Button cancel = (Button) dialog.findViewById(R.id.btn_calcel);
						final Calendar c = Calendar.getInstance();

						SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
						String formattedTime = dt.format(c.getTime());
						//btnTime.setText(formattedTime.toLowerCase());
						final String[] time = {formattedTime.toLowerCase()};
						tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
							@Override
							public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
								//Display the new time to app interface
								String AMPM = "am";
								if (hourOfDay > 11) {
									hourOfDay = hourOfDay - 12;
									AMPM = "pm";
								}
								String hr = "";
								if (Integer.toString(hourOfDay).length() == 1) {
									hr = "0" + Integer.toString(hourOfDay);
								} else {
									hr = Integer.toString(hourOfDay);
								}

								String min = "";
								if (Integer.toString(minute).length() == 1) {
									min = "0" + Integer.toString(minute);
								} else {
									min = Integer.toString(minute);
								}


								time[0] = "" + hr + ":" + min + " " + AMPM;

							}
						});
						set.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								FS_servedTime.setText(time[0]);
								dialog.dismiss();

							}
						});
						cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
						dialog.show();
					}
				});

				FS_layout_manner_of_services.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						getFinal_Staus_Manner_Value(address.getProcessOrderID());
						Final_Status_dialogmanner();
					}
				});
				FS_img_finalstatus_relationship.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialogrelationship();
					}
				});

				FS_layout_age.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialogAge();
					}
				});

				FS_layout_hair.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialoghair();
					}
				});

				FS_layout_race.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialograce();
					}
				});

				FS_layout_weight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialogweight();
					}
				});

				FS_layout_height.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialogheight();
					}
				});

				FS_layout_eye.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intialize();
						dialogeye();
					}
				});

				FS_gps.setText(address.getLatitude() + ", "
						+ address.getLongitude());


				FS_btn_save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {


						String Str_FS_workorder = FS_workorder.getText().toString();
						boolean b_FS_Entity = FS_Entity.isChecked();
						String Str_FS_service = FS_service.getText().toString();
						String Str_FS_name = FS_name.getText().toString();
						String Str_FS_title = FS_title.getText().toString();
						String Str_FS_txtaddress = FS_txtaddress.getText().toString();
						String Str_FS_mannerofService = FS_mannerofService.getTag().toString();
						String Str_FS_leftWith = FS_leftWith.getText().toString();
						String Str_FS_relationship = FS_relationship.getText().toString();
						String Str_FS_servedDate = FS_servedDate.getText().toString();
						String Str_FS_servedTime = FS_servedTime.getText().toString();
						boolean b_FS_male = FS_male.isChecked();
						boolean b_FS_female = FS_female.isChecked();
						boolean b_FS_inuniform = FS_inuniform.isChecked();
						boolean b_FS_military = FS_military.isChecked();
						boolean b_FS_police = FS_police.isChecked();
						String Str_FS_age = FS_age.getText().toString();
						String Str_FS_hair = FS_hair.getText().toString();
						String Str_FS_skin = FS_skin.getText().toString();
						String Str_FS_Weight = FS_Weight.getText().toString();
						String Str_FS_Height = FS_Height.getText().toString();
						String Str_FS_Eye = FS_Eye.getText().toString();
						String Str_FS_distingusihing = FS_distingusihing.getText().toString();
						String Str_FS_reports = FS_reports.getText().toString();
						String Str_FS_gps = FS_gps.getText().toString();
						String Str_FS_signature = FS_signature.getText().toString();


						SubmitFinalStatus submitFinalStatusToDB = new SubmitFinalStatus();
						submitFinalStatusToDB.setWorkorder(Str_FS_workorder);

						for (int i = 0; i < processOrderListArray.size(); i++) {
							Log.d("Address", ""
									+ processOrderListArray.get(i).getAddressLineItem());
							if (processOrderListArray.get(i).getAddressLineItem() == address
									.getAddressLineItem()) {
								submitFinalStatusToDB.setAddressLineitem(address
										.getAddressLineItem());
							}
						}
						submitFinalStatusToDB.setServee(Str_FS_name);
						submitFinalStatusToDB.setEntity(b_FS_Entity);
						submitFinalStatusToDB.setAuthorizedAgentTitle(Str_FS_title);
						submitFinalStatusToDB.setAuthorizedAgent(Str_FS_service);
						submitFinalStatusToDB.setInUniform(b_FS_inuniform);
						submitFinalStatusToDB.setmilitary(b_FS_military);
						submitFinalStatusToDB.setPolice(b_FS_police);

						if (b_FS_male) {
							submitFinalStatusToDB.setServerisMale(true);
						}
						if (b_FS_female) {
							submitFinalStatusToDB.setServerisMale(false);
						}

						submitFinalStatusToDB.setAge(Str_FS_age);
						submitFinalStatusToDB.setHair(Str_FS_hair);
						submitFinalStatusToDB.setSkin(Str_FS_skin);
						submitFinalStatusToDB.setWeight(Str_FS_Weight);
						submitFinalStatusToDB.setHeight(Str_FS_Height);
						submitFinalStatusToDB.setEyes(Str_FS_Eye);
						submitFinalStatusToDB.setMarks(Str_FS_distingusihing);
						submitFinalStatusToDB.setMannerOfServiceCode(Str_FS_mannerofService);
						submitFinalStatusToDB.setLeftWith(Str_FS_leftWith);
						submitFinalStatusToDB.setRelationship(Str_FS_relationship);
						submitFinalStatusToDB.setServeDate(Str_FS_servedDate);
						submitFinalStatusToDB.setLatitude(address.getLatitude());
						submitFinalStatusToDB.setLongitude(address.getLongitude());
						submitFinalStatusToDB.setReport(Str_FS_reports);
						submitFinalStatusToDB.setserveTime(Str_FS_servedTime);
						submitFinalStatusToDB.setFinalStatusLineItem(address.getLineItem());


						try {
							FS_is_updated = database.UpdateIntoFinalStatusTable(submitFinalStatusToDB);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (FS_is_updated) {
						//	Toast.makeText(JobsQueue.this, "Court Services is successfully updated", Toast.LENGTH_SHORT).show();
							Toast.makeText(JobsQueue.this, "Final Status is successfully updated", Toast.LENGTH_SHORT).show();
							processList();
							dialog.dismiss();

						} else {
							//Toast.makeText(JobsQueue.this, "Court Services update unsuccessfull ", Toast.LENGTH_SHORT).show();
							Toast.makeText(JobsQueue.this, "Final Status update unsuccessfull ", Toast.LENGTH_SHORT).show();
						}
					}
				});

				try {
					submitImage = database
							.getAttachementsFromFinalStatuseAttachmentTableForUploadForJobQueue(address
									.getLineItem());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TextView image = (TextView) dialog
						.findViewById(R.id.finalstatus_image);

				TextView audio = (TextView) dialog
						.findViewById(R.id.finalstatus_audio);

				attachedFilesData = new ArrayList<byte[]>();
				for (int i = 0; i < submitImage.size(); i++) {

					Log.d("", ""
							+ submitImage.get(i).getAttachementBase64String());
					if (submitImage.get(i).getAttachementsFileName()
							.contains("Audio")) {
						audio.setText(submitImage.get(i)
								.getAttachementsFileName());
						audiofile = (Base64.decode(submitImage.get(i)
								.getAttachementBase64String(), Base64.DEFAULT));
					}

					if (submitImage.get(i).getAttachementsFileName()
							.contains("signature")) {
						FS_signature.setText(submitImage.get(i)
								.getAttachementsFileName());
						audiofile = (Base64.decode(submitImage.get(i)
								.getAttachementBase64String(), Base64.DEFAULT));
					}
					if (submitImage.get(i).getAttachementsFileName()
							.contains("image")) {

						image.setText(submitImage.get(i)
								.getAttachementsFileName());
						attachedFilesData.add(Base64.decode(submitImage.get(i)
								.getAttachementBase64String(), Base64.DEFAULT));

						Log.d("submitImage", "" + attachedFilesData.toString());
					}
				}
				audio.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						playMp3(audiofile);

					}

				});

				FS_signature.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.job_queue_signature);
						ImageView close = (ImageView) dialog
								.findViewById(R.id.close);

						ImageView sign = (ImageView) dialog
								.findViewById(R.id.signature);
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 8;
						byte[] data = audiofile;
						Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
								data.length, options);

						sign.setImageBitmap(bmp);

						close.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						dialog.show();

					}

				});

				image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.job_queue_attachment);
						ImageView close = (ImageView) dialog
								.findViewById(R.id.close);

						try {
							submitImage = database
									.getAttachementsFromFinalStatuseAttachmentTableForUploadForJobQueue(address
											.getLineItem());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						GridView gridImage = (GridView) dialog
								.findViewById(R.id.lst_image_container);
						gridImage.setNumColumns(3);
						attachedFilesData = new ArrayList<byte[]>();
						for (int i = 0; i < submitImage.size(); i++) {

							Log.d("", ""
									+ submitImage.get(i)
									.getAttachementBase64String());

							if (submitImage.get(i).getAttachementsFileName()
									.contains("signature")) {

							} else {

								attachedFilesData.add(Base64.decode(submitImage
												.get(i).getAttachementBase64String(),
										Base64.DEFAULT));
							}
						}

						ImageListAdapter adapter = new ImageListAdapter();
						adapter.notifyDataSetChanged();
						gridImage.setAdapter(adapter);

						close.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						dialog.show();
					}
				});

				dialog.show();

			} else if (item instanceof SubmitStatusList) {
				final SubmitStatusList address = (SubmitStatusList) item;
				final SubmitStatusList addresssubmit = database
						.getSubmitCourtStatusFromDBForUploadingToServerSingle(address.getLineitem());
				final SubmitStatusList addresssubmits = database
						.getSubmitPickupStatusFromDBForUploadingToServerSingle(address.getLineitem());
				final SubmitStatusList addresssubmitss = database
						.getSubmitDileveryStatusFromDBForUploadingToServerSingle(address.getLineitem());
				if (addresssubmit != null) {
					final Dialog dialog = new Dialog(JobsQueue.this);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.job_queue_status);

					ImageView close = (ImageView) dialog.findViewById(R.id.close);
					ImageView upload = (ImageView) dialog.findViewById(R.id.upload);
					TextView Status = (TextView) dialog.findViewById(R.id.status);
					TextView Date = (TextView) dialog.findViewById(R.id.date);
					TextView Time = (TextView) dialog.findViewById(R.id.time);
					TextView WorkOrder = (TextView) dialog.findViewById(R.id.workorder);
					Status.setText(addresssubmit.getReport());
					Date.setText(addresssubmit.getStatusDate());
					Time.setText(addresssubmit.getStatusTime());
					WorkOrder.setText(addresssubmit.getWorkorder());
					close.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							initializeControlls();
							dialog.dismiss();
						}
					});
					upload.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							initializeControlls();
							dialog.dismiss();

						}

						private void initializeControlls() {
							// TODO Auto-generated method stub

							new AsyncTask<Void, Void, Void>() {
								// String errorString = null;
								protected void onPreExecute() {
									ProgressBar.showCommonProgressDialog(
											JobsQueue.this, "Uploading...");

								}


								@Override
								protected Void doInBackground(Void... params) {

									try {
										// ProcessAddressForServer processOrderList
										// = new ProcessAddressForServer>();
										// processOrderList =
										// database.getFinalStatusToUploadToServer();
										String sessionId = WebServiceConsumer
												.getInstance()
												.signOn(TristarConstants.SOAP_ADDRESS,
														SessionData.getInstance()
																.getUsername(),
														SessionData.getInstance()
																.getPassword());

										serverCode = SubmitWebServiceConsumer
												.getInstance().SubmitStatusDirect(
														sessionId, addresssubmit);

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// errorString = initialUploadWebServiceCall();

									return null;
								}


								@Override
								protected void onPostExecute(Void result) {
									ProgressBar.dismiss();

									if (isServerCodeAccepted(serverCode)) {

										try {
											database.deleteSubmitStatus(addresssubmit.getLineitem());
											database.updateSubmitStatusTable(addresssubmit.getLineitem());
											database.deleteSubmitPickupStatus(addresssubmit.getLineitem());
											database.updateSubmitPickupStatusTable(addresssubmit.getLineitem());
											database.deleteSubmitDeliveryStatus(addresssubmit.getLineitem());
											database.updateSubmitDileveryStatusTable(addresssubmit.getLineitem());
											database.setKdeleteSubmitpickupstatuslist(addresssubmit);
										} catch (Exception e) {
											e.printStackTrace();
										}


										jobList.remove(item);
										if (jobList.get(index - 1) instanceof String) {
											if (jobList.size() == index
													|| jobList.get(index) instanceof String)
												jobList.remove(index - 1);
										}
										JobAdapter adapter = new JobAdapter();
										adapter.notifyDataSetChanged();
										listView.setAdapter(adapter);

									} else {
										new CustomAlertDialog(JobsQueue.this,
												"Invoking initial sync failed with error code : "
														+ serverCode).show();
									}
									super.onPostExecute(result);
								}


							}.execute();

						}
					});


					dialog.show();
				}
				if (addresssubmits != null) {
					final Dialog dialog = new Dialog(JobsQueue.this);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.job_queue_status);

					ImageView close = (ImageView) dialog.findViewById(R.id.close);
					ImageView upload = (ImageView) dialog.findViewById(R.id.upload);
					TextView Status = (TextView) dialog.findViewById(R.id.status);
					TextView Date = (TextView) dialog.findViewById(R.id.date);
					TextView Time = (TextView) dialog.findViewById(R.id.time);
					TextView WorkOrder = (TextView) dialog.findViewById(R.id.workorder);
					Status.setText(addresssubmits.getReport());
					Date.setText(addresssubmits.getStatusDate());
					Time.setText(addresssubmits.getStatusTime());
					WorkOrder.setText(addresssubmits.getWorkorder());

					close.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					upload.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							initializeControlls();
							dialog.dismiss();

						}

						private void initializeControlls() {
							// TODO Auto-generated method stub

							new AsyncTask<Void, Void, Void>() {
								// String errorString = null;
								protected void onPreExecute() {
									ProgressBar.showCommonProgressDialog(
											JobsQueue.this, "Uploading...");
								}

								@Override
								protected Void doInBackground(Void... params) {

									try {
										// ProcessAddressForServer processOrderList
										// = new ProcessAddressForServer>();
										// processOrderList =
										// database.getFinalStatusToUploadToServer();
										String sessionId = WebServiceConsumer
												.getInstance()
												.signOn(TristarConstants.SOAP_ADDRESS,
														SessionData.getInstance()
																.getUsername(),
														SessionData.getInstance()
																.getPassword());

										serverCode = SubmitWebServiceConsumer
												.getInstance().SubmitStatusDirect(
														sessionId, addresssubmits);

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// errorString = initialUploadWebServiceCall();

									return null;
								}


								@Override
								protected void onPostExecute(Void result) {
									ProgressBar.dismiss();

									if (isServerCodeAccepted(serverCode)) {

										try {
											database.deleteSubmitStatus(addresssubmits.getLineitem());
											database.updateSubmitStatusTable(addresssubmits.getLineitem());
											database.deleteSubmitPickupStatus(addresssubmits.getLineitem());
											database.updateSubmitPickupStatusTable(addresssubmits.getLineitem());
											database.deleteSubmitDeliveryStatus(addresssubmits.getLineitem());
											database.updateSubmitDileveryStatusTable(addresssubmits.getLineitem());
											database.setKdeleteSubmitpickupstatuslist(addresssubmits);
										} catch (Exception e) {
											e.printStackTrace();
										}


										jobList.remove(item);
										if (jobList.get(index - 1) instanceof String) {
											if (jobList.size() == index
													|| jobList.get(index) instanceof String)
												jobList.remove(index - 1);
										}
										JobAdapter adapter = new JobAdapter();
										adapter.notifyDataSetChanged();
										listView.setAdapter(adapter);

									} else {
										new CustomAlertDialog(JobsQueue.this,
												"Invoking initial sync failed with error code : "
														+ serverCode).show();
									}
									super.onPostExecute(result);
								}


							}.execute();

						}
					});


					dialog.show();
				}


				if (addresssubmitss != null) {
					final Dialog dialog = new Dialog(JobsQueue.this);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.job_queue_status);

					ImageView close = (ImageView) dialog.findViewById(R.id.close);
					ImageView upload = (ImageView) dialog.findViewById(R.id.upload);
					TextView Status = (TextView) dialog.findViewById(R.id.status);
					TextView Date = (TextView) dialog.findViewById(R.id.date);
					TextView Time = (TextView) dialog.findViewById(R.id.time);
					TextView WorkOrder = (TextView) dialog.findViewById(R.id.workorder);
					Status.setText(addresssubmitss.getReport());
					Date.setText(addresssubmitss.getStatusDate());
					Time.setText(addresssubmitss.getStatusTime());
					WorkOrder.setText(addresssubmitss.getWorkorder());
					close.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					upload.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							initializeControlls();
							dialog.dismiss();

						}

						private void initializeControlls() {
							// TODO Auto-generated method stub

							new AsyncTask<Void, Void, Void>() {
								// String errorString = null;
								protected void onPreExecute() {
									ProgressBar.showCommonProgressDialog(
											JobsQueue.this, "Uploading...");

								}

								@Override
								protected Void doInBackground(Void... params) {

									try {
										// ProcessAddressForServer processOrderList
										// = new ProcessAddressForServer>();
										// processOrderList =
										// database.getFinalStatusToUploadToServer();
										String sessionId = WebServiceConsumer
												.getInstance()
												.signOn(TristarConstants.SOAP_ADDRESS,
														SessionData.getInstance()
																.getUsername(),
														SessionData.getInstance()
																.getPassword());

										serverCode = SubmitWebServiceConsumer
												.getInstance().SubmitStatusDirect(
														sessionId, addresssubmitss);

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// errorString = initialUploadWebServiceCall();

									return null;
								}


								@Override
								protected void onPostExecute(Void result) {
									ProgressBar.dismiss();

									if (isServerCodeAccepted(serverCode)) {

										try {
											database.deleteSubmitStatus(addresssubmitss.getLineitem());
											database.updateSubmitStatusTable(addresssubmitss.getLineitem());
											database.deleteSubmitPickupStatus(addresssubmitss.getLineitem());
											database.updateSubmitPickupStatusTable(addresssubmitss.getLineitem());
											database.deleteSubmitDeliveryStatus(addresssubmitss.getLineitem());
											database.updateSubmitDileveryStatusTable(addresssubmitss.getLineitem());
											//	database.setKdeleteSubmitpickupstatuslist(addresssubmitss);
										} catch (Exception e) {
											e.printStackTrace();
										}


										jobList.remove(item);
										if (jobList.get(index - 1) instanceof String) {
											if (jobList.size() == index
													|| jobList.get(index) instanceof String)
												jobList.remove(index - 1);
										}
										JobAdapter adapter = new JobAdapter();
										adapter.notifyDataSetChanged();
										listView.setAdapter(adapter);

									} else {
										new CustomAlertDialog(JobsQueue.this,
												"Invoking initial sync failed with error code : "
														+ serverCode).show();
									}
									super.onPostExecute(result);
								}


							}.execute();

						}
					});
					dialog.show();
				}

			}

//			else if (item instanceof SubmitStatusList) {
//				final SubmitStatusList address = (SubmitStatusList) item;
//
//
//
//			}
//
//			else if (item instanceof SubmitStatusList) {
//				final SubmitStatusList address = (SubmitStatusList) item;
//
//			}

			else if (item instanceof CourtAddressForServer) {
				final CourtAddressForServer address = (CourtAddressForServer) item;
				final SubmitCourtPOD addresssubmit = database
						.getSubmitCourtPODValuesFromDBForUploadingToServerSingle(address.getWorkorder());

				courtpodImage = database.getAttachementsFromSubmitCourtPODAttachmentTableForUploadSingle(address.getWorkorder());
				final Dialog dialog = new Dialog(JobsQueue.this);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.job_queue_court_pod);

				ImageView close = (ImageView) dialog.findViewById(R.id.close);
				ImageView upload = (ImageView) dialog.findViewById(R.id.upload);

				courtServiceListArray = database
						.getcourtOrderValuesFromTabletoDisplayInListView();

				CS_name = (TextView) dialog
						.findViewById(R.id.coutpod_name);

				CS_addresss = (TextView) dialog
						.findViewById(R.id.courtpod_address);


				CS_workorder = (TextView) dialog
						.findViewById(R.id.courtpod_workorder);

				CS_workorder.setText(address.getWorkorder());

				CS_duedate = (TextView) dialog
						.findViewById(R.id.courtpod_duedate);

				CS_priority = (TextView) dialog
						.findViewById(R.id.courtpod_priority);

				CS_instructions = (TextView) dialog
						.findViewById(R.id.courtpod_instructions);

				CS_documents = (TextView) dialog
						.findViewById(R.id.courtpod_documents);

				CS_date = (TextView) dialog
						.findViewById(R.id.courtpod_date);

				CS_date.setText(address.getDate());

				CS_Time = (TextView) dialog
						.findViewById(R.id.courtpod_time);

				CS_chk_time = (CheckBox) dialog
						.findViewById(R.id.chk_time);

				CS_Time.setText(address.getTime());

				CS_comments = (EditText) dialog
						.findViewById(R.id.courtpod_comments);

				CS_comments.setText(address.getComments());

				CS_feeadvance = (EditText) dialog
						.findViewById(R.id.courtpod_fee_advance);

				CS_casename = (TextView) dialog.findViewById(R.id.courtpod_casename);


				CS_caseNumber = (TextView) dialog.findViewById(R.id.courtpod_casenumber);


				CS_feeadvance.setText(address.getFeeAdvance());

				CS_weight = (EditText) dialog
						.findViewById(R.id.courtpod_weight);

				CS_weight.setText(address.getWeight());

				CS_pieces = (EditText) dialog
						.findViewById(R.id.courtpod_pieces);

				CS_pieces.setText(address.getPieces());

				CS_waittime = (EditText) dialog
						.findViewById(R.id.courtpod_wait_time);

				CS_waittime.setText(address.getWaitTime());

				CS_image = (TextView) dialog
						.findViewById(R.id.courtpod_image);


				CS_btn_save = (Button) dialog
						.findViewById(R.id.btn_save);

				for (int i = 0; i < courtServiceListArray.size(); i++) {
					if (courtServiceListArray.get(i).getWorkorder()
							.equals(address.getWorkorder())) {
						CS_name.setText(courtServiceListArray.get(i).getName());
						CS_addresss.setText(courtServiceListArray.get(i)
								.getAddressFormattedForDisplay());
						CS_duedate.setText(courtServiceListArray.get(i)
								.getDueDate());
						CS_priority.setText(courtServiceListArray.get(i)
								.getPriorityTitle());
						CS_instructions.setText(courtServiceListArray.get(i)
								.getInstructions());
						CS_documents.setText(courtServiceListArray.get(i)
								.getDocuments());
						CS_casename.setText(courtServiceListArray.get(i).getCaseName());
						CS_caseNumber.setText(courtServiceListArray.get(i).getCaseNumber());
					}
				}

				CS_date.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedTextView = (TextView) v;
						showDialog(DATE_DIALOG_ID);
					}
				});

				CS_Time.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

//						if (CS_chk_time.isChecked()) {
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.timepicker_dialog);
						final TimePicker tp = (TimePicker) dialog.findViewById(R.id.tp);
						Button set = (Button) dialog.findViewById(R.id.btn_set);
						Button cancel = (Button) dialog.findViewById(R.id.btn_calcel);
						final Calendar c = Calendar.getInstance();
						SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
						String formattedTime = dt.format(c.getTime());
						//btnTime.setText(formattedTime.toLowerCase());
						final String[] time = {formattedTime.toLowerCase()};
						tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
							@Override
							public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
								//Display the new time to app interface
								String AMPM = "am";
								if (hourOfDay > 11) {
									hourOfDay = hourOfDay - 12;
									AMPM = "pm";
								}
								String hr = "";
								if (Integer.toString(hourOfDay).length() == 1) {
									hr = "0" + Integer.toString(hourOfDay);
								} else {
									hr = Integer.toString(hourOfDay);
								}

								String min = "";
								if (Integer.toString(minute).length() == 1) {
									min = "0" + Integer.toString(minute);
								} else {
									min = Integer.toString(minute);
								}

								time[0] = "" + hr + ":" + min + " " + AMPM;

							}
						});

						set.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								CS_Time.setText(time[0]);
								dialog.dismiss();

							}
						});

						cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
						dialog.show();
					}
//					}
				});

				CS_btn_save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						String Str_CS_name = CS_name.getText().toString();
						String Str_CS_addresss = CS_addresss.getText().toString();
						String Str_CS_workorder = CS_workorder.getText().toString();
						String Str_CS_duedate = CS_duedate.getText().toString();
						String Str_CS_priority = CS_priority.getText().toString();
						String Str_CS_instructions = CS_instructions.getText().toString();
						String Str_CS_documents = CS_documents.getText().toString();
						String Str_CS_date = CS_date.getText().toString();
						String Str_CS_Time;
						boolean B_CS_chk_time = CS_chk_time.isChecked();
						String Str_CS_comments = CS_comments.getText().toString();
						String Str_CS_feeadvance = CS_feeadvance.getText().toString();
						String Str_CS_casename = CS_casename.getText().toString();
						String Str_CS_caseNumber = CS_caseNumber.getText().toString();
						String Str_CS_weight = CS_weight.getText().toString();
						String Str_CS_pieces = CS_pieces.getText().toString();
						String Str_CS_waittime = CS_waittime.getText().toString();

//						if (B_CS_chk_time) {
//							Str_CS_Time = CS_Time.getText().toString();
//						} else {
//							Str_CS_Time = "";
//						}

						Str_CS_Time = CS_Time.getText().toString();

						if (Str_CS_comments == null || Str_CS_comments.length() == 0) {
							new CustomAlertDialog(JobsQueue.this, "Enter Comments").show();
							return;
						}

						SubmitCourtPOD submitCourtPOD = new SubmitCourtPOD();
						submitCourtPOD.setWorkorder(address.getWorkorder());
						submitCourtPOD.setProofDate(Str_CS_date);
						submitCourtPOD.setProofTime(Str_CS_Time);
						submitCourtPOD.setProofComments(Str_CS_comments);
						if(Str_CS_feeadvance.length() == 0){
							submitCourtPOD.setFeeAdvance(0);
						}else {
							submitCourtPOD.setFeeAdvance(Integer.parseInt(Str_CS_feeadvance));

						}
						if(Str_CS_weight.length() == 0){
							submitCourtPOD.setWeight(0);
						}else {
							submitCourtPOD.setWeight(Integer.parseInt(Str_CS_weight));
						}
						if (Str_CS_waittime.length() == 0){
							submitCourtPOD.setWaitTime(0);

						}else {
							submitCourtPOD.setWaitTime(Integer.parseInt(Str_CS_waittime));
						}

						if (Str_CS_pieces.length() == 0){
							submitCourtPOD.setPieces(0);
						}else {
							submitCourtPOD.setPieces(Integer.parseInt(Str_CS_pieces));
						}
						submitCourtPOD.setLatitude(address.getLat());
						submitCourtPOD.setLongitude(address.getLng());
						submitCourtPOD.setSubmitCourtPODID(address.getCourtOpenAddressID());

						try {
							CS_is_updated = database.UpdateIntoSubmitCourtPODTable(submitCourtPOD);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (CS_is_updated) {
							Toast.makeText(JobsQueue.this, "Court Services is successfully updated", Toast.LENGTH_SHORT).show();
							processList();
						} else {
							Toast.makeText(JobsQueue.this, "Court Services update unsuccessfull ", Toast.LENGTH_SHORT).show();

						}

						dialog.dismiss();
					}
				});

				upload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						initializeUpload();

						dialog.dismiss();
					}


					private void initializeUpload() {
						// TODO Auto-generated method stub

						new AsyncTask<Void, Void, Void>() {
							// String errorString = null;
							protected void onPreExecute() {
								ProgressBar.showCommonProgressDialog(
										JobsQueue.this, "Uploading...");

							}

							@Override
							protected Void doInBackground(Void... params) {

								try {
									// ProcessAddressForServer processOrderList
									// = new ProcessAddressForServer>();
									// processOrderList =
									// database.getFinalStatusToUploadToServer();
									String sessionId = WebServiceConsumer
											.getInstance()
											.signOn(TristarConstants.SOAP_ADDRESS,
													SessionData.getInstance()
															.getUsername(),
													SessionData.getInstance()
															.getPassword());

									serverCode = SubmitWebServiceConsumer
											.getInstance().SubmitCourtPOD(
													sessionId, addresssubmit);

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// errorString = initialUploadWebServiceCall();

								return null;
							}

							// private String initialUploadWebServiceCall() {
							// // TODO Auto-generated method stub
							// String errorString = null;
							// ArrayList<Object> jobList = new
							// ArrayList<Object>();
							// {
							// ArrayList<ProcessAddressForServer> list =
							// database
							// .getFinalStatusToSubmit();
							// if (list != null && list.size() > 0) {
							// jobList.addAll(list);
							// }
							// }
							// return errorString;
							// }

							@Override
							protected void onPostExecute(Void result) {
								ProgressBar.dismiss();

								if (isServerCodeAccepted(serverCode)) {

									Log.d("Server Code", "" + serverCode);
									Log.d("Cort pod id", "" + addresssubmit.getSubmitCourtPODID());
									database.deleteSubmitCourtPODTableAfterSync(address
											.getCourtOpenAddressID());

									database.deleteSubmitCourtPODTableAfterSync(addresssubmit.getSubmitCourtPODID());
									try {
										database.deleteSubmitCourtPODTableAfterSync(address
												.getCourtOpenAddressID());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									jobList.remove(item);
									if (jobList.get(index - 1) instanceof String) {
										if (jobList.size() == index
												|| jobList.get(index) instanceof String)
											jobList.remove(index - 1);
									}
									JobAdapter adapter = new JobAdapter();
									adapter.notifyDataSetChanged();
									listView.setAdapter(adapter);
									if (courtpodImage.size() == 0) {
										database.DeleteCourtPODAfteruploadTableBy(address.getWorkorder());
										new CustomAlertDialog(JobsQueue.this,
												"Uploaded Successfully").show();
										// listCategory.initialSync();
									} else {
										uploadImage();
									}

								} else {


//									new CustomAlertDialog(JobsQueue.this,
//											"Invoking initial sync failed with error code : "
//													+ serverCode).show();
									Log.d("Server Code", "" + serverCode);
									Log.d("Cort pod id", "" + addresssubmit.getSubmitCourtPODID());
									database.deleteSubmitCourtPODTableAfterSync(address
											.getCourtOpenAddressID());

									database.deleteSubmitCourtPODTableAfterSync(addresssubmit.getSubmitCourtPODID());
									try {
										database.deleteSubmitCourtPODTableAfterSync(address
												.getCourtOpenAddressID());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									jobList.remove(item);
									if (jobList.get(index - 1) instanceof String) {
										if (jobList.size() == index
												|| jobList.get(index) instanceof String)
											jobList.remove(index - 1);
									}
									JobAdapter adapter = new JobAdapter();
									adapter.notifyDataSetChanged();
									listView.setAdapter(adapter);
									database.DeleteCourtPODAfteruploadTableBy(address.getWorkorder());
									new CustomAlertDialog(JobsQueue.this,
											"Court Job is not submitted, please contact your administrator. This record has been deleted.").show();


								}

								// if (errorString != null) {
								// database.deleteCategoryReleatedTableInDB();
								// new CustomAlertDialog(JobsQueue.this,
								// "Invoking initial sync failed with error code : "
								// + errorString).show();
								// }
								//

								// final Dialog dialog = new
								// Dialog(JobsQueue.this);
								// dialog.getWindow().setBackgroundDrawable(
								// new ColorDrawable(Color.TRANSPARENT));
								// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								// dialog.setContentView(R.layout.alert_upload_final_status);
								// ok = (Button) findViewById(R.id.btn_ok);
								// ok.setOnClickListener(new OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// //deleteRecordFromTable(position);
								// }
								// });
								//
								super.onPostExecute(result);
							}

							private void uploadImage() {

								new AsyncTask<Void, Void, Void>() {
									// String errorString = null;
									protected void onPreExecute() {
										ProgressBar.showCommonProgressDialog(
												JobsQueue.this, "Uploading...");

									}

									@Override
									protected Void doInBackground(
											Void... params) {

										try {
											// ProcessAddressForServer
											// processOrderList
											// = new ProcessAddressForServer>();
											// processOrderList =
											// database.getFinalStatusToUploadToServer();
											for (PODAttachments submitDiligence : courtpodImage) {

												Log.d("attachment upload", ""
														+ podImage.size());
												String sessionID = WebServiceConsumer
														.getInstance()
														.signOn(TristarConstants.SOAP_ADDRESS,
																SessionData
																		.getInstance()
																		.getUsername(),
																SessionData
																		.getInstance()
																		.getPassword());
												serverCode = SubmitWebServiceConsumer.getInstance()
														.SubmitCourtPODAttachments(sessionID, submitDiligence);

												if (isServerCodeAccepted(serverCode)) {
													database.deleteSubmitCourtPODAttachementTable(submitDiligence);
												}
											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										// errorString =
										// initialUploadWebServiceCall();

										return null;
									}

									// private String
									// initialUploadWebServiceCall() {
									// // TODO Auto-generated method stub
									// String errorString = null;
									// ArrayList<Object> jobList = new
									// ArrayList<Object>();
									// {
									// ArrayList<ProcessAddressForServer> list =
									// database
									// .getFinalStatusToSubmit();
									// if (list != null && list.size() > 0) {
									// jobList.addAll(list);
									// }
									// }
									// return errorString;
									// }

									@Override
									protected void onPostExecute(Void result) {
										ProgressBar.dismiss();

										if (isServerCodeAccepted(serverCode)) {
//										database.deleteSubmitCourtPODAttachementTable(courtpodImage);
											database.deleteSubmitCourtPODAttachementTableFromQueue(address
													.getWorkorder());
											database.DeleteCourtPODAfteruploadTableBy(address.getWorkorder());

											new CustomAlertDialog(
													JobsQueue.this,
													"Uploaded Successfully")
													.show();

											submitImage.clear();

											// listCategory.initialSync();
										} else {

											new CustomAlertDialog(
													JobsQueue.this,
													"Invoking initial sync failed with error code : "
															+ serverCode)
													.show();
										}

										// if (errorString != null) {
										// database.deleteCategoryReleatedTableInDB();
										// new CustomAlertDialog(JobsQueue.this,
										// "Invoking initial sync failed with error code : "
										// + errorString).show();
										// }
										//

										// final Dialog dialog = new
										// Dialog(JobsQueue.this);
										// dialog.getWindow().setBackgroundDrawable(
										// new
										// ColorDrawable(Color.TRANSPARENT));
										// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
										// dialog.setContentView(R.layout.alert_upload_final_status);
										// ok = (Button)
										// findViewById(R.id.btn_ok);
										// ok.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										// //deleteRecordFromTable(position);
										// }
										// });
										//
										super.onPostExecute(result);
									}
								}.execute();
							}

						}.execute();

					}
				});

				podImage = database
						.getAttachementsFromSubmitCourtPODAttachmentTableForUploadForQueue(address
								.getWorkorder());

				CS_image.setText("Image");
				CS_image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.job_queue_attachment);
						ImageView close = (ImageView) dialog
								.findViewById(R.id.close);

						podImage = database
								.getAttachementsFromSubmitCourtPODAttachmentTableForUploadForQueue(address
										.getWorkorder());

						GridView gridImage = (GridView) dialog
								.findViewById(R.id.lst_image_container);
						gridImage.setNumColumns(3);
						attachedFilesData = new ArrayList<byte[]>();
						for (int i = 0; i < podImage.size(); i++) {

							Log.d("", ""
									+ podImage.get(i)
									.getAttahmentBase64String());

							attachedFilesData.add(Base64
									.decode(podImage.get(i)
													.getAttahmentBase64String(),
											Base64.DEFAULT));
						}

						ImageListAdapter adapter = new ImageListAdapter();
						adapter.notifyDataSetChanged();
						gridImage.setAdapter(adapter);

						close.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						dialog.show();
					}
				});

				// TextView

				close.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
				Toast.makeText(getApplicationContext(),
						"CourtAddressForServer" + address.getWorkorder(),
						Toast.LENGTH_LONG).show();
			} else if (item instanceof SubmitDiligence) {
				RD_address = (SubmitDiligence) item;

				final Dialog dialog = new Dialog(JobsQueue.this);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.job_queue_record_deligience);

				ImageView close = (ImageView) dialog.findViewById(R.id.close);
				ImageView upload = (ImageView) dialog.findViewById(R.id.upload);

				RD_layout_special_instruction =
						(LinearLayout) dialog.findViewById(R.id.layout_special_instruction);
				RD_workorder = (TextView) dialog
						.findViewById(R.id.deligience_workorder);
				RD_report = (EditText) dialog
						.findViewById(R.id.deligience_specialreport);
				RD_textAddress = (TextView) dialog
						.findViewById(R.id.deligience_address);
				RD_date = (TextView) dialog
						.findViewById(R.id.deligience_date);
				RD_set_time = (TextView) dialog
						.findViewById(R.id.deligience_time);
				RD_latlang = (TextView) dialog
						.findViewById(R.id.deligience_lattitudedeligence);
				RD_image = (TextView) dialog
						.findViewById(R.id.deligience_image);

				RD_audio = (TextView) dialog
						.findViewById(R.id.deligience_audio);
				RD_edt_image = (Button) dialog
						.findViewById(R.id.edt_image);

				RD_timeCheckBox = (CheckBox) dialog.findViewById(R.id.Checkbox);
				RD_timeCheckBox.setChecked(true);

				Rd_btn_Save = (Button) dialog.findViewById(R.id.btn_save);

				listStatusValues = database
						.getStatusValuesFromDBToDisplayIndiligencesView();
				for (int i = 0; i < listStatusValues.size(); i++) {
					Diligence.add(listStatusValues.get(i).getPhoneTitle());
				}

				submitImage = database
						.getAttachementsFromDeligenceAttachmentsTable(RD_address
								.getLineItem());
				Log.d("submitImage", "" + submitImage);


				attachedFilesData = new ArrayList<byte[]>();
				for (int i = 0; i < submitImage.size(); i++) {

					Log.d("", ""
							+ submitImage.get(i).getAttachementBase64String());
					if (submitImage.get(i).getAttachementsFileName()
							.contains("Audio")) {
						RD_audio.setText(submitImage.get(i)
								.getAttachementsFileName());
						audiofile = (Base64.decode(submitImage.get(i)
								.getAttachementBase64String(), Base64.DEFAULT));
					} else {
						RD_image.setText(submitImage.get(i)
								.getAttachementsFileName());
					}

				}


				RD_workorder.setText(RD_address.getWorkorder());
				RD_report.setText(RD_address.getReport());
				RD_textAddress.setText(database
						.getAddressForUpdateRecordDiligences(
								RD_address.getWorkorder(),
								RD_address.getAddressLineItem()));
				RD_date.setText(RD_address.getDiligenceDate());

				RD_set_time.setText(RD_address.getDiligenceTime());
				RD_latlang.setText(RD_address.getLatitude() + ":"
						+ RD_address.getLongitude());

//				RD_timeCheckBox.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (RD_timeCheckBox.isChecked()) {
//							RD_set_time.setEnabled(true);
//						} else {
//							RD_set_time.setEnabled(false);
//						}layout_manner_of_services
//					}layout_manner_of_services
//				});

				RD_edt_image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.RECORD_DILIGENCE;
						Intent attach = new Intent(JobsQueue.this,
								BaseFileIncluder.class);
						attach.putExtra("processOrderID", RD_address.getPROCCESS_ID());
						attach.putExtra("activityId", 1);
						startActivity(attach);
					}
				});

				RD_layout_special_instruction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogmanner();
					}
				});

				String sp = RD_deligience_specialreport[0];
				RD_date.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedTextView = (TextView) v;
						showDialog(DATE_DIALOG_ID);
					}
				});

				RD_set_time.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.timepicker_dialog);
						final TimePicker tp = (TimePicker) dialog.findViewById(R.id.tp);
						Button set = (Button) dialog.findViewById(R.id.btn_set);
						Button cancel = (Button) dialog.findViewById(R.id.btn_calcel);
						final Calendar c = Calendar.getInstance();

						SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
						String formattedTime = dt.format(c.getTime());
						//btnTime.setText(formattedTime.toLowerCase());
						final String[] time = {formattedTime.toLowerCase()};
						tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
							@Override
							public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
								//Display the new time to app interface
								String AMPM = "am";
								if (hourOfDay > 11) {
									hourOfDay = hourOfDay - 12;
									AMPM = "pm";
								}
								String hr = "";
								if (Integer.toString(hourOfDay).length() == 1) {
									hr = "0" + Integer.toString(hourOfDay);
								} else {
									hr = Integer.toString(hourOfDay);
								}

								String min = "";
								if (Integer.toString(minute).length() == 1) {
									min = "0" + Integer.toString(minute);
								} else {
									min = Integer.toString(minute);
								}


								time[0] = "" + hr + ":" + min + " " + AMPM;

							}
						});

						set.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								RD_set_time.setText(time[0]);
								dialog.dismiss();

							}
						});


						cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

						dialog.show();
					}

				});

				Rd_btn_Save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (RD_report.getText().toString() == null || RD_report.getText().toString().length() == 0) {
							new CustomAlertDialog(
									JobsQueue.this,
									"Report cannot be empty. Either type some text or select one diligence item then submit")
									.show();
							return;
						}

						SubmitDiligence submitDiligencesInDB = new SubmitDiligence();
						submitDiligencesInDB.setWorkorder(RD_address.getWorkorder());
						submitDiligencesInDB.setAddressLineItem(RD_address.getAddressLineItem());
						submitDiligencesInDB.setDiligenceDate(RD_date.getText().toString());
//						if (RD_timeCheckBox.isChecked()) {
//							submitDiligencesInDB.setDiligenceTime(RD_set_time.getText().toString());
//						} else {
//							submitDiligencesInDB.setDiligenceTime("");
//						}
						submitDiligencesInDB.setDiligenceTime(RD_set_time.getText().toString());
						String report = RD_report.getText().toString();
						submitDiligencesInDB.setReport(RD_report.getText().toString());
						submitDiligencesInDB
								.setDiligenceCode(RD_address.getDiligenceCode());
						submitDiligencesInDB.setServerCode("");
						submitDiligencesInDB.setLineItem(RD_address.getLineItem());
						submitDiligencesInDB.setLatitude(RD_address.getLatitude());
						submitDiligencesInDB.setLongitude(RD_address.getLongitude());

						boolean isSubmitSuccess = true;
						isSubmitSuccess = database.insertOrUpdateRecordDiligenceInDB(
								submitDiligencesInDB, false);

						Log.i("Record_diligence_dialog", "" + isSubmitSuccess);

						if (isSubmitSuccess) {
							String imageSaved = "";
							SubmitDiligence submitImage = new SubmitDiligence();

							database.deleteAttachmentsFromattachemntsTableBy(RD_address.getLineItem());
							submitImage.setLineItem(RD_address.getLineItem());


							ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance()
									.getAttachedFilesData();
							for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
								submitImage.setWorkorder(RD_address.getWorkorder());
								submitImage.setAddressLineItem(RD_address.getAddressLineItem());
								submitImage.setAttachementsFileName(RD_address.getWorkorder() + "-"
										+ RD_address.getAddressLineItem() + "-image-" + i);
								submitImage.setAttachementBase64String(Base64.encodeToString(
										imageArrayToSaveInDB.get(i), Base64.DEFAULT));
								submitImage.setAttachementOfUrlString("");

								boolean sucess = true;

								sucess = database
										.insertOrUpdateAttachmentsOfSubmitDiligences(
												submitImage);
								if (sucess == false) {
									imageSaved = "Image is not saved!";
								}
							}


							byte[] audioArray = SessionData.getInstance().getAudioData();
							if (audioArray != null && audioArray.length > 0) {

								submitImage.setWorkorder(RD_address.getWorkorder());
								submitImage.setAddressLineItem(RD_address.getAddressLineItem());
								submitImage.setAttachementsFileName(RD_address.getWorkorder() + "-"
										+ (long) RD_address.getAddressLineItem() + "-Audio");

								Log.d("Attached Audio", "" + RD_address.getWorkorder() + "-"
										+ (long) RD_address.getAddressLineItem() + "-Audio");
								submitImage.setAttachementOfUrlString("");

								{
									submitImage.setAttachementBase64String(Base64
											.encodeToString(audioArray, Base64.DEFAULT));
									boolean sucess = true;
									sucess = database
											.insertOrUpdateAttachmentsOfSubmitDiligences(
													submitImage);
									if (sucess == false) {
										new CustomAlertDialog(JobsQueue.this,
												"Audio is not saved").show();

									}
								}
							}
							if (imageSaved.length() == 0) {
								ArrayList<SubmitDiligence> submitDiligencesArrayFromDB = database
										.getSubmitDiligencesValuesFromDBForUploadingToServer();
								if (submitDiligencesArrayFromDB.size() > 0) {

								}
							} else {
								new CustomAlertDialog(JobsQueue.this, imageSaved).show();
								SessionData.getInstance().clearAttachments();
							}
						}

						if (isSubmitSuccess) {
							dialog.dismiss();
							Toast.makeText(JobsQueue.this, "Record Diligence is successfully updated", Toast.LENGTH_SHORT).show();
							processList();
						}
					}
				});

				upload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						initializeUpload();
						dialog.dismiss();
					}

					private void initializeUpload() {
						// TODO Auto-generated method stub

						new AsyncTask<Void, Void, Void>() {
							// String errorString = null;
							protected void onPreExecute() {
								ProgressBar.showCommonProgressDialog(
										JobsQueue.this, "Uploading...");
							}

							@Override
							protected Void doInBackground(Void... params) {
								try {
									// ProcessAddressForServer processOrderList
									// = new ProcessAddressForServer>();
									// processOrderList =
									// database.getFinalStatusToUploadToServer();
									String sessionId = WebServiceConsumer
											.getInstance()
											.signOn(TristarConstants.SOAP_ADDRESS,
													SessionData.getInstance()
															.getUsername(),
													SessionData.getInstance()
															.getPassword());

									serverCode = SubmitWebServiceConsumer
											.getInstance().SubmitDiligence(
													sessionId, RD_address);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// errorString = initialUploadWebServiceCall();
								return null;
							}

							// private String initialUploadWebServiceCall() {
							// // TODO Auto-generated method stub
							// String errorString = null;
							// ArrayList<Object> jobList = new
							// ArrayList<Object>();
							// {
							// ArrayList<ProcessAddressForServer> list =
							// database
							// .getFinalStatusToSubmit();
							// if (list != null && list.size() > 0) {
							// jobList.addAll(list);
							// }
							// }
							// return errorString;
							// }

							@Override
							protected void onPostExecute(Void result) {
								ProgressBar.dismiss();

								if (serverCode != null
										&& serverCode.length() > 0
										&& !serverCode.equals("rejected")) {
									try {

										database.updateSubmitDiligenceTableAfterInsertingToServer(
												RD_address.getLineItem(),
												serverCode);
										database.updateSubmitDiligenceAttachmentsTableWithServerCode(
												RD_address.getLineItem(),
												serverCode);
										database.deleteSubmitDiligence(RD_address
												.getLineItem());
										database.updateSubmitDiligenceAttachmentsTableAfterSync(RD_address
												.getLineItem());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									jobList.remove(item);
									if (jobList.get(index - 1) instanceof String) {
										if (jobList.size() == index
												|| jobList.get(index) instanceof String)
											jobList.remove(index - 1);
									}
									JobAdapter adapter = new JobAdapter();
									adapter.notifyDataSetChanged();
									listView.setAdapter(adapter);
									submitImage = database
											.getAttachementsFromDeligenceAttachmentsTable(RD_address
													.getLineItem());

									Log.d("attachment count",
											"" + submitImage.size());

									if (submitImage.size() == 0) {
										new CustomAlertDialog(JobsQueue.this,
												"Uploaded Successfully").show();

									} else {
										SessionData.getInstance().setStr(serverCode);
										uploadImage();
									}
									// listCategory.initialSync();
								} else {

									new CustomAlertDialog(JobsQueue.this,
											"Invoking initial sync failed with error code : "
													+ serverCode).show();
								}

								// if (errorString != null) {
								// database.deleteCategoryReleatedTableInDB();
								// new CustomAlertDialog(JobsQueue.this,
								// "Invoking initial sync failed with error code : "
								// + errorString).show();
								// }
								//

								// final Dialog dialog = new
								// Dialog(JobsQueue.this);
								// dialog.getWindow().setBackgroundDrawable(
								// new ColorDrawable(Color.TRANSPARENT));
								// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								// dialog.setContentView(R.layout.alert_upload_final_status);
								// ok = (Button) findViewById(R.id.btn_ok);
								// ok.setOnClickListener(new OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// //deleteRecordFromTable(position);
								// }
								// });
								//
								super.onPostExecute(result);
							}

							private void uploadImage() {

								new AsyncTask<Void, Void, Void>() {
									// String errorString = null;
									protected void onPreExecute() {
										ProgressBar.showCommonProgressDialog(
												JobsQueue.this, "Uploading...");

									}

									@Override
									protected Void doInBackground(
											Void... params) {

										try {
											// ProcessAddressForServer
											// processOrderList
											// = new ProcessAddressForServer>();
											// processOrderList =
											// database.getFinalStatusToUploadToServer();

											for (SubmitDiligence submitDiligence : submitImage) {

												Log.d("attachment upload", ""
														+ submitImage.size());
												String sessionID = WebServiceConsumer
														.getInstance()
														.signOn(TristarConstants.SOAP_ADDRESS,
																SessionData
																		.getInstance()
																		.getUsername(),
																SessionData
																		.getInstance()
																		.getPassword());
												serverCode = SubmitWebServiceConsumer
														.getInstance()
														.SubmitAttemptAttachments(
																sessionID,
																submitDiligence);

											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										// errorString =
										// initialUploadWebServiceCall();

										return null;
									}

									// private String
									// initialUploadWebServiceCall() {
									// // TODO Auto-generated method stub
									// String errorString = null;
									// ArrayList<Object> jobList = new
									// ArrayList<Object>();
									// {
									// ArrayList<ProcessAddressForServer> list =
									// database
									// .getFinalStatusToSubmit();
									// if (list != null && list.size() > 0) {
									// jobList.addAll(list);
									// }
									// }
									// return errorString;
									// }

									@Override
									protected void onPostExecute(Void result) {
										ProgressBar.dismiss();

										if (serverCode != null
												&& serverCode.length() > 0
												&& !serverCode
												.equals("rejected")) {
											try {
												database.deleteSubmitDiligence(RD_address
														.getLineItem());
												database.updateSubmitDiligenceAttachmentsTableAfterSync(RD_address
														.getLineItem());
												database.updateSubmitDiligenceTableAfterInsertingToServer(
														RD_address.getLineItem(),
														serverCode);
												database.updateSubmitDiligenceAttachmentsTableWithServerCode(
														RD_address.getLineItem(),
														serverCode);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

											jobList.remove(item);
											// if (jobList.get(index - 1)
											// instanceof String) {
											// if (jobList.size() == index
											// || jobList.get(index) instanceof
											// String)
											// jobList.remove(index - 1);
											// }
											// JobAdapter adapter = new
											// JobAdapter();
											// adapter.notifyDataSetChanged();
											// listView.setAdapter(adapter);

											new CustomAlertDialog(
													JobsQueue.this,
													"Uploaded Successfully")
													.show();

											submitImage.clear();

											// listCategory.initialSync();
										} else {

											new CustomAlertDialog(
													JobsQueue.this,
													"Invoking initial sync failed with error code : "
															+ serverCode)
													.show();
										}

										// if (errorString != null) {
										// database.deleteCategoryReleatedTableInDB();
										// new CustomAlertDialog(JobsQueue.this,
										// "Invoking initial sync failed with error code : "
										// + errorString).show();
										// }
										//

										// final Dialog dialog = new
										// Dialog(JobsQueue.this);
										// dialog.getWindow().setBackgroundDrawable(
										// new
										// ColorDrawable(Color.TRANSPARENT));
										// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
										// dialog.setContentView(R.layout.alert_upload_final_status);
										// ok = (Button)
										// findViewById(R.id.btn_ok);
										// ok.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										// //deleteRecordFromTable(position);
										// }
										// });
										//
										super.onPostExecute(result);
									}
								}.execute();
							}

						}.execute();

					}

				});

//				database.getAttachementsFromDeligenceAttachmentsTable(address
//						.getDiligenceCode());

				submitImage = database
						.getAttachementsFromDeligenceAttachmentsTable(RD_address
								.getLineItem());
				Log.d("submitImage", "" + submitImage);

				attachedFilesData = new ArrayList<byte[]>();
				for (int i = 0; i < submitImage.size(); i++) {

					Log.d("", ""
							+ submitImage.get(i).getAttachementBase64String());
					if (submitImage.get(i).getAttachementsFileName()
							.contains("Audio")) {
						RD_audio.setText(submitImage.get(i)
								.getAttachementsFileName());
						audiofile = (Base64.decode(submitImage.get(i)
								.getAttachementBase64String(), Base64.DEFAULT));
					} else {
						RD_image.setText(submitImage.get(i)
								.getAttachementsFileName());
					}

				}
				RD_audio.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						playMp3(audiofile);

					}

				});

				RD_image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Dialog dialog = new Dialog(JobsQueue.this);
						dialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.job_queue_attachment);
						ImageView close = (ImageView) dialog
								.findViewById(R.id.close);

						submitImage = database
								.getAttachementsFromDeligenceAttachmentsTable(RD_address
										.getLineItem());

						GridView gridImage = (GridView) dialog
								.findViewById(R.id.lst_image_container);
						gridImage.setNumColumns(3);
						attachedFilesData = new ArrayList<byte[]>();

						for (int i = 0; i < submitImage.size(); i++) {

							Log.d("", ""
									+ submitImage.get(i)
									.getAttachementBase64String());

							attachedFilesData.add(Base64.decode(submitImage
											.get(i).getAttachementBase64String(),

									Base64.DEFAULT));
						}

						ImageListAdapter adapter = new ImageListAdapter();
						adapter.notifyDataSetChanged();
						gridImage.setAdapter(adapter);

						close.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						dialog.show();
					}
				});

				close.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});


				dialog.show();

				// database.deleteFinalStatusAttachementTable(address.getLineItem());
			} else if (item instanceof AddressForServer) {
				final AddressForServer address = (AddressForServer) item;
				if (address.TYPE == AddressForServer.PICKUP_SERVICE) {
					final Dialog dialog = new Dialog(JobsQueue.this);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.job_queue_pickup_services);
					PS_submitPickupPOD =
							database.getSubmitPickupPODValuesFromDBForUploadingToServerSingle(address.getWorkorder());

					ImageView close = (ImageView) dialog
							.findViewById(R.id.close);
					ImageView upload = (ImageView) dialog.findViewById(R.id.upload);
					close.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					upload.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							initializeUpload();

							dialog.dismiss();
						}


						private void initializeUpload() {
							// TODO Auto-generated method stub

							new AsyncTask<Void, Void, Void>() {
								// String errorString = null;
								protected void onPreExecute() {
									ProgressBar.showCommonProgressDialog(
											JobsQueue.this, "Uploading...");

								}

								@Override
								protected Void doInBackground(Void... params) {

									try {
										// ProcessAddressForServer processOrderList
										// = new ProcessAddressForServer>();
										// processOrderList =
										// database.getFinalStatusToUploadToServer();
										String sessionId = WebServiceConsumer
												.getInstance()
												.signOn(TristarConstants.SOAP_ADDRESS,
														SessionData.getInstance()
																.getUsername(),
														SessionData.getInstance()
																.getPassword());

										serverCode = SubmitWebServiceConsumer
												.getInstance().SubmitPickupPOD(
														sessionId, PS_submitPickupPOD);

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// errorString = initialUploadWebServiceCall();

									return null;
								}

								// private String initialUploadWebServiceCall() {
								// // TODO Auto-generated method stub
								// String errorString = null;
								// ArrayList<Object> jobList = new
								// ArrayList<Object>();
								// {
								// ArrayList<ProcessAddressForServer> list =
								// database
								// .getFinalStatusToSubmit();
								// if (list != null && list.size() > 0) {
								// jobList.addAll(list);
								// }
								// }
								// return errorString;
								// }

								@Override
								protected void onPostExecute(Void result) {
									ProgressBar.dismiss();

									if (isServerCodeAccepted(serverCode)) {

										Log.d("Server Code", "" + serverCode);
										Log.d("Cort pod id", "" + PS_submitPickupPOD.getSubmitPickupPODID());

										try {
											database.deleteSubmitPickupPODTableAfterSync(PS_submitPickupPOD
													.getSubmitPickupPODID());
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										database.deleteSubmitPickupPODTableAfterSync(address
												.getJobID());
										database.deleteSubmitPickupPODAttachementTableFromQueue(address
												.getWorkorder());

										jobList.remove(item);
										if (jobList.get(index - 1) instanceof String) {
											if (jobList.size() == index
													|| jobList.get(index) instanceof String)
												jobList.remove(index - 1);
										}
										JobAdapter adapter = new JobAdapter();
										adapter.notifyDataSetChanged();
										listView.setAdapter(adapter);
										if (deliverypodImage.size() == 0) {
											database.DeletePickupPODAfteruploadTableBy(address.getWorkorder());

											new CustomAlertDialog(JobsQueue.this,
													"Uploaded Successfully").show();
											// listCategory.initialSync();
										} else {
											uploadImage();
										}

									} else {
										new CustomAlertDialog(JobsQueue.this,
												"Invoking initial sync failed with error code : "
														+ serverCode).show();
									}

									// if (errorString != null) {
									// database.deleteCategoryReleatedTableInDB();
									// new CustomAlertDialog(JobsQueue.this,
									// "Invoking initial sync failed with error code : "
									// + errorString).show();
									// }
									//

									// final Dialog dialog = new
									// Dialog(JobsQueue.this);
									// dialog.getWindow().setBackgroundDrawable(
									// new ColorDrawable(Color.TRANSPARENT));
									// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									// dialog.setContentView(R.layout.alert_upload_final_status);
									// ok = (Button) findViewById(R.id.btn_ok);
									// ok.setOnClickListener(new OnClickListener() {
									// @Override
									// public void onClick(View v) {
									// //deleteRecordFromTable(position);
									// }
									// });
									//
									super.onPostExecute(result);
								}

								private void uploadImage() {

									new AsyncTask<Void, Void, Void>() {
										// String errorString = null;
										protected void onPreExecute() {
											ProgressBar.showCommonProgressDialog(
													JobsQueue.this, "Uploading...");
										}

										@Override
										protected Void doInBackground(
												Void... params) {
											try {
												// ProcessAddressForServer
												// processOrderList
												// = new ProcessAddressForServer>();
												// processOrderList =
												// database.getFinalStatusToUploadToServer();
												for (PODAttachments submitDiligence : deliverypodImage) {

													Log.d("attachment upload", ""
															+ deliverypodImage.size());
													String sessionID = WebServiceConsumer
															.getInstance()
															.signOn(TristarConstants.SOAP_ADDRESS,
																	SessionData
																			.getInstance()
																			.getUsername(),
																	SessionData
																			.getInstance()
																			.getPassword());
													serverCode = SubmitWebServiceConsumer.getInstance()
															.SubmitPickupPODAttachments(sessionID, submitDiligence);
													if (isServerCodeAccepted(serverCode)) {
														database.deleteSubmitPickupPODAttachementTable(submitDiligence);
													}

												}

											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											// errorString =
											// initialUploadWebServiceCall();

											return null;
										}

										// private String
										// initialUploadWebServiceCall() {
										// // TODO Auto-generated method stub
										// String errorString = null;
										// ArrayList<Object> jobList = new
										// ArrayList<Object>();
										// {
										// ArrayList<ProcessAddressForServer> list =
										// database
										// .getFinalStatusToSubmit();
										// if (list != null && list.size() > 0) {
										// jobList.addAll(list);
										// }
										// }
										// return errorString;
										// }

										@Override
										protected void onPostExecute(Void result) {
											ProgressBar.dismiss();

											if (isServerCodeAccepted(serverCode)) {
//											database.deleteSubmitCourtPODAttachementTable(courtpodImage);

												database.deleteSubmitPickupPODTableAfterSync(address
														.getJobID());
												database.deleteSubmitPickupPODAttachementTableFromQueue(address
														.getWorkorder());
												database.DeletePickupPODAfteruploadTableBy(address.getWorkorder());

												new CustomAlertDialog(
														JobsQueue.this,
														"Uploaded Successfully")
														.show();

												submitImage.clear();

												// listCategory.initialSync();
											} else {

												new CustomAlertDialog(
														JobsQueue.this,
														"Invoking initial sync failed with error code : "
																+ serverCode)
														.show();
											}

											// if (errorString != null) {
											// database.deleteCategoryReleatedTableInDB();
											// new CustomAlertDialog(JobsQueue.this,
											// "Invoking initial sync failed with error code : "
											// + errorString).show();
											// }
											//

											// final Dialog dialog = new
											// Dialog(JobsQueue.this);
											// dialog.getWindow().setBackgroundDrawable(
											// new
											// ColorDrawable(Color.TRANSPARENT));
											// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
											// dialog.setContentView(R.layout.alert_upload_final_status);
											// ok = (Button)
											// findViewById(R.id.btn_ok);
											// ok.setOnClickListener(new
											// OnClickListener() {
											// @Override
											// public void onClick(View v) {
											// //deleteRecordFromTable(position);
											// }
											// });
											//
											super.onPostExecute(result);
										}
									}.execute();
								}

							}.execute();

						}
					});

					PS_workorder = (TextView) dialog
							.findViewById(R.id.pickup_workorder);

					PS_casename = (TextView) dialog.findViewById(R.id.pickup_casename);

					PS_casenumber = (TextView) dialog.findViewById(R.id.pickup_casenumber);

					PS_jobinstructions = (TextView) dialog.findViewById(R.id.pickup_jobinstructions);

					PS_addressinstructions = (TextView) dialog.findViewById(R.id.pickup_addressinstructions);

					pickupServiceListArray = database.getPickupOrderValues();

					PS_workorder.setText(address.getWorkorder());

					PS_addresss = (TextView) dialog
							.findViewById(R.id.pickup_address);

					PS_duedate = (TextView) dialog
							.findViewById(R.id.pickup_due_date);

					PS_priority = (TextView) dialog
							.findViewById(R.id.pickup_priority);

					PS_duetime = (TextView) dialog
							.findViewById(R.id.pickup_due_time);

					PS_date = (TextView) dialog
							.findViewById(R.id.pickup_date);

					PS_date.setText(address.getDate());

					PS_time = (TextView) dialog
							.findViewById(R.id.pickup_time);

					PS_time.setText(address.getTime());

					PS_comments = (EditText) dialog
							.findViewById(R.id.pickup_comments);

					PS_btn_Save = (Button) dialog.findViewById(R.id.btn_save);

					PS_comments.setText(address.getComment());

					for (int i = 0; i < pickupServiceListArray.size(); i++) {

						if (pickupServiceListArray.get(i).getWorkorder()
								.contains(address.getWorkorder())) {

							PS_addresss.setText(pickupServiceListArray.get(i)
									.getAddressFormattedForDisplay());
							Log.d("addresss", ""
									+ pickupServiceListArray.get(i)
									.getAddressFormattedForDisplay());
							PS_priority.setText(pickupServiceListArray.get(i)
									.getPriorityTitle());
							Log.d("priority", ""
									+ pickupServiceListArray.get(i)
									.getPriorityTitle());
							PS_duedate.setText(pickupServiceListArray.get(i)
									.getDueDate());
							PS_duetime.setText(pickupServiceListArray.get(i)
									.getDueTime());
							PS_casename.setText(pickupServiceListArray.get(i)
									.getCaseName());
							PS_casenumber.setText(pickupServiceListArray.get(i)
									.getCaseNumber());
							PS_jobinstructions.setText(pickupServiceListArray.get(i)
									.getOrderInstructions());
							PS_addressinstructions.setText(pickupServiceListArray.get(i)
									.getPickupInstructions());
						}

					}

					PS_date.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectedTextView = (TextView) v;
							showDialog(DATE_DIALOG_ID);
						}
					});

					PS_time.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							final Dialog dialog = new Dialog(JobsQueue.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.timepicker_dialog);
							final TimePicker tp = (TimePicker) dialog.findViewById(R.id.tp);
							Button set = (Button) dialog.findViewById(R.id.btn_set);
							Button cancel = (Button) dialog.findViewById(R.id.btn_calcel);
							final Calendar c = Calendar.getInstance();

							SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
							String formattedTime = dt.format(c.getTime());
							//btnTime.setText(formattedTime.toLowerCase());
							final String[] time = {formattedTime.toLowerCase()};
							tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
								@Override
								public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
									//Display the new time to app interface
									String AMPM = "am";
									if (hourOfDay > 11) {
										hourOfDay = hourOfDay - 12;
										AMPM = "pm";
									}
									String hr = "";
									if (Integer.toString(hourOfDay).length() == 1) {
										hr = "0" + Integer.toString(hourOfDay);
									} else {
										hr = Integer.toString(hourOfDay);
									}

									String min = "";
									if (Integer.toString(minute).length() == 1) {
										min = "0" + Integer.toString(minute);
									} else {
										min = Integer.toString(minute);
									}

									time[0] = "" + hr + ":" + min + " " + AMPM;

								}
							});

							set.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									PS_time.setText(time[0]);
									dialog.dismiss();

								}
							});

							cancel.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							dialog.show();
						}
					});

					PS_btn_Save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							String Str_PS_workorder = PS_workorder.getText().toString();
							String Str_PS_casename = PS_casename.getText().toString();
							String Str_PS_casenumber = PS_casenumber.getText().toString();
							String Str_PS_jobinstructions = PS_jobinstructions.getText().toString();
							String Str_PS_addressinstructions = PS_addressinstructions.getText().toString();
							String Str_PS_addresss = PS_addresss.getText().toString();
							String Str_PS_duedate = PS_duedate.getText().toString();
							String Str_PS_priority = PS_priority.getText().toString();
							String Str_PS_duetime = PS_duetime.getText().toString();
							String Str_PS_date = PS_date.getText().toString();
							String Str_PS_time = PS_time.getText().toString();
							String Str_PS_comments = PS_comments.getText().toString();

							SubmitPickupPOD submitPickupPOD = new SubmitPickupPOD();
							submitPickupPOD.setWorkorder(Str_PS_workorder);

							submitPickupPOD.setProofDate(Str_PS_date); // .replace("/", "-")
							submitPickupPOD.setProofTime(Str_PS_time);

							submitPickupPOD.setProofComments(Str_PS_comments);
							submitPickupPOD.setLatitude(PS_submitPickupPOD.getLatitude());
							submitPickupPOD.setLongitude(PS_submitPickupPOD.getLongitude());
							submitPickupPOD.setAddressLineitem(PS_submitPickupPOD.getAddressLineitem());
							submitPickupPOD.setSubmitPickupPODID(PS_submitPickupPOD.getSubmitPickupPODID());

							try {
								PS_isSubmitSucess = database.UpdateIntoSubmitPickupPODTable(submitPickupPOD);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (PS_isSubmitSucess) {
								Toast.makeText(JobsQueue.this, "PickUp Services is successfully updated", Toast.LENGTH_SHORT).show();
								processList();
								dialog.dismiss();
							} else {
								Toast.makeText(JobsQueue.this, "PickUp Services update unsuccessfull ", Toast.LENGTH_SHORT).show();

							}

						}
					});


					TextView image = (TextView) dialog
							.findViewById(R.id.pickup_Image);
					deliverypodImage = database
							.getAttachementsFromSubmitPickupPODAttachmentTableForUploadForQueue(address
									.getWorkorder());
					attachedFilesData = new ArrayList<byte[]>();
					for (int i = 0; i < deliverypodImage.size(); i++) {

						image.setText(deliverypodImage.get(i).getFileName());
					}

					image.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final Dialog dialog = new Dialog(JobsQueue.this);
							dialog.getWindow().setBackgroundDrawable(
									new ColorDrawable(Color.TRANSPARENT));
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.job_queue_attachment);
							ImageView close = (ImageView) dialog
									.findViewById(R.id.close);

							deliverypodImage = database
									.getAttachementsFromSubmitPickupPODAttachmentTableForUploadForQueue(address
											.getWorkorder());

							GridView gridImage = (GridView) dialog
									.findViewById(R.id.lst_image_container);
							gridImage.setNumColumns(3);
							attachedFilesData = new ArrayList<byte[]>();
							for (int i = 0; i < deliverypodImage.size(); i++) {

								Log.d("", ""
										+ deliverypodImage.get(i)
										.getPdfInMemory());

								attachedFilesData.add(Base64.decode(
										deliverypodImage.get(i)
												.getPdfInMemory(),
										Base64.DEFAULT));
							}

							ImageListAdapter adapter = new ImageListAdapter();
							adapter.notifyDataSetChanged();
							gridImage.setAdapter(adapter);

							close.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});

							dialog.show();
						}
					});

					dialog.show();

					Toast.makeText(getApplicationContext(),
							"PICKUP_SERVICE" + address.getWorkorder(),
							Toast.LENGTH_LONG).show();
				} else if (address.TYPE == AddressForServer.DELIVERY_SERVICE) {

					final Dialog dialog = new Dialog(JobsQueue.this);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.job_queue_delivery_services);

					DS_submitDeliveryPOD =
							database.getSubmitDeliveryPODValuesFromDBForUploadingToServerSingle(address.getWorkorder());

					ImageView close = (ImageView) dialog
							.findViewById(R.id.close);
					ImageView upload = (ImageView) dialog
							.findViewById(R.id.upload);
					close.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					upload.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							initializeUpload();

							dialog.dismiss();
						}


						private void initializeUpload() {
							// TODO Auto-generated method stub

							new AsyncTask<Void, Void, Void>() {
								// String errorString = null;
								protected void onPreExecute() {
									ProgressBar.showCommonProgressDialog(
											JobsQueue.this, "Uploading...");

								}

								@Override
								protected Void doInBackground(Void... params) {

									try {
										// ProcessAddressForServer processOrderList
										// = new ProcessAddressForServer>();
										// processOrderList =
										// database.getFinalStatusToUploadToServer();
										String sessionId = WebServiceConsumer
												.getInstance()
												.signOn(TristarConstants.SOAP_ADDRESS,
														SessionData.getInstance()
																.getUsername(),
														SessionData.getInstance()
																.getPassword());

										serverCode = SubmitWebServiceConsumer
												.getInstance().SubmitDeliveryPOD(
														sessionId, DS_submitDeliveryPOD);

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// errorString = initialUploadWebServiceCall();

									return null;
								}

								// private String initialUploadWebServiceCall() {
								// // TODO Auto-generated method stub
								// String errorString = null;
								// ArrayList<Object> jobList = new
								// ArrayList<Object>();
								// {
								// ArrayList<ProcessAddressForServer> list =
								// database
								// .getFinalStatusToSubmit();
								// if (list != null && list.size() > 0) {
								// jobList.addAll(list);
								// }
								// }
								// return errorString;
								// }

								@Override
								protected void onPostExecute(Void result) {
									ProgressBar.dismiss();

									if (isServerCodeAccepted(serverCode)) {

										Log.d("Server Code", "" + serverCode);
										Log.d("Cort pod id", "" + DS_submitDeliveryPOD.getSubmitDeliveryPODID());

										try {
											database.deleteSubmitDeliveryPODTableAfterSync(DS_submitDeliveryPOD
													.getSubmitDeliveryPODID());
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										database.deleteSubmitDeliveryPODTableAfterSync(address
												.getJobID());
										database.deleteSubmitDeliveryPODAttachementTableFromQueue(address
												.getWorkorder());

										jobList.remove(item);
										if (jobList.get(index - 1) instanceof String) {
											if (jobList.size() == index
													|| jobList.get(index) instanceof String)
												jobList.remove(index - 1);
										}
										JobAdapter adapter = new JobAdapter();
										adapter.notifyDataSetChanged();
										listView.setAdapter(adapter);
										if (deliverypodImage.size() == 0) {
											database.DeleteDeliveryPODAfteruploadTableBy(address.getWorkorder());
											new CustomAlertDialog(JobsQueue.this,
													"Uploaded Successfully").show();
											// listCategory.initialSync();
										} else {
											uploadImage();
										}

									} else {
										new CustomAlertDialog(JobsQueue.this,
												"Invoking initial sync failed with error code : "
														+ serverCode).show();
									}

									// if (errorString != null) {
									// database.deleteCategoryReleatedTableInDB();
									// new CustomAlertDialog(JobsQueue.this,
									// "Invoking initial sync failed with error code : "
									// + errorString).show();
									// }
									//

									// final Dialog dialog = new
									// Dialog(JobsQueue.this);
									// dialog.getWindow().setBackgroundDrawable(
									// new ColorDrawable(Color.TRANSPARENT));
									// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									// dialog.setContentView(R.layout.alert_upload_final_status);
									// ok = (Button) findViewById(R.id.btn_ok);
									// ok.setOnClickListener(new OnClickListener() {
									// @Override
									// public void onClick(View v) {
									// //deleteRecordFromTable(position);
									// }
									// });
									//
									super.onPostExecute(result);
								}

								private void uploadImage() {

									new AsyncTask<Void, Void, Void>() {
										// String errorString = null;
										protected void onPreExecute() {
											ProgressBar.showCommonProgressDialog(
													JobsQueue.this, "Uploading...");

										}

										@Override
										protected Void doInBackground(
												Void... params) {

											try {
												// ProcessAddressForServer
												// processOrderList
												// = new ProcessAddressForServer>();
												// processOrderList =
												// database.getFinalStatusToUploadToServer();
												for (PODAttachments submitDiligence : deliverypodImage) {

													Log.d("attachment upload", ""
															+ deliverypodImage.size());
													String sessionID = WebServiceConsumer
															.getInstance()
															.signOn(TristarConstants.SOAP_ADDRESS,
																	SessionData
																			.getInstance()
																			.getUsername(),
																	SessionData
																			.getInstance()
																			.getPassword());
													serverCode = SubmitWebServiceConsumer.getInstance()
															.SubmitDeliveryPODAttachments(sessionID, submitDiligence);
													if (isServerCodeAccepted(serverCode)) {
														database.deleteSubmitDeliveryPODAttachementTable(submitDiligence);
													}

												}

											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											// errorString =
											// initialUploadWebServiceCall();

											return null;
										}

										// private String
										// initialUploadWebServiceCall() {
										// // TODO Auto-generated method stub
										// String errorString = null;
										// ArrayList<Object> jobList = new
										// ArrayList<Object>();
										// {
										// ArrayList<ProcessAddressForServer> list =
										// database
										// .getFinalStatusToSubmit();
										// if (list != null && list.size() > 0) {
										// jobList.addAll(list);
										// }
										// }
										// return errorString;
										// }

										@Override
										protected void onPostExecute(Void result) {
											ProgressBar.dismiss();

											if (isServerCodeAccepted(serverCode)) {
//											database.deleteSubmitCourtPODAttachementTable(courtpodImage);

												database.deleteSubmitDeliveryPODTableAfterSync(address
														.getJobID());
												database.deleteSubmitDeliveryPODAttachementTableFromQueue(address
														.getWorkorder());
												database.DeleteDeliveryPODAfteruploadTableBy(address.getWorkorder());

												new CustomAlertDialog(
														JobsQueue.this,
														"Uploaded Successfully")
														.show();

												submitImage.clear();

												// listCategory.initialSync();
											} else {

												new CustomAlertDialog(
														JobsQueue.this,
														"Invoking initial sync failed with error code : "
																+ serverCode)
														.show();
											}

											// if (errorString != null) {
											// database.deleteCategoryReleatedTableInDB();
											// new CustomAlertDialog(JobsQueue.this,
											// "Invoking initial sync failed with error code : "
											// + errorString).show();
											// }
											//

											// final Dialog dialog = new
											// Dialog(JobsQueue.this);
											// dialog.getWindow().setBackgroundDrawable(
											// new
											// ColorDrawable(Color.TRANSPARENT));
											// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
											// dialog.setContentView(R.layout.alert_upload_final_status);
											// ok = (Button)
											// findViewById(R.id.btn_ok);
											// ok.setOnClickListener(new
											// OnClickListener() {
											// @Override
											// public void onClick(View v) {
											// //deleteRecordFromTable(position);
											// }
											// });
											//
											super.onPostExecute(result);
										}
									}.execute();
								}

							}.execute();

						}
					});

					DS_workorder = (TextView) dialog
							.findViewById(R.id.delivery_workorder);
					deliveryServiceListArray = database
							.getDeliveryOrderValues();
					DS_workorder.setText(address.getWorkorder());

					DS_addresss = (TextView) dialog
							.findViewById(R.id.delivery_address);

					DS_job_instructions = (TextView) dialog.findViewById(R.id.delivery_jobinstructions);

					DS_address_instructions = (TextView) dialog.findViewById(R.id.delivery_addressinstructions);

					DS_Casename = (TextView) dialog.findViewById(R.id.delivery_casename);

					DS_Casenumber = (TextView) dialog.findViewById(R.id.delivery_casenumber);

					DS_duedate = (TextView) dialog
							.findViewById(R.id.delivery_due_date);

					DS_priority = (TextView) dialog
							.findViewById(R.id.delivery_priority);

					DS_duetime = (TextView) dialog
							.findViewById(R.id.delivery_due_time);

					DS_date = (TextView) dialog
							.findViewById(R.id.delivery_date);

					DS_date.setText(address.getDate());

					DS_time = (TextView) dialog
							.findViewById(R.id.delivery_time);

					DS_time.setText(address.getTime());

					DS_comments = (EditText) dialog
							.findViewById(R.id.delivery_comments);

					DS_comments.setText(address.getComment());

					DS_feeadvance = (EditText) dialog
							.findViewById(R.id.delivery_fee_advance);

					DS_feeadvance.setText(address.getFeeAdvance());

					DS_weight = (EditText) dialog
							.findViewById(R.id.delivery_weight);

					DS_weight.setText(address.getWeight());

					DS_pieces = (EditText) dialog
							.findViewById(R.id.delivery_pieces);

					DS_pieces.setText(address.getPieces());

					DS_waittime = (EditText) dialog
							.findViewById(R.id.delivery_wait_time);

					DS_waittime.setText(address.getWaitTime());

					DS_Receivedby = (EditText) dialog
							.findViewById(R.id.delivery_received_by);

					DS_Receivedby.setText(address.getReceivedby());

					DS_image = (TextView) dialog
							.findViewById(R.id.delivery_image);

					DS_btn_save = (Button) dialog.findViewById(R.id.btn_save);


					for (int i = 0; i < deliveryServiceListArray.size(); i++) {

						if (deliveryServiceListArray.get(i).getWorkorder()
								.equals(address.getWorkorder())) {
							DS_addresss.setText(deliveryServiceListArray.get(i)
									.getAddressFormattedForDisplay());
							DS_priority.setText(deliveryServiceListArray.get(i)
									.getPriorityTitle());
							DS_duedate.setText(deliveryServiceListArray.get(i)
									.getDueDate());
							DS_duetime.setText(deliveryServiceListArray.get(i)
									.getDueTime());
							DS_Casename.setText(deliveryServiceListArray.get(i)
									.getCaseName());
							DS_Casenumber.setText(deliveryServiceListArray.get(i)
									.getCaseNumber());
							DS_job_instructions.setText(deliveryServiceListArray.get(i)
									.getOrderInstructions());
							DS_address_instructions.setText(deliveryServiceListArray.get(i)
									.getDeliveryInstructions());

						}

					}

					DS_date.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectedTextView = (TextView) v;
							showDialog(DATE_DIALOG_ID);
						}
					});


					DS_time.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							final Dialog dialog = new Dialog(JobsQueue.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.timepicker_dialog);
							final TimePicker tp = (TimePicker) dialog.findViewById(R.id.tp);
							Button set = (Button) dialog.findViewById(R.id.btn_set);
							Button cancel = (Button) dialog.findViewById(R.id.btn_calcel);
							final Calendar c = Calendar.getInstance();

							SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
							String formattedTime = dt.format(c.getTime());
							//btnTime.setText(formattedTime.toLowerCase());
							final String[] time = {formattedTime.toLowerCase()};
							tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
								@Override
								public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
									//Display the new time to app interface
									String AMPM = "am";
									if (hourOfDay > 11) {
										hourOfDay = hourOfDay - 12;
										AMPM = "pm";
									}
									String hr = "";
									if (Integer.toString(hourOfDay).length() == 1) {
										hr = "0" + Integer.toString(hourOfDay);
									} else {
										hr = Integer.toString(hourOfDay);
									}

									String min = "";
									if (Integer.toString(minute).length() == 1) {
										min = "0" + Integer.toString(minute);
									} else {
										min = Integer.toString(minute);
									}


									time[0] = "" + hr + ":" + min + " " + AMPM;

								}
							});

							set.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									DS_time.setText(time[0]);
									dialog.dismiss();

								}
							});

							cancel.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});


							dialog.show();
						}
					});

					DS_btn_save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							String Str_DS_workorder = DS_workorder.getText().toString();
							String Str_DS_addresss = DS_addresss.getText().toString();
							String Str_DS_job_instructions = DS_job_instructions.getText().toString();
							String Str_DS_address_instructions = DS_address_instructions.getText().toString();
							String Str_DS_Casename = DS_Casename.getText().toString();
							String Str_DS_Casenumber = DS_Casenumber.getText().toString();
							String Str_DS_duedate = DS_duedate.getText().toString();
							String Str_DS_priority = DS_priority.getText().toString();
							String Str_DS_duetime = DS_duetime.getText().toString();
							String Str_DS_date = DS_date.getText().toString();
							String Str_DS_time = DS_time.getText().toString();
							String Str_DS_comments = DS_comments.getText().toString();
							String Str_DS_feeadvance = DS_feeadvance.getText().toString();
							String Str_DS_weight = DS_weight.getText().toString();
							String Str_DS_pieces = DS_pieces.getText().toString();
							String Str_DS_waittime = DS_waittime.getText().toString();
							String Str_DS_Receivedby = DS_Receivedby.getText().toString();

							if (Str_DS_Receivedby.toString().equals("")) {
								new CustomAlertDialog(JobsQueue.this, "Please enter Recieved by before Submit").show();
								return;
							}

							SubmitDeliveryPOD submitDeliveryPOD = new SubmitDeliveryPOD();
							submitDeliveryPOD.setWorkorder(Str_DS_workorder);
							submitDeliveryPOD.setProofDate(Str_DS_date); // .replace("/", "-")
							submitDeliveryPOD.setProofTime(Str_DS_time);

							submitDeliveryPOD.setLatitude(DS_submitDeliveryPOD.getLatitude());
							submitDeliveryPOD.setLongitude(DS_submitDeliveryPOD.getLongitude());
							submitDeliveryPOD.setAddressLineitem(DS_submitDeliveryPOD.getAddressLineitem());
							submitDeliveryPOD.setFeeAdvance((int) Double.parseDouble(Str_DS_feeadvance));
							submitDeliveryPOD.setWeight((int) Double.parseDouble(Str_DS_weight));
							submitDeliveryPOD.setPieces((int) Double.parseDouble(Str_DS_pieces));
							submitDeliveryPOD.setWaitTime((int) Double.parseDouble(Str_DS_waittime));
							submitDeliveryPOD.setReceivedBy(Str_DS_Receivedby);
							submitDeliveryPOD.setProofComments(Str_DS_comments);
							submitDeliveryPOD.setSubmitDeliveryPODID(DS_submitDeliveryPOD.getSubmitDeliveryPODID());

							try {
								DS_isSubmitSucess = database.UpdateIntoSubmitDeliveryPODTable(submitDeliveryPOD);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (DS_isSubmitSucess) {
								Toast.makeText(JobsQueue.this, "PickUp Services is successfully updated", Toast.LENGTH_SHORT).show();
								processList();
								dialog.dismiss();
							} else {
								Toast.makeText(JobsQueue.this, "PickUp Services update unsuccessfull ", Toast.LENGTH_SHORT).show();

							}


						}
					});


					deliverypodImage = database
							.getAttachementsFromSubmitDeliveryPODAttachmentTableForUploadForQueue(address
									.getWorkorder());
					DS_image.setText("Signature");

					DS_image.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final Dialog dialog = new Dialog(JobsQueue.this);
							dialog.getWindow().setBackgroundDrawable(
									new ColorDrawable(Color.TRANSPARENT));
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.job_queue_attachment);
							ImageView close = (ImageView) dialog
									.findViewById(R.id.close);

							deliverypodImage = database
									.getAttachementsFromSubmitDeliveryPODAttachmentTableForUploadForQueue(address
											.getWorkorder());

							GridView gridImage = (GridView) dialog
									.findViewById(R.id.lst_image_container);
							gridImage.setNumColumns(3);
							attachedFilesData = new ArrayList<byte[]>();
							for (int i = 0; i < deliverypodImage.size(); i++) {

								Log.d("", ""
										+ deliverypodImage.get(i)
										.getPdfInMemory());

								attachedFilesData.add(Base64.decode(
										deliverypodImage.get(i)
												.getPdfInMemory(),
										Base64.DEFAULT));
							}

							ImageListAdapter adapter = new ImageListAdapter();
							adapter.notifyDataSetChanged();
							gridImage.setAdapter(adapter);

							close.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});

							dialog.show();
						}
					});

					dialog.show();
					Toast.makeText(getApplicationContext(),
							"DELIVERY_SERVICE" + address.getWorkorder(),
							Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void dialogmanner() {

		final String[] returning_string = {""};

		final Dialog dialog = new Dialog(JobsQueue.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);
//		Window window = dialog.getWindow();
//		window.setGravity(Gravity.CENTER);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Select Diligence Item");

		final ListView list = (ListView) dialog.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, Diligence);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//list.setItemChecked(adapter.getPosition(edt_report.getText().toString()),true);


		final String[] select1 = new String[1];
		if (RD_report.getText().toString().length() != 0) {
			select1[0] = RD_report.getText().toString();
		}


		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String selected = "";
				SparseBooleanArray sparseBooleanArray = list.getCheckedItemPositions();
				for (int i = 0; i < list.getCount(); i++) {
					if (sparseBooleanArray.get(i)) {
						selected += list.getItemAtPosition(i).toString() + ", ";

					}
				}
				if (selected.length() != 0) {
					int value = selected.length();
					value = value - 2;
					String selected_value = selected.substring(0, value);
					RD_report.setText(selected_value);
					returning_string[0] = selected_value;
				} else {
					RD_report.setText(selected);
					returning_string[0] = selected;

				}

				dialog.dismiss();
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});


		dialog.show();


	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, datePickerListener, year, month,
						day);
		}
		return null;
	}

	protected void playMp3(byte[] audiofile2) {
		try {
			// create temp file that will hold byte array
			File tempMp3 = File.createTempFile("Audio", "mp3", getCacheDir());
			tempMp3.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempMp3);
			fos.write(audiofile2);
			fos.close();

			// Tried reusing instance of media player
			// but that resulted in system crashes...
			MediaPlayer mediaPlayer = new MediaPlayer();

			// Tried passing path directly, but kept getting
			// "Prepare failed.: status=0x1"
			// so using file descriptor instead
			FileInputStream fis = new FileInputStream(tempMp3);
			mediaPlayer.setDataSource(fis.getFD());

			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException ex) {
			String s = ex.toString();
			ex.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		Intent courtService = new Intent(JobsQueue.this, ListCategory.class);
		startActivity(courtService);
		super.onBackPressed();
	}

	public boolean isDuplicate(String serverCode) {

		return serverCode.contains("submitted");

	}

	private boolean isServerCodeAccepted(String serverCode) {
		return (serverCode != null && serverCode.length() > 0 && (serverCode
				.equalsIgnoreCase("accepted") || isDuplicate(serverCode)));
	}

	public class ViewHolder {

		public TextView title;
		public Button minus, delete;
	}

	class JobAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public JobAdapter() {
			inflater = (LayoutInflater) JobsQueue.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return jobList.size();
		}

		@Override
		public Object getItem(int index) {
			return jobList.get(index);
		}

		@Override
		public long getItemId(int postition) {
			return postition;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View vi, ViewGroup parent) {

			final ViewHolder holder;
			final Object item = jobList.get(position);
			{
				holder = new ViewHolder();
				if (item instanceof String) {
					vi = inflater.inflate(R.layout.list_item_section, null);
					holder.title = (TextView) vi.findViewById(R.id.title);
				} else {
					vi = inflater.inflate(R.layout.job_list_item, null);

					holder.title = (TextView) vi
							.findViewById(R.id.txt_workorder_job);
					holder.minus = (Button) vi.findViewById(R.id.btn_job_minus);

					holder.minus.setVisibility(View.VISIBLE);

					holder.title.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
 							Dialog(position);

						}
					});

					// holder.minus.setOnClickListener(new
					// View.OnClickListener() {
					//
					// @Override
					// public void onClick(View arg0) {
					//
					// final Dialog dialog = new Dialog(JobsQueue.this);
					// dialog.getWindow().setBackgroundDrawable(
					// new ColorDrawable(Color.TRANSPARENT));
					// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// dialog.setContentView(R.layout.alertdialog);
					//
					// TextView txtDialog = (TextView) dialog
					// .findViewById(R.id.txt_dia);
					// txtDialog.setText("Do you want to delete this Job");
					// Button yes = (Button) dialog
					// .findViewById(R.id.btn_yes);
					// Button no = (Button) dialog
					// .findViewById(R.id.btn_no);
					//
					// yes.setOnClickListener(new View.OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// deleteRecordFromTable(position);
					// dialog.dismiss();
					// }
					// });
					// no.setOnClickListener(new View.OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// dialog.dismiss();
					// }
					// });
					//
					// dialog.show();
					//
					// }
					// });

					if (editMode) {
						vi = inflater.inflate(R.layout.list_job_items, null);
						holder.title = (TextView) vi
								.findViewById(R.id.txt_workorder);
						holder.minus = (Button) vi.findViewById(R.id.btn_minus);
						holder.minus.setVisibility(View.GONE);
						holder.delete = (Button) vi
								.findViewById(R.id.btn_delete);
						holder.delete.setVisibility(View.VISIBLE);


						/*holder.minus
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {


										holder.delete
												.setVisibility(View.VISIBLE);
										holder.minus.setVisibility(View.GONE);
									}
								});*/

						/*holder.title
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										holder.delete.setVisibility(View.GONE);
										holder.minus
												.setVisibility(View.VISIBLE);
									}
								});*/

						holder.delete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								deleteRecordFromTable(position);
							}
						});
					}
				}

			}

			if (item instanceof String) {
				holder.title.setText(item.toString());
			} else if (item instanceof ProcessAddressForServer) {
				ProcessAddressForServer address = (ProcessAddressForServer) item;
				holder.title.setText(address.getWorkorder());
			} else if (item instanceof CourtAddressForServer) {
				CourtAddressForServer address = (CourtAddressForServer) item;
				holder.title.setText(address.getWorkorder());
			} else if (item instanceof AddressForServer) {
				AddressForServer address = (AddressForServer) item;
				holder.title.setText(address.getWorkorder());
			} else if (item instanceof SubmitDiligence) {
				SubmitDiligence address = (SubmitDiligence) item;
				holder.title.setText(address.getWorkorder());
			} else if (item instanceof SubmitStatusList) {
				SubmitStatusList address = (SubmitStatusList) item;
				Log.d("Work Order", "" + address.getWorkorder());
				holder.title.setText(address.getWorkorder());
			}

			return vi;
		}

	}

	class ImageListAdapter extends BaseAdapter {

		ArrayList<byte[]> list;
		LayoutInflater inflator;

		public ImageListAdapter() {

			list = attachedFilesData;
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View currentView,
							ViewGroup parent) {
			@SuppressLint({"InflateParams", "ViewHolder"}) final View view = (View) inflator.inflate(
					R.layout.sub_activity_image, null);
			ImageView img_camera = (ImageView) view
					.findViewById(R.id.img_camera);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			byte[] data = list.get(position);
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
			img_camera.setImageBitmap(bmp);

			return view;
		}

	}
}