
package com.tristar.wheelpicker;

import android.content.Context;


@SuppressWarnings("ALL")
public class NumericWheelAdapter extends AbstractWheelTextAdapter {

    public static final int DEFAULT_MAX_VALUE = 9;

    private static final int DEFAULT_MIN_VALUE = 0;

    private final int minValue;
    private final int maxValue;

    private final String format;


    public NumericWheelAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }


    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }


    public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        super(context);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            return format != null ? String.format(format, value) : Integer.toString(value);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }
}
