package com.srp.ewayspanel.ui.view.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.squareup.picasso.Picasso;
import com.srp.eways.model.banner.Data;
import com.srp.eways.ui.banner.BannerListView;
import com.srp.eways.ui.view.slider.SliderItemView;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.ui.main.GetBannerLink;

import java.util.List;

/**
 * Created by Eskafi on 10/22/2019.
 */
public class SliderView extends FrameLayout {

    private BannerListView mBanner;
    private GetBannerLink mBanerUrl;

    public SliderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SliderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SliderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_slider, this);

        mBanner = findViewById(R.id.banner);
        PagerIndicator pagerIndicator = findViewById(R.id.indicator);

        mBanner.setCustomIndicator(pagerIndicator);
        mBanerUrl=(GetBannerLink)context;

    }

    public void setSliderList(List<Data> sliderList) {
        if (sliderList.size() > 0) {
            mBanner.removeAllSliders();

            for (final Data bannerItem : sliderList) {

                SliderItemView textSliderView = new SliderItemView(getContext());

                textSliderView.setPicasso(Picasso.get());
                textSliderView
                        .image(bannerItem.getUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                textSliderView.image(bannerItem.getUrl()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        mBanerUrl.getDeepUrl(bannerItem.getTarget());
                    }
                });

                mBanner.addSlider(textSliderView);
                if (sliderList.size() == 1){
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
}
