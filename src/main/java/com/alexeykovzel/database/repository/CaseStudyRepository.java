package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.CaseStudy;
import com.alexeykovzel.database.entity.CaseStudyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseStudyRepository extends JpaRepository<CaseStudy, CaseStudyId> {
    List<CaseStudy> findAllByChatId(String chatId);

    List<CaseStudy> findAllByTermId(Long termId);
}
