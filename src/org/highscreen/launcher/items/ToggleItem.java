package org.highscreen.launcher.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 6:47:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class ToggleItem extends AbstractItem {

    public abstract String getImageId();

    public abstract boolean isOn();

    public abstract void toggle();

    private Drawable createDrawableOnNorm(Context context) {
        return createDrawable(context, getImageId() + "_on");
    }

    private Drawable createDrawableOnPressed(Context context) {
        return createDrawable(context, getImageId() + "_on_focus");
    }

    private Drawable createDrawableOn(Context context) {
        android.graphics.drawable.StateListDrawable image = new StateListDrawable();
        image.addState(new int[]{-android.R.attr.state_pressed}, createDrawableOnNorm(context));
        image.addState(new int[]{android.R.attr.state_pressed}, createDrawableOnPressed(context));
        return image;
    }

    private Drawable createDrawableOffNorm(Context context) {
        return createDrawable(context, getImageId() + "_off");
    }

    private Drawable createDrawableOffPressed(Context context) {
        return createDrawable(context, getImageId() + "_off_focus");
    }

    private Drawable createDrawableOff(Context context) {
        android.graphics.drawable.StateListDrawable image = new StateListDrawable();
        image.addState(new int[]{-android.R.attr.state_pressed}, createDrawableOffNorm(context));
        image.addState(new int[]{android.R.attr.state_pressed}, createDrawableOffPressed(context));
        return image;
    }

    ImageButton view = null;

    protected void updateView() {
        ImageButton b = (ImageButton) view;
        if (isOn())
            b.setImageDrawable(createDrawableOn(b.getContext()));
        else
            b.setImageDrawable(createDrawableOff(b.getContext()));
    }

    @Override
    public View createView(Context context) {
        if (this.view == null) {
            
            ImageButton view = new ImageButton(context);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (isEditing)
                    {
                        if (listener!=null)
                            listener.onItemSelected(ToggleItem.this);
                    }
                        else
                            toggle();

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (editListener!=null)
                        {
                            editListener.onStartEdit(ToggleItem.this);
                            return true;
                        }
                        return false;
                    }
                });

            view.setPadding(0, 0, 0, 0);
            view.setLayoutParams(new ViewGroup.LayoutParams(96, 144));

            this.view = view;
        }

        updateView();

        return this.view;
    }
}
