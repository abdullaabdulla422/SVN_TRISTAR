
package com.tristar.wheelpicker;

import java.util.List;

import com.tristar.object.DiligencePhrase;

import android.content.Context;


 
@SuppressWarnings("ALL")
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

	public T items[];
	public List listitems;

	
	public ArrayWheelAdapter(Context context, T items[]) {
		super(context);
		this.items = items;
	}

	public ArrayWheelAdapter(Context context, List list) {
		super(context);
		this.listitems = list;
	}

	@Override
	public CharSequence getItemText(int index) {

		if (index >= 0 && index < listitems.size()) {
			Object obj = listitems.get(index);
			if(obj instanceof String) {
				return obj.toString();
			} else if(obj instanceof DiligencePhrase)
			{
				DiligencePhrase item = (DiligencePhrase) obj;
				return item.getTitle();
			}
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return listitems.size();
	}
}
