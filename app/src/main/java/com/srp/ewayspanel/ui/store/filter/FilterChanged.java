package com.srp.ewayspanel.ui.store.filter;

import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;

/**
 * Created by Eskafi on 11/27/2019.
 */
public interface FilterChanged {
    void onFilterChanged(FilterProductRequest filterProductRequest);
}
