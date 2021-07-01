package com.srp.eways.util;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.srp.eways.ui.view.input.InputElement;

/**
 * Created by Eskafi on 1/12/2020.
 */
public class CalendarListener implements DatePickerDialog.OnDateSetListener {
    private InputElement mSelectedInput;
    private PersianCalendar mCalendar;

    public CalendarListener(InputElement selectedInput, PersianCalendar calendar) {
        mSelectedInput = selectedInput;
        mCalendar = calendar;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setPersianDate(year, monthOfYear, dayOfMonth);

        String mPresentingDate = Utils.getDateString(persianCalendar.getTimeInMillis());
        mSelectedInput.setText(Utils.toPersianNumber(mPresentingDate));
        mCalendar.setTimeInMillis(persianCalendar.getTimeInMillis());
    }
}
