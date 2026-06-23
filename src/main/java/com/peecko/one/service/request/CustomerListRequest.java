package com.peecko.one.service.request;

import com.peecko.one.domain.enumeration.CustomerState;

public class CustomerListRequest {
    private String code;
    private String name;
    private CustomerState state;

    public CustomerListRequest() {
    }

    public CustomerListRequest(String code, String name, CustomerState state) {
        this.code = code;
        this.name = name;
        this.state = state;
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
