package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TileHorta{

    // Estados de cada Tile da horta
    public enum Estado {
        NORMAL, //Solo normal
        ARADA, //Pós interagir com enchada
        SEMEADA, //Tile semeado
        MOLHADA, //Regada e Estágio de crescimento 1
        PRONTA //Pronta pra colher
    }

    public final Rectangle area;
    public Estado estado;

    public TileHorta(float x, float y, float tamanho) {
        this.area = new Rectangle (x, y, tamanho, tamanho);
        this.estado = Estado.NORMAL;
    }

    //Avança para o próximo estado da planta quando o player interage
    public void interagir() {
        switch (estado) {
            case NORMAL: estado = Estado.ARADA; break;
            case ARADA: estado = Estado.SEMEADA; break;
            case SEMEADA: estado = Estado.MOLHADA; break;
            case MOLHADA: estado = Estado.PRONTA; break;
            case PRONTA: estado = Estado.NORMAL; break;
        }
    }

    // Desenha o sprite
    public void desenhar(SpriteBatch batch, Texture tNormal, Texture tArada, Texture tSemeada, Texture tMolhada, Texture tPronta) {
        Texture sprite;
        switch (estado) {
             case ARADA:  sprite = tArada;   break;
            case SEMEADA: sprite = tSemeada; break;
            case MOLHADA: sprite = tMolhada; break;
            case PRONTA:  sprite = tPronta;  break;
            default:      sprite = tNormal;  break;
        }
        batch.draw(sprite, area.x, area.y, area.width, area.height);
    }
}