package com.peecko.one.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.peecko.one.domain.ApsMembership;
import com.peecko.one.domain.ApsOrder;
import com.peecko.one.repository.ApsMembershipRepository;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.domain.dto.MemberDTO;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Service
public class ApsMembershipService {
    private final ApsMembershipRepository apsMembershipRepository;

    private final ApsOrderRepository apsOrderRepository;

    public ApsMembershipService(ApsMembershipRepository apsMembershipRepository, ApsOrderRepository apsOrderRepository) {
        this.apsMembershipRepository = apsMembershipRepository;
        this.apsOrderRepository = apsOrderRepository;
    }

    public int importMembers(Long apsOrderId, MultipartFile file) {
        ApsOrder apsOrder = apsOrderRepository.findById(apsOrderId).orElseThrow(() -> new BadRequestAlertException("Invalid Order Id", "apsOrder", "orderId"));
        List<MemberDTO> members = parseFile(file);
        List<ApsMembership> result = saveOrUpdateMembers(apsOrder.getId(), apsOrder.getPeriod(), apsOrder.getLicense(), members);
        return result.size();
    }

    public List<ApsMembership> saveOrUpdateMembers(Long apsOrderId, Integer period, String license, List<MemberDTO> members) {
        List<ApsMembership> list = members.stream().map(m -> saveOrUpdateMember(apsOrderId, period, license, m)).toList();
        ApsOrder apsOrder = apsOrderRepository.findById(apsOrderId).orElseThrow(() -> new BadRequestAlertException("Invalid Order Id", "apsOrder", "orderId"));
        Long count = apsMembershipRepository.countByApsOrder(apsOrder);
        apsOrder.setNumberOfUsers(count.intValue());
        apsOrderRepository.save(apsOrder);
        return list;
    }

    private ApsMembership saveOrUpdateMember(Long apsOrderId, Integer period, String license, MemberDTO member) {
        ApsOrder apsOrder = new ApsOrder(apsOrderId);
        ApsMembership apsMembership = apsMembershipRepository.findByApsOrderAndUsername(apsOrder, member.getUsername())
            .orElse(ApsMembership.of(apsOrder, period, license, member.getUsername()));
        if (apsMembership.getId() == null) {
            apsMembershipRepository.save(apsMembership);
        } else if (!apsMembership.getLicense().equals(license)){
            apsMembership.setLicense(license);
            apsMembershipRepository.save(apsMembership);
        }
        return apsMembership;
    }

    public List<MemberDTO> parseFile(final MultipartFile file) {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        try (InputStream is = new BufferedInputStream(file.getInputStream())) {
            MappingIterator<MemberDTO> rows = mapper.reader(MemberDTO.class).with(bootstrapSchema).readValues(is);
            return rows.readAll();
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to parse file to ApsMemberships", ioe);
        }
    }
}
