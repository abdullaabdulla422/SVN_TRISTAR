package com.tristar.object;

import android.util.Log;

import com.tristar.utils.SessionData;
import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

public class ProcessAddressForServer extends SoapUtils {
	public static String TAG = ProcessAddressForServer.class.getSimpleName();

	public static String TAG_Workorder = "Workorder";
	public static String TAG_AddressLineItem = "AddressLineItem";
	public static String TAG_Servee = "Servee";
	public static String TAG_AddressFormattedForDisplay = "AddressFormattedForDisplay";
	public static String TAG_AddressFormattedForGoogle = "AddressFormattedForGoogle";
	public static String TAG_DueDate = "DueDate";
	public static String TAG_CourtStateCode = "CourtStateCode";
	public static String TAG_PriorityTitle = "PriorityTitle";
	public static String TAG_Phone = "Phone";
	public static String TAG_Entity = "Entity";
	public static String TAG_AuthorizedAgent = "AuthorizedAgent";
	public static String TAG_AgentForServiceRelationShipToServee = "AgentForServiceRelationShipToServee";
	public static String TAG_ServeeIsMale = "ServeeIsMale";
	public static String TAG_Age = "Age";
	public static String TAG_Height = "Height";
	public static String TAG_Weight = "Weight";
	public static String TAG_Skin = "Skin";
	public static String TAG_Hair = "Hair";
	public static String TAG_Eyes = "Eyes";
	public static String TAG_Marks = "Marks";
	public static String TAG_LatitudeLongitude = "LatitudeLongitude";
	public static String TAG_Servercode = "Servercode";
	public static String TAG_DateReceived = "DateReceived";
	public static String TAG_TimeReceived = "TimeReceived";
	public static String TAG_HasAttachments = "HasAttachments";
	public static String TAG_MilestoneCode = "MilestoneCode";
	public static String TAG_MilestoneTitle = "MilestoneTitle";
	public static String TAG_CustAddressFormattedForDisplay = "CustAddressFormattedForDisplay";
	public static String TAG_CustAddressFormattedForGoogle = "CustAddressFormattedForGoogle";
	public static String TAG_ADDRESSFORMATTEDNEWLINE1 = "AddressFormattedNewLine1";
	public static String TAG_ADDRESSFORMATTEDNEWLINE2 = "AddressFormattedNewLine2";
	public static String TAG_DISPATCHER_EMAIL = "DispatcherEmail";


	private int processOrderID;
	private int intdata;
	private String workorder;
	private int addressLineItem;
	private String servee;
	private String addressFormattedForDisplay;

	private String addressFormattedForGoogle;
	private String dueDate;
	private String courtStateCode;
	private String priorityTitle;
	private String serverCode;

	private boolean entity;
	private String authorizedAgent;
	private String authorizedAgentTitle;
	private String agentForServiceRelationShipToServee;
	private boolean serveeIsMale;

	private String age;
	private String phone;
	private String height;
	private String weight;
	private String skin;

	private String date;
	private String time;

	private String leftWith;

	private String hair;
	private String eyes;
	private String marks;
	private String latitudeLongitude;

	private String latitude = "0.0";
	private String longitude = "0.0";


	private String serveDate;
	private String serveTime;
	private String additinalMarks;
	private String mannerofServicecode;
	private String report;


	private boolean isInuniform;
	private boolean isMilitary;
	private boolean isPolice;
	private String dateTimeSubmitted;
	private String relation;

	private boolean selected = false;

	private String DateReceived;
	private String TimeReceived;
	private boolean HasAttachments;

	private int lineItem;
	private int MilestoneCode;

	private String MilestoneTitle;
	private String CustAddressFormattedForDisplay;
	private String CustAddressFormattedForGoogle;
	private String AddressFormattedNewLine1 = "";
	private String AddressFormattedNewLine2 = "";
	private String DispatcherEmail = "";

	private long id;

	public static ProcessAddressForServer parseObject(SoapObject soapObject)
			throws Exception {
		ProcessAddressForServer object = new ProcessAddressForServer();
		object.setAddressFormattedForDisplay(getProperty(soapObject, TAG_AddressFormattedForDisplay));
		object.setAddressFormattedForGoogle(getProperty(soapObject, TAG_AddressFormattedForGoogle));
		object.setAddressLineItem(getPropertyAsInt(soapObject, TAG_AddressLineItem));
		object.setAge(getProperty(soapObject, TAG_Age));

		object.setAgentForServiceRelationShipToServee(getProperty(soapObject, TAG_AgentForServiceRelationShipToServee));
		object.setAuthorizedAgent(getProperty(soapObject, TAG_AuthorizedAgent));
		object.setCourtStateCode(getProperty(soapObject, TAG_CourtStateCode));
		object.setDueDate(getProperty(soapObject, TAG_DueDate));

		object.setEntity(getPropertyAsBoolean(soapObject, TAG_Entity));
		object.setEyes(getProperty(soapObject, TAG_Eyes));
		object.setHair(getProperty(soapObject, TAG_Hair));
		object.setHeight(getProperty(soapObject, TAG_Height));
		object.setPhone(getProperty(soapObject, TAG_Phone));
		Log.d("Phone", "" + getProperty(soapObject, TAG_Phone) + getProperty(soapObject, TAG_Workorder));

		object.setLatitudeLongitude(getProperty(soapObject, TAG_LatitudeLongitude));
		object.setMarks(getProperty(soapObject, TAG_Marks));
		object.setPriorityTitle(getProperty(soapObject, TAG_PriorityTitle));
		object.setServee(getProperty(soapObject, TAG_Servee));

		object.setServeeIsMale(getPropertyAsBoolean(soapObject, TAG_ServeeIsMale));
		object.setSkin(getProperty(soapObject, TAG_Skin));
		object.setWeight(getProperty(soapObject, TAG_Weight));
		object.setWorkorder(getProperty(soapObject, TAG_Workorder));
		object.setDateReceived(getProperty(soapObject, TAG_DateReceived));
		object.setTimeReceived(getProperty(soapObject, TAG_TimeReceived));
		object.setHasAttachments(getPropertyAsBoolean(soapObject, TAG_HasAttachments));
		object.setMilestoneCode(getPropertyAsInt(soapObject, TAG_MilestoneCode));
		object.setMilestoneTitle(getProperty(soapObject,TAG_MilestoneTitle));
		object.setCustAddressFormattedForDisplay(getProperty(soapObject,TAG_CustAddressFormattedForDisplay));
		object.setCustAddressFormattedForGoogle(getProperty(soapObject,TAG_CustAddressFormattedForGoogle));
		if(SessionData.getInstance().getProcessOrderDetail_Address()==0){
			object.setAddressFormattedNewLine1(getProperty(soapObject,TAG_ADDRESSFORMATTEDNEWLINE1));
			object.setAddressFormattedNewLine2(getProperty(soapObject,TAG_ADDRESSFORMATTEDNEWLINE2));
		}

		object.setDispatcherEmail(getProperty(soapObject,TAG_DISPATCHER_EMAIL));


		return object;
	}


	public String getDispatcherEmail() {
		return DispatcherEmail;
	}

	public void setDispatcherEmail(String dispatcherEmail) {
		DispatcherEmail = dispatcherEmail;
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

	public String getDateTimeSubmitted() {
		return dateTimeSubmitted;
	}

	public void setDateTimeSubmitted(String dateTimeSubmitted) {
		this.dateTimeSubmitted = dateTimeSubmitted;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public int getLineItem() {
		return lineItem;
	}

	public void setLineItem(int lineItem) {
		this.lineItem = lineItem;
	}

	public String getServeDate() {
		return serveDate;
	}

	public void setServeDate(String serveDate) {
		this.serveDate = serveDate;
	}

	public String getServeTime() {
		return serveTime;
	}

	public void setServeTime(String serveTime) {
		this.serveTime = serveTime;
	}

	public String getAdditinalMarks() {
		return additinalMarks;
	}

	public void setAdditinalMarks(String additinalMarks) {
		this.additinalMarks = additinalMarks;
	}

	public String getMannerofServicecode() {
		return mannerofServicecode;
	}

	public void setMannerofServicecode(String mannerofServicecode) {
		this.mannerofServicecode = mannerofServicecode;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public boolean getInuniform() {
		return isInuniform;
	}

	public boolean getMilitary() {
		return isMilitary;
	}

	public boolean getPolice() {
		return isPolice;
	}

	public String getLeftWith() {
		return leftWith;
	}

	public void setLeftWith(String leftWith) {
		this.leftWith = leftWith;
	}

	public String getAuthorizedAgentTitle() {
		return authorizedAgentTitle;
	}

	public void setAuthorizedAgentTitle(String authorizedAgentTitle) {
		this.authorizedAgentTitle = authorizedAgentTitle;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public int getProcessOrderID() {
		return processOrderID;
	}

	public void setProcessOrderID(int processOrderID) {
		this.processOrderID = processOrderID;
	}

	public String getAddressFormattedForDisplay() {
		return addressFormattedForDisplay;
	}

	public void setAddressFormattedForDisplay(String addressFormattedForDisplay) {
		this.addressFormattedForDisplay = addressFormattedForDisplay;
	}

	public String getAddressFormattedForGoogle() {
		return addressFormattedForGoogle;
	}

	public void setAddressFormattedForGoogle(String addressFormattedForGoogle) {
		this.addressFormattedForGoogle = addressFormattedForGoogle;
	}

	public int getAddressLineItem() {
		return addressLineItem;
	}

	public void setAddressLineItem(int addressLineItem) {
		this.addressLineItem = addressLineItem;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAgentForServiceRelationShipToServee() {
		return agentForServiceRelationShipToServee;
	}

	public void setAgentForServiceRelationShipToServee(
			String agentForServiceRelationShipToServee) {
		this.agentForServiceRelationShipToServee = agentForServiceRelationShipToServee;
	}

	public String getAuthorizedAgent() {
		return authorizedAgent;
	}

	public void setAuthorizedAgent(String authorizedAgent) {
		this.authorizedAgent = authorizedAgent;
	}

	public String getCourtStateCode() {
		return courtStateCode;
	}

	public void setCourtStateCode(String courtStateCode) {
		this.courtStateCode = courtStateCode;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getEyes() {
		return eyes;
	}

	public void setEyes(String eyes) {
		this.eyes = eyes;
	}

	public String getHair() {
		return hair;
	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getPriorityTitle() {
		return priorityTitle;
	}

	public void setPriorityTitle(String priorityTitle) {
		this.priorityTitle = priorityTitle;
	}

	public String getServee() {
		return servee;
	}

	public void setServee(String servee) {
		this.servee = servee;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWorkorder() {
		return workorder;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public boolean isEntity() {
		return entity;
	}

	public void setEntity(boolean entity) {
		this.entity = entity;
	}

	public boolean isServeeIsMale() {
		return serveeIsMale;
	}

	public void setServeeIsMale(boolean serveeIsMale) {
		this.serveeIsMale = serveeIsMale;
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

	public boolean isInuniform() {
		return isInuniform;
	}

	public void setInuniform(boolean inuniform) {
		this.isInuniform = inuniform;
	}

	public boolean isMilitary() {
		return isMilitary;
	}

	public void setMilitary(boolean military) {
		this.isMilitary = military;
	}

	public boolean isPolice() {
		return isPolice;
	}

	public void setPolice(boolean police) {
		this.isPolice = police;
	}

	public int getIntdata() {
		return intdata;
	}

	public void setIntdata(int intdata) {
		this.intdata = intdata;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public boolean isHasAttachments() {
		return HasAttachments;
	}

	public void setHasAttachments(boolean hasAttachments) {
		HasAttachments = hasAttachments;
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
