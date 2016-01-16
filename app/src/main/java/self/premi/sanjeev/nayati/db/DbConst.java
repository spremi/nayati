/*
 * [Nayati] DbConst.java
 *
 * Defines constants used in database operations.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.db;


/**
 * Defines constants used in database operations.
 */
public interface DbConst {
    /*
     * Name of database file.
     * First letter MUST be capital, else the check for presence of file would fail.
     */
    String DB_FILE  = "Nayati.db";

    /*
     * Database creation status
     */
    int DB_CREATE_FAILURE       = 0;                // Failed to create database
    int DB_CREATE_SUCCESS       = 1;                // Successfully created database
    int DB_EXISTS_ALREADY       = 2;                // Database already exists


    /*
     * Table containing category of items
     */
    String TABLE_ITEMCATS       = "ITEMCATS";

    String ITEMCAT_ID           = "_id";                // ID
    String ITEMCAT_NAME         = "name";               // Category name
    String ITEMCAT_SYMBOL       = "symbol";             // Symbol representing the category

    /*
     * Table containing items being tracked
     */
    String TABLE_ITEMS          = "ITEMS";

    String ITEM_ID              = "_id";                // ID
    String ITEM_TNUM            = "tnum";               // Tracking number
    String ITEM_NAME            = "name";               // Item name
    String ITEM_ICAT            = "icat";               // Item category
    String ITEM_STATE           = "state";              // Item state
    String ITEM_SYNC            = "sync";               // Item sync time

    /*
     * Table containing tracking information
     */
    String TABLE_TRACKING       = "TRACKING";

    String TRACK_ID             = "_id";                // ID
    String TRACK_TIME           = "time";               // Timestamp
    String TRACK_COUNTRY        = "country";            // Country
    String TRACK_CLOCATION      = "cloc";               // Current location
    String TRACK_EVENT          = "event";              // Event
    String TRACK_NLOCATION      = "nloc";               // Next location
    String TRACK_MCAT           = "mcat";               // Mail category
    String TRACK_ITEMID         = "itemid";             // ID of associated item

    /*
     * Database access modes
     */
    int RO_MODE     = 0;        // Read-only
    int RW_MODE     = 1;        // Read-write


    /*
     * SQL statements to create tables
     */
    String CREATE_TABLE_ITEMCATS =
            "CREATE TABLE " + TABLE_ITEMCATS +	"(" +
                    ITEMCAT_ID      + " INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                    ITEMCAT_NAME    + " TEXT UNIQUE," +
                    ITEMCAT_SYMBOL  + " TEXT UNIQUE" +
                    ");";

    String CREATE_TABLE_ITEMS =
            "CREATE TABLE " + TABLE_ITEMS +	"(" +
                    ITEM_ID         + " INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                    ITEM_TNUM       + " TEXT," +
                    ITEM_NAME       + " TEXT," +
                    ITEM_ICAT       + " INTEGER," +
                    ITEM_STATE      + " INTEGER," +
                    ITEM_SYNC       + " TEXT," +

                    "FOREIGN KEY(" + ITEM_ICAT + ") " +
                    "REFERENCES " + TABLE_ITEMCATS + "(" + ITEMCAT_ID  + ")" +
                    ");";

    String CREATE_TABLE_TRACKING =
            "CREATE TABLE " + TABLE_TRACKING +	"(" +
                    TRACK_ID        + " INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                    TRACK_TIME      + " TEXT," +
                    TRACK_COUNTRY   + " TEXT," +
                    TRACK_CLOCATION + " TEXT," +
                    TRACK_EVENT     + " TEXT," +
                    TRACK_NLOCATION + " TEXT," +
                    TRACK_MCAT      + " TEXT," +
                    TRACK_ITEMID    + " TEXT," +

                    "FOREIGN KEY(" + TRACK_ITEMID + ") " +
                    "REFERENCES " + TABLE_ITEMS + "(" + ITEM_ID  + ")" +

                    ");";


    /*
     * SQL statements to initialize item categories
     */
    String INIT_ITEM_CATEGORIES[] = {
            "INSERT INTO " + TABLE_ITEMCATS + "('" +
                    ITEMCAT_NAME + "','" +
                    ITEMCAT_SYMBOL + "') VALUES('Gift', 'G');",
            "INSERT INTO " + TABLE_ITEMCATS + "('" +
                    ITEMCAT_NAME + "','" +
                    ITEMCAT_SYMBOL + "') VALUES('Personal', 'P');",
            "INSERT INTO " + TABLE_ITEMCATS + "('" +
                    ITEMCAT_NAME + "','" +
                    ITEMCAT_SYMBOL + "') VALUES('Documents', 'D');"
    };


    /*
     * Typical states of an item
     */
    int ITEM_STATE_NONE         = -1;               // Unknown
    int ITEM_STATE_TRANSIT      = 1;                // In transit
    int ITEM_STATE_FINAL        = 2;                // Reached final destination

}
