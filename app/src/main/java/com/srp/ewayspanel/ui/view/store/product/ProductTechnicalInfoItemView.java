package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;

public class ProductTechnicalInfoItemView extends ViewGroup {

    private TextView mTextTitle;
    private TextView mTextValue;
    private View mItemBackground;

    private int mValueMarginRight;
    private int mValueNextLineMarginTop;
    private int mValueNextLineHeight;
    private int mContentMargin;

    private Rect mTitleBounds = new Rect();
    private Rect mValueBounds = new Rect();
    private Rect mItemBackgroundBounds = new Rect();

    public ProductTechnicalInfoItemView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ProductTechnicalInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ProductTechnicalInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.item_product_technicalinfo, this, true);

        mTextTitle = findViewById(R.id.title);
        mTextValue = findViewById(R.id.value);
        mItemBackground=findViewById(R.id.item_container_technical);

        IABResources abResources = DI.getABResources();

        mValueMarginRight = abResources.getDimenPixelSize(R.dimen.producttechnicalinfoitem_value_marginright);
        mValueNextLineMarginTop = abResources.getDimenPixelSize(R.dimen.producttechnicalinfoitem_value_nextline_margintop);
        mValueNextLineHeight = abResources.getDimenPixelSize(R.dimen.producttechnicalinfoitem_value_nextline_height);
        mContentMargin = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_content_margin);

        mTextTitle.setLineSpacing(0,1.2f);
        mTextValue.setLineSpacing(0,1.2f);
    }

    public void setData(ProductTechnicalItem data , boolean haveBackground) {

        String titleWithoutWhiteSpace = Utils.removeStartAndEndWhiteSpace(data.title);

        mTextTitle.setText(Utils.isRtl(titleWithoutWhiteSpace) ? titleWithoutWhiteSpace + ":" : ":" + titleWithoutWhiteSpace );
        mTextValue.setText(Utils.removeStartAndEndWhiteSpace(data.value));

        if (haveBackground){
            mItemBackground.setVisibility(VISIBLE);
        }else {
            mItemBackground.setVisibility(View.INVISIBLE);
        }

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height = getItemHeightAndComputeBounds(width);

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private int getItemHeightAndComputeBounds(int width) {
        mTextTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mTextValue.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mItemBackground.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int titleHeight = mTextTitle.getMeasuredHeight();
        int titleWidth = mTextTitle.getMeasuredWidth();

        int valueHeight = mTextValue.getMeasuredHeight();
        int valueWidth = mTextValue.getMeasuredWidth();

        int itemNormalHeight = Math.max(titleHeight, valueHeight);

        int height = itemNormalHeight + mContentMargin;

        int extraSpaceForTitleString = width/3 ;
        int extraSpaceForValueString = width - extraSpaceForTitleString - (2 * mValueMarginRight);

        int valueTop = mContentMargin/2;
        int valueRight = width - extraSpaceForTitleString - mValueMarginRight;

        mTextValue.setGravity(Gravity.RIGHT);
        mTextTitle.setGravity(Gravity.RIGHT);

        int valueLines = 0;
        int titleLines = 0;

        if (valueWidth > extraSpaceForValueString) {
            valueLines = valueWidth / extraSpaceForValueString;
            while ((extraSpaceForValueString * valueLines) < valueWidth){
                valueLines += 1;
            }
        }

        if (titleWidth > extraSpaceForTitleString) {
            titleLines = titleWidth / extraSpaceForTitleString;
            while (extraSpaceForTitleString * titleLines < titleWidth) {
                titleLines += 1;
            }
        }

        if (titleLines>=valueLines && titleLines!=0){
            height = (titleHeight * titleLines)+ (titleLines * mValueNextLineHeight);

        }else if (titleLines<valueLines && valueLines!=0){

            height =  (valueHeight * valueLines) + (valueLines * mValueNextLineHeight);
        }

        mTitleBounds.set(width - extraSpaceForTitleString, valueTop, width-mContentMargin, height);
        mValueBounds.set(mValueMarginRight, valueTop, valueRight, height);
        mItemBackgroundBounds.set(0, 0, width, height+valueTop );

        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTextTitle.measure(MeasureSpec.makeMeasureSpec(mTitleBounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mTitleBounds.height(), MeasureSpec.EXACTLY));
        mTextTitle.layout(mTitleBounds.left, mTitleBounds.top, mTitleBounds.right, mTitleBounds.bottom);

        mTextValue.measure(MeasureSpec.makeMeasureSpec(mValueBounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mValueBounds.height(), MeasureSpec.EXACTLY));
        mTextValue.layout(mValueBounds.left, mValueBounds.top, mValueBounds.right, mValueBounds.bottom);

        mItemBackground.measure(MeasureSpec.makeMeasureSpec(mItemBackgroundBounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mItemBackgroundBounds.height(), MeasureSpec.EXACTLY));
        mItemBackground.layout(mItemBackgroundBounds.left, mItemBackgroundBounds.top, mItemBackgroundBounds.right, mItemBackgroundBounds.bottom);
    }

    public static class ProductTechnicalItem {

        public final String title;
        public final String value;

        public ProductTechnicalItem(String title, String value) {
            this.title = title;
            this.value = value;
        }

    }

}
