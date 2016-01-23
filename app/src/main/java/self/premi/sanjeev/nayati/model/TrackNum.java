/*
 * [Nayati] TrackNum.java
 *
 * Defines a model class representing a tracking number.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.model;


/**
 * Defines tracking number
 */
public class TrackNum {

    /**
     * Unknown format
     */
    public static final int F_NONE = -1;

    /**
     * EMS (Domestic & International)
     */
    public static final int F_EMS = 1;

    /**
     * Registered mail
     */
    public static final int F_REG = 2;

    /**
     * Express parcel post
     */
    public static final int F_EPP = 3;

    /**
     * Electronic Money Order
     */
    public static final int F_EMO = 4;

    /**
     * Regular expression - EMS
     */
    private final String regexEMS = "(EE)([0-9]{9})([A-Z]{2})";

    /**
     * Regular expression - Registered Post
     */
    private final String regexREG = "(R[A-Z])([0-9]{9})([A-Z]{2})";

    /**
     * Regular expression - Express Parcel
     */
    private final String regexEPP = "([A-Z]{2})([0-9]{9})([A-Z]{2})";

    /**
     * Regular expression - Electronic Money Order
     */
    private final String regexEMO = "([0-9]{18})";


    /**
     * Tracking number
     */
    private String track;


    /**
     * Constructor
     */
    public TrackNum(String track) {
        this.track = track;
    }


    /**
     * Identify format of the tracking number
     */
    public int identify() {
        int len = track.length();

        //
        // Insufficient characters for a 'trackable' post
        //
        if (len < 13) return F_NONE;

        //
        // Insufficient characters for en eMO
        //
        if ((len > 13) && (len != 18)) return F_NONE;

        if (len == 13) {
            if (track.matches(regexEMS)) return F_EMS;

            if (track.matches(regexREG)) return F_REG;

            if (track.matches(regexEPP)) return F_EPP;
        } else {
            if (track.matches(regexEMO)) return F_EMO;
        }

        return F_NONE;
    }
}
