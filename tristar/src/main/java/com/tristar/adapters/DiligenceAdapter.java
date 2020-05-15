package com.tristar.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tristar.main.R;
import com.tristar.object.DiligenceForProcess;
import com.tristar.object.SubmitDiligence;
import com.tristar.utils.DateUtils;
import com.tristar.utils.SessionData;

@SuppressWarnings("ALL")
public class DiligenceAdapter extends BaseAdapter implements OnItemClickListener{
	private Activity activity;
	private ArrayList<?> list;
	private static LayoutInflater inflater = null;
	public Resources res;
	int processId;
	int i = 0;

	@SuppressLint("LongLogTag")
	public DiligenceAdapter(Activity a, ArrayList<?> list, int processId) {

		activity = a;
		this.list = list;
		this.processId = processId;
		inflater = (LayoutInflater) activity
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("Process Order List size = ","" + getCount());
	}

	public int getCount() {

		if (list.size() <= 0)
			return 1;
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView text;
		public TextView text1;
		public TextView text2;
		public TextView text3;
		public TextView textAddressLine;
		public TextView textDate;
		public TextView textTime;
		public TextView textReport;
		public TextView textWide;

	}

	@SuppressLint({"InflateParams", "SetTextI18n"})
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.previous_diligence_items, null);

			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.txt_adresline);
			holder.text1 = (TextView) vi.findViewById(R.id.txt_date);
			holder.text2 = (TextView) vi.findViewById(R.id.txt_time);
			holder.text3 = (TextView) vi.findViewById(R.id.txt_report);
			holder.textAddressLine = (TextView) vi.findViewById(R.id.Previous_diligences_adrsline);
			holder.textDate = (TextView) vi.findViewById(R.id.Previous_diligences_date);
			holder.textTime = (TextView) vi.findViewById(R.id.Previous_diligences_time);
			holder.textReport = (TextView) vi.findViewById(R.id.Previous_diligences_report);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (list.size() <= 0) {
			
			
		} else if(list.get(position) instanceof SubmitDiligence) {
			SubmitDiligence submitDilignec = (SubmitDiligence) list.get(position);
			holder.textAddressLine.setText("Address Line Item:");
			holder.textDate.setText("Date:");
			holder.textTime.setText("Time:");
			holder.textReport.setText("Report:");
			holder.text.setText(submitDilignec.getAddressLineItem()+"");
			holder.text1.setText(DateUtils.getDisplayDate(submitDilignec.getDiligenceDate()));
			holder.text2.setText(submitDilignec.getDiligenceTime().length() >10 ? submitDilignec.getDiligenceTime().substring(11,19) : submitDilignec.getDiligenceTime());
			holder.text3.setText(submitDilignec.getReport());
			
		}
		else if(list.get(position) instanceof DiligenceForProcess){
			
			DiligenceForProcess object = (DiligenceForProcess) list.get(position);
			holder.textAddressLine.setText("Address:");
			holder.textDate.setText("Date:");
			holder.textTime.setText("Time:");
			holder.textReport.setText("Report:");
			holder.text.setText(SessionData.getInstance().getPreviousdiligenceAddress());
			holder.text1.setText(DateUtils.getDisplayDate(object.getDiligenceDate()));
			holder.text2.setText(object.getDiligenceTime().length() >10 ? object.getDiligenceTime().substring(11,19) : object.getDiligenceTime());
			holder.text3.setText(object.getReport());
	
		}
		return vi;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}



}