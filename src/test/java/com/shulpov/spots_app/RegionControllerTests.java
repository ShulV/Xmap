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
public class RegionControllerTests {


    private final MockMvc mockMvc;

    @Autowired
    public RegionControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetAllRegion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/regions/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва и Московская обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("[1610].name").value("Яманаси"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1611));
    }

    @Test
    public void testGetRegionByCountry() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/regions/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва и Московская обл."))
                .andExpect(MockMvcResultMatchers.jsonPath("[1610].name").value("Яманаси"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1611));
    }

}
