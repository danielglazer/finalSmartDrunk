package tk.smartdrunk.smartdrunk.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    //    public boolean isNew;
    private String emergencyContact;
//    public String country;

    /* Physical user parameters */
    private String birthDate;
    private String gender;
    private boolean newDriver;
    private double weight;
    private double lastBAC;

    /* Additional parameters*/
    private double bestSeparator;
    private double confidenceValue;
    private String lastUpdatedDate;

    /*Useful facts*/

    // we can assume that it is 0.03 so we don't need to save it

    //The human body constantly produces small amounts of alcohol itself.
    // Normal levels of 0.01 to 0.03 mg of alcohol/100 ml are contained in the blood.
    // By contrast, a blood alcohol limit for driving of 0.05 per cent is equal to around 50 mg of alcohol/100 ml of blood.
    // max natural BAC of 0.121%

    // 24 µg per 100 ml (0.024%) of breath (penalties only apply above 26 µg per 100 ml (0.026%) of breath due to
    // lawsuits about sensitivity of devices used).
    // This is equivalent to a BAC of 0.05.
    // New drivers,drivers under 24 years of age and commercial drivers 5 µg per 100 ml of breath.
    // This is equivalent to a BAC of 0.01.


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

}
// [END user_class]
