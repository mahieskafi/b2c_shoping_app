package com.srp.eways.ui.view.charge.b.spinner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.ui.charge.dialog.DialogContentView;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.PersianPriceFormatter;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public class AddableClickableSpinner extends FrameLayout implements IClickableSpinner {

    public interface AddOptionListener {

        void onAddButtonClicked(long amount);

        double getTax(long amount);

        double getCoef();

    }

    private ClickableSpinner mChoiceInputView;

    private InputElement addChoiceInputElement;

    private int mElevationWidth;

    private ClickableSpinner.ChargeInputViewListener mChargeInputViewListener;

    public AddableClickableSpinner(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public AddableClickableSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public AddableClickableSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setClipChildren(false);
        setClipToPadding(false);

        final IABResources abResources = DIMain.getABResources();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.charge_add_choice_container, this, true);

        mChoiceInputView = findViewById(R.id.input_choice);
        AppCompatImageView imageAddChoice = findViewById(R.id.image_add_choice);

        mElevationWidth = abResources.getDimenPixelSize(R.dimen.buycharge_input_phonenumber_elevation);

        GradientDrawable imageAddChoiceBackground = new GradientDrawable();
        imageAddChoiceBackground.setColor(abResources.getColor(R.color.buycharge_selectcontact_background));
        imageAddChoiceBackground.setCornerRadius(abResources.getDimen(R.dimen.charge_input_view_cornerradius));

        imageAddChoice.setBackground(imageAddChoiceBackground);

        int imageAddChoiceSize = abResources.getDimenPixelSize(R.dimen.charge_add_choice_image_size);
        int imageAddChoiceMarginRight = abResources.getDimenPixelSize(R.dimen.buycharge_selectcontact_image_margin_right);

        LinearLayout.LayoutParams imageSelectContactLP = (LinearLayout.LayoutParams) imageAddChoice.getLayoutParams();
        imageSelectContactLP.height = imageAddChoiceSize;
        imageSelectContactLP.width = imageAddChoiceSize;
        imageSelectContactLP.rightMargin = imageAddChoiceMarginRight;

        imageAddChoice.setLayoutParams(imageSelectContactLP);

        imageAddChoice.setImageDrawable(abResources.getDrawable(R.drawable.ic_add_choice_b));
        ViewCompat.setElevation(imageAddChoice, mElevationWidth);

        LinearLayout inputView = new LinearLayout(context);
        inputView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, abResources.getDimenPixelSize(R.dimen.charge_add_choice_image_size)));

        mChoiceInputView.setIcon(abResources.getDrawable(R.drawable.ic_charge_choice_b));
        mChoiceInputView.setHint(abResources.getString(R.string.charge_choice_input_hint));

        mChoiceInputView.getInputText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mChargeInputViewListener != null) {
                    //Todo: parseLong
//                    mListener.onAddButtonClicked();
                }
            }
        });

        imageAddChoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                addChoiceInputElement = new InputElement(getContext());

                final ConfirmationDialog dialog = new ConfirmationDialog(getContext());

                final DialogContentView dialogContentView = new DialogContentView(getContext());

                addChoiceInputElement.setTextSize(abResources.getDimenPixelSize(R.dimen.add_choice_dialog_input_textsize));
                addChoiceInputElement.setTextColor(abResources.getColor(R.color.add_choice_dialog_title_textcolor));
                addChoiceInputElement.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background));
                addChoiceInputElement.setSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background));
                addChoiceInputElement.hasIcon(abResources.getBoolean(R.bool.add_choice_dialog_input_has_icon));
                addChoiceInputElement.setBackground(addChoiceInputElement.getUnselectedBackground());
                addChoiceInputElement.setImeOption(EditorInfo.IME_ACTION_DONE);
                addChoiceInputElement.setInputType(InputType.TYPE_CLASS_NUMBER);
                addChoiceInputElement.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//                ViewCompat.setElevation(addChoiceInputElement, mElevationWidth);

                addChoiceInputElement.getEditText().setText("");

                addChoiceInputElement.addTextChangeListener(new PersianPriceFormatter(","));
                addChoiceInputElement.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (addChoiceInputElement.getText().isEmpty()){
//                                || Long.valueOf(addChoiceInputElement.getText()) < 5000) {
                            dialog.setButtonEnable(false);
                        } else {
                            dialog.setButtonEnable(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!addChoiceInputElement.getText().isEmpty() && mChargeInputViewListener != null) {
                            long amount = Long.parseLong(addChoiceInputElement.getText().replaceAll(",", ""));
                            double taxAmount = mChargeInputViewListener.getTax(amount);
                            double taxCoef = mChargeInputViewListener.getCoef();

                            int chargeAmount = (int) (Math.ceil(amount * (taxAmount * .1 + 1)) * taxCoef);
//
                            dialogContentView.setAmount(Utils.toPersianPriceNumber(chargeAmount));
                        } else if (addChoiceInputElement.getText().isEmpty()) {
                            dialogContentView.setAmount(0 + "");
                        }
                    }
                });

                addChoiceInputElement.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, abResources.getDimenPixelSize(R.dimen.charge_inquiry_button_input_height)));
                dialogContentView.setChildContentView(addChoiceInputElement);
                dialogContentView.setAmount(Utils.toPersianNumber(0));

                dialog.setChildContentView(dialogContentView);
                dialog.setButtonEnable(false);
                dialog.show();
                dialog.isMatchWidth(false);

                dialog.setListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
                    @Override
                    public void onConfirmClicked() {
                        if (mChargeInputViewListener != null) {
                            mChargeInputViewListener.onAddButtonClicked(Long.parseLong(addChoiceInputElement.getText().replaceAll(",", "")));
                        }
                        dialog.dismiss();

                    }

                    @Override
                    public void onCancelClicked() {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public void setClickableSpinnerViewListener(ClickableSpinner.ClickableSpinnerViewListener listener) {
        mChargeInputViewListener = (ClickableSpinner.ChargeInputViewListener) listener;
        mChoiceInputView.setClickableSpinnerViewListener(listener);
    }

//    public void setAddOptionListener(AddOptionListener listener) {
//        mListener = listener;
//    }

    @Override
    public void setHint(String hint) {
        mChoiceInputView.setHint(hint);
    }

    @Override
    public void setText(String text) {
        mChoiceInputView.setText(text);
    }

    @Override
    public void setIcon(Drawable drawable) {
        mChoiceInputView.setIcon(drawable);
    }

}
