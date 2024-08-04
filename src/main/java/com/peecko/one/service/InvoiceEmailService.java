package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.AgencyRepository;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.info.ApsOrderInfo;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceEmailService {
    private final UserService userService;
    private final EmailService emailService;
    private final AgencyRepository agencyRepository;
    private final ApsOrderRepository apsOrderRepository;
    private final CustomerRepository customerRepository;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;

    public InvoiceEmailService(UserService userService, EmailService emailService, AgencyRepository agencyRepository, ApsOrderRepository apsOrderRepository, CustomerRepository customerRepository, TemplateEngine templateEngine, MessageSource messageSource) {
        this.userService = userService;
        this.emailService = emailService;
        this.agencyRepository = agencyRepository;
        this.apsOrderRepository = apsOrderRepository;
        this.customerRepository = customerRepository;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    public List<ApsOrderInfo> batchInvoiceEmail(String contract, Integer period) {
        List<ApsOrder> orders;
        Long agencyId = userService.getCurrentAgencyId();;
        Agency agency = agencyRepository.getReferenceById(agencyId);
        if (StringUtils.hasText(contract)) {
            orders = apsOrderRepository.getByContactAndPeriod(contract, period);
        } else {
            orders = apsOrderRepository.getByAgencyAndPeriod(agencyId, period);
        }
        return orders.stream().map(order -> sendEmail(agency, order)).collect(Collectors.toList());
    }

    private ApsOrderInfo sendEmail(Agency agency, ApsOrder order) {
        Customer customer = customerRepository.getReferenceById(order.getCustomerId());
        String from = agency.getBillingEmail();
        String to = customer.getBillingEmail();
        String subject = resolveSubject(agency, order);
        String text = resolveText(agency, order, customer);
        String attachmentPath = order.getInvoice().getFilename();
        boolean sent = emailService.sendEmail(from, to, subject, text, attachmentPath);
        if (sent) {
            order.setInvoiceSent(true);
            order.setInvoiceSentAt(Instant.now());
            apsOrderRepository.save(order);
        }
        return ApsOrderInfo.of(order);
    }

    private String resolveSubject(Agency agency, ApsOrder order) {
        Locale locale = new Locale(agency.getLanguage().name());
        Object[] args = new Object[] {agency.getName(), order.getPeriod(), order.getInvoiceNumber()};
        // messageSource.getMessage("invoice.email.subject", args, locale);
        return agency.getName() + " your invoice " + order.getPeriod() + " - " + order.getInvoiceNumber();
    }

    private String resolveText(Agency agency, ApsOrder order, Customer customer) {
        Context context = new Context();
        context.setVariables(buildInvoiceTemplateModel(agency, order, customer));
        return templateEngine.process("invoice_email.html", context);
    }

    private Map<String, Object> buildInvoiceTemplateModel(Agency agency, ApsOrder order, Customer customer) {
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
