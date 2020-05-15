package com.tristar.webutils;

import android.support.v4.app.NavUtils;
import android.util.Log;

import com.tristar.db.SyncronizeClass;
import com.tristar.object.AddressForServer;
import com.tristar.object.CodeAndTitle;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.DiligencePhrase;
import com.tristar.object.MannerOfService;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnAppOptionsObject;
import com.tristar.object.ReturnHistoryObject;
import com.tristar.object.ReturnMessagesObjects;
import com.tristar.object.ReturnRouteTaskStatusObject;
import com.tristar.object.ReturnRouteTasksObject;
import com.tristar.object.ReturnStatusListObect;
import com.tristar.object.ServerSubmittedGPSUpdateObject;
import com.tristar.object.Tristar;
import com.tristar.object.UpdateGPSCoordinatesObject;
import com.tristar.utils.Loggers;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class WebServiceConsumer {
	private static WebServiceConsumer instance = null;
	private SoapObject requestSoap;

	Loggers logger;

	private SoapSerializationEnvelope envelope;
	private int timeout = 120 * 1000;

	
	public static WebServiceConsumer getInstance() {
		if (instance == null) {
			instance = new WebServiceConsumer();
		}
		return instance;
	}

	private SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
		SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope1.dotNet = true;
		envelope1.setOutputSoapObject(soapObject);
		return envelope1;
	}

	public String getDynamicUrlFromWebserviceByPassingCompanyCode(
			String companycode) throws Exception {


		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_CLOUD_TWO);

		requestSoap.addProperty("theFirst", companycode);
		requestSoap.addProperty("theSecond", "");
		requestSoap.addProperty("theThird", "");

		logger.addLog("TriStar : Dynamic Url From Webservice By Passing Company Code Request : " + requestSoap );
		Log.d("Dynamic Url companycode requestSoap",""+ requestSoap);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);

		try {
			String tempWebServiceUrl = "https://tristarsoftware.net/pic/TheDongle.asmx";
			HttpTransportSE httpTransport = new HttpTransportSE(tempWebServiceUrl);

			httpTransport
					.call(TristarConstants.SOAP_METHOD_CLOUD_TWO, envelope);

			String response = envelope.getResponse().toString().trim();

			logger.addLog("TriStar : Dynamic Url From Webservice By Passing Company Code Response : " + response );
			Log.d("Dynamic Url companycode Responce",""+ response);

			return response;

		} catch (Exception e) {

			logger.addLog("TriStar : Dynamic Url From Webservice By Passing Company Code Response Error : " + e );
			Log.d("Dynamic Url Responce",""+e);
			throw e;
		}
	}

	public String signOn(String soapAddress, String username, String password)
			throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SIGNON);

		requestSoap.addProperty("partOne", username);
		requestSoap.addProperty("partTwo", password);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar : Sign On Request : " + requestSoap);


		try {
			HttpTransportSE httpTransport = new HttpTransportSE(soapAddress);

			httpTransport.call(TristarConstants.SOAP_METHOD_SIGNON, envelope);

			String response = envelope.getResponse().toString();

			logger.addLog("TriStar : Sign On Response : " + response);

			return response;

		} catch (Exception e) {

			logger.addLog("TriStar : Sign On Response Error: " + e);
			Log.d("Sign Responce",""+e);

			throw e;
		}
	}

	public String signOff(String sessionID) throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SyncronizeClass.instance().sessionIDForFinalSync = "";
		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_SIGNOFF);

		requestSoap.addProperty("connectionString", sessionID);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);

		try {
			httpTransport.call(TristarConstants.SOAP_METHOD_SIGNOFF, envelope);

			String signoffResult = envelope.getResponse().toString();
			Log.d("Signoff", "" +signoffResult);
			logger.addLog("TriStar : Sign Off Response : " + signoffResult);

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnSignoff :" + seconds + " seconds.");

			return signoffResult;
			

		} catch (Exception e) {
			logger.addLog("TriStar : Sign Off Response Error : " + e);
			throw e;
		}
	}

	public String GetSpecialInstructions(String sessionid, String workorder)
			throws Exception {
		requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_GET_SPECIAL_INSTRUCTIONS);
		requestSoap.addProperty("sessionID", sessionid);
		requestSoap.addProperty("theWorkorder", workorder);
		Log.d("Special nates", "Special nates is " + requestSoap.toString());
		envelope = getEnvelope(requestSoap);
		logger.addLog("TriStar : Special Instructions Request: " + requestSoap);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);

		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_GET_SPECIAL_INSTRUCTIONS,
					envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			logger.addLog("TriStar : Special Instructions Response: " + response);
			Log.d("Special nates result", "" + response.toString());
			return response.toString();

		} catch (Exception e) {
			logger.addLog("TriStar : Special Instructions Response Error: " + e);

			throw e;
		}

	}

	public String getAttachedPDF(String sessionID, String theWorkorder,
			int theLineitem) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_GET_ATTACHED_PDF);

		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("theWorkorder", theWorkorder);
		requestSoap.addProperty("theLineitem", theLineitem);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar : Attached PDF Request: " + requestSoap);

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {

			httpTransport.call(TristarConstants.SOAP_METHOD_GET_ATTACHED_PDF,
					envelope);

			String response = envelope.getResponse().toString();

			logger.addLog("TriStar : Attached PDF Response: " + response);

			return response;

		} catch (Exception e) {

			logger.addLog("TriStar : Attached PDF Response Error: " + e);

			throw e;
		}

	}

	public ArrayList<CourtAddressForServer> getCourtAddressForServer(
			String sessionID) throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_COURT_OPEN_ADDRESSES);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar : CourtAddressForServer Request : " + requestSoap);
		Log.d("CourtAddressForServer Request : ","" + requestSoap);


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_COURT_OPEN_ADDRESSES,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar : CourtAddressForServer Response : " + response);
			Log.d("CourtAddressForServer Response : ","" + response);

			if(response.toString().contains("AddressFormattedNewLine1")){
				SessionData.getInstance().setCourtjob_Address(0);

			}else{
				SessionData.getInstance().setCourtjob_Address(1);
			}

			ArrayList<CourtAddressForServer> object = new ArrayList<CourtAddressForServer>();
			for (int i = 0; i < numProps; i++) {
				CourtAddressForServer addressForServer = CourtAddressForServer
						.parseObject((SoapObject) response.getProperty(i));
				object.add(addressForServer);
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnCourtOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar : CourtAddressForServer Response Error: " + e);

			throw e;
		}
	}
	
	public ArrayList<ReturnHistoryObject> returnHistory(String sessionID, String Workorder) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_HISTORY);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("Workorder", Workorder);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar : ReturnHistory Request : " + requestSoap);
		Log.d("ReturnHistory Request : ","" + requestSoap);


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_HISTORY,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar : ReturnHistory Response : " + response);
			Log.d("ReturnHistory Response : ","" + response);


			ArrayList<ReturnHistoryObject> object = new ArrayList<ReturnHistoryObject>();
			for (int i = 0; i < numProps; i++) {
				ReturnHistoryObject addressForServer = ReturnHistoryObject
						.parseObject((SoapObject) response.getProperty(i));
				object.add(addressForServer);
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnCourtOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar : ReturnHistory Response Error: " + e);

			throw e;
		}

	}

	public ArrayList<ReturnMessagesObjects> returnMessages(String sessionID) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_MESSAGES);
		requestSoap.addProperty("sessionID", sessionID);


		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar : ReturnMessage Request : " + requestSoap);
		Log.d("ReturnMessage Request : ","" + requestSoap);


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(TristarConstants.SOAP_METHOD_RETURN_MESSAGES, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar : ReturnMessage Response : " + response);
			Log.d("ReturnMessage Response : ","" + response);


			ArrayList<ReturnMessagesObjects> object = new ArrayList<ReturnMessagesObjects>();
			for (int i = 0; i < numProps; i++) {
				ReturnMessagesObjects addressForServer = ReturnMessagesObjects
						.parseObject((SoapObject) response.getProperty(i));
				object.add(addressForServer);
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnCourtOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar : ReturnMessage Response Error: " + e);

			throw e;
		}

	}


	
	public ReturnAppOptionsObject getAppOptions(String sessionID) throws Exception {
	
	SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_APP_OPTIONS);
		
		requestSoap.addProperty("sessionID", sessionID);
		
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Log.d("", "Special nates is " + requestSoap.toString());
		logger.addLog("TriStar :ReturnAppOptions Request: " + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_APP_OPTIONS,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			Log.d(" response", "" + response.toString());
			logger.addLog("TriStar :ReturnAppOptions Response: " + response.toString());

			ReturnAppOptionsObject object = ReturnAppOptionsObject
						.parseObject(response);
				
			return object;

		} catch (Exception e) {

			logger.addLog("TriStar :ReturnAppOptions Response Error : " + e.toString());

			throw e;
		}
	}
	
	
	public ArrayList<ReturnRouteTasksObject> getReturnAppOptions(String sessionID) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_ROUTE_TASKS);
		Log.d("methodcall", "" + requestSoap);
		requestSoap.addProperty("sessionID", sessionID);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :ReturnRouteTasks Request : " + requestSoap.toString());


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_ROUTE_TASKS,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			Log.d(" response", "" + response.toString());

			logger.addLog("TriStar :ReturnRouteTasks Response : " + response.toString());


			int numProps = response.getPropertyCount();

			ArrayList<ReturnRouteTasksObject> object = new ArrayList<ReturnRouteTasksObject>();
			for (int i = 0; i < numProps; i++) {
				ReturnRouteTasksObject attachedpdf = ReturnRouteTasksObject
						.parseObject((SoapObject) response.getProperty(i));
				object.add(attachedpdf);
			}
			Log.d(" result", "" + response.toString());
			return object;

		} catch (Exception e) {

			logger.addLog("TriStar :ReturnRouteTasks Response Error: " + e);


			throw e;
		}

	}
	
	
	
	public ArrayList<ReturnRouteTaskStatusObject> getReturnRouteTaskStatus(String sessionID, int taskcode) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_ROUTE_TASKS_STATUS);
		Log.d("methodcall", "" + requestSoap);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("taskCode", taskcode);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :ReturnRouteTaskStatus Request: " + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_ROUTE_TASKS_STATUS,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			Log.d(" response", "" + response.toString());

			logger.addLog("TriStar :ReturnRouteTaskStatus Response Error: " + response.toString());


			int numProps = response.getPropertyCount();

			ArrayList<ReturnRouteTaskStatusObject> object = new ArrayList<ReturnRouteTaskStatusObject>();
			for (int i = 0; i < numProps; i++) {
				ReturnRouteTaskStatusObject attachedpdf = ReturnRouteTaskStatusObject
						.parseObject((SoapObject) response.getProperty(i));
				object.add(attachedpdf);
			}
			Log.d(" result", "" + response.toString());
			return object;

		} catch (Exception e) {

			logger.addLog("TriStar :ReturnRouteTaskStatus Response Error: " + e.toString());

			throw e;
		}

	}
	
	
	

	public ArrayList<CodeAndTitle> getAttachedPDFList(String sessionID,
			String theWorkorder) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_GET_ATTACHED_PDF_LIST);
		Log.d("methodcall", "" + requestSoap);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("theWorkorder", theWorkorder);
		Log.d("Session ID", "Special nates is " + sessionID);
		Log.d("theWorkorder ID", "Special nates is " + theWorkorder);
		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		Log.d("PdfList", "Special nates is " + requestSoap.toString());

		logger.addLog("TriStar :GetAttachedPDFList for Code and Title Response : " + requestSoap.toString());


		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_GET_ATTACHED_PDF_LIST,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			Log.d("pdflist response", "" + response.toString());

			logger.addLog("TriStar :GetAttachedPDFList for Code and Title Response Error: " + response.toString());


			int numProps = response.getPropertyCount();

			ArrayList<CodeAndTitle> object = new ArrayList<CodeAndTitle>();
			for (int i = 0; i < numProps; i++) {
				CodeAndTitle attachedpdf = CodeAndTitle
						.parseObject((SoapObject) response.getProperty(i));
				object.add(attachedpdf);
			}
			Log.d("pdflist result", "" + response.toString());
			return object;

		} catch (Exception e) {
			logger.addLog("TriStar :GetAttachedPDFList for Code and Title Response Error: " + e.toString());

			throw e;
		}

	}

	public ArrayList<DiligenceForProcess> getDiligenceForProcess(
			String sessionID, String theWorkorder, int theAddressLineitem)
			throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_DILIGENCE);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("theWorkorder", theWorkorder); 
		requestSoap.addProperty("theAddressLineitem", theAddressLineitem);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :ReturnDiligence Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnDiligence Webmethod Request: ","" + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(TristarConstants.SOAP_METHOD_RETURN_DILIGENCE,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();
			Log.d("diligence result", "" + response);

			logger.addLog("TriStar :ReturnDiligence Webmethod Response: " + response.toString());
			Log.d("ReturnDiligence Webmethod Response: ","" + response.toString());

			ArrayList<DiligenceForProcess> object = new ArrayList<DiligenceForProcess>();
			for (int i = 0; i < numProps; i++) {
				DiligenceForProcess diligenceDetail = DiligenceForProcess
						.parseObject((SoapObject) response.getProperty(i));
				object.add(diligenceDetail);

//				long elapsedTime = System.currentTimeMillis() - startTime;
//				System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//				long millis = elapsedTime;  // obtained from StopWatch
//				long minutes = (millis / 1000)  / 60;
//				int seconds = (int) ((millis / 1000) % 60);
//				System.out.println("Sync_ReturnDiligence :" + seconds + " seconds.");
			}                     
			return object;
		} catch (Exception e) {

			logger.addLog("TriStar :ReturnDiligence Webmethod Response Error: " + e.toString());

			throw e;
		}
	}

	public ArrayList<DiligencePhrase> getDiligencePhrase(String sessionID)
			throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_DILIGENCE_PHRASES);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		logger.addLog("TriStar :ReturnDiligencePhrases Webmethod Request : " + requestSoap.toString());
		Log.d("ReturnDiligencePhrases Webmethod Request : ","" + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_DILIGENCE_PHRASES,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			logger.addLog("TriStar :ReturnDiligencePhrases Webmethod Response : " + response.toString());
			Log.d("ReturnDiligencePhrases Webmethod Response : ","" + response.toString());

			int numProps = response.getPropertyCount();
			ArrayList<DiligencePhrase> object = new ArrayList<DiligencePhrase>();
			for (int i = 0; i < numProps; i++) {

					DiligencePhrase diligencePhrase = DiligencePhrase
						.parseObject((SoapObject) response.getProperty(i));
				object.add(diligencePhrase);

			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnDiligencePhrases :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar :ReturnDiligencePhrases Webmethod Response Error: " + e.toString());

			throw e;
		}
	}

	public ArrayList<MannerOfService> getMannerOfService(String sessionID,
			String theCourtStateCode) throws Exception {

//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_MANNER_OF_SERVICE);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("theCourtStateCode", theCourtStateCode);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :ReturnMannerOfService Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnMannerOfService Webmethod Request: ","" + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_MANNER_OF_SERVICE,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar :ReturnMannerOfService Webmethod Response: " + response.toString());
			Log.d("ReturnMannerOfService Webmethod Response: ","" + response.toString());

			ArrayList<MannerOfService> object = new ArrayList<MannerOfService>();
			for (int i = 0; i < numProps; i++) {
				MannerOfService mannerOfService = MannerOfService
						.parseObject((SoapObject) response.getProperty(i));
				object.add(mannerOfService);

//				long elapsedTime = System.currentTimeMillis() - startTime;
//				System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//				long millis = elapsedTime;  // obtained from StopWatch
//				long minutes = (millis / 1000)  / 60;
//				int seconds = (int) ((millis / 1000) % 60);
//				System.out.println("Sync_ReturnMannerOfService :" + seconds + " seconds.");
			}
			return object;
		} catch (Exception e) {

			logger.addLog("TriStar :ReturnMannerOfService Webmethod Response Error: " + e.toString());

			throw e;
		}
	}

	public ArrayList<ProcessAddressForServer> getProcessAddressForServer(
			String sessionID) throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_PROCESS_OPEN_ADDRESSES);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :ReturnProcessOpenAddresses Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnProcessOpenAddresses Webmethod Request: ",""+ requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(TristarConstants.SOAP_METHOD_RETURN_PROCESS_OPEN_ADDRESSES, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();

			logger.addLog("TriStar :ReturnProcessOpenAddresses Webmethod Response: " + response.toString());
			Log.d("ReturnProcessOpenAddresses Webmethod Response: ","" + response.toString());


			if(response.toString().contains("AddressFormattedNewLine1")){
				SessionData.getInstance().setProcessOrderDetail_Address(0);

			}else{
				SessionData.getInstance().setProcessOrderDetail_Address(1);
			}
			ArrayList<ProcessAddressForServer> object = new ArrayList<ProcessAddressForServer>();
			if (response != null && response.getPropertyCount() > 0) {
				int numProps = response.getPropertyCount();

				for (int i = 0; i < numProps; i++) {
					ProcessAddressForServer processAddressForServer = ProcessAddressForServer
							.parseObject((SoapObject) response.getProperty(i));
					Log.d("entity",""+processAddressForServer.isEntity());
					object.add(processAddressForServer);
				}
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnProcessOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar :ReturnProcessOpenAddresses Webmethod Response Error: " + e.toString());

			throw e;
		}

	}



	public String RejectWorkorder(String sessionID, String reason, int addressLineItem, String workorder) throws Exception {

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE, TristarConstants.METHOD_REJECT_WORKORDER);
		requestSoap.addProperty("sessionID", sessionID);
		requestSoap.addProperty("reason", reason);
		requestSoap.addProperty("addressLineItem", addressLineItem);
		requestSoap.addProperty("workorder", workorder);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

		logger.addLog("TriStar :RejectWorkorder Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnProcessOpenAddresses Webmethod Request: ",""+ requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport.call(TristarConstants.SOAP_METHOD_REJECT_WORKORDER, envelope);
//			SoapObject response = (SoapObject) ;

			String response = envelope.getResponse().toString();


			logger.addLog("TriStar :RejectWorkorder Webmethod Response: " + response.toString());
			Log.d("RejectWorkorder Webmethod Response: ","" + response.toString());

			return response;

		} catch (Exception e) {

			logger.addLog("TriStar :RejectWorkorder Webmethod Response Error: " + e.toString());

			throw e;
		}

	}





	public ArrayList<AddressForServer> getPickupOpenAddress(String sessionID)
			throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_PICKUP_OPEN_ADDRESSES);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		logger.addLog("TriStar :ReturnPickupOpenAddresses Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnPickupOpenAddresses Webmethod Request: ","" + requestSoap.toString());

		try {
			httpTransport.call(
					TristarConstants.SOAP_METHOD_RETURN_PICKUP_OPEN_ADDRESSES,
					envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar :ReturnPickupOpenAddresses Webmethod Response: " + response.toString());
			Log.d("ReturnPickupOpenAddresses Webmethod Response: ","" + response.toString());

            if(response.toString().contains("AddressFormattedNewLine1")){
				SessionData.getInstance().setPickupjob_Address(0);
			}else{
				SessionData.getInstance().setPickupjob_Address(1);
			}

			SessionData.getInstance().setMessageInstructions(0);
			ArrayList<AddressForServer> object = new ArrayList<AddressForServer>();
			for (int i = 0; i < numProps; i++) {
				AddressForServer pickUpAddressForServer = AddressForServer
						.parseObject((SoapObject) response.getProperty(i));
				object.add(pickUpAddressForServer);

			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnPickupOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {

			logger.addLog("TriStar :ReturnPickupOpenAddresses Webmethod Response Error : " + e.toString());

			throw e;
		}
	}

	public ArrayList<AddressForServer> getDeliveryOpenAddress(String sessionID)
			throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_DELIVERY_OPEN_ADDRESSES);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		logger.addLog("TriStar :ReturnDeliveryOpenAddresses Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnDeliveryOpenAddresses Webmethod Request: ","" + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport
					.call(TristarConstants.SOAP_METHOD_RETURN_DELIVERY_OPEN_ADDRESSES,
							envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar :ReturnDeliveryOpenAddresses Webmethod Response : " + response.toString());
			Log.d("ReturnDeliveryOpenAddresses Webmethod Response : ","" + response.toString());


			SessionData.getInstance().setMessageInstructions(1);

			if(response.toString().contains("AddressFormattedNewLine1")){
				SessionData.getInstance().setPickupjob_Address(0);
			}else{
				SessionData.getInstance().setPickupjob_Address(1);
			}


			ArrayList<AddressForServer> object = new ArrayList<AddressForServer>();
			for (int i = 0; i < numProps; i++) {
				AddressForServer deliveryAddressForServer = AddressForServer
						.parseObject((SoapObject) response.getProperty(i));
				object.add(deliveryAddressForServer);
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnDeliveryOpenAddresses :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {
			logger.addLog("TriStar :ReturnDeliveryOpenAddresses Webmethod Response Error : " + e.toString());


			throw e;
		}

	}


	public ArrayList<ReturnStatusListObect> getStatusList(String sessionID)
			throws Exception {
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Date resultdate = new Date(startTime);
//		System.out.println(sdf.format(resultdate));
//		Log.d("Method_start time", ""+resultdate);

		SoapObject requestSoap = new SoapObject(TristarConstants.NAME_SPACE,
				TristarConstants.METHOD_RETURN_STATUS_LIST);
		requestSoap.addProperty("sessionID", sessionID);

		SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
		logger.addLog("TriStar :ReturnStatusListObect Webmethod Request: " + requestSoap.toString());
		Log.d("ReturnStatusListObect Webmethod Request: ","" + requestSoap.toString());

		HttpTransportSE httpTransport = new HttpTransportSE(
				TristarConstants.SOAP_ADDRESS);
		try {
			httpTransport
					.call(TristarConstants.SOAP_METHOD_RETURN_STATUS_LIST,
							envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			int numProps = response.getPropertyCount();

			logger.addLog("TriStar :ReturnStatusListObect Webmethod Response : " + response.toString());
			Log.d("ReturnStatusListObect Webmethod Response : ","" + response.toString());

			ArrayList<ReturnStatusListObect> object = new ArrayList<ReturnStatusListObect>();
			for (int i = 0; i < numProps; i++) {
				ReturnStatusListObect StatusList = ReturnStatusListObect
						.parseObject((SoapObject) response.getProperty(i));
				object.add(StatusList);
			}

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Sync_ReturnStatusList :" + seconds + " seconds.");

			return object;
		} catch (Exception e) {
			logger.addLog("TriStar :ReturnStatusListObect Webmethod Response Error : " + e.toString());
			throw e;
		}
	}

	public boolean getIsAudioRecordingOn(String sessionId)throws Exception{

		boolean result = false;

		requestSoap = new SoapObject(TristarConstants.NAME_SPACE,TristarConstants.METHOD_AUDIO_RECORDING);
		requestSoap.addProperty("sessionID",sessionId);
		envelope = getEnvelope(requestSoap);

		HttpTransportSE httpTransportSE = new HttpTransportSE(TristarConstants.SOAP_ADDRESS);
		try{

			httpTransportSE.call(TristarConstants.SOAP_METHOD_AUDIO_RECORDING,envelope);
			String response =  envelope.getResponse().toString();

			Log.d("Audioresponse",response);
			//String str_result = soapObject.getPrimitiveProperty("IsAudioRecordingOnResult");

			if(response == null) {
				result = true;
			}else{

				if (response.contains("true")) {
					result = true;
				} else if (response.contains("false")) {
					result = false;
				}
			}

		}catch (Exception e){
			result = true;
			e.printStackTrace();
		}
		return result;
	}


	public  String UpdateGPSCoordinates(String sessionId, ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjects){
		String result=" ";

		requestSoap = new SoapObject(TristarConstants.NAME_SPACE,TristarConstants.METHOD_UPDATE_GPS_COORDINATES);
		requestSoap.addProperty("sessionID",sessionId);

		SoapObject requestSoapUpdate=new SoapObject(TristarConstants.NAME_SPACE,"updates");

        for (int i = 0; i <serverSubmittedGPSUpdateObjects.size() ; i++) {
            SoapObject requestSoapServerSubmittedGPSUpdate=new SoapObject(TristarConstants.NAME_SPACE,"ServerSubmittedGPSUpdate");

            requestSoapServerSubmittedGPSUpdate.addProperty("AddressLineItem",serverSubmittedGPSUpdateObjects.get(i).getAddressLineItem());
            requestSoapServerSubmittedGPSUpdate.addProperty("LatitudeLongitude",serverSubmittedGPSUpdateObjects.get(i).getLatitudeLongitude());
            requestSoapServerSubmittedGPSUpdate.addProperty("Workorder",serverSubmittedGPSUpdateObjects.get(i).getWorkorder());
            requestSoapServerSubmittedGPSUpdate.addProperty("IsPickup",serverSubmittedGPSUpdateObjects.get(i).isPickup());

//            requestSoapServerSubmittedGPSUpdate.addProperty("ServerSubmittedGPSUpdate",serverSubmittedGPSUpdateObjects);
            requestSoapUpdate.addSoapObject(requestSoapServerSubmittedGPSUpdate);
        }

		requestSoap.addSoapObject(requestSoapUpdate);
		Log.d("UpdateGPSCoordinates_1", "requestSoap_1: "+requestSoap.toString());

		envelope = getEnvelope(requestSoap);
		HttpTransportSE httpTransportSE = new HttpTransportSE(TristarConstants.SOAP_ADDRESS);
		try{
			httpTransportSE.call(TristarConstants.SOAP_METHOD_UPDATE_GPS_COORDINATES,envelope);
			String response=envelope.getResponse().toString();
			result=response;
			Log.d("UpdateGPSCoordinates_1", "response_1 : "+response.toString());

		}catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
