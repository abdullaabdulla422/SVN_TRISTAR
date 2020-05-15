package com.tristar.main;

import android.app.Activity;
import android.os.Bundle;

public class Exiter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		finish();
	
		System.exit(0); 
		
	}

}
   