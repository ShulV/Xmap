package com.shulpov.spots_app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.auth.token.TokenService;
import com.shulpov.spots_app.db_cleaner.DBCleaner;
import com.shulpov.spots_app.responses.ErrorMessageResponse;
import com.shulpov.spots_app.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * @author Shulpov Victor
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthTests {
    private final String cleanScriptPath = "sql/clean/clean_for_auth_tests.sql";
    private final MockMvc mockMvc;
    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private static DBCleaner dbCleaner;

    // correct
    private final String requestMapping = "/api/v1/auth";
    private final String correctName = "Ivan2023";
    private final String correctEmail = "ivanov123@gmail.com";
    private final String correctPhoneNumber = "89138005544";
    private final String correctBirthday = "2001-11-28";
    private final String correctPassword = "hardPassword123";
    private final String incorrectEmail = "ivanov123gmail.com";
    private final String incorrectPassword = "ha";

    @Autowired
    public AuthTests(MockMvc mockMvc, TokenService tokenService, UserService userService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.tokenService = tokenService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @BeforeAll
    public static void prepare() throws SQLException {
        dbCleaner = new DBCleaner();
        dbCleaner.setup();
    }

    @AfterEach
    public void cleanByScript() throws SQLException {
        dbCleaner.cleanupDatabase(cleanScriptPath);
    }

    @AfterAll
    public static void finishTesting() {
        dbCleaner.closeConnection();
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
    void testRegister__correct_data_register() throws Exception {
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
    void testRegister__repeated_correct_data_register() throws Exception {
        //успешно регистрируем пользователя
        performRegister(correctRegisterRequestBody);

        //повторно регистрируем такого же пользователя
        performRegister(correctRegisterRequestBody)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.errors.length()").value(3));
    }

    //использование некорректных данных пользователя для регистрации - обработка ошибки
    @Test
    void testRegister__incorrect_data_register() throws Exception {
        //регистрируем пользователя, используя некорректные данные
        // incorrect
        String incorrectName = "i";
        String incorrectPhoneNumber = "89k";
        String incorrectOldBirthday = "1900-11-28";
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

    private ResultActions performAuthenticate(String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(requestMapping + "/authenticate")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));
    }
    //====================================================================================================
    //====АУТЕНТИФИКАЦИЯ==================================================================================
    //====================================================================================================
    String correctAuthenticateRequestBody = "{\n" +
            "    \"email\": \"" + correctEmail + "\",\n" +
            "    \"password\": \"" + correctPassword + "\"\n" +
            "}";
    //использование корректных данных пользователя для аутентификации
    @Test
    void testAuthenticate__correct_data_authenticate() throws Exception {
        //успешно регистрируем пользователя
        performRegister(correctRegisterRequestBody);
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
    void testAuthenticate__incorrect_password_authenticate() throws Exception {
        //успешно регистрируем пользователя
        performRegister(correctRegisterRequestBody);
        //пытаемся аутентифицироваться с существующим логином, но неправильным паролем
        String incorrectPasswordAuthenticateRequestBody = "{\n" +
                "    \"email\": \"" + correctEmail + "\",\n" +
                "    \"password\": \"" + incorrectPassword + "\"\n" +
                "}";
        performAuthenticate(incorrectPasswordAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) //401
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Неверный пароль"));
    }

    //использование несуществующего логина
    @Test
    void testAuthenticate__non_exist_login_authenticate() throws Exception {
        //пытаемся аутентифицироваться с несуществующим логином
        String nonExistLoginAuthenticateRequestBody = "{\n" +
                "    \"email\": \"" + incorrectEmail + "\",\n" +
                "    \"password\": \"" + correctPassword + "\"\n" +
                "}";
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

    private Long getUserIdFromResult(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(jsonResponse, AuthenticationResponse.class);
        return response.getUserId();
    }

    private ErrorMessageResponse getErrorMessageResponseFromResult(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, ErrorMessageResponse.class);
    }

    //обновление токена сразу после регистрации
    @Test
    void testRefreshToken__refresh_after_register() throws Exception {
        //успешно регистрируем пользователя
        ResultActions resultRegister = performRegister(correctRegisterRequestBody);
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

    //обновление токена для удаленного аккаунта с еще свежим refreshToken'ом
    @Test
    void testRefreshToken__refresh_token_for_deleted_account_by_fresh_refresh() throws Exception {
        //успешно регистрируем пользователя и вытаскиваем его id, accessToken и refreshToken
        ResultActions resultRegister = performRegister(correctRegisterRequestBody);
        MvcResult result = resultRegister.andReturn();
        String refreshToken = getRefreshTokenFromResult(result);
        Long userId = getUserIdFromResult(result);
        //удаляем пользователя
        userService.deleteById(userId);
        //обновляем токен
        ResultActions resultRefreshToken = performRefreshToken(refreshToken)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) //401
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        List<Token> tokenList = tokenService.getAllTokens();
        //проверяем, что refreshToken не добавился в БД
        assertEquals(0, tokenList.size());
        result = resultRefreshToken.andReturn();
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponseFromResult(result);
        assertEquals("Refresh not found in DB", errorMessageResponse.getErrorMessage());
    }


    //тест refresh с регистрацией 1 раз и аутентификацией 2 раза, чтобы стало 3 токена в БД
    @Test
    void testRefreshToken__register_and_2_authenticate_and_check_DB_tokens() throws Exception {
        //успешно регистрируем пользователя и вытаскиваем его id, accessToken и refreshToken
        performRegister(correctRegisterRequestBody);
        //успешно аутентифицируем пользователя 1 раз
        performAuthenticate(correctAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
        //успешно аутентифицируем пользователя 2 раз
        performAuthenticate(correctAuthenticateRequestBody)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString());
        List<Token> tokenList = tokenService.getAllTokens();
        //проверяем, что refreshToken'ы добавились в БД
        assertEquals(3, tokenList.size());
    }

    //     тест refresh с аутентификацией и refresh вызовами вперемешку
}
