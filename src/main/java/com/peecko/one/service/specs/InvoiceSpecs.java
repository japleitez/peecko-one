package com.peecko.one.service.specs;

import com.peecko.one.domain.Invoice;
import com.peecko.one.domain.Invoice_;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceSpecs extends BaseSpecs {

    public static Specification<Invoice> agencyId(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Invoice_.AGENCY_ID), id);
    }

    public static Specification<Invoice> customerId(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Invoice_.CUSTOMER_ID), id);
    }

    public static Specification<Invoice> starts(Integer period) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Invoice_.PERIOD), period);
    }

    public static Specification<Invoice> ends(Integer period) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Invoice_.PERIOD), period);
    }

    public static Specification<Invoice> numberLike(String number) {
        return (root, query, cb) -> cb.like(root.get(Invoice_.NUMBER), anyText(number));
    }

    public static Specification<Invoice> unpaid() {
        return (root, query, cb) -> cb.or(cb.isNull(root.get(Invoice_.PAID)), cb.greaterThan(root.get(Invoice_.DIFF), 0.0));
    }
}
