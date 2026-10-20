package li.com.bobsonclinic.util;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import com.squareup.picasso.LruCache;

import li.com.bobsonclinic.BOBApplication;
import okhttp3.OkHttpClient;


public class Picasso {

	private static com.squareup.picasso.Picasso mPicasso;
	private static LruCache lruCache;
	
	protected static final int CONNECTION_TIMEOUT = 30000;
	protected static final int READ_TIMEOUT = 30000;

	public static com.squareup.picasso.Picasso getInstance() {
		if (mPicasso == null) {
			if (lruCache == null) {
				lruCache = new LruCache(BOBApplication.getAppContext());
			}
			com.squareup.picasso.Picasso.Builder builder = new com.squareup.picasso.Picasso.Builder(BOBApplication.getAppContext());

			builder.memoryCache(lruCache);

			mPicasso = builder.build();

		}
		return mPicasso;
	}
}
