package io.github.some_example_name.model;

import java.util.List;

import io.github.some_example_name.enums.PlantGrowth;

public class Plant extends Ingredient {
    private final PlantGrowth tempoDeCrescimento;
    private final List<Tool> ferramentasNecessarias;

    public Plant(int id, String nome, PlantGrowth tempoDeCrescimento, List<Tool> ferramentasNecessarias) {
        super(id, nome);
        this.tempoDeCrescimento = tempoDeCrescimento;
        this.ferramentasNecessarias = ferramentasNecessarias;
    }

    public float getTempoDeCrescimento() { 
        return tempoDeCrescimento.growthTimeSeconds; 
    }

    public List<Tool> getFerramentasNecessarias() { 
        return ferramentasNecessarias; 
    }

    //será que eu consigo dar commit assim?
    
}