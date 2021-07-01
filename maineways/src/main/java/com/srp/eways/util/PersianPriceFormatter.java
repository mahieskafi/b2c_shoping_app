package com.srp.eways.util;

import com.yashoid.inputformatter.FormattableText;
import com.yashoid.inputformatter.Formatter;
import com.yashoid.inputformatter.InputFormatter;

/**
 * Created by Eskafi on 9/2/2019.
 */
public class PersianPriceFormatter extends InputFormatter {

    private static Formatter makePersianPriceFormatter(final String divider) {
        return new Formatter() {

            private String mDivider = divider;

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


                int seek = text.length();
                while (seek > 3) {
                    text.insert(seek - 3, mDivider);

                    seek -= 3;
                }
            }

        };
    }

    public PersianPriceFormatter(String divider) {
        super(makePersianPriceFormatter(divider));
    }

}
