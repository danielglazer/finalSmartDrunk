package tk.smartdrunk.smartdrunk.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

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
    public String email;
//    public boolean isNew;
    public String emergencyContact;
//    public String country;

    /* Physical user parameters */
    public String birthDate;
    public String gender;
    public boolean newDriver;
    public double weight;
    public double lastBAC;

      /* Additional parameters */
//
//    private Calendar lastOperated;

    // we can assume that it is 0.03 so we don't need to save it

    //The human body constantly produces small amounts of alcohol itself.
    // Normal levels of 0.01 to 0.03 mg of alcohol/100 ml are contained in the blood.
    // By contrast, a blood alcohol limit for driving of 0.05 per cent is equal to around 50 mg of alcohol/100 ml of blood.
    // max natural BAC of 0.121%

    //Another gender based difference is in the elimination of alcohol. Although not explained,
    // studies appear to show that women eliminate alcohol from their bodies at a rate 10% greater than that of men.




    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User( String email, double weight, String gender, String birthDate, boolean newDriver, String emergencyContact) {
        this.email = email;
        this.weight = weight;
        this.gender = gender;
        this.birthDate = birthDate;
        this.newDriver = newDriver;
        this.emergencyContact = emergencyContact;
        this.lastBAC = 0.00003; //since its mg and we need to convert it to grams
    }
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
        return result;
    }
    // 24 µg per 100 ml (0.024%) of breath (penalties only apply above 26 µg per 100 ml (0.026%) of breath due to
    // lawsuits about sensitivity of devices used).
    // This is equivalent to a BAC of 0.05.
    // New drivers,drivers under 24 years of age and commercial drivers 5 µg per 100 ml of breath.
    // This is equivalent to a BAC of 0.01.


//    /**
//     * @param ageGroupNumber the ageGroupNumber of the subject
//     * @param gender the gender of the subject
//     * @return the waterConst
//     */
//    public double waterConst(int ageGroupNumber,String gender){
//        double manBodyWaterConstant;
//        double womanBodyWaterConstant;
//        double bodyWaterConstant;
//        switch (ageGroupNumber) {
//            case 0:
//                manBodyWaterConstant = 0.74;
//                womanBodyWaterConstant = 0.74;
//                break;
//            case 1:
//                manBodyWaterConstant = 0.6;
//                womanBodyWaterConstant = 0.6;
//                break;
//            case 2:
//                manBodyWaterConstant = 0.59;
//                womanBodyWaterConstant = 0.56;
//                break;
//            case 3:
//                manBodyWaterConstant = 0.59;
//                womanBodyWaterConstant = 0.50;
//                break;
//            case 4:
//                manBodyWaterConstant = 0.575;
//                womanBodyWaterConstant = 0.485;
//                break;
//            case 5:
//                manBodyWaterConstant = 0.56;
//                womanBodyWaterConstant = 0.47;
//                break;
//            case 6:
//                manBodyWaterConstant = 0.51;
//                womanBodyWaterConstant = 0.46;
//                break;
//            default:
//                // invalid water constant since the age is invalid
//                return -1;
//        }
//        if (gender == "Other") {
//            bodyWaterConstant = (manBodyWaterConstant + womanBodyWaterConstant)/2.0;
//        }
//        else{
//            bodyWaterConstant =
//                    (gender == "Male") ? manBodyWaterConstant : womanBodyWaterConstant;
//        }
//        return roundDecimal(4,bodyWaterConstant);
//    }
//
//
//
//    /**
//     * @return the body water constant of the subject
//     */
//    public double getBodyWaterConstant() {
//        String[] birth = this.birthDate.split(".");
//        Calendar date = Calendar.getInstance();
//        date.set(Calendar.YEAR, Integer.parseInt(birth[2]));
//        date.set(Calendar.MONTH, Integer.parseInt(birth[0]));
//        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birth[1]));
//        int age = getAge(date);
//        String gender = this.gender;
//        int ageGroupNumber = getAgeGroupNumber(age);
//        double bodyWaterConstant = waterConst(ageGroupNumber,gender);
//
//        // creating a linear connection between age and body weight
//        // if possible(for people between 10 to 60 years old)
//        if( ageGroupNumber != 7 && ageGroupNumber > 1) {
//            double ratio =  (ageGroupNumber*10 - age)/10.0;
//            bodyWaterConstant =
//                    waterConst(ageGroupNumber,gender) * ratio + waterConst(ageGroupNumber+1,gender)*(1-ratio);
//        }
//        return roundDecimal(4,bodyWaterConstant);
//    }
//
//    /**
//     * @param age the user age
//     * @return the user age group number
//     */
//    public int getAgeGroupNumber(int age){
//        int ageGroupNumber = -1; // invalid Age group
//
//        if(0 < age && age < 3) {
//            ageGroupNumber = 0; // Infant
//        } else if(3 <= age && age < 10){
//            ageGroupNumber = 1; // Child
//        } else if(10 <= age && age < 20){
//            ageGroupNumber = 2; // teenager
//        } else if(20 <= age && age < 30){
//            ageGroupNumber = 3; // Young adult
//        } else if(30 <= age && age < 40){
//            ageGroupNumber = 4; // Adult
//        }else if(40 <= age && age < 50){
//            ageGroupNumber = 5; // Middle age Adult
//        } else if(50 <= age && age < 60){
//            ageGroupNumber = 6; // Adult between 50 to 60
//        } else if(60 < age ) {
//            ageGroupNumber = 7; // Adult over 60
//        }
//        return ageGroupNumber;
//    }
//
//    /**
//     * @param birthDate the user date of birth
//     * @return the user age
//     */
//    public int getAge(Calendar birthDate){
//        return getDiffYears(birthDate,getCalendar(new Date()));
//    }
//
//    /**
//     * @param first first date
//     * @param last second date
//     * @return the difference in years between the two
//     */
//    public int getDiffYears(Calendar first, Calendar last) {
//        Calendar a = first;
//        Calendar b = last;
//        int diff = b.get(YEAR) - a.get(YEAR);
//        if (a.get(MONTH) > b.get(MONTH) ||
//                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
//            diff--;
//        }
//        return diff;
//    }
//
//    /**
//     * @param date a date
//     * @return convert this date to Calendar
//     */
//    public Calendar getCalendar(Date date) {
//        Calendar cal = Calendar.getInstance(Locale.US);
//        cal.setTime(date);
//        return cal;
//    }
//
//    public double roundDecimal(int digitNumber, double number) {
//        return Math.floor(number * (10 ^ digitNumber)) / (10 ^ digitNumber);
//    }
}


// [END user_class]
