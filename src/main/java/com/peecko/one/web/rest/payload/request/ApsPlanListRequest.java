package com.peecko.one.web.rest.payload.request;

import com.peecko.one.domain.enumeration.PlanState;

import java.time.LocalDate;

public class ApsPlanListRequest {
    private Long agencyId;
    private String customerCode;
    private String contract;
    private PlanState state;
    private LocalDate starts;
    private LocalDate ends;

    public ApsPlanListRequest(Long agencyId, String customerCode, String contract, PlanState state, LocalDate start, LocalDate end) {
        this.agencyId = agencyId;
        this.customerCode = customerCode;
        this.contract = contract;
        this.state = state;
        this.starts = start;
        this.ends = end;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public PlanState getState() {
        return state;
    }

    public void setState(PlanState state) {
        this.state = state;
    }

    public LocalDate getStarts() {
        return starts;
    }

    public void setStart(LocalDate starts) {
        this.starts = starts;
    }

    public LocalDate getEnds() {
        return ends;
    }

    public void setEnd(LocalDate ends) {
        this.ends = ends;
    }
}
