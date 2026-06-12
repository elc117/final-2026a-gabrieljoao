package io.github.some_example_name.model;

import java.util.List;

public class Plant extends Ingredient {
    private final int tempoDeCrescimento;
    private final List<Tool> ferramentasNecessarias;

    public Plant(int id, String nome, int tempoDeCrescimento, List<Tool> ferramentasNecessarias) {
        super(id, nome);
        this.tempoDeCrescimento = tempoDeCrescimento;
        this.ferramentasNecessarias = ferramentasNecessarias;
    }

    public int getTempoDeCrescimento() { 
        return tempoDeCrescimento; 
    }

    public List<Tool> getFerramentasNecessarias() { 
        return ferramentasNecessarias; 
    }

    public Ingredient trigo(){
        return new Ingredient(1, "TRIGO", );
    }

    public Ingredient cana(){
        return new Ingredient(2, "CANA");
    }

    public Ingredient morango(){
        return new Ingredient(3, "MORANGO");
    }

    public Ingredient abobora(){
        return new Ingredient(4, "ABOBORA");
    }

    public Ingredient tomate(){
        return new Ingredient(5, "TOMATE");
    }
}