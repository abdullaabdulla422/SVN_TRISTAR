package com.tristar.object;

import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-012 on 23/12/16.
 */
@SuppressWarnings("ALL")
public class ReturnStatusListObect extends SoapUtils {
    public String TAG = ReturnStatusListObect.class.getSimpleName();
    public static String TAG_Report = "Report";

    private String Report;

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public static ReturnStatusListObect parseObject(SoapObject soapObject) {
        ReturnStatusListObect object = new ReturnStatusListObect();
        object.setReport(getProperty(soapObject, TAG_Report));

        return object;
    }


}
