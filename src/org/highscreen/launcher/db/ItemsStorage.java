package org.highscreen.launcher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ex3ndr
 * Date: 13.07.2010
 * Time: 20:35:12
 * To change this template use File | Settings | File Templates.
 */
public class ItemsStorage {

    public static class Item {
        public int id;
        public String data;

        public Item(int id, String data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Item)
            {
                return (((Item)o).id == id) && (((Item)o).data.equals(data));
            }

            return false;
        }
    }

    static class ItemsDataBase extends SQLiteOpenHelper {
        private static final int VERSION = 1;

        private static final String ITEM_ID = "item_id";
        private static final String ITEM_DATA = "item_data";

        private static final String TABLE_ITEMS = "items";

        private static final String CREATE_TABLE = "CREATE TABLE items (item_id INTEGER, item_data TEXT);";

        public ItemsDataBase(Context context) {
            super(context, "items.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS items");
            onCreate(sqLiteDatabase);
        }

        public ArrayList<Item> getItems() {
            ArrayList<Item> res = new ArrayList<Item>();
            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();

                Cursor c = db.query(TABLE_ITEMS, new String[]{ITEM_ID, ITEM_DATA}, null, null, null, null, null);
                int id_index = c.getColumnIndex(ITEM_ID);
                int data_index = c.getColumnIndex(ITEM_DATA);

                if (c.moveToFirst()) {
                    res.add(new Item(c.getInt(id_index), c.getString(data_index)));
                    while (c.moveToNext()) {
                        res.add(new Item(c.getInt(id_index), c.getString(data_index)));
                    }
                }
            }
            finally {
                if (db != null)
                    db.close();
            }


            return res;
        }

        public void saveItems(Item[] items) {
            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();
                db.delete(TABLE_ITEMS, null, null);

                ContentValues values = new ContentValues();
                for(Item i : items)
                {
                    values.put(ITEM_ID, i.id);
                    values.put(ITEM_DATA, i.data);

                    db.insert(TABLE_ITEMS, null, values);
                }
            }
            finally {
                if (db != null)
                    db.close();
            }
        }
    }

    private static Thread saver = null;

    public static void saveItems(final Item[] items, final Context context) {
        while (saver!=null) try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        saver = new Thread()
        {
            @Override
            public void run() {
                try
                {
                new ItemsDataBase(context).saveItems(items);
                }
                finally
                {
                    saver = null;
                }
            }
        };
        saver.start();
    }

    public static Item[] loadItems(Context context) {

        while (saver!=null) try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ItemsDataBase(context).getItems().toArray(new Item[0]);
    }
}
