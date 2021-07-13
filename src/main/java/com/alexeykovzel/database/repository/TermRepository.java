package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Term;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByValue(String value);

    boolean existsByValue(String value);

    @EntityGraph(attributePaths = {"definitions", "examples"})
    Term findDetailedById(Long id);
}
