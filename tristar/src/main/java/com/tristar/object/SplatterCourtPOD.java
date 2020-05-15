package com.tristar.object;
@SuppressWarnings("ALL")
public class SplatterCourtPOD {
	private String PODworkorder;
	private String servee_name;
	private int addressLineItem;
	public String getWorkorder() {
		return PODworkorder;
	}

	public void setWorkorder(String PODworkorder) {
		this.PODworkorder = PODworkorder;
	}

	public int getAddressLineItem() {
		return addressLineItem;
	}

	public void setAddressLineItem(int addressLineItem) {
		this.addressLineItem = addressLineItem;
	}

	public String getServee_name() {
		return servee_name;
	}

	public void setServee_name(String servee_name) {
		this.servee_name = servee_name;
	}
}
