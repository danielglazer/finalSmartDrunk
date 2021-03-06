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

import java.util.Calendar;

import tk.smartdrunk.smartdrunk.Adapters.TabListAdapter;
import tk.smartdrunk.smartdrunk.R;

import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.scheduleClient;

/**
 * Created by Daniel on 24/11/2016.
 */

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    //private DatabaseReference userTabsDB;
    private SwipeMenuListView list;

    private boolean isFirst;
    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.home_fragment, container, false);




        // Create a new service client and bind our activity to this service
//        Calendar cal = Calendar.getInstance();
//        createNotification(cal,"");

        //set on click listeners
        my_view.findViewById(R.id.addDrinkFab).setOnClickListener(this);
        my_view.findViewById(R.id.button2).setOnClickListener(this);
        list = (SwipeMenuListView) my_view.findViewById(R.id.listView);
        TabListAdapter adapter = new TabListAdapter(getActivity(), R.layout.tab, ((MenuActivity)getActivity()).tabsList);
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
                    case 0: //TODO: Override this function
                        // open
//                        Intent in = new Intent();
//                        tabsList.get(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Left
        list.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        return my_view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addDrinkFab) {
            addDrink();
        } else if(i == R.id.button2) {
           notifyDemo();
        }
    }

    private void addDrink() {
        startActivity(new Intent(getView().getContext(), AddDrinkActivity.class));
    }

    private void viewTab() {
        return;
    }

    private void notifyDemo(){
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar cal = Calendar.getInstance();
        // Ask our service to set an alarm for that date,
        // this activity talks to the client that talks to the service
//        cal.add(Calendar.SECOND,5);
//        scheduleClient.getmBoundService()
//                .setNotificationString("According to our estimation you are now legally qualify for driving!");
//        scheduleClient.setAlarmForNotification(cal);
//        scheduleClient.getmBoundService().setNotificationString("Did you suffer from a hangover? please let us know!");
//        scheduleClient.setAlarmForNotification(cal);
//        cal.add(Calendar.SECOND,6);
        ((MenuActivity)getActivity()).createNotification(cal,"");
    }
    @Override
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
