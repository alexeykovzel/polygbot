package com.alexeykovzel.db.repositories;

import com.alexeykovzel.db.entities.Term;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByValue(String value);

    boolean existsByValue(String value);

    @EntityGraph(attributePaths = {"definitions", "examples"})
    Term findDetailedById(Long id);
}
