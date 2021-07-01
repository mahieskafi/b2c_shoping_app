package com.srp.eways.ui.view.charge.b;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.charge.IOperatorsView;
import com.srp.eways.ui.view.charge.a.OperatorsViewA;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;

import java.util.List;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 10/1/2019.
 */
public class OperatorsViewB extends IOperatorsView {

    public interface OperatorsViewListener {

        void onTransportableOperatorSelected(IOperator transportedOperator);

    }

    private AppCompatTextView mTextOperatorName;
    private Drawable mDrawableOperator;

    private SwitchCompat mSwitchTransport;
    private AppCompatTextView mTextSwitchTransportTitle;

//    private AppCompatTextView mTextChooseTransportOptionTitle;

    private float chooseTransportOptionTitleTextSize;
    private int chooseTransportOptionTitleColor;
    private Typeface chooseTransportOptionNotCheckedTitleFont;

    private int mOperatorDrawableWidth;

    private int mTextOperatorNameMarginRight;


    private int mOperatorDrawableMarginTop;
    private int mOperatorDrawableMarginRight;


    private int mSwitchTransportHeight;
    private int mSwitchTransportWidth;

    private int mTransportSwitchMarginTop;
    private int mTransportSwitchMarginLeft;

    private int mTransportSwitchTitleMarginLeft;

    private int mTextChooseOperatorMarginTop;

    private Rect mOperatorNameBounds = new Rect();

    private Rect mTransportSwitchBounds = new Rect();
    private Rect mTransportSwitchTitleBounds = new Rect();


    private IOperator[] mTransportableOperatorDrawables = new IOperator[3];

    private IOperator mOperator;

    private IOperator mSelectedTrasportableOperator;

    private OperatorsViewA.OperatorsViewListener mListener;

    private ConfirmationDialog mDialog;

    private int width;

    public OperatorsViewB(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public OperatorsViewB(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public OperatorsViewB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);

        // prepare resources
        IABResources abResources = ABResources.get(context);

        setCardElevation(abResources.getDimen(R.dimen.phoneandoperatorsview_cardelevation));
        setRadius(abResources.getDimen(R.dimen.phoneandoperatorsview_cardecornerradius));

        mOperatorDrawableMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_phone_marginright);
        mOperatorDrawableMarginTop = abResources.getDimenPixelSize(R.dimen.operator_image_margintop_bottom_b);

        mOperatorDrawableWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_drawable_operatoricon_width);

        mTextOperatorNameMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_text_operatorname_marginleft);

        mTransportSwitchMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switch_transport_margintop_b);
        mTransportSwitchMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switch_transport_marginright);

        mTransportSwitchTitleMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switchtitle_transport_marginright);

        mTextChooseOperatorMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_title_chooseoperator_margintop);

        float operatorNameTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_operatorname_textsize);
        Typeface operatorNameFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int operatorNameTextColor = abResources.getColor(R.color.phoneandoperatorsview_operatorname_textcolor);

        float transportSwitchTitleTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_textsize);
        Typeface transportSwitchTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int transportSwitchTitleColor = abResources.getColor(R.color.phoneandoperatorsview_transportswitch_textcolor);

        chooseTransportOptionTitleTextSize = abResources.getDimenPixelSize(R.dimen.operator_dialog_title_textsize);
        final Typeface chooseTransportOptionCheckedTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        chooseTransportOptionNotCheckedTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        chooseTransportOptionTitleColor = abResources.getColor(R.color.phoneandoperatorsview_transportoption_title_textcolor);

        // create child views
        mTextOperatorName = new AppCompatTextView(context);
        mTextOperatorName.setTextSize(TypedValue.COMPLEX_UNIT_PX, operatorNameTextSize);
        mTextOperatorName.setTypeface(operatorNameFont);
        mTextOperatorName.setTextColor(operatorNameTextColor);

        mSwitchTransportHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_height);
        mSwitchTransportWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_width);

        mSwitchTransport = new SwitchCompat(context);
        mSwitchTransport.setShowText(false);
        mSwitchTransport.setChecked(false);
        mSwitchTransport.setMinWidth(0);
        mSwitchTransport.setGravity(Gravity.RIGHT);

        setSwitchStyle();

        mTextSwitchTransportTitle = new AppCompatTextView(context);
        mTextSwitchTransportTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, transportSwitchTitleTextSize);
        mTextSwitchTransportTitle.setTypeface(transportSwitchTitleFont);
        mTextSwitchTransportTitle.setTextColor(transportSwitchTitleColor);
        mTextSwitchTransportTitle.setText(abResources.getString(R.string.phoneandoperatorsview_transportswitch_title));

        mSwitchTransport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTextSwitchTransportTitle.setTypeface(isChecked ? chooseTransportOptionCheckedTitleFont : chooseTransportOptionNotCheckedTitleFont);
                showDialog(isChecked);
            }
        });

        mDialog = new ConfirmationDialog(getContext());

        // add child views
        addView(mTextOperatorName);
        addView(mSwitchTransport);
        addView(mTextSwitchTransportTitle);

    }

    private void setSwitchStyle() {
        IABResources abResources = DIMain.getABResources();

        int switchDisabledThumbColor = abResources.getColor(R.color.switch_disabled_thumb_color);
        int switchEnabledThumbColor = abResources.getColor(R.color.switch_enabled_thumb_color);

        int switchDisabledTrackColor = abResources.getColor(R.color.switch_disabled_track_color);
        int switchEnabledTrackColor = abResources.getColor(R.color.switch_enabled_track_color);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };

        int[] thumbColors = new int[]{
                switchDisabledThumbColor,
                switchEnabledThumbColor,
        };

        int[] trackColors = new int[]{
                switchDisabledTrackColor,
                switchEnabledTrackColor,
        };

        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchTransport.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchTransport.getTrackDrawable()), new ColorStateList(states, trackColors));
    }

    @Override
    public void setListener(OperatorsViewA.OperatorsViewListener listener) {
        mListener = listener;
    }

    @Override
    public void setData(String phoneNumber, IOperator operator, boolean animate) {
        mOperator = operator;
        updateOperatorRelatedIcons();

        mTextOperatorName.setText(operator.getName());

        mSwitchTransport.setChecked(false);
        requestLayout();
    }

    private void updateOperatorRelatedIcons() {
        IABResources abResources = ABResources.get(getContext());

        mDrawableOperator = abResources.getDrawable(mOperator.getIconResId());

        List<IOperator> transportableOperators = mOperator.getTransportableOperators();

        for (int i = 0; i < transportableOperators.size(); ++i) {
            IOperator transportableOperator = transportableOperators.get(i);

            if (transportableOperator == null) {
                mTransportableOperatorDrawables[i] = null;
            } else {
                mTransportableOperatorDrawables[i] = transportableOperator;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);

        mDrawableOperator.setBounds(width - mOperatorDrawableMarginRight - mOperatorDrawableWidth, mOperatorDrawableMarginTop, width - mOperatorDrawableMarginRight, mOperatorDrawableMarginTop + mOperatorDrawableWidth);

        mTextOperatorName.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mOperatorNameBounds.right = mDrawableOperator.getBounds().left - mTextOperatorNameMarginRight;
        mOperatorNameBounds.top = mDrawableOperator.getBounds().centerY() - (mTextOperatorName.getMeasuredHeight() / 2);
        mOperatorNameBounds.left = mOperatorNameBounds.right - mTextOperatorName.getMeasuredWidth();
        mOperatorNameBounds.bottom = mDrawableOperator.getBounds().centerY() + (mTextOperatorName.getMeasuredHeight() / 2);

        mSwitchTransport.measure(MeasureSpec.makeMeasureSpec(mSwitchTransportWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mSwitchTransportHeight, MeasureSpec.EXACTLY));

        mTransportSwitchBounds.right = mTransportSwitchMarginLeft + mSwitchTransport.getMeasuredWidth();
        mTransportSwitchBounds.left = mTransportSwitchBounds.right - mSwitchTransport.getMeasuredWidth();
        mTransportSwitchBounds.top = mTransportSwitchMarginTop;
        mTransportSwitchBounds.bottom = mTransportSwitchBounds.top + mSwitchTransport.getMeasuredHeight();

        mTextSwitchTransportTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mTransportSwitchTitleBounds.right = mTransportSwitchBounds.right + mTextSwitchTransportTitle.getMeasuredWidth() + mTransportSwitchTitleMarginLeft;
        mTransportSwitchTitleBounds.left = mTransportSwitchBounds.right;
        mTransportSwitchTitleBounds.top = mTransportSwitchBounds.centerY() - (mTextSwitchTransportTitle.getMeasuredHeight() / 2);
        mTransportSwitchTitleBounds.bottom = mTransportSwitchBounds.centerY() + (mTextSwitchTransportTitle.getMeasuredHeight() / 2);

        int height = mOperatorDrawableMarginTop
                + mOperatorDrawableWidth
                + mOperatorDrawableMarginTop;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void showDialog(boolean isSwitchChecked) {
        if (!isSwitchChecked) {
            mDialog.dismiss();
        } else {
            View contentView = createChooseOperatorView();

            if (contentView != null) {
                if (mSelectedTrasportableOperator != null) {
                    mDialog.setButtonEnable(true);
                } else {
                    mDialog.setChildContentView(contentView);
                    mDialog.show();
                    mDialog.setButtonEnable(false);
                    mDialog.isMatchWidth(false);

                }

                mDialog.setListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
                    @Override
                    public void onConfirmClicked() {
                        mDialog.dismiss();
                        if (mSelectedTrasportableOperator != null) {
                            mListener.onTransportableOperatorSelected(mSelectedTrasportableOperator);

                            mOperator = mSelectedTrasportableOperator;
                            mSelectedTrasportableOperator = null;
                        } else {
                            mSwitchTransport.setChecked(false);
                        }

                    }

                    @Override
                    public void onCancelClicked() {
                        mSelectedTrasportableOperator = null;
                        mSwitchTransport.setChecked(false);
                    }
                });

                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mDialog.dismiss();
                        mSelectedTrasportableOperator = null;
                        mSwitchTransport.setChecked(false);
                    }
                });

            }
        }
    }

    private View createChooseOperatorView() {
        int mViewChooseOperatorWidth = (int) (width / 1.6);

        IABResources abResources = DIMain.getABResources();

        LinearLayout mViewChooseOperator = new LinearLayout(getContext());
        mViewChooseOperator.setGravity(Gravity.RIGHT);
        mViewChooseOperator.setOrientation(LinearLayout.VERTICAL);
        mViewChooseOperator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout mViewOptionsOperator = new LinearLayout(getContext());
        mViewOptionsOperator.setGravity(Gravity.RIGHT);
        mViewOptionsOperator.setOrientation(LinearLayout.HORIZONTAL);
        mViewOptionsOperator.setWeightSum(3);

        AppCompatTextView mTextChooseTransportOptionTitle = new AppCompatTextView(getContext());
        mTextChooseTransportOptionTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, chooseTransportOptionTitleTextSize);
        mTextChooseTransportOptionTitle.setTypeface(chooseTransportOptionNotCheckedTitleFont);
        mTextChooseTransportOptionTitle.setTextColor(chooseTransportOptionTitleColor);
        mTextChooseTransportOptionTitle.setText(abResources.getString(R.string.phoneandoperatorsview_transportoptions_title_b));

        mTextChooseTransportOptionTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LayoutParams) layoutParams).topMargin = mTextChooseOperatorMarginTop;
        mViewOptionsOperator.setLayoutParams(layoutParams);


        for (int i = 0; i < mTransportableOperatorDrawables.length; ++i) {
            IOperator optionOperator = mTransportableOperatorDrawables[i];
            if (optionOperator != null) {
                mViewOptionsOperator.addView(getItemOperatorView(optionOperator));
            }
        }
        mViewChooseOperator.removeAllViews();
        mViewChooseOperator.addView(mTextChooseTransportOptionTitle);
        mViewChooseOperator.addView(mViewOptionsOperator);

        return mViewChooseOperator;
    }

    private View getItemOperatorView(final IOperator optionOperator) {
        final IABResources abResources = DIMain.getABResources();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewOperator = inflater.inflate(R.layout.item_transportable_operator_b, null);

        AppCompatImageView operatorIcon = viewOperator.findViewById(R.id.iv_icon);
        AppCompatTextView operatorName = viewOperator.findViewById(R.id.tv_name);
        AppCompatImageView operatorIconEnabledBackground = viewOperator.findViewById(R.id.iv_icon_enable_background);

        operatorIcon.setImageDrawable(abResources.getDrawable(optionOperator.getNoNameIconResId()));
        operatorIconEnabledBackground.setImageDrawable(abResources.getDrawable(R.drawable.item_transportation_operator_b_enable_background));

        operatorName.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.operator_dialog_items_name_textsize));

        FrameLayout.LayoutParams backgroundLP = new FrameLayout.LayoutParams(abResources.getDimenPixelSize(R.dimen.operator_dialog_items_operator_icon_background_width),
                abResources.getDimenPixelSize(R.dimen.operator_dialog_items_operator_icon_background_width));
        operatorIcon.setLayoutParams(backgroundLP);
        operatorIconEnabledBackground.setLayoutParams(backgroundLP);

        if (mSelectedTrasportableOperator != null && mSelectedTrasportableOperator.getName().equals(optionOperator.getName())) {
            operatorIconEnabledBackground.setVisibility(VISIBLE);

            operatorName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold));
            operatorName.setText(optionOperator.getName());
        } else {
            operatorIconEnabledBackground.setVisibility(GONE);

            operatorName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
            operatorName.setText(optionOperator.getName());
        }

        LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLP.weight = 1;
        viewOperator.setLayoutParams(itemLP);

        viewOperator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedTrasportableOperator = optionOperator;
                mDialog.setButtonEnable(true);
                mDialog.setChildContentView(createChooseOperatorView());
            }
        });

        return viewOperator;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(mTextOperatorName, mOperatorNameBounds);
        layoutChild(mSwitchTransport, mTransportSwitchBounds);
        layoutChild(mTextSwitchTransportTitle, mTransportSwitchTitleBounds);
    }

    private void layoutChild(View child, Rect bounds) {
        child.measure(MeasureSpec.makeMeasureSpec(bounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(bounds.height(), MeasureSpec.EXACTLY));
        child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawableOperator.draw(canvas);

    }
}

