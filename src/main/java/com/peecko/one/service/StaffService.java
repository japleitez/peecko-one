package com.peecko.one.service;

import com.peecko.one.domain.Staff;
import com.peecko.one.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Optional<Staff> loadById(Long id) {
        List<Staff> list = staffRepository.loadById(id);
        return list.isEmpty()? Optional.empty(): Optional.of(list.get(0));
    }

}
