package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Agency_;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.Customer_;
import com.peecko.one.domain.enumeration.CustomerState;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecs extends BaseSpecs {

    public static Specification<Customer> agency(Long id) {
        return (((root, query, cb) -> {
            Join<Customer, Agency> agencyJoin = root.join(Customer_.AGENCY);
            return cb.equal(agencyJoin.get(Agency_.id), id);
        }));
    }

    public static Specification<Customer> codeLike(String code) {
        return ((root, query, cb) ->
            code == null?
                cb.conjunction():
                cb.like(root.get(Customer_.CODE), anyText(code))
            );
    }

    public static Specification<Customer> nameLike(String name) {
        return ((root, query, cb) ->
            name == null?
                cb.conjunction():
                cb.like(root.get(Customer_.NAME), anyText(name))
            );
    }

    public static Specification<Customer> licenseLike(String license) {
        return ((root, query, cb) ->
            license == null?
                cb.conjunction():
                cb.like(root.get(Customer_.LICENSE), anyText(license))
        );
    }

    public static Specification<Customer> stateEqual(CustomerState state) {
        return ((root, query, cb) ->
            state == null?
                cb.conjunction():
                cb.equal(root.get(Customer_.state), state)
        );
    }

}
