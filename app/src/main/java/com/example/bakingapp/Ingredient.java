package com.example.bakingapp;

public class Ingredient {

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(double quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
