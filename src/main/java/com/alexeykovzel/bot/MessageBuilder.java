package com.alexeykovzel.bot;

import com.alexeykovzel.db.entities.term.TermDto;
import com.alexeykovzel.utils.Pair;

import java.util.List;

public class MessageBuilder {
    public static StringBuilder buildTermInfoMessage(TermDto termDto) {
        StringBuilder response = new StringBuilder();

        // Add 'definitions' section
        response.append(getDefSection(termDto.getDefs(), 2));

        // Add 'examples' section
        response.append(getExamplesSection(termDto.getCases(), 4));

        // Add 'more' section
        response.append("[More](").append(termDto.getLink()).append(") about '*").append(termDto.getValue()).append("*'");

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
                String value = def.getSecond();
                if (pos != null) {
                    defSection.append(String.format("*[%s]* %s\n\n", pos, value));
                } else {
                    defSection.append(value);
                }
            }
        }
        return defSection;
    }

    private static StringBuilder getExamplesSection(List<String> termCases, int maxValue) {
        StringBuilder examplesSection = new StringBuilder();

        if (!termCases.isEmpty()) {
            examplesSection.append("*EXAMPLES*\n\n");
            StringBuilder examples = new StringBuilder();
            for (int i = 0; i < termCases.size(); i++) {
                if (i == maxValue) {
                    break;
                }
                examples.append("- ").append(termCases.get(i)).append("\n");
            }
            examplesSection.append(examples).append("\n");
        }
        return examplesSection;
    }
}
