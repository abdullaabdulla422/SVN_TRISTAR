package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class ReturnRouteTasksObject extends SoapUtils {

	public String TAG = ReturnRouteTasksObject.class.getSimpleName();
	public static String TAG_Code = "Code";
	public static String TAG_Title = "Title";

	private String Code;
	private String Title;

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public static ReturnRouteTasksObject parseObject(SoapObject soapObject) {
		ReturnRouteTasksObject object = new ReturnRouteTasksObject();
		object.setCode(getProperty(soapObject, TAG_Code));
		object.setTitle(getProperty(soapObject, TAG_Title));
		return object;
	}

}
