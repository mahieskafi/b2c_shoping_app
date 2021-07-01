package com.srp.b2b2cEwaysPannel.ui.main;

import android.os.Bundle;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.eways.base.BaseFragment;
import com.srp.eways.base.BaseViewModel;

public class TestFragment extends BaseFragment<BaseViewModel> {

    public static TestFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public BaseViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_otp;
    }
}
