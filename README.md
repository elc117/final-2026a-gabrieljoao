*JOGO DE FAZENDEIRO - HAPPY FARM*

*INFORMAÇÕES - ALUNOS:*
- João Daniel Würdig Lucas (202510515)
- Gabriel Maroneze Ramos (202512445)
- Curso: Sistemas de Informação

*INFORMAÇÕES - DISCIPLINA:*
- Docente: Andrea Schwertner Charão
- Disciplina: Paradigmas de Programação

*INFORMAÇÕES CONCEITUAIS:*
- O projeto consiste em um jogo single-player com a temática de fazenda (semelhante à jogos conhecidos como Stardew Valley e Harvest Moon).
- Os objetivos do jogador serão plantar sementes e cuidar de suas plantas de acordo com as necessidades de cada uma. Após a colheita, ele poderá fazer receitas com os insumos coletados e vendê-las para somar pontos e desbloquear novas hortaliças/receitas.


*PROGRESSO*

- Dia 28/05/2026: Consegui instalar o libGDX em minha máquina e criar um projeto por lá. Não consegui adicionar os arquivos gerados no diretório do trabalho e fazer o commit corretamente. (Gabriel)

- Dia 29/05, 09:24 da manhã: Ontém após a aula, conversei com o Cadu e utilizei o Claude.AI para aprender a unir os arquivos gerados e realizar o commit no github. Consegui colocar em prática agora de manhã. Não sabia bem quais extensões do libGDX incluir, mas selecionei um número elevado cujos títulos tinham relação com jogos 2D ou me pareciam úteis.
Agora, devo usar o Claude para ter um direcionamento sobre como iniciar o projeto, prentendo aprender como construir um cenário base e incluir um personagem com movimentações funcionais e colisão, para testes. (Gabriel)

- Dia 01/06: decidimos que o Gabriel vai ficar procurando sprites para o nosso game e eu vou focar em fazer funcionar os exemplos básicos da aula e organizar a relação de classes do nosso programa. (João)
- Dia 01/06, 16:40: decidimos colocar o nome do bonequinho da sprite que o Gabriel pegou de Alfredo. (João)
- Dia 01/06, 18:00: vimos algumas sprites promissoras com um boneco golpeando com espada, achamos que, se editarmos o arquivo num editor de pixels, podemos mudar essa espada para as nossas ferramentas. (João)
- Dia 06/06: depois de pensarmos sobre, conseguimos chegar num esquema de como vai ser o jogo e os objetos que iremos usar (10 plantas, 5 ferramentas e 15 receitas). As plantas vão ter o estado de aguardar ferramenta, crescendo e pronta pra colher, e as ferramentas vão ser divididas entre as que são usadas antes e depois do plantio. (João)

- Dia 07/06: entre os objetos que estou fazendo, tem alguns que podem ser tanto itens para venda quanto ingredientes (ex.: farinha), então estou em dúvida no que eu faço. (João)
- Dia 10/06: resolvi usar enums para ser como uma lista de quais nomes ão ingredientes, quais são receita e por aí vai, acredito q por agora é uma solução boa. (João)
- Dia 10/06: O Claude.AI me deu algumas possíveis soluções para os problemas com colisões que estava enfrentando usando o Tiled. Removi todas as anteriores e tentarei arrumar (Gabriel)

- Dia 12/06: Criei um contador de colisões que mostra quantas foram carregadas no terminal, para testes. (Gabriel)
- Dia 12/06: Importei uma biblioteca que permite a escrita de caracteres na tela do jogo para rastrear a posição do jogador e encontrar um ponto de spawn dentro do cenário. (Gabriel)
- Dia 12/06: Alterei o ponto de spawn do jogador. (Gabriel)
- Dia 12/06: Criei um novo diretório no libGDX sem as extensões desnecessárias que davam conflito ao tentar rodar o jogo via web. O projeto rodou sem erros, mas não consegui abrir o jogo de fato, nem gerar arquivos. Irei aprender como prosseguir daqui. (Gabriel)
- Dia 12/06: Terminei a lógica das classes e criei uma pasta registry com criação de objetos, acredito que a partir daí conseguimos mexer neles dentro do jogo. (João)
- Dia 22/06: Criei uma bag para guardar os itens, ajudei o gabriel a achar umas sprites, achamos melhor não fazer as ferramentas no jogo por logística e por sprites e animações faltando. (João)
- Dia 23/06: tava em dúvida se usava a bag ou o recipeRegistry para craftar as receitas mas o Claude me explicou que fica mais padronizado ser pelo recipeRegistry, tava dando erro de tipos mas consegui ver e pelo visto a bag não estava atualizada para receber do enum IngredientId o id de cada objeto (que eu fiz isso ontem de criar esse enum), ainda vou esperar para ter uma maior integração com os gráficos e lógica de movimentação e escolhas para ver se eu adiciono ou até apago algumas lógicas ou até enums que eu já tinha colocado. (João)
- Dia 24/06: terminamos de criar as sprites e fizemos uma bancada pra craftar as receitas. (João)

Período de inatividade em função daquele bug com o Github e problemas com diretórios, tanto comigo quanto com o João Daniel (Gabriel)

- Dia 19/06: Fiz alterações no Tiled para ter um cercado e área própria para a plantação. Também adicionei interatividade para que o player consiga plantar arar a terra, plantar, regar, crescer e colher a planta manualmente (Gabriel) 
- Dia 23/06: Adicionei uma mecânica que a planta vai para um estágio pedindo água automáticamente, e após regada, vai para o estágio de pronta também automáticamente, assim que passar o tempo mínimo (Gabriel)
- Dia 24/06: Adicionando as funcionalidades para o Menu de fazer receitas, XP e alteração dos sprites das plantas. Usando bastante o Claude para me auxiliar com as funcionalidades para a sobreposição do menu. Os - sprites das plantas e a Arte do Menu foram feitos via ChatGPT (Gabriel)
- 24/06: Sprites de plantas trocados. Os menus estão funcionais e a música do jogo funciona, mas precisa esperar um tempo para carregar

  AVISO IMPORTANTE!!!!! 
  RODAR APENAS USANDO O HTML. Localmente o jogo fica travado, não entendemos o motivo ainda.


https://github.com/user-attachments/assets/b6537723-4bfc-441e-af96-14887ba98d1b


------------------------------------------------------------------------
**Diagrama de Classes**


%% ── Entidades de domínio ──────────────────────────────────────

class Ingredient {
    -IngredientId id
    -String nome
    +getId() IngredientId
    +getNome() String
    +toString() String
}

class Plant {
    -PlantGrowth tempoDeCrescimento
    -List~Tool~ ferramentasNecessarias
    +getTempoDeCrescimento() float
    +getFerramentasNecessarias() List~Tool~
}

class Recipe {
    -int xpReward
    -List~Ingredient~ ingredientes
    +getXpReward() int
    +getIngredientes() List~Ingredient~
}

class Tool {
    -int id
    -String nome
    -int hierarquia
    +getId() int
    +getNome() String
    +getHierarquia() int
}

class Bag {
    -Map~IngredientId, Integer~ itens
    +adicionar(IngredientId, int) void
    +remover(IngredientId, int) boolean
    +getQuantidade(IngredientId) int
    +temItem(IngredientId, int) boolean
    +getItens() Map~IngredientId, Integer~
}

class PlantInstance {
    -Plant plant
    -PlantState state
    -float timerSegundos
    -boolean foiRegada
    +update(float) void
    +darAgua() void
    +getState() PlantState
    +getPlant() Plant
    +getProgresso() float
}

class User {
    -String nome
    -Level nivel
    -int xp
    +addXp(int) void
    +desbloqueou(IngredientId) boolean
    +getNivel() Level
    +getXp() int
}

%% ── Registries ───────────────────────────────────────────────

class ToolRegistry {
    +Tool enxada
    +Tool rasteira
    +Tool suporte
    +Tool adubo
    +Tool pa
    +List~Tool~ trigoTools
    +List~Tool~ canaTools
    +List~Tool~ morangoTools
    ...
}

class PlantRegistry {
    -ToolRegistry toolRegistry
    +Plant trigo
    +Plant cana
    +Plant morango
    +Plant abobora
    +Plant tomate
    +Plant alface
    +Plant amendoim
    +Plant maca
    +Plant laranja
    +Plant uva
}

class RecipeRegistry {
    -PlantRegistry plantRegistry
    -Map~IngredientId, Recipe~ receitas
    +Recipe farinha
    +Recipe acucar
    +Recipe pao
    +Recipe geleiaMorango
    +Recipe tortaAbobora
    +Recipe sanduiche
    +Recipe americano
    +Recipe tortaMaca
    +Recipe vinho
    +Recipe sucoLaranja
    +getRecipe(IngredientId) Recipe
    +craftar(IngredientId, Bag) Recipe
}

%% ── Tela / Mundo ──────────────────────────────────────────────

class TileHorta {
    +Rectangle area
    -EstadoTile estado
    -PlantInstance planta
    +podeArar() boolean
    +arar() void
    +podePlantar() boolean
    +plantar(Plant) void
    +podeRegar() boolean
    +regar() void
    +podeColher() boolean
    +colher() Plant
    +update(float) void
    +interagir(Plant) boolean
    +desenhar(SpriteBatch, ...) void
    +getEstadoTile() EstadoTile
    +getPlanta() PlantInstance
}

class GameScreen {
    -OrthographicCamera camera
    -SpriteBatch batch
    -TiledMap mapa
    -Array~Rectangle~ colisoes
    -List~TileHorta~ tilesHorta
    -PlantRegistry plantRegistry
    -RecipeRegistry recipeRegistry
    -Bag bag
    -User user
    -Plant plantaSelecionada
    -boolean menuReceitasAberto
    +show() void
    +render(float) void
    +resize(int, int) void
    +dispose() void
}

class Main {
    +Music musicaFundo
    +create() void
    +dispose() void
    +pause() void
    +resume() void
}

%% ── Enums ────────────────────────────────────────────────────

class IngredientId {
    <<enumeration>>
    TRIGO, CANA, MORANGO, ABOBORA
    TOMATE, ALFACE, AMENDOIM, MACA
    LARANJA, UVA
    FARINHA, PAO, ACUCAR
    GELEIA_DE_MORANGO, TORTA_DE_ABOBORA
    SANDUICHE, SANDUICHE_AMERICANO
    SUCO_DE_LARANJA, TORTA_DE_MACA, VINHO
    +int value
    +getValue() int
}

class PlantGrowth {
    <<enumeration>>
    GRASS(5s)
    CREEPING(10s)
    BUSH(15s)
    SUPPORT(30s)
    TREE(60s)
    +float growthTimeSeconds
}

class PlantState {
    <<enumeration>>
    PLANTADA
    PEDINDO_AGUA
    PRONTA
}

class Level {
    <<enumeration>>
    NIVEL_1 .. NIVEL_10
    +int numero
    +int xpNecessario
    +List~IngredientId~ plantasDesbloqueadas
    +List~IngredientId~ receitasDesbloqueadas
    +proximo() Level
}

class EstadoTile {
    <<enumeration>>
    NORMAL
    ARADA
    PLANTADA
}

%% ── Herança ──────────────────────────────────────────────────
Ingredient <|-- Plant
Ingredient <|-- Recipe

%% ── Composição / Associação ──────────────────────────────────
Plant "1" *-- "1" PlantGrowth
Plant "1" o-- "1..*" Tool

Recipe "1" o-- "1..*" Ingredient

PlantInstance "1" --> "1" Plant
PlantInstance --> PlantState

TileHorta "1" *-- "0..1" PlantInstance
TileHorta --> EstadoTile

Bag --> IngredientId

User --> Level

ToolRegistry "1" *-- "5" Tool
PlantRegistry "1" --> "1" ToolRegistry
PlantRegistry "1" *-- "10" Plant

RecipeRegistry "1" --> "1" PlantRegistry
RecipeRegistry "1" *-- "10" Recipe

GameScreen --> "1" Bag
GameScreen --> "1" User
GameScreen --> "1" PlantRegistry
GameScreen --> "1" RecipeRegistry
GameScreen "1" *-- "54" TileHorta

Main --> GameScreen

- Usei o Claude para fazer esse mermaid, fiz pela função projeto, anexei todo o meu projeto e ele fez isso. (João)


---------------------------------------------


