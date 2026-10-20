package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.data.server.BOBLogin;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;
import zk.android.networking.request.HttpRequest;

/**
 * 會員登入
 * 
 * @author Milla.wang
 * 
 */
public class BOBLoginRequest extends BOBRequest {
	public final String RequestName = BOBLoginRequest.class.getSimpleName();

	StringBuffer response;
	String mRegistrationID;
	String mPassword;


	/**
	 * 會員登入
	 */
	public BOBLoginRequest(long id, String url, String username, String pass, String token) {
		super(id, url);
		setRequestMethod(HttpRequest.RequestType.POST);
		//F124601907, 0651108

		this.mRegistrationID = username;
		this.mPassword = pass;
		addEntity("name", username);
		addEntity("password", pass);
		addEntity("device_token", token);
		addEntity("device", "android");
		Log.d("BOBRequest", RequestName + " url.." + url);
	}

	@Override
	protected void onRequestFinished(InputStream inputStream) {
		//super.onRequestFinished(inputStream);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(response.toString()));
			Log.d("BOBRequest", RequestName + " onRequestFinished.." + response.toString());
			BOBLogin response = gson.fromJson(reader, BOBLogin.class);

			BOBPreferenceLogic.getInstance().setUserRgistrationId(mRegistrationID);
			BOBPreferenceLogic.getInstance().setUserPassword(mPassword);
			BOBPreferenceLogic.getInstance().setUserToken(response.getToken());

			if(response.getStatus().equals("601") || response.getStatus().equals("602")) {
				RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
				error.setErrorMsg(response.getMessage());
				error.setErrorCode(response.getStatus());
				onSuccess(error);
			} else {
				onSuccess(response);
			}


		} catch (Exception e) {
			e.printStackTrace();
			onFail(new RequestError(RequestError.ErrorType.EXCEPTION));
		}

	}

	@Override
	protected void onRequestFail(RequestError error) throws Exception {
		//super.onRequestFail(error);
		try {
			if (mInputStream != null) {
				onFail(error);
			} else {
				onFail(error);
			}
		} catch (Exception e) {
			BOBPreferenceLogic.getInstance().logoutData();
			onFail(error);
		}
	}
}
