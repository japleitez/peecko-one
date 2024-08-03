package com.peecko.one.service.request;

public class ApsOrderListRequest {
    private String contract;
    private String customer;
    private Integer period;
    private Integer starts;
    private Integer ends;

    public ApsOrderListRequest(String customer, String contract, Integer period, Integer starts, Integer ends) {
        this.customer = customer;
        this.contract = contract;
        this.period = period;
        this.starts = starts;
        this.ends = ends;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String  customer) {
        this.customer = customer;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getStarts() {
        return starts;
    }

    public void setStarts(Integer starts) {
        this.starts = starts;
    }

    public Integer getEnds() {
        return ends;
    }

    public void setEnds(Integer ends) {
        this.ends = ends;
    }

    @Override
    public String toString() {
        return "ApsOrderListRequest{" +
            "contract='" + contract + '\'' +
            ", customer=" + customer +
            ", period=" + period +
            ", starts=" + starts +
            ", ends=" + ends +
            '}';
    }
}
