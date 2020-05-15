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
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.AddProcessAddress;
import com.tristar.object.Address;
import com.tristar.object.AddressForServer;
import com.tristar.object.MannerOfService;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnAppOptionsObject;
import com.tristar.object.SplatterAddress;
import com.tristar.object.SubmitDiligence;
import com.tristar.object.SubmitFinalStatus;
import com.tristar.signature.CaptureActivity;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;
import com.tristar.wheelpicker.ArrayWheelAdapter;
import com.tristar.wheelpicker.OnWheelChangedListener;
import com.tristar.wheelpicker.OnWheelScrollListener;
import com.tristar.wheelpicker.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class FinalStatus extends Activity implements OnClickListener,
		OnLongClickListener {
	static final int DATE_DIALOG_ID = 999;
	public static Button btnDate;
	public static Button btnTime, btnServeAudio;
	public static int PICKUP_ONLY = 101, DELIVERY_ONLY = 102,
			PICKUP_AND_DELIVERY = 103;
	static int count4;
	private static Button selectedButton;
	final Context context = this;
	public PopupWindow popupManner, popupAge, popupHair, popupRace,
			popupWeight, popupHeight, popupEye;
	public View mannerView, ageView, hairView, raceView, weightView,
			heightView, eyeView;
	public ArrayList<String> wheelMenu1;
	public ArrayList<String> mannerofservice;
	public ArrayList<String> relationship;
	public ArrayList<String> mannerofserviceCode;
	public String selectedManner, selectedAge, selectedHair, selectedRace,
			selectedWeight, selectedHeight, selectedEye;
	public ArrayList<String> wheelMenuAge;
	public ArrayList<String> wheelMenuHair;
	public ArrayList<String> wheelMenuRace;
	public ArrayList<String> wheelMenuWeight;
	public ArrayList<String> wheelMenuHeight;
	public ArrayList<String> wheelMenuEye;
	public boolean gpslocated = false;
	public double latitude, longitude;
	public int addressLineItem;
	public String street, city, state, zip;
	public ArrayList<String> work_order_servee;
	public ArrayList<String> servee_name;
	public ArrayList<String> servee_name_enable;
	public ArrayList<String> work_order;
	public ArrayList<String> work_order_enable;
	public ArrayList<Integer> address_line;
	public ArrayList<Integer> address_line_enable;
	DataBaseHelper database;
	ProcessAddressForServer processOrderToDisplayInDetailView;
	Address storeaddress;
	ArrayList<ProcessAddressForServer> processOrderlist;
	AddProcessAddress addProcessAddressDetails;
	int processOrderID;
	String serveename;
	Context ctx;
	GPSTracker gpstrack;
	ImageView SelectAge, SelectHair, SelectRace, Selectweight, SelectHeight,
			Selecteye, SelectDistingwishingmarks, relationship_btn;
	boolean isAttach = false;
	byte[] signatureData;
	ArrayList<MannerOfService> listofManner;
	ArrayList<MannerOfService> listofAge;
	ArrayList<MannerOfService> listofHair;
	ArrayList<MannerOfService> listofRace;
	ArrayList<MannerOfService> listofWeight;
	ArrayList<MannerOfService> listofHeight;
	ArrayList<MannerOfService> listofEye;
	ArrayList<Address> add;
	String addresgoogle, workorder;
	EditText edt_name, edt_agentforservice, edt_agenttitle, edt_leftwith,
			edt_eye, edt_age, edt_hair, edt_race, edt_weight, edt_height,
			edt_distingusingmark, edt_relatioship, edt_report, edt_street,
			edt_city, edt_state, edt_zip, edt_Mannerservice;
	ReturnAppOptionsObject returnAppOptions;
	ImageView imageButtonback, arrow_expand, arrow_collapse, arrow_expand_phy,

			arrow_expand_addr, arrow_collapse_phy, arrow_collapse_addr, img_mannerservice;
	Button finalstatus_btnattachments, btn_manner,
			spinner_personalservicespinner, serveAudioSplit, btn_sig,
			btn_submit, addaddress, spinner_age;
	TextView address, back, txt_worker, txt_homeaddress, txt_popup_cancel,
			txt_popup_done, txt_gps, txt_view_gps;
	CheckBox chk_entity, check_uniform, check_military, check_time,
			check_ploice, spalttercheck;
	LinearLayout serveAudioBtnwithImageLayout;
	RadioButton ismale, isfemale;
	RadioGroup gender;
	//LinearLayout ClickAge;
	Bundle extra;
	TextView txtAddressType;
	int navigation_btn = 0;
	LinearLayout start_layout;
	ArrayList<HashMap<String, String>> temp_final_status = new ArrayList<>();
	String HashMap_key;
	String HashMap_value;
	private TextView serveeinfo, physical_des, ser_addr;
	private LinearLayout l1, l2, l3;
	private AddressForServer addressServer;
	private int TYPE_OF_JOB = -1;
	private int year;
	private int month;
	private WheelView mannerWheel, ageWheel, hairWheel, raceWheel, weightWheel,
			heightWheel, eyeWheel;
	private boolean wheelScrolled = false;
	private double dpi;
	private int day;
	private AlertDialog.Builder build;
	private ImageView attach_icon;
	private View physical_address, servee_info, last_info;
	private Button next_services, next_phy, next_servee;
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
	private String manner_code = "";
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

	@SuppressLint({"DefaultLocale", "InflateParams", "SetTextI18n"})
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sevicesnew);

//		if (Validate_Clear_ImageSession) {
//			if (SessionData.getInstance().getFinalstatusAttachment() != 1) {
//				SessionData.getInstance().clearAttachments();
//			}
//			Validate_Clear_ImageSession = false;
//		}

		if (SessionData.getInstance().getFinalstatusAttachment() != 1) {
			SessionData.getInstance().clearAttachments();
			SessionData.getInstance().setImageData(null);
		}


		returnAppOptions = SessionData.getInstance().getReturnAppOptions();

		extra = getIntent().getExtras();
		if (extra != null)
			processOrderID = extra.getInt("processOrderID");

		ctx = FinalStatus.this;
		txt_worker = (TextView) findViewById(R.id.finalstatus_id);
		txtAddressType = (TextView) findViewById(R.id.finalstatus_lblhome);

//		btn_manner = (Button) findViewById(R.id.spinner_personalservicespinner);
		edt_Mannerservice = (EditText) findViewById(R.id.spinner_personalservicespinner);
		img_mannerservice = (ImageView) findViewById(R.id.mannerservice_btn);
		relationship_btn = (ImageView) findViewById(R.id.relationship_btn);
		edt_age = (EditText) findViewById(R.id.edt_age);
		// = (LinearLayout) findViewById(R.id.click_age);

		//new
		physical_address = (View) findViewById(R.id.physical_address);
		next_services = (Button) findViewById(R.id.next_services);
		next_phy = (Button) findViewById(R.id.next_phy);
		next_servee = (Button) findViewById(R.id.next_servee);
		servee_info = (View) findViewById(R.id.servee_info);
		last_info = (View) findViewById(R.id.last_info);
		start_layout = (LinearLayout) findViewById(R.id.start_layout);
		database = DataBaseHelper.getInstance(FinalStatus.this);
		edt_name = (EditText) findViewById(R.id.edt_name);
		SelectAge = (ImageView) findViewById(R.id.select_age);
		SelectHair = (ImageView) findViewById(R.id.select_hair);
		SelectRace = (ImageView) findViewById(R.id.select_race);
		Selectweight = (ImageView) findViewById(R.id.select_weight);
		SelectDistingwishingmarks = (ImageView) findViewById(R.id.select_mark);
		SelectHeight = (ImageView) findViewById(R.id.select_height);
		Selecteye = (ImageView) findViewById(R.id.select_eye);

		SelectAge.setOnClickListener(this);
		SelectHair.setOnClickListener(this);
		SelectRace.setOnClickListener(this);
		Selectweight.setOnClickListener(this);
		SelectDistingwishingmarks.setOnClickListener(this);
		SelectHeight.setOnClickListener(this);
		Selecteye.setOnClickListener(this);
		img_mannerservice.setOnClickListener(this);
		relationship_btn.setOnClickListener(this);
		next_services.setOnClickListener(this);
		next_phy.setOnClickListener(this);


		serveeinfo = (TextView) findViewById(R.id.finalstatus_serveeinfo);
		physical_des = (TextView) findViewById(R.id.phy_des);
		ser_addr = (TextView) findViewById(R.id.finalstatus_lblserviceaddress);
		l1 = (LinearLayout) findViewById(R.id.serveeinfo_layout);
		l2 = (LinearLayout) findViewById(R.id.physicaldescription_layout);
		l3 = (LinearLayout) findViewById(R.id.serviceaddress_layout);
//		arrow_expand = (ImageView) findViewById(R.id.expand_arrow);
//		arrow_collapse = (ImageView) findViewById(R.id.collapse_arrow);
//		arrow_expand_phy = (ImageView) findViewById(R.id.expand_arrow_phy);
//		arrow_expand_addr = (ImageView) findViewById(R.id.expand_arrow_addr);
//		arrow_collapse_phy = (ImageView) findViewById(R.id.collapse_arrow_phy);
//		arrow_collapse_addr = (ImageView) findViewById(R.id.collapse_arrow_addr);

		// edt_age = (EditText) findViewById(R.id.edt_age);
		edt_agentforservice = (EditText) findViewById(R.id.edt_agentforservice);
		edt_agenttitle = (EditText) findViewById(R.id.edt_agenttitle);
		edt_relatioship = (EditText) findViewById(R.id.edt_relatioship);

		edt_hair = (EditText) findViewById(R.id.edt_hair);
		edt_race = (EditText) findViewById(R.id.edt_race);
		edt_weight = (EditText) findViewById(R.id.edt_weight);
		edt_height = (EditText) findViewById(R.id.edt_height);
		edt_eye = (EditText) findViewById(R.id.edt_eye);
		edt_leftwith = (EditText) findViewById(R.id.edt_leftwith);
		edt_report = (EditText) findViewById(R.id.edt_report);
		edt_distingusingmark = (EditText) findViewById(R.id.edt_distingusingmark);


		if (returnAppOptions != null) {
			if (returnAppOptions.isShowPhysicalDescription() == true) {

				SelectAge.setVisibility(View.VISIBLE);
				SelectHair.setVisibility(View.VISIBLE);
				SelectRace.setVisibility(View.VISIBLE);
				Selectweight.setVisibility(View.VISIBLE);
				SelectDistingwishingmarks.setVisibility(View.GONE);
				SelectHeight.setVisibility(View.VISIBLE);
				Selecteye.setVisibility(View.VISIBLE);

				edt_hair.setClickable(true);
				edt_hair.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialoghair();
					}
				});
				edt_hair.setFocusable(false);

				edt_age.setClickable(true);
				edt_age.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogAge();
					}
				});
				edt_age.setFocusable(false);

				edt_eye.setClickable(true);
				edt_eye.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogeye();
					}
				});
				edt_eye.setFocusable(false);


				edt_weight.setClickable(true);
				edt_weight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogweight();
					}
				});
				edt_weight.setFocusable(false);


				edt_height.setClickable(true);
				edt_height.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogheight();
					}
				});
				edt_height.setFocusable(false);

				edt_race.setClickable(true);
				edt_race.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialograce();
					}
				});
				edt_race.setFocusable(false);


				edt_distingusingmark.setEnabled(true);

				//	ClickAge.setClickable(true);


			} else {

				SelectAge.setVisibility(View.GONE);
				SelectHair.setVisibility(View.GONE);
				SelectRace.setVisibility(View.GONE);
				Selectweight.setVisibility(View.GONE);
				SelectDistingwishingmarks.setVisibility(View.GONE);
				SelectHeight.setVisibility(View.GONE);
				Selecteye.setVisibility(View.GONE);

				edt_hair.setEnabled(true);
				edt_age.setEnabled(true);
				edt_age.setInputType(InputType.TYPE_CLASS_NUMBER);
				edt_race.setEnabled(true);
				edt_weight.setEnabled(true);
				edt_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
				edt_height.setEnabled(true);
				edt_height.setInputType(InputType.TYPE_CLASS_TEXT);
				edt_distingusingmark.setEnabled(true);
				edt_eye.setEnabled(true);


			}
		} else {
			SelectAge.setVisibility(View.GONE);
			SelectHair.setVisibility(View.GONE);
			SelectRace.setVisibility(View.GONE);
			Selectweight.setVisibility(View.GONE);
			SelectDistingwishingmarks.setVisibility(View.GONE);
			SelectHeight.setVisibility(View.GONE);
			Selecteye.setVisibility(View.GONE);

			edt_hair.setEnabled(true);
			edt_age.setEnabled(true);
			edt_age.setInputType(InputType.TYPE_CLASS_NUMBER);
			edt_race.setEnabled(true);
			edt_weight.setEnabled(true);
			edt_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
			edt_height.setEnabled(true);
			edt_height.setInputType(InputType.TYPE_CLASS_TEXT);
			edt_distingusingmark.setEnabled(true);
			edt_eye.setEnabled(true);
		}


		SessionData.getInstance().setServee(0);

//		serveeinfo.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v){
//
//				if (l1.getVisibility() == View.GONE){
//					l1.setVisibility(View.VISIBLE);
//					arrow_collapse.setVisibility(View.VISIBLE);
//					arrow_expand.setVisibility(View.GONE);
//
//				} else {
//					l1.setVisibility(View.GONE);
//					arrow_collapse.setVisibility(View.GONE);
//					arrow_expand.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		arrow_collapse.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				if (l1.getVisibility() == View.GONE) {
//					l1.setVisibility(View.VISIBLE);
//					arrow_collapse.setVisibility(View.VISIBLE);
//					arrow_expand.setVisibility(View.GONE);
//
//				} else {
//					l1.setVisibility(View.GONE);
//					arrow_collapse.setVisibility(View.GONE);
//					arrow_expand.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		arrow_expand.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (l1.getVisibility() == View.GONE) {
//					l1.setVisibility(View.VISIBLE);
//					arrow_collapse.setVisibility(View.VISIBLE);
//					arrow_expand.setVisibility(View.GONE);
//
//				} else {
//					l1.setVisibility(View.GONE);
//					arrow_collapse.setVisibility(View.GONE);
//					arrow_expand.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		physical_des.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (l2.getVisibility() == View.GONE) {
//					l2.setVisibility(View.VISIBLE);
//					arrow_collapse_phy.setVisibility(View.VISIBLE);
//					arrow_expand_phy.setVisibility(View.GONE);
//
//				} else {
//					l2.setVisibility(View.GONE);
//					arrow_collapse_phy.setVisibility(View.GONE);
//					arrow_expand_phy.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		arrow_collapse_phy.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (l2.getVisibility() == View.GONE) {
//					l2.setVisibility(View.VISIBLE);
//					arrow_collapse_phy.setVisibility(View.VISIBLE);
//					arrow_expand_phy.setVisibility(View.GONE);
//
//				} else {
//					l2.setVisibility(View.GONE);
//					arrow_collapse_phy.setVisibility(View.GONE);
//					arrow_expand_phy.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		arrow_expand_phy.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (l2.getVisibility() == View.GONE) {
//					l2.setVisibility(View.VISIBLE);
//					arrow_collapse_phy.setVisibility(View.VISIBLE);
//					arrow_expand_phy.setVisibility(View.GONE);
//
//				} else {
//					l2.setVisibility(View.GONE);
//					arrow_collapse_phy.setVisibility(View.GONE);
//					arrow_expand_phy.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});
//
//		ser_addr.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (l3.getVisibility() == View.GONE) {
//					l3.setVisibility(View.VISIBLE);
//					arrow_collapse_addr.setVisibility(View.VISIBLE);
//					arrow_expand_addr.setVisibility(View.GONE);
//
//				} else {
//					l3.setVisibility(View.GONE);
//					arrow_collapse_addr.setVisibility(View.GONE);
//					arrow_expand_addr.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//
//		arrow_collapse_addr.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (l3.getVisibility() == View.GONE) {
//					l3.setVisibility(View.VISIBLE);
//					arrow_collapse_addr.setVisibility(View.VISIBLE);
//					arrow_expand_addr.setVisibility(View.GONE);
//
//				} else {
//					l3.setVisibility(View.GONE);
//					arrow_collapse_addr.setVisibility(View.GONE);
//					arrow_expand_addr.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//
//		arrow_expand_addr.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (l3.getVisibility() == View.GONE) {
//					l3.setVisibility(View.VISIBLE);
//					arrow_collapse_addr.setVisibility(View.VISIBLE);
//					arrow_expand_addr.setVisibility(View.GONE);
//
//				} else {
//					l3.setVisibility(View.GONE);
//					arrow_collapse_addr.setVisibility(View.GONE);
//					arrow_expand_addr.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});

		edt_report.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				boolean handled = false;

				if (actionId == EditorInfo.IME_ACTION_DONE) {
					edt_report.setSelection(0);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edt_report.getWindowToken(), 0);

					handled = true;
				}
				return handled;
			}

		});

		chk_entity = (CheckBox) findViewById(R.id.check_entity);
		check_uniform = (CheckBox) findViewById(R.id.check_uniform);
		check_military = (CheckBox) findViewById(R.id.check_miltry);
		check_time = (CheckBox) findViewById(R.id.check_servedtime);
		check_ploice = (CheckBox) findViewById(R.id.check_ploice);
		spalttercheck = (CheckBox) findViewById(R.id.check_finalsplatter);

		check_time.setChecked(true);

		dpi = getResources().getDisplayMetrics().density;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(FinalStatus.LAYOUT_INFLATER_SERVICE);
		mannerView = inflater.inflate(R.layout.activity_manner_selection, null);
		mannerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupManner = new PopupWindow(mannerView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);
		mannerWheel = (WheelView) mannerView.findViewById(R.id.dp1);

		ageView = inflater.inflate(R.layout.activity_manner_selection, null);
		ageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupAge = new PopupWindow(ageView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		ageWheel = (WheelView) ageView.findViewById(R.id.dp1);

		hairView = inflater.inflate(R.layout.activity_manner_selection, null);
		hairView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupHair = new PopupWindow(hairView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		hairWheel = (WheelView) hairView.findViewById(R.id.dp1);

		raceView = inflater.inflate(R.layout.activity_manner_selection, null);
		raceView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupRace = new PopupWindow(raceView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		raceWheel = (WheelView) raceView.findViewById(R.id.dp1);

		weightView = inflater.inflate(R.layout.activity_manner_selection, null);
		weightView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupWeight = new PopupWindow(weightView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		weightWheel = (WheelView) weightView.findViewById(R.id.dp1);

		heightView = inflater.inflate(R.layout.activity_manner_selection, null);
		heightView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupHeight = new PopupWindow(heightView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		heightWheel = (WheelView) heightView.findViewById(R.id.dp1);

		eyeView = inflater.inflate(R.layout.activity_manner_selection, null);
		eyeView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (360 * dpi)));
		popupEye = new PopupWindow(eyeView, LayoutParams.WRAP_CONTENT,
				(int) (360 * dpi), true);

		eyeWheel = (WheelView) eyeView.findViewById(R.id.dp1);

		gender = (RadioGroup) findViewById(R.id.radioGroup1);
		ismale = (RadioButton) findViewById(R.id.radio0);
		isfemale = (RadioButton) findViewById(R.id.radio1);
		gender.clearCheck();
		database = DataBaseHelper.getInstance();

		address = (TextView) findViewById(R.id.txt_homeaddress);
		txt_gps = (TextView) findViewById(R.id.txt_gps_lattitude);
		txt_view_gps = (TextView) findViewById(R.id.txt_gps);
		btnDate = (Button) findViewById(R.id.btn_datepicker);
		btnTime = (Button) findViewById(R.id.btn_timepicker);
		btn_sig = (Button) findViewById(R.id.buttonsign);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		addaddress = (Button) findViewById(R.id.add_address);

		back = (TextView) findViewById(R.id.txt_backfinalstatus);
		imageButtonback = (ImageView) findViewById(R.id.imageButtonback);
		finalstatus_btnattachments = (Button) findViewById(R.id.btn_attachments);

		attach_icon = (ImageView) findViewById(R.id.attach_icon);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		if (chk_entity.isSelected()) {
//			edt_agentforservice.setEnabled(true);
//			edt_agenttitle.setEnabled(true);
		} else if (!chk_entity.isSelected()) {
//			edt_agentforservice.setEnabled(false);
//			edt_agenttitle.setEnabled(false);
		}

		edt_agentforservice.setEnabled(false);
		edt_agenttitle.setEnabled(false);

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dt = new SimpleDateFormat("hh:mm a");
		String formattedDate = df.format(c.getTime());
		String formattedTime = dt.format(c.getTime());

		btnDate.setText(formattedDate);
		btnTime.setText(formattedTime.toLowerCase());
		txt_gps.setText(latitude + "," + longitude);
		txt_gps.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_sig.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnDate.setOnClickListener(this);
		chk_entity.setOnClickListener(this);
		check_time.setOnClickListener(this);
		spalttercheck.setOnClickListener(this);

		back.setOnClickListener(this);
		imageButtonback.setOnClickListener(this);
		finalstatus_btnattachments.setOnClickListener(this);

		address.setOnClickListener(this);
		address.setOnLongClickListener(this);
//		btn_manner.setOnClickListener(this);
		edt_Mannerservice.setClickable(true);
		edt_Mannerservice.setOnClickListener(this);
		edt_Mannerservice.setFocusable(false);

		edt_relatioship.setClickable(true);
		edt_relatioship.setOnClickListener(this);
		edt_relatioship.setFocusable(true);


		//edt_age.setOnClickListener(this);
		//edt_race.setOnClickListener(this);
		addaddress.setOnClickListener(this);
		wheelMenu1 = new ArrayList<String>();
		mannerofservice = new ArrayList<>();
		mannerofserviceCode = new ArrayList<>();
		wheelMenuAge = new ArrayList<String>();
		listofManner = new ArrayList<MannerOfService>();
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

		wheelMenuHair = new ArrayList<String>();
		wheelMenuHair.add("Black");
		wheelMenuHair.add("Brown");
		wheelMenuHair.add("Blond");
		wheelMenuHair.add("Auburn");
		wheelMenuHair.add("Chestnut");
		wheelMenuHair.add("Red");
		wheelMenuHair.add("Gray/White");
		wheelMenuHair.add("Bald");

		wheelMenuRace = new ArrayList<String>();
		wheelMenuRace.add("White");
		wheelMenuRace.add("African American");
		wheelMenuRace.add("Latino");
		wheelMenuRace.add("Asian");
		wheelMenuRace.add("Middle Eastern");
		wheelMenuRace.add("Native American");
		wheelMenuRace.add("Other");

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

		wheelMenuHeight = new ArrayList<String>();
		wheelMenuHeight.add("Under 4 Feet");
		wheelMenuHeight.add("4'0 - 4'6");
		wheelMenuHeight.add("4'7 - 5'0");
		wheelMenuHeight.add("5'1 - 5'6");
		wheelMenuHeight.add("5'7 - 6'0");
		wheelMenuHeight.add("6'1 - 6'6");
		wheelMenuHeight.add("Over 6'6");
		wheelMenuHeight.add("Seated");

		wheelMenuEye = new ArrayList<String>();
		wheelMenuEye.add("Brown");
		wheelMenuEye.add("Hazel");
		wheelMenuEye.add("Green");
		wheelMenuEye.add("Grey");
		wheelMenuEye.add("Blue");
		wheelMenuEye.add("Amber");

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

		add = new ArrayList<Address>();
		processOrderlist = new ArrayList<ProcessAddressForServer>();
		try {
			ProcessAddressForServer procesAddress = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
			listofManner = database.getMannerOfServiceByStateCode(procesAddress
					.getCourtStateCode());

//			btn_manner.setText(listofManner.get(0).getTitle());

//		;	manner_code = listofManner.get(0).getCode();
//			edt_Mannerservice.setTag(manner_code);

			for (int i = 0; i < listofManner.size(); i++) {
				MannerOfService manner = listofManner.get(i);
				mannerofservice.add(manner.getTitle());
				mannerofserviceCode.add(manner.getCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		initializeViews();


		if (SessionData.getInstance().getAttach_Navigation() != null
				&& SessionData.getInstance().getAttach_Navigation().length() != 0) {
			if (SessionData.getInstance().getAttach_Navigation().equals("Final_Status")) {
				physical_address.setVisibility(View.GONE);
				servee_info.setVisibility(View.GONE);
				last_info.setVisibility(View.VISIBLE);
				if (last_info.getVisibility() == View.VISIBLE) {
					getGps();
					txt_gps.setText(latitude + "," + longitude);
					gpslocated = true;
				} else {
				}
				start_layout.setVisibility(View.GONE);
				navigation_btn = 2;
			}
		} else {
			dialogmanner();
		}
		if (SessionData.getInstance().getTemp_Final_Status() != null &&
				SessionData.getInstance().getTemp_Final_Status().size() != 0) {
			for (HashMap<String, String> map : SessionData.getInstance().getTemp_Final_Status()) {
				for (Map.Entry<String, String> mapEntry : map.entrySet()) {
					HashMap_key = mapEntry.getKey();
					HashMap_value = mapEntry.getValue();
					setvalues();
				}
			}
		}


//        InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        keyboard.showSoftInput(edt_leftwith,InputMethodManager.SHOW_IMPLICIT);
	}

	private void setvalues() {

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_txtAddressType")) {
				if (HashMap_value.length() != 0) {
					txtAddressType.setText(HashMap_value);
				} else {
					txtAddressType.setText("");
				}
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_address")) {
				if (HashMap_value.length() != 0) {
					address.setText(HashMap_value);
				} else {
					address.setText("");
				}
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_Mannerservice")) {
				if (HashMap_value.length() != 0) {
					edt_Mannerservice.setText(HashMap_value);
				} else {
					edt_Mannerservice.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_leftwith")) {
				if (HashMap_value.length() != 0) {
					edt_leftwith.setText(HashMap_value);
				} else {
					edt_leftwith.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_relatioship")) {
				if (HashMap_value.length() != 0) {
					edt_relatioship.setText(HashMap_value);
				} else {
					edt_relatioship.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_btnDate")) {
				if (HashMap_value.length() != 0) {
					btnDate.setText(HashMap_value);
				} else {
					btnDate.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_btnTime")) {
				if (HashMap_value.length() != 0) {
					btnTime.setText(HashMap_value);
				} else {
					btnTime.setText("");
				}
			}
		}

		if (HashMap_key.equals("Temp_check_servedtime")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					check_time.setChecked(true);
					btnTime.setEnabled(true);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						check_time.setChecked(false);
						btnTime.setEnabled(false);
					}
				}
			} else {
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_report")) {
				if (HashMap_value.length() != 0) {
					edt_report.setText(HashMap_value);
				} else {
					edt_report.setText("");
				}
			}
		}

		if (HashMap_key.equals("Temp_check_uniform")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					check_uniform.setChecked(true);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						check_uniform.setChecked(false);
					}
				}
			} else {
			}
		}
		if (HashMap_key.equals("Temp_check_military")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					check_military.setChecked(true);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						check_military.setChecked(false);
					}
				}
			} else {
			}
		}
		if (HashMap_key.equals("Temp_check_ploice")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					check_ploice.setChecked(true);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						check_ploice.setChecked(false);
					}
				}
			} else {
			}
		}

		if (HashMap_key.equals("Temp_ismale")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					ismale.setChecked(true);
					isfemale.setChecked(false);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						ismale.setChecked(false);
						isfemale.setChecked(true);
					}
				}
			} else {
			}
		}
		if (HashMap_key.equals("Temp_isfemale")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					isfemale.setChecked(true);
					ismale.setChecked(false);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						isfemale.setChecked(false);
						ismale.setChecked(true);
					}
				}
			} else {
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_age")) {
				if (HashMap_value.length() != 0) {
					edt_age.setText(HashMap_value);
				} else {
					edt_age.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_hair")) {
				if (HashMap_value.length() != 0) {
					edt_hair.setText(HashMap_value);
				} else {
					edt_hair.setText("");
				}
			}
		}
		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_race")) {
				if (HashMap_value.length() != 0) {
					edt_race.setText(HashMap_value);
				} else {
					edt_race.setText("");
				}
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_weight")) {
				if (HashMap_value.length() != 0) {
					edt_weight.setText(HashMap_value);
				} else {
					edt_weight.setText("");
				}
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_height")) {
				if (HashMap_value.length() != 0) {
					edt_height.setText(HashMap_value);
				} else {
					edt_height.setText("");
				}
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_eye")) {
				if (HashMap_value.length() != 0) {
					edt_eye.setText(HashMap_value);
				} else {
					edt_eye.setText("");
				}
			}
		}


		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("Temp_edt_distingusingmark")) {
				if (HashMap_value.length() != 0) {
					edt_distingusingmark.setText(HashMap_value);
				} else {
					edt_distingusingmark.setText("");
				}
			}
		}

		if (HashMap_key.equals("Temp_spalttercheck")) {
			if (HashMap_value.length() != 0) {
				String temp_time_checkstate = HashMap_value;
				if (HashMap_value.equals("state_checked")) {
					spalttercheck.setChecked(true);
				} else {
					if (HashMap_value.equals("state_unchecked")) {
						spalttercheck.setChecked(false);
						ismale.setChecked(true);
					}
				}
			} else {
			}
		}

		if (HashMap_key.length() != 0) {
			if (HashMap_key.equals("edt_Mannerservice_Tag")) {
				if (HashMap_value.length() != 0) {
					edt_Mannerservice.setTag(HashMap_value);
				} else {
					edt_Mannerservice.setText("");
				}
			}
		}

		if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
			attach_icon.setVisibility(View.VISIBLE);
		} else {
			attach_icon.setVisibility(View.GONE);

		}
		if (!isAttach) {
			if (SessionData.getInstance().getImageData() != null) {
				signatureData = SessionData.getInstance().getImageData()
						.clone();
			} else {
				signatureData = null;
			}
		}
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			edt_report.setSelection(0);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edt_report.getWindowToken(), 0);
			return true;

		}
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	protected void onResume() {
		if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
			attach_icon.setVisibility(View.VISIBLE);
		} else {
			attach_icon.setVisibility(View.GONE);

		}
		if (!isAttach) {
			if (SessionData.getInstance().getImageData() != null) {
				signatureData = SessionData.getInstance().getImageData()
						.clone();
			} else {
				signatureData = null;
			}
		}

		super.onResume();
	}

	@SuppressLint("SetTextI18n")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		isAttach = false;

		if (v == btnTime) {
			if (check_time.isChecked()) {
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
		} else if (v == back || v == imageButtonback) {
			SessionData.getInstance().setAttach_Navigation("");
			if (navigation_btn == 1) {
				physical_address.setVisibility(View.GONE);
				servee_info.setVisibility(View.GONE);
				last_info.setVisibility(View.GONE);
				start_layout.setVisibility(View.VISIBLE);
				navigation_btn = 0;
			} else if (navigation_btn == 2) {
				physical_address.setVisibility(View.VISIBLE);
				servee_info.setVisibility(View.GONE);
				last_info.setVisibility(View.GONE);
				start_layout.setVisibility(View.GONE);
				navigation_btn = 1;
//			}else if(navigation_btn==3){
//				physical_address.setVisibility(View.GONE);
//				servee_info.setVisibility(View.VISIBLE);
//				last_info.setVisibility(View.GONE);
//				start_layout.setVisibility(View.GONE);
//				navigation_btn=2;
//			}
			} else {
				finish();
				SessionData.getInstance().getTemp_Final_Status().clear();
				Intent attach = new Intent(FinalStatus.this,
						ProcessOrderDetail.class);
				attach.putExtra("processOrderID", processOrderID);
				startActivity(attach);
			}

		} else if (v == finalstatus_btnattachments) {
			isAttach = true;
			BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.FINAL_STATUS;
//			SessionData.getInstance().setAttach_Navigation("Final_Status");
			Intent attach = new Intent(FinalStatus.this, BaseFileIncluder.class);
			attach.putExtra("processOrderID", processOrderID);
			startActivity(attach);
		} else if (v == addaddress) {
			Intent addIntent = new Intent(FinalStatus.this, AddAddress.class);
			addIntent.putExtra("processOrderID", processOrderID);
			startActivity(addIntent);
		} else if (v == edt_Mannerservice || img_mannerservice == v) {
//			popup();
			dialogmanner();
		} else if (relationship_btn == v) {
			dialogrelationship();

		} else if (v == SelectAge) {

			dialogAge();

			//popupAge();

		} else if (v == SelectHair) {
//			popupHair();
			dialoghair();

		} else if (v == SelectRace) {
//			popupRace();
			dialograce();

		} else if (v == Selectweight) {
//			popupWeight();
			dialogweight();

		} else if (v == SelectHeight) {
//			popupHeight();
			dialogheight();
		} else if (v == Selecteye) {
			dialogeye();
//			popupEye();

		} else if (v == next_services) {

			if (edt_Mannerservice.getText().toString().length() == 0) {
				new CustomAlertDialog(FinalStatus.this,
						"Please enter the Manner of Service before submit").show();
				return;
			} else {
				physical_address.setVisibility(View.VISIBLE);
				servee_info.setVisibility(View.GONE);
				last_info.setVisibility(View.GONE);
				start_layout.setVisibility(View.GONE);
				navigation_btn = 1;
			}
		} else if (v == next_phy) {

			if (!(ismale.isChecked() || isfemale.isChecked())) {
				new CustomAlertDialog(FinalStatus.this,
						"Please Select The Gender before submit").show();
				return;
			} else {
//						physical_address.setVisibility(View.GONE);
//						servee_info.setVisibility(View.VISIBLE);
//						last_info.setVisibility(View.GONE);
//						start_layout.setVisibility(View.GONE);
				physical_address.setVisibility(View.GONE);
				servee_info.setVisibility(View.GONE);
				last_info.setVisibility(View.VISIBLE);
				if (last_info.getVisibility() == View.VISIBLE) {
					getGps();
					txt_gps.setText(latitude + "," + longitude);
					gpslocated = true;
				} else {
				}
				start_layout.setVisibility(View.GONE);
				navigation_btn = 2;
			}
		} else if (v == SelectDistingwishingmarks) {
			popupEye();

		} else if (v == btn_submit) {
			SessionData.getInstance().setAttach_Navigation("");
			SessionData.getInstance().getTemp_Final_Status().clear();
			if (spalttercheck.isChecked()) {
				splatterTheValues();
			} else if (edt_Mannerservice.getText().toString().length() == 0) {
				new CustomAlertDialog(FinalStatus.this,
						"Please enter the Manner of Service before submit").show();
				return;
			} else if (!(spalttercheck.isChecked())) {
				if (!(ismale.isChecked() || isfemale.isChecked())) {
					new CustomAlertDialog(FinalStatus.this,
							"Please Select The Gender before submit").show();
					return;
				} else {
					// SubmitFinalStatus submitFinalStatusToDB = new
					// SubmitFinalStatus();
					// submitFinalStatusToDB.setServee(edt_name.getText().toString());
					saveAllValuesInDB(workorder, addressLineItem);
					// build = new AlertDialog.Builder(this);
					// build.setTitle("Message");
					// build.setMessage("Final status is saved successfully")
					// .setPositiveButton("Ok", new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog, int which) {
					// // continue with delete
					//
					// }
					// })
					// .show();

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.alertbox);

					TextView text = (TextView) dialog
							.findViewById(R.id.txt_dia);
					text.setText("Final status is saved successfully");

					Button dialogButton = (Button) dialog
							.findViewById(R.id.btn_ok);

					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
							Intent court = new Intent(FinalStatus.this,
									ListCategory.class);
							startActivity(court);

						}
					});

					dialog.show();
					// Window window = dialog.getWindow();

					// new CustomAlertDialog(FinalStatus.this,
					// "Final status is saved successfully",
					// CustomAlertDialog.SYNC).show();
				}
			}

		} else if (v == btn_sig) {
			if (TYPE_OF_JOB == PICKUP_ONLY) {
				onClick(finalstatus_btnattachments);
				return;
			} else {
				isAttach = false;
				SessionData.getInstance().setImageData(signatureData);
				Intent signIntent = new Intent(FinalStatus.this,
						CaptureActivity.class);
				startActivity(signIntent);
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
		} else if (v == check_time) {
			if (check_time.isChecked()) {
				btnTime.setEnabled(true);

			} else if (!check_time.isChecked()) {
				btnTime.setEnabled(false);
			}
		} else if (v == address) {
			double lat = Double.parseDouble(processOrderToDisplayInDetailView
					.getLatitude());
			double lon = Double.parseDouble(processOrderToDisplayInDetailView
					.getLongitude());

			String uriBegin = "geo:" + lat + "," + lon;
			String uriString = uriBegin + "?q=" + address.getText().toString();
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

	}

	private void getValuesForTempStorage() {
		HashMap<String, String> final_status_values = new HashMap<>();

		String Temp_txtAddressType = txtAddressType.getText().toString();
		String Temp_address = address.getText().toString();
		String Temp_edt_Mannerservice = edt_Mannerservice.getText().toString();
		String Temp_edt_leftwith = edt_leftwith.getText().toString();
		String Temp_edt_relatioship = edt_relatioship.getText().toString();
		String Temp_btnDate = btnDate.getText().toString();
		String Temp_btnTime = btnTime.getText().toString();
		boolean Temp_check_servedtime = check_time.isChecked();
		boolean Temp_check_uniform = check_uniform.isChecked();
		boolean Temp_check_military = check_military.isChecked();
		boolean Temp_check_ploice = check_ploice.isChecked();
		boolean Temp_ismale = ismale.isChecked();
		boolean Temp_isfemale = isfemale.isChecked();
		boolean Temp_spalttercheck = spalttercheck.isChecked();
		String Temp_edt_report = edt_report.getText().toString();
		String Temp_edt_age = edt_age.getText().toString();
		String Temp_edt_hair = edt_hair.getText().toString();
		String Temp_edt_race = edt_race.getText().toString();
		String Temp_edt_weight = edt_weight.getText().toString();
		String Temp_edt_height = edt_height.getText().toString();
		String Temp_edt_eye = edt_eye.getText().toString();
		String Temp_edt_distingusingmark = edt_distingusingmark.getText().toString();
		String edt_Mannerservice_Tag = edt_Mannerservice.getTag().toString();

		//1
		if (Temp_txtAddressType.length() != 0) {
			final_status_values.put("Temp_txtAddressType", Temp_txtAddressType);
		} else {
			final_status_values.put("Temp_txtAddressType", "");
		}
		temp_final_status.add(final_status_values);

		//2
		if (Temp_address.length() != 0) {
			final_status_values.put("Temp_address", Temp_address);
		} else {
			final_status_values.put("Temp_address", "");
		}
		temp_final_status.add(final_status_values);

		//3
		if (Temp_edt_Mannerservice.length() != 0) {
			final_status_values.put("Temp_edt_Mannerservice", Temp_edt_Mannerservice);
		} else {
			final_status_values.put("Temp_edt_Mannerservice", "");
		}
		temp_final_status.add(final_status_values);

		//4
		if (Temp_edt_leftwith.length() != 0) {
			final_status_values.put("Temp_edt_leftwith", Temp_edt_leftwith);
		} else {
			final_status_values.put("Temp_edt_leftwith", "");
		}
		temp_final_status.add(final_status_values);

		//5
		if (Temp_edt_relatioship.length() != 0) {
			final_status_values.put("Temp_edt_relatioship", Temp_edt_relatioship);
		} else {
			final_status_values.put("Temp_edt_relatioship", "");
		}
		temp_final_status.add(final_status_values);

		//6
		if (Temp_btnDate.length() != 0) {
			final_status_values.put("Temp_btnDate", Temp_btnDate);
		} else {
			final_status_values.put("Temp_btnDate", "");
		}
		temp_final_status.add(final_status_values);

		//7
		if (Temp_btnTime.length() != 0) {
			final_status_values.put("Temp_btnTime", Temp_btnTime);
		} else {
			final_status_values.put("Temp_btnTime", "");
		}
		temp_final_status.add(final_status_values);

		//8
		if (Temp_edt_report.length() != 0) {
			final_status_values.put("Temp_edt_report", Temp_edt_report);
		} else {
			final_status_values.put("Temp_edt_report", "");
		}
		temp_final_status.add(final_status_values);

		//9
		if (Temp_edt_age.length() != 0) {
			final_status_values.put("Temp_edt_age", Temp_edt_age);
		} else {
			final_status_values.put("Temp_edt_age", "");
		}
		temp_final_status.add(final_status_values);

		//10
		if (Temp_edt_hair.length() != 0) {
			final_status_values.put("Temp_edt_hair", Temp_edt_hair);
		} else {
			final_status_values.put("Temp_edt_hair", "");
		}
		temp_final_status.add(final_status_values);

		//11
		if (Temp_edt_race.length() != 0) {
			final_status_values.put("Temp_edt_race", Temp_edt_race);
		} else {
			final_status_values.put("Temp_edt_race", "");
		}
		temp_final_status.add(final_status_values);

		//12
		if (Temp_edt_weight.length() != 0) {
			final_status_values.put("Temp_edt_weight", Temp_edt_weight);
		} else {
			final_status_values.put("Temp_edt_weight", "");
		}
		temp_final_status.add(final_status_values);

		//13
		if (Temp_edt_height.length() != 0) {
			final_status_values.put("Temp_edt_height", Temp_edt_height);
		} else {
			final_status_values.put("Temp_edt_height", "");
		}
		temp_final_status.add(final_status_values);

		//14
		if (Temp_edt_eye.length() != 0) {
			final_status_values.put("Temp_edt_eye", Temp_edt_eye);
		} else {
			final_status_values.put("Temp_edt_eye", "");
		}
		temp_final_status.add(final_status_values);

		//15
		if (Temp_edt_distingusingmark.length() != 0) {
			final_status_values.put("Temp_edt_distingusingmark", Temp_edt_distingusingmark);
		} else {
			final_status_values.put("Temp_edt_distingusingmark", "");
		}
		temp_final_status.add(final_status_values);

		//16
		if (Temp_check_servedtime) {
			final_status_values.put("Temp_check_servedtime", "state_checked");
		} else {
			final_status_values.put("Temp_check_servedtime", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//17
		if (Temp_check_uniform) {
			final_status_values.put("Temp_check_uniform", "state_checked");
		} else {
			final_status_values.put("Temp_check_uniform", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//18
		if (Temp_check_military) {
			final_status_values.put("Temp_check_military", "state_checked");
		} else {
			final_status_values.put("Temp_check_military", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//19
		if (Temp_check_ploice) {
			final_status_values.put("Temp_check_ploice", "state_checked");
		} else {
			final_status_values.put("Temp_check_ploice", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//20
		if (Temp_ismale) {
			final_status_values.put("Temp_ismale", "state_checked");
		} else {
			final_status_values.put("Temp_ismale", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//21
		if (Temp_isfemale) {
			final_status_values.put("Temp_isfemale", "state_checked");
		} else {
			final_status_values.put("Temp_ismale", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//22
		if (Temp_spalttercheck) {
			final_status_values.put("Temp_spalttercheck", "state_checked");
		} else {
			final_status_values.put("Temp_spalttercheck", "state_unchecked");
		}
		temp_final_status.add(final_status_values);

		//23
		if (edt_Mannerservice_Tag.length() != 0) {
			final_status_values.put("edt_Mannerservice_Tag", edt_Mannerservice_Tag);
		} else {
			final_status_values.put("edt_Mannerservice_Tag", "");
		}
		temp_final_status.add(final_status_values);


		SessionData.getInstance().getTemp_Final_Status().addAll(temp_final_status);

	}

	@SuppressLint("SetTextI18n")
	private void dialogmanner() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_Mannerservice.getText().toString()), true);

		final String[] select = new String[1];
		final String[] selectmanner = new String[1];
		if (edt_Mannerservice.getText().toString().length() != 0) {
			select[0] = edt_Mannerservice.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
				selectmanner[0] = mannerofserviceCode.get(position);
			}
		});
		Button Save = (Button) dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowInputManagerKeyboard();
				edt_Mannerservice.setText(select[0]);
				edt_Mannerservice.setTag(selectmanner[0]);
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

		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {

			}
		});

		dialog.show();

	}

	@SuppressLint("SetTextI18n")
	private void dialogweight() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_weight.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_weight.getText().toString().length() != 0) {
			select[0] = edt_weight.getText().toString();
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
				edt_weight.setText(select[0]);
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
	private void dialogheight() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_height.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_height.getText().toString().length() != 0) {
			select[0] = edt_height.getText().toString();
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
				edt_height.setText(select[0]);
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
	private void dialograce() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_race.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_race.getText().toString().length() != 0) {
			select[0] = edt_race.getText().toString();
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
				edt_race.setText(select[0]);
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
	private void dialoghair() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_hair.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_hair.getText().toString().length() != 0) {
			select[0] = edt_hair.getText().toString();
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
				edt_hair.setText(select[0]);
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
	private void dialogeye() {
		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_eye.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_eye.getText().toString().length() != 0) {
			select[0] = edt_eye.getText().toString();
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
				edt_eye.setText(select[0]);
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
	private void dialogAge() {

		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_age.getText().toString()), true);

		final String[] select = new String[1];
		if (edt_age.getText().toString().length() != 0) {
			select[0] = edt_age.getText().toString();
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
				edt_age.setText(select[0]);
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
	private void popupEye() {

		initeye(eyeWheel);

		txt_popup_cancel = (TextView) eyeView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) eyeView.findViewById(R.id.txt_done);
		popupEye.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupEye.showAtLocation(eyeView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupEye.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupEye.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateeye();
				edt_eye.setText(selectedEye);

				popupEye.dismiss();
			}
		});

	}

	@SuppressLint("RtlHardcoded")
	private void popupHeight() {
		// TODO Auto-generated method stub
		initheight(heightWheel);

		txt_popup_cancel = (TextView) heightView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) heightView.findViewById(R.id.txt_done);
		popupHeight.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupHeight.showAtLocation(heightView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupHeight.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupHeight.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateheight();
				edt_height.setText(selectedHeight);

				popupHeight.dismiss();
			}
		});
	}

	@SuppressLint("RtlHardcoded")
	private void popupWeight() {

		initweight(weightWheel);

		txt_popup_cancel = (TextView) weightView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) weightView.findViewById(R.id.txt_done);
		popupWeight.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupWeight.showAtLocation(weightView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupWeight.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWeight.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateweight();
				edt_weight.setText(selectedWeight);

				popupWeight.dismiss();
			}
		});
	}

	@SuppressLint("RtlHardcoded")
	private void popupRace() {

		initrace(raceWheel);

		txt_popup_cancel = (TextView) raceView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) raceView.findViewById(R.id.txt_done);
		popupRace.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupRace.showAtLocation(raceView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupRace.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupRace.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updaterace();
				edt_race.setText(selectedRace);

				popupRace.dismiss();
			}
		});
	}

	@SuppressLint("RtlHardcoded")
	private void popupHair() {

		inithair(hairWheel);

		txt_popup_cancel = (TextView) hairView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) hairView.findViewById(R.id.txt_done);
		popupHair.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupHair.showAtLocation(hairView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupHair.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupHair.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatehair();
				edt_hair.setText(selectedHair);

				popupHair.dismiss();
			}
		});
	}

	@Override
	public boolean onLongClick(View v) {
		if (v == address) {
			Intent addressOptions = new Intent(FinalStatus.this,
					AddressOptions.class);
			addressOptions.putExtra("address", address.getText().toString());
			addressOptions.putExtra("Latitude",
					processOrderToDisplayInDetailView.getLatitude());
			addressOptions.putExtra("Longitude",
					processOrderToDisplayInDetailView.getLongitude());
			startActivity(addressOptions);
		}
		return false;
	}

	public void initializeViews() {
		try {
			processOrderToDisplayInDetailView = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);

			if (processOrderToDisplayInDetailView.getWorkorder().length() == 0) {
				txt_worker.setText("N/A");
			} else {
				txt_worker.setText(processOrderToDisplayInDetailView
						.getWorkorder());
				workorder = processOrderToDisplayInDetailView.getWorkorder();
				SessionData.getInstance().setWorkid(
						processOrderToDisplayInDetailView.getWorkorder());
			}
			addressLineItem = processOrderToDisplayInDetailView
					.getAddressLineItem();
			if (processOrderToDisplayInDetailView
					.getAddressFormattedForDisplay().length() != 0) {

				String addresss = processOrderToDisplayInDetailView
						.getAddressFormattedForDisplay();

				String address_txt;
				if (addresss.toString().startsWith("Home")) {
					txtAddressType.setText("Home : ");
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay().substring(5);
				} else if (addresss.toString().startsWith("Business")) {
					txtAddressType.setText("Business : ");
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay().substring(9);
				} else if (addresss.toString().startsWith("Others")) {
					txtAddressType.setText("Others : ");
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay().substring(7);
				} else if (addresss.toString().startsWith("Government")) {
					txtAddressType.setText("Government : ");
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay().substring(11);
				} else if (addresss.toString().startsWith("Not a physical Address")) {
					txtAddressType.setText("Not a physical Address : ");
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay().substring(23);
				} else {
					address_txt = processOrderToDisplayInDetailView
							.getAddressFormattedForDisplay();
				}

				Log.d("Address_final", address_txt);
				address.setText(address_txt);

				addresgoogle = processOrderToDisplayInDetailView
						.getAddressFormattedForGoogle();
			}
			if (processOrderToDisplayInDetailView.getServee().length() != 0) {
				edt_name.setText(processOrderToDisplayInDetailView.getServee());
				edt_name.setEnabled(false);
			}

			if (processOrderToDisplayInDetailView.isEntity() == false) {
				chk_entity.setChecked(false);
			} else {
				chk_entity.setChecked(true);
			}

			chk_entity.setClickable(false);


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
			if (processOrderToDisplayInDetailView
					.getAgentForServiceRelationShipToServee().length() != 0) {
//				edt_relatioship.setText(processOrderToDisplayInDetailView
//						.getAgentForServiceRelationShipToServee());
			}

			if (processOrderToDisplayInDetailView.getInuniform() == true) {
				check_uniform.setChecked(true);
			}
			if (processOrderToDisplayInDetailView.getMilitary() == true) {
				check_military.setChecked(true);
			}
			if (processOrderToDisplayInDetailView.getPolice() == true) {
				check_ploice.setChecked(true);
			}

			if (processOrderToDisplayInDetailView.getAge().length() != 0) {
				edt_age.setText(processOrderToDisplayInDetailView.getAge());
			}
			if (processOrderToDisplayInDetailView.getHair().length() != 0) {
				edt_hair.setText(processOrderToDisplayInDetailView.getHair());
			}
			if (processOrderToDisplayInDetailView.getWeight().length() != 0) {
				edt_weight.setText(processOrderToDisplayInDetailView
						.getWeight());
			}
			if (processOrderToDisplayInDetailView.getHeight().length() != 0) {
				edt_height.setText(processOrderToDisplayInDetailView
						.getHeight());
			}
			if (processOrderToDisplayInDetailView.getEyes().length() != 0) {
				edt_eye.setText(processOrderToDisplayInDetailView.getEyes());
			}
			if (processOrderToDisplayInDetailView.getMarks().length() != 0) {
				edt_distingusingmark.setText(processOrderToDisplayInDetailView
						.getMarks());
			}

		} catch (Exception e) {
			e.printStackTrace();
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

	@SuppressLint("RtlHardcoded")
	public void popupAge() {

		initage(ageWheel);

		txt_popup_cancel = (TextView) ageView.findViewById(R.id.txt_cancel);
		txt_popup_done = (TextView) ageView.findViewById(R.id.txt_done);
		popupAge.setAnimationStyle(R.style.Manner_AnimationPopup);
		popupAge.showAtLocation(ageView, Gravity.LEFT, (int) (10 * dpi),
				(int) (300 * dpi));
		popupAge.update();

		txt_popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupAge.dismiss();

			}
		});
		txt_popup_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateage();
				edt_age.setText(selectedAge);

				popupAge.dismiss();
			}
		});

	}

	@SuppressLint("RtlHardcoded")
	public void popup() {
		initWheel1(mannerWheel);

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

			@Override
			public void onClick(View arg0) {
				updateStatus();
				btn_manner.setText(selectedManner);
				btn_manner.setTag(manner_code);
				Log.d("Code", "" + manner_code);
				popupManner.dismiss();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void initWheel1(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenu1));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void initage(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuAge));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void inithair(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuHair));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void initrace(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuRace));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void initweight(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuWeight));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void initheight(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuHeight));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings("rawtypes")
	private void initeye(WheelView wheel) {
		wheel.setViewAdapter(new ArrayWheelAdapter(this, wheelMenuEye));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
	}

	public void updateStatus() {
		selectedManner = wheelMenu1.get(getWheelValue(mannerWheel)).toString();
		manner_code = (listofManner.get(getWheelValue(mannerWheel)).getCode());
	}

	public void updateage() {
		selectedAge = wheelMenuAge.get(getWheelValue(ageWheel)).toString();
		// manner_code = (listofManner.get(getWheelValue(agewheel)).getCode());
	}

	public void updatehair() {
		selectedHair = wheelMenuHair.get(getWheelValue(hairWheel)).toString();

	}

	public void updaterace() {
		selectedRace = wheelMenuRace.get(getWheelValue(raceWheel)).toString();

	}

	public void updateweight() {
		selectedWeight = wheelMenuWeight.get(getWheelValue(weightWheel))
				.toString();

	}

	public void updateheight() {
		selectedHeight = wheelMenuHeight.get(getWheelValue(heightWheel))

				.toString();

	}

	public void updateeye() {
		selectedEye = wheelMenuEye.get(getWheelValue(eyeWheel)).toString();

	}

	@SuppressWarnings("unused")
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	private int getWheelValue(WheelView wheel) {
		return wheel.getCurrentItem();
	}

	@Override
	public void onBackPressed() {
		SessionData.getInstance().setAttach_Navigation("");

		if (navigation_btn == 1) {
			physical_address.setVisibility(View.GONE);
			servee_info.setVisibility(View.GONE);
			last_info.setVisibility(View.GONE);
			start_layout.setVisibility(View.VISIBLE);
			navigation_btn = 0;
		} else if (navigation_btn == 2) {
			physical_address.setVisibility(View.VISIBLE);
			servee_info.setVisibility(View.GONE);
			last_info.setVisibility(View.GONE);
			start_layout.setVisibility(View.GONE);
			navigation_btn = 1;
//			}else if(navigation_btn==3){
//				physical_address.setVisibility(View.GONE);
//				servee_info.setVisibility(View.VISIBLE);
//				last_info.setVisibility(View.GONE);
//				start_layout.setVisibility(View.GONE);
//				navigation_btn=2;
//			}
		} else {
			finish();
			SessionData.getInstance().getTemp_Final_Status().clear();
			Intent attach = new Intent(FinalStatus.this,
					ProcessOrderDetail.class);
			attach.putExtra("processOrderID", processOrderID);
			startActivity(attach);
		}

	}

	@SuppressLint("SetTextI18n")
	private void splatterTheValues() {

		if (ismale.isChecked()) {
			if (ismale.isChecked()) {
				processOrderToDisplayInDetailView.setServeeIsMale(true);
			} else {
				processOrderToDisplayInDetailView.setServeeIsMale(false);
			}
		} else if (isfemale.isChecked()) {
			if (isfemale.isChecked()) {
				processOrderToDisplayInDetailView.setServeeIsMale(false);
			} else {
				processOrderToDisplayInDetailView.setServeeIsMale(true);
			}
		} else {
			new CustomAlertDialog(FinalStatus.this,
					"Please Select The Gender before submit").show();
			return;
		}

		final ArrayList<SplatterAddress> countResult = database
				.getProcessAddressComparison(addresgoogle, workorder, serveename);

		if (countResult.size() == 1) {


			saveAllValuesInDB(workorder, addressLineItem);


			Intent detailView = new Intent(FinalStatus.this,
					ListCategory.class);
			startActivity(detailView);
			Toast.makeText(FinalStatus.this, "Final status is saved successfully!", Toast.LENGTH_SHORT).show();

		} else {

			final Dialog mdialog = new Dialog(context);
			mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mdialog.setContentView(R.layout.splatterdialog);

			TextView text = (TextView) mdialog.findViewById(R.id.splattermsg);
			text.setText("You have "
					+ (countResult.size() - 1)
					+ " other jobs at the same address, would you "
					+ "like to spread this diligence (final status) across all jobs?");

			Button dialogButtonYes = (Button) mdialog.findViewById(R.id.btn_yes);
			dialogButtonYes.setOnClickListener(new OnClickListener() {
				@SuppressLint("SetTextI18n")
				@Override
				public void onClick(View v) {


					if (countResult.size() > 1) {

						work_order = new ArrayList<>();
						work_order_enable = new ArrayList<>();
						address_line = new ArrayList<>();
						address_line_enable = new ArrayList<>();

						work_order_servee = new ArrayList<>();
						servee_name = new ArrayList<>();
						servee_name_enable = new ArrayList<>();


						for (int i = 0; i < countResult.size(); i++) {

							if (workorder.toString().equalsIgnoreCase(countResult.get(i).getWorkorder())) {

							} else {
								work_order.add(countResult.get(i).getWorkorder());
								address_line.add(countResult.get(i).getAddressLineItem());
								servee_name.add(countResult.get(i).getServeename());
							}


						}

						final Dialog dialog = new Dialog(FinalStatus.this);
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
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(FinalStatus.this, R.layout.choise_list, work_order_servee);
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
								address_line_enable.add(addressLineItem);

								for (int j = 0; j < work_order_enable.size(); j++) {

									saveAllValuesInDB(work_order_enable.get(j).toString(),
											address_line_enable.get(j));
									Log.d("item check", "" + work_order_enable);

								}

								dialog.dismiss();
								mdialog.dismiss();
								finish();
								Intent detailView = new Intent(FinalStatus.this,
										ListCategory.class);
								startActivity(detailView);
								Toast.makeText(FinalStatus.this, "Final status is saved successfully!", Toast.LENGTH_SHORT).show();
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


					/*SessionData.getInstance().setServee(1);
					for (int i = 0; i < countResult.size(); i++) {
						saveAllValuesInDB(countResult.get(i).getWorkorder(),
								countResult.get(i).getAddressLineItem());
						Log.d("item check", "" + countResult);
						mdialog.dismiss();

					}*/

					/*final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.alertbox);

					TextView text = (TextView) dialog
							.findViewById(R.id.txt_dia);
					text.setText("Final status is saved successfully");

					Button dialogButton = (Button) dialog
							.findViewById(R.id.btn_ok);

					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
							Intent court = new Intent(FinalStatus.this,
									ListCategory.class);
							startActivity(court);

						}
					});

					dialog.show();*/

					}
				}

			});

			Button dialogButtonNo = (Button) mdialog.findViewById(R.id.btn_no);
			// if button is clicked, close the custom dialog
			dialogButtonNo.setOnClickListener(new OnClickListener() {
				@SuppressLint("SetTextI18n")
				@Override
				public void onClick(View v) {
					saveAllValuesInDB(workorder, addressLineItem);
					mdialog.dismiss();

					Intent detailView = new Intent(FinalStatus.this,
							ListCategory.class);
					startActivity(detailView);
					Toast.makeText(FinalStatus.this, "Final status is saved successfully!", Toast.LENGTH_SHORT).show();


					/*final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.alertbox);

					TextView text = (TextView) dialog
							.findViewById(R.id.txt_dia);
					text.setText("Final status is saved successfully");

					Button dialogButton = (Button) dialog
							.findViewById(R.id.btn_ok);

					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
							Intent court = new Intent(FinalStatus.this,
									ListCategory.class);
							startActivity(court);

						}
					});

					dialog.show();*/
				}


			});
			mdialog.show();
			getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
		}


	}

	public void saveAllValuesInDB(String txt_Workorder, int addresslineitem) {
		if (!gpslocated) {
			getGps();
		}
		SubmitFinalStatus submitFinalStatusToDB = new SubmitFinalStatus();
		Log.d("txt_Workorder", "" + txt_Workorder);
		Log.d("addresslineitem", "" + addresslineitem);
		String workorder;
		if (txt_Workorder.toString() == null) {
			workorder = "";
		} else {
			workorder = txt_Workorder;
		}
		submitFinalStatusToDB.setWorkorder(workorder);
		submitFinalStatusToDB.setAddressLineitem(addresslineitem);
		if (SessionData.getInstance().getServee() == 0) {
			submitFinalStatusToDB.setServee(edt_name.getText().toString());
			Log.d("servee", "" + submitFinalStatusToDB.getServee());
		} else {
			submitFinalStatusToDB.setServee("");
			Log.d("servee", "" + submitFinalStatusToDB.getServee());
		}
		if (SessionData.getInstance().getEntityChecked() == 0) {
			submitFinalStatusToDB.setEntity(true);
		} else {
			submitFinalStatusToDB.setEntity(false);
		}
//		if (chk_entity.isChecked()) {
//			submitFinalStatusToDB.setEntity(true);
//		} else {
//			submitFinalStatusToDB.setEntity(false);
//		}
//		submitFinalStatusToDB.setAuthorizedAgentTitle(edt_agentforservice
//				.getText().toString());
//
//		submitFinalStatusToDB.setAuthorizedAgent(edt_agenttitle.getText()
//				.toString());

		submitFinalStatusToDB.setAuthorizedAgentTitle(SessionData.getInstance().getAgenttitle());
		Log.d("title", "" + submitFinalStatusToDB.getAuthorizedAgentTitle());

		submitFinalStatusToDB.setAuthorizedAgent(SessionData.getInstance().getAgentservice());
		Log.d("service", "" + submitFinalStatusToDB.getAuthorizedAgent());
		if (check_uniform.isChecked()) {
			submitFinalStatusToDB.setInUniform(true);
		} else {
			submitFinalStatusToDB.setInUniform(false);
		}
		if (check_military.isChecked()) {
			submitFinalStatusToDB.setmilitary(true);
		} else {
			submitFinalStatusToDB.setmilitary(false);
		}
		if (check_ploice.isChecked()) {
			submitFinalStatusToDB.setPolice(true);
		} else {
			submitFinalStatusToDB.setPolice(false);
		}

		if (ismale.isChecked()) {
			submitFinalStatusToDB.setServerisMale(true);
		}
		if (isfemale.isChecked()) {
			submitFinalStatusToDB.setServerisMale(false);
		}

		submitFinalStatusToDB.setAge(edt_age.getText().toString());
		submitFinalStatusToDB.setHair(edt_hair.getText().toString());
		submitFinalStatusToDB.setSkin(edt_race.getText().toString());
		submitFinalStatusToDB.setWeight(edt_weight.getText().toString());
		submitFinalStatusToDB.setHeight(edt_height.getText().toString());
		submitFinalStatusToDB.setEyes(edt_eye.getText().toString());
		submitFinalStatusToDB.setMarks(edt_distingusingmark.getText()
				.toString());
		submitFinalStatusToDB.setMannerOfServiceCode(edt_Mannerservice.getTag()
				.toString());
		Log.d("Manner of service code", "" + edt_Mannerservice.getTag()
				.toString());
		submitFinalStatusToDB.setLeftWith(edt_leftwith.getText().toString());
		submitFinalStatusToDB.setRelationship(edt_relatioship.getText()
				.toString());
		Log.d("Relationship_finalstatus", "" + edt_relatioship.getText()
				.toString());
		submitFinalStatusToDB.setServeDate(btnDate.getText().toString());
		submitFinalStatusToDB.setLatitude(String.valueOf(latitude));
		submitFinalStatusToDB.setLongitude(String.valueOf(longitude));
		submitFinalStatusToDB.setReport(edt_report.getText().toString());

		if (SessionData.getInstance().getTheAddressType() != 0
				|| SessionData.getInstance().getCity() != ""
				|| SessionData.getInstance().getState() != ""
				|| SessionData.getInstance().getZip() != "") {
			Address submitAddProcessAddressToDB = new Address();

			submitAddProcessAddressToDB.setStreet(SessionData.getInstance()
					.getStreet());

			submitAddProcessAddressToDB.setState(SessionData.getInstance()
					.getState());
			Log.d("state", "" + SessionData.getInstance().getState());
			submitAddProcessAddressToDB.setCity(SessionData.getInstance()
					.getCity());
			submitAddProcessAddressToDB.setLineItem(database
					.getLastinsertedLineItemFromFinalStatus());
			submitAddProcessAddressToDB.setZip(SessionData.getInstance()
					.getZip());
			submitAddProcessAddressToDB.setTheAddressType(SessionData
					.getInstance().getTheAddressType());
			Log.d("addresstype", ""
					+ SessionData.getInstance().getTheAddressType());
			submitAddProcessAddressToDB.setWorkorder(txt_Workorder);

			boolean sucessadress = true;
			try {
				sucessadress = database
						.insertProcessAddressfromServer(submitAddProcessAddressToDB);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sucessadress == false) {
				Toast.makeText(getApplicationContext(), "Addres not saved",
						Toast.LENGTH_LONG).show();
			}
		}

		if (check_time.isChecked()) {
			submitFinalStatusToDB.setserveTime(btnTime.getText().toString());
		} else {
			submitFinalStatusToDB.setserveTime("");
		}
		try {
			boolean isSubmitSucess = database
					.insertIntoFinalStatusTable(submitFinalStatusToDB);
			if (isSubmitSucess) {
				String imageSaved = "";
				SubmitDiligence submitImage = new SubmitDiligence();
				submitImage.setLineItem(database
						.getLastinsertedLineItemFromFinalStatus());
				database.deletesFinalStatementAttachment(submitImage
						.getLineItem());

				ArrayList<byte[]> imageArrayToSaveInDB = SessionData
						.getInstance().getAttachedFilesData();
				for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
					submitImage.setWorkorder(txt_Workorder);
					submitImage.setAddressLineItem(addresslineitem);
					submitImage.setAttachementsFileName(txt_Workorder + "-"
							+ addresslineitem + "-image-" + i);
					submitImage.setAttachementBase64String(Base64
							.encodeToString(imageArrayToSaveInDB.get(i),
									Base64.DEFAULT));
					submitImage.setAttachementOfUrlString("");

					boolean sucess = true;
					sucess = database
							.insertOrUpdateAttachmentsOfFinalStatus(submitImage);
					if (sucess == false) {
						imageSaved = "Image is not saved!";
					}
				}

				byte[] audioArray = SessionData.getInstance().getAudioData();
				if (audioArray != null && audioArray.length > 0) {
					SubmitDiligence submitAudioFile = new SubmitDiligence();
					submitAudioFile.setLineItem(database
							.getLastinsertedLineItemFromFinalStatus());
					submitAudioFile.setWorkorder(txt_Workorder);
					submitAudioFile.setAddressLineItem(addresslineitem);
					submitAudioFile.setAttachementsFileName(txt_Workorder + "-"
							+ (long) addresslineitem + "-Audio");
					submitAudioFile.setAttachementOfUrlString("");

					{
						submitAudioFile.setAttachementBase64String(Base64
								.encodeToString(audioArray, Base64.DEFAULT));
						boolean sucess = true;
						sucess = database
								.insertOrUpdateAttachmentsOfFinalStatus(submitAudioFile);
						if (sucess == false) {
							new CustomAlertDialog(FinalStatus.this,
									"Audio not saved").show();

						}
					}
				}

				byte[] signature = SessionData.getInstance().getImageData();
				if (signature != null && signature.length > 0) {

					SubmitDiligence submitsignature = new SubmitDiligence();
					submitsignature.setLineItem(database
							.getLastinsertedLineItemFromFinalStatus());
					submitsignature.setWorkorder(txt_Workorder);
					submitsignature.setAddressLineItem(addresslineitem);

					submitsignature.setAttachementsFileName(txt_Workorder + "-"
							+ (long) addresslineitem + "-signature");

					submitsignature.setAttachementOfUrlString("");
					{

						submitsignature.setAttachementBase64String(Base64
								.encodeToString(signature, Base64.DEFAULT));

						boolean sucesssig = true;
						sucesssig = database
								.insertOrUpdateAttachmentsOfFinalStatus(submitsignature);
						if (sucesssig == false) {
							imageSaved = "Signature is not saved!";
						} else {

						}
					}
				}
				if (imageSaved.length() == 0) {

				} else {
					new CustomAlertDialog(FinalStatus.this, imageSaved).show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getGps() {
		gpstrack = new GPSTracker(FinalStatus.this);
		if (gpstrack.canGetLocation()) {
			latitude = gpstrack.getLatitude();
			longitude = gpstrack.getLongitude();
		} else {

			gpstrack.showSettingsAlert();
		}
	}

	private void dialogrelationship() {

		final Dialog dialog = new Dialog(FinalStatus.this);
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
		list.setItemChecked(adapter.getPosition(edt_relatioship.getText().toString()), true);

		final String[] select = new String[1];
		//	final String[] selectmanner = new String[1];
		if (edt_relatioship.getText().toString().length() != 0) {
			select[0] = edt_relatioship.getText().toString();
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

				edt_relatioship.setText(select[0]);
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

	private void ShowInputManagerKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		edt_leftwith.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edt_leftwith, InputMethodManager.SHOW_IMPLICIT);
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


}