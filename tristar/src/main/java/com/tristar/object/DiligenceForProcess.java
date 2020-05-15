package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class DiligenceForProcess extends SoapUtils {
	public static String TAG = DiligenceForProcess.class.getSimpleName();
	public static String TAG_Workorder = "Workorder";
	public static String TAG_AddressLineItem = "AddressLineItem";
	public static String TAG_Report = "Report";
	public static String TAG_DiligenceDate = "DiligenceDate";
	public static String TAG_DiligenceTime = "DiligenceTime";

	private String workorder;
	private int addressLineItem;
	private String report;
	private String diligenceDate;
	private String diligenceTime;

	public int getAddressLineItem() {
		return addressLineItem;
	}

	public String getDiligenceDate() {
		return diligenceDate;
	}

	public String getDiligenceTime() {
		return diligenceTime;
	}

	public String getReport() {
		Log.d("Report form web", "" + report);
		return report;
	}

	public String getWorkorder() {
		return workorder;
	}

	public void setAddressLineItem(int addressLineItem) {
		this.addressLineItem = addressLineItem;
	}

	public void setDiligenceDate(String diligenceDate) {
		this.diligenceDate = diligenceDate;
	}

	public void setDiligenceTime(String diligenceTime) {
		this.diligenceTime = diligenceTime;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public static DiligenceForProcess parseObject(SoapObject soapObject) {
		DiligenceForProcess object = new DiligenceForProcess();
		object.setAddressLineItem(getPropertyAsInt(soapObject,
				TAG_AddressLineItem));
		object.setDiligenceDate(getProperty(soapObject, TAG_DiligenceDate));
		object.setDiligenceTime(getProperty(soapObject, TAG_DiligenceTime));
		object.setReport(getProperty(soapObject, TAG_Report));
		object.setWorkorder(getProperty(soapObject, TAG_Workorder));
		Log.d("Object values", "" + object);
		return object;
	}
}
