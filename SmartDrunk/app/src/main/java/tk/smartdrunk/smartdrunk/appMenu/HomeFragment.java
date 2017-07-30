package tk.smartdrunk.smartdrunk.appMenu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tk.smartdrunk.smartdrunk.AddDrinkActivity;
import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.TabListAdapter;
import tk.smartdrunk.smartdrunk.models.Tab;

/**
 * Created by Daniel on 24/11/2016.
 */

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private DatabaseReference userTabsDB;
    private DatabaseReference mDatabase;
    private SwipeMenuListView list;
    private ArrayList<Tab> tabs = new ArrayList<Tab>();
    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.home_layout, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("Tabs");
        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        userTabsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: can be done by checking the last tab added
                /*if (isFirst == false) {
                    return;
                }
                isFirst = false;*/
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Tab tab = ds.getValue(Tab.class);
                    tabs.add(tab);
                    // only 1 tab can be open at a time
//                    if (tab.getTabCloseDate().equals("Not Yet")) {
//                        currentTab = tab;
//                        currentTabKey = ds.getKey();
//                        return;
//                    }
                }
                // no tabs open so new one needed
                // currentTabKey = null
                TabListAdapter adapter = new TabListAdapter(getActivity(), R.layout.tab, tabs);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        my_view.findViewById(R.id.addDrinkFab).setOnClickListener(this);

        list = (SwipeMenuListView) my_view.findViewById(R.id.listView);

        TabListAdapter adapter = new TabListAdapter(getActivity(), R.layout.tab, tabs);
        list.setAdapter(adapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem closeItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                closeItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                closeItem.setWidth(170);
                // set item title
                closeItem.setTitle("Open");
                // set item title fontsize
                closeItem.setTitleSize(18);
                // set item title font color
                closeItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(closeItem);
            }
        };

        // set creator
        list.setMenuCreator(creator);

        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        list.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        return my_view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addDrinkFab) {
            addDrink();
        } else if (i == R.id.addDrinkFab) {
            viewTab();
        }
    }

    private void addDrink() {
        startActivity(new Intent(getView().getContext(), AddDrinkActivity.class));
    }

    private void viewTab() {
        return;
    }
    public String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        } else {
            return user.getUid();
        }
    }
}
