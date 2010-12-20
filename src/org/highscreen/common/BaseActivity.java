package org.highscreen.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 30.06.2010
 * Time: 1:46:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseActivity extends Activity {

	AlexScreensaverLock lock;

    private final static String ALEX_UPDATE_TITLE = "org.highscreen.launcher.UPDATE_TITLE";

    protected void alexUpdateTitle(String title) {
        try {
            Intent intent = new Intent(ALEX_UPDATE_TITLE);
            String key = "apptitle";
            intent.putExtra(key, title);
            sendBroadcast(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lock = new AlexScreensaverLock(this);
        //lock.acquire();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
//        lock.acquire();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        lock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        lock.release();
    }

}