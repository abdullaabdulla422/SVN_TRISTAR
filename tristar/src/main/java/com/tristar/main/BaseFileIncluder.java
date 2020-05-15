package com.tristar.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tristar.db.DataBaseHelper;
import com.tristar.geo.utils.GPSTracker;
import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("ALL")
public class BaseFileIncluder extends Activity implements OnClickListener {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public final static int FINAL_STATUS = 0;
	public final static int RECORD_DILIGENCE = 1;
	public final static int COURT_POD = 2;
	public final static int PICKUP_POD = 3;
	public final static int DELIVERY_POD = 4;
	public final static int TAKE_PICTURE = 5;
	public final static int SURFACE_CAMERA = 6;
	public final static int CHOOSE_IMAGE = 7;
	public final static int SCANNER = 8;
	public final static int NOT_SCANNER_ANYMORE = 9;
	public static int PARENT_ACTIVITY = -1;
	public static int PROCESS_DETAILSNAIGATION = -2;
	final Context context = this;
	ProcessAddressForServer processOrderToDisplay;
	DataBaseHelper database;
	TextView txtTitleWorkOrder;
	GPSTracker gpstrack;
	Bundle extras;
	Double latitude;
	Double longitude;
	GridView gridImage;
	ArrayList<byte[]> attachedFilesData;
	ArrayList<Integer> removableList = new ArrayList<Integer>();
	private Button btnAttachImage;
	private TextView txt_back_text, txt_done, txt_edit;
	private ImageView deleteImage;
	private int processOrderID;
	private String[] displayInfo = {"Final", "Attempt", "Court", "Pickup", "Delivery", "Back","Back","Back","Back"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachment_attempt);

		attachedFilesData = new ArrayList<byte[]>();
		if (BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.CHOOSE_IMAGE) {
			SessionData.getInstance().setImage(0);
			byte[] byteArray;
			if ((byteArray = SessionData.getInstance().getImageData()) != null) {
				attachedFilesData.add(byteArray);
				SessionData.getInstance().setImageData(null);
				//new CustomAlertDialog(BaseFileIncluder.this, "Current GPS is Attached").show();
			}

		} else {
			SessionData.getInstance().setImage(0);
			SessionData.getInstance().setImageData(null);
		}
		if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.SCANNER) {
			BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.NOT_SCANNER_ANYMORE;
		} else {
			attachedFilesData.addAll(SessionData.getInstance().getAttachedFilesData());
		}
		txtTitleWorkOrder = (TextView) findViewById(R.id.txt_workorder);
		extras = getIntent().getExtras();
		database = DataBaseHelper.getInstance();
		if (extras != null) {
			processOrderID = getIntent().getExtras().getInt("processOrderID");
			try {
				processOrderToDisplay = database
						.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
				txtTitleWorkOrder.setText(processOrderToDisplay.getWorkorder());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		btnAttachImage = (Button) findViewById(R.id.btn_attach_image);
		txt_back_text = (TextView) findViewById(R.id.txt_back_text);
		txt_done = (TextView) findViewById(R.id.txt_done);
		txt_edit = (TextView) findViewById(R.id.txt_edit);
		deleteImage = (ImageView) findViewById(R.id.img_delete_images);
		deleteImage.setVisibility(View.GONE);

		if (PARENT_ACTIVITY != -1) {
			txt_back_text.setText(displayInfo[PARENT_ACTIVITY]);
		} else {
			txt_back_text.setText("Back");
		}

		if (BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.COURT_POD) {
			CourtAddressForServer server = (CourtAddressForServer) CourtService.selectedAddressServer;
			txtTitleWorkOrder.setText(server.getWorkorder());
		} else if (BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.PICKUP_POD
				|| BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.DELIVERY_POD) {
			AddressForServer server = (AddressForServer) CourtService.selectedAddressServer;
			txtTitleWorkOrder.setText(server.getWorkorder());
		} else if (BaseFileIncluder.PROCESS_DETAILSNAIGATION == BaseFileIncluder.TAKE_PICTURE) {
			onClick(btnAttachImage);
		}

		btnAttachImage.setOnClickListener(this);
		txt_back_text.setOnClickListener(this);
		txt_done.setOnClickListener(this);
		txt_edit.setOnClickListener(this);
		deleteImage.setOnClickListener(this);
		gridImage = (GridView) findViewById(R.id.lst_image_container);
		gridImage.setNumColumns(3);
	}

	public void getGps() {
		gpstrack = new GPSTracker(BaseFileIncluder.this);
		if (gpstrack.canGetLocation()) {
			latitude = gpstrack.getLatitude();
			longitude = gpstrack.getLongitude();
		} else {
			gpstrack.showSettingsAlert();
		}
	}

	@Override
	protected void onResume() {
		SessionData.getInstance().setImage(0);
		byte[] byteArray;
		if ((byteArray = SessionData.getInstance().getImageData()) != null) {
			attachedFilesData.add(byteArray);
			SessionData.getInstance().setImageData(null);
			//new CustomAlertDialog(BaseFileIncluder.this, "Current GPS is Attached").show();
		}

		new CustomAlertDialog(BaseFileIncluder.this, "Number of images are " + attachedFilesData.size());
		{
			ImageListAdapter adapter = new ImageListAdapter();
			adapter.notifyDataSetChanged();
			gridImage.setAdapter(adapter);
		}
		super.onResume();
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
			default:
				super.onMenuItemSelected(featureId, item);
				break;
		}
		return true;

	}

	@Override
	public void onBackPressed() {

		//super.onBackPressed();

		if (BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.TAKE_PICTURE) {
			Intent attach = new Intent(BaseFileIncluder.this, ProcessOrderDetail.class);
			attach.putExtra("processOrderID", processOrderID);
			startActivity(attach);
			clearSession();
			finish();
		} else {
			super.onBackPressed();
		}

	}

	public void clearSession() {
		SessionData.getInstance().setProcessOrderDetail_Navigation(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		if (v == btnAttachImage) {
			String ButtonText = txt_edit.getText().toString();
			if (ButtonText.equals("Done")) {
				new CustomAlertDialog(BaseFileIncluder.this, "Please disable edit mode and try to attach images").show();
			} else {
				Intent attach = new Intent(BaseFileIncluder.this,
						RawFileIncluder.class);
				attach.putExtra("processOrderID", processOrderID);
				startActivity(attach);
			}
		} else if (v == txt_back_text) {
			finish();
		} else if (v == txt_done) {
			if (attachedFilesData != null && attachedFilesData.size() != 0) {

				if (BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.TAKE_PICTURE
						|| BaseFileIncluder.PARENT_ACTIVITY == BaseFileIncluder.CHOOSE_IMAGE) {
					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.alert_takepic);

					TextView dialogfinal = (TextView) dialog.findViewById(R.id.btn_final);
					TextView dialogrecord = (TextView) dialog.findViewById(R.id.btn_record);
					dialogfinal.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.FINAL_STATUS;
							SessionData.getInstance().setAttachedFilesData((ArrayList<byte[]>) attachedFilesData.clone());

							SessionData.getInstance().setFinalstatusAttachment(1);
							Intent attach = new Intent(BaseFileIncluder.this,
									FinalStatus.class);
							attach.putExtra("processOrderID", processOrderID);
							startActivity(attach);
							finish();
						}

					});


					dialogrecord.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							BaseFileIncluder.PARENT_ACTIVITY = BaseFileIncluder.RECORD_DILIGENCE;
							SessionData.getInstance().setAttachedFilesData((ArrayList<byte[]>) attachedFilesData.clone());

							SessionData.getInstance().setDiligenceAttachment(1);
							Intent attach = new Intent(BaseFileIncluder.this,
									RecordDiligence.class);
							attach.putExtra("processOrderID", processOrderID);
							startActivity(attach);
							finish();

						}


					});
					dialog.show();
				} else {
					SessionData.getInstance().setAttachedFilesData((ArrayList<byte[]>) attachedFilesData.clone());
					finish();
				}
			} else {
				finish();
			}


		} else if (v == txt_edit) {
			String ButtonText = txt_edit.getText().toString();
			if (ButtonText.equals("Edit")) {
				txt_edit.setText("Done");
				deleteImage.setVisibility(View.VISIBLE);

			} else {
				// code for edit
				txt_edit.setText("Edit");
				deleteImage.setVisibility(View.GONE);
				ImageListAdapter adapter = new ImageListAdapter();
				adapter.notifyDataSetChanged();
				gridImage.setAdapter(adapter);
			}
		} else if (v == deleteImage) {
			if (attachedFilesData == null || attachedFilesData.isEmpty()) {
				new CustomAlertDialog(BaseFileIncluder.this, "No images are attached. Please attach atleast one image!").show();
			} else {

				if (removableList.isEmpty()) {
					new CustomAlertDialog(BaseFileIncluder.this, "No images are selected to delete. Please select atleast one image!").show();
				} else {
					new CustomAlertDialog(BaseFileIncluder.this, "Are you sure you want to delete the selected images", true).show();
				}
			}
		}

	}

	public void deleteImages() {
		try {
			int i = removableList.size();
			Collections.sort(removableList);
			Collections.reverse(removableList);

			for (Integer viewId : removableList) {
				attachedFilesData.remove(viewId.intValue());
			}
			removableList.clear();
			ImageListAdapter adapter = new ImageListAdapter();
			adapter.notifyDataSetChanged();
			gridImage.setAdapter(adapter);
			new CustomAlertDialog(BaseFileIncluder.this, i + " image(s) was deleted from attachments").show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ImageListAdapter extends BaseAdapter {

		ArrayList<byte[]> list;
		LayoutInflater inflator;
		int size = 0;

		public ImageListAdapter() {
			removableList = new ArrayList<Integer>();
			list = attachedFilesData;
			size = list.size();
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View currentView, ViewGroup parent) {
			@SuppressLint("InflateParams") final Holder holder;
			if (currentView == null) {
				holder = new Holder();
				currentView = inflator.inflate(R.layout.sub_activity_image, null);
				holder.img_camera = (ImageView) currentView.findViewById(R.id.img_camera);
				holder.selector = (ImageView) currentView.findViewById(R.id.img_selector);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				byte[] data = list.get(position);
				Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
						data.length, options);
				holder.img_camera.setImageBitmap(bmp);
				currentView.setTag(holder);
			} else {
				holder = (Holder) currentView.getTag();
			}

			holder.img_camera.setTag(position);

			holder.img_camera.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (txt_edit.getText().toString().equals("Done")) {

						if (holder.selector.getVisibility() == View.VISIBLE) {
							holder.selector.setTag(position);

							holder.selector.setVisibility(View.GONE);
							removableList.remove((Object) position);
						} else {
							holder.selector.setVisibility(View.VISIBLE);
							holder.selector.setTag(position);

							removableList.add(position);
						}
					}
				}
			});

			return currentView;
		}

		private class Holder {
			ImageView selector, img_camera;
		}
	}


}