package org.zelosin.Configurations.Form;

import java.util.HashMap;
import java.util.Map;

public class CellStyle {

    public String mStyleName, mFontFamily, mFontColor;
    public Integer mColumnWidth, mFontSize;
    public Boolean mIsWrappable, mIsItalic, mIsBold, mIsUnderLine;

    public static Map<String, CellStyle> mCellStyleList = new HashMap<>();

    public CellStyle(String mStyleName, String mFontFamily, String mFontColor, Integer mColumnWidth, Integer mFontSize, Boolean mIsWrappable, Boolean mIsItalic, Boolean mIsBold, Boolean mIsUnderLine) {
        this.mStyleName = mStyleName;
        this.mFontFamily = mFontFamily;
        this.mFontColor = mFontColor;
        this.mColumnWidth = mColumnWidth;
        this.mFontSize = mFontSize;
        this.mIsWrappable = mIsWrappable;
        this.mIsItalic = mIsItalic;
        this.mIsBold = mIsBold;
        this.mIsUnderLine = mIsUnderLine;
        mCellStyleList.put(mStyleName, this);
    }

    public static CellStyle getStyle(String pStyleName){
        CellStyle tReturningValue = mCellStyleList.get(pStyleName);
        if(tReturningValue == null)
            tReturningValue = mCellStyleList.get("Basic");
        return tReturningValue;
    }
}
