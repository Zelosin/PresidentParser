package org.zelosin.Assumers.AssumersComponents.SheetsComponents;

import org.zelosin.Configurations.Form.FilterAction;
import org.zelosin.Configurations.Form.FilterType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SheetFilter{
    private FilterAction mFilterAction;
    private FilterType mFilterType;
    private String mComparableValue;

    private Date mFirstDate, mSecondDate;

    public static final DateFormat mClienDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public SheetFilter(FilterType mFilterType, FilterAction mFilterAction, String mComparableValue) {
        this.mFilterAction = mFilterAction;
        this.mFilterType = mFilterType;
        this.mComparableValue = mComparableValue;
    }

    public SheetFilter(FilterType mFilterType, FilterAction mFilterAction, Date mFirstDate, Date mSecondDate) {
        this.mFilterAction = mFilterAction;
        this.mFilterType = mFilterType;
        this.mFirstDate = mFirstDate;
        this.mSecondDate = mSecondDate;
    }

    public Date getmFirstDate() {
        return mFirstDate;
    }

    public Date getmSecondDate() {
        return mSecondDate;
    }

    public FilterAction getmFilterAction() {
        return mFilterAction;
    }

    public FilterType getmFilterType() {
        return mFilterType;
    }

    public String getmComparableValue() {
        return mComparableValue;
    }
}
