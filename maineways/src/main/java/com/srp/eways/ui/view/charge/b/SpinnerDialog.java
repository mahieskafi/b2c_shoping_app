package com.srp.eways.ui.view.charge.b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.charge.model.IChargeOptionViewType;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.OneColumnRadioGroup;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.ui.charge.dialog.DialogContentView;
import com.srp.eways.ui.view.charge.b.spinner.AddableClickableSpinner;
import com.srp.eways.ui.view.charge.b.spinner.ClickableSpinner;
import com.srp.eways.ui.view.charge.b.spinner.IClickableSpinner;

import java.util.List;

public class SpinnerDialog extends FrameLayout {

    public interface RadioGroupContentViewListener
            extends ConfirmationDialog.ConfirmationDialogItemClickListener,
            NColumnRadioGroup.RadioGroupListener,
            AddableClickableSpinner.AddOptionListener {

        void onClickableSpinnerClicked();

    }

    private IClickableSpinner mSpinnerLikeOptionsView;

    private ConfirmationDialog mConfirmationDialog;
    private DialogContentView mDialogContentView;
    private OneColumnRadioGroup mOneColumnRadioGroup;

    private RadioGroupContentViewListener mListener;

    private int mViewType;

    public SpinnerDialog(Context context, int viewType) {
        super(context);

        mViewType = viewType;

        initialize(context, null, 0);
    }

    public SpinnerDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public SpinnerDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setupMultiColumnRadioGroup(context);

        mDialogContentView = new DialogContentView(context);
        mDialogContentView.setChildContentView(mOneColumnRadioGroup);

        setupConfirmationDialog(context);

        mConfirmationDialog.setChildContentView(mDialogContentView);

        if (mViewType == IChargeOptionViewType.VIEWTYPE_RADIOBUTTON_ONECOLUMN_ADDABLE) {
            mSpinnerLikeOptionsView = new AddableClickableSpinner(context);
        } else {
            mSpinnerLikeOptionsView = new ClickableSpinner(context);
        }

        mSpinnerLikeOptionsView.setClickableSpinnerViewListener(new ClickableSpinner.ChargeInputViewListener() {
            @Override
            public void onChargeInputViewClicked() {
                if (mListener != null) {
                    mListener.onClickableSpinnerClicked();
                }

                mConfirmationDialog.show();
                mConfirmationDialog.isMatchWidth(false);
            }

            @Override
            public void onAddButtonClicked(long amount) {
                if (mListener != null) {
                    mListener.onAddButtonClicked(amount);
                }
            }

            @Override
            public double getTax(long amount) {
                if (mListener != null) {
                    return mListener.getTax(amount);
                }
                return 0;
            }

            @Override
            public double getCoef() {
                if (mListener != null) {
                    return mListener.getCoef();
                }
                return 0;
            }
        });

        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((FrameLayout) mSpinnerLikeOptionsView).setLayoutParams(layoutParams);

        addView(((FrameLayout) mSpinnerLikeOptionsView));
    }

    public void setListener(RadioGroupContentViewListener listener) {
        mListener = listener;
    }

    public void setRadioOptions(List<RadioOptionModel> radioOptions) {
        mOneColumnRadioGroup.setData(radioOptions);
    }

    private void setupMultiColumnRadioGroup(Context context) {
        mOneColumnRadioGroup = new OneColumnRadioGroup(context);

        mOneColumnRadioGroup.setColumnCount(1);
        mOneColumnRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                if (mListener != null) {
                    mListener.onItemSelected(index, data);
                }
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
                if (mListener != null) {
                    mListener.onItemRemoved(index, removedData);
                }
                //Todo: call callback.removeItem(
//                            IChargeChoice userInputChoice = (IChargeChoice) removedData.option;
//                            callback.onRemoveUserInputChoiceClicked(userInputChoice.getAmount());
            }
        });
    }

    private void setupConfirmationDialog(Context context) {
        mConfirmationDialog = new ConfirmationDialog(context);
        mConfirmationDialog.setButtonEnable(false);
        mConfirmationDialog.setListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                mConfirmationDialog.dismiss();

                if (mListener != null) {
                    mListener.onConfirmClicked();
                }
            }

            @Override
            public void onCancelClicked() {
                mConfirmationDialog.dismiss();

                if (mListener != null) {
                    mListener.onCancelClicked();
                }
            }
        });
    }

    public void setRadioOptionSelected(int index) {
        mOneColumnRadioGroup.setSelectedRadioButton(index);

        mConfirmationDialog.setButtonEnable(index >= 0);
    }

    public RadioOptionModel getOptionAt(int index) {
        return mOneColumnRadioGroup.getOptionAt(index);
    }

    public void setDisplayPayAmount(String amount) {
        mDialogContentView.setAmount(amount);
    }

    public IClickableSpinner getSpinnerLikeOptionsView() {
        return mSpinnerLikeOptionsView;
    }

    public void setAmount(String amount) {
        mDialogContentView.setAmount(amount);
    }

    public void setDialogIcon(Drawable dialogTitleIcon) {
        mDialogContentView.setIcon(dialogTitleIcon);
    }

    public void setDialogTitle(String dialogTitle) {
        mDialogContentView.setChoiceTitle(dialogTitle);
    }

    public void showRial(boolean show) {
        mDialogContentView.showRial(show);
    }

}
