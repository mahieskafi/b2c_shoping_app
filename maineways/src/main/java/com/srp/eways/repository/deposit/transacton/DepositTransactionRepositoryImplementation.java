package com.srp.eways.repository.deposit.transacton;

import android.os.Handler;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.BuildConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.deposit.transaction.DepositTransactionItem;
import com.srp.eways.model.deposit.transaction.DepositTransactionResponse;
import com.srp.eways.repository.remote.deposit.transaction.DepositTransactionApiService;
import com.srp.eways.util.Constants;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Eskafi on 9/3/2019.
 */
public class DepositTransactionRepositoryImplementation implements DepositTransactionRepository {

    private static DepositTransactionRepositoryImplementation INSTANCE;

    private DepositTransactionApiService mDepositTransactionApiService;

    private int mPageNumber = 0;

    private int mPageSize = 20;

    private PublishSubject<ArrayList<DepositTransactionItem>> mList;
    private PublishSubject<Boolean> mIsLoading ;
    private PublishSubject<Boolean> mHasMore ;
    private PublishSubject<Integer> mErrorCode;
    
    private ArrayList<DepositTransactionItem> mData;


    public static DepositTransactionRepositoryImplementation getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new DepositTransactionRepositoryImplementation();
        }

        return INSTANCE;
    }

    public DepositTransactionRepositoryImplementation() {

        this.mDepositTransactionApiService = DIMain.getDepositTransactionApi();

        mData = new ArrayList<>();

        mList = PublishSubject.create();
        mIsLoading = PublishSubject.create();
        mHasMore = PublishSubject.create();
        mErrorCode = PublishSubject.create();

        mList.onNext(mData);

        mIsLoading.onNext(false);
        mHasMore.onNext(true);
        mErrorCode.onNext(NetworkResponseCodes.SUCCESS);

    }

    @Override
    public void loadMore() {

        mIsLoading.onNext(true);

        if (BuildConfig.FLAVOR == Constants.MOCK_FLAVER) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callAndDecode();
                }
            }, 3000);
        } else {

            callAndDecode();
        }


    }

    private void callAndDecode() {

        mDepositTransactionApiService.getTransactionList(mPageNumber, mPageSize, new CallBackWrapper<DepositTransactionResponse>() {

            @Override
            public void onSuccess(DepositTransactionResponse t) {



                mPageNumber++;

                if (t.getItems()!= null && t.getItems().size() < mPageSize) {

                    mHasMore.onNext(false);
                }

                mData.addAll(t.getItems());
                mList.onNext(t.getItems() == null ? new ArrayList<DepositTransactionItem>() : t.getItems());

                mIsLoading.onNext(false);
                mErrorCode.onNext(NetworkResponseCodes.SUCCESS);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsLoading.onNext(false);
                mErrorCode.onNext(errorCode);
            }

        });
    }

    @Override
    public Observable<Integer> getErrorCode() {

        return mErrorCode;
    }

    @Override
    public Observable<Boolean> isLoading() {

        return mIsLoading;
    }

    @Override
    public Observable<Boolean> hasMore() {

        return mHasMore;
    }

    @Override
    public Observable<ArrayList<DepositTransactionItem>> getTransactions() {

        return mList;
    }

    @Override
    public void clearData() {

        mPageNumber = 0;

        mData = null;
        mList.onNext(mData);

        mIsLoading.onNext(false);
        mHasMore.onNext(true);
        mErrorCode.onNext(NetworkResponseCodes.SUCCESS);
    }

    @Override
    public void resetToFirstPage() {
        mPageNumber = 0;

        mData = new ArrayList<>();
        mList.onNext(mData);
    }
}
