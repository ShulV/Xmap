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
class RegionControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    RegionControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testGetAllRegion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Москва и Московская обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[1610].name").value("Яманаси"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(1611));
    }

    @Test
    void testGetRegionByCountryCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/by-country/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Винницкая обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[24].name").value("Черновицкая обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(25));
    }

    @Test
    void testGetRegionByCountryWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/by-country/100000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Country isn't exist or no regions in country"));

    }

}
