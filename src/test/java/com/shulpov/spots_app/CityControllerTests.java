package com.shulpov.spots_app;

import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.models.Country;
import com.shulpov.spots_app.models.Region;
import com.shulpov.spots_app.repo.CityRepo;
import com.shulpov.spots_app.repo.CountryRepo;
import com.shulpov.spots_app.repo.RegionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Указываем, что используем профиль "test"
public class CityControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private RegionRepo regionRepo;
    @Autowired
    private CityRepo cityRepo;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAllCitiesWithData() throws Exception {
        City city1 = City.builder()
                .name("Барнаул")
                .build();

        when(cityRepo.findAll()).thenReturn(Collections.singletonList(city1));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"));

        verify(cityRepo, times(1)).findAll();
        verifyNoMoreInteractions(cityRepo);
    }

    @Test
    public void testFindAllCitiesWithoutData() throws Exception {
        when(cityRepo.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(cityRepo, times(1)).findAll();
        verifyNoMoreInteractions(cityRepo);
    }

    @Test
    public void testCitiesByIdRegionWithData() throws Exception {
        Region region1 = Region.builder()
                .name("Московская область")
                .build();

        City city1 = City.builder()
                .name("Барнаул")
                .region(region1)
                .build();

        when(regionRepo.findById(1)).thenReturn(java.util.Optional.of(region1));
        when(cityRepo.findByRegion(region1)).thenReturn(Collections.singletonList(city1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-region-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"));

        verify(regionRepo, times(1)).findById(1);
        verify(cityRepo, times(1)).findByRegion(region1);
        verifyNoMoreInteractions(regionRepo, cityRepo);
    }

    @Test
    public void testCitiesByIdRegionWithoutData() throws Exception {
        Region region1 = Region.builder()
                .name("Московская область")
                .build();

        when(regionRepo.findById(1)).thenReturn(java.util.Optional.of(region1));
        when(cityRepo.findByRegion(region1)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-region-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(regionRepo, times(1)).findById(1);
        verify(cityRepo, times(1)).findByRegion(region1);
        verifyNoMoreInteractions(regionRepo, cityRepo);
    }

    @Test
    public void testCitiesByIdCountryWithData() throws Exception {
        Country country1 = Country.builder()
                .name("Россия")
                .build();

        Region region1 = Region.builder()
                .name("Московская область")
                .country(country1)
                .build();

        City city1 = City.builder()
                .name("Барнаул")
                .region(region1)
                .build();

        when(countryRepo.findById(1)).thenReturn(java.util.Optional.of(country1));
        when(cityRepo.findByRegion_Country(country1)).thenReturn(Collections.singletonList(city1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-country-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"));

        verify(countryRepo, times(1)).findById(1);
        verify(cityRepo, times(1)).findByRegion_Country(country1);
        verifyNoMoreInteractions(countryRepo, cityRepo);
    }

    @Test
    public void testCitiesByIdCountryWithoutData() throws Exception {
        Country country1 = Country.builder()
                .name("Россия")
                .build();

        when(countryRepo.findById(1)).thenReturn(java.util.Optional.of(country1));
        when(cityRepo.findByRegion_Country(country1)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-country-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(countryRepo, times(1)).findById(1);
        verify(cityRepo, times(1)).findByRegion_Country(country1);
        verifyNoMoreInteractions(countryRepo, cityRepo);
    }
}
