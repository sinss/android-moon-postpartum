
package li.com.bobsonclinic.util;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Leo on 01/03/2017.
 */

public class DateHelper {
    public static final long weekUnit = 24 * 60 * 60 * 1000 * 7;
    public static final long dayUnit = 24 * 60 * 60 * 1000;
    public static final long hourUnit = 60 * 60 * 1000;
    public static final long minuteUnit = 60 * 1000;
    public static final long secondUnit = 1000;

    /**
     * Create a Singleton
     * */
    private static DateHelper instance = null;

    public static DateHelper shared() {
        if (instance == null) {
            synchronized (DateHelper.class) {
                if (instance == null) {
                    instance = new DateHelper();
                }
            }
        }
        return instance;
    }

    public Date convertToTodayKeepHourAndMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        return cal.getTime();
    }

    public boolean isBetween(Date source, Date start, Date end) {
        return source.after(start) && source.before(end);
    }

    public String formatDateStringForBirthday(Date date) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        String str = format.format(date);
        if (str.split(" ").length > 1) {
            return str.split(" ")[0];
        }
        return str;
    }

    public String formatDateStringOnlyHourMinute(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        Date dt = cal.getTime();
        return formatDateStringOnlyHourMinute(dt);
    }


    public String formatDateStringOnlyHourMinute(Date date) {
        DateFormat format = new SimpleDateFormat("aa");
        format.setTimeZone(TimeZone.getDefault());
        String part1 = format.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour > 12) {
            hour -= 12;
        }
        return String.format(Locale.getDefault(), "%s%02d:%02d", part1, hour, minute);
    }

    /**
     * 民國年轉日期
     *
     * @author leo.chang
     * @param dateString
     * @return date : 西元日期
     * @throws NumberFormatException
     *             , ParseException
     * */
    public Date chineseDateToDate(String dateString)
            throws NumberFormatException, ParseException {
        Date convertDate = null;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        String str = "";
        if (dateString.length() < 9) {
            return new Date();
        }
        //012/45/89
        String[] items = dateString.split("/");
        if (items.length == 3) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",
                    Locale.US);
            int year = Integer.parseInt(items[0]);
            int mon = Integer.parseInt(items[1]);
            int day = Integer.parseInt(items[2]);
            if (year == 999)
                year = cal.get(Calendar.YEAR) + 1;
            else if (year < 1911)
                year += 1911;
            str = String.format(Locale.getDefault(), "%d-%d-%dT00:00:00.000", year, mon,
                    day);
            convertDate = sdf.parse(str);
            return convertDate;
        }
        return new Date();
    }

    /**
     * 日期轉民國年字串
     *
     * @author leo.chang
     * @param date
     *            :日期
     * @throws NullPointerException
     *             , IllegalFormatException, ArrayIndexOutOfBoundsException
     * */
    public String dateToChineseDate(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        if (year > 1911)
            year -= 1911;
        dateString = String.format(Locale.getDefault(), "%03d/%02d/%02d", year,
                mon + 1, day);
        return dateString;
    }

    /**
     * 日期轉字串
     *
     * @author leo.chang
     * @param date
     *            :日期
     * @throws NullPointerException
     *             , IllegalFormatException, ArrayIndexOutOfBoundsException
     * */
    public String dateToString(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int mins = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        if (year < 1911)
            year += 1911;
        dateString = String.format(Locale.getDefault(),
                "%04d/%02d/%02d %02d:%02d:%02d", year, (mon + 1), day, hours,
                mins, seconds);
        return dateString;
    }

    /**
     * 日期轉完成字串+時間
     *
     * @author leo.chang
     * @param date
     * @throws NullPointerException
     *             , IllegalFormatException, ArrayIndexOutOfBoundsException
     * */
    public String dateToSaveDateString(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        if (year < 1911)
            year += 1911;
        dateString = String.format(Locale.getDefault(),
                "%04d/%02d/%02d %02d:%02d", year, (mon + 1), day, hour, min);
        return dateString;
    }

    /**
     * Format date string XXXX-XX-XX 00:00:00
     * */
    public String dateToFullStringInParse(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        if (year < 1911)
            year += 1911;
        dateString = String.format(Locale.getDefault(),
                "%04d/%02d/%02d %02d:%02d:%02d", year, (mon + 1), day, hour, min, sec);
        return dateString;
    }

    /**
     * Format date string XXXX-XX-XX
     * */
    public String getShortString(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        if (year < 1911)
            year += 1911;
        dateString = String.format(Locale.getDefault(),
                "%04d/%02d/%02d", year, (mon + 1), day);
        return dateString;
    }

    public String getShortKeyString(Date date) throws NullPointerException, IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        if (year < 1911)
            year += 1911;
        dateString = String.format(Locale.getDefault(),
                "%04d-%02d-%02d", year, (mon + 1), day);
        return dateString;
    }

    /**
     * 取得 時:分
     *
     * @author leo.chang
     * @param date
     *            :日期
     * @throws NullPointerException
     *             , IllegalFormatException, ArrayIndexOutOfBoundsException
     * */
    public String getTimeFormatWithDate(Date date) throws NullPointerException,
            IllegalFormatException, ArrayIndexOutOfBoundsException {
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        dateString = String.format(Locale.getDefault(), "%02d:%02d", hour, min);
        return dateString;
    }

    public String formatTime(int hours, int minutes) {
        if (hours >= 12) {
            //PM
            int displayHhour = hours;
            if (displayHhour > 12) {
                displayHhour -= 12;
            }
            return String.format(Locale.getDefault(), "%02d:%02d%s", displayHhour, minutes, "PM");
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d%s", hours, minutes, "AM");
        }
    }

    /**
     * 字串轉日期
     *
     * @author leo.chang
     * @param dateString
     *            :日期字串
     * @throws ParseException
     * */
    public Date stringToDate(String dateString) {
        Date convertDate = null;
        // 2014/6/4
        try {
            String[] items = dateString.split(" ");
            String[] comps = items[0].split("-");
            String[] times = null;
            int hours = 23;
            int mins = 59;
            int seconds = 59;
            if (items.length > 1) {
                times = items[1].split(":");
                hours = Integer.parseInt(times[0]);
                mins = Integer.parseInt(times[1]);
                seconds = Integer.parseInt(times[2]);
            }
            int year = Integer.parseInt(comps[0]);
            int mon = Integer.parseInt(comps[1]);
            int day = Integer.parseInt(comps[2]);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, mon - 1, day, hours, mins, seconds);
            convertDate = cal.getTime();

        } catch (Exception e) {
            return new Date();
        }

        return convertDate;
    }

    public Date addYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    public Date addMonth(Date date, Integer months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public Date addHous(Date date, Integer hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    public Date addMinutes(Date date, Integer minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public Date addSeconds(Date date, Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 產生民國年月(102/01)
     *
     * @author leo.chang
     * @param dateString
     *            :日期格式
     * @throws NullPointerException
     *             , IllegalFormatException, NumberFormatException
     * */
    public String GetYearAndMonthWithDate(String dateString)
            throws NullPointerException, IllegalFormatException,
            NumberFormatException {
        String str = "000/00";
        if (dateString.length() < 9) {
            return str;
        }
        int year = Integer.parseInt(dateString.substring(0, 3));
        int mon = Integer.parseInt(dateString.substring(4, 2));
        if (year < 1911)
            year += 1911;
        str = String.format(Locale.getDefault(), "%03d/%02d", year, mon);
        return str;
    }

    /**
     * 計算兩個日期的天數
     *
     * @author leo.chang
     * @param date1
     *            , date2
     * */
    public Number getDays(Date date1, Date date2) {
        double days = (double) (date1.getTime() - date2.getTime())
                / (double) (dayUnit);
        return days;
    }

    /**
     * 計算傳入的日期是否為今天
     * */
    public boolean isToday(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        return isSameDay(date, new Date());
    }

    public boolean isLessThanDay(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Date today = new Date();
        long interval = today.getTime() - date.getTime();
        if (interval <= dayUnit && interval >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判斷是否同一天
     * @param date1
     * @param date2
     * */
    public boolean isSameDay(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.setTimeZone(TimeZone.getDefault());
        int year1 = calendar.get(Calendar.YEAR);
        int mon1 = calendar.get(Calendar.MONTH) + 1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(date2);
        calendar.setTimeZone(TimeZone.getDefault());
        int year2 = calendar.get(Calendar.YEAR);
        int mon2= calendar.get(Calendar.MONTH) + 1;
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        if (year1 == year2 && mon1 == mon2 && day1 == day2) {
            return true;
        }
        return false;
    }

    public boolean isThisWeek(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year1 = calendar.get(Calendar.YEAR);
        int week1 = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date());
        int year2 = calendar.get(Calendar.YEAR);
        int week2 = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week1 == week2 && year1 == year2) {
            return true;
        }
        return false;
    }

    public boolean isLessThanWeek(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Date today = new Date();
        long interval = today.getTime() - date.getTime();
        if (interval <= weekUnit) {
            return true;
        }
        return false;
    }


    public boolean isThisMonth(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year1 = calendar.get(Calendar.YEAR);
        int mon1 = calendar.get(Calendar.MONTH);
        calendar.setTime(new Date());
        int year2 = calendar.get(Calendar.YEAR);
        int mon2 = calendar.get(Calendar.MONTH);
        if (mon1 == mon2 && year1 == year2) {
            return true;
        }
        return false;
    }

    public boolean isLessThanMonth(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Date today = new Date();
        long interval = today.getTime() - date.getTime();
        if (interval <= dayUnit * 30) {
            return true;
        }
        return false;
    }


    public boolean isThisYear(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year1 = calendar.get(Calendar.YEAR);
        calendar.setTime(new Date());
        int year2 = calendar.get(Calendar.YEAR);
        if (year1 == year2) {
            return true;
        }
        return false;
    }

    public boolean isLessThanYear(@Nullable Date date) {
        if (date == null) {
            return false;
        }
        Date today = new Date();
        long interval = today.getTime() - date.getTime();
        if (interval <= dayUnit * 365) {
            return true;
        }
        return false;
    }


    /**
     * 指出目前是星期幾
     *
     * @author leo.chang
     * @param date
     * @return 0:Sunday ~ 7:Saturday
     * @throws IllegalFormatException
     *             , ArrayIndexOutOfBoundsException
     * */
    public Integer GetWeekOfDate(Date date) throws IllegalFormatException,
            ArrayIndexOutOfBoundsException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 計算實際年齡
     *
     * @author leo.chang
     * @param birth
     *            :計算保險年齡
     * @return realAge:實際年齡
     * */
    public Integer calculateRealAge(Date birth) {
        int age = 30; // default age if fail
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(new Date());
        c2.setTime(birth);
        age = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int mons = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        if (mons < 0) {
            age--;
        }
        return age;
    }

    /**
     * 計算保險年齡
     *
     * @author leo.chang
     * @param birth
     *            :計算保險年齡
     * @return realAge:實際年齡
     * */
    public Integer calculateInsuranceAge(Date birth) {
        int age = 30; // default age if fail

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(new Date());
        c2.setTime(birth);
        // 保險年齡為提早半年起計算年齡
        c2.add(Calendar.MONTH, -6);
        age = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int mons = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        if (mons < 0) {
            age--;
        }
        return age;
    }

    /**
     * 判斷當年度是否為潤年(西元年)
     *
     * @author leo.chang
     * @param year
     * @return true or false
     * */
    public boolean checkIsLeapYearOfYear(Integer year) {
        boolean isleap = false;
        if (year < 1911) {
            year += 1911;
        }
        if (year % 400 == 0 || year % 100 == 0 || year % 4 == 0) {
            isleap = true;
        }
        return isleap;
    }

    public Date getDateFromUTC(String str) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String getUTCString(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * 建立日期物件
     * @param year
     * @param month
     * @param day
     * @return a date instance
     * */
    public Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 建立日期物件
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return a date instance
     * */
    public Date getDate(int year, int month , int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Get date object with hour and minute
     * @param date
     * @param hour
     * @param minute
     * @return a date with given hour and minute
     * */
    public Date getDate(Date date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getDateBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getDate(year, month + 1, day, 0, 0, 0);
    }

    public Date TodayBegin() {
        Date today = new Date();
        return getDateBegin(today);
    }

    public Date getDateEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getDate(year, month + 1, day, 23, 59, 59);
    }

    public Date TodayEnd() {
        Date today = new Date();
        return getDateEnd(today);
    }

    public static boolean isValidData(Date date) {
        Date today = new Date();
        long diff = today.getTime() - date.getTime();

        if (diff > DateHelper.dayUnit * 7) {
            return false;
        }
        return true;
    }
}
