package org.highscreen.common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.PowerManager;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 30.06.2010
 * Time: 1:25:56
 * To change this template use File | Settings | File Templates.
 */
public class AlexScreensaverLock {
    Context context;
    long timeOut = 10 * 60000;
    PowerManager.WakeLock lock;

    public AlexScreensaverLock(Context context) {
        this.context = context;


        PowerManager power = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
        lock = power.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "nookactivity" + hashCode());
        lock.setReferenceCounted(false);

        readSystemValues();
    }

    private void readSystemValues() {

        try {
            String[] values = {
                    "value"
            };
            String name = null;
            String[] fields = {
                    "bnScreensaverDelay"
            };
            for (int i = 0; i < fields.length; i++) {
                if (name == null) {
                    name = "name=?";
                } else {
                    name += " or name=?";
                }
            }
            Cursor c = context.getContentResolver().query(Uri.parse("content://settings/system"), values, name, fields, "name");
            if (c != null) {
                c.moveToFirst();
                long lvalue = c.getLong(0);
                if (lvalue > 0) {
                    timeOut = lvalue;
                }

            }
            c.close();
            c.deactivate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void release() {
        if (lock.isHeld())
            lock.release();
    }

    public void acquire(long time) {
        lock.acquire(time);
    }

    public void acquire() {
        lock.acquire(timeOut);
    }
}