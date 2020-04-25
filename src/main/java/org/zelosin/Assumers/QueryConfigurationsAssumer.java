package org.zelosin.Assumers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zelosin.Assumers.Abstract.Assumer;
import org.zelosin.Assumers.AssumersComponents.QueryConfiguration;
import org.zelosin.Configurations.Query.QueryTypeAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryConfigurationsAssumer implements Assumer {

    public String mSessionDepartmentLink;
    public String mGroupListLink;
    public String mProfileService;

    public QueryTypeAction mQueryType;
    public QueryTypeAction mParseType;
    public String mExportPath;

    public Map<QueryTypeAction, ArrayList<QueryConfiguration>> mQueryConfigurationsList = new HashMap<>();

    @Override
    public void consume(Object key, Object value) {
        (mQueryConfigurationsList.computeIfAbsent((QueryTypeAction)key, k -> new ArrayList<>())).add((QueryConfiguration) value);
    }

    @Override
    public void emptyConsume(Object key) {
        (mQueryConfigurationsList.computeIfAbsent((QueryTypeAction)key, k -> new ArrayList<>())).add(new QueryConfiguration());
    }

    @Override
    public void set(JSONObject jsonObject) {
        mQueryType = QueryTypeAction.valueOf((String)jsonObject.get("QueryType"));
        mParseType = QueryTypeAction.valueOf((String)jsonObject.get("ParseType"));
        mSessionDepartmentLink = (String)jsonObject.get("DepartmentLink");
        mProfileService = (String)jsonObject.get("ProfileService");
        mExportPath = (String)jsonObject.get("FileName");

    }


    public JSONArray provideQueryContent() {
        JSONArray returningList = new JSONArray();
        for (QueryTypeAction queryType : QueryTypeAction.values()) {
            if(mQueryConfigurationsList.containsKey(queryType)) {
                JSONObject processingJSON = new JSONObject();
                mQueryConfigurationsList.get(queryType).forEach(
                        queryConfiguration -> {
                            processingJSON.put(queryConfiguration.mAssignName, queryConfiguration.mVariable);
                        }
                );
                returningList.put(processingJSON);
            }
        }
        return returningList;
    }


    @Override
    public JSONObject provideContent() {
        return new JSONObject()
                .put("QueryType", mQueryType)
                .put("ParseType", mParseType)
                .put("DepartmentLink", mSessionDepartmentLink)
                .put("ProfileService", mProfileService)
                .put("StudentsLinkList", "")
                .put("FileName", "")
                .put("QueryAction", "");
    }

    public void add(QueryTypeAction pQueryType, String mVariable, String mAssignName, boolean mIsAble){
        consume(pQueryType, new QueryConfiguration(mVariable, mAssignName, mIsAble));
    }

    public String getAssignName(QueryTypeAction pQueryType, String pVariable) {
        ArrayList<QueryConfiguration> tArray = mQueryConfigurationsList.get(pQueryType);
        for (QueryConfiguration tConfig : tArray) {
            if (pVariable.equals(tConfig.mVariable)) return tConfig.mAssignName;
        }
        return null;
    }

    public String getVariable(QueryTypeAction pQueryType, String pAssignName) {
        ArrayList<QueryConfiguration> tArray = mQueryConfigurationsList.get(pQueryType);
        for (QueryConfiguration tConfig : tArray) {
            if (pAssignName.equals(tConfig.mAssignName)) return tConfig.mVariable;
        }
        return null;
    }


}











