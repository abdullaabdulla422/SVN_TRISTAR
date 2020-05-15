package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.SplatterCourtPOD;
import com.tristar.object.SubmitCourtPOD;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.DecimalDigitsInputFilter;
import com.tristar.utils.SessionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class
CourtPOD extends Activity implements OnClickListener {
	public Button btn_attachment, btn_submit;
	TextView back, txt_worker,btn_getgps;
	CheckBox chk_time, spalttercheck;
	ImageView image, attach_icon;
	EditText comment, edt_fee, edt_weight, edt_piece, edt_wait, edt_check;
	public static Button btn_date;
	public static Button btn_time;
	static final int DATE_DIALOG_ID = 999;
	private static Button selectedButton;
	private int year;
	private int month;
	private int day;
	CourtAddressForServer addresServer;
	SubmitCourtPOD courtobject;
	public GPSTracker gps;
	public double latitude, longitude;
	public boolean gpsLocated = false;
	DataBaseHelper PODdatabase;
	final Context context = this;
	String addresforDis, workorder,servee_name;
	public int countPODid;
	int addressLineItemForcount;

	public ArrayList<String> work_order;
	public ArrayList<String> ServeeName;
	public ArrayList<String> work_order_enable;
	public ArrayList<String> work_order_servee;
	public ArrayList<Integer> address_line;
	public ArrayList<Integer> address_line_enable;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.court_pod);


		PODdatabase = DataBaseHelper.getInstance(CourtPOD.this);
		PODdatabase = DataBaseHelper.getInstance();
		SessionData.getInstance().clearAttachments();
	

		initializeViews();
		initializeData();


		comment.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.edt_comment) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});

	}

	public void initializeViews() {
		btn_getgps = (TextView) findViewById(R.id.btn_getgps);
		btn_attachment = (Button) findViewById(R.id.btn_attachment);
		btn_date = (Button) findViewById(R.id.btn_date);
		btn_time = (Button) findViewById(R.id.btn_time);
		chk_time = (CheckBox) findViewById(R.id.chk_time);
		spalttercheck = (CheckBox) findViewById(R.id.check_courtsplatter);

		chk_time.setChecked(true);

		back = (TextView) findViewById(R.id.txt_back);
		image = (ImageView) findViewById(R.id.imageButtonback);
		comment = (EditText) findViewById(R.id.edt_court_comment);
		txt_worker = (TextView) findViewById(R.id.lbl_category);

		btn_attachment.setOnClickListener(this);
		btn_date.setOnClickListener(this);
		btn_time.setOnClickListener(this);

		edt_fee = (EditText) findViewById(R.id.edt_court_fee);
		edt_piece = (EditText) findViewById(R.id.edt_court_piece);
		edt_wait = (EditText) findViewById(R.id.edt_court_wait);
		edt_weight = (EditText) findViewById(R.id.edt_court_weigh);

		edt_check = (EditText) findViewById(R.id.edt_check_no);

		btn_submit = (Button) findViewById(R.id.btn_court_Submit);
		attach_icon = (ImageView) findViewById(R.id.attach_icon_general);

		edt_fee.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(10
		) });
		edt_weight.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(
				10) });

		back.setOnClickListener(this);
		image.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_date.setOnClickListener(this);
		btn_time.setOnClickListener(this);
		chk_time.setOnClickListener(this);
		spalttercheck.setOnClickListener(this);

	}

	public void initializeData() {

		//addresServer = (CourtAddressForServer) CourtService.selectedAddressServer;

		if(SessionData.getInstance().getSelectedItem()==1){
			addresServer = (CourtAddressForServer) ListCategory.selectedAddressServer;

		}else{
			addresServer = (CourtAddressForServer) CourtService.selectedAddressServer;

		}

		if (addresServer != null && addresServer.getWorkorder().length() != 0) {
			txt_worker.setText(addresServer.getWorkorder());
			workorder = addresServer.getWorkorder();
		} else {
			txt_worker.setText("N/A");
		}
		addresforDis = addresServer.getAddressFormattedForDisplay();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DATE); 

		Calendar datetime = Calendar.getInstance(); 
		datetime.set(Calendar.DATE, day);
		datetime.set(Calendar.MONTH, month);
		datetime.set(Calendar.YEAR, year);

		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
		String dateFormatted = dateformat.format(datetime.getTime());
		btn_date.setText(dateFormatted);

		dateformat = new SimpleDateFormat("hh:mm a");
		dateFormatted = dateformat.format(datetime.getTime());

		btn_time.setText(dateFormatted);

		gpsLocated = true;
		getGps();

	}

	@SuppressLint("SetTextI18n")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v == btn_date) {
			selectedButton = (Button) v;
			showDialog(DATE_DIALOG_ID);
		} else if (v == btn_time) {
			if (chk_time.isChecked()) {
				selectedButton = (Button) v;
				final Dialog dialog = new Dialog(CourtPOD.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.timepicker_dialog);
				final TimePicker tp = (TimePicker)dialog. findViewById(R.id.tp);
				Button set = (Button)dialog.findViewById(R.id.btn_set);
				Button cancel = (Button)dialog.findViewById(R.id.btn_calcel);
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
						if(hourOfDay>11)
						{
							hourOfDay = hourOfDay-12;
							AMPM = "pm";
						}
						String hr = "";
						if(Integer.toString(hourOfDay).length()==1){
							hr = "0"+Integer.toString(hourOfDay);
						}else{
							hr = Integer.toString(hourOfDay);
						}

						String min = "";
						if(Integer.toString(minute).length()==1){
							min = "0"+Integer.toString(minute);
						}else{
							min = Integer.toString(minute);
						}

						time[0] = "" + hr + ":" + min + " " + AMPM;

					}
				});

				set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						btn_time.setText(time[0]);
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
		} else if (v == btn_attachment) {
			BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.COURT_POD;
			Intent attach = new Intent(CourtPOD.this, BaseFileIncluder.class);
			startActivity(attach);
		}  else if (v == back || v == image) {
			finish();
			Intent courtDetail = new Intent(CourtPOD.this,
					CourtServiceDetail.class);
			startActivity(courtDetail);
		} else if (v == btn_submit) {

			if (validation())

				if (spalttercheck.isChecked()) {
					CourtPODSplatter();
				} else if (!(spalttercheck.isChecked())) {
					saveAllValuesInDB(workorder);
				     final Dialog dialog = new Dialog(context);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.alertbox);

						TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
						text.setText("Court POD is saved successfully");

						Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								finish();
		      					Intent court = new Intent(CourtPOD.this, ListCategory.class);
		      					startActivity(court);

							}
						});

						dialog.show();
				}

		} else if (v == chk_time) {
			if (chk_time.isChecked()) {
				btn_time.setEnabled(true);
			} else {
				btn_time.setEnabled(false);
			}
		}
	}

	public boolean validation() {
		String alert = "";

		if (comment.getText().toString().length() == 0) {
			alert = "Enter Comments";
		} else {

//			if(edt_fee.getText().toString().length() == 0){
//				return true;
//			}else{
//				if(edt_check.getText().toString().length()==0){
//					alert = "Please enter the check";
//				}else{
//					return true;
//				}
//			}

			return true;


		}

		new CustomAlertDialog(CourtPOD.this, alert).show();
		return false;

	}

	@SuppressLint("SetTextI18n")
	public void CourtPODSplatter() {
		try {
			final ArrayList<SplatterCourtPOD> countResultPOD = PODdatabase
					.getCourtPODComparison(addresforDis,servee_name);


			if(countResultPOD.size()==1){


				saveAllValuesInDB(workorder);

				Intent detailView = new Intent(CourtPOD.this,
						ListCategory.class);
				startActivity(detailView);

				Toast.makeText(CourtPOD.this, "Court POD is saved successfully!", Toast.LENGTH_SHORT).show();

			}else{

				final Dialog mdialog = new Dialog(context);
				mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mdialog.setContentView(R.layout.splatterdialog);

				TextView text = (TextView) mdialog
						.findViewById(R.id.splattermsg);
				text.setText("You have "
						+ (countResultPOD.size() - 1)
						+ " other jobs at the same address, would you "
						+ "like to spread this diligence (CourtPOD) across all jobs?");

				Button dialogButtonYes = (Button) mdialog
						.findViewById(R.id.btn_yes);
				dialogButtonYes.setOnClickListener(new OnClickListener() {
					@SuppressLint("SetTextI18n")
					@Override
					public void onClick(View v) {


						if (countResultPOD.size() > 1) {


							work_order = new ArrayList<>();
							work_order_enable = new ArrayList<>();
							work_order_servee = new ArrayList<>();


							ServeeName = new ArrayList<>();


							for (int i = 0; i < countResultPOD.size(); i++) {

								if(workorder.toString().equalsIgnoreCase(countResultPOD.get(i).getWorkorder())){

								}else{
									work_order.add(countResultPOD.get(i).getWorkorder());
									ServeeName.add(countResultPOD.get(i).getServee_name());
								}

								//ServeeName.add(countResultPOD.get(i).getServee_name());
							}

							final Dialog dialog = new Dialog(CourtPOD.this);
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

								work_order_servee.add((work_order.get(i).toString() + " " + "/" + " " + ServeeName.get(i).toString()));

							}

							final ListView list = (ListView) dialog.findViewById(R.id.list);
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(CourtPOD.this, R.layout.choise_list, work_order_servee);
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

									}

								}
							});
							Button Save = (Button) dialog.findViewById(R.id.save);
							Save.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {


									work_order_enable.add(workorder);


									for (int j = 0; j < work_order_enable.size(); j++) {

										saveAllValuesInDB(work_order_enable.get(j).toString());
										Log.d("item check", "" + work_order_enable);

									}

									dialog.dismiss();
									mdialog.dismiss();


									Intent detailView = new Intent(CourtPOD.this,
											ListCategory.class);
									startActivity(detailView);
									Toast.makeText(CourtPOD.this, "Court POD is saved successfully!", Toast.LENGTH_SHORT).show();
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

						/*for (int i = 0; i < countResultPOD.size(); i++) {
							saveAllValuesInDB(countResultPOD.get(i)
									.getWorkorder()
									);
							Log.d("item check", "" + countResultPOD);
							mdialog.dismiss();

						}*/
						  /*final Dialog dialog = new Dialog(context);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.alertbox);


							TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
							text.setText("Court POD is saved successfully");

							Button dialogButton = (Button) dialog
									.findViewById(R.id.btn_ok);

							dialogButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									finish();
			      					Intent court = new Intent(CourtPOD.this,
			      							ListCategory.class);
			      					startActivity(court);

								}
							});

							dialog.show();*/

						}
					}

				});

				Button dialogButtonNo = (Button) mdialog
						.findViewById(R.id.btn_no);
				dialogButtonNo.setOnClickListener(new OnClickListener() {
					@SuppressLint("SetTextI18n")
					@Override
					public void onClick(View v) {


						saveAllValuesInDB(workorder);
						mdialog.dismiss();

						Intent detailView = new Intent(CourtPOD.this,
								ListCategory.class);
						startActivity(detailView);

						Toast.makeText(CourtPOD.this, "Court POD is saved successfully!", Toast.LENGTH_SHORT).show();
						/*final Dialog dialog = new Dialog(context);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.alertbox);


						TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
						text.setText("Court POD is saved successfully");

						Button dialogButton = (Button) dialog
								.findViewById(R.id.btn_ok);

						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								finish();
								Intent court = new Intent(CourtPOD.this,
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



		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void saveAllValuesInDB(String wrk_order ) {
		if (!gpsLocated) {
			getGps();
		}
		SubmitCourtPOD submitCourtPOD = new SubmitCourtPOD();
		submitCourtPOD.setWorkorder(wrk_order);
		submitCourtPOD.setProofDate(btn_date.getText().toString()
				.replaceAll("/", "-"));

		if (chk_time.isChecked()) {
			submitCourtPOD.setProofTime(btn_time.getText().toString()
					.replace(" ", ":00 "));
		} else {
			submitCourtPOD.setProofTime("00:00:00 am");
		}

		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss a");
			Date date = dateformat.parse(submitCourtPOD.getProofTime());
			dateformat = new SimpleDateFormat("HH:mm:ss");
			submitCourtPOD.setProofTime(dateformat.format(date));
		} catch (Exception e) {
			submitCourtPOD.setProofTime("00:00:00");
		}

		if (comment.getText().toString().length() != 0) {
			submitCourtPOD.setProofComments(comment.getText().toString());
		} else {
			submitCourtPOD.setProofComments("");
		}
		if (edt_fee.getText().toString().length() != 0) {
			submitCourtPOD.setFeeAdvance(getInt(edt_fee.getText().toString()));
		} else {
			submitCourtPOD.setFeeAdvance(0);
		}
		if (edt_weight.getText().toString().length() != 0) {
			submitCourtPOD.setWeight(getInt(edt_weight.getText().toString()));
		} else {
			submitCourtPOD.setWeight(0);
		}
		if (edt_wait.getText().toString().length() != 0) {
			submitCourtPOD.setWaitTime(getInt(edt_wait.getText().toString()));
		} else {
			submitCourtPOD.setWaitTime(0);
		}
		if (edt_piece.getText().toString().length() != 0) {
			submitCourtPOD.setPieces(getInt(edt_piece.getText().toString()));
		} else {
			submitCourtPOD.setPieces(0);
		}
		submitCourtPOD.setLatitude(String.valueOf(latitude));
		submitCourtPOD.setLongitude(String.valueOf(longitude));
		PODdatabase = DataBaseHelper.getInstance();
		try {
			boolean isSubmitSucess = PODdatabase.insertIntoSubmitCourtPODTable(submitCourtPOD);
			if (isSubmitSucess) {
				String imageSaved = "";
				SubmitCourtPOD submitImage = new SubmitCourtPOD();
				submitImage.setSubmitCourtPODID(PODdatabase
						.getLastinsertedLineItemFromSubmitCourtPOD());
				ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance().getAttachedFilesData();
				for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
					submitImage.setWorkorder(wrk_order);
					submitImage
							.setAttachmentFilename(wrk_order + "-image-" + i);
					submitImage.setAttahmentBase64String(Base64.encodeToString(
							imageArrayToSaveInDB.get(i), Base64.DEFAULT));
					submitImage.setAttachmentUrlString();

					boolean success = true;
					success = PODdatabase
							.insertOrUpdateAttachmentsOfCourtPOD(submitImage);
					if (!success) {
						imageSaved = "Image is not saved!";
					}
				}
				if (imageSaved.length() == 0) {

				} else {
					new CustomAlertDialog(CourtPOD.this, imageSaved).show();
				}
			} else {
				new CustomAlertDialog(CourtPOD.this, "Court POD is not saved!")
						.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
			attach_icon.setVisibility(View.VISIBLE);
		} else {
			attach_icon.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:

			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

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

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		public Dialog onCreateDialog(Bundle savedInstanceState) {

			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(),
					AlertDialog.THEME_HOLO_LIGHT,this,hour,minute,DateFormat.is24HourFormat(getActivity()));

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
		Intent intent = new Intent(CourtPOD.this, CourtServiceDetail.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	public void getGps() {
		gps = new GPSTracker(CourtPOD.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
				String la = String.valueOf(latitude) +","+String.valueOf(longitude);
			LatLng latLng = new LatLng(latitude,longitude);
			btn_getgps.setText(la);
		} else {
			gps.showSettingsAlert();
		}
	}

	private int getInt(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

}