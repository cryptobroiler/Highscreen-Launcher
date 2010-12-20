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

		selector = new ImageView(this);
		selector.setImageResource(R.drawable.selector);
		selector.setLayoutParams(new ViewGroup.LayoutParams(96, 144));

		topDock = getTopDock();
		bottomDock = getBottomDock();
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
										int resultIndex = 0;
										if (to == topDock) {
											resultIndex = topDock
													.getChildCount() * 2 - 2;
										} else {
											resultIndex = bottomDock
													.getChildCount() * 2 - 1;

										}
										;
										items.add(resultIndex, item);
										saveChanges();
									}
								});
						dialog.show();
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
								// items.remove(selectedItem);

								if (parent == topDock) {
									AbstractItem leftItem = items
											.get(2 * index - 2);
									items.remove(selectedItem);
									items.add(2 * index - 2, selectedItem);
									items.remove(leftItem);

									items.add(2 * index, leftItem);
								} else {
									AbstractItem leftItem = items
											.get(2 * index - 1);
									items.remove(selectedItem);
									items.add(2 * index - 1, selectedItem);
									items.remove(leftItem);

									items.add(2 * index + 1, leftItem);
								}
								dumpItems();

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
							Log.d("Alex Launcher", "Turning right, index="
									+ String.valueOf(index));
							if ((index != -1)
									&& (index < parent.getChildCount() - 1)) {
								focusScroll(parent.getChildAt(index + 1));
								parent.removeView(selectedView);
								if (parent.getChildCount() < index + 1)
									parent.addView(selectedView);
								else
									parent.addView(selectedView, index + 1);

								focusScroll(selectedView);

								if (parent == topDock) {
									AbstractItem rightItem = items
											.get(2 * index + 2);
									items.remove(rightItem);
									items.add(2 * index, rightItem);
									items.remove(selectedItem);
									items.add(2 * index + 2, selectedItem);

								} else {
									AbstractItem rightItem = items
											.get(2 * index + 3);
									items.remove(rightItem);
									items.add(2 * (index) + 1, rightItem);
									items.remove(selectedItem);
									items.add(2 * (index) + 3, selectedItem);

								}
								dumpItems();

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
							if (opposite.getChildAt(index) != null) {
								View oppositeView = opposite.getChildAt(index);
								opposite.removeView(oppositeView);
								parent.removeView(selectedView);
								opposite.addView(selectedView, index);
								parent.addView(oppositeView, index);

								focusScroll(opposite.getChildAt(index));

								if (parent == topDock) {
									items.remove(selectedItem);
									items.add(2 * index + 1, selectedItem);
								} else {
									items.remove(selectedItem);
									items.add(2 * index, selectedItem);

								}
							}

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
							if (opposite.getChildAt(index) != null) {
								View oppositeView = opposite.getChildAt(index);
								opposite.removeView(oppositeView);
								parent.removeView(selectedView);
								opposite.addView(selectedView, index);
								parent.addView(oppositeView, index);

								focusScroll(opposite.getChildAt(index));

								if (parent == topDock) {
									items.remove(selectedItem);
									items.add(2 * index + 1, selectedItem);
								} else {
									items.remove(selectedItem);
									items.add(2 * index, selectedItem);

								}
							}

						}
					}
				});
		((ImageButton) findViewById(R.id.remove))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if ((selectedItem != null) && (selectedView != null)) {
							if (items.size() <= 0) {
								Toast t = Toast.makeText(LauncherActivity.this,
										R.string.too_little_items,
										Toast.LENGTH_LONG);
								t.setGravity(Gravity.CENTER, t.getXOffset(),
										t.getYOffset());
								t.show();
								return;
							}
							// ((LinearLayout) selectedView.getParent())
							// .removeView(selectedView);
							// topDock.removeView(selectedView);
							int indexInItems;
							indexInItems = items.indexOf(selectedItem);
							// for (int i = indexInItems; i < items.size(); i++)
							// {
							// if (i % 2 == 0) {
							// Log.d("Alex Launcher", "Removing view at top :" +
							// indexInItems / 2);
							// topDock.removeViewAt((indexInItems) / 2);
							// } else {
							// Log.d("Alex Launcher", "Removing view at bottom:"
							// + (indexInItems - 1) / 2);
							//
							// bottomDock.removeViewAt((indexInItems) / 2);
							// }
							// }
							int index = getItemIndex(selectedView,((LinearLayout) selectedView
									.getParent()));
							items.remove(selectedItem);
							Log.d("Alex Launcher", "Deleting " + indexInItems);
							if (indexInItems % 2 == 0) { // removing from
															// topDock
								bottomDock.removeViews(index,
										bottomDock.getChildCount() - index);
								topDock.removeViews(index,
										topDock.getChildCount() - index);
							} else { // removing from bottomDock
								Log.d("Alex Launcher", "Shift from " + index
										+ "; total count "
										+ (topDock.getChildCount() - index - 1));
								topDock.removeViews(index + 1,
										topDock.getChildCount() - index - 1);
								bottomDock.removeViews(index,
										bottomDock.getChildCount() - index);



							}
							for (int i = indexInItems; i < items.size(); i++) {
								if (i % 2 == 0) {
									addItemView(items.get(i), topDock);
								} else {
									addItemView(items.get(i), bottomDock);
								}

							}
							selectedView = null;
							selectedItem = null;
							saveChanges();

						}
					}
				});

		((ImageButton) findViewById(R.id.refresh))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						selectedItem = null;
						selectedView = null;
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

	private LinearLayout getBottomDock() {
		return ((LinearLayout) findViewById(R.id.bottomDock));
	}

	private LinearLayout getTopDock() {
		return ((LinearLayout) findViewById(R.id.topDock));
	}

	void updateItems() {
		items = ItemFactory.loadItems(this);
		dumpItems();
		topDock = getTopDock();
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
		dumpItems();
		long time = System.currentTimeMillis() - start;

		Log.d("TEST", "Time:" + time);
	}

	private void dumpItems() {
		for (int i = 0; i < items.size(); i++) {
			Log.d("Alex Launcher", "Item at " + String.valueOf(i) + ":"
					+ items.get(i).getCaption());
		}
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
