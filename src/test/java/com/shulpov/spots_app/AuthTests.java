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
    // correct
    private final String requestMapping = "/api/v1/auth";
    private final String correctName = "Ivan2023";
    private final String correctEmail = "ivanov123@gmail.com";
    private final String correctPhoneNumber = "89138005544";
    private final String correctBirthday = "2001-11-28";
    private final String correctPassword = "hardPassword123";
    // incorrect
    private final String incorrectName = "i";
    private final String incorrectEmail = "ivanov123gmail.com";
    private final String incorrectPhoneNumber = "89k";
    private final String incorrectOldBirthday = "1900-11-28";
    private final String incorrectPassword = "ha";

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    //====================================================================================================
    //====РЕГИСТРАЦИЯ=====================================================================================
    //====================================================================================================
    private final String correctRegisterRequestBody = "{\n" +
            "    \"name\": \"" + correctName + "\",\n" +
            "    \"email\": \"" + correctEmail + "\",\n" +
            "    \"phoneNumber\": \"" + correctPhoneNumber + "\",\n" +
            "    \"birthday\": \"" + correctBirthday + "\",\n" +
            "    \"password\": \"" + correctPassword + "\"\n" +
            "}";

    private final String incorrectRegisterRequestBody = "{\n" +
            "    \"name\": \"" + incorrectName + "\",\n" +
            "    \"email\": \"" + incorrectEmail + "\",\n" +
            "    \"phoneNumber\": \"" + incorrectPhoneNumber + "\",\n" +
            "    \"birthday\": \"" + incorrectOldBirthday + "\",\n" +
            "    \"password\": \"" + incorrectPassword + "\"\n" +
            "}";

    private ResultActions performRegister(String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON));

    }

    //регистрация с корректными данными пользователя - успех
    @Test
    @Transactional
    public void testCorrectDataRegister() throws Exception {
        //успешно регистрируем пользователя
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
        //успешно регистрируем пользователя
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

    //====================================================================================================
    //====АУТЕНТИФИКАЦИЯ==================================================================================
    //====================================================================================================
    private final String correctAuthenticateRequestBody = "{\n" +
            "    \"email\": \"" + correctEmail + "\",\n" +
            "    \"password\": \"" + correctPassword + "\"\n" +
            "}";

    private final String incorrectPasswordAuthenticateRequestBody = "{\n" +
            "    \"email\": \"" + correctEmail + "\",\n" +
            "    \"password\": \"" + incorrectPassword + "\"\n" +
            "}";

    private final String nonExistLoginAuthenticateRequestBody = "{\n" +
            "    \"email\": \"" + incorrectEmail + "\",\n" +
            "    \"password\": \"" + correctPassword + "\"\n" +
            "}";

    private ResultActions performAuthenticate(String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/authenticate")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

    }

    //использование корректных данных пользователя для аутентификации
    @Test
    @Transactional
    public void testCorrectDataAuthenticate() throws Exception {
        //успешно регистрируем пользователя
        performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isCreated()) //201
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
        //успешно аутентифицируем пользователя
        performAuthenticate(correctAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
    }

    //использование существующего логина, но неправильного пароля
    @Test
    @Transactional
    public void testIncorrectPasswordAuthenticate() throws Exception {
        //успешно регистрируем пользователя
        performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isCreated()) //201
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
        //птыаемся аутентифицироваться с существующим логином, но неправильным паролем
        performAuthenticate(incorrectPasswordAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) //401
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Неверный пароль"));
    }

    //использование несуществующего логина
    @Test
    @Transactional
    public void testNonExistLoginAuthenticate() throws Exception {
        //пытаемся аутентифицироваться с несуществующим логином
        performAuthenticate(nonExistLoginAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) //401
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Неверный логин"));
    }
    //====================================================================================================
    //====================================================================================================
    //====================================================================================================
}
