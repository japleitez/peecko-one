package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.AgencyRepository;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class InvoiceEmailService {
    private final EmailService emailService;
    private final AgencyRepository agencyRepository;
    private final ApsOrderRepository apsOrderRepository;
    private final CustomerRepository customerRepository;
    private final TemplateEngine templateEngine;

    public InvoiceEmailService(EmailService emailService, AgencyRepository agencyRepository, ApsOrderRepository apsOrderRepository, CustomerRepository customerRepository, TemplateEngine templateEngine) {
        this.emailService = emailService;
        this.agencyRepository = agencyRepository;
        this.apsOrderRepository = apsOrderRepository;
        this.customerRepository = customerRepository;
        this.templateEngine = templateEngine;
    }

    public void batchInvoiceEmail(String contract, Integer period) {
        if (StringUtils.hasText(contract)) {
            apsOrderRepository.getByContactAndPeriod(contract, period).forEach(this::sendEmail);
        } else {
            //TODO batch for period
            Long agencyId = SecurityUtils.getCurrentAgencyId();
            apsOrderRepository.getByAgencyAndPeriod(agencyId, period).forEach(this::sendEmail);
        }
    }

    private void sendEmail(ApsOrder order) {
        Agency agency = agencyRepository.getReferenceById(SecurityUtils.getCurrentAgencyId());
        Customer customer = customerRepository.getReferenceById(order.getCustomerId());
        String from = agency.getBillingEmail();
        String to = resolveTo(order);
        String subject = resolveSubject(agency, order);
        String text = resolveText(agency, order, customer);
        String attachmentPath = resolveAttachmentPath(order);
        boolean sent = emailService.sendEmail(from, to, subject, text, attachmentPath);
        if (sent) {
            order.setInvoiceSent(true);
            order.setInvoiceSentAt(Instant.now());
            apsOrderRepository.save(order);
        }
    }

    private String resolveTo(ApsOrder order) {
        return "";
    }

    private String resolveSubject(Agency agency, ApsOrder order) {
        return String.format("%s your %s invoice %s", agency.getName(), order.getPeriod(), order.getInvoiceNumber());
    }

    private String resolveText(Agency agency, ApsOrder order, Customer customer) {
        Context context = new Context();
        context.setVariables(buildTemplateModel(agency, order, customer));
        return templateEngine.process("invoice_template", context);
    }

    private String resolveAttachmentPath(ApsOrder order) {
        return "";
    }

    private Map<String, Object> buildTemplateModel(Agency agency, ApsOrder order, Customer customer) {
        Invoice invoice = order.getInvoice();
        Map<String, Object> data = new HashMap<>();
        data.put("number", invoice.getNumber());
        data.put("issued", invoice.getIssued());
        data.put("total", invoice.getTotal());
        data.put("dueDate", invoice.getDueDate());
        data.put("clientName", customer.getName());
        data.put("agencyName", agency.getName());
        data.put("supportEmail", agency.getBillingEmail());
        return data;
    }

}
