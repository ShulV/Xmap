package com.shulpov.spots_app;

import com.shulpov.spots_app.common.ApiResponseStatus;
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
class CityControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    CityControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testGetAllCities() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[17286].name").value("Vert"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(17287))
                .andExpect(MockMvcResultMatchers.jsonPath("$.custom_status")
                        .value(ApiResponseStatus.SUCCESS.toString()));

    }

    @Test
    void testGetCityByCountryCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/by-country/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[2567].name").value("Черкесск"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(2568))
                .andExpect(MockMvcResultMatchers.jsonPath("$.custom_status")
                        .value(ApiResponseStatus.SUCCESS.toString()));

    }

    @Test
    void testGetCityByCountryWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/by-country/100000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Country isn't exist or no cities in country"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.custom_status")
                        .value(ApiResponseStatus.CLIENT_ERROR.toString()));
    }


    @Test
    void testGetCityByRegionCorrectIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/by-region/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[0].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list[171].name").value("Яхрома"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data_list.length()").value(172))
                .andExpect(MockMvcResultMatchers.jsonPath("$.custom_status")
                        .value(ApiResponseStatus.SUCCESS.toString()));
    }

    @Test
    void testGetCityByRegionWrongIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/by-region/1000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Region isn't exist or no cities in region"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.custom_status")
                        .value(ApiResponseStatus.CLIENT_ERROR.toString()));

    }


}
