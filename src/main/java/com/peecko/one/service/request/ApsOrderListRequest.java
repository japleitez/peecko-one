package com.peecko.one.service.request;

public class ApsOrderListRequest {

    private Long agencyId;
    private String apsPlanContract;
    private Long customerId;
    private Integer period;
    private Integer startPeriod;
    private Integer endPeriod;

    public ApsOrderListRequest(Long agencyId) {
        this.agencyId = agencyId;
    }

    public ApsOrderListRequest(Long agencyId, String apsPlanContract, Long customerId, Integer period, Integer startPeriod, Integer endPeriod) {
        this.agencyId = agencyId;
        this.apsPlanContract = apsPlanContract;
        this.customerId = customerId;
        this.period = period;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public String getApsPlanContract() {
        return apsPlanContract;
    }

    public void setApsPlanContract(String apsPlanContract) {
        this.apsPlanContract = apsPlanContract;
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

    public Integer getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Integer getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Integer endPeriod) {
        this.endPeriod = endPeriod;
    }
}
