package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Term;
import org.springframework.data.repository.CrudRepository;

public interface TermRepository extends CrudRepository<Term, Integer> {
}
