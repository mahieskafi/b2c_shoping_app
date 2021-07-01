package com.srp.b2b2cEwaysPannel.di;


import com.srp.b2b2cEwaysPannel.repository.login.LoginRepository;
import com.srp.b2b2cEwaysPannel.repository.login.LoginRepositoryImplementation;
import com.srp.b2b2cEwaysPannel.repository.remote.login.LoginApiImplementation;
import com.srp.b2b2cEwaysPannel.ui.login.LoginViewModel;
import com.srp.eways.di.DIMain;

/**
 * Created by ErfanG on 3/1/2020.
 */
public class DICommon extends DIMain {

    public static <T> T getViewModel(Class<? extends T> clazz) {


        return DIMain.getViewModel(clazz);
    }

    public static LoginApiImplementation getLoginApi() {
        return LoginApiImplementation.INSTANCE;
    }

    public static LoginRepository getLoginRepository() {
        return LoginRepositoryImplementation.INSTANCE;
    }
}
