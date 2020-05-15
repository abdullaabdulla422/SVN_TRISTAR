package com.tristar.webutils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;


@SuppressWarnings("ALL")
public class RestFulWebservice {
	
	private static StrictMode.ThreadPolicy policy;
	public static String ARGS_URI = "uri", ARGS_PARAMS = "params";
    
    static {
    	
    	policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    }
    
    public static String request(String url) throws Exception{
    	try {
			StrictMode.setThreadPolicy(policy);
			

			
			DefaultHttpClient hc = new DefaultHttpClient();
			
		   	HttpPost httpPost=new HttpPost(url ); 
		   	httpPost.addHeader("Content-Type", "application/xml");
		   	HttpResponse httpResponse = hc.execute(httpPost);

		   	HttpEntity he = httpResponse.getEntity();
			String xml = EntityUtils.toString(he);
			hc.getConnectionManager().shutdown();

			return xml;
		} catch (ClientProtocolException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
    }
}