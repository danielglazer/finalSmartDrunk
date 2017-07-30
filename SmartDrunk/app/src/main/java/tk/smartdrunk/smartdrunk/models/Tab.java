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

    //public Map<String,DrinkOccasion> drinkOccasionMap;
    //public double satisfaction; ///between 0 to 5 stars

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
    public String getTabOpenDate() {
        return tabOpenDate;
    }

    public void setTabOpenDate(String tabOpenDate) {
        this.tabOpenDate = tabOpenDate;
    }

    public String getTabCloseDate() {
        return tabCloseDate;
    }

    public void setTabCloseDate(String tabCloseDate) {
        this.tabCloseDate = tabCloseDate;
    }

    public double getMaxBAC() {
        return maxBAC;
    }

    public void setMaxBAC(double maxBAC) {
        this.maxBAC = maxBAC;
    }

    public String getWasHangover() {
        return wasHangover;
    }

    public void setWasHangover(String wasHangover) {
        this.wasHangover = wasHangover;
    }

}
// [END Tab_class]
