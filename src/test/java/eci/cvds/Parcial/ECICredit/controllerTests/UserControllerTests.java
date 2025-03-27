package eci.cvds.Parcial.ECICredit.controllerTests;

import eci.cvds.Parcial.ECICredit.controller.UserController;
import eci.cvds.Parcial.ECICredit.model.User;
import eci.cvds.Parcial.ECICredit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postUser_Success() {
        // Datos de prueba
        User userMock = new User("001", "juan@example.com");

        when(userService.registrarUsuario(userMock.getName(), userMock.getEmail())).thenReturn(userMock);

        // Ejecutar prueba
        ResponseEntity<User> response = userController.postUser(userMock);

        // Verificaciones
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userMock, response.getBody());
        verify(userService, times(1)).registrarUsuario(userMock.getName(), userMock.getEmail());
    }

    @Test
    void getUser_Success() {
        // Datos de prueba
        String email = "juan@example.com";
        User userMock = new User("001", email);

        when(userService.findByEmail(email)).thenReturn(Optional.of(userMock));

        // Ejecutar prueba
        ResponseEntity<User> response = userController.getUser(email);

        // Verificaciones
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userMock, response.getBody());
        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void getUser_NotFound() {
        // Datos de prueba
        String email = "noExiste@example.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        // Ejecutar prueba
        ResponseEntity<User> response = userController.getUser(email);

        // Verificaciones
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void handleGeneralException() {
        // Ejecutar prueba
        ResponseEntity<String> response = userController.handleGeneralException(new Exception("Error inesperado"));

        // Verificaciones
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno del servidor", response.getBody());
    }
}
