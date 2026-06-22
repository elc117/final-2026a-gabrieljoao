package io.github.some_example_name.registry;

import io.github.some_example_name.enums.IngredientId;
import io.github.some_example_name.enums.PlantGrowth;
import io.github.some_example_name.model.Plant;

public class PlantRegistry{
    private final ToolRegistry toolRegistry = new ToolRegistry();

    public final Plant trigo = new Plant(IngredientId.TRIGO, "TRIGO", PlantGrowth.GRASS, toolRegistry.trigoTools);
    public final Plant cana = new Plant(IngredientId.CANA, "CANA", PlantGrowth.GRASS, toolRegistry.canaTools);
    public final Plant morango = new Plant(IngredientId.MORANGO, "MORANGO", PlantGrowth.CREEPING, toolRegistry.morangoTools);
    public final Plant abobora = new Plant(IngredientId.ABOBORA, "ABOBORA", PlantGrowth.CREEPING, toolRegistry.aboboraTools);
    public final Plant tomate = new Plant(IngredientId.TOMATE, "TOMATE", PlantGrowth.SUPPORT, toolRegistry.tomateTools);
    public final Plant alface = new Plant(IngredientId.ALFACE, "ALFACE", PlantGrowth.BUSH, toolRegistry.alfaceTools);
    public final Plant amendoim = new Plant(IngredientId.AMENDOIM, "AMENDOIM", PlantGrowth.BUSH, toolRegistry.amendoimTools);
    public final Plant maca = new Plant(IngredientId.MACA, "MACA", PlantGrowth.TREE, toolRegistry.macaTools);
    public final Plant laranja = new Plant(IngredientId.LARANJA, "LARANJA", PlantGrowth.TREE, toolRegistry.laranjaTools);
    public final Plant uva = new Plant(IngredientId.UVA, "UVA", PlantGrowth.SUPPORT, toolRegistry.uvaTools);
}
