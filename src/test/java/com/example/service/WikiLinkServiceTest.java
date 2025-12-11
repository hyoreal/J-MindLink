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

    @Test
    @DisplayName("본문이 null 또는 빈 경우 그대로 반환")
    void returnAsIsForNullOrEmpty() {
        List<WikiPage> pages = List.of(new WikiPage(null, "Java", "about java"));

        assertThat(wikiLinkService.convert(null, pages)).isNull();
        assertThat(wikiLinkService.convert("", pages)).isEqualTo("");
    }

    @Test
    @DisplayName("키워드가 없으면 변환하지 않는다")
    void noKeywordNoChange() {
        List<WikiPage> pages = List.of(new WikiPage(null, "Java", "about java"));
        String content = "Nothing to link here.";

        String result = wikiLinkService.convert(content, pages);

        assertThat(result).isEqualTo(content);
    }

    @Test
    @DisplayName("긴 단어를 우선 링크 처리한다 - JavaScript vs Java")
    void prioritizeLongerKeyword() {
        List<WikiPage> pages = List.of(
                new WikiPage(null, "Java", "java"),
                new WikiPage(null, "JavaScript", "javascript")
        );
        String content = "JavaScript is different from Java.";

        String result = wikiLinkService.convert(content, pages);

        assertThat(result).isEqualTo("<a href=\"/wiki/JavaScript\">JavaScript</a> is different from <a href=\"/wiki/Java\">Java</a>.");
    }
}
