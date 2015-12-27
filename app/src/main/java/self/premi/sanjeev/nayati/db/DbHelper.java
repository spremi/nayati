/*
 * [Nayati] DbHelper.java
 *
 * Implements helper for database operations.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Helper for database operations. Uses singleton pattern.
 */
public class DbHelper extends SQLiteOpenHelper {
    /**
     * Name of database
     */
    private static final String DATABASE_NAME = "Nayati.db";

    /**
     * Version of database
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Instance of "this" class
     */
    private static DbHelper self;


    /**
     * Constructor
     */
    private DbHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    /**
     * Get instance of this object.
     */
    public static synchronized DbHelper getInstance(Context context)
    {
        if (self == null) {
            self = new DbHelper(context.getApplicationContext());
        }

        return self;
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db)
    {
        try {
            db.execSQL(DbConst.CREATE_TABLE_ITEMCATS);
            db.execSQL(DbConst.CREATE_TABLE_ITEMS);
            db.execSQL(DbConst.CREATE_TABLE_TRACKING);
        } catch (SQLException e) {
            // TODO: Handle exception
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
         * TODO:
         * 1) Export data from existing tables
         * 2) Drop tables
         * 3) Create new tables
         * 4) Import data exported in step 1.
         */

        onCreate(db);
    }

}
