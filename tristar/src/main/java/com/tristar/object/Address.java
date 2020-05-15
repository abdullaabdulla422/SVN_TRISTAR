package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class Address extends SoapUtils {
	public static String TAG = Address.class.getSimpleName();
	public static String TAG_Workorder = "Workorder";
	public static String TAG_LineItem = "LineItem";

	public static String TAG_Street = "Street";
	public static String TAG_Suite = "Suite";
	public static String TAG_City = "City";
	public static String TAG_State = "State";
	public static String TAG_Zip = "Zip";
	public static String TAG_theAddressType = "theAddressType";
	

	private String workorder;
	private int LineItem;
	
	private String street;
	private String suite;
	private String city;
	private String state;
	private String zip;
	private int theAddressType;
	

	public String getWorkorder() {
		return workorder;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public int getLineItem() {
		return LineItem;
	}

	public void setLineItem(int lineItem) {
		LineItem = lineItem;
	}

	

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = ""+street;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = ""+city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = ""+state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = ""+zip;
	}

	

	public static Address parseObject(SoapObject soapObject) {
		Address object = new Address();
		object.setWorkorder(getProperty(soapObject, TAG_Workorder));
		object.setLineItem(getPropertyAsInt(soapObject, TAG_LineItem));
		
		object.setStreet(getProperty(soapObject, TAG_Street));
	
		object.setSuite(getProperty(soapObject, TAG_Suite));
		object.setCity(getProperty(soapObject, TAG_City));
		object.setState(getProperty(soapObject, TAG_State));
		object.setZip(getProperty(soapObject, TAG_Zip));
		object.setTheAddressType(getPropertyAsInt(soapObject,TAG_theAddressType ));
	

		
		Log.d("SubmitAddresstype", "" + object);

		return object;
	}

	public int getTheAddressType() {
		return theAddressType;
	}

	public void setTheAddressType(int theAddressType) {
		this.theAddressType = theAddressType;
	}	

}