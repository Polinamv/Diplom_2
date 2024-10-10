package org.example;

import java.util.List;

public class OrderRequestBody {
    private List<String> ingredients;

    public OrderRequestBody() {
    }

    public OrderRequestBody(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}