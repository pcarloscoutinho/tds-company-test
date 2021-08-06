package com.tds.urlshortener.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:statistic-controller;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StatisticControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void getStatisticsByShortUrl_GivenShortUrl_ShouldReturnStatusOKAndStatisticDTO() throws Exception {
        Url url = Url.builder().id(1L).longUrl("http://longurltest.com").statistic(Statistic.builder()
                .totalAccessCount(2L).build()).build();

        urlRepository.save(url);

        mockMvc.perform(get("/v1/statistics/b")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAccessCount").value(2L));
    }
}
