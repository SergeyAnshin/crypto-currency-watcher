package com.serg.ans.cryptocurrencywatcher.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathOperationServiceTest {


    @Test
    void differenceBetweenFourAndThreeGreaterSixPercent() {
        assertTrue(MathOperationService.isDifferenceBetweenNumbersGreaterPercent(4, 3, 6));
    }

    @Test
    void differenceBetweenSixAndFourGreaterFiftyPercent() {
        assertFalse(MathOperationService.isDifferenceBetweenNumbersGreaterPercent(6, 4, 50));
    }

    @Test
    void percentageDifferenceBetweenSixAndFourIsFiftyPercent() {
        assertEquals(50, MathOperationService.getPercentageDifferenceBetweenNumbers(6, 4));
    }

    @Test
    void percentageDifferenceBetweenThirteenPointFourHundredFiftyNineAndEightPointSixIsFiftySixPointFivePercent() {
        assertEquals(56.5, MathOperationService.getPercentageDifferenceBetweenNumbers(13.459, 8.6));
    }
}