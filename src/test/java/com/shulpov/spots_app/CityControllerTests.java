package com.shulpov.spots_app;

import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.models.Country;
import com.shulpov.spots_app.models.Region;
import com.shulpov.spots_app.repo.CityRepo;
import com.shulpov.spots_app.repo.CountryRepo;
import com.shulpov.spots_app.repo.RegionRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
//@SpringJUnitConfig(TestDatabaseConfig.class)
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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @AfterEach
    public void tearDown(){
        countryRepo.findAll().clear();
        regionRepo.findAll().clear();
        cityRepo.findAll().clear();
    }


    @Test
    public void testRegion() throws Exception {

        Region region1 = Region.builder()
                .id(0)
                .name("Московская область")
                .build();

        regionRepo.save(region1);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/regions/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Московская область"));

    }

    @Test
    public void testCities() throws Exception {

        // Создаем несколько городов
        City city1 = City.builder()
                .name("Барнаул")
                .build();

        City city2 = City.builder()
                .id(2)
                .name("Москва")
                .build();

        City city3 = City.builder()
                .id(3)
                .name("Алейск")
                .build();

        // Сохраняем города в репозитории
        cityRepo.save(city1);
        cityRepo.save(city2);
        cityRepo.save(city3);


        // Выполняем запрос и проверки
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Алейск"));
    }


    @Test
    public void testCitiesByIdRegion() throws Exception {

        Region region1 = Region.builder()
                .name("Московская область")
                .build();

        // Создаем несколько городов
        City city1 = City.builder()
                .name("Барнаул")
                .region(region1)
                .build();

        City city2 = City.builder()
                .id(2)
                .name("Москва")
                .region(region1)
                .build();

        City city3 = City.builder()
                .id(3)
                .name("Алейск")
                .region(region1)
                .build();



        // Сохраняем города в репозитории
        regionRepo.save(region1);
        cityRepo.save(city1);
        cityRepo.save(city2);
        cityRepo.save(city3);



        // Выполняем запрос и проверки
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-region-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Алейск"));
    }


    @Test
    public void testCitiesByIdCountry() throws Exception {

        Country country1 = Country.builder()
                .name("Россия")
                .build();

        Country country2 = Country.builder()
                .name("Азербайджан")
                .build();

        Region region1 = Region.builder()
                .name("Московская область")
                .country(country1)
                .build();

        // Создаем несколько городов
        City city1 = City.builder()
                .name("Барнаул")
                .region(region1)
                .build();

        City city2 = City.builder()
                .id(2)
                .name("Москва")
                .region(region1)
                .build();

        City city3 = City.builder()
                .id(3)
                .name("Алейск")
                .region(region1)
                .build();



        // Сохраняем города в репозитории
        countryRepo.save(country1);
        countryRepo.save(country2);
        regionRepo.save(region1);
        cityRepo.save(city1);
        cityRepo.save(city2);
        cityRepo.save(city3);



        // Выполняем запрос и проверки
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/get-by-country-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Барнаул"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Москва"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Алейск"));
    }


        @Test
    public void testCountry() throws Exception {
        Country country1 = Country.builder()
                .id(0)
                .name("Россия")
                .build();


//        Region region1 = Region.builder()
//                .id(0)
//                .name("Московская область")
//                .country(country1)
//                .build();
//
//
//        City city1 = City.builder()
//                .id(0)
//                .name("Москва")
//                .region(region1)
//                .build();
//
//        City city2 = City.builder()
//                .id(1)
//                .name("Барнаул")
//                .region(region1)
//                .build();
//
//        City city3 = City.builder()
//                .id(2)
//                .name("Алейск")
//                .region(region1)
//                .build();

        countryRepo.save(country1);


        //todo аналогично сделать билдеры и такие аннотации как у country у region и city
        //todo  так же создавать экземпляры

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Россия"));


    }
}
