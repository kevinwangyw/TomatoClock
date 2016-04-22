package com.kevinwang.tomatoClock;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dateTest() {
        Date myDate = new Date();
        String datestr = DateFormat.getDateInstance().format(myDate);
        String timestr = DateFormat.getTimeInstance().format(myDate);
        Date parseDate = null;
        DateFormat dateFormat = DateFormat.getDateInstance();
        try {
            parseDate = dateFormat.parse(datestr);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(myDate.getTime());
        System.out.println(datestr);
        System.out.println(timestr);
        System.out.println(parseDate.toString());
    }
}