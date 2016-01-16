/*
 * [Nayati] TrackItem.java
 *
 * Defines model class representing package being tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */

package self.premi.sanjeev.nayati.model;


/**
 * Model class representing package being tracked.
 */
public class TrackItem {
    /**
     * Id (from database). Else -1.
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
    private long category;

    /**
     * Item state (-1 for unknown)
     */
    private int state;

    /**
     * Last sync time as string. ("" for never)
     */
    private String sync;


    public TrackItem(long id, String trackNum, String name, long category, int state, String sync) {
        this.id = id;
        this.trackNum = trackNum;
        this.name = name;
        this.category = category;
        this.state = state;
        this.sync = sync;
    }

    public TrackItem(long id, String trackNum, String name, long category, int state) {
        this(id, trackNum, name, category, state, "");
    }

    public TrackItem(String trackNum, String name, long category) {
        this(-1, trackNum, name, category, -1, "");
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(String trackNum) {
        this.trackNum = trackNum;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return "TrackItem [" + id + "] " + trackNum + ", " + name + ", " +
                                            category + ", " + state  + ", " + sync;
    }
}
