package com.tristar.object;

import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-012 on 23/12/16.
 */
@SuppressWarnings("ALL")
public class SubmitStatusList extends SoapUtils {

    public static String TAG_WORKORDER = "Workorder";
    public static String TAG_LINE_ITEM = "Lineitem";
    public static String TAG_STATUS_DATE = "StatusDate";
    public static String TAG_STATUS_TIME = "StatusTime";
    public static String TAG_REPORT = "Report";
    public static String TAG_SERVER_CODE = "ServerCode";
    public static String TAG_DATETIME_SUBMITTED = "DateTimeSubmitted";


    private String Workorder;
    private int Lineitem;
    private String StatusDate;
    private String StatusTime;
    private String Report;
    private String ServerCode;
    private String DateTimeSubmitted;

    public String getWorkorder() {
        return Workorder;
    }

    public void setWorkorder(String workorder) {
        Workorder = workorder;
    }

    public int getLineitem() {
        return Lineitem;
    }

    public void setLineitem(int lineitem) {
        Lineitem = lineitem;
    }

    public String getStatusDate() {
        return StatusDate;
    }

    public void setStatusDate(String statusDate) {
        StatusDate = statusDate;
    }

    public String getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(String statusTime) {
        StatusTime = statusTime;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getServerCode() {
        return ServerCode;
    }

    public void setServerCode(String serverCode) {
        ServerCode = serverCode;
    }

    public String getDateTimeSubmitted() {
        return DateTimeSubmitted;
    }

    public void setDateTimeSubmitted(String dateTimeSubmitted) {
        DateTimeSubmitted = dateTimeSubmitted;
    }

    public static SubmitStatusList parseStatusObject(SoapObject soapObject) {
        SubmitStatusList object = new SubmitStatusList();

        object.setWorkorder(getProperty(soapObject, TAG_WORKORDER));
        object.setLineitem(getPropertyAsInt(soapObject,
                TAG_LINE_ITEM));
        object.setStatusDate(getProperty(soapObject, TAG_STATUS_DATE));

        object.setStatusTime(getProperty(soapObject, TAG_STATUS_TIME));
        object.setReport(getProperty(soapObject, TAG_REPORT));
        object.setServerCode(getProperty(soapObject, TAG_SERVER_CODE));
        object.setDateTimeSubmitted(getProperty(soapObject, TAG_DATETIME_SUBMITTED));



        return object;
    }
}
