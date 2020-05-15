package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class Tristar extends SoapUtils {
	public static String TAG = Tristar.class.getSimpleName();

	public static String TAG_Code = "Code";
	public static String TAG_Title = "Title";

	private String code;
	private String title;

	public String getCode() {

		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static Tristar parseObject(SoapObject soapObject) {
		Tristar object = new Tristar();

		object.setCode(getProperty(soapObject, TAG_Code));
		object.setTitle(getProperty(soapObject, TAG_Title));
		return object;
	}
}
