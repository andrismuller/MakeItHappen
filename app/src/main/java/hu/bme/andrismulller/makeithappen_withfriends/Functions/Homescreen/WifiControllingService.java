package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.MyUtils;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class WifiControllingService extends Service {

	long id;
	public static Controlling controlling;

	private static final BroadcastReceiver wifiConnectionReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
				MyUtils.turnOffWifi(context);
		}
	};

	public WifiControllingService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerReceiver(wifiConnectionReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(wifiConnectionReceiver);
		stopSelf();
	}
}
