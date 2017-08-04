package tk.smartdrunk.smartdrunk.appMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.models.Drink;

import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.tabStrings;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.tabsList;
import static tk.smartdrunk.smartdrunk.models.User.getUid;

/**
 * Created by Daniel on 24/11/2016.
 */

public class StatisticFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StatisticFragment";
    private DatabaseReference tabDrinksDB;
    private ArrayList<String> tabkeyStrings = new ArrayList<String>();
    private Map<Drink, String> drinkToKey = new HashMap<>();
    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.statistics_layout, container, false);
        //TODO: get user drinks
        tabDrinksDB = FirebaseDatabase.getInstance().getReference().child("tab-drinks").child(getUid());
        ValueEventListener drinksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the drinks of the user
                tabsList.clear();
                tabStrings.clear();
                // loop 1 tab at a time
                for (DataSnapshot tab : dataSnapshot.getChildren()) {
                    tabkeyStrings.add(tab.getKey());
                    // loop 1 drink at a time
                    for (DataSnapshot drink : tab.getChildren()) {
                        Drink d = drink.getValue(Drink.class);
                        drinkToKey.put(d, tab.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        tabDrinksDB.getRef().addValueEventListener(drinksListener);
        return my_view;
    }
}
