package com.tristar.utils;

import com.tristar.object.AddressForServer;
import com.tristar.object.CodeAndTitle;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.PreviousDelignesObject;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnAppOptionsObject;
import com.tristar.object.ReturnHistoryObject;
import com.tristar.object.ReturnMessagesObjects;
import com.tristar.object.SubmitCourtPOD;

import java.util.ArrayList;
import java.util.HashMap;

//@SuppressWarnings("ALL")
public class SessionData {
	private static SessionData instance;
	private String sessionId;
	private String oldUsername;
	private String oldPassword;
	private String workid;
	private String worklistid;
	private String companyCode;
	private int processorderid;
	private int messageInstructions;
	private int primaryKey;

	private String username;
	private String password;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String companyname;
	private String userlocalname;
	private String passworddname;
	private String companynewname;
	private String selectedserver;
	private int filtermap;
	private int lineItem;
	private int servee;
	private int checkvisible;
	private int theAddressType;
	private ReturnAppOptionsObject returnAppOptions;
	private byte[] capturedimage;
	private int image;
	private double imagelat;
	private String Attach_Navigation = "";
	private int CourtPOD_Navigation = 0;
	private int PickupPOD_Navigation = 0;
	private int DeliveryPOD_Navigation = 0;
	private double imagelong;
	private int diligenceAttachment;
	private int finalstatusAttachment;
	private String PreviousdiligenceAddress;
	private ArrayList<CodeAndTitle> detail;
	private ArrayList<PreviousDelignesObject> previous;
	private ArrayList<String> direction;

	private ArrayList<DiligenceForProcess> arrayListProcessDiligence;

	private int SubfinalStatus;
	private ArrayList<ReturnHistoryObject> returnHistoryObjects;

	private ArrayList<ReturnMessagesObjects> returnMessagesObjectses;

	private String str;

	private String addaddressWorkorder;
	private String imageworkorder;
	private int addaddressLineItem;

	private int selectedItem;

	private int logger = 0;

	private int EntityChecked = 0;
	private String agentservice;
	private String agenttitle;

	private int scanner_activity = 0;
	private int scanner_activity_result = 0;

	private int scanner_result = 0;
	private int scanner = 0;
	private int scanner_loca = 0;
	private String scanner_value = "";
	private String final_scanjobresult = "";
	private String final_locationresult = "";
	private int type = 0;
	private int status = 0;

	private String Scanjobresult = "";

	private ArrayList<ProcessAddressForServer> ArrayprocessOrderList;
	private ArrayList<CourtAddressForServer> ArraycourtServiceList;
	private ArrayList<AddressForServer> ArraypickupServiceList;
	private ArrayList<AddressForServer> ArraydeliveryServiceList;

	private boolean Audio_on = true;

	private ArrayList<SubmitCourtPOD> equipmentModelList;
	private byte[] imageData;

	private byte[] audioData;
	private byte[] addaddress;
	private ArrayList<byte[]> attachedFilesData = new ArrayList<byte[]>();

	private ArrayList<HashMap<String,String>> Temp_Record_Diligence_Status = new ArrayList<>();
	private ArrayList<HashMap<String,String>> Temp_Final_Status = new ArrayList<>();

	private ArrayList<String> Scanned_Workorder;
	private ArrayList<Integer> Scanned_Item_Process_ID;
	private boolean Validate_record_Deligence_CheckBox;
	private int Validate_record_Deligence_Scanned_result ;
	private int ProcessOrderDetail_Navigation=0;
	private int ProcessOrderDetail_Address=0;
	private int Deliveryjob_Address=0;
	private int Pickupjob_Address=0;
	private int Courtjob_Address=0;
	private int filter_arrow_visible = - 1;
	private int synchandler = 0;


	public static SessionData getInstance() {
		if (instance == null) {
			instance = new SessionData();
		}
		return instance;
	}

	public int getFilter_arrow_visible() {
		return filter_arrow_visible;
	}

	public void setFilter_arrow_visible(int filter_arrow_visible) {
		this.filter_arrow_visible = filter_arrow_visible;
	}


	public ArrayList<Integer> getScanned_Item_Process_ID() {
		if (Scanned_Item_Process_ID == null)
			Scanned_Item_Process_ID = new ArrayList<>();
		return Scanned_Item_Process_ID;
	}

	public void setScanned_Item_Process_ID(ArrayList<Integer> scanned_Item_Process_ID) {
		Scanned_Item_Process_ID = scanned_Item_Process_ID;
	}

	public int getValidate_record_Deligence_Scanned_result() {
		return Validate_record_Deligence_Scanned_result;
	}

	public void setValidate_record_Deligence_Scanned_result(int validate_record_Deligence_Scanned_result) {
		Validate_record_Deligence_Scanned_result = validate_record_Deligence_Scanned_result;
	}

	public ArrayList<String> getScanned_Workorder() {
		if (Scanned_Workorder == null)
			Scanned_Workorder = new ArrayList<>();
		return Scanned_Workorder;
	}

	public void setScanned_Workorder(ArrayList<String> scanned_Workorder) {
		Scanned_Workorder = scanned_Workorder;
	}

	public boolean isValidate_record_Deligence_CheckBox() {
		return Validate_record_Deligence_CheckBox;
	}

	public void setValidate_record_Deligence_CheckBox(boolean validate_record_Deligence_CheckBox) {
		Validate_record_Deligence_CheckBox = validate_record_Deligence_CheckBox;
	}

	public ArrayList<HashMap<String, String>> getTemp_Final_Status() {
		if (Temp_Final_Status == null)
			Temp_Final_Status = new ArrayList<>();
		return Temp_Final_Status;
	}

	public void setTemp_Final_Status(ArrayList<HashMap<String, String>> temp_Final_Status) {
		Temp_Final_Status = temp_Final_Status;
	}

	public ArrayList<HashMap<String, String>> getTemp_Record_Diligence_Status() {
		if(Temp_Record_Diligence_Status == null)
			Temp_Record_Diligence_Status = new ArrayList<>();
		return Temp_Record_Diligence_Status;
	}

	public void setTemp_Record_Diligence_Status(ArrayList<HashMap<String, String>> temp_Record_Diligence_Status) {
		Temp_Record_Diligence_Status = temp_Record_Diligence_Status;
	}

	public byte[] getAudioData() {
		return audioData;
	}

	public void setAudioData(byte[] audioData) {
		this.audioData = audioData;
	}

	public void clearAttachments() {
		if (attachedFilesData != null) {
			attachedFilesData.clear();
			attachedFilesData = new ArrayList<byte[]>();
		}
	}

	public boolean isAudio_on() {
		return Audio_on;
	}

	public void setAudio_on(boolean audio_on) {
		Audio_on = audio_on;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getOldUsername() {
		return oldUsername;
	}

	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public ArrayList<SubmitCourtPOD> getEquipmentModelList() {
		return equipmentModelList;
	}

	public void setEquipmentModelList(
			ArrayList<SubmitCourtPOD> equipmentModelList) {
		this.equipmentModelList = equipmentModelList;
	}

	public ArrayList<byte[]> getAttachedFilesData() {
		if (attachedFilesData == null)
			attachedFilesData = new ArrayList<byte[]>();
		return attachedFilesData;
	}

	public void setAttachedFilesData(ArrayList<byte[]> attachedFilesData) {
		this.attachedFilesData = attachedFilesData;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public ArrayList<CodeAndTitle> getDetail() {
		return detail;
	}

	public void setDetail(ArrayList<CodeAndTitle> detail) {
		this.detail = detail;
	}

	public String getWorklistid() {
		return worklistid;
	}

	public void setWorklistid(String worklistid) {
		this.worklistid = worklistid;
	}

	public byte[] getAddaddress() {
		return addaddress;
	}

	public void setAddaddress(byte[] addaddress) {
		this.addaddress = addaddress;
	}

	public int getLineItem() {
		return lineItem;
	}

	public void setLineItem(int lineItem) {
		this.lineItem = lineItem;
	}

	public int getTheAddressType() {
		return theAddressType;
	}

	public void setTheAddressType(int theAddressType) {
		this.theAddressType = theAddressType;
	}

	public ArrayList<PreviousDelignesObject> getPrevious() {
		return previous;
	}

	public void setPrevious(ArrayList<PreviousDelignesObject> previous) {
		this.previous = previous;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getUserlocalname() {
		return userlocalname;
	}

	public void setUserlocalname(String userlocalname) {
		this.userlocalname = userlocalname;
	}

	public String getPassworddname() {
		return passworddname;
	}

	public void setPassworddname(String passworddname) {
		this.passworddname = passworddname;
	}

	public String getSelectedserver() {
		return selectedserver;
	}

	public void setSelectedserver(String selectedserver) {
		this.selectedserver = selectedserver;
	}

	public String getCompanynewname() {
		return companynewname;
	}

	public void setCompanynewname(String companynewname) {
		this.companynewname = companynewname;
	}

	public String getPreviousdiligenceAddress() {
		return PreviousdiligenceAddress;
	}

	public void setPreviousdiligenceAddress(String previousdiligenceAddress) {
		PreviousdiligenceAddress = previousdiligenceAddress;
	}

	public int getServee() {
		return servee;
	}

	public void setServee(int servee) {
		this.servee = servee;
	}

	public String getAddaddressWorkorder() {
		return addaddressWorkorder;
	}

	public void setAddaddressWorkorder(String addaddressWorkorder) {
		this.addaddressWorkorder = addaddressWorkorder;
	}

	public int getAddaddressLineItem() {
		return addaddressLineItem;
	}

	public void setAddaddressLineItem(int addaddressLineItem) {
		this.addaddressLineItem = addaddressLineItem;
	}

	public int getCheckvisible() {
		return checkvisible;
	}

	public void setCheckvisible(int checkvisible) {
		this.checkvisible = checkvisible;
	}

	public int getFiltermap() {
		return filtermap;
	}

	public void setFiltermap(int filtermap) {
		this.filtermap = filtermap;
	}

	public ArrayList<String> getDirection() {
		return direction;
	}

	public void setDirection(ArrayList<String> direction) {
		this.direction = direction;
	}

	public int getProcessorderid() {
		return processorderid;
	}

	public void setProcessorderid(int processorderid) {
		this.processorderid = processorderid;
	}

	public String getImageworkorder() {
		return imageworkorder;
	}

	public void setImageworkorder(String imageworkorder) {
		this.imageworkorder = imageworkorder;
	}

	public byte[] getCapturedimage() {
		return capturedimage;
	}

	public void setCapturedimage(byte[] capturedimage) {
		this.capturedimage = capturedimage;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public double getImagelat() {
		return imagelat;
	}

	public void setImagelat(double imagelat) {
		this.imagelat = imagelat;
	}

	public double getImagelong() {
		return imagelong;
	}

	public void setImagelong(double imagelong) {
		this.imagelong = imagelong;
	}

	public int getDiligenceAttachment() {
		return diligenceAttachment;
	}

	public void setDiligenceAttachment(int diligenceAttachment) {
		this.diligenceAttachment = diligenceAttachment;
	}

	public int getFinalstatusAttachment() {
		return finalstatusAttachment;
	}

	public void setFinalstatusAttachment(int finalstatusAttachment) {
		this.finalstatusAttachment = finalstatusAttachment;
	}

	public ReturnAppOptionsObject getReturnAppOptions() {
		return returnAppOptions;
	}

	public void setReturnAppOptions(ReturnAppOptionsObject returnAppOptions) {
		this.returnAppOptions = returnAppOptions;
	}

	public String getAttach_Navigation() {
		return Attach_Navigation;
	}

	public void setAttach_Navigation(
			String attach_Navigation) {
		Attach_Navigation = attach_Navigation;
	}

	public int getCourtPOD_Navigation() {
		return CourtPOD_Navigation;
	}

	public void setCourtPOD_Navigation(int courtPOD_Navigation) {
		CourtPOD_Navigation = courtPOD_Navigation;
	}

	public int getPickupPOD_Navigation() {
		return PickupPOD_Navigation;
	}

	public void setPickupPOD_Navigation(int pickupPOD_Navigation) {
		PickupPOD_Navigation = pickupPOD_Navigation;
	}

	public int getDeliveryPOD_Navigation() {
		return DeliveryPOD_Navigation;
	}

	public void setDeliveryPOD_Navigation(int deliveryPOD_Navigation) {
		DeliveryPOD_Navigation = deliveryPOD_Navigation;
	}


	public int getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
	}

	public int getLogger() {
		return logger;
	}

	public void setLogger(int logger) {
		this.logger = logger;
	}

	public int getMessageInstructions() {
		return messageInstructions;
	}

	public void setMessageInstructions(int messageInstructions) {
		this.messageInstructions = messageInstructions;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getEntityChecked() {
		return EntityChecked;
	}

	public void setEntityChecked(int entityChecked) {
		EntityChecked = entityChecked;
	}

	public String getAgentservice() {
		return agentservice;
	}

	public void setAgentservice(String agentservice) {
		this.agentservice = agentservice;
	}

	public String getAgenttitle() {
		return agenttitle;
	}

	public void setAgenttitle(String agenttitle) {
		this.agenttitle = agenttitle;
	}

	public int getScanner_activity() {
		return scanner_activity;
	}

	public void setScanner_activity(int scanner_activity) {
		this.scanner_activity = scanner_activity;
	}

	public int getScanner() {
		return scanner;
	}

	public void setScanner(int scanner) {
		this.scanner = scanner;
	}

	public ArrayList<ProcessAddressForServer> getArrayprocessOrderList() {
		if (ArrayprocessOrderList == null)
			ArrayprocessOrderList = new ArrayList<>();
		return ArrayprocessOrderList;
	}

	public void setArrayprocessOrderList(ArrayList<ProcessAddressForServer> arrayprocessOrderList) {
		ArrayprocessOrderList = arrayprocessOrderList;
	}

	public ArrayList<CourtAddressForServer> getArraycourtServiceList() {
		return ArraycourtServiceList;
	}

	public void setArraycourtServiceList(ArrayList<CourtAddressForServer> arraycourtServiceList) {
		ArraycourtServiceList = arraycourtServiceList;
	}

	public ArrayList<AddressForServer> getArraypickupServiceList() {
		return ArraypickupServiceList;
	}

	public void setArraypickupServiceList(ArrayList<AddressForServer> arraypickupServiceList) {
		ArraypickupServiceList = arraypickupServiceList;
	}

	public ArrayList<AddressForServer> getArraydeliveryServiceList() {
		return ArraydeliveryServiceList;
	}

	public void setArraydeliveryServiceList(ArrayList<AddressForServer> arraydeliveryServiceList) {
		ArraydeliveryServiceList = arraydeliveryServiceList;
	}

	public int getScanner_activity_result() {
		return scanner_activity_result;
	}

	public void setScanner_activity_result(int scanner_activity_result) {
		this.scanner_activity_result = scanner_activity_result;
	}

	public int getScanner_result() {
		return scanner_result;
	}

	public void setScanner_result(int scanner_result) {
		this.scanner_result = scanner_result;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getSubfinalStatus() {
		return SubfinalStatus;
	}

	public void setSubfinalStatus(int subfinalStatus) {
		SubfinalStatus = subfinalStatus;
	}

	public ArrayList<DiligenceForProcess> getArrayListProcessDiligence() {
		return arrayListProcessDiligence;
	}

	public void setArrayListProcessDiligence(ArrayList<DiligenceForProcess> arrayListProcessDiligence) {
		this.arrayListProcessDiligence = arrayListProcessDiligence;
	}

	public ArrayList<ReturnHistoryObject> getReturnHistoryObjects() {
		return returnHistoryObjects;
	}

	public void setReturnHistoryObjects(ArrayList<ReturnHistoryObject> returnHistoryObjects) {
		this.returnHistoryObjects = returnHistoryObjects;
	}

	public ArrayList<ReturnMessagesObjects> getReturnMessagesObjectses() {
		return returnMessagesObjectses;
	}

	public void setReturnMessagesObjectses(ArrayList<ReturnMessagesObjects> returnMessagesObjectses) {
		this.returnMessagesObjectses = returnMessagesObjectses;
	}

	public int getScanner_loca() {
		return scanner_loca;
	}

	public void setScanner_loca(int scanner_loca) {
		this.scanner_loca = scanner_loca;
	}

	public String getScanner_value() {
		return scanner_value;
	}

	public void setScanner_value(String scanner_value) {
		this.scanner_value = scanner_value;
	}

	public String getFinal_locationresult() {
		return final_locationresult;
	}

	public void setFinal_locationresult(String final_locationresult) {
		this.final_locationresult = final_locationresult;
	}

	public String getFinal_scanjobresult() {
		return final_scanjobresult;
	}

	public void setFinal_scanjobresult(String final_scanjobresult) {
		this.final_scanjobresult = final_scanjobresult;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getScanjobresult() {
		return Scanjobresult;
	}

	public void setScanjobresult(String scanjobresult) {
		Scanjobresult = scanjobresult;
	}

	public int getProcessOrderDetail_Navigation() {
		return ProcessOrderDetail_Navigation;
	}

	public void setProcessOrderDetail_Navigation(int processOrderDetail_Navigation) {
		ProcessOrderDetail_Navigation = processOrderDetail_Navigation;
	}

	public int getProcessOrderDetail_Address() {
		return ProcessOrderDetail_Address;
	}

	public void setProcessOrderDetail_Address(int processOrderDetail_Address) {
		ProcessOrderDetail_Address = processOrderDetail_Address;
	}

	public int getDeliveryjob_Address() {
		return Deliveryjob_Address;
	}

	public void setDeliveryjob_Address(int deliveryjob_Address) {
		Deliveryjob_Address = deliveryjob_Address;
	}

	public int getPickupjob_Address() {
		return Pickupjob_Address;
	}

	public void setPickupjob_Address(int pickupjob_Address) {
		Pickupjob_Address = pickupjob_Address;
	}

	public int getCourtjob_Address() {
		return Courtjob_Address;
	}

	public void setCourtjob_Address(int courtjob_Address) {
		Courtjob_Address = courtjob_Address;
	}

    public int getSynchandler() {
        return synchandler;
    }

    public void setSynchandler(int synchandler) {
        this.synchandler = synchandler;
    }
}
