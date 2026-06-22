package io.github.some_example_name.model;

import java.util.HashMap;
import java.util.Map;

public class Bag {
    private final Map<String, Integer> itens = new HashMap<>();

    public void adicionar(String itemId, int quantidade) {
        itens.merge(itemId, quantidade, Integer::sum);
    }

    public boolean remover(String itemId, int quantidade) {
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

    public int getQuantidade(String itemId) {
        return itens.getOrDefault(itemId, 0);
    }

    public boolean temItem(String itemId, int quantidade) {
        return getQuantidade(itemId) >= quantidade;
    }

    public Map<String, Integer> getItens() { 
        return itens; 
    }
}