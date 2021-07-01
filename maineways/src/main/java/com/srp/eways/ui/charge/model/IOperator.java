package com.srp.eways.ui.charge.model;

import java.util.List;

public interface IOperator extends IChargeOptionViewType {

    String getName();

    boolean isTransportable();

    List<IOperator> getTransportableOperators();

    List<IChargeOption> getChargeOptions();

    int getIconResId();

    int getNoNameIconResId();

    int getTransportableIconResId();

    int getOperatorColor();

    int getOperatorServiceColor();

    int getOperatorName();

    int getIrancellSpecialOfferProductId();

    int getOperatorKey();

}
