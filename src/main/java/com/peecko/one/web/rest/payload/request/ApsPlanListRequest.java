package com.peecko.one.web.rest.payload.request;

import com.peecko.one.domain.enumeration.PlanState;

import java.time.LocalDate;

public class ApsPlanListRequest {
    private Long agencyId;
    private String customer;
    private String contract;
    private PlanState state;
    private LocalDate starts;
    private LocalDate ends;

    public ApsPlanListRequest(Long agencyId, String customer, String contract, PlanState state, LocalDate start, LocalDate end) {
        this.agencyId = agencyId;
        this.customer = customer;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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
