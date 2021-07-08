package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByValue(String value);
}
