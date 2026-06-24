package io.github.some_example_name;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // Desenha formas (hitbox de colisão)

import com.badlogic.gdx.Gdx; //Representa uma camada genérica do Tiled
import com.badlogic.gdx.Input; // Representa objetos genéricos do Tiled
import com.badlogic.gdx.Screen; // Representa objetos retangulares do Tiled (colisões)
import com.badlogic.gdx.graphics.Color; // Representa o mapa carregado do Tiled
import com.badlogic.gdx.graphics.GL20; // Obtém dimensões do mapa
import com.badlogic.gdx.graphics.OrthographicCamera; // Carrega o tmx do tiled
import com.badlogic.gdx.graphics.Texture; //Permite display de fonte na tela (descobrir posição do jogador)
import com.badlogic.gdx.graphics.g2d.Animation; //imports para visualizar a hitbox de colisões (testes)
import com.badlogic.gdx.graphics.g2d.BitmapFont; //Lista dinâmica do libGDX. Para armazenar as colisões do mapa
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array; // Ler entradas do teclado

import io.github.some_example_name.enums.IngredientId; //Interface para telas do jogo
import io.github.some_example_name.enums.PlantState;
import io.github.some_example_name.model.Bag;
import io.github.some_example_name.model.Plant;
import io.github.some_example_name.model.Recipe;
import io.github.some_example_name.model.User;
import io.github.some_example_name.registry.PlantRegistry;
import io.github.some_example_name.registry.RecipeRegistry;

public class GameScreen implements Screen {

    // VARIAVEIS E CONSTANTES DO JOGO

    private boolean musicaTocando = true;
    private static final float BTN_W = 50f;
    private static final float BTN_H = 14f;

    private ShapeRenderer shapes; //Desenha formas

    // Câmera e renderização
    private OrthographicCamera camera;
    private SpriteBatch batch;

    // Viewport (controla o zoom)
    private static final float VIEWPORT_WIDTH = 320f;
    private static final float VIEWPORT_HEIGHT = 240f;

    // Tiled Map
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapaRenderer;
    private static final float ESCALA_MAPA = 1f;

    // Colisões do mapa
    private Array<Rectangle> colisoes = new Array<>();

    // Spritesheets do personagem
    private Texture sheetDown, sheetUp, sheetLeft, sheetRight;
    private Animation<TextureRegion> animDown, animUp, animLeft, animRight;
    private Animation<TextureRegion> animAtual;

    // Dimensões do frame
    private static final int FRAME_COLS = 12;
    private static final int FRAME_WIDTH = 24;
    private static final int FRAME_HEIGHT = 24;
    private static final float FRAME_DURATION = 0.1f;

    // Hitbox do personagem
    private Rectangle hitboxPlayer;
    // Hitbox menor que o sprite para ficar mais natural
    private static final float HITBOX_W = 12f;
    private static final float HITBOX_H = 10f;

    // Posição e velocidade
    private float playerX, playerY;
    private static final float VELOCIDADE = 70f;
    private float mapWidth, mapHeight;

    // Controle de animação
    private float stateTime = 0f;
    private boolean movendo = false;

    //Variável para exibir texto na tela (descobrir posição do jogador)
    private BitmapFont font;

    // Sistema de entrada e saída da casa
    private Rectangle zonaEntradaCasa;
    private Rectangle zonaSaidaCasa;
    private boolean dentroDaCasa = false;
    private boolean podeInteragir = false;

    // Zona de interação com os armários
    private Rectangle abrirMenuReceitas;
    private boolean podeAbrirReceitas = false;

    // Horta
    private List<TileHorta> tilesHorta = new ArrayList<>();
    private Texture spriteTerraNormal, spriteTerraArada, spriteTerraSemeada, spriteTerraMolhada, spriteTerraPronta;
    private Map<IngredientId, Texture> spritesSemeadas = new HashMap<>();
    private Map<IngredientId, Texture> spritesMolhadas = new HashMap<>();
    private Map<IngredientId, Texture> spritesProntas = new HashMap<>();

    private PlantRegistry plantRegistry;
    private Bag bag;
    private Plant plantaSelecionada; //Escolhe a planta que vai plantar (0 - 9)

    // Sistema de crafting/menu de vendas
    private Texture menuReceitasTexture;
    private Texture menuBagTexture;
    private RecipeRegistry recipeRegistry;
    private User user;
    private boolean menuReceitasAberto = false;
    private boolean menuBagAberto = false;

    // Mapeamento de teclas 0-9 → receita correspondente (na ordem do Menu.png)
    private IngredientId[] receitasPorTecla;
    private IngredientId[] itensBagPorOrdem;

    //==============================================================

    //FUNÇÕES DO CICLO DE VIDA DO JOGO

    // Inicializa jogo
    //=============================================================
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        batch = new SpriteBatch();
        shapes = new ShapeRenderer(); //Inicializa o Shape Renderer

        // Inicializa fonte para exibir a posição do jogador na tela
        font = new BitmapFont();

        // Carrega o Mapa
        mapa = new TmxMapLoader().load("Exterior.tmx");
        mapaRenderer = new OrthogonalTiledMapRenderer(mapa, ESCALA_MAPA);

        // Dimensões do mapa
        TiledMapTileLayer camada = (TiledMapTileLayer) mapa.getLayers().get(0);
        mapWidth = camada.getWidth() * camada.getTileWidth();
        mapHeight = camada.getHeight() * camada.getTileHeight();

        criarColisoesManuais(); // Cria colisões manuais, sem usar o tiled
        zonaEntradaCasa = new Rectangle(710f, 152f, 18f, 14f); // Cria zona de interação na frente da casa

        // Carrega sprites da horta
        spriteTerraNormal = new Texture("terra_normal.png");
        spriteTerraArada = new Texture("terra_arada.png");
        spriteTerraSemeada = new Texture("terra_semeada.png");
        spriteTerraMolhada = new Texture("terra_molhada.png");
        spriteTerraPronta = new Texture("terra_pronta.png");

        // Cria a grade de tiles plantáveis (9 cols x 6 linhas, 16px cada)
        criarHorta(389f, 200f, 9, 6, 16f);
        plantRegistry = new PlantRegistry();

        //Planta padrão é trigo
        plantaSelecionada = plantRegistry.trigo;
        bag = new Bag();

        recipeRegistry = new RecipeRegistry();
        user = new User("Jogador");
        menuReceitasTexture = carregarTextureOpcional("Menu.png");
        menuBagTexture = carregarTextureOpcional("BagMenu.png");

        receitasPorTecla = new IngredientId[] {
            IngredientId.FARINHA,
            IngredientId.ACUCAR,
            IngredientId.PAO,
            IngredientId.GELEIA_DE_MORANGO,
            IngredientId.TORTA_DE_ABOBORA,
            IngredientId.SANDUICHE,
            IngredientId.SANDUICHE_AMERICANO,
            IngredientId.TORTA_DE_MACA,
            IngredientId.VINHO,
            IngredientId.SUCO_DE_LARANJA,
        };

        itensBagPorOrdem = new IngredientId[] {
            IngredientId.TRIGO,
            IngredientId.CANA,
            IngredientId.MORANGO,
            IngredientId.ABOBORA,
            IngredientId.TOMATE,
            IngredientId.ALFACE,
            IngredientId.AMENDOIM,
            IngredientId.MACA,
            IngredientId.LARANJA,
            IngredientId.UVA,
        };

        carregarSpritesEspecificosDasPlantas();

        // Spritesheets
        sheetDown = new Texture("Walk_Down.png");
        sheetUp = new Texture("Walk_Up.png");
        sheetLeft = new Texture("Walk_Left.png");
        sheetRight = new Texture("Walk_Right.png");

        animDown = criarAnimacao(sheetDown);
        animUp = criarAnimacao(sheetUp);
        animLeft = criarAnimacao(sheetLeft);
        animRight = criarAnimacao(sheetRight);
        animAtual = animDown;

        // Posição inicial e hitbo
        playerX = 722f;
        playerY = 151f;
        hitboxPlayer = new Rectangle(playerX, playerY, HITBOX_W, HITBOX_H);
    }

    private void criarHorta(
        float originX,
        float originY,
        int cols,
        int rows,
        float tamanhoTile
    ) {
        for (int linha = 0; linha < rows; linha++) {
            for (int col = 0; col < cols; col++) {
                float x = originX + col * tamanhoTile;
                float y = originY + linha * tamanhoTile;
                tilesHorta.add(new TileHorta(x, y, tamanhoTile));
            }
        }
    }

    // Carrega os objetos da camada "Collisions" do Tiled
    //==========================================================
    private void carregarColisoes() {
        for (MapLayer layer : mapa.getLayers()) {
            // Pula layers
            if (layer instanceof TiledMapTileLayer) continue;

            // Processa só a camada de colisões
            if (!layer.getName().equals("Collisions")) continue;

            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) object).getRectangle();
                    // Ajusta o tamanho e posição do y(hitbox) do tiled para o personagem

                    float yCorrigido = mapHeight - r.y - r.height;
                    colisoes.add(
                        new Rectangle(r.x, yCorrigido, r.width, r.height)
                    );
                }
            }
        }
        // Contador de colisões, para testes.
        Gdx.app.log(
            "Colisoes",
            "Total de colisoes carregadas: " + colisoes.size
        );
    }

    // Cria colisões via libGDX
    //==========================================================
    private void criarColisoesManuais() {
        // Cercas ao redor da casa
        colisoes.add(new Rectangle(588f, 107f, 118f, 8f)); // Inferior esquerda
        colisoes.add(new Rectangle(584f, 104f, 7f, 137f)); // Esquerda (vertical)
        colisoes.add(new Rectangle(584f, 233f, 204f, 8f)); // Superior
        colisoes.add(new Rectangle(787f, 113f, 6f, 120f)); // Direita (vertical)
        colisoes.add(new Rectangle(733f, 105f, 58f, 8f)); // Inferior direita

        colisoes.add(new Rectangle(620f, 160f, 130f, 70f)); //Casa

        //Bordas Inferiores
        colisoes.add(new Rectangle(383.5f, 0.0f, 226.5f, 16.0f));
        colisoes.add(new Rectangle(608.0f, 0.0f, 287.0f, 16.0f));
        colisoes.add(new Rectangle(895.5f, 0.0f, 129.0f, 16.0f));

        // Borda da Direita
        colisoes.add(new Rectangle(1008.5f, 16.0f, 14.5f, 307.0f));

        // Borda superior
        colisoes.add(new Rectangle(777.5f, 320.5f, 245.5f, 16.0f));
        colisoes.add(new Rectangle(437.0f, 320.5f, 340.0f, 16.0f));
        colisoes.add(new Rectangle(368.0f, 320.0f, 67.5f, 15.5f));

        // Borda da Esquerda
        colisoes.add(new Rectangle(368.0f, 180.5f, 15.5f, 140.0f));
        colisoes.add(new Rectangle(367.5f, 176.5f, 16.0f, 3.5f));
        colisoes.add(new Rectangle(367.0f, 0.0f, 16.5f, 176.0f));

        // Cercas da Horta
        colisoes.add(new Rectangle(389f, 184f, 38f, 9f)); // inferior esquerda
        colisoes.add(new Rectangle(486f, 184f, 47f, 9f)); // inferior direita
        colisoes.add(new Rectangle(530f, 193f, 6f, 137f)); // lateral direita
    }

    private void carregarColisoesInterior() {
        // Paredes
        colisoes.add(new Rectangle(0, 176, 320, 64)); // parede superior
        colisoes.add(new Rectangle(0, 0, 314, 18)); // parede inferior
        colisoes.add(new Rectangle(0, 8, 16, 208)); // parede esquerda
        colisoes.add(new Rectangle(306, 0, 8, 180)); // parede direita
        colisoes.add(new Rectangle(91, 122, 24, 53)); // divisória do quarto

        // Móveis
        colisoes.add(new Rectangle(166, 85, 37, 30)); // mesa redonda
        colisoes.add(new Rectangle(22, 28, 55, 32)); // mesas de canto
        colisoes.add(new Rectangle(259, 167, 39, 13)); // estante de vendas
        colisoes.add(new Rectangle(287, 113, 12, 19)); // sofá

        // ZONA DE SAÍDA
        zonaSaidaCasa = new Rectangle(241f, 20f, 40f, 29f);

        // ZONA DE INTERAÇÃO DAS VENDAS
        abrirMenuReceitas = new Rectangle(250f, 154f, 47f, 21f);
    }

    private void entrarNaCasa() {
        mapa.dispose();
        mapa = new TmxMapLoader().load("Interior.tmx");
        mapaRenderer.setMap(mapa);

        TiledMapTileLayer camada = (TiledMapTileLayer) mapa.getLayers().get(0);
        mapWidth = camada.getWidth() * camada.getTileWidth();
        mapHeight = camada.getHeight() * camada.getTileHeight();

        colisoes.clear();
        carregarColisoesInterior();

        playerX = 262f;
        playerY = 34f;
        dentroDaCasa = true;
    }

    private void abrirMenuReceitas() {
        if (menuBagAberto) {
            return;
        }

        menuReceitasAberto = true;
        Gdx.app.log("Receitas", "Menu de Receitas Aberto!");
    }

    private void fecharMenuReceitas() {
        menuReceitasAberto = false;
        Gdx.app.log("Receitas", "Menu de Receitas Fechado!");
    }

    private void sairDaCasa() {
        mapa.dispose();
        mapa = new TmxMapLoader().load("Exterior.tmx");
        mapaRenderer.setMap(mapa);

        TiledMapTileLayer camada = (TiledMapTileLayer) mapa.getLayers().get(0);
        mapWidth = camada.getWidth() * camada.getTileWidth();
        mapHeight = camada.getHeight() * camada.getTileHeight();

        colisoes.clear();
        criarColisoesManuais();

        playerX = 719f;
        playerY = 145f;
        dentroDaCasa = false;
    }

    private Animation<TextureRegion> criarAnimacao(Texture sheet) {
        TextureRegion[][] tmp = TextureRegion.split(
            sheet,
            FRAME_WIDTH,
            FRAME_HEIGHT
        );
        TextureRegion[] frames = tmp[0];
        return new Animation<>(FRAME_DURATION, frames);
    }

    private Texture carregarTextureOpcional(String nomeArquivo) {
        if (Gdx.files.internal(nomeArquivo).exists()) {
            return new Texture(nomeArquivo);
        }

        Gdx.app.log("UI", "Asset não encontrado: " + nomeArquivo);
        return null;
    }

    private void carregarSpritesEspecificosDasPlantas() {
        for (IngredientId plantaId : itensBagPorOrdem) {
            Texture semeada = carregarSpriteDePlanta(plantaId, "semeada");
            Texture molhada = carregarSpriteDePlanta(plantaId, "molhada");
            Texture pronta = carregarSpriteDePlanta(plantaId, "pronta");

            if (semeada != null) {
                spritesSemeadas.put(plantaId, semeada);
            }
            if (molhada != null) {
                spritesMolhadas.put(plantaId, molhada);
            }
            if (pronta != null) {
                spritesProntas.put(plantaId, pronta);
            }
        }
    }

    private Texture carregarSpriteDePlanta(
        IngredientId plantaId,
        String estado
    ) {
        String nomeBase = plantaId.name().toLowerCase();
        String[] candidatos = new String[] {
            "terra_" + estado + "_" + nomeBase + ".png",
            nomeBase + "_" + estado + ".png",
            nomeBase + "_planta_" + estado + ".png",
            "planta_" + nomeBase + "_" + estado + ".png",
        };

        for (String candidato : candidatos) {
            if (Gdx.files.internal(candidato).exists()) {
                return new Texture(candidato);
            }
        }

        Gdx.app.log(
            "Horta",
            "Sprite específico não encontrado para " +
                plantaId +
                " (" +
                estado +
                ")"
        );
        return null;
    }

    private Texture obterSpriteDaPlanta(TileHorta tile) {
        if (tile.getEstadoTile() == TileHorta.EstadoTile.NORMAL) {
            return spriteTerraNormal;
        }

        if (tile.getEstadoTile() == TileHorta.EstadoTile.ARADA) {
            return spriteTerraArada;
        }

        if (tile.getPlanta() == null) {
            return spriteTerraNormal;
        }

        IngredientId plantaId = tile.getPlanta().getPlant().getId();
        PlantState estadoPlanta = tile.getPlanta().getState();

        switch (estadoPlanta) {
            case PEDINDO_AGUA:
                return spritesSemeadas.getOrDefault(
                    plantaId,
                    spriteTerraSemeada
                );
            case PRONTA:
                return spritesProntas.getOrDefault(plantaId, spriteTerraPronta);
            case PLANTADA:
            default:
                return spritesMolhadas.getOrDefault(
                    plantaId,
                    spriteTerraMolhada
                );
        }
    }

    private void abrirMenuBag() {
        if (menuReceitasAberto) {
            return;
        }

        menuBagAberto = true;
        Gdx.app.log("Bag", "Menu da Bag Aberto!");
    }

    private void fecharMenuBag() {
        menuBagAberto = false;
        Gdx.app.log("Bag", "Menu da Bag Fechado!");
    }

    private void desenharPainelFallback(
        float menuX,
        float menuY,
        float menuW,
        float menuH
    ) {
        batch.end();

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(new Color(0.24f, 0.15f, 0.07f, 0.95f));
        shapes.rect(menuX, menuY, menuW, menuH);
        shapes.setColor(new Color(0.90f, 0.78f, 0.58f, 1f));
        shapes.rect(menuX + 8f, menuY + 8f, menuW - 16f, menuH - 16f);
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(new Color(0.38f, 0.22f, 0.10f, 1f));
        shapes.rect(menuX, menuY, menuW, menuH);
        shapes.rect(menuX + 8f, menuY + 8f, menuW - 16f, menuH - 16f);
        shapes.end();

        batch.begin();
    }

    private void renderizarMenuReceitas() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float menuW = VIEWPORT_WIDTH * 0.8f;
        float menuH = VIEWPORT_HEIGHT * 0.8f;
        float menuX = camera.position.x - menuW / 2f;
        float menuY = camera.position.y - menuH / 2f;

        if (menuReceitasTexture != null) {
            batch.draw(menuReceitasTexture, menuX, menuY, menuW, menuH);
        } else {
            desenharPainelFallback(menuX, menuY, menuW, menuH);
        }

        font.draw(
            batch,
            "Aperte 0-9 para craftar | ESC para fechar | XP: " +
                user.getXp() +
                " Nv " +
                user.getNivel().numero,
            camera.position.x - VIEWPORT_WIDTH / 2f + 5,
            camera.position.y + VIEWPORT_HEIGHT / 2f - 5
        );
        batch.end();
    }

    private void renderizarMenuBag() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float menuW = VIEWPORT_WIDTH * 0.8f;
        float menuH = VIEWPORT_HEIGHT * 0.8f;
        float menuX = camera.position.x - menuW / 2f;
        float menuY = camera.position.y - menuH / 2f;

        if (menuBagTexture != null) {
            batch.draw(menuBagTexture, menuX, menuY, menuW, menuH);
        } else {
            desenharPainelFallback(menuX, menuY, menuW, menuH);
        }

        font.draw(
            batch,
            "Bag | B ou ESC para fechar",
            menuX + 10f,
            menuY + menuH - 8f
        );

        float primeiraLinhaY = menuY + menuH * 0.82f;
        float espacamentoLinha = menuH * 0.095f;
        float contadorX = menuX + menuW * 0.63f;
        Color corOriginal = font.getColor().cpy();
        font.setColor(new Color(0.30f, 0.18f, 0.09f, 1f));

        for (int i = 0; i < itensBagPorOrdem.length; i++) {
            IngredientId itemId = itensBagPorOrdem[i];
            int quantidade = bag.getQuantidade(itemId);
            float linhaY = primeiraLinhaY - i * espacamentoLinha;

            if (menuBagTexture != null) {
                font.draw(batch, "x" + quantidade, contadorX, linhaY);
            } else {
                font.draw(
                    batch,
                    itemId.name().replace('_', ' ') + ": " + quantidade,
                    menuX + 16f,
                    linhaY
                );
            }
        }

        font.setColor(corOriginal);
        batch.end();
    }

    private void tentarCraftar() {
        int[] teclas = {
            Input.Keys.NUM_0,
            Input.Keys.NUM_1,
            Input.Keys.NUM_2,
            Input.Keys.NUM_3,
            Input.Keys.NUM_4,
            Input.Keys.NUM_5,
            Input.Keys.NUM_6,
            Input.Keys.NUM_7,
            Input.Keys.NUM_8,
            Input.Keys.NUM_9,
        };

        for (int i = 0; i < teclas.length; i++) {
            if (Gdx.input.isKeyJustPressed(teclas[i])) {
                IngredientId id = receitasPorTecla[i];
                // Verifica se o nível do user permite essa receita
                if (!user.desbloqueou(id)) {
                    Gdx.app.log(
                        "Crafting",
                        "Receita " + id + " bloqueada — nível insuficiente."
                    );
                    return;
                }
                Recipe craftada = recipeRegistry.craftar(id, bag);
                if (craftada != null) {
                    user.addXp(craftada.getXpReward());
                    Gdx.app.log(
                        "Crafting",
                        "Craftou " +
                            craftada.getNome() +
                            " (+" +
                            craftada.getXpReward() +
                            " XP) " +
                            "| Total XP: " +
                            user.getXp() +
                            " | Nível: " +
                            user.getNivel().numero
                    );
                } else {
                    Gdx.app.log("Crafting", "Faltam ingredientes para " + id);
                }
                return;
            }
        }
    }

    // Renderiza o jogo
    // ============================================================
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        movendo = false;

        //Atualiza crescimento das plantas
        for (TileHorta tile : tilesHorta) {
            tile.update(delta);
        }

        if (menuReceitasAberto) {
            if (
                Gdx.input.isKeyJustPressed(Input.Keys.E) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            ) {
                fecharMenuReceitas();
            }
            tentarCraftar();

            renderizarMundo(delta, false);
            renderizarMenuReceitas();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            if (menuBagAberto) {
                fecharMenuBag();
            } else {
                abrirMenuBag();
            }
        }

        if (menuBagAberto) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                fecharMenuBag();
            }

            renderizarMundo(delta, false);
            renderizarMenuBag();
            return;
        }

        renderizarMundo(delta, true);
    }

    // Renderização e lógica do mundo
    // atualizar = false: só renderiza o mundo congelado (usado quando o menu está aberto)
    // atualizar = true:  roda input, movimento, colisões e interações normalmente
    // ============================================================
    private void renderizarMundo(float delta, boolean atualizar) {
        // Guarda posição anterior para reverter se colidir
        float anteriorX = playerX;
        float anteriorY = playerY;

        if (atualizar) {
            // Movimentação WASD
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

            if (!dentroDaCasa) {
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
                ) plantaSelecionada = plantRegistry.trigo;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)
                ) plantaSelecionada = plantRegistry.cana;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)
                ) plantaSelecionada = plantRegistry.morango;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)
                ) plantaSelecionada = plantRegistry.abobora;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)
                ) plantaSelecionada = plantRegistry.tomate;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)
                ) plantaSelecionada = plantRegistry.alface;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)
                ) plantaSelecionada = plantRegistry.amendoim;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)
                ) plantaSelecionada = plantRegistry.maca;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)
                ) plantaSelecionada = plantRegistry.laranja;
                if (
                    Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)
                ) plantaSelecionada = plantRegistry.uva;
            }
        }

        // Atualiza hitbox na nova posição
        // Centraliza a hitbox nos pés do personagem
        hitboxPlayer.setPosition(playerX - HITBOX_W / 2f, playerY - HITBOX_H);

        if (atualizar) {
            // Verifica colisão com os objetos do mapa
            for (Rectangle obstaculo : colisoes) {
                if (hitboxPlayer.overlaps(obstaculo)) {
                    // Reverte para a posição anterior se colidiu
                    playerX = anteriorX;
                    playerY = anteriorY;
                    hitboxPlayer.setPosition(
                        playerX - HITBOX_W / 2f,
                        playerY - HITBOX_H
                    );
                    break;
                }
            }

            // Limites do mapa (não sai pela borda)
            playerX = Math.max(
                HITBOX_W / 2f,
                Math.min(playerX, mapWidth - HITBOX_W / 2f)
            );
            playerY = Math.max(
                HITBOX_H,
                Math.min(playerY, mapHeight - HITBOX_H)
            );

            // Verifica se pode sair ou entrar na casa
            boolean podeEntrar =
                !dentroDaCasa && hitboxPlayer.overlaps(zonaEntradaCasa);
            boolean podeSair =
                dentroDaCasa && hitboxPlayer.overlaps(zonaSaidaCasa);
            podeInteragir = podeEntrar || podeSair || podeAbrirReceitas;

            podeAbrirReceitas =
                dentroDaCasa && hitboxPlayer.overlaps(abrirMenuReceitas);

            if (podeEntrar && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                entrarNaCasa();
            }
            if (podeSair && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                sairDaCasa();
            }

            if (podeAbrirReceitas && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                abrirMenuReceitas();
            }
            //Interação pra plantar
            if (!dentroDaCasa && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                for (TileHorta tile : tilesHorta) {
                    if (hitboxPlayer.overlaps(tile.area)) {
                        boolean colheu = tile.podeColher();
                        Plant resultado = colheu ? tile.colher() : null;

                        if (!colheu) {
                            // Usa a planta selecionada pelo teclado
                            tile.interagir(plantaSelecionada);
                        } else {
                            if (resultado != null) {
                                bag.adicionar(resultado.getId(), 1);
                                Gdx.app.log(
                                    "Horta",
                                    "Colheu " +
                                        resultado.getNome() +
                                        " (total: " +
                                        bag.getQuantidade(resultado.getId()) +
                                        ")"
                                );
                            } else {
                            }
                        }
                        break;
                    }
                }
            }
        }

        // Frame da animação
        TextureRegion frameAtual;
        if (movendo) {
            frameAtual = animAtual.getKeyFrame(stateTime, true);
        } else {
            frameAtual = animAtual.getKeyFrame(0);
            stateTime = 0f;
        }

        // Câmera segue o personagem
        camera.position.set(playerX, playerY, 0);
        camera.update();

        // Renderiza mapa
        mapaRenderer.setView(camera);
        mapaRenderer.render();

        // ── Renderiza personagem
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Desenha tiles da horta (no exterior)
        if (!dentroDaCasa) {
            for (TileHorta tile : tilesHorta) {
                Texture spriteTile = obterSpriteDaPlanta(tile);
                batch.draw(
                    spriteTile,
                    tile.area.x,
                    tile.area.y,
                    tile.area.width,
                    tile.area.height
                );
            }
        }

        //Desenha personagem
        float escala = 1f;
        batch.draw(
            frameAtual,
            playerX - (FRAME_WIDTH * escala) / 2f,
            playerY - (FRAME_HEIGHT * escala) / 2f,
            FRAME_WIDTH * escala,
            FRAME_HEIGHT * escala
        );
        font.draw(
            batch,
            "X:" + (int) playerX + " Y:" + (int) playerY,
            playerX - 40,
            playerY + 30
        );
        if (podeInteragir) {
            font.draw(batch, "Pressione E", playerX - 30, playerY + 45);
        }
        if (!dentroDaCasa) {
            font.draw(
                batch,
                "Planta: " + plantaSelecionada.getNome(),
                playerX - 40,
                playerY + 60
            );
        }
        batch.end();

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        // Desenha hitbox do jogador
        for (Rectangle obstaculo : colisoes) {
            shapes.rect(
                obstaculo.x,
                obstaculo.y,
                obstaculo.width,
                obstaculo.height
            );
        }
        shapes.setColor(Color.LIME);
        shapes.rect(
            hitboxPlayer.x,
            hitboxPlayer.y,
            hitboxPlayer.width,
            hitboxPlayer.height
        );
        shapes.end();
    }

    // Redimensiona Camera
    //=============================================================
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    //Libera recursos
    //=============================================================
    @Override
    public void dispose() {
        batch.dispose();
        mapa.dispose();
        mapaRenderer.dispose();
        sheetDown.dispose();
        sheetUp.dispose();
        sheetLeft.dispose();
        sheetRight.dispose();
        shapes.dispose();
        spriteTerraNormal.dispose();
        spriteTerraArada.dispose();
        spriteTerraSemeada.dispose();
        spriteTerraMolhada.dispose();
        spriteTerraPronta.dispose();

        for (Texture texture : spritesSemeadas.values()) {
            texture.dispose();
        }
        for (Texture texture : spritesMolhadas.values()) {
            texture.dispose();
        }
        for (Texture texture : spritesProntas.values()) {
            texture.dispose();
        }

        font.dispose();

        if (menuReceitasTexture != null) {
            menuReceitasTexture.dispose();
        }
        if (menuBagTexture != null) {
            menuBagTexture.dispose();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
