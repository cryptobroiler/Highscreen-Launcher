package org.highscreen.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.highscreen.launcher.db.ItemsStorage;
import org.highscreen.launcher.items.AbstractItem;
import org.highscreen.launcher.items.StandardItem;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 4:26:50
 * To change this template use File | Settings | File Templates.
 */
public class ItemFactory {

    public static final String[] BLOCKED = new String[]{
            "com.bravo.ereader.activities.ReaderActivity",
            "com.bravo.reader.instore.ReaderActivity",
            "com.ereader.android.nook.ReaderActivity",
            "com.android.development.Development",
            "com.bravo.FormatSdcard",
            "com.bravo.app.log.LogSettings",
          //  "org.geometerplus.android.fbreader.FBReader",
            "org.highscreen.launcher.LauncherActivity",
    };

    public static boolean isBlocked(String name) {
        for (int i = 0; i < BLOCKED.length; i++)
            if (BLOCKED[i].equals(name))
                return true;

        return false;
    }

    private static ItemsStorage.Item[] getAllItems(Context context) {
        ArrayList<ItemsStorage.Item> items = new ArrayList<ItemsStorage.Item>();

        PackageManager pkgManager = context.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installed = pkgManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo i : installed) {
            if (isBlocked(i.activityInfo.name))
                continue;
            Log.d("NOOK:",i.activityInfo.name);
            items.add(new ItemsStorage.Item(AbstractItem.TYPE_STANDARD, Intent.ACTION_MAIN + ":" + i.activityInfo.packageName + ":" + i.activityInfo.name));
        }

//        items.add(new ItemsStorage.Item(AbstractItem.TYPE_READING_NOW, ""));
//
//items.add(new ItemsStorage.Item(AbstractItem.TYPE_WIFI, ""));
items.add(new ItemsStorage.Item(AbstractItem.TYPE_WIFI_TOGGLE, ""));

        return items.toArray(new ItemsStorage.Item[0]);
    }

    private static ItemsStorage.Item[] getDefault() {
        return new ItemsStorage.Item[]{
                new ItemsStorage.Item(AbstractItem.TYPE_STANDARD, "android.intent.action.MAIN:com.android.settings:com.android.settings.Settings"),
                
        };
    }

    private static ItemsStorage.Item[] getSelectedItems(Context context) {
        ItemsStorage.Item[] items = ItemsStorage.loadItems(context);
        Log.d("NOOK",String.valueOf(items.length));
        if (items.length == 0)
            items = getDefault();

        return items;
    }

    private static ItemsStorage.Item[] getUnselectedItems(Context context) {
        ArrayList<ItemsStorage.Item> items = new ArrayList<ItemsStorage.Item>();

        ItemsStorage.Item[] allItems = getAllItems(context);
        ItemsStorage.Item[] selected = getSelectedItems(context);

        outer:
        for (int a = 0; a < allItems.length; a++) {
            for (int s = 0; s < selected.length; s++) {
                if (selected[s].equals(allItems[a]))
                    continue outer;
            }

            items.add(allItems[a]);
        }

        return items.toArray(new ItemsStorage.Item[0]);
    }

    public static List<AbstractItem> loadUnselectedItems(Context context) {
        ItemsStorage.Item[] items = getUnselectedItems(context);

        ArrayList<AbstractItem> res = new ArrayList<AbstractItem>();
        for (ItemsStorage.Item i : items) {
            res.add(AbstractItem.createItem(i.data, i.id, context));
        }
        return res;
    }

    public static List<AbstractItem> loadItems(Context context) {
        ItemsStorage.Item[] items = getSelectedItems(context);

        ArrayList<AbstractItem> res = new ArrayList<AbstractItem>();
        for (ItemsStorage.Item i : items) {
            AbstractItem item = AbstractItem.createItem(i.data, i.id, context);
            if (item instanceof StandardItem) {
                if (((StandardItem) item).isWorks()) {
                    res.add(item);
                }
            } else
                res.add(item);
        }
        return res;
    }

    public static void saveItems(List<AbstractItem> items, Context context) {
        ItemsStorage.Item[] res = new ItemsStorage.Item[items.size()];
        for (int i = 0; i < res.length; i++) {
            AbstractItem item = items.get(i);
            res[i] = new ItemsStorage.Item(item.getType(), item.getData());
        }

        ItemsStorage.saveItems(res, context);
    }
}
