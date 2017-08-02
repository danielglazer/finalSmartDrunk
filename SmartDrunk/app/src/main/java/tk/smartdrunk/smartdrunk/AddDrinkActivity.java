package tk.smartdrunk.smartdrunk;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import tk.smartdrunk.smartdrunk.models.Drink;
import tk.smartdrunk.smartdrunk.models.Tab;
import tk.smartdrunk.smartdrunk.models.User;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class AddDrinkActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddDrinkActivity";

    private DatabaseReference userDB, userTabsDB, tabDrinksDB;
    private EditText drinkVolume, alcoholVolume, drinkNumber;
    private TextView servingSize, drinkType;
    private User user;
    private String currentTabKey = null;
    private String tabCloseDate;
    private String tabOpenDate;
    private Tab currentTab = null;
    private boolean isFirstUser;
    private boolean isFirstTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        isFirstTab = true;
        isFirstUser = true;
        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        tabDrinksDB = FirebaseDatabase.getInstance().getReference().child("tab-drinks");
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

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isFirstUser) {
                    isFirstUser=false;
                    //get current user
                    user = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // We need to search for an open tab if found add a drink to this tab if not open one
        userTabsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: can be done by checking the last tab added
                if (isFirstTab == false) {
                    //when adding a tab we don't want to go further
                    return;
                }
                isFirstTab = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Tab tab = ds.getValue(Tab.class);
                    // only 1 tab can be open at a time
                    if (tab.getTabCloseDate().equals("Not Yet")) {
                        currentTab = tab;
                        currentTabKey = ds.getKey();
                        return;
                    }
                }
                // no tabs open so new one needed
                // currentTabKey = null
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        // Custom date format
        String dateStart = user.getLastUpdatedDate(); // 1st date

        final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_NOW);
        Calendar cal = Calendar.getInstance();
        String currentDate = format.format(cal.getTime()); //2nd date

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long duration = d2.getTime() - d1.getTime(); //difference between dates
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        double DP = diffInSeconds / 60.0 / 60;      // DP = difference in hours

        //Another gender based difference is in the elimination of alcohol. Although not explained,
        // studies appear to show that women eliminate alcohol from their bodies at a rate 10% greater than that of men.
        double MR = user.getGender().equals("female") ? (0.017 * 1.1) : 0.017;

        double lastBAC = user.getLastBAC();
        double beforeNewBAC = (lastBAC - (DP * MR));
        // can't be lower than normal alcohol found in the blood though
        beforeNewBAC = beforeNewBAC < 0.00003 ? 0.00003 : beforeNewBAC;

        //create a drink
        Drink drink = new Drink(Integer.parseInt(drinkVolume.getText().toString()),
                Double.parseDouble(alcoholVolume.getText().toString()),
                parseInt(drinkNumber.getText().toString()),
                currentDate);

        // We need to recalculate the user BAC
        double alcoholInML = drink.getDrinkVolume() * (drink.getAlcoholVolume() / 100) * drink.getDrinkNumber();
        double ethanolDensity = 0.7893; // at 20 celsius
        //SD is the number of standard drinks, that being 10 grams of ethanol each
        double SD = (ethanolDensity * alcoholInML) / 10;
        double weight = user.getWeight();
        double bodyWater = getBodyWaterConstant();
        double newBAC = (0.806 * SD * 1.2) / (bodyWater * weight);
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
            tabOpenDate = currentDate;
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
        double beerInML = 333 * 0.05; // 1/3 liter beer containing 5% alcohol
        SD = ethanolDensity * beerInML / 10;
        double beerAway = (0.806 * SD * 1.2) / (bodyWater * weight);

        if (user.getConfidenceValue() > 0.6 &&
                user.getBestSeparator() < (newBAC + beerAway)) {
            // the user need to be warned
            Toast.makeText(getParent(),
                    "You are 1 beer away from getting a hangover.", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    /**
     * @param ageGroupNumber the ageGroupNumber of the subject
     * @param gender         the gender of the subject
     * @return the waterConst
     */
    public double waterConst(int ageGroupNumber, String gender) {
        double manBodyWaterConstant;
        double womanBodyWaterConstant;
        double bodyWaterConstant;
        switch (ageGroupNumber) {
            case 0:
                manBodyWaterConstant = 0.74;
                womanBodyWaterConstant = 0.74;
                break;
            case 1:
                manBodyWaterConstant = 0.6;
                womanBodyWaterConstant = 0.6;
                break;
            case 2:
                manBodyWaterConstant = 0.59;
                womanBodyWaterConstant = 0.56;
                break;
            case 3:
                manBodyWaterConstant = 0.59;
                womanBodyWaterConstant = 0.50;
                break;
            case 4:
                manBodyWaterConstant = 0.575;
                womanBodyWaterConstant = 0.485;
                break;
            case 5:
                manBodyWaterConstant = 0.56;
                womanBodyWaterConstant = 0.47;
                break;
            case 6:
                manBodyWaterConstant = 0.51;
                womanBodyWaterConstant = 0.46;
                break;
            case 7:
                manBodyWaterConstant = 0.51;
                womanBodyWaterConstant = 0.46;
                break;
            default:
                // invalid water constant since the age is invalid
                return -1;
        }
        if (gender.equals("Other")) {
            bodyWaterConstant = (manBodyWaterConstant + womanBodyWaterConstant) / 2.0;
        } else {
            bodyWaterConstant =
                    (gender.equals("Male")) ? manBodyWaterConstant : womanBodyWaterConstant;
        }
        return bodyWaterConstant;
    }

    /**
     * @return the body water constant of the subject
     */
    public double getBodyWaterConstant() {
        String[] birth = user.getBirthDate().split("\\.");
        Calendar date = Calendar.getInstance();
        date.set(YEAR, parseInt(birth[2]));
        date.set(MONTH, parseInt(birth[0]));
        date.set(Calendar.DAY_OF_MONTH, parseInt(birth[1]));
        int age = getAge(date);
        String gender = user.getGender();
        int ageGroupNumber = getAgeGroupNumber(age);
        double bodyWaterConstant = waterConst(ageGroupNumber, gender);


        // creating a linear connection between age and body weight
        // if possible(for people between 10 to 60 years old)
        //linear interpolation
        if (ageGroupNumber != 7 && ageGroupNumber > 1) {
            double ratio = (ageGroupNumber * 10 - age) / 10.0;
            bodyWaterConstant =
                    waterConst(ageGroupNumber, gender) * ratio + waterConst(ageGroupNumber + 1, gender) * (1 - ratio);
        }
        return bodyWaterConstant;
    }

    /**
     * @param age the user age
     * @return the user age group number
     */
    public int getAgeGroupNumber(int age) {
        int ageGroupNumber = -1; // invalid Age group

        if (0 < age && age < 3) {
            ageGroupNumber = 0; // Infant
        } else if (3 <= age && age < 10) {
            ageGroupNumber = 1; // Child
        } else if (10 <= age && age < 20) {
            ageGroupNumber = 2; // teenager
        } else if (20 <= age && age < 30) {
            ageGroupNumber = 3; // Young adult
        } else if (30 <= age && age < 40) {
            ageGroupNumber = 4; // Adult
        } else if (40 <= age && age < 50) {
            ageGroupNumber = 5; // Middle age Adult
        } else if (50 <= age && age < 60) {
            ageGroupNumber = 6; // Adult between 50 to 60
        } else if (60 < age) {
            ageGroupNumber = 7; // Adult over 60
        }
        return ageGroupNumber;
    }

    /**
     * @param birthDate the user date of birth
     * @return the user age
     */
    public static int getAge(Calendar birthDate) {
        return getDiffYears(birthDate, getCalendar(new Date()));
    }

    /**
     * @param first first date
     * @param last  second date
     * @return the difference in years between the two
     */
    public static int getDiffYears(Calendar first, Calendar last) {
        Calendar a = first;
        Calendar b = last;
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    /**
     * @param date a date
     * @return convert this date to Calendar
     */
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
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
