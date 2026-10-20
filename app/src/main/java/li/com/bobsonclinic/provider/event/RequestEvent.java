package li.com.bobsonclinic.provider.event;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import com.google.gson.Gson;

public class RequestEvent {

    public enum Event { START, SUCCEED, ERROR, TIMEOUT, END, PAUSE };

    private Class<?> mRequestClass;
    private Event mEvent;
    private int mResponseCode = -1;
    private int mResponseType = -1;
    private long mRequestId = -1;
    private String mMessage ="";

    private Object data;

    public RequestEvent(Class<?> requestClass, long requestId, Event event){
        this.mRequestClass = requestClass;
        this.mRequestId = requestId;
        this.mEvent = event;
    }

    public long getRequestId(){
        return mRequestId;
    }

    public Class<?> getRequestClass(){
        return mRequestClass;
    }

    public Event getRequestEvent(){
        return mEvent;
    }

    public void setMessage(String message){
        this.mMessage = message;
    }

    public String getMessage(){
        return mMessage;
    }

    public void setResponseCode(int responseCode){
        this.mResponseCode = responseCode;
    }

    public int getResponseCode(){
        return mResponseCode;
    }

    /** 傳入request回傳的資料物件 */
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, RequestEvent.class);
    }

}
