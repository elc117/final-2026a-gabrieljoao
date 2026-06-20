package io.github.some_example_name.registry;

import io.github.some_example_name.enums.PlantGrowth;
import io.github.some_example_name.model.Plant;

public class PlantRegistry{
    private final ToolRegistry toolRegistry = new ToolRegistry();

    public final Plant trigo = new Plant(1, "TRIGO", PlantGrowth.GRASS, toolRegistry.trigoTools);
    public final Plant cana = new Plant(2, "CANA", PlantGrowth.GRASS, toolRegistry.canaTools);
    public final Plant morango = new Plant(3, "MORANGO", PlantGrowth.CREEPING, toolRegistry.morangoTools);
    public final Plant abobora = new Plant(4, "ABOBORA", PlantGrowth.CREEPING, toolRegistry.aboboraTools);
    public final Plant tomate = new Plant(5, "TOMATE", PlantGrowth.SUPPORT, toolRegistry.tomateTools);
    public final Plant alface = new Plant(6, "ALFACE", PlantGrowth.BUSH, toolRegistry.alfaceTools);
    public final Plant amendoim = new Plant(7, "AMENDOIM", PlantGrowth.BUSH, toolRegistry.amendoimTools);
    public final Plant maca = new Plant(8, "MACA", PlantGrowth.TREE, toolRegistry.macaTools);
    public final Plant laranja = new Plant(9, "LARANJA", PlantGrowth.TREE, toolRegistry.laranjaTools);
    public final Plant uva = new Plant(10, "UVA", PlantGrowth.SUPPORT, toolRegistry.uvaTools);
}
