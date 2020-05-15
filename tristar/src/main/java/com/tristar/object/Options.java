package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class Options extends SoapUtils {
	public static String TAG = Options.class.getSimpleName();

	public static String TAG_Comment = "Comment";
	public static String TAG_AppliesToSpecialsOnly = "AppliesToSpecialsOnly";
	public static String TAG_AppliesToServerCheckIn = "AppliesToServerCheckIn";

	private boolean comment;
	private boolean appliesToSpecialsOnly;
	private boolean appliesToServerCheckIn;

	public boolean isAppliesToServerCheckIn() {
		return appliesToServerCheckIn;
	}

	public boolean isAppliesToSpecialsOnly() {
		return appliesToSpecialsOnly;
	}

	public boolean isComment() {
		return comment;
	}

	public void setAppliesToServerCheckIn(boolean appliesToServerCheckIn) {
		this.appliesToServerCheckIn = appliesToServerCheckIn;
	}

	public void setAppliesToSpecialsOnly(boolean appliesToSpecialsOnly) {
		this.appliesToSpecialsOnly = appliesToSpecialsOnly;
	}

	public void setComment(boolean comment) {
		this.comment = comment;
	}

	public static Options parseObject(SoapObject soapObject) throws Exception {
		Options object = new Options();
		object.setAppliesToServerCheckIn(getPropertyAsBoolean(soapObject,
				TAG_AppliesToServerCheckIn));
		object.setAppliesToServerCheckIn(getPropertyAsBoolean(soapObject,
				TAG_AppliesToSpecialsOnly));
		object.setComment(getPropertyAsBoolean(soapObject, TAG_Comment));
		return object;
	}

}
