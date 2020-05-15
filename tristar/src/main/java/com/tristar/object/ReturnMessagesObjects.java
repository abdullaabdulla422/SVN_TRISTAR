package com.tristar.object;

import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-017 on 14/10/17.
 */

public class ReturnMessagesObjects extends SoapUtils {
    public String TAG = ReturnMessagesObjects.class.getSimpleName();


    public static String TAG_ServerCode = "ServerCode";
    public static String TAG_LineItem = "LineItem";
    public static String TAG_Message = "Message";
    public static String TAG_DateStamp = "DateStamp";
    public static String TAG_ReadDateTime = "ReadDateTime";

    private String ServerCode;
    private int LineItem;
    private String Message;
    private String DateStamp;
    private String ReadDateTime;


    public String getServerCode() {
        return ServerCode;
    }

    public void setServerCode(String serverCode) {
        ServerCode = serverCode;
    }

    public int getLineItem() {
        return LineItem;
    }

    public void setLineItem(int lineItem) {
        LineItem = lineItem;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDateStamp() {
        return DateStamp;
    }

    public void setDateStamp(String dateStamp) {
        DateStamp = dateStamp;
    }

    public String getReadDateTime() {
        return ReadDateTime;
    }

    public void setReadDateTime(String readDateTime) {
        ReadDateTime = readDateTime;
    }

    public static ReturnMessagesObjects parseObject(SoapObject soapObject) {
        ReturnMessagesObjects object = new ReturnMessagesObjects();
        object.setServerCode(getProperty(soapObject,
                TAG_ServerCode));
        object.setLineItem(getPropertyAsInt(soapObject, TAG_LineItem));
        object.setMessage(getProperty(soapObject, TAG_Message));
        object.setDateStamp(getProperty(soapObject, TAG_DateStamp));
        object.setReadDateTime(getProperty(soapObject, TAG_ReadDateTime));


        return object;
    }

}
