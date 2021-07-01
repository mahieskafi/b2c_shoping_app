package com.srp.ewayspanel.ui.view.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.srp.ewayspanel.R;
import com.srp.eways.util.Utils;

/**
 * Created by ErfanG on 7/21/2019.
 */
public class Toolbar extends ViewGroup {

    private Context mContext;

    private ImageView mNavigationDrawer;
    private ImageView mShop;
    private TextView mShopCountView;
    private TextView mDeposit;
    private TextView mDepositValue;
    private View mSearchBackground;
    private ImageView mSearchIcon;

    private SearchView mSearchView;

    private int mShopCount = 0;

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        //initialize subViews for adding to our custom view
        initialViews();

        //newInstance attributes from xml file and set their values on view
        setAttrs(attrs);

        //initialize actions for every subView
        setActions();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        float density = mContext.getResources().getDisplayMetrics().density;

        //connect this view to left side
        mShop.measure(MeasureSpec.makeMeasureSpec((int)(density * 21.6), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int)(density * 21.4), MeasureSpec.EXACTLY));
        mShop.layout((int)(density * 16), (int)(density * 17), (int)(density * 37.6), (int)(density * 38.4));

        //connect this view to left side
        mShopCountView.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 16), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int)(density * 16), MeasureSpec.EXACTLY));
        mShopCountView.layout(
                (int)(density * 16),
                (int)(density * 9),
                (int)(density * 32),
                (int)(density * 25));

        //connect this view to right side
        mNavigationDrawer.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 22.4), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int)(density * 21), MeasureSpec.EXACTLY));
        mNavigationDrawer.layout(
                r - (int)(density * 38.4),
                (int)(density * 16.6),
                r - (int)(density * 16),
                (int)(density * 37.6));

        //connect this view to left of navigation
        mDeposit.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 67), MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec((int)(density * 19), MeasureSpec.EXACTLY));

        int depositMeasuredWidth = mDeposit.getMeasuredWidth();

        //connect this view to left of navigation
        mDepositValue.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 67), MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec((int)(density * 19), MeasureSpec.EXACTLY));

        int depositValueMeasuredWith = mDepositValue.getMeasuredWidth();

        int depositGuysWidth = Math.max(depositMeasuredWidth, depositValueMeasuredWith);
        int depositGuysLeft = mNavigationDrawer.getLeft() - (int)(density * 77.6); // Erfanian measurement.
        int depositGuysRight = depositGuysLeft + depositGuysWidth;

        mDepositValue.layout(
                depositGuysLeft,
                mDeposit.getBottom() + (int)(density * 1),
                depositGuysRight,
                mDeposit.getBottom() + (int)(density * 20));

        mDeposit.layout(
                depositGuysLeft,
                (int)(density * 6.6),
                depositGuysRight,
                (int)(density * 25.6));

        //connect this view to left of deposit and right of shop
        mSearchBackground.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 187), MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec((int)(density * 32), MeasureSpec.EXACTLY));
        mSearchBackground.layout(
                (int)(density * 46),
                (int)(density * 13.6),
                mDeposit.getLeft() - (int)(density * 11),
                (int)(density * 45.6));

        //connect this view to left of deposit
        mSearchIcon.measure(
                MeasureSpec.makeMeasureSpec((int)(density * 17.1), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int)(density * 17.1), MeasureSpec.EXACTLY));
        mSearchIcon.layout(
                mDeposit.getLeft() - (int)(density * 20) - mSearchIcon.getMeasuredWidth(),
                (int)(density * 20.3),
                mDeposit.getLeft() - (int)(density * 20),
                (int)(density * 20.3) + mSearchIcon.getMeasuredHeight());


    }

    private void initialViews(){

        mNavigationDrawer = new ImageView(mContext);
        mShop = new ImageView(mContext);
        mShopCountView = new TextView(mContext);
        mDeposit = new TextView(mContext);
        mDepositValue = new TextView(mContext);
        mSearchBackground = new View(mContext);
        mSearchIcon = new ImageView(mContext);
        mSearchView = new SearchView(mContext);

        addView(mNavigationDrawer);
        addView(mShop);
        addView(mShopCountView);
        addView(mDeposit);
        addView(mDepositValue);
        addView(mSearchBackground);
        addView(mSearchIcon);
        addView(mSearchView);
    }

    private void setAttrs(AttributeSet attrs) {

        TypedArray attrArray = mContext.obtainStyledAttributes(attrs, R.styleable.Toolbar, 0, 0);

        String depositText = attrArray.getString(R.styleable.Toolbar_deposit_text);
        int depositColor = attrArray.getColor(R.styleable.Toolbar_deposit_text_color, Color.WHITE);
        float depositSize = attrArray.getDimensionPixelSize(R.styleable.Toolbar_deposit_text_size, 0);

        String depositValueText = attrArray.getString(R.styleable.Toolbar_deposit_value_text);
        int depositValueColor = attrArray.getColor(R.styleable.Toolbar_deposit_value_text_color, Color.WHITE);
        float depositValueSize = attrArray.getDimensionPixelSize(R.styleable.Toolbar_deposit_value_text_size, 0);

        Drawable searchBackground = attrArray.getDrawable(R.styleable.Toolbar_search_background_color);

        Drawable searchIcon = attrArray.getDrawable(R.styleable.Toolbar_search_icon);
        Drawable shopIcon = attrArray.getDrawable(R.styleable.Toolbar_shop_icon);
        Drawable navigationIcon = attrArray.getDrawable(R.styleable.Toolbar_navigation_icon);


        mDeposit.setText(depositText);
        mDeposit.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mDeposit.setTextColor(depositColor);
        mDeposit.setTextSize(TypedValue.COMPLEX_UNIT_PX, depositSize);

        mDepositValue.setText(Utils.toPersianNumber(depositValueText));
        mDepositValue.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mDepositValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, depositValueSize);
        mDepositValue.setTextColor(depositValueColor);
        mDepositValue.setBackground(mContext.getResources().getDrawable(R.drawable.deposit_background));


        mSearchBackground.setBackground(searchBackground);

        mShop.setImageDrawable(shopIcon);
        mSearchIcon.setImageDrawable(searchIcon);
        mSearchIcon.setBackgroundColor(Color.TRANSPARENT);

        mNavigationDrawer.setImageDrawable(navigationIcon);

        mShopCountView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        mShopCountView.setTextColor(Color.WHITE);
        mShopCountView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        mShopCountView.setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        mShopCountView.setVisibility(INVISIBLE);
        mShopCountView.setBackground(mContext.getResources().getDrawable(R.drawable.shop_count_background));

        addProduct();
        addProduct();
        addProduct();
        addProduct();
        addProduct();
        addProduct();

        attrArray.recycle();
    }

    private void setActions(){

        //set actions for items like, shop icon, navigationDrawer, search item and etc.
        //TODO
    }


    //call these 2 function for update value on shop icon(red detail_transaction_card_circle_view)
    public void addProduct(){
        mShopCount++;

        String value ;
        if(mShopCount > 9){
            value = "+9";
        }
        else{
            value = String.valueOf(mShopCount);
        }

        mShopCountView.setText(Utils.toPersianNumber(value));
        mShopCountView.setVisibility(VISIBLE);
    }

    public void removeProduct(){
        mShopCount--;
        if(mShopCount < 0)
            mShopCount = 0;

        String value = "";
        if(mShopCount > 9){
            value = "+9";
        }
        else if(mShopCount > 0){
            value = String.valueOf(mShopCount);
        }

        mShopCountView.setText(Utils.toPersianNumber(value));
        if(mShopCount > 0)
            mShopCountView.setVisibility(VISIBLE);
        else
            mShopCountView.setVisibility(INVISIBLE);
    }
}
