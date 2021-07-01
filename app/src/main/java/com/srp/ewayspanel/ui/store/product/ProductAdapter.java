package com.srp.ewayspanel.ui.store.product;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.ewayspanel.ui.club.ClubProductItemViewHolder;

import org.jetbrains.annotations.NotNull;


/**
 * Created by Eskafi on 10/28/2019.
 */
public class ProductAdapter extends BaseRecyclerAdapter2<ProductInfo, BaseViewHolder> {

    public static final int TYPE_HEADER = 2;
    public static final int TYPE_CLUB = 3;

    private ProductItemClickListener mItemClickListener;
    private Context mContext;

    private int mHeaderViewHeight = 0;
    private int mItemViewHeight = 0;

    private boolean mFromMobilePage = false;
    private boolean mIsHorizontal;

    private int mParentPosition = -1;

    private long mLoyaltyScore = 0;

    public ProductAdapter(@NotNull Context context, @NotNull BaseRecyclerAdapter2.RetryClickListener retryListener,
                          ProductItemClickListener itemClickListener, boolean fromMobilePage) {
        super(context, retryListener);

        mContext = context;
        mItemClickListener = itemClickListener;

        mFromMobilePage = fromMobilePage;
        mIsHorizontal = fromMobilePage;
    }

    public ProductAdapter(@NotNull Context context, @NotNull BaseRecyclerAdapter2.RetryClickListener retryListener,
                          ProductItemClickListener itemClickListener, boolean fromMainPage, boolean isHorizontal) {
        super(context, retryListener);

        mContext = context;
        mItemClickListener = itemClickListener;

        mIsHorizontal = isHorizontal;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderViewHeight != 0) {
            return TYPE_HEADER;
        } else if (position > 0 && position <= getData().size() && getData().get(position - 1).getPoint() > 0) {
            return TYPE_CLUB;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mHeaderViewHeight != 0) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    public void setHeaderViewHeight(int headerViewHeight) {
        if (mFromMobilePage) {
            mHeaderViewHeight = 0;
        } else {
            mHeaderViewHeight = headerViewHeight;
        }

    }

    public void setItemViewHeight(int itemViewHeight) {

        mItemViewHeight = itemViewHeight;
    }

    public void setParentPosition(int position) {
        mParentPosition = position;
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder2(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return getViewHolderItem(parent, viewType);
        } else if (viewType == TYPE_CLUB) {
            return getViewHolderItem(parent, viewType);
        } else if (viewType == TYPE_HEADER) {

            FrameLayout view = new FrameLayout(parent.getContext());

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = mHeaderViewHeight;

            view.setLayoutParams(layoutParams);

            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type");
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (mHeaderViewHeight != 0 && position == 0) {
            holder.onBind(new Object());
        } else {
            onBind(holder, position);
        }

    }

    private void onBind(BaseViewHolder holder, int position) {
        if (mFromMobilePage && position < getData().size()) {
            ProductInfo item = getData().get(position);

            holder.onBind(item);
        } else if (!mFromMobilePage) {
            if (position <= getData().size()) {
                ProductInfo item;

                if (mHeaderViewHeight == 0) {
                    item = getData().get(position);
                } else {
                    item = getData().get(position - 1);
                }

                holder.onBind(item);
            } else if (position == getItemCount()) {
//                ProductInfo item = (ProductInfo) new Object();
                holder.onBind(new Object());
            }
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

    private BaseViewHolder getViewHolderItem(ViewGroup parent, int viewType) {
        View view = null;
        DisplayMetrics displayMetrics = ViewUtils.getDisplayMetrics(parent.getContext());

        int height = 0;
        if (mItemViewHeight != 0) {
            height = mItemViewHeight;
        }

        if (mIsHorizontal) {
            if (viewType == VIEW_TYPE_ITEM) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product_horizontal, parent, false);
            } else if (viewType == TYPE_CLUB) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_club_horizontal, parent, false);
            }

            if (height == 0) {
                height = ((int) (displayMetrics.density * 136));
            }
            int margin = ((int) (displayMetrics.density * 10));

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            marginLayoutParams.setMargins(margin, 15, margin, 15);
            if (view != null) {
                view.setLayoutParams(marginLayoutParams);
            }

        } else {
            if (viewType == VIEW_TYPE_ITEM) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product, parent, false);
            } else if (viewType == TYPE_CLUB) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_club, parent, false);
            }

            int screenWidth = (int) (displayMetrics.widthPixels - 2 * (displayMetrics.density * 14));
            int width = (screenWidth / 2);
            int margin = ((int) (displayMetrics.density * 4));

            if (height == 0) {
                height = ((int) (displayMetrics.density * 260));
            }

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(width, height);
            marginLayoutParams.setMargins(margin, margin, margin, margin);

            if (view != null) {
                view.setLayoutParams(marginLayoutParams);
            }
        }
        if (viewType == TYPE_CLUB) {
            return new ClubProductItemViewHolder(view, mItemClickListener, mLoyaltyScore);
        } else {
            ProductItemViewHolder productItemViewHolder = new ProductItemViewHolder(view, mItemClickListener);
            if (mParentPosition != -1) {
                productItemViewHolder.setParentPosition(mParentPosition);
            }
            return productItemViewHolder;
        }
    }

    public void setLoyaltyScore(long loyaltyScore) {
        mLoyaltyScore = loyaltyScore;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
