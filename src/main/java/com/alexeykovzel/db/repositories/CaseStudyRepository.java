package com.alexeykovzel.db.repositories;

import com.alexeykovzel.db.entities.CaseStudy;
import com.alexeykovzel.db.entities.CaseStudyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseStudyRepository extends JpaRepository<CaseStudy, CaseStudyId> {
    List<CaseStudy> findAllByChatId(String chatId);

    List<CaseStudy> findAllByTermId(Long termId);
}
