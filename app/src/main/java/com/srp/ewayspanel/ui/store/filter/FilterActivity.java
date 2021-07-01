package com.srp.ewayspanel.ui.store.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.base.BaseViewModel;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;

/**
 * Created by Eskafi on 11/10/2019.
 */
public class FilterActivity extends BaseActivity<BaseViewModel> {

    public final static String FILTER_REQUEST_EXTERA = "filter_request_extera";

    public static Intent getIntent(Context context, FilterProductRequest filterProductRequest) {
        Intent intent = new Intent(context, FilterActivity.class);
        intent.putExtra(FILTER_REQUEST_EXTERA, filterProductRequest);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
        FilterFragment.newInstance(((FilterProductRequest) getIntent().getSerializableExtra(FILTER_REQUEST_EXTERA)))).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }
}
