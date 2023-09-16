package com.shulpov.spots_app;

import com.shulpov.spots_app.models.Country;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author Orlov Daniil
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // ���������, ��� ���������� ������� "test"
public class CountryControllerTests {


    private final MockMvc mockMvc;

    public CountryControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    @Test
    public void testCountry() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Москва и Московская обл."));

    }
}
