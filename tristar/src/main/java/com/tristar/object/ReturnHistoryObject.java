package com.tristar.object;

import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-017 on 11/10/17.
 */

public class ReturnHistoryObject extends SoapUtils {
    public String TAG = ReturnHistoryObject.class.getSimpleName();


    public static String TAG_Workorder = "Workorder";
    public static String TAG_EntryDate = "EntryDate";
    public static String TAG_EntryTime = "EntryTime";
    public static String TAG_Report = "Report";


    private String Workorder;
    private String EntryDate;
    private String EntryTime;
    private String Report;


    public String getWorkorder() {
        return Workorder;
    }

    public void setWorkorder(String workorder) {
        Workorder = workorder;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getEntryTime() {
        return EntryTime;
    }

    public void setEntryTime(String entryTime) {
        EntryTime = entryTime;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public static ReturnHistoryObject parseObject(SoapObject soapObject) {
        ReturnHistoryObject object = new ReturnHistoryObject();
        object.setWorkorder(getProperty(soapObject,
                TAG_Workorder));
        object.setEntryDate(getProperty(soapObject, TAG_EntryDate));
        object.setEntryTime(getProperty(soapObject, TAG_EntryTime));
        object.setReport(getProperty(soapObject, TAG_Report));


        return object;
    }
}
