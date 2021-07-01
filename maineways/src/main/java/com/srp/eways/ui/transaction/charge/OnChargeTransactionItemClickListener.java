package com.srp.eways.ui.transaction.charge;

import android.view.View;

import com.srp.eways.model.transaction.charge.ChargeSale;

public interface OnChargeTransactionItemClickListener {

    void onShowMoreClickListener(boolean isShowMore,ChargeSale billReportItem);

}
