package org.highscreen.launcher.items;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.highscreen.common.AlexWifiLock;
import org.highscreen.launcher.R;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 7:42:55
 * To change this template use File | Settings | File Templates.
 */
public class WifiLockerItem extends ToggleItem {

    @Override
    public String getImageId() {
        return "ru.mynook.locker.WifiLocker";
    }

    AlexWifiLock wifiLock;
    Context context;

    public WifiLockerItem(Context context) {
        this.context = context;
        wifiLock = new AlexWifiLock(context);
    }

    @Override
    public boolean isOn() {
        return wifiLock.isHeld();
    }

    @Override
    public void toggle() {
        if (wifiLock.isHeld()) {
            wifiLock.release();
            updateView();
        } else {
            wifiLock.acquire();
            WifiTask task = new WifiTask(context);
            task.execute();
        }
    }

    @Override
    public int getType() {
        return TYPE_WIFI_LOCK;
    }

    @Override
    public String getData() {
        return "";
    }

    @Override
    public String getCaption() {
        return "Wifi Locker";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WifiLockerItem)
            return ((WifiLockerItem) o).getType() == getType();

        return false;
    }

    class WifiTask extends AsyncTask<Void, Integer, Boolean> {
        Context context;

        public WifiTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            displayAlert(context.getString(R.string.start_wifi), context.getString(R.string.please_wait), 1, null, -1);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectivityManager cmgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo info = cmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean connection = (info == null) ? false : info.isConnected();
            int attempts = 1;
            while (!connection && attempts < 20) {
                try {
                    Thread.sleep(3000);
                } catch (Exception ex) {

                }
                info = cmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                connection = (info == null) ? false : info.isConnected();
                attempts++;
            }
            return connection;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            closeAlert();
            if (!result) {
                displayAlert(context.getString(R.string.wifi_timeout), "", 2, null, -1);
                wifiLock.release();
            } else {
            }
            updateView();
        }
    }

    AlertDialog m_AlertDialog = null;

    public void displayAlert(String title, String msg, final int type, AlertDialog.OnClickListener listener,
                             int drawable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        if (type == 1) {
            builder.setNegativeButton(R.string.cancel, listener).setCancelable(true);
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
