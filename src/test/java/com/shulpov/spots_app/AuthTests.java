package com.shulpov.spots_app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.auth.token.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * @author Shulpov Victor
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthTests {
    private final MockMvc mockMvc;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

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
    public AuthTests(MockMvc mockMvc, TokenService tokenService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
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

    private ResultActions performRegister(String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON));

    }

    //регистрация с корректными данными пользователя - успех
    @Test
    @Transactional
    void testCorrectDataRegister() throws Exception {
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
    void testRepeatedCorrectDataRegister() throws Exception {
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
    void testIncorrectDataRegister() throws Exception {
        //регистрируем пользователя, используя некорректные данные
        String incorrectRegisterRequestBody = "{\n" +
                "    \"name\": \"" + incorrectName + "\",\n" +
                "    \"email\": \"" + incorrectEmail + "\",\n" +
                "    \"phoneNumber\": \"" + incorrectPhoneNumber + "\",\n" +
                "    \"birthday\": \"" + incorrectOldBirthday + "\",\n" +
                "    \"password\": \"" + incorrectPassword + "\"\n" +
                "}";
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
    void testCorrectDataAuthenticate() throws Exception {
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
    void testIncorrectPasswordAuthenticate() throws Exception {
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
    void testNonExistLoginAuthenticate() throws Exception {
        //пытаемся аутентифицироваться с несуществующим логином
        performAuthenticate(nonExistLoginAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) //401
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Неверный логин"));
    }
    //====================================================================================================
    //======ОБНОВЛЕНИЕ ТОКЕНОВ============================================================================
    //====================================================================================================
    private ResultActions performRefreshToken(String oldRefreshToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/refresh-token")
                .header("Authorization", "Refresh " + oldRefreshToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private String getRefreshTokenFromResult(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(jsonResponse, AuthenticationResponse.class);
        return response.getRefreshToken();
    }

    //обновление токена сразу после регистрации
    @Test
    @Transactional
    void testRefreshToken__refresh_after_register() throws Exception {
        //успешно регистрируем пользователя
        ResultActions resultRegister = performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isCreated()) //201
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
        List<Token> tokenList = tokenService.getAllTokens();
        //только один refresh токен в БД
        assertEquals(1, tokenList.size());
        //получаем refreshToken из результата запроса
        MvcResult result = resultRegister.andReturn();
        String refreshToken = getRefreshTokenFromResult(result);
        //токен из response и токен, который только что добавился в БД совпадают
        assertEquals(tokenList.get(0).getValue(), refreshToken);
        //обновляем токен
        ResultActions resultRefreshToken = performRefreshToken(refreshToken)
                .andExpect(MockMvcResultMatchers.status().isOk()) //200
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        tokenList = tokenService.getAllTokens();
        //только один refresh токен в БД (старый должен был удалиться)
        assertEquals(1, tokenList.size());
        //получаем refreshToken из результата запроса
        result = resultRefreshToken.andReturn();
        refreshToken = getRefreshTokenFromResult(result);
        //токен из response и токен, который только что добавился в БД совпадают
        assertEquals(tokenList.get(0).getValue(), refreshToken);
    }
}
