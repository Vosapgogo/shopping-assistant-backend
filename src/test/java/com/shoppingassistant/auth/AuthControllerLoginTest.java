package com.shoppingassistant.auth;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shoppingassistant.auth.api.AuthController;
import com.shoppingassistant.auth.application.AuthException;
import com.shoppingassistant.auth.application.AuthService;
import com.shoppingassistant.auth.infrastructure.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerLoginTest {

    private static final String LOGIN_BODY = "{\"email\":\"anna@example.com\",\"password\":\"Secret123\"}";

    @MockitoBean
    private AuthService authService;

    // Needed by JwtAuthenticationFilter, which the web slice picks up
    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unknownEmailReturns401WithEmailNotFoundCode() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new AuthException(AuthException.Code.EMAIL_NOT_FOUND, "No account found with this email"));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("EMAIL_NOT_FOUND"));
    }

    @Test
    void wrongPasswordReturns401WithWrongPasswordCode() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new AuthException(AuthException.Code.WRONG_PASSWORD, "Incorrect password"));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("WRONG_PASSWORD"));
    }

    @Test
    void validCredentialsReturnToken() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn("jwt-token");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
