package com.srp.eways.di;


import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srp.eways.network.AppKeyProvider;
import com.srp.eways.network.HeaderInterceptor;
import com.srp.eways.repository.banner.BannerRepository;
import com.srp.eways.repository.banner.BannerRepositoryImplementation;
import com.srp.eways.repository.bill.inquiry.BillInquiryRepository;
import com.srp.eways.repository.bill.inquiry.BillInquiryRepositoryImplementation;
import com.srp.eways.repository.bill.report.BillReportRepository;
import com.srp.eways.repository.bill.report.BillReportRepositoryImplementation;
import com.srp.eways.repository.bill.payment.BillInquiryPayRepository;
import com.srp.eways.repository.bill.payment.BillInquiryPayRepositoryImplementation;
import com.srp.eways.repository.charge.ChargeRepository;
import com.srp.eways.repository.charge.ChargeRepositoryImplementation;
import com.srp.eways.repository.chargetransaction.ChargeTransactionRepository;
import com.srp.eways.repository.chargetransaction.ChargeTransactionRepositoryImplementation;
import com.srp.eways.repository.chargetransaction.inquiry.InquiryTransactionRepository;
import com.srp.eways.repository.chargetransaction.inquiry.InquiryTransactionRepositoryImplementation;
import com.srp.eways.repository.contact.ContactSaleExpertRepository;
import com.srp.eways.repository.contact.ContactSaleExpertRepositoryImplementation;
import com.srp.eways.repository.deposit.DepositRepository;
import com.srp.eways.repository.deposit.DepositRepositoryImplementation;
import com.srp.eways.repository.deposit.transacton.DepositTransactionRepository;
import com.srp.eways.repository.deposit.transacton.DepositTransactionRepositoryImplementation;
import com.srp.eways.repository.local.PreferencesProvider;
import com.srp.eways.repository.logout.LogoutRepository;
import com.srp.eways.repository.logout.LogoutRepositoryImplementation;
import com.srp.eways.repository.phonebook.PhoneBookRepository;
import com.srp.eways.repository.phonebook.PhoneBookRepositoryImplementation;
import com.srp.eways.repository.remote.banner.BannerApiImplementation;
import com.srp.eways.repository.remote.banner.BannerApiService;
import com.srp.eways.repository.remote.bill.BillApiImplementation;
import com.srp.eways.repository.remote.bill.BillApiService;
import com.srp.eways.repository.remote.bill.archive.BillArchiveApiImplementation;
import com.srp.eways.repository.remote.bill.inquiry.BillInquiryApiImplementation;
import com.srp.eways.repository.remote.bill.inquiry.BillInquiryApiService;
import com.srp.eways.repository.remote.bill.payment.BillInquiryPayApiImplementation;
import com.srp.eways.repository.remote.bill.payment.BillInquiryPayApiService;
import com.srp.eways.repository.remote.bill.report.BillReportApiImplementation;
import com.srp.eways.repository.remote.bill.report.BillReportApiService;
import com.srp.eways.repository.remote.chargetransaction.ChargeTransactionApiImplementation;
import com.srp.eways.repository.remote.contact.ContactSaleExpertApiImplementation;
import com.srp.eways.repository.remote.deposit.DepositApiImplementation;
import com.srp.eways.repository.remote.deposit.transaction.DepositTransactionApiImplementation;
import com.srp.eways.repository.remote.login.MainLoginApiImplementation;
import com.srp.eways.repository.remote.logout.LogoutApiImplementation;
import com.srp.eways.repository.remote.phonebook.PhoneBookApiImplementation;
import com.srp.eways.repository.validatetoken.ValidateTokenRepository;
import com.srp.eways.repository.validatetoken.ValidateTokenRepositoryImplementation;
import com.srp.eways.ui.aboutus.AboutUsViewModel;
import com.srp.eways.ui.bill.MainBillViewModel;
import com.srp.eways.ui.bill.inquiry.BillInquiryViewModel;
import com.srp.eways.ui.bill.payment.BillPaymentViewModel;
import com.srp.eways.ui.bill.report.BillReportViewModel;
import com.srp.eways.ui.bill.receipt.ReceiptViewModel;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.contact.ContactViewModel;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigationdrawer.NavigationDrawerViewModel;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;
import com.srp.eways.ui.receipt.ChargeReceiptViewModel;
import com.srp.eways.ui.transaction.charge.ChargeTransactionViewModel;
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionViewModel;
import com.srp.eways.ui.transaction.deposit.DepositTransactionViewModel;
import com.srp.eways.util.analytic.EventSender;
import com.srp.eways.repository.remote.charge.ChargeApiImplementation;
import com.srp.eways.repository.remote.validatetoken.ValidateTokenApiImplementation;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;
import okhttp3.Interceptor;

public class DIMainCommon {

    private static Context sContext;

    private static Gson sGson;

    private static PreferencesProvider mPreferencesProvider;

    public static void setContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

    public static void setPreferencesProvider(PreferencesProvider preferencesProvider) {
        mPreferencesProvider = preferencesProvider;
    }

    public static PreferencesProvider getPreferencesProvider() {
       return mPreferencesProvider ;
    }

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setLenient()
                    .create();
        }

        return sGson;
    }


    public static void provideNetworkConfig(AppKeyProvider appKeyProvider) {
        //todo : set APP_KEY
    }
    protected static Interceptor getHeaderInterceptor(AppKeyProvider appKeyProvider) {
        return new HeaderInterceptor(appKeyProvider);
    }

    public static EventSender getEventSender() {
        return EventSender.get(getContext());
    }

    public static IABResources getABResources() {
        return ABResources.get(getContext());
    }

    public static <T> T getViewModel(Class<? extends T> clazz) {

        if (clazz.equals(NavigationDrawerViewModel.class))
            return (T) new NavigationDrawerViewModel();

        if (clazz.equals(UserInfoViewModel.class)) {
            return (T) UserInfoViewModel.Companion.getInstance();
        }

        if (clazz.equals(AboutUsViewModel.class)) {
            return (T) new AboutUsViewModel();
        }

        if (clazz.equals(ContactViewModel.class)) {
            return (T) new ContactViewModel();
        }

        if (clazz.equals(ChargeTransactionViewModel.class))
            return (T) ChargeTransactionViewModel.Companion.getInstance();

        if (clazz.equals(ChargeViewModel.class))
            return (T)  ChargeViewModel.getInstance();

        if (clazz.equals(InquiryTransactionViewModel.class))
            return (T) new InquiryTransactionViewModel();

        if (clazz.equals(DepositTransactionViewModel.class))
            return (T) DepositTransactionViewModel.Companion.getInstance();

        if (clazz.equals(IncreaseDepositViewModel.class))
            return (T) IncreaseDepositViewModel.Companion.getInstance();

        if (clazz.equals(PhoneBookViewModel.class)) {
            return (T)  PhoneBookViewModel.newInstance();
        }
        if (clazz.equals(BillPaymentViewModel.class)) {
            return (T) BillPaymentViewModel.Companion.getInstance();
        }

        if (clazz.equals(BillPaymentTypeViewModel.class)) {
            return (T) BillPaymentTypeViewModel.Companion.getInstance();
        }

        if (clazz.equals(BillInquiryViewModel.class)) {
            return (T) BillInquiryViewModel.Companion.newInstance();
        }
        if (clazz.equals(MainBillViewModel.class)) {
            return (T) new MainBillViewModel();
        }
        if (clazz.equals(BillReportViewModel.class)) {
            return (T) BillReportViewModel.Companion.getInstance();
        }

        if (clazz.equals(ReceiptViewModel.class)) {
            return (T) ReceiptViewModel.Companion.getInstance();
        }

        if (clazz.equals(ChargeReceiptViewModel.class)) {
            return (T) ChargeReceiptViewModel.Companion.getInstance();
        }

        return null;
    }


    public static ChargeRepository getChargeRepo() {
        return ChargeRepositoryImplementation.getInstance();
    }

    public static ChargeApiImplementation getChargeApi() {
        return ChargeApiImplementation.getInstance();
    }

    public static PhoneBookRepository getPhoneBookRepository() {
        return PhoneBookRepositoryImplementation.getInstance();
    }

    public static PhoneBookApiImplementation getPhoneBookApiImplementation() {
        return PhoneBookApiImplementation.getInstance();
    }

    public static MainLoginApiImplementation getMainLoginApiImplementation() {
        return MainLoginApiImplementation.Companion.getInstance();
    }

    public static DepositApiImplementation getDepositApi() {
        return DepositApiImplementation.Companion.getInstance();
    }

    public static DepositRepository getDepositRepo() {
        return DepositRepositoryImplementation.getInstance();
    }

    public static DepositTransactionRepository getDepositTransactionRepo() {
        return DepositTransactionRepositoryImplementation.getInstance();
    }

    public static DepositTransactionApiImplementation getDepositTransactionApi() {
        return DepositTransactionApiImplementation.Companion.get();
    }

    public static ChargeTransactionRepository getChargeTransRepo() {
        return ChargeTransactionRepositoryImplementation.Companion.getInstance();
    }

    public static ChargeTransactionApiImplementation getChargeTransApi() {
        return ChargeTransactionApiImplementation.Companion.getInstance();
    }

    public static InquiryTransactionRepository getInquiryTransactionRepo() {
        return InquiryTransactionRepositoryImplementation.Companion.getInstance();
    }

    public static ContactSaleExpertApiImplementation getContactSaleExpertApiImplementation() {
        return ContactSaleExpertApiImplementation.getInstance();
    }

    public static ContactSaleExpertRepository getContactSaleExpertRepository() {
        return ContactSaleExpertRepositoryImplementation.getInstance();
    }

    public static ValidateTokenApiImplementation getValidateTokenApi() {
        return ValidateTokenApiImplementation.Companion.getInstance();
    }

    public static ValidateTokenRepository getValidTokenRepo() {
        return ValidateTokenRepositoryImplementation.getInstance();
    }

    public static LogoutApiImplementation getLogoutApi() {
        return LogoutApiImplementation.getInstance();
    }

    public static LogoutRepository getLogoutRepo() {
        return LogoutRepositoryImplementation.getInstance();
    }

    public static BillArchiveApiImplementation getBillArchiveApi() {
        return BillArchiveApiImplementation.Companion.getInstant();
    }


    public static BillInquiryRepository getBillInquiryRepo(){
        return BillInquiryRepositoryImplementation.INSTANCE;
    }

    public static BillInquiryPayRepository getBillInquiryPayRepo(){
        return BillInquiryPayRepositoryImplementation.INSTANCE;
    }

    public static BillInquiryApiService getBillInquiryApi(){
        return BillInquiryApiImplementation.INSTANCE;
    }

    public static BillInquiryPayApiService getBillInquiryPayApi(){
        return BillInquiryPayApiImplementation.INSTANCE;
    }

    public static BillApiService getMainBillApi() {
        return BillApiImplementation.INSTANCE;
    }

    public static BillApiImplementation getBillApi() {
        return BillApiImplementation.INSTANCE;
    }

    public static BillReportRepository getBillReportRepo(){
        return new BillReportRepositoryImplementation();
    }

    public static BillReportApiService getBillReportApi(){
        return new BillReportApiImplementation();
    }

    public static BannerApiService getBannerApi() {
        return BannerApiImplementation.INSTANCE;
    }

    public static BannerRepository getBannerRepo(){
        return new BannerRepositoryImplementation();
    }
}
