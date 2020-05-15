package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class ReturnRouteTaskStatusObject extends SoapUtils{
	
	public String TAG = ReturnRouteTaskStatusObject.class.getSimpleName();
	public static String TAG_Code = "Code";
	public static String TAG_Title = "Title";
	public static String TAG_TaskCode = "TaskCode";

	private String Code;
	private String Title;
	private String TaskCode;
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
	public String getTaskCode() {
		return TaskCode;
	}
	public void setTaskCode(String taskCode) {
		TaskCode = taskCode;
	}
	
	public static ReturnRouteTaskStatusObject parseObject(SoapObject soapObject) {
		ReturnRouteTaskStatusObject object = new ReturnRouteTaskStatusObject();
		object.setCode(getProperty(soapObject, TAG_Code));
		object.setTitle(getProperty(soapObject, TAG_Title));
		object.setTaskCode(getProperty(soapObject, TAG_TaskCode));
		return object;
	}
	
}
