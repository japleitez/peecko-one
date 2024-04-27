package com.peecko.one.web.rest.payload.request;

import com.peecko.one.domain.enumeration.PlanState;

public class ApsPlanListRequest {
    private Long agencyId;
    private String customer;
    private String contract;
    private PlanState state;

    public ApsPlanListRequest(Long agencyId, String customer, String contract, PlanState state) {
        this.agencyId = agencyId;
        this.customer = customer;
        this.contract = contract;
        this.state = state;
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
}
