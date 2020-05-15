package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class AttachedPDF extends SoapUtils {
	public static String TAG = AttachedPDF.class.getSimpleName();
	public static String TAG_SessionID = "sessionID";
	
	public static String TAG_TheWorkOrder = "theWorkorder";
	
	public static String TAG_TheLineItem = "theLineitem";
	
	
private String sessionID;
private String theWorkorder;
private int theLineItem;

public static AttachedPDF parseObject(SoapObject soapObject)
		throws Exception {
	AttachedPDF object = new AttachedPDF();
	object.setSessionID(getProperty(soapObject, TAG_SessionID));
	object.setTheWorkorder(getProperty(soapObject, TAG_TheWorkOrder));
	object.setTheLineItem(getPropertyAsInt(soapObject, TAG_TheLineItem));
	return object;
}

public String getSessionID() {
	return sessionID;
}

public void setSessionID(String sessionID) {
	this.sessionID = sessionID;
}

public String getTheWorkorder() {
	return theWorkorder;
}

public void setTheWorkorder(String theWorkorder) {
	this.theWorkorder = theWorkorder;
}

public int getTheLineItem() {
	return theLineItem;
}

public void setTheLineItem(int theLineItem) {
	this.theLineItem = theLineItem;
}
	
}