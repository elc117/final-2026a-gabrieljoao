package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

    // ── Câmera e renderização ─────────────────────────────────
    private OrthographicCamera camera;
    private SpriteBatch batch;

    // ── Tiled Map ─────────────────────────────────────────────
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapaRenderer;
    private static final float ESCALA_MAPA = 1f; // aumente para 2f ou 3f se quiser zoom

    // ── Spritesheets do personagem ────────────────────────────
    private Texture sheetDown, sheetUp, sheetLeft, sheetRight;
    private Animation<TextureRegion> animDown, animUp, animLeft, animRight;
    private Animation<TextureRegion> animAtual;

    // ── Dimensões do frame (288px ÷ 12 frames = 24x24) ───────
    private static final int FRAME_COLS   = 12;
    private static final int FRAME_WIDTH  = 24;
    private static final int FRAME_HEIGHT = 24;
    private static final float FRAME_DURATION = 0.1f;

    // ── Posição e velocidade do personagem ────────────────────
    private float playerX, playerY;
    private static final float VELOCIDADE = 100f; // pixels/segundo no mundo do mapa

    // ── Controle de animação ──────────────────────────────────
    private float stateTime = 0f;
    private boolean movendo = false;

    // ─────────────────────────────────────────────────────────
    @Override
    public void show() {

        // Câmera ortográfica — tamanho em tiles visíveis na tela
        // Com tiles de 16px, 20x15 tiles = 320x240px (estilo pixel art)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 240);

        batch = new SpriteBatch();

        // ── Carrega o mapa TMX ──
        mapa = new TmxMapLoader().load("Exterior.tmx");
        mapaRenderer = new OrthogonalTiledMapRenderer(mapa, ESCALA_MAPA);

        // ── Carrega spritesheets ──
        sheetDown  = new Texture("Walk_Down.png");
        sheetUp    = new Texture("Walk_Up.png");
        sheetLeft  = new Texture("Walk_Left.png");
        sheetRight = new Texture("Walk_Right.png");

        animDown  = criarAnimacao(sheetDown);
        animUp    = criarAnimacao(sheetUp);
        animLeft  = criarAnimacao(sheetLeft);
        animRight = criarAnimacao(sheetRight);

        animAtual = animDown;

        // ── Posição inicial no mapa (em pixels) ──
        // O mapa começa em coordenadas negativas — spawna perto do centro visível
        playerX = 0;
        playerY = 0;
    }

    private Animation<TextureRegion> criarAnimacao(Texture sheet) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, FRAME_WIDTH, FRAME_HEIGHT);
        TextureRegion[] frames = tmp[0];
        return new Animation<>(FRAME_DURATION, frames);
    }

    // ─────────────────────────────────────────────────────────
    @Override
    public void render(float delta) {
        // Limpa tela
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        movendo = false;

        // ── Movimentação WASD ──
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += VELOCIDADE * delta;
            animAtual = animUp;
            movendo = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= VELOCIDADE * delta;
            animAtual = animDown;
            movendo = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= VELOCIDADE * delta;
            animAtual = animLeft;
            movendo = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += VELOCIDADE * delta;
            animAtual = animRight;
            movendo = true;
        }

        // Frame atual da animação
        TextureRegion frameAtual;
        if (movendo) {
            frameAtual = animAtual.getKeyFrame(stateTime, true);
        } else {
            frameAtual = animAtual.getKeyFrame(0);
            stateTime = 0f;
        }

        // ── Câmera segue o personagem ──
        camera.position.set(playerX, playerY, 0);
        camera.update();

        // ── Renderiza o mapa PRIMEIRO (fundo) ──
        mapaRenderer.setView(camera);
        mapaRenderer.render();

        // ── Renderiza o personagem POR CIMA do mapa ──
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            // Personagem renderizado 2x maior (48x48px) para ficar proporcional ao mapa
            float escalaPersonagem = 2f;
            batch.draw(
                frameAtual,
                playerX - (FRAME_WIDTH  * escalaPersonagem) / 2f,
                playerY - (FRAME_HEIGHT * escalaPersonagem) / 2f,
                FRAME_WIDTH  * escalaPersonagem,
                FRAME_HEIGHT * escalaPersonagem
            );
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 320, 240);
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapa.dispose();
        mapaRenderer.dispose();
        sheetDown.dispose();
        sheetUp.dispose();
        sheetLeft.dispose();
        sheetRight.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}