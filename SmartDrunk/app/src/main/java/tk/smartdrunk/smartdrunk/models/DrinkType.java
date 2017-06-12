package tk.smartdrunk.smartdrunk.models;

/**********************************************************************
 *  class: DrinkType (DrinkType.java)
 *  author: Daniel Glazer
 *  date: 5/22/2017
 *  description:
 **********************************************************************/

// [START DrinkType_class]
public class DrinkType {

    private String name;
    private String type;
    private double minAlcoholContent;
    private double maxAlcoholContent;
    private double defaultPortion;


    /**
     * @param name the name of the drink
     * @param type the type of the drink
     * @param minAlcoholContent the minAlcoholContent of the drink
     * @param maxAlcoholContent the maxAlcoholContent of the drink
     * @param defaultPortion the defaultPortion of the drink
     */
    public DrinkType(String name, String type, double minAlcoholContent, double maxAlcoholContent, double defaultPortion){
        this.name = name;
        this.type = type;
        this.minAlcoholContent = minAlcoholContent;
        this.maxAlcoholContent = maxAlcoholContent;
        this.defaultPortion = defaultPortion;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the type
     */
    public String getType() {
        return type;
    }


    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * @return the minAlcoholContent
     */
    public double getMinAlcoholContent() {
        return minAlcoholContent;
    }


    /**
     * @param minAlcoholContent the minAlcoholContent to set
     */
    public void setMinAlcoholContent(double minAlcoholContent) {
        this.minAlcoholContent = minAlcoholContent;
    }


    /**
     * @return the maxAlcoholContent
     */
    public double getMaxAlcoholContent() {
        return maxAlcoholContent;
    }


    /**
     * @param maxAlcoholContent the maxAlcoholContent to set
     */
    public void setMaxAlcoholContent(double maxAlcoholContent) {
        this.maxAlcoholContent = maxAlcoholContent;
    }


    /**
     * @return the defaultPortion
     */
    public double getDefaultPortion() {
        return defaultPortion;
    }


    /**
     * @param defaultPortion the defaultPortion to set
     */
    public void setDefaultPortion(double defaultPortion) {
        this.defaultPortion = defaultPortion;
    }

}
// [END DrinkType_class]