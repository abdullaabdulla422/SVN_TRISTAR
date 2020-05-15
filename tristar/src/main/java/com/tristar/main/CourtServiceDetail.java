package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tristar.db.DataBaseHelper;
import com.tristar.object.CodeAndTitle;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.ReturnHistoryObject;
import com.tristar.object.ReturnStatusListObect;
import com.tristar.object.SubmitStatusList;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;
import com.tristar.wheelpicker.ArrayWheelAdapter;
import com.tristar.wheelpicker.OnWheelChangedListener;
import com.tristar.wheelpicker.OnWheelScrollListener;
import com.tristar.wheelpicker.WheelView;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class CourtServiceDetail extends Activity implements OnClickListener, OnLongClickListener {
    public static boolean mapSelect = false;
    public PopupWindow popupManner;
    public View mannerView;
    public String selectedManner;
    public ArrayList<ReturnStatusListObect> statuslist = new ArrayList<>();
    Button submit, submitStatus, submit_status, select_status;
    TextView id, back, address, txt_name, txt_duedate, txt_priority, txt_recieveddate, txt_recievedtime,
            txt_instructions, txt_documents, txt_workorder, txt_caseName, txt_caseNumber, txt_requestor, txt_phonenumber, txt_court,
            Custumer_Name, Custumer_address;
    ImageView image;
    EditText Edt_comment;
    String Id = "";
    CourtAddressForServer addresServer;
    String workorder;
    Dialog mDialog, Dialog;
    Context context;
    TextView txt_Dialog_Workorder;
    TextView txtClientAddress;
    String phone1, phone2, phone3, phone, finalphone;
    TextView txt_popup_cancel, txt_popup_done;
    int diligenceCodeForCurrentStatusInSubmitDiligence;
    DataBaseHelper database;
    ArrayList<ReturnHistoryObject> returnHistoryObjects;
    Button BtnHistory;
    Button BtnViewAttachement;
    ArrayList<CodeAndTitle> code;
    private double dpi;
    private WheelView mannerWhhel;
    private boolean wheelScrolled = false;
    private final OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged() {
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @SuppressWarnings("unused")
        public void onScrollStarts(WheelView wheel) {
            wheelScrolled = true;
        }

        @SuppressWarnings("unused")
        public void onScrollEnds(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }

        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_service_detail_view);
        database = DataBaseHelper.getInstance();
        if (database == null) {
            database = DataBaseHelper.getInstance(CourtServiceDetail.this);
        }
        initialiseViews();
        setlisteners();

        if (addresServer != null) {
            initializeData();
        }

        if (database.getStatusValuesFromDBToDisplay() != null) {
            statuslist = database
                    .getStatusValuesFromDBToDisplay();
        }


    }

    public void initialiseViews() {


        if (SessionData.getInstance().getSelectedItem() == 1) {
            addresServer = (CourtAddressForServer) ListCategory.selectedAddressServer;

        } else {
            addresServer = (CourtAddressForServer) CourtService.selectedAddressServer;

        }

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_duedate = (TextView) findViewById(R.id.txt_duedate);
        txt_court = (TextView) findViewById(R.id.txt_court);
        txtClientAddress = (TextView) findViewById(R.id.txt_clientaddress);
        BtnViewAttachement = (Button) findViewById(R.id.btn_attachment);

        txt_recievedtime = (TextView) findViewById(R.id.txt_recievedtime);
        txt_recieveddate = (TextView) findViewById(R.id.txt_recieveddate);
        txt_phonenumber = (TextView) findViewById(R.id.txt_phonenumber);

        address = (TextView) findViewById(R.id.txt_address);
        txt_priority = (TextView) findViewById(R.id.txt_priority);
        txt_instructions = (TextView) findViewById(R.id.txt_instructions);
        txt_caseName = (TextView) findViewById(R.id.txt_casename);
        txt_caseNumber = (TextView) findViewById(R.id.txt_casenumber);
        txt_requestor = (TextView) findViewById(R.id.txt_requestor);

        txt_workorder = (TextView) findViewById(R.id.lbl_category);
        txt_documents = (TextView) findViewById(R.id.txt_documents);
        submit = (Button) findViewById(R.id.btnSubmit);
        submitStatus = (Button) findViewById(R.id.btnStatus);
        BtnHistory = (Button) findViewById(R.id.btn_history);
        back = (TextView) findViewById(R.id.txt_backfinalstatus);
        image = (ImageView) findViewById(R.id.imageButtonback);
        Custumer_Name = (TextView) findViewById(R.id.txt_custumer_name);
        Custumer_address = (TextView) findViewById(R.id.txt_custumer_address);
    }

    public void setlisteners() {
        address.setOnClickListener(this);
        address.setOnLongClickListener(this);
        back.setOnClickListener(this);
        image.setOnClickListener(this);
        submit.setOnClickListener(this);
        submitStatus.setOnClickListener(this);
        BtnHistory.setOnClickListener(this);
        txt_phonenumber.setOnClickListener(this);
        BtnViewAttachement.setOnClickListener(this);
        Custumer_address.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(CourtServiceDetail.this,
                CourtService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void initializeData() {


        if (addresServer.getCaseName().length() == 0) {
            Custumer_Name.setText("N/A");
        } else {
            Custumer_Name.setText(addresServer.getCaseName());
            String for_Google = addresServer.getCustAddressFormattedForGoogle();
        }

        if (addresServer.getAddressFormattedForDisplay().length() == 0) {
            Custumer_address.setText("N.A");
        } else {
            Custumer_address.setText(addresServer.getCustAddressFormattedForDisplay());
        }
        if (addresServer.getWorkorder().length() == 0) {
            txt_workorder.setText("N/A");
        } else {
            SessionData.getInstance().setImageworkorder(addresServer.getWorkorder());
            txt_workorder.setText(addresServer.getWorkorder());
            workorder = addresServer.getWorkorder();
        }
//		if (addresServer.getName().length() == 0) {
//			txt_name.setText("N/A");
//		} else {
//			txt_name.setText(addresServer.getName());
//		}
        if (addresServer.getAddressFormattedNewLine1().length() != 0) {
            address.setText(addresServer.getAddressFormattedNewLine1() + "\n" + addresServer.getAddressFormattedNewLine2());
        }

        if (addresServer.getServeeName().length() == 0) {
            txt_name.setText("N/A");
        } else {
            txt_name.setText(addresServer.getServeeName());
        }
        if (addresServer.getAddressFormattedForDisplay().length() == 0) {
//			address.setText("N/A");
        } else {
            if (addresServer.getAddressFormattedNewLine1().length() == 0) {
                address.setText(addresServer.getAddressFormattedForDisplay());
            }
            String str = addresServer.getAddressFormattedForDisplay();
            Log.d("Get_CourtSubtitle", "" + str.toString());

            if (str != null || str.length() != 0) {
                for (int j = 0; j < str.length(); j++) {
                    Character character = str.charAt(j);
                    if (character.toString().equals("&")) {
                        str = str.substring(j + 2);
                        if (addresServer.getAddressFormattedNewLine1().length() == 0) {
                            address.setText(str);
                        }
                        Log.d("Get_CourtSubtitle_&", "" + address.getText().toString());
                        break;
                    } else {
                        if (addresServer.getAddressFormattedNewLine1().length() == 0) {
                            address.setText(addresServer.getAddressFormattedForDisplay());
                        }
                        Log.d("Get_CourtSubtitle_", "" + address.getText().toString());
                    }
                }
            }
        }
        if (addresServer.getDueDate() == null || addresServer.getDueDate().length() == 0) {
            txt_duedate.setText("N/A");
        } else {
            String date = addresServer.getDueDate().substring(0, 10);
            try {
                String dateget = getFormattedDateOnly(date);
                txt_duedate.setText(dateget);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (addresServer.getDateReceived() == null) {
            txt_recieveddate.setText("N/A");
        } else {
            String rec_date = addresServer.getDateReceived();
            try {
                //String recdateget = getFormattedDateOnly(rec_date);
                txt_recieveddate.setText(rec_date);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (addresServer.getTimeReceived() == null) {
            txt_recievedtime.setText("N/A");
        } else {
            String time = addresServer.getTimeReceived();
            try {
                //String timeget = getFormattedDateOnly(time);
                txt_recievedtime.setText(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (addresServer.getPriorityTitle().length() == 0) {
            txt_priority.setText("N/A");
        } else {
            txt_priority.setText(addresServer.getPriorityTitle());
        }

        if (addresServer.getAddressFormattedForDisplay().length() == 0) {
            txtClientAddress.setText("N/A");
        } else {
            txtClientAddress.setText(addresServer.getAddressFormattedForDisplay());
        }

        if (addresServer.getInstructions().length() == 0) {
            txt_instructions.setText("N/A");
        } else {
            txt_instructions.setText(addresServer.getInstructions());
        }
        if (addresServer.getDocuments().length() == 0) {
            txt_documents.setText("N/A");
        } else {
            txt_documents.setText(addresServer.getDocuments());
        }
        if (addresServer.getCaseName().length() == 0) {
            txt_caseName.setText("N/A");
        } else {
            txt_caseName.setText(addresServer.getCaseName());
        }
        if (addresServer.getCaseNumber().length() == 0) {
            txt_caseNumber.setText("N/A");
        } else {
            txt_caseNumber.setText(addresServer.getCaseNumber());
        }

        if (addresServer.getContact() == null) {
            txt_requestor.setText("N/A");
        } else {
            txt_requestor.setText(addresServer.getContact());
        }

//		txt_phonenumber

        if (addresServer.getContactPhone().length() == 0) {
            txt_phonenumber.setText("N/A");
        } else {
            phone = addresServer.getContactPhone();

            phone1 = phone.substring(0, 3);
            phone2 = phone.substring(3, 6);
            phone3 = phone.substring(6, 10);
            Log.d("split phone", "" + phone1);

            txt_phonenumber.setText("(" + phone1 + ") " + phone2 + "-" + phone3);
            //txt_phonenumber.setText();
        }

        if (addresServer.getName().length() == 0) {
            txt_court.setText("N/A");
        } else {
            txt_court.setText(addresServer.getName());
        }
    }

    private String getFormattedDateOnly(String date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject = null;
        try {
            dateObject = dateFormat.parse(date);
        } catch (Exception e) {
            dateObject = new Date();
        }
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        return dateFormat.format(dateObject);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {

        if (v == Custumer_address) {
            new getAddressForMap().execute();
        }

        if (v == address) {
            double lat = Double.parseDouble(addresServer.getLatitude());
            double lon = Double.parseDouble(addresServer.getLongitude());

            String uriBegin = "geo:" + lat + "," + lon;
            String uriString = uriBegin + "?q=" + addresServer.getAddressFormattedForDisplay();
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if (txt_phonenumber == v) {
            if (addresServer.getContactPhone().length() != 0) {
                phonedialog();
            }

        } else if (BtnViewAttachement == v) {
            new MyAstask().execute();
        } else if (v == back || v == image) {
            finish();
            Intent submit = new Intent(CourtServiceDetail.this,
                    CourtService.class);
            startActivity(submit);
        } else if (v == submit) {
            finish();
            Intent submit = new Intent(CourtServiceDetail.this,
                    CourtPOD.class);
            startActivity(submit);
        } else if (v == BtnHistory) {
            new GetHistoryList().execute();
        } else if (v == submitStatus) {

            mDialog = new Dialog(CourtServiceDetail.this);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_status_details);

            dpi = getResources().getDisplayMetrics().density;
            LayoutInflater inflater = (LayoutInflater) mDialog.getContext()
                    .getSystemService(FinalStatus.LAYOUT_INFLATER_SERVICE);
            mannerView = inflater.inflate(R.layout.activity_manner_selection, null);

            mannerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (300 * dpi)));
            popupManner = new PopupWindow(mannerView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    (int) (300 * dpi), true);
            mannerWhhel = (WheelView) mannerView.findViewById(R.id.dp1);

            txt_Dialog_Workorder = (TextView) mDialog.findViewById(R.id.workoder);
            txt_Dialog_Workorder.setText(addresServer.getWorkorder());
            workorder = txt_Dialog_Workorder.getText().toString();
            submit_status = (Button) mDialog.findViewById(R.id.button_submit);
            select_status = (Button) mDialog.findViewById(R.id.select_status);
            Edt_comment = (EditText) mDialog.findViewById(R.id.edt_comment);

            select_status.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {


                    if (statuslist.size() != 0) {

                        dialog();
                    } else {
                        new CustomAlertDialog(
                                CourtServiceDetail.this,
                                "No Status Available, Enter manually")
                                .show();
                    }
                    //	popup();
                }
            });
            submit_status.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    if (Edt_comment.getText().length() == 0) {
                        new CustomAlertDialog(
                                CourtServiceDetail.this,
                                "Report cannot be empty. Either type some text or select one status item then submit")
                                .show();
                    } else {


                        saveValuesInDB(workorder);
//						Intent detailView = new Intent(CourtServiceDetail.this,
//								ListCategory.class);
//						detailView.putExtra("processOrderID", processOrderID);
                        new CustomAlertDialog(CourtServiceDetail.this,
                                "Status value is saved successfully!")
                                .show();
                        mDialog.dismiss();

                    }
                    mDialog.dismiss();
                }
            });

            mDialog.show();

            Window window = mDialog.getWindow();
            window.setLayout(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);

        }
    }

    private void phonedialog() {

        final Dialog dialog = new Dialog(CourtServiceDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.phone_dialog);
        TextView phone = (TextView) dialog.findViewById(R.id.phoneno);
        phone.setText("(" + phone1 + ")" + phone2 + "-" + phone3);

        Button cancel = (Button) dialog.findViewById(R.id.btn_yes);
        Button call = (Button) dialog.findViewById(R.id.btn_no);

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + addresServer
                            .getContactPhone().toString()));
                    startActivity(callIntent);
                } catch (Exception exception) {
                    //Logger.log(exception);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void dialog() {


        final Dialog dialog = new Dialog(CourtServiceDetail.this);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_1);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        TextView txt_Header = (TextView) dialog.findViewById(R.id.txt_header);
        txt_Header.setText("Select Status");

        final ListView list = (ListView) dialog.findViewById(R.id.list);
        final ArrayList<String> array = new ArrayList<>();

        for (int i = 0; statuslist.size() > i; i++) {
            array.add(statuslist.get(i).getReport());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choise_list, array);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        // we want multiple clicks
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(adapter.getPosition(Edt_comment.getText().toString()), true);

        final String[] select = new String[1];
        if (Edt_comment.getText().toString().length() != 0) {
            select[0] = Edt_comment.getText().toString();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select[0] = list.getItemAtPosition(position).toString();
            }
        });
        Button Save = (Button) dialog.findViewById(R.id.save);
        Save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Edt_comment.setText(select[0]);
                dialog.dismiss();
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    public void saveValuesInDB(String wrk_order) {
        SubmitStatusList submitstatusInDB = new SubmitStatusList();
        submitstatusInDB.setWorkorder(wrk_order);
        Log.d("Insert Work", "" + wrk_order);
        int randomPIN = (int) (Math.random() * 999999) + 100000;
        Log.d("RandomNumber:", "" + randomPIN);
        submitstatusInDB.setReport(Edt_comment.getText().toString());

        submitstatusInDB.setServerCode("");
        submitstatusInDB.setLineitem(randomPIN);
        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String date = dateFormatter.format(today);


        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        timeFormatter.setLenient(false);
        Date todayTime = new Date();
        String time = timeFormatter.format(todayTime);


        DateFormat DFormatter = new SimpleDateFormat("yyyy-MM-dd");
        DFormatter.setLenient(false);
        Date Ddate = new Date();
        String ddate = DFormatter.format(Ddate);

        DateFormat TFormatter = new SimpleDateFormat("hh:mm a");
        TFormatter.setLenient(false);
        Date Ttime = new Date();
        String ttime = TFormatter.format(Ttime);


        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String submitDate = dateFormat.format(cal.getTime());

        submitstatusInDB.setStatusTime(time);
        submitstatusInDB.setStatusDate(date);
        submitstatusInDB.setDateTimeSubmitted(submitDate);

        String result = database.check_SubmitStatusTable(workorder);
        if (result.contains("no workorder")) {
            try {
                database.insertsubmitStatusfromServer(submitstatusInDB);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                database.update_SubmitStatusTable(submitstatusInDB);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @SuppressLint("RtlHardcoded")
    public void popup() {
        initWheel1(mannerWhhel);
        txt_popup_cancel = (TextView) mannerView.findViewById(R.id.txt_cancel);
        txt_popup_done = (TextView) mannerView.findViewById(R.id.txt_done);
        popupManner.setAnimationStyle(R.style.Manner_AnimationPopup);
        popupManner.showAtLocation(mannerView, Gravity.LEFT, (int) (10 * dpi),
                (int) (300 * dpi));

        popupManner.update();
        txt_popup_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupManner.dismiss();
            }
        });
        txt_popup_done.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View arg0) {
                updateStatus();
                if (Edt_comment.getText().length() == 0) {
                    Edt_comment.setText(selectedManner.trim());
                } else {
                    Edt_comment.setText(Edt_comment.getText().toString().trim()
                            + ", " + selectedManner.trim());
                }

                popupManner.dismiss();
            }
        });
    }


    @SuppressWarnings("rawtypes")
    private void initWheel1(WheelView wheel) {
        wheel.setViewAdapter(new ArrayWheelAdapter(mDialog.getContext(), statuslist));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(0);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
    }

    public void updateStatus() {
        diligenceCodeForCurrentStatusInSubmitDiligence = getWheelValue(mannerWhhel);
        selectedManner = statuslist.get(getWheelValue(mannerWhhel))
                .getReport();

    }

    @SuppressWarnings("unused")
    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    private int getWheelValue(WheelView wheel) {
        return wheel.getCurrentItem();
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == address) {
            Intent addressOptions = new Intent(CourtServiceDetail.this, AddressOptions.class);
            addressOptions.putExtra("address", addresServer.getAddressFormattedForDisplay());
            addressOptions.putExtra("Latitude", addresServer.getLatitude());
            addressOptions.putExtra("Longitude", addresServer.getLongitude());
            startActivity(addressOptions);
        }
        return false;
    }

    private class MyAstask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(CourtServiceDetail.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String session = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());
                code = WebServiceConsumer.getInstance().getAttachedPDFList(
                        session,
                        workorder);
                SessionData.getInstance().setWorklistid(
                        workorder);
            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
                code = null;
            } catch (Exception e) {
                e.printStackTrace();
                code = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (code != null && code.size() > 0) {
                Log.d("The detail Array Size", "" + code.size());
                SessionData.getInstance().setDetail(code);
                Intent intent = new Intent(CourtServiceDetail.this,
                        ViewPdf.class);
                //	intent.putExtra("processOrderID", processOrderID);
                startActivity(intent);
            } else {
                final Dialog dialog = new Dialog(CourtServiceDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertbox);

                TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                text.setText("No Attachment for this Workorder");

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
            ProgressBar.dismiss();
            super.onPostExecute(result);
        }
    }

    private class GetHistoryList extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            com.tristar.main.ProgressBar.showCommonProgressDialog(CourtServiceDetail.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String sessionId = WebServiceConsumer.getInstance().signOn(
                        TristarConstants.SOAP_ADDRESS,
                        SessionData.getInstance().getUsername(),
                        SessionData.getInstance().getPassword());
                returnHistoryObjects = WebServiceConsumer.getInstance()
                        .returnHistory(
                                sessionId,
                                addresServer.getWorkorder()
                        );

            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
                returnHistoryObjects = null;
            } catch (Exception e) {
                e.printStackTrace();
                returnHistoryObjects = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            com.tristar.main.ProgressBar.dismiss();
            //Log.d("History List", " " + returnHistoryObjects.size());
            if (returnHistoryObjects != null) {
                if (returnHistoryObjects.size() == 0) {

                    final Dialog dialog = new Dialog(CourtServiceDetail.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertbox);

                    TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                    text.setText("No History for this Workorder");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

                    dialogButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();

                    //Toast.makeText(CourtServiceDetail.this,"No History found for this WorkOrder",Toast.LENGTH_LONG).show();
                } else {

                    SessionData.getInstance().setReturnHistoryObjects(returnHistoryObjects);
                    Intent HistoryList = new Intent(CourtServiceDetail.this, History.class);
                    startActivity(HistoryList);

                }
            } else {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertbox);

                TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                text.setText("No History for this Workorder");

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);

                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }

        }
    }

    private class getAddressForMap extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {


                Thread thread = new Thread() {
                    @Override
                    public void run() {

                        double lat = Double.parseDouble(addresServer.getLatitude());
                        double lon = Double.parseDouble(addresServer.getLongitude());
                        List<android.location.Address> addresses = null;
                        android.location.Address Location;
                        if (addresServer.getCustAddressFormattedForGoogle().length() == 0) {
                            return;
                        } else {
                            if (addresServer.getLatitude().equals("0.0")
                                    && addresServer.getLongitude().equals("0.0")) {

                                try {
                                    HttpGet httppost = new HttpGet("http://api.geonames.org/geoCodeAddressJSON?q=" + URLEncoder.encode(addresServer.getCustAddressFormattedForGoogle()) + "&username=tristarsoftware");

                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpResponse response = httpclient.execute(httppost);

                                    int status = response.getStatusLine().getStatusCode();

                                    if (status == 200) {
                                        HttpEntity entity = response.getEntity();
                                        String data = EntityUtils.toString(entity);
                                        Log.d("geonames response :", "addresServer: " + data);
                                        JSONObject jsono = new JSONObject(data);

                                        JSONObject json_LL = jsono.getJSONObject("address");


                                        lat = Double.parseDouble(json_LL.getString("lat"));
                                        lon = Double.parseDouble(json_LL.getString("lng"));

                                    }
                                } catch (IOException e) {

                                    Log.e("Error", "Unable to connect to Geonames", e);
                                } catch (JSONException e) {

                                    System.out.println(" Geonames : Data Not found : " + addresServer.getCustAddressFormattedForGoogle());

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            

//				Geocoder geocoder = new Geocoder(CourtServiceDetail.this);
//				try {
//					addresses = geocoder.getFromLocationName(addresServer.getCustAddressFormattedForGoogle(), 1);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				if (addresses == null) {
//					return;
//				} else {
//					Location = addresses.get(0);
//				}
//				if (Location != null) {
//					lat = Location.getLatitude();
//					lon = Location.getLongitude();
//				} else {
//					return;
//				}

                            if ((lat != 0.0) && (lon != 0.0)) {
                                String uriBegin = "geo:" + lat + "," + lon;
                                String uriString = uriBegin + "?q=" + addresServer.getCustAddressFormattedForGoogle().toString();
                                Uri uri = Uri.parse(uriString);
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            } else {
                                return;
                            }

                        }

                    }
                };
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(Void result) {

        }
    }
}