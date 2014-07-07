package com.cisco.prepos.services.discount.utils;

public class DiscountPartCounter {

    public static double getDiscountPart(double first, double second) {
        return getRoundedDouble(1 - (first / second));
    }

    public static double getRoundedDouble(double value) {
        return (double) Math.round(value * 100) / 100;
    }
}