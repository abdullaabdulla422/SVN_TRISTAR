package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class SubmitFinalStatus extends SoapUtils {

	public String TAG = SubmitFinalStatus.class.getSimpleName();

	public static String TAG_AddressFormattedForDisplay = "AddressFormattedForDisplay";
	public static String TAG_sessionID = "sessionID";
	public static String TAG_WorkorderD = "Workorder";
	public static String TAG_AddressLineitem = "AddressLineitem";
	public static String TAG_ServerCode = "ServerCode";
	public static String TAG_Servee = "Servee";
	public static String TAG_AuthorizedAgent = "AuthorizedAgent";
	public static String TAG_AuthorizedAgentTitle = "AuthorizedAgentTitle";
	public static String TAG_LeftWith = "LeftWith";

	public static String TAG_Relationship = "Relationship";
	public static String TAG_ServeDate = "ServeDate";
	public static String TAG_ServeTime = "ServeTime";
	public static String TAG_Age = "Age";
	public static String TAG_Height = "Height";
	public static String TAG_Weight = "Weight";
	public static String TAG_Skin = "Skin";

	public static String TAG_Hair = "Hair";
	public static String TAG_Eyes = "Eyes";
	public static String TAG_Marks = "Marks";
	public static String TAG_DateTimeSubmitted = "DateTimeSubmitted";
	public static String TAG_AdditionalMarks = "AdditionalMarks";
	public static String TAG_MannerOfServiceCode = "MannerOfServiceCode";
	public static String TAG_Report = "Report";

	public static String TAG_Entity = "Entity";
	public static String TAG_ServerisMale = "ServerisMale";
	public static String TAG_InUniform = "InUniform";
	public static String TAG_Military = "Military";
	public static String TAG_Police = "Police";
	public static String TAG_LatitudeLongitude = "LatitudeLongitude";

	private String sessionID;
	private String workOrder;
	private String addressFormattedForDisplay;
	private int addressLineitem;
	private String serverCode;
	private String servee;

	private String authorizedAgent;
	private String authorizedAgentTitle;
	private String leftWith;
	private String relationship;

	private String serveDate;
	private String serveTime;
	private String age;
	private String height;

	private String weight;
	private String skin;
	private String hair;
	private String eyes;
	private String marks;
	private String dateTimeSubmitted;
	private String additionalMarks;
	private String mannerOfServiceCode;

	private String report;
	private Boolean entity;
	private Boolean serverisMale;
	private Boolean inUniform;

	private Boolean military;
	private Boolean police;
	private String latitudeLongitude;
	private String latitude;
	private String longitude;

	private int FinalStatusLineItem;

	public int getFinalStatusLineItem() {
		return FinalStatusLineItem;
	}

	public void setFinalStatusLineItem(int finalStatusLineItem) {
		FinalStatusLineItem = finalStatusLineItem;
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

	public String getsessionID() {
		return sessionID;
	}

	public String getWorkorder() {
		return workOrder;
	}

	public int getAddressLineitem() {
		return addressLineitem;
	}

	public String getServerCode() {
		return serverCode;
	}

	public String getServee() {
		return servee;
	}

	public String getAuthorizedAgent() {
		return authorizedAgent;
	}

	public String getAuthorizedAgentTitle() {
		return authorizedAgentTitle;
	}

	public String getLeftWith() {
		return leftWith;
	}

	public String getRelationship() {
		return relationship;
	}

	public String getServeDate() {
		return serveDate;
	}

	public String getserveTime() {
		return serveTime;
	}

	public String getAge() {
		return age;
	}

	public String getHeight() {
		return height;
	}

	public String getWeight() {
		return weight;
	}

	public String getSkin() {
		return skin;
	}

	public String getHair() {
		return hair;
	}

	public String getEyes() {
		return eyes;
	}

	public String getMarks() {
		return marks;
	}

	public String getDateTimeSubmitted() {
		return dateTimeSubmitted;
	}

	public String getAdditionalMarks() {
		return additionalMarks;
	}

	public String getMannerOfServiceCode() {
		return mannerOfServiceCode;
	}

	public String getReport() {
		return report;
	}

	public Boolean getEntity() {
		return entity;
	}

	public Boolean getServerisMale() {
		return serverisMale;
	}

	public Boolean getInUniform() {
		return inUniform;
	}

	public Boolean getmilitary() {
		return military;
	}

	public Boolean getPolice() {
		return police;
	}

	public String getlatitudeLongitude() {
		return latitudeLongitude;
	}

	public String getAddressFormattedForDisplay() {
		return addressFormattedForDisplay;
	}

	public void setAddressFormattedForDisplay(String addressFormattedForDisplay) {
		this.addressFormattedForDisplay = addressFormattedForDisplay;
	}

	public void setsessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setWorkorder(String workOrder) {
		this.workOrder = workOrder;
	}

	public void setAddressLineitem(int addressLineitem) {
		this.addressLineitem = addressLineitem;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public void setServee(String servee) {
		this.servee = servee;
	}

	public void setAuthorizedAgent(String authorizedAgent) {
		this.authorizedAgent = authorizedAgent;
	}

	public void setAuthorizedAgentTitle(String authorizedAgentTitle) {
		this.authorizedAgentTitle = authorizedAgentTitle;
	}

	public void setLeftWith(String leftWith) {
		this.leftWith = leftWith;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public void setServeDate(String serveDate) {
		this.serveDate = serveDate;
	}

	public void setserveTime(String serveTime) {
		this.serveTime = serveTime;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void setSkin(String skin) {
		this.skin = skin;

	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public void setEyes(String eyes) {
		this.eyes = eyes;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public void setDateTimeSubmitted(String dateTimeSubmitted) {
		this.dateTimeSubmitted = dateTimeSubmitted;
	}

	public void setAdditionalMarks(String additionalMarks) {
		this.additionalMarks = additionalMarks;
	}

	public void setMannerOfServiceCode(String mannerOfServiceCode) {
		this.mannerOfServiceCode = mannerOfServiceCode;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setEntity(Boolean entity) {
		this.entity = entity;
	}

	public void setServerisMale(Boolean serverisMale) {
		this.serverisMale = serverisMale;
		Log.d("The Male of female", Boolean.toString(serverisMale));
	}

	public void setInUniform(Boolean inUniform) {
		this.inUniform = inUniform;
	}

	public void setmilitary(Boolean military) {
		this.military = military;
	}

	public void setPolice(Boolean police) {
		this.police = police;
	}

	public void setlatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
		if (latitudeLongitude != null) {
			String[] splitArray = latitudeLongitude.split(";");
			if (splitArray.length == 2) {
				setLatitude(splitArray[0]);
				setLongitude(splitArray[1]);
			}
		}
	}

	public static SubmitFinalStatus parseObject(SoapObject soapObject) {
		SubmitFinalStatus object = new SubmitFinalStatus();

		object.setAddressFormattedForDisplay(getProperty(soapObject,
				TAG_AddressFormattedForDisplay));
		object.setsessionID(getProperty(soapObject, TAG_sessionID));
		object.setAddressLineitem(getPropertyAsInt(soapObject,
				TAG_AddressLineitem));
		object.setServerCode(getProperty(soapObject, TAG_ServerCode));
		object.setServee(getProperty(soapObject, TAG_Servee));

		object.setAuthorizedAgent(getProperty(soapObject, TAG_AuthorizedAgent));
		object.setAuthorizedAgentTitle(getProperty(soapObject,
				TAG_AuthorizedAgentTitle));
		object.setLeftWith(getProperty(soapObject, TAG_LeftWith));

		object.setRelationship(getProperty(soapObject, TAG_Relationship));
		object.setServeDate(getProperty(soapObject, TAG_ServeDate));
		object.setserveTime(getProperty(soapObject, TAG_ServeTime));
		object.setAge(getProperty(soapObject, TAG_Age));
		object.setHeight(getProperty(soapObject, TAG_Height));

		object.setWeight(getProperty(soapObject, TAG_Weight));
		object.setSkin(getProperty(soapObject, TAG_Skin));
		object.setHair(getProperty(soapObject, TAG_Hair));

		object.setEyes(getProperty(soapObject, TAG_Eyes));
		object.setMarks(getProperty(soapObject, TAG_Marks));
		object.setDateTimeSubmitted(getProperty(soapObject,
				TAG_DateTimeSubmitted));
		object.setAdditionalMarks(getProperty(soapObject, TAG_AdditionalMarks));
		object.setMannerOfServiceCode(getProperty(soapObject,
				TAG_MannerOfServiceCode));

		object.setReport(getProperty(soapObject, TAG_Report));
		object.setEntity(getPropertyAsBoolean(soapObject, TAG_Entity));
		object.setServerisMale(getPropertyAsBoolean(soapObject,
				TAG_ServerisMale));
		object.setInUniform(getPropertyAsBoolean(soapObject, TAG_InUniform));

		object.setmilitary(getPropertyAsBoolean(soapObject, TAG_Military));
		object.setPolice(getPropertyAsBoolean(soapObject, TAG_Police));
		object.setlatitudeLongitude(getProperty(soapObject,
				TAG_LatitudeLongitude));
		return object;
	}

}
