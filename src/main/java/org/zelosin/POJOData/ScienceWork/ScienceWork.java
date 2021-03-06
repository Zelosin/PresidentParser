package org.zelosin.POJOData.ScienceWork;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.Services.BasicServicesCompiler;
import org.zelosin.Services.Samples.PSUService;

import java.util.HashMap;
import java.util.Map;

public class ScienceWork {

    public Map<String, String> mScienceWorkInformation = new HashMap<String, String>();
    protected String mScienceWorkLink;
    public boolean isVerified = true;
    public boolean hasDuplicate = true;

    public void parse(){};

    public ScienceWork(String mScienceWorkLink) {
        this.mScienceWorkLink = mScienceWorkLink;
    }

    public ScienceWork() {}

    public String getmScienceWorkLink() {
        return mScienceWorkLink;
    }

    public void parsePSUScienceWorkPage( QueryTypeAction pQueryType, BasicServicesCompiler pBasicCompiler){
        Document mCurrentDocument = PSUService.makeJSOUPQuery(this.getmScienceWorkLink());
        String tKey;

        this.mScienceWorkInformation.put(pBasicCompiler.mQueryConfigurationsAssumer.getVariable(pQueryType, "Наименование:"), mCurrentDocument.select("h1").last().text());

        for (Element tTableElement : mCurrentDocument.select("tr")) {
            tKey = tTableElement.select("td").first().text();
            if (tKey.equals("Авторы (ПГУ):") || tKey.equals("Участники (ПГУ):")) {
                this.mScienceWorkInformation.put(
                        pBasicCompiler.mQueryConfigurationsAssumer.getVariable(pQueryType, tKey),
                        tTableElement.select("td").last().text().replaceAll("\\s\\[\\d+\\]", "")
                );
                continue;
            }
            if (tKey.equals("Место хранения:") || tKey.equals("Категория:"))
                this.mScienceWorkInformation.put(
                        pBasicCompiler.mQueryConfigurationsAssumer.getVariable(pQueryType, tKey),
                        tTableElement.select("td").last().previousElementSibling().text()
                );
            else
                this.mScienceWorkInformation.put(
                        pBasicCompiler.mQueryConfigurationsAssumer.getVariable(pQueryType, tKey),
                        tTableElement.select("td").last().text()
                );
        }
    }

}
