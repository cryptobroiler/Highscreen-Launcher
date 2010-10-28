package org.highscreen.launcher.items;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 6:29:42
 * To change this template use File | Settings | File Templates.
 */
public class ReadingNowItem extends ButtonItem {

    @Override
    protected String getImageId() {
        return "com.bravo.ReadingNow";
    }

    @Override
    public int getType() {
        return AbstractItem.TYPE_READING_NOW;
    }

    @Override
    public String getData() {
        return "";
    }

    @Override
    public String getCaption() {
        return "ReadingNow";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ReadingNowItem)
            return ((ReadingNowItem)o).getType() == getType();
        
        return false;
    }

    @Override
    public void onAction(Context context) {
        context.startActivity(getReadingNowIntent(context));
    }

    private Intent getReadingNowIntent(Context context) {
        Intent intent = null;
        try {
            Cursor c =
                    context.getContentResolver().query(Uri.parse("content://com.ereader.android/last"), null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                byte[] data = c.getBlob(0);
                c.close();
                c.deactivate();
                if (data == null) {
                    return null;
                }
                DataInputStream din = new DataInputStream(new ByteArrayInputStream(data));
                intent = new Intent();
                String tmp = din.readUTF();
                intent.setAction(tmp);
                tmp = din.readUTF();
                String tmp1 = din.readUTF();
                if (tmp != null && tmp.length() > 0) {
                    Uri uri = Uri.parse(tmp);
                    if (tmp1 != null && tmp1.length() > 0) {
                        intent.setDataAndType(uri, tmp1);
                    } else {
                        intent.setData(uri);
                    }
                }
                byte b = din.readByte();
                if (b > 0) {
                    tmp = din.readUTF();
                    tmp1 = din.readUTF();
                    intent.putExtra(tmp, tmp1);
                }
            }
        } catch (Exception ex) {
            intent = null;
        } finally {
            if (intent == null) {
                Toast.makeText(context, "You do not have a current book open right now.", Toast.LENGTH_SHORT).show();
            }
        }
        return intent;
    }
}
