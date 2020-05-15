package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class SubmitFinalAttachments extends SoapUtils{

public static String TAG = SubmitFinalAttachments.class.getSimpleName();
	public static String TAG_WORKORDER = "Workorder";
	public static String TAG_DILIGENCE_ADDRESS_LINEITEM = "DiligenceAddressLineitem";
	public static String TAG_DILIGENCE_LINEITEM = "DiligenceLineitem";
	public static String TAG_LINEITEM = "Lineitem";
	public static String TAG_FILENAME = "FileName";
	public static String TAG_PDF_INMEMORY = "PDFInMemory";

	private String Workorder;
	private int DiligenceAddressLineitem;
	private int DiligenceLineitem;
	private int Lineitem;
	private String FileName;
	private String PDFInMemory;
	private String attachString;
	private int SubmitCourtPODID;
	
	public String getWorkorder() {
		return Workorder;
	}
	public void setWorkorder(String workorder) {
		Workorder = workorder;
	}
	public int getDiligenceAddressLineitem() {
		return DiligenceAddressLineitem;
	}
	public void setDiligenceAddressLineitem(int diligenceAddressLineitem) {
		DiligenceAddressLineitem = diligenceAddressLineitem;
	}
	public int getDiligenceLineitem() {
		return DiligenceLineitem;
	}
	public void setDiligenceLineitem(int diligenceLineitem) {
		DiligenceLineitem = diligenceLineitem;
	}
	public int getLineitem() {
		return Lineitem;
	}
	public void setLineitem(int lineitem) {
		Lineitem = lineitem;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getPDFInMemory() {
		return PDFInMemory;
	}
	public void setPDFInMemory(String pDFInMemory) {
		PDFInMemory = pDFInMemory;
	}
	
	public static SubmitFinalAttachments parseObject(SoapObject soapObject) {
		SubmitFinalAttachments object = new SubmitFinalAttachments();
		
			object.setWorkorder(getProperty(soapObject, TAG_WORKORDER));
			object.setDiligenceAddressLineitem(getPropertyAsInt(soapObject, TAG_DILIGENCE_ADDRESS_LINEITEM));
			object.setDiligenceLineitem(getPropertyAsInt(soapObject, TAG_DILIGENCE_LINEITEM));
			
			object.setLineitem(getPropertyAsInt(soapObject, TAG_LINEITEM));
			object.setFileName(getProperty(soapObject, TAG_FILENAME));
			object.setPDFInMemory(getProperty(soapObject, TAG_PDF_INMEMORY));
	
		return object;
	}
	public String getAttachString() {
		return attachString;
	}
	public void setAttachString(String attachString) {
		this.attachString = attachString;
	}
	public int getSubmitCourtPODID() {
		return SubmitCourtPODID;
	}
	public void setSubmitCourtPODID(int submitCourtPODID) {
		SubmitCourtPODID = submitCourtPODID;
	}
}
