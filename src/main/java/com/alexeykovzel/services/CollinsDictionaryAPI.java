package com.alexeykovzel.services;

import com.alexeykovzel.db.entities.term.TermDto;
import com.alexeykovzel.utils.Pair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollinsDictionaryAPI extends WebDictionary {

    @Override
    public TermDto getTermDto(String termValue) throws IOException {
        String searchQuery = "https://www.collinsdictionary.com/dictionary/english/" + toSearchForm(termValue);
        Element body = getPageBody(searchQuery);

        String origValue = body.getElementsByClass("orth").first().text();

        List<Pair<String, String>> defs = getDefs(body, 3);
        List<String> cases = getCases(body, 3);

        return new TermDto(origValue, searchQuery, defs, cases);
    }

    private List<Pair<String, String>> getDefs(Element body, int maxValue) {
        List<Pair<String, String>> defs = new ArrayList<>();
        Elements defElements = body.getElementsByClass("hom");

        if (!defElements.isEmpty()) {
            for (int i = 0; i < defElements.size(); i++) {
                if (i >= maxValue) {
                    break;
                }
                Element defSection = defElements.get(i);
                Element defElement = defSection.getElementsByClass("def").first();
                if (defElement != null) {
                    Element posElement = defSection.getElementsByClass("pos").first();
                    String pos = null;
                    if (posElement != null) {
                        pos = escapeMarkdown(posElement.text().toUpperCase());
                    }
                    defs.add(new Pair<>(pos, defElement.text()));
                }
            }
        }
        return defs;
    }

    private List<String> getCases(Element body, int maxValue) {
        List<String> cases = new ArrayList<>();
        Elements caseElements = body.getElementsByClass("quote");

        if (!caseElements.isEmpty()) {
            for (int i = 0; i < caseElements.size(); i++) {
                if (i >= maxValue) {
                    break;
                }
                cases.add(escapeMarkdown(caseElements.get(i).text()));
            }
        }
        return cases;
    }
}
