package com.tristar.webutils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.tristar.object.Address;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.Loggers;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ALL")
public class SubmitWebServiceConsumer {

	private static SubmitWebServiceConsumer instance = null;
	Loggers logger;

	public static SubmitWebServiceConsumer getInstance() {
		if (instance == null) {
			instance = new SubmitWebServiceConsumer();
		}
		return instance;
	}

	private SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		return envelope;
	}

	@SuppressLint("SimpleDateFormat")
	private String getFormattedDate(String date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateObject = null;
		try {
			dateObject = dateFormat.parse(date);
		} catch (Exception e) {
			dateObject = new Date();
		}
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		return dateFormat.format(dateObject);
	}

	@SuppressLint("SimpleDateFormat")
	private String getFormattedDateOnly(String date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date dateObject = null;
		try {
			dateObject = dateFormat.parse(date);
		} catch (Exception e) {
			dateObject = new Date();
		}
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(dateObject);
	}

	public String SubmitAttemptAttachments(String sessionId,
			com.tristar.object.SubmitDiligence dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_ATTEMPT_ATTACHMENT);

		requestSoap.addProperty("sessionID", sessionId);

		SoapObject theAttachment = new SoapObject();

		theAttachment.addProperty("Workorder", dataObject.getWorkorder());
		theAttachment.addProperty("DiligenceAddressLineitem",
				dataObject.getAddressLineItem());
		if(dataObject.getServerCode().length()==0){
			theAttachment.addProperty("DiligenceLineitem",
					SessionData.getInstance().getStr());
		}
		else{
			theAttachment.addProperty("DiligenceLineitem",
					dataObject.getServerCode());
		}

		theAttachment.addProperty("Lineitem", 0);

		theAttachment.addProperty("FileName",
				dataObject.getAttachementsFileName() + ".jpg");
		theAttachment.addProperty("PDFInMemory",
				dataObject.getAttachementBase64String());

		requestSoap.addProperty("theAttachment", theAttachment);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		envelope.implicitTypes = true;

		envelope.setAddAdornments(false);

		Log.d("SubmitAttemptAttachments Webmethod Request ", ""+ requestSoap.toString());

		Loggers.addLog("TriStar :SubmitAttemptAttachments Webmethod Request : " + requestSoap.toString());


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(
					TristarConstants.SOAP_METHOD_SUBMIT_ATTEMPT_ATTACHMENT,
					envelope);

			String response = envelope.getResponse().toString();

			Loggers.addLog("TriStar :SubmitAttemptAttachments Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitAttemptAttachments Webmethod Response Error: " + e.toString());

			throw e;
		}
	}

	public String SubmitCourtPOD(String sessionId,
			com.tristar.object.SubmitCourtPOD dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_COURT_POD);

		requestSoap.addProperty("sessionId", sessionId);
		SoapObject singleProof = new SoapObject();

		singleProof.addProperty("Workorder", dataObject.getWorkorder());
		singleProof.addProperty("AddressLineitem", 0);
		singleProof.addProperty("ProofType", "");
		singleProof.addProperty("ProofDate", getFormattedDateOnly(dataObject
				.getProofDate().replace("-", "/")));

		singleProof.addProperty("ProofTime", dataObject.getProofTime());
		singleProof.addProperty("ProofComments", dataObject.getProofComments());
		singleProof.addProperty("WaitTime", dataObject.getWaitTime());
		singleProof.addProperty("Distance", 0);

		singleProof.addProperty("FeeAdvance", dataObject.getFeeAdvance());
		singleProof.addProperty("Weight", dataObject.getWeight());
		singleProof.addProperty("Pieces", dataObject.getPieces());
		singleProof.addProperty("LatitudeLongitude", dataObject.getLatitude()
				+ ";" + dataObject.getLongitude());

		singleProof.addProperty("ReceivedBy", "");

		requestSoap.addProperty("singleProof", singleProof);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		Loggers.addLog("TriStar :SubmitCourtPOD Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_COURT_POD,
					envelope);

			String response = envelope.getResponse().toString();

			Loggers.addLog("TriStar :SubmitCourtPOD Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {

			SessionData.getInstance().setPrimaryKey(0);

			if(e.toString().contains("Violation of PRIMARY KEY constraint")){


				SessionData.getInstance().setPrimaryKey(1);


			}

			Loggers.addLog("TriStar :SubmitCourtPOD Webmethod Response Error : " + e.toString());

			throw e;
		}

	}

	public String SubmitCourtPODAttachments(String sessionID,
			com.tristar.object.PODAttachments dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_COURT_POD_ATTACHMENTS);

		requestSoap.addProperty("sessionID", sessionID);
		SoapObject theAttachment = new SoapObject();

		theAttachment.addProperty("Workorder", dataObject.getWorkorder());
		theAttachment.addProperty("DiligenceAddressLineitem",
				dataObject.getDiligenceAddressLineitem());
		theAttachment.addProperty("DiligenceLineitem", 0);
		theAttachment.addProperty("Lineitem", 0);

		theAttachment
				.addProperty("FileName", dataObject.getFileName() + ".jpg");
		theAttachment.addProperty("PDFInMemory", dataObject.getPdfInMemory());

		requestSoap.addProperty("theAttachment", theAttachment);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitCourtPODAttachments Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(
					TristarConstants.SOAP_METHOD_SUBMIT_COURT_POD_ATTACHMENTS,
					envelope);

			String response = envelope.getResponse().toString();

			Loggers.addLog("TriStar :SubmitCourtPODAttachments Webmethod Response : " + response.toString());

			Log.d("Responsecourt", "" + response);
			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitCourtPODAttachments Webmethod Response : " + e.toString());

			throw e;
		}

	}

	public String SubmitDeliveryPOD(String sessionID,
			com.tristar.object.SubmitDeliveryPOD dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_DELIVERY_POD);

		requestSoap.addProperty("sessionId", sessionID);

		SoapObject singleProof = new SoapObject();
		singleProof.addProperty("Workorder", dataObject.getWorkorder());
		singleProof.addProperty("AddressLineitem",
				dataObject.getAddressLineitem());
		singleProof.addProperty("ProofType", dataObject.getProofType());
		singleProof.addProperty("ProofDate", getFormattedDateOnly(dataObject
				.getProofDate().replace("-", "/")));

		singleProof.addProperty("WaitTime", dataObject.getWaitTime());
		singleProof.addProperty("ProofTime", dataObject.getProofTime());
		singleProof.addProperty("ProofComments", dataObject.getProofComments());
		singleProof.addProperty("Distance", 0);
		singleProof.addProperty("FeeAdvance", dataObject.getFeeAdvance());

		singleProof.addProperty("Weight", dataObject.getWeight());
		singleProof.addProperty("Pieces", dataObject.getPieces());
		singleProof.addProperty("LatitudeLongitude", dataObject.getLatitude()
				+ ";" + dataObject.getLongitude());
		singleProof.addProperty("ReceivedBy", dataObject.getReceivedBy());

		requestSoap.addProperty("singleProof", singleProof);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		Loggers.addLog("TriStar :SubmitDeliveryPOD Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_DELIVERY_POD,
					envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitDeliveryPOD Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitDeliveryPOD Webmethod Response Error : " + e.toString());

			throw e;
		}

	}

	@SuppressLint("LongLogTag")
	public String SubmitDeliveryPODAttachments(String sessionID,
											   com.tristar.object.PODAttachments dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_DELIVERY_POD_ATTACHMENTS);

		requestSoap.addProperty("sessionID", sessionID);
		SoapObject theAttachment = new SoapObject();

		theAttachment.addProperty("Workorder", dataObject.getWorkorder());
		theAttachment.addProperty("DiligenceAddressLineitem",
				dataObject.getLineitem());
		theAttachment.addProperty("DiligenceLineitem", 0);
		theAttachment.addProperty("Lineitem", 0);

		theAttachment
				.addProperty("FileName", dataObject.getFileName() + ".jpg");
		theAttachment.addProperty("PDFInMemory", dataObject.getPdfInMemory());
		Log.d("Submit Diligence request", "" + dataObject.getPdfInMemory());
		requestSoap.addProperty("theAttachment", theAttachment);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		Loggers.addLog("TriStar :SubmitDeliveryPODAttachments Webmethod Request : " + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);

		try {

			httpTransport
					.call(TristarConstants.SOAP_METHOD_SUBMIT_DELIVERY_POD_ATTACHMENTS,
							envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitDeliveryPODAttachments Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {

			Loggers.addLog("TriStar :SubmitDeliveryPODAttachments Webmethod Response Error: " + e.toString());

			throw e;
		}

	}

	@SuppressLint("LongLogTag")
	public String SubmitDiligence(String sessionId,
								  com.tristar.object.SubmitDiligence dataObject) throws Exception {


//		<Workorder>string</Workorder>
//        <AddressLineitem>int</AddressLineitem>
//        <Lineitem>int</Lineitem>
//        <DiligenceDate>string</DiligenceDate>
//        <DiligenceTime>string</DiligenceTime>
//        <Report>string</Report>
//        <ServerCode>string</ServerCode>
//        <DiligenceCode>int</DiligenceCode>
//        <DateTimeSubmitted>dateTime</DateTimeSubmitted>
//        <LatitudeLongitude>string</LatitudeLongitude>

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_DILIGENCE);

		requestSoap.addProperty("sessionID", sessionId);

		SoapObject theDiligence = new SoapObject();
		theDiligence.addProperty("Workorder", dataObject.getWorkorder());
		Log.d("Submit Diligence request", ""
				+ dataObject.getWorkorder().toString());
		theDiligence.addProperty("AddressLineitem",
				dataObject.getAddressLineItem());
		theDiligence
				.addProperty("Lineitem", dataObject.getAddressLineItem());  // getLineItem
																				// may
																				// here
		theDiligence
				.addProperty("DiligenceDate", dataObject.getDiligenceDate());

		theDiligence
				.addProperty("DiligenceTime", dataObject.getDiligenceTime());
		theDiligence.addProperty("Report", dataObject.getReport());
		theDiligence.addProperty("ServerCode", dataObject.getServerCode());
		theDiligence
				.addProperty("DiligenceCode", dataObject.getDiligenceCode());

		theDiligence.addProperty("DateTimeSubmitted",
				getFormattedDate(dataObject.getDateTimeSubmitted()));
		theDiligence.addProperty("LatitudeLongitude", dataObject.getLatitude()
				+ ";" + dataObject.getLongitude());

		requestSoap.addProperty("theDiligence", theDiligence);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Log.d("Submit Diligence request", "" + envelope.toString());
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		Loggers.addLog("TriStar :SubmitDiligence Webmethod Request: " + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_DILIGENCE,
					envelope);
			String response = envelope.getResponse().toString();
			Log.d("Submit Diligence response", "" + response.toString());
			Loggers.addLog("TriStar :SubmitDiligence Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {

			Loggers.addLog("TriStar :SubmitDiligence Webmethod Response : " + e.toString());


			throw e;
		}
	}

	@SuppressLint("LongLogTag")
	public String SubmitFinalAttachments(String sessionID,
										 com.tristar.object.SubmitDiligence dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_FINAL_ATTACHMENTS);

		requestSoap.addProperty("sessionID", sessionID);

		SoapObject theAttachment = new SoapObject();
		String extension = ".jpg";
		if (dataObject.getAttachementsFileName().toLowerCase(Locale.US)
				.contains("audio")) {
			extension = ".mp3";
		}
		theAttachment.addProperty("Workorder", dataObject.getWorkorder());
		theAttachment.addProperty("DiligenceAddressLineitem",
				dataObject.getAddressLineItem());
		theAttachment.addProperty("DiligenceLineitem", 0);
		theAttachment.addProperty("Lineitem", 0);

		theAttachment.addProperty("FileName",
				dataObject.getAttachementsFileName() + extension);
		Log.d("getAttachementsFileName",
				"" + dataObject.getAttachementBase64String());
		theAttachment.addProperty("PDFInMemory",
				dataObject.getAttachementBase64String());
		Log.d("getAttachementBase64String",
				"" + dataObject.getAttachementBase64String());
		requestSoap.addProperty("theAttachment", theAttachment);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		Loggers.addLog("TriStar :SubmitFinalAttachments Webmethod Request : " + requestSoap.toString());


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(
					TristarConstants.SOAP_METHOD_SUBMIT_FINAL_ATTACHMENTS,
					envelope);

			String response = envelope.getResponse().toString();
			Log.d("result of attachment", ""
					+ envelope.getResponse().toString());

			Loggers.addLog("TriStar :SubmitFinalAttachments Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitFinalAttachments Webmethod Response Result: " + e.toString());

			throw e;
		}

	}

	public String SubmitFinalStatus(String sessionId,
			ProcessAddressForServer dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_FINAL_STATUS);

		requestSoap.addProperty("sessionID", sessionId);

		SoapObject theFinal = new SoapObject(TristarConstants.NAME_SPACE,
				"theFinal"); // requestSoap.newInstance();
		theFinal.addProperty("Workorder", dataObject.getWorkorder());
		Log.d("Workorder", "" + dataObject.getWorkorder());
		theFinal.addProperty("AddressLineitem", dataObject.getAddressLineItem());
		Log.d("AddressLineitem", "" + dataObject.getAddressLineItem());
		theFinal.addProperty("ServerCode", "");
		theFinal.addProperty("Servee", dataObject.getServee());
  		Log.d("Servee", "" + dataObject.getServee());
		theFinal.addProperty("AuthorizedAgent", dataObject.getAuthorizedAgent());
		Log.d("AuthorizedAgent", "" + dataObject.getAuthorizedAgent());
		theFinal.addProperty("AuthorizedAgentTitle",
				dataObject.getAuthorizedAgentTitle());
		Log.d("AuthorizedAgentTitle", "" + dataObject.getAuthorizedAgentTitle());
		theFinal.addProperty("LeftWith", dataObject.getLeftWith());
		Log.d("LeftWith", "" + dataObject.getLeftWith());
		if(SessionData.getInstance().getSubfinalStatus()==0){
			theFinal.addProperty("Relationship",
					dataObject.getAgentForServiceRelationShipToServee());

		}else{
			theFinal.addProperty("Relationship",
					dataObject.getRelation());
		}
				Log.d("Relationship", "" + dataObject.getAgentForServiceRelationShipToServee());
		theFinal.addProperty("ServeDate", dataObject.getServeDate());
		Log.d("ServeDate", "" + dataObject.getServeDate());
		theFinal.addProperty("ServeTime", dataObject.getServeTime());
		Log.d("ServeTime", "" + dataObject.getServeTime());
		theFinal.addProperty("Age", dataObject.getAge());
		Log.d("Age", "" + dataObject.getAge());
		theFinal.addProperty("Height", dataObject.getHeight());
		Log.d("Height",""+dataObject.getHeight());
		theFinal.addProperty("Weight", dataObject.getWeight());

		theFinal.addProperty("Skin", dataObject.getSkin());
		theFinal.addProperty("Hair", dataObject.getHair());
		theFinal.addProperty("Eyes", dataObject.getEyes());

		theFinal.addProperty("Marks", dataObject.getMarks());
		theFinal.addProperty("DateTimeSubmitted",
				getFormattedDate(dataObject.getDateTimeSubmitted()));
		theFinal.addProperty("AdditionalMarks", "");
		theFinal.addProperty("MannerOfServiceCode",
				dataObject.getMannerofServicecode());

		theFinal.addProperty("Report", dataObject.getReport());
		theFinal.addProperty("Entity", dataObject.isEntity() ? 1 : 0);
		theFinal.addProperty("ServerisMale", dataObject.isServeeIsMale() ? 1
				: 0);
		theFinal.addProperty("InUniform", dataObject.getInuniform() ? 1 : 0);

		theFinal.addProperty("Military", dataObject.getMilitary() ? 1 : 0);
		theFinal.addProperty("Police", dataObject.getPolice() ? 1 : 0);
		theFinal.addProperty("LatitudeLongitude", dataObject.getLatitude()
				+ ";" + dataObject.getLongitude());
		theFinal.addAttribute("type", new SoapObject().getClass());

		requestSoap.addProperty("theFinal", theFinal);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitFinalStatus Webmethod Request: " + requestSoap.toString());

		Log.d("SubmitFinalStatus Webmethod Requestnew:",""+requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);

		try {

			httpTransport.call(
					TristarConstants.SOAP_METHOD_SUBMIT_FINAL_STATUS, envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitFinalStatus Webmethod Response: " + response.toString());

			return response;

		} catch (Exception e) {

			Loggers.addLog("TriStar :SubmitFinalStatus Webmethod Response error : " + e.toString());

			throw e;
		}

	}

	public int AddProcessAddress(String sessionId, Address dataObject)
			throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_ADD_PROCESS_ADDRESS);

		requestSoap.addProperty("sessionID", sessionId);

		SoapObject theAddress = new SoapObject(TristarConstants.NAME_SPACE,
				"theAddress"); 
		theAddress.addProperty("Workorder", dataObject.getWorkorder());
		theAddress.addProperty("LineItem", dataObject.getLineItem());
		Log.d("LineItem ", "" + dataObject.getLineItem());

		theAddress.addProperty("Street", dataObject.getStreet());
		Log.d("Submit ", "" + dataObject.getStreet());
		theAddress.addProperty("Suite", dataObject.getSuite());

		theAddress.addProperty("City", dataObject.getCity());
		Log.d("Submit", "" + dataObject.getCity());
		theAddress.addProperty("State", dataObject.getState());
		Log.d("Submit", "" + dataObject.getState());

		theAddress.addProperty("Zip", dataObject.getZip());
		Log.d("Submit", "" + dataObject.getZip());

		Log.d("Submit", "" + dataObject.getTheAddressType());

		requestSoap.addProperty("theAddress", theAddress);
		requestSoap.addProperty("theAddressType",
				dataObject.getTheAddressType());

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		Loggers.addLog("TriStar :AddProcessAddressReturnLineItem Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);

		try {

			httpTransport.call(
					TristarConstants.SOAP_METHID_ADD_PROCESS_ADDRESS, envelope);

			int response = Integer.parseInt(envelope.getResponse().toString());
			Log.d("Submit Address Response", ""
					+ envelope.getResponse().toString());
			Loggers.addLog("TriStar :AddProcessAddressReturnLineItem Webmethod Response : " + response);

			return response;

		} catch (Exception e) {

			Loggers.addLog("TriStar :AddProcessAddressReturnLineItem Webmethod Response Error: " + e.toString());

			throw e;
		}

	}

	public String SubmitPickupPOD(String sessionId,
			com.tristar.object.SubmitPickupPOD dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_PICK_UP_POD);

		requestSoap.addProperty("sessionId", sessionId);
		SoapObject singleProof = new SoapObject();
		String workorder = dataObject.getWorkorder();
		singleProof.addProperty("Workorder", workorder);
		singleProof.addProperty("AddressLineitem",
				dataObject.getAddressLineitem());
		singleProof.addProperty("ProofType", "");

		singleProof.addProperty("ProofDate", getFormattedDateOnly(dataObject
				.getProofDate().replace("/", "-")));
		singleProof.addProperty("ProofTime", dataObject.getProofTime());
		singleProof.addProperty("ProofComments", dataObject.getProofComments());
		singleProof.addProperty("WaitTime", 0);

		singleProof.addProperty("Distance", 0);
		singleProof.addProperty("FeeAdvance", 0);
		singleProof.addProperty("Weight", 0);
		singleProof.addProperty("Pieces", 0);

		singleProof.addProperty("LatitudeLongitude", dataObject.getLatitude()
				+ ";" + dataObject.getLongitude());
		singleProof.addProperty("ReceivedBy", "");

		requestSoap.addProperty("singleProof", singleProof);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitPickupPOD Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_PICK_UP_POD,
					envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitPickupPOD Webmethod Response : " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitPickupPOD Webmethod Response Error: " + e.toString());

			throw e;
		}

	}

	public String SubmitPickupPODAttachments(String sessionID,
			com.tristar.object.PODAttachments dataObject) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_PICK_UP_POD_ATTACHMENTS);

		requestSoap.addProperty("sessionID", sessionID);
		SoapObject theAttachment = new SoapObject();
		theAttachment.addProperty("Workorder", dataObject.getWorkorder());
		theAttachment.addProperty("DiligenceAddressLineitem",
				dataObject.getLineitem());
		theAttachment.addProperty("DiligenceLineitem", 0);
		theAttachment.addProperty("Lineitem", 0);

		theAttachment
				.addProperty("FileName", dataObject.getFileName() + ".jpg");
		theAttachment.addProperty("PDFInMemory", dataObject.getPdfInMemory());
		requestSoap.addProperty("theAttachment", theAttachment);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitPickupPODAttachments Webmethod Request: " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport
					.call(TristarConstants.SOAP_METHOD_SUBMIT_PICK_UP_POD_ATTACHMENTS,
							envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitPickupPODAttachments Webmethod Response: " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitPickupPODAttachments Webmethod Response Error: " + e.toString());

			throw e;
		}
	}

	public String SubmitStatus(String sessionid, String workorder,
			int lineitem, String diligencedate, String diligencetime,
			String report, boolean comment) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_STATUS);

		requestSoap.addProperty("sessionID", sessionid);

		SoapObject singleStatus = new SoapObject();

		singleStatus.addProperty("Workorder", workorder);
		singleStatus.addProperty("Lineitem", lineitem);
		singleStatus.addProperty("DiligenceDate", diligencedate);
		singleStatus.addProperty("DiligenceTime", diligencetime);


		singleStatus.addProperty("Report", report);
		singleStatus.addProperty("Comment", comment);
		requestSoap.addProperty("singleStatus", singleStatus);


		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitStatus Webmethod Request: " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_STATUS, envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitStatus Webmethod Response: " + response.toString());

			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitStatus Webmethod Response Error : " + e.toString());

			throw e;
		}

	}
	
	public String SubmitJobTrack(String sessionID, String Workorder,
								 int TaskCode, int StatusCode, String CustomerCode, String date) throws Exception {
		
		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_JOB_TRACK);
		
		requestSoap.addProperty("sessionID", sessionID);
		
//		SoapObject requestSoapNew = new SoapObject(TristarConstants.NAME_SPACE,"tracking");
//		Log.d("size array", ""+Workorder.size());
//		for(int i = 0; Workorder.size() > i;i++){
//
//			SoapObject theTrack = new SoapObject();
//			theTrack.addProperty("Workorder",Workorder.get(i));
//			theTrack.addProperty("ServerCode",ServerCode);
//			theTrack.addProperty("TaskCode",TaskCode);
//			theTrack.addProperty("StatusCode",StatusCode);
//			theTrack.addProperty("CustomerCode",CustomerCode);
//			theTrack.addProperty("DateTimeSubmitted",date);
//			requestSoapNew.addSoapObject(theTrack);
//
//		}
//
//		requestSoap.addSoapObject(requestSoapNew);

//		Log.d("size array", ""+Workorder.size());
//		for(int i = 0; Workorder.size() > i;i++){
			SoapObject theTrack = new SoapObject();
			theTrack.addProperty("Workorder",Workorder);
			theTrack.addProperty("ServerCode", "None");
			theTrack.addProperty("TaskCode",TaskCode);
			theTrack.addProperty("StatusCode",StatusCode);
			theTrack.addProperty("Code",CustomerCode);
			theTrack.addProperty("DateTimeSubmitted",date);
			requestSoap.addProperty("tracking", theTrack);

	//	}
		
		
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitJobTrack Webmethod Request : " + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_JOB_TRACK,
					envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitJobTrack Webmethod Response : " + response.toString());

			Log.d("Response for Data",""+response);
			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitJobTrack Webmethod Response Error : " + e.toString());

			throw e;
		}	
		
	}


	public String SubmitStatusDirect(String sessionID, SubmitStatusList submitStatus) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SUBMIT_STATUS_DIRECT);

		requestSoap.addProperty("sessionID", sessionID);

//		SoapObject requestSoapNew = new SoapObject(TristarConstants.NAME_SPACE,"tracking");
//		Log.d("size array", ""+Workorder.size());
//		for(int i = 0; Workorder.size() > i;i++){
//
//			SoapObject theTrack = new SoapObject();
//			theTrack.addProperty("Workorder",Workorder.get(i));
//			theTrack.addProperty("ServerCode",ServerCode);
//			theTrack.addProperty("TaskCode",TaskCode);
//			theTrack.addProperty("StatusCode",StatusCode);
//			theTrack.addProperty("CustomerCode",CustomerCode);
//			theTrack.addProperty("DateTimeSubmitted",date);
//			requestSoapNew.addSoapObject(theTrack);
//
//		}
//
//		requestSoap.addSoapObject(requestSoapNew);

//		Log.d("size array", ""+Workorder.size());
//		for(int i = 0; Workorder.size() > i;i++){
		SoapObject myStatus = new SoapObject();
		myStatus.addProperty("Workorder",submitStatus.getWorkorder());
		myStatus.addProperty("Lineitem",submitStatus.getLineitem());
		myStatus.addProperty("StatusDate",submitStatus.getStatusDate());
		myStatus.addProperty("StatusTime",submitStatus.getStatusTime());
		myStatus.addProperty("Report",submitStatus.getReport());
		myStatus.addProperty("ServerCode","");

		myStatus.addProperty("DateTimeSubmitted", submitStatus.getDateTimeSubmitted());
		requestSoap.addProperty("myStatus", myStatus);

		//	}


		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Loggers.addLog("TriStar :SubmitStatus Webmethod Request : " + requestSoap.toString());

		Log.d("SubmitStatus Request : ","" + requestSoap.toString());

		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_SUBMIT_STATUS_DIRECT,
					envelope);

			String response = envelope.getResponse().toString();
			Loggers.addLog("TriStar :SubmitStatus Webmethod Response : " + response.toString());

			Log.d("Response for Data",""+response);
			return response;

		} catch (Exception e) {
			Loggers.addLog("TriStar :SubmitStatus Webmethod Response Error : " + e.toString());

			throw e;
		}

	}
	
}
