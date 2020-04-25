package org.zelosin.Services.Samples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.zelosin.Assumers.AssumersComponents.SheetConfiguration;
import org.zelosin.Configurations.Form.CellStyle;
import org.zelosin.Configurations.Form.FilterAction;
import org.zelosin.Configurations.Form.FilterType;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.Services.BasicServicesCompiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XMLService{

    private InputStream mQueryFileStream;
    private Document mQueryFileDocument;

    private BasicServicesCompiler mReturningCompiler;
    private String pConfigName;

    public BasicServicesCompiler basicConfigurationReadCycle(){
        mReturningCompiler = new BasicServicesCompiler();
        try {
            openAllFileStreams();
            getFileDocument(mQueryFileStream);
            mQueryFileStream.close();
            parseQueryFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mReturningCompiler;
    }

    public XMLService(String pConfigName) {
        this.pConfigName = pConfigName;
    }

    public XMLService(ByteArrayInputStream pByteArray) {
        this.mQueryFileStream = pByteArray;
    }

    private  void openAllFileStreams(){
        if(mQueryFileStream == null) {
            mQueryFileStream = XMLService.class.
                    getResourceAsStream(pConfigName != null ? pConfigName : "/resources/SampleQueryConfiguration.xml");
            if (mQueryFileStream == null) {
                mQueryFileStream = XMLService.class.
                        getClassLoader().getResourceAsStream(pConfigName != null ? pConfigName : "SampleQueryConfiguration.xml");
            }
        }
    }

    private  void getFileDocument(InputStream pFileStream) throws IOException {
         mQueryFileDocument = Jsoup.parse(pFileStream, null, "", Parser.xmlParser());
    }


    private void parseQueryFile(){
        parseGeneralPart(mQueryFileDocument);
        parseForPersons(mQueryFileDocument);
        parseAJAXPart(mQueryFileDocument);
        parseForExcelSheets(mQueryFileDocument);
        parseQueryConfigurationSection(mQueryFileDocument);
    }

    private  void parseAJAXPart(Document pParsingDocument){
        for(Element mAJAXLElement : pParsingDocument.selectFirst("resource_ajax_url_list").children())
            mReturningCompiler.mAjaxConfigurationsAssumer.add(mAJAXLElement.attr("assign_name"), mAJAXLElement.attr("url_part"), mAJAXLElement.attr("variable"), QueryTypeAction.Resource);
        for(Element mAJAXLElement : pParsingDocument.selectFirst("document_ajax_url_list").children())
            mReturningCompiler.mAjaxConfigurationsAssumer.add(mAJAXLElement.attr("assign_name"), mAJAXLElement.attr("url_part"), mAJAXLElement.attr("variable"), QueryTypeAction.Document);
        for(Element mAJAXLElement : pParsingDocument.selectFirst("nir_ajax_url_list").children())
            mReturningCompiler.mAjaxConfigurationsAssumer.add(mAJAXLElement.attr("assign_name"), mAJAXLElement.attr("url_part"), mAJAXLElement.attr("variable"), QueryTypeAction.NIR);
        for(Element mAJAXLElement : pParsingDocument.selectFirst("rid_ajax_url_list").children())
            mReturningCompiler.mAjaxConfigurationsAssumer.add(mAJAXLElement.attr("assign_name"), mAJAXLElement.attr("url_part"), mAJAXLElement.attr("variable"), QueryTypeAction.RID);
    }

    private  void parseGeneralPart(Document pParsingDocument) {
        Element mGeneralListElement = pParsingDocument.selectFirst("general");
        for(Element mGeneralElement : mGeneralListElement.children())
            switch (mGeneralElement.tagName()){
                case("psu_query_settings"): {
                    mReturningCompiler.mQueryConfigurationsAssumer.mSessionDepartmentLink = mGeneralElement.attr("department_link");
                    mReturningCompiler.mQueryConfigurationsAssumer.mGroupListLink = mGeneralElement.attr("list_of_study_groups_link");
                    mReturningCompiler.mQueryConfigurationsAssumer.mProfileService = mGeneralElement.attr("profile_service_link");
                    mReturningCompiler.mQueryConfigurationsAssumer.mQueryType = QueryTypeAction.valueOf(mGeneralElement.attr("query_type"));
                    mReturningCompiler.mQueryConfigurationsAssumer.mParseType =  QueryTypeAction.valueOf(mGeneralElement.attr("parse_type"));
                    mReturningCompiler.mQueryConfigurationsAssumer.mExportPath = mGeneralElement.attr("export_path");
                    break;
                }
            }
    }

    private  void parseForPersons(Document pParsingDocument) {
        Element mPersonListElement = pParsingDocument.selectFirst("person_query_list");
        for(Element tPersonElement : mPersonListElement.children()){
            String tGroup = tPersonElement.attr("group");
            if(tGroup != null){
                // TODO add group dependence
                mReturningCompiler.mDepartmentMembersAssumer.add(tPersonElement.attr("full_name"));
            }
            else
                mReturningCompiler.mDepartmentMembersAssumer.add(tPersonElement.attr("full_name"));
        }
    }

    private void parseForExcelSheets(Document pParsingDocument) {
        for(Element mStylesList : pParsingDocument.selectFirst("excel_styles").children())
            new CellStyle(
                    mStylesList.attr("Name"),
                    mStylesList.attr("FontFamily"),
                    mStylesList.attr("Color"),
                    Integer.valueOf(mStylesList.attr("ColumnWidth")),
                    Integer.valueOf(mStylesList.attr("FontSize")),
                    Boolean.valueOf(mStylesList.attr("Wrappable")),
                    Boolean.valueOf(mStylesList.attr("Italic")),
                    Boolean.valueOf(mStylesList.attr("Bold")),
                    Boolean.valueOf(mStylesList.attr("UnderLine"))
                    );
        Element mSheetList = pParsingDocument.selectFirst("sheets_list");
        for(Element tSheetElement : mSheetList.children()){
            SheetConfiguration tCurrentSheet = mReturningCompiler.mSheetConfigurationsAssumer.add(tSheetElement.attr("name"), false);

            for(Element tSheetFilterElement : tSheetElement.selectFirst("filters_list").children()) {
                tCurrentSheet.new SheetFilter(
                        FilterType.valueOf(tSheetFilterElement.attr("type")),
                        tSheetFilterElement.attr("variable"),
                        FilterAction.valueOf(tSheetFilterElement.attr("action")),
                        tSheetFilterElement.attr("value"));
            }

            for(Element tTableConfig : tSheetElement.selectFirst("sheet_body").children()){
                switch (tTableConfig.tagName()){
                    case("cell"):{
                        tCurrentSheet.new TableFormConfigurations(
                            tTableConfig.attr("display_text"),
                            QueryTypeAction.valueOf(tTableConfig.attr("query_type")),
                            tTableConfig.attr("section_name"),
                            tTableConfig.attr("variable"),
                            Integer.valueOf(tTableConfig.attr("column")),
                            Integer.valueOf(tTableConfig.attr("row")),
                            tTableConfig.attr("style"));
                            break;
                    }
                    case("label"):{
                        tCurrentSheet.new SimpleLabel(
                                tTableConfig.attr("display_text"),
                                Integer.parseInt(tTableConfig.attr("row")),
                                Integer.parseInt(tTableConfig.attr("column")),
                                tTableConfig.attr("style")
                        );
                        break;
                    }
                }

            }
        }
    }

    private  void parseQuerySection(QueryTypeAction pQueryType, String pListName, Element pQueryTypeListElement){
        for(Element mQueryTypeElement : pQueryTypeListElement.selectFirst(pListName).children()){
            mReturningCompiler.mQueryConfigurationsAssumer.add(
                    pQueryType, mQueryTypeElement.attr("assign_name"), mQueryTypeElement.attr("variable"), Boolean.parseBoolean(mQueryTypeElement.attr("able")));
        }
    }

    private  void parseQueryConfigurationSection(Document pParsingDocument){
        Element mQueryTypeListElement = pParsingDocument.selectFirst("query_configurations_list");
        Map<QueryTypeAction, String> tQueryTypeMap = new HashMap<QueryTypeAction, String>(){{
            put(QueryTypeAction.Resource, "resource_query");
            put(QueryTypeAction.Document, "document_query");
            put(QueryTypeAction.NIR, "nir_query");
            put(QueryTypeAction.RID, "rid_query");
        }};
        tQueryTypeMap.forEach((key, value) -> {
            parseQuerySection(key, value, mQueryTypeListElement);
        });
    }
}



















