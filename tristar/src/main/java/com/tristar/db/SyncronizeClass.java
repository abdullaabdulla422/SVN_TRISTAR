package com.tristar.db;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.main.CourtPOD;
import com.tristar.main.CourtService;
import com.tristar.main.DeliveryPOD;
import com.tristar.main.FinalStatus;
import com.tristar.main.ListCategory;
import com.tristar.main.MainActivity;
import com.tristar.main.ProcessOrder;
import com.tristar.main.R;
import com.tristar.object.PODAttachments;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.SubmitCourtPOD;
import com.tristar.object.SubmitDeliveryPOD;
import com.tristar.object.SubmitDiligence;
import com.tristar.object.SubmitPickupPOD;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.SubmitWebServiceConsumer;
import com.tristar.webutils.WebServiceConsumer;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SyncronizeClass {
	public String sessionIDForFinalSync;
	private static SyncronizeClass instance;
	private Activity parentActivity;
	Context context;
	DataBaseHelper database;
	String sessionID;


	public static SyncronizeClass instance() {
		if (instance == null) {
			instance = new SyncronizeClass();
		}
		return instance;
	}



	public void syncronizeMainMethod(Activity activity) {
		database = DataBaseHelper.getInstance(activity);
		sessionIDForFinalSync = "";
		this.parentActivity = activity;
		new AsyncTask<Void, Void, Void>() {
			String errorMessage = null;

			@Override
			protected void onPreExecute() {
				ProgressBar.showCommonProgressDialog(parentActivity,
						"Sync in Process, please wait..");
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {
					sessionIDForFinalSync = WebServiceConsumer.getInstance().signOn(TristarConstants.SOAP_ADDRESS,
									SessionData.getInstance().getUsername(),
									SessionData.getInstance().getPassword());

					Log.d("UserName",""+SessionData.getInstance().getUsername());
					Log.d("Password",""+SessionData.getInstance().getPassword());
					SessionData.getInstance().setSessionId(sessionIDForFinalSync);
					Log.d("After Sync Values of DB Passed",""+sessionIDForFinalSync);
					if (sessionIDForFinalSync == null
							|| sessionIDForFinalSync.equals("rejected")
							|| sessionIDForFinalSync.length() == 0
							|| sessionIDForFinalSync
							.equals("Signon: server code / server password is invalid")) {
						errorMessage = "Could not authenticate and synchronize, you will be storing data locally. Please sync once you have connectivity.";
					} else {
						errorMessage = syncSecondaryData(parentActivity);
					}
					if (errorMessage == null) {
						DataBaseHelper database = DataBaseHelper.getInstance(parentActivity);
						signOff(database);
					}
				} catch (Exception exception) {
					errorMessage = "Could not authenticate and synchronize, you w ill be storing data locally. Please sync once you have connectivity.";
				} finally {
					if (MainActivity.sharedPreferences.getBoolean("logOut",  false)) {
						try {
							WebServiceConsumer.getInstance().signOff(
									sessionIDForFinalSync);
						} catch (Exception e) {
						}
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				ProgressBar.dismiss();

				if (errorMessage != null) {

					if (MainActivity.sharedPreferences.getBoolean("logOut", false)) {
						MainActivity.setLogoutStatus(MainActivity.sharedPreferences, false);
					}

					if (parentActivity instanceof DeliveryPOD
							|| parentActivity instanceof CourtPOD) {
						Intent intent = new Intent(parentActivity,
								CourtService.class);
						new CustomAlertDialog(parentActivity, errorMessage,
								intent).show();
					} else if (parentActivity instanceof FinalStatus) {
						Intent intent = new Intent(parentActivity,
								ProcessOrder.class);
						new CustomAlertDialog(parentActivity, errorMessage,
								intent).show();
					} else {
						Intent intent = new Intent(parentActivity,
								ListCategory.class);
						new CustomAlertDialog(parentActivity, errorMessage,
								intent).show();
						new CustomAlertDialog(parentActivity, errorMessage)
								.show();
					}

				} else {

					if (MainActivity.sharedPreferences.getBoolean("logOut",
							false)) {
						parentActivity.finish();
						Intent intent = new Intent(parentActivity, MainActivity.class);
						parentActivity.startActivity(intent);
					} else {
						if (parentActivity instanceof ListCategory) { parentActivity.recreate();
						} else {
							parentActivity.finish();
							Intent intent = new Intent(parentActivity, ListCategory.class);
							parentActivity.startActivity(intent);
						}
					}
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	public static void deleteFiles() {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"WinServe_Data");

		if (mediaStorageDir.exists()) {
			for (File file : mediaStorageDir.listFiles()) {
				file.delete();
			}
			mediaStorageDir.delete();
		}
	}

	private String syncSecondaryData(Activity activity) throws Exception {

		DataBaseHelper database = DataBaseHelper.getInstance(activity);
		String error = submitDiligence(database);
		Log.d("submitDiligence",""+submitDiligence(database));
		Log.d("error",""+error);
		if (error == null) {
			error = submitAttemptAttachments(database);
			Log.d("submitAttemptAttachments",""+error);
		}
		if (error == null) {
			error = submitFinalStatus(database);
			Log.d("submitFinalStatus",""+error);
		}
		if (error == null) {
			error = submitFinalAttachments(database);
			Log.d("submitFinalAttachments",""+error);
		}
		if (error == null) {
			error = submitCourtPOD(database);
			Log.d("submitCourtPOD",""+error);
		}
		if (error == null) {
			error = submitCourtPODAttachments(database);
			Log.d("submitCourtPODAttachments",""+error);
		}

		if (error == null) {
			error = submitPickupPOD(database);
			Log.d("submitPickupPOD",""+error);
		}
		if (error == null) {
			error = submitPickupPODAttachments(database);
			Log.d("submitPickupPODAttachments",""+error);
		}
		if (error == null) {
			error = submitDeliveryPOD(database);
			Log.d("submitDeliveryPOD",""+error);
		}
		if (error == null) {
			error = submitDeliveryPODAttachments(database);
			Log.d("submitDeliveryPODAttachments",""+error);
		}
		if (error == null) {
			error = submitCourtStatus(database);
			Log.d("submitCourtStatus",""+error);
		}
		if (error == null) {
			error = submitPickupStatus(database);
			Log.d("submitCourtStatus",""+error);
		}if (error == null) {
			error = submitDeliveryStatus(database);
			Log.d("submitCourtStatus",""+error);
		}

//		if (error == null) {
//			error = submitAddress(database);
//		}

		return error;
	}

	public String submitDiligence(DataBaseHelper database) throws Exception {
		ArrayList<SubmitDiligence> submitDiligenceList = database
				.getSubmitDiligencesValuesFromDBForUploadingToServer();

		if (sessionIDForFinalSync == null
				|| sessionIDForFinalSync.equals("rejected")
				|| sessionIDForFinalSync.length() == 0
				|| sessionIDForFinalSync
				.equals("Signon: server code / server password is invalid")) {
			return "Could not authenticate and synchronize, you will be storing data locally. Please sync once you have connectivity.";
		}
		if (submitDiligenceList != null && submitDiligenceList.size() > 0) {
			try {
				for (SubmitDiligence submitDiligence : submitDiligenceList) {
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitDiligence(sessionIDForFinalSync,
									submitDiligence);
					if (serverCode != null && serverCode.length() > 0
							&& !serverCode.equals("rejected")) {
						Log.d("submitDiligence.getLineItem","Data = "+submitDiligence.getLineItem());
						database.updateSubmitDiligenceTableAfterInsertingToServer(
								submitDiligence.getLineItem(), serverCode);
						database.updateSubmitDiligenceAttachmentsTableWithServerCode(
								submitDiligence.getLineItem(), serverCode);

					} else {
						return ("Submit diligences values are not uploaded!");
					}
				}
				submitDiligenceList.clear();
			} catch (Exception e) {
				return ("Submit diligences values are not uploaded!");
			}
		} else {
		}
		return null;
	}


	public String submitAttemptAttachments(DataBaseHelper database)
			throws Exception {
		ArrayList<SubmitDiligence> submitDiligenceList = database
				.getAttachementsFromSubmitDeligenceAttachmentsTableForUpload();
		if (submitDiligenceList != null && submitDiligenceList.size() > 0) {

			try {
				for (SubmitDiligence submitDiligence : submitDiligenceList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitAttemptAttachments(sessionID,
									submitDiligence);
					Log.d("SubmitAttemptAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						Log.d("submitDiligence.getLineItem","Atachment = "+submitDiligence.getLineItem());
						database.updateSubmitDiligenceAttachmentsTableAfterSync(submitDiligence
								.getLineItem());
						Log.d("Server message", "" + serverCode);
					} else {
						return ("Submit diligences attachment is not uploaded!");
					}
				}
				submitDiligenceList.clear();
			} catch (Exception e) {
				Log.d("error message", "" + e);
				return ("Submit diligences attachment is not uploaded!");

			}
		} else {
		}
		return null;
	}

	public String submitFinalStatus(DataBaseHelper database) throws Exception {
		ArrayList<ProcessAddressForServer> processOrderList;
		processOrderList = database.getFinalStatusToUploadToServer();
		if (processOrderList.size() > 0) {
			try {
				for (ProcessAddressForServer processOrder : processOrderList) {

					SessionData.getInstance().setSubfinalStatus(0);

					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitFinalStatus(sessionIDForFinalSync,
									processOrder);
					Log.d("SubmitFinalStatus",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteFinalStausTableAfterSync(processOrder
								.getLineItem());
					} else {
						return ("Final status of ProcessOrder is not uploaded!");
					}
				}
				processOrderList.clear();
			} catch (Exception e) {

				return ("Final status of ProcessOrder is not uploaded!");
			}

		} else {
		}
		return null;
	}

	public String submitFinalAttachments(DataBaseHelper database)
			throws Exception {
		ArrayList<SubmitDiligence> submitDiligenceList = database
				.getAttachementsFromFinalStatuseAttachmentTableForUpload();
		if (submitDiligenceList != null && submitDiligenceList.size() > 0) {

			try {
				for (SubmitDiligence submitDiligence : submitDiligenceList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitFinalAttachments(sessionID, submitDiligence);

					Log.d("SubmitFinalAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deletesFinalStatementAttachment(submitDiligence
								.getLineItem());
					} else {
						return ("Final status attachment is not uploaded!");
					}
				}
				submitDiligenceList.clear();
			} catch (Exception e) {
				return ("Final status attachment is not uploaded!");
			}
		} else {
		}
		return null;
	}

	public String submitCourtPOD(DataBaseHelper database) throws Exception {
		ArrayList<SubmitCourtPOD> submitCourtPODList = database
				.getSubmitCourtPODValuesFromDBForUploadingToServer();
		SubmitCourtPOD courtPOD = new SubmitCourtPOD();
		if (submitCourtPODList.size() > 0) {
			try {
				for (SubmitCourtPOD submitCourtPOD : submitCourtPODList) {
					courtPOD = submitCourtPOD;
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitCourtPOD(sessionIDForFinalSync,
									submitCourtPOD);


					Log.d("SubmitCourtPOD", "" + serverCode);
					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitCourtPODTableAfterSync(submitCourtPOD
								.getSubmitCourtPODID());
					} else {


//						database.deleteSubmitCourtPODTableAfterSync(submitCourtPOD
//								.getSubmitCourtPODID());
//						//	new CustomAlertDialog(parentActivity, "Orer number "+ submitCourtPOD.getWorkorder()+"  already exists on the server, please contact your administrator. This record has been deleted.").show();
//
//
//						final Dialog dialog = new Dialog(context);
//						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//						dialog.setContentView(R.layout.alertbox);
//
//						TextView text = (TextView) dialog
//								.findViewById(R.id.txt_dia);
//						text.setText("Court Job is not submitted, please contact your administrator. This record has been deleted.");
//
//						Button dialogButton = (Button) dialog
//								.findViewById(R.id.btn_ok);
//
//						dialogButton.setOnClickListener(new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//
//
//
//							}
//						});
//
//						dialog.show();
						Toast.makeText(context, "Court Job is not submitted, please contact your administrator. This record has been deleted.", Toast.LENGTH_LONG).show();

					}
				}
				submitCourtPODList.clear();
			} catch (Exception e) {

//				if(SessionData.getInstance().getPrimaryKey()==1){
				database.deleteSubmitCourtPODTableAfterSync(courtPOD
						.getSubmitCourtPODID());
//				final Dialog dialog = new Dialog(context);
//				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//				dialog.setContentView(R.layout.alertbox);
//
//				TextView text = (TextView) dialog
//						.findViewById(R.id.txt_dia);
//				text.setText("Court Job is not submitted, please contact your administrator. This record has been deleted.");
//
//				Button dialogButton = (Button) dialog
//						.findViewById(R.id.btn_ok);
//
//				dialogButton.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//
//
//
//					}
//				});
//
//				dialog.show();

				Toast.makeText(context, "Court Job is not submitted, please contact your administrator. This record has been deleted.", Toast.LENGTH_LONG).show();


				//	Toast.makeText(context, "Court POD Not Uploaded", Toast.LENGTH_SHORT).show();

//					return
//							"Order number "+ courtPOD.getWorkorder()+"  already exists on the server, please contact your administrator. This record has been deleted.";

//				}else{
//					return "Submit Court POD is not uploaded!";
//				}
			}
		}
		else {
		}
		return null;
	}

//	public String submitAddress(DataBaseHelper database) throws Exception {
//		ArrayList<com.tristar.object.Address> processAddress = new ArrayList<com.tristar.object.Address>();
//		processAddress = database.getprocessaddress();
//		if (processAddress.size() > 0) {
//			try {
//				for (com.tristar.object.Address processOrder : processAddress) {
//					String serverCode = SubmitWebServiceConsumer.getInstance()
//							.AddProcessAddress(sessionIDForFinalSync,
//									processOrder);
//					if (isServerCodeAccepted(serverCode)) {
//						database.deleteAddaddress(processOrder.getLineItem());
//					} else {
//						return ("status of Address is not uploaded!");
//					}
//				}
//				processAddress.clear();
//			} catch (Exception e) {
//
//				return ("status of Address is not uploaded!");
//			}
//
//		} else {
//		}
//		return null;
//	}

	public String submitCourtPODAttachments(DataBaseHelper database)
			throws Exception {
		ArrayList<PODAttachments> podAttachmentList = database
				.getAttachementsFromSubmitCourtPODAttachmentTableForUpload();
		if (podAttachmentList.size() > 0) {
			try {
				for (PODAttachments podAttachment : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer
							.getInstance()
							.SubmitCourtPODAttachments(sessionID, podAttachment);

					Log.d("SubmitCourtPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitCourtPODAttachementTable(podAttachment);
					} else {
						return "Submit Court POD attachment is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit Court POD attachment is not uploaded!";
			}
		} else {
		}
		return null;
	}

	public String submitPickupPOD(DataBaseHelper database) throws Exception {
		ArrayList<SubmitPickupPOD> pickupPODList = database
				.getSubmitPickupPODValuesFromDBForUploadingToServer();
		if (pickupPODList != null && pickupPODList.size() > 0) {
			try {
				for (SubmitPickupPOD dataObject : pickupPODList) {
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitPickupPOD(sessionIDForFinalSync, dataObject);

					Log.d("SubmitPickupPOD",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitPickupPODTableAfterSync(dataObject
								.getSubmitPickupPODID());
					} else {
						return "Submit Pickup POD is not uploaded!";
					}
				}
				pickupPODList.clear();
			} catch (Exception e) {

				return "Submit Pickup POD is not uploaded!";
			}
		} else {
		}
		return null;
	}

	public String submitPickupPODAttachments(DataBaseHelper database)
			throws Exception {
		ArrayList<PODAttachments> podAttachmentList = database
				.getAttachementsFromSubmitPickupPODAttachmentTableForUpload();
		if (podAttachmentList != null && podAttachmentList.size() > 0) {
			try {
				for (PODAttachments dataObject : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitPickupPODAttachments(sessionID, dataObject);

					Log.d("SubmitPickupPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitPickupPODAttachementTable(dataObject);
					} else {
						return "Submit Pickup POD attachment is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit Pickup POD attachment is not uploaded!";
			}
		} else {
		}
		return null;
	}


	public String submitCourtStatus(DataBaseHelper database)
			throws Exception {
		ArrayList<SubmitStatusList> podAttachmentList = database
				.getSubmitStatusValuesFromDBToDisplay();
		if (podAttachmentList != null && podAttachmentList.size() > 0) {
			try {
				for (SubmitStatusList dataObject : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitStatusDirect(sessionID, dataObject);

					Log.d("SubmitPickupPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.setKdeleteSubmitstatuslist(dataObject);
					} else {
						return "Submit status is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit status is not uploaded!";
			}
		} else {
		}
		return null;
	}

	public String submitPickupStatus(DataBaseHelper database)
			throws Exception {
		ArrayList<SubmitStatusList> podAttachmentList = database
				.getSubmitPickupStatusValuesFromDBToDisplay();
		if (podAttachmentList != null && podAttachmentList.size() > 0) {
			try {
				for (SubmitStatusList dataObject : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitStatusDirect(sessionID, dataObject);

					Log.d("SubmitPickupPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.setKdeleteSubmitpickupstatuslist(dataObject);
					} else {
						return "Submit status is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit status is not uploaded!";
			}
		} else {
		}
		return null;
	}


	public String submitDeliveryStatus(DataBaseHelper database)
			throws Exception {
		ArrayList<SubmitStatusList> podAttachmentList = database
				.getSubmitDeliveryStatusValuesFromDBToDisplay();
		if (podAttachmentList != null && podAttachmentList.size() > 0) {
			try {
				for (SubmitStatusList dataObject : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitStatusDirect(sessionID, dataObject);

					Log.d("SubmitPickupPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.setKdeleteSubmitdeliverystatuslist(dataObject);
					} else {
						return "Submit status is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit status is not uploaded!";
			}
		} else {
		}
		return null;
	}


	public String submitDeliveryPOD(DataBaseHelper database) throws Exception {
		ArrayList<SubmitDeliveryPOD> deliveryPODList = database
				.getSubmitDeliveryPODValuesFromDBForUploadingToServer();
		if (deliveryPODList != null && deliveryPODList.size() > 0) {
			try {
				for (SubmitDeliveryPOD dataObject : deliveryPODList) {
					String serverCode = SubmitWebServiceConsumer.getInstance()
							.SubmitDeliveryPOD(sessionIDForFinalSync,
									dataObject);

					Log.d("SubmitDeliveryPOD",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitDeliveryPODTableAfterSync(dataObject
								.getSubmitDeliveryPODID());
					} else {
						return "Submit Delivery POD is not uploaded!";
					}
				}
				deliveryPODList.clear();
			} catch (Exception e) {

				return "Submit Delivery POD is not uploaded!";
			}
		} else {
		}
		return null;
	}

	public String submitDeliveryPODAttachments(DataBaseHelper database)
			throws Exception {
		ArrayList<PODAttachments> podAttachmentList = database
				.getAttachementsFromSubmitDeliveryPODAttachmentTableForUpload();
		if (podAttachmentList != null && podAttachmentList.size() > 0) {
			try {
				for (PODAttachments dataObject : podAttachmentList) {
					sessionID = WebServiceConsumer.getInstance().signOn(
							TristarConstants.SOAP_ADDRESS,
							SessionData.getInstance().getUsername(),
							SessionData.getInstance().getPassword());
					String serverCode = SubmitWebServiceConsumer
							.getInstance()
							.SubmitDeliveryPODAttachments(sessionID, dataObject);

					Log.d("SubmitDeliveryPODAttachments",""+serverCode);

					if (isServerCodeAccepted(serverCode)) {
						database.deleteSubmitDeliveryPODAttachementTable(dataObject);
					} else {
						return "Submit Delivery POD attachment is not uploaded!";
					}
				}
				podAttachmentList.clear();
			} catch (Exception e) {

				return "Submit Delivery POD attachment is not uploaded!";
			}
		} else {
		}
		return null;
	}

	public String signOff(DataBaseHelper database) throws Exception {

		{
			SessionData.getInstance().setOldPassword("");
			SessionData.getInstance().setOldUsername("");
		}
		database.deleteCategoryReleatedTableInDB();

		if (parentActivity instanceof MainActivity) {
			MainActivity.setLogoutStatus(MainActivity.sharedPreferences, false);
		}

		return null;
	}

	public boolean isDuplicate(String serverCode) {

		if (serverCode.contains("submitted")) {
			return true;
		}

		return false;

	}

	private boolean isServerCodeAccepted(String serverCode) {
		return (serverCode != null && serverCode.length() > 0 && (serverCode
				.equalsIgnoreCase("accepted") || isDuplicate(serverCode)));
	}


}