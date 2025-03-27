package eci.cvds.Parcial.ECICredit.serviceTests;

import eci.cvds.Parcial.ECICredit.model.Factura;
import eci.cvds.Parcial.ECICredit.model.Product;
import eci.cvds.Parcial.ECICredit.model.User;
import eci.cvds.Parcial.ECICredit.mongoConnection.FacturasRepository;
import eci.cvds.Parcial.ECICredit.mongoConnection.UserRepository;
import eci.cvds.Parcial.ECICredit.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacturaServiceTests {
    @Mock
    private FacturasRepository facturasRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FacturaService facturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void procesarPagoFactura_Success() {
        String email = "juan@example.com";
        User userMock = new User("001", email);
        List<Product> productos = Arrays.asList(new Product("Teclado", 100, 1));
        int montoIngresado = 100;
        LocalDate fechaCompra = LocalDate.of(2025, 3, 26);
        Factura facturaMock = new Factura(userMock.getId(), productos, fechaCompra);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));
        when(facturasRepository.save(any(Factura.class))).thenReturn(facturaMock);

        Factura result = facturaService.procesarPagoFactura(email, productos, montoIngresado, fechaCompra);

        assertNotNull(result);
        assertEquals(productos, result.getProducts());
        verify(userRepository, times(1)).findByEmail(email);
        verify(facturasRepository, times(1)).save(any(Factura.class));
    }

    @Test
    void procesarPagoFactura_UserNotFound() {
        String email = "noExiste@example.com";
        List<Product> productos = Arrays.asList(new Product("Teclado", 100, 1));
        int montoIngresado = 100;
        LocalDate fechaCompra = LocalDate.of(2025, 3, 26);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facturaService.procesarPagoFactura(email, productos, montoIngresado, fechaCompra);
        });

        assertEquals("No existe un usuario relacionado con este email.", exception.getMessage());
        verify(facturasRepository, never()).save(any());
    }

    @Test
    void procesarPagoFactura_MontoIncorrecto() {
        String email = "juan@example.com";
        User userMock = new User("001", email);
        List<Product> productos = Arrays.asList(new Product("Teclado", 100, 1));
        int montoIngresado = 50;  // Incorrecto
        LocalDate fechaCompra = LocalDate.of(2025, 3, 26);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facturaService.procesarPagoFactura(email, productos, montoIngresado, fechaCompra);
        });

        assertEquals("El monto ingresado no coincide con el total de la compra", exception.getMessage());
        verify(facturasRepository, never()).save(any());
    }

    @Test
    void obtenerPagosPorEmail_Success() {
        String email = "juan@example.com";
        User userMock = new User("001", email);
        List<Factura> facturasMock = Arrays.asList(
                new Factura(userMock.getId(), Arrays.asList(new Product("Mouse", 50, 2)), LocalDate.now())
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));
        when(facturasRepository.findAll()).thenReturn(facturasMock);

        List<Factura> result = facturaService.obtenerPagosPorEmail(email);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmail(email);
        verify(facturasRepository, times(1)).findAll();
    }

    @Test
    void obtenerPagosPorEmail_UserNotFound() {
        String email = "noExiste@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facturaService.obtenerPagosPorEmail(email);
        });

        assertEquals("No existe un usuario relacionado con este email.", exception.getMessage());
        verify(facturasRepository, never()).findAll();
    }

    @Test
    void updateProductosFactura_Success() {
        String facturaId = "1";
        List<Product> productos = Arrays.asList(new Product("Mouse", 50, 2));
        Factura facturaMock = new Factura("001", productos, LocalDate.now());

        when(facturasRepository.findByFacturaId(facturaId)).thenReturn(facturaMock);
        when(facturasRepository.save(any(Factura.class))).thenReturn(facturaMock);

        Factura result = facturaService.updateProductosFactura(facturaId, productos);

        assertNotNull(result);
        assertEquals(productos, result.getProducts());
        verify(facturasRepository, times(1)).save(facturaMock);
    }

    @Test
    void updateProductosFactura_FacturaNotFound() {
        String facturaId = "999";
        List<Product> productos = Arrays.asList(new Product("Mouse", 50, 2));

        when(facturasRepository.findByFacturaId(facturaId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facturaService.updateProductosFactura(facturaId, productos);
        });

        assertEquals("No existe factura con el id indicado.", exception.getMessage());
        verify(facturasRepository, never()).save(any());
    }

    @Test
    void updateProductosFactura_EmptyProductsList() {
        String facturaId = "1";
        Factura facturaMock = new Factura("001", Arrays.asList(new Product("Monitor", 200, 1)), LocalDate.now());
        List<Product> emptyProductsList = List.of();

        when(facturasRepository.findByFacturaId(facturaId)).thenReturn(facturaMock);
        when(facturasRepository.save(any(Factura.class))).thenReturn(facturaMock);

        Factura result = facturaService.updateProductosFactura(facturaId, emptyProductsList);

        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
        verify(facturasRepository, times(1)).save(facturaMock);
    }
}
