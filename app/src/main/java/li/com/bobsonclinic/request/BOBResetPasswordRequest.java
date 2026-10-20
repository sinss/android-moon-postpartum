package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.data.server.BOBActivityResponse;
import li.com.bobsonclinic.data.server.base.BOBResponse;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * AboutUS or Education
 * 
 * @author Milla.wang
 * 
 */
public class BOBResetPasswordRequest extends BOBRequest {
	public final String RequestName = BOBResetPasswordRequest.class.getSimpleName();

	StringBuffer response;
	String babyId;


	public BOBResetPasswordRequest(long id, String url, String token, String password) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		addEntity("token", token);
		addEntity("new_password", password);
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

			BOBResponse res = gson.fromJson(reader, BOBResponse.class);

			if(res.getStatus().equals("802")) {
				onSuccess(res);
			} else {
				RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
				error.setErrorMsg(res.getStatus());
				error.setErrorCode(res.getStatus());
				onRequestFail(error);
			}

		} catch (Exception e) {
			e.printStackTrace();
			onFail(new RequestError(RequestError.ErrorType.EXCEPTION));
		}

	}

	@Override
	protected void onRequestFail(RequestError error) throws Exception {
		super.onRequestFail(error);
	}
}
