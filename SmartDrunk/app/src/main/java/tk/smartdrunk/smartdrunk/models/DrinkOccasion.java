package tk.smartdrunk.smartdrunk.models;

import java.text.SimpleDateFormat;

/**********************************************************************
 *  class: DrinkOccasion (DrinkOccasion.java)
 *  author: Daniel Glazer
 *  date: 5/22/2017
 *  description:
 **********************************************************************/

// [START DrinkOccasion_class]
public class DrinkOccasion {

    private DrinkType drinkType;
    private SimpleDateFormat consumptionTime;
    private double price;
    private int numberOfDrinks;

    /**
     * @param price the price to set
     * @param numberOfDrinks the numberOfDrinks to set
     * @param drinkType the drinkType to set
     */
    public DrinkOccasion(double price, int numberOfDrinks, DrinkType drinkType ){
        this.setDrinkType(drinkType);
        this.setConsumptionTime(new SimpleDateFormat()); // need to check if this constructor works
        this.setPrice(price);
        this.setNumberOfDrinks(numberOfDrinks);
    }

    /**
     * @return the numberOfDrinks
     */
    public int getNumberOfDrinks() {
        return numberOfDrinks;
    }

    /**
     * @param numberOfDrinks the numberOfDrinks to set
     */
    public void setNumberOfDrinks(int numberOfDrinks) {
        this.numberOfDrinks = numberOfDrinks;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the consumptionTime
     */
    public SimpleDateFormat getConsumptionTime() {
        return consumptionTime;
    }

    /**
     * @param consumptionTime the consumptionTime to set
     */
    public void setConsumptionTime(SimpleDateFormat consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    /**
     * @return the drinkType
     */
    public DrinkType getDrinkType() {
        return drinkType;
    }

    /**
     * @param drinkType the drinkType to set
     */
    public void setDrinkType(DrinkType drinkType) {
        this.drinkType = drinkType;
    }
}
// [END DrinkOccasion_class]