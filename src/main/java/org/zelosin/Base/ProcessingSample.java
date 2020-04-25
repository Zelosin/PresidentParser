package org.zelosin.Base;
import org.zelosin.Services.BasicServicesCompiler;
import org.zelosin.Services.Samples.ExcelService;
import org.zelosin.Services.Samples.PSUService;
import org.zelosin.Services.Samples.XMLService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ProcessingSample {

    private XMLService mXMLService;
    private PSUService mPSUService;
    private ExcelService mExcelService;
    public BasicServicesCompiler mBasicServicesCompiler;
    private String mOutputFileName;

    public void loadConfigFile(String pConfigName){
        mXMLService = new XMLService(pConfigName);
        mBasicServicesCompiler = mXMLService.basicConfigurationReadCycle();
    }

    public void loadConfigFile(ByteArrayInputStream pByteArray){
        mXMLService = new XMLService(pByteArray);
        mBasicServicesCompiler = mXMLService.basicConfigurationReadCycle();
    }

    public ByteArrayOutputStream parseCycle(){
        mExcelService = new ExcelService(mBasicServicesCompiler);
        return mExcelService.writeSheetToExcelFileByColumnConfiguration(mBasicServicesCompiler.mQueryConfigurationsAssumer.mExportPath);
    }

    public void queryCycle(){
        mPSUService = new PSUService(mBasicServicesCompiler);
        mPSUService.assignDepartmentMemberList(mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryType);
        mPSUService.parseScienceWorkPageByQueryType(mBasicServicesCompiler.mQueryConfigurationsAssumer.mParseType);
    }

    public ByteArrayOutputStream basicProcessingCycle(){
        queryCycle();
        return parseCycle();
    }

    public ProcessingSample() {
        //loadConfigFile( null );
    }

    public String getmOutputFileName() {
        return mOutputFileName;
    }
}
