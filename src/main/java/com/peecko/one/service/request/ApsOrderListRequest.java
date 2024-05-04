package com.peecko.one.service.request;

public class ApsOrderListRequest {

    private Long agencyId;
    private String contract;
    private Long customerId;
    private Integer period;
    private Integer starts;
    private Integer ends;

    public ApsOrderListRequest(Long agencyId) {
        this.agencyId = agencyId;
    }

    public ApsOrderListRequest(Long agencyId, Long customerId, String contract, Integer period, Integer starts, Integer ends) {
        this.agencyId = agencyId;
        this.customerId = customerId;
        this.contract = contract;
        this.period = period;
        this.starts = starts;
        this.ends = ends;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
            "agencyId=" + agencyId +
            ", contract='" + contract + '\'' +
            ", customerId=" + customerId +
            ", period=" + period +
            ", starts=" + starts +
            ", ends=" + ends +
            '}';
    }
}
