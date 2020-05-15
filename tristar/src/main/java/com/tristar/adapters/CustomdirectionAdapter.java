package com.tristar.adapters;

import java.util.ArrayList;

import com.tristar.main.ProcessOrder;
import com.tristar.main.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class CustomdirectionAdapter extends BaseAdapter{
    ArrayList<String> result;
    Context context;

      private static LayoutInflater inflater=null;
    public CustomdirectionAdapter(ProcessOrder mainActivity, ArrayList<String> prgmNameList) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
       
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.distance_list_row, null);
             holder.tv=(TextView) rowView.findViewById(R.id.direction_detail);
             holder.img=(ImageView) rowView.findViewById(R.id.direction_image);       
         holder.tv.setText(result.get(position));
         if(result.get(position).contains("Destination")){
        	 holder.img.setImageResource(R.drawable.endpin);  
         }
         else if(result.get(position).contains("left"))
         {
        	 holder.img.setImageResource(R.drawable.turnleft);
         }
         else if(result.get(position).contains("right"))
         {
        	 holder.img.setImageResource(R.drawable.turnright);
         }else if(result.get(position).contains("exit"))          
         {
        	 holder.img.setImageResource(R.drawable.exit);
         }
         else if(result.get(position).contains("straight"))
         {
        	 holder.img.setImageResource(R.drawable.straight);
         }
         else if(position == 0){
        	 holder.img.setImageResource(R.drawable.startpin);
         }
         
         
//         holder.img.setImageResource(R.drawable.startpin);
//         holder.img.setImageResource(R.drawable.endpin);
//         holder.img.setImageResource(R.drawable.exit);
         
         
        return rowView;
    }

} 