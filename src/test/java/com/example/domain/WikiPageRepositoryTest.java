package com.example.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class WikiPageRepositoryTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Test
    @DisplayName("위키 페이지 저장 후 조회")
    void saveAndFind() {
        WikiPage page = new WikiPage(null, "hello", "content");

        WikiPage saved = wikiPageRepository.save(page);

        assertThat(saved.getId()).isNotNull();
        assertThat(wikiPageRepository.findById(saved.getId()))
                .get()
                .extracting(WikiPage::getTitle, WikiPage::getContent)
                .containsExactly("hello", "content");
    }

    @Test
    @DisplayName("중복 타이틀 저장 시 예외")
    void duplicateTitleThrowsException() {
        wikiPageRepository.save(new WikiPage(null, "dup", "first"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            wikiPageRepository.saveAndFlush(new WikiPage(null, "dup", "second"));
        });
    }
}
