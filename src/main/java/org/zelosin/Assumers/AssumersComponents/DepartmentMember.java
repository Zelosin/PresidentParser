package org.zelosin.Assumers.AssumersComponents;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.POJOData.Abstract.DepartmentMemberInformation;
import org.zelosin.POJOData.ScienceWork.ScienceWork;

import java.util.ArrayList;

public class DepartmentMember extends DepartmentMemberInformation {

    public Table<QueryTypeAction, String, ArrayList<ScienceWork>> mMemberInformationList = HashBasedTable.create();
    public ArrayList<ScienceWork> getSectionArray(QueryTypeAction pQueryType, String pSectionName){
        ArrayList<ScienceWork> tScienceWorkArray = mMemberInformationList.get(pQueryType, pSectionName);
        if(tScienceWorkArray == null) {
            tScienceWorkArray = new ArrayList<>();
            mMemberInformationList.put(pQueryType, pSectionName, tScienceWorkArray);
        }
        return tScienceWorkArray;
    }

    public DepartmentMember(String mPSUProfileLink, String mName, String mPSUProfileID) {
        this.mPSUProfileLink = mPSUProfileLink;
        this.mName = mName;
        this.mPSUProfileID = mPSUProfileID;
    }

    public DepartmentMember(String mName) {
        this.mName = mName;
    }

    public String getmPSUProfileLink() {
        return mPSUProfileLink;
    }

    public String getmName() {
        return mName;
    }

    public String getmPSUProfileID() {
        return mPSUProfileID;
    }

    public void setmPSUProfileLink(String mPSUProfileLink) {
        this.mPSUProfileID = (mPSUProfileLink.replaceAll("\\D+",""));
        this.mPSUProfileLink = mPSUProfileLink;
        DepartmentMemberInformation.mAssignedProfileCount++;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
