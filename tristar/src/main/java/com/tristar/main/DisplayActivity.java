package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tristar.db.DbHelper;
import com.tristar.utils.SessionData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class DisplayActivity extends Activity implements OnClickListener{

	private DbHelper mHelper;
	private SQLiteDatabase dataBase;
	ToggleButton debugLog;
	Button btnClearLog;
	String server, company, password, companyname, selectedserver;
	private ArrayList<String> userId = new ArrayList<String>();
	private ArrayList<String> user_fName = new ArrayList<String>();
	private ArrayList<String> user_lName = new ArrayList<String>();
	private ArrayList<String> user_pName = new ArrayList<String>();
	private ArrayList<String> user_pcompanyname = new ArrayList<String>();
	public static SharedPreferences loginPreferencess;
	public static SharedPreferences.Editor loginPrefsEditors;

	public static SharedPreferences notificationPreferencess;
	public static SharedPreferences.Editor notificationPrefsEditors;

	String shared_CompanyName,shared_Companycode,shared_userid,shared_password;
	String Shared_NotificationName;

	private ListView userList;
	private AlertDialog.Builder build;
	private SharedPreference sharedPreference;

	private ArrayList<String> notificationList = new ArrayList<>();
	private ArrayList<String> notificationListvalues = new ArrayList<>();
	//CheckBox debugLog;
	Button sendMail;

	Activity context = this;

	private static String filename = "winserve_log";
	LinearLayout layoutNotifications;
	TextView NotificationValues;

	ArrayAdapter<String> arrayAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_activity);

		notificationList.add("5 Minutes");
		notificationList.add("10 Minutes");
		notificationList.add("15 Minutes");
		notificationList.add("30 Minutes");
		notificationList.add("1 hour");




		notificationListvalues.add("5");
		notificationListvalues.add("10");
		notificationListvalues.add("15");
		notificationListvalues.add("30");
		notificationListvalues.add("60");

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());

		arrayAdapter = new ArrayAdapter<String>(DisplayActivity.this,
				R.layout.notification_row, R.id.txt_Category, notificationList);


		File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+filename+".txt");

		if (!logFile.exists()) {
			try {
				Log.d("File created ", "File created ");
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		sendMail = (Button)findViewById(R.id.send_mail);

		userList = (ListView) findViewById(R.id.list);

		debugLog = (ToggleButton) findViewById(R.id.debug);
		btnClearLog = (Button) findViewById(R.id.btnClearLog);

		layoutNotifications = (LinearLayout) findViewById(R.id.select_notification);
		NotificationValues = (TextView) findViewById(R.id.values);
		layoutNotifications.setOnClickListener(this);

		if(SessionData.getInstance().getLogger()==0){
			debugLog.setChecked(false);
			sendMail.setVisibility(View.GONE);
		}else{
			debugLog.setChecked(true);
			sendMail.setVisibility(View.VISIBLE);
		}

		debugLog.setOnClickListener(this);
		btnClearLog.setOnClickListener(this);

		mHelper = new DbHelper(this);
		sharedPreference = new SharedPreference();

		sendMail.setOnClickListener(this);

		findViewById(R.id.serverlist).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						AddActivity.class);
				i.putExtra("update", false);
				startActivity(i);

			}
		});
		if (userList.equals(null)) {
			selectedserver = null;
		}

		loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		loginPrefsEditors = loginPreferencess.edit();

		shared_CompanyName = loginPreferencess.getString("Company Name","");
		shared_Companycode = loginPreferencess.getString("Company Code","");
		shared_userid = loginPreferencess.getString("user Id","");
		shared_password = loginPreferencess.getString("password","");


		notificationPreferencess = getSharedPreferences("notification",MODE_PRIVATE);
		notificationPrefsEditors = notificationPreferencess.edit();
		Shared_NotificationName = notificationPreferencess.getString("notificationvalue","");

		if(Shared_NotificationName.length()==0){
			NotificationValues.setText("5 minutes");
		}else if(Shared_NotificationName.contains("60")){
			NotificationValues.setText("1 Hours");
		}else {
			NotificationValues.setText(Shared_NotificationName+" Minutes");
		}


		userList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				userList.setSelection(arg2);
				companyname = user_pcompanyname.get(arg2);
				server = user_fName.get(arg2);
				company = user_lName.get(arg2);
				password = user_pName.get(arg2);
				selectedserver = user_fName.get(arg2) + ","
						+ user_lName.get(arg2) + "," + user_pName.get(arg2)
						+ "," + user_pcompanyname.get(arg2);

				Log.d("serve value", "" + selectedserver);
				Toast.makeText(getApplicationContext(),
						user_pcompanyname.get(arg2) + " selected",
						Toast.LENGTH_LONG).show();
				try {
					for (int ctr = 0; ctr <= user_pcompanyname.size(); ctr++) {
						if (arg2 == ctr) {

							userList.getChildAt(ctr).setBackgroundResource(
									R.color.graycolor);
						} else {
							userList.getChildAt(ctr).setBackgroundColor(
									Color.WHITE);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		findViewById(R.id.done).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				loginPrefsEditors.putString("Company Name",companyname);
				loginPrefsEditors.putString("Company Code",server);
				loginPrefsEditors.putString("user Id",company);
				loginPrefsEditors.putString("password",password);
				loginPrefsEditors.apply();
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				SessionData.getInstance().setSelectedserver(selectedserver);
				SessionData.getInstance().setCompanynewname(companyname);
				SessionData.getInstance().setCompanyname(server);
				SessionData.getInstance().setUserlocalname(company);
				SessionData.getInstance().setPassworddname(password);

				startActivity(i);

			}
		});

	}

	@Override
	protected void onResume() {
		displayData();
		super.onResume();
	}

	private void displayData() {
		dataBase = mHelper.getWritableDatabase();
		Cursor mCursor = dataBase.rawQuery("SELECT * FROM "
				+ DbHelper.TABLE_NAME, null);

		userId.clear();
		user_pcompanyname.clear();
		user_fName.clear();
		user_lName.clear();
		user_pName.clear();

		if (mCursor.moveToFirst()) {
			do {
				userId.add(mCursor.getString(mCursor
						.getColumnIndex(DbHelper.KEY_ID)));
				user_pcompanyname.add(mCursor.getString(mCursor
						.getColumnIndex(DbHelper.KEY_LCOMPANYNAME)));
				user_fName.add(mCursor.getString(mCursor
						.getColumnIndex(DbHelper.KEY_FNAME)));
				user_lName.add(mCursor.getString(mCursor
						.getColumnIndex(DbHelper.KEY_LNAME)));
				user_pName.add(mCursor.getString(mCursor
						.getColumnIndex(DbHelper.KEY_LPASS)));

			} while (mCursor.moveToNext());

		}
		if (!(user_fName.isEmpty())) {
			selectedserver = SessionData.getInstance().getSelectedserver();
			Log.d("server", "" + selectedserver);

		}
		DisplayAdapter disadpt = new DisplayAdapter(DisplayActivity.this,
				userId, user_pcompanyname, user_fName, user_lName, user_pName);
		userList.setAdapter(disadpt);
		mCursor.close();
	}

	@Override
	public void onClick(View v) {
		if(debugLog==v){
			if(debugLog.isChecked()){
				SessionData.getInstance().setLogger(1);
				sendMail.setVisibility(View.VISIBLE);

				Log.d("Checked","1");
			}else{
				SessionData.getInstance().setLogger(0);
				sendMail.setVisibility(View.GONE);
				Log.d("Un Checked", "0");
			}
		}else if(btnClearLog==v){
			String filename = "winserve_log.txt";
			File fdelete = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

			if (fdelete.exists()) {
				if (fdelete.delete()) {
					Log.d("Deleted ", "Deleted");

				} else {
					Log.d(" Not Deleted", "Not Deleted");

				}
			}

		}
		else if(layoutNotifications == v){
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.notifications_alert);

			ListView list = (ListView) dialog.findViewById(R.id.list);
			TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

            list.setAdapter(arrayAdapter);

		   list.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				   NotificationValues.setText(notificationList.get(position));
				   notificationPrefsEditors.putString("notificationvalue",notificationListvalues.get(position));
				   notificationPrefsEditors.apply();
				   dialog.cancel();
			   }
		   });





			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			dialog.show();
		}
		else if(sendMail ==v){
			String filename="winserve_log.txt";
			File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
			Uri path = Uri.fromFile(filelocation);
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
			emailIntent .setType("vnd.android.cursor.dir/email");

// the attachment
			emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
			emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Winserve mobile debuglog attachmant :");
			startActivity(Intent.createChooser(emailIntent , "Send email..."));
		}
	}

	public class DisplayAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<String> id;
		private ArrayList<String> companyname;
		private ArrayList<String> firstName;
		private ArrayList<String> lastName;
		private ArrayList<String> password;

		private AlertDialog.Builder build;

		public DisplayAdapter(Context c, ArrayList<String> id,
				ArrayList<String> lcompanyname, ArrayList<String> fname,
				ArrayList<String> lname, ArrayList<String> fpass) {
			this.mContext = c;

			this.id = id;
			this.companyname = lcompanyname;
			this.firstName = fname;
			this.lastName = lname;
			this.password = fpass;

		}

		public int getCount() {
			// TODO Auto-generated method stub
			return id.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("InflateParams")
		public View getView(final int pos, View child, ViewGroup parent) {
			Holder mHolder;
			LayoutInflater layoutInflater;
			if (child == null) {
				layoutInflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				child = layoutInflater.inflate(R.layout.listcell, null);
				mHolder = new Holder();
				mHolder.txt_fName = (TextView) child
						.findViewById(R.id.txt_fName);
				mHolder.txt_lName = (TextView) child
						.findViewById(R.id.txt_lName);
				final View finalChild = child;
				ImageView delete = (ImageView) child.findViewById(R.id.delete);
				ImageView edit = (ImageView) child.findViewById(R.id.edit);
				delete.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						build = new AlertDialog.Builder(DisplayActivity.this);
						build.setTitle("Delete " + user_pcompanyname.get(pos)
								+ " " + user_fName.get(pos));
						build.setMessage("Do you want to delete ?");
						build.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@SuppressLint("ShowToast")
									public void onClick(DialogInterface dialog,
														int which) {

										Toast.makeText(
												getApplicationContext(),
												user_pcompanyname.get(pos)
														+ " "
														+ user_fName.get(pos)
														+ " is deleted.", 3000)
												.show();

										dataBase.delete(
												DbHelper.TABLE_NAME,
												DbHelper.KEY_ID + "="
														+ userId.get(pos), null);
										if ((user_fName.get(pos))
												.equals(selectedserver)) {
											SessionData.getInstance()
													.setSelectedserver(null);
											selectedserver = null;
											sharedPreference.save(context,
													selectedserver);
										}
										displayData();
										dialog.cancel();

									}

								});

						build.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});
						AlertDialog alert = build.create();
						alert.show();

					}
				});
				if (((user_fName.get(pos)) + "," + (user_lName.get(pos)) + ","
						+ (user_pName.get(pos)) + "," + (user_pcompanyname
							.get(pos))).equals(selectedserver)) {
					finalChild.setBackgroundResource(R.color.graycolor);
				}

				else {
					finalChild.setBackgroundColor(Color.WHITE);
				}
				edit.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								AddActivity.class);
						i.putExtra("lcompanyname", user_pcompanyname.get(pos));
						i.putExtra("Fname", user_fName.get(pos));
						i.putExtra("Lname", user_lName.get(pos));
						i.putExtra("Fpass", user_pName.get(pos));
						i.putExtra("ID", userId.get(pos));
						i.putExtra("update", true);
						startActivity(i);
					}
				});

				child.setTag(mHolder);
			} else {
				mHolder = (Holder) child.getTag();
			}
			mHolder.txt_fName.setText(companyname.get(pos));
			mHolder.txt_lName.setText(firstName.get(pos));

			return child;
		}

		public class Holder {
			TextView txt_id;
			TextView txt_fName;
			TextView txt_lName;
		}

	}
}
