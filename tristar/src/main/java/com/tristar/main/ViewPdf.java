package com.tristar.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.tristar.object.CodeAndTitle;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.SessionData;
import com.tristar.utils.TristarConstants;
import com.tristar.webutils.WebServiceConsumer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView; 
import android.widget.TextView;

@SuppressWarnings("ALL")
public class ViewPdf extends FragmentActivity implements OnClickListener {
	ListView listViewProcessOrder;
	ArrayList<CodeAndTitle> CodeandTitlelist;
	//ProcessAddressForServer processOrderToDisplayInDetailView;
	String Texttitle;
	private int count;
	public ImageAdapter adap;
	
	int lineitem;   
	TextView Lblitem1cs, txt_worker, back;
	ImageView imageButtonback;
	private boolean[] thumbnailsselection;

	int processOrderID;
	String mapSouce;
	Bundle extra;  
	String pdfdownload;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdf_list);
//		extra = getIntent().getExtras();
//
//		if (extra != null) {
//			processOrderID = extra.getInt("processOrderID");
//
//		}
	//	processOrderToDisplayInDetailView = new ProcessAddressForServer();
		listViewProcessOrder = (ListView) findViewById(R.id.listViewpdf);
		listViewProcessOrder.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Texttitle = CodeandTitlelist.get(position).getTitle();
				String item = CodeandTitlelist.get(position).getCode();
				lineitem = Integer.parseInt(item);
				new PDFdown().execute();
			}
		});

		imageButtonback = (ImageView) findViewById(R.id.imageButtonback);
		back = (TextView) findViewById(R.id.txt_backfinalstatus);
		CodeandTitlelist = new ArrayList<CodeAndTitle>();
		CodeandTitlelist = SessionData.getInstance().getDetail();
		Log.d("The Array Size", "" + CodeandTitlelist.size());
		listViewProcessOrder
				.setAdapter(new ImageAdapter(this, CodeandTitlelist));
		back.setOnClickListener(this);
		imageButtonback.setOnClickListener(this);

	}

	private class PDFdown extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {

			ProgressBar.showCommonProgressDialog(ViewPdf.this);

		}


		@Override
		protected Void doInBackground(Void... params) {
			try {
				String workorder = SessionData.getInstance().getWorklistid();
				String sessionID = WebServiceConsumer.getInstance().signOn(
						TristarConstants.SOAP_ADDRESS,
						SessionData.getInstance().getUsername(),
						SessionData.getInstance().getPassword());

				pdfdownload = WebServiceConsumer.getInstance().getAttachedPDF(
						sessionID, workorder, lineitem);
				String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
				path = path +"/WinServe_Data/PDF";
				final File dwldsPath = new File(path + "/" +Texttitle);
				new File(path).mkdirs();
				byte[] pdfAsBytes = Base64.decode(pdfdownload, 0);
				FileOutputStream os;
				os = new FileOutputStream(dwldsPath, false);
				os.write(pdfAsBytes);  
				os.flush();   
				os.close();
				 Uri paths = Uri.fromFile(dwldsPath);
			        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
			        pdfIntent.setDataAndType(paths, "application/pdf");
			        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(pdfIntent);
			} catch (java.net.SocketTimeoutException e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void reuslt) {
         ProgressBar.dismiss();
			super.onPostExecute(reuslt);

		}

	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private ArrayList<CodeAndTitle> list;

		public ImageAdapter(Context context, ArrayList<CodeAndTitle> list) {
			mContext = context;
			this.list = list;
			list = null;

		}

		@SuppressLint("LongLogTag")
		@Override
		public int getCount() {
			list = SessionData.getInstance().getDetail();
			Log.d("The Checklist Array Size", "" + list.size());
			return list.size();

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_pdf_items, null);
				holder.txttitile = (TextView) convertView
						.findViewById(R.id.tit);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txttitile.setText(CodeandTitlelist.get(position).getTitle());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	class ViewHolder {
		TextView  txttitile;

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
//		Intent intent = new Intent(ViewPdf.this, ProcessOrderDetail.class);
//		intent.putExtra("processOrderID", processOrderID);
//		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == back || v == imageButtonback) {
			finish();
//			Intent attach_white = new Intent(ViewPdf.this, ProcessOrderDetail.class);
//			attach_white.putExtra("processOrderID", processOrderID);
//			startActivity(attach_white);


		}

	}

}