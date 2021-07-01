package com.srp.eways.ui.view.receipt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.util.Utils;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ReceiptView extends ViewGroup {

    public interface ButtonClickListener {
        void onButtonClicked();
    }

    private Path mPath;
    private Paint mPaint;
    private Paint mCirclePaint;
    private ImageView mIcon;
    private ReceiptListView mReceiptListView;
    private TextView mTitle;
    private LinearLayout mLinear;
    private ButtonElement mButton;

    private int mTitleColor;
    private float mTitleSize;
    private float mDepositTextSize;
    private int mDepositTitleColor;
    private int mDepositValueColor;

    private int mWidth;
    private int mHeight;
    private int mMarginTop;
    private int mStandardMargin;
    private float mDensity;
    private int mButtonHeight;

    private ButtonClickListener mListener;

    private IABResources abResources;

    public ReceiptView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ReceiptView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ReceiptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        abResources = DIMain.getABResources();

        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMarginTop = ((int) (mDensity * 50));
        mStandardMargin = ((int) (mDensity * 16));

        mButtonHeight = abResources.getDimenPixelSize(R.dimen.receipt_home_button_height);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.receipt_paint_color));
        mPaint.setShadowLayer(((int) (mDensity * 2)), 0, 0, getResources().getColor(R.color.receipt_shadow_color));
        setBackgroundColor(getResources().getColor(R.color.receipt_background_color));

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(getResources().getColor(R.color.receipt_circle_paint_color));

        mLinear = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLinear.setLayoutParams(params);
        mLinear.setGravity(Gravity.CENTER_HORIZONTAL);
        mLinear.setOrientation(LinearLayout.VERTICAL);

        mIcon = new ImageView(context);
        mReceiptListView = new ReceiptListView(context);
        mTitle = new TextView(context);
        mButton = new ButtonElement(context);

        addView(mLinear);
    }

    public void setTitleColor(int color) {
        mTitleColor = color;
    }

    public void setTitleSize(float size) {
        mTitleSize = size;
    }

    public void setDepositTextSize(float size) {
        mDepositTextSize = size;
    }

    public void setDepositTitleColor(int color) {
        mDepositTitleColor = color;
    }

    public void setDepositValueColor(int color) {
        mDepositValueColor = color;
    }

    public void setTitle(String text) {
        LinearLayout.LayoutParams paramsIcon = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        mTitle.setLayoutParams(paramsIcon);
        mTitle.setTextColor(mTitleColor);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        mTitle.setText(text);
        mTitle.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        mLinear.addView(mTitle);

        requestLayout();
    }

    public void setDeposit(ReceiptItem receiptItem) {
        ViewGroup itemView = getDepositView();
        TextView textViewTitle = itemView.findViewById(R.id.txt_title);
        ImageView imageViewIcon = itemView.findViewById(R.id.iv_icon);
        TextView textViewValue = itemView.findViewById(R.id.txt_value);

        if (mDepositTextSize != 0) {
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDepositTextSize);
            textViewTitle.setTypeface(textViewTitle.getTypeface(), Typeface.BOLD);
            textViewValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDepositTextSize);
        }
        if (mDepositValueColor != 0)
            textViewValue.setTextColor(mDepositValueColor);
        if (mDepositTitleColor != 0)
            textViewTitle.setTextColor(mDepositTitleColor);

        textViewTitle.setText(receiptItem.getTitle());
        textViewValue.setText(Utils.toPersianNumber(receiptItem.getValue()));
        if (receiptItem.getIcon() != null) {
            imageViewIcon.setVisibility(VISIBLE);
            imageViewIcon.setImageDrawable(receiptItem.getIcon());
        }
        mLinear.addView(itemView);

        View view = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(mDensity * 3));
        params.setMargins(mStandardMargin, 0, mStandardMargin, 0);
        view.setLayoutParams(params);
        view.setBackgroundColor(getResources().getColor(R.color.receipt_deposit_divider_background));
        mLinear.addView(view);

        requestLayout();
    }

    public void setReceiptItem(List<ReceiptItem> receiptItem) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(mStandardMargin, 0, mStandardMargin, mStandardMargin);
        mReceiptListView.setLayoutParams(params);
        mReceiptListView.setReceiptItem(receiptItem);
        mLinear.addView(mReceiptListView);
    }

    public void setBackColor(int color) {
        mPaint.setColor(color);
        mCirclePaint.setColor(color);
    }

    public void setIcon(Drawable icon) {
        LinearLayout.LayoutParams paramsIcon = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mIcon.setLayoutParams(paramsIcon);
        mIcon.setImageDrawable(icon);
        addView(mIcon);
    }

    public void setButtonText(String text) {
        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mButtonHeight);
        paramsButton.setMargins(mStandardMargin * 2, 0, mStandardMargin * 2, mStandardMargin);
        mButton.setLayoutParams(paramsButton);

        mButton.setTextSize(abResources.getDimenPixelSize(R.dimen.increase_deposit_button_payment_text_size));
        mButton.setTextColor(abResources.getColor(R.color.increase_deposit_button_text_color));
        mButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mButton.setEnable(true);
        mButton.setLoadingVisibility(View.GONE);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonClicked();
                }
            }
        });

        mButton.setText(text);
        mLinear.addView(mButton);

        requestLayout();
    }

    public void setButtonClickListener(ButtonClickListener listener) {
        mListener = listener;
    }

    private ViewGroup getDepositView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.item_receipt, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(mStandardMargin, mStandardMargin, mStandardMargin, mStandardMargin);
        itemView.setLayoutParams(params);

        return itemView;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int right, int bottom) {

        int width = getWidth();
        int height = getHeight();

        mIcon.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int top = mMarginTop - mIcon.getMeasuredWidth() / 2;
        mIcon.layout(
                getWidth() / 2 - mIcon.getMeasuredWidth() / 2,
                top,
                getWidth() / 2 + mIcon.getMeasuredWidth() / 2,
                top + mIcon.getMeasuredHeight());

        mLinear.measure(
                MeasureSpec.makeMeasureSpec(width - mStandardMargin, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mLinear.layout(
                mStandardMargin,
                mMarginTop + mStandardMargin + ((int) (mDensity * 8)),
                width - mStandardMargin,
                mMarginTop + mStandardMargin + mLinear.getMeasuredHeight() + ((int) (mDensity * 8)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = mLinear.getChildCount();
        mHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = mLinear.getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            final LayoutParams lp = child.getLayoutParams();
            mHeight += Math.max(child.getMeasuredHeight(), lp.height) + mStandardMargin;
        }

        mLinear.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        setMeasuredDimension(mLinear.getMeasuredWidth(), MeasureSpec.makeMeasureSpec(mHeight + mMarginTop + mStandardMargin, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        int rx = ((int) (mDensity * 12));
        int ry = ((int) (mDensity * 12));
        int top = mMarginTop;
        int height = mHeight;

        int circlRedius = mIcon.getMeasuredWidth() / 2 + mIcon.getMeasuredWidth() / 4;

        RectF oval = new RectF();
        oval.set(mWidth / 2 - circlRedius, top - circlRedius, mWidth / 2 + circlRedius, top + (circlRedius));

        mPath.reset();
        mPath.moveTo(mWidth - mStandardMargin, top + ry);
        mPath.rQuadTo(0, -ry, -rx, -ry);//right top corner
        mPath.rLineTo(-(mWidth - (2 * rx) - (mStandardMargin * 2)), 0);
        mPath.rQuadTo(-rx, 0, -rx, ry);
        mPath.rLineTo(0, (height - (2 * ry)));
        mPath.rQuadTo(0, ry, rx, ry);
        mPath.rLineTo((mWidth - (2 * rx)) - (mStandardMargin * 2), 0);
        mPath.rQuadTo(rx, 0, rx, -ry);
        mPath.rLineTo(0, -(height - (2 * ry) + mStandardMargin));
        mPath.close();

        Paint shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(getResources().getColor(R.color.receipt_shadow_paint_color));
        shadowPaint.setShadowLayer(((int) (mDensity * 2)), 0, 0, getResources().getColor(R.color.receipt_shadow_color));

        canvas.drawPath(mPath, mPaint);

//        RectF ovalShadow = new RectF();
//        ovalShadow.set(mWidth / 2 - (circlRedius + ((int) (mDensity * 4))), top - (circlRedius + ((int) (mDensity * 4))), mWidth / 2 + (circlRedius + ((int) (mDensity * 4))), top + ((circlRedius) + ((int) (mDensity * 4))));
        canvas.drawArc(oval, -180, 180, true, shadowPaint);
        canvas.drawCircle(oval.centerX(), oval.centerY(), circlRedius, mCirclePaint);
    }
}
