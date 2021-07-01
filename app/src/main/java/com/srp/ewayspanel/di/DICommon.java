package com.srp.ewayspanel.di;


import androidx.room.Room;

import com.srp.eways.di.DIMain;
import com.srp.eways.repository.bill.archive.BillArchiveRepository;
import com.srp.eways.repository.local.bill.BillTableHelper;
import com.srp.ewayspanel.repository.appservice.AppServiceRepository;
import com.srp.ewayspanel.repository.appservice.AppServiceRepositoryImplementation;
import com.srp.ewayspanel.repository.bill.BillRepositoryImplementation;
import com.srp.ewayspanel.repository.remote.address.AddressApiImplementation;
import com.srp.ewayspanel.repository.remote.address.AddressApiService;
import com.srp.ewayspanel.repository.remote.appservice.AppServiceApiImplementation;
import com.srp.ewayspanel.repository.remote.sale.SaleReportApiImplementation;
import com.srp.ewayspanel.db.AppDataBase;
import com.srp.ewayspanel.repository.address.AddressRepository;
import com.srp.ewayspanel.repository.address.AddressRepositoryImplementation;
import com.srp.ewayspanel.repository.login.LoginRepository;
import com.srp.ewayspanel.repository.login.LoginRepositoryImplementation;
//import com.srp.ewayspanel.repository.remote.charge.ChargeApiImplementation;
import com.srp.ewayspanel.repository.remote.login.LoginApiImplementation;
//import com.srp.eways.repository.remote.phonebook.PhoneBookApiImplementation;
//import com.srp.eways.repository.remote.phonebook.PhoneBookApiService;
import com.srp.ewayspanel.repository.remote.shopcart.ShopCartApiImplementation;
import com.srp.ewayspanel.repository.remote.storepage.StorePageApiImplementation;
import com.srp.ewayspanel.repository.remote.storepage.filter.FilteredProductApiImplementation;
import com.srp.ewayspanel.repository.remote.storepage.mainpage.StoreMainPageApiImplementation;
import com.srp.ewayspanel.repository.remote.storepage.mainpage.StoreMainPageApiService;
import com.srp.ewayspanel.repository.remote.storetransaction.OrderTransactionApiImplementation;
import com.srp.ewayspanel.repository.sale.SaleReportRepository;
import com.srp.ewayspanel.repository.sale.SaleReportRepositoryImplementation;
import com.srp.ewayspanel.repository.shopcart.ShopCartRepository;
import com.srp.ewayspanel.repository.shopcart.ShopCartRepositoryImplementation;
import com.srp.ewayspanel.repository.storepage.StorePageRepository;
import com.srp.ewayspanel.repository.storepage.StorePageRepositoryImplementation;
import com.srp.ewayspanel.repository.storepage.filter.FilteredProductRepository;
import com.srp.ewayspanel.repository.storepage.filter.FilteredProductRepositoryImplementation;
import com.srp.ewayspanel.repository.storepage.mainpage.StoreMainPageRepository;
import com.srp.ewayspanel.repository.storepage.mainpage.StoreMainPageRepositoryImplementation;
import com.srp.ewayspanel.repository.storetransaction.OrderTransactionRepositoryImplementation;
import com.srp.ewayspanel.ui.bill.archive.BillArchiveViewModel;
import com.srp.ewayspanel.ui.followorder.FollowOrderViewModel;
import com.srp.ewayspanel.ui.landing.LandingViewModel;
import com.srp.ewayspanel.ui.login.AddressViewModel;
import com.srp.ewayspanel.ui.login.LoginViewModel;
import com.srp.ewayspanel.ui.main.MainViewModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.sale.SaleReportViewModel;
import com.srp.ewayspanel.ui.store.StoreViewModel;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;
import com.srp.ewayspanel.ui.store.mainpage.StoreMainPageViewModel;
import com.srp.ewayspanel.ui.store.mobilelist.StoreMobileListViewModel;
import com.srp.ewayspanel.ui.store.product.detail.ProductViewModel;
import com.srp.eways.util.Constants;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;
import com.srp.ewayspanel.repository.local.bill.BillDao;
import com.srp.ewayspanel.repository.local.bill.BillTableHelperImplementation;
import com.srp.ewayspanel.repository.local.address.AddressDao;
import com.srp.ewayspanel.repository.local.address.AddressTableHelper;
import com.srp.ewayspanel.repository.local.address.AddressTableHelperImplementation;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ErfanG on 25/08/2019.
 */
public class DICommon extends DIMain {

    protected static AppDataBase mDataBase;

    public static AppDataBase getAppDatabase() {
        if (mDataBase == null) {
            mDataBase = Room.databaseBuilder(getContext(), AppDataBase.class, Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return mDataBase;
    }


    public static AddressDao getAddressDao() {
        return getAppDatabase().addressDao();
    }

    public static BillDao getBillDao() {
        return getAppDatabase().billDao();
    }

    public static <T> T getViewModel(Class<? extends T> clazz) {
        if (clazz.equals(MainViewModel.class))
            return (T) new MainViewModel();

        if (clazz.equals(LoginViewModel.class))
            return (T) LoginViewModel.Companion.getInstance();

        if (clazz.equals(AddressViewModel.class)) {
            return (T) AddressViewModel.Companion.getInstance();
        }


        if (clazz.equals(OrderTransactionViewModel.class))
            return (T) OrderTransactionViewModel.getInstance();


        if (clazz.equals(StoreViewModel.class)) {

            return (T) new StoreViewModel();
        }

        if (clazz.equals(FilteredProductViewModel.class)) {

            return (T) new FilteredProductViewModel();
        }

        if (clazz.equals(StoreMobileListViewModel.class)) {

            return (T) new StoreMobileListViewModel();
        }

        if (clazz.equals(ProductViewModel.class)) {
            return (T) new ProductViewModel();
        }

        if (clazz.equals(ShopCartViewModel.class)) {
            return (T) ShopCartViewModel.INSTANCE;
        }

        if (clazz.equals(SaleReportViewModel.class)) {
            return (T) new SaleReportViewModel();
        }

        if (clazz.equals(StoreMainPageViewModel.class)) {
            return (T) new StoreMainPageViewModel();
        }

        if (clazz.equals(BillArchiveViewModel.class)) {
            return (T) new BillArchiveViewModel();
        }

        if (clazz.equals(BillArchiveViewModel.class)) {
            return (T) new BillArchiveViewModel();
        }

        if (clazz.equals(LandingViewModel.class)) {
            return (T) new LandingViewModel();
        }

        if (clazz.equals(FollowOrderViewModel.class)) {
            return (T) FollowOrderViewModel.Companion.getInstance();
        }

        return DIMain.getViewModel(clazz);
    }

    public static LoginRepository getLoginRepo() {
        return LoginRepositoryImplementation.getInstance();
    }

    public static LoginApiImplementation getLoginApi() {
        return LoginApiImplementation.Companion.getInstance();
    }

    public static SaleReportApiImplementation getSaleReportApi() {
        return SaleReportApiImplementation.Companion.getInstance();
    }

    public static SaleReportRepository getSaleReportRepo() {
        return SaleReportRepositoryImplementation.getInstance();
    }

    public static StorePageApiImplementation getStorePageApi() {
        return StorePageApiImplementation.Companion.getINSTANCE();
    }

    public static StorePageRepository getStorePageRepo() {
        return StorePageRepositoryImplementation.getInstance();
    }

    public static FilteredProductRepository getFilteredProductRepo() {
        return FilteredProductRepositoryImplementation.getInstance();
    }

    public static FilteredProductApiImplementation getFilteredProductApi() {
        return FilteredProductApiImplementation.getInstance();
    }

    public static OrderTransactionApiImplementation getStoreTransactionListApi() {
        return OrderTransactionApiImplementation.getInstance();
    }

    public static OrderTransactionRepositoryImplementation getStroreTransactionRepo() {
        return OrderTransactionRepositoryImplementation.getInstance();
    }

    public static ShopCartRepository getShopCartRepository() {
        return ShopCartRepositoryImplementation.INSTANCE;
    }

    @NotNull
    public static ShopCartApiImplementation getShopCartApi() {
        return ShopCartApiImplementation.INSTANCE;
    }

    @NotNull
    public static StoreMainPageRepository getStoreMainPageRepository() {
        return StoreMainPageRepositoryImplementation.INSTANCE;
    }

    @NotNull
    public static StoreMainPageApiService getStoreMainPageApi() {
        return StoreMainPageApiImplementation.INSTANCE;
    }

    public static AddressTableHelper getAddressHelper() {
        return AddressTableHelperImplementation.getInstance();
    }

    @NotNull
    public static AddressApiService getAddressApi() {
        return AddressApiImplementation.Companion.getInstance();
    }

    public static BillTableHelper getBillHelper() {
        return BillTableHelperImplementation.getInstance();
    }

    public static BillArchiveRepository getBillRepository() {
        return BillRepositoryImplementation.getInstance();
    }

    public static AddressRepository getAddressRepository() {
        return AddressRepositoryImplementation.getInstance();
    }

    public static OrderTransactionApiImplementation getOrderTransactionApi() {
        return OrderTransactionApiImplementation.getInstance();
    }

    public static AppServiceApiImplementation getAppServiceApi() {
        return AppServiceApiImplementation.Companion.getInstance();
    }

    public static AppServiceRepository getAppServiceRepo() {
        return AppServiceRepositoryImplementation.Companion.getInstance();
    }

}
