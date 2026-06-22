package io.github.some_example_name.model;

import io.github.some_example_name.enums.IngredientId;

public class Ingredient {
    private final IngredientId id;
    private final String nome;

    public Ingredient(IngredientId id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public IngredientId getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome;
    }

}
