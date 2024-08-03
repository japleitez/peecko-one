package com.peecko.one.service.request;

import com.peecko.one.domain.enumeration.CustomerState;

public class CustomerListRequest {

    private Long agencyId;
    private String code;
    private String name;
    private CustomerState state;

    public CustomerListRequest() {
    }

    public CustomerListRequest(Long agencyId, String code, String name, CustomerState state) {
        this.agencyId = agencyId;
        this.code = code;
        this.name = name;
        this.state = state;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }
}
