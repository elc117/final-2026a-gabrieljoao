package io.github.some_example_name.registry;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.some_example_name.enums.IngredientId;
import io.github.some_example_name.model.Bag;
import io.github.some_example_name.model.Ingredient;
import io.github.some_example_name.model.Recipe;

public class RecipeRegistry{
    private final PlantRegistry plantRegistry = new PlantRegistry();
    private final Map<IngredientId, Recipe> receitas = new HashMap<>();

    public final Recipe farinha = new Recipe(IngredientId.FARINHA, "FARINHA", 3, List.of(plantRegistry.trigo));
    public final Recipe acucar = new Recipe(IngredientId.ACUCAR, "ACUCAR", 5, List.of(plantRegistry.cana));
    public final Recipe sucoLaranja = new Recipe(IngredientId.SUCO_DE_LARANJA, "SUCO-DE-LARANJA", 200, List.of(plantRegistry.laranja));
    public final Recipe saladaFrutas = new Recipe(IngredientId.SALADA_DE_FRUTAS, "SALADA-DE-FRUTAS", 500, List.of(plantRegistry.maca, plantRegistry.laranja, plantRegistry.morango, plantRegistry.uva));
    public final Recipe alcool = new Recipe(IngredientId.ALCOOL, "ALCOOL", 250, List.of(plantRegistry.cana));

    public final Recipe pao = new Recipe(IngredientId.PAO, "PAO", 5, List.of(this.farinha));
    public final Recipe geleiaMorango = new Recipe(IngredientId.GELEIA_DE_MORANGO, "GELEIA-DE-MORANGO", 10, List.of(this.acucar, plantRegistry.morango));
    public final Recipe geleiaAbobora = new Recipe(IngredientId.GELEIA_DE_ABOBORA, "GELEIA-DE-ABOBORA", 20, List.of(this.acucar, plantRegistry.abobora));
    public final Recipe tortaAbobora = new Recipe(IngredientId.TORTA_DE_ABOBORA, "TORTA-DE-ABOBORA", 20, List.of(this.farinha, this.acucar, plantRegistry.abobora));
    public final Recipe molhoTomate = new Recipe(IngredientId.MOLHO_DE_TOMATE, "MOLHO-DE-TOMATE", 40, List.of(this.acucar, plantRegistry.tomate));
    public final Recipe cremeAmendoim = new Recipe(IngredientId.CREME_DE_AMENDOIM, "CREME-DE-AMENDOIM", 80, List.of(this.acucar, plantRegistry.amendoim));
    public final Recipe tortaMaca = new Recipe(IngredientId.TORTA_DE_MACA, "TORTA-DE-MACA", 250, List.of(this.farinha, this.acucar, plantRegistry.maca));

    public final Recipe sanduiche = new Recipe(IngredientId.SANDUICHE, "SANDUICHE", 80, List.of(this.pao, this.molhoTomate, plantRegistry.alface, plantRegistry.tomate));
    public final Recipe americano = new Recipe(IngredientId.SANDUICHE_AMERICANO, "SANDUICHE-AMERICANO", 160, List.of(this.pao, this.cremeAmendoim, this.geleiaMorango));
    public final Recipe vinho = new Recipe(IngredientId.VINHO, "VINHO", 500, List.of(plantRegistry.uva, this.alcool));

    private Recipe criar(IngredientId id, String nome, int xpReward, List<Ingredient> ingredientes) {
        Recipe r = new Recipe(id, nome, xpReward, ingredientes);
        receitas.put(id, r);
        return r;
    }

    public Recipe getRecipe(IngredientId id) {
        return receitas.get(id);
    }

    public boolean craftar(IngredientId id, Bag bag) {
        Recipe receita = getRecipe(id);
        if (receita == null) return false;

        for (Ingredient Ingredient : receita.getIngredientes()) {
            if (!bag.temItem(Ingredient.getId(), 1)) return false;
        }

        for (Ingredient Ingredient : receita.getIngredientes()) {
            bag.remover(Ingredient.getId(), 1);
        }
        bag.adicionar(receita.getId(), 1);
        return true;
    }
}
