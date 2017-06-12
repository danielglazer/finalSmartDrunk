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

    //public String uid;  - redundant
    //public Map<String,DrinkOccasion> drinkOccasionMap;
    //public double satisfaction; ///between 0 to 5 stars
    public String tabOpenDate;
    public String tabCloseDate;
    public boolean wasHangover;
    public double maxBAC;

    public Tab(){
    }
    public Tab(String tabOpenDate, String tabCloseDate, boolean wasHangover, boolean isOpen,double maxBAC){
        this.tabOpenDate = tabOpenDate;
        this.tabCloseDate = tabCloseDate;
        this.wasHangover = wasHangover;
        this.maxBAC = maxBAC;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tabOpenDate", tabOpenDate);
        result.put("tabCloseDate", tabCloseDate);
        result.put("wasHangover", wasHangover);
        result.put("maxBAC", maxBAC);
        return result;
    }
}
// [END Tab_class]
