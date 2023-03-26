package com.uc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FOUtils {
    public static String removeDecimalPlaces(double number, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.toString();
    }
//
//    public static void main(String...args) {
//        double num = 3.141592653589793;
//        int decimalPlaces = 0;
//        String result = test.convert(num, decimalPlaces);
//        System.out.println(result); // output: "3.1416"
//    }

}
