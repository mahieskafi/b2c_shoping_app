package com.srp.eways.ui.bill.archive;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.R;
import com.srp.eways.base.BasePageableListFragment;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.BillPaymentResult;
import com.srp.eways.model.bill.BillPaymentStatusResult;
import com.srp.eways.model.bill.archivedList.BillCounter;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse;
import com.srp.eways.model.bill.archivedList.BillTempResponse;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.IContentLoadingStateManager;
import com.srp.eways.ui.bill.MainBillFragment;
import com.srp.eways.ui.bill.archive.BillArchiveAdapter.BillArchivedItemListener;
import com.srp.eways.ui.bill.paymenttype.BillInfo;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeFragment;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.paymenttype.PaymentResponseListener;
import com.srp.eways.ui.bill.receipt.ReceiptFragment;
import com.srp.eways.ui.bill.report.BillReportItemView;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.bill.BillConfirmationDialog;
import com.srp.eways.ui.view.bill.BillPaymentSummaryView;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.util.BillUtil;
import com.srp.eways.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public abstract class MainBillArchiveListFragment<V extends MainBillArchiveViewModel> extends BasePageableListFragment<V>
        implements BillArchivedItemListener, LoadingStateView.RetryListener, BasePageableListFragment.OnRecyclereScrollListener
        , PaymentResponseListener
        , BillPaymentSummaryView.PaymentClickListener {

    private BillPaymentSummaryView mBillPaymentSummaryView;

    private int mDeletedId = 0;

    private boolean mIsListReset = false;

    protected MainBillFragment.OnPageChangeListener mPageChangeListener;

    private BillPaymentTypeViewModel billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.Companion.getInstance().getClass());

    private Observer<BillPaymentStatusResult> billPaymentStatusResultObserver = new Observer<BillPaymentStatusResult>() {
        @Override
        public void onChanged(BillPaymentStatusResult billPaymentStatusResult) {
            if (billPaymentStatusResult != null) {
                BillPaymentResult billPaymentResult =
                        new BillPaymentResult(billPaymentStatusResult.getBills(), billPaymentStatusResult.getStatus(), 0, 0);
                BillPaymentResponse billPaymentResponse =
                        new BillPaymentResponse(billPaymentResult, "", billPaymentStatusResult.getStatus(), billPaymentStatusResult.getDescription());

                if(BillUtil.getBillReportStatus(billPaymentResponse.getStatus()) == BillReportItemView.Status.OK){
                    onBackPressed();
                }
                showReceipt(billPaymentResponse);

                getViewModel().consumeBillPaymentStatusResponseLiveLiveData();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bill_archive_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.bill_archive_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);
        mBillPaymentSummaryView = view.findViewById(R.id.bill_payment_view);

        mBillPaymentSummaryView.setClickListener(this);

        mAdapter = new BillArchiveAdapter(getContext(), this, null);
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerDecoration();

        setLoadingRetryListener(this);
        setPaginationScrollListener(this);

        if (isNetworkConnected()) {
//            reading data from server when connection is success
            resetList();
        } else {
//        reading data from database when internet is no access
            getViewModel().getAllArchivedBills();
        }

        onObserveToResponse();
        getViewModel().consumePaymentListLiveData();
    }

    private void onObserveToResponse() {
        getViewModel().getOnlineBillArchivedListLiveData().observe(this, new Observer<BillTempResponse>() {
            @Override
            public void onChanged(BillTempResponse billTempResponse) {

                mBillPaymentSummaryView.setEnabled(true);
                if (billTempResponse != null) {
                    ArrayList<BillTemp> billTemps = billTempResponse.getData();

                    int status = billTempResponse.getStatus();
                    mErrorMessage = billTempResponse.getDescription();

                    if (billTemps != null) {
                        if (mAdapter.getData().size() == 0) {
                            getViewModel().deleteAllBills();
                        }

                        checkDataStatus(billTemps, status);

                        //update database
                        if (billTemps.size() > 0) {
                            getViewModel().saveAllBills(billTemps);
                        } else if (mAdapter.getData().size() == 0) {
                            //there is no data
                            getViewModel().deleteAllBills();
                        }
                    }
                    mIsListReset = false;
                    getViewModel().consumeOnlineBillArchivedListLiveData();
                }

            }
        });

        getViewModel().getBillArchivedListLiveData().observe(this, new Observer<ArrayList<BillTemp>>() {
            @Override
            public void onChanged(ArrayList<BillTemp> billTables) {
                if (billTables != null) {
                    checkDataStatus(billTables, NetworkResponseCodes.SUCCESS);

                    getViewModel().consumeBillArchivedListLiveData();
                }
            }
        });
    }

    private void setRecyclerDecoration() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                int spaceSide = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_archive_item_margin_side);
                int spaceBottom = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_archive_item_margin_bottom);
                int spaceTop = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_archive_item_margin_top);

                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = spaceTop;
                } else {
                    outRect.top = 0;
                }

                outRect.left = spaceSide;
                outRect.right = spaceSide;
                outRect.bottom = spaceBottom;
            }
        });
    }

    @Override
    public void onBillClicked(BillTemp billTable) {
    }

    @Override
    public void onBillRemoved(final int id) {
        IABResources resources = DIMain.getABResources();

        final BillConfirmationDialog dialog = new BillConfirmationDialog(getContext());

        dialog.setIcon(resources.getDrawable(R.drawable.ic_bill_dialog_question_mark));
        dialog.setText(resources.getString(R.string.bill_confirm_delete_dialog_text));
        dialog.setCancelButtonText(resources.getString(R.string.bill_confirm_delete_dialog_cancel_button_text));
        dialog.setConfirmButtonText(resources.getString(R.string.bill_confirm_delete_dialog_confirm_button_text));
        dialog.show();
        dialog.setClickListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                mDeletedId = id;
                getViewModel().removeTempBill(id);
                observeToRemoveResponse();

                dialog.dismiss();
            }

            @Override
            public void onCancelClicked() {
                dialog.dismiss();
            }
        });
    }

    protected void observeToRemoveResponse() {
        getViewModel().getRemoveBillResponseLiveData().observe(this, new Observer<BillTempRemoveResponse>() {
            @Override
            public void onChanged(BillTempRemoveResponse billTempRemoveResponse) {
                if (billTempRemoveResponse != null) {
                    if (billTempRemoveResponse.getStatus() == NetworkResponseCodes.SUCCESS) {
                        int position = getAdapterPosition(mDeletedId);
                        getViewModel().setChangeBillPaymentList(false, (BillTemp) mAdapter.getData().get(position));

                        mAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getData().size() == 0) {
                            setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY);
                        }

                        getViewModel().deleteBill(mDeletedId);

                        getViewModel().consumeRemoveBillResponseLiveData();
                    } else {
                        Toast.makeText(getContext(), billTempRemoveResponse.getDescription(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private int getAdapterPosition(int id) {
        List<BillTemp> billTempList = mAdapter.getData();
        for (int i = 0; i < billTempList.size(); i++) {
            if (billTempList.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBillSelected(final boolean isChecked, final BillTemp billTemp) {
        if (billTemp.isSelected() != isChecked) {
            ((BillTemp) mAdapter.getData().get(getAdapterPosition(billTemp.getId()))).setSelected(isChecked);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    getViewModel().setChangeBillPaymentList(isChecked, billTemp);
                }
            });

        }

        getViewModel().getPaymentLiveData().observe(this, new Observer<BillCounter>() {
            @Override
            public void onChanged(BillCounter billCounter) {
                if (billCounter != null) {
                    if (billCounter.getCount() > 0) {
                        mBillPaymentSummaryView.setViewEnabled(true);
                        mBillPaymentSummaryView.setCount(billCounter.getCount());
                        mBillPaymentSummaryView.setPrice(billCounter.getPrice());
                    } else {
                        mBillPaymentSummaryView.setViewEnabled(false);
                    }
                }
            }
        });

    }

    @Override
    public void onBillShowMore(boolean isShowMore, final BillTemp billTemp) {
        ArrayList<BillTemp> billTempList = (ArrayList<BillTemp>) mAdapter.getData();

        for (int i = 0; i < billTempList.size(); i++) {
            if (billTempList.get(i).getId() == billTemp.getId()) {
                if (billTempList.get(i).isShowMore() != isShowMore) {
                    ((BillTemp) mAdapter.getData().get(i)).setShowMore(isShowMore);
                }
            } else {
                ((BillTemp) mAdapter.getData().get(i)).setShowMore(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPaymentClicked() {
        ArrayList<BillInfo> bills = new ArrayList<>();
        ArrayList<BillTemp> billPaymentDetailList = getViewModel().getBillPaymentList();

        for (BillTemp bill : billPaymentDetailList) {
            bills.add(new BillInfo(bill.getId(), bill.getBillTypeId(), Long.valueOf(bill.getBillId()), Long.valueOf(bill.getPaymentId()), bill.getPrice(),bill.getInquiryNumber()));
        }

        billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_ARCHIVE_PAGE);
        openFragment(BillPaymentTypeFragment.newInstance(MainBillArchiveListFragment.this, bills));

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

                for (BillTemp bill : billPaymentDetailList) {
                    bills.add(new BillInfo(bill.getId(), bill.getBillTypeId(), Long.valueOf(bill.getBillId()), Long.valueOf(bill.getPaymentId()), bill.getPrice()));
                }

                billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_ARCHIVE_PAGE);
                openFragment(BillPaymentTypeFragment.newInstance(MainBillArchiveListFragment.this, bills));
            }

            @Override
            public void onMainPageClicked() {
            }

            @Override
            public void onReportClicked() {
                mPageChangeListener.onPageChanged(0);
            }
        };

        ReceiptFragment receiptFragment = ReceiptFragment.newInstance();
        receiptFragment.setBillPaymentResponse(paymentResponse);
        receiptFragment.setBottomButtonsListener(listener);
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);

        resetList();

        getViewModel().clearSelectedBillPaymentList();
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
                mPageChangeListener.onPageChanged(0);
            }
        };

        ReceiptFragment receiptFragment = ReceiptFragment.newInstance();
        receiptFragment.setBillPaymentResponse(billPaymentResponse);
        receiptFragment.setBottomButtonsListener(listener);
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
    }

    @Override
    public void onDeepLinkResponseReceived(@NotNull String requestId) {
        resetList();
        billPaymentTypeViewModel.setCheckLoading(true);

        getViewModel().clearSelectedBillPaymentList();

        getViewModel().getPaymentStatus(requestId);
        getViewModel().getBillPaymentStatusResponseLiveLiveData().observe(this, billPaymentStatusResultObserver);
    }

    @Override
    public void onRetryButtonClicked() {
        if (isNetworkConnected()) {
            getViewModel().getAllOnlineArchivedBills();
        }
    }

    @Override
    public boolean hasMoreItems() {
        return getViewModel().hasMore();
    }

    @Override
    public void loadMoreItems() {
        getViewModel().getAllOnlineArchivedBills();
    }

    private void resetList() {
        if (!mIsListReset) {
            mIsListReset = true;

            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();

            setLoadingState(LoadingStateView.STATE_LOADING);

            getViewModel().resetPage();
            getViewModel().getAllOnlineArchivedBills();
        }
    }

    public void onListResumed() {
        resetList();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetList();
    }

}
