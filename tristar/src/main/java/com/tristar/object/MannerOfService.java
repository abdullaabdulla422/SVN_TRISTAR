package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class MannerOfService extends SoapUtils {
	public static String TAG = MannerOfService.class.getSimpleName();

	public static String TAG_State = "State";
	public static String TAG_Code = "Code";
	public static String TAG_Title = "Title";
	private String State;
	private String Code;
	private String Title;

	public String getCode() {
		return Code;
	}

	public String getState() {
		return State;
	}

	public String getTitle() {
		return Title;
	}

	public void setCode(String code) {
		Code = code;
	}

	public void setState(String state) {
		State = state;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public static MannerOfService parseObject(SoapObject soapObject)
			throws Exception {
		MannerOfService object = new MannerOfService();
		object.setCode(getProperty(soapObject, TAG_Code));
		object.setState(getProperty(soapObject, TAG_State));
		object.setTitle(getProperty(soapObject, TAG_Title));
		return object;
	}

}
