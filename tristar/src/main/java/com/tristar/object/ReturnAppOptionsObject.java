package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class ReturnAppOptionsObject extends SoapUtils {
	public String TAG = ReturnAppOptionsObject.class.getSimpleName();
	
	public static String TAG_ShowPhysicalDescription = "ShowPhysicalDescription";
	public static String TAG_UseJobTracking = "UseJobTracking";
	
	private boolean ShowPhysicalDescription;
	private boolean UseJobTracking;
	
	public boolean isShowPhysicalDescription() {
		return ShowPhysicalDescription;
	}
	
	public void setShowPhysicalDescription(boolean showPhysicalDescription) {
		ShowPhysicalDescription = showPhysicalDescription;
	}
	
	public boolean isUseJobTracking() {
		return UseJobTracking;
	}
	
	public void setUseJobTracking(boolean useJobTracking) {
		UseJobTracking = useJobTracking;
	}
	
	public static ReturnAppOptionsObject parseObject(SoapObject soapObject) {
		ReturnAppOptionsObject object = new ReturnAppOptionsObject();
		object.setShowPhysicalDescription(getPropertyAsBoolean(soapObject, TAG_ShowPhysicalDescription));
		object.setUseJobTracking(getPropertyAsBoolean(soapObject, TAG_UseJobTracking));
		return object;
	}
}
