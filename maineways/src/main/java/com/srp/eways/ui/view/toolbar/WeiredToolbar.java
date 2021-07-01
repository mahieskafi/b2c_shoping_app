package com.srp.eways.ui.view.toolbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.base.BaseActivity;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.Utils;
import com.yashoid.sequencelayout.SequenceLayout;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 8/29/2019.
 */
public class WeiredToolbar extends SequenceLayout {

    public static int SEQUENCE_LAYOUT = 0;

    public interface SearchTextListener {
        void onSearchListener(String text);
    }

    private ImageView mButtonNavigationDrawer;
    private ImageView mButtonBack;
    private ImageView mShopIcon;
    private TextView mShopCountView;
    private InputElement mSearchBar;
    private ImageView mImageTitleIcon;
    private TextView mTextTitle;
    private TextView mTextDepositTitle;
    private TextView mTextDepositValue;
    private View mDepositHelper;
    private HorizontalDottedProgress mProgress;

    private Path mPath;
    private Paint mPaint;
    private boolean mShowBelowArc = false;

    private Point mFirstCurveStartPoint = new Point();

    private double mSideCircleXOffsetFromCenterCircle;
    private RectF mSideCircleRect = new RectF();
    private RectF mCenterCircleRect = new RectF();
    private int widthPixels;

    private SearchTextListener mSearchTextListener;

    private int mShopCount = 0;

    public WeiredToolbar(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public WeiredToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public WeiredToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public int getSequence() {
        return SEQUENCE_LAYOUT;
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);

        IABResources resources = DIMain.getABResources();

        widthPixels = getResources().getDisplayMetrics().widthPixels;

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        LayoutInflater.from(context).inflate(R.layout.weirdtoolbar, this, true);

        mButtonNavigationDrawer = findViewById(R.id.button_navigationdrawer);
        mButtonBack = findViewById(R.id.button_back);
        mShopIcon = findViewById(R.id.shop_icon);
        mShopCountView = findViewById(R.id.shop_count);
        mSearchBar = findViewById(R.id.search_bar);
        mImageTitleIcon = findViewById(R.id.image_titleicon);
        mTextTitle = findViewById(R.id.text_title);
        mTextDepositTitle = findViewById(R.id.text_deposittitle);
        mTextDepositValue = findViewById(R.id.text_depositvalue);
        mDepositHelper = findViewById(R.id.helper_deposit);
        mProgress = findViewById(R.id.shop_progress);

        addSequences(getSequence());

        String depositTitle = resources.getString(R.string.toolbar_deposittitle);
        String depositTitleRials = resources.getString(R.string.toolbar_deposittitle_rials);

        final float rialsTextSize = getResources().getDisplayMetrics().scaledDensity * 12;
        final Typeface rialsFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light);

        MetricAffectingSpan span = new MetricAffectingSpan() {

            @Override
            public void updateDrawState(TextPaint tp) {
                tp.setTextSize(rialsTextSize);
                tp.setTypeface(rialsFont);
            }

            @Override
            public void updateMeasureState(@NonNull TextPaint textPaint) {
                textPaint.setTextSize(rialsTextSize);
                textPaint.setTypeface(rialsFont);
            }

        };

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(depositTitle).append(" ").append(depositTitleRials);
        sb.setSpan(span, depositTitle.length() + 1, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextDepositTitle.setText(sb);

        mTextTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_medium));
        mTextTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.weird_toolbar_title_text_size));
        mTextTitle.setTextColor(resources.getColor(R.color.weird_toolbar_title_text_color));
        mTextTitle.setGravity(Gravity.CENTER);
        mTextTitle.setTextAlignment(TEXT_ALIGNMENT_CENTER);


        mShopCountView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mShopCountView.setTextColor(Color.WHITE);
        mShopCountView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mShopCountView.setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        mShopCountView.setVisibility(INVISIBLE);
        mShopCountView.setBackground(getResources().getDrawable(R.drawable.shop_count_background));

        removeProduct();


        mSearchBar.setHint(resources.getString(R.string.toolbar_query_hint));
        mSearchBar.clearFocus();
        mSearchBar.setFocusableInTouchMode(true);
        mSearchBar.setTextDirection(TEXT_DIRECTION_ANY_RTL);
        mSearchBar.setIconDrawable(resources.getDrawable(R.drawable.ic_search));
        mSearchBar.setHintColor(resources.getColor(R.color.toolbar_query_hint_color));
        mSearchBar.setTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan_light));
        mSearchBar.setTextSize(resources.getDimenPixelSize(R.dimen.weird_toolbar_search_size));
        mSearchBar.setUnSelectedBackground(resources.getDrawable(R.drawable.weird_toolbar_search_background));
        mSearchBar.setSelectedBackground(resources.getDrawable(R.drawable.weird_toolbar_search_background));
        mSearchBar.setTextColor(resources.getColor(R.color.weird_toolbar_title_text_color));
        mSearchBar.setBackground(mSearchBar.getSelectedBackground());
        mSearchBar.setImeOption(EditorInfo.IME_ACTION_SEARCH);
        mSearchBar.setCancelIcon(resources.getDrawable(R.drawable.remove_searchbar_content));

        mSearchBar.setCancelIconPadding(resources.getDimenPixelSize(R.dimen.search_input_text_clear_padding));
        mSearchBar.hasIcon(true);

        mSearchBar.setVisibility(VISIBLE);
        mSearchBar.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mSearchTextListener != null && !mSearchBar.getText().isEmpty()) {
                        mSearchTextListener.onSearchListener(mSearchBar.getText());
                        return true;
                    }
                    Utils.hideKeyboard(((BaseActivity) getContext()));
                    return true;
                }
                return false;
            }
        });
//        LinearLayout ll = (LinearLayout)mSearchBar.getChildAt(0);
//        LinearLayout ll2 = (LinearLayout)ll.getChildAt(2);
//        LinearLayout ll3 = (LinearLayout)ll2.getChildAt(1);
//        SearchView.SearchAutoComplete autoComplete = ((SearchView.SearchAutoComplete)ll3.getChildAt(0));

//        autoComplete.setHintTextColor(resources.getColor(R.color.toolbar_query_hint_color));
//        autoComplete.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_light));
//        autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.weird_toolbar_search_size));

        mShopIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_shop));
    }

    public void setOnBackClickListener(OnClickListener onClickListener) {
        mButtonBack.setOnClickListener(onClickListener);
    }

    public void clearSearch() {
        mSearchBar.clearFocus();
        mSearchBar.setFocusableInTouchMode(true);
        mSearchBar.clearText();
    }

    public void setSearchText(String text) {
        mSearchBar.clearFocus();
        mSearchBar.setFocusableInTouchMode(true);
        mSearchBar.setText(text);
    }

    public void setOnNavigationDrawerClickListener(OnClickListener onClickListener) {
        mButtonNavigationDrawer.setOnClickListener(onClickListener);
    }

    public void setOnShopIconClickListener(OnClickListener onClickListener) {
        mShopIcon.setOnClickListener(onClickListener);
    }

    public void setOnSearchClickListener(SearchTextListener searchClickListener) {
        mSearchTextListener = searchClickListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float density = getResources().getDisplayMetrics().density;

        int toolbarWidth = getWidth();
        int toolbarHeight = getHeight();

        int mCenterCircleRadius = (int) (((float) 78 / 600) * widthPixels);
        int mSideCircleRadius = (int) (((float) 40 / 600) * widthPixels);
        int mCenterCircleVisibleHeight = (int) (((float) 20 / 600) * widthPixels);

        double tetha2 = Math.acos((double) (mCenterCircleRadius + mSideCircleRadius - mCenterCircleVisibleHeight) / (mCenterCircleRadius + mSideCircleRadius));
        double tetha1 = (Math.PI / 2) - tetha2;

        mFirstCurveStartPoint.set((int) ((toolbarWidth / 2) - mSideCircleXOffsetFromCenterCircle), toolbarHeight);
        mSideCircleXOffsetFromCenterCircle = Math.sin(tetha2) * (mSideCircleRadius + mCenterCircleRadius);

        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, toolbarHeight);
        mPath.lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y);


        mSideCircleRect.left = (float) (toolbarWidth / 2 - mSideCircleXOffsetFromCenterCircle - mSideCircleRadius);
        mSideCircleRect.top = toolbarHeight;
        mSideCircleRect.right = mSideCircleRect.left + 2 * mSideCircleRadius;
        mSideCircleRect.bottom = mSideCircleRect.top + 2 * mSideCircleRadius;

        mPath.arcTo(mSideCircleRect, -90, getDegreeFromRadian(tetha2));

        mCenterCircleRect.left = (float) (toolbarWidth / 2 - mCenterCircleRadius);
        mCenterCircleRect.bottom = toolbarHeight + mCenterCircleVisibleHeight;
        mCenterCircleRect.top = mCenterCircleRect.bottom - 2 * mCenterCircleRadius;
        mCenterCircleRect.right = mCenterCircleRect.left + 2 * mCenterCircleRadius;

        float mCenterCircleStartAngle = 90 + getDegreeFromRadian(tetha2);
        float mCenterCircleSweepAngle = -2 * getDegreeFromRadian(tetha2);
        mPath.arcTo(mCenterCircleRect, mCenterCircleStartAngle, mCenterCircleSweepAngle);

        mSideCircleRect.offset((float) (2 * mSideCircleXOffsetFromCenterCircle), 0);
        mPath.arcTo(mSideCircleRect, 180 + getDegreeFromRadian(tetha1), getDegreeFromRadian(tetha2));

        mPath.lineTo(toolbarWidth, toolbarHeight);
        mPath.lineTo(toolbarWidth, 0);
        mPath.lineTo(0, 0);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mShowBelowArc) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void setBackgroundToolbarColor(int color) {
        mPaint.setColor(color);

        invalidate();
    }

    public void showBelowArc(boolean show) {
        mShowBelowArc = show;

        invalidate();
    }

    public void showNavigationDrawer(boolean show) {
        mButtonNavigationDrawer.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void setShowNavigationUp(boolean isShow) {
        if (isShow) {
            mButtonBack.setVisibility(VISIBLE);
        } else {
            mButtonBack.setVisibility(GONE);
        }
    }

    public void setShowNavigationDrawer(boolean isShow) {
        if (isShow) {
            mButtonNavigationDrawer.setVisibility(VISIBLE);
        } else {
            mButtonNavigationDrawer.setVisibility(INVISIBLE);
        }
    }

    public void setShowNavigationDrawerMenu(boolean show) {
        mButtonNavigationDrawer.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setSearchWord(String text){
        mSearchBar.setText(text);
    }

    public void setTitleTextColor(int color) {
        mTextTitle.setTextColor(color);
    }

    public void setTitleTextSize(float size) {
        mTextTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setDeposit(long deposit) {
        mTextDepositValue.setText(Utils.toPersianPriceNumber(String.valueOf(deposit)));
    }

    public void setDepositTextColor(int color) {
        mTextDepositTitle.setTextColor(color);
    }

    public void setDepositValueTextColor(int color) {
        mTextDepositValue.setTextColor(color);
    }

    public void setDepositValueTextSize(int size) {
        mTextDepositValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setDepositTextSize(int size) {
        mTextDepositTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTitleIcon(Drawable icon) {
        mImageTitleIcon.setImageDrawable(icon);

        mImageTitleIcon.setVisibility(icon == null ? GONE : VISIBLE);
    }

    public void setShowTitle(boolean isShow) {
        if (isShow) {
            mTextTitle.setVisibility(VISIBLE);
            mSearchBar.setVisibility(GONE);
        } else {
            mTextTitle.setVisibility(GONE);
            mSearchBar.setVisibility(VISIBLE);
        }
    }

    public void setShowShop(boolean isShow) {
        if (isShow) {
            mShopIcon.setVisibility(VISIBLE);
        } else {
            mShopIcon.setVisibility(GONE);
        }
    }

    public void setShopIconAction(OnClickListener action) {
        mShopIcon.setOnClickListener(action);
    }

    public void setDepositActionClick(OnClickListener action) {
        mTextDepositValue.setOnClickListener(action);
    }

    public void setShowDeposit(boolean visibility) {
        if (visibility) {
            mDepositHelper.setVisibility(VISIBLE);
            mTextDepositTitle.setVisibility(VISIBLE);
            mTextDepositValue.setVisibility(VISIBLE);
        } else {
            mDepositHelper.setVisibility(GONE);
            mTextDepositTitle.setVisibility(GONE);
            mTextDepositValue.setVisibility(GONE);
        }
    }

    private float getDegreeFromRadian(double angelInRadian) {
        return (float) (angelInRadian * 180 / Math.PI);
    }

    //call these 2 function for update value on shop icon(red detail_transaction_card_circle_view)
    public void addProduct() {
        mShopCount++;

        String value;
        if (mShopIcon.getVisibility() == VISIBLE) {
            if (mShopCount == 0) {
                mShopCountView.setVisibility(INVISIBLE);
            } else {
                if (mShopCount > 99) {
                    value = "+99";
                } else {
                    value = String.valueOf(mShopCount);
                }


                mShopCountView.setText(Utils.toPersianNumber(value));
                mShopCountView.setVisibility(VISIBLE);
            }
        } else {
            mShopCountView.setVisibility(INVISIBLE);
        }
    }

    public void showProgress() {
        mProgress.startAnimation();
        mProgress.setVisibility(VISIBLE);
        mShopCountView.setText("");
    }

    public void stopProgress() {
        mProgress.setVisibility(View.GONE);
        mProgress.stopAnimation();
    }

    public void setProductCount(int productCount) {
        mShopCount = productCount - 1;
        addProduct();

        stopProgress();
    }

    public void removeProduct() {
        mShopCount--;
        if (mShopCount < 0)
            mShopCount = 0;

        String value = "";
        if (mShopCount > 99) {
            value = "+99";
        } else if (mShopCount > 0) {
            value = String.valueOf(mShopCount);
        }

        mShopCountView.setText(Utils.toPersianNumber(value));
        if (mShopIcon.getVisibility() == VISIBLE) {
            if (mShopCount > 0)
                mShopCountView.setVisibility(VISIBLE);
            else
                mShopCountView.setVisibility(INVISIBLE);
        } else {
            mShopCountView.setVisibility(INVISIBLE);
        }
    }

}
