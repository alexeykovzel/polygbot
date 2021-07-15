package com.alexeykovzel.db.entities.term;

import com.alexeykovzel.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class TermDto {
    private String value;
    private String link;
    private List<Pair<String, String>> defs;
    private List<String> cases;
}