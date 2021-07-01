package com.srp.eways.util;

import com.yashoid.inputformatter.FormattableText;
import com.yashoid.inputformatter.Formatter;
import com.yashoid.inputformatter.InputFormatter;

/**
 * Created by Eskafi on 9/8/2019.
 */
public class PersianNumberFormatter extends InputFormatter {

    public PersianNumberFormatter() {
        super(makePersianNumberFormatter());
    }

    private static Formatter makePersianNumberFormatter() {

        return new Formatter() {

            @Override
            public void format(FormattableText text) {
                text.replaceAll('0', '۰');
                text.replaceAll('1', '۱');
                text.replaceAll('2', '۲');
                text.replaceAll('3', '۳');
                text.replaceAll('4', '۴');
                text.replaceAll('5', '۵');
                text.replaceAll('6', '۶');
                text.replaceAll('7', '۷');
                text.replaceAll('8', '۸');
                text.replaceAll('9', '۹');
            }

        };
    }
}
