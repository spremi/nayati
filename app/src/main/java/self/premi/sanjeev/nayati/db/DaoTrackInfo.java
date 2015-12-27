/*
 * [Nayati] DaoTrackInfo.java
 *
 * Implements 'Database Access Object' for tracking information.
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

import self.premi.sanjeev.nayati.model.TrackInfo;


/**
 * 'Database Access Object' for the tracking information.
 */
public class DaoTrackInfo extends DbAccess {

    /**
     * Constructor
     */
    public DaoTrackInfo(Context ctx) {
        super(ctx);
    }


    /**
     * Adds an item to be tracked.
     */
    public long add(TrackInfo inf)
    {
        long id;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.TRACK_TIME, inf.getDate());
        cv.put(DbConst.TRACK_COUNTRY, inf.getCountry());
        cv.put(DbConst.TRACK_CLOCATION, inf.getCurrLoc());
        cv.put(DbConst.TRACK_EVENT, inf.getEvent());
        cv.put(DbConst.TRACK_NLOCATION, inf.getNextLoc());
        cv.put(DbConst.TRACK_MCAT, inf.getMailCat());
        cv.put(DbConst.TRACK_ITEMID, inf.getItemId());

        db.beginTransaction();
        try {
            id = db.insert(DbConst.TABLE_TRACKING, null, cv);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        return id;
    }

    public long add(String date,
                    String country,
                    String currLoc,
                    String event,
                    String nextLoc,
                    String mailCat,
                    long itemId)
    {
        TrackInfo inf = new TrackInfo(date, country, currLoc, event, nextLoc, mailCat, itemId);

        return add(inf);
    }


    /**
     * Deletes all rows associated with specified 'itemId'.
     */
    public int delete(long itemId)
    {
        int rows = 0;

        db.beginTransaction();
        try {
            rows = db.delete(DbConst.TABLE_TRACKING, DbConst.TRACK_ITEMID + "=" + itemId, null);

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
    public boolean update(TrackInfo inf)
    {
        long id = inf.getId();

        long ret;

        ContentValues cv = new ContentValues();

        cv.put(DbConst.TRACK_TIME, inf.getDate());
        cv.put(DbConst.TRACK_COUNTRY, inf.getCountry());
        cv.put(DbConst.TRACK_CLOCATION, inf.getCurrLoc());
        cv.put(DbConst.TRACK_EVENT, inf.getEvent());
        cv.put(DbConst.TRACK_NLOCATION, inf.getNextLoc());
        cv.put(DbConst.TRACK_MCAT, inf.getMailCat());
        cv.put(DbConst.TRACK_ITEMID, inf.getItemId());

        db.beginTransaction();
        try {
            ret = db.update(DbConst.TABLE_TRACKING, cv, DbConst.TRACK_ID + "=" + id, null);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (ret == 0) return false;

        return true;
    }

    /**
     * List all items matching specified item id.
     */
    public List<TrackInfo> list(long itemId)
    {
        List<TrackInfo> items = new ArrayList<>();

        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(DbConst.TABLE_TRACKING,
                    null,
                    DbConst.TRACK_ITEMID + " = " + itemId,
                    null, null, null, null);

            db.setTransactionSuccessful();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        TrackInfo inf = new TrackInfo();

                        inf.setDate(c.getString(c.getColumnIndex(DbConst.TRACK_TIME)));
                        inf.setCountry(c.getString(c.getColumnIndex(DbConst.TRACK_COUNTRY)));
                        inf.setCurrLoc(c.getString(c.getColumnIndex(DbConst.TRACK_CLOCATION)));
                        inf.setEvent(c.getString(c.getColumnIndex(DbConst.TRACK_EVENT)));
                        inf.setNextLoc(c.getString(c.getColumnIndex(DbConst.TRACK_NLOCATION)));
                        inf.setMailCat(c.getString(c.getColumnIndex(DbConst.TRACK_MCAT)));
                        inf.setItemId(c.getLong(c.getColumnIndex(DbConst.TRACK_ITEMID)));

                        items.add(inf);
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
}
