package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.CaseStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaseStudyRepository extends JpaRepository<CaseStudy, String> {
    List<CaseStudy> findAllByChatId(String chatId);
}