package com.tristar.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.tristar.adapters.CustomAdapter;
import com.tristar.adapters.CustomdirectionAdapter;
import com.tristar.adapters.MilestoneFilter_Adapter;
import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ListViewSwipeGesture;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")
public class ProcessOrder extends FragmentActivity implements OnClickListener {

	public static Comparator<ProcessAddressForServer> StringAscComparator = new Comparator<ProcessAddressForServer>() {

		public int compare(ProcessAddressForServer app1,
						   ProcessAddressForServer app2) {

			ProcessAddressForServer stringName1 = app1;
			ProcessAddressForServer stringName2 = app2;

			return stringName1.getServee().compareToIgnoreCase(
					stringName2.getServee());
		}
	};
	public static Comparator<ProcessAddressForServer> StringDescComparator = new Comparator<ProcessAddressForServer>() {

		public int compare(ProcessAddressForServer app1,
						   ProcessAddressForServer app2) {

			ProcessAddressForServer stringName1 = app1;
			ProcessAddressForServer stringName2 = app2;

			return stringName2.getServee().compareToIgnoreCase(
					stringName1.getServee());
		}
	};
	public static Comparator<ProcessAddressForServer> StringworkorderAscComparator = new Comparator<ProcessAddressForServer>() {

		public int compare(ProcessAddressForServer app1,
						   ProcessAddressForServer app2) {

			ProcessAddressForServer stringName1 = app1;
			ProcessAddressForServer stringName2 = app2;

			return stringName1.getWorkorder().compareToIgnoreCase(
					stringName2.getWorkorder());
		}
	};
	public static Comparator<ProcessAddressForServer> StringworkorderDseComparator = new Comparator<ProcessAddressForServer>() {

		public int compare(ProcessAddressForServer app1,
						   ProcessAddressForServer app2) {

			ProcessAddressForServer stringName1 = app1;
			ProcessAddressForServer stringName2 = app2;

			return stringName2.getWorkorder().compareToIgnoreCase(
					stringName1.getWorkorder());
		}
	};

	public static Comparator<ProcessAddressForServer> DateAsc_Comparator = new Comparator<ProcessAddressForServer>() {

		private String[] Time_separated;
		private int i = 0;

		@Override
		public int compare(ProcessAddressForServer t1, ProcessAddressForServer t2) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

			String Due_one = t1.getDueDate();
			Time_separated = Due_one.split("T");
			String date_one = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);

			Log.d("changeDateFormat_Due_one ", Due_one + "");

			String Due_two = t2.getDueDate();
			Time_separated = Due_two.split("T");
			String date_two = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);

			try {

				Date one = dateFormat.parse(date_one);
				Date two = dateFormat.parse(date_two);
				i = one.compareTo(two);
			} catch (ParseException e) {
				e.printStackTrace();

				Log.d("sort_dueDate", e.getMessage() + "");
			}

			return i;

		}
	};


	public static Comparator<ProcessAddressForServer> DateDse_Comparator = new Comparator<ProcessAddressForServer>() {

		private String[] Time_separated;
		private int i = 0;

		@Override
		public int compare(ProcessAddressForServer t1, ProcessAddressForServer t2) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

			String Due_one = t1.getDueDate();
			Time_separated = Due_one.split("T");
			String date_one = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);

			Log.d("changeDateFormat_Due_one ", Due_one + "");

			String Due_two = t2.getDueDate();
			Time_separated = Due_two.split("T");
			String date_two = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);

			try {

				Date one = dateFormat.parse(date_one);
				Date two = dateFormat.parse(date_two);
				i = two.compareTo(one);
			} catch (ParseException e) {
				e.printStackTrace();

				Log.d("sort_dueDate", e.getMessage() + "");
			}

			return i;

		}
	};



	public double latitude, longitude;
	DynamicListView listViewProcessOrder;
	ViewFlipper flipper;
	GPSTracker gpstrack;
	int mapfunction = 0;
	Context context;
	DataBaseHelper database;
	ProcessAddressForServer processOrderForTable, processMaps, processfilterMaps;
	ArrayList<ProcessAddressForServer> processOrderListArray, processOrderlist,
			filterlist;
	ImageButton category, mapview, imageBtn_self, image_direction;
	ArrayList<LatLng> markerPoints;
	PolylineOptions lineOptions = null;
	TextView cancel, Reset, MyList, Filter, txt_direction;
	HashMap<String, String> processList;
	int markerSelect;
	TextView lbl_self, txt_logout, txt_sync, mapText, lbl_category, servelab,
			workorderlabel, title, sortDuedate;
	LinearLayout lin_lis, lin_map, lin_distance;
	FragmentManager manager;
	Marker googleMarker;
	Button btn_hybrid, btn_satellite, btn_standard;
	String mapSouce;
	Bundle extra;
	ImageView Img_clear_search;
	int click = 0;
	EditText edt_SearchWorkOrder;
	private GoogleMap googleMap, googleMapfilter;
	private double dpi;
	private LinearLayout tabCategory, tabCourt, tabMap, sortby, filter_layout, titlet_layout, reset_layout;
	private TextView milestone_txt_filter;
	private ImageView milestone_img_filter;
	private ListView filter_listView;
	private LinearLayout milestone_layout;
    private LinearLayout parent_layout;
	private CustomAdapter adapter;
	private ListViewSwipeGesture swipeGesture;
	private String RejectWorkorderResult = "";
	private String sessionID = "";
	private String reason = "";
	private int addressLineItem = 0;
	private String workorder = "";
	private String strUserName = "";
	private String strPassword = "";

	ArrayList<ProcessAddressForServer> processAddressForServersfilter = new ArrayList<ProcessAddressForServer>();


	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.processorder);
		SessionData.getInstance().clearAttachments();
		SessionData.getInstance().setAudioData(null);
		context = this;


		DisplayActivity.loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		DisplayActivity.loginPrefsEditors = DisplayActivity.loginPreferencess.edit();

		strUserName = DisplayActivity.loginPreferencess.getString("user Id","");
		strPassword = DisplayActivity.loginPreferencess.getString("password","");

		extra = getIntent().getExtras();
		if (extra != null) {
			mapSouce = extra.getString("mapview");
		}

		dpi = getResources().getDisplayMetrics().density;
		database = DataBaseHelper.getInstance(ProcessOrder.this);

		edt_SearchWorkOrder = (EditText) findViewById(R.id.edt_workordersearch);

		edt_SearchWorkOrder.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				//MainActivity.this.adapt.getFilter().filter(s);
				int textlength = s.length();

//				processOrderlist = new ArrayList<ProcessAddressForServer>();

				ArrayList<ProcessAddressForServer> processAddressForServers = new ArrayList<ProcessAddressForServer>();


//				for (ProcessAddressForServer c : processOrderListArray) {
				for (ProcessAddressForServer c : processOrderlist) {
					if (textlength <= c.getWorkorder().length()) {
						if (c.getWorkorder().toLowerCase().contains(s.toString().toLowerCase())) {
							processAddressForServers.add(c);
						}
					}
				}

				processAddressForServersfilter = processAddressForServers;
				final CustomAdapter adapter = new CustomAdapter(ProcessOrder.this, processAddressForServers, true);
				listViewProcessOrder.setAdapter(adapter);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		sortby = (LinearLayout) findViewById(R.id.sortby);
		filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
		titlet_layout = (LinearLayout) findViewById(R.id.titlet_layout);
		reset_layout = (LinearLayout) findViewById(R.id.reset_layout);
		title = (TextView) findViewById(R.id.txt_logout);

		Img_clear_search = (ImageView) findViewById(R.id.clear_search);

		Img_clear_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_SearchWorkOrder.setText("");
				milestone_txt_filter.setText("All");
                SessionData.getInstance().setFilter_arrow_visible(0);


            }
		});
		txt_logout = (TextView) findViewById(R.id.txt_logout);
		txt_sync = (TextView) findViewById(R.id.txt_sync);
		flipper = (ViewFlipper) findViewById(R.id.viewfliper);
		lin_lis = (LinearLayout) findViewById(R.id.lin_list);
		lin_map = (LinearLayout) findViewById(R.id.lin_mapview);
		image_direction = (ImageButton) findViewById(R.id.imageBtn_direction);
		txt_direction = (TextView) findViewById(R.id.lbl_direction);
		image_direction.setVisibility(View.GONE);
		txt_direction.setVisibility(View.GONE);

		cancel = (TextView) findViewById(R.id.txt_cancel);
		Reset = (TextView) findViewById(R.id.txt_reset);
		MyList = (TextView) findViewById(R.id.txt_mylist);
		Filter = (TextView) findViewById(R.id.txt_filter);

		btn_hybrid = (Button) findViewById(R.id.btn_hydrid);
		btn_satellite = (Button) findViewById(R.id.btn_satellite);
		servelab = (TextView) findViewById(R.id.txtserlab);
		sortDuedate = (TextView) findViewById(R.id.txtsortDuedate);
		workorderlabel = (TextView) findViewById(R.id.txtworkorder);
		btn_standard = (Button) findViewById(R.id.btn_standard);

		lin_distance = (LinearLayout) findViewById(R.id.tab_distance);
		lin_distance.setVisibility(View.GONE);

		btn_hybrid.setOnClickListener(this);
		btn_satellite.setOnClickListener(this);
		btn_standard.setOnClickListener(this);

		cancel.setOnClickListener(this);
		Reset.setOnClickListener(this);
		MyList.setOnClickListener(this);
		Filter.setOnClickListener(this);

		btn_hybrid.setBackgroundColor(Color.WHITE);
		btn_satellite.setBackgroundColor(Color.DKGRAY);
		btn_standard.setBackgroundColor(Color.DKGRAY);

		mapview = (ImageButton) findViewById(R.id.imageBtn_mapView);
		lbl_self = (TextView) findViewById(R.id.lbl_courtservice);
		category = (ImageButton) findViewById(R.id.imageBtn_category);
		mapText = (TextView) findViewById(R.id.lbl_mapview);
		lbl_category = (TextView) findViewById(R.id.lbl_category);
		imageBtn_self = (ImageButton) findViewById(R.id.imageBtn_self);
		milestone_txt_filter = (TextView) findViewById(R.id.milestone_txt_filter);
		milestone_img_filter = (ImageView) findViewById(R.id.milestone_img_filter);
		filter_listView = (ListView) findViewById(R.id.filter_listView);
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
		SessionData.getInstance().setFilter_arrow_visible(0);



		category.setOnClickListener(this);
		lbl_category.setOnClickListener(this);
		mapview.setOnClickListener(this);

		mapText.setOnClickListener(this);
		lbl_self.setOnClickListener(this);
		servelab.setOnClickListener(this);
		sortDuedate.setOnClickListener(this);
		workorderlabel.setOnClickListener(this);
		imageBtn_self.setOnClickListener(this);
		image_direction.setOnClickListener(this);
		milestone_txt_filter.setOnClickListener(this);
		milestone_img_filter.setOnClickListener(this);

		processOrderlist = new ArrayList<ProcessAddressForServer>();

		lbl_self.setText("Process Orders");
		lbl_self.setTextColor(getResources().getColor(R.color.blue_text));

		findViewById(R.id.imageBtn_self).setSelected(true);

		mapview.setOnClickListener(this);
		lbl_self.setOnClickListener(this);
		flipper.setDisplayedChild(0);

		txt_logout.setOnClickListener(this);
		txt_sync.setOnClickListener(this);
		category.setOnClickListener(this);

		initializeControls();
		initializeData();
		googleMap();
		if (mapSouce != null && mapSouce.length() != 0) {
			findViewById(R.id.imageBtn_self).setSelected(false);
			mapview.setSelected(true);
			flipper.setDisplayedChild(1);
		}
	}

	private void initializeControls() {
		listViewProcessOrder = (DynamicListView) findViewById(R.id.listView_Processorder);

		tabCategory = (LinearLayout) findViewById(R.id.tab_category);
		tabCourt = (LinearLayout) findViewById(R.id.tab_court);
		tabMap = (LinearLayout) findViewById(R.id.tab_map);

		tabCategory.setOnClickListener(this);
		tabCourt.setOnClickListener(this);
		tabMap.setOnClickListener(this);
	}

	private void initializeData() {

		try {
			processOrderlist = new ArrayList<ProcessAddressForServer>();
			processOrderListArray = new ArrayList<ProcessAddressForServer>();
			processOrderListArray = database.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

			filterlist = new ArrayList<ProcessAddressForServer>();
//			 filterlist = database.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

			for (int i = 0; i < processOrderListArray.size(); i++) {

				ProcessAddressForServer process = processOrderListArray.get(i);
				if (process.getWorkorder().length() > 2) {

					processOrderlist.add(process);
				}
			}

		} catch (Exception e) {
			processOrderListArray = new ArrayList<ProcessAddressForServer>();
			e.printStackTrace();
		}

		processAddressForServersfilter = processOrderlist;

		adapter = new CustomAdapter(this, processOrderlist,
				true);
		listViewProcessOrder.setAdapter(adapter);

		listViewProcessOrder.enableDragAndDrop();
		listViewProcessOrder.setDraggableManager(new TouchViewDraggableManager(
				R.layout.list_courtservice_items));

		listViewProcessOrder.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
										   View view, int position, long id) {
				listViewProcessOrder.startDragging(position);
				listViewProcessOrder.setId(position);
				return true;
			}
		});
		listViewProcessOrder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

//				processOrderForTable = processOrderlist.get(position);
//
//				if ((processOrderlist.get(position).getMilestoneTitle().contains("Cancelled"))) {
//					return;
//				} else {
//					finish();
//					Intent processdetail = new Intent(ProcessOrder.this,
//							ProcessOrderDetail.class);
//					int getProcessOrderID = processOrderForTable.getProcessOrderID();
//					processdetail.putExtra("processOrderID", processOrderForTable.getProcessOrderID());
//					startActivity(processdetail);
//				}

			}
		});

		swipeGesture = new ListViewSwipeGesture(listViewProcessOrder, touchCallbacks, processOrderlist, ProcessOrder.this);
		swipeGesture.SwipeType = ListViewSwipeGesture.Double;
		listViewProcessOrder.setOnTouchListener(swipeGesture);


	}

	ListViewSwipeGesture.TouchCallbacks touchCallbacks = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(final int position, final ProcessAddressForServer address , String type) {


			if (type.contains("email"))
			{
				String to = "";
				String subject = "Regarding: " + address.getWorkorder() + ".";
				String body = "";

				if (address != null && address.getDispatcherEmail() != null)
					to = address.getDispatcherEmail();



				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
				i.putExtra(Intent.EXTRA_SUBJECT, subject);
				i.putExtra(Intent.EXTRA_TEXT   , body);

				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ProcessOrder.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}
			else if (type.contains("Reject"))
			{
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_reject);

				Button button_submit = (Button) dialog.findViewById(R.id.button_submit_reject);
				final EditText edt_reject = (EditText) dialog.findViewById(R.id.edt_reject);
				ImageView img_cancel = (ImageView) dialog.findViewById(R.id.img_cancel);

				img_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
						hideKeyboardFrom(ProcessOrder.this);
					}
				});

				button_submit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {


						sessionID = SessionData.getInstance().getSessionId();
						reason = edt_reject.getText().toString();
						addressLineItem = address.getAddressLineItem();
						workorder = address.getWorkorder();


						if (reason.length() == 0)
						{
							new CustomAlertDialog(ProcessOrder.this, "\n" + "Please enter the reason for rejct").show();

						}
						else
						{
							dialog.dismiss();

							int i = database.delete_ProcessOpenAddressID(address.getProcessOrderID());

							new AsyncLoginTask().execute();

							for (ProcessAddressForServer server : processOrderlist)
							{
								if (address == server)
								{
									processOrderlist.remove(address);
									break;
								}
							}
							CustomAdapter adapter = (CustomAdapter) listViewProcessOrder.getAdapter();
							adapter.getList().remove(position);
							adapter.notifyDataSetChanged();
						}


					}
				});

				dialog.show();


			}

		}

		@Override
		public void HalfSwipeListView(final int position) {

		}


		@Override
		public void OnClickListView(int position) {

			processOrderForTable = processAddressForServersfilter.get(position);

			if ((processAddressForServersfilter.get(position).getMilestoneTitle().contains("Cancelled"))) {
				return;
			} else {
				finish();
				Intent processdetail = new Intent(ProcessOrder.this, ProcessOrderDetail.class);
				int getProcessOrderID = processOrderForTable.getProcessOrderID();
				processdetail.putExtra("processOrderID", processOrderForTable.getProcessOrderID());
				startActivity(processdetail);
			}
		}

		@Override
		public void LoadDataForScroll() {

		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

			Log.d("Dimiss", ""+reverseSortedPositions);
		}
	};


	public void hideKeyboardFrom(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}



	public class AsyncRejectWorkorder extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(ProcessOrder.this);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				RejectWorkorderResult = WebServiceConsumer.getInstance().RejectWorkorder(
						sessionID,
						reason,
						addressLineItem,
						workorder
				);

			} catch (SocketTimeoutException e) {
				RejectWorkorderResult = null;
			} catch (Exception e) {
				RejectWorkorderResult = null;
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			ProgressBar.dismiss();

			if (RejectWorkorderResult != null)
			{
				if (RejectWorkorderResult.length() != 0)
				{
					new CustomAlertDialog(ProcessOrder.this, "\n" +RejectWorkorderResult.toUpperCase() + "").show();

				}

			}




		}
	}


	public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(ProcessOrder.this);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				sessionID = WebServiceConsumer.getInstance().signOn(TristarConstants.SOAP_ADDRESS, strUserName,strPassword);

			} catch (SocketTimeoutException e) {
				sessionID = null;
			} catch (Exception e) {
				sessionID = null;
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			ProgressBar.dismiss();

//			if (sessionID != null)
//			{
//				new AsyncRejectWorkorder().execute();
//			}

			if (sessionID.equals("rejected")
					|| sessionID.length() == 0
					|| sessionID.equals("Signon: server code / server password is invalid")) {
				new CustomAlertDialog(ProcessOrder.this, "Login information incorrect.").show();
			}
			else
			{
				new AsyncRejectWorkorder().execute();
			}



		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		//initilizeMap();

	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onClick(View v) {

		if (v == milestone_img_filter || v == milestone_txt_filter)
		{
			milestone_filter_dilaog();
		}

		if (v == tabMap || v == mapview || v == mapText) {
			if (SessionData.getInstance().getCheckvisible() == 1) {
				initializeData();
				googleMap();
			}

			findViewById(R.id.imageBtn_self).setSelected(false);
			findViewById(R.id.imageBtn_mapView).setSelected(true);
			lbl_self.setTextColor(getResources()
					.getColor(android.R.color.white));
			if (mapfunction == 1) {

				getGps();
			} else if (mapfunction == 2) {
				Intent intent = new Intent(ProcessOrder.this, ProcessOrder.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				flipper.setDisplayedChild(1);
			}
			mapText.setTextColor(getResources().getColor(R.color.blue_text));
			flipper.setDisplayedChild(1);

			sortby.setVisibility(View.GONE);

			filter_layout.setVisibility(View.GONE);
			titlet_layout.setVisibility(View.VISIBLE);
			reset_layout.setVisibility(View.GONE);

			/*if (Filter.getVisibility() == View.VISIBLE && cancel.getVisibility() == View.VISIBLE){
				Filter.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				Reset.setVisibility(View.GONE);
				MyList.setVisibility(View.GONE);
			}else if (Filter.getVisibility() == View.GONE && cancel.getVisibility() == View.GONE){
				Filter.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				Reset.setVisibility(View.VISIBLE);
				MyList.setVisibility(View.VISIBLE);
			}else {
				Filter.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				Reset.setVisibility(View.GONE);
				MyList.setVisibility(View.GONE);
			}*/

		} else if (v == tabCourt || v == imageBtn_self || v == lbl_self) {
			findViewById(R.id.imageBtn_self).setSelected(true);
			findViewById(R.id.imageBtn_mapView).setSelected(false);
			lbl_self.setTextColor(getResources().getColor(R.color.blue_text));
			mapText.setTextColor(getResources().getColor(android.R.color.white));
			flipper.setDisplayedChild(0);

			sortby.setVisibility(View.VISIBLE);

			filter_layout.setVisibility(View.VISIBLE);
			titlet_layout.setVisibility(View.VISIBLE);
			reset_layout.setVisibility(View.VISIBLE);

			/*if (Filter.getVisibility() == View.VISIBLE && cancel.getVisibility() == View.VISIBLE){
				Filter.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				Reset.setVisibility(View.GONE);
				MyList.setVisibility(View.GONE);
			}else if (Filter.getVisibility() == View.GONE && cancel.getVisibility() == View.GONE){
				Filter.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				Reset.setVisibility(View.VISIBLE);
				MyList.setVisibility(View.VISIBLE);
			}else {
				Filter.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				Reset.setVisibility(View.GONE);
				MyList.setVisibility(View.GONE);
			}*/

		} else if (v == btn_hybrid) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			if (googleMapfilter != null) {
				googleMapfilter.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			}
			btn_hybrid.setBackgroundColor(Color.WHITE);
			btn_satellite.setBackgroundColor(Color.DKGRAY);
			btn_standard.setBackgroundColor(Color.DKGRAY);
		} else if (v == btn_satellite) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			if (googleMapfilter != null) {
				googleMapfilter.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			}
			btn_hybrid.setBackgroundColor(Color.DKGRAY);
			btn_satellite.setBackgroundColor(Color.WHITE);
			btn_standard.setBackgroundColor(Color.DKGRAY);
		} else if (v == btn_standard) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			if (googleMapfilter != null) {
				googleMapfilter.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
			btn_hybrid.setBackgroundColor(Color.DKGRAY);
			btn_satellite.setBackgroundColor(Color.DKGRAY);
			btn_standard.setBackgroundColor(Color.WHITE);
		} else if (v == image_direction) {
			if (SessionData.getInstance().getDirection() == null || SessionData.getInstance().getDirection().size() == 0) {

				new CustomAlertDialog(ProcessOrder.this,
						"Selected Address is InValid! Please Select Proper Address").show();
			} else {
				dierctions();
			}
		} else if (v == txt_logout) {
			MainActivity.setLogoutStatus(MainActivity.sharedPreferences, true);
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alertdialog);

			TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
			text.setText("Are you sure you want to logout?");

			Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
			Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_no);

			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
					Intent court = new Intent(ProcessOrder.this,
							MainActivity.class);
					startActivity(court);

				}
			});
			dialogButton1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			dialog.show();

			// new CustomAlertDialog(ProcessOrder.this,
			// getResources().getString(R.string.alert_logout), true,
			// CustomAlertDialog.SYNC).show();
		} else if (v == txt_sync) {
			new CustomAlertDialog(this, getResources().getString(R.string.alert_sync), CustomAlertDialog.SYNC).show();
		} else if (v == tabCategory || v == category || v == lbl_category) {

			MyList.setVisibility(View.VISIBLE);
			Reset.setVisibility(View.VISIBLE);
			Filter.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);

			@SuppressLint("InflateParams") View vi = getLayoutInflater().inflate(
					R.layout.list_courtservice_items, null);
			CheckBox check = (CheckBox) vi
					.findViewById(R.id.check_process_order);
			check.setVisibility(View.GONE);
			SessionData.getInstance().setCheckvisible(2);
			listViewProcessOrder.setAdapter(new CustomAdapter(this,
					processOrderlist, true));
			finish();
			Intent category = new Intent(ProcessOrder.this, ListCategory.class);
			startActivity(category);
		} else if (v == servelab) {

			if (click == 0) {

				processAddressForServersfilter = processOrderlist;
				listViewProcessOrder.setAdapter(new CustomAdapter(this,
						processOrderlist, true));

				Collections.sort(processOrderlist, StringAscComparator);
				Toast.makeText(ProcessOrder.this, "Sort Servee By Ascending ", Toast.LENGTH_LONG).show();
				click = 1;
			} else if (click == 1) {
				listViewProcessOrder.setAdapter(new CustomAdapter(this,
						processOrderlist, true));

				Collections.sort(processOrderlist, StringDescComparator);
				Toast.makeText(ProcessOrder.this, "Sort Servee By Descending", Toast.LENGTH_LONG).show();
				click = 0;
			}

		}
		else if (v == sortDuedate) {

			if (click == 0) {

				Collections.sort(processOrderlist, DateAsc_Comparator);
				listViewProcessOrder.setAdapter(new CustomAdapter(this, processOrderlist, true));
				Toast.makeText(ProcessOrder.this, "Sort Due date By Ascending ", Toast.LENGTH_LONG).show();
				click = 1;

			} else if (click == 1) {

				Collections.sort(processOrderlist, DateDse_Comparator);
				listViewProcessOrder.setAdapter(new CustomAdapter(this, processOrderlist, true));
				Toast.makeText(ProcessOrder.this, "Sort Due date By Descending", Toast.LENGTH_LONG).show();
				click = 0;
			}

		}
		else if (v == workorderlabel) {

			if (click == 0) {
				listViewProcessOrder.setAdapter(new CustomAdapter(this,
						processOrderlist, true));

				Collections.sort(processOrderlist, StringworkorderAscComparator);
				Toast.makeText(ProcessOrder.this, "Sort Workorder By Ascending", Toast.LENGTH_LONG).show();
				click = 1;
			} else if (click == 1) {
				listViewProcessOrder.setAdapter(new CustomAdapter(this,
						processOrderlist, true));

				Collections.sort(processOrderlist, StringworkorderDseComparator);
				Toast.makeText(ProcessOrder.this, "Sort Workorder By Descending", Toast.LENGTH_LONG).show();
				click = 0;
			}
		} else if (cancel == v) {
			MyList.setVisibility(View.VISIBLE);
			Reset.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.GONE);
			Filter.setVisibility(View.GONE);
			@SuppressLint("InflateParams") View vi = getLayoutInflater().inflate(
					R.layout.list_courtservice_items, null);
			CheckBox check = (CheckBox) vi
					.findViewById(R.id.check_process_order);
			check.setVisibility(View.GONE);
			SessionData.getInstance().setCheckvisible(2);
			listViewProcessOrder.setAdapter(new CustomAdapter(this,
					processOrderlist, true));
			//googleMap();

		} else if (MyList == v) {
			MyList.setVisibility(View.GONE);
			Reset.setVisibility(View.GONE);
			cancel.setVisibility(View.VISIBLE);
			Filter.setVisibility(View.VISIBLE);
			@SuppressLint("InflateParams") View vi = getLayoutInflater().inflate(R.layout.list_courtservice_items, null);
			CheckBox check = (CheckBox) vi
					.findViewById(R.id.check_process_order);
			check.setVisibility(View.VISIBLE);

			for (int i = 0; i < processOrderlist.size(); i++) {

				processOrderlist.get(i).setSelected(false);
			}

			SessionData.getInstance().setCheckvisible(1);
			listViewProcessOrder.setAdapter(new CustomAdapter(this,
					processOrderlist, true));
			//googleMap();

		} else if (Filter == v) {


			MyList.setVisibility(View.VISIBLE);
			Reset.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.GONE);
			Filter.setVisibility(View.GONE);
			mapfunction = 1;
			SessionData.getInstance().setCheckvisible(2);
			processOrderlist = new ArrayList<ProcessAddressForServer>();

			for (int i = 0; i < processOrderListArray.size(); i++) {

				ProcessAddressForServer process = processOrderListArray.get(i);
				if (processOrderListArray.get(i).isSelected()) {

					processOrderlist.add(process);
					filterlist.add(process);
				}
			}
			if (processOrderlist.size() != 0) {
				listViewProcessOrder.setAdapter(new CustomAdapter(this,
						processOrderlist, true));

				for (int i = 0; i < processOrderlist.size(); i++) {

				}


			} else {


//				listViewProcessOrder.setAdapter(new CustomAdapter(this,
//						null, true));
			}
			image_direction.setVisibility(View.VISIBLE);
			txt_direction.setVisibility(View.VISIBLE);
			lin_distance.setVisibility(View.VISIBLE);

		} else if (Reset == v) {

			SessionData.getInstance().setCheckvisible(2);
			processOrderlist = new ArrayList<ProcessAddressForServer>();


			for (int i = 0; i < processOrderListArray.size(); i++) {

				ProcessAddressForServer process = processOrderListArray.get(i);
				if (process.getWorkorder().length() > 2) {

					processOrderlist.add(process);
				}
			}
			mapfunction = 2;
			listViewProcessOrder.setAdapter(new CustomAdapter(this,
					processOrderlist, true));
			googleMap();

		}
	}

	private static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {

		String result="";

		SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
		SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
		Date date=null;
		try {
			date = formatterOld.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			result = formatterNew.format(date);
		}
		return result;
	}



	private void milestone_filter_dilaog() {

		ArrayList<String> filterList = new ArrayList<String>(Arrays.asList("All", "Active", "Cancelled", "Rush"));

		MilestoneFilter_Adapter adapter = new MilestoneFilter_Adapter(ProcessOrder.this, filterList, new MilestoneFilter_Adapter.ListInterFace() {
			@Override
			public void OnClick(int pos, String filter_by) {

				milestone_txt_filter.setText(filter_by);

				if (filter_by.contains("All")) filter_by = "";
				ArrayList<ProcessAddressForServer> temp_processOrderlist = new ArrayList<ProcessAddressForServer>();

				for (ProcessAddressForServer server : processOrderlist)
				{
					Boolean isContain = server.getMilestoneTitle().toLowerCase().contains(filter_by.toLowerCase());
					if (isContain)
					{
						isContain = false;
						temp_processOrderlist.add(server);
					}
				}



				if (filter_by.contains("Rush") && temp_processOrderlist.size() == 0)
				{

					for (ProcessAddressForServer server : processOrderlist)
					{
						Boolean isContain = server.getPriorityTitle().toLowerCase().contains(filter_by.toLowerCase());
						if (isContain)
						{
							isContain = false;
							temp_processOrderlist.add(server);
						}
					}
				}

				processAddressForServersfilter = temp_processOrderlist;

				listViewProcessOrder.setAdapter(new CustomAdapter(ProcessOrder.this, temp_processOrderlist, true));
				getAnim(milestone_layout, 0.0f);


			}
		});
		milestone_layout = (LinearLayout) findViewById(R.id.milestone_layout);

		if (milestone_layout.getVisibility() == View.VISIBLE )
		{
			getAnim(milestone_layout, 0.0f);
		}
		else
		{
//			milestone_layout.setVisibility(View.VISIBLE);
			getAnim(milestone_layout, 1.0f);
		}
		ListView filter_listView = (ListView) findViewById(R.id.filter_listView);
		filter_listView.setAdapter(adapter);

	}

	public void getAnim(final View view, final float alpha)
	{

	    if(alpha == 1.0f)
        {
            milestone_layout.setVisibility(View.VISIBLE);
        }

		view.animate()
				.alpha(alpha)
				.setDuration(500)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);

//						view.setVisibility(view.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);

						if (alpha == 0.0f)
							milestone_layout.setVisibility(View.INVISIBLE);

					}
				});

	}



	private void dierctions() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(ProcessOrder.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.directiondetails_list);

		ListView list = (ListView) dialog.findViewById(R.id.directionlist);
		TextView text = (TextView) dialog.findViewById(R.id.alertmessage);
		ImageView close = (ImageView) dialog
				.findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.listrow, SessionData.getInstance().getDirection());

		Log.d("direction data", "" + SessionData.getInstance().getDirection());

		list.setAdapter(new CustomdirectionAdapter(this, SessionData.getInstance().getDirection()));

		dialog.show();


	}

	private void googleMapFilter() {
		//getGps();
		// TODO Auto-generated method stub
		try {
			// Showing / hiding your current location
			initilizeMapFilter();
			googleMap.clear();

			if (googleMapfilter == null)
				return;
			markerPoints = new ArrayList<LatLng>();
			googleMap.clear();
			googleMapfilter.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			googleMapfilter.setMyLocationEnabled(true);
			lin_distance.setVisibility(View.VISIBLE);

			// Enable / Disable zooming controls
			googleMapfilter.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMapfilter.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMapfilter.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMapfilter.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMapfilter.getUiSettings().setZoomGesturesEnabled(true);
			lineOptions = new PolylineOptions();
			// lets place some 10 random markers
			Log.d("processOrderlist size", "" + processOrderlist.size());
//			 latitude = gpstrack.getLatitude();
//				longitude = gpstrack.getLongitude();
			markerPoints.add(
					new LatLng(latitude, longitude));

			for (int i = 0; i < processOrderlist.size(); i++) {
				processfilterMaps = processOrderlist.get(i);
				//filterlist.add(processOrderlist.get(i));
				processMaps = null;
				markerSelect = i;
				// random latitude and logitude
				double[] randomLocation = createRandLocation(
						Double.parseDouble(processfilterMaps.getLatitude()),
						Double.parseDouble(processfilterMaps.getLongitude()));


				// Adding a marker
				MarkerOptions markerfilter = new MarkerOptions().position(
						new LatLng(randomLocation[0], randomLocation[1]))
						.title(processfilterMaps.getWorkorder() + "\n"
								+ processfilterMaps.getAddressFormattedForGoogle());

				markerPoints.add(
						new LatLng(randomLocation[0], randomLocation[1]));


				Log.e("Random", "> " + randomLocation[0] + ","
						+ +randomLocation[1]);


				// changing marker color
				markerfilter.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));

				googleMapfilter.addMarker(markerfilter);


				// Move the camera to last position with a zoom level
				if (i == 0) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(randomLocation[0],
									randomLocation[1])).zoom(5).build();

					googleMapfilter.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}

			Log.d("Marker point", "" + markerPoints.size());

			if (markerPoints.size() >= 2) {
				LatLng origin = markerPoints.get(0);
				LatLng dest = markerPoints.get(1);

				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(origin, dest);

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			}
			image_direction.setVisibility(View.VISIBLE);
			txt_direction.setVisibility(View.VISIBLE);

			googleMapfilter.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					int j = Integer.parseInt(marker.getId().substring(1));
					final ProcessAddressForServer server = filterlist.get(j);
					final Dialog dialog = new Dialog(ProcessOrder.this, android.R.style.Theme_Translucent);


					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.google_filter_dialog);


					Log.d("View", "New View");
					TextView info = (TextView) dialog.findViewById(R.id.txt_udno);
					TextView address = (TextView) dialog
							.findViewById(R.id.txt_addresslist);
					ImageView icon = (ImageView) dialog.findViewById(R.id.csdirection);

					icon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String uriBegin = "google.navigation:";
							String uriString = uriBegin + "?q=" + server.getAddressFormattedForGoogle();
							Uri uri = Uri.parse(uriString);

							Log.d("URI", "" + uriString);
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
							startActivity(intent);
							dialog.dismiss();
						}
					});

					info.setText(server.getWorkorder());
					address.setText(server.getAddressFormattedForDisplay());

					address.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							//
							Intent intent = new Intent(ProcessOrder.this,
									ProcessOrderDetail.class);
							intent.putExtra("processOrderID",
									server.getProcessOrderID());
							intent.putExtra("mapview", "mapview");
							startActivity(intent);
						}
					});

					dialog.show();
					Window window = dialog.getWindow();
					window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					return true;
				}
			});

//			googleMapfilter
//					.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//
//						@Override
//						public void onInfoWindowClick(Marker marker) {
//							int j = Integer.parseInt(marker.getId()
//									.substring(1));
//							final ProcessAddressForServer server = filterlist
//									.get(j);
//							
//							//finish();
//							final Dialog dialog = new Dialog(ProcessOrder.this);
//							dialog.getWindow().setBackgroundDrawable(
//									new ColorDrawable(Color.TRANSPARENT));
//							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//							dialog.setContentView(R.layout.router_alertbox);
//							TextView workorder = (TextView)dialog.findViewById(R.id.router_workorder);
//							
//							workorder.setText(server.getWorkorder());
//							
//							TextView address = (TextView)dialog.findViewById(R.id.router_Address);
//							
//							address.setText("Process Order Detail");
//							
//							address.setOnClickListener(new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//
//									Intent intent = new Intent(ProcessOrder.this,
//											ProcessOrderDetail.class);
//									intent.putExtra("processOrderID",
//											server.getProcessOrderID());
//									intent.putExtra("mapview", "mapview");
//									startActivity(intent);
//									dialog.dismiss();
//								}
//							});
//							
//							TextView router = (TextView)dialog.findViewById(R.id.router_navigation);
//							router.setText("Navigation");
//							router.setOnClickListener(new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//
//									String uriBegin = "google.navigation:";
//									String uriString = uriBegin + "?q=" + server.getAddressFormattedForGoogle();
//									Uri uri = Uri.parse(uriString);
//									
//									Log.d("URI", "" + uriString);
//									Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
//									startActivity(intent);
//									dialog.dismiss();
//								}
//							});
//							
//							
//							dialog.show();
//							
//							
//							
//						}
//					});
//
//			googleMapfilter.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//				@Override
//				public View getInfoWindow(Marker arg0) {
//					return null;
//				}
//
//				@Override
//				public View getInfoContents(Marker marker) {
//					int j = Integer.parseInt(marker.getId().substring(1));
//					ProcessAddressForServer server = filterlist.get(j);
//
//					View v = getLayoutInflater().inflate(
//							R.layout.list_courtservice_items, null);
//					v.setBackgroundResource(R.drawable.mapview);
//					LayoutParams lp = new LayoutParams((int) (300 * dpi),
//							(int) (50 * dpi));
//					v.setLayoutParams(lp);
//
//					TextView info = (TextView) v.findViewById(R.id.txt_udno);
//					TextView address = (TextView) v
//							.findViewById(R.id.txt_addresslist);
//
//					info.setText(server.getWorkorder());
//					address.setText(server.getAddressFormattedForDisplay());
//
//					return v;
//				}
//			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void googleMap() {
		try {
			// Showing / hiding your current location
			initilizeMap();
			if (googleMapfilter != null) {
				googleMapfilter.clear();
			}
			image_direction.setVisibility(View.GONE);
			txt_direction.setVisibility(View.GONE);
			lin_distance.setVisibility(View.GONE);

			if (googleMap == null)
				return;
			///	markerPoints = new ArrayList<LatLng>();

			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			googleMap.setMyLocationEnabled(true);

			googleMap.setIndoorEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			lineOptions = new PolylineOptions();
			// lets place some 10 random markers
			for (int i = 0; i < processOrderlist.size(); i++) {

				processMaps = processOrderlist.get(i);
				filterlist.add(processOrderlist.get(i));
				markerSelect = i;
				// random latitude and logitude
				double[] randomLocation = createRandLocation(
						Double.parseDouble(processMaps.getLatitude()),
						Double.parseDouble(processMaps.getLongitude()));

				Log.d("Latitude", "" + Double.parseDouble(processMaps.getLatitude()));
				Log.d("Longitude", "" + Double.parseDouble(processMaps.getLongitude()));

				if (Double.parseDouble(processMaps.getLatitude()) != 0.0
						&& Double.parseDouble(processMaps.getLongitude()) != 0.0) {

					// Adding a marker
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(randomLocation[0], randomLocation[1]))
							.title(processMaps.getWorkorder() + "\n"
									+ processMaps.getAddressFormattedForGoogle());

//				markerPoints.add(
//						new LatLng(randomLocation[0], randomLocation[1]));
//
					Log.e("Random", "> " + randomLocation[0] + ","
							+ +randomLocation[1]);


					// changing marker color
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));

					googleMap.addMarker(marker);

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(randomLocation[0],
									randomLocation[1])).zoom(5).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));

				}
//				else{
//					Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//					String result = null;
//					try {
//						List<Address> addressList = geocoder.getFromLocationName(processMaps
//								.getAddressFormattedForGoogle(), 1);
//						if (addressList != null && addressList.size() > 0) {
//							Address address = addressList.get(0);
//							StringBuilder sb = new StringBuilder();
//							sb.append(address.getLatitude()).append("\n");
//							sb.append(address.getLongitude()).append("\n");
//							Log.d("Geo Code",""+address.getLatitude()+","+address.getLongitude());
//							processMaps.setLatitude(address.getLatitude() + "");
//							processMaps.setLongitude(address.getLongitude() + "");
//							double[] randomLocation1 = createRandLocation(
//									Double.parseDouble(processMaps.getLatitude()),
//									Double.parseDouble(processMaps.getLongitude()));
//							MarkerOptions marker = new MarkerOptions().position(
//									new LatLng(randomLocation1[0], randomLocation1[1]))
//									.title(processMaps.getWorkorder() + "\n"
//											+ processMaps.getAddressFormattedForGoogle());
//
////				markerPoints.add(
////						new LatLng(randomLocation[0], randomLocation[1]));
////
//							Log.e("Random", "> " + randomLocation[0] + ","
//									+ +randomLocation[1]);
//
//
//							// changing marker color
//							marker.icon(BitmapDescriptorFactory
//									.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//							googleMap.addMarker(marker);
//
//							result = sb.toString();
//						}
//					} catch (IOException e) {
//						Log.e("Error", "Unable to connect to Geocoder", e);
//					}
//				}

				// Move the camera to last position with a zoom level
				if (i == 0) {

				}
			}

			//Log.d("Marker point", "" + markerPoints.size());

//			if(markerPoints.size() >= 2){					
//				LatLng origin = markerPoints.get(0);
//				LatLng dest = markerPoints.get(1);
//				
//				// Getting URL to the Google Directions API
//				String url = getDirectionsUrl(origin, dest);				
//				
//				DownloadTask downloadTask = new DownloadTask();
//				
//				// Start downloading json data from Google Directions API
//				downloadTask.execute(url);
//			}


//			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//				
//				@Override
//				public boolean onMarkerClick(Marker marker) {
//
//					int j = Integer.parseInt(marker.getId().substring(1));
//					final ProcessAddressForServer server = filterlist.get(j);
//					final Dialog dialog = new Dialog(ProcessOrder.this);
//					dialog.getWindow().setBackgroundDrawableResource(R.drawable.mapview);
//					
//					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//					dialog.setContentView(R.layout.list_courtservice_items);
//					
//
//					Log.d("View", "New View");
//					TextView info = (TextView) dialog.findViewById(R.id.txt_udno);
//					TextView address = (TextView) dialog
//							.findViewById(R.id.txt_addresslist);
//
//					info.setText(server.getWorkorder());
//					address.setText(server.getAddressFormattedForDisplay());
//					
//					address.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//
//							Intent intent = new Intent(ProcessOrder.this,
//									ProcessOrderDetail.class);
//							intent.putExtra("processOrderID",
//									server.getProcessOrderID());
//							intent.putExtra("mapview", "mapview");
//							startActivity(intent);
//						}
//					});
//
//					dialog.show();
//					return true;
//				}
//			});

			googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker marker) {

//							finish();
							int j = Integer.parseInt(marker.getId().substring(1));

							if ( j < filterlist.size())
							{
								ProcessAddressForServer server = filterlist.get(j);
								Intent intent = new Intent(ProcessOrder.this,
										ProcessOrderDetail.class);
								intent.putExtra("processOrderID",
										server.getProcessOrderID());
								intent.putExtra("mapview", "mapview");
								startActivity(intent);
							}


						}
					});


			googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {

					int j = Integer.parseInt(marker.getId().substring(1));

					ProcessAddressForServer server = new ProcessAddressForServer();


					@SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.list_courtservice_items, null);

					if ( j < filterlist.size()) {
						server = filterlist.get(j);

						v.setBackgroundResource(R.drawable.mapview);
						LayoutParams lp = new LayoutParams((int) (300 * dpi),
								LayoutParams.WRAP_CONTENT);
						v.setLayoutParams(lp);

						Log.d("View", "New View");
						TextView info = (TextView) v.findViewById(R.id.txt_udno);
						TextView address = (TextView) v
								.findViewById(R.id.txt_addresslist);

						info.setText(server.getWorkorder());
						address.setText(server.getAddressFormattedForDisplay());

					}
					return v;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "";
		for (int i = 2; i < markerPoints.size(); i++) {
			LatLng point = (LatLng) markerPoints.get(i);
			if (i == 2)
				waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		Log.d("url", "" + url);

		Log.d("parameters", "" + parameters);

		return url;
	}

	/**
	 * A method to download json data from url
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
//	            Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private double[] createRandLocation(double latitude, double longitude) {

		return new double[]{latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10)};
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		Intent intent = new Intent(ProcessOrder.this, ListCategory.class);
		startActivity(intent);
	}

	private void initilizeMap() {

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();


			// Enable MyLocation Button in the Map
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			googleMap.setMyLocationEnabled(true);

			// check if map is created successfully or not
			if (googleMap != null) {
				return;
			}
		}
		return;
	}

	private void initilizeMapFilter() {

		if (googleMapfilter == null) {
			googleMapfilter = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();


			// Enable MyLocation Button in the Map
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			googleMapfilter.setMyLocationEnabled(true);
			// check if map is created successfully or not
			if (googleMapfilter != null) {
				return;
			}
		}
		return;
	}

	public void getGps() {
		gpstrack = new GPSTracker(ProcessOrder.this);
		if (gpstrack.canGetLocation()) {
			latitude = gpstrack.getLatitude();
			longitude = gpstrack.getLongitude();

			googleMapFilter();
		} else {

			gpstrack.showSettingsAlert();
		}
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service

			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				data = null;
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

			Log.d("Parser result", "" + result);
		}
	}

	/**
	 * A class to parse the Google Places in JSON format
	 */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();


				// Starts parsing data
				routes = parser.parse(jObject);
				Log.d("Parser result 2", "" + parser.parse(jObject));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			//Log.d("result", ""+result.size());
			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);


					//Log.d("positions", ""+position);

					points.add(position);
				}
				Log.d("path.size", "" + path.size());
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.GREEN);

			}
			Log.d("line", "" + lineOptions);
			// Drawing polyline in the Google Map for the i-th route
			if (lineOptions != null) {
				googleMapfilter.addPolyline(lineOptions);
			}
		}
	}

}