package tk.smartdrunk.smartdrunk.models;

/**
 * Created by Daniel on 6/12/2017.
 */

public class Drink {

    private int drinkVolume; // in milliliters
    private double alcoholVolume; // percent
    private int drinkNumber; // number of shots for example
    private String drinkDate; // the date in a simple date format as defined in AddDrink.java

    //private String servingSize, drinkType;

    /*Constructors*/
    public Drink() {
        // Default constructor required for calls to DataSnapshot.getValue(Drink.class)
    }

    public Drink(int drinkVolume, double alcoholVolume, int drinkNumber, String drinkDate) {
        this.drinkVolume = drinkVolume;
        this.drinkNumber = drinkNumber;
        this.alcoholVolume = alcoholVolume;
        this.drinkDate = drinkDate;
    }

    /*Getters and setters*/
    public int getDrinkVolume() {
        return drinkVolume;
    }

    public void setDrinkVolume(int drinkVolume) {
        this.drinkVolume = drinkVolume;
    }

    public double getAlcoholVolume() {
        return alcoholVolume;
    }

    public void setAlcoholVolume(double alcoholVolume) {
        this.alcoholVolume = alcoholVolume;
    }

    public int getDrinkNumber() {
        return drinkNumber;
    }

    public void setDrinkNumber(int drinkNumber) {
        this.drinkNumber = drinkNumber;
    }

    public String getDrinkDate() {
        return drinkDate;
    }

    public void setDrinkDate(String drinkDate) {
        this.drinkDate = drinkDate;
    }

    /**
     * @param drink
     * @return the number of SD in the input drink
     */
    public static double getSD(Drink drink) {
        double alcoholInML = drink.getDrinkVolume() * (drink.getAlcoholVolume() / 100) * drink.getDrinkNumber();
        double ethanolDensity = 0.7893; // at 20 celsius
        //SD is the number of standard drinks, that being 10 grams of ethanol each
        return (ethanolDensity * alcoholInML) / 10;
    }
}
