/*
 * [Nayati] ItemCategoory.java
 *
 * Defines model class representing an item category.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati.model;


/**
 * Model class representing an item category.
 */
public class ItemCategory {
    /**
     * Id (from database). Else -1.
     */
    private long id;

    /**
     * Name of category
     */
    private String name;

    /**
     * Symbol associated with the category
     */
    private String symbol;



    public ItemCategory(long id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    public ItemCategory(String name, String symbol) {
        this(-1, name, symbol);
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


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    @Override
    public String toString() {
        return "ItemCategory [ " + id + "] " + name + ", " + symbol;
    }
}
