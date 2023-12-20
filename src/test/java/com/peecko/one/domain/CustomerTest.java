package com.peecko.one.domain;

import static com.peecko.one.domain.AgencyTestSamples.*;
import static com.peecko.one.domain.ApsPlanTestSamples.*;
import static com.peecko.one.domain.ContactTestSamples.*;
import static com.peecko.one.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void contactTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        customer.addContact(contactBack);
        assertThat(customer.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getCustomer()).isEqualTo(customer);

        customer.removeContact(contactBack);
        assertThat(customer.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getCustomer()).isNull();

        customer.contacts(new HashSet<>(Set.of(contactBack)));
        assertThat(customer.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getCustomer()).isEqualTo(customer);

        customer.setContacts(new HashSet<>());
        assertThat(customer.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getCustomer()).isNull();
    }

    @Test
    void apsPlanTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        ApsPlan apsPlanBack = getApsPlanRandomSampleGenerator();

        customer.addApsPlan(apsPlanBack);
        assertThat(customer.getApsPlans()).containsOnly(apsPlanBack);
        assertThat(apsPlanBack.getCustomer()).isEqualTo(customer);

        customer.removeApsPlan(apsPlanBack);
        assertThat(customer.getApsPlans()).doesNotContain(apsPlanBack);
        assertThat(apsPlanBack.getCustomer()).isNull();

        customer.apsPlans(new HashSet<>(Set.of(apsPlanBack)));
        assertThat(customer.getApsPlans()).containsOnly(apsPlanBack);
        assertThat(apsPlanBack.getCustomer()).isEqualTo(customer);

        customer.setApsPlans(new HashSet<>());
        assertThat(customer.getApsPlans()).doesNotContain(apsPlanBack);
        assertThat(apsPlanBack.getCustomer()).isNull();
    }

    @Test
    void agencyTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        customer.setAgency(agencyBack);
        assertThat(customer.getAgency()).isEqualTo(agencyBack);

        customer.agency(null);
        assertThat(customer.getAgency()).isNull();
    }
}
