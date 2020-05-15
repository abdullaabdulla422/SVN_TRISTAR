package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class SampleObject extends SoapUtils {
	public static String TAG = SampleObject.class.getSimpleName();

	public static SampleObject parseObject(SoapObject soapObject) {
		// TODO soap parsing
		return new SampleObject();
	}

}
