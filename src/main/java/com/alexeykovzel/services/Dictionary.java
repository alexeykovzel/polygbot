package com.alexeykovzel.services;

import com.alexeykovzel.db.entities.Term;

import java.io.IOException;

interface Dictionary {
    Term.Details getTermDetails(String termValue) throws IOException;
}
