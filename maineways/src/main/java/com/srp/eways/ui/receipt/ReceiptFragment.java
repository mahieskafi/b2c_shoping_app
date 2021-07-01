package com.srp.eways.ui.receipt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMainCommon;
import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.BillPaymentResult;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.ui.bill.receipt.ReceiptForShairingView;
import com.srp.eways.ui.bill.receipt.ReceiptItemView;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.transaction.TransactionsFragment;
import com.srp.eways.ui.transaction.charge.ChargeTransactionFragment;
import com.srp.eways.ui.transaction.deposit.DepositTransactionFragment;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.receipt.Receipt;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import ir.abmyapp.androidsdk.IABResources;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.srp.eways.util.Constants.SAVE_TYPE;

/**
 * Created by Eskafi on 9/7/2019.
 */
public class ReceiptFragment extends NavigationMemberFragment<BaseViewModel> {

    public static String RECEIPT_KEY = "receipt";
    public static String TITLE = "title";

    private ButtonElement mGoHomeButton;
    private ButtonElement mTransactionButton;
    private WeiredToolbar mToolbar;

    private Receipt mReceipt;
    private ReceiptItemView mReceiptView;


    private IABResources AB;

    public static ReceiptFragment newInstance(Receipt receipt) {
        ReceiptFragment fragment = new ReceiptFragment();

        Bundle args = new Bundle();
        args.putSerializable(RECEIPT_KEY, receipt);

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * sample code for open this fragment
     * <p>
     * List<ReceiptItem> list = new ArrayList<>();
     * list.add(new ReceiptItem("کلید 1 ", "مقدار 1", null));
     * list.add(new ReceiptItem("کلید 2 ", "مقدار 2", ContextCompat.getDrawable(this, R.drawable.ic_shop)));
     * list.add(new ReceiptItem("کلید 1 ", "مقدار 1", null));
     * list.add(new ReceiptItem("کلید 1 ", "مقدار 1", null));
     * <p>
     * <p>
     * Receipt receipt = new Receipt();
     * receipt.setReceiptItems(list);
     * receipt.setStatusCode(0);
     * receipt.setTitleDeposit("اعتبار شما : ");
     * receipt.setValueDeposit(Utils.toPersianPriceNumber(4500000));
     * <p>
     * Bundle bundle = new Bundle();
     * bundle.putSerializable(ReceiptFragment.RECEIPT_KEY,receipt);
     * openFragment(new BackStackFragment(ReceiptFragment.newInstance(), bundle, false));
     **/

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AB = DIMain.getABResources();

        mReceiptView = view.findViewById(R.id.receipt_view);
        final AppCompatTextView newDeposit = view.findViewById(R.id.new_deposit);
        AppCompatTextView newDepositTitle = view.findViewById(R.id.new_deposit_title);
        AppCompatTextView newDepositSuffix = view.findViewById(R.id.new_deposit_suffix);

        mGoHomeButton = view.findViewById(R.id.btn_goto_home);
        mTransactionButton = view.findViewById(R.id.btn_transaction);
        UserInfoViewModel mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);

        mToolbar = view.findViewById(R.id.toolbar);

        mReceipt = (Receipt) getArguments().getSerializable(RECEIPT_KEY);

        if (mReceipt.getStatusCode() == 0) {
            mReceiptView.setStatusIcon(AB.getDrawable(R.drawable.ic_transaction_success));
            mReceiptView.setDescriptionColor(AB.getColor(R.color.receipt_transaction_success_color));

            if (mReceipt.getReceiptType() == Receipt.RECEIPT_CHARGE) {
                mReceiptView.setDescription(AB.getString(R.string.receipt_buy_charge));
            } else if (mReceipt.getReceiptType() == Receipt.RECEIPT_INCREASE_DEPOSIT) {
                mReceiptView.setDescription(AB.getString(R.string.receipt_increase_deposit));
            } else if (mReceipt.getReceiptType() == Receipt.RECEIPT_INTERNET) {
                mReceiptView.setDescription(AB.getString(R.string.receipt_buy_internet));
            }

        } else {
            mReceiptView.setStatusIcon(AB.getDrawable(R.drawable.ic_transaction_faild));
            mReceiptView.setDescriptionColor(AB.getColor(R.color.receipt_transaction_failed_color));
            mReceiptView.setDescription(AB.getString(R.string.receipt_transaction_failed));
        }

        newDepositSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_suffix_size));
        newDepositSuffix.setTextColor(AB.getColor(R.color.receipt_transaction_deposit_title_color));
        newDepositSuffix.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        newDepositSuffix.setText(AB.getString(R.string.rial));

        newDepositTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_title_size));
        newDepositTitle.setTextColor(AB.getColor(R.color.receipt_transaction_deposit_title_color));
        newDepositTitle.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        newDepositTitle.setText(mReceipt.getTitleDeposit());

        newDeposit.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.receipt_deposit_text_size));
        newDeposit.setTextColor(AB.getColor(R.color.receipt_transaction_deposit_title_color));
        newDeposit.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
//        newDeposit.setText(mReceipt.getValueDeposit());

        mUserInfoViewModel.getCreditLiveData().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long deposit) {

                if (deposit != null) {
                    newDeposit.setText(Utils.toPersianPriceNumber(deposit));
                }
            }
        });

        mReceiptView.setChargeResult(mReceipt);
        mReceiptView.setSaveButtonAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBitmap(Constants.SAVE_TYPE);
            }
        });

        mReceiptView.setShareButtonAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBitmap(Constants.SHARE_TYPE);
            }

        });

        setupToolbar();
        setupGoHomeButton();
        if (mReceipt.getReceiptType() != Receipt.TRANSACTION) {
            setupTransactionButton();
        }
    }

    private void setupGoHomeButton() {
        mGoHomeButton.setText(AB.getString(R.string.receipt_go_home));
        mGoHomeButton.setTextSize(AB.getDimenPixelSize(R.dimen.receipt_button_text_size));
        mGoHomeButton.setTextColor(AB.getColor(R.color.increase_deposit_button_text_color));
        mGoHomeButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled));
        mGoHomeButton.setEnable(true);
        mGoHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupTransactionButton() {
        mTransactionButton.setText(AB.getString(R.string.receipt_go_transaction));
        mTransactionButton.setTextSize(AB.getDimenPixelSize(R.dimen.receipt_button_text_size));
        mTransactionButton.setTextColor(AB.getColor(R.color.bill_confirmation_button_cancel_text_color));
        mTransactionButton.setEnabledBackground(AB.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled));
        mTransactionButton.setEnable(true);
        mTransactionButton.setVisibility(View.VISIBLE);
        mTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                if (mReceipt.getReceiptType() == Receipt.RECEIPT_CHARGE || mReceipt.getReceiptType() == Receipt.RECEIPT_INTERNET) {

                    ChargeReceiptViewModel viewModel = DIMain.getViewModel(ChargeReceiptViewModel.class);
                    viewModel.setGoToTransaction(true);


                } else {

                    IncreaseDepositViewModel viewModel = DIMain.getViewModel(IncreaseDepositViewModel.Companion.getInstance().getClass());
                    viewModel.setGoToDepositTransaction(true);
                }

            }
        });
    }

    private void setupToolbar() {
        mToolbar.setBackgroundColor(AB.getColor(R.color.receipt_fragment_toolbar_background));
        if (mReceipt.getReceiptType() == Receipt.TRANSACTION) {
            mToolbar.setTitle(AB.getString(R.string.receipt_transaction_title_fragment));
        } else if (mReceipt.getReceiptType() == Receipt.RECEIPT_INCREASE_DEPOSIT) {
            mToolbar.setTitle(AB.getString(R.string.receipt_increase_deposit_title_fragment));
        } else {
            mToolbar.setTitle(AB.getString(R.string.receipt_title_fragment));
        }

        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.setBackgroundColor(AB.getColor(R.color.receipt_fragment_toolbar_background));
        mToolbar.setTitleTextColor(AB.getColor(R.color.white));
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.receipt_fragment_title_toolbar_size));
        mToolbar.setShowNavigationUp(AB.getBoolean(R.bool.receipt_toolbar_has_navigation_up));
        mToolbar.setShowDeposit(AB.getBoolean(R.bool.receipt_toolbar_has_deposit));
        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return new BaseViewModel();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_receipt;
    }

    private void createBitmap(final int type) {
//        View view = mReceiptView.getViewForSaving();
        ReceiptForShairingView view = new ReceiptForShairingView(getContext());
        view.setChargeResult(mReceipt);

        final Bitmap bitmap = ViewUtils.convertViewToBitmap(getContext(), view);

        getViewModel().isPermissionAccessed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    saveBitmap(bitmap, type);
                    getViewModel().setPermissionAccessedConsumed();
                }
            }

        });

        if (ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{(Manifest.permission.WRITE_EXTERNAL_STORAGE)}, Constants.WRITE_STORAGE_PERMISSION);
        } else {
            saveBitmap(bitmap, type);
        }

    }

    private void saveBitmap(Bitmap bitmap, int type) {
        IABResources resources = DIMain.getABResources();

        if (bitmap != null) {
            Calendar calendar = Calendar.getInstance();
            String fileName = "EWAYS_RECEIPT" + "_" + calendar.getTimeInMillis();

            if (Utils.saveBitmap(bitmap, getContext(), fileName)) {
                if (type == SAVE_TYPE) {
                    Toast.makeText(getContext(), resources.getString(R.string.bill_receipt_save_result_success), Toast.LENGTH_LONG).show();
                } else {

                    Uri uri = null;
                    uri = FileProvider.getUriForFile(getContext(), "com.srp.ewayspanel.fileprovider", Utils.getFileforShare(fileName, getContext(), bitmap));
                    Intent shareIntent = Utils.getShareFileIntent("image/jpeg", uri);
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));

                }
            } else {
                Toast.makeText(getContext(), resources.getString(R.string.bill_receipt_save_result_error), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), resources.getString(R.string.bill_receipt_save_result_error), Toast.LENGTH_LONG).show();
        }
    }
}
