package com.tristar.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tristar.db.DataBaseHelper;
import com.tristar.db.SyncronizeClass;
import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.DiligencePhrase;
import com.tristar.object.MannerOfService;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.object.ReturnAppOptionsObject;
import com.tristar.object.ReturnMessagesObjects;
import com.tristar.object.ReturnStatusListObect;
import com.tristar.object.ServerSubmittedGPSUpdateObject;
import com.tristar.object.SubmitDiligence;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.ProgressBar;
import com.tristar.utils.SessionData;
import com.tristar.utils.SimpleScannerActivity;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

@SuppressWarnings("ALL")
public class ListCategory extends Activity implements OnClickListener {

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final int ZBAR_SCANNER_REQUEST = 0;
    public static Object selectedAddressServer;
    public static Object selectedAddres;
    TextView txt_logout, txt_sync;
    Button BtnScanJob;
    ActionBar actionBar;
    RelativeLayout bottom_checkbox;

    Context context = null;
    String message;
    long startTime;
    LinearLayout layout_Notifications;
    TextView txt_notifications;
    CheckBox Checkbox_diligence;
    String UserName, Password;
    ArrayList<String> ArrayPodWorkOrder;
    ArrayList<ProcessAddressForServer> processOrderListArray;
    ArrayList<CourtAddressForServer> courtServiceListArray;
    ArrayList<AddressForServer> pickupServiceListArray;
    ArrayList<AddressForServer> deliveryServiceListArray;
    ArrayList<Object> serviceAddressAll;
    ArrayList<CourtAddressForServer> courtAddressList;
    ArrayList<CourtAddressForServer> courtAddressList1;
    ArrayList<ProcessAddressForServer> insertprocess;
    ArrayList<AddressForServer> PickupAddresList = new ArrayList<>();
    ArrayList<AddressForServer> DeliveryAddresList = new ArrayList<>();
    String Str_ScanedWorkOrder;
    int ProcessOrderID;
    String sessionId;

    ArrayList<CourtAddressForServer> courtAddressForServers = new ArrayList<>();
    ArrayList<AddressForServer> getPickupAddresList = new ArrayList<>();
    ArrayList<AddressForServer> getDeliveryAddresList = new ArrayList<>();
    ArrayList<ProcessAddressForServer> newAddress = new ArrayList<>();
    int processOrderCount = 0;
    int courtJobs_count = 0;
    String mProcessJobs = "Process Jobs";
    String mDelivery_CourtJobs = "Delivery / Court Jobs";

    String items[] = {"Process Jobs", "Delivery / Court Jobs", "Jobs in Queue (0)", "Route Tracker", "Scan Jobs"};
    String itemss[] = {"Process Jobs", "Delivery / Court Jobs", "Jobs in Queue (0)", "Scan Jobs"};
    String itemsss[] = {"Process Jobs", "Delivery / Court Jobs", "Jobs in Queue (0)", "Scan Jobs"};
    ReturnAppOptionsObject returnAppOptions;
    ArrayList<ReturnMessagesObjects> returnMessagesObjectses;
    Object item;
    boolean flag = false;
    boolean flagpod = false;
    int jobsInQueue = 0;
    DataBaseHelper database;
    TextView txtNotificationItems;
    String str_notifications;
    String str_notificationHistory;
    SharedPreferences RD_sharedPreference;
    SharedPreferences.Editor RD_sharedPreference_Editor;
    Handler handler;
    private ListView mylist;
    private Class<?> mClss;
    private Timer timer = new Timer();
    private ArrayAdapter<String> itemadapter;
    private ArrayAdapter<String> itemadapter_1;
    private ArrayAdapter<String> itemadapter_2;


    @SuppressLint({"CommitPrefEdits", "LongLogTag"})
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_category);

        context = this;
        database = DataBaseHelper.getInstance(ListCategory.this);
        init_listCount();

        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)) {
            String[] perms = {"android.permission.CAMERA",
                    "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.CALL_PHONE",
                    "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);
        }

        DisplayActivity.loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        DisplayActivity.loginPrefsEditors = DisplayActivity.loginPreferencess.edit();
        NotificationActivity.notificationHistory = getSharedPreferences("notificationhistory", MODE_PRIVATE);
        NotificationActivity.notificationHistoryPrefsEditors = NotificationActivity.notificationHistory.edit();
        str_notificationHistory = NotificationActivity.notificationHistory.getString("message", "");

        Log.d("Notification History", "" + str_notificationHistory);

        UserName = DisplayActivity.loginPreferencess.getString("user Id", "");
        Password = DisplayActivity.loginPreferencess.getString("password", "");
        SessionData.getInstance().setUsername(UserName);
        SessionData.getInstance().setPassword(Password);

        layout_Notifications = (LinearLayout) findViewById(R.id.notifications);
        txt_notifications = (TextView) findViewById(R.id.txt_notifications);
        txtNotificationItems = (TextView) findViewById(R.id.txt_notificationsitems);
        Checkbox_diligence = (CheckBox) findViewById(R.id.Checkbox_diligence);
        bottom_checkbox = (RelativeLayout) findViewById(R.id.bottom_checkbox);
        DisplayActivity.notificationPreferencess = getSharedPreferences("notification", MODE_PRIVATE);
        DisplayActivity.notificationPrefsEditors = DisplayActivity.notificationPreferencess.edit();

        str_notifications = DisplayActivity.notificationPreferencess.getString("notificationvalue", "");

        if (str_notifications.length() == 0) {
            str_notifications = "5";
        }
        Log.d("Notifications value", "" + str_notifications);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Log.d("Notifications", "" + "NOtifi");
                new Notifications().execute();

            }
        }, 0, Integer.parseInt(str_notifications) * 60 * 1000);

        layout_Notifications.setOnClickListener(this);
        txt_notifications.setOnClickListener(this);

        Log.d("User Name from shared Preference", "" + UserName);
        Log.d("Password from shared Preference", "" + Password);
        new getIsAudiorecordingon().execute();
        new Mytask().execute();
        initialSync();
        initialize();


        insertprocess = new ArrayList<ProcessAddressForServer>();
        processOrderListArray = new ArrayList<ProcessAddressForServer>();
        deliveryServiceListArray = new ArrayList<AddressForServer>();
        courtServiceListArray = new ArrayList<CourtAddressForServer>();
        pickupServiceListArray = new ArrayList<AddressForServer>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 1) {
                SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
                String scan_value = bundle.getString("value");
                Str_ScanedWorkOrder = scan_value;


//				String scan = Str_ScanedWorkOrder.replace(".",",");
///...................
//				String scanAtrray[] = Str_ScanedWorkOrder.trim().split("\\.",5);
//
//				System.out.println("Scan Length: " + scanAtrray.length);
//				System.out.println("Scan value: " + scanAtrray[0]);
//
//				String dta = scanAtrray[0];
//
//				System.out.println("Scan "+dta);
//......................
//				String scandot[] = scan_value.split(".",5);
//				System.out.println("Scan Length dot: " + scandot.length);
//				System.out.println("Scan value dot: " + scandot[0]);
//				String first_element = String.valueOf(scan[0]);
//				Str_ScanedWorkOrder = first_element;

                boolean Check_Scanned_Value_Existence = false;
                if (Str_ScanedWorkOrder != null && Str_ScanedWorkOrder.length() != 0) {

                    String scanAtrray[] = Str_ScanedWorkOrder.trim().split("\\.", 5);
                    Str_ScanedWorkOrder = scanAtrray[0];

//					System.out.println("Scan Length: " + scanAtrray.length);
//					System.out.println("Scan value: " + scanAtrray[0]);

                    processOrderListArray = SessionData.getInstance().getArrayprocessOrderList();
                    int m = 0;
                    if (processOrderListArray != null && processOrderListArray.size() != 0) {
                        for (int i = 0; i < processOrderListArray.size(); i++) {
                            ProcessAddressForServer process = processOrderListArray.get(i);
                            if (process.getWorkorder().length() > 2) {


                                for (int j = 0; j < SessionData.getInstance().getScanned_Workorder().size(); j++) {

                                    if (SessionData.getInstance().getScanned_Workorder().get(j).equals(Str_ScanedWorkOrder)) {
                                        m = 1;
                                        break;
                                    }

                                }
                                if (m == 1) {
                                    break;
                                }
                                if (Str_ScanedWorkOrder.equals(process.getWorkorder())) {
                                    ProcessOrderID = process.getProcessOrderID();
                                    SessionData.getInstance().getScanned_Workorder().add(Str_ScanedWorkOrder);
                                    SessionData.getInstance().getScanned_Item_Process_ID().add(ProcessOrderID);
                                    Check_Scanned_Value_Existence = true;
                                    break;
                                } else {
                                    Check_Scanned_Value_Existence = false;
                                }
                                //	processOrderlist.add(process);
                            }
                        }
                    }
                    if (!Check_Scanned_Value_Existence) {
                        if (m == 0) {
                            Intent Scanner_Activity = new Intent(ListCategory.this,
                                    SimpleScannerActivity.class);
                            Scanner_Activity.putExtra("Message",
                                    "Scanned Workorder is not available");
                            startActivity(Scanner_Activity);
                        } else {
                            Intent Scanner_Activity = new Intent(ListCategory.this,
                                    SimpleScannerActivity.class);
                            Scanner_Activity.putExtra("Message",
                                    "Workorder is already scanned");
                            startActivity(Scanner_Activity);
                        }

                    } else {
                        Intent Scanner_Activity = new Intent(ListCategory.this,
                                SimpleScannerActivity.class);
                        Scanner_Activity.putExtra("Message",
                                "Scanned Workorder added Successfuly");
                        startActivity(Scanner_Activity);
                    }
                }

            } else {

                if (SessionData.getInstance().getScanner_activity_result() == 1) {
                    String scan_value = bundle.getString("value");

                    Str_ScanedWorkOrder = scan_value;

                    if (Str_ScanedWorkOrder == null) {
                        return;
                    }

                    Log.d("Str_ScanedWorkOrder", "" + Str_ScanedWorkOrder);

                    processOrderListArray = SessionData.getInstance().getArrayprocessOrderList();
//			Log.d("processOrderListArray1",""+processOrderListArray.size());

                    deliveryServiceListArray = SessionData.getInstance().getArraydeliveryServiceList();
                    courtServiceListArray = SessionData.getInstance().getArraycourtServiceList();
                    pickupServiceListArray = SessionData.getInstance().getArraypickupServiceList();

                    //	Log.d("deliveryServiceListArray",""+deliveryServiceListArray.size());
                    //	Log.d("courtServiceListArray",""+courtServiceListArray.size());
                    //	Log.d("pickupServiceListArray",""+pickupServiceListArray.size());

                    for (int i = 0; i < processOrderListArray.size(); i++) {

                        ProcessAddressForServer process = processOrderListArray.get(i);
                        if (process.getWorkorder().length() > 2) {

                            if (Str_ScanedWorkOrder != null) {
                                if (Str_ScanedWorkOrder.contains(process.getWorkorder())) {
                                    ProcessOrderID = process.getProcessOrderID();
                                    flag = true;
                                    break;
                                }
                            }

                            //	processOrderlist.add(process);
                        }
                    }

                    if (flag == true) {
                        finish();
                        Intent processdetail = new Intent(ListCategory.this,
                                ProcessOrderDetail.class);
                        processdetail.putExtra("processOrderID",
                                ProcessOrderID);
                        SessionData.getInstance().setScanner_activity_result(2);
                        startActivity(processdetail);
                    } else {

                        if (courtServiceListArray.size() > 0) {
                            serviceAddressAll.add(("Court Services"));
                            serviceAddressAll.addAll(courtServiceListArray);
                            ArrayPodWorkOrder.add("Court Services");

                            for (int i = 0; i < courtServiceListArray.size(); i++) {
                                ArrayPodWorkOrder.add(courtServiceListArray.get(i).getWorkorder());
                            }
                        }

                        if (pickupServiceListArray.size() > 0) {
                            serviceAddressAll.add(("Pickup Services"));
                            serviceAddressAll.addAll(pickupServiceListArray);

                            ArrayPodWorkOrder.add("Pickup Services");

                            for (int i = 0; i < pickupServiceListArray.size(); i++) {
                                ArrayPodWorkOrder.add(pickupServiceListArray.get(i).getWorkorder());
                            }

                        }
                        if (deliveryServiceListArray.size() > 0) {
                            serviceAddressAll.add(("Delivery Services"));
                            serviceAddressAll.addAll(deliveryServiceListArray);

                            ArrayPodWorkOrder.add("Delivery Services");

                            for (int i = 0; i < deliveryServiceListArray.size(); i++) {
                                ArrayPodWorkOrder.add(deliveryServiceListArray.get(i).getWorkorder());
                            }

                        }
                        for (int i = 0; i < serviceAddressAll.size(); i++) {
                            if (Str_ScanedWorkOrder.contains(ArrayPodWorkOrder.get(i))) {
                                item = serviceAddressAll.get(i);
                                flagpod = true;
                                break;
                            }
                        }
                        if (flagpod == true) {
                            navigateOnSelectItem(item);
                        } else {
                            Toast.makeText(this, "Scanned Workorder is not available Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        }

    }

    private void init_listCount() {

        processOrderListArray = new ArrayList<ProcessAddressForServer>();
        courtServiceListArray = new ArrayList<>();
        pickupServiceListArray = new ArrayList<>();
        deliveryServiceListArray = new ArrayList<>();

        try {
            processOrderListArray = database.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();


            courtServiceListArray = database.getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
            pickupServiceListArray = database.getPickupOrderValuesFromTable();
            deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();

            processOrderCount = processOrderListArray.size();

            courtJobs_count = courtServiceListArray.size() + pickupServiceListArray.size() + deliveryServiceListArray.size();


        } catch (Exception e) {
            e.printStackTrace();
        }


        mProcessJobs = "Process Jobs" + " (" + processOrderCount + ") ";
        mDelivery_CourtJobs = "Delivery / Court Jobs" + " (" + courtJobs_count + ") ";

        items[0] = mProcessJobs;
        items[1] = mDelivery_CourtJobs;

        itemss[0] = mProcessJobs;
        itemss[1] = mDelivery_CourtJobs;

        itemsss[0] = mProcessJobs;
        itemsss[1] = mDelivery_CourtJobs;

    }

    private void initialize() {
        txt_logout = (TextView) findViewById(R.id.txt_logout);
        txt_sync = (TextView) findViewById(R.id.txt_sync);
        BtnScanJob = (Button) findViewById(R.id.btn_scan_job);

        courtServiceListArray = new ArrayList<CourtAddressForServer>();
        pickupServiceListArray = new ArrayList<AddressForServer>();
        deliveryServiceListArray = new ArrayList<AddressForServer>();
        serviceAddressAll = new ArrayList<Object>();

        ArrayPodWorkOrder = new ArrayList<>();

        BtnScanJob.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        txt_sync.setOnClickListener(this);
        Checkbox_diligence.setOnClickListener(this);

    }

    private void loadLoginView() {
        Intent category = new Intent(ListCategory.this, MainActivity.class);
        startActivity(category);
    }

    public void initialSync() {

        if (!database.processOrderTableHasValue()) {

            SessionData.getInstance().setSynchandler(0);
            if (MainActivity.sharedPreferences.getBoolean("logOut", true)) {
                MainActivity.setLogoutStatus(MainActivity.sharedPreferences,
                        true);
                database.deleteLoginTable();
                loadLoginView();
            } else {

                startTime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date resultdate = new Date(startTime);
                System.out.println(sdf.format(resultdate));
                Log.d("Method_begin time", "" + sdf.format(resultdate));

                new AsyncTask<Void, Void, Void>() {

                    String errorString = null;

                    protected void onPreExecute() {
                        ProgressBar.showCommonProgressDialog(ListCategory.this,
                                "Updating Local Database...");
                    }

                    ;

                    @Override
                    protected Void doInBackground(Void... params) {
                        errorString = initialSycnWebServiceCall();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        ProgressBar.dismiss();
                        if (errorString != null) {
                            database.deleteCategoryReleatedTableInDB();
                            new CustomAlertDialog(ListCategory.this,
                                    "Invoking initial sync failed with error code : "
                                            + errorString).show();
                        }
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Method_end time: " + elapsedTime + " milliseconds.");
                        long millis = elapsedTime;  // obtained from StopWatch
                        long minutes = (millis / 1000) / 60;
                        int seconds = (int) ((millis / 1000) % 60);
                        System.out.println("Sync_Time :" + seconds + " seconds.");
                        getJobsInQueue();
                        init_listCount();

                        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) mylist.getAdapter();
                        arrayAdapter.notifyDataSetChanged();


                        super.onPostExecute(result);
                    }

                    ;

                }.execute();
            }
        } else {
            //         initialSycnWebServiceCall();
            getJobsInQueue();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (SessionData.getInstance().getScanner_activity_result() == 1) {
                String scan_value = bundle.getString("value");
                Str_ScanedWorkOrder = scan_value;

                Log.d("Str_ScanedWorkOrder", "" + Str_ScanedWorkOrder);

                processOrderListArray = SessionData.getInstance().getArrayprocessOrderList();
                //	Log.d("processOrderListArray1",""+processOrderListArray.size());

                deliveryServiceListArray = SessionData.getInstance().getArraydeliveryServiceList();
                courtServiceListArray = SessionData.getInstance().getArraycourtServiceList();
                pickupServiceListArray = SessionData.getInstance().getArraypickupServiceList();

//			Log.d("deliveryServiceListArray",""+deliveryServiceListArray.size());
//			Log.d("courtServiceListArray",""+courtServiceListArray.size());
//			Log.d("pickupServiceListArray",""+pickupServiceListArray.size());

                for (int i = 0; i < processOrderListArray.size(); i++) {

                    ProcessAddressForServer process = processOrderListArray.get(i);
                    if (process.getWorkorder().length() > 2) {

                        if (Str_ScanedWorkOrder.equals(process.getWorkorder())) {

                            ProcessOrderID = process.getProcessOrderID();
                            flag = true;
                            break;
                        }

                        //	processOrderlist.add(process);
                    }
                }

                if (flag == true) {
                    finish();
                    Intent processdetail = new Intent(ListCategory.this,
                            ProcessOrderDetail.class);
                    processdetail.putExtra("processOrderID",
                            ProcessOrderID);
                    startActivity(processdetail);
                } else {


                    if (courtServiceListArray.size() > 0) {
                        serviceAddressAll.add(("Court Services"));
                        serviceAddressAll.addAll(courtServiceListArray);
                        ArrayPodWorkOrder.add("Court Services");

                        for (int i = 0; i < courtServiceListArray.size(); i++) {
                            ArrayPodWorkOrder.add(courtServiceListArray.get(i).getWorkorder());
                        }

                    }

                    if (pickupServiceListArray.size() > 0) {
                        serviceAddressAll.add(("Pickup Services"));
                        serviceAddressAll.addAll(pickupServiceListArray);

                        ArrayPodWorkOrder.add("Pickup Services");

                        for (int i = 0; i < pickupServiceListArray.size(); i++) {
                            ArrayPodWorkOrder.add(pickupServiceListArray.get(i).getWorkorder());
                        }

                    }
                    if (deliveryServiceListArray.size() > 0) {
                        serviceAddressAll.add(("Delivery Services"));
                        serviceAddressAll.addAll(deliveryServiceListArray);

                        ArrayPodWorkOrder.add("Delivery Services");

                        for (int i = 0; i < deliveryServiceListArray.size(); i++) {
                            ArrayPodWorkOrder.add(deliveryServiceListArray.get(i).getWorkorder());
                        }

                    }


                    for (int i = 0; i < serviceAddressAll.size(); i++) {

                        if (Str_ScanedWorkOrder.equals(ArrayPodWorkOrder.get(i))) {
                            item = serviceAddressAll.get(i);
                            flagpod = true;
                            break;

                        }

                    }

                    if (flagpod == true) {
                        navigateOnSelectItem(item);
                    } else {
                        Toast.makeText(this, "Scanned Workorder is not available", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }

    }

    private void getJobsInQueue() {
        ArrayList<Object> jobList = new ArrayList<Object>();
        {
            ArrayList<ProcessAddressForServer> list = database
                    .getFinalStatusToSubmit();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        {
            ArrayList<CourtAddressForServer> list = database
                    .getSubmitCourtOrder();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        {
            ArrayList<SubmitDiligence> list = database
                    .getSubmitDiligencesValuesFromDBForUploadingToServer();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        {
            ArrayList<SubmitStatusList> list = database.getSubmitStatusValuesFromDBToDisplay();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        {
            ArrayList<SubmitStatusList> list = database.getSubmitPickupStatusValuesFromDBToDisplay();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        {
            ArrayList<SubmitStatusList> list = database.getSubmitDeliveryStatusValuesFromDBToDisplay();
            if (list != null && list.size() > 0) {
                jobList.addAll(list);
            }
        }
        ArrayList<AddressForServer> list = database.getSubmitPickupOrder();
        if (list != null && list.size() > 0) {
            jobList.addAll(list);
        }
        list = database.getSubmitDeliveryOrder();
        if (list != null && list.size() > 0) {
            jobList.addAll(list);
        }
        jobsInQueue = jobList.size();
        items[2] = "Jobs in Queue (" + jobsInQueue + ")";
        itemss[2] = "Jobs in Queue (" + jobsInQueue + ")";
        itemsss[2] = "Jobs in Queue (" + jobsInQueue + ")";
    }

    public void Start_Timer(String message) {
        long startTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date resultdate = new Date(startTime);
        System.out.println(sdf.format(resultdate));
        Log.d("Timer_start time:" + message, "" + sdf.format(resultdate));
    }

    public void End_Timer(String message) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Timer_execution time: " + elapsedTime + " milliseconds." + message);
        long millis = elapsedTime;  // obtained from StopWatch
        long minutes = (millis / 1000) / 60;
        int seconds = (int) ((millis / 1000) % 60);
        System.out.println("Timer_end time :" + seconds + " seconds." + message);
    }

    @SuppressLint("LongLogTag")
    private String initialSycnWebServiceCall() {
        String errorString = null;

        try {
            sessionId = SyncronizeClass.instance().sessionIDForFinalSync;

            Log.d("Session ID form login111", "" + sessionId);
            if (sessionId == null || sessionId.equals(""))
                return null;

//			long startTime = System.currentTimeMillis();
//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//			Date resultdate = new Date(startTime);
//			System.out.println(sdf.format(resultdate));addresses:
//			Log.d("Req_start time", ""+sdf.format(resultdate));
            message = "ReturnProcessOpenAddresses";
            Start_Timer(message);


            //	ArrayList<ProcessAddressForServer> processOpenAddress = new ArrayList<>();

            ArrayList<ProcessAddressForServer> processOpenAddress = WebServiceConsumer
                    .getInstance().getProcessAddressForServer(sessionId);

//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Req_execution time: " + elapsedTime + " milliseconds.");
//			long millis = elapsedTime;  // obtained from StopWatch
//			long minutes = (millis / 1000)  / 60;
//			int seconds = (int) ((millis / 1000) % 60);
//			System.out.println("Req_ReturnPickupOpenAddresses :" + seconds + " seconds.");

            if (processOpenAddress != null) {
                Log.d("Size_processOpenAddress", "" + processOpenAddress.size());
            }

            ArrayList<ProcessAddressForServer> courtStateCodeArray = insertProcessOpenAddress(processOpenAddress);

            End_Timer(message);

            ArrayList<MannerOfService> mannerOfServiceList;
            ArrayList<DiligenceForProcess> diligenceList;

            if (processOpenAddress != null && processOpenAddress.size() > 0) {

                if (courtStateCodeArray != null
                        && courtStateCodeArray.size() > 0) {
                    message = "ReturnMannerOfService";
                    Start_Timer(message);

                    for (ProcessAddressForServer processOrder : courtStateCodeArray) {

                        mannerOfServiceList = WebServiceConsumer
                                .getInstance().getMannerOfService(sessionId,
                                        processOrder.getCourtStateCode());

                        if (mannerOfServiceList.size() > 0) {
                            for (MannerOfService mannerOfService : mannerOfServiceList)
                                database.insertMannerOfServicefromServer(mannerOfService);
                        }

                        if (mannerOfServiceList != null) {
                            Log.d("Size_mannerOfServiceList", "" + mannerOfServiceList.size());
                        }
                    }
                    End_Timer(message);
                } else {
                }


//				processOpenAddress.clear();
//				processOpenAddress = database
//						.getProcessOrderValuesFromDBTogetValuesOfReturnDiligenceFromWebservice();
//				if (processOpenAddress != null && processOpenAddress.size() > 0) {
//					message = "ReturnDiligence";
//					Start_Timer(message);
//					for (ProcessAddressForServer processOrder : processOpenAddress) {
//
//						diligenceList = WebServiceConsumer
//								.getInstance().getDiligenceForProcess(
//										sessionId, processOrder.getWorkorder(),
//										processOrder.getAddressLineItem());
//
//
//						if (diligenceList != null && diligenceList.size() > 0) {
//							for (DiligenceForProcess diligence : diligenceList) {
//								database.insertReturnDiligencefromServer(diligence);
//							}
//						}
//
//						Log.d("Size_diligenceList",""+diligenceList.size());
//					}
//					End_Timer(message);
//				}

                message = "ReturnDiligencePhrases";
                Start_Timer(message);
                ArrayList<DiligencePhrase> diligencePhraseList = WebServiceConsumer
                        .getInstance().getDiligencePhrase(sessionId);
                End_Timer(message);

                if (diligencePhraseList != null) {
                    Log.d("Size_diligencePhraseList", "" + diligencePhraseList.size());
                }

                if (diligencePhraseList != null
                        && diligencePhraseList.size() > 0) {
                    for (DiligencePhrase diligencePhrase : diligencePhraseList) {
                        database.insertReturnDiligencePhrasesfromServer(diligencePhrase);
                    }
                    diligencePhraseList.clear();
                }

                message = "ReturnStatusList";
                Start_Timer(message);
                ArrayList<ReturnStatusListObect> statusListObect = WebServiceConsumer
                        .getInstance().getStatusList(sessionId);
                End_Timer(message);

                if (statusListObect != null) {
                    Log.d("Size_statusListObect", "" + statusListObect.size());
                }

                if (statusListObect != null
                        && statusListObect.size() > 0) {
                    for (ReturnStatusListObect statusList : statusListObect) {
                        database.insertReturnStatusListfromServer(statusList);

                    }
                    statusListObect.clear();
                }

            }

            {
                message = "ReturnCourtOpenAddresses";
                Start_Timer(message);
                //new AsynCourtOrder().execute();
                courtAddressList = WebServiceConsumer
                        .getInstance().getCourtAddressForServer(sessionId);
                End_Timer(message);

                if (courtAddressList != null && courtAddressList.size() > 0) {


                    courtServiceListArray = database.getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();

                    for (int i = 0; i < courtAddressList.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < courtServiceListArray.size(); j++) {

                            if (courtServiceListArray.get(j).getWorkorder().equals(courtAddressList.get(i).getWorkorder())) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            courtServiceListArray.add(courtAddressList.get(i));
                            courtAddressForServers.add(courtAddressList.get(i));
                        }

                    }

                    for (int i = 0; i < courtServiceListArray.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < courtAddressList.size(); j++) {

                            if (courtAddressList.get(j).getWorkorder().equals(courtServiceListArray.get(i).getWorkorder())) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            database.DeleteCourtPODAfteruploadTableBy(courtServiceListArray.get(i).getWorkorder());
                        }

                    }


                    for (CourtAddressForServer courtAddress : courtAddressForServers) {

                        Log.d("court lat", "" + courtAddress.getLatitude());
                        courtAddress.getLatitude();
                        database.insertCourtOpenAddressFromServer(courtAddress);
                    }
                    //courtAddressList = courtAddressList1;
                    new InsertCourtOpenAddress().execute();
                    //	courtAddressList.clear();
                } else {
                    database.deleteallcourt();
                }

                if (courtAddressList != null) {
                    Log.d("Size_courtAddressList", "" + courtAddressList.size());
                }

                message = "ReturnPickupOpenAddresses";
                Start_Timer(message);
                ArrayList<AddressForServer> addressList = WebServiceConsumer
                        .getInstance().getPickupOpenAddress(sessionId);
                End_Timer(message);

                if (addressList != null) {
                    Log.d("Size_addressList", "" + addressList.size());
                }

                if (addressList != null && addressList.size() > 0) {


                    pickupServiceListArray = database.getPickupOrderValuesFromTable();

                    for (int i = 0; i < addressList.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < pickupServiceListArray.size(); j++) {

                            if (pickupServiceListArray.get(j).getAddressLineItem() == addressList.get(i).getAddressLineItem()) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            getPickupAddresList.add(addressList.get(i));
                        }

                    }


                    for (int i = 0; i < pickupServiceListArray.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < addressList.size(); j++) {

                            if (addressList.get(j).getAddressLineItem() == pickupServiceListArray.get(i).getAddressLineItem()) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            database.DeletePickupPODAfteruploadTableBy(pickupServiceListArray.get(i).getWorkorder());
                        }

                    }


                    for (AddressForServer address : getPickupAddresList) {
                        database.insertPickupOpenAddressFromServer(address);
                    }
                    PickupAddresList = getPickupAddresList;
                    new InsertPickupOpenAddress().execute();
//					addressList.clear();
                } else {

                    database.deleteallpickup();

                }
                message = "ReturnDeliveryOpenAddress";
                Start_Timer(message);
                addressList = WebServiceConsumer.getInstance()
                        .getDeliveryOpenAddress(sessionId);
                End_Timer(message);

                if (addressList != null) {
                    Log.d("Size_DeliveryOpenAddress", "" + addressList.size());
                }

                if (addressList != null && addressList.size() > 0) {


                    deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();

                    for (int i = 0; i < addressList.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < deliveryServiceListArray.size(); j++) {

                            if (deliveryServiceListArray.get(j).getAddressLineItem() == addressList.get(i).getAddressLineItem()) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            getDeliveryAddresList.add(addressList.get(i));
                        }

                    }

                    for (int i = 0; i < deliveryServiceListArray.size(); i++) {

                        int m = 0;
                        for (int j = 0; j < addressList.size(); j++) {

                            if (addressList.get(j).getAddressLineItem() == deliveryServiceListArray.get(i).getAddressLineItem()) {

                                m = 1;
                                break;
                            }

                        }
                        if (m == 0) {
                            database.DeleteDeliveryPODAfteruploadTableBy(deliveryServiceListArray.get(i).getWorkorder());
                        }

                    }


                    for (AddressForServer address : getDeliveryAddresList) {
                        database.insertDeliveryOpenAddressFromServer(address);
                    }
                    DeliveryAddresList = getDeliveryAddresList;
                    new InsertDeliveryOpenAddress().execute();

                    //addressList.clear();
                } else {
                    database.deletealldelivery();
                }
            }

            try {
                //Start_Timer();
                WebServiceConsumer.getInstance().signOff(sessionId);
                //End_Timer();
            } catch (Exception e) {
            }

        } catch (Exception e) {
            errorString = "Could not authenticate and synchronize, you will be storing data locally. Please sync once you have connectivity.";
            e.printStackTrace();
        }
        return errorString;
    }

    private ArrayList<ProcessAddressForServer> insertProcessOpenAddress(
            ArrayList<ProcessAddressForServer> processOpenAddress)
            throws Exception {
        ArrayList<ProcessAddressForServer> courtStateCodeArray = new ArrayList<ProcessAddressForServer>();
        if (processOpenAddress != null && processOpenAddress.size() > 0) {


            insertprocess = database.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();


            Log.d("Process Jobs count Before Sync", "" + insertprocess.size());

            for (int i = 0; i < processOpenAddress.size(); i++) {


                int m = 0;

                for (int j = 0; j < insertprocess.size(); j++) {

                    if (insertprocess.get(j).getAddressLineItem() == processOpenAddress.get(i).getAddressLineItem()) {

                        m = 1;
                        break;
                    }

                }
                if (m == 0) {
                    insertprocess.add(processOpenAddress.get(i));
                    newAddress.add(processOpenAddress.get(i));
                }

            }


            insertprocess = database.getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

            Log.d("Process Jobs count after Sync", "" + insertprocess.size());

            for (int i = 0; i < insertprocess.size(); i++) {


                int m = 0;

                for (int j = 0; j < processOpenAddress.size(); j++) {

                    if (processOpenAddress.get(j).getAddressLineItem() == insertprocess.get(i).getAddressLineItem()) {

                        m = 1;
                        break;
                    }

                }
                if (m == 0) {
                    database.DeleteFinalStatusAfteruploadTableBy(insertprocess.get(i).getAddressLineItem());
                }

            }


            //insertprocess = processOpenAddress;
            new processorderinsert().execute();

            Log.d("insertprocess", "" + newAddress.size());

            for (int i = 0; i < newAddress.size(); i++) {
//				if (insertprocess.get(i).getLatitude()!="0.0"
//						&& insertprocess.get(i).getLongitude()!="0.0") {
                database.insertProcessOrdernew(newAddress.get(i));
//				}

            }

            ArrayList<String> existingNames = new ArrayList<String>();

            ProcessAddressForServer processOrder = null;

            try {
                for (int index = 0; index < processOpenAddress.size(); index++) {

                    Log.d("Index", "" + index);

                    processOrder = processOpenAddress
                            .get(index);

                    if (!existingNames.contains(processOrder
                            .getCourtStateCode())) {
                        existingNames.add(processOrder.getCourtStateCode());
                        courtStateCodeArray.add(processOrder);

                    }

                }

//				database.insertProcessOpenAddressFromServer(processOrder);

            } catch (Exception e) {
                throw e;
            }
        } else {
        }
        return courtStateCodeArray;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText("Are you sure you want to logout?");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_no);

        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setLogoutStatus(MainActivity.sharedPreferences, true);
                dialog.dismiss();
                finish();
                Intent court = new Intent(ListCategory.this,
                        MainActivity.class);
                startActivity(court);

            }
        });
        dialogButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

	/*private class AsynCourtOrder extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
		}

		;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				courtAddressList = WebServiceConsumer
						.getInstance().getCourtAddressForServer(sessionId);

			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();
				courtAddressList = null;
			} catch (Exception e) {
				e.printStackTrace();
				courtAddressList = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (courtAddressList != null && courtAddressList.size() > 0) {
				for (CourtAddressForServer courtAddress : courtAddressList) {
					try {
						database.insertCourtOpenAddressFromServer(courtAddress);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				courtAddressList.clear();
			}

		}
	}*/

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == txt_logout) {


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertdialog);

            TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
            text.setText("Are you sure you want to logout?");

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_no);

            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.setLogoutStatus(MainActivity.sharedPreferences, true);
                    dialog.dismiss();
                    finish();
                    Intent court = new Intent(ListCategory.this,
                            MainActivity.class);
                    startActivity(court);

                }
            });
            dialogButton1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        } else if (v == txt_sync) {

            SessionData.getInstance().setSynchandler(1);

            new CustomAlertDialog(this, getResources().getString(
                    R.string.alert_sync), CustomAlertDialog.SYNC).show();
        } else if (v == layout_Notifications) {
            if (returnMessagesObjectses != null) {
                if (returnMessagesObjectses.size() == 0) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertbox);

                    TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                    text.setText("No Notifications to show ");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

                    dialogButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                } else {
                    Log.d("Notifications", "" + returnMessagesObjectses.size());
                    SessionData.getInstance().setReturnMessagesObjectses(returnMessagesObjectses);
                    Intent HistoryList = new Intent(ListCategory.this, NotificationActivity.class);
                    startActivity(HistoryList);
                }


            }
        } else if (v == txt_notifications) {
            if (returnMessagesObjectses != null) {
                if (returnMessagesObjectses.size() == 0) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertbox);

                    TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                    text.setText("No Notifications to show ");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

                    dialogButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                } else {
                    Log.d("Notifications", "" + returnMessagesObjectses.size());
                    SessionData.getInstance().setReturnMessagesObjectses(returnMessagesObjectses);
                    Intent HistoryList = new Intent(ListCategory.this, NotificationActivity.class);
                    startActivity(HistoryList);
                }


            }

        } else if (v == BtnScanJob) {

            try {
                processOrderListArray = database
                        .getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

                SessionData.getInstance().setArrayprocessOrderList(processOrderListArray);

                courtServiceListArray = database
                        .getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
                pickupServiceListArray = database.getPickupOrderValuesFromTable();
                deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();
                SessionData.getInstance().setSelectedItem(1);

                SessionData.getInstance().setArraydeliveryServiceList(deliveryServiceListArray);
                SessionData.getInstance().setArraycourtServiceList(courtServiceListArray);
                SessionData.getInstance().setArraypickupServiceList(pickupServiceListArray);

            } catch (Exception e) {
                processOrderListArray = new ArrayList<ProcessAddressForServer>();
                e.printStackTrace();
            }
            if (isCameraAvailable()) {

                SessionData.getInstance().setScanner_activity(1);
                SessionData.getInstance().setScanner_activity_result(0);

                launchActivity(SimpleScannerActivity.class);
				/*Intent intent = new Intent(getApplicationContext(),
						ZBarScannerActivity.class);

				startActivityForResult(intent, ZBAR_SCANNER_REQUEST);*/
            } else {
                Toast.makeText(getApplicationContext(),
                        "Rear Facing Camera Unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (v == Checkbox_diligence) {
            if (Checkbox_diligence.isChecked()) {
                SessionData.getInstance().setValidate_record_Deligence_CheckBox(true);
            } else {
                SessionData.getInstance().setValidate_record_Deligence_CheckBox(false);
            }
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void navigateOnSelectItem(Object item) {
        if (!(item instanceof String)) {
            selectedAddressServer = item;
            if (item instanceof CourtAddressForServer) {
                finish();
                Intent courtdetail = new Intent(ListCategory.this,
                        CourtServiceDetail.class);
                startActivity(courtdetail);
            } else if (item instanceof AddressForServer) {
                finish();
                Intent deliveryPickup = new Intent(ListCategory.this,
                        DeliveryServiceDetail.class);
                startActivity(deliveryPickup);
            }
        }
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    private class processorderinsert extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {


                //database.insertProcessOpenAddressFromServernew(insertprocess);

				/*ListCategory.this.runOnUiThread(new Runnable() {
													@Override
													public void run() {


													}
												});*/


                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjects = new ArrayList<>();

                        for (int i = 0; i < newAddress.size(); i++) {
                            if (newAddress.get(i).getLatitude().equals("0.0")
                                    && newAddress.get(i).getLongitude().equals("0.0")) {
                                try {
                                    HttpGet httppost = new HttpGet("http://api.geonames.org/geoCodeAddressJSON?q=" + URLEncoder.encode(String.valueOf(newAddress.get(i).getAddressFormattedForGoogle())) + "&username=tristarsoftware");

                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpResponse response = httpclient.execute(httppost);

                                    int status = response.getStatusLine().getStatusCode();

                                    if (status == 200) {
                                        HttpEntity entity = response.getEntity();
                                        String data = EntityUtils.toString(entity);
                                        Log.d("geonames addresses: ", "newAddress: " + i + ":" + data + newAddress.get(i).getAddressFormattedForGoogle());


                                        JSONObject jsono = new JSONObject(data);

                                        JSONObject json_LL = jsono.getJSONObject("address");


                                        String lat = json_LL.getString("lat");
                                        String lng = json_LL.getString("lng");

                                        newAddress.get(i).setLatitude(lat + "");
                                        newAddress.get(i).setLongitude(lng + "");
                                        database.UpdateProcessOpenAddressTable(newAddress.get(i));

                                        if (!lat.isEmpty() && !lng.isEmpty()) {
                                            System.out.println("Data LAT : " + lat + " Data LNG : " + lng + " ----- " + newAddress.get(i).getAddressFormattedForGoogle());

                                        }


                                        if (lat.length() != 0 && lng.length() != 0) {


                                        }

                                        if (!newAddress.get(i).getLatitude().equals("0.0")
                                                && !newAddress.get(i).getLongitude().equals("0.0")) {

                                            ServerSubmittedGPSUpdateObject serverSubmittedGPSUpdateObject = new ServerSubmittedGPSUpdateObject();
                                            serverSubmittedGPSUpdateObject.setAddressLineItem(newAddress.get(i).getAddressLineItem());
                                            serverSubmittedGPSUpdateObject.setLatitudeLongitude(newAddress.get(i).getLatitude() + ";" + newAddress.get(i).getLongitude());
                                            serverSubmittedGPSUpdateObject.setWorkorder(newAddress.get(i).getWorkorder());
                                            serverSubmittedGPSUpdateObject.setPickup(false);
                                            serverSubmittedGPSUpdateObjects.add(serverSubmittedGPSUpdateObject);

                                        }

                                    }
                                } catch (IOException e) {

                                    e.printStackTrace();
                                } catch (JSONException e) {

                                    System.out.println(" Data Not found : " + newAddress.get(i).getAddressFormattedForGoogle());

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//								Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//								String result = null;
//								try {
//									List<Address> addressList = geocoder.getFromLocationName(newAddress.get(i)
//											.getAddressFormattedForGoogle(), 1);
//									if (addressList != null && addressList.size() > 0) {
//										Address address = addressList.get(0);
//										StringBuilder sb = new StringBuilder();
//										sb.append(address.getLatitude()).append("\n");
//										sb.append(address.getLongitude()).append("\n");
//										Log.d("Geo Code", "" + address.getLatitude() + "," + address.getLongitude());
//                                        newAddress.get(i).setLatitude(address.getLatitude() + "");
//                                        newAddress.get(i).setLongitude(address.getLongitude() + "");
//										database.UpdateProcessOpenAddressTable(newAddress.get(i));
//										result = sb.toString();
//									}
//								} catch (IOException e) {
//									Log.e("Error", "Unable to connect to Geocoder", e);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}


//								try {
//									BSKmlResult geoResult = DataBaseHelper
//											.getGeoCodeAddress(insertprocess.get(i)
//													.getAddressFormattedForGoogle());
//									Log.d("Geo Code Address",""+insertprocess.get(i)
//											.getAddressFormattedForGoogle());
//									if (geoResult != null && geoResult.getLocation() != null) {
//										Log.d("Geo Code",""+geoResult.getLocation()
//												.getLatitude()+","+geoResult.getLocation()
//												.getLongitude());
//										insertprocess.get(i).setLatitude(geoResult.getLocation()
//												.getLatitude() + "");
//										insertprocess.get(i).setLongitude(geoResult.getLocation()
//												.getLongitude() + "");
//									}
//								} catch (Exception e) {
//								}

                            }
                        }

                        if (serverSubmittedGPSUpdateObjects.size() != 0) {
                            new updateGPSCoordinates().execute(serverSubmittedGPSUpdateObjects);
                        }


                    }

                };
                thread.start();


            } catch (Exception e) {
                e.printStackTrace();
                returnAppOptions = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class InsertCourtOpenAddress extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {


                //database.insertProcessOpenAddressFromServernew(insertprocess);

				/*ListCategory.this.runOnUiThread(new Runnable() {
													@Override
													public void run() {


													}
												});*/

                Log.d("", "thread starting:Geonames");
                Thread thread = new Thread() {
                    @Override
                    public void run() {

                        ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjects = new ArrayList<>();

                        for (int i = 0; i < courtAddressForServers.size(); i++) {
                            if (courtAddressForServers.get(i).getLatitude().equals("0.0")
                                    && courtAddressForServers.get(i).getLongitude().equals("0.0")) {
                                String result = null;
                                try {
                                    HttpGet httppost = new HttpGet("http://api.geonames.org/geoCodeAddressJSON?q=" + URLEncoder.encode(String.valueOf(courtAddressForServers.get(i).getAddressFormattedForDisplay())) + "&username=tristarsoftware");

                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpResponse response = httpclient.execute(httppost);

                                    int status = response.getStatusLine().getStatusCode();

                                    if (status == 200) {
                                        HttpEntity entity = response.getEntity();
                                        String data = EntityUtils.toString(entity);
                                        Log.d("geonames response:", "courtAddressForServers: " + i + ":" + data);
                                        JSONObject jsono = new JSONObject(data);

                                        JSONObject json_LL = jsono.getJSONObject("address");


                                        String lat = json_LL.getString("lat");
                                        String lng = json_LL.getString("lng");

                                        StringBuilder sb = new StringBuilder();
                                        sb.append(lat).append("\n");
                                        sb.append(lng).append("\n");
                                        Log.d("Geonames", "" + lat + "," + lng);
                                        courtAddressForServers.get(i).setLatitude(lat + "");
                                        courtAddressForServers.get(i).setLongitude(lng + "");
                                        database.UpdateCourtJobTable(courtAddressForServers.get(i));
                                        result = sb.toString();

                                        if (!lat.isEmpty() && !lng.isEmpty()) {
                                            System.out.println("Data LAT : Geonames" + lat + " Data LNG : " + lng + " ----- " + courtAddressForServers.get(i).getAddressFormattedForDisplay());
                                        }

                                        if (lat.length() != 0 && lng.length() != 0) {

                                        }
                                        if (!courtAddressForServers.get(i).getLatitude().equals("0.0")
                                                && !courtAddressForServers.get(i).getLongitude().equals("0.0")) {

                                            ServerSubmittedGPSUpdateObject serverSubmittedGPSUpdateObject = new ServerSubmittedGPSUpdateObject();
                                            serverSubmittedGPSUpdateObject.setAddressLineItem(courtAddressForServers.get(i).getAddresslineitem());
                                            serverSubmittedGPSUpdateObject.setLatitudeLongitude(courtAddressForServers.get(i).getLatitude() + ";" + courtAddressForServers.get(i).getLongitude());
                                            serverSubmittedGPSUpdateObject.setWorkorder(courtAddressForServers.get(i).getWorkorder());
                                            serverSubmittedGPSUpdateObject.setPickup(false);
                                            serverSubmittedGPSUpdateObjects.add(serverSubmittedGPSUpdateObject);

                                        }

                                    }
                                } catch (IOException e) {

                                    Log.e("Error", "Unable to connect to Geonames", e);
                                } catch (JSONException e) {

                                    System.out.println(" Geonames : Data Not found : " + courtAddressForServers.get(i).getAddressFormattedForDisplay());

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//								Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//								String result2 = null;
//								try {
//									List<Address> addressList = geocoder.getFromLocationName(courtAddressForServers.get(i)
//											.getAddressFormattedForDisplay(), 1);
//									if (addressList != null && addressList.size() > 0) {
//										Address address = addressList.get(0);
//										StringBuilder sb = new StringBuilder();
//										sb.append(address.getLatitude()).append("\n");
//										sb.append(address.getLongitude()).append("\n");
//										Log.d("Geo Code", "" + address.getLatitude() + "," + address.getLongitude());
//                                        courtAddressForServers.get(i).setLatitude(address.getLatitude() + "");
//                                        courtAddressForServers.get(i).setLongitude(address.getLongitude() + "");
//										database.UpdateCourtJobTable(courtAddressForServers.get(i));
//										result2 = sb.toString();
//									}
//								} catch (IOException e) {
//									Log.e("Error", "Unable to connect to Geocoder", e);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}


//								try {
//									BSKmlResult geoResult = DataBaseHelper
//											.getGeoCodeAddress(insertprocess.get(i)
//													.getAddressFormattedForGoogle());
//									Log.d("Geo Code Address",""+insertprocess.get(i)
//											.getAddressFormattedForGoogle());
//									if (geoResult != null && geoResult.getLocation() != null) {
//										Log.d("Geo Code",""+geoResult.getLocation()
//												.getLatitude()+","+geoResult.getLocation()
//												.getLongitude());
//										insertprocess.get(i).setLatitude(geoResult.getLocation()
//												.getLatitude() + "");
//										insertprocess.get(i).setLongitude(geoResult.getLocation()
//												.getLongitude() + "");
//									}
//								} catch (Exception e) {
//								}

                            }
                        }

                        if (serverSubmittedGPSUpdateObjects.size() != 0) {
                            new updateGPSCoordinates().execute(serverSubmittedGPSUpdateObjects);
                        }


                        courtAddressList.clear();


                    }

                };
                thread.start();


            } catch (Exception e) {
                e.printStackTrace();
                returnAppOptions = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class InsertPickupOpenAddress extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {


                //database.insertProcessOpenAddressFromServernew(insertprocess);

				/*ListCategory.this.runOnUiThread(new Runnable() {
													@Override
													public void run() {


													}
												});*/


                Thread thread = new Thread() {
                    @Override
                    public void run() {

                        ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjects = new ArrayList<>();

                        for (int i = 0; i < PickupAddresList.size(); i++) {
                            if (PickupAddresList.get(i).getLatitude().equals("0.0")
                                    && PickupAddresList.get(i).getLongitude().equals("0.0")) {


                                String AddressForDisplay = PickupAddresList.get(i).getAddressFormattedForDisplay();
                                Log.d("Address for display", "" + AddressForDisplay);
                                for (int ii = 0; ii < AddressForDisplay.length(); ii++) {
                                    Character character = AddressForDisplay.charAt(ii);
                                    if (Character.isDigit(character)) {

                                        AddressForDisplay = AddressForDisplay.substring(ii);
                                        //result += character;
                                    }
                                }

                                String result = null;
                                try {
                                    HttpGet httppost = new HttpGet("http://api.geonames.org/geoCodeAddressJSON?q=" + URLEncoder.encode(AddressForDisplay) + "&username=tristarsoftware");

                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpResponse response = httpclient.execute(httppost);

                                    int status = response.getStatusLine().getStatusCode();

                                    if (status == 200) {
                                        HttpEntity entity = response.getEntity();
                                        String data = EntityUtils.toString(entity);
                                        Log.d("geonames response :", "PickupAddresList: " + i + ":" + data);
                                        JSONObject jsono = new JSONObject(data);

                                        JSONObject json_LL = jsono.getJSONObject("address");


                                        String lat = json_LL.getString("lat");
                                        String lng = json_LL.getString("lng");

                                        StringBuilder sb = new StringBuilder();
                                        sb.append(lat).append("\n");
                                        sb.append(lng).append("\n");
                                        Log.d("Geonames", "" + lat + "," + lng);
                                        PickupAddresList.get(i).setLatitude(lat + "");
                                        PickupAddresList.get(i).setLongitude(lng + "");
                                        database.UpdatePickupJobTable(PickupAddresList.get(i));
                                        result = sb.toString();

                                        if (!lat.isEmpty() && !lng.isEmpty()) {
                                            System.out.println("Pickup_1 : Data LAT : Geonames" + lat + "pickup_1 Data LNG : " + lng + " ----- " + PickupAddresList.get(i).getAddressFormattedForDisplay());
                                        }

                                        if (lat.length() != 0 && lng.length() != 0) {

                                        }
                                        if (!PickupAddresList.get(i).getLatitude().equals("0.0")
                                                && !PickupAddresList.get(i).getLongitude().equals("0.0")) {

                                            ServerSubmittedGPSUpdateObject serverSubmittedGPSUpdateObject = new ServerSubmittedGPSUpdateObject();
                                            serverSubmittedGPSUpdateObject.setAddressLineItem(PickupAddresList.get(i).getAddressLineItem());
                                            serverSubmittedGPSUpdateObject.setLatitudeLongitude(PickupAddresList.get(i).getLatitude() + ";" + PickupAddresList.get(i).getLongitude());
                                            serverSubmittedGPSUpdateObject.setWorkorder(PickupAddresList.get(i).getWorkorder());
                                            serverSubmittedGPSUpdateObject.setPickup(true);
                                            serverSubmittedGPSUpdateObjects.add(serverSubmittedGPSUpdateObject);

                                        }

                                    }
                                } catch (IOException e) {

                                    Log.e("Error", "Unable to connect to Geonames", e);
                                } catch (JSONException e) {

                                    System.out.println(" Geonames : Data Not found : " + PickupAddresList.get(i).getAddressFormattedForDisplay());

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                                String result = null;
//
//
//								try {
//									List<Address> addressList = geocoder.getFromLocationName(AddressForDisplay, 1);
//									if (addressList != null && addressList.size() > 0) {
//										Address address = addressList.get(0);
//										StringBuilder sb = new StringBuilder();
//										sb.append(address.getLatitude()).append("\n");
//										sb.append(address.getLongitude()).append("\n");
//										Log.d("Geo Code", "" + address.getLatitude() + "," + address.getLongitude());
//										PickupAddresList.get(i).setLatitude(address.getLatitude() + "");
//										PickupAddresList.get(i).setLongitude(address.getLongitude() + "");
//										database.UpdatePickupJobTable(PickupAddresList.get(i));
//										result = sb.toString();
//									}
//								} catch (IOException e) {
//									Log.e("Error", "Unable to connect to Geocoder", e);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}


                            }
                        }

                        if (serverSubmittedGPSUpdateObjects.size() != 0) {
                            new updateGPSCoordinates().execute(serverSubmittedGPSUpdateObjects);
                        }

                        PickupAddresList.clear();

                    }

                };
                thread.start();


            } catch (Exception e) {
                e.printStackTrace();
                returnAppOptions = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class InsertDeliveryOpenAddress extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {


                //database.insertProcessOpenAddressFromServernew(insertprocess);

				/*ListCategory.this.runOnUiThread(new Runnable() {
													@Override
													public void run() {


													}
												});*/


                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjects = new ArrayList<>();

                        for (int i = 0; i < DeliveryAddresList.size(); i++) {
                            if (DeliveryAddresList.get(i).getLatitude().equals("0.0")
                                    && DeliveryAddresList.get(i).getLongitude().equals("0.0")) {


                                String AddressForDisplay = DeliveryAddresList.get(i).getAddressFormattedForDisplay();
                                Log.d("Address for display Delivery Jobs", "" + AddressForDisplay);
                                for (int ii = 0; ii < AddressForDisplay.length(); ii++) {
                                    Character character = AddressForDisplay.charAt(ii);
                                    if (Character.isDigit(character)) {

                                        AddressForDisplay = AddressForDisplay.substring(ii);
                                        //result += character;
                                    }
                                }
                                String result = null;
                                try {
                                    HttpGet httppost = new HttpGet("http://api.geonames.org/geoCodeAddressJSON?q=" + URLEncoder.encode(AddressForDisplay) + "&username=tristarsoftware");

                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpResponse response = httpclient.execute(httppost);

                                    int status = response.getStatusLine().getStatusCode();

                                    if (status == 200) {
                                        HttpEntity entity = response.getEntity();
                                        String data = EntityUtils.toString(entity);
                                        Log.d("geonames response:", "DeliveryAddresList: " + i + ":" + data);
                                        JSONObject jsono = new JSONObject(data);

                                        JSONObject json_LL = jsono.getJSONObject("address");


                                        String lat = json_LL.getString("lat");
                                        String lng = json_LL.getString("lng");

                                        StringBuilder sb = new StringBuilder();
                                        sb.append(lat).append("\n");
                                        sb.append(lng).append("\n");
                                        Log.d("Geonames", "" + lat + "," + lng);
                                        DeliveryAddresList.get(i).setLatitude(lat + "");
                                        DeliveryAddresList.get(i).setLongitude(lng + "");
                                        database.UpdateDeliveryJobTable(DeliveryAddresList.get(i));
                                        result = sb.toString();

                                        if (!lat.isEmpty() && !lng.isEmpty()) {
                                            System.out.println("Delivery_1 Data LAT : Geonames" + lat + "Delivery_1 Data LNG : " + lng + " ----- " + DeliveryAddresList.get(i).getAddressFormattedForDisplay());
                                        }

                                        if (lat.length() != 0 && lng.length() != 0) {

                                        }

                                        if (!DeliveryAddresList.get(i).getLatitude().equals("0.0")
                                                && !DeliveryAddresList.get(i).getLongitude().equals("0.0")) {

                                            ServerSubmittedGPSUpdateObject serverSubmittedGPSUpdateObject = new ServerSubmittedGPSUpdateObject();
                                            serverSubmittedGPSUpdateObject.setAddressLineItem(DeliveryAddresList.get(i).getAddressLineItem());
                                            serverSubmittedGPSUpdateObject.setLatitudeLongitude(DeliveryAddresList.get(i).getLatitude() + ";" + DeliveryAddresList.get(i).getLongitude());
                                            serverSubmittedGPSUpdateObject.setWorkorder(DeliveryAddresList.get(i).getWorkorder());
                                            serverSubmittedGPSUpdateObject.setPickup(false);
                                            serverSubmittedGPSUpdateObjects.add(serverSubmittedGPSUpdateObject);

                                        }
                                    }
                                } catch (IOException e) {

                                    Log.e("Error", "Unable to connect to Geonames", e);
                                } catch (JSONException e) {

                                    System.out.println(" Geonames : Data Not found : " + DeliveryAddresList.get(i).getAddressFormattedForDisplay());

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                                String result = null;
//                                try {
//
//                                    List<Address> addressList = geocoder.getFromLocationName(AddressForDisplay, 1);
//                                    if (addressList != null && addressList.size() > 0) {
//                                        Address address = addressList.get(0);
//                                        StringBuilder sb = new StringBuilder();
//                                        sb.append(address.getLatitude()).append("\n");
//                                        sb.append(address.getLongitude()).append("\n");
//                                        Log.d("Geo Code Delivery Jobs", "" + address.getLatitude() + "," + address.getLongitude());
//                                        DeliveryAddresList.get(i).setLatitude(address.getLatitude() + "");
//                                        DeliveryAddresList.get(i).setLongitude(address.getLongitude() + "");
//                                        database.UpdateDeliveryJobTable(DeliveryAddresList.get(i));
//                                        result = sb.toString();
//                                    }
//                                } catch (IOException e) {
//                                    Log.e("Error", "Unable to connect to Geocoder", e);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }


//								try {
//									BSKmlResult geoResult = DataBaseHelper
//											.getGeoCodeAddress(insertprocess.get(i)
//													.getAddressFormattedForGoogle());
//									Log.d("Geo Code Address",""+insertprocess.get(i)
//											.getAddressFormattedForGoogle());
//									if (geoResult != null && geoResult.getLocation() != null) {
//										Log.d("Geo Code",""+geoResult.getLocation()
//												.getLatitude()+","+geoResult.getLocation()
//												.getLongitude());
//										insertprocess.get(i).setLatitude(geoResult.getLocation()
//												.getLatitude() + "");
//										insertprocess.get(i).setLongitude(geoResult.getLocation()
//												.getLongitude() + "");
//									}
//								} catch (Exception e) {
//								}

                            }
                        }

                        if (serverSubmittedGPSUpdateObjects.size() != 0) {
                            new updateGPSCoordinates().execute(serverSubmittedGPSUpdateObjects);
                        }


                        DeliveryAddresList.clear();

                    }

                };
                thread.start();


            } catch (Exception e) {
                e.printStackTrace();
                returnAppOptions = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class Notifications extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            //	com.tristar.main.ProgressBar.showCommonProgressDialog(ListCategory.this);
        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String sessionId = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());
                returnMessagesObjectses = WebServiceConsumer.getInstance()
                        .returnMessages(
                                sessionId);

            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
                returnMessagesObjectses = null;
            } catch (Exception e) {
                e.printStackTrace();
                returnMessagesObjectses = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//			com.tristar.main.ProgressBar.dismiss();
            if (returnMessagesObjectses != null) {
                if (returnMessagesObjectses.size() == 0) {
                    layout_Notifications.setBackgroundResource(R.color.blackcolor);
                    txtNotificationItems.setText("No Notifications Found");
//					final Dialog dialog = new Dialog(context);
//					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//					dialog.setContentView(R.layout.alertbox);
//
//					TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
//					text.setText("No Notifications to show ");
//
//					Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
//
//					dialogButton.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.dismiss();
//
//						}
//					});
//
//					dialog.show();
                } else {

                    StringBuilder t = new StringBuilder();
                    for (int i = 0; i < returnMessagesObjectses.size(); i++) {

                        t.append(returnMessagesObjectses.get(i).getMessage() + " ,");
                    }

                    String shared_Message = t.toString();
                    if (shared_Message.equals(str_notificationHistory)) {
                        layout_Notifications.setBackgroundResource(R.color.blackcolor);
                        txtNotificationItems.setText("No New Notifications");
                    } else {
                        layout_Notifications.setBackgroundResource(R.color.accent);
                        txtNotificationItems.setText("" + returnMessagesObjectses.size() + " item(s) require your attention");
                    }

//					Log.d("Notifications",""+returnMessagesObjectses.size());
//					SessionData.getInstance().setReturnMessagesObjectses(returnMessagesObjectses);
//					Intent HistoryList = new Intent(ListCategory.this,NotificationActivity.class);
//					startActivity(HistoryList);
                }
            }
        }
    }

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ZBAR_SCANNER_REQUEST:
				// case ZBAR_QR_SCANNER_REQUEST:
				if (resultCode == RESULT_OK) {

					//
					// Str_ScanedWorkOrder=(data.getStringExtra(ZBarConstants.SCAN_RESULT));

					for (int i = 0; i < processOrderListArray.size(); i++) {

						ProcessAddressForServer process = processOrderListArray.get(i);
						if (process.getWorkorder().length() > 2) {

							if(Str_ScanedWorkOrder.contains(process.getWorkorder())){

								ProcessOrderID = process.getProcessOrderID();
								flag = true;
								break;
							}

						//	processOrderlist.add(process);
						}
					}

					if(flag==true){
						finish();
						Intent processdetail = new Intent(ListCategory.this,
								ProcessOrderDetail.class);
						processdetail.putExtra("processOrderID",
								ProcessOrderID);
						startActivity(processdetail);
					}else{



						if (courtServiceListArray.size() > 0) {
							serviceAddressAll.add(("Court Services"));
							serviceAddressAll.addAll(courtServiceListArray);
							ArrayPodWorkOrder.add("Court Services");

							for(int i = 0; i<courtServiceListArray.size();i++){
								ArrayPodWorkOrder.add(courtServiceListArray.get(i).getWorkorder());
							}

						}

						if (pickupServiceListArray.size() > 0) {
							serviceAddressAll.add(("Pickup Services"));
							serviceAddressAll.addAll(pickupServiceListArray);

							ArrayPodWorkOrder.add("Pickup Services");

							for(int i = 0; i<pickupServiceListArray.size();i++){
								ArrayPodWorkOrder.add(pickupServiceListArray.get(i).getWorkorder());
							}

						}
						if (deliveryServiceListArray.size() > 0) {
							serviceAddressAll.add(("Delivery Services"));
							serviceAddressAll.addAll(deliveryServiceListArray);

							ArrayPodWorkOrder.add("Delivery Services");

							for(int i = 0; i<deliveryServiceListArray.size();i++){
								ArrayPodWorkOrder.add(deliveryServiceListArray.get(i).getWorkorder());
							}

						}


						for(int i = 0;i< serviceAddressAll.size();i++){

							if(Str_ScanedWorkOrder.contains(ArrayPodWorkOrder.get(i))){
								item = serviceAddressAll.get(i);
								flagpod = true;
								break;

							}

						}

						if(flagpod == true){
							navigateOnSelectItem(item);
						}
						else{
							Toast.makeText(this, "Scanned Workorder is not available", Toast.LENGTH_SHORT).show();
						}

					}


					//
//					str_jobScanner = str_jobScanner.concat(data
//							.getStringExtra(ZBarConstants.SCAN_RESULT) + "\n");
//					Job_Scanner.setText(str_jobScanner);

					// Toast.makeText(this, "Scan Result = " +
					// data.getStringExtra(ZBarConstants.SCAN_RESULT),
					// Toast.LENGTH_SHORT).show();
				} else if (resultCode == RESULT_CANCELED && data != null) {
					String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
					if (!TextUtils.isEmpty(error)) {
						Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
					}
				}
				break;

		}
	}*/

    private class Mytask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            //ProgressBar.showCommonProgressDialog(ListCategory.this,"Processing");
        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String sessionId = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());
                returnAppOptions = WebServiceConsumer.getInstance()
                        .getAppOptions(
                                sessionId);

            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
                returnAppOptions = null;
            } catch (Exception e) {
                e.printStackTrace();
                returnAppOptions = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //	ProgressBar.dismiss();
            layout_Notifications.setVisibility(View.VISIBLE);
            if (returnAppOptions != null) {
                //	ProgressBar.dismiss();
                SessionData.getInstance().setReturnAppOptions(returnAppOptions);

                Log.d("Physical", "" + returnAppOptions.isShowPhysicalDescription());
                Log.d("Job", "" + returnAppOptions.isUseJobTracking());
                mylist = (ListView) findViewById(R.id.listView1);


                if (returnAppOptions.isUseJobTracking() == true) {
                    itemadapter = new ArrayAdapter<String>(ListCategory.this,
                            R.layout.textcategory, R.id.txt_Category, items);

                    mylist.setAdapter(itemadapter);

                    mylist.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            int itemposition = position;

                            switch (itemposition) {
//							case 0:
//								new Notifications().execute();
//								break;
                                case 0:
                                    finish();

                                    SessionData.getInstance().setScanner_activity(0);
                                    SessionData.getInstance().setScanner_activity_result(0);
                                    SessionData.getInstance().setScanner(0);
                                    SessionData.getInstance().setScanner_result(0);

                                    SessionData.getInstance().setScanner_value("");
                                    SessionData.getInstance().setFinal_scanjobresult("");
                                    SessionData.getInstance().setFinal_locationresult("");

                                    SessionData.getInstance().setScanner_loca(0);

                                    Intent process = new Intent(ListCategory.this, ProcessOrder.class);
                                    startActivity(process);
                                    break;

                                case 1:
                                    finish();

                                    Intent court = new Intent(ListCategory.this,
                                            CourtService.class);
                                    startActivity(court);
                                    break;
                                case 2:
                                    if (jobsInQueue > 0) {
                                        finish();
                                        Intent main1 = new Intent(ListCategory.this,
                                                JobsQueue.class);
                                        startActivity(main1);
                                    } else {
                                        new CustomAlertDialog(ListCategory.this,
                                                "No jobs available in Queue!").show();
                                    }
                                    break;
                                case 3:
                                    finish();

                                    SessionData.getInstance().setScanner_activity(0);
                                    SessionData.getInstance().setScanner_activity_result(0);
                                    SessionData.getInstance().setScanner(0);
                                    SessionData.getInstance().setScanner_result(0);

                                    SessionData.getInstance().setScanner_value("");
                                    SessionData.getInstance().setFinal_scanjobresult("");
                                    SessionData.getInstance().setFinal_locationresult("");
                                    SessionData.getInstance().setType(0);
                                    SessionData.getInstance().setStatus(0);

                                    SessionData.getInstance().setScanner_loca(0);

                                    Intent route = new Intent(ListCategory.this, RouteTrackerApp.class);
                                    startActivity(route);
                                    break;

                                case 4:

                                    try {
                                        processOrderListArray = database
                                                .getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

                                        SessionData.getInstance().setArrayprocessOrderList(processOrderListArray);

                                        courtServiceListArray = database
                                                .getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
                                        pickupServiceListArray = database.getPickupOrderValuesFromTable();
                                        deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();
                                        SessionData.getInstance().setSelectedItem(1);
                                        SessionData.getInstance().setArraydeliveryServiceList(deliveryServiceListArray);
                                        SessionData.getInstance().setArraycourtServiceList(courtServiceListArray);
                                        SessionData.getInstance().setArraypickupServiceList(pickupServiceListArray);

                                    } catch (Exception e) {
                                        processOrderListArray = new ArrayList<ProcessAddressForServer>();
                                        e.printStackTrace();
                                    }
                                    if (isCameraAvailable()) {
                                        SessionData.getInstance().setScanner_activity(1);
                                        SessionData.getInstance().setScanner_activity_result(0);

                                        launchActivity(SimpleScannerActivity.class);

									/*Intent intent = new Intent(getApplicationContext(),
											ZBarScannerActivity.class);

									startActivityForResult(intent, ZBAR_SCANNER_REQUEST);*/
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Rear Facing Camera Unavailable",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    //finish();
//								Intent route = new Intent(ListCategory.this,
//										RouteTrackerApp.class);
//								startActivity(route);
                                    break;
                                default:

                            }
                        }
                    });
                } else {

                    itemadapter_1 = new ArrayAdapter<String>(ListCategory.this, R.layout.textcategory, R.id.txt_Category, itemss);

                    mylist.setAdapter(itemadapter_1);
                    mylist.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            int itemposition = position;

                            switch (itemposition) {

//							case 0:
//								new Notifications().execute();
//								break;
                                case 0:
                                    finish();

                                    SessionData.getInstance().setScanner_activity(0);
                                    SessionData.getInstance().setScanner_activity_result(0);
                                    SessionData.getInstance().setScanner(0);
                                    SessionData.getInstance().setScanner_result(0);

                                    SessionData.getInstance().setScanner_value("");
                                    SessionData.getInstance().setFinal_scanjobresult("");
                                    SessionData.getInstance().setFinal_locationresult("");

                                    SessionData.getInstance().setScanner_loca(0);

                                    Intent process = new Intent(ListCategory.this,
                                            ProcessOrder.class);
                                    startActivity(process);
                                    break;

                                case 1:
                                    finish();
                                    Intent court = new Intent(ListCategory.this,
                                            CourtService.class);
                                    startActivity(court);
                                    break;
                                case 2:
                                    if (jobsInQueue > 0) {
                                        finish();
                                        Intent main1 = new Intent(ListCategory.this,
                                                JobsQueue.class);
                                        startActivity(main1);
                                    } else {
                                        new CustomAlertDialog(ListCategory.this,
                                                "No jobs available in Queue!").show();
                                    }
                                    break;

                                case 3:

                                    try {
                                        processOrderListArray = database
                                                .getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();
                                        SessionData.getInstance().setArrayprocessOrderList(processOrderListArray);
                                        courtServiceListArray = database
                                                .getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
                                        pickupServiceListArray = database.getPickupOrderValuesFromTable();
                                        deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();
                                        SessionData.getInstance().setSelectedItem(1);
                                        SessionData.getInstance().setArraydeliveryServiceList(deliveryServiceListArray);
                                        SessionData.getInstance().setArraycourtServiceList(courtServiceListArray);
                                        SessionData.getInstance().setArraypickupServiceList(pickupServiceListArray);

                                    } catch (Exception e) {
                                        processOrderListArray = new ArrayList<ProcessAddressForServer>();
                                        e.printStackTrace();
                                    }
                                    if (isCameraAvailable()) {
									/*Intent intent = new Intent(getApplicationContext(),
											ZBarScannerActivity.class);

									startActivityForResult(intent, ZBAR_SCANNER_REQUEST);*/
                                        if (SessionData.getInstance().isValidate_record_Deligence_CheckBox()) {
                                            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
                                            SessionData.getInstance().setScanner_activity(2);
                                            SessionData.getInstance().setScanner_activity_result(2);
                                            launchActivity(SimpleScannerActivity.class);
                                        } else {
                                            SessionData.getInstance().setScanner_activity(1);
                                            SessionData.getInstance().setScanner_activity_result(0);
                                            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(2);
                                            launchActivity(SimpleScannerActivity.class);
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Rear Facing Camera Unavailable",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    //finish();
                                    break;
                                default:

                            }
                        }
                    });
                }

            } else {
                mylist = (ListView) findViewById(R.id.listView1);

                itemadapter_2 = new ArrayAdapter<String>(ListCategory.this,
                        R.layout.textcategory, R.id.txt_Category, itemsss);

                mylist.setAdapter(itemadapter_2);
                mylist.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        int itemposition = position;

                        switch (itemposition) {
//					case 0:
//						new Notifications().execute();
//						break;
                            case 0:
                                finish();

                                SessionData.getInstance().setScanner_activity(0);
                                SessionData.getInstance().setScanner_activity_result(0);
                                SessionData.getInstance().setScanner(0);
                                SessionData.getInstance().setScanner_result(0);

                                SessionData.getInstance().setScanner_value("");
                                SessionData.getInstance().setFinal_scanjobresult("");
                                SessionData.getInstance().setFinal_locationresult("");

                                SessionData.getInstance().setScanner_loca(0);

                                Intent process = new Intent(ListCategory.this,
                                        ProcessOrder.class);
                                startActivity(process);
                                break;

                            case 1:
                                finish();
                                Intent court = new Intent(ListCategory.this,
                                        CourtService.class);
                                startActivity(court);
                                break;
                            case 2:
                                if (jobsInQueue > 0) {
                                    finish();
                                    Intent main1 = new Intent(ListCategory.this,
                                            JobsQueue.class);
                                    startActivity(main1);
                                } else {
                                    new CustomAlertDialog(ListCategory.this,
                                            "No jobs available in Queue!").show();
                                }
                                break;
                            case 3:

                                try {
                                    processOrderListArray = database
                                            .getprocessOrderValuesFromTabletoDisplayInListVieAndMapView();

                                    SessionData.getInstance().setArrayprocessOrderList(processOrderListArray);

                                    courtServiceListArray = database
                                            .getcourtOrderValuesFromTabletoDisplayInListVieAndMapView();
                                    pickupServiceListArray = database.getPickupOrderValuesFromTable();
                                    deliveryServiceListArray = database.getDeliveryOrderValuesFromTable();
                                    SessionData.getInstance().setSelectedItem(1);

                                    SessionData.getInstance().setArraydeliveryServiceList(deliveryServiceListArray);
                                    SessionData.getInstance().setArraycourtServiceList(courtServiceListArray);
                                    SessionData.getInstance().setArraypickupServiceList(pickupServiceListArray);

                                } catch (Exception e) {
                                    processOrderListArray = new ArrayList<ProcessAddressForServer>();
                                    e.printStackTrace();
                                }
                                if (isCameraAvailable()) {

                                    SessionData.getInstance().setScanner_activity(1);
                                    SessionData.getInstance().setScanner_activity_result(0);

                                    launchActivity(SimpleScannerActivity.class);
							/*Intent intent = new Intent(getApplicationContext(),
									ZBarScannerActivity.class);

							startActivityForResult(intent, ZBAR_SCANNER_REQUEST);*/
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Rear Facing Camera Unavailable",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //finish();
                                break;

                            default:

                        }
                    }
                });

                Log.d("processOrderListArray", "" + processOrderListArray.size());

            }

            if (mylist.getVisibility() == View.VISIBLE) {
                bottom_checkbox.setVisibility(View.VISIBLE);
            } else {

            }
            super.onPostExecute(result);
        }
    }

    private class getIsAudiorecordingon extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//			ProgressBar.showCommonProgressDialog(ListCategory.this,"Loading");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//			ProgressBar.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String sessionId = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());

                boolean isaudio_recode_on = WebServiceConsumer.getInstance().getIsAudioRecordingOn(sessionId);

                SessionData.getInstance().setAudio_on(isaudio_recode_on);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    private class updateGPSCoordinates extends AsyncTask<ArrayList, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//			ProgressBar.showCommonProgressDialog(ListCategory.this,"Loading");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//			ProgressBar.dismiss();

        }

        @Override
        protected Void doInBackground(ArrayList... params) {

            ArrayList<ServerSubmittedGPSUpdateObject> serverSubmittedGPSUpdateObjectsList = params[0];

            try {

                String sessionId = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());
                String result = WebServiceConsumer.getInstance().UpdateGPSCoordinates(
                        sessionId, serverSubmittedGPSUpdateObjectsList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


}