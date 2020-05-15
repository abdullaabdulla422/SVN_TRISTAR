package com.tristar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tristar.db.SyncronizeClass;
import com.tristar.main.BaseFileIncluder;
import com.tristar.main.MainActivity;
import com.tristar.main.R;

@SuppressWarnings("ALL")
public class CustomAlertDialog extends Dialog implements
		android.view.View.OnClickListener {
	public boolean isConfirmDialog = false;
	public final Activity parentActivity;
	public Dialog d;
	public Button yes, no;
	public final String message;
	private Intent intent;
	public final static int SYNC = 101;
	int sync = -1;

	public CustomAlertDialog(Activity a, String message) {
		super(a);
		this.parentActivity = a;
		this.message = message;
		isConfirmDialog = false;
	}

	public CustomAlertDialog(Activity a, String message, int sync) {
		super(a);
		this.parentActivity = a;
		this.message = message;
		//isConfirmDialog = false;
		isConfirmDialog = true;
		this.sync = sync;
	}

	public CustomAlertDialog(Activity activity, String message,
							 boolean confirmDialog, int sync) {
		super(activity);
		this.parentActivity = activity;
		this.message = message;
		this.isConfirmDialog = true;

		this.sync = sync;
	}

	public CustomAlertDialog(Activity a, String message, Intent intent) {
		super(a);
		this.parentActivity = a;
		this.message = message;
		isConfirmDialog = false;
		this.intent = intent;
	}

	public CustomAlertDialog(Activity activity, String message,
							 boolean confirmDialog) {
		super(activity);
		this.parentActivity = activity;
		this.message = message;
		this.isConfirmDialog = confirmDialog;
	}

	public CustomAlertDialog(Activity activity, String message,
							 boolean confirmDialog, Intent intent) {
		super(activity);
		this.parentActivity = activity;
		this.message = message;
		this.isConfirmDialog = confirmDialog;
		this.intent = intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alertdialog);
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		if (!isConfirmDialog) {
			yes.setVisibility(View.GONE);
			findViewById(R.id.btn_devider).setVisibility(View.GONE);
			no.setText("OK");
		}else {
			yes.setVisibility(View.VISIBLE);
			findViewById(R.id.btn_devider).setVisibility(View.VISIBLE);
			no.setVisibility(View.VISIBLE);
			no.setText("Cancel");
			yes.setText("Ok");
		}
		((TextView) findViewById(R.id.txt_dia)).setText(message);
		no.setOnClickListener(this);
		yes.setOnClickListener(this);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_no:
				dismiss();
				if (sync == SYNC && no.getText().toString().equals("OK")) {
					SyncronizeClass.instance().syncronizeMainMethod(parentActivity);
				}
				else if (sync == SYNC && no.getText().toString().equals("Cancel")) {

				}
				else if (intent != null && !isConfirmDialog) {
					parentActivity.finish();
					parentActivity.startActivity(intent);
				}
				break;
			case R.id.btn_yes:
				dismiss();
				if (sync == SYNC) {
					if (message.equals(parentActivity.getResources().getString(
							R.string.alert_logout))) {
						MainActivity.setLogoutStatus(
								MainActivity.sharedPreferences, true);
					}
					SyncronizeClass.instance().syncronizeMainMethod(parentActivity);

				} else if (parentActivity instanceof BaseFileIncluder) {
					((BaseFileIncluder) parentActivity).deleteImages();
				}

				else if (intent != null) {
					parentActivity.finish();
					parentActivity.startActivity(intent);
				}

				break;
			default:
				break;
		}
		dismiss();
	}

}