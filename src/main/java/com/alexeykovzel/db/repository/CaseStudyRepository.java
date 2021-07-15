package com.alexeykovzel.db.repository;

import com.alexeykovzel.db.entity.CaseStudy;
import com.alexeykovzel.db.entity.CaseStudyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseStudyRepository extends JpaRepository<CaseStudy, CaseStudyId> {
    List<CaseStudy> findAllByChatId(String chatId);

    List<CaseStudy> findAllByTermId(Long termId);

    @Query("SELECT t.value FROM Term t JOIN CaseStudy cs ON t.id = cs.termId WHERE cs.chatId = :chatId ORDER BY t.value ASC")
    Optional<List<String>> findAllTermValuesByChatId(@Param("chatId") String chatId);
}
