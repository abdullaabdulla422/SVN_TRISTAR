package com.tristar.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tristar.adapters.CustomAdapter;
import com.tristar.adapters.MilestoneFilter_Adapter;
import com.tristar.db.DataBaseHelper;
import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;
import com.tristar.utils.Sorting;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
public class CourtService extends FragmentActivity implements OnClickListener {
	public static Object selectedAddressServer;
	public static Object selectedAddres;
	public static boolean mapselected = false;
	ListView ListViewCourtService;
	ViewFlipper flipper;
	Context context;
	TextView courtservice, mapText, lbl_category, txt_logout, txt_sync,
			txt_nvno, txt_nvval, txt_nvno2, txt_nvval2, txtReceivedDate;
	ImageButton category, mapview, imageBtn_self;
	Button btn_hybrid, btn_satellite, btn_standard;
	DataBaseHelper database;
	ArrayList<Object> serviceAddressAll, serviceAddressAllForMap;
	ArrayList<CourtAddressForServer> courtServiceListArray;
	ArrayList<AddressForServer> pickupServiceListArray;
	ArrayList<AddressForServer> deliveryServiceListArray;
	ListView listview;
	String Lati, longi;
	private GoogleMap googleMap;
	private double dpi;
	private LinearLayout tabCategory, tabCourt, tabMap;
	private TextView milestone_txt_filter;
	private ImageView milestone_img_filter;
	private ListView filter_listView;
	private LinearLayout milestone_layout;
	private int click = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.courtservice);
		database = DataBaseHelper.getInstance();
		SessionData.getInstance().setSelectedItem(0);
		SessionData.getInstance().clearAttachments();
		context = this;
		if (database == null) {
			database = DataBaseHelper.getInstance(CourtService.this);
		}

		dpi = getResources().getDisplayMetrics().density;
		courtServiceListArray = new ArrayList<CourtAddressForServer>();
		pickupServiceListArray = new ArrayList<AddressForServer>();
		deliveryServiceListArray = new ArrayList<AddressForServer>();

		serviceAddressAll = new ArrayList<Object>();
		serviceAddressAllForMap = new ArrayList<Object>();

		intializeControlls();
		intializeData();
		googleMap();
		if (mapselected) {
			imageBtn_self.setSelected(false);
			mapview.setSelected(true);
			mapselected = true;
			flipper.setDisplayedChild(1);
			mapText.setTextColor(getResources().getColor(R.color.blue_text));
			courtservice.setTextColor(getResources().getColor(
					android.R.color.white));
		} else {
			imageBtn_self.setSelected(true);
			mapview.setSelected(false);
			mapselected = false;
			flipper.setDisplayedChild(0);
			courtservice.setTextColor(getResources()
					.getColor(R.color.blue_text));
			mapText.setTextColor(getResources().getColor(android.R.color.white));
		}
	}


	@SuppressLint("SetTextI18n")
	public void onClick(View v) {

		if (v == milestone_img_filter || v == milestone_txt_filter)
		{
			milestone_filter_dilaog();
		}
		else if ( v == txtReceivedDate)
		{

			sortByReceivedDate();
		}
		else if (v == tabMap || v == mapview || v == mapText) {
			imageBtn_self.setSelected(false);
			mapview.setSelected(true);
			mapselected = true;
			flipper.setDisplayedChild(1);
			googleMap();
			mapText.setTextColor(getResources().getColor(R.color.blue_text));
			courtservice.setTextColor(getResources().getColor(
					android.R.color.white));

		} else if (v == tabCourt || v == imageBtn_self || v == courtservice) {
			imageBtn_self.setSelected(true);
			mapview.setSelected(false);
			mapselected = false;
			flipper.setDisplayedChild(0);
			courtservice.setTextColor(getResources()
					.getColor(R.color.blue_text));
			mapText.setTextColor(getResources().getColor(android.R.color.white));
		} else if (v == tabCategory || v == category || v == lbl_category) {
			finish();
			mapselected = false;
			Intent category = new Intent(CourtService.this, ListCategory.class);
			startActivity(category);
		} else if (v == txt_logout) {

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
					MainActivity.setLogoutStatus(MainActivity.sharedPreferences, true);
					dialog.dismiss();
					finish();
					Intent court = new Intent(CourtService.this,
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

		} else if (v == txt_sync) {
            SessionData.getInstance().setSynchandler(1);
			new CustomAlertDialog(this, getResources().getString(
					R.string.alert_sync), CustomAlertDialog.SYNC).show();
		} else if (v == btn_hybrid) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			btn_hybrid.setBackgroundColor(Color.WHITE);
			btn_satellite.setBackgroundColor(Color.DKGRAY);
			btn_standard.setBackgroundColor(Color.DKGRAY);
		} else if (v == btn_satellite) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			btn_hybrid.setBackgroundColor(Color.DKGRAY);
			btn_satellite.setBackgroundColor(Color.WHITE);
			btn_standard.setBackgroundColor(Color.DKGRAY);
		} else if (v == btn_standard) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			btn_hybrid.setBackgroundColor(Color.DKGRAY);
			btn_satellite.setBackgroundColor(Color.DKGRAY);
			btn_standard.setBackgroundColor(Color.WHITE);
		}

	}

	private void sortByReceivedDate() {


		ArrayList<Object> objectArrayList = new ArrayList<Object>();

		ArrayList<CourtAddressForServer> courtServiceList = new ArrayList<CourtAddressForServer>();
		ArrayList<AddressForServer> pickupServiceList = new ArrayList<AddressForServer>();
		ArrayList<AddressForServer> deliveryServiceList = new ArrayList<AddressForServer>();

		courtServiceListArray = database.getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
		pickupServiceListArray = database.getPickupOrderValuesFromTable();
		deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();

		if (click == 0)
		{
			Collections.sort(courtServiceListArray, Sorting.DateAsc_CourtAddressForServer);
			if (courtServiceListArray.size() > 0)
			{
				objectArrayList.add(("Court Services"));
				objectArrayList.addAll(courtServiceListArray);
			}

			Collections.sort(pickupServiceListArray, Sorting.DateAsc_AddressForServer);
			if (pickupServiceListArray.size() > 0)
			{
				objectArrayList.add(("Pickup Services"));
				objectArrayList.addAll(pickupServiceListArray);
			}

			Collections.sort(deliveryServiceListArray, Sorting.DateAsc_AddressForServer);
			if (deliveryServiceListArray.size() > 0)
			{
				objectArrayList.add(("Delivery Services"));
				objectArrayList.addAll(deliveryServiceListArray);
			}

			listview.setAdapter(new EntryAdapter(CourtService.this, objectArrayList));
			Toast.makeText(CourtService.this, "Sort By Aecending", Toast.LENGTH_LONG).show();

			click = 1;
		}
		else if (click == 1)
		{
			Collections.sort(courtServiceListArray, Sorting.DateDes_CourtAddressForServer);
			if (courtServiceListArray.size() > 0)
			{
				objectArrayList.add(("Court Services"));
				objectArrayList.addAll(courtServiceListArray);
			}

			Collections.sort(pickupServiceListArray, Sorting.DateDes_AddressForServer);
			if (pickupServiceListArray.size() > 0)
			{
				objectArrayList.add(("Pickup Services"));
				objectArrayList.addAll(pickupServiceListArray);
			}

			Collections.sort(deliveryServiceListArray, Sorting.DateDes_AddressForServer);
			if (deliveryServiceListArray.size() > 0)
			{
				objectArrayList.add(("Delivery Services"));
				objectArrayList.addAll(deliveryServiceListArray);
			}

			listview.setAdapter(new EntryAdapter(CourtService.this, objectArrayList));
			Toast.makeText(CourtService.this, "Sort By Descending", Toast.LENGTH_LONG).show();
			click = 0;
		}












	}

	private void milestone_filter_dilaog() {

		ArrayList<String> filterList = new ArrayList<String>(Arrays.asList("All", "Active", "Cancelled", "Rush"));

		MilestoneFilter_Adapter adapter = new MilestoneFilter_Adapter(CourtService.this, filterList, new MilestoneFilter_Adapter.ListInterFace() {
			@Override
			public void OnClick(int pos, String filter_by) {

				milestone_txt_filter.setText(filter_by);

				if (filter_by.contains("All"))
				{
					filter_by = "";
					listview.setAdapter(new EntryAdapter(CourtService.this, serviceAddressAll));
					getAnim(milestone_layout, 0.0f);

				}else
				{

					ArrayList<Object> objectArrayList = new ArrayList<Object>();

					ArrayList<CourtAddressForServer> courtServiceList = new ArrayList<CourtAddressForServer>();
					ArrayList<AddressForServer> pickupServiceList = new ArrayList<AddressForServer>();
					ArrayList<AddressForServer> deliveryServiceList = new ArrayList<AddressForServer>();

					courtServiceListArray = database.getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
					pickupServiceListArray = database.getPickupOrderValuesFromTable();
					deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();

					for (CourtAddressForServer object : courtServiceListArray)
					{
						Boolean isContain = object.getMilestoneTitle().toLowerCase().contains(filter_by.toLowerCase());
						if (isContain)
						{
							isContain = false;
							courtServiceList.add(object);
						}

					}

					if (courtServiceList.size() > 0) {
						objectArrayList.add(("Court Services"));
						objectArrayList.addAll(courtServiceList);
					}


					for (AddressForServer object : pickupServiceListArray)
					{
						Boolean isContain = object.getMilestoneTitle().toLowerCase().contains(filter_by.toLowerCase());
						if (isContain)
						{
							isContain = false;
							pickupServiceList.add(object);
						}

					}

					if (pickupServiceList.size() > 0) {
						objectArrayList.add(("Pickup Services"));
						objectArrayList.addAll(pickupServiceList);
					}



					for (AddressForServer object : deliveryServiceListArray)
					{
						Boolean isContain = object.getMilestoneTitle().toLowerCase().contains(filter_by.toLowerCase());
						if (isContain)
						{
							isContain = false;
							deliveryServiceList.add(object);
						}

					}


					if (deliveryServiceList.size() > 0) {
						objectArrayList.add(("Delivery Services"));
						objectArrayList.addAll(deliveryServiceList);
					}

					listview.setAdapter(new EntryAdapter(CourtService.this, objectArrayList));
					getAnim(milestone_layout, 0.0f);

				}



			}
		});

		milestone_layout = (LinearLayout) findViewById(R.id.milestone_layout);

		if (milestone_layout.getVisibility() == View.VISIBLE )
		{
			getAnim(milestone_layout, 0.0f);
		}
		else
		{
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


	@SuppressLint("SetTextI18n")
	private void intializeControlls() {
		txt_logout = (TextView) findViewById(R.id.txt_logout);
		txt_sync = (TextView) findViewById(R.id.txt_sync);

		tabCategory = (LinearLayout) findViewById(R.id.tab_category);
		tabCourt = (LinearLayout) findViewById(R.id.tab_court);
		tabMap = (LinearLayout) findViewById(R.id.tab_map);

		category = (ImageButton) findViewById(R.id.imageBtn_category);
		lbl_category = (TextView) findViewById(R.id.lbl_category);
		mapview = (ImageButton) findViewById(R.id.imageBtn_mapView);
		mapText = (TextView) findViewById(R.id.lbl_mapview);
		courtservice = (TextView) findViewById(R.id.lbl_courtservice);
		imageBtn_self = (ImageButton) findViewById(R.id.imageBtn_self);
		courtservice.setText("Delivery/Court Services");

		tabCategory.setOnClickListener(this);
		tabCourt.setOnClickListener(this);
		tabMap.setOnClickListener(this);

		category.setOnClickListener(this);
		lbl_category.setOnClickListener(this);
		mapview.setOnClickListener(this);

		mapText.setOnClickListener(this);
		courtservice.setOnClickListener(this);
		imageBtn_self.setOnClickListener(this);

		courtservice.setTextColor(getResources().getColor(R.color.blue_text));
		findViewById(R.id.imageBtn_self).setSelected(true);

		btn_hybrid = (Button) findViewById(R.id.btn_hybrid);
		btn_satellite = (Button) findViewById(R.id.btn_satellite);
		btn_standard = (Button) findViewById(R.id.btn_standard);
		btn_hybrid.setOnClickListener(this);
		btn_satellite.setOnClickListener(this);
		btn_standard.setOnClickListener(this);


		btn_hybrid.setBackgroundColor(Color.WHITE);
		btn_satellite.setBackgroundColor(Color.DKGRAY);
		btn_standard.setBackgroundColor(Color.DKGRAY);

		txt_logout.setOnClickListener(this);
		txt_sync.setOnClickListener(this);

		findViewById(R.id.imageBtn_self).setSelected(true);
		flipper = (ViewFlipper) findViewById(R.id.viewfliper);

		flipper.setDisplayedChild(0);

		listview = (ListView) findViewById(R.id.listView1);


		milestone_txt_filter = (TextView) findViewById(R.id.milestone_txt_filter);
		milestone_img_filter = (ImageView) findViewById(R.id.milestone_img_filter);
		filter_listView = (ListView) findViewById(R.id.filter_listView);
		milestone_txt_filter.setOnClickListener(this);
		milestone_img_filter.setOnClickListener(this);

		txtReceivedDate = (TextView) findViewById(R.id.txtReceivedDate);
		txtReceivedDate.setOnClickListener(this);
	}

	private void intializeData() {

		courtServiceListArray = database
				.getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
		pickupServiceListArray = database.getPickupOrderValuesFromTable();
		deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();

		if (courtServiceListArray.size() > 0) {
			serviceAddressAll.add(("Court Services"));
			serviceAddressAll.addAll(courtServiceListArray);
			serviceAddressAllForMap.addAll(courtServiceListArray);
		}

		if (pickupServiceListArray.size() > 0) {
			serviceAddressAll.add(("Pickup Services"));
			serviceAddressAll.addAll(pickupServiceListArray);
			serviceAddressAllForMap.addAll(pickupServiceListArray);
		}
		if (deliveryServiceListArray.size() > 0) {
			serviceAddressAll.add(("Delivery Services"));
			serviceAddressAll.addAll(deliveryServiceListArray);
			serviceAddressAllForMap.addAll(deliveryServiceListArray);
		}

		final EntryAdapter adapter = new EntryAdapter(this, serviceAddressAll);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {

				Object item = serviceAddressAll.get(position);
				navigateOnSelectItem(item);

				Object items = serviceAddressAll.get(position);
				navigateOnSelect(items);
			}
		});

	}

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
			Log.d("Exception download url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	public void googleMap() {
		try {
			if (!initilizeMap()) {
				return;
			}
			if (googleMap == null)
				return;
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.setMyLocationEnabled(true);

			googleMap.getUiSettings().setZoomControlsEnabled(false);

			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			googleMap.getUiSettings().setCompassEnabled(true);

			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			googleMap.getUiSettings().setZoomGesturesEnabled(true);

			boolean firstItem = true;

			for (int i = 0; i < serviceAddressAllForMap.size(); i++) {
				Object item = serviceAddressAllForMap.get(i);
				double[] randomLocation = null;
				String workOrder = "", addressForGoogle = "";
				if (!(item instanceof String)) {
					if (item instanceof CourtAddressForServer) {

						CourtAddressForServer courtAddress = (CourtAddressForServer) item;
						randomLocation = createRandLocation(
								Double.parseDouble(courtAddress.getLatitude()),
								Double.parseDouble(courtAddress.getLongitude()));
						Log.d("Latitude court", "" + courtAddress.getLatitude());
						workOrder = courtAddress.getWorkorder();
						addressForGoogle = courtAddress
								.getAddressFormattedForDisplay();
//						if(courtAddress.getLatitude().equals("0.0")){
////							Geocoder geocoder = new Geocoder(context, Locale.getDefault());
////							String result = null;
////							try {
////								List<android.location.Address> addressList = geocoder.getFromLocationName(courtAddress.getAddressFormattedForDisplay(), 1);
////								if (addressList != null && addressList.size() > 0) {
////									android.location.Address address AddressForDisplay= addressList.get(0);
////									StringBuilder sb = new StringBuilder();
////									sb.append(address.getLatitude()).append("\n");
////									sb.append(address.getLongitude()).append("\n");
////									Log.d("Geo Code court new",""+address.getLatitude()+","+address.getLongitude());
////									randomLocation = createRandLocation(
////											address.getLatitude(),
////											address.getLongitude());
////
////									result = sb.toString();
////								}
////							} catch (IOException e) {
////								Log.e("Error", "Unable to connect to Geocoder", e);
////							} catch (Exception e) {
////								e.printStackTrace();
////							}
////							MarkerOptions marker = new MarkerOptions().position(
////									new LatLng(randomLocation[0], randomLocation[1]))
////									.title(workOrder + "\n" + addressForGoogle);
////							marker.icon(BitmapDescriptorFactory
////									.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
////							googleMap.addMarker(marker);
//						}
//						else{
						MarkerOptions marker = new MarkerOptions().position(
								new LatLng(randomLocation[0], randomLocation[1]))
								.title(workOrder + "\n" + addressForGoogle);
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
						googleMap.addMarker(marker);
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(randomLocation[0],
										randomLocation[1])).zoom(5).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
						//	}


					} else if (item instanceof AddressForServer) {
						AddressForServer address = (AddressForServer) item;

						Log.d("Latitude", "" + address.getLatitude());

						Lati = String.valueOf(address.getLatitude());
						longi = String.valueOf(address.getLongitude());

						String AddressForDisplay = address.getAddressFormattedForDisplay();
//						Log.d("Address Length",""+AddressForDisplay.length());
						for (int ii = 0; ii < AddressForDisplay.length(); ii++) {
							Character character = AddressForDisplay.charAt(ii);
							if (Character.isDigit(character)) {

								AddressForDisplay = AddressForDisplay.substring(ii);
								//result += character;
							}
						}
//						Log.d("Address for json",""+AddressForDisplay);
//
//
//
//
////						if(AddressForDisplay==null || AddressForDisplay.equals("")){
////							Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
////							return;
////						}
////
////						String url = "https://maps.googleapis.com/maps/api/geocode/json?";
////
////						try {
////							// encoding special characters like space in the user input place
////							AddressForDisplay = URLEncoder.encode(AddressForDisplay, "utf-8");
////						} catch (UnsupportedEncodingException e) {
////							e.printStackTrace();
////						}
////
////						String add = "address=" + AddressForDisplay;
////
////						String sensor = "sensor=false";
////
////						// url , from where the geocoding data is fetched
////						url = url + add + "&" + sensor;
////
////						Log.d("URL",""+url);
//						// Instantiating DownloadTask to get places from Google Geocoding service
//						// in a non-ui thread
////						DownloadTask downloadTask = new DownloadTask();
////
////						// Start downloading the geocoding places
////						downloadTask.execute(url);
//
//
//
//

//						if (address.getLatitude().contains("0.0")){
//							Geocoder coder = new Geocoder(this);
//							try {
//								List<Address> addressList = coder.getFromLocationName(AddressForDisplay, 1);
//								Log.d("Address size",""+addressList.size());
//								if (addressList != null && addressList.size() > 0) {
//									Lati = String.valueOf(addressList.get(0).getLatitude());
//									longi = String.valueOf(addressList.get(0).getLongitude());
//
//									Log.d("Lati 11",""+Lati);
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							} // end catch
//						}


//						Log.d("Latitude from address",""+Lati+AddressForDisplay);
//						Log.d("Longitude from address",""+longi+AddressForDisplay);


//						if(AddressForDisplay.length()!=0) {
//							if (address.getLongitude().contains("0.0")) {
//								Geocoder coder = new Geocoder(this);
//								List<Address> add;
//
//								try {
//									add = coder.getFromLocationName(AddressForDisplay, 5);
//									Address location = add.get(0);
//									Lati = String.valueOf(location.getLatitude());
//									longi = String.valueOf(location.getLongitude());
//
//									address.setLatitude(Lati);
//									address.setLongitude(longi);
//
//
//									Log.d("Latitude from addressss",""+Lati);
//									Log.d("Longitude from addressss",""+longi);
//
//
//								} catch (Exception e) {
//
//								}
//
//							}
//						}


						if (address.TYPE == AddressForServer.PICKUP_SERVICE) {
							randomLocation = createRandLocation(
									Double.parseDouble(Lati),
									Double.parseDouble(longi));

							workOrder = address.getWorkorder();
							addressForGoogle = address
									.getAddressFormattedForDisplay();
							MarkerOptions marker = new MarkerOptions().position(
									new LatLng(randomLocation[0], randomLocation[1]))
									.title(workOrder + "\n" + addressForGoogle);
							marker.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
							googleMap.addMarker(marker);
						} else if (address.TYPE == AddressForServer.DELIVERY_SERVICE) {
							randomLocation = createRandLocation(
									Double.parseDouble(Lati),
									Double.parseDouble(longi));

							workOrder = address.getWorkorder();
							addressForGoogle = address
									.getAddressFormattedForDisplay();
							MarkerOptions marker = new MarkerOptions().position(
									new LatLng(randomLocation[0], randomLocation[1]))
									.title(workOrder + "\n" + addressForGoogle);
							marker.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED));
							googleMap.addMarker(marker);
						}

					}


					if (firstItem) {
						// Move the camera to last position with a zoom level

						firstItem = false;
					}
				}
			}

			googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker marker) {

							int i = Integer.parseInt(marker.getId()
									.substring(1));
							Object item = serviceAddressAllForMap.get(i);

							navigateOnSelectItem(item);
							navigateOnSelect(item);
						}
					});

			googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {

					int i = Integer.parseInt(marker.getId().substring(1));
					Object item = serviceAddressAllForMap.get(i);

					@SuppressLint("InflateParams") View v = getLayoutInflater().inflate(
							R.layout.list_courtservice_items, null);

					v.setBackgroundResource(R.drawable.mapview);
					LayoutParams lp = new LayoutParams((int) (300 * dpi),
							LayoutParams.WRAP_CONTENT);
					v.setLayoutParams(lp);

					TextView title = (TextView) v.findViewById(R.id.txt_udno);
					TextView subtitle = (TextView) v
							.findViewById(R.id.txt_addresslist);

					if (item instanceof CourtAddressForServer) {
						CourtAddressForServer courtAddress = (CourtAddressForServer) item;
						title.setText(courtAddress.getWorkorder());
						subtitle.setText(courtAddress
								.getAddressFormattedForDisplay());
					} else if (item instanceof AddressForServer) {
						AddressForServer address = (AddressForServer) item;
						title.setText(address.getWorkorder());
						subtitle.setText(address
								.getAddressFormattedForDisplay());
					}

					return v;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void navigateOnSelectItem(Object item) {
		if (!(item instanceof String)) {
			selectedAddressServer = item;
			if (item instanceof CourtAddressForServer) {

				if (((CourtAddressForServer) item).getMilestoneTitle().contains("Cancelled")) {
					return;
				} else {
					finish();
					Intent courtdetail = new Intent(CourtService.this,
							CourtServiceDetail.class);
					startActivity(courtdetail);
					Log.d("Called","CourtServiceDetail - item");
				}
			} else if (item instanceof AddressForServer) {

				if (((AddressForServer) item).getMilestoneTitle().contains("Cancelled")) {

					return;
				} else {
					finish();
					Intent deliveryPickup = new Intent(CourtService.this,
							DeliveryServiceDetail.class);
					startActivity(deliveryPickup);
					Log.d("Called","DeliveryServiceDetail - item");
				}
			}
		}
	}

	private void navigateOnSelect(Object items) {
		if (!(items instanceof String)) {
			selectedAddres = items;
			if (items instanceof CourtAddressForServer) {

				if (((CourtAddressForServer) items).getMilestoneTitle().contains("Cancelled")) {
					return;
				} else {
					finish();
					Intent courtdetail = new Intent(CourtService.this,
							CourtServiceDetail.class);
					startActivity(courtdetail);
					Log.d("Called","CourtServiceDetail - items");
				}
			} else if (items instanceof AddressForServer) {

				if (((AddressForServer) items).getMilestoneTitle().contains("Cancelled")) {
					return;
				} else {
					finish();
					Intent deliveryPickup = new Intent(CourtService.this,
							DeliveryServiceDetail.class);
					startActivity(deliveryPickup);
					Log.d("Called","DeliveryServiceDetail - items");
				}
			}
		}
	}

	private double[] createRandLocation(double latitude, double longitude) {

		return new double[]{latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10)};
	}

	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(CourtService.this, ListCategory.class);
		startActivity(intent);
	}

	private boolean initilizeMap() {

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mapview)).getMap();
			if (googleMap != null) {
				return true;
			}
		}
		return false;
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object

		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {

			// Instantiating ParserTask which parses the json data from Geocoding webservice
			// in a non-ui thread
			ParserTask parserTask = new ParserTask();

			// Start parsing the places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}

	class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsonData) {

			List<HashMap<String, String>> places = null;
			GeocodeJSONParser parser = new GeocodeJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a an ArrayList */
				places = parser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			// Clears all the existing markers


			for (int i = 0; i < list.size(); i++) {

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();

				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);

				// Getting latitude of the place
				Lati = String.valueOf(Double.parseDouble(hmPlace.get("lat")));

				// Getting longitude of the place
				longi = String.valueOf(Double.parseDouble(hmPlace.get("lng")));

				Log.d("From Json latitide", "" + Lati);

				// Getting name

			}
		}
	}

}
