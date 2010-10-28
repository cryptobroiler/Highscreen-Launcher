package org.highscreen.launcher.items;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.highscreen.launcher.R;

/**
 * Created by IntelliJ IDEA. User: ex3ndr Date: 13.07.2010 Time: 3:09:37 To
 * change this template use File | Settings | File Templates.
 */
public class StandardItem extends ButtonItem {

	private String action, caption, pkgName, activityName;

	private boolean works;

	public boolean isWorks() {
		return works;
	}

	public StandardItem(String action, String pkgName, String activityName,
			String caption, boolean works) {
		this.action = action;
		this.caption = caption;
		this.pkgName = pkgName;
		this.activityName = activityName;
		this.works = works;
	}

	public StandardItem(String action, String pkgName, String activityName,
			String caption) {
		this(action, pkgName, activityName, caption, true);
	}

	@Override
	protected String getImageId() {
		return activityName;
	}

	@Override
	public int getType() {
		return AbstractItem.TYPE_STANDARD;
	}

	@Override
	public String getData() {
		return action + ":" + pkgName + ":" + activityName;
	}

	@Override
	public String getCaption() {
		return caption;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof StandardItem) {
			StandardItem itm = (StandardItem) o;
			return itm.activityName.equals(activityName)
					&& itm.pkgName.equals(pkgName) && itm.action.equals(action);
		}
		return false;
	}

	@Override
	public void onAction(Context context) {
		Intent intent = new Intent(action);
		intent.setClassName(pkgName, activityName);
		Log.d("AlexLauncher", pkgName + ":" + activityName);
		context.startActivity(intent);
	}

	@Override
	protected View createViewInternal(final Context context) {
		View res = super.createViewInternal(context);
		if (res != null)
			return res;

		View view = LayoutInflater.from(context).inflate(R.layout.item, null);
		((TextView) view.findViewById(R.id.text)).setText(caption);

		try {
			((ImageView) view.findViewById(R.id.icon)).setImageDrawable(context
					.getPackageManager().getActivityIcon(
							new ComponentName(pkgName, activityName)));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		ImageButton btn = (ImageButton) view.findViewById(R.id.btn);

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isEditing) {
					if (listener != null)
						listener.onItemSelected(StandardItem.this);
				} else
					onAction(context);
			}
		});

		btn.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View view) {
				if (editListener != null) {
					editListener.onStartEdit(StandardItem.this);
					return true;
				}
				return false;
			}
		});

		return view;
	}
}
