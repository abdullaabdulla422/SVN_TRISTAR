package com.tristar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

@SuppressWarnings("ALL")
public class DecimalDigitsInputFilter implements InputFilter {

Pattern mPattern;

public DecimalDigitsInputFilter(int digitsBeforeZero) {
    mPattern=Pattern.compile("[0-9]{0," + (10 -1) + "}+((\\.[0-9]{0," + (2 -1) + "})?)||(\\.)?");
}

@Override
public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);       
        if(!matcher.matches())
            return "";
        return null;
    }

}