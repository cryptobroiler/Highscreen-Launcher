package org.highscreen.launcher;

import org.highscreen.launcher.items.AbstractItem;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 19:55:06
 * To change this template use File | Settings | File Templates.
 */
public class AddItemDialog extends Dialog {

    public interface OnAddItemDialogResultListener {
        public void onAddItemDialogResult(AbstractItem item);
    }

    OnAddItemDialogResultListener listener;

    public AddItemDialog(Context context, OnAddItemDialogResultListener listener) {
        super(context, R.style.Theme_Translucent_NoTitleBar);
        setContentView(org.highscreen.launcher.R.layout.add_item);

        this.listener = listener;

        LinearLayout apps = ((LinearLayout) findViewById(org.highscreen.launcher.R.id.apps));
        apps.removeAllViews();

        for (final AbstractItem i : ItemFactory.loadUnselectedItems(context)) {
            apps.addView(i.createView(context));

            i.startEdit();
            i.setItemSelectedListener(new AbstractItem.OnItemSelectedListener() {
                public void onItemSelected(AbstractItem item) {

                    //i.stopEdit();

                    if (AddItemDialog.this.listener != null)
                        AddItemDialog.this.listener.onAddItemDialogResult(i);

                    AddItemDialog.this.dismiss();
                }
            });
        }

        ((ImageButton)findViewById(org.highscreen.launcher.R.id.exit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddItemDialog.this.cancel();
            }
        });
    }
}