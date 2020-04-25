package org.zelosin.Assumers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zelosin.Assumers.Abstract.Assumer;
import org.zelosin.Assumers.AssumersComponents.SheetConfiguration;
import org.zelosin.Configurations.Query.QueryTypeAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                                currentSheet.new TableFormConfigurations(
                                        (String)((JSONObject)sheetConfig).get("cellText"),
                                        QueryTypeAction.valueOf((String)((JSONObject)sheetConfig).get("selectedQueryType")),
                                        (String)((JSONObject)sheetConfig).get("selectedSection"),
                                        (String)((JSONObject)sheetConfig).get("selectedVariable"),
                                        (Integer) (((JSONObject)sheetConfig)).get("columnNumber"),
                                        (Integer) (((JSONObject)sheetConfig)).get("rowNumber"),
                                        null);
                                break;
                            }
                            case("Label"):{
                                currentSheet.new SimpleLabel(
                                        (String)((JSONObject)sheetConfig).get("cellText"),
                                        (Integer)(((JSONObject)sheetConfig)).get("rowNumber"),
                                        (Integer)(((JSONObject)sheetConfig)).get("columnNumber"),
                                        null
                                );
                                break;
                            }
                        }
                    });
                }
        );
    }

    @Override
    public JSONObject provideContent() {
        JSONObject returningJSON = new JSONObject();
        JSONArray sheetsList = new JSONArray();
        mSheetConfigurationsList.forEach(sheet ->{
            JSONArray currentSheetList = new JSONArray();
            currentSheetList.put(sheet.mSheetName);
            JSONArray configList = new JSONArray();
            sheet.mTableFormConfigurations.forEach(tableConfig ->{
                JSONObject sheetConfig = new JSONObject();
                sheetConfig.put("selectedCellType", "Cell");
                sheetConfig.put("selectedVariable", tableConfig.mVariable);
                sheetConfig.put("selectedSection", tableConfig.mSectionName);
                sheetConfig.put("cellText", tableConfig.mDisplayText);
                sheetConfig.put("rowNumber", tableConfig.mDisplayRow);
                sheetConfig.put("columnNumber", tableConfig.mDisplayColumn);
                sheetConfig.put("selectedQueryType", String.valueOf(tableConfig.mQueryType));
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






















