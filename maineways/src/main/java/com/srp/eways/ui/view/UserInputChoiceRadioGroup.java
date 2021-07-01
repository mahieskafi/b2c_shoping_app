package com.srp.eways.ui.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.ui.charge.dialog.DialogContentView;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.PersianPriceFormatter;
import com.srp.eways.util.TaxUtil;
import com.srp.eways.util.Utils;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class UserInputChoiceRadioGroup extends LinearLayout {

    public interface UserInputChoiceRadioGroupListener
            extends NColumnRadioGroup.RadioGroupListener,
            UserInputChoiceView.UserInputChoiceListener {

    }

    private UserInputChoiceView mInputUserChoice;
    private AppCompatImageView mCalculateChargeIcon;

    private NColumnRadioGroup mMultiColumnRadioGroup;

    private UserInputChoiceRadioGroupListener mListener;

    public UserInputChoiceRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public UserInputChoiceRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public UserInputChoiceRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(final Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);

        IABResources abResources = DIMain.getABResources();

        int paddingLeft = abResources.getDimenPixelSize(R.dimen.userinputchoice_raiogroup_paddingLeft);
        int paddingTop = abResources.getDimenPixelSize(R.dimen.userinputchoice_raiogroup_paddingTop);
        int paddingRight = abResources.getDimenPixelSize(R.dimen.userinputchoice_raiogroup_paddingRight);
        int paddingBottom = abResources.getDimenPixelSize(R.dimen.userinputchoice_raiogroup_paddingBottom);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        ViewUtils.setCardBackground(this);
        LinearLayout horizontalLinear = new LinearLayout(context);
        horizontalLinear.setOrientation(HORIZONTAL);

        mInputUserChoice = new UserInputChoiceView(context, attrs, defStyleAttr);
        LinearLayout.LayoutParams inputLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, abResources.getDimenPixelSize(R.dimen.userinputchoice_height));
        inputLp.bottomMargin = abResources.getDimenPixelSize(R.dimen.userinputchoice_margin_bottom);
        mInputUserChoice.setLayoutParams(inputLp);


        mCalculateChargeIcon = new AppCompatImageView(context);
        mCalculateChargeIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_calculate_charge));
        mCalculateChargeIcon.setBackgroundDrawable(abResources.getDrawable(R.drawable.calculate_charge_icon_background));

        LinearLayout.LayoutParams calculateIconLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        calculateIconLP.setMargins(0, 0, abResources.getDimenPixelSize(R.dimen.userinputchoice_calculate_charge_margin_right), 0);
        mCalculateChargeIcon.setLayoutParams(calculateIconLP);

        mCalculateChargeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputElement addChoiceInputElement = new InputElement(getContext());

                IABResources abResources = DIMain.getABResources();

                final ConfirmationDialog dialog = new ConfirmationDialog(getContext());

                final DialogContentView dialogContentView = new DialogContentView(getContext());
                dialogContentView.setChoiceTitle(abResources.getString(R.string.userinputchoice_calculate_charge_dialog_title));
                dialogContentView.setIconVisibility(false);
                dialogContentView.showRial(true);
                dialogContentView.setTitleTextSize(abResources.getDimenPixelSize(R.dimen.userinputchoice_calculate_charge_title_text_size));
                dialogContentView.setTitleRialTextSize(abResources.getDimenPixelSize(R.dimen.userinputchoice_calculate_charge_title_rial_text_size));
                dialogContentView.setAmountText(abResources.getString(R.string.userinputchoice_calculate_charge_dialog_amount_title) + " ");

                addChoiceInputElement.setTextSize(abResources.getDimenPixelSize(R.dimen.add_choice_dialog_input_textsize));
                addChoiceInputElement.setTextColor(abResources.getColor(R.color.add_choice_dialog_title_textcolor));
                addChoiceInputElement.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background));
                addChoiceInputElement.setSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background));
                addChoiceInputElement.hasIcon(abResources.getBoolean(R.bool.add_choice_dialog_input_has_icon));
                addChoiceInputElement.setBackground(addChoiceInputElement.getUnselectedBackground());
                addChoiceInputElement.setImeOption(EditorInfo.IME_ACTION_DONE);
                addChoiceInputElement.setInputType(InputType.TYPE_CLASS_NUMBER);
                addChoiceInputElement.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
//                ViewCompat.setElevation(addChoiceInputElement, mElevationWidth);

                addChoiceInputElement.getEditText().setText("");

                addChoiceInputElement.addTextChangeListener(new PersianPriceFormatter(","));
                addChoiceInputElement.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (addChoiceInputElement.getText().isEmpty()) {
//                                || Long.valueOf(addChoiceInputElement.getText()) < 5000) {
                            dialog.setButtonEnable(false);
                        } else {
                            dialog.setButtonEnable(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        RadioOptionModel radioOptionModel = getOptionAt(0);
                        if (!addChoiceInputElement.getText().isEmpty() && mListener != null &&
                                radioOptionModel.option instanceof IChargeChoice) {
                            long amount = Long.parseLong(addChoiceInputElement.getText().replaceAll(",", ""));
                            double taxAmount = ((IChargeChoice) radioOptionModel.option).getTax();

                            dialogContentView.setAmount(Utils.toPersianPriceNumber(TaxUtil.getChargeAmount(amount, taxAmount)));

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
//                        if (mListener != null) {
//                            mListener.onAddUserInputChoiceClicked(Long.parseLong(dialogContentView.getAmount().replaceAll(",", "")));
//                        }
                        RadioOptionModel radioOptionModel = getOptionAt(0);
                        long amount = Long.parseLong(addChoiceInputElement.getText().replaceAll(",", ""));
                        double taxAmount = ((IChargeChoice) radioOptionModel.option).getTax();

                        mInputUserChoice.setTextToUserInputChoice(String.valueOf(TaxUtil.getChargeAmount(amount, taxAmount)));

//                        ((Activity)getContext()).getCurrentFocus().clearFocus();
                        getRootView().requestFocus();

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(addChoiceInputElement.getWindowToken(), 0);


                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClicked() {
                        dialog.dismiss();
                    }
                });

            }
        });

        horizontalLinear.addView(mCalculateChargeIcon);
        horizontalLinear.addView(mInputUserChoice);

        mMultiColumnRadioGroup = new NColumnRadioGroup(context, attrs, defStyleAttr);

        addView(horizontalLinear);
        addView(mMultiColumnRadioGroup);
    }

    public void setColumnCount(int columnCount) {
        mMultiColumnRadioGroup.setColumnCount(columnCount);
    }

    public void setOptions(List<RadioOptionModel> options) {
        mMultiColumnRadioGroup.setData(options);

        if (((IChargeChoice) getOptionAt(0).option).isIrancell()) {
            mCalculateChargeIcon.setVisibility(VISIBLE);
        } else {
            mCalculateChargeIcon.setVisibility(GONE);
        }

        requestLayout();
    }

    public RadioOptionModel getOptionAt(int index) {
        return mMultiColumnRadioGroup.getOptionAt(index);
    }

    public void setListener(UserInputChoiceRadioGroupListener listener) {
        mInputUserChoice.setListener(listener);
        mMultiColumnRadioGroup.setOnItemSelectedListener(listener);

        mListener = listener;
    }

    public void setSelectedRadioButton(int position) {
        mMultiColumnRadioGroup.setSelectedRadioButton(position);
    }

}
