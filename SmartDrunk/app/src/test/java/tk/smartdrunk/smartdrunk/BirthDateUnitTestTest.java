package tk.smartdrunk.smartdrunk;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static tk.smartdrunk.smartdrunk.models.User.isValidDate;

/**
 * Created by Daniel on 8/3/2017.
 */
public class BirthDateUnitTestTest {
    @Test
    public void testDateIsNull() {
        assertFalse(isValidDate(null));
    }

    @Test
    public void testDayIsInvalid() {
        assertFalse(isValidDate("02.32.2012"));
    }

    @Test
    public void testMonthIsInvalid() {
        assertFalse(isValidDate("20.31.2012"));
    }

    @Test
    public void testYearIsInvalid() {
        assertFalse(isValidDate("20.31.19991"));
    }

    @Test
    public void testDateFormatIsInvalid() {
        assertFalse(isValidDate("2012.02.20"));
    }

    @Test
    public void testDateFeb29_2017() {
        assertFalse(isValidDate("02.29.2017"));
    }

    @Test
    public void testDateFeb29_2011() {
        assertFalse(isValidDate("29.02.2011"));
    }

    @Test
    public void testDateJuly() {
        assertTrue(isValidDate("07.28.2017"));
    }

    @Test
    public void testDateIsValid_1() {
        assertTrue(isValidDate("01.31.2012"));
    }

    @Test
    public void past() {
        assertTrue(isValidDate("08.01.2017"));
    }

    @Test
    public void test2(){
        assertTrue(isValidDate("08.01.2017"));
    }

    /*The test below where created at the start of August 2017.
    if they fail it is since time has pass and can be removed */
    @Test
    public void Future() {
        assertFalse(isValidDate("08.29.2020"));
    }

    @Test
    public void test1(){
        assertFalse(isValidDate("09.29.2017"));
    }
}