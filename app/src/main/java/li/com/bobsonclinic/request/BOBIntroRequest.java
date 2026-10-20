package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.data.server.BOBIntroResponse;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.logic.CommunicationLogic;
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
public class BOBIntroRequest extends BOBRequest {
	public final String RequestName = BOBIntroRequest.class.getSimpleName();

	StringBuffer response;
	String introId;
	String mCategory;


	public BOBIntroRequest(long id, String url, String token, String introid, String category) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		introId = introid;
		mCategory = category;
		addEntity("token", token);
		addEntity("intro_id", introid);
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
			BOBIntroResponse intro = gson.fromJson(reader, BOBIntroResponse.class);
			BOBDataManager.getInstance().setCategoryIntro(mCategory, intro.getIntro());
			Log.d("BOBRequest", RequestName + " onRequestFinished.." + intro.getIntro().getTitle());
			CommunicationLogic.getInstance().postSubIntroList(BOBSystemManager.getInstance().getUserToken(), intro.getIntro().getId(), mCategory);
			onSuccess(intro);

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
