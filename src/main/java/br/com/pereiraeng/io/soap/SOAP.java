package br.com.pereiraeng.io.soap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SOAP {

	private static final int TIME_OUT = 20_000;

	public static InputStream getSOAP(String url, byte[] content) throws IOException, URISyntaxException {
		return getSOAP(url, content, "text/xml;charset=UTF-8");
	}

	public static InputStream getSOAP(String url, byte[] content, String contentType)
			throws IOException, URISyntaxException {
		return getSOAP(url, content, contentType, null);
	}

	public static InputStream getSOAP(String url, byte[] content, String contentType, String soapAction)
			throws IOException, URISyntaxException {
		URL obj = new URI(url).toURL();
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setConnectTimeout(TIME_OUT);

		// request headers (add all headers needed)
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-length", String.valueOf(content.length));
		conn.setRequestProperty("Content-Type", contentType);
		if (soapAction != null)
			conn.setRequestProperty("SOAPAction", soapAction);

		// Send post request
		conn.setDoOutput(true);
		conn.setReadTimeout(TIME_OUT);
		DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
		outputStream.write(content);
		int responseCode = conn.getResponseCode();
		InputStream in = null;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
			in = conn.getInputStream();
		else {
			System.out.println("Response Code : " + responseCode);
			in = conn.getErrorStream();
		}
		return in;
	}
}
