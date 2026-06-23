package com.peecko.one.service.request;

public class InvoiceListRequest {

    private Long customerId;
    private Integer starts;
    private Integer ends;
    private String number;
    private boolean unpaid;

    public InvoiceListRequest(Long customerId, Integer starts, Integer ends, String number, boolean unpaid) {
        this.customerId = customerId;
        this.starts = starts;
        this.ends = ends;
        this.number = number;
        this.unpaid = unpaid;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Integer getStarts() {
        return starts;
    }

    public Integer getEnds() {
        return ends;
    }

    public String getNumber() {
        return number;
    }

    public boolean isUnpaid() {
        return unpaid;
    }
}
