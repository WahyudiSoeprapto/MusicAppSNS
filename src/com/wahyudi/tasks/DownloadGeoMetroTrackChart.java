package com.wahyudi.tasks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * to interact with Last.FM's public API.
 *
 */
public class DownloadGeoMetroTrackChart {

	private static final String LastFMMetroTrackChartUrl = 
		"http://ws.audioscrobbler.com/2.0/?method=geo.getmetrotrackchart&country=united+kingdom&format=json&api_key=4d1c9efa0dc746a2dd7f2deddd436f58";
	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logTag = "LastFMHelper";

	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException (String msg)
		{
			super (msg);
		}

		public ApiException (String msg, Throwable thr)
		{
			super (msg, thr);
		}
	}

	/**
	 * download most popular tracks in given metro.
	 * @param params search strings
	 * @return Array of json strings returned by the API. 
	 * @throws ApiException
	 */
	protected static synchronized String downloadFromServer (String... params)
	throws ApiException
	{
		String retval = null;
		String metro = params[0]; 


		String url = LastFMMetroTrackChartUrl 
		+ "&metro=" + metro; 

		Log.d(logTag,"Fetching " + url);
		
		// create an http client and a request object.
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		try {

			// execute the request
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				// handle error here
				throw new ApiException("Invalid response from last.fm" + 
						status.toString());
			}

			// process the content. 
			HttpEntity entity = response.getEntity();
			InputStream ist = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readCount = 0;
			while ((readCount = ist.read(buff)) != -1) {
				content.write(buff, 0, readCount);
			}
			retval = new String (content.toByteArray());

		} catch (Exception e) {
			throw new ApiException("Problem connecting to the server " + 
					e.getMessage(), e);
		}

		return retval;
	}
}
