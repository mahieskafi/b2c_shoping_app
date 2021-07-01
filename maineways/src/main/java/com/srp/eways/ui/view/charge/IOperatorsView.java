package com.srp.eways.ui.view.charge;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.charge.a.OperatorsViewA;

public abstract class IOperatorsView extends CardView {

    public interface OperatorsViewListener {

        void onCancelOperator();

        void onOperatorLoadAnimationEnded();

        void onTransportableOperatorSelected(IOperator transportedOperator);

    }

    public IOperatorsView(@NonNull Context context) {
        super(context);
    }

    public IOperatorsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IOperatorsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setListener(OperatorsViewA.OperatorsViewListener listener);

    public abstract void setData(String phoneNumber, IOperator operator, boolean animate);

}
