package com.srp.eways.ui.charge.buy;

public class RadioOptionModel {

    public final String title;
    public final boolean isUserInputChoice;
    public final Object option;

    public RadioOptionModel(String title, boolean isUserInputChoice, Object option) {
        this.title = title;
        this.isUserInputChoice = isUserInputChoice;

        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RadioOptionModel that = (RadioOptionModel) o;
        return isUserInputChoice == that.isUserInputChoice &&
                title.equals(that.title);
    }

}
