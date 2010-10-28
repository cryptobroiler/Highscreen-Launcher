package org.highscreen.launcher.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 3:31:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class ButtonItem extends AbstractItem {

    public abstract void onAction(Context context);

    protected abstract String getImageId();

    private Drawable createDrawable(Context context) {
        return createDrawable(context, getImageId());
    }

    private Drawable createDrawablePressed(Context context) {
        return createDrawable(context, getImageId() + "_focus");
    }

    protected Drawable getImageDrawable(Context context) {
        Drawable normal = createDrawable(context);
        if (normal == null)
            return null;
        Drawable pressed = createDrawablePressed(context);
        if (pressed == null)
            return null;

        android.graphics.drawable.StateListDrawable image = new StateListDrawable();
        image.addState(new int[]{-android.R.attr.state_pressed}, normal);
        image.addState(new int[]{android.R.attr.state_pressed}, pressed);

        return image;
    }

    protected View createViewInternal(final Context context) {
        Drawable image = getImageDrawable(context);

        if (image != null) {

            ImageButton view = new ImageButton(context);

            view.setImageDrawable(image);

            //view.setBackgroundColor(Color.TRANSPARENT);

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (isEditing) {
                        if (listener != null)
                            listener.onItemSelected(ButtonItem.this);
                    } else
                        onAction(context);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (editListener != null) {
                        editListener.onStartEdit(ButtonItem.this);
                        return true;
                    }
                    return false;
                }
            });
            return view;
        }
        return null;
    }

    private View view;

    @Override
    public View createView(final Context context) {
        if (view == null) {

            view = createViewInternal(context);

            if (view == null) {
                Button view = new Button(context);

                view.setText(getCaption());

                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (isEditing) {
                            if (listener != null)
                                listener.onItemSelected(ButtonItem.this);
                        } else
                            onAction(context);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (editListener != null) {
                            editListener.onStartEdit(ButtonItem.this);
                            return true;
                        }
                        return false;
                    }
                });

                this.view = view;
            }

            view.setPadding(0, 0, 0, 0);
            view.setLayoutParams(new ViewGroup.LayoutParams(96, 144));
        }
        return view;
    }
}
