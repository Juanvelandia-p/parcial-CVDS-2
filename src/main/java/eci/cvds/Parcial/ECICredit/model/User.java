package eci.cvds.Parcial.ECICredit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private List<String> facturasIds;

    public User(String nombre, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = nombre;
        this.email = email;
        this.facturasIds = new ArrayList<>();
    }

    public void agregarPago(String pagoId) {
        this.facturasIds.add(pagoId);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getFacturasIds() {
        return facturasIds;
    }
    public void setFacturasIds(List<String> facturasIds) {
        this.facturasIds = facturasIds;
    }
}
