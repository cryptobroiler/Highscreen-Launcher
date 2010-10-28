package org.highscreen.common;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 30.06.2010
 * Time: 1:46:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseActivity extends Activity {



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


}