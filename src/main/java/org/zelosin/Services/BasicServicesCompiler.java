package org.zelosin.Services;

import org.zelosin.Assumers.AJAXConfigurationsAssumer;
import org.zelosin.Assumers.DepartmentMembersAssumer;
import org.zelosin.Assumers.QueryConfigurationsAssumer;
import org.zelosin.Assumers.SheetConfigurationsAssumer;

public class BasicServicesCompiler {

    public DepartmentMembersAssumer mDepartmentMembersAssumer;
    public QueryConfigurationsAssumer mQueryConfigurationsAssumer;
    public AJAXConfigurationsAssumer mAjaxConfigurationsAssumer;
    public SheetConfigurationsAssumer mSheetConfigurationsAssumer;

    public BasicServicesCompiler() {
        this.mDepartmentMembersAssumer = new DepartmentMembersAssumer();
        this.mQueryConfigurationsAssumer = new QueryConfigurationsAssumer();
        this.mAjaxConfigurationsAssumer = new AJAXConfigurationsAssumer();
        this.mSheetConfigurationsAssumer = new SheetConfigurationsAssumer();
    }

}
