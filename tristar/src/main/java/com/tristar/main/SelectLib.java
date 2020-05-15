package com.tristar.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressWarnings("ALL")
public class SelectLib  extends Activity {
	Button btn_cancel;
	protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.select_from_lib);
    getActionBar().setDisplayShowTitleEnabled(false);
    btn_cancel = (Button)findViewById(R.id.btn_cancelcamera);
    btn_cancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
		finish();
		}
	});

	}
}
