package tk.smartdrunk.smartdrunk.appMenu;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.loginAndRegister.SignInActivity;
import tk.smartdrunk.smartdrunk.models.Tab;
import tk.smartdrunk.smartdrunk.models.User;
import tk.smartdrunk.smartdrunk.notificationsAndAlarms.ScheduleClient;

import static tk.smartdrunk.smartdrunk.models.User.getUid;

public class MenuActivity extends android.support.v4.app.FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MenuActivity";

    //view related
    private NavigationView navigationView;
    FragmentManager fragmentManager;

    // auth related
    static FirebaseAuth mAuth;

    //real time DB related
    DatabaseReference usersDB, userTabsDB, tabDrinksDB;
    static User user;
    ArrayList<Tab> tabsList = new ArrayList<Tab>();
    ArrayList<String> tabStrings = new ArrayList<String>();
//    static Map<Drink,String> tabKeyToDrink =new HashMap<>();
    //notification "handler"
    static ScheduleClient scheduleClient;

    // AddDrinkActivity.java related
    static Tab currentTab;
    static String currentTabKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        currentTab = null;
        currentTabKey = null;
        mAuth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // set home as default fragment
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersDB = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());
        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        usersDB.getRef().addValueEventListener(userListener);
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        ValueEventListener tabsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tabsList.clear();
                tabStrings.clear();
                // loop 1 tab at a time
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Tab tab = ds.getValue(Tab.class);
                    tabsList.add(tab);
                    tabStrings.add(ds.getKey());
                    // We need to search for an open tab if found add a drink to this tab
                    // if not open one in to it
                    if (tab.getTabCloseDate().equals("Not Yet")) {
                        currentTab = tab;
                        currentTabKey = ds.getKey();
                    }
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment(), "homeFragment").commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userTabsDB.getRef().addValueEventListener(tabsListener);
    }

    @Override
    public void onBackPressed() {
        //always go back to homeFragment
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().findFragmentByTag("homeFragment") == null) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment(), "homeFragment").commit();
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                navigationView.setCheckedItem(R.id.nav_home);
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment(), "homeFragment").commit();
        } else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment(), "profileFragment").commit();
        } else if (id == R.id.nav_statistics) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new StatisticFragment(), "statisticsFragment").commit();
        } else if (id == R.id.nav_Driver) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new DriveFragment(), "driveFragment").commit();
        } else if (id == R.id.nav_Hangover) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HangoverFragment(), "hangoverFragment").commit();
        } else if (id == R.id.nav_Info) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new InfoFragment(), "infoFragment").commit();
        } else if (id == R.id.nav_Emergency_Contact) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EmergencyContactFragment(), "emergencyFragment").commit();
        } else if (id == R.id.nav_signOut) {
            signOut();
        } else if (id == R.id.nav_share) {
            Resources resources = getResources();

            Intent emailIntent = new Intent();
            emailIntent.setAction(Intent.ACTION_SEND);
            // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
            emailIntent.setType("message/rfc822");

            PackageManager pm = getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");


            Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                if (packageName.contains("android.email")) {
                    emailIntent.setPackage(packageName);
                } else if (packageName.contains("twitter") || packageName.contains("facebook") ||
                        packageName.contains("mms")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    if (packageName.contains("twitter")) {
                        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter));
                    } else if (packageName.contains("facebook")) {
                        // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves.
                        // Pre-filling these fields erodes the authenticity of the user voice.
                        // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they
                        // want to share. We can also make a custom landing page, and the link
                        // will show the <meta content ="..."> text from that page with our link in Facebook.
                        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook));
                    } else if (packageName.contains("sms")) {
                        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms));
                    }

                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mAuth.signOut();
        // Go to SignInActivity
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * @param cal - the time in which the notification is due
     * @param msg - a message for the body of the notification
     */
     public void createNotification(Calendar cal, String msg) {
        scheduleClient.getmBoundService().setNotificationString(msg);
        scheduleClient.setAlarmForNotification(cal);
    }

    @Override
    public void onStop() {
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
