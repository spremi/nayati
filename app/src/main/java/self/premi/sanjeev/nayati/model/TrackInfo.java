/*
 * [Nayati] TrackInfo.java
 *
 * Defines model class representing tracking information associated with a package.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.model;


/**
 * Model class representing tracking information associated with a package.
 */
public class TrackInfo {
    /**
     * Id (from database). Else -1.
     */
    private long id;

    /**
     * Time stamp of the information
     */
    private String date;

    /**
     * Country (of current location)
     */
    private String country;

    /**
     * Current location
     */
    private String currLoc;

    /**
     * Event description
     */
    private String event;

    /**
     * Next location (as result of event)
     */
    private String nextLoc;

    /**
     * Mail category
     */
    private String mailCat;

    /**
     * Associated item id
     */
    private long itemId;

    public TrackInfo() {
    }


    public TrackInfo(long id,
                     String date,
                     String country,
                     String currLoc,
                     String event,
                     String nextLoc,
                     String mailCat,
                     long itemId) {
        this.id = id;
        this.date = date;
        this.country = country;
        this.currLoc = currLoc;
        this.event = event;
        this.nextLoc = nextLoc;
        this.mailCat = mailCat;
        this.itemId = itemId;
    }

    public TrackInfo(String date,
                     String country,
                     String currLoc,
                     String event,
                     String nextLoc,
                     String mailCat,
                     long itemId) {
        this(-1, date, country, currLoc, event, nextLoc, mailCat, itemId);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(String currLoc) {
        this.currLoc = currLoc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNextLoc() {
        return nextLoc;
    }

    public void setNextLoc(String nextLoc) {
        this.nextLoc = nextLoc;
    }

    public String getMailCat() {
        return mailCat;
    }

    public void setMailCat(String mailCat) {
        this.mailCat = mailCat;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }


    /**
     * Compare data fields against data fields of argument, ignoring 'id' and 'itemId'.
     *
     * @param arg   Object to match contents with
     *
     * @return  Status of comparison.
     */
    public boolean matches(TrackInfo arg) {
        if ((this.date.equals(arg.date)) &&
            (this.country.equals(arg.country)) &&
            (this.currLoc.equals(arg.currLoc)) &&
            (this.event.equals(arg.event)) &&
            (this.nextLoc.equals(arg.nextLoc)) &&
            (this.mailCat.equals(arg.mailCat))) return true;

        return false;
    }


    @Override
    public String toString() {
        return "TrackInfo [" + id + "] " + id +
                            ", " + date +
                            ", " + country +
                            ", " + currLoc +
                            ", " + event +
                            ", " + nextLoc +
                            ", " + mailCat +
                            ", " + itemId;
    }
}
