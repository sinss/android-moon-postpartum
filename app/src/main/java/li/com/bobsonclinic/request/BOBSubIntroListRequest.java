package li.com.bobsonclinic.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.data.BOBSubIntroList;
import li.com.bobsonclinic.data.server.BOBSubIntroListResponse;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.logic.CommunicationLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * AboutUS or Education
 * 
 * @author Milla.wang
 * 
 */
public class BOBSubIntroListRequest extends BOBRequest {
	public final String RequestName = BOBSubIntroListRequest.class.getSimpleName();

	StringBuffer response;
	String introId;
	String mCategory;


	public BOBSubIntroListRequest(long id, String url, String token, String introid, String category) {
		super(id, url);
		setRequestMethod(RequestType.POST);

		introId = introid;
		addEntity("token", token);
		addEntity("intro_id", introid);
		mCategory = category;
		Log.d("BOBRequest", RequestName + " url.."  +url+ ", introid="+ introid);
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
			BOBSubIntroListResponse dataResponse = gson.fromJson(reader, BOBSubIntroListResponse.class);
			if(dataResponse.getStatus().equals("601") || dataResponse.getStatus().equals("602")) {
				RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
				error.setErrorMsg(dataResponse.getMessage());
				error.setErrorCode(dataResponse.getStatus());
			}

			if(dataResponse != null) {
				if(dataResponse.getSub_intro_list() != null) {
					for (BOBSubIntroList intro : dataResponse.getSub_intro_list()) {
						Log.d("BOBRequest", RequestName + " BOBSubIntroList.." + intro.getId());
						CommunicationLogic.getInstance().postSubIntro(BOBSystemManager.getInstance().getUserToken(), intro.getId(), introId, mCategory);
					}
				}

				if(mCategory.equals(BOBDataManager.INTRO_ABOUT_US)){
					BOBApplication.getAppContext().getContentResolver().update(BOBProvider.URI_AboutUs, null, null, null);
				} else if(mCategory.equals(BOBDataManager.INTRO_EDU)){
					BOBApplication.getAppContext().getContentResolver().update(BOBProvider.URI_EduInfo, null, null, null);
				}

				onSuccess(mCategory);
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
