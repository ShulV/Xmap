package com.shulpov.spots_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author Orlov Daniil
 * @since 1.0
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    CountryControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testGetAllCountry() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/countries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Россия"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[217].name").value("Япония"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(218));
    }

}