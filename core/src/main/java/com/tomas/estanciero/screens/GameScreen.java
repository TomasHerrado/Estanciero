package com.tomas.estanciero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tomas.estanciero.Main;
import com.tomas.estanciero.model.*;

public class GameScreen implements Screen{
    private Main game;
    private OrthographicCamera camera;
    private GameManager gameManager;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont fontGrande;
    private Texture tableroTexture;
    private Stage stage;
    private Skin skin;

    private float ANCHO_TABLERO = 700;
    private float ALTO_TABLERO = 700;
    private Vector2 posicionTablero;

    private Vector2[] posicionesCasillas;

    private static final Color[] COLORES_JUGADORES = {
        new Color(0.9f, 0.2f, 0.2f, 1f), // Rojo
        new Color(0.2f, 0.4f, 0.9f, 1f), // Azul
        new Color(0.2f, 0.8f, 0.3f, 1f), // Verde
        new Color(0.95f, 0.8f, 0.1f, 1f)  // Amarillo
    };

    private Label labelTurno;
    private Label labelDinero;
    private Label labelPosicion;
    private Label labelDados;
    private TextButton btnTirarDados;
    private TextButton btnComprar;
    private TextButton btnTerminarTurno;
    private Table panelJugadores;
    private Table panelInfo;

    private boolean turnoEnCurso = false;
    private int ultimosDados[] = {0, 0};

    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        shapeRenderer = new ShapeRenderer();

        crearFuentes();

        try {
            tableroTexture = new Texture(Gdx.files.internal("tablero.png"));
            System.out.println("✓ Imagen del tablero cargada");
        } catch (Exception e) {
            System.out.println("✗ No se pudo cargar tablero.png");
            tableroTexture = null;
        }

        posicionTablero = new Vector2(40, 10);

        inicializarPosicionesCasillas();

        gameManager = new GameManager(4, 5000, 20000);

        crearInterfaz();

        System.out.println("\n=== ESTANCIERO ===");
        System.out.println("¡Juego iniciado con éxito!");
    }

    private void crearFuentes() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.0f);

        fontGrande = new BitmapFont();
        fontGrande.setColor(Color.WHITE);
        fontGrande.getData().setScale(1.8f);
    }

    private void inicializarPosicionesCasillas() {
        posicionesCasillas = new Vector2[40];

        float baseX = posicionTablero.x;
        float baseY = posicionTablero.y;

        float anchoCasilla = 70;
        float altoCasilla = 70;

        for (int i = 0; i <= 9; i++) {
            posicionesCasillas[i] = new Vector2(
                baseX + i * anchoCasilla,
                baseY
            );
        }

        for (int i = 10; i <= 19; i++) {
            posicionesCasillas[i] = new Vector2(
                baseX + 9 * anchoCasilla,
                baseY + (i - 9) * altoCasilla
            );
        }

        for (int i = 20; i <= 29; i++) {
            posicionesCasillas[i] = new Vector2(
                baseX + (29 - i) * anchoCasilla,
                baseY + 10 * altoCasilla
            );
        }

        for (int i = 30; i <= 39; i++) {
            posicionesCasillas[i] = new Vector2(
                baseX,
                baseY + (39 - i) * altoCasilla
            );
        }
    }

    private void crearInterfaz() {
        Table panelDerecho = new Table();
        panelDerecho.setFillParent(true);
        panelDerecho.top().right();
        panelDerecho.pad(10);

        Label titulo = new Label("ESTANCIERO", skin, "window");
        titulo.setFontScale(1.8f);
        titulo.setColor(Color.GOLD);
        panelDerecho.add(titulo).colspan(2).padBottom(15).row();

        panelDerecho.add(new Label("", skin)).height(2).colspan(2).fillX().padBottom(10).row();

        panelInfo = new Table();
        panelInfo.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));
        panelInfo.pad(10);

        labelTurno = new Label("Turno: Jugador 1", skin);
        labelTurno.setFontScale(1.3f);
        labelTurno.setColor(COLORES_JUGADORES[0]);
        panelInfo.add(labelTurno).colspan(2).padBottom(8).row();

        labelDinero = new Label("Dinero: $5000", skin);
        labelDinero.setFontScale(1.2f);
        panelInfo.add(labelDinero).colspan(2).padBottom(8).row();

        labelPosicion = new Label("", skin);
        labelPosicion.setFontScale(1.0f);
        labelPosicion.setWrap(true);
        panelInfo.add(labelPosicion).width(250).colspan(2).padBottom(10).row();

        labelDados = new Label("", skin);
        labelDados.setFontScale(1.5f);
        labelDados.setColor(Color.YELLOW);
        panelInfo.add(labelDados).colspan(2).padBottom(5).row();

        panelDerecho.add(panelInfo).width(280).padBottom(15).row();

        btnTirarDados = new TextButton("TIRAR DADOS", skin);
        btnTirarDados.getLabel().setFontScale(1.3f);
        btnTirarDados.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ejecutarTurno();
            }
        });
        panelDerecho.add(btnTirarDados).width(280).height(60).padBottom(10).row();

        btnComprar = new TextButton("COMPRAR", skin);
        btnComprar.getLabel().setFontScale(1.3f);
        btnComprar.setDisabled(true);
        btnComprar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                comprarPropiedad();
            }
        });
        panelDerecho.add(btnComprar).width(280).height(60).padBottom(10).row();

        btnTerminarTurno = new TextButton("TERMINAR TURNO", skin);
        btnTerminarTurno.getLabel().setFontScale(1.3f);
        btnTerminarTurno.setDisabled(true);
        btnTerminarTurno.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                terminarTurno();
            }
        });
        panelDerecho.add(btnTerminarTurno).width(280).height(60).padBottom(15).row();

        panelDerecho.add(new Label("", skin)).height(2).colspan(2).fillX().padBottom(10).row();

        Label labelJugadores = new Label("JUGADORES", skin);
        labelJugadores.setFontScale(1.3f);
        labelJugadores.setColor(Color.CYAN);
        panelDerecho.add(labelJugadores).colspan(2).padBottom(10).row();

        panelJugadores = new Table();
        actualizarPanelJugadores();

        ScrollPane scrollJugadores = new ScrollPane(panelJugadores, skin);
        scrollJugadores.setFadeScrollBars(false);
        panelDerecho.add(scrollJugadores).width(280).height(250).row();

        stage.addActor(panelDerecho);

        actualizarUI();
    }

    private void actualizarPanelJugadores() {
        panelJugadores.clear();

        for (int i = 0; i < gameManager.getJugadores().size(); i++) {
            Jugador jugador = gameManager.getJugadores().get(i);

            Table filaJugador = new Table();
            filaJugador.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
            filaJugador.pad(8);

            Table colorIndicator = new Table();
            colorIndicator.setBackground(skin.newDrawable("white", COLORES_JUGADORES[i]));
            filaJugador.add(colorIndicator).size(30, 30).padRight(10);

            String info = jugador.getNombre();
            String estado = "$" + jugador.getDinero();

            if (jugador == gameManager.getJugadorActual()) {
                info = "► " + info;
            }

            Label labelNombre = new Label(info, skin);
            labelNombre.setFontScale(1.1f);
            labelNombre.setColor(COLORES_JUGADORES[i]);

            Label labelEstado = new Label(estado, skin);
            labelEstado.setFontScale(0.9f);

            if (jugador.enBancarrota()) {
                labelEstado.setText("[BANCARROTA]");
                labelEstado.setColor(Color.RED);
            } else if (jugador.isEnCarcel()) {
                labelEstado.setText(estado + " [CÁRCEL]");
                labelEstado.setColor(Color.ORANGE);
            }

            Table infoTable = new Table();
            infoTable.add(labelNombre).left().row();
            infoTable.add(labelEstado).left().row();

            filaJugador.add(infoTable).left().expandX();

            panelJugadores.add(filaJugador).expandX().fillX().padBottom(8).row();
        }
    }

    private void ejecutarTurno() {
        if (gameManager.isJuegoTerminado()) {
            mostrarDialogoFinJuego();
            return;
        }

        Dado dado = gameManager.getDado();
        gameManager.ejecutarTurno();
        ultimosDados[0] = dado.getDado1();
        ultimosDados[1] = dado.getDado2();

        turnoEnCurso = true;
        actualizarUI();

        verificarCasillaActual();

        if (gameManager.isJuegoTerminado()) {
            mostrarDialogoFinJuego();
        }
    }

    private void verificarCasillaActual() {
        Jugador actual = gameManager.getJugadorActual();
        Casilla casilla = gameManager.getTablero().getCasilla(actual.getPosicion());

        btnComprar.setDisabled(true);

        if (casilla != null && casilla.getTipo() == Casilla.TipoCasilla.PROPIEDAD) {
            Propiedad prop = casilla.getPropiedad();
            if (prop != null && !prop.tieneDueno()) {
                btnComprar.setDisabled(false);
                mostrarDialogoPropiedad(prop);
            }
        }
    }

    private void comprarPropiedad() {
        boolean comprado = gameManager.comprarPropiedad();
        if (comprado) {
            mostrarMensaje("¡Propiedad comprada!", Color.GREEN);
            btnComprar.setDisabled(true);
        }
        actualizarUI();
    }

    private void terminarTurno() {
        turnoEnCurso = false;
        ultimosDados[0] = 0;
        ultimosDados[1] = 0;
        actualizarUI();
    }

    private void mostrarMensaje(String mensaje, Color color) {
        Dialog dialogo = new Dialog("", skin);
        Label label = new Label(mensaje, skin);
        label.setFontScale(1.3f);
        label.setColor(color);
        dialogo.text(label);
        dialogo.button("OK");
        dialogo.show(stage);
    }

    private void mostrarDialogoPropiedad(Propiedad propiedad) {
        Dialog dialogo = new Dialog("Propiedad Disponible", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    comprarPropiedad();
                }
            }
        };

        Table contenido = new Table();
        contenido.pad(20);

        Label labelNombre = new Label(propiedad.getNombre(), skin);
        labelNombre.setFontScale(1.5f);
        labelNombre.setColor(Color.CYAN);
        contenido.add(labelNombre).colspan(2).padBottom(15).row();

        contenido.add(new Label("Tipo:", skin)).left().padRight(20);
        contenido.add(new Label(propiedad.getTipo().toString(), skin)).left().row();

        contenido.add(new Label("Precio:", skin)).left().padRight(20);
        Label labelPrecio = new Label("$" + propiedad.getPrecio(), skin);
        labelPrecio.setColor(Color.YELLOW);
        contenido.add(labelPrecio).left().row();

        contenido.add(new Label("Alquiler:", skin)).left().padRight(20);
        contenido.add(new Label("$" + propiedad.getAlquilerBase(), skin)).left().row();

        dialogo.getContentTable().add(contenido);
        dialogo.button("COMPRAR", true);
        dialogo.button("NO COMPRAR", false);
        dialogo.show(stage);
    }

    private void mostrarDialogoFinJuego() {
        Dialog dialogo = new Dialog("¡FIN DEL JUEGO!", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    gameManager.reiniciarJuego();
                    turnoEnCurso = false;
                    ultimosDados[0] = 0;
                    ultimosDados[1] = 0;
                    actualizarUI();
                }
            }
        };

        Jugador ganador = gameManager.getGanador();

        Table contenido = new Table();
        contenido.pad(20);

        Label labelGanador = new Label("¡" + ganador.getNombre() + " GANÓ!", skin);
        labelGanador.setFontScale(2.0f);
        labelGanador.setColor(Color.GOLD);
        contenido.add(labelGanador).padBottom(20).row();

        Label labelDinero = new Label("Dinero final: $" + ganador.getDinero(), skin);
        labelDinero.setFontScale(1.5f);
        contenido.add(labelDinero).row();

        dialogo.getContentTable().add(contenido);
        dialogo.button("JUGAR DE NUEVO", true);
        dialogo.button("CERRAR", false);
        dialogo.show(stage);
    }

    private void actualizarUI() {
        Jugador actual = gameManager.getJugadorActual();
        int indexJugador = gameManager.getJugadores().indexOf(actual);

        labelTurno.setText("Turno: " + actual.getNombre());
        labelTurno.setColor(COLORES_JUGADORES[indexJugador]);

        labelDinero.setText("Dinero: $" + actual.getDinero());

        Casilla casilla = gameManager.getTablero().getCasilla(actual.getPosicion());
        if (casilla != null) {
            String posInfo = "Posición " + actual.getPosicion() + ":\n" + casilla.getNombre();

            if (casilla.getTipo() == Casilla.TipoCasilla.PROPIEDAD && casilla.getPropiedad() != null) {
                Propiedad prop = casilla.getPropiedad();
                if (prop.tieneDueno()) {
                    posInfo += "\nDueño: " + prop.getDueno().getNombre();
                }
            }

            labelPosicion.setText(posInfo);
        }

        if (ultimosDados[0] > 0) {
            String dadosTexto = "[" + ultimosDados[0] + "] [" + ultimosDados[1] + "] = " + (ultimosDados[0] + ultimosDados[1]);
            if (ultimosDados[0] == ultimosDados[1]) {
                dadosTexto += " ¡DOBLE!";
            }
            labelDados.setText(dadosTexto);
        } else {
            labelDados.setText("");
        }

        btnTirarDados.setDisabled(turnoEnCurso || gameManager.isJuegoTerminado());
        btnTerminarTurno.setDisabled(!turnoEnCurso || gameManager.isJuegoTerminado());

        actualizarPanelJugadores();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.1f, 1f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        if (tableroTexture != null) {
            game.batch.draw(tableroTexture,
                posicionTablero.x,
                posicionTablero.y,
                ANCHO_TABLERO,
                ALTO_TABLERO);
        }
        game.batch.end();

        dibujarJugadores();

        stage.act(delta);
        stage.draw();
    }

    private void dibujarJugadores() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < gameManager.getJugadores().size(); i++) {
            Jugador jugador = gameManager.getJugadores().get(i);
            if (jugador.enBancarrota()) continue;

            int pos = jugador.getPosicion();
            if (pos < 0 || pos >= posicionesCasillas.length) continue;

            Vector2 posCasilla = posicionesCasillas[pos];

            float offsetX = (i % 2) * 30 + 35;
            float offsetY = (i / 2) * 30 + 35;

            shapeRenderer.setColor(COLORES_JUGADORES[i]);
            shapeRenderer.circle(
                posCasilla.x + offsetX,
                posCasilla.y + offsetY,
                14
            );

            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.circle(
                posCasilla.x + offsetX,
                posCasilla.y + offsetY,
                14
            );
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        fontGrande.dispose();
        stage.dispose();
        skin.dispose();
        if (tableroTexture != null) {
            tableroTexture.dispose();
        }
    }
}
