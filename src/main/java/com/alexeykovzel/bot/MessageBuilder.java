package com.alexeykovzel.bot;

import com.alexeykovzel.db.entities.Term;
import kotlin.Pair;

import java.util.List;

public class MessageBuilder {
    public static StringBuilder buildTermInfoMessage(Term.Details details) {
        StringBuilder response = new StringBuilder();

        // Add 'definitions' section
        response.append(getDefSection(details.getDefinitions(), 2));

        // Add 'examples' section
        response.append(getExamplesSection(details.getExamples(), 4));

        // Add 'more' section
        response.append("[More](").append(details.getLink()).append(") about '*").append(details.getValue()).append("*'");

        return response;
    }

    private static StringBuilder getDefSection(List<Pair<String, String>> defList, int maxValue) {
        StringBuilder defSection = new StringBuilder();
        if (!defList.isEmpty()) {
            for (int i = 0; i < defList.size(); i++) {
                if (i == maxValue) {
                    break;
                }
                Pair<String, String> def = defList.get(i);
                String pos = def.getFirst();
                if (pos != null) {
                    defSection.append(String.format("*[%s]* %s\n\n", pos, def.getSecond()));
                }
            }
        }
        return defSection;
    }

    private static StringBuilder getExamplesSection(List<String> examplesList, int maxValue) {
        StringBuilder examplesSection = new StringBuilder();

        if (!examplesList.isEmpty()) {
            examplesSection.append("*EXAMPLES*\n\n");
            StringBuilder examples = new StringBuilder();
            for (int i = 0; i < examplesList.size(); i++) {
                if (i == maxValue) {
                    break;
                }
                examples.append("- ").append(examplesList.get(i)).append("\n");
            }
            examplesSection.append(examples).append("\n");
        }
        return examplesSection;
    }
}
