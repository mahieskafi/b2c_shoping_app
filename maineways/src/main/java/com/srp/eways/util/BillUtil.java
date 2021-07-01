package com.srp.eways.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.archivedList.BillPaymentDetail;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.srp.eways.ui.bill.inquiry.BillType;
import com.srp.eways.ui.bill.report.BillReportItemView;

import org.jetbrains.annotations.NotNull;

import ir.abmyapp.androidsdk.IABResources;

public class BillUtil {

    public static final int SERVICE_WATER = 1;
    public static final int SERVICE_ELECTRICITY = 2;
    public static final int SERVICE_GAS = 3;
    public static final int SERVICE_PHONE = 4;
    public static final int SERVICE_MOBILE = 5;
    public static final int SERVICE_MUNICIPALITY = 6;
    public static final int SERVICE_TAX = 8;
    public static final int SERVICE_DRIVING_POLICY_PENALTY = 9;

    public static final int DEPOSIT_GID = 41;
    public static final int PERSIAN_BANK = 42;
    public static final int SAMAN_BANK = 44;
    public static final int PERSIAN_BANK2 = 7;
    public static final int PERSIAN_BANK3 = 2;
    public static final int MELLAT_BANK = 0;
    public static final int MELLAT_BANK2 = 23;
    public static final int JIRING_BANK = 11;

    public static final int SUCCESS = 700;
    public static final int SUCCESS_IN_OTHER_DARGAH = 713;
    public static final int UNKNOWN_1 = 714;
    public static final int UNKNOWN_2 = -1;
    public static final int FAILED = 715;


    public static class BillPattern {
        String billIdPattern;
        String payIdPattern;

        public BillPattern(String billIdPattern, String payIdPattern) {
            this.billIdPattern = billIdPattern;
            this.payIdPattern = payIdPattern;
        }

        public String getBillIdPattern() {
            return billIdPattern;
        }

        public String getPayIdPattern() {
            return payIdPattern;
        }

        public static ArrayList<BillPattern> getBillPatterns() {
            ArrayList<BillPattern> billPatterns = new ArrayList<>();

            IABResources abResources = DIMain.getABResources();

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern1) + ": " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern1) + ": " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern1) + " :" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern1) + " :" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern1) + " : " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern1) + " : " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern1) + ":" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern1) + ":" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern1) + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern1) + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern2) + ": " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern2) + ": " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern2) + ":" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern2) + ":" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern2) + " :" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern2) + " :" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern2) + " : " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern2) + " : " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern2) + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern2) + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern3) + ": " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern3) + ": " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern3) + ":" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern3) + ":" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern3) + " :" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern3) + " :" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern3) + " : " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern3) + " : " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern3) + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern3) + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern4) + ": " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern4) + ": " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern4) + ":" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern4) + ":" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern4) + " :" + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern4) + " :" + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern4) + " : " + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern4) + " : " + ")" + "\\d+"));

            billPatterns.add(new BillPattern(
                    "(" + abResources.getString(R.string.sms_bill_id_key_pattern4) + ")" + "\\d+",
                    "(" + abResources.getString(R.string.sms_pay_id_key_pattern4) + ")" + "\\d+"));

            return billPatterns;
        }
    }

    public static String getServiceName(int service) {
        IABResources abResources = DIMain.getABResources();

        switch (service) {
            case SERVICE_WATER:
                return abResources.getString(R.string.bill_type_water);
            case SERVICE_ELECTRICITY:
                return abResources.getString(R.string.bill_type_electricity);
            case SERVICE_GAS:
                return abResources.getString(R.string.bill_type_gas);
            case SERVICE_MOBILE:
                return abResources.getString(R.string.bill_type_mobile);
            case SERVICE_MUNICIPALITY:
                return abResources.getString(R.string.bill_type_municipality);
            case SERVICE_PHONE:
                return abResources.getString(R.string.bill_type_phone);
            case SERVICE_TAX:
                return abResources.getString(R.string.bill_type_tax);
            case SERVICE_DRIVING_POLICY_PENALTY:
                return abResources.getString(R.string.bill_driving_policy_penalty);

        }

        return "نامشخص";
    }


    public static BillType getBillType(String billType) {

        switch (billType) {

            case "آب و فاضلاب":
                return BillType.WATER;
            case "برق":
                return BillType.ELECTRICITY;
            case "گاز":
                return BillType.GAS;
        }

        return null;
    }

    public static String getServiceName(BillType billType) {
        IABResources abResources = DIMain.getABResources();

        switch (billType) {
            case WATER:
                return abResources.getString(R.string.bill_type_water);
            case ELECTRICITY:
                return abResources.getString(R.string.bill_type_electricity);
            case GAS:
                return abResources.getString(R.string.bill_type_gas);
            case MOBILE:
                return abResources.getString(R.string.bill_type_mobile);
            case PHONE:
                return abResources.getString(R.string.bill_type_phone);
        }

        return "";
    }

    public static int getServiceId(BillType billType) {

        switch (billType) {
            case WATER:
                return SERVICE_WATER;
            case ELECTRICITY:
                return SERVICE_ELECTRICITY;
            case GAS:
                return SERVICE_GAS;
            case MOBILE:
                return SERVICE_MOBILE;
            case PHONE:
                return SERVICE_PHONE;
        }

        return -1;
    }

    @NotNull
    public static Drawable getBillStatusIcon(int status) {

        IABResources abResources = DIMain.getABResources();

        switch (status) {

            case 0:
                return abResources.getDrawable(R.drawable.bill_reciept_result_ok);
            case 1:
                return abResources.getDrawable(R.drawable.bill_reciept_result_unknown);
            default:
                return abResources.getDrawable(R.drawable.bill_reciept_result_failed);
        }
    }

    @NotNull
    public static String getDefaultBillStatusDescription(int status) {

        IABResources abResources = DIMain.getABResources();

        switch (status) {

            case SUCCESS:
            case SUCCESS_IN_OTHER_DARGAH:
                return abResources.getString(R.string.receipt_bill_status_success_description);
            case UNKNOWN_1:
            case UNKNOWN_2:
                return abResources.getString(R.string.receipt_bill_status_unknown_description);
            default:
                return abResources.getString(R.string.receipt_bill_status_failed_description);
        }
    }

    public static @DrawableRes
    int getServiceIcon(int service) {
        switch (service) {
            case SERVICE_WATER:
                return R.drawable.ic_water_bill;
            case SERVICE_ELECTRICITY:
                return R.drawable.ic_bill_item_icon_electricity;
            case SERVICE_GAS:
                return R.drawable.ic_gas_bill;
            case SERVICE_MOBILE:
                return R.drawable.ic_mobile_bill;
            case SERVICE_MUNICIPALITY:
                return R.drawable.ic_bill_item_icon_municipality;
            case SERVICE_PHONE:
                return R.drawable.ic_bill_item_icon_phone;
            case SERVICE_TAX:
                return R.drawable.ic_bill_item_icon_tax;
            case SERVICE_DRIVING_POLICY_PENALTY:
                return R.drawable.ic_bill_item_icon_driving_policy_penalty;
        }

        return 0;
    }

    @NotNull
    public static @DrawableRes
    int getBillReportStatusIcon(int status) {
        switch (status) {
            case SUCCESS:
            case SUCCESS_IN_OTHER_DARGAH:
                return R.drawable.bill_reciept_result_ok;
            case UNKNOWN_1:
            case UNKNOWN_2:
                return R.drawable.bill_reciept_result_unknown;
            default:
                return R.drawable.bill_reciept_result_failed;
        }
    }

    @NotNull
    public static BillReportItemView.Status getBillReportStatus(int status) {
        switch (status) {
            case SUCCESS:
            case SUCCESS_IN_OTHER_DARGAH:
                return BillReportItemView.Status.OK;
            case UNKNOWN_1:
            case UNKNOWN_2:
                return BillReportItemView.Status.UNKNOWN;
            default:
                return BillReportItemView.Status.FAILED;
        }
    }

    public static @ColorRes
    int getServiceColor(int service) {
        switch (service) {
            case SERVICE_WATER:
                return R.color.bill_type_water_backcolor;
            case SERVICE_ELECTRICITY:
                return R.color.bill_type_electricity_backcolor;
            case SERVICE_GAS:
                return R.color.bill_type_gas_backcolor;
            case SERVICE_MOBILE:
                return R.color.bill_type_mobile_backcolor;
            case SERVICE_MUNICIPALITY:
                return R.color.bill_type_municipality_backcolor;
            case SERVICE_PHONE:
                return R.color.bill_type_phone_backcolor;
            case SERVICE_TAX:
                return R.color.bill_type_tax_backcolor;
            case SERVICE_DRIVING_POLICY_PENALTY:
                return R.color.bill_type_driving_policy_penalty_backcolor;
        }

        return 0;
    }

    public static BillPaymentDetail getPaymentDetailFromText(String text) {
        ArrayList<BillPattern> billPatterns = BillPattern.getBillPatterns();
        for (BillPattern pattern : billPatterns) {

            Matcher billIdMatcher = Pattern.compile(pattern.getBillIdPattern()).matcher(text);
            if (billIdMatcher.find()) {

                Matcher payIdMatcher = Pattern.compile(pattern.getPayIdPattern()).matcher(text);
                if (payIdMatcher.find()) {
                    return new BillPaymentDetail(billIdMatcher.group().replaceAll("\\D+", ""), payIdMatcher.group().replaceAll("\\D+", ""));
                }
            }
        }
        return null;
    }

    private static BillPaymentDetail getPayIdFromText(BillPattern pattern, Matcher billIdMatcher, String text) {

        Matcher payIdMatcher = Pattern.compile(pattern.getPayIdPattern()).matcher(text);
        Matcher payIdMatcherWithoutSpace = Pattern.compile(pattern.getPayIdPattern()).matcher(text.trim());

        if (payIdMatcher.find()) {
            return new BillPaymentDetail(billIdMatcher.group().replaceAll("\\D+", ""), payIdMatcher.group().replaceAll("\\D+", ""));
        } else if (payIdMatcherWithoutSpace.find()) {
            return new BillPaymentDetail(billIdMatcher.group().replaceAll("\\D+", ""), payIdMatcherWithoutSpace.group().replaceAll("\\D+", ""));
        }
        return null;
    }

    public static BillPaymentDetail getPaymentDetailFromBarcode(String barCode) {
        if (barCode != null) {
            String billBarCode = barCode.replaceAll("\\D+", "");
            if (billBarCode.length() >= 26) {
                return new BillPaymentDetail(barCode.trim().substring(0, 13), barCode.trim().substring(13, barCode.length()));
            } else if (billBarCode.length() >= 16) {
                int barcodeLength = billBarCode.length();
                String paymentId = billBarCode.substring(barcodeLength - 8);

                int countFirst = barcodeLength - paymentId.length();
                String billId = billBarCode.substring(0, countFirst);
                return new BillPaymentDetail(billId, paymentId);
            }
        }
        return null;
    }

    public static int getBankName(int gId) {
        switch (gId) {
            case DEPOSIT_GID:
                return R.string.bill_payment_type_deposit_payment_type_title;
            case PERSIAN_BANK:
            case PERSIAN_BANK2:
            case PERSIAN_BANK3:
                return R.string.bill_payment_type_persian_bank_title;
            case SAMAN_BANK:
                return R.string.bill_payment_type_saman_bank_title;
            case MELLAT_BANK:
            case MELLAT_BANK2:
                return R.string.bill_payment_type_mellat_bank_title;
            case JIRING_BANK:
                return R.string.bill_payment_type_jiring_bank_title;
        }

        return 0;
    }

    public static int getBankIcon(int gId) {
        switch (gId) {
            case DEPOSIT_GID:
                return R.drawable.ic_bank_deposit;
            case PERSIAN_BANK:
            case PERSIAN_BANK2:
            case PERSIAN_BANK3:
                return R.drawable.ic_persian_bank;
            case SAMAN_BANK:
                return R.drawable.ic_bank_saman;
            case MELLAT_BANK:
            case MELLAT_BANK2:
                return R.drawable.ic_mellat;
            case JIRING_BANK:
                return R.drawable.ic_jiring;
        }

        return 0;
    }
}
