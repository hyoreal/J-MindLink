package com.example.service;

import com.example.domain.WikiPage;
import java.util.List;
import java.util.Objects;

public class WikiLinkService {

    /**
     * 본문 내 위키 페이지 타이틀을 링크로 변환한다.
     */
    public String convert(String content, List<WikiPage> pages) {
        if (content == null) {
            return null;
        }
        if (pages == null || pages.isEmpty()) {
            return content;
        }

        String result = content;
        for (WikiPage page : pages) {
            if (page == null || page.getTitle() == null) {
                continue;
            }
            String title = page.getTitle();
            String link = "<a href=\"/wiki/" + title + "\">" + title + "</a>";
            result = result.replace(title, link);
        }
        return result;
    }
}
