package hu.bme.andrismulller.makeithappen_withfriends.MyUtils;

/**
 * Created by Muller Andras on 2/22/2018.
 */

public class Constants {
    public static final String DELIMITER_REQUEST = "#;#";
    public static final String REQUEST_TYPE_CALL = "call";
    public static final String REQUEST_TYPE_UNLOCK = "unlock";
    public static final String FT = "ft";

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final int REQUEST_PERMISSION_LOCATION = 1004;

    public final static int REQUEST_FORECAST = 1;
    public final static int REQUEST_WEATHER = 2;
    public final static String KEY_FORCAST_UPDATE_TIME = "forecast_update_time";
    public final static String KEY_WEATHER_UPDATE_TIME = "weather_update_time";
    public final static int WEATHER_UPDATE_INTERVAL = 0;

    public static final String MY_PREFERENCE = "mypref";

    public static final String BUDAPEST_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=1358e65404bbf025e405a5f58ded63ec";
    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    public static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Budapest,hu&units=metric&appid=1358e65404bbf025e405a5f58ded63ec";

    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final int RESULT_OK = 1;

    public static final int MAX_ALARM_NUMBER = 100;

	public static final long CLOCK_ALARM = -1;

	public static final int ALARM_TYPE_CLOCK = 1;
	public static final int ALARM_TYPE_TODO = 2;
}
