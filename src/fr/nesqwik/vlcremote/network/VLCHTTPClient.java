package fr.nesqwik.vlcremote.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import fr.nesqwik.vlcremote.bean.VLCResponse;
import fr.nesqwik.vlcremote.bean.VLCStatus;



public class VLCHTTPClient {
	private AndroidHttpClient client;
	private static VLCHTTPClient instance;
	
	
	private String serverIp;
	
	private VLCHTTPClient() {
		client = AndroidHttpClient.newInstance(Build.MODEL);
	}
	
	public static VLCHTTPClient getInstance() {
		if (instance == null) {
			instance = new VLCHTTPClient();
		}

		return instance;
	}
	
	private String getServerUrl() {
		return "http://" + serverIp + ":8080/requests/";
	}
	
	public VLCResponse httpGET(String resource, Class<? extends VLCResponse> type) {
		try {
			//Log.d("HTTP_GET", "url : " + getServerUrl() + resource); 
			HttpGet getRequest = new HttpGet(getServerUrl() + resource);
			final String basicAuth = "Basic " + Base64.encodeToString(":vlcremote".getBytes(), Base64.NO_WRAP);
			getRequest.addHeader("Authorization", basicAuth);
			
			//Log.d("HTTP_GET", "SENDING REQUEST TO SERVER");
			HttpResponse response = client.execute(getRequest);
			//Log.d("HTTP_GET", response.getStatusLine().toString());
			if(response.getStatusLine().getStatusCode() == 200) {
				InputStream in = response.getEntity().getContent();
				VLCResponse status = getVlcResponse(in, type);
				in.close();
				
				return status;
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private VLCResponse getVlcResponse(InputStream in, Class<? extends VLCResponse> type) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(new InputStreamReader(in), type);
		} catch(Exception e) {
			// TODO : Remove on release
			Log.e("VLCHTTPClient", e.getMessage());
			return null;
		}
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
}
