/*
 * [Nayati] ItemClickListener.java
 *
 * Defines interface for a click event listener associated with an item
 * in the recycler view.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;


/**
 * Interface for click event listener for an item in the recycler view.
 */
public interface ItemClickListener {
    /**
     * Handle click event for an item at specified position.
     */
    void onItemClick(int position);
}
