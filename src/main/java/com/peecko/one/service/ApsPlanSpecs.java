package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.domain.enumeration.PlanState;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

public class ApsPlanSpecs extends BaseSpecs {

    public static Specification<ApsPlan> agencyId(Long id) {
        return (((root, query, cb) -> {
            if (Objects.nonNull(id)) {
                Join<ApsPlan, Customer> customerJoin = root.join(ApsPlan_.CUSTOMER);
                Join<Customer, Agency> agencyJoin = customerJoin.join(Customer_.AGENCY);
                return cb.equal(agencyJoin.get(Agency_.ID), id);
            } else {
                return cb.conjunction();
            }
        }));
    }

    public static Specification<ApsPlan> customerCode(String code) {
        return (((root, query, cb) -> {
            if (StringUtils.hasText(code)) {
                Join<ApsPlan, Customer> customerJoin = root.join(ApsPlan_.CUSTOMER);
                return cb.like(customerJoin.get(Customer_.code), anyText(code));
            } else {
                return cb.conjunction();
            }
        }));
    }

    public static Specification<ApsPlan> contract(String contract) {
        return (((root, query, cb) ->
            Objects.isNull(contract)?
                cb.conjunction():
                cb.like(root.get(ApsPlan_.CONTRACT), anyText(contract))));
    }

    public static Specification<ApsPlan> state(PlanState state) {
        return (((root, query, cb) ->
            Objects.isNull(state)?
                cb.conjunction():
                cb.equal(root.get(ApsPlan_.STATE), state)));
    }

    public static Specification<ApsPlan> starts(LocalDate starts) {
        return (((root, query, cb) ->
            Objects.isNull(starts)?
                cb.conjunction():
                cb.greaterThanOrEqualTo(root.get(ApsPlan_.STARTS), starts)));
    }

    public static Specification<ApsPlan> ends(LocalDate ends) {
        return (((root, query, cb) ->
            Objects.isNull(ends)?
                cb.conjunction():
                cb.lessThanOrEqualTo(root.get(ApsPlan_.ENDS), ends)));
    }

}
