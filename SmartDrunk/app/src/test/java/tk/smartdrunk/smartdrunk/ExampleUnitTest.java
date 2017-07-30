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
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void classifierOptimal() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=0; i<5; i++){
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Object[] result = classifier.isOptimalClassification(tabs);
        if((Boolean)result[0] == true){
            assertEquals("test classifier",(tab1.getMaxBAC() + tab2.getMaxBAC()) / 4, (Double) result[1], 3);
        }
    }

    @Test
    public void classifierExhaustive1() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=0; i<2; i++){
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "Yes", 0.1);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A",0.2,  result[0], 0);
        System.out.println(result[0] +" , " + result[1] );
        assertEquals("test classifier 2B",0.8,  result[1],0 );
    }

    @Test
    public void classifierExhaustive2() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=0; i<5; i++){
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "Yes", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A",0.125,  result[0], 0);
        System.out.println(result[0] +" , " + result[1] );
        assertEquals("test classifier 2B",1,  result[1],0 );
    }

    @Test
    public void classifierExhaustive3() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        Tab tab1 = new Tab("", "", "Yes", 0.3);
        Tab tab2 = new Tab("", "", "No", 0.1);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=0; i<5; i++){
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "No", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A",0.15*1.5,  result[0], 0);
        System.out.println(round(result[0],3) +" , " + result[1] );
        assertEquals("test classifier 2B",1,  result[1],0 );
    }

    @Test
    public void classifierExhaustive4() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=1; i<=5; i++){
            Tab tab1 = new Tab("", "", "Yes", 1-i*0.1);
            Tab tab2 = new Tab("", "", "No", i*0.1);
            tabs.add(tab1);
            tabs.add(tab2);
        }
        Tab tab3 = new Tab("", "", "No", 0.15);
        tabs.add(tab3);
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A",0.55,  result[0], 0);
        System.out.println(round(result[0],3) +" , " + result[1] );
        assertEquals("test classifier 2B",10.0/11,  result[1],0 );
    }

    @Test
    public void classifierExhaustive5() throws Exception {

        BAC_LDA classifier = new BAC_LDA();
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for (int i=1; i<=5; i++){
            Tab tab1 = new Tab("", "", "Yes", 1-i*0.1);
            Tab tab2 = new Tab("", "", "No", i*0.1);
            tabs.add(tab1);
            tabs.add(tab2);
        }
        double[] result = classifier.classify(tabs);
        assertEquals("test classifier 2A",0.55,  result[0], 0);
        System.out.println(round(result[0],3) +" , " + result[1] );
        assertEquals("test classifier 2B",9.0/10,  result[1],0 );
    }
}