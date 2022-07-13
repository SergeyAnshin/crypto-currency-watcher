package com.serg.ans.cryptocurrencywatcher.service;

public class MathOperationService {

    public static double getPercentageDifferenceBetweenNumbers(double firstNumber, double secondNumber) {
        return firstNumber/secondNumber * 100 - 100;
    }

    public static boolean isDifferenceBetweenNumbersGreaterPercent(double firstValue, double secondValue, double percent) {
        return MathOperationService.getPercentageDifferenceBetweenNumbers(firstValue, secondValue) > percent;
    }
}
