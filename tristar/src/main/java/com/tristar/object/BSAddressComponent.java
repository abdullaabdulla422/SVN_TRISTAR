package com.tristar.object;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("ALL")
public class BSAddressComponent {
	private String shortName;
	private String longName;
	private ArrayList<String> types;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	
	public BSAddressComponent() {
		
	}
	
	public static BSAddressComponent parseObject(Element element) throws Exception {
		BSAddressComponent result = new BSAddressComponent();
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			
			if(! (childNodes.item(i) instanceof Element))
				continue;
			element = (Element) childNodes.item(i);
			String tag = element.getNodeName(), value = element.getTextContent(); 
			if (tag.equals("long_name")) {
				result.setLongName(value);
			} else if (tag.equals("short_name")) {
				result.setShortName(value);
			} else if (tag.equals("type")) {
				if(result.getTypes() == null) {
					result.setTypes(new ArrayList<String>());
				}
				result.getTypes().add(value);
			}
		}
		
		return result;
	}
}
