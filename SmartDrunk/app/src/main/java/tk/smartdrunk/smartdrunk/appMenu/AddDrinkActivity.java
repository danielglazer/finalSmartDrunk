package tk.smartdrunk.smartdrunk.appMenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.models.Drink;
import tk.smartdrunk.smartdrunk.models.Tab;

import static java.lang.Integer.parseInt;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.currentTab;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.currentTabKey;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.user;
import static tk.smartdrunk.smartdrunk.models.User.BAC_toAdd;
import static tk.smartdrunk.smartdrunk.models.User.getUid;
import static tk.smartdrunk.smartdrunk.models.User.getUpdatedBAC;

public class AddDrinkActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddDrinkActivity";

    private DatabaseReference userDB, userTabsDB, tabDrinksDB;
    private EditText drinkVolume, alcoholVolume, drinkNumber;
    private TextView servingSize, drinkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        tabDrinksDB = FirebaseDatabase.getInstance().getReference().child("tab-drinks").child(getUid());
        userDB = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());

        drinkVolume = (EditText) findViewById(R.id.drinkVolumeEditText);
        alcoholVolume = (EditText) findViewById(R.id.alcoholVolumeEditText);
        drinkNumber = (EditText) findViewById(R.id.drinkNumberEditText);
        servingSize = (TextView) findViewById(R.id.drinkServingSizeTextView);
        drinkType = (TextView) findViewById(R.id.drinkTypeTextView);

        drinkVolume.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //TODO: show the user his serving size
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no op
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no op
            }

        });
        alcoholVolume.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //TODO: show the user his drink type
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no op
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no op
            }

        });

        findViewById(R.id.addDrinkButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addDrinkButton) {
            addDrink();
        }
    }

    /**
     * add a drink to user open tab
     * or to a newly created tab if an open one has not been active recently
     */
    private void addDrink() {
        if (!validate()) {
            return;
        }

        double beforeNewBAC = getUpdatedBAC(user);

        final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_NOW);
        Calendar cal = Calendar.getInstance();
        String currentDate = format.format(cal.getTime());

        //create a drink
        Drink drink = new Drink(Integer.parseInt(drinkVolume.getText().toString()),
                Double.parseDouble(alcoholVolume.getText().toString()),
                parseInt(drinkNumber.getText().toString()),
                currentDate);

        // We need to recalculate the user BAC
        double newBAC = BAC_toAdd(drink, user);
        newBAC += beforeNewBAC;
        userDB.child("lastBAC").setValue(newBAC);
        userDB.child("lastUpdatedDate").setValue(currentDate);

        if (currentTabKey == null) {

            //creating new tab and initializing it
            currentTab = new Tab();
            currentTab.setTabOpenDate(currentDate);
            currentTab.setWasHangover("Not Supplied");
            currentTab.setTabCloseDate("Not Yet");
            currentTab.setMaxBAC(newBAC);

            // saving the new tab in the DB
            currentTabKey = userTabsDB.push().getKey();
            userTabsDB = userTabsDB.child(currentTabKey);
            userTabsDB.setValue(currentTab);
        } else {
            userTabsDB = userTabsDB.child(currentTabKey);
            if (currentTab.getMaxBAC() < newBAC) {
              /* currentTab.setMaxBAC(newBAC);
               userTabsDB.setValue(currentTab);*/
                userTabsDB.child("maxBAC").setValue(newBAC);
            }
        }

        // add drink to tab's drinks
        tabDrinksDB.child(currentTabKey).push().setValue(drink);

        // does the user need to be warn ( is he/she 1 beer away from a hangover?)
        // 1/3 liter beer containing 5% alcohol
        Drink beer = new Drink(333, 5, 1, currentDate); //the date is irrelevant in this case but needed for constructor
        double beerAway = BAC_toAdd(beer, user);

        if (user.getConfidenceValue() > 0.6 &&
                user.getBestSeparator() < (newBAC + beerAway)) {
            // the user need to be warned
            Toast.makeText(getParent(),
                    "Please be advised: You are 1 beer away from getting a hangover.", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    /**
     * validate the user input
     *
     * @return true if the input is valid else false
     */
    private boolean validate() {
        boolean result = true;
        if (TextUtils.isEmpty(drinkVolume.getText().toString())) {
            drinkVolume.setError("Required");
            result = false;
        } else if (parseInt(drinkVolume.getText().toString()) == 0) {
            drinkVolume.setError("Must be positive number");
            result = false;
        } else {
            drinkVolume.setError(null);
        }

        if (TextUtils.isEmpty(alcoholVolume.getText().toString())) {
            alcoholVolume.setError("Required");
            result = false;
        } else if (Double.parseDouble(alcoholVolume.getText().toString()) > 100) {
            alcoholVolume.setError("must be lower than 100%");
            result = false;
        } else if (Double.parseDouble(alcoholVolume.getText().toString()) == 0) {
            alcoholVolume.setError("this is apparently not an alcoholic beverage");
            result = false;
        } else {
            alcoholVolume.setError(null);
        }

        if (TextUtils.isEmpty(drinkNumber.getText().toString())) {
            drinkNumber.setError("Required");
            result = false;
        } else {
            drinkNumber.setError(null);
        }
        return result;
    }

}
