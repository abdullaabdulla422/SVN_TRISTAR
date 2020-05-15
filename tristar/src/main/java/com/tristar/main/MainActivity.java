package com.tristar.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.db.SyncronizeClass;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;

import java.io.File;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class MainActivity extends Activity implements OnClickListener {

	public static SharedPreferences sharedPreferences;
	private SparseIntArray mErrorString;
	Button btn_login, btn_configure;
	EditText txtCompanycode, txtUsername, txtPassword;
	String strCompanyCode, strUserName, strPassword;
	CheckBox chk_validate;
	ImageView img_button;
	String encryptedString, hexString, clearPassword = "";
	String sessionIDToSignOFF;
	Context context;
	String hexstring = "";
	private SharedPreference sharedPreference;
	private SharedPreference shared;
	ImageView settings;

	private boolean isUpdate;
	String id, fname, companyname, username, password, selectedcompany;
	String storecompany, storeusername, storepass, storecompanyname,
			splitfname, slpitlname, splitlpass, splitcompanyname, splitcompany;
	Activity contextt = this;
	private static final int REQUEST_APP_SETTINGS = 168;
	private SessionData sessionData;
	private static final int REQUEST_PERMISSIONS = 20;

	String shared_CompanyName,shared_Companycode,shared_userid,shared_password;

	private static final String[] requiredPermissions = new String[]
			{
			Manifest.permission.INTERNET,
			Manifest.permission.ACCESS_FINE_LOCATION
            /* ETC.. */
	};
	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences("TRISTAR", MODE_PRIVATE);
		storecompany = SessionData.getInstance().getCompanyname();
		storeusername = SessionData.getInstance().getUserlocalname();
		storepass = SessionData.getInstance().getPassworddname();
		storecompanyname = SessionData.getInstance().getCompanynewname();
		selectedcompany = SessionData.getInstance().getSelectedserver();

		Log.d("storedvalue", "" + selectedcompany);
		settings = (ImageView)findViewById(R.id.settings);
		mErrorString = new SparseIntArray();

		DisplayActivity.loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		DisplayActivity.loginPrefsEditors = DisplayActivity.loginPreferencess.edit();

		shared_CompanyName = DisplayActivity.loginPreferencess.getString("Company Name", "");
		shared_Companycode = DisplayActivity.loginPreferencess.getString("Company Code","");
		shared_userid = DisplayActivity.loginPreferencess.getString("user Id","");
		shared_password = DisplayActivity.loginPreferencess.getString("password","");

		if (selectedcompany != null) {
			String[] str = selectedcompany.split(",");
			splitfname = str[0];
			splitlpass = str[1];
			splitcompany = str[2];
			splitcompanyname = str[3];

			Log.d("splitname", "" + selectedcompany);

		}
		context = this;
		initialize();
	}



	private void initialize() {
		sessionData = SessionData.getInstance();
		btn_configure = (Button) findViewById(R.id.btn_configure);
		btn_login = (Button) findViewById(R.id.btn_login);
		txtCompanycode = (EditText) findViewById(R.id.edt_company_code);
		txtUsername = (EditText) findViewById(R.id.edt_username);
		txtPassword = (EditText) findViewById(R.id.edt_password);
		img_button = (ImageView) findViewById(R.id.img_button);

		btn_configure.setOnClickListener(this);
		img_button.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		settings.setOnClickListener(this);

		TristarConstants.SOAP_ADDRESS = sharedPreferences.getString(
				"webServiceUrlD", "");

		DataBaseHelper.getInstance(MainActivity.this);

		if(shared_Companycode != "" || shared_userid != "" || shared_password != "" )
		{
			txtCompanycode.setText(shared_Companycode);
			txtUsername.setText(shared_userid);
			txtPassword.setText(shared_password);
			btn_configure.setText(shared_CompanyName);

//			sessionData.setOldUsername(shared_userid);
//			sessionData.setOldPassword(shared_password);

		} else {

			sessionData.setOldUsername("");
			sessionData.setOldPassword("");
		}

		if (!MainActivity.sharedPreferences.getBoolean("logOut", true)) {
			sessionData.setUsername(sessionData.getOldUsername());
			sessionData.setPassword(sessionData.getOldPassword());
			setLogoutStatus(sharedPreferences, false);
			finish();
			Intent category = new Intent(MainActivity.this,
					ListCategory.class);
			startActivity(category);
		}

		/*if (DataBaseHelper.getInstance().loginTableHasValue()) {

			HashMap<String, String> userInfo = DataBaseHelper.getInstance()
					.getUserInfoValuesFromDB();
			String value = userInfo.get("companyCode");
			if (value != null) {
				txtCompanycode.setText(splitfname);
			}
			value = userInfo.get("userName");
			if (value != null) {
				txtUsername.setText(splitlpass);
				sessionData.setOldUsername(value);
			}
			value = userInfo.get("password");
			if (value != null) {
				txtPassword.setText(splitcompany);
				sessionData.setOldPassword(value);
			}
			if (splitcompanyname != null) {
				btn_configure.setText(splitcompanyname);
			}

			userInfo.clear();
			userInfo = null;
			if (!MainActivity.sharedPreferences.getBoolean("logOut", true)) {
				sessionData.setUsername(sessionData.getOldUsername());
				sessionData.setPassword(sessionData.getOldPassword());
				setLogoutStatus(sharedPreferences, false);
				finish();
				Intent category = new Intent(MainActivity.this,
						ListCategory.class);
				startActivity(category);
			}
		} else {

			sessionData.setOldUsername("");
			sessionData.setOldPassword("");
		}*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_APP_SETTINGS) {
			if (hasPermissions(requiredPermissions)) {
				Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@TargetApi(Build.VERSION_CODES.M)
	public boolean hasPermissions(@NonNull String... permissions) {
		for (String permission : permissions)
			if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
				return false;
		return true;
	}

	@Override
	public void onClick(View v) {

		if (v == btn_login) {
			if (isLoginFormPassedValidation()) {
                SessionData.getInstance().setSynchandler(1);


				DisplayActivity.loginPrefsEditors.putString("Company Name",btn_configure.getText().toString());
				DisplayActivity.loginPrefsEditors.putString("Company Code",txtCompanycode.getText().toString());
				DisplayActivity.loginPrefsEditors.putString("user Id",txtUsername.getText().toString());
				DisplayActivity.loginPrefsEditors.putString("password",txtPassword.getText().toString());
				DisplayActivity.loginPrefsEditors.commit();


				new AsyncLoginTask().execute();
			}

		}

		if(v == settings){

			                Intent intent = new Intent();
							intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							intent.setData(Uri.parse("package:" + getPackageName()));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							startActivity(intent);

////			ActivityCompat.shouldShowRequestPermissionRationale(
////					this,Manifest.permission.MAPS_RECEIVE);
////
////			if (hasPermissions(requiredPermissions)) {
////				Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
////				myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
////				myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
////			}
////
////			//if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////				if (ActivityCompat.shouldShowRequestPermissionRationale(
////						this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////
////
////					Intent intent = new Intent();
////					intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////					Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
////					intent.setData(uri);
////					intent.addCategory(Intent.CATEGORY_DEFAULT);
////					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////					intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
////					context.startActivity(intent);
////					//Log.d(TAG, "Should show rationale");
////
////					// From the developers handbook:
////					// Show an explanation to the user *asynchronously* -- don't block
////					// this thread waiting for the user's response! After the user
////					// sees the explanation, try again to request the permission.
//////					final Handler handler = new Handler();
//////					handler.postDelayed(new Runnable() {
//////						@Override
//////						public void run() {
//////							populateContacts();
//////						}
//////					}, 100);
////				} else {
////					Intent intent = new Intent();
////				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////				Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
////				intent.setData(uri);
////				intent.addCategory(Intent.CATEGORY_DEFAULT);
////				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
////				context.startActivity(intent);
////				}
////				return;
//
//
////			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////				new AlertDialog.Builder(this)
////						.setMessage("Allow Permission")
////						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////							@Override
////							public void onClick(DialogInterface dialog, int which) {
////								ActivityCompat.requestPermissions(MainActivity.this,
////										new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
////										REQUEST_APP_SETTINGS);
////							}
////						}).setNegativeButton(android.R.string.cancel, null);
////			} else {
////				ActivityCompat.requestPermissions(MainActivity.this,
////						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
////						REQUEST_APP_SETTINGS);
////			}
////
////			return;
//
////			} else {
////				//Do the stuff that requires permission...
////			}
////			if (ActivityCompat.shouldShowRequestPermissionRationale(
////					)) {
////				Intent intent = new Intent();
////				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////				Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
////				intent.setData(uri);
////				intent.addCategory(Intent.CATEGORY_DEFAULT);
////				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
////				context.startActivity(intent);
////			}
//			//Context appContext = this.getApplicationContext();
//			//PermissionsHelper.show(this);
//
//			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//					android.Manifest.permission.CAMERA)) {
//
//
//				// ????????????????????????????
//				new AlertDialog.Builder(this)
//						.setTitle("Permission")
//						.setMessage("Allow Camera Permission")
//						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								ActivityCompat.requestPermissions(MainActivity.this,
//										new String[]{ android.Manifest.permission.CAMERA},
//										REQUEST_APP_SETTINGS);
//							}
//						})
//						.create()
//						.show();
//				return;
//			}
//
//			// ???????
//			ActivityCompat.requestPermissions(this, new String[]{
//							android.Manifest.permission.CAMERA
//					},
//					REQUEST_APP_SETTINGS);
//			return;
//
////			Intent intent = new Intent(MainActivity.this, RuntimePermissionsActivity.class);
////			startActivity(intent);

		}

		if (v == img_button) {
			final Intent relaunch = new Intent(this, Exiter.class)
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK
															// requires
															// this
							| Intent.FLAG_ACTIVITY_CLEAR_TASK // finish
																// everything
																// else in the
																// task
							| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide

			startActivity(relaunch);
			finish();
		}
		if (v == btn_configure) {

			Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
			startActivity(intent);

		}
	}

	private boolean isLoginFormPassedValidation() {
		String validationMessage = null;
		if ((strCompanyCode = txtCompanycode.getText().toString().trim())
				.length() == 0) {
			validationMessage = "Please enter company code";
		} else if ((strUserName = txtUsername.getText().toString().trim())
				.length() == 0) {
			validationMessage = "Please enter user ID";
		} else if ((strPassword = txtPassword.getText().toString().trim())
				.length() == 0) {
			validationMessage = "Please enter password";
		} else {
			return true;
		}
		new CustomAlertDialog(MainActivity.this, validationMessage).show();
		return false;
	}

	public String getDynamicUrlFromWebservice() throws Exception {

		String encryptedStringForWS = encryptString(strCompanyCode);
		Log.d("Company Code",""+strCompanyCode);

		return WebServiceConsumer.getInstance()
				.getDynamicUrlFromWebserviceByPassingCompanyCode(
						encryptedStringForWS);
	}

	public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {

		String sessionId = "", errorString = null;

		protected void onPreExecute() {
			ProgressBar.showCommonProgressDialog(MainActivity.this,
					"Validating user. please wait...");
		};

		@Override
		protected Void doInBackground(Void... params) {
			try {
				clearPassword = "";
				TristarConstants.SOAP_ADDRESS = decryptTheResponseFromServer(getDynamicUrlFromWebservice())
						+ "samsung.asmx";
				Log.d("URL",""+TristarConstants.SOAP_ADDRESS);

				sessionId = WebServiceConsumer.getInstance()
						.signOn(TristarConstants.SOAP_ADDRESS, strUserName,
								strPassword);

			} catch (Exception e) {
				if (e instanceof ConnectException
						|| e instanceof MalformedURLException) {
					errorString = "No network Connection. Enable Wi-fi or turn on Mobile Data!";
				} else if (e instanceof StringIndexOutOfBoundsException) {
					errorString = "Bad input: Either Company Code or credential is wrong.";
				} else if (e instanceof UnknownHostException) {
					errorString = "The Company URL is not found! Please contact your Administrator.";
				}else if(e instanceof SocketTimeoutException){
					errorString = "The Company URL is not found! Please contact your Administrator.";
				}

				else {
					errorString = "Unable to access Profile: Please contact your Administrator.";
				}

			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (errorString == null) {

				if (sessionData == null
						|| sessionId.equals("rejected")
						|| sessionId.length() == 0
						|| sessionId
								.equals("Signon: server code / server password is invalid")) {
					ProgressBar.dismiss();
					new CustomAlertDialog(MainActivity.this,
							"Login information incorrect.").show();
				} else {
					if (DataBaseHelper.getInstance().loginTableHasValue()) {
						HashMap<String, String> userInfo = DataBaseHelper
								.getInstance().getUserInfoValuesFromDB();
						Log.d("Previous Company ", ""+userInfo.get("companyCode"));
						Log.d("Current Company ", ""+strCompanyCode);

						if (!userInfo.get("companyCode").equalsIgnoreCase(
								strCompanyCode)) {
							DataBaseHelper database = DataBaseHelper
									.getInstance(MainActivity.this);
							database.deleteCategoryReleatedTableInDB1();
							deleteFiles();
						}
					}
					signOnProcess(sessionId);

				}
			} else {
				ProgressBar.dismiss();
				new CustomAlertDialog(MainActivity.this, errorString).show();
			}
		}
	}

	@SuppressLint("CommitPrefEdits")
	private void signOnProcess(String sessionId) {

		Editor editor = MainActivity.sharedPreferences.edit();
		editor.putString("webServiceUrlD", TristarConstants.SOAP_ADDRESS);
		editor.commit();

		Log.d("Previous User ", ""+sessionData.getOldUsername());
		Log.d("Current User ", ""+strUserName);
//		{
			DataBaseHelper database = DataBaseHelper
					.getInstance(MainActivity.this);
			if (!strUserName.equals(sessionData.getOldUsername())
					&& !strPassword.equals(sessionData.getOldPassword())
					&& database.loginTableHasValue()) {


				database.deleteLoginTable();

				database.deleteAllUnSyncData();
				database.deletedatas();

			}
//			{
				ProgressBar.dismiss();
				if (!database.loginTableHasValue()) {
					sessionData.setOldPassword(strPassword);
					sessionData.setOldUsername(strUserName);
				} else {
					database.deleteLoginTable();
				}

				try {
					if (database.insertValuesInLocalTable(strCompanyCode,
							strUserName, strPassword)) {
						sessionData.setUsername(strUserName);
						sessionData.setPassword(strPassword);
						SyncronizeClass.instance().syncronizeMainMethod(MainActivity.this);
					} else {
						new CustomAlertDialog(MainActivity.this,
								"Crediantials are not updated in Local database!")
								.show();
					}
				} catch (Exception e) {
					new CustomAlertDialog(MainActivity.this,
							"Crediantials are not updated in Local database!")
							.show();
				}

	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {

				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}



	public static void deleteFiles() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"WinServe_Data");
		deleteDirectory(mediaStorageDir);

	}

	@SuppressLint("DefaultLocale")
	public String encryptString(String strinToEncrypt) {
		String theString = "The quick brown fox jumped over the lazy dog's back 1234567890";
		String clearPassword;
		@SuppressWarnings("unused")
		int theSum = 0;
		String returnString = "";
		clearPassword = strinToEncrypt.replaceAll("\\s", "");
		if (clearPassword.length() >= theString.length()
				|| clearPassword.length() > 95) {
			returnString = "";
			return returnString;
		}

		encryptedString = "";
		for (int a = 0; a < clearPassword.length(); a++) {
			int ascii = theString.charAt(a) + clearPassword.charAt(a);
			theSum += clearPassword.charAt(a);

			hexString = String.format("%x", ascii);
			encryptedString = encryptedString.concat(hexString);
		}
		returnString = encryptedString.toLowerCase();
		return returnString;
	}

	public String decryptTheResponseFromServer(String StringToDecrypt)
			throws Exception {

		String decryptedUrl = "";
		try {
			String seed = "The quick brown fox jumped over the lazy dogs back 1234567890";
			for (int a = 0; a < StringToDecrypt.length(); a += 2) {
				int Nowis = 0;
				switch (StringToDecrypt.charAt(a)) {
				case '0':
					Nowis = 16 * 0;
					break;
				case '1':
					Nowis = 16 * 1;
					break;
				case '2':
					Nowis = 16 * 2;
					break;
				case '3':
					Nowis = 16 * 3;
					break;
				case '4':
					Nowis = 16 * 4;
					break;
				case '5':
					Nowis = 16 * 5;
					break;
				case '6':
					Nowis = 16 * 6;
					break;
				case '7':
					Nowis = 16 * 7;
					break;
				case '8':
					Nowis = 16 * 8;
					break;
				case '9':
					Nowis = 16 * 9;
					break;
				case 'a':
					Nowis = 16 * 10;
					break;
				case 'b':
					Nowis = 16 * 11;
					break;
				case 'c':
					Nowis = 16 * 12;
					break;
				case 'd':
					Nowis = 16 * 13;
					break;
				case 'e':
					Nowis = 16 * 14;
					break;
				case 'f':
					Nowis = 16 * 15;
					break;
				default:
					Nowis = 0;
					break;
				}
				switch (StringToDecrypt.charAt(a + 1)) {
				case '0':
					Nowis = Nowis + 0;
					break;
				case '1':
					Nowis = Nowis + 1;
					break;
				case '2':
					Nowis = Nowis + 2;
					break;
				case '3':
					Nowis = Nowis + 3;
					break;
				case '4':
					Nowis = Nowis + 4;
					break;
				case '5':
					Nowis = Nowis + 5;
					break;
				case '6':
					Nowis = Nowis + 6;
					break;
				case '7':
					Nowis = Nowis + 7;
					break;
				case '8':
					Nowis = Nowis + 8;
					break;
				case '9':
					Nowis = Nowis + 9;
					break;
				case 'a':
					Nowis = Nowis + 10;
					break;
				case 'b':
					Nowis = Nowis + 11;
					break;
				case 'c':
					Nowis = Nowis + 12;
					break;
				case 'd':
					Nowis = Nowis + 13;
					break;
				case 'e':
					Nowis = Nowis + 14;
					break;
				case 'f':
					Nowis = Nowis + 15;
					break;
				default:
					Nowis = 0;
					break;
				}
				int singleCharacter = seed.charAt((int) Math.floor(a / 2));
				hexstring = Character
						.toString((char) (Nowis - singleCharacter));
				clearPassword = clearPassword + hexstring;
			}
		} catch (Exception e) {
			throw e;
		}
		decryptedUrl = clearPassword;

		return decryptedUrl;
	}

	@Override
	public void onBackPressed() {
		final Intent relaunch = new Intent(this, Exiter.class)
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK
						// requires
						// this
						| Intent.FLAG_ACTIVITY_CLEAR_TASK // finish
						// everything
						// else in the
						// task
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide

		startActivity(relaunch);
		finish();
		super.onBackPressed();
	}

	@SuppressLint("CommitPrefEdits")
	public static void setLogoutStatus(SharedPreferences pref, boolean status) {
		Editor editor = pref.edit();
		editor.putBoolean("logOut", status);
		editor.commit();
	}
}
