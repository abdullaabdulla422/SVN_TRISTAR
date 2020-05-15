package com.tristar.webutils;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class SoapUtils {
	protected static String
	getProperty(SoapObject soapObject, String tag) {
		try {
			if(soapObject.getProperty(tag) instanceof SoapPrimitive)
				return soapObject.getPropertyAsString(tag).trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	protected static boolean getPropertyAsBoolean(SoapObject soapObject, String tag) {
		tag = getProperty(soapObject, tag);
		return (tag.equals("1") || tag.equals("true"));
	}
	
	protected static int getPropertyAsInt(SoapObject soapObject, String tag) {
		tag = getProperty(soapObject, tag);
		try {
			return Integer.parseInt(tag.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
