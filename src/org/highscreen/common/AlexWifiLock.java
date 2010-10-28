package org.highscreen.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * Created by IntelliJ IDEA. User: ex3ndr Date: 30.06.2010 Time: 1:17:54 To
 * change this template use File | Settings | File Templates.
 */
public class AlexWifiLock {


	WifiLock wifiLock = null;
	ConnectivityManager cmgr;

	public AlexWifiLock(Context content) {
		cmgr = (ConnectivityManager) content
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		WifiManager wifiManager = (WifiManager) content
				.getSystemService(Context.WIFI_SERVICE);
		wifiLock = wifiManager.createWifiLock("ALEX_WIFILOCK");
	}

	public boolean isHeld(){

		return wifiLock.isHeld();

	}

	public void release() {
		wifiLock.release();
	}

	public void acquire() {
		wifiLock.acquire();
	}

}
