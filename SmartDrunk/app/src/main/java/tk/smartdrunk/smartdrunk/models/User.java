package tk.smartdrunk.smartdrunk.models;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static tk.smartdrunk.smartdrunk.models.Drink.getSD;

/**********************************************************************
 *  class: User (User.java)
 *  author: Daniel Glazer
 *  date: 5/22/2017
 **********************************************************************/


// [START user_class]
@IgnoreExtraProperties
public class User {

    /* Virtual user parameters */
    private String email;
    private String emergencyContact;

    /* Physical user parameters */
    private String birthDate;
    private String gender;
    private boolean newDriver;
    private double weight;
    private double lastBAC;

    /* Additional parameters*/
    private double bestSeparator;
    private double confidenceValue;
    private String lastUpdatedDate; // last time the BAC was changed
    private String lastUpdatedSeparatorDate; // last time the LDA algorithm ran

    /*Useful facts*/
    //The human body constantly produces small amounts of alcohol itself.
    // Normal levels of 0.01 to 0.03 mg of alcohol/100 ml are contained in the blood.


    /*constructors*/
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, double weight, String gender, String birthDate, boolean newDriver, String emergencyContact) {
        this.email = email;
        this.weight = weight;
        this.gender = gender;
        this.birthDate = birthDate;
        this.newDriver = newDriver;
        this.emergencyContact = emergencyContact;
        // Normal levels of 0.01 to 0.03 mg of alcohol/100 ml are contained in the blood.
        //since its mg and we need to convert it to grams of alcohol/100 ml are contained in the blood.
        this.lastBAC = 0.00003;
        final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        this.lastUpdatedDate = sdf.format(cal.getTime());
        this.lastUpdatedSeparatorDate = lastUpdatedDate;
        this.bestSeparator = -1;
        this.confidenceValue = -1;
    }

    /*Helpful methods*/
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("emergencyContact", emergencyContact);
        result.put("newDriver", newDriver);
        result.put("birthDate", birthDate);
        result.put("lastBAC", lastBAC);
        result.put("weight", weight);
        result.put("gender", gender);
        result.put("lastUpdatedDate", lastUpdatedDate);
        result.put("bestSeparator", bestSeparator);
        result.put("confidenceValue", confidenceValue);
        result.put("lastUpdatedSeparatorDate", lastUpdatedSeparatorDate);
        return result;
    }

    /*Getters and setters*/
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isNewDriver() {
        return newDriver;
    }

    public void setNewDriver(boolean newDriver) {
        this.newDriver = newDriver;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLastBAC() {
        return lastBAC;
    }

    public void setLastBAC(double lastBAC) {
        this.lastBAC = lastBAC;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public double getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(double confidenceValue) {
        this.confidenceValue = confidenceValue;
    }

    public double getBestSeparator() {
        return bestSeparator;
    }

    public void setBestSeparator(double bestSeparator) {
        this.bestSeparator = bestSeparator;
    }

    public String getLastUpdatedSeparatorDate() {
        return lastUpdatedSeparatorDate;
    }

    public void setLastUpdatedSeparatorDate(String lastUpdatedSeparatorDate) {
        this.lastUpdatedSeparatorDate = lastUpdatedSeparatorDate;
    }


     /*class related methods
      can be used outside of class and without a User object
      (mainly for code reuse purposes).*/

    /**
     * @return a String that represent the user unique hashed string created in Firebase.
     */
    @Nullable
    public static String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        } else {
            return user.getUid();
        }
    }

    /**
     * @param ageGroupNumber the ageGroupNumber of the subject
     * @param gender         the gender of the subject
     * @return the waterConst
     */
    public static double waterConst(int ageGroupNumber, String gender) {
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
    public static double getBodyWaterConstant(User user) {
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
    public static int getAgeGroupNumber(int age) {
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
     * @param dateToValidate
     * @return true if the input format is correct and the date is valid and in the past
     */
    public static boolean isValidDate(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
        sdf.setLenient(false);
        Date date;
        try {

            //if not valid, it will throw ParseException
            date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {
            return false;
        }
        Calendar birth = getCalendar(date);
        Calendar today = getInstance();
        // no one's age is more than 120 (at least not at 8.3.2017)
        // and one can't come from the future of curse
        if (birth.after(today) || getAge(birth) > 120) {
            return false;
        }
        return true;
    }

    /**
     * @param drink
     * @param user
     * @return the BAC to add to the user if he/she drink that drink
     */
    public static double BAC_toAdd(Drink drink, User user) {
        return ((0.806 * getSD(drink) * 1.2) / (getBodyWaterConstant(user) * user.getWeight()));
    }

    /**
     * after call from this method we need to update the BAC
     * of the user and the lastUpdatedDate field
     * @param user
     * @return
     */
    public static double getUpdatedBAC(User user){
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
        double UpdatedBAC = (lastBAC - (DP * MR));
        // can't be lower than normal alcohol found in the blood though
        UpdatedBAC = UpdatedBAC < 0.00003 ? 0.00003 : UpdatedBAC;
        return UpdatedBAC;
    }
}
// [END user_class]
