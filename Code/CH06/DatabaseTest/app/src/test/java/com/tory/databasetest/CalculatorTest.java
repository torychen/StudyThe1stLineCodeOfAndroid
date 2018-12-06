package com.tory.databasetest;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
    private static final String TAG = "CalculatorTest --->";
    Calculator calculator;

    @Before
    public void setUp() throws Exception {
        //Log.d(TAG, "setUp: ");
        calculator = new Calculator();

    }

    @After
    public void tearDown() throws Exception {
        //Log.d(TAG, "tearDown: ");
    }

    @Test
    public void sum() {
        //expected: 6, sum of 1 and 5
        assertEquals(6, calculator.sum(1, 5), 0);

    }

    @Test
    public void substract() {
        assertEquals(1, calculator.substract(2,3));
    }
}