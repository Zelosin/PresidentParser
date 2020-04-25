package org.zelosin.Assumers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zelosin.Assumers.Abstract.Assumer;
import org.zelosin.Assumers.AssumersComponents.DepartmentMember;

import java.util.HashMap;


public class DepartmentMembersAssumer implements Assumer {

    public HashMap<String, DepartmentMember> mDepartmentMembersList = new HashMap<>();

    @Override
    public void consume(Object pName, Object pDM) {
        mDepartmentMembersList.put((String)pName, (DepartmentMember) pDM);
    }

    @Override
    public void emptyConsume(Object pName) {
        mDepartmentMembersList.put((String)pName, new DepartmentMember((String)pName));
    }

    @Override
    public void set(JSONObject jsonObject) {
        mDepartmentMembersList.clear();
        JSONArray membersArray = (JSONArray) jsonObject.get("members_list");
        membersArray.forEach(member -> {
            add((String) member);
        });
    }

    @Override
    public JSONObject provideContent() {
        JSONArray membersList = new JSONArray();
        mDepartmentMembersList.forEach((key, value) ->{
            membersList.put(value.getmName());
        });
        return (new JSONObject()).put("members_list", membersList);
    }

    public void add(String pName) {
        this.consume(pName, new DepartmentMember(pName));
    }
}
