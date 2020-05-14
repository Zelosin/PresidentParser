package org.zelosin.Assumers.AssumersComponents;

import org.zelosin.Assumers.AssumersComponents.SheetsComponents.SheetFilter;
import org.zelosin.Assumers.AssumersComponents.SheetsComponents.SimpleLabel;
import org.zelosin.Assumers.AssumersComponents.SheetsComponents.TableFormConfigurations;
import org.zelosin.Assumers.SheetConfigurationsAssumer;
import org.zelosin.Configurations.Form.FilterAction;
import org.zelosin.Configurations.Form.FilterType;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.POJOData.ScienceWork.ScienceWork;

import java.text.ParseException;
import java.util.ArrayList;

public class SheetConfiguration {

    public String mSheetName;
    public Boolean mIsTextWrappable;

    public ArrayList<TableFormConfigurations> mTableFormConfigurations = new ArrayList<>();
    public ArrayList<SimpleLabel> mLabelsList = new ArrayList<>();

    public SheetConfiguration(String mSheetName, Boolean mIsTextWrappable) {
        this.mSheetName = mSheetName;
        this.mIsTextWrappable = mIsTextWrappable;
    }

    public void addTableConfig(TableFormConfigurations config){
        mTableFormConfigurations.add(config);
    }

    public void addTableConfig(TableFormConfigurations config, SheetFilter filter){
        mTableFormConfigurations.add(config.setmValueFilter(filter));
    }

    public void addLabel(SimpleLabel label){
        mLabelsList.add(label);
    }

   /* public boolean verifyScienceWork(ScienceWork pScienceWork) throws NullPointerException {
        boolean tRetuningValue = true;
        for (SheetConfiguration.SheetFilter tFilter : mSheetFiltersList) {
            switch (tFilter.mFilterType){
                case StringCompare: {
                    switch (tFilter.mFilterAction){
                        case NotEqual:{
                            for(String mParam : tFilter.mComparableValue.split("::")) {
                                //if (pScienceWork.mScienceWorkInformation.get(tFilter.mVariable).equals(mParam))
                                    tRetuningValue = false;
                            }
                            return tRetuningValue;
                        }
                        default:
                        case Equal:{
                            for(String mParam : tFilter.mComparableValue.split("::")) {
                                //if (pScienceWork.mScienceWorkInformation.get(tFilter.mVariable).equals(mParam))
                                    return true;
                            }
                        }
                    }
                }
                case DateCompare: {
                    switch (tFilter.mFilterAction){
                        case BiggestThan:{
                            try {
                                if(!(SheetConfigurationsAssumer.DATE_FORMAT.parse(pScienceWork.mScienceWorkInformation.get(tFilter.mVariable)).after(
                                        SheetConfigurationsAssumer.DATE_FORMAT.parse(tFilter.mComparableValue)
                                )))
                                    return false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case LessThan:{
                            try {
                                if(SheetConfigurationsAssumer.DATE_FORMAT.parse(pScienceWork.mScienceWorkInformation.get(tFilter.mVariable))
                                        .after(
                                                SheetConfigurationsAssumer.DATE_FORMAT.parse(tFilter.mComparableValue)
                                        ))
                                    return false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        default:
                            return false;
                    }
                }
            }
        }
        return true;
    }*/
}
