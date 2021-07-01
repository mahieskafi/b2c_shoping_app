package com.srp.ewayspanel.ui.store.product.detail.imagefullscreen;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.storepage.product.ProductDetailResponse;
import com.srp.ewayspanel.ui.store.product.detail.ProductImageSliderAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.srp.ewayspanel.ui.store.product.detail.ProductImageSliderAdapter.FULL_SCREEN_VIEW_TYPE;

/**
 * Created by Eskafi on 2/22/2020.
 */
public class ProductImageSliderFragment extends NavigationMemberFragment<BaseViewModel> implements ProductFullScreenSmallImageAdapter.ImageClickListener {

    private static String PRODUCT_DETAIL_RESPONSE = "productDetailResponse";

    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;
    private AppCompatImageView mExitImageView;
    private AppCompatImageView mArrowPreviousImage;
    private AppCompatImageView mArrowNextImage;

    private ProductImageSliderAdapter mAdapter;
    private ProductFullScreenSmallImageAdapter mSmallImagesAdapter;

    public static ProductImageSliderFragment newInstance(ProductDetailResponse productDetailResponse) {

        Bundle args = new Bundle();

        ProductImageSliderFragment fragment = new ProductImageSliderFragment();
        args.putSerializable(PRODUCT_DETAIL_RESPONSE, productDetailResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.view_pager);
        mRecyclerView = view.findViewById(R.id.recycler_images);
        mExitImageView = view.findViewById(R.id.exit_button);
        mArrowPreviousImage = view.findViewById(R.id.previous_arrow);
        mArrowNextImage = view.findViewById(R.id.next_arrow);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        ProductDetailResponse productDetailResponse = (ProductDetailResponse) getArguments().getSerializable(PRODUCT_DETAIL_RESPONSE);
        if (productDetailResponse != null) {
            List<String> images = productDetailResponse.productDetailModel.getProduct().getImages();

            mAdapter = new ProductImageSliderAdapter(FULL_SCREEN_VIEW_TYPE);
            mAdapter.setUrls(images);

            mViewPager.setAdapter(mAdapter);

            mSmallImagesAdapter = new ProductFullScreenSmallImageAdapter(ProductImageSliderFragment.this);
            List<ProductFullScreenSmallImageAdapter.ProductImageUrl> productImageUrls = new ArrayList<>();
            for (String url : images) {
                productImageUrls.add(new ProductFullScreenSmallImageAdapter.ProductImageUrl(url, false));
            }
            mSmallImagesAdapter.setUrls(productImageUrls);

            mRecyclerView.setAdapter(mSmallImagesAdapter);
            mSmallImagesAdapter.setSelectedItem(0);
        }

        mExitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mArrowNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem < mAdapter.getCount()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
            }
        });

        mArrowPreviousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem >= 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRecyclerView.smoothScrollToPosition(position);
                mSmallImagesAdapter.setSelectedItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_image_slider;
    }

    @Override
    public void onImageClickListener(int position) {
        mSmallImagesAdapter.setSelectedItem(position);
        mViewPager.setCurrentItem(position, true);
    }
}
