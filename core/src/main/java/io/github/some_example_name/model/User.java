package io.github.some_example_name.model;

import io.github.some_example_name.enums.IngredientId;
import io.github.some_example_name.enums.Level;

public class User {
    private final String nome;
    private Level nivel;
    private int xp;

    public User(String nome) {
        this.nome = nome;
        this.nivel = Level.NIVEL_1;
        this.xp = 0;
    }

    /** Adiciona XP e sobe de nível automaticamente se atingir o necessário. */
    public void addXp(int quantidade) {
        xp += quantidade;

        Level proximo = nivel.proximo();
        while (proximo != null && xp >= proximo.xpNecessario) {
            nivel = proximo;
            proximo = nivel.proximo();
            System.out.println("Subiu para " + nivel);
        }
    }

    /** Retorna true se o nível do usuário é suficiente para usar o item. */
    public boolean desbloqueou(IngredientId id) {
        for (Level l : Level.values()) {
            if (l.plantasDesbloqueadas.contains(id) || l.receitasDesbloqueadas.contains(id)) {
                return nivel.numero >= l.numero;
            }
        }
        return false;
    }

    public Level getNivel(){ 
        return nivel; 
    }

    public int getXp(){ 
        return xp; 
    }
}