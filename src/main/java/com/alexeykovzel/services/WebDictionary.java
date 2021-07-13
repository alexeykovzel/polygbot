package com.alexeykovzel.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

abstract class WebDictionary implements Dictionary {

    Element getPageBody(String searchQuery) throws IOException {
        return Jsoup.connect(searchQuery).get();
    }
    
    String toSearchForm(String text) {
        return text.toLowerCase().replace(" ", "-");
    }

    String escapeMarkdown(String text) {
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`");
    }
}
