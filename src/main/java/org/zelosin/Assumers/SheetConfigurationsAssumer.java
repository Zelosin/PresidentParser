package org.zelosin.Assumers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zelosin.Assumers.Abstract.Assumer;
import org.zelosin.Assumers.AssumersComponents.AJAXConfiguration;
import org.zelosin.Assumers.AssumersComponents.SheetConfiguration;
import org.zelosin.Assumers.AssumersComponents.SheetsComponents.SheetFilter;
import org.zelosin.Assumers.AssumersComponents.SheetsComponents.SimpleLabel;
import org.zelosin.Assumers.AssumersComponents.SheetsComponents.TableFormConfigurations;
import org.zelosin.Configurations.Form.FilterAction;
import org.zelosin.Configurations.Form.FilterType;
import org.zelosin.Configurations.Query.QueryTypeAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SheetConfigurationsAssumer implements Assumer {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
    public ArrayList<SheetConfiguration> mSheetConfigurationsList = new ArrayList<>();

    @Override
    public void consume(Object key, Object value) {
        mSheetConfigurationsList.add((SheetConfiguration)value);
    }


    @Override
    public void emptyConsume(Object key) {

    }

    @Override
    public void set(JSONObject jsonObject) {
        JSONArray sheetsList = (JSONArray) jsonObject.get("sheets_list");
        mSheetConfigurationsList.clear();
        sheetsList.forEach(sheet -> {
                    SheetConfiguration currentSheet = add(((JSONArray)sheet).get(0), false);
                    ((JSONArray)((JSONArray)sheet).get(1)).forEach(sheetConfig->{
                        switch ((String)((JSONObject)sheetConfig).get("selectedCellType")){
                            case("Cell"):{
                                TableFormConfigurations configurations = new TableFormConfigurations(
                                        (String)((JSONObject)sheetConfig).get("cellText"),
                                        QueryTypeAction.valueOf((String)((JSONObject)sheetConfig).get("selectedQueryType")),
                                        null,
                                        (String)((JSONObject)sheetConfig).get("selectedVariable"),
                                        (Integer) (((JSONObject)sheetConfig)).get("columnNumber"),
                                        (Integer) (((JSONObject)sheetConfig)).get("rowNumber"),
                                        null);
                                List<String> selectedSections = new ArrayList<>();
                                ((JSONArray)((JSONObject)sheetConfig).get("selectedSection")).forEach(json ->{
                                    selectedSections.add((String)((JSONObject)json).get("variable"));
                                });
                                configurations.setmSectionName(String.join("::", selectedSections));

                                if(!(((JSONObject)sheetConfig).get("filterType")).equals("NoFilter")) {
                                    try {
                                        if (((JSONObject) sheetConfig).get("filterType").equals("DateCompare")) {
                                            try {
                                                configurations.setmValueFilter(
                                                        new SheetFilter(
                                                                FilterType.valueOf((String) ((JSONObject) sheetConfig).get("filterType")),
                                                                FilterAction.valueOf((String) ((JSONObject) sheetConfig).get("filterAction")),
                                                                SheetFilter.mClienDateFormatter.parse((String) ((JSONObject) sheetConfig).get("filterDateValue")),
                                                                null
                                                        )
                                                );
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (((JSONObject) sheetConfig).get("filterType").equals("DateInterval")) {
                                            try {
                                                configurations.setmValueFilter(
                                                        new SheetFilter(
                                                                FilterType.valueOf((String) ((JSONObject) sheetConfig).get("filterType")),
                                                                FilterAction.valueOf((String) ((JSONObject) sheetConfig).get("filterAction")),
                                                                SheetFilter.mClienDateFormatter.parse((String) ((JSONArray) ((JSONObject) sheetConfig).get("filterDateValue")).get(0)),
                                                                SheetFilter.mClienDateFormatter.parse((String) ((JSONArray) ((JSONObject) sheetConfig).get("filterDateValue")).get(1))
                                                        )
                                                );
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            configurations.setmValueFilter(
                                                    new SheetFilter(
                                                            FilterType.valueOf((String) ((JSONObject) sheetConfig).get("filterType")),
                                                            FilterAction.valueOf((String) ((JSONObject) sheetConfig).get("filterAction")),
                                                            (String) ((JSONObject) sheetConfig).get("filterCompValue")
                                                    )
                                            );
                                        }
                                    }catch (Exception e){

                                    }
                                }

                                currentSheet.addTableConfig(configurations);
                                break;
                            }
                            case("Label"):{
                                currentSheet.addLabel(new SimpleLabel(
                                        (String)((JSONObject)sheetConfig).get("cellText"),
                                        (Integer)(((JSONObject)sheetConfig)).get("rowNumber"),
                                        (Integer)(((JSONObject)sheetConfig)).get("columnNumber"),
                                        null
                                ));
                                break;
                            }
                        }
                    });
                }
        );
    }

    @Override
    public JSONObject provideContent() {
        return null;
    }

    public JSONObject provideContent(AJAXConfigurationsAssumer ajaxAssumer) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject returningJSON = new JSONObject();
        JSONArray sheetsList = new JSONArray();
        mSheetConfigurationsList.forEach(sheet ->{
            JSONArray currentSheetList = new JSONArray();
            currentSheetList.put(sheet.mSheetName);
            JSONArray configList = new JSONArray();
            sheet.mTableFormConfigurations.forEach(tableConfig ->{
                JSONObject sheetConfig = new JSONObject();
                sheetConfig.put("selectedCellType", "Cell");
                sheetConfig.put("selectedVariable", tableConfig.getmVariable());
                //sheetConfig.put("selectedSection", tableConfig.getmSectionName());


                JSONArray sectionsArray = new JSONArray();
                for(String section : tableConfig.getmSectionName().split("::")){
                    JSONObject processingSection = new JSONObject();
                    HashMap<String, AJAXConfiguration> ajaxMap = ajaxAssumer.mAJAXConfigurationsList.get(tableConfig.getmQueryType());
                    ajaxMap.forEach((key, value) ->{
                        if(value.getmVariableName().equals(section)){
                            processingSection.put("sectionName", value.getmAssignName());
                            processingSection.put("variable", value.getmVariableName());
                            sectionsArray.put(processingSection);
                        }
                    });
                }

                sheetConfig.put("selectedSection", sectionsArray);
                sheetConfig.put("cellText", tableConfig.getmDisplayText());
                sheetConfig.put("rowNumber", tableConfig.getmDisplayRow());
                sheetConfig.put("columnNumber", tableConfig.getmDisplayColumn());
                sheetConfig.put("selectedQueryType", String.valueOf(tableConfig.getmQueryType()));
                if(tableConfig.getmValueFilter() != null) {
                    if (tableConfig.getmValueFilter().getmFilterType() != null) {
                        sheetConfig.put("filterAction", tableConfig.getmValueFilter().getmFilterAction().toString());
                        sheetConfig.put("filterType", tableConfig.getmValueFilter().getmFilterType().toString());
                        if (tableConfig.getmValueFilter().getmFilterType() != FilterType.DateCompare &&
                                tableConfig.getmValueFilter().getmFilterType() != FilterType.DateInterval) {
                            sheetConfig.put("filterCompValue", tableConfig.getmValueFilter().getmComparableValue());
                        } else if (tableConfig.getmValueFilter().getmFilterType() == FilterType.DateCompare) {
                            sheetConfig.put("filterDateValue", dateFormatter.format(tableConfig.getmValueFilter().getmFirstDate()) + "T21:00:00.000Z");
                        } else {
                            JSONArray array = new JSONArray();
                            array.put(dateFormatter.format(tableConfig.getmValueFilter().getmFirstDate()) + "T21:00:00.000Z");
                            array.put(dateFormatter.format(tableConfig.getmValueFilter().getmSecondDate()) + "T21:00:00.000Z");
                            sheetConfig.put("filterDateValue", array);
                        }
                    } else {
                        sheetConfig.put("filterType", "NoFilter");
                    }
                }
                else {
                    sheetConfig.put("filterType", "NoFilter");
                }
                configList.put(sheetConfig);
            });
            sheet.mLabelsList.forEach(lableConfig ->{
                JSONObject sheetConfig = new JSONObject();
                sheetConfig.put("selectedCellType", "Label");
                sheetConfig.put("cellText", lableConfig.mDisplayText);
                sheetConfig.put("rowNumber", lableConfig.mDisplayRow);
                sheetConfig.put("columnNumber", lableConfig.mDisplayColumn);
                configList.put(sheetConfig);
            });
            currentSheetList.put(configList);
            sheetsList.put(currentSheetList);
        });
        returningJSON.put("sheets", sheetsList);
        return returningJSON;
    }

    public SheetConfiguration add(Object mSheetName, Boolean mIsTextWrappable) {
        SheetConfiguration tSheet = new SheetConfiguration((String)mSheetName, mIsTextWrappable);
        consume(null, tSheet);
        return tSheet;
    }
}






















