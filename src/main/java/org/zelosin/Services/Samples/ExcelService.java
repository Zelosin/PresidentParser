package org.zelosin.Services.Samples;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.zelosin.Configurations.Form.CellStyle;
import org.zelosin.POJOData.ScienceWork.ScienceWork;
import org.zelosin.Services.BasicServicesCompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ExcelService {
    private ByteArrayOutputStream mOutputStream = null;
    private  HSSFWorkbook mWorkBook = null;

    private  CreationHelper mCreateHelper = null;

    private  HSSFSheet mCurrentSheet = null;
    private  HSSFRow tCurrentRow = null;
    private  int tCellRow = 0;

    private String mOutputFileName;

    private BasicServicesCompiler mProcessingCompiler;

    public ExcelService(BasicServicesCompiler pCompiler) {
        this.mProcessingCompiler = pCompiler;
    }

    private void fillExcelCell(int pRow, int pColumn, String pValue, String mStyleLink){
        tCurrentRow = mCurrentSheet.getRow(pRow);
        if(tCurrentRow == null)
            tCurrentRow = mCurrentSheet.createRow(pRow);
        Cell tCurrentCell = tCurrentRow.createCell(pColumn);
        tCurrentCell.setCellValue(pValue);

        org.apache.poi.ss.usermodel.CellStyle tOriginalStyle = mWorkBook.createCellStyle();
        CellStyle tCustomStyle = CellStyle.getStyle(mStyleLink);

        tOriginalStyle.setWrapText(tCustomStyle.mIsWrappable);
        Font tCellFont = mWorkBook.createFont();
        tCellFont.setColor(HSSFColor.HSSFColorPredefined.valueOf(tCustomStyle.mFontColor).getIndex());
        tCellFont.setFontHeight((short)(tCustomStyle.mFontSize*20));
        tCellFont.setFontName(tCustomStyle.mFontFamily);
        if(tCustomStyle.mIsUnderLine)
            tCellFont.setUnderline(HSSFFont.U_SINGLE);
        tCellFont.setItalic(tCustomStyle.mIsItalic);
        tCellFont.setBold(tCustomStyle.mIsBold);
        tOriginalStyle.setFont(tCellFont);

        tCurrentCell.setCellStyle(tOriginalStyle);
    }

    private void openStreams(String pFilePath){

        mOutputStream = new ByteArrayOutputStream();

        mWorkBook = new HSSFWorkbook();
        mCreateHelper = mWorkBook.getCreationHelper();
    }

    private void flushSheet(){
        try {
            mWorkBook.write(mOutputStream);
            mWorkBook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream writeSheetToExcelFileByColumnConfiguration(String pFilePath) {
        mOutputFileName = UUID.randomUUID().toString() + ".xls";
        openStreams(mOutputFileName);
        mProcessingCompiler.mSheetConfigurationsAssumer.mSheetConfigurationsList.forEach(tConfig ->{
            mCurrentSheet = mWorkBook.createSheet(tConfig.mSheetName);
            tConfig.mLabelsList.forEach(tLabel ->{
                fillExcelCell(tLabel.mDisplayRow, tLabel.mDisplayColumn, tLabel.mDisplayText, tLabel.mStyleLink);
            });

            tConfig.mTableFormConfigurations.forEach(tCell ->{
                tCellRow = tCell.mDisplayRow;
                fillExcelCell(tCell.mDisplayRow, tCell.mDisplayColumn, tCell.mDisplayText, tCell.mStyleLink);

                mProcessingCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, tMember) ->{

                    for(String mParam : tCell.mSectionName.split("::")) {
                        ArrayList<ScienceWork> tScienceWorkArray = tMember.mMemberInformationList.get(tCell.mQueryType, mParam);
                        if(tScienceWorkArray != null) {
                            tScienceWorkArray.forEach(tScienceWork -> {
                                try {
                                    if (!tConfig.verifyScienceWork(tScienceWork))
                                        return;
                                } catch (NullPointerException e) {
                                    return;
                                }
                                tCellRow++;
                                fillExcelCell(tCellRow, tCell.mDisplayColumn, tScienceWork.mScienceWorkInformation.get(tCell.mVariable), tCell.mStyleLink);
                            });
                        }
                    }
                });
            });
        });
        flushSheet();
        try {
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mOutputStream;
    }

}




















