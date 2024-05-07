package com.peecko.one.service.specs;

import com.peecko.one.domain.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ApsOrderSpecs extends BaseSpecs {
    public static Specification<ApsOrder> agency(Long id) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            Join<ApsPlan, Customer> customerJoin = planJoin.join(ApsPlan_.CUSTOMER);
            Join<Customer, Agency> agencyJoin = customerJoin.join(Customer_.AGENCY);
            return cb.equal(agencyJoin.get(Agency_.ID), id);
        }));
    }

    public static Specification<ApsOrder> contractLike(String contract) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            return cb.like(planJoin.get(ApsPlan_.CONTRACT), anyText(contract));
        }));
    }

    public static Specification<ApsOrder> customerLike(String code) {
        return (((root, query, cb) -> {
            Join<ApsOrder, ApsPlan> planJoin = root.join(ApsOrder_.APS_PLAN);
            Join<ApsPlan, Customer> customerJoin = planJoin.join(ApsPlan_.CUSTOMER);
            return cb.like(customerJoin.get(Customer_.CODE), anyText(code));
        }));
    }

    public static Specification<ApsOrder> period(Integer period) {
        return (((root, query, cb) -> cb.equal(root.get(ApsOrder_.PERIOD), period)));
    }

    public static Specification<ApsOrder> starts(Integer period) {
        return (((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ApsOrder_.PERIOD), period)));
    }

    public static Specification<ApsOrder> ends(Integer period) {
        return (((root, query, cb) -> cb.lessThanOrEqualTo(root.get(ApsOrder_.PERIOD), period)));
    }

}
