package io.github.some_example_name.model;

import java.util.List;
import java.util.stream.Collectors;

public class Plant extends Ingredient {
    private final int tempoDeCrescimento; // em dias
    private final List<Tool> ferramentasNecessarias;

    public Plant(int id, String nome, int tempoDeCrescimento, List<Tool> ferramentasNecessarias) {
        super(id, nome);
        this.tempoDeCrescimento = tempoDeCrescimento;
        this.ferramentasNecessarias = ferramentasNecessarias;
    }

    public int getTempoDeCrescimento() { return tempoDeCrescimento; }
    public List<Tool> getFerramentasNecessarias() { return ferramentasNecessarias; }

    @Override
    public String toString() {
        String ferramentas = ferramentasNecessarias.stream()
            .map(Tool::getNome)
            .collect(Collectors.joining(", "));
        return getNome() + " (cresce em " + tempoDeCrescimento + "d, usa: " + ferramentas + ")";
    }
}