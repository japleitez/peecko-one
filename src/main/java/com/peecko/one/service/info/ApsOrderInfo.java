package com.peecko.one.service.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;

public class ApsOrderInfo {
    private final Long id;
    private final Integer period;
    private final String license;
    private final Double unitPrice;
    private final Double vatRate;
    private final Integer numberOfUsers;
    private final String invoiceNumber;
    private final String contract;
    private final String pricing;
    @JsonProperty("plnId")
    private final Long apsPlanId;
    @JsonProperty("plnState")
    private final String planState;
    @JsonProperty("cstName")
    private final String customer;
    @JsonProperty("cstState")
    private final String customerState;

    public static ApsOrderInfo of(ApsOrder apsOrder) {
        return ApsOrderInfo.of(apsOrder, apsOrder.getApsPlan());
    }

    public static ApsOrderInfo of(ApsOrder apsOrder, ApsPlan apsPlan) {
        String planState = null;
        String customer = null;
        String customerState = null;
        if(apsPlan.getState()!= null) {
            planState = apsPlan.getState().name();
        }
        if(apsPlan.getCustomer() != null) {
            customer = apsPlan.getCustomer().getName();
            if(apsPlan.getCustomer().getState() != null) {
                customerState = apsPlan.getCustomer().getState().name();
            }
        }
        return new ApsOrderInfo(
            apsOrder.getId(),
            apsOrder.getPeriod(),
            apsOrder.getLicense(),
            apsOrder.getUnitPrice(),
            apsOrder.getVatRate(),
            apsOrder.getNumberOfUsers(),
            apsOrder.getInvoiceNumber(),
            apsPlan.getContract(),
            apsPlan.getPricing().name(),
            apsPlan.getId(),
            planState,
            customer,
            customerState
        );
    }

    private ApsOrderInfo(Long id, Integer period, String license, Double unitPrice, Double vatRate, Integer numberOfUsers, String invoiceNumber, String contract, String pricing, Long apsPlanId, String planState, String customer, String customerState) {
        this.id = id;
        this.period = period;
        this.license = license;
        this.unitPrice = unitPrice;
        this.vatRate = vatRate;
        this.numberOfUsers = numberOfUsers;
        this.invoiceNumber = invoiceNumber;
        this.contract = contract;
        this.pricing = pricing;
        this.apsPlanId = apsPlanId;
        this.planState = planState;
        this.customer = customer;
        this.customerState = customerState;
    }

    public Long getId() {
        return id;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getLicense() {
        return license;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getContract() {
        return contract;
    }

    public String getPricing() {
        return pricing;
    }

    @JsonProperty("plnId")
    public Long getApsPlanId() { return apsPlanId; }

    @JsonProperty("plnState")
    public String getPlanState() {
        return planState;
    }

    @JsonProperty("cstName")
    public String getCustomer() {
        return customer;
    }

    @JsonProperty("cstState")
    public String getCustomerState() {
        return customerState;
    }
}
