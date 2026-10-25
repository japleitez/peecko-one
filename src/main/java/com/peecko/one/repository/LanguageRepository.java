package com.peecko.one.repository;

import com.peecko.one.domain.Language;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByActiveTrueOrderByIdAsc();

    boolean existsByCodeIgnoreCase(String code);
}
