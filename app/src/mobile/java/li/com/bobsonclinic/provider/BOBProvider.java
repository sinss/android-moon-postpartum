package li.com.bobsonclinic.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;




public class BOBProvider extends ContentProvider {
    private static final String TAG = BOBProvider.class.getSimpleName();
    public static final String AUTHORITY = "com.moon.momcare.mobile.provider.BOBProvider";


    private static final int Camera = 1;
    private static final int AboutUs = 2;
    private static final int EduInfo = 3;
    private static final int BabySwim = 4;
    private static final int AddService = 5;
    private static final int Consumer = 6;
    private static final int MonthClass = 7;
    private static final int news = 8;
    private static final int todayPlan = 9;
    private static final int Mather = 10;
    private static final int Babies = 11;


    public static final Uri URI_Mather = Uri.parse("content://" + AUTHORITY + "/Mather");
    public static final Uri URI_Babies = Uri.parse("content://" + AUTHORITY + "/Babies");
    public static final Uri URI_Camera = Uri.parse("content://" + AUTHORITY + "/Camera");
    public static final Uri URI_AboutUs = Uri.parse("content://" + AUTHORITY + "/AboutUs");
    public static final Uri URI_EduInfo = Uri.parse("content://" + AUTHORITY + "/EduInfo");
    public static final Uri URI_BabySwim = Uri.parse("content://" + AUTHORITY + "/BabySwim");
    public static final Uri URI_AddService = Uri.parse("content://" + AUTHORITY + "/AddService");
    public static final Uri URI_Consumer = Uri.parse("content://" + AUTHORITY + "/Consumer");
    public static final Uri URI_MonthClass = Uri.parse("content://" + AUTHORITY + "/MonthClass");
    public static final Uri URI_News = Uri.parse("content://" + AUTHORITY + "/news");
    public static final Uri URI_TodayPlan = Uri.parse("content://" + AUTHORITY + "/todayPlan");

    private static final UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, GetUriPath(URI_Camera), Camera);
        matcher.addURI(AUTHORITY, GetUriPath(URI_AboutUs), AboutUs);
        matcher.addURI(AUTHORITY, GetUriPath(URI_EduInfo), EduInfo);
        matcher.addURI(AUTHORITY, GetUriPath(URI_BabySwim), BabySwim);
        matcher.addURI(AUTHORITY, GetUriPath(URI_AddService), AddService);
        matcher.addURI(AUTHORITY, GetUriPath(URI_Consumer), Consumer);
        matcher.addURI(AUTHORITY, GetUriPath(URI_MonthClass), MonthClass);
        matcher.addURI(AUTHORITY, GetUriPath(URI_News), news);
        matcher.addURI(AUTHORITY, GetUriPath(URI_TodayPlan), todayPlan);
        matcher.addURI(AUTHORITY, GetUriPath(URI_Mather), Mather);
        matcher.addURI(AUTHORITY, GetUriPath(URI_Babies), Babies);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        this.getContext().getContentResolver().notifyChange(uri, (ContentObserver)null);
        return 0;
    }

    protected static final String GetUriPath(Uri uri) {
        String str = uri.toString();
        String path = str.substring(str.lastIndexOf("/") + 1, str.length());
        return path;
    }
}
