package tk.smartdrunk.smartdrunk.classifier_LDA;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import tk.smartdrunk.smartdrunk.models.Tab;

/**
 * Created by Daniel on 6/23/2017.
 */

public class BAC_LDA {
    /*
    A class use for getting the best linear discriminant function to classify if the user will be hungover or not
    given a set of Tabs of this specific user.
    assumption : the class of hungover is likely to have higher maxBAC then NotHungover
    this is an implantation of link: https://en.wikipedia.org/wiki/Linear_discriminant_analysis#LDA_for_two_classes
     */

    public BAC_LDA() {
        // no op
    }

    /**
     * @param tabs - an array list of tabs
     * @return result - a Object array that the first value in it is boolean(true is optimal classification is possible)
     * if true the second is the best separator (double)
     * ConfidenceValue in this case is always 1
     */
    public Object[] isOptimalClassification(ArrayList<Tab> tabs) {
        double minHungover = 1, maxHungover = 0, minNotHungover = 1, maxNotHungover = 0;
        // get min/max BAC limits of the classes
        for (Tab tab : tabs) {
            if (tab.getWasHangover() == "Yes") {
                if (tab.getMaxBAC() > maxHungover) {
                    maxHungover = tab.getMaxBAC();
                }
                if (tab.getMaxBAC() < minHungover) {
                    minHungover = tab.getMaxBAC();
                }
            }
            if (tab.getWasHangover() == "No") {
                if (tab.getMaxBAC() > maxNotHungover) {
                    maxNotHungover = tab.getMaxBAC();
                }
                if (tab.getMaxBAC() < minNotHungover) {
                    minNotHungover = tab.getMaxBAC();
                }
            }
        }

        // initialization
        double separator = 0;
        boolean hungoverWithHigherBAC = true;

        //checking if optimal classification is possible
        if (minHungover > maxNotHungover) {
            hungoverWithHigherBAC = true;
            separator = (minHungover + maxNotHungover) / 2;
        }
      /* TODO: check if this 'if' section is needed (we can assume that the user only                                                               get hungover from high BAC and not the other way around */
        if (maxHungover < minNotHungover) {
            hungoverWithHigherBAC = false;
            separator = (minNotHungover + maxHungover) / 2;
        }
        Object[] result;
        if (separator != 0) {
            result = new Object[]{true, separator, hungoverWithHigherBAC};
        } else {
            result = new Object[]{false};
        }
        return result;
    }


    /**
     * @param tabs - an array list of tabs
     * @return result - a double array that the first value in it contain the
     * separator value and the second the ConfidenceValue
     */
    public double[] exhaustiveClassification(ArrayList<Tab> tabs) {
        double maxConfidenceValue = -1, bestSeparator = 0, currentConfidence = 0;
        //boolean isHungoverOnBestSeparator = false;
        for (Tab tab : tabs) {
            currentConfidence = getConfidenceValue(tabs, tab.getMaxBAC());
            if (currentConfidence > maxConfidenceValue) {
                maxConfidenceValue = currentConfidence;
                bestSeparator = tab.getMaxBAC();
                //isHungoverOnBestSeparator =  tab.getWasHangover().equals("Yes") ? true : false;
            }
        }
        // now what we need to do is to move the separator left or right for better Confidence
        if (getConfidenceValue(tabs, bestSeparator + 0.0000001) > maxConfidenceValue) {
            //move to the right
            // find minimum that is larger then bestSeparator than find avg
            double min = 1;
            double currentBAC;
            for (Tab tab : tabs) {
                currentBAC = tab.getMaxBAC();
                if (min > currentBAC && currentBAC > bestSeparator) {
                    min = currentBAC;
                }
            }
            bestSeparator = (min + bestSeparator) / 2;
        }
        if (getConfidenceValue(tabs, bestSeparator - 0.0000001) > maxConfidenceValue) {
            //move to the left
            // find maximum that is smaller then bestSeparator than find avg
            double max = -1;
            double currentBAC;
            for (Tab tab : tabs) {
                currentBAC = tab.getMaxBAC();
                if (max < currentBAC && currentBAC < bestSeparator) {
                    max = currentBAC;
                }
            }
            bestSeparator = (max + bestSeparator) / 2;
        }
        maxConfidenceValue = getConfidenceValue(tabs, bestSeparator);
        double[] result = {bestSeparator, maxConfidenceValue};
        return result;
    }

    public double[] classify(ArrayList<Tab> tabs) {
        // try to optimally classify
        double[] result = new double[2];
        Object[] optimalResult = isOptimalClassification(tabs); // if possible optimal classify the data
        if ((boolean) optimalResult[0] == true) {
            result[0] = (double) optimalResult[1];
            // since this is optimal the result confidence always be 1
            result[1] = 1.0;
        } else {                                                   // if not do it not optimally
            result = exhaustiveClassification(tabs);
        }
        return result;
    }

    /**
     * @param tabs - an array list of tabs
     * @param x    - a BAC value tested for it's ConfidenceValue
     * @return result - the ConfidenceValue of x in the group tabs
     */
    public double getConfidenceValue(ArrayList<Tab> tabs, double x) {
        double result = 0;
        int errorCounter = 0, size = 0;

        for (Tab tab : tabs) {
            if (!tab.getWasHangover().equals("Not Supplied")) {
                size++;
                if ((tab.getWasHangover().equals("Yes") && tab.getMaxBAC() < x)
                        || (tab.getWasHangover().equals("No") && tab.getMaxBAC() > x)
                        || tab.getMaxBAC() == x)
                    errorCounter++;
            }
        }
        result = 1 - (double) (errorCounter) / size;
        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
