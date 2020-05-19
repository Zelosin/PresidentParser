package org.zelosin.Assumers.AssumersComponents.SheetsComponents;

import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.POJOData.ScienceWork.ScienceWork;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TableFormConfigurations{

    private Integer mDisplayColumn, mDisplayRow;
    private String mStyleLink, mDisplayText, mVariable, mSectionName;
    private QueryTypeAction mQueryType;
    private SheetFilter mValueFilter;

    private static final SimpleDateFormat mYearOfPublishFormatter = new SimpleDateFormat("yyyy (dd.MM.yyyy)");
    private static final SimpleDateFormat mReleseFormatter = new SimpleDateFormat("dd.MM.yyyy");

    public static synchronized Date tryToGetDateFromString(String pDateString) {
        Date tReturningDate;
        try{
            tReturningDate = mYearOfPublishFormatter.parse(pDateString);
        }
        catch (ParseException e) {
            try {
                tReturningDate = mReleseFormatter.parse(pDateString);
            } catch (ParseException ex) {
                return null;
            }
        }
        return tReturningDate;
    }

    public TableFormConfigurations(String mDisplayText, QueryTypeAction mQueryType, String mSectionName, String mVariable,
                                   Integer mDisplayColumn, Integer mDisplayRow, String mStyleLink) {
        this.mStyleLink = mStyleLink;
        this.mDisplayColumn = mDisplayColumn;
        this.mDisplayRow = mDisplayRow;
        this.mDisplayText = mDisplayText;
        this.mVariable = mVariable;
        this.mSectionName = mSectionName;
        this.mQueryType = mQueryType;
    }

    public void setmSectionName(String mSectionName) {
        this.mSectionName = mSectionName;
    }

    public boolean verifyScienceWork(ScienceWork pScienceWork) throws NullPointerException {
            if(mValueFilter == null)
                return true;
            switch (mValueFilter.getmFilterType()){
                case StringCompare: {
                    switch (mValueFilter.getmFilterAction()){
                        case NotEqual:{
                            boolean tRetuningValue = true;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                if (pScienceWork.mScienceWorkInformation.get(this.mVariable).equals(mParam))
                                    tRetuningValue = false;
                            }
                            return tRetuningValue;
                        }
                        default:
                        case Equal:{
                            boolean tRetuningValue = false;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                if (pScienceWork.mScienceWorkInformation.get(this.mVariable).equals(mParam))
                                    tRetuningValue = true;
                            }
                            return tRetuningValue;
                        }
                        case Contains:{
                            boolean tRetuningValue = false;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                if (pScienceWork.mScienceWorkInformation.get(this.mVariable).contains(mParam))
                                    tRetuningValue = true;
                            }
                            return tRetuningValue;
                        }
                        case DontContains:{
                            boolean tRetuningValue = true;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                if (!pScienceWork.mScienceWorkInformation.get(this.mVariable).contains(mParam))
                                    tRetuningValue = false;
                            }
                            return tRetuningValue;
                        }
                    }
                }
                case NoFilter:{
                    return true;
                }
                case NumberCompare: {
                    switch (mValueFilter.getmFilterAction()){
                        case NotEqual:{
                            boolean tRetuningValue = true;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                try {
                                    if (Integer.valueOf(pScienceWork.mScienceWorkInformation.get(this.mVariable)).equals(Integer.valueOf(mParam)))
                                        tRetuningValue = false;
                                }catch (NumberFormatException e){
                                    return true;
                                }
                            }
                            return tRetuningValue;
                        }
                        default:
                        case Equal:{
                            boolean tRetuningValue = false;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                try {
                                    if (!Integer.valueOf(pScienceWork.mScienceWorkInformation.get(this.mVariable)).equals(Integer.valueOf(mParam)))
                                        tRetuningValue = true;
                                }catch (NumberFormatException e){
                                    return true;
                                }
                            }
                            return tRetuningValue;
                        }
                        case LessOrEqual:
                        case Less:{
                            boolean tRetuningValue = false;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                try {
                                    if (Integer.parseInt(pScienceWork.mScienceWorkInformation.get(this.mVariable)) > (Integer.parseInt(mParam)))
                                        tRetuningValue = true;
                                }catch (NumberFormatException e){
                                    return true;
                                }
                            }
                            return tRetuningValue;
                        }
                        case BiggestOrEqual:
                        case Biggest:{
                            boolean tRetuningValue = false;
                            for(String mParam : mValueFilter.getmComparableValue().split("::")) {
                                if(pScienceWork.mScienceWorkInformation.get(this.mVariable) == null)
                                    return true;
                                try {
                                    if (Integer.parseInt(pScienceWork.mScienceWorkInformation.get(this.mVariable)) < (Integer.parseInt(mParam)))
                                        tRetuningValue = true;
                                }catch (NumberFormatException e){
                                    return true;
                                }
                            }
                            return tRetuningValue;
                        }
                    }
                }
                case DateInterval: {
                    if(this.mValueFilter.getmFirstDate() == null || this.mValueFilter.getmSecondDate() == null)
                        return true;
                    Date tComparebleDate;
                    String tComparableString = pScienceWork.mScienceWorkInformation.get(this.mVariable);
                    if(tComparableString == null)
                        return true;
                    tComparebleDate = tryToGetDateFromString(tComparableString);
                    if(tComparebleDate == null)
                        return true;

                    switch (mValueFilter.getmFilterAction()){
                        case InInterval:{
                            return tComparebleDate.after(this.mValueFilter.getmFirstDate()) && tComparebleDate.before(this.mValueFilter.getmSecondDate());
                        }
                        case OutOfInterval:{
                            return tComparebleDate.before(this.mValueFilter.getmFirstDate()) || tComparebleDate.after(this.mValueFilter.getmSecondDate());
                        }
                    }
                }
                case DateCompare: {
                    if(this.mValueFilter.getmFirstDate() == null || this.mValueFilter.getmSecondDate() == null)
                        return true;
                    Date tComparebleDate;
                    String tComparableString = pScienceWork.mScienceWorkInformation.get(this.mVariable);
                    if(tComparableString == null)
                        return true;
                    tComparebleDate = tryToGetDateFromString(tComparableString);
                    if(tComparebleDate == null)
                        return true;

                    switch (mValueFilter.getmFilterAction()){
                        case After:{
                            return tComparebleDate.after(this.mValueFilter.getmFirstDate());
                        }
                        case Before:{
                            return tComparebleDate.before(this.mValueFilter.getmFirstDate());
                        }
                        case Equal:{
                            return tComparebleDate.equals(this.mValueFilter.getmFirstDate());
                        }
                        case NotEqual:{
                            return !tComparebleDate.equals(this.mValueFilter.getmFirstDate());
                        }
                    }
                }
        }
        return true;
    }

    public TableFormConfigurations setmValueFilter(SheetFilter mValueFilter) {
        this.mValueFilter = mValueFilter;
        return this;
    }

    public Integer getmDisplayColumn() {
        return mDisplayColumn;
    }

    public Integer getmDisplayRow() {
        return mDisplayRow;
    }

    public String getmStyleLink() {
        return mStyleLink;
    }

    public String getmDisplayText() {
        return mDisplayText;
    }

    public String getmVariable() {
        return mVariable;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public QueryTypeAction getmQueryType() {
        return mQueryType;
    }

    public SheetFilter getmValueFilter() {
        return mValueFilter;
    }
}