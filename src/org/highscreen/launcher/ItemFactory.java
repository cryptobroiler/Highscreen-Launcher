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


public class ItemFactory {

	public static final String[] BLOCKED = new String[] {
			"com.android.development.Development",
			// "org.geometerplus.android.fbreader.FBReader",
			"org.highscreen.launcher.LauncherActivity", };

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
		List<ResolveInfo> installed = pkgManager.queryIntentActivities(
				mainIntent, 0);

		for (ResolveInfo i : installed) {
			if (isBlocked(i.activityInfo.name))
				continue;
			Log.d("ALEX:", Intent.ACTION_MAIN + ":"
					+ i.activityInfo.packageName + ":" + i.activityInfo.name);
			items.add(new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
					Intent.ACTION_MAIN + ":" + i.activityInfo.packageName + ":"
							+ i.activityInfo.name));
		}

		items.add(new ItemsStorage.Item(AbstractItem.TYPE_WIFI_TOGGLE, ""));
		items.add(new ItemsStorage.Item(AbstractItem.TYPE_HYPERLINK,
				"‹итрес;org.highscreen.launcher.litres;http://highscreen.litres.ru"));

		return items.toArray(new ItemsStorage.Item[0]);
	}

	private static ItemsStorage.Item[] getDefault() {
		return new ItemsStorage.Item[] {
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.settings:com.android.settings.Settings"),
				new ItemsStorage.Item(AbstractItem.TYPE_WIFI_TOGGLE, ""),
				new ItemsStorage.Item(AbstractItem.TYPE_HYPERLINK,
						"‹итђес;org.highscreen.launcher.litres;http://highscreen.litres.ru"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.email:com.android.email.activity.Welcome"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.camera:com.android.camera.GalleryPicker"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.browser:com.android.browser.BrowserActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.music:com.android.music.MusicBrowserActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:alex.reader.ebook.viewer:alex.reader.ebook.viewer.activity.MainMenuActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:alex.reader.ebook.viewer:alex.reader.ebook.viewer.activity.About"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.android.calculator2:com.android.calculator2.Calculator"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.funkyandroid.appstore.client.alex:com.funkyandroid.appstore.client.alex.AppStoreClient"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.kldsp:com.mobilein.kldsp.MainActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:org.geometerplus.zlibrary.ui.android:org.geometerplus.android.fbreader.FBReader"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.kom:com.mobilein.kom.MainActivity"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.mk:com.mobilein.mk.MainActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.imobilco.alex.app:com.imobilco.alex.app.AlexActivity"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.trud:com.mobilein.trud.MainActivity"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.news:com.mobilein.news.MainActivity"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.eg:com.mobilein.eg.MainActivity"),
				new ItemsStorage.Item(AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.mobilein.aif:com.mobilein.aif.MainActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:org.openintents.filemanager:org.openintents.filemanager.FileManagerActivity"),
				new ItemsStorage.Item(
						AbstractItem.TYPE_STANDARD,
						"android.intent.action.MAIN:com.cbaplab.bogr:com.cbaplab.bogr.activity.QuotesList"),

		};
	}

	private static ItemsStorage.Item[] getSelectedItems(Context context) {
		ItemsStorage.Item[] items = ItemsStorage.loadItems(context);
		Log.d("Alex Launcher", String.valueOf(items.length));
		if (items.length == 0)
			items = getDefault();

		return items;
	}

	private static ItemsStorage.Item[] getUnselectedItems(Context context) {
		ArrayList<ItemsStorage.Item> items = new ArrayList<ItemsStorage.Item>();

		ItemsStorage.Item[] allItems = getAllItems(context);
		ItemsStorage.Item[] selected = getSelectedItems(context);

		outer: for (int a = 0; a < allItems.length; a++) {
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
