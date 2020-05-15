package com.tristar.main;

import java.util.ArrayList;

import com.tristar.db.DataBaseHelper;
import com.tristar.object.AddProcessAddress;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.SessionData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

@SuppressWarnings("ALL")
public class AddAddress extends Activity implements OnClickListener {
	ImageView imageButtonback;
	int processOrderID;
	TextView back;
	TextView Title;
	Bundle extra;
	EditText edt_type;
	ImageView select_Type;
	Spinner spin;
	EditText state, city, street, zip;
	public final static int SYNC = 101;
	Button submit;
	DataBaseHelper database;
	ProcessAddressForServer processOrderToDisplayInDetailView;
	AddProcessAddress addprocessaddress;
	public int lineItem;
	int i;
	ArrayAdapter<String> adapter;
	ArrayList<String> TypeList = new ArrayList<>();
	ArrayList<Integer> TypeListID = new ArrayList<>();
	//final MyData items[] = new MyData[5];
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
		imageButtonback = (ImageView) findViewById(R.id.imageButtonback);
		submit = (Button) findViewById(R.id.btnsubmit);
		edt_type.setFocusable(false);
		edt_type = (EditText)findViewById(R.id.edt_type);
		select_Type = (ImageView)findViewById(R.id.select_type);
		edt_type.setOnClickListener(this);
		select_Type.setOnClickListener(this);


		SessionData.getInstance().setCity("");
		SessionData.getInstance().setState("");
		SessionData.getInstance().setZip("");
		SessionData.getInstance().setStreet("");
		spin = (Spinner) findViewById(R.id.spinner1);

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



//		items[0] = new MyData("Home", "2");
//		items[1] = new MyData("Business", "1");
//		items[2] = new MyData("Others", "3");
//		items[3] = new MyData("Government", "4");
//		items[4] = new MyData("Not a physical Address", "5");

//		adapter = new ArrayAdapter<MyData>(this,
//				R.layout.choise_list, items);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//ArrayAdapter<MyData> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, items);
//		spin.setAdapter(adapter);
//		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				MyData d = items[position];
//				i = Integer.parseInt(d.getValue());
//
//				Log.d("count", "" + i);
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

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
//		state.setFilters(new InputFilter[]{ new InputFilter.AllCaps()});
		zip = (EditText) findViewById(R.id.edt_zip);
		zip.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
		back.setOnClickListener(this);
		imageButtonback.setOnClickListener(this);
		submit.setOnClickListener(this);
		initializeViews();
	}

	private void initializeViews() {

		Title.setText(SessionData.getInstance().getWorkid());

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {

		if (v == back || v == imageButtonback) {
			finish();

		} else if (v == submit) {
			if (street.getText().length() == 0 || city.getText().length() == 0
					|| state.getText().length() == 0
					|| zip.getText().length() == 0) {
				Toast.makeText(AddAddress.this, "Enter all fields",
						Toast.LENGTH_LONG).show();
			} else {
				processadressInDB();
			}
		} else if(v == edt_type){
			dialogType();

		} else if(v == select_Type){
			dialogType();
		}

	}

	@SuppressLint("SetTextI18n")
	private  void dialogType(){
		final Dialog dialog = new Dialog(AddAddress.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogbox_1);

		int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
		int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

		dialog.getWindow().setLayout(width, height);

		TextView txt_Header = (TextView)dialog.findViewById(R.id.txt_header);
		txt_Header.setText("Select Type");

		final ListView list = (ListView)dialog.findViewById(R.id.list);
	     adapter = new ArrayAdapter<String>(this, R.layout.choise_list, TypeList);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		// we want multiple clicks
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//list.setItemChecked(adapter.getPosition(edt_type.getText().toString()),true);

		final String[] select = new String[1];
		if(edt_type.getText().toString().length()!=0){
			select[0] = edt_type.getText().toString();
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//MyData d = items[position];
			//	i = Integer.parseInt(d.getValue());
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




	private void processadressInDB() {
		SessionData.getInstance().setStreet(street.getText().toString());
		SessionData.getInstance().setState(state.getText().toString());
		SessionData.getInstance().setCity(city.getText().toString());
		SessionData.getInstance().setZip(zip.getText().toString());
		SessionData.getInstance().setTheAddressType(i);
		Log.d("addresstype", "" + i);

		Toast.makeText(AddAddress.this, "Address saved successfully",
				Toast.LENGTH_LONG).show();

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
