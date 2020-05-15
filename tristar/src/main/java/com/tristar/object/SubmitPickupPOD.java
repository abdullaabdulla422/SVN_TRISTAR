package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class SubmitPickupPOD  extends SoapUtils {
	public static String TAG = SampleObject.class.getSimpleName();
	
	public static String TAG_WORKORDER = "Workorder";
	public static String TAG_ADDRESS_LINE_ITEM = "AddressLineitem";
	public static String TAG_PROOF_TYPE = "ProofType";
	public static String TAG_PROOF_DATE = "ProofDate";
	public static String TAG_PROOF_TIME = "ProofTime";
	public static String TAG_PROOF_COMMENTS= "ProofComments";
	public static String TAG_WAIT_TIME = "WaitTime";
	public static String TAG_DISTANCE = "Distance";
	public static String TAG_FEEADVANVCE = "FeeAdvance";
	public static String TAG_WEIGHT = "Weight";
	public static String TAG_PIECES = "Pieces";
	public static String TAG_RECEIVE_BY = "ReceivedBy";
	public static String TAG_LATITUDE_LONGITUDE = "LatitudeLongitude";
	private int submitPickupPODID;
	private String workorder;
	private int addressLineitem;
	private String proofType;
	private String proofDate;
	private String proofTime;
	private String proofComments;
	private double waitTime;
	private double distance;
	private double feeAdvance;
	private double weight;
	private double pieces;
	private String receivedBy;
	private String latitude;
	private String longitude;
	private String attachmentFilename;
	private String attachmentBase64String;
	private String attachmentUrlString;
	
	
	
	public int getSubmitPickupPODID() {
		return submitPickupPODID;
	}
	
	public String getWorkorder() {
		return workorder;
	}
	
	public int getAddressLineitem() {
		return addressLineitem;
	}
	
	public String getProofType() {
		return proofType;
	}
	
	public String getProofDate() {
		return proofDate;
	}
	
	public String getProofTime() {
		return proofTime;
	}
	
	public String getProofComments() {
		return proofComments;
	}
	
	public double getWaitTime() {
		return waitTime;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getFeeAdvance() {
		return feeAdvance;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getPieces() {
		return pieces;
	}
	
	public String getReceivedBy() {
		return receivedBy;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}

	public String getAttachmentFilename() {
		return attachmentFilename;
	}
	
	public String getAttachmentBase64String() {
		return attachmentBase64String;
	}
	
	public String getAttachmentUrlString() {
		return attachmentUrlString;
	}

	
	/**************** Getters ended here ****************/
	
	/**************** Setters started here ****************/
	
	public void setSubmitPickupPODID(int submitCourtPODID) {
		this.submitPickupPODID = submitCourtPODID;
	}
	
	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}
	
	public void setAddressLineitem(int addressLineitem) {
		this.addressLineitem = addressLineitem;
	}
	
	public void setProofType(String proofType) {
		this.proofType = proofType;
	}
	
	public void setProofDate(String proofDate) {
		this.proofDate = proofDate;
	}
	
	public void setProofTime(String proofTime) {
		this.proofTime = proofTime;
	}
	
	public void setProofComments(String proofComments) {
		this.proofComments = proofComments;
	}
	
	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setFeeAdvance(double feeAdvance) {
		this.feeAdvance = feeAdvance;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void setPieces(double pieces) {
		this.pieces = pieces;
	}
	
	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}
	
	public void setAttachmentBase64String(String attachmentBase64String) {
		this.attachmentBase64String = attachmentBase64String;
	}

	public void setAttachmentUrlString() {
		this.attachmentUrlString = "";
	}


	/**************** Setters ended here ****************/

	/**************** Constructor started here ****************/
	/**************** Constructor ended here ****************/

	/**************** Functions started here ****************/
	public static SubmitPickupPOD parseObject(SoapObject soapObject) throws Exception {
		SubmitPickupPOD object = new SubmitPickupPOD();
			//TODO soap parsing
		object.setWorkorder(getProperty(soapObject, TAG_WORKORDER));
		object.setAddressLineitem(getPropertyAsInt(soapObject, TAG_ADDRESS_LINE_ITEM));
		object.setProofType(getProperty(soapObject, TAG_PROOF_TYPE));
		object.setProofDate(getProperty(soapObject, TAG_PROOF_DATE));
		object.setProofTime(getProperty(soapObject, TAG_PROOF_TIME));
		object.setProofComments(getProperty(soapObject, TAG_PROOF_COMMENTS));
		object.setWaitTime(getPropertyAsInt(soapObject, TAG_WAIT_TIME));
		object.setDistance(getPropertyAsInt(soapObject, TAG_DISTANCE));
		object.setFeeAdvance(getPropertyAsInt(soapObject, TAG_FEEADVANVCE));
		object.setWeight(getPropertyAsInt(soapObject, TAG_WEIGHT));
		object.setPieces(getPropertyAsInt(soapObject, TAG_PIECES));
		object.setReceivedBy(getProperty(soapObject, TAG_RECEIVE_BY));
		object.setLatitudeLongitude(getProperty(soapObject, TAG_LATITUDE_LONGITUDE));
		return object;
	}
	/**************** Functions ended here ****************/

	private void setLatitudeLongitude(String property) {
		// TODO Auto-generated method stub
		
	}
}
