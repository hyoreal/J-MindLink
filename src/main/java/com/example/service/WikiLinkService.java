package com.example.service;

import com.example.domain.WikiPage;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WikiLinkService {

    /**
     * 본문 내 위키 페이지 타이틀을 링크로 변환한다.
     */
    public String convert(String content, List<WikiPage> pages) {
        if (content == null) {
            return null;
        }
        if (content.isEmpty()) {
            return content;
        }
        if (pages == null || pages.isEmpty()) {
            return content;
        }

        List<WikiPage> orderedPages = pages.stream()
                .filter(Objects::nonNull)
                .filter(p -> p.getTitle() != null && !p.getTitle().isEmpty())
                .sorted(Comparator.comparingInt((WikiPage p) -> p.getTitle().length()).reversed())
                .collect(Collectors.toList());

        String result = content;
        Map<String, String> placeholders = new LinkedHashMap<>();
        int index = 0;

        for (WikiPage page : orderedPages) {
            String title = page.getTitle();
            String placeholder = "__WIKI_LINK_" + index++ + "__";
            placeholders.put(placeholder, buildAnchor(title));
            result = result.replace(title, placeholder);
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private String buildAnchor(String title) {
        return "<a href=\"/wiki/" + title + "\">" + title + "</a>";
    }
}
