/*
 * [Nayati] DaoItemCategory.java
 *
 * Implements 'Database Access Object' for item category.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import self.premi.sanjeev.nayati.model.ItemCategory;


/**
 * 'Database Access Object' for item category
 */
public class DaoItemCategory extends DbAccess {

    /**
     * Constructor
     */
    public DaoItemCategory(Context ctx) {
        super(ctx);
    }


    /**
     * Adds an item category.
     */
    public long add(ItemCategory cat)
    {
        long id = -1;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.ITEMCAT_NAME, cat.getName());
        cv.put(DbConst.ITEMCAT_SYMBOL, cat.getSymbol());

        db.beginTransaction();
        try {
            id = db.insert(DbConst.TABLE_ITEMCATS, null, cv);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return id;
    }

    public long add(String name, String symbol)
    {
        return add(new ItemCategory(name, symbol));
    }


    /**
     * Deletes an item category.
     */
    public int delete(long id)
    {
        int rows = 0;

        db.beginTransaction();
        try {
            rows = db.delete(DbConst.TABLE_ITEMCATS, DbConst.ITEMCAT_ID + "=" + id, null);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return rows;
    }

    /**
     * Updates an item category.
     */
    public boolean update(ItemCategory cat)
    {
        long id = cat.getId();

        long ret;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.ITEMCAT_NAME, cat.getName());

        db.beginTransaction();
        try {
            ret = db.update(DbConst.TABLE_ITEMCATS, cv, DbConst.ITEMCAT_ID + "=" + id, null);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (ret == 0) return false;

        return true;
    }

    /**
     * List all item categories.
     */
    public List<ItemCategory> list()
    {
        List<ItemCategory> items = new ArrayList<>();

        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(DbConst.TABLE_ITEMCATS, null, null, null, null, null, null);

            db.setTransactionSuccessful();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        long id       = c.getLong(c.getColumnIndex(DbConst.ITEMCAT_ID));
                        String name   = c.getString(c.getColumnIndex(DbConst.ITEMCAT_NAME));
                        String symbol = c.getString(c.getColumnIndex(DbConst.ITEMCAT_SYMBOL));

                        items.add(new ItemCategory(id, name, symbol));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
        }

        c.close();

        return items;
    }

    /**
     * Get item category for the specified Id.
     */
    public ItemCategory get(long id)
    {
        ItemCategory cat = null;

        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(DbConst.TABLE_ITEMCATS,
                    null,
                    DbConst.ITEMCAT_ID + " = " + "\"" + id + "\"",
                    null, null, null, null);

            db.setTransactionSuccessful();

            if (c != null) {
                if (c.moveToFirst()) {
                    long cid      = c.getLong(c.getColumnIndex(DbConst.ITEMCAT_ID));
                    String name   = c.getString(c.getColumnIndex(DbConst.ITEMCAT_NAME));
                    String symbol = c.getString(c.getColumnIndex(DbConst.ITEMCAT_SYMBOL));

                    cat = new ItemCategory(cid, name, symbol);
                }
            }
        }
        finally {
            db.endTransaction();
        }

        c.close();

        return cat;
    }
}
