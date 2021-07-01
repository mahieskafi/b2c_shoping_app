package com.srp.eways.util;

/**
 * Created by Eskafi on 12/31/2019.
 */
public class TaxUtil {

    public static int getChargeAmount(long totalAmount, double tax) {
        return (int) Math.ceil(totalAmount / (tax * .1 + 1));
    }

    public static int getFinalPaidAmount(int totalAmount, double coef) {
        return (int) Math.floor(totalAmount * coef);
    }

    public static int getTaxAmount(int totalAmount, double tax) {
        return totalAmount - getChargeAmount(totalAmount, tax);
    }
}
