package eci.cvds.Parcial.ECICredit.model;

public class Product {

    private String name;
    private int value;
    private int amount;

    public Product(String nombre, int value, int amount) {
        this.name = nombre;
        this.value = value;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalValue() {
        return value * amount;
    }
}
