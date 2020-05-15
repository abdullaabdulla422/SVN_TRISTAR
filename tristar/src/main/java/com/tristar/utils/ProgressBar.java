package com.tristar.utils;

import com.tristar.main.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class ProgressBar {
	public static Dialog commonProgressDialog;
	public static void showCommonProgressDialog(Activity activity, String message) {
		commonProgressDialog = new Dialog(activity);
		commonProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		commonProgressDialog.setContentView(R.layout.activity_progress_bar);
		((TextView)commonProgressDialog.findViewById(R.id.progress_message)).setText(message);
		commonProgressDialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);
		commonProgressDialog.setCanceledOnTouchOutside(false);
		commonProgressDialog.setCancelable(false);	
		commonProgressDialog.show();
	}

	public static void dismiss() {
		if(commonProgressDialog != null) {
			commonProgressDialog.dismiss();
		}
	}
}
