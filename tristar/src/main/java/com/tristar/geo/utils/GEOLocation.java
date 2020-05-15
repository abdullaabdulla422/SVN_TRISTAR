package com.tristar.geo.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("ALL")
public class GEOLocation {
	private double latitude;
	private double longitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public GEOLocation() {
	}
	
	public GEOLocation(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public static GEOLocation parseObject(Element element) throws Exception {
		GEOLocation result = new GEOLocation();
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			
			if(!(childNodes.item(i) instanceof Element))
				continue;
			element = (Element) childNodes.item(i);
			String tag = element.getNodeName(), value = element.getTextContent(); 
			if (tag.equals("lat")) {
				result.setLatitude(Double.parseDouble(value));
			} else if (tag.equals("lng")) {
				result.setLongitude(Double.parseDouble(value));
			} 
		}
		
		return result;
	}
}
