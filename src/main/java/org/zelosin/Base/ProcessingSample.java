package org.zelosin.Base;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zelosin.Assumers.AssumersComponents.DepartmentMember;
import org.zelosin.Configurations.Form.FilterType;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.Services.BasicServicesCompiler;
import org.zelosin.Services.Samples.ExcelService;
import org.zelosin.Services.Samples.PSUService;
import org.zelosin.Services.Samples.XMLService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;

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



    public ByteArrayOutputStream build() throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element body = doc.createElement("body");
        doc.appendChild(body);


        Element general = doc.createElement("general");

        Element psu_query_settings = doc.createElement("psu_query_settings");

        psu_query_settings.setAttribute("department_link", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mSessionDepartmentLink);
        psu_query_settings.setAttribute("list_of_study_groups_link", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mGroupListLink);
        psu_query_settings.setAttribute("profile_service_link", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mProfileService);
        psu_query_settings.setAttribute("query_type", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryType.toString());
        psu_query_settings.setAttribute("parse_type", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mParseType.toString());
        psu_query_settings.setAttribute("export_path", this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mExportPath);

        general.appendChild(psu_query_settings);

        body.appendChild(general);

        Element person_query_list = doc.createElement("person_query_list");
        this.mBasicServicesCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, value) ->{
            Element person = doc.createElement("person");
            person.setAttribute("full_name", key);
            person_query_list.appendChild(person);
        });

        body.appendChild(person_query_list);


        Element query_configurations_list = doc.createElement("query_configurations_list");
        Element resource_query = doc.createElement("resource_query");

        this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryConfigurationsList.get(QueryTypeAction.Resource).forEach(
                queryConfig ->{
                    Element query = doc.createElement("query");
                    query.setAttribute("assign_name", queryConfig.mAssignName);
                    query.setAttribute("variable", queryConfig.mVariable);
                    resource_query.appendChild(query);
                }
        );

        query_configurations_list.appendChild(resource_query);


        Element document_query = doc.createElement("document_query");

        this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryConfigurationsList.get(QueryTypeAction.Document).forEach(
                queryConfig ->{
                    Element query = doc.createElement("query");
                    query.setAttribute("assign_name", queryConfig.mAssignName);
                    query.setAttribute("variable", queryConfig.mVariable);
                    document_query.appendChild(query);
                }
        );

        query_configurations_list.appendChild(document_query);


        Element nir_query = doc.createElement("nir_query");

        this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryConfigurationsList.get(QueryTypeAction.NIR).forEach(
                queryConfig ->{
                    Element query = doc.createElement("query");
                    query.setAttribute("assign_name", queryConfig.mAssignName);
                    query.setAttribute("variable", queryConfig.mVariable);
                    nir_query.appendChild(query);
                }
        );

        query_configurations_list.appendChild(nir_query);


        Element rid_query = doc.createElement("rid_query");

        this.mBasicServicesCompiler.mQueryConfigurationsAssumer.mQueryConfigurationsList.get(QueryTypeAction.RID).forEach(
                queryConfig ->{
                    Element query = doc.createElement("query");
                    query.setAttribute("assign_name", queryConfig.mAssignName);
                    query.setAttribute("variable", queryConfig.mVariable);
                    rid_query.appendChild(query);
                }
        );

        query_configurations_list.appendChild(rid_query);


        body.appendChild(query_configurations_list);


        Element ajax_url_list = doc.createElement("ajax_url_list");

        Element resource_ajax_url_list = doc.createElement("resource_ajax_url_list");

        this.mBasicServicesCompiler.mAjaxConfigurationsAssumer.mAJAXConfigurationsList.get(QueryTypeAction.Resource).forEach(
                (key, value) ->{
                    Element ajax = doc.createElement("ajax");
                    ajax.setAttribute("assign_name", value.getmAssignName());
                    ajax.setAttribute("url_part", value.getmURLPart());
                    ajax.setAttribute("variable", value.getmVariableName());
                    resource_ajax_url_list.appendChild(ajax);
                }
        );

        ajax_url_list.appendChild(resource_ajax_url_list);


        Element document_ajax_url_list = doc.createElement("document_ajax_url_list");

        this.mBasicServicesCompiler.mAjaxConfigurationsAssumer.mAJAXConfigurationsList.get(QueryTypeAction.Document).forEach(
                (key, value) ->{
                    Element ajax = doc.createElement("ajax");
                    ajax.setAttribute("assign_name", value.getmAssignName());
                    ajax.setAttribute("url_part", value.getmURLPart());
                    ajax.setAttribute("variable", value.getmVariableName());
                    document_ajax_url_list.appendChild(ajax);
                }
        );

        ajax_url_list.appendChild(document_ajax_url_list);


        Element nir_ajax_url_list = doc.createElement("nir_ajax_url_list");

        this.mBasicServicesCompiler.mAjaxConfigurationsAssumer.mAJAXConfigurationsList.get(QueryTypeAction.NIR).forEach(
                (key, value) ->{
                    Element ajax = doc.createElement("ajax");
                    ajax.setAttribute("assign_name", value.getmAssignName());
                    ajax.setAttribute("url_part", value.getmURLPart());
                    ajax.setAttribute("variable", value.getmVariableName());
                    nir_ajax_url_list.appendChild(ajax);
                }
        );

        ajax_url_list.appendChild(nir_ajax_url_list);


        Element rid_ajax_url_list = doc.createElement("rid_ajax_url_list");

        this.mBasicServicesCompiler.mAjaxConfigurationsAssumer.mAJAXConfigurationsList.get(QueryTypeAction.RID).forEach(
                (key, value) ->{
                    Element ajax = doc.createElement("ajax");
                    ajax.setAttribute("assign_name", value.getmAssignName());
                    ajax.setAttribute("url_part", value.getmURLPart());
                    ajax.setAttribute("variable", value.getmVariableName());
                    rid_ajax_url_list.appendChild(ajax);
                }
        );

        ajax_url_list.appendChild(rid_ajax_url_list);

        body.appendChild(ajax_url_list);

        Element excel_configurations_list = doc.createElement("excel_configurations_list");


        Element excel_styles = doc.createElement("excel_styles");

        Element basic_style = doc.createElement("style");
        basic_style.setAttribute("Name", "Basic");
        basic_style.setAttribute("FontFamily", "Calibri");
        basic_style.setAttribute("Color", "BLACK");
        basic_style.setAttribute("ColumnWidth", "5000");
        basic_style.setAttribute("FontSize", "11");
        basic_style.setAttribute("Wrappable", "false");
        basic_style.setAttribute("Italic", "false");
        basic_style.setAttribute("Bold", "false");
        basic_style.setAttribute("UnderLine", "false");
        excel_styles.appendChild(basic_style);


        Element red_guardian_style = doc.createElement("style");
        red_guardian_style.setAttribute("Name", "RedGuardian");
        red_guardian_style.setAttribute("FontFamily", "Arial");
        red_guardian_style.setAttribute("Color", "RED");
        red_guardian_style.setAttribute("ColumnWidth", "10000");
        red_guardian_style.setAttribute("FontSize", "15");
        red_guardian_style.setAttribute("Wrappable", "true");
        red_guardian_style.setAttribute("Italic", "true");
        red_guardian_style.setAttribute("Bold", "true");
        red_guardian_style.setAttribute("UnderLine", "false");
        excel_styles.appendChild(red_guardian_style);


        excel_configurations_list.appendChild(excel_styles);

        Element sheets_list = doc.createElement("sheets_list");


        this.mBasicServicesCompiler.mSheetConfigurationsAssumer.mSheetConfigurationsList.forEach(
                sheetConfig ->{
                    Element sheet = doc.createElement("sheet");

                    sheet.setAttribute("name", sheetConfig.mSheetName);

                    Element sheet_body = doc.createElement("sheet_body");

                    sheetConfig.mLabelsList.forEach(
                            simpleLabel -> {
                                Element label = doc.createElement("label");
                                label.setAttribute("display_text", simpleLabel.mDisplayText);
                                label.setAttribute("column", String.valueOf(simpleLabel.mDisplayColumn));
                                label.setAttribute("row", String.valueOf(simpleLabel.mDisplayRow));
                                sheet_body.appendChild(label);
                            }
                    );

                    SimpleDateFormat dateForamter = new SimpleDateFormat("dd.MM.yyyy");

                    sheetConfig.mTableFormConfigurations.forEach(
                            tableFormConfigurations -> {
                                try {
                                    Element cell = doc.createElement("cell");
                                    cell.setAttribute("display_text", tableFormConfigurations.getmDisplayText());
                                    cell.setAttribute("query_type", tableFormConfigurations.getmQueryType().toString());
                                    cell.setAttribute("section_name", tableFormConfigurations.getmSectionName());
                                    cell.setAttribute("variable", tableFormConfigurations.getmVariable());
                                    cell.setAttribute("column", tableFormConfigurations.getmDisplayColumn().toString());
                                    cell.setAttribute("row", tableFormConfigurations.getmDisplayRow().toString());
                                    if (tableFormConfigurations.getmValueFilter() != null) {
                                        cell.setAttribute("comp_type", tableFormConfigurations.getmValueFilter().getmFilterType().toString());
                                        cell.setAttribute("comp_action", tableFormConfigurations.getmValueFilter().getmFilterAction().toString());
                                        cell.setAttribute("comp_value", tableFormConfigurations.getmValueFilter().getmComparableValue());
                                        if (tableFormConfigurations.getmValueFilter().getmFilterType().equals(FilterType.DateInterval)) {
                                            cell.setAttribute("comp_first_date", dateForamter.format(tableFormConfigurations.getmValueFilter().getmFirstDate()));
                                            cell.setAttribute("comp_second_date", dateForamter.format(tableFormConfigurations.getmValueFilter().getmSecondDate()));
                                        }
                                        else if(tableFormConfigurations.getmValueFilter().getmFilterType().equals(FilterType.DateCompare)){
                                            cell.setAttribute("comp_first_date", dateForamter.format(tableFormConfigurations.getmValueFilter().getmFirstDate()));
                                            cell.setAttribute("comp_second_date", null);
                                        }
                                    }

                                    sheet_body.appendChild(cell);
                                }
                                catch (Exception e){
                                }
                            }
                    );
                    sheet.appendChild(sheet_body);
                    sheets_list.appendChild(sheet);
                }
        );


        excel_configurations_list.appendChild(sheets_list);





        body.appendChild(excel_configurations_list);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        StreamResult result=new StreamResult(bos);
        transformer.transform(source, result);
        return bos;
    }

}
