/*
 * Copyright 2017-2018 Diing, Inc. All Rights Reserved.
 */

package li.com.bobsonclinic.util;

import android.net.Uri;

/**
 * Created by Leo on 07/02/2017.
 */
public class Config {

    public static final Integer databaseVersion = 0;
    public static final String BROADCAST_NAME = "com.bobsonclinic.mobile.";
    public static final String BROADCAST_CONNECTED = BROADCAST_NAME + ".network.connected";
    public static final String BROADCAST_DISCONNECTED = BROADCAST_NAME + ".network.disconnected";

    /**
     * Broadcast Action Uri define
     * */
    public static final String BROADCAST_VIDEO = BROADCAST_NAME+"video.ablum";

}
