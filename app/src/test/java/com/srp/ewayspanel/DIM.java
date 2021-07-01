package com.srp.ewayspanel;



import com.srp.eways.repository.charge.ChargeRepository;
import com.srp.eways.repository.charge.ChargeRepositoryImplementation;
import com.srp.eways.repository.remote.charge.ChargeApiImplementation;
import com.srp.eways.repository.remote.validatetoken.ValidateTokenApiImplementation;
import com.srp.ewayspanel.repository.login.LoginRepository;
import com.srp.ewayspanel.repository.login.LoginRepositoryImplementation;
import com.srp.eways.repository.remote.contact.ContactSaleExpertApiImplementation;
import com.srp.eways.repository.remote.deposit.DepositApiImplementation;
import com.srp.ewayspanel.repository.remote.login.LoginApiImplementation;
import com.srp.eways.repository.remote.phonebook.PhoneBookApiImplementation;
import com.srp.ewayspanel.repository.remote.storepage.filter.FilteredProductApiImplementation;
import com.srp.ewayspanel.ui.login.LoginViewModel;


/**
 * Created by Arcane on 7/20/2019.
 */
public class DIM {

    public static LoginRepository provideLoginRepository() {
        return LoginRepositoryImplementation.getInstance();
    }

    public static LoginApiImplementation getLoginApi(){
        return LoginApiImplementation.Companion.getInstance();
    }

    public static ChargeRepository provideChargeRepository() {
        return ChargeRepositoryImplementation.getInstance();
    }

    public static LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }
    public static ChargeRepository getChargeRepo(){
        return ChargeRepositoryImplementation.getInstance();
    }

    public static ChargeApiImplementation getChargeApi(){
        return ChargeApiImplementation.getInstance();
    }

    public static ValidateTokenApiImplementation getValidateTokenApi(){
        return ValidateTokenApiImplementation.Companion.getInstance();
    }

    public static DepositApiImplementation getDepositApi(){
        return DepositApiImplementation.Companion.getInstance();
    }

    public static ContactSaleExpertApiImplementation getContactApi(){
        return ContactSaleExpertApiImplementation.getInstance();
    }

    public static PhoneBookApiImplementation getPhoneBookApi(){
        return PhoneBookApiImplementation.getInstance();
    }

    public static FilteredProductApiImplementation getFilterProductApi(){
        return FilteredProductApiImplementation.getInstance();
    }
}
