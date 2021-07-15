package com.alexeykovzel.service;

import com.alexeykovzel.db.entity.term.TermDto;

import java.io.IOException;

interface Dictionary {
    TermDto getTerm(String termValue) throws IOException;

    String getName();
}
