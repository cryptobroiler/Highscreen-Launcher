package org.highscreen.launcher.items;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.FileInputStream;
import java.io.InputStream;

/**
  * User: ex3ndr
 * Date: 13.07.2010
 * Time: 3:08:12
  */
public abstract class AbstractItem {

    public static AbstractItem createItem(String data, int id, Context context) {
        switch (id) {
            case TYPE_WIFI:
                return new WifiLockerItem(context);
            case TYPE_WIFI_TOGGLE:
            	return new WifiToggler(context);
            case TYPE_STANDARD:
                String[] params = data.split(":", 3);
                String label = params[2].substring(params[1].length());
                boolean works = false;
                try {
                    PackageManager pkMgr = context.getPackageManager();
                    label = pkMgr.getActivityInfo(new ComponentName(params[1], params[2]), 0).loadLabel(pkMgr).toString();
                    works = true;
                }
                catch (Exception e) {
                }
                return new StandardItem(params[0], params[1], params[2], label, works);
            case TYPE_READING_NOW:
                return new ReadingNowItem();
        }

        return null;
    }

    public interface OnStartEditListener {
        public void onStartEdit(AbstractItem item);
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(AbstractItem item);
    }

    protected OnItemSelectedListener listener;
    protected OnStartEditListener editListener;
    protected boolean isEditing = false;

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_STANDARD = 2;
    public static final int TYPE_READING_NOW = 3;
    public static final int TYPE_WIFI_TOGGLE = 4;

    public abstract int getType();

    public abstract String getData();

    public abstract View createView(Context context);

    public abstract String getCaption();

    public void setStartEditListener(OnStartEditListener editListener) {
        this.editListener = editListener;
    }

    public void setItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void startEdit() {
        isEditing = true;
    }

    public void stopEdit() {
        isEditing = false;
    }

    protected Drawable createDrawable(Context context, String id) {
        try {
            String fileName = id + ".png";
            InputStream image = new FileInputStream("/system/media/sdcard/my icons/" + fileName);
            return BitmapDrawable.createFromStream(image, fileName);
        }
        catch (Exception e) {
        }

        try {
            String fileName = id + ".png";
            InputStream image = new FileInputStream("/my icons/" + fileName);
            return BitmapDrawable.createFromStream(image, fileName);
        }
        catch (Exception e) {
        }

        String locale = context.getResources().getConfiguration().locale.getLanguage();

        try {
            String fileName = id + ".png";
            InputStream image = context.getAssets().open(locale + "/" + fileName);
            return BitmapDrawable.createFromStream(image, fileName);
        }
        catch (Exception e) {
        }

        try {
            String fileName = id + ".png";
            InputStream image = context.getAssets().open("icons/" + fileName);
            return BitmapDrawable.createFromStream(image, fileName);
        }
        catch (Exception e) {
        }

        return null;
    }
}
