package eci.cvds.Parcial.ECICredit.serviceTests;

import eci.cvds.Parcial.ECICredit.model.User;
import eci.cvds.Parcial.ECICredit.mongoConnection.UserRepository;
import eci.cvds.Parcial.ECICredit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarUsuario_Success() {
        String nombre = "Juan";
        String email = "juan@example.com";
        User userMock = new User(nombre, email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userMock);

        User result = userService.registrarUsuario(nombre, email);

        assertNotNull(result);
        assertEquals(nombre, result.getName());
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registrarUsuario_EmailAlreadyExists() {
        String nombre = "Juan";
        String email = "juan@example.com";
        User existingUser = new User(nombre, email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registrarUsuario(nombre, email);
        });

        assertEquals("El email ya esta registrado.", thrown.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class)); // No debe guardar usuario
    }

    @Test
    void findByEmail_UserExists() {
        String email = "juan@example.com";
        User userMock = new User("Juan", email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(userMock, result.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_UserNotFound() {
        String email = "noExiste@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
