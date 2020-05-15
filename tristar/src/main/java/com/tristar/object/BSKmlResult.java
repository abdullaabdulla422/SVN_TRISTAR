package com.tristar.object;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tristar.geo.utils.GEOConstants;
import com.tristar.geo.utils.GEOLocation;
import com.tristar.geo.utils.ViewPortBounds;

@SuppressWarnings("ALL")
public class BSKmlResult {
	private String address;
	int accuracy;
	private String countryNameCode;
	private String countryName;
	private String subAdministrativeAreaName;
	private String localityName;
	private ArrayList<BSAddressComponent> addressComponents;
	private GEOLocation location;
	private ViewPortBounds viewPort;
	private ViewPortBounds bounds;

	private GEOLocation coordinate;

	public static int statusCode = -1;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public String getCountryNameCode() {
		return countryNameCode;
	}

	public void setCountryNameCode(String countryNameCode) {
		this.countryNameCode = countryNameCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getSubAdministrativeAreaName() {
		return subAdministrativeAreaName;
	}

	public void setSubAdministrativeAreaName(String subAdministrativeAreaName) {
		this.subAdministrativeAreaName = subAdministrativeAreaName;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public ArrayList<BSAddressComponent> getAddressComponents() {
		return addressComponents;
	}

	public void setAddressComponents(
			ArrayList<BSAddressComponent> addressComponents) {
		this.addressComponents = addressComponents;
	}

	public GEOLocation getLocation() {
		return location;
	}

	public void setLocation(GEOLocation location) {
		this.location = location;
	}

	public ViewPortBounds getViewPort() {
		return viewPort;
	}

	public void setViewPort(ViewPortBounds viewPort) {
		this.viewPort = viewPort;
	}

	public ViewPortBounds getBounds() {
		return bounds;
	}

	public void setBounds(ViewPortBounds bounds) {
		this.bounds = bounds;
	}

	public GEOLocation getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(GEOLocation coordinate) {
		this.coordinate = coordinate;
	}

	public static BSKmlResult parseObject(String xmlString) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(xmlString));
		Document dom = db.parse(is);
		return parseObject(dom.getDocumentElement());
	}

	public static BSKmlResult parseObject(Element element) throws Exception {
		BSKmlResult result = new BSKmlResult();

		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {

			if (!(childNodes.item(i) instanceof Element))
				continue;
			element = (Element) childNodes.item(i);
			String tag = element.getNodeName(), value = element
					.getTextContent();
			if (tag.equals("result")) {
				NodeList resultNodes = element.getChildNodes();
				for (int resultIndex = 0; resultIndex < resultNodes.getLength(); resultIndex++) {
					if (!(resultNodes.item(resultIndex) instanceof Element))
						continue;
					element = (Element) resultNodes.item(resultIndex);
					tag = element.getNodeName();
					value = element.getTextContent();
					if (tag.equals("type")) {
					} else if (tag.equals("formatted_address")) {
						result.setAddress(value);
					} else if (tag.equals("address_component")) {
						if (result.getAddressComponents() == null) {
							result.setAddressComponents(new ArrayList<BSAddressComponent>());
						}
						result.getAddressComponents().add(
								BSAddressComponent.parseObject(element));
					} else if (tag.equals("geometry")) {
						NodeList geoNodes = element.getChildNodes();
						for (int geoIndex = 0; geoIndex < geoNodes.getLength(); geoIndex++) {
							if (!(geoNodes.item(geoIndex) instanceof Element))
								continue;
							element = (Element) geoNodes.item(geoIndex);
							tag = element.getNodeName();
							// Logger.log("DEBUG: Parsing node "+ tag);
							if (tag.equals("location")) {
								result.setLocation(GEOLocation
										.parseObject(element));
							} else if (tag.equals("viewport")) {
								result.setViewPort(ViewPortBounds
										.parseObject(element));
							}
						}
					}
				}
			} else if (tag.equals("status")) {

				if (value.equals("OK")) {
					statusCode = GEOConstants.G_GEO_SUCCESS;
				} else if (value.equals("ZERO_RESULTS")) {
					statusCode = GEOConstants.G_GEO_UNKNOWN_ADDRESS;
				} else if (value.equals("OVER_QUERY_LIMIT")) {
					statusCode = GEOConstants.G_GEO_TOO_MANY_QUERIES;
				} else if (value.equals("REQUEST_DENIED")) {
					statusCode = GEOConstants.G_GEO_SERVER_ERROR;
				} else if (value.equals("INVALID_REQUEST")) {
					statusCode = GEOConstants.G_GEO_BAD_REQUEST;
				}
			}
		}

		return result;
	}
}
