package eci.cvds.Parcial.ECICredit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Document (collection = "Facturas")
public class Factura {

    @Id
    private String id;
    private String facturaId;
    private List<Product> products;
    private int totalAmount;
    private boolean estado;
    private LocalDate fechaCompra;
    private String userId;

    public Factura(String userId, List<Product> products, LocalDate fechaCompra) {
        this.facturaId = userId + "-" + UUID.randomUUID().toString();
        this.products = products;
        this.totalAmount = calcularTotalAmount();
        this.userId = userId;
        this.estado = true;
        this.fechaCompra = fechaCompra;
    }

    private int calcularTotalAmount() {
        int total = 0;
        for (Product product : products) {
            total += product.getTotalValue();
        }
        return total;
    }
    public String getId() {
        return id;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public int getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    public boolean getEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public LocalDate getFechaCompra() {
        return fechaCompra;
    }
    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFacturaId() {
        return facturaId;
    }
    public void setFacturaId(String facturaId) {
        this.facturaId = facturaId;
    }
}
