package org.zelosin.Services.Samples;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.zelosin.Configurations.Form.CellStyle;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.POJOData.ScienceWork.ScienceWork;
import org.zelosin.Services.BasicServicesCompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
                mProcessingCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, tMember) -> {

                    for (String mParam : tCell.getmSectionName().split("::")) {
                        ArrayList<ScienceWork> tScienceWorkArray = tMember.mMemberInformationList.get(tCell.getmQueryType(), mParam);
                        if(tScienceWorkArray != null) {
                            tScienceWorkArray.forEach(scienceWork -> {
                                scienceWork.isVerified = true;
                                scienceWork.hasDuplicate = true;
                            });
                        }
                    }
                });
            });

            tConfig.mTableFormConfigurations.forEach(tCell ->{
                mProcessingCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, tMember) -> {
                    for (String mParam : tCell.getmSectionName().split("::")) {
                        ArrayList<ScienceWork> tScienceWorkArray = tMember.mMemberInformationList.get(tCell.getmQueryType(), mParam);
                        if(tScienceWorkArray != null) {
                            tScienceWorkArray.forEach(scienceWork -> {
                                if (scienceWork.isVerified) {
                                    if (!tCell.verifyScienceWork(scienceWork))
                                        scienceWork.isVerified = false;
                                }
                            });
                        }
                    }
                });
            });

            HashMap<String, ScienceWork> scienceWorksWithDuplicates = new HashMap<>();
            mProcessingCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, tMember) -> {
                for (QueryTypeAction action : QueryTypeAction.values()) {
                    tMember.mMemberInformationList.row(action).forEach((stringKey, worksArray) ->{
                        worksArray.forEach(scienceWork -> {
                            scienceWorksWithDuplicates.put(scienceWork.getmScienceWorkLink(), scienceWork);
                        });
                    });
                }
            });

            scienceWorksWithDuplicates.forEach(((link, scienceWork) -> {
                scienceWork.hasDuplicate = false;
            }));



            tConfig.mTableFormConfigurations.forEach(tCell ->{
                tCellRow = tCell.getmDisplayRow();
                fillExcelCell(tCell.getmDisplayRow(), tCell.getmDisplayColumn(), tCell.getmDisplayText(), tCell.getmStyleLink());

                mProcessingCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, tMember) ->{

                    for(String mParam : tCell.getmSectionName().split("::")) {
                        ArrayList<ScienceWork> tScienceWorkArray = tMember.mMemberInformationList.get(tCell.getmQueryType(), mParam);
                        if(tScienceWorkArray != null) {
                            tScienceWorkArray.forEach(tScienceWork -> {
                                if (!tScienceWork.isVerified || tScienceWork.hasDuplicate)
                                    return;
                                tCellRow++;
                                fillExcelCell(tCellRow, tCell.getmDisplayColumn(), tScienceWork.mScienceWorkInformation.get(tCell.getmVariable()), tCell.getmStyleLink());
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




















