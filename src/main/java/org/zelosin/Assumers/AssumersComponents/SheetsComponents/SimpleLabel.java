package org.zelosin.Assumers.AssumersComponents.SheetsComponents;

public class SimpleLabel {
    public String mDisplayText, mStyleLink;
    public int mDisplayRow, mDisplayColumn;

    public SimpleLabel(String mText, int mRow, int mColumn, String mStyleLink) {
        this.mDisplayText = mText;
        this.mDisplayRow = mRow;
        this.mDisplayColumn = mColumn;
        this.mStyleLink = mStyleLink;
}
}