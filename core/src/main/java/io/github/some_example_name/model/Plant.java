package io.github.some_example_name.model;

import java.util.ArrayList;
import io.github.some_example_name.enums.PlantGrowth;

public class Plant {
    private int id;
    private String name;
    private PlantGrowth GrowthTime;
    private int unlockLevel;

    private ArrayList<Recipe> recipes;
    private ArrayList<Tool> tools;


}