package com.tristar.main;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressWarnings("ALL")
public class AddressOptions extends Activity implements OnClickListener{
	String address, latitude, longitude , copyaddress;
	Button btnOpenMaps, btnAddContact, btnCopy, btnCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_options);
		
		address = getIntent().getExtras().getString("address");
		longitude = getIntent().getExtras().getString("Longitude", "0.0");
		latitude =getIntent().getExtras().getString("Latitude", "0.0");
		
		btnAddContact = (Button) findViewById(R.id.buttonAddtoContacts);
		btnOpenMaps = (Button) findViewById(R.id.buttonOpeninMaps);
		btnCopy = (Button) findViewById(R.id.buttonCopy);
		btnCancel = (Button) findViewById(R.id.buttonCancel);
		
		btnAddContact.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnCopy.setOnClickListener(this);
		btnOpenMaps.setOnClickListener(this); 
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if(v == btnOpenMaps) {
			double lat = Double.parseDouble(latitude);
			double lon = Double.parseDouble(longitude);
					
			String uriBegin = "geo:" + lat + "," + lon;
			String uriString = uriBegin + "?q=" + address;
			Uri uri = Uri.parse(uriString);
			finish();
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
		else if(v == btnAddContact){
			finish();
			Intent intentInsertEdit = new Intent(Intent.ACTION_INSERT_OR_EDIT);
		    intentInsertEdit.setType(Contacts.CONTENT_ITEM_TYPE);
			intentInsertEdit.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
			startActivity(intentInsertEdit);
		}
		else if(v == btnCancel) {
			finish();
		}
		else if(v == btnCopy) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(address);
			finish();
		}
	}

	
}
