package com.shulpov.spots_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegionControllerTests {


    private final MockMvc mockMvc;

    @Autowired
    RegionControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testGetAllRegion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва и Московская обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("[1610].name").value("Яманаси"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1611));
    }

    @Test
    void testGetRegionByCountryCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/get-by-country-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Винницкая обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("[24].name").value("Черновицкая обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(25));
    }

    @Test
    void testGetRegionByCountryWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/get-by-country-id/100000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Country with id=100000 not found"));

    }

}
