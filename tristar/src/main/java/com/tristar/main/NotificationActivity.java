package com.tristar.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tristar.object.ReturnMessagesObjects;
import com.tristar.utils.SessionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NotificationActivity extends Activity implements View.OnClickListener{

    ArrayList<ReturnMessagesObjects> returnMessagesObjectses;
    ListView list;
    TextView txtBack;
    ImageView imgBack;
    ListAdapter Adap_History;

    public static SharedPreferences notificationHistory;
    public static SharedPreferences.Editor notificationHistoryPrefsEditors;
    String shared_Message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        list = (ListView)findViewById(R.id.listView_notification);
        txtBack = (TextView) findViewById(R.id.textviewback);
        imgBack = (ImageView) findViewById(R.id.imageButtonback);
        returnMessagesObjectses = SessionData.getInstance().getReturnMessagesObjectses();
        Adap_History = new ListAdapter(this,returnMessagesObjectses);
        list.setAdapter(Adap_History);
        notificationHistory = getSharedPreferences("notificationhistory", MODE_PRIVATE);
        notificationHistoryPrefsEditors = notificationHistory.edit();
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < returnMessagesObjectses.size(); i ++){

            t.append(returnMessagesObjectses.get(i).getMessage()+" ,");
        }

        shared_Message = t.toString();
        Log.d("Messages",""+ shared_Message);
        notificationHistoryPrefsEditors.putString("message",shared_Message);
        notificationHistoryPrefsEditors.commit();

        shared_Message = notificationHistory.getString("message","");

        txtBack.setOnClickListener(this);
        imgBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == txtBack){
            onBackPressed();
        }else if(v == imgBack){
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent processdetail = new Intent(NotificationActivity.this,
                ListCategory.class);
        startActivity(processdetail);
        finish();
    }


    private class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<ReturnMessagesObjects> List;

        public ListAdapter(Context context, ArrayList<ReturnMessagesObjects> List) {
            this.context = context;
            this.List = List;
        }



        @Override
        public int getCount() {
            return List.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReturnMessagesObjects group = List.get(position);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.notifications_row, null);
            }

            TextView txt_serverCode = (TextView) convertView.findViewById(R.id.txt_servercode);
            TextView txt_message = (TextView) convertView.findViewById(R.id.txt_report);
            TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            TextView txt_time = (TextView) convertView.findViewById(R.id.txt_time);

//            txt_detail1.setText(group.getWorkorder());
            txt_message.setText(group.getMessage());


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObject = null;
            try {
                dateObject = dateFormat.parse(group.getDateStamp());
            } catch(Exception e) {
                dateObject = new Date();
            }
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            txt_date.setText( dateFormat.format(dateObject));



            txt_serverCode.setText(group.getServerCode());
            String date = group.getReadDateTime().substring(11);
            txt_time.setText(date);

//            txt_detail2.setText(group.get);
//            txt_detail3.setText(group.getDetail3());
//            txt_detail4.setText(group.getDetail4());

//            Log.d("Parent Postion",""+groupPosition);

            return convertView;
        }


    }

}
