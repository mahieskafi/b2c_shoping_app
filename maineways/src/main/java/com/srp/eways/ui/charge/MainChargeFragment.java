package com.srp.eways.ui.charge;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.di.DIMainCommon;
import com.srp.eways.model.banner.BannerResponse;
import com.srp.eways.model.banner.Data;
import com.srp.eways.model.survey.Poll;
import com.srp.eways.repository.banner.BannerRepository;
import com.srp.eways.ui.banner.BannerListView;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.receipt.ChargeReceiptViewModel;
import com.srp.eways.ui.survey.SurveyActivity;
import com.srp.eways.ui.survey.SurveyIconView;
import com.srp.eways.ui.view.slider.SliderItemView;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Constants;
import com.srp.eways.util.KeyboardVisibilityChangeListener;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public abstract class MainChargeFragment extends NavigationMemberFragment<UserInfoViewModel> {

    public interface GetBannerLinkCharge {
        void getDeepUrlCharge(String url);
    }

    protected WeiredToolbar weiredToolbar;
    protected TabLayout mTabBar;
    protected ViewPager mViewPager;
    private BannerListView mBanner;
    private MainChargePagerAdapter mAdapter;

    private CoordinatorLayout mCoordinateLayout;
    private AppBarLayout mAppBarLayout;
    private SurveyIconView mGoSurvey;

    private Boolean isKeyboardVisible=false;
    private int mBannerHeight = 0;

    GetBannerLinkCharge mGetbannerCharg;


    @Override
    public UserInfoViewModel acquireViewModel() {
        return DIMain.getViewModel(UserInfoViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mGetbannerCharg=(GetBannerLinkCharge)activity;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IABResources abResources = DIMain.getABResources();
        mBannerHeight = abResources.getDimenPixelSize(R.dimen.bannerheight);
        view.setBackgroundColor(DIMain.getABResources().getColor(R.color.buycharge_rootview_background));

        weiredToolbar = view.findViewById(R.id.toolbar);
        mTabBar = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.viewpager);
        mBanner = view.findViewById(R.id.banner);


        mCoordinateLayout = view.findViewById(R.id.coordinate_layout);
        mAppBarLayout = view.findViewById(R.id.banner_bar);

        mGoSurvey = view.findViewById(R.id.go_survey);
        setSurveyOptions();


        mAdapter = getAdapter();
        mViewPager.setAdapter(mAdapter);
        mTabBar.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);

        final ChargeReceiptViewModel viewModel = DIMain.getViewModel(ChargeReceiptViewModel.class);
        viewModel.isGoToTransaction().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isGoToTransaction) {

                if (isGoToTransaction != null) {
                    if (isGoToTransaction) {
                        mViewPager.setCurrentItem(0);
                        viewModel.consumeGoToTransaction();
                    }
                }

            }
        });

        setUpTabBar();
        setupWeiredToolbar();

        ViewGroup.LayoutParams layoutParams = mTabBar.getLayoutParams();
        if (getArguments() != null && getArguments().getBoolean(Constants.IS_PAGE_TRANSACTION_EXECUTED_KEY)) {
            layoutParams.height = 0;
            mTabBar.setLayoutParams(layoutParams);

            animateIn();
        } else {
            layoutParams.height = abResources.getDimenPixelSize(R.dimen.bill_page_tabbar_height);
            mTabBar.setLayoutParams(layoutParams);
        }

        observeRepeatTransactionPhoneNumber();

        ViewCompat.setElevation(weiredToolbar, abResources.getDimen(R.dimen.toolbar_elevation));
        ViewCompat.setElevation(mCoordinateLayout, abResources.getDimen(R.dimen.charge_fragment_banner_elevation));

        mBanner.setCustomIndicator((PagerIndicator) view.findViewById(R.id.indicator));

        Utils.observeKeyboardVisibility(view, new KeyboardVisibilityChangeListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean visible) {

                if (visible && isKeyboardVisible) {
                    mAppBarLayout.setExpanded(false);
                }
                isKeyboardVisible=visible;
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LinearLayout.LayoutParams viewPagerLayoutParams = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
                viewPagerLayoutParams.topMargin = verticalOffset;
                viewPagerLayoutParams.height += -verticalOffset;

                mViewPager.setLayoutParams(viewPagerLayoutParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Utils.hideKeyboard(getActivity());
            }


            @Override
            public void onPageSelected(int position) {
                Utils.hideKeyboard(getActivity());

                Fragment selectedFragment = mAdapter.getItem(position);

                if (position==0){
                    mAppBarLayout.setExpanded(false);
                    mAppBarLayout.setVisibility(View.INVISIBLE);
                }else {
                    isKeyboardVisible=false;
                    mAppBarLayout.setExpanded(true);
                    mAppBarLayout.setVisibility(View.VISIBLE);
                }

                if (selectedFragment instanceof MainChargePagerAdapter.PagerFragmentLifeCycle) {
                    MainChargePagerAdapter.PagerFragmentLifeCycle fragmentToShow = (MainChargePagerAdapter.PagerFragmentLifeCycle) selectedFragment;
                    fragmentToShow.onResumePagerFragment();
                }
            }
        });
    }

    private void setUpTabBar() {
        final IABResources abResources = DIMain.getABResources();

        mTabBar.setSelectedTabIndicatorColor(abResources.getColor(R.color.bill_page_tabbar_indicator_color));

        mAppBarLayout.setExpanded(true);
        final Typeface selectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold);
        final Typeface unSelectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan);

        for (int i = 0; i < mTabBar.getTabCount(); i++) {

            TextView textView = new TextView(getContext());
            textView.setTextColor(abResources.getColor(R.color.bill_page_tabbar_unselected_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_page_tabbar_text_size));
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(unSelectedTypeface);
            textView.setText(mTabBar.getTabAt(i).getText());

            mTabBar.getTabAt(i).setCustomView(textView);
        }

        mTabBar.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(selectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(abResources.getColor(R.color.bill_page_tabbar_selected_text_color));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(unSelectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(abResources.getColor(R.color.bill_page_tabbar_unselected_text_color));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mViewPager.setCurrentItem(mTabBar.getTabCount());

    }

    public void setupWeiredToolbar() {
        IABResources resources = DIMain.getABResources();

        weiredToolbar.setShowTitle(false);
        weiredToolbar.setShowDeposit(true);
        weiredToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background));

        weiredToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    protected void observeRepeatTransactionPhoneNumber() {
        ChargeViewModel viewModel = DIMain.getViewModel(ChargeViewModel.class);

        if (viewModel != null) {
            viewModel.getRepeatTransactionPhoneNumberLive().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String phoneNumber) {
                    if (phoneNumber != null) {
                        mViewPager.setCurrentItem(mTabBar.getTabCount(), true);
                    }
                }
            });

            viewModel.getmIssecondState().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    mAppBarLayout.setExpanded(!aBoolean);
                }
            });

            viewModel.getBannerLive().observe(getViewLifecycleOwner(), new Observer<BannerResponse>() {

                @Override
                public void onChanged(BannerResponse bannerResponse) {
                    if (bannerResponse.getBannerList().size() > 0) {

                        mBanner.removeAllSliders();

                        for (final Data bannerItem : bannerResponse.getBannerList()) {

                            SliderItemView textSliderView = new SliderItemView(getContext());

                            textSliderView.setPicasso(Picasso.get());
                            textSliderView
                                    .image(bannerItem.getUrl())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            textSliderView.image(bannerItem.getUrl()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    mGetbannerCharg.getDeepUrlCharge(bannerItem.getTarget());
                                }
                            });

                            mBanner.addSlider(textSliderView);

                            if (bannerResponse.getBannerList().size() == 1){
                                mBanner.stopAutoCycle();
                                mBanner.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                                mBanner.setPagerTransformer(false, new BaseTransformer() {
                                    @Override
                                    protected void onTransform(View view, float position) {}
                                });
                            }
                        }

                    }else {
                        mBanner.removeAllSliders();
                        SliderItemView textSliderView = new SliderItemView(getContext());

                        textSliderView.setPicasso(Picasso.get());
//                        int resID = get
                        textSliderView
                                .image(R.mipmap.default_bnner)
                                .setScaleType(BaseSliderView.ScaleType.Fit);

                        mBanner.addSlider(textSliderView);
                        mBanner.stopAutoCycle();
                        mBanner.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                        mBanner.setPagerTransformer(false, new BaseTransformer() {
                            @Override
                            protected void onTransform(View view, float position) {}
                        });
                    }
                }
            });

            viewModel.getBannerFromServer(BannerRepository.Companion.getBANNER_TYPE_CHARGE());

        }
    }


    @Override
    protected void getDataFromServer() {
        getViewModel().getCredit();
    }

    private void setSurveyOptions() {
        final IABResources resources = DIMain.getABResources();

        final String pollString = resources.getString("poll"); // TODO Put it as static final somewhere.

        boolean hasSurvey;

        if (!TextUtils.isEmpty(pollString)) {
            Poll poll = new Gson().fromJson(pollString, Poll.class);

            hasSurvey = !hasCompleteSurvey(poll.getId());
        } else {
            hasSurvey = false;
        }


        if (hasSurvey) {
            mGoSurvey.setVisibility(View.VISIBLE);
            mGoSurvey.setAction(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SurveyActivity.class);
                    intent.putExtra("survey", pollString); // TODO Add static newIntent method to SurveyActivity.
                    startActivity(intent);
                }
            });
        } else {
            mGoSurvey.setVisibility(View.GONE);
        }
    }

    public boolean hasCompleteSurvey(String id) {
        SharedPreferences preferences = getContext().getApplicationContext().getSharedPreferences(SurveyActivity.SURVEY_SHARE_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean("survey_" + id + "_complete", false);

    }


    protected MainChargePagerAdapter getPagerAdapter() {
        return mAdapter;
    }


    private void animateIn() {

        int tabBarHeight = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_page_tabbar_height);

        ValueAnimator anim = ValueAnimator.ofInt(0, tabBarHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mTabBar.getLayoutParams();
                layoutParams.height = val;
                mTabBar.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);
        anim.start();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {

        if (!hidden) {
            setSurveyOptions();
            weiredToolbar.clearSearch();
        }
    }


    protected ViewPager getViewPager() {
        return mViewPager;
    }

    public abstract MainChargePagerAdapter getAdapter();

}
