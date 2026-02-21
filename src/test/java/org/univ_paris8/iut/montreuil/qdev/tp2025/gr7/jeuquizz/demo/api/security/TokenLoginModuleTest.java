package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.TokenService;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenLoginModuleTest {

    private TokenLoginModule loginModule;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        loginModule = new TokenLoginModule();
        subject = new Subject();
        callbackHandler = mock(CallbackHandler.class);
        tokenService = mock(TokenService.class);
        loginModule.setTokenService(tokenService);
        loginModule.initialize(subject, callbackHandler, new HashMap<>(), new HashMap<>());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        User user = new User("testuser", "test@test.com", "password");
        user.setId(1L);
        when(tokenService.validateToken("valid-token")).thenReturn(user);

        doAnswer(invocation -> {
            Callback[] callbacks = invocation.getArgument(0);
            for (Callback c : callbacks) {
                if (c instanceof TextInputCallback) {
                    ((TextInputCallback) c).setText("valid-token");
                }
            }
            return null;
        }).when(callbackHandler).handle(any());

        // Act
        boolean loginResult = loginModule.login();
        boolean commitResult = loginModule.commit();

        // Assert
        assertTrue(loginResult);
        assertTrue(commitResult);
        assertEquals(2, subject.getPrincipals().size());
    }

    @Test
    void testLoginFailure() throws Exception {
        // Arrange
        when(tokenService.validateToken(anyString())).thenReturn(null);

        doAnswer(invocation -> {
            Callback[] callbacks = invocation.getArgument(0);
            for (Callback c : callbacks) {
                if (c instanceof TextInputCallback) {
                    ((TextInputCallback) c).setText("invalid-token");
                }
            }
            return null;
        }).when(callbackHandler).handle(any());

        // Act & Assert
        assertThrows(LoginException.class, () -> loginModule.login());
        assertFalse(loginModule.commit());
    }
}
