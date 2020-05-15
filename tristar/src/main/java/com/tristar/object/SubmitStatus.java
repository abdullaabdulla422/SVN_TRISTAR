package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class SubmitStatus extends SoapUtils{

public static String TAG = SubmitStatus.class.getSimpleName();
	public static String TAG_WORKORDER = "Workorder";
	public static String TAG_LINEITEM = "Lineitem";
	public static String TAG_DILIGENCE_DATE = "DiligenceDate";
	public static String TAG_DILIGENCE_TIME= "DiligenceTime";
	public static String TAG_REPORT = "Report";
	public static String TAG_COMMENT = "Comment";
	
	private String Workorder;
	private int Lineitem;
	private String DiligenceDate;
	private String DiligenceTime;
	private String Report;
	private Boolean Comment;
	public String getWorkorder() {
		return Workorder;
	}
	public void setWorkorder(String workorder) {
		Workorder = workorder;
	}
	public int getLineitem() {
		return Lineitem;
	}
	public void setLineitem(int lineitem) {
		Lineitem = lineitem;
	}
	public String getDiligenceDate() {
		return DiligenceDate;
	}
	public void setDiligenceDate(String diligenceDate) {
		DiligenceDate = diligenceDate;
	}
	public String getDiligenceTime() {
		return DiligenceTime;
	}
	public void setDiligenceTime(String diligenceTime) {
		DiligenceTime = diligenceTime;
	}
	public String getReport() {
		return Report;
	}
	public void setReport(String report) {
		Report = report;
	}
	public boolean getComment() {
		return Comment;
	}
	public void setComment(boolean comment) {
		Comment = comment;
	}
	
	public static SubmitStatus parseObject(SoapObject soapObject) {
		SubmitStatus object = new SubmitStatus();
		
			object.setWorkorder(getProperty(soapObject, TAG_WORKORDER));
			object.setLineitem(getPropertyAsInt(soapObject, TAG_LINEITEM));
			object.setDiligenceDate(getProperty(soapObject, TAG_DILIGENCE_DATE));
			object.setDiligenceTime(getProperty(soapObject, TAG_DILIGENCE_TIME));
			object.setReport(getProperty(soapObject, TAG_REPORT));
			object.setComment(getPropertyAsBoolean(soapObject, TAG_COMMENT));
			return object;
		}
	
}