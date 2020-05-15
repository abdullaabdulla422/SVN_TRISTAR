package com.tristar.object;

import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;

@SuppressWarnings("ALL")
public class Tristar2 extends SoapUtils {
	public String TAG = AddProcessAddress.class.getSimpleName();
	public static String TAG_special = "GetSpecialInstructionsResult";
	
	private String getSpecialInstruction;
	
	
	public String getGetSpecialInstruction() {
		return getSpecialInstruction;
	}

	public void setGetSpecialInstruction(String getSpecialInstruction) {
		this.getSpecialInstruction = getSpecialInstruction;
	}

public static Tristar2 parseObject(SoapObject property) {
		
		Tristar2 object = new Tristar2();
		object.setGetSpecialInstruction(getProperty(property, TAG_special));
		// TODO Auto-generated method stub
		return object;
	}

}
