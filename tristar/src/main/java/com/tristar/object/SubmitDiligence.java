package com.tristar.object;

import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

@SuppressWarnings("ALL")
public class SubmitDiligence extends SoapUtils {
	public static String TAG = SubmitDiligence.class.getSimpleName();

	public static String TAG_LINEITEM = "Lineitem";
	public static String TAG_WORKDER = "Workorder";
	public static String TAG_ADDRESS_LINE_ITEM = "AddressLineitem";
	public static String TAG_REPORT = "Report";
	public static String TAG_DILIGENCE_DATE = "DiligenceDate";
	public static String TAG_DILIGENCE_TIME = "DiligenceTime";
	public static String TAG_SERVER_CODE = "ServerCode";
	public static String TAG_DILIGENCE_CODE = "DiligenceCode";
	public static String TAG_LATITUDE = "LatitudeLongitude";
	public static String TAG_LONGITUDE = "longitude";
	public static String TAG_DATE_SUBMITTED = "DateTimeSubmitted";
	public static String TAG_SYNC = "isSync";
	public static String TAG_ATTACH_FILE_NAME = "FileName";
	public static String TAG_ATTACH_BASE_STRING = "PDFInMemory";
	public static String TAG_ATTACH_URL = "attachementOfUrlString";


	private int lineItem;
	private String workorder;
	private int addressLineItem;
	private String report;
	private String diligenceDate;
	private String diligenceTime;
	private String serverCode = "";
	private int diligenceCode = 0;
	private String latitude;
	private String longitude;
	private String dateTimeSubmitted;
	private boolean isSync;
	private String attachementsFileName;
	private String attachementBase64String;
	private String attachementOfImage;
	private String attachementOfUrlString;
	private int PROCCESS_ID = 0;

	public static SubmitDiligence parseObject(SoapObject soapObject) {
		SubmitDiligence object = new SubmitDiligence();
		object.setAddressLineItem(getPropertyAsInt(soapObject,
				TAG_ADDRESS_LINE_ITEM));

		object.setDiligenceCode(getPropertyAsInt(soapObject, TAG_DILIGENCE_CODE));
		object.setAttachementBase64String(getProperty(soapObject,
				TAG_ATTACH_BASE_STRING));
		object.setDiligenceTime(getProperty(soapObject, TAG_DILIGENCE_TIME));
		object.setReport(getProperty(soapObject, TAG_REPORT));
		object.setWorkorder(getProperty(soapObject, TAG_WORKDER));

		return object;
	}


	public int getPROCCESS_ID() {
		return PROCCESS_ID;
	}

	public void setPROCCESS_ID(int PROCCESS_ID) {
		this.PROCCESS_ID = PROCCESS_ID;
	}

	public String getAttachementBase64String() {
		return attachementBase64String;
	}

	public void setAttachementBase64String(String attachementBase64String) {
		this.attachementBase64String = attachementBase64String;
	}

	public String getAttachementOfImage() {
		return attachementOfImage;
	}

	public void setAttachementOfImage(String attachementOfImage) {
		this.attachementOfImage = attachementOfImage;
	}

	public String getAttachementOfUrlString() {
		return attachementOfUrlString;
	}

	public void setAttachementOfUrlString(String attachementOfUrlString) {
		this.attachementOfUrlString = attachementOfUrlString;
	}

	public String getAttachementsFileName() {
		return attachementsFileName;
	}

	public void setAttachementsFileName(String attachementsFileName) {
		this.attachementsFileName = attachementsFileName;
	}

	public int getLineItem() {
		return lineItem;
	}

	public void setLineItem(int lineItem) {
		this.lineItem = lineItem;
	}

	public String getWorkorder() {
		return workorder;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public int getAddressLineItem() {
		return addressLineItem;
	}

	public void setAddressLineItem(int addressLineItem) {
		this.addressLineItem = addressLineItem;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getDiligenceDate() {
		return diligenceDate;
	}

	public void setDiligenceDate(String diligenceDate) {
		this.diligenceDate = diligenceDate;
	}

	public String getDiligenceTime() {
		return diligenceTime;
	}

	public void setDiligenceTime(String diligenceTime) {
		this.diligenceTime = diligenceTime;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public int getDiligenceCode() {
		return diligenceCode;
	}

	public void setDiligenceCode(int diligenceCode) {
		this.diligenceCode = diligenceCode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDateTimeSubmitted() {
		return dateTimeSubmitted;
	}

	public void setDateTimeSubmitted(String dateTimeSubmitted) {
		this.dateTimeSubmitted = dateTimeSubmitted;
	}

	public boolean isSync() {
		return isSync;
	}

	public void setSync(boolean isSync) {
		this.isSync = isSync;
	}

}
