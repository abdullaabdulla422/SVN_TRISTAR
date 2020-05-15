package com.tristar.object;

import android.util.Log;

import com.tristar.utils.SessionData;
import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

//@SuppressWarnings("ALL")
public class CourtAddressForServer extends SoapUtils {
	public static String TAG_Workorder = "Workorder";
	public static String TAG_AddressFormattedForDisplay = "AddressFormattedForDisplay";
	public static String TAG_Documents = "Documents";
	public static String TAG_Instructions = "Instructions";
	public static String TAG_FileAndServe = "FileAndServe";
	public static String TAG_FileOnly = "FileOnly";
	public static String TAG_Conform = "Conform";
	public static String TAG_Issue = "Issue";
	public static String TAG_Record = "Record";
	public static String TAG_Copy = "Copy";
	public static String TAG_Certify = "Certify";
	public static String TAG_DeliverCourtesyCopy = "DeliverCourtesyCopy";
	public static String TAG_PriorityTitle = "PriorityTitle";
	public static String TAG_DueDate = "DueDate";
	public static String TAG_Name = "Name";
	public static String TAG_LatitudeLongitude = "LatitudeLongitude";
	public static String TAG_CaseName = "CaseName";
	public static String TAG_CaseNumber = "CaseNumber";
	public static String TAG_ServeeName = "CustomerName";
	public static String TAG_DateReceived = "DateReceived";
	public static String TAG_TimeReceived = "TimeReceived";
	public static String TAG_Contact = "Contact";
	public static String TAG_ContactPhone = "ContactPhone";
	public static String TAG_HasAttachments = "HasAttachments";
	public static String TAG_MilestoneCode = "MilestoneCode";
	public static String TAG_MilestoneTitle = "MilestoneTitle";
	public static String TAG_CustAddressFormattedForDisplay = "CustAddressFormattedForDisplay";
	public static String TAG_CustAddressFormattedForGoogle = "CustAddressFormattedForGoogle";
	public static String TAG_AddressFormattedNewLine1 = "AddressFormattedNewLine1";
	public static String TAG_AddressFormattedNewLine2 = "AddressFormattedNewLine2";
	public String TAG = CourtAddressForServer.class.getSimpleName();
	private String ServeeName;
	private int courtId;
	private int addresslineitem;

	private String workorder;
	private String addressFormattedForDisplay;
	private String documents;
	private String instructions;
	private String CaseName;
	private String CaseNumber;


	private int CourtOpenAddressID;

	private boolean fileAndServe;
	private boolean fileOnly;
	private boolean conform;
	private boolean issue;

	private String comments;

	private boolean record;
	private boolean copy;
	private boolean certify;
	private boolean deliverCourtesyCopy;

	private String priorityTitle;
	private String dueDate;
	private String name;
	private String latitudeLongitude;

	private String latitude;
	private String longitude;

//	"Date, Time, FeeAdvance, Weight, Pieces, WaitTime, Latitude, Longtitude

	private String Date;
	private String Time;
	private String FeeAdvance;
	private String Weight;
	private String Pieces;
	private String WaitTime;
	private String lat;
	private String lng;


	private String DateReceived;
	private String TimeReceived;
	private String Contact;
	private String ContactPhone;
	private int MilestoneCode;
	private String MilestoneTitle;
	private String CustAddressFormattedForDisplay;
	private String CustAddressFormattedForGoogle;
	private String AddressFormattedNewLine1 = "";
	private String AddressFormattedNewLine2 = "";
	private boolean HasAttachments;

	public static CourtAddressForServer parseObject(SoapObject soapObject) {
		CourtAddressForServer object = new CourtAddressForServer();
		object.setAddressFormattedForDisplay(getProperty(soapObject,
				TAG_AddressFormattedForDisplay));
		object.setCertify(getPropertyAsBoolean(soapObject, TAG_Certify));
		object.setConform(getPropertyAsBoolean(soapObject, TAG_Conform));
		object.setCopy(getPropertyAsBoolean(soapObject, TAG_Copy));

		object.setDeliverCourtesyCopy(getPropertyAsBoolean(soapObject,
				TAG_DeliverCourtesyCopy));
		object.setDocuments(getProperty(soapObject, TAG_Documents));
		object.setDueDate(getProperty(soapObject, TAG_DueDate));
		object.setFileAndServe(getPropertyAsBoolean(soapObject,
				TAG_FileAndServe));

		object.setFileOnly(getPropertyAsBoolean(soapObject, TAG_FileOnly));
		object.setInstructions(getProperty(soapObject, TAG_Instructions));
		object.setIssue(getPropertyAsBoolean(soapObject, TAG_Issue));
		object.setLatitudeLongitude(getProperty(soapObject,
				TAG_LatitudeLongitude));

		object.setName(getProperty(soapObject, TAG_Name));
		object.setPriorityTitle(getProperty(soapObject, TAG_PriorityTitle));
		object.setRecord(getPropertyAsBoolean(soapObject, TAG_Record));
		object.setWorkorder(getProperty(soapObject, TAG_Workorder));
		object.setCaseName(getProperty(soapObject, TAG_CaseName));
		object.setCaseNumber(getProperty(soapObject, TAG_CaseNumber));

		object.setServeeName(getProperty(soapObject, TAG_ServeeName));

		object.setDateReceived(getProperty(soapObject, TAG_DateReceived));
		object.setTimeReceived(getProperty(soapObject, TAG_TimeReceived));
		object.setContact(getProperty(soapObject, TAG_Contact));
		object.setContactPhone(getProperty(soapObject, TAG_ContactPhone));
		object.setHasAttachments(getPropertyAsBoolean(soapObject, TAG_HasAttachments));
		object.setMilestoneCode(getPropertyAsInt(soapObject, TAG_MilestoneCode));
		object.setMilestoneTitle(getProperty(soapObject, TAG_MilestoneTitle));
		object.setCustAddressFormattedForDisplay(getProperty(soapObject, TAG_CustAddressFormattedForDisplay));
		object.setCustAddressFormattedForGoogle(getProperty(soapObject, TAG_CustAddressFormattedForGoogle));
		if(SessionData.getInstance().getCourtjob_Address()==0){
			object.setAddressFormattedNewLine1(getProperty(soapObject, TAG_AddressFormattedNewLine1));
			object.setAddressFormattedNewLine2(getProperty(soapObject, TAG_AddressFormattedNewLine2));
		}


		return object;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getCourtId() {
		return courtId;
	}

	public void setCourtId(int courtId) {
		this.courtId = courtId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddressFormattedForDisplay() {
		return addressFormattedForDisplay;
	}

	public void setAddressFormattedForDisplay(String addressFormattedForDisplay) {
		this.addressFormattedForDisplay = addressFormattedForDisplay;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
		if (latitudeLongitude != null) {
			String[] splitArray = latitudeLongitude.split(";");
			if (splitArray.length == 2) {
				setLatitude(splitArray[0]);
				setLongitude(splitArray[1]);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriorityTitle() {
		return priorityTitle;
	}

	public void setPriorityTitle(String priorityTitle) {
		this.priorityTitle = priorityTitle;
	}

	public String getWorkorder() {
		return workorder;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public boolean isCertify() {
		return certify;
	}

	public void setCertify(boolean certify) {
		this.certify = certify;
	}

	public boolean isConform() {
		return conform;
	}

	public void setConform(boolean conform) {
		this.conform = conform;
	}

	public boolean isCopy() {
		return copy;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public boolean isDeliverCourtesyCopy() {
		return deliverCourtesyCopy;
	}

	public void setDeliverCourtesyCopy(boolean deliverCourtesyCopy) {
		this.deliverCourtesyCopy = deliverCourtesyCopy;
	}

	public boolean isFileAndServe() {
		return fileAndServe;
	}

	public void setFileAndServe(boolean fileAndServe) {
		this.fileAndServe = fileAndServe;
	}

	public boolean isFileOnly() {
		return fileOnly;
	}

	public void setFileOnly(boolean fileOnly) {
		this.fileOnly = fileOnly;
	}

	public boolean isIssue() {
		return issue;
	}

	public void setIssue(boolean issue) {
		this.issue = issue;
	}

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public int getCourtOpenAddressID() {
		return CourtOpenAddressID;
	}

	public void setCourtOpenAddressID(int courtOpenAddressID) {
		this.CourtOpenAddressID = courtOpenAddressID;
	}

	public int getAddresslineitem() {
		return addresslineitem;
	}

	public void setAddresslineitem(int addresslineitem) {
		this.addresslineitem = addresslineitem;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getFeeAdvance() {
		return FeeAdvance;
	}

	public void setFeeAdvance(String feeAdvance) {
		FeeAdvance = feeAdvance;
	}

	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}

	public String getPieces() {
		return Pieces;
	}

	public void setPieces(String pieces) {
		Pieces = pieces;
	}

	public String getWaitTime() {
		return WaitTime;
	}

	public void setWaitTime(String waitTime) {
		WaitTime = waitTime;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getCaseName() {
		return CaseName;
	}

	public void setCaseName(String caseName) {
		CaseName = caseName;
	}

	public String getCaseNumber() {
		return CaseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		CaseNumber = caseNumber;
	}

	public String getServeeName() {
		return ServeeName;
	}

	public void setServeeName(String serveeName) {
		ServeeName = serveeName;
	}

	public String getDateReceived() {
		return DateReceived;
	}

	public void setDateReceived(String dateReceived) {
		DateReceived = dateReceived;
	}

	public String getTimeReceived() {
		return TimeReceived;
	}

	public void setTimeReceived(String timeReceived) {
		TimeReceived = timeReceived;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public String getContactPhone() {
		return ContactPhone;
	}

	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}

	public boolean isHasAttachments() {
		return HasAttachments;
	}

	public void setHasAttachments(boolean hasAttachments) {
		HasAttachments = hasAttachments;
	}

	public int getMilestoneCode() {
		return MilestoneCode;
	}

	public void setMilestoneCode(int milestoneCode) {
		MilestoneCode = milestoneCode;
	}

	public String getMilestoneTitle() {
		return MilestoneTitle;
	}

	public void setMilestoneTitle(String milestoneTitle) {
		MilestoneTitle = milestoneTitle;
	}

	public String getCustAddressFormattedForDisplay() {
		return CustAddressFormattedForDisplay;
	}

	public void setCustAddressFormattedForDisplay(String custAddressFormattedForDisplay) {
		CustAddressFormattedForDisplay = custAddressFormattedForDisplay;
	}

	public String getCustAddressFormattedForGoogle() {
		return CustAddressFormattedForGoogle;
	}

	public void setCustAddressFormattedForGoogle(String custAddressFormattedForGoogle) {
		CustAddressFormattedForGoogle = custAddressFormattedForGoogle;
	}

	public String getAddressFormattedNewLine1() {
		return AddressFormattedNewLine1;
	}

	public void setAddressFormattedNewLine1(String addressFormattedNewLine1) {
		AddressFormattedNewLine1 = addressFormattedNewLine1;
	}

	public String getAddressFormattedNewLine2() {
		return AddressFormattedNewLine2;
	}

	public void setAddressFormattedNewLine2(String addressFormattedNewLine2) {
		AddressFormattedNewLine2 = addressFormattedNewLine2;
	}
}
