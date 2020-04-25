package org.zelosin.Assumers.AssumersComponents;

public class QueryConfiguration {
    public String mVariable, mAssignName;
    public boolean mIsAble;

    public QueryConfiguration(String mAssignName, String mVariable, boolean mIsAble) {
        this.mVariable = mVariable;
        this.mAssignName = mAssignName;
        this.mIsAble = mIsAble;
    }

    public QueryConfiguration() {
    }
}