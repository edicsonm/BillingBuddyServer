package au.com.billingbuddy.common.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import au.com.billingbuddy.exceptions.objects.JsonException;
import au.com.billingbuddy.exceptions.objects.SearchBinException;

public class SearchBin {
	
	private static String USER_AGENT = "Mozilla/5.0";
	
	public static String getBinInformation(String bin) throws SearchBinException {
		StringBuffer response = null;
		try {
			System.out.println("BIN: "+bin);
			String url = "http://www.binlist.net/json/" + bin;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			System.out.println("responseCode: " + responseCode);
			if(responseCode != 200) throw new SearchBinException("");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (MalformedURLException e) {
			SearchBinException searchBinException = new SearchBinException(e);
			searchBinException.setErrorCode("0");
			throw searchBinException;
		} catch (ProtocolException e) {
			SearchBinException searchBinException = new SearchBinException(e);
			searchBinException.setErrorCode("1");
			throw searchBinException;
		} catch (IOException e) {
			SearchBinException searchBinException = new SearchBinException(e);
			searchBinException.setErrorCode("2");
			throw searchBinException;
		}
	}
	
}
