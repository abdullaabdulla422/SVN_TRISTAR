package com.tristar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tristar.main.R;
import com.tristar.utils.SessionData;

import java.util.ArrayList;

public class MilestoneFilter_Adapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    String filter_by = "";
    ListInterFace listInterFace;

    public MilestoneFilter_Adapter(Context context, ArrayList<String> list, ListInterFace listInterFace) {
        this.context = context;
        this.list = list;
        this.listInterFace = listInterFace;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.child_milestone, null);

        TextView milestone_txt_filter = (TextView) view.findViewById(R.id.milestone_txt_filter);
        final ImageView right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
        milestone_txt_filter.setText(list.get(i));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionData.getInstance().setFilter_arrow_visible(i);
                filter_by = list.get(i);
                listInterFace.OnClick(i, filter_by);
                notifyDataSetChanged();

            }
        });

       right_arrow.setVisibility(SessionData.getInstance().getFilter_arrow_visible() == i ? View.VISIBLE : View.INVISIBLE);
       view.setBackgroundColor(SessionData.getInstance().getFilter_arrow_visible() == i ? Color.parseColor("#EDEDED") : Color.parseColor("#FFFFFF"));

        return view;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public interface ListInterFace
    {
        void OnClick( int pos , String filter_by);
    }

}
