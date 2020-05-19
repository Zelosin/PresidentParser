package org.zelosin.Services.Samples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zelosin.Assumers.AssumersComponents.AJAXConfiguration;
import org.zelosin.Assumers.AssumersComponents.DepartmentMember;
import org.zelosin.Configurations.Query.QueryTypeAction;
import org.zelosin.POJOData.ScienceWork.ScienceWork;
import org.zelosin.Services.BasicServicesCompiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PSUService{
    private Document mDepartmentMembersDocument;
    private Document mGroupsListDocument;
    private Document mGroupDocument;

    private BasicServicesCompiler mBasicServiceCompiler;

    private static final String WORK_LIST_API = "https://lk.pnzgu.ru/ajax/portfolio/";
    private static final String WORK_LIST_API_TYPE = "/type/";

    private  Document mMemberWorksDocument;

    public PSUService(BasicServicesCompiler mBasicServiceCompiler) {
        this.mBasicServiceCompiler = mBasicServiceCompiler;
    }

    public  class AsyncPSUScienceWorkPageParseTask implements Runnable {
        public ScienceWork mCurrentScienceWork;
        public QueryTypeAction mQueryType;

        public AsyncPSUScienceWorkPageParseTask(ScienceWork mCurrentScienceWork, QueryTypeAction mQueryType) {
            this.mCurrentScienceWork = mCurrentScienceWork;
            this.mQueryType = mQueryType;
        }

        @Override
        public void run() {
            mCurrentScienceWork.parsePSUScienceWorkPage(mQueryType, mBasicServiceCompiler);
        }
    }

    public  class AsyncPSUProfilePageParseTask implements Runnable {
        public DepartmentMember mCurrentDepartmentMember;
        public QueryTypeAction mQueryType;

        public AsyncPSUProfilePageParseTask(DepartmentMember mCurrentDepartmentMember, QueryTypeAction mQueryType) {
            this.mCurrentDepartmentMember = mCurrentDepartmentMember;
            this.mQueryType = mQueryType;
        }

        @Override
        public void run() {
            assignScienceWorksToDepartmentMember(
                    mQueryType,
                    mCurrentDepartmentMember
            );
        }
    }

    public static synchronized Document makeJSOUPQuery(String pURL) {
        Document mReturningDocument = null;
        if(!pURL.contains("https") && (pURL.contains("http")))
            pURL = pURL.replaceFirst("http", "https");
        System.setProperty("javax.net.ssl.trustStore", "/path/to/web2.uconn.edu.jks");
        try {
            mReturningDocument = Jsoup.connect(pURL)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .get();
        } catch (NullPointerException | IOException e) {
            return null;
        }
        return mReturningDocument;
    }

    public static synchronized Document makeAJAXQueryForDepartmentMember(String pProfileID, String pAJAXQuery) {
        Document mReturningDocument = null;
        try {
            mReturningDocument = Jsoup.connect(WORK_LIST_API + pProfileID + WORK_LIST_API_TYPE + pAJAXQuery)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .get();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return mReturningDocument;
    }

   /* private  void getPersonProfileLinkFromGroupList(DepartmentMember pMember){
        mGroupDocument = PSUService.makeJSOUPQuery(
                mQueryConfigurationsAssumer.mGroupListLink + "/" +
                mGroupsListDocument.select(
                "a:contains("+pMember.getmName()+")").first().attr("href").replaceAll(".+[/]", ""));
                pMember.setmPSUProfileLink( QueryConfigurationsAssumer.mProfileService +
                mGroupDocument.select("a:contains("+pMember.getmName()+")").first().attr("href")
        );
    }*/

    public  void assignDepartmentMemberList(QueryTypeAction pQueryType) {
        mDepartmentMembersDocument = PSUService.makeJSOUPQuery(mBasicServiceCompiler.mQueryConfigurationsAssumer.mSessionDepartmentLink);
        for (Element mDepartmentMember : mDepartmentMembersDocument.select(".nps-row-th")){
            DepartmentMember tMember = mBasicServiceCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.get(mDepartmentMember.select("a").last().text());
            if(tMember != null)
                tMember.setmPSUProfileLink(mDepartmentMember.select("a").last().attr("href"));
        }

        ArrayList<Thread> mAsyncParseThreadArrayList = new ArrayList<>();
        mBasicServiceCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((key, value) ->{
            Thread tThread = new Thread(new AsyncPSUProfilePageParseTask(value, pQueryType));
            mAsyncParseThreadArrayList.add(tThread);
            tThread.start();
        });
        mAsyncParseThreadArrayList.forEach(tThread ->{
            try {
                tThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    public void assignScienceWorksToDepartmentMember(QueryTypeAction pQueryType, DepartmentMember tDepartmentMember) {
        mMemberWorksDocument = PSUService.makeJSOUPQuery(tDepartmentMember.getmPSUProfileLink());
        if(mMemberWorksDocument == null)
            return;

        ArrayList tSectionArray = tDepartmentMember.getSectionArray(QueryTypeAction.Basic, "b_psu_profile_basic_information");
        ScienceWork tBasicInformation = new ScienceWork();
        tSectionArray.add(tBasicInformation);


        for(Element tElement: mMemberWorksDocument.select("[class=\"mr-2\"]").select("a")){
            String tProfileLink = tElement.attr("href");
            if(tProfileLink.contains("elibrary")) tBasicInformation.mScienceWorkInformation.put("b_elibrary_link", tProfileLink);
            else if(tProfileLink.contains("scopus")) tBasicInformation.mScienceWorkInformation.put("b_scopus_link", tProfileLink);
            else if(tProfileLink.contains("publons")) tBasicInformation.mScienceWorkInformation.put("b_publons_link", tProfileLink);
        }

        Elements tWorkElements = mMemberWorksDocument.select(".ru_wrapper_user_row").select(".w-75");
        tBasicInformation.mScienceWorkInformation.put("b_academic_degree", tWorkElements.get(0).text());
        tBasicInformation.mScienceWorkInformation.put("b_academic_rank", tWorkElements.get(1).text());
        tBasicInformation.mScienceWorkInformation.put("b_name", tDepartmentMember.getmName());

        outcrop:
        for(Element tPositionElement : mMemberWorksDocument.selectFirst("#collapse_rows").select(".ru_wrapper_user_row"))
            if(tPositionElement.childNodeSize() > 7) {
                for (Element tWrapElement : tPositionElement.children()) {
                    if (tWrapElement.childNodeSize() == 2) {
                        try {
                            if (tWrapElement.children().first().text().equals("Тип:")) {
                                tBasicInformation.mScienceWorkInformation.put("b_position", tWrapElement.children().get(1).text());
                                break outcrop;
                            }
                        } catch (NullPointerException n) {
                            break outcrop;
                        }
                    }
                }
            }
        switch (pQueryType){
            case Document: {parseMembersSection("#collapseDoc", pQueryType, tDepartmentMember); break;}
            case Resource: {parseMembersSection("#collapseIR", pQueryType, tDepartmentMember);; break;}
            case NIR: {parseMembersSection("#collapseNir", pQueryType, tDepartmentMember);; break;}
            case RID: {parseMembersSection("#collapseRid", pQueryType, tDepartmentMember);; break;}
            case ALL: {
                Map<QueryTypeAction, String> tCSSMap = new HashMap<QueryTypeAction, String>(){{
                    put(QueryTypeAction.Resource, "#collapseIR");
                    put(QueryTypeAction.Document, "#collapseDoc");
                    put(QueryTypeAction.NIR, "#collapseNir");
                    put(QueryTypeAction.RID, "#collapseRid");
                }};
                tCSSMap.forEach((key, value) -> {
                    parseMembersSection(value, key, tDepartmentMember);
                });
                break;
            }
        }
    }

    private  void parseMembersSection(String tCSSQuery, QueryTypeAction pQueryType, DepartmentMember tDepartmentMember){
        Elements tWorkElements = mMemberWorksDocument.select(tCSSQuery);
        if(tWorkElements.size() == 0)
            return;
        tWorkElements = tWorkElements.first().select(".btn.btn-link");
        Document tWorkDocument;
        for (Element tWorkType : tWorkElements) {
            AJAXConfiguration tAJAXParam = (mBasicServiceCompiler.mAjaxConfigurationsAssumer.mAJAXConfigurationsList.get(pQueryType)).get(tWorkType.text());
            if( tAJAXParam != null) {
                tWorkDocument = PSUService.makeAJAXQueryForDepartmentMember(
                        tDepartmentMember.getmPSUProfileID(),
                        tAJAXParam.getmURLPart()
                );
                for (Element tWork : tWorkDocument.select(".showed"
                        + pQueryType.ordinal()
                        + "-"
                        + (tAJAXParam.getmURLPart()).replaceAll("\\D+", "").substring(1)
                ))
                    tDepartmentMember.getSectionArray(pQueryType, tAJAXParam.getmVariableName()).add(new ScienceWork(tWork.select("a").first().attr("href")));
            }
        }
    }


    private  void parseScienceWorkPageByQueryType(QueryTypeAction pQueryType, DepartmentMember tMember) {
        ArrayList<Thread> mAsyncParseThreadArrayList = new ArrayList<>();
        tMember.mMemberInformationList.row(pQueryType).forEach((tSectionName, tScienceWorkArray)-> {
            mAsyncParseThreadArrayList.clear();
            tScienceWorkArray.forEach((tWork) -> {
                Thread tThread = new Thread(new AsyncPSUScienceWorkPageParseTask(tWork, pQueryType));
                mAsyncParseThreadArrayList.add(tThread);
                tThread.start();
            });
            mAsyncParseThreadArrayList.forEach(tThread ->{
                try {
                    tThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void parseScienceWorkPageByQueryType(QueryTypeAction pQueryType){
        if(pQueryType.equals(QueryTypeAction.ALL)){
            mBasicServiceCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((tName, tMember) -> {
                parseScienceWorkPageByQueryType(QueryTypeAction.Resource, tMember);
                parseScienceWorkPageByQueryType(QueryTypeAction.Document, tMember);
                parseScienceWorkPageByQueryType(QueryTypeAction.NIR, tMember);
                parseScienceWorkPageByQueryType(QueryTypeAction.RID, tMember);
            });
        }
        else
            mBasicServiceCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((tName, tMember) -> parseScienceWorkPageByQueryType(pQueryType, tMember));
    }

    public  void parseScienceWorkPageByQueryTypeAndSectionName(QueryTypeAction pQueryType, String pSectionName){
        ArrayList<Thread> mAsyncParseThreadArrayList = new ArrayList<>();
        mBasicServiceCompiler.mDepartmentMembersAssumer.mDepartmentMembersList.forEach((tName, tMember) ->{
            ArrayList tScienceWorkArray = tMember.mMemberInformationList.get(pQueryType, pSectionName);
            if(tScienceWorkArray != null) {
                mAsyncParseThreadArrayList.clear();
                tScienceWorkArray.forEach((tWork) -> {
                    Thread tThread = new Thread(new AsyncPSUScienceWorkPageParseTask((ScienceWork) tWork, pQueryType));
                    mAsyncParseThreadArrayList.add(tThread);
                    tThread.start();
                });
                mAsyncParseThreadArrayList.forEach(tThread ->{
                    try {
                        tThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}






























