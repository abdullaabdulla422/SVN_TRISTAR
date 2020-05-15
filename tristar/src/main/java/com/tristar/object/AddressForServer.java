package com.tristar.object;

import android.util.Log;

import com.tristar.utils.SessionData;
import com.tristar.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;


//@SuppressWarnings("ALL")
public class AddressForServer extends SoapUtils {
    public String TAG = AddressForServer.class.getSimpleName();

    public static String TAG_Workorder = "Workorder";
    public static String TAG_JobID = "JobID";
    public static String TAG_AddressLineItem = "AddressLineItem";
    public static String TAG_AddressFormattedForDisplay = "AddressFormattedForDisplay";
    public static String TAG_PriorityTitle = "PriorityTitle";
    public static String TAG_DueDate = "DueDate";
    public static String TAG_DueTime = "DueTime";
    public static String TAG_CASENAME = "CaseName";
    public static String TAG_CASENUMBER = "CaseNumber";
    public static String TAG_ORDERINSTRUCTIONS = "OrderInstructions";
    public static String TAG_PICKUPINSTRUCTIONS = "PickupInstructions";
    public static String TAG_DELIVERYINSTRUCTIONS = "DeliveryInstructions";
    public static String TAG_LatitudeLongitude = "LatitudeLongitude";

    public static String TAG_ServeeName = "CustomerName";

    public static String TAG_Contact = "Contact";
    public static String TAG_Business = "Business";
    public static String TAG_DateReceived = "DateReceived";
    public static String TAG_TimeReceived = "TimeReceived";
    public static String TAG_OrderContact = "OrderContact";
    public static String TAG_ContactPhone = "ContactPhone";
    public static String TAG_HasAttachments = "HasAttachments";
    public static String TAG_MilestoneCode = "MilestoneCode";
    public static String TAG_MilestoneTitle = "MilestoneTitle";
    public static String TAG_AddressFormattedNewLine1 = "AddressFormattedNewLine1";
    public static String TAG_AddressFormattedNewLine2 = "AddressFormattedNewLine2";

    public static String TAG_PickupLatitudeLongitude = "PickupLatitudeLongitude";
    public static String TAG_DeliveryLatitudeLongitude = "DeliveryLatitudeLongitude";



    static int Instructions;

    private String workorder = "";

    private String ServeeName;

    private int jobID;
    private int addressLineItem;
    private String addressFormattedForDisplay;
    private String priorityTitle;
    private String dueDate;
    private String dueTime;
    private String comment;
    private String Date;
    private String time;
    private String OrderInstructions;
    private String PickupInstructions;
    private String DeliveryInstructions;

    private String Contact;
    private String Business;
    private String DateReceived;
    private String TimeReceived;
    private String OrderContact;
    private String ContactPhone;
    private boolean HasAttachments;
    private int MilestoneCode;
    private String MilestoneTitle;
    private String AddressFormattedNewLine1 = "";
    private String AddressFormattedNewLine2 = "";


//	FeeAdvance, Weight, Pieces, WaitTime, Receivedby, Latitude, Longtitude

    private String FeeAdvance;
    private String Weight;
    private String Pieces;
    private String WaitTime;
    private String Receivedby;
    private String lat;
    private String lng;

    private String CaseName;
    private String CaseNumber;

    private String latitudeLongitude;
    public final static int DELIVERY_SERVICE = 2;
    public final static int PICKUP_SERVICE = 1;

    public int TYPE = -1;

    private String latitude;
    private String longitude;

    private String PickupLatitudeLongitude;
    private String DeliveryLatitudeLongitude;

    public int getMilestoneCode() {
        return MilestoneCode;
    }

    public void setMilestoneCode(int milestoneCode) {
        MilestoneCode = milestoneCode;
    }

    public String getMilestoneTitle() {
        return MilestoneTitle;
    }

    public void setMilestoneTitle(String milestoneTitle) {
        MilestoneTitle = milestoneTitle;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddressFormattedForDisplay() {
        return addressFormattedForDisplay;
    }

    public int getAddressLineItem() {
        return addressLineItem;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public String getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public String getPriorityTitle() {
        return priorityTitle;
    }

    public String getWorkorder() {
        return workorder;
    }

    public void setAddressFormattedForDisplay(String addressFormattedForDisplay) {
        this.addressFormattedForDisplay = addressFormattedForDisplay;
    }

    public void setAddressLineItem(int addressLineItem) {
        this.addressLineItem = addressLineItem;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
        if (latitudeLongitude != null) {
            String[] splitArray = latitudeLongitude.split(";");
            if (splitArray.length == 2) {
                setLatitude(splitArray[0]);
                setLongitude(splitArray[1]);
            }
        }
    }

    public void setPriorityTitle(String priorityTitle) {
        this.priorityTitle = priorityTitle;
    }

    public void setWorkorder(String workorder) {
        this.workorder = workorder;
    }

    public static AddressForServer parseObject(SoapObject soapObject) {
        AddressForServer object = new AddressForServer();

        Instructions = SessionData.getInstance().getMessageInstructions();

        Log.d("Instructions int ", "" + Instructions);
        object.setAddressFormattedForDisplay(getProperty(soapObject, TAG_AddressFormattedForDisplay));
        object.setAddressLineItem(getPropertyAsInt(soapObject, TAG_AddressLineItem));
        object.setDueDate(getProperty(soapObject, TAG_DueDate));
        object.setDueTime(getProperty(soapObject, TAG_DueTime));

        object.setLatitudeLongitude(getProperty(soapObject, TAG_LatitudeLongitude));
        object.setPriorityTitle(getProperty(soapObject, TAG_PriorityTitle));
        object.setWorkorder(getProperty(soapObject, TAG_Workorder));
        object.setCaseName(getProperty(soapObject, TAG_CASENAME));
        object.setCaseNumber(getProperty(soapObject, TAG_CASENUMBER));
        object.setOrderInstructions(getProperty(soapObject, TAG_ORDERINSTRUCTIONS));

        object.setServeeName(getProperty(soapObject, TAG_ServeeName));
        object.setBusiness(getProperty(soapObject, TAG_Business));

        object.setDateReceived(getProperty(soapObject, TAG_DateReceived));
        object.setTimeReceived(getProperty(soapObject, TAG_TimeReceived));
        object.setOrderContact(getProperty(soapObject, TAG_OrderContact));
        object.setContactPhone(getProperty(soapObject, TAG_ContactPhone));
        object.setHasAttachments(getPropertyAsBoolean(soapObject, TAG_HasAttachments));
        object.setMilestoneCode(getPropertyAsInt(soapObject, TAG_MilestoneCode));
        object.setMilestoneTitle(getProperty(soapObject, TAG_MilestoneTitle));

        if (SessionData.getInstance().getPickupjob_Address() == 0) {
            object.setAddressFormattedNewLine1(getProperty(soapObject, TAG_AddressFormattedNewLine1));
            object.setAddressFormattedNewLine2(getProperty(soapObject, TAG_AddressFormattedNewLine2));
        }


        if (Instructions == 0) {
            object.setPickupInstructions(getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
            //object.setPickupInstructions("Demo for pickup");
            Log.d("PickupInstructions", "" + getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
        } else if (Instructions == 1) {
            object.setDeliveryInstructions(getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
            //object.setDeliveryInstructions("Demo for Delivery");
            Log.d("DeliveryInstructions", "" + getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
        }

        Log.d("Canse Name & Number", "" + getProperty(soapObject, TAG_CASENAME) + getProperty(soapObject, TAG_CASENUMBER));


        return object;
    }

//    public static AddressForServer parseObjectForPickup(SoapObject soapObject) {
//        AddressForServer object = new AddressForServer();
//
//        Instructions = SessionData.getInstance().getMessageInstructions();
//
//        Log.d("Instructions int ", "" + Instructions);
//        object.setAddressFormattedForDisplay(getProperty(soapObject, TAG_AddressFormattedForDisplay));
//        object.setAddressLineItem(getPropertyAsInt(soapObject, TAG_AddressLineItem));
//        object.setDueDate(getProperty(soapObject, TAG_DueDate));
//        object.setDueTime(getProperty(soapObject, TAG_DueTime));
//
//        object.setLatitudeLongitude(getProperty(soapObject, TAG_LatitudeLongitude));
//        object.setPriorityTitle(getProperty(soapObject, TAG_PriorityTitle));
//        object.setWorkorder(getProperty(soapObject, TAG_Workorder));
//        object.setCaseName(getProperty(soapObject, TAG_CASENAME));
//        object.setCaseNumber(getProperty(soapObject, TAG_CASENUMBER));
//        object.setOrderInstructions(getProperty(soapObject, TAG_ORDERINSTRUCTIONS));
//
//        object.setServeeName(getProperty(soapObject, TAG_ServeeName));
//        object.setBusiness(getProperty(soapObject, TAG_Business));
//
//        object.setDateReceived(getProperty(soapObject, TAG_DateReceived));
//        object.setTimeReceived(getProperty(soapObject, TAG_TimeReceived));
//        object.setOrderContact(getProperty(soapObject, TAG_OrderContact));
//        object.setContactPhone(getProperty(soapObject, TAG_ContactPhone));
//        object.setHasAttachments(getPropertyAsBoolean(soapObject, TAG_HasAttachments));
//        object.setMilestoneCode(getPropertyAsInt(soapObject, TAG_MilestoneCode));
//        object.setMilestoneTitle(getProperty(soapObject, TAG_MilestoneTitle));
//        object.setPickupLatitudeLongitude(getProperty(soapObject, TAG_PickupLatitudeLongitude));
//
//        if (SessionData.getInstance().getPickupjob_Address() == 0) {
//            object.setAddressFormattedNewLine1(getProperty(soapObject, TAG_AddressFormattedNewLine1));
//            object.setAddressFormattedNewLine2(getProperty(soapObject, TAG_AddressFormattedNewLine2));
//        }
//
//
//        if (Instructions == 0) {
//            object.setPickupInstructions(getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
//            //object.setPickupInstructions("Demo for pickup");
//            Log.d("PickupInstructions", "" + getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
//        } else if (Instructions == 1) {
//            object.setDeliveryInstructions(getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
//            //object.setDeliveryInstructions("Demo for Delivery");
//            Log.d("DeliveryInstructions", "" + getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
//        }
//
//        Log.d("Canse Name & Number", "" + getProperty(soapObject, TAG_CASENAME) + getProperty(soapObject, TAG_CASENUMBER));
//
//
//        return object;
//    }
//    public static AddressForServer parseObjectForDelivery(SoapObject soapObject) {
//        AddressForServer object = new AddressForServer();
//
//        Instructions = SessionData.getInstance().getMessageInstructions();
//
//        Log.d("Instructions int ", "" + Instructions);
//        object.setAddressFormattedForDisplay(getProperty(soapObject, TAG_AddressFormattedForDisplay));
//        object.setAddressLineItem(getPropertyAsInt(soapObject, TAG_AddressLineItem));
//        object.setDueDate(getProperty(soapObject, TAG_DueDate));
//        object.setDueTime(getProperty(soapObject, TAG_DueTime));
//
//        object.setLatitudeLongitude(getProperty(soapObject, TAG_LatitudeLongitude));
//        object.setPriorityTitle(getProperty(soapObject, TAG_PriorityTitle));
//        object.setWorkorder(getProperty(soapObject, TAG_Workorder));
//        object.setCaseName(getProperty(soapObject, TAG_CASENAME));
//        object.setCaseNumber(getProperty(soapObject, TAG_CASENUMBER));
//        object.setOrderInstructions(getProperty(soapObject, TAG_ORDERINSTRUCTIONS));
//
//        object.setServeeName(getProperty(soapObject, TAG_ServeeName));
//        object.setBusiness(getProperty(soapObject, TAG_Business));
//
//        object.setDateReceived(getProperty(soapObject, TAG_DateReceived));
//        object.setTimeReceived(getProperty(soapObject, TAG_TimeReceived));
//        object.setOrderContact(getProperty(soapObject, TAG_OrderContact));
//        object.setContactPhone(getProperty(soapObject, TAG_ContactPhone));
//        object.setHasAttachments(getPropertyAsBoolean(soapObject, TAG_HasAttachments));
//        object.setMilestoneCode(getPropertyAsInt(soapObject, TAG_MilestoneCode));
//        object.setMilestoneTitle(getProperty(soapObject, TAG_MilestoneTitle));
//        object.setDeliveryLatitudeLongitude(getProperty(soapObject, TAG_DeliveryLatitudeLongitude));
//
//        if (SessionData.getInstance().getPickupjob_Address() == 0) {
//            object.setAddressFormattedNewLine1(getProperty(soapObject, TAG_AddressFormattedNewLine1));
//            object.setAddressFormattedNewLine2(getProperty(soapObject, TAG_AddressFormattedNewLine2));
//        }
//
//
//        if (Instructions == 0) {
//            object.setPickupInstructions(getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
//            //object.setPickupInstructions("Demo for pickup");
//            Log.d("PickupInstructions", "" + getProperty(soapObject, TAG_PICKUPINSTRUCTIONS));
//        } else if (Instructions == 1) {
//            object.setDeliveryInstructions(getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
//            //object.setDeliveryInstructions("Demo for Delivery");
//            Log.d("DeliveryInstructions", "" + getProperty(soapObject, TAG_DELIVERYINSTRUCTIONS));
//        }
//
//        Log.d("Canse Name & Number", "" + getProperty(soapObject, TAG_CASENAME) + getProperty(soapObject, TAG_CASENUMBER));
//
//
//        return object;
//    }


    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFeeAdvance() {
        return FeeAdvance;
    }

    public void setFeeAdvance(String feeAdvance) {
        FeeAdvance = feeAdvance;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getPieces() {
        return Pieces;
    }

    public void setPieces(String pieces) {
        Pieces = pieces;
    }

    public String getWaitTime() {
        return WaitTime;
    }

    public void setWaitTime(String waitTime) {
        WaitTime = waitTime;
    }

    public String getReceivedby() {
        return Receivedby;
    }

    public void setReceivedby(String receivedby) {
        Receivedby = receivedby;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCaseName() {
        return CaseName;
    }

    public void setCaseName(String caseName) {
        CaseName = caseName;
    }

    public String getCaseNumber() {
        return CaseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        CaseNumber = caseNumber;
    }

    public String getOrderInstructions() {
        return OrderInstructions;
    }

    public void setOrderInstructions(String orderInstructions) {
        OrderInstructions = orderInstructions;
    }

    public String getPickupInstructions() {
        return PickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        PickupInstructions = pickupInstructions;
    }

    public String getDeliveryInstructions() {
        return DeliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        DeliveryInstructions = deliveryInstructions;
    }

    public String getServeeName() {
        return ServeeName;
    }

    public void setServeeName(String serveeName) {
        ServeeName = serveeName;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getDateReceived() {
        return DateReceived;
    }

    public void setDateReceived(String dateReceived) {
        DateReceived = dateReceived;
    }

    public String getTimeReceived() {
        return TimeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        TimeReceived = timeReceived;
    }

    public String getOrderContact() {
        return OrderContact;
    }

    public void setOrderContact(String orderContact) {
        OrderContact = orderContact;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public boolean isHasAttachments() {
        return HasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        HasAttachments = hasAttachments;
    }

    public String getAddressFormattedNewLine1() {
        return AddressFormattedNewLine1;
    }

    public void setAddressFormattedNewLine1(String addressFormattedNewLine1) {
        AddressFormattedNewLine1 = addressFormattedNewLine1;
    }

    public String getAddressFormattedNewLine2() {
        return AddressFormattedNewLine2;
    }

    public void setAddressFormattedNewLine2(String addressFormattedNewLine2) {
        AddressFormattedNewLine2 = addressFormattedNewLine2;
    }

//    public String getPickupLatitudeLongitude() {
//        return PickupLatitudeLongitude;
//    }
//
//    public void setPickupLatitudeLongitude(String pickupLatitudeLongitude) {
//        PickupLatitudeLongitude = pickupLatitudeLongitude;
//    }
//
//    public String getDeliveryLatitudeLongitude() {
//        return DeliveryLatitudeLongitude;
//    }
//
//    public void setDeliveryLatitudeLongitude(String deliveryLatitudeLongitude) {
//        DeliveryLatitudeLongitude = deliveryLatitudeLongitude;
//    }
}
