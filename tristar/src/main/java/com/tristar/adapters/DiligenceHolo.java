package com.tristar.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tristar.db.DataBaseHelper;
import com.tristar.main.R;
import com.tristar.main.RecordDiligence;
import com.tristar.object.DiligencePhrase;
import com.tristar.wheelpicker.AbstractWheelTextAdapter;
import com.tristar.wheelpicker.OnWheelChangedListener;
import com.tristar.wheelpicker.OnWheelScrollListener;
import com.tristar.wheelpicker.WheelView;

@SuppressWarnings("ALL")
public class DiligenceHolo extends Activity {
	DataBaseHelper database;
	ArrayList<DiligencePhrase> diligencesPhrasesArray ;
	String wheelMenu1[] = new String[] { "Bars on Windows", "Blinds Closed",
			"Blinds Open", "Docs In Yard", "NO Change", "No Lights",
			"No Answer", "No Movements" };
	public static String strselected_txt3;
	private boolean wheelScrolled = false;
	Button btn, cancel;
	@SuppressWarnings("unused")
	private TextView text;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holo_picker); 
		database = DataBaseHelper.getInstance();
		
		diligencesPhrasesArray = database.getStatusValuesFromDBToDisplayIndiligencesView();
		btn = (Button) findViewById(R.id.changeset);
		cancel = (Button) findViewById(R.id.changecancel);
		text = (TextView) this.findViewById(R.id.result4);
		final WheelView itemchange = (WheelView) findViewById(R.id.wheel);
		itemchange.setVisibleItems(5); // Number of items
		itemchange.setWheelBackground(R.drawable.wheel_bg_holo);
		itemchange.setWheelForeground(R.drawable.wheel_val_holo);
		itemchange.setShadowColor(0xFF000000, 0x88000000, 0x00000000);
		itemchange.setViewAdapter(new ChangeAdapter(this));
		itemchange.setCurrentItem(3);
		itemchange.addChangingListener(changedListener1);
		itemchange.addScrollingListener(scrolledListener);
	}

	public void loadAllStatusForPickerFromDB() {
		diligencesPhrasesArray = database.getStatusValuesFromDBToDisplayIndiligencesView();
	}
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {

		}
	};
	final OnWheelChangedListener changedListener1 = new OnWheelChangedListener() {
		public void onChanged() {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};

	

	protected void updateStatus() {
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				strselected_txt3 = wheelMenu1[getWheel(R.id.wheel)
						.getCurrentItem()];

			
				RecordDiligence.count3++;
				finish();
			}
		});

	}

	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}
}

@SuppressWarnings("ALL")
class ChangeAdapter extends AbstractWheelTextAdapter {
	final String itemschange[] = new String[] { "Bars on Windows",
			"Blinds Closed", "Blinds Open", "Docs In Yard", "NO Change",
			"No Lights", "No Answer", "No Movements" };

	
	protected ChangeAdapter(Context context) {
		super(context, R.layout.changeholoitems);

		setItemTextResource();
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		return super.getItem(index, cachedView, parent);
	}

	@Override
	public int getItemsCount() {
		return itemschange.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return itemschange[index];
	}
}