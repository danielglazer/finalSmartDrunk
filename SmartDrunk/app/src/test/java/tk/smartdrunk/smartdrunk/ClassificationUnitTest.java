package tk.smartdrunk.smartdrunk;

import org.junit.Test;

import java.util.ArrayList;

import tk.smartdrunk.smartdrunk.classifier_LDA.BAC_LDA;
import tk.smartdrunk.smartdrunk.models.Tab;

import static org.junit.Assert.assertEquals;
import static tk.smartdrunk.smartdrunk.classifier_LDA.BAC_LDA.round;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ClassificationUnitTest {

    /*tabs were created here without dates since they are irrelevant*/

    @Test
    public void classifyOptimal() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 0; i < 5; i++) {
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Object[] result = classifier.isOptimalClassification(tabs);
        if ((Boolean) result[0] == true) {
            assertEquals("test classifier", (tab1.getMaxBAC() + tab2.getMaxBAC()) / 2, (Double) result[1], 0);
        }
    }

    @Test
    public void classify1() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 0; i < 2; i++) {
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "Yes", 0.1);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A", 0.2, result[0], 0);
        System.out.println(result[0] + " , " + result[1]);
        assertEquals("test classifier 2B", 0.8, result[1], 0);
    }

    @Test
    public void classify2() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 0; i < 5; i++) {
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "Yes", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A", 0.125, result[0], 0);
        System.out.println(result[0] + " , " + result[1]);
        assertEquals("test classifier 2B", 1, result[1], 0);
    }

    @Test
    public void classify3() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 0; i < 5; i++) {
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "No", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A", 0.225, round(result[0], 3), 0);
        System.out.println(round(result[0], 3) + " , " + result[1]);
        assertEquals("test classifier 2B", 1, result[1], 0);
    }

    @Test
    public void classify4() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 1; i <= 5; i++) {
            Tab tab1 = new Tab("", "", "Yes", 1 - i * 0.1);
            Tab tab2 = new Tab("", "", "No", i * 0.1);
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "No", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A", 0.55, result[0], 0);
        System.out.println(result[0] + " , " + result[1]);
        assertEquals("test classifier 2B", 10.0 / 11, result[1], 0);
    }

    @Test
    public void classify5() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 1; i <= 5; i++) {
            Tab tab1 = new Tab("", "", "Yes", 1 - i * 0.1);
            Tab tab2 = new Tab("", "", "No", i * 0.1);
            tabs.add(tab1);
            tabs.add(tab2);
        }
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A", 0.55, result[0], 0);
        System.out.println(result[0] + " , " + result[1]);
        assertEquals("test classifier 2B", 9.0 / 10, result[1], 0);
    }

    @Test
    public void classify6() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 1; i <= 5; i++) {
            Tab tab1 = new Tab("", "", "Yes", 1 - i * 0.1);
            Tab tab2 = new Tab("", "", "No", i * 0.1);
            tabs.add(tab1);
//            tabs.add(tab2);
        }
        double[] result = classifier.classify(tabs);
        System.out.println("");
        System.out.println(result[0] + " , " + result[1]);
        System.out.println("");
        assertEquals("test classifier 2A", 0.25, result[0], 0);
        assertEquals("test classifier 2B", 1, result[1], 0);
    }

    @Test
    public void classify7() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i = 1; i <= 5; i++) {
            Tab tab1 = new Tab("", "", "Yes", 1 - i * 0.1);
            Tab tab2 = new Tab("", "", "No", i * 0.1);
//            tabs.add(tab1);
            tabs.add(tab2);
        }
        double[] result = classifier.classify(tabs);
        System.out.println("");
        System.out.println(result[0] + " , " + result[1]);
        System.out.println("");
        assertEquals("test classifier 2A", 0.75, result[0], 0);
        assertEquals("test classifier 2B", 1, result[1], 0);
    }

    @Test
    public void round1() throws Exception {
        System.out.print(round(0.44, 1));
        assertEquals("", 0.4, round(0.43, 1), 0);
    }

    @Test
    public void round2() throws Exception {
        System.out.print(round(0.45555555555555, 5));
        assertEquals("", 0.45556, round(0.45555555555555, 5), 0);
    }
}