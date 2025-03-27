package eci.cvds.Parcial.ECICredit.controller;

import eci.cvds.Parcial.ECICredit.model.Factura;
import eci.cvds.Parcial.ECICredit.model.Product;
import eci.cvds.Parcial.ECICredit.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.IllegalFormatException;
import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping
    public ResponseEntity<Factura> postFactura(
            @RequestParam String userEmail,
            @RequestBody List<Product> products,
            @RequestParam int montoIngresado,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaCompra) {
        try{
            Factura factura = facturaService.procesarPagoFactura(userEmail, products, montoIngresado, fechaCompra);
            return ResponseEntity.ok(factura);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/usuarios/{email}")
    public ResponseEntity<List<Factura>> getFacturasPorUsuario(@PathVariable String email) {
        return ResponseEntity.ok(facturaService.obtenerPagosPorEmail(email));
    }

    @PutMapping("/facturas/{facturaId}")
    public ResponseEntity<Factura> actualizarProductoEnFactura(
            @PathVariable String facturaId,
            @RequestBody List<Product> products) {
        try {
            Factura newFactura = facturaService.updateProductosFactura(facturaId, products);
            return ResponseEntity.ok(newFactura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
