package com.alexeykovzel.services;

import com.alexeykovzel.db.entities.term.TermDto;

import java.io.IOException;

interface Dictionary {
    TermDto getTermDto(String termValue) throws IOException;
}
