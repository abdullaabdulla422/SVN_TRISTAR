package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tristar.adapters.DiligenceHolo;
import com.tristar.db.DataBaseHelper;
import com.tristar.db.SyncronizeClass;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.Address;
import com.tristar.object.DiligencePhrase;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnHistoryObject;
import com.tristar.object.SplatterAddress;
import com.tristar.object.SubmitDiligence;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;
import com.tristar.wheelpicker.ArrayWheelAdapter;
import com.tristar.wheelpicker.OnWheelChangedListener;
import com.tristar.wheelpicker.OnWheelScrollListener;
import com.tristar.wheelpicker.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.tristar.main.ProcessOrderDetail.Validate_Clear_ImageSession;

@SuppressWarnings("ALL")
@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class RecordDiligence extends Activity implements OnClickListener,
		OnLongClickListener {

	static final int DATE_DIALOG_ID = 999;
	public static Button btnDate;
	public static Button btnTime;
	public static String kSubmitDiligencesFlag = "subMitDiligencesServiceFlag";
	public static String kSubmitDiligencesAttachemntsFlag = "subMitDiligencesAttachementsServiceFlag";
	public static int _lineItem;
	public static int count3 = 0;
	private static Button selectedButton;
	public PopupWindow popupManner;
	public View mannerView;
	public ArrayList<DiligencePhrase> listStatusValues;
	public ArrayList<String> Diligence = new ArrayList<>();
	public String selectedManner;
	public ArrayList<String> work_order;
	public ArrayList<String> work_order_servee;
	public ArrayList<String> servee_name;
	public ArrayList<String> work_order_enable;
	public ArrayList<Integer> address_line;
	public ArrayList<Integer> address_line_enable;
	public ArrayList<String> servee_name_enable;
	public ArrayList<String> multiple_items;
	public boolean gpslocated = false;
	public double latitude, longitude;
	DataBaseHelper database;
	CheckBox timeCheckBox, spalttercheck;
	String addresgoogle, workorder, serveename;
	GPSTracker gps;
	String serviceFlag, responseForInsertedSubmitDiligencesToServer;
	ArrayList<ReturnHistoryObject> returnHistoryObjects;
	Context context;
	EditText edt_report;
	int processOrderID, diligenceCodeForCurrentStatusInSubmitDiligence,
			addressLineItemForCurrentAddressInSubmitDiligence;
	TextView back, addressActivityAttempt, txt_workorder, txt_popup_cancel,
			txt_popup_done, txtgview, txt_gps;
	Button btnattachment, btnSubmit, btndiligence, buttonadd, btnHistory;
	ImageView image, attach_icon;
	ProcessAddressForServer processOrderToDisplay;
	TextView txtAddressType;
	ArrayList<HashMap<String, String>> temp_final_status;
	String HashMap_key;
	String HashMap_value;
	ArrayList<String> Scand;
	// false = Normal Scanning Process
	// true = Spread diligence selected
	boolean Validate_flow_Change = false;
	ArrayList<String> addressStringArrayList;
	String error = null;
	private WheelView mannerWhhel;
	private boolean wheelScrolled = false;
	private final OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged() {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		@SuppressWarnings("unused")
		public void onScrollStarts(WheelView wheel) {
			wheelScrolled = true;
		}

		@SuppressWarnings("unused")
		public void onScrollEnds(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}

		@Override
		public void onScrollingStarted(WheelView wheel) {
		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
		}
	};
	private double dpi;
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
			selectedButton.setText(dateFormatted);

		}
	};

	@SuppressLint({"DefaultLocale", "InflateParams", "SetTextI18n"})
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.processorder_attempt);

//		if (Validate_Clear_ImageSession) {

//			if (SessionData.getInstance().getDiligenceAttachment() != 1) {
//				SessionData.getInstance().clearAttachments();
//				Validate_Clear_ImageSession = false;
//			}
//		}
		if (SessionData.getInstance().getDiligenceAttachment() != 1) {
			SessionData.getInstance().clearAttachments();
		}
		Scand = new ArrayList<>();
		Scand = SessionData.getInstance().getScanned_Workorder();

		Log.d("info", " Recoed_diligence" + SessionData.getInstance().getAttachedFilesData().size());

		diligenceCodeForCurrentStatusInSubmitDiligence = -1;
		addressLineItemForCurrentAddressInSubmitDiligence = -1;
		database = DataBaseHelper.getInstance();

		if (getIntent().getExtras() != null)
			processOrderID = getIntent().getExtras().getInt("processOrderID");

		context = this;

		edt_report = (EditText) findViewById(R.id.edt_comment);

		edt_report.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				boolean handled = false;
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					handled = true;
				}
				return handled;
			}
		});

		timeCheckBox = (CheckBox) findViewById(R.id.checkBox1);
		txt_workorder = (TextView) findViewById(R.id.txt_workorder);
		addressActivityAttempt = (TextView) findViewById(R.id.textActivityAttemptAddress);

		btnattachment = (Button) findViewById(R.id.btn_attachment);
		btnSubmit = (Button) findViewById(R.id.btn_Submit);
		btnHistory = (Button) findViewById(R.id.btn_history);
		buttonadd = (Button) findViewById(R.id.address);
		txtgview = (TextView) findViewById(R.id.txt_gpsdeligence);
		txt_gps = (TextView) findViewById(R.id.txt_gps_lattitudedeligence);
		btnDate = (Button) findViewById(R.id.btn_datedelivery);
		btnTime = (Button) findViewById(R.id.btn_timedelivery);
		back = (TextView) findViewById(R.id.textviewback);
		btndiligence = (Button) findViewById(R.id.diligenceitem);
		spalttercheck = (CheckBox) findViewById(R.id.check_splatter);
		image = (ImageView) findViewById(R.id.imageButtonback);
		attach_icon = (ImageView) findViewById(R.id.attach_icon);
		txtAddressType = (TextView) findViewById(R.id.txt_address_type);

		timeCheckBox.setChecked(true);

		dpi = getResources().getDisplayMetrics().density;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(FinalStatus.LAYOUT_INFLATER_SERVICE);
		mannerView = inflater.inflate(R.layout.activity_manner_selection, null);
		mannerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (300 * dpi)));
		popupManner = new PopupWindow(mannerView, LayoutParams.WRAP_CONTENT,
				(int) (300 * dpi), true);
		mannerWhhel = (WheelView) mannerView.findViewById(R.id.dp1);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		getGpss();
		txt_gps.setText(latitude + "," + longitude);
		gpslocated = true;
		//txt_gps.setText(latitude + "," + longitude);
		btnHistory.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btndiligence.setOnClickListener(this);
		btnattachment.setOnClickListener(this);
		back.setOnClickListener(this);
		image.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnDate.setOnClickListener(this);
		timeCheckBox.setOnClickListener(this);
		spalttercheck.setOnClickListener(this);
		buttonadd.setOnClickListener(this);

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
		String formattedDate = df.format(c.getTime());
		String formattedTime = dt.format(c.getTime());
		btnDate.setText(formattedDate);
		btnTime.setText(formattedTime.toLowerCase());
		addressActivityAttempt.setOnLongClickListener(this);
		addressActivityAttempt.setOnClickListener(this);

		if (count3 >= 1) {
			String itemcancel = DiligenceHolo.strselected_txt3;
			btndiligence.setText(itemcancel);
		}

		listStatusValues = database
				.getStatusValuesFromDBToDisplayIndiligencesView();
		for (int i = 0; i < listStatusValues.size(); i++) {
			Diligence.add(listStatusValues.get(i).getPhoneTitle());

		}

		if ((SessionData.getInstance().getScanned_Item_Process_ID() != null
				&& SessionData.getInstance().getScanned_Item_Process_ID().size() != 0)
				&& (SessionData.getInstance().getScanned_Workorder() != null
				&& SessionData.getInstance().getScanned_Workorder().size() != 0)) {
			Validate_flow_Change = true;
			try {
				loadAllFieldsFromDB_For_ScannerFlow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Validate_flow_Change = false;
			loadAllFieldsFromDB();
		}

		if (SessionData.getInstance().getTemp_Record_Diligence_Status() != null &&
				SessionData.getInstance().getTemp_Record_Diligence_Status().size() != 0) {
			for (HashMap<String, String> map : SessionData.getInstance().getTemp_Record_Diligence_Status()) {
				for (Map.Entry<String, String> mapEntry : map.entrySet()) {
					HashMap_key = mapEntry.getKey();
					HashMap_value = mapEntry.getValue();

				}
			}
		}
	}

	private void loadAllFieldsFromDB_For_ScannerFlow() throws Exception {

//		processOrderToDisplay = database
//				.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
//		addressLineItemForCurrentAddressInSubmitDiligence = processOrderToDisplay
//				.getAddressLineItem();

		if (SessionData.getInstance().getScanned_Workorder().size() == 0) {
			txt_workorder.setText("N/A");
		} else {
			StringBuilder scanned_workorders = new StringBuilder();
			String str_scanned_workorders;

			int size = SessionData.getInstance().getScanned_Workorder().size() - 1;
			str_scanned_workorders = SessionData.getInstance().getScanned_Workorder().get(size);
			SessionData.getInstance().setImageworkorder(str_scanned_workorders);
			txt_workorder.setText(str_scanned_workorders.toString());
			txt_workorder.setTextColor(getColor(R.color.blackcolor));
		}

		if (SessionData.getInstance().getScanned_Item_Process_ID().size() == 0) {
		} else {
			addressStringArrayList = new ArrayList<>();
			for (int b = 0; b < SessionData.getInstance().getScanned_Item_Process_ID().size(); b++) {
				processOrderToDisplay = database
						.getProcessOrderValuesFromDBToDisplayInDetailView
								(SessionData.getInstance().getScanned_Item_Process_ID().get(b));
				addressStringArrayList.add(processOrderToDisplay.getAddressFormattedForDisplay());

			}

			if (addressStringArrayList.size() != 0) {
				int size = addressStringArrayList.size() - 1;
				String addresss = addressStringArrayList.get(size);
				String address;
				if (addresss.toString().startsWith("Home")) {
					txtAddressType.setText("Home: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(5);
				} else if (addresss.toString().startsWith("Business")) {
					txtAddressType.setText("Business: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(9);
				} else if (addresss.toString().startsWith("Others")) {
					txtAddressType.setText("Others: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(7);
				} else if (addresss.toString().startsWith("Government")) {
					txtAddressType.setText("Government: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(11);
				} else if (addresss.toString().startsWith("Not a physical Address")) {
					txtAddressType.setText("Not a physical Address: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(23);
				} else {
					address = processOrderToDisplay
							.getAddressFormattedForDisplay();
				}
				Log.d("Address_record", address);
				addressActivityAttempt.setText(address);
			}
		}
	}

	private void setvalues() {

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("temp_report")) {
				if (HashMap_value.length() != 0) {
					edt_report.setText(HashMap_value);
				} else {
					edt_report.setText("");
				}
			}
			if (HashMap_key.equals("temp_starting_address")) {
				if (HashMap_value.length() != 0) {
					txtAddressType.setText(HashMap_value);
				} else {
					txtAddressType.setText("");
				}
			}
			if (HashMap_key.equals("temp_addrdess")) {
				if (HashMap_value.length() != 0) {
					addressActivityAttempt.setText(HashMap_value);
				} else {
					addressActivityAttempt.setText("");
				}
			}
			if (HashMap_key.equals("temp_date")) {
				if (HashMap_value.length() != 0) {
					btnDate.setText(HashMap_value);
				} else {
					btnDate.setText("");
				}
			}
			if (HashMap_key.equals("temp_time")) {
				if (HashMap_value.length() != 0) {
					btnTime.setText(HashMap_value);
				} else {
					btnTime.setText("");
				}
			}
			if (HashMap_key.equals("temp_time_checkstate")) {
				if (HashMap_value.length() != 0) {
					String temp_time_checkstate = HashMap_value;
					if (HashMap_value.equals("Checked")) {
						timeCheckBox.setChecked(true);
						btnTime.setEnabled(true);
					} else {
						if (HashMap_value.equals("UnChecked")) {
							timeCheckBox.setChecked(false);
							btnTime.setEnabled(false);
						}
					}
				} else {
				}
			}

			if (HashMap_key.equals("temp_spread_diligence_checkstate")) {
				if (HashMap_value.length() != 0) {
					String temp_spread_diligence_checkstate = HashMap_value;
					if (HashMap_value.equals("Checked")) {
						spalttercheck.setChecked(true);
					} else {
						if (HashMap_value.equals("UnChecked")) {
							spalttercheck.setChecked(false);
						}
					}
				} else {
				}
			}
			if (HashMap_key.equals("temp_history_button_status")) {
				if (HashMap_value.length() != 0) {
					String temp_spread_diligence_checkstate = HashMap_value;
					if (HashMap_value.equals("Clicked")) {
						returnHistoryObjects = SessionData.getInstance().getReturnHistoryObjects();
						Log.d("temp_history_button_status", "Clicked");
					} else {
						if (HashMap_value.equals("unCliked")) {
							Log.d("temp_history_button_status", "unCliked");
						}
					}
				} else {
				}
			}
			if (HashMap_key.equals("temp_Process_ID")) {
				if (HashMap_value.length() != 0) {
					String temp_Process_ID = HashMap_value;
					if (HashMap_value.length() != 0) {
						processOrderID = Integer.parseInt(HashMap_value);
					} else {
						if (getIntent().getExtras() != null)
							processOrderID = getIntent().getExtras().getInt("processOrderID");
					}
				} else {
				}
			}

			if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
				attach_icon.setVisibility(View.VISIBLE);
			} else {
				attach_icon.setVisibility(View.GONE);
			}

		}
	}

	@Override
	protected void onResume() {
		if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.SCANNER){

		}else {
		if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
			attach_icon.setVisibility(View.VISIBLE);
		} else {
			attach_icon.setVisibility(View.GONE);
		}
		}
		super.onResume();
	}

	@Override
	public boolean onLongClick(View v) {

		if (Validate_flow_Change) {

			showDialog();
		} else {

			if (v == addressActivityAttempt) {
				Intent addressOptions = new Intent(RecordDiligence.this,
						AddressOptions.class);
				addressOptions.putExtra("address", addressActivityAttempt.getText()
						.toString());
				addressOptions.putExtra("Latitude",
						processOrderToDisplay.getLatitude());
				addressOptions.putExtra("Longitude",
						processOrderToDisplay.getLongitude());
				startActivity(addressOptions);
			}
		}
		return false;
	}

	@SuppressLint("SetTextI18n")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v == btnSubmit) {
			SessionData.getInstance().getTemp_Record_Diligence_Status().clear();
			if (edt_report.getText().length() == 0) {
				new CustomAlertDialog(
						RecordDiligence.this,
						"Report cannot be empty. Either type some text or select one diligence item then submit")
						.show();
			} else {

				if (Validate_flow_Change) {

					if (SessionData.getInstance().getScanned_Workorder().size() !=0
							&& SessionData.getInstance().getScanned_Item_Process_ID().size() != 0
							){
					save_valuesInDB_ForScanner();
						SessionData.getInstance().getScanned_Workorder().clear();
						SessionData.getInstance().getScanned_Item_Process_ID().clear();
						Log.d("Clear","SessionData.getInstance().getScanned_Item_Process_ID " + SessionData.getInstance().getScanned_Item_Process_ID().size());
						Log.d("Clear","SessionData.getInstance().getScanned_Workorder = " + SessionData.getInstance().getScanned_Workorder().size());

					Intent detailView = new Intent(RecordDiligence.this,
							ListCategory.class);
					new CustomAlertDialog(RecordDiligence.this,
							"Diligence values are saved successfully!", detailView)
							.show();
					}
					else {

						new CustomAlertDialog(RecordDiligence.this,
								"No Workorder available")
								.show();

					}
				} else {

					if (spalttercheck.isChecked()) {
						DiligenceValues();
					} else if (!(spalttercheck.isChecked())) {
						saveValuesInDB(workorder,
								addressLineItemForCurrentAddressInSubmitDiligence);
						Intent detailView = new Intent(RecordDiligence.this,
								ListCategory.class);
						detailView.putExtra("processOrderID", processOrderID);
						new CustomAlertDialog(RecordDiligence.this,
								"Diligence values are saved successfully!", detailView)
								.show();


					}
				}
			}

		} else if (v == btnHistory) {

			new GetHistoryList().execute();

//			Intent HistoryList = new Intent(RecordDiligence.this,History.class);
//			startActivity(HistoryList);
		} else if (v == btndiligence) {
//			popup();
			dialogmanner();

		} else if (v == btnattachment) {
			SessionData.getInstance().setAttach_Navigation("Record_Diligence");
			getAllvaluesforTempStorage();

			if (Validate_flow_Change) {
				int size = SessionData.getInstance().getScanned_Item_Process_ID().size() - 1;
				Intent attach = new Intent(RecordDiligence.this,
						BaseFileIncluder.class);
				attach.putExtra("processOrderID", SessionData.getInstance().getScanned_Item_Process_ID().get(size));
				attach.putExtra("activityId", 1);
				startActivity(attach);

			} else {
				BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.RECORD_DILIGENCE;
				Intent attach = new Intent(RecordDiligence.this,
						BaseFileIncluder.class);
				attach.putExtra("processOrderID", processOrderID);
				attach.putExtra("activityId", 1);
				startActivity(attach);
			}
		} else if (v == back) {

			if (Validate_flow_Change){
				SessionData.getInstance().getTemp_Record_Diligence_Status().clear();
				Intent detailView = new Intent(RecordDiligence.this,
						ProcessOrder.class);
				startActivity(detailView);
			}else {

			finish();
			SessionData.getInstance().getTemp_Record_Diligence_Status().clear();
			SessionData.getInstance().getScanned_Workorder().clear();
			Intent detailView = new Intent(RecordDiligence.this,
					ProcessOrderDetail.class);
			detailView.putExtra("processOrderID", processOrderID);
			startActivity(detailView);
			}
		} else if (v == buttonadd) {
			Intent addIntent = new Intent(RecordDiligence.this,
					AddAddressRecordDiligence.class);
			addIntent.putExtra("processOrderID", processOrderID);
			startActivity(addIntent);
		} else if (v == image) {
			finish();
			Intent detailView = new Intent(RecordDiligence.this,
					ProcessOrderDetail.class);
			detailView.putExtra("processOrderID", processOrderID);
			startActivity(detailView);
		} else if (v == btnTime) {
			if (timeCheckBox.isChecked()) {
				selectedButton = (Button) v;
				final Dialog dialog = new Dialog(context);
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

						btnTime.setText(time[0]);
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
//				DialogFragment newFragment = new TimePickerFragment();
//				newFragment.show(getFragmentManager(), "timePicker");
			}
		} else if (v == btnDate) {
			selectedButton = (Button) v;
			showDialog(DATE_DIALOG_ID);
		} else if (v == addressActivityAttempt) {

			if (Validate_flow_Change) {

			} else {
				double lat = Double
						.parseDouble(processOrderToDisplay.getLatitude());
				double lon = Double.parseDouble(processOrderToDisplay
						.getLongitude());

				String uriBegin = "geo:" + lat + "," + lon;
				String uriString = uriBegin + "?q="
						+ addressActivityAttempt.getText().toString();
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		} else if (v == timeCheckBox) {
			if (timeCheckBox.isChecked()) {
				btnTime.setEnabled(true);
			} else {
				btnTime.setEnabled(false);
			}
		}
	}

	private void save_valuesInDB_ForScanner() {

		boolean isSubmitSuccess = false;
		boolean sucess = false;

		if (gpslocated) {
			getGps();
		}
		if (diligenceCodeForCurrentStatusInSubmitDiligence == -1) {
			diligenceCodeForCurrentStatusInSubmitDiligence = 0;
		}

		for (int i = 0; i < SessionData.getInstance().getScanned_Item_Process_ID().size(); i++) {

			if (i < SessionData.getInstance().getScanned_Item_Process_ID().size()){
			SubmitDiligence submitDiligencesInDB = new SubmitDiligence();
			try {
				processOrderToDisplay = database
						.getProcessOrderValuesFromDBToDisplayInDetailView(SessionData.getInstance().getScanned_Item_Process_ID().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}

			Log.d("Scanner", "processOrderToDisplay = "+ processOrderToDisplay);

			submitDiligencesInDB.setPROCCESS_ID(SessionData.getInstance().getScanned_Item_Process_ID().get(i));
				Log.d("Scanner", "PROCCESS_ID = "+ SessionData.getInstance().getScanned_Item_Process_ID().get(i));
			submitDiligencesInDB.setWorkorder(SessionData.getInstance().getScanned_Workorder().get(i));
				Log.d("Scanner", "Workorder = "+ SessionData.getInstance().getScanned_Workorder().get(i));
			submitDiligencesInDB.setAddressLineItem(processOrderToDisplay.getAddressLineItem());
			submitDiligencesInDB.setDiligenceDate(btnDate.getText().toString());
			if (timeCheckBox.isChecked()) {
				submitDiligencesInDB.setDiligenceTime(btnTime.getText().toString());
			} else {
				submitDiligencesInDB.setDiligenceTime("");
			}
			submitDiligencesInDB.setReport(edt_report.getText().toString());
			submitDiligencesInDB
					.setDiligenceCode(diligenceCodeForCurrentStatusInSubmitDiligence);

			submitDiligencesInDB.setServerCode("");
			submitDiligencesInDB.setLineItem(_lineItem);
			Log.d("Line Item",""+_lineItem);
			submitDiligencesInDB.setLatitude(String.valueOf(latitude));
			submitDiligencesInDB.setLongitude(String.valueOf(longitude));

			isSubmitSuccess = database.insertOrUpdateRecordDiligenceInDB(
					submitDiligencesInDB, true);
//			if (_lineItem == 0) {
//
//			} else {
//				isSubmitSuccess = database.insertOrUpdateRecordDiligenceInDB(
//						submitDiligencesInDB, false);
//			}
			if (isSubmitSuccess == false) {
				String Data_Saved = "Data is not saved!";
				Log.d("Scanned_Values", "" + Data_Saved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
			} else {
				String Data_Saved = "Data saved!";
				Log.d("Scanned_Values", "" + Data_Saved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
			}

			if (isSubmitSuccess) {
				String imageSaved = "";
				SubmitDiligence submitImage = new SubmitDiligence();
				ProcessAddressForServer Image_Process_Address_For_server = null;
				String imageSaved1= "";
				if (_lineItem == 0) {
					submitImage.setLineItem(database
							.getLastinsertedLineItemFromSubmitDiligences());
					database.deleteAttachmentsFromattachemntsTableBy(submitImage
							.getLineItem());
					Log.d("deleteAttachmentsFromattachemntsTableBy"," Yes  getLineItem ");

				} else {
					database.deleteAttachmentsFromattachemntsTableBy(_lineItem);
					submitImage.setLineItem(_lineItem);
					Log.d("deleteAttachmentsFromattachemntsTableBy"," Yes  _lineItem");
				}

				ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance()
						.getAttachedFilesData();
				for (int j = 0; j < imageArrayToSaveInDB.size(); j++) {

					try {
						Image_Process_Address_For_server = database
								.getProcessOrderValuesFromDBToDisplayInDetailView(SessionData.getInstance().getScanned_Item_Process_ID().get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
					submitImage.setWorkorder(SessionData.getInstance().getScanned_Workorder().get(i));
					Log.d("Record_For_Scanner","setWorkorder = "+SessionData.getInstance().getScanned_Workorder().get(i));

					submitImage.setAddressLineItem(Image_Process_Address_For_server.getAddressLineItem());
					Log.d("Record_For_Scanner","setAddressLineItem = "+Image_Process_Address_For_server.getAddressLineItem());

					submitImage.setAttachementsFileName(SessionData.getInstance().getScanned_Workorder().get(i) + "-"
							+ Image_Process_Address_For_server.getAddressLineItem() + "-image-" + i);
					Log.d("Record_For_Scanner","setAttachementsFileName = "+SessionData.getInstance().getScanned_Workorder().get(i) + "-"
							+ Image_Process_Address_For_server.getAddressLineItem() + "-image-" + i);

					submitImage.setAttachementBase64String(Base64.encodeToString(
							imageArrayToSaveInDB.get(j), Base64.DEFAULT));
					Log.d("Record_For_Scanner","setAttachementBase64String = "+Base64.encodeToString(

							imageArrayToSaveInDB.get(j), Base64.DEFAULT));
					submitImage.setAttachementOfUrlString("");

					sucess = true;

					if (_lineItem == 0) {
						sucess = database
								.insertOrUpdateAttachmentsOfSubmitDiligences(
										submitImage);
					} else {
						sucess = database
								.insertOrUpdateAttachmentsOfSubmitDiligences(
										submitImage);
					}
					if (sucess == false) {
						imageSaved = "Image is not saved!";
						Log.d("Scanned_Values", "" + imageSaved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
					} else {
						imageSaved = "Image  saved!";
						Log.d("Scanned_Values", "" + imageSaved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
					}
				}
				byte[] audioArray = SessionData.getInstance().getAudioData();
				if (audioArray != null && audioArray.length > 0) {

					submitImage.setWorkorder(SessionData.getInstance().getScanned_Workorder().get(i));
					submitImage.setAddressLineItem(processOrderToDisplay.getAddressLineItem());
					submitImage.setAttachementsFileName(SessionData.getInstance().getScanned_Workorder().get(i) + "-"
							+ (long) processOrderToDisplay.getAddressLineItem() + "-Audio");

					Log.d("Attached Audio", "" + SessionData.getInstance().getScanned_Workorder().get(i) + "-"
							+ (long) processOrderToDisplay.getAddressLineItem() + "-Audio");
					submitImage.setAttachementOfUrlString("");

					{
						submitImage.setAttachementBase64String(Base64
								.encodeToString(audioArray, Base64.DEFAULT));
						sucess = true;

						if (_lineItem == 0) {
							sucess = database
									.insertOrUpdateAttachmentsOfSubmitDiligences(
											submitImage);
						} else {
							sucess = database
									.insertOrUpdateAttachmentsOfSubmitDiligences(
											submitImage);
						}

						if (sucess == false) {
							String Audio_Saved = "Audio is not saved!";
							Log.d("Scanned_Values", "" + Audio_Saved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
						} else {
							String Audio_Saved = "Audio  saved!";
							Log.d("Scanned_Values", "" + Audio_Saved + "for workorder = " + SessionData.getInstance().getScanned_Workorder().get(i));
						}


					}
				}

			}

			if (isSubmitSuccess) {
				Log.d("Final_status", "Every data Saved_Succesfully");
			} else {
				Log.d("Final_status", "Every data Saved UnSuccesfully");

			}
			if (sucess) {
				Log.d("Final_status", "Every Image and audio Saved_Succesfully");
			} else {
				Log.d("Final_status", "Every Image and audio Saved UnSuccesfully");

			}
				Log.d("ArryList_Loop_Success"," i = " + i
						+ "SessionData.getInstance().getScanned_Item_Process_ID().size() = " + SessionData.getInstance().getScanned_Item_Process_ID().size());


			}else {
				Log.d("ArryList_Loop_Error"," i = " + i
						+ "SessionData.getInstance().getScanned_Item_Process_ID().size() = " + SessionData.getInstance().getScanned_Item_Process_ID().size());

			}
		}


	}

	private void showDialog(){

		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
		aBuilder.setTitle("Scanned Workorders");
		Scanned_Value_Adapter adapter = new Scanned_Value_Adapter(addressStringArrayList, this);
		aBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		aBuilder.show();
	}

	private void getAllvaluesforTempStorage() {

		temp_final_status = new ArrayList<>();
		HashMap<String, String> final_status_values = new HashMap<>();
		String temp_report = edt_report.getText().toString();
		String temp_starting_address = txtAddressType.getText().toString();
		String temp_addrdess = addressActivityAttempt.getText().toString();
		String temp_date = btnDate.getText().toString();
		String temp_time = btnTime.getText().toString();
		String temp_time_checkstate = "";
		String temp_spread_diligence_checkstate = "";
		String temp_history_button_status = "";
		String temp_Process_ID = "";


		//1
		if (temp_report.length() != 0) {
			final_status_values.put("temp_report", temp_report);
		} else {
			final_status_values.put("temp_report", "");
		}
		temp_final_status.add(final_status_values);
		//2
		if (temp_starting_address.length() != 0) {
			final_status_values.put("temp_starting_address", temp_starting_address);
		} else {
			final_status_values.put("temp_starting_address", "");
		}
		temp_final_status.add(final_status_values);
		//3
		if (temp_addrdess.length() != 0) {
			final_status_values.put("temp_addrdess", temp_addrdess);
		} else {
			final_status_values.put("temp_addrdess", "");
		}
		temp_final_status.add(final_status_values);
		//4
		if (temp_date.length() != 0) {
			final_status_values.put("temp_date", temp_date);
		} else {
			final_status_values.put("temp_date", "");
		}
		temp_final_status.add(final_status_values);
		//5
		if (temp_time.length() != 0) {
			final_status_values.put("temp_time", temp_time);
		} else {
			final_status_values.put("temp_time", "");
		}
		temp_final_status.add(final_status_values);
		//6
		if (timeCheckBox.isChecked()) {
			final_status_values.put("temp_time_checkstate", "Checked");
		} else {
			final_status_values.put("temp_time_checkstate", "UnChecked");
		}
		temp_final_status.add(final_status_values);
		//7
		if (spalttercheck.isChecked()) {
			final_status_values.put("temp_spread_diligence_checkstate", "Checked");
		} else {
			final_status_values.put("temp_spread_diligence_checkstate", "UnChecked");
		}
		temp_final_status.add(final_status_values);
		//8
		if (SessionData.getInstance().getReturnHistoryObjects() != null
				&& SessionData.getInstance().getReturnHistoryObjects().size() != 0) {
			temp_history_button_status = "Clicked";
			final_status_values.put("temp_history_button_status", temp_history_button_status);
		} else {
			temp_history_button_status = "unCliked";
			final_status_values.put("temp_history_button_status", temp_history_button_status);
		}
		//9
		if (processOrderID != 0) {
			temp_Process_ID = String.valueOf(processOrderID);
			final_status_values.put("temp_Process_ID", temp_Process_ID);
		} else {
			final_status_values.put("temp_Process_ID", "");
		}
		if (temp_final_status != null && temp_final_status.size() != 0) {
			SessionData.getInstance().getTemp_Record_Diligence_Status().addAll(temp_final_status);
		} else {
		}

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, datePickerListener, year, month,
						day);
		}
		return null;
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
		if (Validate_flow_Change){
			SessionData.getInstance().getScanned_Workorder().clear();
			Intent detailView = new Intent(RecordDiligence.this,
					ProcessOrder.class);
			startActivity(detailView);
		}else {
			finish();
			SessionData.getInstance().getTemp_Record_Diligence_Status().clear();
			SessionData.getInstance().getScanned_Workorder().clear();
			Intent intent = new Intent(RecordDiligence.this,
					ProcessOrderDetail.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
		}

	}

	public void loadAllFieldsFromDB() {
		try {
			processOrderToDisplay = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
			addressLineItemForCurrentAddressInSubmitDiligence = processOrderToDisplay
					.getAddressLineItem();

			if (processOrderToDisplay.getWorkorder().length() == 0) {
				txt_workorder.setText("N/A");
			} else {
				txt_workorder.setText(processOrderToDisplay.getWorkorder());
				workorder = processOrderToDisplay.getWorkorder();
				SessionData.getInstance().setWorkid(
						processOrderToDisplay.getWorkorder());
			}
			if (processOrderToDisplay.getAddressFormattedForDisplay().length() == 0) {
				addressActivityAttempt.setText("N/A");
			} else {
				String addresss = processOrderToDisplay
						.getAddressFormattedForDisplay();
				String address;
				if (addresss.toString().startsWith("Home")) {
					txtAddressType.setText("Home: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(5);
				} else if (addresss.toString().startsWith("Business")) {
					txtAddressType.setText("Business: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(9);
				} else if (addresss.toString().startsWith("Others")) {
					txtAddressType.setText("Others: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(7);
				} else if (addresss.toString().startsWith("Government")) {
					txtAddressType.setText("Government: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(11);
				} else if (addresss.toString().startsWith("Not a physical Address")) {
					txtAddressType.setText("Not a physical Address: ");
					address = processOrderToDisplay
							.getAddressFormattedForDisplay().substring(23);
				} else {
					address = processOrderToDisplay
							.getAddressFormattedForDisplay();
				}
				Log.d("Address_record", address);
				addressActivityAttempt.setText(address);
			}
			addresgoogle = processOrderToDisplay.getAddressFormattedForGoogle();
		} catch (Exception e) {
			Log.d("Inside Activity Attempt", "Error in fetching data from db");

		}
	}

	@SuppressLint("SetTextI18n")
	private void DiligenceValues() {
		final ArrayList<SplatterAddress> countResult = database
				.getProcessAddressComparison(addresgoogle, workorder, serveename);

		if (countResult.size() == 1) {
			saveValuesInDB(workorder,
					addressLineItemForCurrentAddressInSubmitDiligence);

			Intent detailView = new Intent(RecordDiligence.this,
					ListCategory.class);
			detailView.putExtra("processOrderID", processOrderID);
			startActivity(detailView);
			Toast.makeText(RecordDiligence.this, "Diligence values are saved successfully!", Toast.LENGTH_SHORT).show();

		} else {
			final Dialog mdialog = new Dialog(context);
			mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mdialog.setContentView(R.layout.splatterdialog);

			TextView text = (TextView) mdialog.findViewById(R.id.splattermsg);
			text.setText("You have "
					+ (countResult.size() - 1)
					+ " other jobs at the same address, would you "
					+ "like to spread this diligence (Record Diligence) across all jobs?");

			Button dialogButtonYes = (Button) mdialog.findViewById(R.id.btn_yes);
			dialogButtonYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (countResult.size() > 1) {
						work_order_servee = new ArrayList<>();
						work_order = new ArrayList<>();
						work_order_enable = new ArrayList<>();
						servee_name = new ArrayList<>();
						servee_name_enable = new ArrayList<>();
						address_line = new ArrayList<>();
						address_line_enable = new ArrayList<>();

						for (int i = 0; i < countResult.size(); i++) {

							if (workorder.toString().equalsIgnoreCase(countResult.get(i).getWorkorder())) {

							} else {
								work_order.add(countResult.get(i).getWorkorder());
								address_line.add(countResult.get(i).getAddressLineItem());
								servee_name.add(countResult.get(i).getServeename());
							}

						}

						final Dialog dialog = new Dialog(RecordDiligence.this);
						dialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.dialogbox_2);

						int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
						int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

						dialog.getWindow().setLayout(width, height);

						TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
						txt_Header.setText("Same Address");

						for (int i = 0; i < work_order.size(); i++) {

							work_order_servee.add((work_order.get(i).toString() + " " + "/" + " " + servee_name.get(i).toString()));

						}

						final ListView list = (ListView) dialog.findViewById(R.id.list);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecordDiligence.this, R.layout.choise_list, work_order_servee);
						list.setAdapter(adapter);
						list.setItemsCanFocus(false);
						// we want multiple clicks
						list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

						list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

								String work = "";

								if (list.isItemChecked(position)) {

									if (list.getItemAtPosition(position).toString().length() != 0) {
										for (int i = 0; i < list.getItemAtPosition(position).toString().length(); i++) {
											Character character = list.getItemAtPosition(position).toString().charAt(i);
											if (character.toString().equals("/")) {
												work = list.getItemAtPosition(position).toString().substring(0, i);
												Log.d("workorder_trim", "" + work);
												break;
											}
										}
									}

									work_order_enable.add(work);
									address_line_enable.add(address_line.get(position));
									servee_name_enable.add(servee_name.get(position).toString());

								} else {

									if (list.getItemAtPosition(position).toString().length() != 0) {
										for (int i = 0; i < list.getItemAtPosition(position).toString().length(); i++) {
											Character character = list.getItemAtPosition(position).toString().charAt(i);
											if (character.toString().equals("/")) {
												work = list.getItemAtPosition(position).toString().substring(0, i);
												Log.d("workorder_trim", "" + work);
												break;
											}
										}
									}
									work_order_enable.remove(work);
									address_line_enable.remove(address_line.get(position));
									servee_name_enable.remove(servee_name.get(position).toString());
								}
							}
						});
						Button Save = (Button) dialog.findViewById(R.id.save);
						Save.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								work_order_enable.add(workorder);
								address_line_enable.add(addressLineItemForCurrentAddressInSubmitDiligence);

								for (int j = 0; j < work_order_enable.size(); j++) {

									saveValuesInDB(work_order_enable.get(j).toString(),
											address_line_enable.get(j));
									Log.d("item check", "" + work_order_enable);

								}
								dialog.dismiss();
								mdialog.dismiss();

								Intent detailView = new Intent(RecordDiligence.this,
										ListCategory.class);
								detailView.putExtra("processOrderID", processOrderID);
								startActivity(detailView);
								Toast.makeText(RecordDiligence.this, "Diligence values are saved successfully!", Toast.LENGTH_SHORT).show();
					/*new CustomAlertDialog(RecordDiligence.this,
							"Diligence values are saved successfully!", detailView)
							.show();*/

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
						getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
				/*for (int i = 0; i < countResult.size(); i++) {
					saveValuesInDB(countResult.get(i).getWorkorder(),
							countResult.get(i).getAddressLineItem());
					Log.d("item check", "" + countResult);
					mdialog.dismiss();

				}

				Intent detailView = new Intent(RecordDiligence.this,
						ListCategory.class);
				detailView.putExtra("processOrderID", processOrderID);
				startActivity(detailView);
				new CustomAlertDialog(RecordDiligence.this,
						"Diligence values are saved successfully!", detailView)
						.show();*/

					}
				}

			});


			Button dialogButtonNo = (Button) mdialog.findViewById(R.id.btn_no);
			dialogButtonNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					saveValuesInDB(workorder,
							addressLineItemForCurrentAddressInSubmitDiligence);
					mdialog.dismiss();
					Intent detailView = new Intent(RecordDiligence.this,
							ListCategory.class);
					detailView.putExtra("processOrderID", processOrderID);
					startActivity(detailView);
					Toast.makeText(RecordDiligence.this, "Diligence values are saved successfully!", Toast.LENGTH_SHORT).show();

				/*new CustomAlertDialog(RecordDiligence.this,
						"Diligence values are saved successfully!", detailView).show();*/

				}

			});
			mdialog.show();
			getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
		}


	}

	public void saveValuesInDB(String wrk_order, int addressline) {
		if (gpslocated) {
			getGps();
		}
		if (diligenceCodeForCurrentStatusInSubmitDiligence == -1) {
			diligenceCodeForCurrentStatusInSubmitDiligence = 0;
		}
		SubmitDiligence submitDiligencesInDB = new SubmitDiligence();
		submitDiligencesInDB.setPROCCESS_ID(processOrderID);
		submitDiligencesInDB.setWorkorder(wrk_order);
		submitDiligencesInDB.setAddressLineItem(addressline);
		submitDiligencesInDB.setDiligenceDate(btnDate.getText().toString());
		if (timeCheckBox.isChecked()) {
			submitDiligencesInDB.setDiligenceTime(btnTime.getText().toString());
		} else {
			submitDiligencesInDB.setDiligenceTime("");
		}
		submitDiligencesInDB.setReport(edt_report.getText().toString());
		submitDiligencesInDB
				.setDiligenceCode(diligenceCodeForCurrentStatusInSubmitDiligence);
		int de = diligenceCodeForCurrentStatusInSubmitDiligence;
		submitDiligencesInDB.setServerCode("");
		submitDiligencesInDB.setLineItem(_lineItem);
		submitDiligencesInDB.setLatitude(String.valueOf(latitude));
		submitDiligencesInDB.setLongitude(String.valueOf(longitude));

		boolean isSubmitSuccess = true;

		if (_lineItem == 0) {
			isSubmitSuccess = database.insertOrUpdateRecordDiligenceInDB(
					submitDiligencesInDB, true);
		} else {
			isSubmitSuccess = database.insertOrUpdateRecordDiligenceInDB(
					submitDiligencesInDB, false);
		}

		if (SessionData.getInstance().getTheAddressType() != 0
				|| SessionData.getInstance().getCity() != ""
				|| SessionData.getInstance().getState() != ""
				|| SessionData.getInstance().getZip() != "") {
			Address submitAddProcessAddressToDB = new Address();

			submitAddProcessAddressToDB.setStreet(SessionData.getInstance()
					.getStreet());
			Log.d("Session data one", ""
					+ SessionData.getInstance().getStreet());
			submitAddProcessAddressToDB.setState(SessionData.getInstance()
					.getState());

			submitAddProcessAddressToDB.setCity(SessionData.getInstance()
					.getCity());
			submitAddProcessAddressToDB.setLineItem(_lineItem);
			submitAddProcessAddressToDB.setZip(SessionData.getInstance()
					.getZip());
			submitAddProcessAddressToDB.setTheAddressType(SessionData
					.getInstance().getTheAddressType());
			submitAddProcessAddressToDB.setWorkorder(wrk_order);

			boolean sucessadress = true;
			try {

				sucessadress = database
						.insertProcessAddressfromServer(submitAddProcessAddressToDB);

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sucessadress == false) {
				Toast.makeText(getApplicationContext(), "Addres not saved",
						Toast.LENGTH_LONG).show();
			}
		}
		if (isSubmitSuccess) {
			String imageSaved = "";
			SubmitDiligence submitImage = new SubmitDiligence();
			if (_lineItem == 0) {
				submitImage.setLineItem(database
						.getLastinsertedLineItemFromSubmitDiligences());
				database.deleteAttachmentsFromattachemntsTableBy(submitImage
						.getLineItem());
			} else {
				database.deleteAttachmentsFromattachemntsTableBy(_lineItem);
				submitImage.setLineItem(_lineItem);
			}

			ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance()
					.getAttachedFilesData();
			for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
				submitImage.setWorkorder(wrk_order);
				submitImage.setAddressLineItem(addressline);
				submitImage.setAttachementsFileName(wrk_order + "-"
						+ addressline + "-image-" + i);
				submitImage.setAttachementBase64String(Base64.encodeToString(
						imageArrayToSaveInDB.get(i), Base64.DEFAULT));
				submitImage.setAttachementOfUrlString("");

				boolean sucess = true;
				if (_lineItem == 0) {
					sucess = database
							.insertOrUpdateAttachmentsOfSubmitDiligences(
									submitImage);
				} else {
					sucess = database
							.insertOrUpdateAttachmentsOfSubmitDiligences(
									submitImage);
				}
				if (sucess == false) {
					imageSaved = "Image is not saved!";
				}
			}
			byte[] audioArray = SessionData.getInstance().getAudioData();
			if (audioArray != null && audioArray.length > 0) {

				submitImage.setWorkorder(wrk_order);
				submitImage.setAddressLineItem(addressline);
				submitImage.setAttachementsFileName(wrk_order + "-"
						+ (long) addressline + "-Audio");

				Log.d("Attached Audio", "" + wrk_order + "-"
						+ (long) addressline + "-Audio");
				submitImage.setAttachementOfUrlString("");

				{
					submitImage.setAttachementBase64String(Base64
							.encodeToString(audioArray, Base64.DEFAULT));
					boolean sucess = true;
					if (_lineItem == 0) {
						sucess = database
								.insertOrUpdateAttachmentsOfSubmitDiligences(
										submitImage);
					} else {
						sucess = database
								.insertOrUpdateAttachmentsOfSubmitDiligences(
										submitImage);
					}

					if (sucess == false) {
						new CustomAlertDialog(RecordDiligence.this,
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
				new CustomAlertDialog(RecordDiligence.this, imageSaved).show();
				SessionData.getInstance().clearAttachments();
			}

		}
	}

	private void submitDiligencesToServerAtObject() {

		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				ProgressBar.showCommonProgressDialog(RecordDiligence.this,
						"Uploading Data, please wait...");
				if (!gpslocated) {
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {

					error = SyncronizeClass.instance().sessionIDForFinalSync = WebServiceConsumer
							.getInstance().signOn(
									TristarConstants.SOAP_ADDRESS,
									SessionData.getInstance().getUsername(),
									SessionData.getInstance().getPassword());

					error = SyncronizeClass.instance().submitDiligence(
							DataBaseHelper.getInstance());
					if (error == null) {
						error = SyncronizeClass.instance()
								.submitAttemptAttachments(
										DataBaseHelper.getInstance());
					}
				} catch (Exception e) {
					error = "Diligences value is not saved! Please try again";

				} finally {
					try {
						WebServiceConsumer
								.getInstance()
								.signOff(
										SyncronizeClass.instance().sessionIDForFinalSync);
					} catch (Exception ex) {
					}
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				ProgressBar.dismiss();

				if (error == null) {
					Intent detailView = new Intent(RecordDiligence.this,
							ProcessOrderDetail.class);
					detailView.putExtra("processOrderID", processOrderID);
					new CustomAlertDialog(RecordDiligence.this,
							"Diligence values are saved successfully!", detailView)
							.show();
				} else {
					Intent detailView = new Intent(RecordDiligence.this,
							ProcessOrderDetail.class);
					detailView.putExtra("processOrderID", processOrderID);
					new CustomAlertDialog(RecordDiligence.this, error,
							detailView).show();
				}

			}

		}.execute();

	}

	@SuppressLint("SetTextI18n")
	private void dialogmanner() {
		final Dialog dialog = new Dialog(RecordDiligence.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);
//		Window window = dialog.getWindow();
//		window.setGravity(Gravity.CENTER);

		int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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

		final String[] select = new String[1];
		if (edt_report.getText().toString().length() != 0) {
			select[0] = edt_report.getText().toString();
		}


		/*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});*/

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
					edt_report.setText(selected_value);
				} else {
					edt_report.setText(selected);
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

	@SuppressLint("RtlHardcoded")
	public void paopup() {
		initWheel1(mannerWhhel);
		txt_popup_cancel = (TextView) mannerView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) mannerView.findViewById(R.id.txt_done);
		popupManner.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupManner.showAtLocation(mannerView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));

		popupManner.update();
		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupManner.dismiss();
			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@SuppressLint("SetTextI18n")
			@Override
			public void onClick(View arg0) {
				updateStatus();
				if (edt_report.getText().length() == 0) {
					edt_report.setText(selectedManner.trim());
				} else {
					edt_report.setText(edt_report.getText().toString().trim()
							+ ", " + selectedManner.trim());
				}

				popupManner.dismiss();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void initWheel1(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, listStatusValues));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	public void updateStatus() {
		diligenceCodeForCurrentStatusInSubmitDiligence = getWheelValue(mannerWhhel);
		selectedManner = listStatusValues.get(getWheelValue(mannerWhhel))
				.getPhoneTitle();

	}

	@SuppressWarnings("unused")
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	private int getWheelValue(WheelView wheel) {
		return wheel.getCurrentItem();
	}

	public void getGps() {
		gps = new GPSTracker(RecordDiligence.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
//			new CustomAlertDialog(RecordDiligence.this, latitude + ","
//					+ longitude + "\n" + "Current GPS is Attached").show();
		} else {
			gps.showSettingsAlert();
		}
	}

	public void getGpss() {
		gps = new GPSTracker(RecordDiligence.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

		} else {
			gps.showSettingsAlert();
		}
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			return new TimePickerDialog(getActivity(),
					AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
//			return new TimePickerDialog(getActivity(), this, hour, minute,
//					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker arg0, int hourOfDay, int minOfHour) {

			Calendar datetime = Calendar.getInstance();

			datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			datetime.set(Calendar.MINUTE, minOfHour);

			SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
			selectedButton.setText(dateformat.format(datetime.getTime()));
		}
	}

	private class GetHistoryList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			com.tristar.main.ProgressBar.showCommonProgressDialog(RecordDiligence.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (Validate_flow_Change){

					int process_id_size = SessionData.getInstance().getScanned_Item_Process_ID().size() - 1 ;
					int scanned_workorder_size = SessionData.getInstance().getScanned_Workorder().size() -1;
					processOrderToDisplay = database
							.getProcessOrderValuesFromDBToDisplayInDetailView(SessionData.getInstance().getScanned_Item_Process_ID().get(process_id_size));
					String sessionId = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					returnHistoryObjects = WebServiceConsumer.getInstance()
							.returnHistory(
									sessionId,
									SessionData.getInstance().getScanned_Workorder().get(scanned_workorder_size)
							);
				}else {

					processOrderToDisplay = database
							.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
					String sessionId = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					returnHistoryObjects = WebServiceConsumer.getInstance()
							.returnHistory(
									sessionId,
									processOrderToDisplay.getWorkorder()
							);
				}



			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				returnHistoryObjects = null;
			} catch (Exception e) {
				e.printStackTrace();
				returnHistoryObjects = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			com.tristar.main.ProgressBar.dismiss();
			if (returnHistoryObjects != null && returnHistoryObjects.size() == 0) {
				Log.d("History List", " " + returnHistoryObjects.size());
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.alertbox);

				TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
				text.setText("No History for this Workorder");

				Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				dialog.show();

			} else {
				SessionData.getInstance().setReturnHistoryObjects(returnHistoryObjects);
				Intent HistoryList = new Intent(RecordDiligence.this, History.class);
				startActivity(HistoryList);

			}

		}
	}

}
