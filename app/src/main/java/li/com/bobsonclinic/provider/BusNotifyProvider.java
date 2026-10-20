package li.com.bobsonclinic.provider;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


public class BusNotifyProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusNotifyProvider() {
        // No instances.
    }
}
