package com.cisco.prepos.services.discount.utils;

public class DiscountPartCounter {
    public DiscountPartCounter() {
    }

    public double getDiscountPart(double first, int second) {
        return (double) Math.round((1 - (first / second)) * 100) / 100;
    }
}