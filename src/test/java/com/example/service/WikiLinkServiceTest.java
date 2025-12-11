package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.domain.WikiPage;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WikiLinkServiceTest {

    private final WikiLinkService wikiLinkService = new WikiLinkService();

    @Test
    @DisplayName("본문의 키워드를 링크로 변환")
    void convertKeywordToLink() {
        List<WikiPage> pages = List.of(new WikiPage(null, "Java", "about java"));
        String content = "I love Java and Java is popular.";

        String result = wikiLinkService.convert(content, pages);

        assertThat(result).isEqualTo("I love <a href=\"/wiki/Java\">Java</a> and <a href=\"/wiki/Java\">Java</a> is popular.");
    }
}
