package com.srp.eways.repository.deposit.transacton;

import com.srp.eways.model.deposit.transaction.DepositTransactionItem;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by Eskafi on 9/3/2019.
 */
public interface DepositTransactionRepository {

    @NonNull
    Observable<Integer> getErrorCode();

    Observable<Boolean> isLoading();

    Observable<Boolean> hasMore();

    Observable<ArrayList<DepositTransactionItem>> getTransactions();

    void clearData();

    void resetToFirstPage();

    void loadMore();

}
