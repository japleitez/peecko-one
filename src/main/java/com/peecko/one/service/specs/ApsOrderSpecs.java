package com.peecko.one.service.specs;

import com.peecko.one.domain.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ApsOrderSpecs extends BaseSpecs {
    public static Specification<ApsOrder> agency(Long id) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            Join<ApsPlan, Customer> customerJoin = planJoin.join(ApsPlan_.CUSTOMER);
            Join<Customer, Agency> agencyJoin = customerJoin.join(Customer_.AGENCY);
            return cb.equal(agencyJoin.get(Agency_.ID), id);
        }));
    }

    public static Specification<ApsOrder> contract(String contract) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            return cb.equal(planJoin.get(ApsPlan_.CONTRACT), contract);
        }));
    }

    public static Specification<ApsOrder> customer(Long id) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            Join<ApsPlan, Customer> customerJoin = planJoin.join(ApsPlan_.CUSTOMER);
            return cb.equal(customerJoin.get(Customer_.ID), id);
        }));
    }

    public static Specification<ApsOrder> period(Integer period) {
        return (((root, query, cb) -> cb.equal(root.get(ApsOrder_.PERIOD), period)));
    }

    public static Specification<ApsOrder> startPeriod(Integer period) {
        return (((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ApsOrder_.PERIOD), period)));
    }

    public static Specification<ApsOrder> endPeriod(Integer period) {
        return (((root, query, cb) -> cb.lessThanOrEqualTo(root.get(ApsOrder_.PERIOD), period)));
    }

}
