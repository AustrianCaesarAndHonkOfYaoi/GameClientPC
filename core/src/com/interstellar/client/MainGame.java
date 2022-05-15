package com.interstellar.client;

import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.Stellarsystem;
import Logic.UI.InterstellarInputProcessor;
import Logic.UI.SystemTerminal;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainGame extends ApplicationAdapter {
    private Random randomCalls = new Random();
    private boolean blockInput = false;

    Logger log = LoggerFactory.getLogger(MainGame.class);

    //ToDo Renderers
    OkHttpClient client = new OkHttpClient();
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private AssetManager assetManager;
    private ShapeRenderer shapeRenderer;
private InputMultiplexer multiplexer;

    //ToDo Texturen
    private Texture bg;
    private Texture systemSkin;
    private Texture incomeDisplay;
    private Texture test;

    //ToDO Bitmaps
    private BitmapFont energyDisplay;
    private BitmapFont mineralDisplay;
    private BitmapFont alloyDisplay;
    private BitmapFont yaoiDisplay;
    private BitmapFont foodDisplay;
    private BitmapFont fleetDisplay;
    private BitmapFont systemNationality;
    //TODo Coords
    private int systemX ;
    private int systemY ;

    private int chooseLimit;
    //Todo Game objekte
    private GameBoard board;
    private Nation playerNation;
    private Table terminal;


    //Todo Stages
    private Stage systemManagement;
    private boolean terminalActivation = false;

    public MainGame() {
    }

    //Texturen laden, Vorbereiten zum Rendern, Sounds, bearbeiten der Musiklautstärke
    //ToDO Create
    @Override
    public void create() {
        //Initializing
chooseLimit =11;
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        board = new GameBoard();
        playerNation = new Nation();


        //TOdo Assets laden
        assetManager.load("Colonize.json", Skin.class);
        assetManager.finishLoading();

        bg = new Texture("bg.jpg");
        systemSkin = new Texture("Symbol_System_01.png");
        incomeDisplay = new Texture("Entwurf02_Incomes.png");
        test = new Texture("Honk.png");

        energyDisplay = new BitmapFont();
        mineralDisplay = new BitmapFont();
        alloyDisplay = new BitmapFont();
        yaoiDisplay = new BitmapFont();
        foodDisplay = new BitmapFont();
        fleetDisplay = new BitmapFont();
        systemNationality = new BitmapFont();

        systemManagement = new Stage(new ExtendViewport(1920, 1080, 1920, 1080));
     //  Gdx.input.setInputProcessor(systemManagement);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(systemManagement);
        Gdx.input.setInputProcessor(multiplexer);

        terminal = new Table();
        board.initBoard();
        createTerminal();

        systemManagement.draw();
        Runnable updateIncomes = () -> {
            playerNation.setAmountEnergy(playerNation.getIncomeEnergy());
            playerNation.setAmountAlloy(playerNation.getIncomeAlloy());
            playerNation.setAmountFood(playerNation.getIncomeFood());
            playerNation.setAmountMinerals(playerNation.getIncomeMinerals());
            playerNation.setAmountYaoi(playerNation.getIncomeYaoi());
        };

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(updateIncomes, 0, 20, TimeUnit.MILLISECONDS);


    }


    //Todo Render
    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();


//Mache Zeichnungnen;
        shapeRenderer.end();
        batch.begin();
        batch.draw(bg, 0, 0);
        //Texturenzeichnungen implementieren
        batch.end();
        setBase();
        stageInput();
        shapeRenderer.end();
        if (terminalActivation) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(1620,0,308,1040);
            shapeRenderer.end();
            chooseLimit = 9;
            systemManagement.draw();
        }
    }

    //Entladen
    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
    }

    public void setBase() {
        //Sektorbreite 154
        // Sektorhöhe  84


        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                try {
                    Stellarsystem boardTile = board.getBoardTile(i, j);
                    if (boardTile == null) {
                        continue;
                    }

                    int boardX = boardTile.getPixelX();
                    int pixelY = boardTile.getPixelY();


                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.WHITE);
                    Stellarsystem xAxis = board.getBoardTile(i + 1, j);
                    if (xAxis != null) {
                        shapeRenderer.line(boardX + 25, pixelY + 25, xAxis.getPixelX() + 25, xAxis.getPixelY() + 25);
                    }
                    Stellarsystem yAxis = board.getBoardTile(i, j + 1);
                    if (yAxis != null) {
                        shapeRenderer.line(boardX + 25, pixelY + 25, yAxis.getPixelX() + 25, yAxis.getPixelY() + 25);
                    }
                    shapeRenderer.end();

                    batch.begin();
                    batch.draw(systemSkin, boardX, pixelY, 50, 50);
                    batch.end();

                    for (int k = 0; k < boardTile.getPlanetAmount(); k++) {
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(Color.valueOf("c7d2dc"));
                        shapeRenderer.circle(boardX + 50 + 20 * k, pixelY + 25, 7);
                        shapeRenderer.end();
                    }

                } catch (IndexOutOfBoundsException ignored) {
                    if (shapeRenderer.isDrawing()) {
                        shapeRenderer.end();
                    }
                }

            }
        }
        batch.begin();
        batch.draw(incomeDisplay, 0, 1041);

        energyDisplay.draw(batch, playerNation.getAmountEnergy() + " / " + playerNation.getIncomeEnergy(), 935, 1055);
        mineralDisplay.draw(batch, playerNation.getAmountMinerals() + " / " + playerNation.getIncomeMinerals(), 1010, 1055);
        foodDisplay.draw(batch, playerNation.getAmountFood() + " / " + playerNation.getIncomeFood(), 1100, 1055);
        alloyDisplay.draw(batch, playerNation.getAmountAlloy() + " / " + playerNation.getIncomeAlloy(), 1180, 1055);
        yaoiDisplay.draw(batch, playerNation.getAmountYaoi() + " / " + playerNation.getIncomeYaoi(), 1255, 1055);
        fleetDisplay.draw(batch, playerNation.getUsedFleetSpace() + " / " + playerNation.getFleetCapacity(), 1320, 1055);

        batch.end();

    }

    private void stageInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int testX = Gdx.input.getX() / 154;
            int testY = (Gdx.input.getY() / 84);
            if (testX > chooseLimit) {
                testX = chooseLimit;
            }
            if (testY > 11) {
                testY = 11;
            }

            shapeRenderer.begin();
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(154 * testX + 39, (11 - testY) * 84 + 39, 154, 84);
            shapeRenderer.end();


            blockInput = true;
            int inputX = Gdx.input.getX();
            int inputY = Gdx.input.getY();

            boolean found = false;
            // System.out.println("X: " + inputX + " Y: " + inputY);
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int minX = 39 + 154 * i;
                    int minY = 39 + 84 * j;
                    int maxX = 39 + 154 * (i + 1);
                    int maxY = 39 + 84 * (j + 1);
                    //log.info(minX + ">=" + inputX + "<=" + maxX + "\n" + minY + ">=" + inputY + "<=" + maxY);
                    if (inputX >= minX && inputX <= maxX && inputY >= minY && inputY <= maxY) {
                        systemX = i;
                        systemY = j;
                        log.info("SystemX: " + systemX + "\nSystemy: " + systemY);

                        found = true;
                        break;
                    }
                    if (found) {
                        terminalActivation = true;
                        break;
                    }
                }
            }
            // systemManagement.draw();
        }
        //ToDo Stage
        blockInput = false;


    }

    public void createTerminal() {

        terminal.setFillParent(true);
        terminal.setDebug(true, true);
        Skin systemColonisation = assetManager.get("Colonize.json", Skin.class);
        ImageButton button = new ImageButton(systemColonisation);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            terminalActivation = false;
            }
        });


        ImageButton button1 = new ImageButton(systemColonisation);
        terminal.moveBy(549, 475);

        String t = board.getBoardTile(0, 0).getNationality();


        terminal.add(button);
        terminal.row();
        terminal.add(button1);

        systemManagement.addActor(terminal);
        // setTableVisible(false);


    }

}


