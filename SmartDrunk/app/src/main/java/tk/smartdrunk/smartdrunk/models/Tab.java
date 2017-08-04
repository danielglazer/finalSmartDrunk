package tk.smartdrunk.smartdrunk.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**********************************************************************
 *  class: Tab (Tab.java)
 *  author: Daniel Glazer
 *  date: 5/22/2017
 *  description:
 **********************************************************************/

// [START Tab_class]
public class Tab {

    private String tabOpenDate;
    private String tabCloseDate; // simple date string or "Not Yet"
    private String wasHangover; // can store the values: 'Yes', 'No' or 'Not Supplied'
    private double maxBAC;

    /*constructors*/
    public Tab() {
        // Default constructor required for calls to DataSnapshot.getValue(Tab.class)
    }

    public Tab(String tabOpenDate, String tabCloseDate, String wasHangover, double maxBAC) {
        this.tabOpenDate = tabOpenDate;
        this.tabCloseDate = tabCloseDate;
        this.wasHangover = wasHangover;
        this.maxBAC = maxBAC;
    }

    /*helpful method*/
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tabOpenDate", tabOpenDate);
        result.put("tabCloseDate", tabCloseDate);
        result.put("wasHangover", wasHangover);
        result.put("maxBAC", maxBAC);
        return result;
    }

    /*Getters and Setters*/

    /**
     * @return tabOpenDate
     */
    public String getTabOpenDate() {
        return tabOpenDate;
    }

    /**
     * set tabOpenDate
     *
     * @param tabOpenDate
     */
    public void setTabOpenDate(String tabOpenDate) {
        this.tabOpenDate = tabOpenDate;
    }

    /**
     * @return tabCloseDate
     */
    public String getTabCloseDate() {
        return tabCloseDate;
    }

    /**
     * set tabCloseDate
     *
     * @param tabCloseDate
     */
    public void setTabCloseDate(String tabCloseDate) {
        this.tabCloseDate = tabCloseDate;
    }

    /**
     * @return maxBAC
     */
    public double getMaxBAC() {
        return maxBAC;
    }

    /**
     * set maxBAC
     *
     * @param maxBAC
     */
    public void setMaxBAC(double maxBAC) {
        this.maxBAC = maxBAC;
    }

    /**
     * @return wasHangover
     */
    public String getWasHangover() {
        return wasHangover;
    }

    /**
     * set wasHangover
     *
     * @param wasHangover
     */
    public void setWasHangover(String wasHangover) {
        this.wasHangover = wasHangover;
    }

}
// [END Tab_class]
