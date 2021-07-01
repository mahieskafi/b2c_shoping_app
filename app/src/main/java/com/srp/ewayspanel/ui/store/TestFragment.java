package com.srp.ewayspanel.ui.store;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.srp.eways.base.BaseFragment;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment<StoreViewModel> {

//    public Button mbtn;


    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance() {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public StoreViewModel acquireViewModel() {
        return DI.getViewModel(StoreViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        final FrameLayout container = view.findViewById(R.id.container);
//       LoadingStateView mLoadingStateView = view.findViewById(R.id.loadingstateview);
//       mLoadingStateView.setViewOrientation(LinearLayout.HORIZONTAL);
//        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, DI.getABResources().getString(com.srp.eways.R.string.loading_message), true);

        //TODO
//        getViewModel().getCategoryList();
//        getViewModel().getSliderListMutableLiveData().observe(this, new Observer<SliderResponse>() {
//            @Override
//            public void onChanged(SliderResponse sliderResponse) {
//                SliderView sliderView = new SliderView(getContext());
//
//                sliderView.setSliderList(sliderResponse.getSliderList());
//
//                container.removeAllViews();
//                container.addView(sliderView);
//            }
//        });
//        mbtn = view.findViewById(R.id.btnnn);
//        mbtn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//               InventoryNotExistDialog inventoryNotExistDialog = new InventoryNotExistDialog(getContext()) ;
//               inventoryNotExistDialog.show();
//
//            }
//        });


    }
}

