package com.peecko.one.web.rest.payload.request;

import java.time.LocalDate;

public class ActivateTrialPlanRequest {
    private Long customerId;
    private LocalDate start;
    private LocalDate ends;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnds() {
        return ends;
    }

    public void setEnds(LocalDate ends) {
        this.ends = ends;
    }
}
