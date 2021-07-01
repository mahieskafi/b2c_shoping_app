package com.srp.ewayspanel.ui.club;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener;

import org.jetbrains.annotations.NotNull;

public class ClubProductsAdapter extends BaseRecyclerAdapter2<ProductInfo, BaseViewHolder> {

    private long mLoyaltyScore = 0;
    private ProductItemClickListener mItemClickListener;

    public static final int TYPE_HEADER = 2;
    private Context mContext;
    private int mHeaderViewHeight = 0;
    private int mItemViewHeight = 0;

    private boolean mIsHorizontal = false;

    public ClubProductsAdapter(Context context, ProductItemClickListener productItemClickListener,
                               RetryClickListener mRetryClickListener) {
        super(context, mRetryClickListener);
        mContext = context;

        mItemClickListener = productItemClickListener;
    }

    public ClubProductsAdapter(@NotNull Context context, @NotNull BaseRecyclerAdapter2.RetryClickListener retryListener,
                               ProductItemClickListener itemClickListener, boolean isHorizontal) {
        super(context, retryListener);

        mContext = context;
        mItemClickListener = itemClickListener;
        mIsHorizontal = isHorizontal;
    }


    public void setLoyaltyScore(long loyaltyScore) {
        mLoyaltyScore = loyaltyScore;
    }

    @Override
    public BaseViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            DisplayMetrics displayMetrics = ViewUtils.getDisplayMetrics(parent.getContext());

            int height = 0;
            if (mItemViewHeight != 0) {
                height = mItemViewHeight;
            }

            if (mIsHorizontal) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_club_horizontal, parent, false);

                if (height == 0) {
                    height = ((int) (displayMetrics.density * 136));
                }
                int margin = ((int) (displayMetrics.density * 10));

                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                marginLayoutParams.setMargins(margin, 15, margin, 15);

                view.setLayoutParams(marginLayoutParams);

            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_club, parent, false);

                int screenWidth = (int) (displayMetrics.widthPixels - 2 * (displayMetrics.density * 14));
                int width = (screenWidth / 2);
                int margin = ((int) (displayMetrics.density * 4));

                if (height == 0) {
                    height = ((int) (displayMetrics.density * 260));
                }

                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(width, height);
                marginLayoutParams.setMargins(margin, margin, margin, margin);

                view.setLayoutParams(marginLayoutParams);
            }

            return new ClubProductItemViewHolder(view, mItemClickListener, mLoyaltyScore);

        } else if (viewType == TYPE_HEADER) {

            FrameLayout view = new FrameLayout(parent.getContext());

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = mHeaderViewHeight;

            view.setLayoutParams(layoutParams);

            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type");
    }

    public void setHeaderViewHeight(int headerViewHeight) {
        mHeaderViewHeight = headerViewHeight;
    }

    public void setIsHorizontal(boolean isHorizontal) {
        mIsHorizontal = isHorizontal;

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position == 0) {
            holder.onBind(new Object());
        } else {
            onBind(holder, position);
        }

    }

    private void onBind(BaseViewHolder holder, int position) {

        if (position <= getData().size()) {
            ProductInfo item = getData().get(position - 1);
            holder.onBind(item);

        } else if (position == getItemCount()) {
            ProductInfo item = (ProductInfo) new Object();
            holder.onBind(item);
        }
    }

    public class RecyclerHeaderViewHolder extends BaseViewHolder {
        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Object item) {

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
