package io.github.some_example_name.model;

import java.util.HashMap;
import java.util.Map;

import io.github.some_example_name.enums.IngredientId;

public class Bag {
    private final Map<IngredientId, Integer> itens = new HashMap<>();

    public void adicionar(IngredientId itemId, int quantidade) {
        itens.merge(itemId, quantidade, Integer::sum);
    }

    public boolean remover(IngredientId itemId, int quantidade) {
        int atual = itens.getOrDefault(itemId, 0);
        if (atual < quantidade) 
            return false; 

        if (atual == quantidade) {
            itens.remove(itemId);
        } 
        else {
            itens.put(itemId, atual - quantidade);
        }
        return true;
    }

    public int getQuantidade(IngredientId itemId) {
        return itens.getOrDefault(itemId, 0);
    }

    public boolean temItem(IngredientId itemId, int quantidade) {
        return getQuantidade(itemId) >= quantidade;
    }

    public Map<IngredientId, Integer> getItens() { 
        return itens; 
    }
}