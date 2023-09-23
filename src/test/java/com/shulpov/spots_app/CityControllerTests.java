package com.shulpov.spots_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    public CityControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetAllCities() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[17286].name").value("Vert"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(17287));
    }

    @Test
    public void testGetCityByCountryCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-country-id/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2567].name").value("Черкесск"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2568));
    }

    @Test
    public void testGetCityByCountryWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-country-id/100000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Country with id=100000 not found"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void testGetCityByRegionCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-region-id/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("[171].name").value("Яхрома"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(172));
    }

    @Test
    public void testGetCityByRegionWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-region-id/1000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Region with id=1000000 not found"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
