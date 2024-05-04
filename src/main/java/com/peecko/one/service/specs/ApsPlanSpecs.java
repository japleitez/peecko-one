package com.peecko.one.service.specs;

import com.peecko.one.domain.*;
import com.peecko.one.domain.enumeration.PlanState;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ApsPlanSpecs extends BaseSpecs {

    public static Specification<ApsPlan> agencyId(Long id) {
        return (((root, query, cb) -> {
            Join<ApsPlan, Customer> customerJoin = root.join(ApsPlan_.CUSTOMER);
            Join<Customer, Agency> agencyJoin = customerJoin.join(Customer_.AGENCY);
            return cb.equal(agencyJoin.get(Agency_.ID), id);
        }));
    }

    public static Specification<ApsPlan> customerCode(String code) {
        return (((root, query, cb) -> {
            Join<ApsPlan, Customer> customerJoin = root.join(ApsPlan_.CUSTOMER);
            return cb.like(customerJoin.get(Customer_.code), anyText(code));
        }));
    }

    public static Specification<ApsPlan> contract(String contract) {
        return (((root, query, cb) ->
            cb.like(root.get(ApsPlan_.CONTRACT), anyText(contract))));
    }

    public static Specification<ApsPlan> state(PlanState state) {
        return (((root, query, cb) ->
            cb.equal(root.get(ApsPlan_.STATE), state)));
    }

    public static Specification<ApsPlan> starts(LocalDate starts) {
        return (((root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(ApsPlan_.STARTS), starts)));
    }

    public static Specification<ApsPlan> ends(LocalDate ends) {
        Specification<ApsPlan> nullEnd =
            ((root, query, cb) -> cb.isNotNull(root.get(ApsPlan_.ENDS)));
        Specification<ApsPlan> lessThan =
            ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(ApsPlan_.ENDS), ends));
        return nullEnd.and(lessThan);
    }

}
