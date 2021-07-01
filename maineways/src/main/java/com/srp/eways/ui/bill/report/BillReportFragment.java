package com.srp.eways.ui.bill.report;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.srp.eways.R;
import com.srp.eways.base.BasePageableListFragment;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.BillPaymentResult;
import com.srp.eways.model.bill.BillPaymentStatusResult;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.model.bill.report.BillReportItem;
import com.srp.eways.model.bill.report.BillReportResponse;
import com.srp.eways.ui.IContentLoadingStateManager;
import com.srp.eways.ui.bill.paymenttype.BillInfo;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeFragment;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.paymenttype.PaymentResponseListener;
import com.srp.eways.ui.bill.receipt.ReceiptForShairingView;
import com.srp.eways.ui.bill.receipt.ReceiptFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.util.BillUtil;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.srp.eways.util.Constants.SAVE_TYPE;


public class BillReportFragment extends BasePageableListFragment<BillReportViewModel> implements
        IContentLoadingStateManager, BasePageableListFragment.OnRecyclereScrollListener
        , BillReportListAdapter.ActionsListener, PaymentResponseListener {

    private BillPaymentTypeViewModel billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.Companion.getInstance().getClass());

    public static BillReportFragment newInstance() {

        Bundle args = new Bundle();

        BillReportFragment fragment = new BillReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Observer<BillReportResponse> mReportListObserver;

    private Observer<BillPaymentStatusResult> billPaymentStatusResultObserver = new Observer<BillPaymentStatusResult>() {
        @Override
        public void onChanged(BillPaymentStatusResult billPaymentStatusResult) {
            if (billPaymentStatusResult != null) {
                BillPaymentResult billPaymentResult =
                        new BillPaymentResult(billPaymentStatusResult.getBills(), billPaymentStatusResult.getStatus(), 0, 0);
                BillPaymentResponse billPaymentResponse =
                        new BillPaymentResponse(billPaymentResult, "", billPaymentStatusResult.getStatus(), billPaymentStatusResult.getDescription());

                if (BillUtil.getBillReportStatus(billPaymentResponse.getStatus()) == BillReportItemView.Status.OK) {
                    onBackPressed();
                }

                showReceipt(billPaymentResponse);

                getViewModel().consumeBillPaymentStatusResponseLiveLiveData();
            }
        }
    };

    @Override
    public BillReportViewModel acquireViewModel() {
        return DIMain.getViewModel(BillReportViewModel.Companion.getInstance().getClass());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bill_report;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportListObserver = new Observer<BillReportResponse>() {
            @Override
            public void onChanged(BillReportResponse response) {
                if (mAdapter != null && response != null) {

                    //                        if (item.getStatus() != 0) {
                    //                        }
                    ArrayList<BillReportItem> data = new ArrayList<>(response.getData());

                    checkDataStatus(data, response.getStatus());
                    observeToBillReportResponse(false);
//                    mAdapter.notifyDataSetChanged();
//                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        };


        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);

        mRecyclerView = view.findViewById(R.id.report_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new BillReportListAdapter(getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {
                getViewModel().clearData();
                getList();
            }
        }, BillReportFragment.this);


        mRecyclerView.setAdapter(mAdapter);

        setPaginationScrollListener(this);

        if (!isNetworkConnected()) {
            setLoadingState(VIEWSTATE_NO_INTERNET_ERROR);
        }

        setLoadingRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
             getList();
            }
        });

//        mAdapter.clearData();
//        getViewModel().clearData();
//
//        getList();

    }

    private void getList() {
        if (!isNetworkConnected()) {
            setLoadingState(VIEWSTATE_NO_INTERNET_ERROR);

        } else {
            setLoadingState(VIEWSTATE_SHOW_LOADING);
            getViewModel().getBills();
            observeToBillReportResponse(true);
        }
    }

    @Override
    protected void getDataFromServer() {
        getList();
    }

    @Override
    public boolean hasMoreItems() {
        return getViewModel().hasMore().getValue() != null ? getViewModel().hasMore().getValue() : true;
    }

    @Override
    public void loadMoreItems() {
        getViewModel().getBills();
        observeToBillReportResponse(true);
    }

    @Override
    public void onShareActionClickListener(BillReportItem billReportItem) {
        BillTemp billTemp = new BillTemp(billReportItem.getInquiryNumber(), billReportItem.getBillId(), billReportItem.getPaymentId(), billReportItem.getBillTypeId(), billReportItem.getPrice());

        createBitmap(Constants.SHARE_TYPE, billTemp);
    }

    @Override
    public void onSaveActionClickListener(BillReportItem billReportItem) {
        BillTemp billTemp = new BillTemp(billReportItem.getInquiryNumber(), billReportItem.getBillId(), billReportItem.getPaymentId(), billReportItem.getBillTypeId(), billReportItem.getPrice());

        createBitmap(Constants.SAVE_TYPE, billTemp);
    }

    private void createBitmap(final int type, final BillTemp billTemp) {
        ReceiptForShairingView view = new ReceiptForShairingView(getContext());

        ArrayList<BillTemp> billTemps = new ArrayList<>();
        billTemps.add(billTemp);

        BillPaymentResult billPaymentResult = new BillPaymentResult(billTemps, 0, 41, 0);
        BillPaymentResponse billPaymentResponse = new BillPaymentResponse(billPaymentResult, null, 700, "");

        view.setBillResult(billPaymentResponse);
//        view.setActionContainerVisibility(View.INVISIBLE);
//        view.setDetailMargin(0, getResources().getDimensionPixelSize(R.dimen.bill_receipt_single_detail_margin_top), 0, 0);

        final Bitmap bitmap = ViewUtils.convertViewToBitmap(getContext(), view);

        getViewModel().isPermissionAccessed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    saveBitmap(bitmap, billTemp, type);
                    getViewModel().setPermissionAccessedConsumed();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, Constants.WRITE_STORAGE_PERMISSION);
        } else {
            saveBitmap(bitmap, billTemp, type);
        }

    }

    private void saveBitmap(Bitmap bitmap, BillTemp billTemp, int type) {
        IABResources resources = DIMain.getABResources();

        if (bitmap != null) {
            int random_int = (int) (Math.random() * (100 - 1 + 1) + 1);
            String fileName = "Bill" + billTemp.getBillId() + "_" + billTemp.getPaymentId() + "_" + billTemp.getInquiryNumber() + "_" + random_int;

            if (Utils.saveBitmap(bitmap, getContext(), fileName)) {
                if (type == SAVE_TYPE) {
                    Toast.makeText(getContext(), resources.getString(R.string.bill_receipt_save_result_success), Toast.LENGTH_LONG).show();
                } else {

                    Uri uri = null;
                    uri = FileProvider.getUriForFile(getContext(), "com.srp.ewayspanel.fileprovider", Utils.getFileforShare(fileName,getContext(),bitmap));
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

    private void observeToBillReportResponse(boolean addOrRemoveObserver) {

        if (addOrRemoveObserver) {
            if (!getViewModel().getBillsLive().hasObservers()) {
                getViewModel().getBillsLive().observe(this, mReportListObserver);
            }
        } else {
            getViewModel().getBillsLive().removeObserver(mReportListObserver);
        }
    }

    public void getNewData() {
        if (mLoadingStateView.getVisibility() != View.VISIBLE) {

            setLoadingState(VIEWSTATE_SHOW_LOADING);
            if (getViewModel() != null) {
                mAdapter.clearData();
                mAdapter.notifyDataSetChanged();
                getViewModel().clearData();
                getList();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getNewData();
    }

    @Override
    public void onPause() {
        super.onPause();

        observeToBillReportResponse(false);
    }

    @Override
    public void onShowMoreClickListener(boolean isShowMore, @NotNull BillReportItem billReportItem) {
        ArrayList<BillReportItem> billTempList = (ArrayList<BillReportItem>) mAdapter.getData();

        for (int i = 0; i < billTempList.size(); i++) {
            if (billTempList.get(i).getId() == billReportItem.getId()) {
                if (billTempList.get(i).isShowMore() != isShowMore) {
                    ((BillReportItem) mAdapter.getData().get(i)).setShowMore(isShowMore);
                }
            } else {
                ((BillReportItem) mAdapter.getData().get(i)).setShowMore(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPayActionClickListener(@NotNull BillReportItem billReportItem) {
        ArrayList<BillInfo> bills = new ArrayList<>();

        bills.add(new BillInfo(billReportItem.getId(), billReportItem.getBillTypeId(), Long.valueOf(billReportItem.getBillId()), Long.valueOf(billReportItem.getPaymentId()), billReportItem.getPrice()));

        billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_REPORT_PAGE);
        openFragment(BillPaymentTypeFragment.newInstance(BillReportFragment.this, bills));

    }

    @Override
    public void onPaymentResponseListener(@NotNull final BillPaymentResponse paymentResponse) {
        ReceiptFragment.BottomButtonsListener listener = new ReceiptFragment.BottomButtonsListener() {
            @Override
            public void onPhoneBackPressed(boolean isPhone) {

            }

            @Override
            public void onRetryClicked() {
                ArrayList<BillInfo> bills = new ArrayList<>();
                ArrayList<BillTemp> billPaymentDetailList = paymentResponse.getResult().getBills();

                assert billPaymentDetailList != null;
                for (BillTemp bill : billPaymentDetailList) {
                    bills.add(new BillInfo(bill.getId(), bill.getBillTypeId(), Long.valueOf(bill.getBillId()), Long.valueOf(bill.getPaymentId()), bill.getPrice()));
                }

                billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_REPORT_PAGE);
                openFragment(BillPaymentTypeFragment.newInstance(BillReportFragment.this, bills));
            }

            @Override
            public void onMainPageClicked() {
            }

            @Override
            public void onReportClicked() {
                onBackPressed();
            }
        };

        ReceiptFragment receiptFragment = ReceiptFragment.newInstance();
        receiptFragment.setBillPaymentResponse(paymentResponse);
        receiptFragment.setBottomButtonsListener(listener);
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);

        getNewData();
    }

    private void showReceipt(BillPaymentResponse billPaymentResponse) {
        billPaymentTypeViewModel.setCheckLoading(false);
        ReceiptFragment.BottomButtonsListener listener = new ReceiptFragment.BottomButtonsListener() {
            @Override
            public void onPhoneBackPressed(boolean isPhone) {

            }

            @Override
            public void onRetryClicked() {
            }

            @Override
            public void onMainPageClicked() {
                onBackPressed();
            }

            @Override
            public void onReportClicked() {
                onBackPressed();
                getNewData();
            }
        };

        ReceiptFragment receiptFragment = ReceiptFragment.newInstance();
        receiptFragment.setBillPaymentResponse(billPaymentResponse);
        receiptFragment.setBottomButtonsListener(listener);
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDeepLinkResponseReceived(@NotNull String requestId) {
        getNewData();

        getViewModel().getPaymentStatus(requestId);
        getViewModel().getBillPaymentStatusResponseLiveLiveData().observe(this, billPaymentStatusResultObserver);
    }
}
