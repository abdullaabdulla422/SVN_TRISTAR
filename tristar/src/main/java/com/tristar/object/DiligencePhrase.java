package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class DiligencePhrase extends SoapUtils {
	public static String TAG = DiligencePhrase.class.getSimpleName();

	public static String TAG_Code = "Code";
	public static String TAG_Title = "Title";
	public static String TAG_PhoneTitle = "PhoneTitle";
	public static String TAG_Options = "Options";

	private int code;
	private String title;
	private String phoneTitle;
	private Options options;

	public int getCode() {
		return code;
	}

	public Options getOptions() {
		return options;
	}

	public String getPhoneTitle() {
		return phoneTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public void setPhoneTitle(String phoneTitle) {
		this.phoneTitle = phoneTitle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static DiligencePhrase parseObject(SoapObject soapObject)
			throws Exception {
		DiligencePhrase object = new DiligencePhrase();
		object.setCode(getPropertyAsInt(soapObject, TAG_Code));
		object.setOptions(Options.parseObject((SoapObject) soapObject
				.getProperty(TAG_Options)));
		object.setPhoneTitle(getProperty(soapObject, TAG_PhoneTitle));
		object.setTitle(getProperty(soapObject, TAG_Title));
		Log.d("Title", "" + getProperty(soapObject, TAG_Title));
		return object;
	}

}
