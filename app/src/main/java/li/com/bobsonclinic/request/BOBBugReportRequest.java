package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.data.server.base.BOBResponse;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * AboutUS or Education
 * 
 * @author Milla.wang
 * 
 */
public class BOBBugReportRequest extends BOBRequest {

	public final String RequestName = BOBBugReportRequest.class.getSimpleName();

	StringBuffer response;
	String babyId;


	public BOBBugReportRequest(long id, String url, String room,String email,boolean limit,String content) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		addEntity("token", BOBSystemManager.getInstance().getUserToken());
		addEntity("name", BOBDataManager.getInstance().getMomeyData().getName());
		addEntity("room", room);
		addEntity("phone_model",BOBSystemManager.getInstance().getDevice()+", "+ BOBSystemManager.getInstance().getModel());
		addEntity("limit_internet", (limit?"1":"0"));
		addEntity("email", email);
		addEntity("commit", content);

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

			onSuccess(res);

		} catch (Exception e) {
			e.printStackTrace();
			onFail(new RequestError(RequestError.ErrorType.EXCEPTION));
		}

	}

	@Override
	protected void onRequestFail(RequestError error) throws Exception {
		super.onRequestFail(error);
		Log.d("BOBRequest", RequestName + " onRequestFail.." + error.getInputStream()+", "+error.getResponseCode());

	}
}
