package com.srp.eways.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.yashoid.inputformatter.FormattableText;
import com.yashoid.inputformatter.Formatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by ErfanG on 7/23/2019.
 */
public class Utils {

    private static final char[] persianNumbers =
            new char[]{'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};

    private static final String[] englishNumbers =
            new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


    public static String toPersianNumber(int number) {

        return toPersianNumber(String.valueOf(number));
    }

    public static String toPersianNumber(long number) {

        return toPersianNumber(String.valueOf(number));
    }

    public static String toPersianPriceNumberWithText(String number) {

        if (number != null && number.length() > 0) {
            return toPersianNumber(getNumberFromString(number));
        }
        return number;
    }

    public static String toPersianPriceNumber(String number) {

        if (number != null && number.length() > 0) {
            return toPersianNumber(new DecimalFormat("###,###").format(Long.valueOf(number)));
        }
        return number;
    }

    public static String toPersianPriceNumber(int number) {

        return toPersianNumber(new DecimalFormat("###,###").format(number));
    }

    public static String toPersianPriceNumber(long number) {

        return toPersianNumber(new DecimalFormat("###,###").format(number));
    }

    private static String getNumberFromString(String text) {
        if (TextUtils.isEmpty(text)) {

            return "";
        }

        String outNumber = "";
        String out = "";

        int length = text.length();

        for (int i = 0; i < length; i++) {

            char c = text.charAt(i);

            if (('0' <= c && c <= '9') || ('۰' <= c && c <= '۹')) {

                int number = Integer.parseInt(String.valueOf(c));
                outNumber += persianNumbers[number];
            } else {
                if (outNumber.length() > 3) {
                    outNumber = toPersianPriceNumber(Long.valueOf(outNumber));
                    out += outNumber + " ";
                    outNumber = "";
                } else {
                    out += outNumber + c;
                    outNumber = "";
                }
            }
        }
        if (outNumber.length() > 3) {
            outNumber = toPersianPriceNumber(Long.valueOf(outNumber));
            out += outNumber;
        }
        return out;

    }

    public static String toPersianNumber(String text) {

        if (TextUtils.isEmpty(text)) {

            return "";
        }

        String out = "";

        int length = text.length();

        for (int i = 0; i < length; i++) {

            char c = text.charAt(i);

            if ('0' <= c && c <= '9') {

                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫' || c == ',') {

                out += ',';
            } else {
                out += c;
            }
        }

        return out;
    }

    public static String removeThousandSeparator(String text) {

        if (TextUtils.isEmpty(text)) {

            return "";
        }

        String out = "";

        int length = text.length();

        for (int i = 0; i < length; i++) {

            char c = text.charAt(i);

            if (c == '٫' || c == ',' || c == '،' || c == '٬') {
                continue;
            } else {
                out += c;
            }
        }

        return out;
    }

    public static Formatter PersianNumberFormatter = new Formatter() {

        @Override
        public void format(FormattableText text) {

            text.replaceAll('0', persianNumbers[0]);
            text.replaceAll('1', persianNumbers[1]);
            text.replaceAll('2', persianNumbers[2]);
            text.replaceAll('3', persianNumbers[3]);
            text.replaceAll('4', persianNumbers[4]);
            text.replaceAll('5', persianNumbers[5]);
            text.replaceAll('6', persianNumbers[6]);
            text.replaceAll('7', persianNumbers[7]);
            text.replaceAll('8', persianNumbers[8]);
            text.replaceAll('9', persianNumbers[9]);
        }
    };

    public static String toEnglishNumber(int number) {

        return toEnglishNumber(String.valueOf(number));
    }

    public static String toEnglishNumber(String text) {

        if (text.length() == 0) {

            return "";
        }

        String out = "";

        int length = text.length();

        for (int i = 0; i < length; i++) {

            char c = text.charAt(i);

            if ('۰' <= c && c <= '۹') {

                int number = Integer.parseInt(String.valueOf(c));
                out += englishNumbers[number];

            } else if (c == '٫' || c == '،') {

                out += ',';
            } else {

                out += c;
            }
        }
        return out;
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {

            return false;
        }

        if (phoneNumber.startsWith("093") || phoneNumber.startsWith("090") || phoneNumber.startsWith("094") ||
                phoneNumber.startsWith("091") || phoneNumber.startsWith("099") || phoneNumber.startsWith("0920") ||
                phoneNumber.startsWith("0921") || phoneNumber.startsWith("0922")) {

            return true;
        }

        return false;
    }

    public static boolean isPhoneNumberLikeString(String phoneNumber) {
        if (phoneNumber.length() <= 3) {
            return true;//saeideh said once!
        }

        if (phoneNumber.startsWith("093") || phoneNumber.startsWith("090") || phoneNumber.startsWith("094") ||
                phoneNumber.startsWith("091") || phoneNumber.startsWith("099") || phoneNumber.startsWith("0920") ||
                phoneNumber.startsWith("0921") || phoneNumber.startsWith("0922")) {

            return true;
        }

        return false;
    }

    public static String creatStandardPhoneInput(String inputString) {

        inputString = inputString.replace(" ", "");
        inputString = inputString.replace("-", "");
        if (inputString.startsWith("+98")) {
            inputString = "0" + inputString.substring(3);
        }
        return inputString;
    }

    public static String getDateString(long time) {

        PersianCalendar persianCalendar = new PersianCalendar();

        persianCalendar.setTimeInMillis(time);

        int day = persianCalendar.getPersianDay();
        int month = persianCalendar.getPersianMonth() + 1;
        int year = persianCalendar.getPersianYear();

        return year + "/" + formatToMilitary(month) + "/" + formatToMilitary(day);
    }

    private static String formatToMilitary(int i) {
        return (i <= 9) ? "0" + i : String.valueOf(i);
    }

    public static String getDateStringServerFormat(long time) {
        PersianCalendar persianCalendar = new PersianCalendar();

        persianCalendar.setTimeInMillis(time);

        int day = persianCalendar.getPersianDay();
        int month = persianCalendar.getPersianMonth() + 1;
        int year = persianCalendar.getPersianYear();

        return day + "/" + month + "/" + year;
    }

    public static Intent getTelegramIntent(String telegramId) {

        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/" + telegramId));
    }

    public static Intent getEmailIntent(String emailId, String subject, String text) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailId});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);

        return emailIntent;
    }

    public static Intent getDialIntent(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }


    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();

            if (view == null) {
                view = new View(activity);
            }

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Activity activity, EditText view) {

        if (view != null) {

            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {

                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) DIMain.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = (connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null);

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void observeKeyboardVisibility(final View rootView, final KeyboardVisibilityChangeListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int mPreviousHeight;

            @Override
            public void onGlobalLayout() {
                int newHeight = rootView.getHeight();
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight - 100 > newHeight) {
                        // Height decreased: keyboard was shown
                        listener.onKeyboardVisibilityChanged(true);
                    } else if (mPreviousHeight < newHeight) {
                        // Height increased: keyboard was hidden
                        listener.onKeyboardVisibilityChanged(false);
                    } else {
                        // No change
                    }
                }
                mPreviousHeight = newHeight;
            }
        });
    }

    private static PersianCalendar getGorgianToPersian(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        return persianCalendar;
    }

    public static String getGorgianToPersianFormattedDate(Context context, int year, int monthOfYear, int dayOfMonth) {

        PersianCalendar persianCalendar = getGorgianToPersian(year, monthOfYear, dayOfMonth);

        return toPersianNumber(getFormattedDate(context, persianCalendar.getPersianYear(), persianCalendar.getPersianMonth(), persianCalendar.getPersianDay()));
    }

    public static String getGorgianToPersianNumberFormattedDate(int year, int monthOfYear, int dayOfMonth) {

        PersianCalendar persianCalendar = getGorgianToPersian(year, monthOfYear, dayOfMonth);

        int month = persianCalendar.getPersianMonth() + 1;
        return toPersianNumber(persianCalendar.getPersianYear() + "/" + month + "/" + persianCalendar.getPersianDay());
    }

    public static String getFormattedDate(Context context, int year, int monthOfYear, int dayOfMonth) {
        String monthString = "";
        switch (monthOfYear) {
            case 0:
                monthString = "فروردین";
                break;
            case 1:
                monthString = "اردیبهشت";
                break;
            case 2:
                monthString = "خرداد";
                break;
            case 3:
                monthString = "تیر";
                break;
            case 4:
                monthString = "مرداد";
                break;
            case 5:
                monthString = "شهریور";
                break;
            case 6:
                monthString = "مهر";
                break;
            case 7:
                monthString = "آبان";
                break;
            case 8:
                monthString = "آذر";
                break;
            case 9:
                monthString = "دی";
                break;
            case 10:
                monthString = "بهمن";
                break;
            case 11:
                monthString = "اسفند";
                break;
        }

        return String.format(context.getResources().getString(R.string.date_format), dayOfMonth, monthString);
    }

    public static String[] getFormattedDateFromTTemplate(String unFormattedDate) {
        String[] dateTime = new String[2];

        String[] date = unFormattedDate.split("T");
        // 0 Time
        dateTime[0] = (date[1].substring(0, 5));

        String[] parts = date[0].split("-");
        //1 date
        dateTime[1] = (Utils.getGorgianToPersianNumberFormattedDate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));

        return dateTime;
    }

    public static String changeDateToRtl(String text) {
        String[] parts = text.split("/");
        return parts[2] + "/" + parts[1] + "/" + parts[0];
    }

    public static boolean saveBitmap(Bitmap bitmap, Context context, String fileName) {
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/"
                    + context.getPackageName() + "/Files";

            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return false;
                }
            }

            File file = new File(dir.getPath() + File.separator + fileName + ".jpg");
            if (!file.exists()) {
                OutputStream outputStream = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Intent getShareFileIntent(String type, Uri filePath) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(type);
        shareIntent.putExtra(Intent.EXTRA_STREAM, filePath);

        return shareIntent;
    }

    public static File getFileforShare(String filename ,Context context ,Bitmap bitmap){

        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/"
                + context.getPackageName() + "/Files";

        File file = null;
        try {
             file = new File(path,filename + ".jpg" );
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
        }

        return file;
    }

    public static String removeStartAndEndWhiteSpace(String value) {

        int startSubIndex = 0;
        int endSubIndex = 0;

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '\u0020') {
                startSubIndex++;
            } else {
                break;
            }
        }

        value = value.substring(startSubIndex);

        for (int i = value.length() - 1; i >= 0; i--) {
            if (value.charAt(i) == '\u0020') {
                endSubIndex++;
            } else {
                break;
            }
        }

        value = value.substring(0, value.length() - endSubIndex);

        return value;
    }

    public static boolean isRtl(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        char c = string.charAt(0);
        return c >= 0x590 && c <= 0x6ff;
    }

    public static String getIMEI(Activity context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String removeZeroFormBillId(String id){
        int zeroNumber=0;
        while (id.substring(zeroNumber,zeroNumber+1).equals("0")){
            id=id.substring(zeroNumber+1,id.length());
        }
        return id;
    }

    public static boolean checkBillId(String billId) {

        String subBillid = billId.substring(0, billId.length() - 1);
        int controllNumber = Integer.parseInt(billId.substring(billId.length() - 1 , billId.length()));

        int billIdLength=subBillid.length();

        int SplitBillIdNumber = 0;
        int sumAllNumber = 0;
        int calculateTemp = 0;
        int counter = 2;

        while (billIdLength > 0) {
            SplitBillIdNumber = Integer.parseInt(subBillid.substring(billIdLength - 1 , billIdLength));
            sumAllNumber += counter * SplitBillIdNumber;

            counter++;
            billIdLength--;
            if (counter == 8) {
                counter = 2;
            }
        }

        calculateTemp = sumAllNumber % 11;

        if (calculateTemp == 0 || calculateTemp == 1) {
            if (controllNumber == 0) {
                return true;
            } else {
                return false;
            }

        } else {
            calculateTemp = 11 - calculateTemp;

            if (calculateTemp == controllNumber) {
                return true;
            } else {
                return false;
            }
        }
    }
}
