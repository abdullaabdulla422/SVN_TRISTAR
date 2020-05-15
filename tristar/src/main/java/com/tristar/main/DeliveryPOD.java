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
import android.os.Bundle;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.AddressForServer;
import com.tristar.object.PODAttachments;
import com.tristar.object.SubmitDeliveryPOD;
import com.tristar.object.SubmitPickupPOD;
import com.tristar.signature.Capture;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.DecimalDigitsInputFilter;
import com.tristar.utils.SessionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class DeliveryPOD extends Activity implements OnClickListener {

	private static final int DATE_DIALOG_ID = 999;
	public static int PICKUP_ONLY = 101, DELIVERY_ONLY = 102, PICKUP_AND_DELIVERY = 103;
	private static Button selectedButton;
	final Context context = this;
	boolean isAttach = false;
	byte[] signatureData = null;
	private int year, month, day;
	private int TYPE_OF_JOB = -1;
	private AddressForServer addressServer;
	private ImageView img_back;
	private TextView txtJobTitleId, txt_back,btnGetGPS;
	private Button btnPickDate, btnPickTime, btnPickAttach;
	private EditText txtPickComment;
	private CheckBox chkPicktime, chkDeliveryTime;
	private Button btnDeliveryDate, btnDeliveryTime;
	private EditText txtDeliveryComment, txtDeliFee, txtDeliWeight, txtDeliPieces, txtDeliWaitTime, txtDeliRecievedBy;
	private Button  btnSubmit, btnSign;
	private GPSTracker gps;
	private EditText edt_check;
	private double latitude, longitude;
	private DataBaseHelper database;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_pod);

		SessionData.getInstance().clearAttachments();
		SessionData.getInstance().setImageData(null);
		if (SessionData.getInstance().getSelectedItem() == 1) {
			addressServer = (AddressForServer) ListCategory.selectedAddressServer;

		} else {
			addressServer = (AddressForServer) CourtService.selectedAddressServer;

		}
		database = DataBaseHelper.getInstance(DeliveryPOD.this);


		initializeControlls();
		setBasicData();
		setListeners();
		if (isPickupOnly()) {
			hideDelivery();
		}
		if (isDeliveryOnly()) {
			hidePickup();
		}

	}

	@Override
	protected void onResume() {

		View attach_icon = null;
		View signature_icon = null;
		if (isDeliveryOnly()) {
			attach_icon = findViewById(R.id.attach_icon);
		} else {
			attach_icon = findViewById(R.id.attach_icon_general);
		}
		if (SessionData.getInstance().getAttachedFilesData().size() > 0) {
			attach_icon.setVisibility(View.VISIBLE);

		} else {
			attach_icon.setVisibility(View.GONE);
		}

		if (!isAttach) {
			signature_icon = findViewById(R.id.attach_icon_general);
			if (SessionData.getInstance().getImageData() != null) {
				signatureData = SessionData.getInstance().getImageData().clone();
				signature_icon.setVisibility(View.VISIBLE);
			} else {
				signature_icon.setVisibility(View.GONE);
			}
		} else {

		}

		super.onResume();
	}

	private void setBasicData() {

		if (addressServer.TYPE == AddressForServer.PICKUP_SERVICE) {
			TYPE_OF_JOB = PICKUP_ONLY;
		}

//		else if(!database.isPickPodAvailableToAdd(addressServer.getWorkorder())) {
//			TYPE_OF_JOB = DELIVERY_ONLY;
//		}

		else {
			TYPE_OF_JOB = DELIVERY_ONLY;
//			TYPE_OF_JOB = PICKUP_AND_DELIVERY;
		}

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
		btnPickDate.setText(dateFormatted);
		btnDeliveryDate.setText(dateFormatted);

		dateformat = new SimpleDateFormat("hh:mm a");
		dateFormatted = dateformat.format(datetime.getTime());

		btnPickTime.setText(dateFormatted);
		btnDeliveryTime.setText(dateFormatted);


		chkPicktime.setChecked(true);
		chkDeliveryTime.setChecked(true);

		getGps();
	}

	private void initializeControlls() {

		txt_back = (TextView) findViewById(R.id.txt_back);
		img_back = (ImageView) findViewById(R.id.img_back);
		txtJobTitleId = (TextView) findViewById(R.id.txt_court_job_title_id);

		btnPickDate = (Button) findViewById(R.id.btn_date);
		btnPickTime = (Button) findViewById(R.id.btn_time);
		chkPicktime = (CheckBox) findViewById(R.id.check_servedtime);
		txtPickComment = (EditText) findViewById(R.id.edt_comment);
		btnPickAttach = (Button) findViewById(R.id.btn_pick_attach);
		edt_check = (EditText) findViewById(R.id.edt_check_no);

		btnDeliveryDate = (Button) findViewById(R.id.btn_delivery_date);
		btnDeliveryTime = (Button) findViewById(R.id.btn_delivery_time);
		chkDeliveryTime = (CheckBox) findViewById(R.id.chk_delivery_time);
		txtDeliveryComment = (EditText) findViewById(R.id.txt_delivery_comment);
		txtDeliFee = (EditText) findViewById(R.id.txt_deli_fee);
		txtDeliWeight = (EditText) findViewById(R.id.txt_deli_weight);
		txtDeliPieces = (EditText) findViewById(R.id.txt_deli_pieces);
		txtDeliWaitTime = (EditText) findViewById(R.id.txt_deli_wait_time);
		txtDeliRecievedBy = (EditText) findViewById(R.id.txt_deli_recieved_by);
		txtDeliRecievedBy.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(txtDeliRecievedBy, InputMethodManager.SHOW_IMPLICIT);

		btnGetGPS = (TextView) findViewById(R.id.btn_getgps);
		btnSign = (Button) findViewById(R.id.buttonsign);
		btnSubmit = (Button) findViewById(R.id.btn_Submit);

		txtDeliFee.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10)});
		txtDeliWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10)});

		if (addressServer != null) {
			txtJobTitleId.setText(addressServer.getWorkorder());
		}

	}

	private void setListeners() {

		txt_back.setOnClickListener(this);
		img_back.setOnClickListener(this);

		btnPickTime.setOnClickListener(this);
		btnPickDate.setOnClickListener(this);
		btnPickAttach.setOnClickListener(this);

		btnDeliveryTime.setOnClickListener(this);
		btnDeliveryDate.setOnClickListener(this);

		btnSign.setOnClickListener(this);
		// btnGetGPS.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);

		chkDeliveryTime.setOnClickListener(this);
		chkPicktime.setOnClickListener(this);
	}

	private void hidePickup() {
		findViewById(R.id.pick_title).setVisibility(View.GONE);
		findViewById(R.id.delivery_title).setVisibility(View.GONE);
		findViewById(R.id.layout_pick_items).setVisibility(View.GONE);
		findViewById(R.id.layout_attach_top).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.txt_form_title)).setText(getResources().getString(R.string.deli_pod));

	}

	private void hideDelivery() {
		findViewById(R.id.pick_title).setVisibility(View.GONE);
		findViewById(R.id.delivery_title).setVisibility(View.GONE);
		findViewById(R.id.layout_delivery_items).setVisibility(View.GONE);
		findViewById(R.id.layout_attach_top).setVisibility(View.GONE);
		btnSign.setText(getResources().getString(R.string.attachments));
		((TextView) findViewById(R.id.txt_form_title)).setText(getResources().getString(R.string.pick_pod));
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		isAttach = false;
		if (v == btnPickAttach) {
			isAttach = true;

			if (TYPE_OF_JOB == PICKUP_ONLY) {
				BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.PICKUP_POD;
				SessionData.getInstance().setAttach_Navigation("PICKUP_POD");
			} else {
				BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.DELIVERY_POD;
				SessionData.getInstance().setAttach_Navigation("DELIVERY_POD");
			}
			Intent attach = new Intent(DeliveryPOD.this, BaseFileIncluder.class);
			startActivity(attach);
		} else if (v == btnSign) {
			if (TYPE_OF_JOB == PICKUP_ONLY) {
				onClick(btnPickAttach);
				return;
			} else {
				isAttach = false;
				SessionData.getInstance().setImageData(signatureData);
				Intent signIntent = new Intent(DeliveryPOD.this, Capture.class);
				startActivity(signIntent);
			}
		} else if (v == img_back || v == txt_back) {
			finish();
			Intent submit = new Intent(DeliveryPOD.this, DeliveryServiceDetail.class);
			startActivity(submit);
		} else if (v == btnSubmit) {
			getGps();

			if (validation())
			{
				if (isDeliveryOnly()) {
					saveDelivery();
				} else {
					savePickup();
				}
			}

		} else if (v == btnDeliveryTime) {
			if (chkDeliveryTime.isChecked()) {
				selectedButton = (Button) v;
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getFragmentManager(), "timePicker");
			}
		} else if (v == btnPickTime) {
			if (chkPicktime.isChecked()) {
				selectedButton = (Button) v;
				final Dialog dialog = new Dialog(DeliveryPOD.this);
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

						btnPickTime.setText(time[0]);
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
		} else if (v == btnDeliveryDate || v == btnPickDate) {
			selectedButton = (Button) v;
			showDialog(DATE_DIALOG_ID);
		} else if (v == chkDeliveryTime) {
			if (chkDeliveryTime.isChecked()) {
				btnDeliveryTime.setEnabled(true);
			} else {
				btnDeliveryTime.setEnabled(false);
			}
		} else if (v == chkPicktime) {
			if (chkPicktime.isChecked()) {
				btnPickTime.setEnabled(true);
			} else {
				btnPickTime.setEnabled(false);
			}
		}
	}

	private boolean validation() {

//		if (txtDeliFee.getText().toString().length() != 0)
//		{
//			if (edt_check.getText().toString().length() == 0)
//			{
//				new CustomAlertDialog(DeliveryPOD.this, "Please enter the Check").show();
//				return false;
//
//			}
//		}

		return true;
	}

	private boolean isPickupOnly() {
		return TYPE_OF_JOB == PICKUP_ONLY;
	}

	private boolean isDeliveryOnly() {
		return TYPE_OF_JOB == DELIVERY_ONLY;
	}

	public void getGps() {
		gps = new GPSTracker(DeliveryPOD.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			String la = String.valueOf(latitude) +","+String.valueOf(longitude);
			LatLng latLng = new LatLng(latitude,longitude);
			btnGetGPS.setText(la);
		} else {

			gps.showSettingsAlert();
		}
	}

//	private boolean isPickupAndDelivery() {
//		return TYPE_OF_JOB == PICKUP_AND_DELIVERY;
//	}

	@SuppressLint("SetTextI18n")
	public void savePickup() {
		String workOrder = addressServer.getWorkorder();
		SubmitPickupPOD submitPickupPOD = new SubmitPickupPOD();
		submitPickupPOD.setWorkorder(workOrder);

		submitPickupPOD.setProofDate(btnPickDate.getText().toString()); // .replace("/", "-")
		submitPickupPOD.setProofTime("00:00:00 am");

		if (chkPicktime.isChecked()) {
			submitPickupPOD.setProofTime(btnPickTime.getText().toString().replace(" ", ":00 "));
		}
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss a");
			Date date = dateformat.parse(submitPickupPOD.getProofTime());
			dateformat = new SimpleDateFormat("HH:mm:ss");
			submitPickupPOD.setProofTime(dateformat.format(date));
		} catch (Exception e) {
			submitPickupPOD.setProofTime("00:00:00");
		}
		//if(txtPickComment.getText().toString().trim().equals("")) {
//			new CustomAlertDialog(DeliveryPOD.this, "Enter Pickup Comments").show();
//			return;
		//	}
//		if(isPickupAndDelivery()) {
//			if(txtDeliveryComment.getText().toString().trim().equals("")) {
//				new CustomAlertDialog(DeliveryPOD.this, "Enter Delivery Comments").show();
//				return;
//			}
//			if(txtDeliRecievedBy.getText().toString().equals("")) {
//				new CustomAlertDialog(DeliveryPOD.this, "Please enter Recieved by before Submit").show();
//				return;
//			}
//
//		}

		submitPickupPOD.setProofComments(txtPickComment.getText().toString());
		submitPickupPOD.setLatitude(String.valueOf(latitude));
		submitPickupPOD.setLongitude(String.valueOf(longitude));
		submitPickupPOD.setAddressLineitem(addressServer.getAddressLineItem());
		try {
			boolean isSubmitSucess = database.insertIntoSubmitPickupPODTable(submitPickupPOD);
			if (isSubmitSucess) {
				String imageSaved = "";
				SubmitPickupPOD submitImage = new SubmitPickupPOD();
				submitImage.setSubmitPickupPODID(database.getLastinsertedLineItemFromSubmitPickupPOD());
				ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance().getAttachedFilesData();
				for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
					submitImage.setWorkorder(workOrder);
					submitImage.setAttachmentFilename(workOrder + "-" + addressServer.getAddressLineItem() + "-image-" + i);
					String base64String = Base64.encodeToString(imageArrayToSaveInDB.get(i), Base64.DEFAULT);
					submitImage.setAttachmentBase64String(base64String);
					submitImage.setAttachmentUrlString();
					submitImage.setAddressLineitem(addressServer.getAddressLineItem());
					boolean success = true;
					success = database.insertAttachmentsOfPickupPOD(submitImage);
					if (!success) {
						imageSaved = "Image is not saved!";
					}
				}
				if (imageSaved.length() == 0) {
				} else {
					new CustomAlertDialog(DeliveryPOD.this, imageSaved).show();
					return;
				}

//				if(isPickupAndDelivery()) {
//			    	saveDelivery();
//				} else {
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.alertbox);


				TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
				text.setText("Pickup POD is saved successfully");

				Button dialogButton = (Button) dialog
						.findViewById(R.id.btn_ok);

				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
						Intent court = new Intent(DeliveryPOD.this,
								ListCategory.class);
						startActivity(court);

					}
				});

				dialog.show();
//				}
			} else {
				new CustomAlertDialog(DeliveryPOD.this, "Pickup POD is not saved!please try again").show();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SetTextI18n")
	private void saveDelivery() {
		String workOrder = addressServer.getWorkorder();
		SubmitDeliveryPOD submitDeliveryPOD = new SubmitDeliveryPOD();
		submitDeliveryPOD.setWorkorder(workOrder);

		submitDeliveryPOD.setProofDate(btnDeliveryDate.getText().toString()); // .replace("/", "-")
		submitDeliveryPOD.setProofTime("00:00:00 am");
		if (submitDeliveryPOD.getProofDate().length() != 10) {
			new CustomAlertDialog(DeliveryPOD.this, "Delivery POD is not saved due to none date").show();
			return;
		}

		if (chkDeliveryTime.isChecked()) {
			submitDeliveryPOD.setProofTime(btnDeliveryTime.getText().toString().replace(" ", ":00 "));
			if (submitDeliveryPOD.getProofTime().length() != 11) {
				new CustomAlertDialog(DeliveryPOD.this, "Delivery POD is not saved due to none time").show();
				return;
			}
		}
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss a");
			Date date = dateformat.parse(submitDeliveryPOD.getProofTime());
			dateformat = new SimpleDateFormat("HH:mm:ss");
			submitDeliveryPOD.setProofTime(dateformat.format(date));
		} catch (Exception e) {
			submitDeliveryPOD.setProofTime("00:00:00");
		}
		submitDeliveryPOD.setLatitude(String.valueOf(latitude));
		submitDeliveryPOD.setLongitude(String.valueOf(longitude));
		submitDeliveryPOD.setAddressLineitem(addressServer.getAddressLineItem());
		submitDeliveryPOD.setFeeAdvance(getInt(txtDeliFee.getText().toString()));
		submitDeliveryPOD.setWeight(getInt(txtDeliWeight.getText().toString()));
		submitDeliveryPOD.setPieces(getInt(txtDeliPieces.getText().toString()));
		submitDeliveryPOD.setWaitTime(getInt(txtDeliWaitTime.getText().toString()));
		submitDeliveryPOD.setReceivedBy(txtDeliRecievedBy.getText().toString());

		submitDeliveryPOD.setProofComments(txtDeliveryComment.getText().toString().trim());
//		if(txtDeliveryComment.getText().toString().trim().equals("")) {
//			new CustomAlertDialog(DeliveryPOD.this, "Enter Delivery Comments").show();
//			return;
//		}
		if (txtDeliRecievedBy.getText().toString().equals("")) {
			new CustomAlertDialog(DeliveryPOD.this, "Please enter Recieved by before Submit").show();
			return;
		}
//		if(signatureData == null) {
//			new CustomAlertDialog(DeliveryPOD.this, "Please make Signature before submit").show();
//			return;
//		}
		try {
			boolean isSubmitSucess = database.insertIntoSubmitDeliveryPODTable(submitDeliveryPOD);
			if (isSubmitSucess) {

				if (SessionData.getInstance().getAttachedFilesData() != null
						&& SessionData.getInstance().getAttachedFilesData().size() != 0) {
					String Attach_imageSaved = "";
					PODAttachments Attach_submitImage = new PODAttachments();
					Attach_submitImage.setSubmitPODID(database.getLastinsertedLineItemFromSubmitPickupPOD());
					ArrayList<byte[]> imageArrayToSaveInDB = SessionData.getInstance().getAttachedFilesData();
					for (int i = 0; i < imageArrayToSaveInDB.size(); i++) {
						Attach_submitImage.setWorkorder(workOrder);
						Attach_submitImage.setFileName(workOrder + "-" + addressServer.getAddressLineItem() + "-image-" + i);
						String base64String = Base64.encodeToString(imageArrayToSaveInDB.get(i), Base64.DEFAULT);
						Attach_submitImage.setPdfInMemory(base64String);
						Attach_submitImage.setLineitem(addressServer.getAddressLineItem());
						boolean Attache_Image_success = true;
						Attache_Image_success = database.insertAttachmentsOfDeliveryPOD(Attach_submitImage);
						if (!Attache_Image_success) {
							Attach_imageSaved = "Image is not saved!";
							Log.d("Attache_Image_success",""+Attache_Image_success);
						}else {
							Log.d("Attache_Image_success",""+Attache_Image_success);
						}
					}
					if (Attach_imageSaved.length() == 0) {
					}
				}

				String imageSaved = "";
				PODAttachments submitImage = new PODAttachments();
				boolean success = true;
				submitImage.setSubmitPODID(database.getLastinsertedLineItemFromSubmitPickupPOD());
				if (signatureData != null) {
					submitImage.setWorkorder(workOrder);
					submitImage.setFileName(workOrder + "-" + addressServer.getAddressLineItem() + "-signature");
					String base64String = Base64.encodeToString(signatureData, Base64.DEFAULT);

					submitImage.setPdfInMemory(base64String);

					submitImage.setLineitem(addressServer.getAddressLineItem());

					success = database.insertAttachmentsOfDeliveryPOD(submitImage);
				}
				if (success) {
					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.alertbox);

					TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
					text.setText("Delivery POD is saved successfully");

					Button dialogButton = (Button) dialog
							.findViewById(R.id.btn_ok);

					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
							Intent court = new Intent(DeliveryPOD.this,
									ListCategory.class);
							startActivity(court);

						}
					});

					dialog.show();
				} else {
					new CustomAlertDialog(DeliveryPOD.this, imageSaved).show();
					return;
				}

			} else {
				new CustomAlertDialog(DeliveryPOD.this, "Delivery POD is not saved!").show();
			}


		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public void onBackPressed() {
		finish();
		Intent courtService = new Intent(DeliveryPOD.this, DeliveryServiceDetail.class);
		startActivity(courtService);
		super.onBackPressed();
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
