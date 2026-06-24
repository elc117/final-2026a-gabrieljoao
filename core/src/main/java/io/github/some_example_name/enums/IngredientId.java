package io.github.some_example_name.enums;

public enum IngredientId {
    // Plants
    TRIGO(1),
    CANA(2),
    MORANGO(3),
    ABOBORA(4),
    TOMATE(5),
    ALFACE(6),
    AMENDOIM(7),
    MACA(8),
    LARANJA(9),
    UVA(10),

    // Recipes
    FARINHA(11),
    PAO(12),
    ACUCAR(13),
    GELEIA_DE_MORANGO(14),
    
    TORTA_DE_ABOBORA(15),
    
    SANDUICHE(16),
    
    SANDUICHE_AMERICANO(17),
    SUCO_DE_LARANJA(18),
    TORTA_DE_MACA(19),
    
   
    VINHO(20);

    public final int value;

    IngredientId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
