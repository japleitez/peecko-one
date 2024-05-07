package com.peecko.one.service;

import com.peecko.one.domain.Contact;
import com.peecko.one.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Optional<Contact> loadById(Long id) {
        List<Contact> list = contactRepository.loadById(id);
        return list.isEmpty()? Optional.empty(): Optional.of(list.get(0));
    }

    public boolean notFound(Long id) {
        return !contactRepository.existsById(id);
    }

    public Contact create(Contact contact) {
        contact.setCreated(Instant.now());
        return contactRepository.save(contact);
    }

    public Contact update(Contact contact) {
        contact.setUpdated(Instant.now());
        return contactRepository.save(contact);
    }

    public void delete(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
        }
    }

    public List<Contact> getContactsByCode(String customerCode) {
        if (Objects.nonNull(customerCode)) {
            return contactRepository.getContactsByCustomerCode(customerCode);
        } else {
            return contactRepository.findAll();
        }
    }
    public Optional<Contact> partialUpdateContact(Contact contact) {
        return contactRepository
            .findById(contact.getId())
            .map(existingContact -> {
                if (contact.getType() != null) {
                    existingContact.setType(contact.getType());
                }
                if (contact.getName() != null) {
                    existingContact.setName(contact.getName());
                }
                if (contact.getLine1() != null) {
                    existingContact.setLine1(contact.getLine1());
                }
                if (contact.getLine2() != null) {
                    existingContact.setLine2(contact.getLine2());
                }
                if (contact.getZip() != null) {
                    existingContact.setZip(contact.getZip());
                }
                if (contact.getCity() != null) {
                    existingContact.setCity(contact.getCity());
                }
                if (contact.getCountry() != null) {
                    existingContact.setCountry(contact.getCountry());
                }
                if (contact.getEmail() != null) {
                    existingContact.setEmail(contact.getEmail());
                }
                if (contact.getPhone() != null) {
                    existingContact.setPhone(contact.getPhone());
                }
                if (contact.getNotes() != null) {
                    existingContact.setNotes(contact.getNotes());
                }
                if (contact.getCreated() != null) {
                    existingContact.setCreated(contact.getCreated());
                }
                if (contact.getUpdated() != null) {
                    existingContact.setUpdated(contact.getUpdated());
                }

                return existingContact;
            })
            .map(contactRepository::save);
    }
}
