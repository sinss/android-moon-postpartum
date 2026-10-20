package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.data.server.BOBMomDataResponse;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * AboutUS or Education
 * 
 * @author Milla.wang
 * 
 */
public class BOBMomDataRequest extends BOBRequest {
	public final String RequestName = BOBMomDataRequest.class.getSimpleName();

	StringBuffer response;
	String introId;


	public BOBMomDataRequest(long id, String url, String token) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		addEntity("token", token);
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
			BOBMomDataResponse dataResponse = gson.fromJson(reader, BOBMomDataResponse.class);
			if(dataResponse.getStatus().equals("601") || dataResponse.getStatus().equals("602")) {
				RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
				error.setErrorMsg(dataResponse.getMessage());
				error.setErrorCode(dataResponse.getStatus());
				onSuccess(error);
			} else {
				BOBDataManager.getInstance().setMomeyData(dataResponse.getMom());
				onSuccess(dataResponse.getMom());
				BOBApplication.getAppContext().getContentResolver().update(BOBProvider.URI_Mather, null, null, null);
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
