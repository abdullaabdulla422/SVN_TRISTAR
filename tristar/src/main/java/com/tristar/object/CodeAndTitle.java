package com.tristar.object;
import org.ksoap2.serialization.SoapObject;

import com.tristar.webutils.SoapUtils;
@SuppressWarnings("ALL")
public class CodeAndTitle extends SoapUtils {
	public static String TAG = CodeAndTitle.class.getSimpleName();
	public static String TAG_Code = "Code";
	
	public static String TAG_Title = "Title";
private String title;
private String code;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public static CodeAndTitle parseObject(SoapObject soapObject)
		throws Exception {
 CodeAndTitle object = new CodeAndTitle();
	object.setCode(getProperty(soapObject, TAG_Code));
	object.setTitle(getProperty(soapObject, TAG_Title));
	return object;
}
	
}
