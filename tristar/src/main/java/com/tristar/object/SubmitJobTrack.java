//package com.tristar.object;
//
//import org.ksoap2.serialization.SoapObject;
//
//import com.tristar.webutils.SoapUtils;
//
//public class SubmitJobTrack extends SoapUtils {
//	
//	public String TAG = SubmitJobTrack.class.getSimpleName();
//	public static String TAG_sessionID = "sessionID";
//	public static String TAG_Workorder = "Workorder";
//	public static String TAG_ServerCode = "ServerCode";
//	public static String TAG_TaskCode = "TaskCode";
//	public static String TAG_StatusCode = "StatusCode";
//    public static String TAG_CustomerCode = "CustomerCode";
//    public static String TAG_DateTimeSubmitted = "DateTimeSubmitted";
//    
//    private String sessionID;
//    private String Workorder;
//    private String ServerCode;
//    private int TaskCode;
//    private int StatusCode;
//    private String CustomerCode;
//    private String DateTimeSubmitted;
//    
//    public String getsessionID(){
//    	return sessionID;
//    }
//    public void setsessionID(String sessionID){
//    	this.sessionID = sessionID;
//    }
//    public String getWorkorder(){
//    	return Workorder;
//    }
//    public void setWokorder(String Workorder){
//    	this.Workorder = Workorder;
//    }
//    public String getServerCode(){
//    	return ServerCode;
//    }
//    public void setServerCode(String ServerCode){
//    	this.ServerCode = ServerCode;
//    }
//    public int getTaskCode(){
//    	return TaskCode;
//    }
//    public void setTaskCode(int TaskCode){
//    	this.TaskCode = TaskCode;
//    }
//    public int getStatusCode(){
//    	return StatusCode;
//    }
//    public void setStatusCode(int StatusCode){
//    	this.StatusCode = StatusCode;
//    }
//    public String getCustomerCode(){
//    	return CustomerCode;
//    }
//    public void setCustomerCode(String CustomerCode){
//    	this.CustomerCode = CustomerCode;
//    }
//    public String getDateTimeSubmitted(){
//    	return DateTimeSubmitted;
//    }
//    public void setDateTimeSubmitted(String DateTimeSubmitted){
//    	this.DateTimeSubmitted = DateTimeSubmitted;
//    }
//    
//    public static SubmitJobTrack parseObject(SoapObject soapObject) {
//    	SubmitJobTrack object = new SubmitJobTrack();
//
//		object.setsessionID(getProperty(soapObject,TAG_sessionID));
//		object.setWokorder(getProperty(soapObject,TAG_Workorder));
//		object.setServerCode(getProperty(soapObject,TAG_ServerCode));
//		object.setTaskCode(getPropertyAsInt(soapObject,TAG_TaskCode));
//		object.setStatusCode(getPropertyAsInt(soapObject,TAG_StatusCode));
//		object.setCustomerCode(getProperty(soapObject,TAG_CustomerCode));
//		object.setDateTimeSubmitted(getProperty(soapObject,TAG_DateTimeSubmitted));
//		return object;
//    }
//}



