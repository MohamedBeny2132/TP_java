package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

/**
 * Niveau 4 – Tests Web : Servlet Login avec mocks HTTP.
 *
 * On simule HttpServletRequest / Response / Session avec Mockito.
 * Pas de serveur Tomcat nécessaire.
 */
@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AuthService authService;

    private Login loginServlet;

    @BeforeEach
    void setUp() throws Exception {
        loginServlet = new Login();
        // Injection du mock authService dans le servlet via réflexion
        Field field = Login.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(loginServlet, authService);
    }

    // ===========================
    // Tests GET
    // ===========================

    @Test
    @DisplayName("GET /login – utilisateur non connecté → afficher la page de connexion")
    void testGet_PasConnecte() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestDispatcher("/jsp/Connection.jsp")).thenReturn(dispatcher);

        loginServlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("GET /login – utilisateur déjà connecté → redirect vers la liste")
    void testGet_DejaConnecte() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(1); // session active
        when(request.getContextPath()).thenReturn("/myapp");

        loginServlet.doGet(request, response);

        verify(response).sendRedirect("/myapp/annonce/list");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    // ===========================
    // Tests POST
    // ===========================

    @Test
    @DisplayName("POST /login – champs vides → erreur de validation")
    void testPost_ChampsVides() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("/jsp/Connection.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        // Doit remettre un message d'erreur et rester sur la page
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
        verify(authService, never()).login(anyString(), anyString()); // Ne pas appeler le service
    }

    @Test
    @DisplayName("POST /login – identifiants corrects → créer session et rediriger")
    void testPost_IdentifiantsCorrects() throws ServletException, IOException {
        User user = new User("motez", "motez@test.com", "hashed");
        // Simuler l'ID (normalement généré par la BDD)
        user.setId(42L);

        when(request.getParameter("email")).thenReturn("motez@test.com");
        when(request.getParameter("password")).thenReturn("Password123");
        when(authService.login("motez@test.com", "Password123")).thenReturn(user);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("");

        loginServlet.doPost(request, response);

        verify(session).setAttribute("id", 42L);
        verify(response).sendRedirect("/annonce/list");
        verify(request, never()).setAttribute(eq("error"), anyString());
    }

    @Test
    @DisplayName("POST /login – mauvais mot de passe → message d'erreur")
    void testPost_MauvaisMotDePasse() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("motez@test.com");
        when(request.getParameter("password")).thenReturn("MAUVAIS");
        when(authService.login("motez@test.com", "MAUVAIS")).thenReturn(null); // login échoué
        when(request.getRequestDispatcher("/jsp/Connection.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Email ou mot de passe incorrect"));
        verify(dispatcher).forward(request, response);
        // getSession(true) ne doit pas être appelé (login échoué, pas de session créée)
        verify(request, never()).getSession(true);
    }

    @Test
    @DisplayName("POST /login – email null → erreur de validation")
    void testPost_EmailNull() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("Password123");
        when(request.getRequestDispatcher("/jsp/Connection.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
        verify(authService, never()).login(any(), any());
    }
}
