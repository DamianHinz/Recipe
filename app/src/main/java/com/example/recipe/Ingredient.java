package com.example.recipe;

public class Ingredient {
    private String name;
    private String unit;
    private double amount;
    private int dataNumber;

    public Ingredient() {

    }

    public Ingredient(String name, String unit, double amount, int dataNumber) {
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.dataNumber = dataNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDataNumber() { return dataNumber; }

    public void setDataNumber(int dataNumber) { this.dataNumber = dataNumber; }

    @Override
    public String toString(){
        return name + " " + amount + " " + unit ;
    }
}
