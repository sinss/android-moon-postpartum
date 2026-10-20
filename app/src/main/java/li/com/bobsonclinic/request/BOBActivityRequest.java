package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.data.server.BOBActivityResponse;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * AboutUS or Education
 * 
 * @author Milla.wang
 * 
 */
public class BOBActivityRequest extends BOBRequest {
	public final String RequestName = BOBActivityRequest.class.getSimpleName();

	StringBuffer response;
	String babyId;


	public BOBActivityRequest(long id, String url, String token, String actId) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		addEntity("token", token);
		addEntity("activity_id", actId);
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
			BOBActivityResponse res = gson.fromJson(reader, BOBActivityResponse.class);
			res.getActivity().setJoin_status(res.getJoin_status());
			res.getActivity().setMember_count(String.valueOf(res.getMember_count()));

			if(res.getStatus().equals("601") || res.getStatus().equals("602")) {
				RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
				error.setErrorMsg(res.getStatus());
				error.setErrorCode(res.getStatus());
				onSuccess(error);
			} else {
				onSuccess(res.getActivity());
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
