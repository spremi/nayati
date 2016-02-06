/*
 * [Nayati] TrackItemView.java
 *
 * Defines model class representing 'resolved' view of the package being tracked.
 * The resolution shall be done by SQL 'join' query from the database.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */

package self.premi.sanjeev.nayati.model;


/**
 * Model class representing 'resolved' view of package being tracked.
 */
public class TrackItemView {
    /**
     * Id
     */
    private long id;

    /**
     * Tracking number
     */
    private String trackNum;

    /**
     * Name of the item
     */
    private String name;

    /**
     * Item category
     */
    private String category;

    /**
     * Item state
     */
    private int state;

    /**
     * Last sync time as string.
     */
    private String sync;


    public TrackItemView(long id, String trackNum, String name, String category, int state, String sync) {
        this.id = id;
        this.trackNum = trackNum;
        this.name = name;
        this.category = category;
        this.state = state;
        this.sync = sync;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getTrackNum() {
        return trackNum;
    }

    public int getState() {
        return state;
    }

    public String getSync() {
        return sync;
    }

    @Override
    public String toString() {
        return "TrackItemView [" + id + "] " + trackNum + ", " + name + ", " +
                category + ", " + state  + ", " + sync;
    }
}
