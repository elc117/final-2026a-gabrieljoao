package io.github.some_example_name.model;

import io.github.some_example_name.enums.PlantState;

public class PlantInstance{
    private final Plant plant;
    private PlantState state; 
    private float timerSegundos;   

    public PlantInstance(Plant plant) {
        this.plant = plant;
        this.state = PlantState.PLANTADA;
        this.timerSegundos = 0f;

        //se precisa de ferramenta antes, começa pedindo
        //boolean precisaFerramenta = !plant.getFerramentasNecessarias().isEmpty();
        //this.state = precisaFerramenta ? PlantState.PEDINDO_AGUA : PlantState.PLANTADA;
        //this.timerSegundos = 0f;
    }

    // chamado a cada frame
    public void update(float delta) {
        if (state != PlantState.PLANTADA) return;

        timerSegundos += delta;

        if (timerSegundos >= plant.getTempoDeCrescimento()) {
            state = PlantState.PRONTA;
        }
    }

    public void darAgua() {
        if (state == PlantState.PEDINDO_AGUA) {
            state = PlantState.PLANTADA;
        }
    }

    public PlantState getState() { return state; }
    public Plant getPlant() { return plant; }
    public float getProgresso() { return timerSegundos / plant.getTempoDeCrescimento(); }
}
