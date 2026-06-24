package io.github.some_example_name.enums;

import java.util.List;

public enum Level {
    NIVEL_1  (1,    0,  List.of(IngredientId.TRIGO),    List.of(IngredientId.FARINHA)),
    NIVEL_2  (2,   10,  List.of(IngredientId.CANA),     List.of(IngredientId.ACUCAR, IngredientId.PAO)),
    NIVEL_3  (3,   20,  List.of(IngredientId.MORANGO),  List.of(IngredientId.GELEIA_DE_MORANGO)),
    NIVEL_4  (4,   40,  List.of(IngredientId.ABOBORA),  List.of(IngredientId.TORTA_DE_ABOBORA)),
    NIVEL_5  (5,   80,  List.of(IngredientId.TOMATE),   List.of()),
    NIVEL_6  (6,  160,  List.of(IngredientId.ALFACE),   List.of(IngredientId.SANDUICHE)),
    NIVEL_7  (7,  320,  List.of(IngredientId.AMENDOIM), List.of(IngredientId.SANDUICHE_AMERICANO)),
    NIVEL_8  (8,  640,  List.of(IngredientId.MACA),     List.of(IngredientId.TORTA_DE_MACA)),
    NIVEL_9  (9,  800,  List.of(IngredientId.LARANJA),  List.of(IngredientId.SUCO_DE_LARANJA)),
    NIVEL_10 (10, 1000, List.of(IngredientId.UVA),      List.of(IngredientId.VINHO));

    public final int numero;
    public final int xpNecessario;
    public final List<IngredientId> plantasDesbloqueadas;
    public final List<IngredientId> receitasDesbloqueadas;

    Level(int numero, int xpNecessario, List<IngredientId> plantasDesbloqueadas, List<IngredientId> receitasDesbloqueadas) {
        this.numero = numero;
        this.xpNecessario = xpNecessario;
        this.plantasDesbloqueadas = plantasDesbloqueadas;
        this.receitasDesbloqueadas = receitasDesbloqueadas;
    }

    public Level proximo() {
        int idx = this.ordinal() + 1;
        return idx < values().length ? values()[idx] : null;
    }

}