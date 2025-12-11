package com.example.api;

import com.example.domain.WikiPage;
import com.example.domain.WikiPageRepository;
import com.example.service.WikiLinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wiki")
public class WikiRestController {

    private final WikiPageRepository wikiPageRepository;
    private final WikiLinkService wikiLinkService;

    public WikiRestController(WikiPageRepository wikiPageRepository, WikiLinkService wikiLinkService) {
        this.wikiPageRepository = wikiPageRepository;
        this.wikiLinkService = wikiLinkService;
    }

    @PostMapping
    public ResponseEntity<WikiResponse> create(@Valid @RequestBody WikiCreateRequest request) {
        WikiPage saved = wikiPageRepository.save(new WikiPage(null, request.title(), request.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WikiResponse> get(@PathVariable Long id) {
        Optional<WikiPage> found = wikiPageRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<WikiPage> otherPages = wikiPageRepository.findAll().stream()
                .filter(page -> !page.getId().equals(id))
                .toList();
        String linkedContent = wikiLinkService.convert(found.get().getContent(), otherPages);
        return ResponseEntity.ok(new WikiResponse(found.get().getId(), found.get().getTitle(), linkedContent));
    }

    @GetMapping
    public ResponseEntity<List<WikiResponse>> list() {
        List<WikiResponse> responses = wikiPageRepository.findAll().stream()
                .sorted(Comparator.comparing(WikiPage::getId))
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private WikiResponse toResponse(WikiPage page) {
        return new WikiResponse(page.getId(), page.getTitle(), page.getContent());
    }

    public record WikiCreateRequest(
            @NotBlank String title,
            @NotBlank String content
    ) {
    }

    public record WikiResponse(
            Long id,
            String title,
            String content
    ) {
    }
}
