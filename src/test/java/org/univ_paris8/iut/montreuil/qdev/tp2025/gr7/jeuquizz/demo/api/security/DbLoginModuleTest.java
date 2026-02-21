package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DbLoginModuleTest {

    private DbLoginModule loginModule;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        loginModule = new DbLoginModule();
        subject = new Subject();
        callbackHandler = mock(CallbackHandler.class);
        authService = mock(AuthService.class);
        loginModule.setAuthService(authService);
        loginModule.initialize(subject, callbackHandler, new HashMap<>(), new HashMap<>());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        User user = new User("testuser", "test@test.com", "password");
        user.setId(1L);
        when(authService.login("test@test.com", "password")).thenReturn(user);

        doAnswer(invocation -> {
            Callback[] callbacks = invocation.getArgument(0);
            for (Callback c : callbacks) {
                if (c instanceof NameCallback) {
                    ((NameCallback) c).setName("test@test.com");
                } else if (c instanceof PasswordCallback) {
                    ((PasswordCallback) c).setPassword("password".toCharArray());
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
        assertTrue(subject.getPrincipals().stream().anyMatch(p -> p instanceof UserPrincipal));
        assertTrue(subject.getPrincipals().stream().anyMatch(p -> p instanceof RolePrincipal));
    }

    @Test
    void testLoginFailure() throws Exception {
        // Arrange
        when(authService.login(anyString(), anyString())).thenReturn(null);

        doAnswer(invocation -> {
            Callback[] callbacks = invocation.getArgument(0);
            for (Callback c : callbacks) {
                if (c instanceof NameCallback) {
                    ((NameCallback) c).setName("wrong@test.com");
                } else if (c instanceof PasswordCallback) {
                    ((PasswordCallback) c).setPassword("wrong".toCharArray());
                }
            }
            return null;
        }).when(callbackHandler).handle(any());

        // Act & Assert
        assertThrows(LoginException.class, () -> loginModule.login());
        assertFalse(loginModule.commit());
        assertEquals(0, subject.getPrincipals().size());
    }
}
