package com.tristar.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tristar.utils.SessionData;
 
@SuppressWarnings("ALL")
public class DirectionsJSONParser {
 
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){
 
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        ArrayList<String> distance = new ArrayList<String>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
 
        try {
 
            jRoutes = jObject.getJSONArray("routes");
            Log.d("jObject", "" + jObject);
 
            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
 
                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                	
                	//Log.d("array1", "" + jLegs.length());
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
                    
 
                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                    	
                        String polyline = "";
                        String html = "";
                        
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        html = (String)((JSONObject)jSteps.get(k)).get("html_instructions");
                        
                        Log.d("html", "" + Html.fromHtml(Html.fromHtml(html).toString()));
                        String htmlconvert = Html.fromHtml(Html.fromHtml(html).toString()).toString();
                        distance.add(htmlconvert);
                        
                        
                        List<LatLng> list = decodePoly(polyline);
                      
                   
                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){                
                        //	Log.d("array3", "" + list.size());
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                    
                    
                }
            }
            SessionData.getInstance().setDirection(distance);
           // Log.d("array3", "" + routes);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
        
    }
 
    
    private List<LatLng> decodePoly(String encoded) {
 
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
 
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
 
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
 
            LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
            poly.add(p);
        }
 
        return poly;
    }
}