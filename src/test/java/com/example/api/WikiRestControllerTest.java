package com.example.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.domain.WikiPage;
import com.example.domain.WikiPageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class WikiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @BeforeEach
    void setUp() {
        wikiPageRepository.deleteAll();
    }

    @Test
    @DisplayName("위키 등록 API")
    void createWiki() throws Exception {
        var request = new WikiCreateRequest("Java", "About Java");

        mockMvc.perform(post("/api/v1/wiki")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Java"))
                .andExpect(jsonPath("$.content").value("About Java"))
                .andDo(document("wiki-create",
                        requestFields(
                                fieldWithPath("title").description("위키 제목(링크 키워드)"),
                                fieldWithPath("content").description("본문 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 위키 ID"),
                                fieldWithPath("title").description("위키 제목"),
                                fieldWithPath("content").description("본문 내용")
                        )
                ));
    }

    @Test
    @DisplayName("위키 단건 조회 API - 본문 키워드 자동 링크")
    void getWiki() throws Exception {
        wikiPageRepository.saveAll(List.of(
                new WikiPage(null, "Java", "About Java"),
                new WikiPage(null, "JavaScript", "About JavaScript")
        ));
        WikiPage target = wikiPageRepository.save(new WikiPage(null, "Spring", "Spring uses Java and JavaScript"));

        mockMvc.perform(get("/api/v1/wiki/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(target.getId()))
                .andExpect(jsonPath("$.title").value("Spring"))
                .andExpect(jsonPath("$.content").value("Spring uses <a href=\"/wiki/Java\">Java</a> and <a href=\"/wiki/JavaScript\">JavaScript</a>"))
                .andDo(document("wiki-get",
                        pathParameters(
                                parameterWithName("id").description("조회할 위키 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("위키 ID"),
                                fieldWithPath("title").description("위키 제목"),
                                fieldWithPath("content").description("본문 내용 (키워드 링크 변환 후)")
                        )
                ));
    }

    @Test
    @DisplayName("위키 목록 조회 API")
    void listWiki() throws Exception {
        wikiPageRepository.saveAll(List.of(
                new WikiPage(null, "Java", "About Java"),
                new WikiPage(null, "Spring", "About Spring")
        ));

        mockMvc.perform(get("/api/v1/wiki"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].content").value("About Java"))
                .andDo(document("wiki-list",
                        responseFields(
                                fieldWithPath("[].id").description("위키 ID"),
                                fieldWithPath("[].title").description("위키 제목"),
                                fieldWithPath("[].content").description("본문 내용")
                        )
                ));
    }

    private record WikiCreateRequest(String title, String content) {
    }
}
