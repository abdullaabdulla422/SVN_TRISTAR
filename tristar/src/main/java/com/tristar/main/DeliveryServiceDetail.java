package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.object.AddressForServer;
import com.tristar.object.CodeAndTitle;
import com.tristar.object.ReturnHistoryObject;
import com.tristar.object.ReturnStatusListObect;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class DeliveryServiceDetail extends Activity implements OnClickListener, OnLongClickListener{
	Button delivery;
	TextView back, address, txt_worker, txt_duetime,txt_receivedate, txt_duedate, txt_priority, txt_title, txt_caseName, txt_caseNumber;
  	ImageView image;
  	AddressForServer addressServer;
	Button btnSpecialInstruction;
	String AddressInstructions;
	Button btnSubmitStatus,submit_status,select_status;
	Dialog mDialog, Dialog;
	TextView txt_Dialog_Workorder;
	String specialMessage;
    EditText Edt_comment;
	int status = 0;
	String phone1, phone2, phone3 , phone, finalphone;

	Context context;
	public ArrayList<ReturnStatusListObect> statuslist = new ArrayList<>();

	ArrayList<ReturnHistoryObject> returnHistoryObjects;

	TextView TxtBusiness, TxtContact, TxtPhone;

	String workorder;
  	private DataBaseHelper database;
	String SpecialInstructions;
	TextView TxtJobInstructions, TxtAddressInstructions;
	Button BtnHistory;
	Button BtnAttachment;
	ArrayList<CodeAndTitle> code;
  	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_service_detail_view);
		context = this;
		if(SessionData.getInstance().getSelectedItem()==1){
			addressServer = (AddressForServer) ListCategory.selectedAddressServer;

		}else{
			addressServer = (AddressForServer) CourtService.selectedAddressServer;

		}
		//addressServer = (AddressForServer) CourtService.selectedAddressServer;
		database = DataBaseHelper.getInstance();
		if (database == null) {
			database = DataBaseHelper.getInstance(DeliveryServiceDetail.this);
		}
		intializeControlls();
		intializeData();
	
	}
	
	public void intializeControlls() {
		back = (TextView)findViewById(R.id.txt_processback);
	    image = (ImageView)findViewById(R.id.imageButtonback);
		address = (TextView) findViewById(R.id.txt_address);
		TxtBusiness = (TextView) findViewById(R.id.txt_name);
		TxtContact = (TextView) findViewById(R.id.txt_requestor);
		TxtPhone = (TextView) findViewById(R.id.txt_phonenumber);

		delivery = (Button) findViewById(R.id.btn_submitpod);
		btnSpecialInstruction = (Button) findViewById(R.id.btnspecial);
		btnSubmitStatus = (Button)findViewById(R.id.btn_status);
		BtnHistory = (Button)findViewById(R.id.btn_history);
		BtnAttachment = (Button)findViewById(R.id.btn_attachment);


		ScrollView Main = (ScrollView)findViewById(R.id.scroll_main);
		ScrollView ScrollJob = (ScrollView)findViewById(R.id.scroll_job);
		ScrollView ScrollAdd = (ScrollView)findViewById(R.id.scroll_add);

//		Main.setOnTouchListener(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//
//				findViewById(R.id.scroll_job).getParent()
//						.requestDisallowInterceptTouchEvent(false);
//				findViewById(R.id.scroll_add).getParent()
//						.requestDisallowInterceptTouchEvent(false);
//				return false;
//			}
//		});
//
//		ScrollJob.setOnTouchListener(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//
//				// Disallow the touch request for parent scroll on touch of  process_order_child view
//				v.getParent().requestDisallowInterceptTouchEvent(true);
//				return false;
//			}
//		});
//
//		ScrollAdd.setOnTouchListener(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//
//				// Disallow the touch request for parent scroll on touch of  process_order_child view
//				v.getParent().requestDisallowInterceptTouchEvent(true);
//				return false;
//			}
//		});
		
		txt_worker = (TextView) findViewById(R.id.txt_worker);
		TxtJobInstructions = (TextView) findViewById(R.id.txt_jobinstructions);
		TxtAddressInstructions = (TextView) findViewById(R.id.txt_address_instructions);
//		TxtAddressInstructions.setMovementMethod(new ScrollingMovementMethod());
//		TxtJobInstructions.setMovementMethod(new ScrollingMovementMethod());
		txt_duetime = (TextView) findViewById(R.id.txt_duetime);
		txt_duedate = (TextView) findViewById(R.id.txt_duedate);
		txt_receivedate = (TextView) findViewById(R.id.txt_recieveddate);
		txt_priority = (TextView) findViewById(R.id.txt_priority);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_caseName = (TextView) findViewById(R.id.txt_casename);
		txt_caseNumber = (TextView) findViewById(R.id.txt_casenumber);


		btnSubmitStatus.setOnClickListener(this);
		back.setOnClickListener(this);
		image.setOnClickListener(this);
		delivery.setOnClickListener(this);
		address.setOnLongClickListener(this);
		address.setOnClickListener(this);
		btnSpecialInstruction.setOnClickListener(this);
		BtnHistory.setOnClickListener(this);
		TxtPhone.setOnClickListener(this);
		BtnAttachment.setOnClickListener(this);

	}
	
	@SuppressLint("SetTextI18n")
	public void intializeData() {

		if(database
				.getStatusValuesFromDBToDisplay()!=null){
			statuslist = database
					.getStatusValuesFromDBToDisplay();
		}
		
		if( addressServer.getWorkorder()!= null && addressServer.getWorkorder().length() != 0) {
			 SessionData.getInstance().setImageworkorder(addressServer.getWorkorder());
			
			txt_worker.setText(addressServer.getWorkorder());
		}
		else {
			txt_worker.setText("N/A");
		}
		if(addressServer.getAddressFormattedNewLine1().length()!=0){
			address.setText(addressServer.getAddressFormattedNewLine1()+"\n"+addressServer.getAddressFormattedNewLine2());
		}

		if(addressServer.getAddressFormattedForDisplay().length() != 0) {
			if(addressServer.getAddressFormattedNewLine1().length()==0) {
				address.setText(addressServer.getAddressFormattedForDisplay());
			}
			String str = addressServer.getAddressFormattedForDisplay();
			Log.d("Get_PDSubtitle",""+str.toString());

			if (str != null || str.length() != 0){
				for(int j=0; j<str.length();j++){
					Character character = str.charAt(j);
					if(character.toString().equals("&"))
					{
						str=str.substring(j+2);
						if(addressServer.getAddressFormattedNewLine1().length()==0) {
							address.setText(str);
						}
						Log.d("Get_PDSubtitle_&",""+address.getText().toString());
						break;
					}else {
						if(addressServer.getAddressFormattedNewLine1().length()==0) {
							address.setText(addressServer.getAddressFormattedForDisplay());
						}
						Log.d("Get_PDSubtitle_",""+address.getText().toString());
					}
				}
			}
		}
		else {
			//address.setText("N/A");
		}
		if(addressServer.getDueDate().length() != 0) {
			String date = addressServer.getDueDate().substring(0, 10);
			try {
				String dateget = getFormattedDateOnly(date);
				txt_duedate.setText(dateget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			txt_duedate.setText("N/A");
		}
		if(addressServer.getDateReceived() != null) {
			String recdate = addressServer.getDateReceived();
			//try {
				//String recdateget = getFormattedDateOnly(recdate);
				txt_receivedate.setText(recdate);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		else {
			txt_receivedate.setText("N/A");
		}
		if(addressServer.getTimeReceived().length() != 0) {
			txt_duetime.setText(addressServer.getTimeReceived());
		}
		else {
			txt_duetime.setText("N/A");
		}
		if(addressServer.getPriorityTitle().length() != 0) {
			txt_priority.setText(addressServer.getPriorityTitle());
		}
		else {
			txt_priority.setText("N/A");
			
		}

//

		if(addressServer.getCaseName().length() != 0) {
			txt_caseName.setText(addressServer.getCaseName());
		}
		else {
			txt_caseName.setText("N/A");

		}

		if(addressServer.getCaseNumber().length() != 0) {
			txt_caseNumber.setText(addressServer.getCaseNumber());
		}
		else {
			txt_caseNumber.setText("N/A");

		}

		if(addressServer.getBusiness().length() != 0){
			TxtBusiness.setText(addressServer.getBusiness());
		}
		else {
			TxtBusiness.setText("N/A");

		}
		if(addressServer.getOrderContact().length() != 0) {
			TxtContact.setText(addressServer.getOrderContact());
		}
		else {
			TxtContact.setText("N/A");

		}

		if(addressServer.getContactPhone().length() != 0) {

			phone = addressServer.getContactPhone();

			phone1 = phone.substring(0, 3);
			phone2 = phone.substring(3, 6);
			phone3 = phone.substring(6,10);
			Log.d("split phone", "" + phone1);

			TxtPhone.setText("(" + phone1 + ") " + phone2 + "-" + phone3);

			//TxtPhone.setText(addressServer.getContactPhone());
		}
		else {
			TxtPhone.setText("N/A");

		}
		
		boolean isPickupExists = database.isPickPodAvailableToAdd(addressServer.getWorkorder());
		txt_title.setText("Delivery Service");
		if(addressServer.TYPE == AddressForServer.PICKUP_SERVICE) {
			txt_title.setText("Pickup Service");
			SpecialInstructions = addressServer.getOrderInstructions();
			AddressInstructions = addressServer.getPickupInstructions();

			delivery.setText("Submit Pickup POD");
			status = 0;
		} 
//		else if(! isPickupExists) {
//			delivery.setText("Submit Delivery POD");
//		}
		else {
			status = 1;
			SpecialInstructions = addressServer.getOrderInstructions();
			AddressInstructions = addressServer.getDeliveryInstructions();

			delivery.setText("Submit Delivery POD");
//			delivery.setText("Submit Pickup & Delivery POD");
		}

		TxtJobInstructions.setText(SpecialInstructions);
		TxtAddressInstructions.setText(AddressInstructions);
	}
	
	@Override
	public void onClick(View v) {
		if(v == back || v == image) {
			finish();
			Intent courtService = new Intent(DeliveryServiceDetail.this, CourtService.class);
			startActivity(courtService);
		}
		else if(v == delivery) {
			finish();
			Intent category = new Intent(DeliveryServiceDetail.this,DeliveryPOD.class);
			startActivity(category);
		}
		else if(v == TxtPhone){
			if (addressServer.getContactPhone().length() != 0) {
				phonedialog();
			}
		}
		else if(v == BtnAttachment){
			new MyAstask().execute();
		}
		else if(v == BtnHistory){
			new GetHistoryList().execute();
		}
		else if(v == address) {
			double lat = Double.parseDouble(addressServer.getLatitude());
			double lon = Double.parseDouble(addressServer.getLongitude());
					
			String uriBegin = "geo:" + lat + "," + lon;
			String uriString = uriBegin + "?q=" + addressServer.getAddressFormattedForDisplay() ;
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}else if(v==btnSpecialInstruction){
			if(SpecialInstructions.length()+AddressInstructions.length()==0){
				if(status==0) {
					new CustomAlertDialog(
							DeliveryServiceDetail.this,
							"This Pickup POD has no special instructions.")
							.show();
				}
				else{
					new CustomAlertDialog(
							DeliveryServiceDetail.this,
							"This Delivery POD has no special instructions.")
							.show();
				}

			}else{
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.message_insptructions);
				TextView work = (TextView) dialog.findViewById(R.id.workoder);
				work.setText(addressServer.getWorkorder());

				TextView text = (TextView) dialog.findViewById(R.id.textdes);
				text.setText(SpecialInstructions);

				TextView txt_addressinstruction = (TextView)dialog.findViewById(R.id.textView_instructions);
				txt_addressinstruction.setText(AddressInstructions);
				Button dialogButton = (Button) dialog
						.findViewById(R.id.dialogButtonOK);

				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				dialog.show();
				Window window = dialog.getWindow();
				window.setLayout(ActionBar.LayoutParams.MATCH_PARENT,
						ActionBar.LayoutParams.WRAP_CONTENT);
			}

			//new Mytask().execute();
		}
		else if(v==btnSubmitStatus){
			StatusDialog();
		}
	}

	private class MyAstask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(DeliveryServiceDetail.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String session = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				code = WebServiceConsumer.getInstance().getAttachedPDFList(
						session,
						workorder);
				SessionData.getInstance().setWorklistid(
						workorder);
			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				code = null;
			} catch (Exception e) {
				e.printStackTrace();
				code = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (code != null && code.size() > 0) {
				Log.d("The detail Array Size", "" + code.size());
				SessionData.getInstance().setDetail(code);
				Intent intent = new Intent(DeliveryServiceDetail.this,
						ViewPdf.class);
				//	intent.putExtra("processOrderID", processOrderID);
				startActivity(intent);
			} else {
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.alertbox);

				TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
				text.setText("No Attachment for this Workorder");

				Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				dialog.show();
			}
			ProgressBar.dismiss();
			super.onPostExecute(result);
		}
	}

	private void phonedialog() {

		final Dialog dialog = new Dialog(DeliveryServiceDetail.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.phone_dialog);
		TextView phone = (TextView) dialog.findViewById(R.id.phoneno);
		phone.setText("(" + phone1 + ")" + phone2 + "-" + phone3);

		Button cancel = (Button) dialog.findViewById(R.id.btn_yes);
		Button call = (Button) dialog.findViewById(R.id.btn_no);

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
			}
		});
		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + addressServer
							.getContactPhone().toString()));
					startActivity(callIntent);
				} catch (Exception exception) {
					//Logger.log(exception);
				}

				dialog.dismiss();
			}
		});
		dialog.show();
	}


	private void StatusDialog() {
		mDialog = new Dialog(DeliveryServiceDetail.this);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_status_details);

//		dpi = getResources().getDisplayMetrics().density;
//		LayoutInflater inflater = (LayoutInflater) mDialog.getContext()
//				.getSystemService(FinalStatus.LAYOUT_INFLATER_SERVICE);
//		mannerView = inflater.inflate(R.layout.activity_manner_selection, null);
//
//		mannerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//				(int) (300 * dpi)));
//		popupManner = new PopupWindow(mannerView, ViewGroup.LayoutParams.WRAP_CONTENT,
//				(int) (300 * dpi), true);
//		mannerWhhel = (WheelView) mannerView.findViewById(R.id.dp1);

		txt_Dialog_Workorder = (TextView)mDialog.findViewById(R.id.workoder);
		txt_Dialog_Workorder.setText(addressServer.getWorkorder());
		workorder = txt_Dialog_Workorder.getText().toString();
		submit_status=(Button)mDialog.findViewById(R.id.button_submit);
		select_status=(Button)mDialog.findViewById(R.id.select_status);
		Edt_comment=(EditText)mDialog.findViewById(R.id.edt_comment);

		select_status.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {


				if(statuslist.size()!=0) {

					dialog();
				}
				else{
					new CustomAlertDialog(
							DeliveryServiceDetail.this,
							"No Status Available, Enter manually")
							.show();
				}
				//	popup();
			}
		});
		submit_status.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {
				if (Edt_comment.getText().length() == 0) {
					new CustomAlertDialog(
							DeliveryServiceDetail.this,
							"Report cannot be empty. Either type some text or select one status item then submit")
							.show();
				} else {


					saveValuesInDB(workorder
					);
//						Intent detailView = new Intent(CourtServiceDetail.this,
//								ListCategory.class);
//						detailView.putExtra("processOrderID", processOrderID);
					new CustomAlertDialog(DeliveryServiceDetail.this,
							"Status value is saved successfully!")
							.show();

				}
				mDialog.dismiss();
			}
		});

		mDialog.show();

		Window window = mDialog.getWindow();
		window.setLayout(ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.WRAP_CONTENT);

	}

	@SuppressLint("SetTextI18n")
	private void dialog() {
		final Dialog dialog = new Dialog(DeliveryServiceDetail.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
		int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

		TextView txt_Header = (TextView)dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Select Status");

		final ListView list = (ListView)dialog.findViewById(R.id.list);
		final ArrayList<String> array = new ArrayList<>();

		for (int i =0; statuslist.size()>i;i++){
			array.add(statuslist.get(i).getReport());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, array);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapter.getPosition(Edt_comment.getText().toString()),true);

		final String[] select = new String[1];
		if(Edt_comment.getText().toString().length()!=0){
			select[0] = Edt_comment.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button)dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Edt_comment.setText(select[0]);
				dialog.dismiss();
			}
		});
		ImageView close =(ImageView)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});


		dialog.show();
	}

	public void saveValuesInDB(String wrk_order) {
		SubmitStatusList submitstatusInDB = new SubmitStatusList();
		submitstatusInDB.setWorkorder(wrk_order);
		Log.d("Insert Work",""+wrk_order);
		int randomPIN = (int)(Math.random()*999999)+100000;
		Log.d("RandomNumber:","" + randomPIN);
		submitstatusInDB.setReport(Edt_comment.getText().toString());

		submitstatusInDB.setServerCode("");
		submitstatusInDB.setLineitem(randomPIN);
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		dateFormatter.setLenient(false);
		Date today = new Date();
		String date = dateFormatter.format(today);

		DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
		dateFormatter.setLenient(false);
		Date todayTime = new Date();
		String time = timeFormatter.format(todayTime);

		Log.d("Date", " "+ date);
		Log.d("time", " "+ time);

		DateFormat DFormatter = new SimpleDateFormat("yyyy-MM-dd");
		DFormatter.setLenient(false);
		Date Ddate = new Date();
		String ddate = DFormatter.format(Ddate);

		DateFormat TFormatter = new SimpleDateFormat("hh:mm a");
		TFormatter.setLenient(false);
		Date Ttime = new Date();
		String ttime = TFormatter.format(Ttime);

		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String submitDate = dateFormat.format(cal.getTime());

		submitstatusInDB.setStatusTime(time);
		submitstatusInDB.setStatusDate(date);
		submitstatusInDB.setDateTimeSubmitted(submitDate);

//		if(status==0){
//			try {
//				database.insertsubmitPickupStatusfromServer(submitstatusInDB);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			try {
//				database.insertsubmitDeliveryStatusfromServer(submitstatusInDB);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}


		String result = database.check_SubmitPickupStatusTable(workorder);
		if (result.contains("no workorder"))
		{
			try {
				database.insertsubmitPickupStatusfromServer(submitstatusInDB);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				database.update_SubmitPickupStatusTable(submitstatusInDB);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



	}


	private class Mytask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(DeliveryServiceDetail.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				specialMessage = WebServiceConsumer.getInstance()
						.GetSpecialInstructions(
								sessionId,
								addressServer.getWorkorder());

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				specialMessage = null;
			} catch (Exception e) {
				e.printStackTrace();
				specialMessage = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			ProgressBar.dismiss();
			if (specialMessage == null) {
				Toast.makeText(getApplicationContext(),
						"This Workorder has no Special Instructions",
						Toast.LENGTH_LONG).show();
			} else {

			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(v == address) {
			Intent addressOptions = new Intent(DeliveryServiceDetail.this, AddressOptions.class);
			addressOptions.putExtra("address" , addressServer.getAddressFormattedForDisplay());
			addressOptions.putExtra("Latitude", addressServer.getLatitude());
			addressOptions.putExtra("Longitude", addressServer.getLongitude());
			startActivity(addressOptions);
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		finish();
		Intent courtService = new Intent(DeliveryServiceDetail.this, CourtService.class);
		startActivity(courtService);
		super.onBackPressed();
	}
	
	@SuppressLint("SimpleDateFormat")
	private String getFormattedDateOnly(String date) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObject = null;
		try {
			dateObject = dateFormat.parse(date);
		} catch(Exception e) {
			dateObject = new Date();
		}
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		return dateFormat.format(dateObject);
	}
	private class GetHistoryList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			com.tristar.main.ProgressBar.showCommonProgressDialog(DeliveryServiceDetail.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				returnHistoryObjects = WebServiceConsumer.getInstance()
						.returnHistory(
								sessionId,
								addressServer.getWorkorder()
						);

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
		protected void onPostExecute(Void result){

			super.onPostExecute(result);
			com.tristar.main.ProgressBar.dismiss();
			//Log.d("History List", " " + returnHistoryObjects.size());
			if(returnHistoryObjects!=null){
				if (returnHistoryObjects.size() == 0) {

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
					Intent HistoryList = new Intent(DeliveryServiceDetail.this, History.class);
					startActivity(HistoryList);

				}
			}
			else{
				final Dialog dialog = new Dialog(DeliveryServiceDetail.this);
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
			}

		}

	}

}
