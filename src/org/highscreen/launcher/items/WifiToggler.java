package org.highscreen.launcher.items;

import org.highscreen.launcher.R;
import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiToggler extends ToggleItem {
	Context context;
	WifiManager wifiManager;

	public WifiToggler(Context context) {
		this.context = context;
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

	}

	@Override
	public String getImageId() {
		return "org.highscreen.launcher.WifiToggle";
	}

	@Override
	public boolean isOn() {
		return wifiManager.isWifiEnabled();
	}

	@Override
	public void toggle() {
		if (isOn()) {
			wifiManager.setWifiEnabled(false);
			WifiTask task = new WifiTask(context,false);
			task.execute();
		} else {
			wifiManager.setWifiEnabled(true);
			WifiTask task = new WifiTask(context,true);
			task.execute();
		}
		

	}

	@Override
	public int getType() {
		return TYPE_WIFI_TOGGLE;
	}

	@Override
	public String getData() {
		return "";
	}

	@Override
	public String getCaption() {
		return "Wifi Toggler";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WifiToggler)
			return ((WifiToggler) o).getType() == getType();

		return false;
	}

	class WifiTask extends AsyncTask<Void, Integer, Boolean> {
		Context context;
		private boolean isTurningOn;

		public WifiTask(Context context, boolean isTurningOn) {
			this.context = context;
			this.isTurningOn = isTurningOn;
		}

		@Override
		protected void onPreExecute() {
			displayAlert(context.getString(R.string.start_wifi),
					context.getString(R.string.please_wait), 1, null, -1);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			int attempts = 1;
			while (((!wifiManager.isWifiEnabled() && isTurningOn) || (wifiManager
					.isWifiEnabled() && !isTurningOn)) && attempts < 20) {
				try {
					Thread.sleep(3000);
				} catch (Exception ex) {

				}
				attempts++;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			closeAlert();
			if (!result) {
				displayAlert(context.getString(R.string.wifi_timeout), "", 2,
						null, -1);
			} else {
			}
			updateView();
		}
	}

	AlertDialog m_AlertDialog = null;

	public void displayAlert(String title, String msg, final int type,
			AlertDialog.OnClickListener listener, int drawable) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		if (type == 1) {
			builder.setNegativeButton(R.string.cancel, listener).setCancelable(
					true);
			if (drawable != -1) {
				builder.setIcon(drawable);
			}
		} else if (type == 2 || type == 3) {
			builder.setPositiveButton(android.R.string.ok, listener);
			if (drawable != -1) {
				builder.setIcon(drawable);
			}
		}
		m_AlertDialog = builder.show();
	}

	public void closeAlert() {
		if (m_AlertDialog != null) {
			m_AlertDialog.dismiss();
		}
	}

}
