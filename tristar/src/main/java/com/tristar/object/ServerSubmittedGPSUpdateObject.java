package com.tristar.object;

public class ServerSubmittedGPSUpdateObject  {
    private int AddressLineItem;
    private String LatitudeLongitude;
    private String Workorder;
    private boolean IsPickup;


    public ServerSubmittedGPSUpdateObject(int addressLineItem, String latitudeLongitude) {
        AddressLineItem = addressLineItem;
        LatitudeLongitude = latitudeLongitude;
    }

    public ServerSubmittedGPSUpdateObject() {
    }

    public int getAddressLineItem() {
        return AddressLineItem;
    }

    public void setAddressLineItem(int addressLineItem) {
        AddressLineItem = addressLineItem;
    }

    public String getLatitudeLongitude() {
        return LatitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        LatitudeLongitude = latitudeLongitude;
    }

    public String getWorkorder() {
        return Workorder;
    }

    public void setWorkorder(String workorder) {
        Workorder = workorder;
    }

    public boolean isPickup() {
        return IsPickup;
    }

    public void setPickup(boolean pickup) {
        IsPickup = pickup;
    }
}
