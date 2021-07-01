package com.srp.ewayspanel.charge;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.gson.Gson;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.charge.model.ChargeData;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.di.DICommon;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.repository.remote.charge.ChargeApiImplementation;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.IOperator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static com.srp.eways.ui.charge.ChargeViewModel.ERROR_CHARGECHOICE_NOT_SELECTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ChargeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ChargeViewModel mViewModel;

    private Gson mGson;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mViewModel = Mockito.spy(new ChargeViewModel());

        mGson = new Gson();
    }

    public class Person {
        private String name;
    }

    @Test
    public void testGson() {
        String s = "{\"name\":\"ramin\"}";

        Person person = mGson.fromJson(s, Person.class);

        assertNotNull(person);
        assertEquals("ramin", person.name);
    }

    @Test
    public void testGetChargeDataSuccess() {
        ChargeApiImplementation chargeApiImplementation = DIM.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();
        assertTrue(mViewModel.isLoadingChargeData().getValue());

        Robolectric.flushForegroundThreadScheduler();

        ChargeData realResult = mViewModel.getChargeData();
        assertNotNull(realResult);

        assertFalse(mViewModel.isLoadingChargeData().getValue());

        mViewModel.setChargeDataConsumed();
        assertNull(mViewModel.getChargeDataLive().getValue());
    }

    @Test
    public void testGetChargeDataError500() {
        ChargeApiImplementation chargeApiImplementation = DIM.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.ERROR_SERVER_PROBLEM);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getChargeDataLive().getValue());
        assertEquals(NetworkResponseCodes.ERROR_SERVER_PROBLEM, mViewModel.getChargeData().mStatus);

        mViewModel.setChargeDataConsumed();

        assertNull(mViewModel.getChargeDataLive().getValue());
    }

    @Test
    public void testOnPhoneNumberChanged() {
        ChargeApiImplementation chargeApiImplementation = DIM.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        UserPhoneBook contactItem = new UserPhoneBook("mahsa", null);

        // inValid phoneNumber
        contactItem.setPhone("87300383837");
        mViewModel.onContactInfoChanged(contactItem);

        assertNull(mViewModel.getOperator().getValue());
        assertEquals(0, mViewModel.getChargeSelectionState().getValue().size());

        // valid phoneNumber
        contactItem.setPhone("09300383837");
        mViewModel.onContactInfoChanged(contactItem);

        assertNotNull(mViewModel.getOperator().getValue());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(0, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        //validOperator for irancell
        contactItem.setPhone("09300383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("ایرانسل", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());

        contactItem.setPhone("09010383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("ایرانسل", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(0, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09400383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("ایرانسل", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        //validOperator for hamrah aval
        contactItem.setPhone("09100383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("همراه اول", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09900383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("همراه اول", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09910383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("همراه اول", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09920383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("همراه اول", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09940383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("همراه اول", mViewModel.getOperator().getValue().getName());
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09200383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("رایتل دائمی", mViewModel.getOperator().getValue().getName());
        // expected in rightel operator is 3 : because we select next levels if there is only one option and select this one child until there is more than one option
        assertEquals(3, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(4, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);
        assertEquals(0, mViewModel.getChargeSelectionState().getValue().get(1).chargeOptionIndex);
        assertEquals(0, mViewModel.getChargeSelectionState().getValue().get(2).chargeOptionIndex);

        contactItem.setPhone("09210383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("رایتل اعتباری", mViewModel.getOperator().getValue().getName());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(3, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);

        contactItem.setPhone("09220383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("رایتل اعتباری", mViewModel.getOperator().getValue().getName());
        assertEquals(2, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(3, mViewModel.getChargeSelectionState().getValue().get(0).chargeOptionIndex);
    }

    @Test
    public void testOnChargeOptionSelected() {
        ChargeApiImplementation chargeApiImplementation = DIM.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        UserPhoneBook contactItem = new UserPhoneBook("mahsa", null);

        // valid phoneNumber
        contactItem.setPhone("09300383837");

        mViewModel.onContactInfoChanged(contactItem);
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());

        IOperator selectedOperator = mViewModel.getOperator().getValue();
        List<IChargeOption> operatorServiceOptions = selectedOperator.getChargeOptions();

        assertNotNull(operatorServiceOptions);
        assertTrue(operatorServiceOptions.size() > 0);

        int irancell_service_topup_index = 0;
        int irancell_service_internet_index = 1;
        int irancell_service_specialloffers_index = 2;

        int selectedIndexInLevel_1 = irancell_service_topup_index;
        Object selectedOperatorService = operatorServiceOptions.get(selectedIndexInLevel_1);
        mViewModel.onChargeOptionSelected(1, selectedIndexInLevel_1, selectedOperatorService);

        assertEquals(2, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(selectedIndexInLevel_1, mViewModel.getChargeSelectionState().getValue().get(1).chargeOptionIndex);

        List selectedServiceOptions = ((IChargeOption)selectedOperatorService).getChargeOptions();

        Object selectedServiceOption = selectedServiceOptions.get(0);
        int selectedIndexInLevel_2 = 0;

        mViewModel.onChargeOptionSelected(2, selectedIndexInLevel_2, selectedServiceOption);

        assertEquals(3, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(selectedIndexInLevel_2, mViewModel.getChargeSelectionState().getValue().get(2).chargeOptionIndex);

        /**
         if you are testing topup, unComment following 6 lines of code and unComment testing internetSection
         */

        List topupOptions = ((IChargeOption)selectedServiceOption).getChargeChoices();

        Object selectedTopupOption = topupOptions.get(0);
        int selectedIndexInLevel_3 = 0;

        mViewModel.onChargeOptionSelected(3, selectedIndexInLevel_3, selectedTopupOption);

        assertEquals(4, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(selectedIndexInLevel_3, mViewModel.getChargeSelectionState().getValue().get(2).chargeOptionIndex);

        /**
             if you are testing internet, unComment following lines
             Note: be aware that current mock data for internet is not complete!
             so when testing internetPackages you might encounter NullPointerException!(hint: complete mock data)
         */
//        List packageTypeOptions = ((IChargeOption)selectedServiceOption).getChargeOptions();
//
//        int selectedPackageTypeOptionIndex = 0;
//        Object selectedPackageTypeOption = packageTypeOptions.get(selectedPackageTypeOptionIndex);
//
//        mViewModel.onChargeOptionSelected(3, 0, selectedPackageTypeOption);
//
//        assertEquals(4, mViewModel.getChargeSelectionState().getValue().size());
//        assertEquals(0, mViewModel.getChargeSelectionState().getValue().get(3).chargeOptionIndex);
//
//        List internetPackageOptions = ((IChargeOption)selectedServiceOption).getChargeOptions();
//
//        int selectedInternetPackageOptionsIndex = 0;
//        Object selectedInternetPackageOption = internetPackageOptions.get(selectedInternetPackageOptionsIndex);
//
//        mViewModel.onChargeOptionSelected(4, 2, selectedInternetPackageOption);
//
//        assertEquals(5, mViewModel.getChargeSelectionState().getValue().size());
//        assertEquals(selectedInternetPackageOptionsIndex, mViewModel.getChargeSelectionState().getValue().get(4).chargeOptionIndex);

        //Todo: write test for IrancellSpecialOffers

        assertNotNull(mViewModel.getSelectedChargeChoice().getValue());// end of chargeSelection pass 1

        //now test if user change some prev selections
        selectedIndexInLevel_2 = 2;
        selectedServiceOption = selectedServiceOptions.get(selectedIndexInLevel_2);
        mViewModel.onChargeOptionSelected(2, selectedIndexInLevel_2, selectedServiceOption);

        assertEquals(3, mViewModel.getChargeSelectionState().getValue().size());
        assertEquals(selectedIndexInLevel_2, mViewModel.getChargeSelectionState().getValue().get(2).chargeOptionIndex);
        assertNull(mViewModel.getSelectedChargeChoice().getValue());

        contactItem.setPhone("09300383837");
        mViewModel.onContactInfoChanged(contactItem);
        assertEquals(1, mViewModel.getChargeSelectionState().getValue().size());
    }

    @Test
    public void testBuyCharge() {
        ChargeApiImplementation chargeApiImplementation = DICommon.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        UserPhoneBook contactItem = new UserPhoneBook("mahsa", null);

        //success scenario
        contactItem.setPhone("09300383837");
        mViewModel.onContactInfoChanged(contactItem);

        IOperator selectedOperator = mViewModel.getOperator().getValue();
        List<IChargeOption> operatorServiceOptions = selectedOperator.getChargeOptions();

//        mViewModel.onChargeOptionSelected(1, 0);
        int irancell_service_topup_index = 0;
        int irancell_service_internet_index = 1;
        int irancell_service_specialloffers_index = 2;

        int selectedIndexInLevel_1 = irancell_service_topup_index;
        Object selectedOperatorService = operatorServiceOptions.get(selectedIndexInLevel_1);
        mViewModel.onChargeOptionSelected(1, selectedIndexInLevel_1, selectedOperatorService);

//        mViewModel.onChargeOptionSelected(2, 0);
        List selectedServiceOptions = ((IChargeOption)selectedOperatorService).getChargeOptions();

        Object selectedServiceOption = selectedServiceOptions.get(0);
        int selectedIndexInLevel_2 = 0;

        mViewModel.onChargeOptionSelected(2, selectedIndexInLevel_2, selectedServiceOption);

//        mViewModel.onChargeOptionSelected(3, 0);
        List topupOptions = ((IChargeOption)selectedServiceOption).getChargeChoices();

        Object selectedTopupOption = topupOptions.get(0);
        int selectedIndexInLevel_3 = 0;

        mViewModel.onChargeOptionSelected(3, selectedIndexInLevel_3, selectedTopupOption);

        mViewModel.buyCharge();
        assertTrue(mViewModel.isBuyingCharge().getValue());

        Robolectric.flushForegroundThreadScheduler();

        BuyChargeResult buyChargeResult = mViewModel.getBuyChargeResultLive().getValue();

        assertNotNull(buyChargeResult);
        assertEquals(NetworkResponseCodes.SUCCESS, buyChargeResult.getStatus().intValue());

        assertFalse(mViewModel.isBuyingCharge().getValue());

        mViewModel.onBuyChargeResultConsumed();
        assertNull(mViewModel.getBuyChargeResultLive().getValue());

        // test no chargeChoiceSelected
        contactItem.setPhone("09200383837");
        mViewModel.onContactInfoChanged(contactItem);

        mViewModel.buyCharge();
        assertEquals(ERROR_CHARGECHOICE_NOT_SELECTED, mViewModel.getBuyChargeResultLive().getValue().getStatus().intValue());
    }

    @Test
    public void testAddChargeChoice() {
        ChargeApiImplementation chargeApiImplementation = DIM.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        UserPhoneBook contactItem = new UserPhoneBook("mahsa", null);

        contactItem.setPhone("09300383837");

        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("ایرانسل", mViewModel.getOperator().getValue().getName());

        IOperator selectedOperator = mViewModel.getOperator().getValue();
        List<IChargeOption> operatorServiceOptions = selectedOperator.getChargeOptions();

        // select chargeOptionsForLevel_1
        int irancell_service_topup_index = 0;
        int irancell_service_internet_index = 1;
        int irancell_service_specialloffers_index = 2;

        int selectedIndexInLevel_1 = irancell_service_topup_index;
        Object selectedOperatorService = operatorServiceOptions.get(selectedIndexInLevel_1);
        mViewModel.onChargeOptionSelected(1, selectedIndexInLevel_1, selectedOperatorService);

        // select chargeOptionsForLevel_2
        List selectedServiceOptions = ((IChargeOption)selectedOperatorService).getChargeOptions();

        Object selectedServiceOption = selectedServiceOptions.get(0);
        int selectedIndexInLevel_2 = 0;
        mViewModel.onChargeOptionSelected(2, selectedIndexInLevel_2, selectedServiceOption);

        assertEquals("شارژ مستقیم", mViewModel.getLastChargeOption().getName());

        assertEquals(5, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4000);
        assertEquals(6, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4001);
        assertEquals(7, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4002);
        assertEquals(8, mViewModel.getLastChargeOption().getChargeChoices().size());
    }

    @Test
    public void testRemoveChargeChoice() {
        ChargeApiImplementation chargeApiImplementation = DICommon.getChargeApi();
        chargeApiImplementation.setMode(NetworkResponseCodes.SUCCESS);

        mViewModel.loadChargeData();

        Robolectric.flushForegroundThreadScheduler();

        UserPhoneBook contactItem = new UserPhoneBook("mahsa", null);

        contactItem.setPhone("09300383837");

        mViewModel.onContactInfoChanged(contactItem);
        assertEquals("ایرانسل", mViewModel.getOperator().getValue().getName());

        IOperator selectedOperator = mViewModel.getOperator().getValue();
        List<IChargeOption> operatorServiceOptions = selectedOperator.getChargeOptions();

        // select chargeOptionsForLevel_1
        int irancell_service_topup_index = 0;
        int irancell_service_internet_index = 1;
        int irancell_service_specialloffers_index = 2;

        int selectedIndexInLevel_1 = irancell_service_topup_index;
        Object selectedOperatorService = operatorServiceOptions.get(selectedIndexInLevel_1);
        mViewModel.onChargeOptionSelected(1, selectedIndexInLevel_1, selectedOperatorService);

        // select chargeOptionsForLevel_2
        List selectedServiceOptions = ((IChargeOption)selectedOperatorService).getChargeOptions();

        Object selectedServiceOption = selectedServiceOptions.get(0);
        int selectedIndexInLevel_2 = 0;
        mViewModel.onChargeOptionSelected(2, selectedIndexInLevel_2, selectedServiceOption);

        assertEquals(5, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4000);
        assertEquals(6, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4001);
        assertEquals(7, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4002);
        assertEquals(8, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.addAndSelectChargeChoice(4003);
        assertEquals(9, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.removeChargeChoice(4000);
        assertEquals(8, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.removeChargeChoice(4001);
        assertEquals(7, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.removeChargeChoice(4002);
        assertEquals(6, mViewModel.getLastChargeOption().getChargeChoices().size());

        mViewModel.removeChargeChoice(4003);
        assertEquals(5, mViewModel.getLastChargeOption().getChargeChoices().size());
    }

    @After
    public void finish() {

    }

}
