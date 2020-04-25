package org.zelosin.Assumers.AssumersComponents;

public class AJAXConfiguration {

    private String mURLPart, mVariableName, mAssignName;

    public AJAXConfiguration(String mURLPart, String mVariableName, String mAssignName) {
        this.mURLPart = mURLPart;
        this.mVariableName = mVariableName;
        this.mAssignName = mAssignName;
    }

    public String getmURLPart() {
        return mURLPart;
    }

    public String getmVariableName() {
        return mVariableName;
    }

    public String getmAssignName() {
        return mAssignName;
    }

    public AJAXConfiguration() {
    }
}
