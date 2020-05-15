package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tristar.db.DataBaseHelper;
import com.tristar.object.AddProcessAddress;
import com.tristar.object.Address;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.SubmitWebServiceConsumer;
import com.tristar.webutils.WebServiceConsumer;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ProcessOrderAddAddress extends Activity implements OnClickListener {
	ImageView imageButtonback;
	int processOrderID, processOrderIDsession, newprocessorderid;
	TextView back, Title;
	Bundle extra;
	Spinner spin;
	String addtype;
	String str_city, str_state, str_zip, str_street;
	
	int addaddress;
	EditText state, city, street, zip;
	Address address = new Address();
	public final static int SYNC = 101;
	Button submit;
	DataBaseHelper database;
	ProcessAddressForServer processOrderToDisplayInDetailView;
	AddProcessAddress addprocessaddress;
	public int lineItem;
	int i;

	EditText edt_type;
	ImageView select_Type;

	ArrayAdapter<String> adapterss;
	ArrayList<String> TypeList = new ArrayList<>();
	ArrayList<Integer> TypeListID = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);
		extra = getIntent().getExtras();
		if (extra != null)
			processOrderID = extra.getInt("processOrderID");
		back = (TextView) findViewById(R.id.txt_backfinalstatus);
		Title = (TextView) findViewById(R.id.title_id);
		processOrderIDsession = SessionData.getInstance().getProcessorderid();
		Log.d("processOrderIDsession",""+ processOrderIDsession);
		database = DataBaseHelper.getInstance();
		processOrderToDisplayInDetailView = new ProcessAddressForServer();
		
		imageButtonback = (ImageView) findViewById(R.id.imageButtonback);
		submit = (Button) findViewById(R.id.btnsubmit);
		SessionData.getInstance().setCity("");
		SessionData.getInstance().setState("");
		SessionData.getInstance().setZip("");
		SessionData.getInstance().setStreet("");
		spin = (Spinner) findViewById(R.id.spinner1);
		spin.setVisibility(View.GONE);

		final MyData items[] = new MyData[5];
		items[0] = new MyData("Home", "2");
		items[1] = new MyData("Business", "1");
		items[2] = new MyData("Others", "3");
		items[3] = new MyData("Government", "4");
		items[4] = new MyData("Not a physical Address", "5");


		edt_type = (EditText)findViewById(R.id.edt_type);
		select_Type = (ImageView)findViewById(R.id.select_type);
		edt_type.setFocusable(false);
		edt_type.setOnClickListener(this);
		select_Type.setOnClickListener(this);

		TypeList.add("Home");
		TypeList.add("Business");
		TypeList.add("Others");
		TypeList.add("Government");
		TypeList.add("Not a physical Address");

		TypeListID.add(2);
		TypeListID.add(1);
		TypeListID.add(3);
		TypeListID.add(4);
		TypeListID.add(5);


		ArrayAdapter<MyData> adapter = new ArrayAdapter<MyData>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				MyData d = items[position];
				i = Integer.parseInt(d.getValue());
                
				Log.d("count", "" + i);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		street = (EditText) findViewById(R.id.edt_street);
		city = (EditText) findViewById(R.id.edt_city);
		city.setFilters(new InputFilter[] { new InputFilter() {
			public CharSequence filter(CharSequence src, int start, int end,
					Spanned dst, int dstart, int dend) {
				if (src.equals("")) {
					return src;
				}
				if (src.toString().matches("[a-zA-Z ]+")) {
					return src;
				}
				return "";
			}
		} });
		state = (EditText) findViewById(R.id.edt_state);
		state.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
		zip = (EditText) findViewById(R.id.edt_zip);
		zip.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
		back.setOnClickListener(this);
		imageButtonback.setOnClickListener(this);
		submit.setOnClickListener(this);
		initializeViews();
	}

	private void initializeViews() {

		Title.setText(SessionData.getInstance().getAddaddressWorkorder());
		try {
			processOrderToDisplayInDetailView = database
					.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {

		if (v == back || v == imageButtonback) {
			hideKeyboard(v);
			finish();

		} else if (v == submit) {
			if (street.getText().length() == 0 || city.getText().length() == 0
					|| state.getText().length() == 0
					|| zip.getText().length() == 0) {
				Toast.makeText(ProcessOrderAddAddress.this, "Enter all fields",
						Toast.LENGTH_LONG).show();
			} else {

				address.setCity("" + city.getText().toString());
				
				str_city = ("" + city.getText().toString());
				address.setState("" + state.getText().toString());
				str_state = ("" + state.getText().toString());
				address.setStreet("" + street.getText().toString());
				str_street = ("" + street.getText().toString());
				address.setZip("" + zip.getText().toString());
				str_zip = ("" + zip.getText().toString());
				
				address.setTheAddressType(i);
				address.setWorkorder(""
						+ SessionData.getInstance().getAddaddressWorkorder());
				address.setLineItem(SessionData.getInstance()
						.getAddaddressLineItem());

				new AddAddress().execute();

				processadressInDB();
			}
		}

		else if(v == edt_type){
			dialogType();

		} else if(v == select_Type){
			dialogType();
		}

	}

	public void hideKeyboard(View activity) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getWindowToken(), 0);
	}


	@SuppressLint("SetTextI18n")
	private  void dialogType(){
		final Dialog dialog = new Dialog(ProcessOrderAddAddress.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
		int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

		TextView txt_Header = (TextView)dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Select Type");

		final ListView list = (ListView)dialog.findViewById(R.id.list);
		adapterss = new ArrayAdapter<String>(this, R.layout.choise_list, TypeList);
		list.setAdapter(adapterss);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemChecked(adapterss.getPosition(edt_type.getText().toString()),true);
		Log.d("adapterss",""+adapterss.getPosition(edt_type.getText().toString()));

		final String[] select = new String[1];
		if(edt_type.getText().toString().length()!=0){
			select[0] = edt_type.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//MyData d = items[position];
				//	i = Integer.parseInt(d.getValue());
				i = TypeListID.get(position);

				select[0] = list.getItemAtPosition(position).toString();
			}
		});
		Button Save = (Button)dialog.findViewById(R.id.save);
		Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_type.setText(select[0]);

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

	private class AddAddress extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			//ProgressBar.showCommonProgressDialog(ProcessOrderAddAddress.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				String sessionId = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());
				addaddress = SubmitWebServiceConsumer.getInstance()
						.AddProcessAddress(sessionId, address);

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				addaddress = 0;
			} catch (Exception e) {
				e.printStackTrace();
				addaddress = 0;
			}

			return null;
		}

		@SuppressLint("LongLogTag")
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			//ProgressBar.dismiss();
			if (addaddress != 0) {
				processOrderToDisplayInDetailView.setAddressLineItem(addaddress);
				Log.d("getAddressLineItem", ""+processOrderToDisplayInDetailView.getAddressLineItem());
				
				if(i == 1){
					addtype = "Business";
				}
				else if(i ==2){
					addtype = "Home";
				}
				else if(i ==3){
					addtype = "Others";
				}
				else if(i == 4){
					addtype = "Government";
				}
				else if(i == 5){
					addtype = "Not a physical Address";
				}
				
				processOrderToDisplayInDetailView.setAddressFormattedForDisplay(addtype + ":" 
				+ str_street + " " + str_city + ", " + str_state + " " + str_zip);
				Log.d("getAddressFormattedForDisplay", ""+processOrderToDisplayInDetailView.getAddressFormattedForDisplay());
				
				processOrderToDisplayInDetailView.setAddressFormattedForGoogle(str_street + " " + str_city + ", " + str_state + " " + str_zip);
				Log.d("getAddressFormattedForGoogle", ""+processOrderToDisplayInDetailView.getAddressFormattedForGoogle());
				
				try {
					database.insertProcessOpenAddressFromServer(processOrderToDisplayInDetailView);
					newprocessorderid = database
					.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView().size();
					
					Log.d("new ID", ""+newprocessorderid);
						
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				Intent processdetail = new Intent(ProcessOrderAddAddress.this,
						ProcessOrderDetail.class);
				processdetail.putExtra("processOrderID",
						newprocessorderid);
				startActivity(processdetail);            
				
				
				
			} else {
				Toast.makeText(ProcessOrderAddAddress.this,
						"Address values are not saved! Please try again", Toast.LENGTH_LONG).show();
			}
               
		}

	}

	private void processadressInDB() {
		SessionData.getInstance().setStreet(street.getText().toString());
		SessionData.getInstance().setState(state.getText().toString());
		SessionData.getInstance().setCity(city.getText().toString());
		SessionData.getInstance().setZip(zip.getText().toString());
		SessionData.getInstance().setTheAddressType(i);
		Log.d("addresstype", "" + i);

		Toast.makeText(ProcessOrderAddAddress.this,
				"Address values are saved successfully!", Toast.LENGTH_LONG).show();

		finish();

	}

	class MyData {
		public MyData(String spinnerText, String value) {
			this.spinnerText = spinnerText;
			this.value = value;
		}

		public String getSpinnerText() {
			return spinnerText;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return spinnerText;
		}

		String spinnerText;
		String value;
	}

}
