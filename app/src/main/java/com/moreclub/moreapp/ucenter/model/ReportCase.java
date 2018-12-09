package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Captain on 2017/6/2.
 */

public class ReportCase {

    public ReportCase(String name,boolean sel){
        reportName = name;
        select = sel;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private String reportName;
    private boolean select;
}
