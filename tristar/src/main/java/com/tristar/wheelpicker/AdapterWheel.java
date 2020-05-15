package com.tristar.wheelpicker;

import android.content.Context;

@SuppressWarnings("ALL")
public class AdapterWheel extends AbstractWheelTextAdapter {

	private WheelAdapter adapter;

	public AdapterWheel(Context context, WheelAdapter adapter) {
		super(context);

		this.adapter = adapter;
	}

	public WheelAdapter getAdapter() {
		return adapter;
	}

	@Override
	public int getItemsCount() {
		return adapter.getItemsCount();
	}

	@Override
	protected CharSequence getItemText(int index) {
		return adapter.getItem(index);
	}

}
