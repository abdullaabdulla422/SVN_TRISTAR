package com.tristar.geo.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ViewPortBounds {
	private GEOLocation southWest;
	private GEOLocation northEast;
	public GEOLocation getSouthWest() {
		return southWest;
	}
	public void setSouthWest(GEOLocation southWest) {
		this.southWest = southWest;
	}
	public GEOLocation getNorthEast() {
		return northEast;
	}
	public void setNorthEast(GEOLocation northEast) {
		this.northEast = northEast;
	}
	
	public ViewPortBounds() {
		
	}
	
	public static ViewPortBounds parseObject(Element element) throws Exception {
		ViewPortBounds result = new ViewPortBounds();
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			
			if(! (childNodes.item(i) instanceof Element))
				continue;
			element = (Element) childNodes.item(i);
			String tag = element.getNodeName(); 
			if (tag.equals("southwest")) {
				result.setSouthWest(GEOLocation.parseObject(element));
			} else if (tag.equals("northeast")) {
				result.setNorthEast(GEOLocation.parseObject(element));
			}
		}
		
		return result;
	}
}
