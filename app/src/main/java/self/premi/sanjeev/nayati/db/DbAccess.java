/*
 * [Nayati] DbAccess.java
 *
 * Implements base class for database access objects.
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


/**
 * Base class for database access objects.
 * Ensure database object is accessible from derived classes.
 */
public class DbAccess {
    /*
     * Instance of database object
     */
    protected SQLiteDatabase db;

    /*
     * Instance of database helper
     */
    private DbHelper dbh;

    private Context ctx;


    /**
     * Constructor
     */
    public DbAccess(Context ctx) {
        this.ctx = ctx;
        this.dbh = DbHelper.getInstance(ctx);
    }

    /**
     * Open database for access in specified mode.
     */
    public void open(int mode) throws SQLException
    {
        if (mode == DbConst.RO_MODE) {
            db = dbh.getReadableDatabase();
        } else if (mode == DbConst.RW_MODE) {
            db = dbh.getWritableDatabase();
        }
    }

    /**
     * Close database.
     */
    public void close()
    {
        dbh.close();
        db = null;
    }

}
