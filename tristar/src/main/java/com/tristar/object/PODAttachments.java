package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class PODAttachments extends SoapUtils {

	public static String TAG = PODAttachments.class.getSimpleName();

	public static String TAG_Workorder = "Workorder";
	public static String TAG_Diligenceaddresslineitem = "DiligenceAddressLineitem";
	public static String TAG_Diligencelineitem = "DiligenceLineitem";
	public static String TAG_Lineitem = "Lineitem";
	public static String TAG_Filename = "FileName";
	public static String TAG_PPDFInMemory = "PDFInMemory";

	private String workorder;
	private int diligenceAddressLineitem;
	private int diligenceLineitem;
	private int lineitem;
	private String fileName;
	private String pdfInMemory;
	private String attachString;
	private int SubmitCourtPODID;

	public String getWorkorder() {
		return workorder;
	}

	public int getDiligenceAddressLineitem() {
		return diligenceAddressLineitem;
	}

	public int getDiligenceLineitem() {
		return diligenceLineitem;
	}

	public int getLineitem() {
		return lineitem;
	}

	public String getFileName() {
		return fileName;
	}

	public String getPdfInMemory() {
		return pdfInMemory;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public void setDiligenceAddressLineitem(int diligenceAddressLineitem) {
		this.diligenceAddressLineitem = diligenceAddressLineitem;
	}

	public void setDiligenceLineitem(int diligenceLineitem) {
		this.diligenceLineitem = diligenceLineitem;
	}

	public void setLineitem(int lineitem) {
		this.lineitem = lineitem;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSubmitPODID() {
		return SubmitCourtPODID;
	}

	public void setSubmitPODID(int submitCourtPODID) {
		this.SubmitCourtPODID = submitCourtPODID;
	}

	public String getAttachString() {
		return attachString;
	}

	public void setAttachString(String attachString) {
		this.attachString = attachString;
	}

	public void setPdfInMemory(String pdfInMemory) {
		this.pdfInMemory = pdfInMemory;
	}

	public static PODAttachments parseObject(SoapObject soapObject)
			throws Exception {
		PODAttachments object = new PODAttachments();
		// TODO soap parsing
		object.setWorkorder(getProperty(soapObject, TAG_Workorder));
		object.setDiligenceAddressLineitem(getPropertyAsInt(soapObject,
				TAG_Diligenceaddresslineitem));
		object.setDiligenceLineitem(getPropertyAsInt(soapObject,
				TAG_Diligencelineitem));
		object.setLineitem(getPropertyAsInt(soapObject, TAG_Lineitem));
		object.setFileName(getProperty(soapObject, TAG_Filename));
		object.setPdfInMemory(getProperty(soapObject, TAG_PPDFInMemory));
		return object;
	}

}
