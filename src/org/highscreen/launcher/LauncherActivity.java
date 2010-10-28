package org.highscreen.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.*;

import org.highscreen.common.BaseActivity;
import org.highscreen.launcher.R;

import java.util.List;

import org.highscreen.launcher.items.AbstractItem;

/**
 * Created by IntelliJ IDEA. User: ex3ndr Date: 13.07.2010 Time: 2:46:11 To
 * change this template use File | Settings | File Templates.
 */


public class LauncherActivity extends BaseActivity {

	List<AbstractItem> items = null;

	ImageView selector;
	View selectedView;
	AbstractItem selectedItem;
	LinearLayout topDock;
	LinearLayout bottomDock;
	HorizontalScrollView scrollView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d("NOOK", "Shit started!");
		selector = new ImageView(this);
		selector.setImageResource(R.drawable.selector);
		selector.setLayoutParams(new ViewGroup.LayoutParams(96, 144));

		topDock = ((LinearLayout) findViewById(R.id.topDock));
		bottomDock = ((LinearLayout) findViewById(R.id.bottomDock));
		scrollView = ((HorizontalScrollView) findViewById(R.id.apps_scroll));

		((ImageButton) findViewById(R.id.exit))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						stopEdit();
					}
				});

		((ImageButton) findViewById(R.id.add))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						AddItemDialog dialog = new AddItemDialog(
								LauncherActivity.this,
								new AddItemDialog.OnAddItemDialogResultListener() {
									public void onAddItemDialogResult(
											AbstractItem item) {
										LinearLayout to;
										if (topDock.getChildCount() <= bottomDock
												.getChildCount()) {
											to = topDock;
										} else {
											to = bottomDock;
										}
										addItemView(item, to);
										items.add(item);
										saveChanges();
									}
								});
						dialog.show();
					}
				});

		((ImageButton) findViewById(R.id.remove))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if ((selectedItem != null) && (selectedView != null)) {
							if (items.size() <= 5) {
								Toast t = Toast.makeText(LauncherActivity.this,
										R.string.too_little_items,
										Toast.LENGTH_LONG);
								t.setGravity(Gravity.CENTER, t.getXOffset(),
										t.getYOffset());
								t.show();
								return;
							}
							((LinearLayout) selectedView.getParent())
									.removeView(selectedView);
							// topDock.removeView(selectedView);
							items.remove(selectedItem);
							selectedView = null;
							selectedItem = null;

							saveChanges();
						}
					}
				});

		((ImageButton) findViewById(R.id.turnLeft))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if ((selectedItem != null) && (selectedView != null)) {
							LinearLayout parent = ((LinearLayout) selectedView
									.getParent());
							int index = getItemIndex(selectedView, parent);
							if (index > 0) {

								focusScroll(parent.getChildAt(index - 1));

								parent.removeView(selectedView);
								parent.addView(selectedView, index - 1);

								items.remove(selectedItem);
								items.add(index - 1, selectedItem);

							} else
								focusScroll(parent.getChildAt(0));
						}
					}
				});

		((ImageButton) findViewById(R.id.turnRight))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if ((selectedItem != null) && (selectedView != null)) {
							LinearLayout parent = ((LinearLayout) selectedView
									.getParent());
							int index = getItemIndex(selectedView, parent);
							if ((index != -1)
									&& (index < parent.getChildCount() - 1)) {
								focusScroll(parent.getChildAt(index + 1));
								parent.removeView(selectedView);
								if (parent.getChildCount() < index + 1)
									parent.addView(selectedView);
								else
									parent.addView(selectedView, index + 1);

								focusScroll(selectedView);

								items.remove(selectedItem);
								items.add(index + 1, selectedItem);

							} else if (index == parent.getChildCount() - 1) {
								focusScroll(parent.getChildAt(parent
										.getChildCount() - 1));
							}
						}
					}
				});
		((ImageButton) findViewById(R.id.turnUp))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if ((selectedItem != null) && (selectedView != null)) {
							LinearLayout parent = ((LinearLayout) selectedView
									.getParent());
							LinearLayout opposite = (parent == topDock) ? bottomDock
									: topDock;
							int index = getItemIndex(selectedView, parent);

							parent.removeView(selectedView);
							opposite.addView(selectedView, index);
							focusScroll(opposite.getChildAt(index));

							items.remove(selectedItem);
							items.add(index, selectedItem);

						}
					}
				});
		((ImageButton) findViewById(R.id.turnDown))
		.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if ((selectedItem != null) && (selectedView != null)) {
					LinearLayout parent = ((LinearLayout) selectedView
							.getParent());
					LinearLayout opposite = (parent == topDock) ? bottomDock
							: topDock;
					int index = getItemIndex(selectedView, parent);

					parent.removeView(selectedView);
					opposite.addView(selectedView, index);
					focusScroll(opposite.getChildAt(index));

					items.remove(selectedItem);
					items.add(index, selectedItem);

				}
			}
		});

		((ImageButton) findViewById(R.id.refresh))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						updateItems();
						startEdit();
						/*
						 * if ((selectedItem != null) && (selectedView != null))
						 * { int index = getItemIndex(selectedView); if ((index
						 * != -1) && (index < items.size() - 1)) {
						 * focusScroll(apps.getChildAt(index + 1));
						 * apps.removeView(selectedView); if
						 * (apps.getChildCount() < index + 1)
						 * apps.addView(selectedView); else
						 * apps.addView(selectedView, index + 1);
						 * 
						 * focusScroll(selectedView);
						 * 
						 * items.remove(selectedItem); items.add(index + 1,
						 * selectedItem);
						 * 
						 * } else if (index == items.size() - 1) {
						 * focusScroll(apps.getChildAt(items.size() - 1)); } }
						 */
					}
				});

		updateItems();
	}

	void updateItems() {
		items = ItemFactory.loadItems(this);

		topDock = ((LinearLayout) findViewById(R.id.topDock));
		topDock.removeAllViews();
		bottomDock = (LinearLayout) findViewById(R.id.bottomDock);
		bottomDock.removeAllViews();
		int count = 0;
		for (AbstractItem i : items) {
			if (count++ % 2 == 0) {
				addItemView(i, topDock);
			} else {
				addItemView(i, bottomDock);
			}

		}
	}

	void focusScroll(View view) {
		if (scrollView.getScrollX() + scrollView.getWidth() < view.getRight()) {
			scrollView.smoothScrollTo(view.getRight() - scrollView.getWidth(),
					0);
		} else if (scrollView.getScrollX() > view.getLeft()) {
			scrollView.smoothScrollTo(view.getLeft(), 0);
		}
	}

	int getItemIndex(View view) {
		for (int i = 0; i < topDock.getChildCount(); i++)
			if (topDock.getChildAt(i) == view)
				return i;
		return -1;
	}

	int getItemIndex(View view, LinearLayout dock) {
		for (int i = 0; i < dock.getChildCount(); i++)
			if (dock.getChildAt(i) == view)
				return i;
		return -1;
	}

	void startEdit() {
		for (final AbstractItem i : items) {
			i.startEdit();
		}
		findViewById(R.id.edit).setVisibility(View.VISIBLE);
	}

	void stopEdit() {
		findViewById(R.id.edit).setVisibility(View.GONE);

		ViewParent parent = selector.getParent();
		if (parent != null)
			((ViewGroup) parent).removeView(selector);

		for (final AbstractItem i : items) {
			i.stopEdit();
		}

		saveChanges();
	}

	void addItemView(final AbstractItem i) {
		final FrameLayout view = new FrameLayout(this);
		view.setLayoutParams(new ViewGroup.LayoutParams(96, 144));
		View itemView = i.createView(this);

		ViewParent parent = itemView.getParent();
		if (parent != null)
			((ViewGroup) parent).removeView(itemView);

		view.addView(itemView);

		i.setStartEditListener(new AbstractItem.OnStartEditListener() {
			public void onStartEdit(AbstractItem item) {
				selectedView = view;
				selectedItem = i;

				ViewParent parent = selector.getParent();
				if (parent != null)
					((ViewGroup) parent).removeView(selector);

				view.addView(selector);

				startEdit();
			}
		});

		topDock.addView(view);

		i.setItemSelectedListener(new AbstractItem.OnItemSelectedListener() {
			public void onItemSelected(AbstractItem item) {
				ViewParent parent = selector.getParent();
				if (parent != null)
					((ViewGroup) parent).removeView(selector);

				view.addView(selector);

				selectedView = view;
				selectedItem = i;
			}
		});
	}

	void addItemView(final AbstractItem i, LinearLayout to) {
		final FrameLayout view = new FrameLayout(this);
		view.setLayoutParams(new ViewGroup.LayoutParams(96, 144));
		View itemView = i.createView(this);

		ViewParent parent = itemView.getParent();
		if (parent != null)
			((ViewGroup) parent).removeView(itemView);

		view.addView(itemView);

		i.setStartEditListener(new AbstractItem.OnStartEditListener() {
			public void onStartEdit(AbstractItem item) {
				selectedView = view;
				selectedItem = i;

				ViewParent parent = selector.getParent();
				if (parent != null)
					((ViewGroup) parent).removeView(selector);

				view.addView(selector);

				startEdit();
			}
		});

		to.addView(view);

		i.setItemSelectedListener(new AbstractItem.OnItemSelectedListener() {
			public void onItemSelected(AbstractItem item) {
				ViewParent parent = selector.getParent();
				if (parent != null)
					((ViewGroup) parent).removeView(selector);

				view.addView(selector);

				selectedView = view;
				selectedItem = i;
			}
		});
	}

	void saveChanges() {
		long start = System.currentTimeMillis();
		ItemFactory.saveItems(items, this);

		long time = System.currentTimeMillis() - start;

		Log.d("TEST", "Time:" + time);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// ((ImageView)
		// findViewById(R.id.wallpaper)).setImageBitmap(BitmapFactory.decodeFile(getWallpaperPath().substring(7)));

		alexUpdateTitle("Home");

		updateItems();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		alexUpdateTitle("Home");
	}
}
