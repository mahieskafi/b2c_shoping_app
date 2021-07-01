package com.srp.ewayspanel.ui.store.product.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.view.store.product.ProductItemSliderView;

import java.util.List;

public class ProductImageSliderAdapter extends PagerAdapter implements ProductItemSliderView.ProductItemSliderListener {

    public static int SMALL_IMAGE_VIEW_TYPE = 0;
    public static int FULL_SCREEN_VIEW_TYPE = 1;

    public interface ProductSliderAdapterListener {

        void onProductImageClicked(String url);

    }

    private int mViewType;

    private List<String> mUrls;

    private ProductSliderAdapterListener mListener;

    public ProductImageSliderAdapter(int viewType) {
        mViewType = viewType;
    }

    public void setUrls(List<String> urls) {
        mUrls = urls;

        notifyDataSetChanged();
    }

    public void setListener(ProductSliderAdapterListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {



        View itemView = null;

        if (mViewType == SMALL_IMAGE_VIEW_TYPE) {
            itemView = new ProductItemSliderView(container.getContext());
            ((ProductItemSliderView) itemView).setListener(this);
            ((ProductItemSliderView) itemView).setImageUrl(getUriAt(position));
        } else {
            itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.product_item_slider_full_screen, container, false);
            AppCompatImageView imageView = itemView.findViewById(R.id.image_view);

            Glide.with(container.getContext())
                    .load(getUriAt(position))
                    .into(imageView);
        }

        if (itemView != null) {
            container.addView(itemView);
        }

        return itemView;
    }

    private String getUriAt(int position) {
        return mUrls.get(position);
    }

    @Override
    public int getCount() {
        return mUrls == null ? 0 : mUrls.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void onProductImageClicked(String imageUrl) {
        mListener.onProductImageClicked(imageUrl);
    }

}
