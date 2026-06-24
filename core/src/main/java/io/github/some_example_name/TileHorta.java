package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.enums.PlantState;
import io.github.some_example_name.model.Plant;
import io.github.some_example_name.model.PlantInstance;

public class TileHorta {

    // Estados do tile
    public enum EstadoTile {
        NORMAL,    // terra normal
        ARADA,     // arada, pronta para receber semente
        PLANTADA   // tem uma planta, delega o estado para o PlantInstance
    }

    public final Rectangle area;
    private EstadoTile estado;
    private PlantInstance planta; // null se ainda não plantou

    public TileHorta(float x, float y, float tamanho) {
        this.area    = new Rectangle(x, y, tamanho, tamanho);
        this.estado  = EstadoTile.NORMAL;
        this.planta  = null;
    }

    // Ações

    public boolean podeArar() {
        return estado == EstadoTile.NORMAL;
    }

    public void arar() {
        if (podeArar()) estado = EstadoTile.ARADA;
    }

    public boolean podePlantar() {
        return estado == EstadoTile.ARADA;
    }

    public void plantar(Plant plant) {
        if (podePlantar()) {
            this.planta = new PlantInstance(plant);
            this.estado = EstadoTile.PLANTADA;
        }
    }

    public boolean podeRegar() {
        return planta != null && planta.getState() == PlantState.PEDINDO_AGUA;
    }

    public void regar() {
        if (podeRegar()) planta.darAgua();
    }

    public boolean podeColher() {
        return planta != null && planta.getState() == PlantState.PRONTA;
    }

    // Retorna a planta colhida (ou null se não dá pra colher) e reseta o tile
    public Plant colher() {
        if (!podeColher()) return null;
        Plant colhida = planta.getPlant();
        this.planta = null;
        this.estado = EstadoTile.NORMAL; // após colher, volta a precisar arar
        return colhida;
    }

    // Atualiza a cada frame

    public void update(float delta) {
        if (planta != null) planta.update(delta);
    }

    // === Interação genérica (tecla E) ===
    // Retorna true se houve alguma ação (útil para feedback)
    public boolean interagir(Plant plantaParaSemear) {
        if (podeArar())   { arar();   return true; }
        if (podePlantar() && plantaParaSemear != null) {
            plantar(plantaParaSemear); return true;
        }
        if (podeRegar())  { regar();  return true; }
        if (podeColher()) { colher(); return true; }
        return false;
    }

    // Renderização

    public void desenhar(SpriteBatch batch,
                         Texture tNormal, Texture tArada,
                         Texture tSemeada, Texture tMolhada, Texture tPronta) {
        Texture sprite;

        switch (estado) {
            case ARADA:
                sprite = tArada;
                break;
            case PLANTADA:
                // Planta presente, escolhe sprite pelo estado da planta
                switch (planta.getState()) {
                    case PEDINDO_AGUA: sprite = tSemeada; break; // antes de regar
                    case PRONTA:       sprite = tPronta;  break;
                    case PLANTADA:
                    default:
                        // Se já foi regada, mostra "molhada" (crescendo)
                        // senão mostra "semeada" (acabou de plantar)
                        sprite = tMolhada;
                        break;
                }
                break;
            default:
                sprite = tNormal;
        }

        batch.draw(sprite, area.x, area.y, area.width, area.height);
    }

    // Getters para debug e UI futura
    public EstadoTile getEstadoTile() { return estado; }
    public PlantInstance getPlanta()  { return planta; }
}