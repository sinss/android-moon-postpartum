package li.com.bobsonclinic.request.base;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import li.com.bobsonclinic.data.server.base.BOBRequestListener;
import li.com.bobsonclinic.data.server.base.BOBResponse;
import li.com.bobsonclinic.provider.BusNotifyProvider;
import zk.android.networking.RequestError;
import zk.android.networking.request.HttpRequest;


public class BOBRequest extends HttpRequest {

    protected int retryCount = 0;

    protected BOBRequestListener mBOBListener;

    public BOBRequest(long id, String url) {
        super(id, url);
        setConnectionTimeout(10*1000);
        setReadTimeout(10*1000);
    }

    @Override
    protected void execute() throws Exception {
        retryCount++;
        super.execute();
    }

    public void sendRequestEvent(Object event) {
        if (event != null)
            BusNotifyProvider.getInstance().post(event);
    }

    public int getRetryCount(){
        return retryCount;
    }

    protected boolean checkResponse(BOBResponse dataResponse){
        if(dataResponse.getStatus().equals("601") || dataResponse.getStatus().equals("602")) {
            RequestError error = new RequestError(RequestError.ErrorType.EXCEPTION);
            error.setErrorMsg(dataResponse.getMessage());
            error.setErrorCode(dataResponse.getStatus());
            onSuccess(error);
            return false;
        }
        return true;
    }

    public BOBRequestListener getBOBListener() {
        return mBOBListener;
    }

    public void setBOBListener(BOBRequestListener mBOBListener) {
        this.mBOBListener = mBOBListener;
    }
}
