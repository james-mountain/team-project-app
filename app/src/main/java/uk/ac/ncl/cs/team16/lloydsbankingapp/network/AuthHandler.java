package uk.ac.ncl.cs.team16.lloydsbankingapp.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by James Mountain on 10/03/2015.
 *
 * AuthHandler is a utility class for handling any requests that
 * make use of an authenticated session ID, and require a signature
 */
public class AuthHandler {
	private static AuthHandler singleton = null;
	private Context prefContext;

	public static AuthHandler getInstance() {
		if (singleton == null) {
			singleton = new AuthHandler();
		}

		return singleton;
	}

	public void setPrefContext(Context context) {
		prefContext = context;
	}

	public String handleAuthentication(LinkedHashMap<String, String> params) {
		long curTime = new Date().getTime()/1000;
		int nonce = new Random().nextInt(900000) + 100000;

		params.put("timestamp", String.valueOf(curTime));
		params.put("nonce", String.valueOf(nonce));

		String paramString = "";
		for (String param : params.keySet()) {
			paramString = paramString + param + "=" + params.get(param) + "&";
		}
		paramString = paramString.substring(0, paramString.length()-1); // Remove last ampersand.

		String signature = null;
		try {
			String key = "IgzW60g7VfCvgT8AuZG2tRRgqbx5Cfhj";
			Key secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");

			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKeySpec);
			byte[] paramStringEncBytes = mac.doFinal(paramString.getBytes());

			signature = new String(Hex.encodeHex(paramStringEncBytes));
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		}

		params.put("signature", signature);

		Gson gson = new Gson();
		return gson.toJson(params, LinkedHashMap.class);
	}

	public String obtainSessionID() {
		String location = "uk.ac.ncl.cs.team16.lloydsbankingapp";
		SharedPreferences sharedPref = prefContext.getSharedPreferences(location, Context.MODE_PRIVATE);

		return sharedPref.getString(location + ".session", "SESSIONFAILURE");
	}
}
