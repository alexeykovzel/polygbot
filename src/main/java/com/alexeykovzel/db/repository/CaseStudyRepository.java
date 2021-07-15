package com.alexeykovzel.db.repository;

import com.alexeykovzel.db.entity.CaseStudy;
import com.alexeykovzel.db.entity.CaseStudyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseStudyRepository extends JpaRepository<CaseStudy, CaseStudyId> {
    List<CaseStudy> findAllByChatId(String chatId);

    List<CaseStudy> findAllByTermId(Long termId);
}
