package com.shulpov.spots_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * @author Shulpov Victor
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {
    private final MockMvc mockMvc;

    private final String requestMapping = "/api/v1/auth";
    private final String correctName = "Ivan2023";
    private final String correctEmail = "ivanov123@gmail.com";
    private final String correctPhoneNumber = "89138005544";
    private final String correctBirthday = "2001-11-28";
    private final String correctPassword = "hardPassword123";
    private final String correctRegisterRequestBody = "{\n" +
            "    \"name\": \"" + correctName + "\",\n" +
            "    \"email\": \"" + correctEmail + "\",\n" +
            "    \"phoneNumber\": \"" + correctPhoneNumber + "\",\n" +
            "    \"birthday\": \"" + correctBirthday + "\",\n" +
            "    \"password\": \"" + correctPassword + "\"\n" +
            "}";
    private final String incorrectName = "i";
    private final String incorrectEmail = "ivanov123gmail.com";
    private final String incorrectPhoneNumber = "89k";
    private final String incorrectOldBirthday = "1900-11-28";
    private final String incorrectPassword = "ha";

    private final String incorrectRegisterRequestBody = "{\n" +
            "    \"name\": \"" + incorrectName + "\",\n" +
            "    \"email\": \"" + incorrectEmail + "\",\n" +
            "    \"phoneNumber\": \"" + incorrectPhoneNumber + "\",\n" +
            "    \"birthday\": \"" + incorrectOldBirthday + "\",\n" +
            "    \"password\": \"" + incorrectPassword + "\"\n" +
            "}";

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private ResultActions performRegister(String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON));

    }

    //регистрация с корректными данными пользователя - успех
    @Test
    @Transactional
    public void testCorrectDataRegister() throws Exception {
                performRegister(correctRegisterRequestBody)
                        .andExpect(MockMvcResultMatchers.status().isCreated()) //201
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").isNumber())
                        .andExpect(jsonPath("$.access_token").isString())
                        .andExpect(jsonPath("$.refresh_token").isString());
    }

    //использование одних и тех же данных пользователя для регистрации - обработка ошибки
    @Test
    @Transactional
    public void testRepeatedCorrectDataRegister() throws Exception {
        //регистрируем пользователя
        performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isCreated()) //201
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());

        //повторно регистрируем такого же пользователя
        performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.errors.length()").value(3));
    }

    //использование некорректных данных пользователя для регистрации - обработка ошибки
    @Test
    @Transactional
    public void testIncorrectDataRegister() throws Exception {
        //регистрируем пользователя, используя некорректные данные
        performRegister(incorrectRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.errors.length()").value(5));
    }
}
