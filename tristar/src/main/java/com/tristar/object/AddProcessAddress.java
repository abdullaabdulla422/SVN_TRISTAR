package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class AddProcessAddress extends SoapUtils {
	public String TAG = AddProcessAddress.class.getSimpleName();
	public static String TAG_street = "Street";
	public static String TAG_city = "City";
	public static String TAG_state = "State";
	public static String TAG_zip = "Zip";
	public static String TAG_SessionID = "sessionID";
	public static String TAG_workOrder = "Workorder";
	public static String TAG_lineItem = "LineItem";
	public static String TAG_BussinessName = "BusinessName";
	public static String TAG_bussinessHours = "BusinessHours";
	public static String TAG_suite = "Suite";
	public static String TAG_phone = "Phone";
	public static String TAG_StatusDatedueby = "StatusDateDueBy";
	public static String TAG_Instructions = "Instructions";
	public static String TAG_Servercode = "ServerCode";
	public static String TAG_Statustimedueby = "StatusTimeDueBy";
	public static String TAG_County = "County";
	public static String TAG_LatitudeLangitude = "LatitudeLongitude";
	public static String TAG_theAddressType = "theAddressType";

	private String street;
	private String city;
	private String state;
	private String zip;
	private String sessionID;
	private String workOrder;
	private int LineItem;
	private int theAddresstype;
	private String serverCode;
	private boolean isSync;
	

	public static AddProcessAddress parseObject(SoapObject soapObject) {
		AddProcessAddress object = new AddProcessAddress();
		object.setStreet(getProperty(soapObject, TAG_street));
		object.setCity(getProperty(soapObject, TAG_city));
		object.setState(getProperty(soapObject, TAG_state));
		object.setZip(getProperty(soapObject, TAG_zip));
		object.setSessionID(getProperty(soapObject, TAG_SessionID));
		object.setWorkOrder(getProperty(soapObject, TAG_workOrder));
		object.setLineItem(getPropertyAsInt(soapObject, TAG_lineItem));
		object.setTheAddresstype(getPropertyAsInt(soapObject, TAG_theAddressType));
		object.setServerCode(getProperty(soapObject, TAG_Servercode));
		
		return object;

	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public int getLineItem() {
		return LineItem;
	}

	public void setLineItem(int lineItem) {
		LineItem = lineItem;
	}

	public int getTheAddresstype() {
		return theAddresstype;
	}

	public void setTheAddresstype(int theAddresstype) {
		this.theAddresstype = theAddresstype;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public boolean isSync() {
		return isSync;
	}

	public void setSync(boolean isSync) {
		this.isSync = isSync;
	}
}
