package org.highscreen.launcher.items;

import android.content.Context;
import android.content.Intent;

public class HyperlinkItem extends ButtonItem {
	private String link;
	private String caption;
	private String image;
	@Override
	public void onAction(Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(android.net.Uri.parse(link));
		context.startActivity(intent);
	}
	public HyperlinkItem(String link, String image, String caption) {
		this.link = link;
		this.image = image;
		this.caption = caption;
	}

	@Override
	protected String getImageId() {
		return image;
	}

	@Override
	public int getType() {
		return AbstractItem.TYPE_HYPERLINK;
	}

	@Override
	public String getData() {
		return caption+ ";" + image + ";" + link;
	}

	@Override
	public String getCaption() {
		return caption;
	}
	

}
