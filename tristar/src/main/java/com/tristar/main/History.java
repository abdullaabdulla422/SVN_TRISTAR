package com.tristar.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tristar.object.ReturnHistoryObject;
import com.tristar.utils.SessionData;

import java.util.ArrayList;



public class History extends Activity implements View.OnClickListener{

    ImageView Img_Buttonback;
    TextView txt_viewback;
    ExpandableListView listView_history;

    ArrayList<
            Listdata> History_data = new ArrayList<>();
    
    ExpandListAdapter Adap_History;

    ArrayList<ReturnHistoryObject> returnHistoryObjects;
    TextView txt_WorkOrder;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.history);
        txt_viewback = (TextView) findViewById(R.id.textviewback);
        Img_Buttonback = (ImageView) findViewById(R.id.imageButtonback);
        listView_history = (ExpandableListView) findViewById(R.id.listView_history);

        returnHistoryObjects = SessionData.getInstance().getReturnHistoryObjects();


        Img_Buttonback.setOnClickListener(this);
        txt_viewback.setOnClickListener(this);
        txt_WorkOrder = (TextView) findViewById(R.id.txt_Historyworkorder);

        if (returnHistoryObjects != null && returnHistoryObjects.size() !=0) {
            txt_WorkOrder.setText(returnHistoryObjects.get(0).getWorkorder());
            Adap_History = new ExpandListAdapter (History.this,returnHistoryObjects);
            listView_history.setAdapter(Adap_History);
        }else {
            Log.d("History = " , "" +returnHistoryObjects.size());

        }
        listView_history.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        listView_history.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == Img_Buttonback){
            onBackPressed();
        }else if(v == txt_viewback){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class ExpandListAdapter extends BaseExpandableListAdapter {

        Context context;
        ArrayList<ReturnHistoryObject> List;

        public ExpandListAdapter(Context context, ArrayList<ReturnHistoryObject> List) {
            this.context = context;
            this.List = List;
        }

        @Override
        public int getGroupCount() {
            return List.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return List.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){

            ReturnHistoryObject group = (ReturnHistoryObject) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.group_item, null);
            }

//            TextView txt_detail1 = (TextView) convertView.findViewById(R.id.textview1);
            TextView txt_report = (TextView) convertView.findViewById(R.id.txt_report);
            TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            TextView txt_time = (TextView) convertView.findViewById(R.id.txt_time);

//            txt_detail1.setText(group.getWorkorder());
            txt_report.setText(group.getReport());
            txt_date.setText(group.getEntryDate());


            String date = group.getEntryTime().substring(11);
            txt_time.setText(date);

//            txt_detail2.setText(group.get);
//            txt_detail3.setText(group.getDetail3());
//            txt_detail4.setText(group.getDetail4());

//            Log.d("Parent Postion",""+groupPosition);

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.child_item, null);
//
//                Button btn_Del = (Button) convertView.findViewById(R.id.child_del);
//                Button btn_Edit = (Button) convertView.findViewById(R.id.child_edit);
//
//                btn_Edit.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//
//                        Log.d("Group Positin",""+groupPosition);
//                        Log.d("Child Position",""+ childPosition);
//
//                        Intent navInt = new Intent(History.this,HistoryDetails.class);
//                        startActivity(navInt);
//                    }
//                });
//
//                btn_Del.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        final Dialog dialog = new Dialog(context);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setContentView(R.layout.alert_history);
//
//                        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
//                        text.setText("Do you want to remove this record?");
//
//                        TextView txt_yes = (TextView) dialog.findViewById(R.id.btn_yes);
//                        TextView txt_no = (TextView) dialog.findViewById(R.id.btn_no);
//
//                        txt_yes.setOnClickListener(new View.OnClickListener(){
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//
//
//                            }
//                        });
//                        txt_no.setOnClickListener(new View.OnClickListener(){
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//
//                            }
//                        });
//
//                        dialog.show();
//
//                    }
//                });

//                Log.d("Child Postion",""+childPosition);

            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
