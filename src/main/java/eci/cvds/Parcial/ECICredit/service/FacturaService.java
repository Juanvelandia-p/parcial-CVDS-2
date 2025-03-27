package eci.cvds.Parcial.ECICredit.service;

import eci.cvds.Parcial.ECICredit.model.Factura;
import eci.cvds.Parcial.ECICredit.model.Product;
import eci.cvds.Parcial.ECICredit.model.User;
import eci.cvds.Parcial.ECICredit.mongoConnection.FacturasRepository;
import eci.cvds.Parcial.ECICredit.mongoConnection.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturasRepository facturasRepository;
    @Autowired
    private UserRepository userRepository;

    public Factura procesarPagoFactura(String userEmail, List<Product> articulos, int montoIngresado, LocalDate fechaCompra) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario relacionado con este email.");
        }

        Factura pago = new Factura(user.get().getId(),articulos, fechaCompra);

        if (pago.getTotalAmount() != montoIngresado && montoIngresado >= 0) {
            pago.setEstado(false);
            throw new IllegalArgumentException("El monto ingresado no coincide con el total de la compra");
        }

        pago.setUserId(user.get().getId());
        facturasRepository.save(pago);
        user.get().agregarPago(pago.getId());
        userRepository.save(user.get());

        return pago;
    }

    public List<Factura> obtenerPagosPorEmail(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario relacionado con este email.");
        }
        return facturasRepository.findAll()
                .stream()
                .filter(p -> p.getUserId().equals(user.get().getId()))
                .toList();
    }

    public Factura updateProductosFactura(String facturaId, List<Product> products) {
        Factura factura = facturasRepository.findByFacturaId(facturaId);
        if (factura == null) {
            throw new IllegalArgumentException("No existe factura con el id indicado.");
        }
        factura.setProducts(products);
        return facturasRepository.save(factura);
    }
}
