package com.tds.urlshortener.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tds.urlshortener.dto.CreateShortUrlDTO;
import com.tds.urlshortener.model.Statistic;
import com.tds.urlshortener.model.Url;
import com.tds.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:url-controller;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUrl_GivenCreateShortUrlDTO_ShouldReturnStatusOKAndShortUrl() throws Exception {
        String testPort = "-1";
        CreateShortUrlDTO createShortUrlDTO = CreateShortUrlDTO.builder()
                .longUrl("http://longtesturl.com").build();

        mockMvc.perform(post("/v1/url/createUrl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createShortUrlDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl")
                        .value(String.format("http://localhost:%s/api/v1/b", testPort)));
    }

    @Test
    void redirectToOriginalUrl_GivenShortUrl_ShouldReturnStatusPermanentRedirectAndRedirectClient() throws Exception {
        String longURL = "http://longtesturl.com";
        Url url = Url.builder().id(1L).longUrl(longURL).statistic(Statistic.builder().totalAccessCount(0L).build()).build();
        urlRepository.save(url);

        mockMvc.perform(get("/v1/b"))
                .andExpect(status().isPermanentRedirect())
                .andExpect(redirectedUrl(longURL));
    }
}
