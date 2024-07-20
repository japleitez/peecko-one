package com.peecko.one.domain.enumeration;

import java.util.List;

/**
 * The PlanState enumeration.
 */
public enum PlanState {
    TRIAL,
    ACTIVE,
    CLOSED;
    public static final List<PlanState> TRIAL_ACTIVE = List.of(TRIAL, ACTIVE);
}
