package com.alexeykovzel.services;

import com.alexeykovzel.db.entities.Term;
import kotlin.Pair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollinsDictionaryAPI extends WebDictionary {

    @Override
    public Term.Details getTermDetails(String termValue) throws IOException {
        String searchQuery = "https://www.collinsdictionary.com/dictionary/english/" + toSearchForm(termValue);
        Element body = getPageBody(searchQuery);

        String origValue = body.getElementsByClass("orth").first().text();

        List<Pair<String, String>> definitions = getDefinitions(body, 3);
        List<String> examples = getExamples(body, 3);

        return Term.Details.builder()
                .value(origValue)
                .link(searchQuery)
                .definitions(definitions)
                .examples(examples).build();
    }

    private List<Pair<String, String>> getDefinitions(Element body, int maxValue) {
        List<Pair<String, String>> definitions = new ArrayList<>();
        Elements defElementsList = body.getElementsByClass("hom");

        if (!defElementsList.isEmpty()) {
            for (int i = 0; i < defElementsList.size(); i++) {
                if (i >= maxValue) {
                    break;
                }
                Element defSection = defElementsList.get(i);
                Element defElement = defSection.getElementsByClass("def").first();
                if (defElement != null) {
                    Element posElement = defSection.getElementsByClass("pos").first();
                    String pos = null;
                    if (posElement != null) {
                        pos = escapeMarkdown(posElement.text().toUpperCase());
                    }
                    definitions.add(new Pair<>(pos, defElement.text()));
                }
            }
        }
        return definitions;
    }

    private List<String> getExamples(Element body, int maxValue) {
        List<String> examples = new ArrayList<>();
        Elements examplesList = body.getElementsByClass("quote");

        if (!examplesList.isEmpty()) {
            for (int i = 0; i < examplesList.size(); i++) {
                if (i >= maxValue) {
                    break;
                }
                examples.add(escapeMarkdown(examplesList.get(i).text()));
            }
        }
        return examples;
    }
}
