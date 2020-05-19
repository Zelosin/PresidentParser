package org.zelosin.Assumers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zelosin.Assumers.Abstract.Assumer;
import org.zelosin.Assumers.AssumersComponents.AJAXConfiguration;
import org.zelosin.Configurations.Query.QueryTypeAction;

import java.util.HashMap;

public class AJAXConfigurationsAssumer implements Assumer {

    public HashMap<QueryTypeAction, HashMap<String, AJAXConfiguration>> mAJAXConfigurationsList = new HashMap<>();

    {
        for (QueryTypeAction value : QueryTypeAction.values()) {
            mAJAXConfigurationsList.put(value, new HashMap<>());
        }
    }

    @Override
    public void consume(Object key, Object value) {
        mAJAXConfigurationsList.get((QueryTypeAction) key).put(((AJAXConfiguration)value).getmAssignName(), (AJAXConfiguration) value);
    }

    @Override
    public void emptyConsume(Object key) {
    }

    @Override
    public void set(JSONObject jsonObject) {

    }

    public JSONArray provideAJAXContent() {
        JSONArray returningList = new JSONArray();
        for (QueryTypeAction queryType : QueryTypeAction.values()) {
            JSONArray queryList = new JSONArray();
            if(!mAJAXConfigurationsList.get(queryType).isEmpty()) {
                mAJAXConfigurationsList.get(queryType).forEach(
                        (key, value) -> {
                            JSONObject processingJSON = new JSONObject();
                            processingJSON.put("sectionName", key);
                            processingJSON.put("variable", value.getmVariableName());
                            queryList.put(processingJSON);
                        }
                );
            }
            returningList.put(queryList);
        }
        return returningList;
    }

    @Override
    public JSONObject provideContent() {
        return null;
    }

    public void add(String mAssignName, String pURLPart, String pVariableName, QueryTypeAction pQueryType) {
        consume(pQueryType, new AJAXConfiguration(pURLPart, pVariableName, mAssignName));
    }


}
