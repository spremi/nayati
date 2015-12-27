/*
 * [Nayati] DaoTrackItem.java
 *
 * Implements 'Database Access Object' for the item being tracked.
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

import self.premi.sanjeev.nayati.model.TrackItem;


/**
 * 'Database Access Object' for the item being tracked.
 */
public class DaoTrackItem extends DbAccess {

    /**
     * Constructor
     */
    public DaoTrackItem(Context ctx) {
        super(ctx);
    }

    /**
     * Adds an item to be tracked.
     *
     * When item is being added, the state is expected to be unknown.
     */
    public long add(String tnum, String name, long cat)
    {
        long id = -1;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.ITEM_TNUM, tnum);
        cv.put(DbConst.ITEM_NAME, name);
        cv.put(DbConst.ITEM_ICAT, cat);
        cv.put(DbConst.ITEM_STATE, DbConst.ITEM_STATE_NONE);

        db.beginTransaction();
        try {
            id = db.insert(DbConst.TABLE_ITEMS, null, cv);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return id;
    }


    /**
     * Deletes an item being tracked.
     */
    public int delete(long id)
    {
        int rows = 0;

        db.beginTransaction();
        try {
            rows = db.delete(DbConst.TABLE_ITEMS, DbConst.ITEM_ID + "=" + id, null);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return rows;
    }

    /**
     * Updates an item being tracked.
     */
    public boolean update(TrackItem item)
    {
        long id = item.getId();

        long ret;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.ITEM_TNUM, item.getTrackNum());
        cv.put(DbConst.ITEM_NAME, item.getName());
        cv.put(DbConst.ITEM_ICAT, item.getCategory());
        cv.put(DbConst.ITEM_STATE, item.getState());

        db.beginTransaction();
        try {
            ret = db.update(DbConst.TABLE_ITEMS, cv, DbConst.ITEM_ID + "=" + id, null);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (ret == 0) return false;

        return true;
    }

    /**
     * List all items.
     */
    public List<TrackItem> list()
    {
        List<TrackItem> items = new ArrayList<>();

        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(DbConst.TABLE_ITEMS, null, null, null, null, null, null);

            db.setTransactionSuccessful();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        long id     = c.getLong(c.getColumnIndex(DbConst.ITEM_ID));
                        String tnum = c.getString(c.getColumnIndex(DbConst.ITEM_TNUM));
                        String name = c.getString(c.getColumnIndex(DbConst.ITEM_NAME));
                        long cat    = c.getLong(c.getColumnIndex(DbConst.ITEM_ICAT));
                        int state   = c.getInt(c.getColumnIndex(DbConst.ITEM_STATE));

                        items.add(new TrackItem(id, tnum, name, cat, state));
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
     * Get an item.
     */
    public TrackItem get(String tnum)
    {
        TrackItem item = null;

        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(DbConst.TABLE_ITEMS,
                    null,
                    DbConst.ITEM_TNUM + " = " + "\"" + tnum + "\"",
                    null, null, null, null);

            db.setTransactionSuccessful();

            if (c != null) {
                if (c.moveToFirst()) {
                    long id     = c.getLong(c.getColumnIndex(DbConst.ITEM_ID));
                    String name = c.getString(c.getColumnIndex(DbConst.ITEM_NAME));
                    long cat    = c.getLong(c.getColumnIndex(DbConst.ITEM_ICAT));
                    int state   = c.getInt(c.getColumnIndex(DbConst.ITEM_ICAT));

                    item = new TrackItem(id, tnum, name, cat, state);
                }
            }
        }
        finally {
            db.endTransaction();
        }

        c.close();

        return item;
    }

}
