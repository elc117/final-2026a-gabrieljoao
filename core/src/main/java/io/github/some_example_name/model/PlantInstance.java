package io.github.some_example_name.model;

import io.github.some_example_name.enums.PlantState;

public class PlantInstance {
    private final Plant plant;
    private PlantState state;
    private float timerSegundos;
    private boolean foiRegada;

    public PlantInstance(Plant plant) {
        this.plant = plant;
        this.state = PlantState.PLANTADA;
        this.timerSegundos = 0f;
        this.foiRegada = false;
    }

    public void update(float delta) {
        if (state == PlantState.PRONTA) return;

        // Em PEDINDO_AGUA, o timer congela e a planta espera ser regada
        if (state == PlantState.PEDINDO_AGUA) return;

        timerSegundos += delta;

        float metade  = plant.getTempoDeCrescimento() / 2f;
        float total   = plant.getTempoDeCrescimento();

        // Se atingiu o tempo e a planta não foi regada, pede agua
        if (!foiRegada && timerSegundos >= metade) {
            state = PlantState.PEDINDO_AGUA;
            return;
        }

        // Se a planta foi regada e passou o tempo, a planta fica pronta
        if (foiRegada && timerSegundos >= total) {
            state = PlantState.PRONTA;
        }
    }

    public void darAgua() {
        if (state == PlantState.PEDINDO_AGUA) {
            foiRegada = true;
            state = PlantState.PLANTADA; // volta a crescer
        }
    }

    public PlantState getState()  { return state; }
    public Plant      getPlant()  { return plant; }
    public float getProgresso()   { return timerSegundos / plant.getTempoDeCrescimento(); }
}