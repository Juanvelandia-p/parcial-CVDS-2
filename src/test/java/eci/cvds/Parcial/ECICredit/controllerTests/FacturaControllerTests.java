package eci.cvds.Parcial.ECICredit.controllerTests;


import eci.cvds.Parcial.ECICredit.controller.FacturaController;
import eci.cvds.Parcial.ECICredit.model.Factura;
import eci.cvds.Parcial.ECICredit.model.Product;
import eci.cvds.Parcial.ECICredit.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacturaControllerTests {

    @Mock
    private FacturaService facturaService;

    @InjectMocks
    private FacturaController facturaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postFactura_Success() {
        String userEmail = "juan@example.com";
        List<Product> products = Arrays.asList(new Product("Teclado", 100, 1));
        int montoIngresado = 100;
        LocalDate fechaCompra = LocalDate.of(2025, 3, 27);
        Factura facturaMock = new Factura("1L", products, fechaCompra);

        when(facturaService.procesarPagoFactura(userEmail, products, montoIngresado, fechaCompra)).thenReturn(facturaMock);

        ResponseEntity<Factura> response = facturaController.postFactura(userEmail, products, montoIngresado, fechaCompra);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(facturaMock, response.getBody());
        verify(facturaService, times(1)).procesarPagoFactura(userEmail, products, montoIngresado, fechaCompra);
    }

    @Test
    void postFactura_ShouldReturnNotFound_WhenIllegalArgumentExceptionThrown() {
        String email = "test@example.com";
        List<Product> products = List.of(new Product("Laptop", 1000, 1));
        int montoIngresado = 900;
        LocalDate fechaCompra = LocalDate.now();

        when(facturaService.procesarPagoFactura(email, products, montoIngresado, fechaCompra))
                .thenThrow(new IllegalArgumentException("Monto incorrecto"));

        ResponseEntity<Factura> response = facturaController.postFactura(email, products, montoIngresado, fechaCompra);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getFacturasPorUsuario_Success() {
        String email = "juan@example.com";
        List<Product> products = Arrays.asList(new Product("Teclado", 100, 1));
        List<Factura> facturasMock = Arrays.asList(new Factura("001", products, LocalDate.now()));

        when(facturaService.obtenerPagosPorEmail(email)).thenReturn(facturasMock);

        ResponseEntity<List<Factura>> response = facturaController.getFacturasPorUsuario(email);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(facturasMock, response.getBody());
        verify(facturaService, times(1)).obtenerPagosPorEmail(email);
    }

    @Test
    void actualizarProductoEnFactura_Success() {
        String facturaId = "1";
        List<Product> products = Arrays.asList(new Product("Mouse", 50, 2));
        Factura facturaMock = new Factura("1L", products, LocalDate.now());

        when(facturaService.updateProductosFactura(facturaId, products)).thenReturn(facturaMock);

        ResponseEntity<Factura> response = facturaController.actualizarProductoEnFactura(facturaId, products);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(facturaMock, response.getBody());
    }

    @Test
    void actualizarProductoEnFactura_Failure() {
        String facturaId = "1";
        List<Product> products = Arrays.asList(new Product("Mouse", 50, 2));

        when(facturaService.updateProductosFactura(facturaId, products)).thenThrow(new IllegalArgumentException("Factura no encontrada"));

        ResponseEntity<Factura> response = facturaController.actualizarProductoEnFactura(facturaId, products);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
