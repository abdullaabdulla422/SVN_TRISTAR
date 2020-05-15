package com.tristar.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Geocoder;
import android.util.Log;

import com.tristar.geo.utils.GEOLocation;
import com.tristar.geo.utils.ViewPortBounds;
import com.tristar.object.Address;
import com.tristar.object.AddressForServer;
import com.tristar.object.BSKmlResult;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.DiligencePhrase;
import com.tristar.object.MannerOfService;
import com.tristar.object.PODAttachments;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnStatusListObect;
import com.tristar.object.SplatterAddress;
import com.tristar.object.SplatterCourtPOD;
import com.tristar.object.SubmitCourtPOD;
import com.tristar.object.SubmitDeliveryPOD;
import com.tristar.object.SubmitDiligence;
import com.tristar.object.SubmitFinalStatus;
import com.tristar.object.SubmitPickupPOD;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.SessionData;
import com.tristar.webutils.RestFulWebservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class DataBaseHelper extends SQLiteOpenHelper {
	private static DataBaseHelper instance;

	public static DataBaseHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DataBaseHelper(context);
			instance.databaseConnection(instance);
			instance.getReadableDatabase();
		}
		return instance;
	}

	public static DataBaseHelper getInstance() {
		return instance;
	}

	private static String DB_PATH = "";

	private static String DB_NAME = "TriStarDataBase6.sqlite";
	private SQLiteDatabase database;
	private final Context context;

	public Context getContext() {
		return context;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 4);
		DB_PATH = context.getFilesDir().getParent() + "/databases/";
		this.context = context;
	}

	private boolean isDBExists(){
		return new File(DB_PATH + DB_NAME).exists();
	}

	public void createDataBase() throws IOException {
		boolean mDataBaseExist = isDBExists();
		if (!mDataBaseExist) {
			getReadableDatabase();
			close();
			try {
				copyDataBase();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw ioe;

			}
		}
	}

	private void copyDataBase() throws IOException {
		InputStream mInput = context.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		database = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return database != null;
	}

	@Override
	public synchronized void close() {
		if (database != null)
			database.close();
		super.close();
	}

	public void deleteInstance() {
		instance = null;
	}

	public void databaseConnection(DataBaseHelper eb) {
		try {
			eb.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			eb.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
	}

	final static String kGetLoginTableCount = "SELECT count(*) as num from LoginTable";

	public boolean loginTableHasValue() {
		try {
			@SuppressLint("Recycle")
			Cursor cursor = database.rawQuery(kGetLoginTableCount, null);
			if (cursor.moveToFirst()) {
				do {
					if (cursor.getColumnCount() > 0 && cursor.getInt(0) > 0) {
						return true;
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	final static String kinsertIntoLoginTable = "insert into LoginTable  (CompanyCode, UserName, Password) values(?, ?, ?);";

	public boolean insertValuesInLocalTable(String companyCode,
			String userName, String password) throws Exception {

		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoLoginTable);
			stmt.bindString(1, companyCode);
			stmt.bindString(2, userName);
			stmt.bindString(3, password);
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kGetLoginValues = "SELECT CompanyCode, UserName, Password FROM LoginTable";

	public HashMap<String, String> getUserInfoValuesFromDB() {

		HashMap<String, String> result = new HashMap<String, String>();

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetLoginValues, null);
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				result.put("companyCode", value == null ? "" : value);
				value = cursor.getString(1);
				result.put("userName", value == null ? "" : value);
				value = cursor.getString(2);
				result.put("password", value == null ? "" : value);
			} while (cursor.moveToNext());
		}

		return result;
	}

	final static String kDeleteLoginTable = "DELETE FROM LoginTable";

	public void deleteLoginTable() {
		database.execSQL(kDeleteLoginTable);
	}

	final static String kcheckIfTheUserIsValid = "SELECT count(*) FROM LoginTable WHERE CompanyCode = ? AND UserName  = ?  AND Password =?";

	public boolean checkIfTheUserIsValid(String userName, String password,
			String companyCode) throws Exception {
		try {
			@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kcheckIfTheUserIsValid,
					new String[] { companyCode, userName, password });
			if (cursor.moveToNext()) {
				return cursor.getInt(0) > 0;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}


	final static String kUpdateIntoSProcessOpenAddressTable = "UPDATE ProcessOpenAddressTable SET Latitude = ?, Longitude = ? WHERE AddressLineItem =?";

	public long UpdateProcessOpenAddressTable(ProcessAddressForServer processOrder)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSProcessOpenAddressTable);

			int i = 0;



			stmt.bindString(++i, processOrder.getLatitude());

			stmt.bindString(++i, processOrder.getLongitude());
			stmt.bindLong(++i, processOrder.getAddressLineItem());

			Log.d("Laticude and longitude update",""+processOrder.getLatitude()+" , "+processOrder.getLongitude());



			return stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}


	final static String kUpdateIntoSCountJobTable = "UPDATE CourtOpenAddressTable SET Latitude = ?, Longitude = ? WHERE Workorder =?";

	public long UpdateCourtJobTable(CourtAddressForServer CourtJobs)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSCountJobTable);

			int i = 0;



			stmt.bindString(++i, CourtJobs.getLatitude());

			stmt.bindString(++i, CourtJobs.getLongitude());
			stmt.bindString(++i, CourtJobs.getWorkorder());

			Log.d("Laticude and longitude update",""+CourtJobs.getLatitude()+" , "+CourtJobs.getLongitude());



			return stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}



	final static String kUpdateIntoSPickupJobTable = "UPDATE PickupOpenAddressTable SET Latitude = ?, Longitude = ? WHERE Workorder =?";

	public long UpdatePickupJobTable(AddressForServer PickupJobs)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSPickupJobTable);

			int i = 0;



			stmt.bindString(++i, PickupJobs.getLatitude());

			stmt.bindString(++i, PickupJobs.getLongitude());
			stmt.bindString(++i, PickupJobs.getWorkorder());

			Log.d("Laticude and longitude update",""+PickupJobs.getLatitude()+" , "+PickupJobs.getLongitude());



			return stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}



	final static String kUpdateIntoSDeliveryJobTable = "UPDATE DeliveryOpenAddressTable SET Latitude = ?, Longitude = ? WHERE Workorder =?";

	public long UpdateDeliveryJobTable(AddressForServer PickupJobs)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSDeliveryJobTable);

			int i = 0;



			stmt.bindString(++i, PickupJobs.getLatitude());

			stmt.bindString(++i, PickupJobs.getLongitude());
			stmt.bindString(++i, PickupJobs.getWorkorder());

			Log.d("Laticude and longitude update",""+PickupJobs.getLatitude()+" , "+PickupJobs.getLongitude());


			return stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}




	final static String kinsertIntoProcessOrderTable = "insert into ProcessOpenAddressTable  "
			+ "(Workorder , AddressLineItem,  Servee,  AddressFormattedForDisplay,"
			+ "AddressFormattedForGoogle,  DueDate,   CourtStateCode, PriorityTitle, "
			+ "Entity, AuthorizedAgent, AgentForServiceRelationShipToServee, ServeeIsMale, "
			+ "Age, Height, Weight, Skin, Hair, Eyes, Marks, Latitude, Longitude,Phone, DateReceived," +
			"TimeReceived, HasAttachments, MilestoneCode, MilestoneTitle, CustAddressFormattedForDisplay," +
			" CustAddressFormattedForGoogle , AddressFormattedNewLine1, AddressFormattedNewLine2, DispatcherEmail) "
			+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public void insertProcessOpenAddressFromServer(
			final ProcessAddressForServer processOrder) throws Exception {

		if (processOrder.getLatitude().equals("0.0")
				&& processOrder.getLongitude().equals("0.0")) {

			try {
				BSKmlResult geoResult = DataBaseHelper
						.getGeoCodeAddress(processOrder
								.getAddressFormattedForGoogle());
				if (geoResult != null && geoResult.getLocation() != null) {
					processOrder.setLatitude(geoResult.getLocation()
							.getLatitude() + "");
					processOrder.setLongitude(geoResult.getLocation()
							.getLongitude() + "");
				}
			} catch (Exception e) {
			}
			insertProcessOrder(processOrder);
		} else {
			insertProcessOrder(processOrder);
		}
	}

	public void insertProcessOpenAddressFromServernew(
			final ArrayList<ProcessAddressForServer> processOrder) throws Exception {


		for (int i = 0; i < processOrder.size(); i++) {
			if (processOrder.get(i).getLatitude().equals("0.0")
					&& processOrder.get(i).getLongitude().equals("0.0")) {
				try {
					BSKmlResult geoResult = DataBaseHelper
							.getGeoCodeAddress(processOrder.get(i)
									.getAddressFormattedForGoogle());
					if (geoResult != null && geoResult.getLocation() != null) {
						processOrder.get(i).setLatitude(geoResult.getLocation()
								.getLatitude() + "");
						processOrder.get(i).setLongitude(geoResult.getLocation()
								.getLongitude() + "");
					}
				} catch (Exception e) {
				}

			}



		}
	}



	public void insertProcessOrder(ProcessAddressForServer processOrder)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoProcessOrderTable);

			int i = 0;
			stmt.bindString(++i, processOrder.getWorkorder());
			stmt.bindLong(++i, processOrder.getAddressLineItem());
			stmt.bindString(++i, processOrder.getServee());
			stmt.bindString(++i, processOrder.getAddressFormattedForDisplay());

			stmt.bindString(++i, processOrder.getAddressFormattedForGoogle());
			stmt.bindString(++i, processOrder.getDueDate());
			stmt.bindString(++i, processOrder.getCourtStateCode());
			stmt.bindString(++i, processOrder.getPriorityTitle());

			stmt.bindLong(++i, processOrder.isEntity() ? 1 : 0);
			stmt.bindString(++i, processOrder.getAuthorizedAgent());
			stmt.bindString(++i,
					processOrder.getAgentForServiceRelationShipToServee());
			stmt.bindLong(++i, processOrder.isServeeIsMale() ? 1 : 0);

			stmt.bindString(++i, processOrder.getAge());
			stmt.bindString(++i, processOrder.getHeight());
			stmt.bindString(++i, processOrder.getWeight());
			stmt.bindString(++i, processOrder.getSkin());

			stmt.bindString(++i, processOrder.getHair());
			stmt.bindString(++i, processOrder.getEyes());
			stmt.bindString(++i, processOrder.getMarks());
			stmt.bindString(++i, processOrder.getLatitude());

			stmt.bindString(++i, processOrder.getLongitude());
			stmt.bindString(++i, processOrder.getPhone());
			stmt.bindString(++i, processOrder.getDateReceived());
			stmt.bindString(++i, processOrder.getTimeReceived());
			stmt.bindLong(++i, processOrder.isHasAttachments()? 1 : 0);
			stmt.bindLong(++i, processOrder.getMilestoneCode());
			stmt.bindString(++i, processOrder.getMilestoneTitle());
			stmt.bindString(++i, processOrder.getCustAddressFormattedForDisplay());
			stmt.bindString(++i, processOrder.getCustAddressFormattedForGoogle());
			stmt.bindString(++i, processOrder.getDispatcherEmail());

			stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}

	public void insertProcessOrdernew(ProcessAddressForServer processOrder)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoProcessOrderTable);

			int i = 0;
			stmt.bindString(++i, processOrder.getWorkorder());
			stmt.bindLong(++i, processOrder.getAddressLineItem());
			stmt.bindString(++i, processOrder.getServee());
			stmt.bindString(++i, processOrder.getAddressFormattedForDisplay());

			stmt.bindString(++i, processOrder.getAddressFormattedForGoogle());
			stmt.bindString(++i, processOrder.getDueDate());
			stmt.bindString(++i, processOrder.getCourtStateCode());
			stmt.bindString(++i, processOrder.getPriorityTitle());

			stmt.bindLong(++i, processOrder.isEntity() ? 1 : 0);
			stmt.bindString(++i, processOrder.getAuthorizedAgent());
			stmt.bindString(++i,
					processOrder.getAgentForServiceRelationShipToServee());
			stmt.bindLong(++i, processOrder.isServeeIsMale() ? 1 : 0);

			stmt.bindString(++i, processOrder.getAge());
			stmt.bindString(++i, processOrder.getHeight());
			stmt.bindString(++i, processOrder.getWeight());
			stmt.bindString(++i, processOrder.getSkin());

			stmt.bindString(++i, processOrder.getHair());
			stmt.bindString(++i, processOrder.getEyes());
			stmt.bindString(++i, processOrder.getMarks());
			stmt.bindString(++i, processOrder.getLatitude());

			stmt.bindString(++i, processOrder.getLongitude());

			Log.d("Laticude and longitude",""+processOrder.getLatitude()+" , "+processOrder.getLongitude());

			stmt.bindString(++i, processOrder.getPhone());
			stmt.bindString(++i, processOrder.getDateReceived());
			stmt.bindString(++i, processOrder.getTimeReceived());
			stmt.bindLong(++i, processOrder.isHasAttachments()? 1 : 0);
			stmt.bindLong(++i, processOrder.getMilestoneCode());
			stmt.bindString(++i, processOrder.getMilestoneTitle());
			stmt.bindString(++i, processOrder.getCustAddressFormattedForDisplay());
			stmt.bindString(++i, processOrder.getCustAddressFormattedForGoogle());
			stmt.bindString(++i, processOrder.getAddressFormattedNewLine1());
			stmt.bindString(++i, processOrder.getAddressFormattedNewLine2());
			stmt.bindString(++i, processOrder.getDispatcherEmail());

			stmt.executeInsert();
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoMannerOfServiceTable = "insert into MannerOfServiceTable  (Code, State, Title) values(?, ?, ?);";

	public boolean insertMannerOfServicefromServer(
			MannerOfService mannerOfService) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoMannerOfServiceTable);
			int i = 0;
			stmt.bindString(++i, mannerOfService.getCode());
			stmt.bindString(++i, mannerOfService.getState());
			stmt.bindString(++i, mannerOfService.getTitle());

			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoProcessAddressTable = "insert into ProcessAddressTable  (Workorder, LineItem, Street, City, State, Zip, theAddressType,isSync) values(?, ?, ?, ?, ?, ?, ?, ?);";

	public boolean insertProcessAddressfromServer(Address processAddress)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoProcessAddressTable);
			int i = 0;
			stmt.bindString(++i, processAddress.getWorkorder().toString());
			stmt.bindLong(++i, processAddress.getLineItem());
			stmt.bindString(++i, processAddress.getStreet().toString());
			stmt.bindString(++i, processAddress.getCity().toString());
			stmt.bindString(++i, processAddress.getState().toString());
			stmt.bindString(++i, processAddress.getZip().toString());
			stmt.bindLong(++i, processAddress.getTheAddressType());
			stmt.bindLong(++i, 0);

			return stmt.executeInsert() != -1;

		} catch (Exception e) {
			Log.d("Error", "" + e);
			throw e;
		}
	}

	private final static String kGetProcessOrder = "SELECT DISTINCT  Workorder, AddressLineItem, Servee FROM ProcessOpenAddressTable ORDER BY Workorder";

	public ArrayList<ProcessAddressForServer> getProcessOrderValuesFromDBTogetValuesOfReturnDiligenceFromWebservice() {
		ArrayList<ProcessAddressForServer> processOrderArray = new ArrayList<ProcessAddressForServer>();
		ProcessAddressForServer processOrders = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetProcessOrder, null);
		if (cursor.moveToFirst()) {
			do {
				processOrders = new ProcessAddressForServer();
				processOrders.setWorkorder(cursor.getString(0) == null ? ""
						: cursor.getString(0).trim());
				processOrders.setAddressLineItem(cursor.getInt(1) < 0 ? 0
						: cursor.getInt(1));
				processOrders.setServee(cursor.getString(2) == null  ? ""
						: cursor.getString(2).trim());

				processOrderArray.add(processOrders);
			} while (cursor.moveToNext());
		}
		return processOrderArray;
	}

	final static String kinsertIntoReturnDiligence = "INSERT INTO DiligenceTable (Workorder, AddressLineItem, Report, DiligenceDate, DiligenceTime) VALUES (?, ?, ?, ?, ?);";

	public boolean insertReturnDiligencefromServer(
			DiligenceForProcess returnDiligence) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnDiligence);
			int i = 0;
			stmt.bindString(++i, returnDiligence.getWorkorder());
			stmt.bindLong(++i, returnDiligence.getAddressLineItem());
			stmt.bindString(++i, returnDiligence.getReport());
			stmt.bindString(++i, returnDiligence.getDiligenceDate());
			stmt.bindString(++i, returnDiligence.getDiligenceTime());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoReturnDiligencePhrases = "INSERT INTO DeligencePhrasesTable (DeligenceCode, DeligenceTitle, DeligencePhoneTitle) VALUES (?, ?, ?)";

	public boolean insertReturnDiligencePhrasesfromServer(
			DiligencePhrase returnDiligencePhrases) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnDiligencePhrases);
			int i = 0;
			stmt.bindLong(++i, returnDiligencePhrases.getCode());
			stmt.bindString(++i, returnDiligencePhrases.getPhoneTitle());
			stmt.bindString(++i, returnDiligencePhrases.getTitle());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}
	final static String kinsertIntoReturnSubmitStatusTable = "INSERT INTO SubmitStatusTable (isSync, workorder, lineitem, statusdate, statustime, report, servercode, datetimesubmitted) VALUES (?, ?, ?, ?, ?, ?, ?,?)";

	public boolean insertsubmitStatusfromServer(
			SubmitStatusList returnsubmitPhrases) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnSubmitStatusTable);
			int i = 0;
			stmt.bindLong(++i, 0);
			stmt.bindString(++i, returnsubmitPhrases.getWorkorder());
			Log.d("Get INserted Work",""+ returnsubmitPhrases.getWorkorder());

			stmt.bindLong(++i, returnsubmitPhrases.getLineitem());
			stmt.bindString(++i, returnsubmitPhrases.getStatusDate());
			stmt.bindString(++i, returnsubmitPhrases.getStatusTime());
			stmt.bindString(++i, returnsubmitPhrases.getReport());
			stmt.bindString(++i, returnsubmitPhrases.getServerCode());
			stmt.bindString(++i, returnsubmitPhrases.getDateTimeSubmitted());

			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}


	public String check_SubmitStatusTable(String workorder)
	{
		String q = "SELECT * FROM SubmitStatusTable WHERE workorder = '" + workorder + "'";

		String result = "";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.rawQuery(q,null);

		if (c != null)
		{
			if (c.getCount() == 0)
			{
				result = "no workorder";
			}
			else
			{
				while (c.moveToNext())
				{
					result = c.getString(c.getColumnIndex("workorder"));
				}
			}

		}

		return result;

	}

	public void update_SubmitStatusTable(SubmitStatusList submitStatusList)
	{

		String workorder = submitStatusList.getWorkorder();

		ContentValues values = new ContentValues();
		values.put("isSync", 0);
		values.put("workorder", submitStatusList.getWorkorder());
		values.put("lineitem", submitStatusList.getLineitem());
		values.put("statusdate", submitStatusList.getStatusDate());
		values.put("statustime", submitStatusList.getStatusTime());
		values.put("report", submitStatusList.getReport());
		values.put("servercode", submitStatusList.getServerCode());
		values.put("datetimesubmitted", submitStatusList.getDateTimeSubmitted());

		SQLiteDatabase db = this.getWritableDatabase();
		int result = db.update("SubmitStatusTable", values, "workorder='" + workorder + "'", null);


		Log.d("SubmitStatusTable", " =" + result );

	}




	public String check_SubmitPickupStatusTable(String workorder)
	{
		String q = "SELECT * FROM SubmitPickupStatusTable WHERE workorder = '" + workorder + "'";

		String result = "";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.rawQuery(q,null);

		if (c != null)
		{


			if (c.getCount() == 0)
			{
				result = "no workorder";
			}
			else
			{
				while (c.moveToNext())
				{
					result = c.getString(c.getColumnIndex("workorder"));
				}
			}

		}

		return result;

	}

	public void update_SubmitPickupStatusTable(SubmitStatusList submitStatusList)
	{

		String workorder = submitStatusList.getWorkorder();

		ContentValues values = new ContentValues();
		values.put("isSync", 0);
		values.put("workorder", submitStatusList.getWorkorder());
		values.put("lineitem", submitStatusList.getLineitem());
		values.put("statusdate", submitStatusList.getStatusDate());
		values.put("statustime", submitStatusList.getStatusTime());
		values.put("report", submitStatusList.getReport());
		values.put("servercode", submitStatusList.getServerCode());
		values.put("datetimesubmitted", submitStatusList.getDateTimeSubmitted());

		SQLiteDatabase db = this.getWritableDatabase();
		int result = db.update("SubmitPickupStatusTable", values, "workorder='" + workorder + "'", null);

		Log.d("SubmitStatusTable", " =" + result );

	}




	final static String kinsertIntoReturnSubmitPickupStatusTable = "INSERT INTO SubmitPickupStatusTable (isSync, workorder, lineitem, statusdate, statustime, report, servercode, datetimesubmitted) VALUES (?, ?, ?, ?, ?, ?, ?,?)";

	public boolean insertsubmitPickupStatusfromServer(
			SubmitStatusList returnsubmitPhrases) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnSubmitPickupStatusTable);
			int i = 0;
			stmt.bindLong(++i, 0);
			stmt.bindString(++i, returnsubmitPhrases.getWorkorder());
			Log.d("Get INserted Work",""+ returnsubmitPhrases.getWorkorder());

			stmt.bindLong(++i, returnsubmitPhrases.getLineitem());
			stmt.bindString(++i, returnsubmitPhrases.getStatusDate());
			stmt.bindString(++i, returnsubmitPhrases.getStatusTime());
			stmt.bindString(++i, returnsubmitPhrases.getReport());
			stmt.bindString(++i, returnsubmitPhrases.getServerCode());
			stmt.bindString(++i, returnsubmitPhrases.getDateTimeSubmitted());

			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoReturnSubmitDeliveryStatusTable = "INSERT INTO SubmitDeliveryStatusTable (isSync, workorder, lineitem, statusdate, statustime, report, servercode, datetimesubmitted) VALUES (?, ?, ?, ?, ?, ?, ?,?)";

	public boolean insertsubmitDeliveryStatusfromServer(
			SubmitStatusList returnsubmitPhrases) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnSubmitDeliveryStatusTable);
			int i = 0;
			stmt.bindLong(++i, 0);
			stmt.bindString(++i, returnsubmitPhrases.getWorkorder());
			Log.d("Get INserted Work",""+ returnsubmitPhrases.getWorkorder());

			stmt.bindLong(++i, returnsubmitPhrases.getLineitem());
			stmt.bindString(++i, returnsubmitPhrases.getStatusDate());
			stmt.bindString(++i, returnsubmitPhrases.getStatusTime());
			stmt.bindString(++i, returnsubmitPhrases.getReport());
			stmt.bindString(++i, returnsubmitPhrases.getServerCode());
			stmt.bindString(++i, returnsubmitPhrases.getDateTimeSubmitted());

			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}


	final static String kdeleteSubmitstatuslist = "DELETE FROM SubmitStatusTable  WHERE lineitem =?";

	public void deleteSubmitStatus(int lineItem) {
		database.execSQL(kdeleteSubmitstatuslist, new Object[] { lineItem });
	}

	final static String kdeleteSubmitPickupstatuslist = "DELETE FROM SubmitPickupStatusTable  WHERE lineitem =?";

	public void deleteSubmitPickupStatus(int lineItem) {
		database.execSQL(kdeleteSubmitPickupstatuslist, new Object[] { lineItem });
	}

	final static String kdeleteSubmitDeliverystatuslist = "DELETE FROM SubmitDeliveryStatusTable  WHERE lineitem =?";

	public void deleteSubmitDeliveryStatus(int lineItem) {
		database.execSQL(kdeleteSubmitDeliverystatuslist, new Object[] { lineItem });
	}

	final static String kGetSubmitStatusTable = "SELECT  *  FROM SubmitStatusTable";

	public ArrayList<SubmitStatusList> getSubmitStatusValuesFromDBToDisplay() {
		ArrayList<SubmitStatusList> SubmitstatusArray = new ArrayList<SubmitStatusList>();
	//	Log.d("statuslist workorder", "" + SubmitstatusArray.get(0).getWorkorder());
		SubmitStatusList SubmitStatus = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitStatusTable, null);
		if (cursor.moveToFirst()) {
			do {
				SubmitStatus = new SubmitStatusList();


				SubmitStatus.setWorkorder(cursor.getString(1));
				SubmitStatus.setLineitem(cursor.getInt(2));
				SubmitStatus.setStatusDate(cursor.getString(3));
				SubmitStatus.setStatusTime(cursor.getString(4));
				SubmitStatus.setReport(cursor.getString(5));
				SubmitStatus.setServerCode(cursor.getString(6));
				SubmitStatus.setDateTimeSubmitted(cursor.getString(7));


				SubmitstatusArray.add(SubmitStatus);
				Log.d("statuslist", "" + SubmitstatusArray);
			} while (cursor.moveToNext());
		}

		return SubmitstatusArray;
	}


	final static String kGetSubmitPickupStatusTable = "SELECT  *  FROM SubmitPickupStatusTable";

	public ArrayList<SubmitStatusList> getSubmitPickupStatusValuesFromDBToDisplay() {
		ArrayList<SubmitStatusList> SubmitstatusArray = new ArrayList<SubmitStatusList>();
		//	Log.d("statuslist workorder", "" + SubmitstatusArray.get(0).getWorkorder());
		SubmitStatusList SubmitStatus = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitPickupStatusTable, null);
		if (cursor.moveToFirst()) {
			do {
				SubmitStatus = new SubmitStatusList();


				SubmitStatus.setWorkorder(cursor.getString(1));
				SubmitStatus.setLineitem(cursor.getInt(2));
				SubmitStatus.setStatusDate(cursor.getString(3));
				SubmitStatus.setStatusTime(cursor.getString(4));
				SubmitStatus.setReport(cursor.getString(5));
				SubmitStatus.setServerCode(cursor.getString(6));
				SubmitStatus.setDateTimeSubmitted(cursor.getString(7));


				SubmitstatusArray.add(SubmitStatus);
				Log.d("statuslist", "" + SubmitstatusArray);
			} while (cursor.moveToNext());
		}

		return SubmitstatusArray;
	}



	final static String kGetSubmitDeliveryStatusTable = "SELECT  *  FROM SubmitDeliveryStatusTable";

	public ArrayList<SubmitStatusList> getSubmitDeliveryStatusValuesFromDBToDisplay() {
		ArrayList<SubmitStatusList> SubmitstatusArray = new ArrayList<SubmitStatusList>();
		//	Log.d("statuslist workorder", "" + SubmitstatusArray.get(0).getWorkorder());
		SubmitStatusList SubmitStatus = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitDeliveryStatusTable, null);
		if (cursor.moveToFirst()) {
			do {
				SubmitStatus = new SubmitStatusList();


				SubmitStatus.setWorkorder(cursor.getString(1));
				SubmitStatus.setLineitem(cursor.getInt(2));
				SubmitStatus.setStatusDate(cursor.getString(3));
				SubmitStatus.setStatusTime(cursor.getString(4));
				SubmitStatus.setReport(cursor.getString(5));
				SubmitStatus.setServerCode(cursor.getString(6));
				SubmitStatus.setDateTimeSubmitted(cursor.getString(7));


				SubmitstatusArray.add(SubmitStatus);
				Log.d("statuslist", "" + SubmitstatusArray);
			} while (cursor.moveToNext());
		}

		return SubmitstatusArray;
	}



	final static String kinsertIntoReturnStatusList = "INSERT INTO StatusTable (Report) VALUES (?)";

	public boolean insertReturnStatusListfromServer(
			ReturnStatusListObect returnStatus) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoReturnStatusList);
			int i = 0;

			stmt.bindString(++i, returnStatus.getReport());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}


	final static String kinsertIntoCourtOrderTable = "insert into CourtOpenAddressTable  "
			+ "(Workorder , AddressFormattedForDisplay,  Documents,  Instructions"
			+ ",  FileAndServe,  FileOnly,   Conform, Issue"
			+ ", Record, Copy, Certify, DeliverCourtesyCopy"
			+ ", PriorityTitle, DueDate, Name, Latitude"
			+ ", Longitude, CaseName, CaseNumber, ServeeName, DateReceived" +
			",TimeReceived, Contact, ContactPhone, HasAttachments, MilestoneCode, MilestoneTitle, "
			+ "CustAddressFormattedForDisplay, CustAddressFormattedForGoogle, AddressFormattedNewLine1, AddressFormattedNewLine2) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


	public boolean insertCourtOpenAddressFromServer(
			final CourtAddressForServer courtAddressForServer) throws Exception {
//		if (courtAddressForServer.getLatitude().equals("0.0")
//				&& courtAddressForServer.getLongitude().equals("0.0")) {
//			BSKmlResult geoResult = DataBaseHelper
//					.getGeoCodeAddress(courtAddressForServer
//							.getAddressFormattedForDisplay());
//
//			if (geoResult != null && geoResult.getLocation() != null) {
//				courtAddressForServer.setLatitude(geoResult.getLocation()
//						.getLatitude() + "");
//				courtAddressForServer.setLongitude(geoResult.getLocation()
//						.getLongitude() + "");
//			}
//			return insertCourtOpenAddressOrder(courtAddressForServer);
//		} else {
			return insertCourtOpenAddressOrder(courtAddressForServer);
//		}
	}
	private boolean insertCourtOpenAddressOrder(
			CourtAddressForServer courtAddressForServer) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoCourtOrderTable);
			int i = 0;
			stmt.bindString(++i, courtAddressForServer.getWorkorder());
			stmt.bindString(++i,
					courtAddressForServer.getAddressFormattedForDisplay());
			stmt.bindString(++i, courtAddressForServer.getDocuments());
			stmt.bindString(++i, courtAddressForServer.getInstructions());

			stmt.bindLong(++i, courtAddressForServer.isFileAndServe() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.isFileOnly() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.isConform() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.isIssue() ? 1 : 0);

			stmt.bindLong(++i, courtAddressForServer.isRecord() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.isCopy() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.isCertify() ? 1 : 0);
			stmt.bindLong(++i,
					courtAddressForServer.isDeliverCourtesyCopy() ? 1 : 0);

			stmt.bindString(++i, courtAddressForServer.getPriorityTitle());
			stmt.bindString(++i, courtAddressForServer.getDueDate());
			stmt.bindString(++i, courtAddressForServer.getName());

			stmt.bindString(++i, courtAddressForServer.getLatitude());
			stmt.bindString(++i, courtAddressForServer.getLongitude());
			stmt.bindString(++i, courtAddressForServer.getCaseName());
			stmt.bindString(++i, courtAddressForServer.getCaseNumber());
			stmt.bindString(++i, courtAddressForServer.getServeeName());
			stmt.bindString(++i, courtAddressForServer.getDateReceived());
			stmt.bindString(++i, courtAddressForServer.getTimeReceived());
			stmt.bindString(++i, courtAddressForServer.getContact());
			stmt.bindString(++i, courtAddressForServer.getContactPhone());
			stmt.bindLong(++i,
					courtAddressForServer.isHasAttachments() ? 1 : 0);
			stmt.bindLong(++i, courtAddressForServer.getMilestoneCode());
			stmt.bindString(++i, courtAddressForServer.getMilestoneTitle());
			stmt.bindString(++i, courtAddressForServer.getCustAddressFormattedForDisplay());
			stmt.bindString(++i, courtAddressForServer.getCustAddressFormattedForGoogle());
			stmt.bindString(++i, courtAddressForServer.getAddressFormattedNewLine1());
			stmt.bindString(++i, courtAddressForServer.getAddressFormattedNewLine2());

			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoPickupOrderTable = "insert into PickupOpenAddressTable  (Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle, DueDate, DueTime, Latitude, Longitude, CaseName, CaseNumber, OrderInstructions," +
			" PickupInstructions, ServeeName, Business, DateReceived, TimeReceived, OrderContact, ContactPhone, HasAttachments, MilestoneCode, MilestoneTitle, AddressFormattedNewLine1, AddressFormattedNewLine2) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public boolean insertPickupOpenAddressFromServer(
			AddressForServer addressForServer) throws Exception {
//		if (addressForServer.getLongitude().equals("0.0")) {
//
//			String AddressForDisplay = addressForServer.getAddressFormattedForDisplay();
//			Log.d("Address for display",""+AddressForDisplay);
//			for (int ii = 0; ii < AddressForDisplay.length(); ii++) {
//				Character character = AddressForDisplay.charAt(ii);
//				if (Character.isDigit(character)) {
//
//					AddressForDisplay = AddressForDisplay.substring(ii);
//					//result += character;
//				}
//			}
//
//			if (AddressForDisplay != null && !AddressForDisplay.isEmpty()) {
//				Geocoder coder = new Geocoder(getContext());
//				try {
//					List<android.location.Address> addressList = coder.getFromLocationName(AddressForDisplay, 1);
//					Log.d("Address size",""+addressList.size());
//					if (addressList != null && addressList.size() > 0) {
//						addressForServer.setLatitude(String.valueOf(addressList.get(0).getLatitude()));
//						addressForServer.setLongitude(String.valueOf(addressList.get(0).getLongitude()));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} // end catch
//			}

//			BSKmlResult geoResult = DataBaseHelper
//					.getGeoCodeAddress(addressForServer
//							.getAddressFormattedForDisplay());
//
//			if (geoResult != null && geoResult.getLocation() != null) {
//				addressForServer.setLatitude(geoResult.getLocation()
//						.getLatitude() + "");
//				addressForServer.setLongitude(geoResult.getLocation()
//						.getLongitude() + "");
//			}
			//return insertPickupOpenAddressOrder(addressForServer);
		//} else {
			return insertPickupOpenAddressOrder(addressForServer);
	//	}
	}

	private boolean insertPickupOpenAddressOrder(
			AddressForServer addressForServer) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoPickupOrderTable);
			int i = 0;

			stmt.bindString(++i, addressForServer.getWorkorder());
			stmt.bindLong(++i, addressForServer.getAddressLineItem());
			stmt.bindString(++i,
					addressForServer.getAddressFormattedForDisplay());
			stmt.bindString(++i, addressForServer.getPriorityTitle());
			stmt.bindString(++i, addressForServer.getDueDate());
			stmt.bindString(++i, addressForServer.getDueTime());

			stmt.bindString(++i, addressForServer.getLatitude());
			stmt.bindString(++i, addressForServer.getLongitude());
			stmt.bindString(++i, addressForServer.getCaseName());
			stmt.bindString(++i, addressForServer.getCaseNumber());
			stmt.bindString(++i, addressForServer.getOrderInstructions());
			stmt.bindString(++i, addressForServer.getPickupInstructions());
			stmt.bindString(++i, addressForServer.getServeeName());
			stmt.bindString(++i, addressForServer.getBusiness());
			stmt.bindString(++i, addressForServer.getDateReceived());
			stmt.bindString(++i, addressForServer.getTimeReceived());
			stmt.bindString(++i, addressForServer.getOrderContact());
			stmt.bindString(++i, addressForServer.getContactPhone());
			stmt.bindLong(++i,
					addressForServer.isHasAttachments() ? 1 : 0);
			stmt.bindLong(++i, addressForServer.getMilestoneCode());
			stmt.bindString(++i, addressForServer.getMilestoneTitle());
			stmt.bindString(++i, addressForServer.getAddressFormattedNewLine1());
			stmt.bindString(++i, addressForServer.getAddressFormattedNewLine2());

			return stmt.executeInsert() != -1;
		}

		catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoDeliveryOrderTable = "insert into DeliveryOpenAddressTable (Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle, DueDate, DueTime, Latitude, Longitude, CaseName, CaseNumber, OrderInstructions," +
			" DeliveryInstructions, ServeeName, Business, DateReceived, TimeReceived, OrderContact, ContactPhone, HasAttachments, MilestoneCode, MilestoneTitle, AddressFormattedNewLine1, AddressFormattedNewLine2) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public boolean insertDeliveryOpenAddressFromServer(
			AddressForServer addressForServer) throws Exception {
//		if (addressForServer.getAddressFormattedForDisplay().length()!=0
//				) {
//
//
//
//
//
//			String AddressForDisplay = addressForServer.getAddressFormattedForDisplay();
//			Log.d("Address Length",""+AddressForDisplay.length());
//			for (int ii = 0; ii < AddressForDisplay.length(); ii++) {
//				Character character = AddressForDisplay.charAt(ii);
//				if (Character.isDigit(character)) {
//
//					AddressForDisplay = AddressForDisplay.substring(ii);
//					//result += character;
//				}
//			}
//
//			if (AddressForDisplay != null && !AddressForDisplay.isEmpty()) {
//				Geocoder coder = new Geocoder(getContext());
//				try {
//					List<android.location.Address> addressList = coder.getFromLocationName(AddressForDisplay, 1);
//					Log.d("Address size",""+addressList.size());
//					if (addressList != null && addressList.size() > 0) {
//						addressForServer.setLatitude(String.valueOf(addressList.get(0).getLatitude()));
//						addressForServer.setLongitude(String.valueOf(addressList.get(0).getLongitude()));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} // end catch
//			}
////			BSKmlResult geoResult = DataBaseHelper
////					.getGeoCodeAddress(addressForServer
////							.getAddressFormattedForDisplay());
////
////			if (geoResult != null && geoResult.getLocation() != null) {
////				addressForServer.setLatitude(geoResult.getLocation()
////						.getLatitude() + "");
////				addressForServer.setLongitude(geoResult.getLocation()
////						.getLongitude() + "");
////			}
//			return insertDeliveryOpenAddressOrder(addressForServer);
//		} else {
			return insertDeliveryOpenAddressOrder(addressForServer);
//		}
	}

	private boolean insertDeliveryOpenAddressOrder(
			AddressForServer addressForServer) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoDeliveryOrderTable);
			int i = 0;
			stmt.bindString(++i, addressForServer.getWorkorder());
			stmt.bindLong(++i, addressForServer.getAddressLineItem());
			stmt.bindString(++i,
					addressForServer.getAddressFormattedForDisplay());
			stmt.bindString(++i, addressForServer.getPriorityTitle());

			stmt.bindString(++i, addressForServer.getDueDate());
			stmt.bindString(++i, addressForServer.getDueTime());
			stmt.bindString(++i, addressForServer.getLatitude());
			stmt.bindString(++i, addressForServer.getLongitude());
			stmt.bindString(++i, addressForServer.getCaseName());
			stmt.bindString(++i, addressForServer.getCaseNumber());
			stmt.bindString(++i, addressForServer.getOrderInstructions());
			stmt.bindString(++i, addressForServer.getDeliveryInstructions());
			stmt.bindString(++i, addressForServer.getServeeName());
			stmt.bindString(++i, addressForServer.getBusiness());
			stmt.bindString(++i, addressForServer.getDateReceived());
			stmt.bindString(++i, addressForServer.getTimeReceived());
			stmt.bindString(++i, addressForServer.getOrderContact());
			stmt.bindString(++i, addressForServer.getContactPhone());
			stmt.bindLong(++i,
					addressForServer.isHasAttachments() ? 1 : 0);
			stmt.bindLong(++i, addressForServer.getMilestoneCode());
			stmt.bindString(++i, addressForServer.getMilestoneTitle());
			stmt.bindString(++i, addressForServer.getAddressFormattedNewLine1());
			stmt.bindString(++i, addressForServer.getAddressFormattedNewLine2());
			return stmt.executeInsert() != -1;
		}

		catch (Exception e) {
			throw e;
		}
	}

	final static String kDeleteDeligencePhrasesTable = "DELETE FROM DeligencePhrasesTable";
	final static String kDeleteDiligenceTable = "DELETE FROM DiligenceTable";
	final static String kDeleteMannerOfServiceTable = "DELETE FROM MannerOfServiceTable";
	final static String kDeleteProcessOpenAddressTable = "DELETE FROM ProcessOpenAddressTable";
	final static String kDeleteCourtOpenAddressTable = "DELETE FROM CourtOpenAddressTable";
	final static String kDeletePickupOpenAddressTable = "DELETE FROM PickupOpenAddressTable";
	final static String kDeleteDeliveryOpenAddressTable = "DELETE FROM DeliveryOpenAddressTable";
	final static String kDeleteStatusTable = "DELETE FROM StatusTable";

	public void deleteCategoryReleatedTableInDB() {

		database.execSQL(kDeleteDeligencePhrasesTable);
		database.execSQL(kDeleteDiligenceTable);
		database.execSQL(kDeleteMannerOfServiceTable);
//		database.execSQL(kDeleteProcessOpenAddressTable);
//
//		database.execSQL(kDeleteCourtOpenAddressTable);
//		database.execSQL(kDeletePickupOpenAddressTable);
//		database.execSQL(kDeleteDeliveryOpenAddressTable);
		database.execSQL(kDeleteStatusTable);

	}


	public void deleteCategoryReleatedTableInDB1() {

		database.execSQL(kDeleteDeligencePhrasesTable);
		database.execSQL(kDeleteDiligenceTable);
		database.execSQL(kDeleteMannerOfServiceTable);
		database.execSQL(kDeleteProcessOpenAddressTable);

		database.execSQL(kDeleteCourtOpenAddressTable);
		database.execSQL(kDeletePickupOpenAddressTable);
		database.execSQL(kDeleteDeliveryOpenAddressTable);
		database.execSQL(kDeleteStatusTable);

	}

	public void deleteallcourt(){

        database.execSQL(kDeleteCourtOpenAddressTable);
    }
    public void deleteallpickup(){

        database.execSQL(kDeletePickupOpenAddressTable);
    }
    public void deletealldelivery(){

        database.execSQL(kDeleteDeliveryOpenAddressTable);
    }

	public void deleteAllUnSyncData() {

		database.execSQL("DELETE FROM SubmitFinalStatusTable");
		database.execSQL("DELETE  FROM FinalStatuseAttachmentTable");
		database.execSQL("DELETE FROM SubmitCourtPODTable");
		database.execSQL("DELETE FROM SubmitCourtPODAttachmentTable");

		database.execSQL("DELETE FROM SubmitPickupPODTable");
		database.execSQL("DELETE FROM SubmitPickupPODAttachmentTable");
		database.execSQL("DELETE FROM SubmitDeliveryPODTable");
		database.execSQL("DELETE FROM ProcessAddressTable");
		database.execSQL("DELETE FROM SubmitDeliveryPODAttachmentTable");
	}

	public void deletedatas()
	{
		database.execSQL("DELETE FROM SubmitDiligenceTable");
		database.execSQL("DELETE FROM SubmitDiligenceAttachmentTable");
//		database.execSQL("DELETE FROM DiligencePhraseTable");
	}

	final static String kGetProcessOrderCount = "SELECT count(*) as count from ProcessOpenAddressTable";

	public boolean processOrderTableHasValue() {

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetProcessOrderCount, null);
		if (cursor.moveToFirst()) {
			do {
			    if(SessionData.getInstance().getSynchandler()==1){
                    return false;

                }
				else if(cursor.getColumnCount() > 0 && cursor.getInt(0) > 0) {
					return true;
				}
			} while (cursor.moveToNext());
		}
		return false;
	}

	final static String kGetProcessOrderForListViewAndMapView = "SELECT DISTINCT  "
			+ "ProcessOpenAddressTable.ProcessOpenAddressID,  ProcessOpenAddressTable.Workorder, "
			+ "ProcessOpenAddressTable.AddressFormattedForDisplay, ProcessOpenAddressTable.Servee, ProcessOpenAddressTable.AddressFormattedForGoogle, "
			+ "ProcessOpenAddressTable.Latitude, ProcessOpenAddressTable.Longitude , ProcessOpenAddressTable.HasAttachments ,"
			+" ProcessOpenAddressTable.PriorityTitle , ProcessOpenAddressTable.DueDate , ProcessOpenAddressTable.MilestoneCode , ProcessOpenAddressTable.MilestoneTitle , "
			+ "ProcessOpenAddressTable.CustAddressFormattedForDisplay , ProcessOpenAddressTable.CustAddressFormattedForGoogle, ProcessOpenAddressTable.AddressFormattedNewLine1, ProcessOpenAddressTable.AddressFormattedNewLine2,"
			+ "ProcessOpenAddressTable.DispatcherEmail , ProcessOpenAddressTable.AddressLineItem "
			+ "FROM ProcessOpenAddressTable where not exists(select 1 from SubmitFinalStatusTable  "
			+ "where submitFinalStatusTable.Workorder = ProcessOpenAddressTable.Workorder and "
			+ "SubmitFinalStatusTable.AddressLineItem = ProcessOpenAddressTable.AddressLineItem)";

	public ArrayList<ProcessAddressForServer> getprocessOrderValuesFromTabletoDisplayInListVieAndMapView()
			throws Exception {
		ArrayList<ProcessAddressForServer> processOrderArray = new ArrayList<ProcessAddressForServer>();
		ProcessAddressForServer processOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetProcessOrderForListViewAndMapView, null);
		if (cursor.moveToFirst()) {
			do {
				processOrder = new ProcessAddressForServer();

				processOrder.setProcessOrderID(cursor.getInt(0) != -1 ? cursor
						.getInt(0) : 0);
				processOrder.setWorkorder(cursor.getString(1) == null ? ""
						: cursor.getString(1).trim());
				processOrder
						.setAddressFormattedForDisplay(cursor.getString(2) == null ? ""
								: cursor.getString(2));
				processOrder.setServee(cursor.getString(3) == null ? ""
						: cursor.getString(3));
				processOrder
						.setAddressFormattedForGoogle(cursor.getString(4) == null ? ""
								: cursor.getString(4));

				processOrder.setLatitude(cursor.getString(5));
				processOrder.setLongitude(cursor.getString(6));
				processOrder.setHasAttachments(cursor.getInt(7)>0);
				processOrder.setPriorityTitle(cursor.getString(8) == null ?""
						: cursor.getString(8).trim());
				processOrder.setDueDate(cursor.getString(9) == null ?""
						: cursor.getString(9).trim());
				processOrder.setMilestoneCode(cursor.getInt(10)!= -1 ? cursor
						.getInt(10) : 0);
				processOrder.setMilestoneTitle(cursor.getString(11)== null ? ""
						: cursor.getString(11).trim());

				processOrder.setCustAddressFormattedForDisplay(cursor.getString(12)== null ? ""
						: cursor.getString(12).trim());

				processOrder.setCustAddressFormattedForGoogle(cursor.getString(13)== null ? ""
						: cursor.getString(13).trim());

				processOrder.setAddressFormattedNewLine1(cursor.getString(14)== null ? ""
						: cursor.getString(14).trim());

				processOrder.setAddressFormattedNewLine2(cursor.getString(15)== null ? ""
						: cursor.getString(15).trim());

				processOrder.setDispatcherEmail(cursor.getString(cursor.getColumnIndex("DispatcherEmail")));
                processOrder.setAddressLineItem(cursor.getInt(cursor.getColumnIndex("AddressLineItem")));

				processOrderArray.add(processOrder);
			} while (cursor.moveToNext());
		}
		return processOrderArray;
	}



//	final static String kinsertIntoReturnSubmitStatusTable = "INSERT INTO SubmitStatusTable (Workorder, Lineitem, StatusDate, StatusTime, Report, ServerCode, DateTimeSubmitted) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//	public boolean insertReturnsubmitPhrasesfromServer(
//			SubmitStatusList returnsubmitPhrases) throws Exception {
//		try {
//			SQLiteStatement stmt = database
//					.compileStatement(kinsertIntoReturnSubmitStatusTable);
//			int i = 0;
//			stmt.bindString(++i, returnsubmitPhrases.getWorkorder());
//			stmt.bindLong(++i, returnsubmitPhrases.getLineitem());
//			stmt.bindString(++i, returnsubmitPhrases.getStatusDate());
//			stmt.bindString(++i, returnsubmitPhrases.getStatusTime());
//			stmt.bindString(++i, returnsubmitPhrases.getReport());
//			stmt.bindString(++i, returnsubmitPhrases.getServerCode());
//			stmt.bindString(++i, returnsubmitPhrases.getDateTimeSubmitted());
//			return stmt.executeInsert() != -1;
//		} catch (Exception e) {
//			throw e;
//		}
//	}

	final static String kGetProcessOrderForListViewAndJob = "SELECT  * FROM ProcessOpenAddressTable";

	public ArrayList<ProcessAddressForServer> getprocessOrderValuesFromTabletoDisplayInListView()
			throws Exception {
		ArrayList<ProcessAddressForServer> processOrderArray = new ArrayList<ProcessAddressForServer>();
		ProcessAddressForServer processOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetProcessOrderForListViewAndJob, null);
		if (cursor.moveToFirst()) {
			do {
				processOrder = new ProcessAddressForServer();
				processOrder.setHasAttachments(cursor.getInt(0) > 0);
				processOrder.setTimeReceived(cursor.getString(1));
				processOrder.setDateReceived(cursor.getString(2));
				processOrder.setPhone(cursor.getString(3));
				processOrder.setProcessOrderID(cursor.getInt(4));
				processOrder.setWorkorder(cursor.getString(5).trim());
				processOrder.setAddressLineItem(cursor.getInt(6));

				processOrder.setServee(cursor.getString(7));
				processOrder.setAddressFormattedForDisplay(cursor.getString(8));
				processOrder.setAddressFormattedForGoogle(cursor.getString(9));
				processOrder.setDueDate(cursor.getString(10));

				processOrder.setCourtStateCode(cursor.getString(11));
				processOrder.setPriorityTitle(cursor.getString(12));
				processOrder.setEntity(cursor.getInt(13) > 0);
				processOrder.setAuthorizedAgent(cursor.getString(14));

				processOrder.setAgentForServiceRelationShipToServee(cursor
						.getString(15));
				processOrder.setServeeIsMale(cursor.getInt(16) > 0);
				processOrder.setAge(cursor.getString(17));
				processOrder.setHeight(cursor.getString(18));

				processOrder.setWeight(cursor.getString(19));
				processOrder.setSkin(cursor.getString(20));
				processOrder.setHair(cursor.getString(21));
				processOrder.setEyes(cursor.getString(22));

				processOrder.setMarks(cursor.getString(23));
				processOrder.setLatitude(cursor.getString(24));
				processOrder.setLongitude(cursor.getString(25));
				processOrder.setDispatcherEmail(cursor.getString(cursor.getColumnIndex("DispatcherEmail")));

				processOrderArray.add(processOrder);
			} while (cursor.moveToNext());
		}
		return processOrderArray;
	}


	public Integer delete_ProcessOpenAddressID(int id)
	{

		Integer i = null;

		SQLiteDatabase db = this.getWritableDatabase();
		i = db.delete("ProcessOpenAddressTable", "ProcessOpenAddressID=?", new String[]{String.valueOf(id)});
		return i;
	}





	final static String kGetProcessOrderFromDB = "SELECT * FROM ProcessOpenAddressTable WHERE ProcessOpenAddressID =?";

	public ProcessAddressForServer getProcessOrderValuesFromDBToDisplayInDetailView(
			int processOrderID) throws Exception {
		ProcessAddressForServer processOrder = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetProcessOrderFromDB,
				new String[] { processOrderID + "" });

		if (cursor.moveToFirst()) {
			do {

				processOrder = new ProcessAddressForServer();
				processOrder.setHasAttachments(cursor.getInt(0) > 0);
				processOrder.setTimeReceived(cursor.getString(1));
				processOrder.setDateReceived(cursor.getString(2));
				processOrder.setPhone(cursor.getString(3));
				processOrder.setProcessOrderID(cursor.getInt(4));
				processOrder.setWorkorder(cursor.getString(5).trim());
				processOrder.setAddressLineItem(cursor.getInt(6));

				processOrder.setServee(cursor.getString(7));
				processOrder.setAddressFormattedForDisplay(cursor.getString(8));
				processOrder.setAddressFormattedForGoogle(cursor.getString(9));
				processOrder.setDueDate(cursor.getString(10));

				processOrder.setCourtStateCode(cursor.getString(11));
				processOrder.setPriorityTitle(cursor.getString(12));
				processOrder.setEntity(cursor.getInt(13) > 0);
				processOrder.setAuthorizedAgent(cursor.getString(14));

				processOrder.setAgentForServiceRelationShipToServee(cursor
						.getString(15));
				processOrder.setServeeIsMale(cursor.getInt(16) > 0);
				processOrder.setAge(cursor.getString(17));
				processOrder.setHeight(cursor.getString(18));

				processOrder.setWeight(cursor.getString(19));
				processOrder.setSkin(cursor.getString(20));
				processOrder.setHair(cursor.getString(21));
				processOrder.setEyes(cursor.getString(22));

				processOrder.setMarks(cursor.getString(23));
				processOrder.setLatitude(cursor.getString(24));
				processOrder.setLongitude(cursor.getString(25));
				processOrder.setMilestoneCode(cursor.getInt(26));
				processOrder.setMilestoneTitle(cursor.getString(27));
				processOrder.setCustAddressFormattedForDisplay(cursor.getString(28));
				processOrder.setCustAddressFormattedForGoogle(cursor.getString(29));
				processOrder.setAddressFormattedNewLine1(cursor.getString(30));
				processOrder.setAddressFormattedNewLine2(cursor.getString(31));
				processOrder.setDispatcherEmail(cursor.getString(cursor.getColumnIndex("DispatcherEmail")));


			} while (cursor.moveToNext());
		}

		return processOrder;
	}

	final static String kGetDeligencePhrasesTable = "SELECT  *  FROM DeligencePhrasesTable";

	public ArrayList<DiligencePhrase> getStatusValuesFromDBToDisplayIndiligencesView() {
		ArrayList<DiligencePhrase> diligencesPhrasesArray = new ArrayList<DiligencePhrase>();
		DiligencePhrase diligencePhrase = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetDeligencePhrasesTable, null);
		if (cursor.moveToFirst()) {
			do {
				diligencePhrase = new DiligencePhrase();

				diligencePhrase.setCode(cursor.getInt(0));
				diligencePhrase.setTitle(cursor.getString(1));
				diligencePhrase.setPhoneTitle(cursor.getString(2));

				diligencesPhrasesArray.add(diligencePhrase);
			} while (cursor.moveToNext());
		}

		return diligencesPhrasesArray;
	}


	final static String kGetStatusTable = "SELECT  *  FROM StatusTable";

	public ArrayList<ReturnStatusListObect> getStatusValuesFromDBToDisplay() {
		ArrayList<ReturnStatusListObect> statusArray = new ArrayList<ReturnStatusListObect>();
		ReturnStatusListObect Status = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetStatusTable, null);
		if (cursor.moveToFirst()) {
			do {
				Status = new ReturnStatusListObect();


				Status.setReport(cursor.getString(0));


				statusArray.add(Status);
			} while (cursor.moveToNext());
		}

		return statusArray;
	}

	final static String kGetReturnDeligenceTable = "SELECT * FROM DiligenceTable WHERE Workorder = ?  AND AddressLineItem =?";

	public ArrayList<DiligenceForProcess> getReturnDiligenceValuesFromDBToDisplay(
			String workOrder, int addressLineItem) {
		ArrayList<DiligenceForProcess> diligenceForProcessArray = new ArrayList<DiligenceForProcess>();
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetReturnDeligenceTable,
				new String[] { workOrder, addressLineItem + "" });
		if (cursor.moveToFirst()) {
			do {
				DiligenceForProcess diligenceForProcess = new DiligenceForProcess();
				diligenceForProcess.setWorkorder(cursor.getString(0).trim());
				diligenceForProcess.setAddressLineItem(cursor.getInt(1));
				diligenceForProcess.setReport(cursor.getString(2));
				diligenceForProcess.setDiligenceDate(cursor.getString(3));
				diligenceForProcess.setDiligenceTime(cursor.getString(4));
				diligenceForProcessArray.add(diligenceForProcess);
			} while (cursor.moveToNext());
		}

		return diligenceForProcessArray;
	}

	final static String kGetSubmitCourtOrder = "SELECT DISTINCT  SubmitCourtPODID,  Workorder,comments,"
			+ "Date, Time, FeeAdvance, Weight, Pieces, WaitTime, Latitude, Longtitude FROM SubmitCourtPODTable";

	public ArrayList<CourtAddressForServer> getSubmitCourtOrder() {
		ArrayList<CourtAddressForServer> courtAddressArray = new ArrayList<CourtAddressForServer>();
		CourtAddressForServer courtAddress = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitCourtOrder, null);
		if (cursor.moveToFirst()) {
			do {
				courtAddress = new CourtAddressForServer();
				courtAddress.setCourtOpenAddressID(cursor.getInt(0));
				courtAddress.setWorkorder(cursor.getString(1).trim());
				courtAddress.setComments(cursor.getString(2).trim());
				courtAddress.setDate(cursor.getString(3).trim());
				courtAddress.setTime(cursor.getString(4).trim());
				courtAddress.setFeeAdvance(cursor.getString(5).trim());
				courtAddress.setWeight(cursor.getString(6).trim());
				courtAddress.setPieces(cursor.getString(7).trim());
				courtAddress.setWaitTime(cursor.getString(8).trim());
				courtAddress.setLat(cursor.getString(9).trim());
				courtAddress.setLng(cursor.getString(10).trim());
				courtAddressArray.add(courtAddress);
			} while (cursor.moveToNext());
		}

		return courtAddressArray;
	}

	final static String kGetCourtOrderForListViewAndMapView =
			"SELECT DISTINCT  CourtOpenAddressID,  Workorder, AddressFormattedForDisplay," +
					" Documents, Instructions, PriorityTitle, DueDate, Name, Latitude," +
					" Longitude, CaseName, CaseNumber, ServeeName , DateReceived ,TimeReceived," +
					" Contact, ContactPhone, HasAttachments, MilestoneCode, " +
					"MilestoneTitle, CustAddressFormattedForDisplay," +
					"CustAddressFormattedForGoogle, AddressFormattedNewLine1, AddressFormattedNewLine2 FROM CourtOpenAddressTable where not exists(select 1 from SubmitCourtPODTable where SubmitCourtPODTable.Workorder = CourtOpenAddressTable.Workorder)";

	public ArrayList<CourtAddressForServer> getcourtOrderValuesFromTabletoDisplayInListVieAndMapView() {
		ArrayList<CourtAddressForServer> courtAddressArray = new ArrayList<CourtAddressForServer>();
		CourtAddressForServer courtAddress = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetCourtOrderForListViewAndMapView,
				null);
		if (cursor.moveToFirst()) {
			do {
				courtAddress = new CourtAddressForServer();
				courtAddress.setCourtOpenAddressID(cursor.getInt(0));
				courtAddress.setWorkorder(cursor.getString(1).trim());
				courtAddress.setAddressFormattedForDisplay(cursor.getString(2));
				courtAddress.setDocuments(cursor.getString(3));

				courtAddress.setInstructions(cursor.getString(4));
				courtAddress.setPriorityTitle(cursor.getString(5));
				courtAddress.setDueDate(cursor.getString(6));
				courtAddress.setName(cursor.getString(7));

				courtAddress.setLatitude(cursor.getString(8));
				courtAddress.setLongitude(cursor.getString(9));

				courtAddress.setCaseName(cursor.getString(10));
				courtAddress.setCaseNumber(cursor.getString(11));
				courtAddress.setServeeName(cursor.getString(12));
                courtAddress.setDateReceived(cursor.getString(13));
                courtAddress.setTimeReceived(cursor.getString(14));
                courtAddress.setContact(cursor.getString(15));
                courtAddress.setContactPhone(cursor.getString(16));
				courtAddress.setHasAttachments(cursor.getInt(17)>0);
				courtAddress.setMilestoneCode(cursor.getInt(18));
				courtAddress.setMilestoneTitle(cursor.getString(19));
				courtAddress.setCustAddressFormattedForDisplay(cursor.getString(20).trim());
				courtAddress.setCustAddressFormattedForGoogle(cursor.getString(21).trim());
				courtAddress.setAddressFormattedNewLine1(cursor.getString(22).trim());
				courtAddress.setAddressFormattedNewLine2(cursor.getString(23).trim());
				courtAddressArray.add(courtAddress);
			} while (cursor.moveToNext());
		}

		return courtAddressArray;
	}


	final static String kGetCourtOrderForListView = "SELECT DISTINCT  CourtOpenAddressID,  "
			+ "Workorder, AddressFormattedForDisplay, Documents, Instructions, "
			+ "PriorityTitle, DueDate, Name, Latitude, Longitude, CaseName, CaseNumber, ServeeName FROM CourtOpenAddressTable";


	public ArrayList<CourtAddressForServer> getcourtOrderValuesFromTabletoDisplayInListView() {
		ArrayList<CourtAddressForServer> courtAddressArray = new ArrayList<CourtAddressForServer>();
		CourtAddressForServer courtAddress = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetCourtOrderForListView,
				null);
		if (cursor.moveToFirst()) {
			do {
				courtAddress = new CourtAddressForServer();
				courtAddress.setCourtOpenAddressID(cursor.getInt(0));
				courtAddress.setWorkorder(cursor.getString(1).trim());
				courtAddress.setAddressFormattedForDisplay(cursor.getString(2));
				courtAddress.setDocuments(cursor.getString(3));

				courtAddress.setInstructions(cursor.getString(4));
				courtAddress.setPriorityTitle(cursor.getString(5));
				courtAddress.setDueDate(cursor.getString(6));
				courtAddress.setName(cursor.getString(7));

				courtAddress.setLatitude(cursor.getString(8));
				courtAddress.setLongitude(cursor.getString(9));
				courtAddress.setCaseName(cursor.getString(10));
				courtAddress.setCaseNumber(cursor.getString(11));
				courtAddress.setServeeName(cursor.getString(12));
				courtAddressArray.add(courtAddress);
			} while (cursor.moveToNext());
		}

		return courtAddressArray;
	}




	final static String kGetSubmitPickupOrder = "SELECT DISTINCT  SubmitPickupPODID,  Workorder, Comments, Date, Time, Latitude,"
			+ "Longtitude FROM SubmitPickupPODTable";

	public ArrayList<AddressForServer> getSubmitPickupOrder() {
		ArrayList<AddressForServer> pickupOrderArray = new ArrayList<AddressForServer>();
		AddressForServer pickupOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitPickupOrder, null);
		if (cursor.moveToFirst()) {
			do {
				pickupOrder = new AddressForServer();
				pickupOrder.setJobID(cursor.getInt(0));
				pickupOrder.setWorkorder(cursor.getString(1).trim());
				pickupOrder.setComment(cursor.getString(2).trim());
				pickupOrder.setDate(cursor.getString(3).trim());
				pickupOrder.setTime(cursor.getString(4).trim());
				pickupOrder.setLat(cursor.getString(5).trim());
				pickupOrder.setLng(cursor.getString(6).trim());

				pickupOrder.TYPE = AddressForServer.PICKUP_SERVICE;
				pickupOrderArray.add(pickupOrder);
			} while (cursor.moveToNext());
		}

		return pickupOrderArray;
	}

	final static String kGetPickupOrderForListView
			= "SELECT DISTINCT  PickupOpenAddressID,  Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle, DueDate, DueTime, Latitude, Longitude, CaseName, CaseNumber, OrderInstructions, PickupInstructions, ServeeName, Business, DateReceived, TimeReceived, OrderContact, ContactPhone, HasAttachments, MilestoneCode, MilestoneTitle, AddressFormattedNewLine1, AddressFormattedNewLine2 FROM PickupOpenAddressTable where not exists(select 1 from SubmitPickupPODTable where SubmitPickupPODTable.Workorder = PickupOpenAddressTable.Workorder)";

	public ArrayList<AddressForServer> getPickupOrderValuesFromTable() {
		ArrayList<AddressForServer> addressForServerArray = new ArrayList<AddressForServer>();
		AddressForServer addressForServer = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetPickupOrderForListView, null);
		if (cursor.moveToFirst()) {
			do {
				addressForServer = new AddressForServer();
				addressForServer.setJobID((cursor.getInt(0)));
				addressForServer.setWorkorder(cursor.getString(1).trim());
				addressForServer.setAddressLineItem(cursor.getInt(2));
				addressForServer.setAddressFormattedForDisplay(cursor
						.getString(3));

				addressForServer.setPriorityTitle(cursor.getString(4));
				addressForServer.setDueDate(cursor.getString(5));
				addressForServer.setDueTime(cursor.getString(6));
				addressForServer.setLatitude(cursor.getString(7));

				addressForServer.setLongitude(cursor.getString(8));
				addressForServer.setCaseName(cursor.getString(9));
				addressForServer.setCaseNumber(cursor.getString(10));
				addressForServer.setOrderInstructions(cursor.getString(11));
				addressForServer.setPickupInstructions(cursor.getString(12));
				addressForServer.setServeeName(cursor.getString(13));

				addressForServer.setBusiness(cursor.getString(14));
				addressForServer.setDateReceived(cursor.getString(15));
				addressForServer.setTimeReceived(cursor.getString(16));
				addressForServer.setOrderContact(cursor.getString(17));
				addressForServer.setContactPhone(cursor.getString(18));
				addressForServer.setHasAttachments(cursor.getInt(19)>0);
				addressForServer.setMilestoneCode(cursor.getInt(20));
				addressForServer.setMilestoneTitle(cursor.getString(21));
				addressForServer.setAddressFormattedNewLine1(cursor.getString(22));
				addressForServer.setAddressFormattedNewLine2(cursor.getString(23));

				addressForServer.TYPE = AddressForServer.PICKUP_SERVICE;
				addressForServerArray.add(addressForServer);
			} while (cursor.moveToNext());
		}
		return addressForServerArray;
	}

	final static String kGetPickupOrderForListViewJobQueue = "SELECT DISTINCT  PickupOpenAddressID,  Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle, DueDate, DueTime, Latitude, Longitude,CaseName, CaseNumber, OrderInstructions, PickupInstructions, ServeeName FROM PickupOpenAddressTable";

	public ArrayList<AddressForServer> getPickupOrderValues() {
		ArrayList<AddressForServer> addressForServerArray = new ArrayList<AddressForServer>();
		AddressForServer addressForServer = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetPickupOrderForListViewJobQueue, null);
		if (cursor.moveToFirst()) {
			do {
				addressForServer = new AddressForServer();
				addressForServer.setJobID((cursor.getInt(0)));
				addressForServer.setWorkorder(cursor.getString(1).trim());
				addressForServer.setAddressLineItem(cursor.getInt(2));
				addressForServer.setAddressFormattedForDisplay(cursor
						.getString(3));

				addressForServer.setPriorityTitle(cursor.getString(4));
				addressForServer.setDueDate(cursor.getString(5));
				addressForServer.setDueTime(cursor.getString(6));
				addressForServer.setLatitude(cursor.getString(7));

				addressForServer.setLongitude(cursor.getString(8));
				addressForServer.setCaseName(cursor.getString(9));
				addressForServer.setCaseNumber(cursor.getString(10));
				addressForServer.setOrderInstructions(cursor.getString(11));
				addressForServer.setPickupInstructions(cursor.getString(12));
				addressForServer.setServeeName(cursor.getString(13));
				addressForServer.TYPE = AddressForServer.PICKUP_SERVICE;
				addressForServerArray.add(addressForServer);
			} while (cursor.moveToNext());
		}
		return addressForServerArray;
	}

	public ArrayList<SplatterAddress> getProcessAddressComparison(
			String addressToCount, String workorderToCount,String serveename) {
		ArrayList<SplatterAddress> AddressArray = new ArrayList<SplatterAddress>();
		SplatterAddress splatteraddress = null;

		@SuppressLint("Recycle") Cursor mCount = database
				.rawQuery(
						"select AddressLineItem, Workorder, Servee from ProcessOpenAddressTable where AddressFormattedForGoogle='"
								+ addressToCount + "'", null);
		if (mCount.moveToFirst()) {
			do {
				splatteraddress = new SplatterAddress();
				splatteraddress.setAddressLineItem(mCount.getInt(0));
				splatteraddress.setWorkorder(mCount.getString(1));
				splatteraddress.setServeename(mCount.getString(2));
				AddressArray.add(splatteraddress);
			} while (mCount.moveToNext());
		}
		return AddressArray;
	}

	final static String kGetSubmitDeliveryOrder = "SELECT DISTINCT  SubmitDeliveryPODID,  Workorder, Comments, Date, Time, "
			+ "FeeAdvance, Weight, Pieces, WaitTime, Receivedby, Latitude, Longtitude FROM SubmitDeliveryPODTable";

	public ArrayList<AddressForServer> getSubmitDeliveryOrder() {
		ArrayList<AddressForServer> deliveryOrderArray = new ArrayList<AddressForServer>();
		AddressForServer deliveryOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetSubmitDeliveryOrder, null);
		if (cursor.moveToFirst()) {
			do {
				deliveryOrder = new AddressForServer();
				deliveryOrder.setJobID(cursor.getInt(0));
				deliveryOrder.setWorkorder(cursor.getString(1).trim());
				deliveryOrder.setComment(cursor.getString(2).trim());
				deliveryOrder.setDate(cursor.getString(3).trim());
				deliveryOrder.setTime(cursor.getString(4).trim());
				deliveryOrder.setFeeAdvance(cursor.getString(5).trim());
				deliveryOrder.setWeight(cursor.getString(6).trim());
				deliveryOrder.setPieces(cursor.getString(7).trim());
				deliveryOrder.setWaitTime(cursor.getString(8).trim());
				deliveryOrder.setReceivedby(cursor.getString(9).trim());
				deliveryOrder.setLat(cursor.getString(10).trim());
				deliveryOrder.setLng(cursor.getString(11).trim());
				deliveryOrder.TYPE = AddressForServer.DELIVERY_SERVICE;
				deliveryOrderArray.add(deliveryOrder);
			} while (cursor.moveToNext());
		}

		return deliveryOrderArray;
	}

	final static String kGetDeliveryOrderForListView = "SELECT DISTINCT  DeliveryOpenAddressID,  Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle, DueDate, DueTime, Latitude, Longitude, CaseName, CaseNumber, OrderInstructions, DeliveryInstructions, ServeeName,  Business, DateReceived, TimeReceived, OrderContact, ContactPhone , HasAttachments, MilestoneCode, MilestoneTitle, AddressFormattedNewLine1, AddressFormattedNewLine2 FROM DeliveryOpenAddressTable where not exists(select 1 from SubmitDeliveryPODTable where SubmitDeliveryPODTable.Workorder = DeliveryOpenAddressTable.Workorder)";

	public ArrayList<AddressForServer> getDeliveryOrderValuesFromTable() {
		ArrayList<AddressForServer> deliveryOrderArray = new ArrayList<AddressForServer>();
		AddressForServer deliveryOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetDeliveryOrderForListView, null);
		if (cursor.moveToFirst()) {
			do {
				deliveryOrder = new AddressForServer();
				deliveryOrder.setJobID(cursor.getInt(0));
				deliveryOrder.setWorkorder(cursor.getString(1).trim());
				deliveryOrder.setAddressLineItem(cursor.getInt(2));
				deliveryOrder.setAddressFormattedForDisplay(cursor.getString(3));

				deliveryOrder.setPriorityTitle(cursor.getString(4));
				deliveryOrder.setDueDate(cursor.getString(5));
				deliveryOrder.setDueTime(cursor.getString(6));
				deliveryOrder.setLatitude(cursor.getString(7));

				deliveryOrder.setLongitude(cursor.getString(8));

				deliveryOrder.setCaseName(cursor.getString(9));
				deliveryOrder.setCaseNumber(cursor.getString(10));
				deliveryOrder.setOrderInstructions(cursor.getString(11));
				deliveryOrder.setDeliveryInstructions(cursor.getString(12));
				deliveryOrder.setServeeName(cursor.getString(13));

				deliveryOrder.setBusiness(cursor.getString(14));
				deliveryOrder.setDateReceived(cursor.getString(15));
				deliveryOrder.setTimeReceived(cursor.getString(16));
				deliveryOrder.setOrderContact(cursor.getString(17));
				deliveryOrder.setContactPhone(cursor.getString(18));
				deliveryOrder.setHasAttachments(cursor.getInt(19)>0);
				deliveryOrder.setMilestoneCode(cursor.getInt(20));
				deliveryOrder.setMilestoneTitle(cursor.getString(21));
				deliveryOrder.setAddressFormattedNewLine1(cursor.getString(22));
				deliveryOrder.setAddressFormattedNewLine2(cursor.getString(23));
				deliveryOrder.TYPE = AddressForServer.DELIVERY_SERVICE;
				deliveryOrderArray.add(deliveryOrder);
			} while (cursor.moveToNext());
		}

		return deliveryOrderArray;
	}


	final static String kGetDeliveryOrderForListViewforjobQueue = "SELECT DISTINCT  DeliveryOpenAddressID,"
			+ "  Workorder, AddressLineItem, AddressFormattedForDisplay, PriorityTitle,"
			+ " DueDate, DueTime, Latitude, Longitude, CaseName, CaseNumber,OrderInstructions, DeliveryInstructions, ServeeName FROM DeliveryOpenAddressTable";
	public ArrayList<AddressForServer> getDeliveryOrderValues() {
		ArrayList<AddressForServer> deliveryOrderArray = new ArrayList<AddressForServer>();
		AddressForServer deliveryOrder = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetDeliveryOrderForListViewforjobQueue, null);
		if (cursor.moveToFirst()) {
			do {
				deliveryOrder = new AddressForServer();
				deliveryOrder.setJobID(cursor.getInt(0));
				deliveryOrder.setWorkorder(cursor.getString(1).trim());
				deliveryOrder.setAddressLineItem(cursor.getInt(2));
				deliveryOrder
						.setAddressFormattedForDisplay(cursor.getString(3));

				deliveryOrder.setPriorityTitle(cursor.getString(4));
				deliveryOrder.setDueDate(cursor.getString(5));
				deliveryOrder.setDueTime(cursor.getString(6));
				deliveryOrder.setLatitude(cursor.getString(7));

				deliveryOrder.setLongitude(cursor.getString(8));
				deliveryOrder.setCaseName(cursor.getString(9));
				deliveryOrder.setCaseNumber(cursor.getString(10));
				deliveryOrder.setOrderInstructions(cursor.getString(11));
				deliveryOrder.setDeliveryInstructions(cursor.getString(12));
				deliveryOrder.setServeeName(cursor.getString(13));
				deliveryOrder.TYPE = AddressForServer.DELIVERY_SERVICE;
				deliveryOrderArray.add(deliveryOrder);
			} while (cursor.moveToNext());
		}

		return deliveryOrderArray;
	}


	final static String kinsertIntoSubmitDiligence = "INSERT  INTO SubmitDiligenceTable (Workorder, AddressLineitem, "
			+ "DiligenceDate, DiligenceTime, Report, ServerCode, DiligenceCode, "
			+ "DateTimeSubmitted, Latitude, Longtitude)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	final static String kUpdateIntoSubmitDiligence = "UPDATE SubmitDiligenceTable SET Workorder = ?, AddressLineitem = ?, DiligenceDate = ?, DiligenceTime = ?, Report = ?, ServerCode = ?, DiligenceCode = ?, DateTimeSubmitted = ?, Latitude = ?, Longtitude = ? WHERE Lineitem =?";

	public boolean insertOrUpdateRecordDiligenceInDB(
			SubmitDiligence submitDiligence, boolean insert) {

		SQLiteStatement stmt = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date date = new Date();
		String datesubmitted = dateFormat.format(date);
		if (insert) {
			stmt = database.compileStatement(kinsertIntoSubmitDiligence);
		} else {
			stmt = database.compileStatement(kUpdateIntoSubmitDiligence);
		}
		int i = 0;
		stmt.bindString(++i, submitDiligence.getWorkorder());
		stmt.bindLong(++i, submitDiligence.getAddressLineItem());
		stmt.bindString(++i, submitDiligence.getDiligenceDate());
		stmt.bindString(++i, submitDiligence.getDiligenceTime());

		stmt.bindString(++i, submitDiligence.getReport());
		stmt.bindString(++i, submitDiligence.getServerCode());
		stmt.bindLong(++i, submitDiligence.getDiligenceCode());
		stmt.bindString(++i, datesubmitted);

		stmt.bindString(++i, submitDiligence.getLatitude());
		stmt.bindString(++i, submitDiligence.getLongitude());

		if (!insert) {
			stmt.bindLong(++i, submitDiligence.getLineItem());
			return stmt.executeUpdateDelete() > 0;
		} else {
			return stmt.executeInsert() != -1;
		}
	}

	final static String kGetLastinsertedLineItem = "SELECT max(Lineitem) as maxLineItem FROM SubmitDiligenceTable WHERE isSync = 0";

	public int getLastinsertedLineItemFromSubmitDiligences() {

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetLastinsertedLineItem, null);

		if (cursor.moveToFirst()) {
			do {
				if (cursor.getColumnCount() > 0) {
					return cursor.getInt(0);
				}
			} while (cursor.moveToNext());
		}
		return 0;
	}

	final static String kinsertAttachementsToSubmitDiligenceTable = "INSERT  INTO SubmitDiligenceAttachmentTable  (DiligenceLineitem, Workorder, DiligenceAddressLineitem, FileName, PDFInMemory, urlString) VALUES (?, ?, ?, ?, ?, ?)";

	public boolean insertOrUpdateAttachmentsOfSubmitDiligences(
			SubmitDiligence submitImage) {

		SQLiteStatement stmt = database
				.compileStatement(kinsertAttachementsToSubmitDiligenceTable);

		int i = 0;
		stmt.bindLong(++i, submitImage.getLineItem());
		stmt.bindString(++i, submitImage.getWorkorder());
		stmt.bindLong(++i, submitImage.getAddressLineItem());
		stmt.bindString(++i, submitImage.getAttachementsFileName());

		stmt.bindString(++i, submitImage.getAttachementBase64String());
		stmt.bindString(++i, submitImage.getAttachementOfUrlString());
		if (!true) {
			stmt.bindLong(++i, submitImage.getLineItem());
		}

		return stmt.executeInsert() != -1;

	}

	final static String kDeletestmtattachtable = "DELETE  FROM SubmitDiligenceAttachmentTable WHERE DiligenceLineitem =?";

	public void deleteAttachmentsFromattachemntsTableBy(int finalStatusLineItem) {

		database.execSQL(kDeletestmtattachtable,
				new Object[] { finalStatusLineItem });
	}


	final static String kDeleteFinalStatusAfterupload = "DELETE  FROM ProcessOpenAddressTable WHERE AddressLineItem =?";

	public void DeleteFinalStatusAfteruploadTableBy(int AddressLineItem) {

		database.execSQL(kDeleteFinalStatusAfterupload,
				new Object[] { AddressLineItem });
	}

	final static String kDeleteCourtPODAfterupload = "DELETE  FROM CourtOpenAddressTable WHERE Workorder =?";

	public void DeleteCourtPODAfteruploadTableBy(String Workorder) {

		database.execSQL(kDeleteCourtPODAfterupload,
				new Object[] { Workorder });
	}

	final static String kDeletePickupPODAfterupload = "DELETE  FROM PickupOpenAddressTable WHERE Workorder =?";

	public void DeletePickupPODAfteruploadTableBy(String Workorder) {

		database.execSQL(kDeletePickupPODAfterupload,
				new Object[] { Workorder });
	}

	final static String kDeleteDeliveryPODAfterupload = "DELETE  FROM DeliveryOpenAddressTable WHERE Workorder =?";

	public void DeleteDeliveryPODAfteruploadTableBy(String Workorder) {

		database.execSQL(kDeleteDeliveryPODAfterupload,
				new Object[] { Workorder });
	}

	final static String kGetLocallyAddedFromReturnDeligenceTables = "SELECT DiligenceDate, DiligenceTime, Report, AddressLineItem FROM SubmitDiligenceTable  WHERE Workorder = ?  AND AddressLineItem =? UNION SELECT DiligenceDate,DiligenceTime,Report,AddressLineItem FROM DiligenceTable WHERE Workorder = ?  AND AddressLineItem =?";

	public void previousdiligensetable(String workorder, int addressLineItem,
			String Workorder, int AddressLineItem) {

		database.execSQL(kGetLocallyAddedFromReturnDeligenceTables,
				new Object[] { workorder, addressLineItem, Workorder,
						AddressLineItem });
	}

	final static String kGetLocallyAddedFromReturnDeligenceTable = "SELECT  DiligenceDate, DiligenceTime, Report, AddressLineItem, DiligenceCode, Lineitem, isSync FROM SubmitDiligenceTable  WHERE Workorder = ?  AND AddressLineItem =?";

	public ArrayList<SubmitDiligence> getLocallyAddedDiligenceValuesFromDBToDisplay(
			String workorder, int addressLineItem) {
		ArrayList<SubmitDiligence> submitDiligenceArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence submitDiligence = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromReturnDeligenceTable, new String[] {
						workorder, addressLineItem + "" });
		if (cursor.moveToFirst()) {
			do {
				submitDiligence = new SubmitDiligence();

				submitDiligence.setDiligenceDate(cursor.getString(0));
				submitDiligence.setDiligenceTime(cursor.getString(1));
				submitDiligence.setReport(cursor.getString(2));
				submitDiligence.setAddressLineItem(cursor.getInt(3));

				submitDiligence.setDiligenceCode(cursor.getInt(4));
				submitDiligence.setLineItem(cursor.getInt(5));
				submitDiligence.setSync(cursor.getInt(6) > 0);

				submitDiligenceArray.add(submitDiligence);
			} while (cursor.moveToNext());
		}

		return submitDiligenceArray;
	}

	final static String kstatusByCode = "SELECT DeligencePhoneTitle from DeligencePhrasesTable where DeligenceCode =?";

	public String getstatusByCode(int diligenceCode) throws Exception {
		try {
			@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kstatusByCode, new String[] { ""
					+ diligenceCode });
			String status = "N/A";
			if (cursor.moveToFirst()) {
				do {
					status = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			return status;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kGetValuesForUpdateDeligenceTable = "SELECT  DiligenceDate, DiligenceTime, Report, AddressLineItem, DiligenceCode, Lineitem, Workorder  FROM SubmitDiligenceTable  WHERE Lineitem =?";

	public SubmitDiligence getValuesForUpdateDeligenceTable(int lineItem) {
		SubmitDiligence diligenceForProcess = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetValuesForUpdateDeligenceTable.toString(),
				new String[] { lineItem + "" });
		if (cursor.moveToFirst()) {
			do {
				diligenceForProcess = new SubmitDiligence();

				diligenceForProcess.setDiligenceDate(cursor.getString(0));
				diligenceForProcess.setDiligenceTime(cursor.getString(1));
				diligenceForProcess.setReport(cursor.getString(2));
				diligenceForProcess.setAddressLineItem(cursor.getInt(3));

				diligenceForProcess.setDiligenceCode(cursor.getInt(4));
				diligenceForProcess.setLineItem(cursor.getInt(5));
				diligenceForProcess.setWorkorder(cursor.getString(6).trim());
			} while (cursor.moveToNext());
		}

		return diligenceForProcess;
	}

	final static String kGetAddressForUpdateRecordDiligences = "SELECT  AddressFormattedForDisplay  FROM ProcessOpenAddressTable "
			+ "													WHERE Workorder = ? AND AddressLineItem =?";

	public String getAddressForUpdateRecordDiligences(String workOrder,
			int addressLineItem) {

		String address = "";
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetAddressForUpdateRecordDiligences,
				new String[] { workOrder, addressLineItem + "" });
		if (cursor.moveToFirst()) {
			do {
				if (cursor.getString(0) == null) {
					address = "";
				} else {
					address = cursor.getString(0);
				}
			} while (cursor.moveToNext());
		}

		return address;
	}

	final static String kGetAttachementsDeligenceTable = "SELECT  DiligenceLineitem, Workorder, DiligenceAddressLineitem, FileName, PDFInMemory, urlString  FROM SubmitDiligenceAttachmentTable  WHERE  DiligenceLineitem =?";

	public ArrayList<SubmitDiligence> getAttachementsFromDeligenceAttachmentsTable(
			int diligenceLineItem) {
		ArrayList<SubmitDiligence> diligenceForProcessArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence diligenceForProcess = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetAttachementsDeligenceTable,
				new String[] { diligenceLineItem + "" });
		if (cursor.moveToFirst()) {
			do {
				diligenceForProcess = new SubmitDiligence();

				diligenceForProcess.setLineItem(cursor.getInt(0));
				diligenceForProcess.setWorkorder(cursor.getString(1).trim());
				diligenceForProcess.setAddressLineItem(cursor.getInt(2));
				diligenceForProcess
						.setAttachementsFileName(cursor.getString(3));

				diligenceForProcess.setAttachementBase64String(cursor
						.getString(4));
				diligenceForProcess.setAttachementOfUrlString(cursor
						.getString(5));

				diligenceForProcessArray.add(diligenceForProcess);

			} while (cursor.moveToNext());
		}

		return diligenceForProcessArray;
	}

	final static String kGetMannerOfServiceByStateCode = "SELECT  Code, State, Title FROM MannerOfServiceTable WHERE State =?";

	public ArrayList<MannerOfService> getMannerOfServiceByStateCode(
			String stateCode) {
		ArrayList<MannerOfService> mannerOfServiceArray = new ArrayList<MannerOfService>();
		MannerOfService mannerofservvice = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetMannerOfServiceByStateCode,
				new String[] { stateCode });
		if (cursor.moveToFirst()) {
			do {
				mannerofservvice = new MannerOfService();

				mannerofservvice.setCode(cursor.getString(0));
				mannerofservvice.setState(cursor.getString(1));
				mannerofservvice.setTitle(cursor.getString(2));

				mannerOfServiceArray.add(mannerofservvice);
			} while (cursor.moveToNext());
		}

		return mannerOfServiceArray;
	}

	final static String kGetProcessAddress = "SELECT  Workorder, LineItem, Street, City, State, Zip, theAddressType FROM ProcessAddressTable WHERE isSync = 0";

	public ArrayList<Address> getprocessaddress() {
		ArrayList<Address> processaddressArray = new ArrayList<Address>();
		Address address = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetProcessAddress, null);
		if (cursor.moveToFirst()) {
			do {
				address = new Address();
				address.setWorkorder(cursor.getString(0));
				address.setLineItem(cursor.getInt(1));
				address.setStreet(cursor.getString(2));
				address.setCity(cursor.getString(3));
				address.setState(cursor.getString(4));
				address.setZip(cursor.getString(5));
				address.setTheAddressType(cursor.getInt(6));

				processaddressArray.add(address);
			} while (cursor.moveToNext());

		}
		return processaddressArray;

	}

	final static String kinsertIntoFinalStatusTable = "INSERT INTO SubmitFinalStatusTable ("
			+ "Workorder, AddressLineItem, Servee, AuthorizedAgentTitle"
			+ ",  AgentForService, LeftWith, RelationShip, ServeDate"
			+ ", ServeTime, Age, Height, Weight"
			+ ", Skin, Hair, Eyes, DistinguishingMarks"
			+ ", DateTimeSubmitted, MannerOfServiceCode, Report, Entity, ServeeIsMale"
			+ ", InUniform, Military, Police, isSync"
			+ ", Latitude, Longtitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public boolean insertIntoFinalStatusTable(
			SubmitFinalStatus submitFinalStatus) throws Exception {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd-MM-yyyy HH:mm");
			Date date = new Date();
			String datesubmitted = dateFormat.format(date);
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoFinalStatusTable);
			int i = 0;

			stmt.bindString(++i, submitFinalStatus.getWorkorder());
			stmt.bindLong(++i, submitFinalStatus.getAddressLineitem());
			Log.d("addresslinelitem",""+submitFinalStatus.getAddressLineitem());
			stmt.bindString(++i, submitFinalStatus.getServee());
			stmt.bindString(++i, submitFinalStatus.getAuthorizedAgent());

			stmt.bindString(++i, submitFinalStatus.getAuthorizedAgentTitle());
			stmt.bindString(++i, submitFinalStatus.getLeftWith());
			stmt.bindString(++i, submitFinalStatus.getRelationship());
			stmt.bindString(++i, submitFinalStatus.getServeDate());

			stmt.bindString(++i, submitFinalStatus.getserveTime());
			stmt.bindString(++i, submitFinalStatus.getAge());
			stmt.bindString(++i, submitFinalStatus.getHeight());
			stmt.bindString(++i, submitFinalStatus.getWeight());

			stmt.bindString(++i, submitFinalStatus.getSkin());
			stmt.bindString(++i, submitFinalStatus.getHair());
			stmt.bindString(++i, submitFinalStatus.getEyes());
			stmt.bindString(++i, submitFinalStatus.getMarks());

			stmt.bindString(++i, datesubmitted);
			stmt.bindString(++i, submitFinalStatus.getMannerOfServiceCode());
			stmt.bindString(++i, submitFinalStatus.getReport());
			stmt.bindLong(++i, submitFinalStatus.getEntity() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getServerisMale() ? 1 : 0);

			stmt.bindLong(++i, submitFinalStatus.getInUniform() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getmilitary() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getPolice() ? 1 : 0);
			stmt.bindLong(++i, 0); // is sync

			stmt.bindString(++i, submitFinalStatus.getLatitude());
			stmt.bindString(++i, submitFinalStatus.getLongitude());

			return stmt.executeInsert() != -1;

		} catch (Exception e) {
			throw e;
		}
	}

	final static String kUpdateIntoFinalStatusTable = "UPDATE SubmitFinalStatusTable SET Workorder = ?, AddressLineItem = ?, Servee = ?, AuthorizedAgentTitle = ?," +
			" AgentForService = ?, LeftWith = ?, RelationShip = ?, ServeDate = ?, ServeTime = ?, Age = ?," +
			" Height = ?, Weight = ?, Skin = ?,  Hair = ?, Eyes = ?, DistinguishingMarks = ?, DateTimeSubmitted = ?," +
			" MannerOfServiceCode = ?, Report = ?, Entity = ?, ServeeIsMale = ?, InUniform = ?, Military = ?," +
			" Police = ?, isSync = ?, Latitude = ?, Longtitude = ? "+
			" WHERE FinalStatusLineItem =?";


	public boolean UpdateIntoFinalStatusTable(
			SubmitFinalStatus submitFinalStatus) throws Exception {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd-MM-yyyy HH:mm");
			Date date = new Date();
			String datesubmitted = dateFormat.format(date);
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoFinalStatusTable);
			int i = 0;

			stmt.bindString(++i, submitFinalStatus.getWorkorder());
			stmt.bindLong(++i, submitFinalStatus.getAddressLineitem());
			stmt.bindString(++i, submitFinalStatus.getServee());
			stmt.bindString(++i, submitFinalStatus.getAuthorizedAgent());

			stmt.bindString(++i, submitFinalStatus.getAuthorizedAgentTitle());
			stmt.bindString(++i, submitFinalStatus.getLeftWith());
			stmt.bindString(++i, submitFinalStatus.getRelationship());
			stmt.bindString(++i, submitFinalStatus.getServeDate());

			stmt.bindString(++i, submitFinalStatus.getserveTime());
			stmt.bindString(++i, submitFinalStatus.getAge());
			stmt.bindString(++i, submitFinalStatus.getHeight());
			stmt.bindString(++i, submitFinalStatus.getWeight());

			stmt.bindString(++i, submitFinalStatus.getSkin());
			stmt.bindString(++i, submitFinalStatus.getHair());
			stmt.bindString(++i, submitFinalStatus.getEyes());
			stmt.bindString(++i, submitFinalStatus.getMarks());

			stmt.bindString(++i, datesubmitted);
			stmt.bindString(++i, submitFinalStatus.getMannerOfServiceCode());
			stmt.bindString(++i, submitFinalStatus.getReport());
			stmt.bindLong(++i, submitFinalStatus.getEntity() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getServerisMale() ? 1 : 0);

			stmt.bindLong(++i, submitFinalStatus.getInUniform() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getmilitary() ? 1 : 0);
			stmt.bindLong(++i, submitFinalStatus.getPolice() ? 1 : 0);
			stmt.bindLong(++i, 0); // is sync

			stmt.bindString(++i, submitFinalStatus.getLatitude());
			stmt.bindString(++i, submitFinalStatus.getLongitude());

			stmt.bindLong(++i, submitFinalStatus.getFinalStatusLineItem());
			return stmt.executeUpdateDelete() > 0;

		} catch (Exception e) {
			throw e;
		}
	}

	private final static String kDeletesFinalStatementAttachment = "DELETE  FROM FinalStatuseAttachmentTable WHERE FinalStatusLineitem =?";

	public void deletesFinalStatementAttachment(int finalStatusLineItem) {
		database.execSQL(kDeletesFinalStatementAttachment,
				new Object[] { finalStatusLineItem });
	}

	static String kGetLastinsertedLineItemFromFinalStatus = "SELECT max(FinalStatusLineitem) FROM SubmitFinalStatusTable";

	public int getLastinsertedLineItemFromFinalStatus() {
		int lastInsertedLineItem = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLastinsertedLineItemFromFinalStatus, null);
		if (cursor.moveToFirst()) {
			do {
				lastInsertedLineItem = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		return lastInsertedLineItem;
	}

	final static String kinsertAttachementsToFinalStatusTable = "INSERT  INTO FinalStatuseAttachmentTable  (FinalStatusLineitem, Workorder, FinalStatusAddressLineitem, FileName, PDFInMemory, urlString) VALUES (?, ?, ?, ?, ?, ?)";

	public Boolean insertOrUpdateAttachmentsOfFinalStatus(
			SubmitDiligence submitImage) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertAttachementsToFinalStatusTable);
			int i = 0;
			stmt.bindLong(++i, submitImage.getLineItem());
			stmt.bindString(++i, submitImage.getWorkorder());
			stmt.bindLong(++i, submitImage.getAddressLineItem());
			stmt.bindString(++i, submitImage.getAttachementsFileName());

			stmt.bindString(++i, submitImage.getAttachementBase64String());
			stmt.bindString(++i, submitImage.getAttachementOfUrlString());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoSubmitCourtPODTable = "INSERT INTO SubmitCourtPODTable "
			+ "(Workorder, Date, Time, Comments"
			+ ",  FeeAdvance, Weight, Pieces, WaitTime"
			+ ", isSync, Latitude, Longtitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public boolean insertIntoSubmitCourtPODTable(SubmitCourtPOD submitPickupPOD)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoSubmitCourtPODTable);
			int i = 0;
			stmt.bindString(++i, submitPickupPOD.getWorkorder());
			stmt.bindString(++i, submitPickupPOD.getProofDate());
			stmt.bindString(++i, submitPickupPOD.getProofTime());
			stmt.bindString(++i, submitPickupPOD.getProofComments());

			stmt.bindLong(++i, submitPickupPOD.getFeeAdvance());
			stmt.bindLong(++i, submitPickupPOD.getWeight());
			stmt.bindLong(++i, submitPickupPOD.getPieces());
			stmt.bindLong(++i, submitPickupPOD.getWaitTime());

			stmt.bindLong(++i, 0); // sync
			stmt.bindString(++i, submitPickupPOD.getLatitude());
			stmt.bindString(++i, submitPickupPOD.getLongitude());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kUpdateIntoSubmitCourtPODTable = "UPDATE SubmitCourtPODTable SET Workorder = ?, Date = ?, Time = ?, Comments = ?, FeeAdvance = ?, Weight = ?, Pieces = ?, WaitTime = ?, isSync = ?, Latitude = ?, Longtitude = ? WHERE SubmitCourtPODID =?";

	public boolean UpdateIntoSubmitCourtPODTable(SubmitCourtPOD submitPickupPOD)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSubmitCourtPODTable);

			int i = 0;
			stmt.bindString(++i, submitPickupPOD.getWorkorder());
			stmt.bindString(++i, submitPickupPOD.getProofDate());
			stmt.bindString(++i, submitPickupPOD.getProofTime());
			stmt.bindString(++i, submitPickupPOD.getProofComments());

			stmt.bindLong(++i, submitPickupPOD.getFeeAdvance());
			stmt.bindLong(++i, submitPickupPOD.getWeight());
			stmt.bindLong(++i, submitPickupPOD.getPieces());
			stmt.bindLong(++i, submitPickupPOD.getWaitTime());

			stmt.bindLong(++i, 0); // sync
			stmt.bindString(++i, submitPickupPOD.getLatitude());
			stmt.bindString(++i, submitPickupPOD.getLongitude());

			stmt.bindLong(++i , submitPickupPOD.getSubmitCourtPODID());
			return stmt.executeUpdateDelete() > 0;
		} catch (Exception e) {
			throw e;
		}
	}




	private final static String kGetLastinsertedIDFromSubmitCourtPOD = "SELECT max(SubmitCourtPODID) maxline FROM SubmitCourtPODTable";


	public int getLastinsertedLineItemFromSubmitCourtPOD() {
		int lastInsertedLineItem = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kGetLastinsertedIDFromSubmitCourtPOD,
				null);
		if (cursor.moveToFirst()) {
			do {
				lastInsertedLineItem = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return lastInsertedLineItem;
	}

	final static String kinsertAttachementsToCourtPODTable = "INSERT  INTO SubmitCourtPODAttachmentTable  ("
			+ "SubmitCourtPODAttachmentID, Workorder, FileName, PDFInMemory"
			+ ", urlString, isSync) VALUES (?, ?, ?, ?, ?, ?)";

	public boolean insertOrUpdateAttachmentsOfCourtPOD(
			SubmitCourtPOD submitCourtPOD) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertAttachementsToCourtPODTable);
			int i = 0;
			stmt.bindLong(++i, submitCourtPOD.getSubmitCourtPODID());
			stmt.bindString(++i, submitCourtPOD.getWorkorder());
			stmt.bindString(++i, submitCourtPOD.getAttachmentFilename());
			stmt.bindString(++i, submitCourtPOD.getAttahmentBase64String());

			stmt.bindString(++i, submitCourtPOD.getAttachmentUrlString());
			stmt.bindLong(++i, 0); // sync
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoSubmitPickupPODTable = "INSERT INTO SubmitPickupPODTable ("
			+ "Workorder, AddressLineItem, Date, Time"
			+ ", Comments, isSync, Latitude, Longtitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public boolean insertIntoSubmitPickupPODTable(
			SubmitPickupPOD submitPickupPODObject) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoSubmitPickupPODTable);
			int i = 0;
			stmt.bindString(++i, submitPickupPODObject.getWorkorder());
			stmt.bindLong(++i, submitPickupPODObject.getAddressLineitem());
			stmt.bindString(++i, submitPickupPODObject.getProofDate());
			stmt.bindString(++i, submitPickupPODObject.getProofTime());

			stmt.bindString(++i, submitPickupPODObject.getProofComments());
			stmt.bindLong(++i, 0);
			stmt.bindString(++i, submitPickupPODObject.getLatitude());
			stmt.bindString(++i, submitPickupPODObject.getLongitude());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kUpdateIntoSubmitPickupPODTable = "UPDATE SubmitPickupPODTable SET Workorder = ?, AddressLineItem = ?, Date = ?, Time = ?, Comments = ?, isSync = ?, Latitude = ?, Longtitude = ? WHERE SubmitPickupPODID =?";

	public boolean UpdateIntoSubmitPickupPODTable(SubmitPickupPOD submitPickupPODObject)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSubmitPickupPODTable);
			int i = 0;
			stmt.bindString(++i, submitPickupPODObject.getWorkorder());
			stmt.bindLong(++i, submitPickupPODObject.getAddressLineitem());
			stmt.bindString(++i, submitPickupPODObject.getProofDate());
			stmt.bindString(++i, submitPickupPODObject.getProofTime());

			stmt.bindString(++i, submitPickupPODObject.getProofComments());
			stmt.bindLong(++i, 0);
			stmt.bindString(++i, submitPickupPODObject.getLatitude());
			stmt.bindString(++i, submitPickupPODObject.getLongitude());

			stmt.bindLong(++i, submitPickupPODObject.getSubmitPickupPODID());
			return stmt.executeUpdateDelete() > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	private final static String kGetLastinsertedIDFromSubmitPickupPOD = "SELECT max(SubmitPickupPODID) FROM SubmitPickupPODTable";

	public int getLastinsertedLineItemFromSubmitPickupPOD() {
		int lastInsertedLineItem = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLastinsertedIDFromSubmitPickupPOD, null);
		if (cursor.moveToFirst()) {
			do {
				lastInsertedLineItem = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return lastInsertedLineItem;
	}

	final static String kinsertAttachementsToPickupPODTable = "INSERT  INTO SubmitPickupPODAttachmentTable  ("
			+ "SubmitPickupPODAttachmentID, Workorder, AddressLineItem, FileName"
			+ ", PDFInMemory, urlString, isSync) VALUES (?, ?, ?, ?, ?, ?, ?)";

	public boolean insertAttachmentsOfPickupPOD(SubmitPickupPOD submitImage)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertAttachementsToPickupPODTable);
			int i = 0;
			stmt.bindLong(++i, submitImage.getSubmitPickupPODID());
			stmt.bindString(++i, submitImage.getWorkorder());
			stmt.bindLong(++i, submitImage.getAddressLineitem());
			stmt.bindString(++i, submitImage.getAttachmentFilename());

			stmt.bindString(++i, submitImage.getAttachmentBase64String());
			stmt.bindString(++i, submitImage.getAttachmentUrlString());
			stmt.bindLong(++i, 0); // sync
			return stmt.executeInsert() != -1;
		}

		catch (Exception e) {
			throw e;
		}
	}

	final static String kinsertIntoSubmitDeliveryPODTable = "INSERT INTO SubmitDeliveryPODTable ("
			+ "Workorder, AddressLineItem, Date, Time"
			+ ", Comments, FeeAdvance, Weight, Pieces"
			+ ", WaitTime, ReceivedBy, isSync, Latitude"
			+ ", Longtitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public boolean insertIntoSubmitDeliveryPODTable(
			SubmitDeliveryPOD submitDeliveryPODObject) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertIntoSubmitDeliveryPODTable);
			int i = 0;
			stmt.bindString(++i, submitDeliveryPODObject.getWorkorder());
			stmt.bindLong(++i, submitDeliveryPODObject.getAddressLineitem());
			stmt.bindString(++i, submitDeliveryPODObject.getProofDate());
			stmt.bindString(++i, submitDeliveryPODObject.getProofTime());

			stmt.bindString(++i, submitDeliveryPODObject.getProofComments());
			stmt.bindDouble(++i, submitDeliveryPODObject.getFeeAdvance());
			stmt.bindDouble(++i, submitDeliveryPODObject.getWeight());
			stmt.bindDouble(++i, submitDeliveryPODObject.getPieces());

			stmt.bindDouble(++i, submitDeliveryPODObject.getWaitTime());
			stmt.bindString(++i, submitDeliveryPODObject.getReceivedBy());
			stmt.bindLong(++i, 0);// sync
			stmt.bindString(++i, submitDeliveryPODObject.getLatitude());

			stmt.bindString(++i, submitDeliveryPODObject.getLongitude());
			return stmt.executeInsert() != -1;
		} catch (Exception e) {
			throw e;
		}
	}


	final static String kUpdateIntoSubmitDeliveryPODTable = "UPDATE SubmitDeliveryPODTable SET Workorder = ?, AddressLineItem = ?, Date = ?, Time = ?, Comments = ?, FeeAdvance = ?, " +
			"Weight = ?, Pieces = ?, WaitTime = ?, ReceivedBy = ?, isSync = ?, Latitude = ?, Longtitude = ? WHERE SubmitDeliveryPODID =?";

	public boolean UpdateIntoSubmitDeliveryPODTable(SubmitDeliveryPOD submitDeliveryPODObject)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kUpdateIntoSubmitDeliveryPODTable);
			int i = 0;
			stmt.bindString(++i, submitDeliveryPODObject.getWorkorder());
			stmt.bindLong(++i, submitDeliveryPODObject.getAddressLineitem());
			stmt.bindString(++i, submitDeliveryPODObject.getProofDate());
			stmt.bindString(++i, submitDeliveryPODObject.getProofTime());

			stmt.bindString(++i, submitDeliveryPODObject.getProofComments());
			stmt.bindDouble(++i, submitDeliveryPODObject.getFeeAdvance());
			stmt.bindDouble(++i, submitDeliveryPODObject.getWeight());
			stmt.bindDouble(++i, submitDeliveryPODObject.getPieces());

			stmt.bindDouble(++i, submitDeliveryPODObject.getWaitTime());
			stmt.bindString(++i, submitDeliveryPODObject.getReceivedBy());
			stmt.bindLong(++i, 0);// sync
			stmt.bindString(++i, submitDeliveryPODObject.getLatitude());

			stmt.bindString(++i, submitDeliveryPODObject.getLongitude());

			stmt.bindLong(++i, submitDeliveryPODObject.getSubmitDeliveryPODID());
			return stmt.executeUpdateDelete() > 0;
		} catch (Exception e) {
			throw e;
		}
	}


	private final static String kGetLastinsertedIDFromSubmitDeliveryPOD = "SELECT max(SubmitDeliveryPODID) FROM SubmitDeliveryPODTable";

	public int getLastinsertedLineItemFromSubmitDeliveryPOD() {
		int lastInsertedLineItem = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLastinsertedIDFromSubmitDeliveryPOD, null);
		if (cursor.moveToFirst()) {
			do {
				lastInsertedLineItem = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return lastInsertedLineItem;
	}

	final static String kinsertAttachementsToDeliveryPODTable = "INSERT  INTO SubmitDeliveryPODAttachmentTable  ("
			+ "SubmitDeliveryPODAttachmentID, Workorder, AddressLineItem, FileName, PDFInMemory, isSync) VALUES (?, ?, ?, ?, ?, ?)";

	public boolean insertAttachmentsOfDeliveryPOD(
			PODAttachments submitPickupPODAttachments) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kinsertAttachementsToDeliveryPODTable);
			int i = 0;
			stmt.bindLong(++i, submitPickupPODAttachments.getSubmitPODID());
			stmt.bindString(++i, submitPickupPODAttachments.getWorkorder());
			stmt.bindLong(++i, submitPickupPODAttachments.getLineitem());
			stmt.bindString(++i, submitPickupPODAttachments.getFileName());

			stmt.bindString(++i, submitPickupPODAttachments.getPdfInMemory());
			stmt.bindLong(++i, 0); // sync
			return stmt.executeInsert() != -1;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private final static String kisPickupAndDeliveryPODSubmited = "SELECT count(SubmitPickupPODID) FROM SubmitPickupPODTable WHERE Workorder IN "
			+ "(SELECT PickupOpenAddressTable. Workorder  FROM PickupOpenAddressTable INNER JOIN DeliveryOpenAddressTable ON PickupOpenAddressTable. Workorder = DeliveryOpenAddressTable. Workorder) AND  Workorder =?";

	public boolean isPickupAndDeliveryPODSubmited(String workOrder) {
		int countOfPOD = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kisPickupAndDeliveryPODSubmited,
				new String[] { workOrder });
		if (cursor.moveToFirst()) {
			do {
				countOfPOD = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		if (countOfPOD <= 0) {
			return isPickPodAvailableToAdd(workOrder);
		}
		return countOfPOD > 0;
	}

	public boolean isPickPodAvailableToAdd(String workOrder) {

		String isPickupAndDeliveryPODSubmited = "SELECT count(Workorder) FROM PickupOpenAddressTable WHERE Workorder IN (SELECT PickupOpenAddressTable. Workorder  FROM PickupOpenAddressTable INNER JOIN DeliveryOpenAddressTable ON PickupOpenAddressTable. Workorder = DeliveryOpenAddressTable. Workorder) AND  Workorder =?";

		int countOfPOD = 0;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(isPickupAndDeliveryPODSubmited,
				new String[] { workOrder.trim() });
		if (cursor.moveToFirst()) {
			do {
				countOfPOD = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return countOfPOD > 0;
	}

	final static String kGetLocallyAddedFromSubmitDeligenceTable = "SELECT  "
			+ "Lineitem, Workorder, AddressLineitem, DiligenceDate"
			+ ", DiligenceTime,  Report, DiligenceCode, DateTimeSubmitted"
			+ ", Latitude, Longtitude FROM SubmitDiligenceTable  WHERE isSync = 0";

	public ArrayList<SubmitDiligence> getSubmitDiligencesValuesFromDBForUploadingToServer() {
		ArrayList<SubmitDiligence> diligencesArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence diligences = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitDeligenceTable, null);
		if (cursor.moveToFirst()) {
			do {
				diligences = new SubmitDiligence();

				diligences.setLineItem(cursor.getInt(0));
				diligences.setWorkorder(cursor.getString(1).trim());
				diligences.setAddressLineItem(cursor.getInt(2));
				diligences.setDiligenceDate(cursor.getString(3));

				diligences.setDiligenceTime(cursor.getString(4));
				diligences.setReport(cursor.getString(5));
				diligences.setDiligenceCode(cursor.getInt(6));
				diligences.setDateTimeSubmitted(cursor.getString(7));

				diligences.setLatitude(cursor.getString(8));
				diligences.setLongitude(cursor.getString(9));

				diligencesArray.add(diligences);
			} while (cursor.moveToNext());
		}

		return diligencesArray;
	}

	final static String kupdateSubmitDiligenceTableAfterInsertingToServer = "UPDATE SubmitDiligenceTable SET ServerCode = ? , isSync = 1 WHERE Lineitem =?";

	public boolean updateSubmitDiligenceTableAfterInsertingToServer(
			int lineItem, String serverCode) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitDiligenceTableAfterInsertingToServer);
			int i = 0;
			stmt.bindString(++i, serverCode);
			stmt.bindLong(++i, lineItem);
			int result = stmt.executeUpdateDelete();
			return result > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kupdateSubmitAddAddress = "UPDATE ProcessAddressTable SET ServerCode = ? , isSync = 1 WHERE LineItem =?";

	public boolean updateAddaddressTableAfterInsertingToServer(int lineItem,
			String serverCode) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitAddAddress);
			int i = 0;
			stmt.bindString(++i, serverCode);
			stmt.bindLong(++i, lineItem);
			int result = stmt.executeUpdateDelete();
			return result > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kupdateSubmitDiligenceAttachementTableWithServerCode = "UPDATE SubmitDiligenceAttachmentTable SET ServerCode = ?  WHERE DiligenceLineitem =?";

	public boolean updateSubmitDiligenceAttachmentsTableWithServerCode(
			int lineItem, String serverCode) throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitDiligenceAttachementTableWithServerCode);
			stmt.bindString(1, serverCode);
			stmt.bindLong(2, lineItem);
			int result = stmt.executeUpdateDelete();
			return result > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kGetAttachementsOfSubmitDeligenceTableForUpload = "SELECT  DiligenceLineitem, Workorder, DiligenceAddressLineitem, FileName, PDFInMemory, ServerCode  FROM SubmitDiligenceAttachmentTable  WHERE  isSync = 0";

	public ArrayList<SubmitDiligence> getAttachementsFromSubmitDeligenceAttachmentsTableForUpload() {

		ArrayList<SubmitDiligence> diligencesArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence returnDiligences = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitDeligenceTableForUpload, null);
		if (cursor.moveToFirst()) {
			do {
				returnDiligences = new SubmitDiligence();
				returnDiligences.setLineItem(cursor.getInt(0));
				returnDiligences.setWorkorder(cursor.getString(1).trim());
				returnDiligences.setAddressLineItem(cursor.getInt(2));

				returnDiligences.setAttachementsFileName(cursor.getString(3));
				returnDiligences
						.setAttachementBase64String(cursor.getString(4));
				returnDiligences.setServerCode(cursor.getString(5));
				diligencesArray.add(returnDiligences);
			} while (cursor.moveToNext());
		}

		return diligencesArray;

	}

	final static String kGetLocallyAddedFromSubmitcourtStatusSingle = "SELECT "
			+ "workorder, lineitem, statusdate, statustime"
			+ ", report,  servercode, datetimesubmitted From SubmitStatusTable WHERE lineitem = ? AND isSync = 0";


	public SubmitStatusList getSubmitCourtStatusFromDBForUploadingToServerSingle(int Workorder) {

		SubmitStatusList submitCourtPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitcourtStatusSingle, new String[] {Integer.toString(Workorder)},null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitStatusList();

				Log.d("Count", ""+cursor.getCount());

				int i = -1;

				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setLineitem(cursor.getInt(++i));
				submitCourtPOD.setStatusDate(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setStatusTime(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setReport(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setServerCode(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());

				submitCourtPOD.setDateTimeSubmitted(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());


			} while (cursor.moveToNext());
		}

		return submitCourtPOD;

	}



	final static String kGetLocallyAddedFromSubmitPickupStatusSingle = "SELECT "
			+ "workorder, lineitem, statusdate, statustime"
			+ ", report,  servercode, datetimesubmitted From SubmitPickupStatusTable WHERE lineitem = ? AND isSync = 0";


	public SubmitStatusList getSubmitPickupStatusFromDBForUploadingToServerSingle(int Workorder) {

		SubmitStatusList submitCourtPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitPickupStatusSingle, new String[] {Integer.toString(Workorder)},null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitStatusList();

				Log.d("Count", ""+cursor.getCount());

				int i = -1;

				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setLineitem(cursor.getInt(++i));
				submitCourtPOD.setStatusDate(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setStatusTime(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setReport(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setServerCode(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());

				submitCourtPOD.setDateTimeSubmitted(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());


			} while (cursor.moveToNext());
		}

		return submitCourtPOD;

	}



	final static String kGetLocallyAddedFromSubmitDeliveryStatusSingle = "SELECT "
			+ "workorder, lineitem, statusdate, statustime"
			+ ", report,  servercode, datetimesubmitted From SubmitDeliveryStatusTable WHERE lineitem = ? AND isSync = 0";


	public SubmitStatusList getSubmitDileveryStatusFromDBForUploadingToServerSingle(int Workorder) {

		SubmitStatusList submitCourtPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitDeliveryStatusSingle, new String[] {Integer.toString(Workorder)},null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitStatusList();

				Log.d("Count", ""+cursor.getCount());

				int i = -1;

				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setLineitem(cursor.getInt(++i));
				submitCourtPOD.setStatusDate(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setStatusTime(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setReport(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setServerCode(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());

				submitCourtPOD.setDateTimeSubmitted(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());


			} while (cursor.moveToNext());
		}

		return submitCourtPOD;

	}






	final static String kGetLocallyAddedFromSubmitSubmitCourtPODTableSingle = "SELECT "
			+ "SubmitCourtPODID, Workorder, Date, Time"
			+ ", Comments,  FeeAdvance, Weight, Pieces"
			+ ", WaitTime, Latitude, Longtitude FROM SubmitCourtPODTable  WHERE Workorder = ? AND isSync = 0";

	public SubmitCourtPOD getSubmitCourtPODValuesFromDBForUploadingToServerSingle(String Workorder) {

		SubmitCourtPOD submitCourtPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitCourtPODTableSingle, new String[] {Workorder},null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitCourtPOD();

				Log.d("Count", ""+cursor.getCount());

				int i = -1;
				submitCourtPOD.setSubmitCourtPODID(cursor.getInt(++i));
				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setProofDate(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD.setProofTime(cursor.getString(++i) == null ? ""
						: cursor.getString(i));

				submitCourtPOD
						.setProofComments(cursor.getString(++i) == null ? ""
								: cursor.getString(i));
				submitCourtPOD.setFeeAdvance(cursor.getInt(++i));
				submitCourtPOD.setWeight(cursor.getInt(++i));
				submitCourtPOD.setPieces(cursor.getInt(++i));

				submitCourtPOD.setWaitTime(cursor.getInt(++i));
				submitCourtPOD.setLatitude(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD.setLongitude(cursor.getString(++i) == null ? ""
						: cursor.getString(i));


			} while (cursor.moveToNext());
		}

		return submitCourtPOD;

}

	final static String kGetLocallyAddedFromSubmitSubmitCourtPODTable = "SELECT  "
			+ "SubmitCourtPODID, Workorder, Date, Time"
			+ ", Comments,  FeeAdvance, Weight, Pieces"
			+ ", WaitTime, Latitude, Longtitude FROM SubmitCourtPODTable  WHERE isSync = 0";
	public ArrayList<SubmitCourtPOD> getSubmitCourtPODValuesFromDBForUploadingToServer() {
		ArrayList<SubmitCourtPOD> submitCourtPODArray = new ArrayList<SubmitCourtPOD>();
		SubmitCourtPOD submitCourtPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitCourtPODTable, null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitCourtPOD();
				int i = -1;
				submitCourtPOD.setSubmitCourtPODID(cursor.getInt(++i));
				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setProofDate(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD.setProofTime(cursor.getString(++i) == null ? ""
						: cursor.getString(i));

				submitCourtPOD
						.setProofComments(cursor.getString(++i) == null ? ""
								: cursor.getString(i));
				submitCourtPOD.setFeeAdvance(cursor.getInt(++i));
				submitCourtPOD.setWeight(cursor.getInt(++i));
				submitCourtPOD.setPieces(cursor.getInt(++i));

				submitCourtPOD.setWaitTime(cursor.getInt(++i));
				submitCourtPOD.setLatitude(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD.setLongitude(cursor.getString(++i) == null ? ""
						: cursor.getString(i));

				submitCourtPODArray.add(submitCourtPOD);
			} while (cursor.moveToNext());
		}

		return submitCourtPODArray;
	}

	final static String kGetAttachementsOfSubmitCourtPODTableForUpload = "SELECT  "
			+ "SubmitCourtPODAttachmentID, Workorder, FileName, PDFInMemory "
			+ " FROM SubmitCourtPODAttachmentTable  WHERE  isSync = 0";

	public ArrayList<PODAttachments> getAttachementsFromSubmitCourtPODAttachmentTableForUpload() {
		ArrayList<PODAttachments> submitCourtPODArray = new ArrayList<PODAttachments>();
		PODAttachments submitCourtPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitCourtPODTableForUpload, null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new PODAttachments();
				int i = -1;
				submitCourtPOD.setSubmitPODID(cursor.getInt(++i));
				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setFileName(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD
						.setPdfInMemory(cursor.getString(++i) == null ? ""
								: cursor.getString(i));
				submitCourtPODArray.add(submitCourtPOD);
			} while (cursor.moveToNext());
		}

		return submitCourtPODArray;
	}


	final static String kGetAttachementsOfSubmitCourtPODTableForUploadSingle = "SELECT  "
			+ "SubmitCourtPODAttachmentID, Workorder, FileName, PDFInMemory "
			+ " FROM SubmitCourtPODAttachmentTable  WHERE Workorder = ? AND isSync = 0";

	public ArrayList<PODAttachments> getAttachementsFromSubmitCourtPODAttachmentTableForUploadSingle(String Workorder) {
		ArrayList<PODAttachments> submitCourtPODArray = new ArrayList<PODAttachments>();
		PODAttachments submitCourtPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitCourtPODTableForUploadSingle,new String[] {Workorder}, null);
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new PODAttachments();
				int i = -1;
				submitCourtPOD.setSubmitPODID(cursor.getInt(++i));
				submitCourtPOD.setWorkorder(cursor.getString(++i) == null ? ""
						: cursor.getString(i).trim());
				submitCourtPOD.setFileName(cursor.getString(++i) == null ? ""
						: cursor.getString(i));
				submitCourtPOD
						.setPdfInMemory(cursor.getString(++i) == null ? ""
								: cursor.getString(i));
				submitCourtPODArray.add(submitCourtPOD);
			} while (cursor.moveToNext());
		}

		return submitCourtPODArray;
	}


	final static String kGetAttachementsOfSubmitCourtPODTableForUploadForQueue = "SELECT  "
			+ "SubmitCourtPODAttachmentID, Workorder, FileName, PDFInMemory "
			+ " FROM SubmitCourtPODAttachmentTable  WHERE  Workorder = ?";

	public ArrayList<SubmitCourtPOD> getAttachementsFromSubmitCourtPODAttachmentTableForUploadForQueue(String Workorder) {
		ArrayList<SubmitCourtPOD> submitCourtPODArray = new ArrayList<SubmitCourtPOD>();
		SubmitCourtPOD submitCourtPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitCourtPODTableForUploadForQueue, new String[] {Workorder});
		if (cursor.moveToFirst()) {
			do {
				submitCourtPOD = new SubmitCourtPOD();

				submitCourtPOD.setAddressLineitem(cursor.getInt(0));
				submitCourtPOD.setWorkorder(cursor.getString(1));
				submitCourtPOD.setAttachmentFilename(cursor.getString(2));
				submitCourtPOD
						.setAttahmentBase64String(cursor.getString(3));
				submitCourtPODArray.add(submitCourtPOD);
			} while (cursor.moveToNext());
		}

		return submitCourtPODArray;
	}



	final static String kGetLocallyAddedFromSubmitSubmitPickupPODTable = "SELECT  SubmitPickupPODID, Workorder, AddressLineItem, Date, Time, Comments, Latitude, Longtitude FROM SubmitPickupPODTable  WHERE isSync = 0";

	public ArrayList<SubmitPickupPOD> getSubmitPickupPODValuesFromDBForUploadingToServer() {
		ArrayList<SubmitPickupPOD> submitPickupPODArray = new ArrayList<SubmitPickupPOD>();
		SubmitPickupPOD submitPickupPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitPickupPODTable, null);
		if (cursor.moveToFirst()) {
			do {
				submitPickupPOD = new SubmitPickupPOD();
				submitPickupPOD.setSubmitPickupPODID(cursor.getInt(0));
				submitPickupPOD.setWorkorder(cursor.getString(1).trim());
				submitPickupPOD.setAddressLineitem(cursor.getInt(2));
				submitPickupPOD.setProofDate(cursor.getString(3));
				submitPickupPOD.setProofTime(cursor.getString(4));
				submitPickupPOD.setProofComments(cursor.getString(5));
				submitPickupPOD.setLatitude(cursor.getString(6));
				submitPickupPOD.setLongitude(cursor.getString(7));
				submitPickupPODArray.add(submitPickupPOD);
			} while (cursor.moveToNext());
		}

		return submitPickupPODArray;
	}

	final static String kGetLocallyAddedFromSubmitSubmitPickupPODTableSingle = "SELECT  SubmitPickupPODID, Workorder, AddressLineItem, Date, Time, Comments, Latitude, Longtitude FROM SubmitPickupPODTable  WHERE Workorder = ? AND isSync = 0";

	public SubmitPickupPOD getSubmitPickupPODValuesFromDBForUploadingToServerSingle(String Workorder) {

		SubmitPickupPOD submitPickupPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitPickupPODTableSingle,new String[] {Workorder}, null);
		if (cursor.moveToFirst()) {
			do {
				submitPickupPOD = new SubmitPickupPOD();
				submitPickupPOD.setSubmitPickupPODID(cursor.getInt(0));
				submitPickupPOD.setWorkorder(cursor.getString(1).trim());
				submitPickupPOD.setAddressLineitem(cursor.getInt(2));
				submitPickupPOD.setProofDate(cursor.getString(3));
				submitPickupPOD.setProofTime(cursor.getString(4));
				submitPickupPOD.setProofComments(cursor.getString(5));
				submitPickupPOD.setLatitude(cursor.getString(6));
				submitPickupPOD.setLongitude(cursor.getString(7));

			} while (cursor.moveToNext());
		}

		return submitPickupPOD;
	}


	final static String kGetAttachementsOfSubmitPickupPODTableForUpload = "SELECT  SubmitPickupPODAttachmentID, Workorder, AddressLineItem, FileName, PDFInMemory  FROM SubmitPickupPODAttachmentTable  WHERE  isSync = 0";

	public ArrayList<PODAttachments> getAttachementsFromSubmitPickupPODAttachmentTableForUpload() {
		ArrayList<PODAttachments> submitPickupPODPODArray = new ArrayList<PODAttachments>();
		PODAttachments submitPickupPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitPickupPODTableForUpload, null);
		if (cursor.moveToFirst()) {
			do {
				submitPickupPOD = new PODAttachments();
				submitPickupPOD.setSubmitPODID(cursor.getInt(0));
				submitPickupPOD.setWorkorder(cursor.getString(1).trim());
				submitPickupPOD.setLineitem(cursor.getInt(2));
				submitPickupPOD.setFileName(cursor.getString(3));
				submitPickupPOD.setPdfInMemory(cursor.getString(4));
				submitPickupPODPODArray.add(submitPickupPOD);
			} while (cursor.moveToNext());
		}

		return submitPickupPODPODArray;
	}

	final static String kGetAttachementsOfSubmitPickupPODTableForUploadForQueue = "SELECT  SubmitPickupPODAttachmentID, Workorder, AddressLineItem, FileName, PDFInMemory  FROM SubmitPickupPODAttachmentTable  WHERE  Workorder = ?";

	public ArrayList<PODAttachments> getAttachementsFromSubmitPickupPODAttachmentTableForUploadForQueue(String Workorder) {
		ArrayList<PODAttachments> submitPickupPODPODArray = new ArrayList<PODAttachments>();
		PODAttachments submitPickupPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitPickupPODTableForUploadForQueue, new String[] { Workorder });
		if (cursor.moveToFirst()) {
			do {
				submitPickupPOD = new PODAttachments();
				submitPickupPOD.setSubmitPODID(cursor.getInt(0));
				submitPickupPOD.setWorkorder(cursor.getString(1).trim());
				submitPickupPOD.setLineitem(cursor.getInt(2));
				submitPickupPOD.setFileName(cursor.getString(3));
				submitPickupPOD.setPdfInMemory(cursor.getString(4));
				submitPickupPODPODArray.add(submitPickupPOD);
			} while (cursor.moveToNext());
		}

		return submitPickupPODPODArray;
	}



	final static String kGetLocallyAddedFromSubmitSubmitDeliveryPODTable = "SELECT  SubmitDeliveryPODID, Workorder, AddressLineItem, Date, Time, Comments,  FeeAdvance, Weight, Pieces, WaitTime, ReceivedBy, Latitude, Longtitude FROM SubmitDeliveryPODTable  WHERE isSync = 0";

	public ArrayList<SubmitDeliveryPOD> getSubmitDeliveryPODValuesFromDBForUploadingToServer() {
		ArrayList<SubmitDeliveryPOD> submitPickupPODArray = new ArrayList<SubmitDeliveryPOD>();
		SubmitDeliveryPOD submitDeliveryPOD = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitDeliveryPODTable, null);
		if (cursor.moveToFirst()) {
			do {
				submitDeliveryPOD = new SubmitDeliveryPOD();
				submitDeliveryPOD.setSubmitDeliveryPODID(cursor.getInt(0));
				submitDeliveryPOD.setWorkorder(cursor.getString(1).trim());
				submitDeliveryPOD.setAddressLineitem(cursor.getInt(2));

				submitDeliveryPOD.setProofDate(cursor.getString(3));
				submitDeliveryPOD.setProofTime(cursor.getString(4));
				submitDeliveryPOD.setProofComments(cursor.getString(5));
				submitDeliveryPOD.setFeeAdvance(cursor.getInt(6));

				submitDeliveryPOD.setWeight(cursor.getInt(7));
				submitDeliveryPOD.setPieces(cursor.getInt(8));
				submitDeliveryPOD.setWaitTime(cursor.getInt(9));
				submitDeliveryPOD.setReceivedBy(cursor.getString(10));

				submitDeliveryPOD.setLatitude(cursor.getString(11));
				submitDeliveryPOD.setLongitude(cursor.getString(12));
				submitPickupPODArray.add(submitDeliveryPOD);
			} while (cursor.moveToNext());
		}

		return submitPickupPODArray;
	}

	final static String kGetLocallyAddedFromSubmitSubmitDeliveryPODTableSingle = "SELECT  SubmitDeliveryPODID, Workorder, AddressLineItem, Date, Time, Comments,  FeeAdvance, Weight, Pieces, WaitTime, ReceivedBy, Latitude, Longtitude FROM SubmitDeliveryPODTable  WHERE Workorder = ? AND isSync = 0";

	public SubmitDeliveryPOD getSubmitDeliveryPODValuesFromDBForUploadingToServerSingle(String Workorder) {

		SubmitDeliveryPOD submitDeliveryPOD = null;
		@SuppressLint({"NewApi", "Recycle", "LocalSuppress"}) Cursor cursor = database.rawQuery(
				kGetLocallyAddedFromSubmitSubmitDeliveryPODTableSingle,new String[] { Workorder } , null);
		if (cursor.moveToFirst()) {
			do {
				submitDeliveryPOD = new SubmitDeliveryPOD();
				submitDeliveryPOD.setSubmitDeliveryPODID(cursor.getInt(0));
				submitDeliveryPOD.setWorkorder(cursor.getString(1).trim());
				submitDeliveryPOD.setAddressLineitem(cursor.getInt(2));

				submitDeliveryPOD.setProofDate(cursor.getString(3));
				submitDeliveryPOD.setProofTime(cursor.getString(4));
				submitDeliveryPOD.setProofComments(cursor.getString(5));
				submitDeliveryPOD.setFeeAdvance(cursor.getInt(6));

				submitDeliveryPOD.setWeight(cursor.getInt(7));
				submitDeliveryPOD.setPieces(cursor.getInt(8));
				submitDeliveryPOD.setWaitTime(cursor.getInt(9));
				submitDeliveryPOD.setReceivedBy(cursor.getString(10));

				submitDeliveryPOD.setLatitude(cursor.getString(11));
				submitDeliveryPOD.setLongitude(cursor.getString(12));

			} while (cursor.moveToNext());
		}

		return submitDeliveryPOD;
	}


	final static String kGetAttachementsOfSubmitDeliveryPODTableForUpload = "SELECT  SubmitDeliveryPODAttachmentID, Workorder, AddressLineItem, FileName, PDFInMemory  FROM SubmitDeliveryPODAttachmentTable  WHERE  isSync = 0";

	public ArrayList<PODAttachments> getAttachementsFromSubmitDeliveryPODAttachmentTableForUpload() {
		ArrayList<PODAttachments> podAttachmentList = new ArrayList<PODAttachments>();
		PODAttachments podAttachment = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitDeliveryPODTableForUpload, null);
		if (cursor.moveToFirst()) {
			do {
				podAttachment = new PODAttachments();

				podAttachment.setSubmitPODID(cursor.getInt(0));
				podAttachment.setWorkorder(cursor.getString(1).trim());
				podAttachment.setLineitem(cursor.getInt(2));
				podAttachment.setFileName(cursor.getString(3));
				podAttachment.setPdfInMemory(cursor.getString(4));

				podAttachmentList.add(podAttachment);
			} while (cursor.moveToNext());
		}

		return podAttachmentList;
	}


	final static String kGetAttachementsOfSubmitDeliveryPODTableForUploadForQueue = "SELECT  SubmitDeliveryPODAttachmentID, Workorder, AddressLineItem, FileName, PDFInMemory  FROM SubmitDeliveryPODAttachmentTable  WHERE  Workorder = ?";

	public ArrayList<PODAttachments> getAttachementsFromSubmitDeliveryPODAttachmentTableForUploadForQueue(String Workorder) {
		ArrayList<PODAttachments> podAttachmentList = new ArrayList<PODAttachments>();
		PODAttachments podAttachment = null;
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfSubmitDeliveryPODTableForUploadForQueue, new String[] { Workorder});
		if (cursor.moveToFirst()) {
			do {
				podAttachment = new PODAttachments();

				podAttachment.setSubmitPODID(cursor.getInt(0));
				podAttachment.setWorkorder(cursor.getString(1).trim());
				podAttachment.setLineitem(cursor.getInt(2));
				podAttachment.setFileName(cursor.getString(3));
				podAttachment.setPdfInMemory(cursor.getString(4));

				podAttachmentList.add(podAttachment);
			} while (cursor.moveToNext());
		}

		return podAttachmentList;
	}

	final static String kupdateSubmitStatusTableAfterSync = "UPDATE SubmitStatusTable SET isSync = 1  WHERE lineitem =?";

	public void updateSubmitStatusTable(int lineItem)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitStatusTableAfterSync);

			stmt.bindLong(1, lineItem);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kupdateSubmitPickupStatusTableAfterSync = "UPDATE SubmitPickupStatusTable SET isSync = 1  WHERE lineitem =?";

	public void updateSubmitPickupStatusTable(int lineItem)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitPickupStatusTableAfterSync);

			stmt.bindLong(1, lineItem);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kupdateSubmitDeliveryStatusTableAfterSync = "UPDATE SubmitDeliveryStatusTable SET isSync = 1  WHERE lineitem =?";

	public void updateSubmitDileveryStatusTable(int lineItem)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitDeliveryStatusTableAfterSync);

			stmt.bindLong(1, lineItem);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}
	}

	private final static String kFinalStatusToSubmit = "SELECT  FinalStatusLineItem, Workorder, AddressLineItem, Servee, Entity, AuthorizedAgentTitle, AgentForService,"
			+ "MannerOfServiceCode, LeftWith, Relationship, ServeDate, ServeTime, Age, Hair, Skin, Height, Weight, Eyes, DistinguishingMarks,"
			+ "Latitude, Longtitude, InUniform, Military, Police, ServeeIsMale , Report FROM  SubmitFinalStatusTable  WHERE isSync = 0";

	public ArrayList<ProcessAddressForServer> getFinalStatusToSubmit() {

		ArrayList<ProcessAddressForServer> result = new ArrayList<ProcessAddressForServer>();
		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kFinalStatusToSubmit, null);
		if (cursor.moveToFirst()) {
			do {
				ProcessAddressForServer address = new ProcessAddressForServer();
				address.setLineItem(cursor.getInt(0));
				address.setWorkorder(cursor.getString(1).trim());
				address.setAddressLineItem(cursor.getInt(2));
				address.setServee(cursor.getString(3).trim());
				address.setEntity(cursor.getInt(4) == 1);
				address.setAuthorizedAgentTitle(cursor.getString(5).trim());
				address.setAuthorizedAgent(cursor.getString(6).trim());
				address.setMannerofServicecode(cursor.getString(7).trim());
				address.setLeftWith(cursor.getString(8).trim());
				address.setRelation(cursor.getString(9).trim());
				address.setServeDate(cursor.getString(10).trim());
				address.setServeTime(cursor.getString(11).trim());
				address.setAge(cursor.getString(12).trim());
				address.setHair(cursor.getString(13).trim());
				address.setSkin(cursor.getString(14).trim());
				address.setHeight(cursor.getString(15).trim());
				address.setWeight(cursor.getString(16).trim());
				address.setEyes(cursor.getString(17).trim());
				address.setMarks(cursor.getString(18).trim());
				address.setLatitude(cursor.getString(19).trim());
				address.setLongitude(cursor.getString(20).trim());
				address.setInuniform(cursor.getInt(21) == 1);
				address.setMilitary(cursor.getInt(22) == 1);
				address.setPolice(cursor.getInt(23) == 1);
				address.setServeeIsMale(cursor.getInt(24) == 1);

				address.setReport(cursor.getString(25));

				result.add(address);
			} while (cursor.moveToNext());
		}
		return result;
	}

	final static String kupdateSubmitDiligenceAttachementTableAfterSync = "UPDATE SubmitDiligenceAttachmentTable SET isSync = 1  WHERE DiligenceLineitem =?";

	public void updateSubmitDiligenceAttachmentsTableAfterSync(int lineItem)
			throws Exception {
		try {
			SQLiteStatement stmt = database
					.compileStatement(kupdateSubmitDiligenceAttachementTableAfterSync);

			stmt.bindLong(1, lineItem);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}
	}

	final static String kFinalStatusToUploadToServer = "SELECT  "
			+ "FinalStatusLineItem, Workorder, AddressLineItem, Servee"
			+ ", AuthorizedAgentTitle,  AgentForService, LeftWith, RelationShip"
			+ ", ServeDate, ServeTime, Age, Height"
			+ ", Weight, Skin, Hair, Eyes"
			+ ", DistinguishingMarks, DateTimeSubmitted, MannerOfServiceCode, Entity"
			+ ", ServeeIsMale, InUniform, Military, Police"
			+ ", Latitude, Longtitude, Report FROM  SubmitFinalStatusTable  WHERE isSync = 0";

	public ArrayList<ProcessAddressForServer> getFinalStatusToUploadToServer() {
		ArrayList<ProcessAddressForServer> SubmitFinalStatusArray = new ArrayList<ProcessAddressForServer>();
		ProcessAddressForServer SubmitFinalStatus = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kFinalStatusToUploadToServer, null);

		if (cursor.moveToFirst()) {
			do {
				SubmitFinalStatus = new ProcessAddressForServer();
				SubmitFinalStatus.setLineItem(cursor.getInt(0));
				SubmitFinalStatus.setWorkorder(cursor.getString(1).trim());
				SubmitFinalStatus.setAddressLineItem(cursor.getInt(2));
				SubmitFinalStatus.setServee(cursor.getString(3));

				SubmitFinalStatus.setAuthorizedAgentTitle(cursor.getString(4));
				SubmitFinalStatus.setAuthorizedAgent(cursor.getString(5));
				SubmitFinalStatus.setLeftWith(cursor.getString(6));
				SubmitFinalStatus.setAgentForServiceRelationShipToServee(cursor
						.getString(7));

				SubmitFinalStatus.setServeDate(cursor.getString(8));
				SubmitFinalStatus.setServeTime(cursor.getString(9));
				SubmitFinalStatus.setAge(cursor.getString(10));
				SubmitFinalStatus.setHeight(cursor.getString(11));

				SubmitFinalStatus.setWeight(cursor.getString(12));
				SubmitFinalStatus.setSkin(cursor.getString(13));
				SubmitFinalStatus.setHair(cursor.getString(14));
				SubmitFinalStatus.setEyes(cursor.getString(15));

				SubmitFinalStatus.setMarks(cursor.getString(16));
				SubmitFinalStatus.setDateTimeSubmitted(cursor.getString(17));
				SubmitFinalStatus.setMannerofServicecode(cursor.getString(18));
				SubmitFinalStatus.setEntity(cursor.getLong(19) == 1);

				SubmitFinalStatus.setServeeIsMale(cursor.getInt(20) == 1);
				SubmitFinalStatus.setInuniform(cursor.getInt(21) == 1);
				SubmitFinalStatus.setMilitary(cursor.getInt(22) == 1);
				SubmitFinalStatus.setPolice(cursor.getInt(23) == 1);

				SubmitFinalStatus.setLatitude(cursor.getString(24));
				SubmitFinalStatus.setLongitude(cursor.getString(25));
				SubmitFinalStatus.setReport(cursor.getString(26));

				SubmitFinalStatusArray.add(SubmitFinalStatus);
			} while (cursor.moveToNext());
		}

		return SubmitFinalStatusArray;
	}

	public ProcessAddressForServer getFinalStatusToUpload() {

		ProcessAddressForServer SubmitFinalStatus = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(kFinalStatusToUploadToServer, null);


				SubmitFinalStatus = new ProcessAddressForServer();
				SubmitFinalStatus.setLineItem(cursor.getInt(0));
				SubmitFinalStatus.setWorkorder(cursor.getString(1).trim());
				SubmitFinalStatus.setAddressLineItem(cursor.getInt(2));
				SubmitFinalStatus.setServee(cursor.getString(3));

				SubmitFinalStatus.setAuthorizedAgentTitle(cursor.getString(4));
				SubmitFinalStatus.setAuthorizedAgent(cursor.getString(5));
				SubmitFinalStatus.setLeftWith(cursor.getString(6));
				SubmitFinalStatus.setAgentForServiceRelationShipToServee(cursor
						.getString(7));

				SubmitFinalStatus.setServeDate(cursor.getString(8));
				SubmitFinalStatus.setServeTime(cursor.getString(9));
				SubmitFinalStatus.setAge(cursor.getString(10));
				SubmitFinalStatus.setHeight(cursor.getString(11));

				SubmitFinalStatus.setWeight(cursor.getString(12));
				SubmitFinalStatus.setSkin(cursor.getString(13));
				SubmitFinalStatus.setHair(cursor.getString(14));
				SubmitFinalStatus.setEyes(cursor.getString(15));

				SubmitFinalStatus.setMarks(cursor.getString(16));
				SubmitFinalStatus.setDateTimeSubmitted(cursor.getString(17));
				SubmitFinalStatus.setMannerofServicecode(cursor.getString(18));
				SubmitFinalStatus.setEntity(cursor.getLong(19) == 1);

				SubmitFinalStatus.setServeeIsMale(cursor.getInt(20) == 1);
				SubmitFinalStatus.setInuniform(cursor.getInt(21) == 1);
				SubmitFinalStatus.setMilitary(cursor.getInt(22) == 1);
				SubmitFinalStatus.setPolice(cursor.getInt(23) == 1);

				SubmitFinalStatus.setLatitude(cursor.getString(24));
				SubmitFinalStatus.setLongitude(cursor.getString(25));
				SubmitFinalStatus.setReport(cursor.getString(26));




		return SubmitFinalStatus;
	}
	final static String kGetAttachementsOfFinaleTableForUpload = "SELECT  "
			+ "FinalStatusLineitem, Workorder, FinalStatusAddressLineitem, FileName"
			+ ", PDFInMemory  FROM FinalStatuseAttachmentTable  WHERE  isSync = 0 ";

	public ArrayList<SubmitDiligence> getAttachementsFromFinalStatuseAttachmentTableForUpload()
			throws Exception {
		ArrayList<SubmitDiligence> submitDiligenceArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence submitDiligence = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfFinaleTableForUpload, null);
		if (cursor.moveToFirst()) {
			do {
				submitDiligence = new SubmitDiligence();
				submitDiligence.setLineItem(cursor.getInt(0));
				submitDiligence.setWorkorder(cursor.getString(1).trim());
				submitDiligence.setAddressLineItem(cursor.getInt(2));

				submitDiligence.setAttachementsFileName(cursor.getString(3));
				submitDiligence.setAttachementBase64String(cursor.getString(4));

				submitDiligenceArray.add(submitDiligence);
			} while (cursor.moveToNext());
		}

		return submitDiligenceArray;
	}



	final static String kGetAttachementsOfFinaleTableForUploadForJobQueue = "SELECT  "
			+ "FinalStatusLineitem, Workorder, FinalStatusAddressLineitem, FileName"
			+ ", PDFInMemory  FROM FinalStatuseAttachmentTable  WHERE  FinalStatusLineitem = ? ";


	public ArrayList<SubmitDiligence> getAttachementsFromFinalStatuseAttachmentTableForUploadForJobQueue(int FinalStatusLineitem)
			throws Exception {
		ArrayList<SubmitDiligence> submitDiligenceArray = new ArrayList<SubmitDiligence>();
		SubmitDiligence submitDiligence = null;

		@SuppressLint("Recycle") Cursor cursor = database.rawQuery(
				kGetAttachementsOfFinaleTableForUploadForJobQueue, new String[] { FinalStatusLineitem + "" });
		if (cursor.moveToFirst()) {
			do {
				submitDiligence = new SubmitDiligence();
				submitDiligence.setLineItem(cursor.getInt(0));
				submitDiligence.setWorkorder(cursor.getString(1).trim());
				submitDiligence.setAddressLineItem(cursor.getInt(2));

				submitDiligence.setAttachementsFileName(cursor.getString(3));
				submitDiligence.setAttachementBase64String(cursor.getString(4));

				submitDiligenceArray.add(submitDiligence);
			} while (cursor.moveToNext());
		}

		return submitDiligenceArray;
	}



	final static String kdeleteFinalStausTableAfterSync = "DELETE FROM SubmitFinalStatusTable  WHERE FinalStatusLineItem =?";

	public void deleteFinalStausTableAfterSync(int lineItem) {

		database.execSQL(kdeleteFinalStausTableAfterSync,
				new Object[] { lineItem });
	}

	final static String kdeleteAddaddress = "DELETE FROM ProcessAddressTable  WHERE LineItem =?";

	public void deleteAddaddress(int lineItem) {
		database.execSQL(kdeleteAddaddress, new Object[] { lineItem });
	}

	final static String kdeleteSubmitDiligence = "DELETE FROM SubmitDiligenceTable  WHERE LineItem =?";

	public void deleteSubmitDiligence(int lineItem) {
		database.execSQL(kdeleteSubmitDiligence, new Object[] { lineItem });
	}

	final static String kdeleteFinalStatusAttachementTableWithServerCode = "DELETE FROM FinalStatuseAttachmentTable WHERE FinalStatusLineItem =?";

	public void deleteFinalStatusAttachementTable(int lineItem)
			throws Exception {
		try {
			database.execSQL(kdeleteFinalStatusAttachementTableWithServerCode,
					new Object[] { lineItem });

		} catch (Exception e) {
			throw e;
		}
	}

	final static String kdeleteSubmitCourtPODTableAfterSync = "DELETE FROM SubmitCourtPODTable  WHERE SubmitCourtPODID =?";

	public void deleteSubmitCourtPODTableAfterSync(int submitCourtPODID) {

		database.execSQL(kdeleteSubmitCourtPODTableAfterSync,
				new Object[] { submitCourtPODID });
	}

	final static String kdeleteSubmitCourtPODAttachementTableAfterSyncFromQueue = "DELETE FROM SubmitCourtPODAttachmentTable WHERE Workorder =?";

	public void deleteSubmitCourtPODAttachementTableFromQueue(String workorder) {

		database.execSQL(
				kdeleteSubmitCourtPODAttachementTableAfterSyncFromQueue,
				new Object[] { workorder });
	}

	final static String kdeleteSubmitPickupPODTableAfterSync = "DELETE FROM SubmitPickupPODTable  WHERE SubmitPickupPODID =?";

	public void deleteSubmitPickupPODTableAfterSync(int submitPickupPODID) {
		database.execSQL(kdeleteSubmitPickupPODTableAfterSync,
				new Object[] { submitPickupPODID });
	}

	final static String kdeleteSubmitPickupPODAttachementTableAfterSync = "DELETE FROM SubmitPickupPODAttachmentTable WHERE SubmitPickupPODAttachmentID = ? AND FileName =?";

	public void deleteSubmitPickupPODAttachementTable(
			PODAttachments submitPickupPODID) throws Exception {

		database.execSQL(kdeleteSubmitPickupPODAttachementTableAfterSync,
				new Object[] { submitPickupPODID.getSubmitPODID(),
						submitPickupPODID.getFileName() });
	}


	final static String getKdeleteSubmitstatuslistAfterSync = "DELETE FROM SubmitStatusTable WHERE lineitem = ?";

	public void setKdeleteSubmitstatuslist(
			SubmitStatusList submitPickupPODID) throws Exception {

		database.execSQL(getKdeleteSubmitstatuslistAfterSync,
				new Object[] { submitPickupPODID.getLineitem(),
						});
	}

	final static String getKdeleteSubmitPickupstatuslistAfterSync = "DELETE FROM SubmitPickupStatusTable WHERE lineitem = ?";

	public void setKdeleteSubmitpickupstatuslist(
			SubmitStatusList submitPickupPODID) throws Exception {

		database.execSQL(getKdeleteSubmitPickupstatuslistAfterSync,
				new Object[] { submitPickupPODID.getLineitem(),
				});
	}


	final static String getKdeleteSubmitDeliverystatuslistAfterSync = "DELETE FROM SubmitDeliveryStatusTable WHERE lineitem = ?";

	public void setKdeleteSubmitdeliverystatuslist(
			SubmitStatusList submitPickupPODID) throws Exception {

		database.execSQL(getKdeleteSubmitDeliverystatuslistAfterSync,
				new Object[] { submitPickupPODID.getLineitem(),
				});
	}

	final static String kdeleteSubmitPickupPODAttachementTableAfterSyncFromQueue = "DELETE FROM SubmitPickupPODAttachmentTable WHERE Workorder =?";

	public void deleteSubmitPickupPODAttachementTableFromQueue(String workorder) {

		database.execSQL(
				kdeleteSubmitPickupPODAttachementTableAfterSyncFromQueue,
				new Object[] { workorder });
	}

	final static String kdeleteSubmitDeliveryPODTableAfterSync = "DELETE FROM SubmitDeliveryPODTable  WHERE SubmitDeliveryPODID =?";

	public void deleteSubmitDeliveryPODTableAfterSync(int submitDeliveryPODID) {

		database.execSQL(kdeleteSubmitDeliveryPODTableAfterSync,
				new Object[] { submitDeliveryPODID });
	}

	final static String kdeleteSubmitDeliveryPODAttachementTableAfterSync = "DELETE FROM SubmitDeliveryPODAttachmentTable WHERE SubmitDeliveryPODAttachmentID = ? AND FileName =?";

	public void deleteSubmitDeliveryPODAttachementTable(
			PODAttachments podAttachments) {

		database.execSQL(
				kdeleteSubmitDeliveryPODAttachementTableAfterSync,
				new Object[] { podAttachments.getSubmitPODID(),
						podAttachments.getFileName() });
	}

	final static String kdeleteSubmitDeliveryPODAttachementTableAfterSyncFromQueue = "DELETE FROM SubmitDeliveryPODAttachmentTable WHERE Workorder =?";

	public void deleteSubmitDeliveryPODAttachementTableFromQueue(
			String workorder) {

		database.execSQL(
				kdeleteSubmitDeliveryPODAttachementTableAfterSyncFromQueue,
				new Object[] { workorder });
	}

	private final static String kdeleteSubmitCourtPODAttachementTableAfterSync = "DELETE FROM SubmitCourtPODAttachmentTable WHERE SubmitCourtPODAttachmentID = ? AND FileName =?";

	public void deleteSubmitCourtPODAttachementTable(PODAttachments object) {
		database.execSQL(kdeleteSubmitCourtPODAttachementTableAfterSync,
				new Object[] { object.getSubmitPODID(), object.getFileName() });
	}

	public static BSKmlResult getGeoCodeAddress(String address) {
		try {
			ViewPortBounds viewportBiasing = new ViewPortBounds();
			viewportBiasing
					.setSouthWest(new GEOLocation(34.172684, -118.604794));
			viewportBiasing
					.setNorthEast(new GEOLocation(34.236144, -118.500938));

			boolean useHTTP = true;
			String geocodeUrl = (useHTTP ? "http" : "https")
					+ "://maps.google.com/maps/api/geocode/xml?sensor=false&address="
					+ URLEncoder.encode(address, "UTF-8");

			if (viewportBiasing != null) {
				String boundsString = viewportBiasing.getSouthWest()
						.getLatitude()
						+ ","
						+ viewportBiasing.getSouthWest().getLongitude()
						+ "|"
						+ viewportBiasing.getNorthEast().getLatitude()
						+ ","
						+ viewportBiasing.getNorthEast().getLongitude();

				geocodeUrl = geocodeUrl + "&bounds="
						+ URLEncoder.encode(boundsString, "UTF-8");
			}

			String xmlString = RestFulWebservice.request(geocodeUrl);

			if (xmlString == null || xmlString.equals("")) {
				return null;
			}
			return BSKmlResult.parseObject(xmlString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<SplatterCourtPOD> getCourtPODComparison(String addresforDis,String casename)
			throws Exception {
		ArrayList<SplatterCourtPOD> CourtArray = new ArrayList<SplatterCourtPOD>();
		SplatterCourtPOD splattercourt = null;

		@SuppressLint("Recycle") Cursor PODCount = database
				.rawQuery(
						"select Workorder, Name from CourtOpenAddressTable where AddressFormattedForDisplay='"
								+ addresforDis + "'", null);
		if (PODCount.moveToFirst()) {
			do {
				splattercourt = new SplatterCourtPOD();
				splattercourt.setWorkorder(PODCount.getString(0));
				splattercourt.setServee_name(PODCount.getString(1));
				CourtArray.add(splattercourt);
			} while (PODCount.moveToNext());
		}
		return CourtArray;
	}

}