package com.interstellar.client;

import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.Stellarsystem;
import Logic.UI.Terminals;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
    private Texture energyTexture;
    private Texture mineralTexture;
    private Texture alloyTesxture;
    private Texture foodTexture;
    private Texture yaoiTexture;
    private Texture fleetTexture;


    //ToDO Bitmaps
    private Label energyDisplay;
    private Label mineralDisplay;
    private Label alloyDisplay;
    private Label yaoiDisplay;
    private Label foodDisplay;
    private Label fleetDisplay;
    private BitmapFont systemNationality;
    //TODo Coords
    private int systemX;
    private int systemY;

    private int chooseLimit;
    //Todo Game objekte
    private GameBoard board;
    private Nation playerNation;
    private Terminals terminal;


    //Todo Stages
    private Stage systemManagement;
    private Stage ressourceDisplay;
    private boolean terminalActivation;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle displays;
    private Boolean systemDisplay;

    public MainGame() {
    }

    //Texturen laden, Vorbereiten zum Rendern, Sounds, bearbeiten der Musiklautstärke
    //ToDO Create
    @Override
    public void create() {
        //Initializing
        chooseLimit = 11;
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        board = new GameBoard();
        playerNation = new Nation();
        terminalActivation = false;
        terminal = new Terminals();

        //TOdo Assets laden
        assetManager.load("Colonize.json", Skin.class);
        assetManager.load("CloseButton.json", Skin.class);
        assetManager.load("LaunchButton.json", Skin.class);
        assetManager.load("Halogen.json", Skin.class);

        FileHandle fontFile = Gdx.files.internal("Halogen.ttf");
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 24;

        BitmapFont font = freeTypeFontGenerator.generateFont(parameter);
        labelStyle = new Label.LabelStyle(font, Color.BLACK);
        displays = new Label.LabelStyle(font, Color.WHITE);


        assetManager.finishLoading();

        bg = new Texture("bg.jpg");
        systemSkin = new Texture("Symbol_System_01.png");
        incomeDisplay = new Texture("Entwurf02_Incomes.png");
        test = new Texture("Honk.png");
        energyTexture=new Texture("Energy.png");
        mineralTexture=new Texture("Mineral.png");
        alloyTesxture=new Texture("Alloy.png");
        foodTexture=new Texture("Food.png");
        yaoiTexture=new Texture("Yaoi.png");
        fleetTexture=new Texture("FleetSpace.png");

        systemDisplay = false;
        systemManagement = new Stage(new ExtendViewport(1920, 1080, 1920, 1080));
        ressourceDisplay = new Stage();
        //  Gdx.input.setInputProcessor(systemManagement);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(systemManagement);
        Gdx.input.setInputProcessor(multiplexer);


        board.initBoard();
        //createTerminal();

        systemManagement.draw();
        Runnable updateIncomes = () -> {
            try {
                playerNation.setAmountEnergy(playerNation.getIncomeEnergy());
                playerNation.setAmountAlloy(playerNation.getIncomeAlloy());
                playerNation.setAmountFood(playerNation.getIncomeFood());
                playerNation.setAmountMinerals(playerNation.getIncomeMinerals());
                playerNation.setAmountYaoi(playerNation.getIncomeYaoi());

                energyDisplay = new Label(null, labelStyle);

            } catch (IndexOutOfBoundsException ignored) {

            }

        };

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(updateIncomes, 0, 1, TimeUnit.MILLISECONDS);

        terminal.setMainGame(this);
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
            shapeRenderer.rect(1520, 0, 408, 1040);
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


        batch.end();
        Table ressources = new Table();
        ressources.setPosition(700, 1060);

        energyDisplay = new Label(null, displays);
        energyDisplay.setText("   "+playerNation.getAmountEnergy() + " / " + playerNation.getIncomeEnergy());
        mineralDisplay = new Label(null, displays);
        mineralDisplay.setText("   "+playerNation.getAmountMinerals() + " / " + playerNation.getIncomeMinerals());
        alloyDisplay = new Label(null, displays);
        alloyDisplay.setText("   "+playerNation.getAmountAlloy() + " / " + playerNation.getIncomeAlloy());
        yaoiDisplay = new Label(null, displays);
        yaoiDisplay.setText("   "+playerNation.getAmountYaoi() + " / " + playerNation.getIncomeYaoi());
        foodDisplay = new Label(null, displays);
        foodDisplay.setText("   "+playerNation.getAmountFood() + " / " + playerNation.getIncomeFood());
        fleetDisplay = new Label(null, displays);
        fleetDisplay.setText("   "+playerNation.getUsedFleetSpace() + " / " + playerNation.getFleetCapacity());

        //ToDo Texturen
        ressources.add();
        ressources.add(energyDisplay);
        ressources.add();
        ressources.add(mineralDisplay);
        ressources.add();
        ressources.add(alloyDisplay);
        ressources.add();
        ressources.add(yaoiDisplay);
        ressources.add();
        ressources.add(foodDisplay);
        ressources.add();
        ressources.add(fleetDisplay);
        ressourceDisplay.clear();
        ressourceDisplay.addActor(ressources);
        ressourceDisplay.draw();

    }

    private void stageInput() {

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


        if (!systemDisplay && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            systemDisplay = true;
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
            systemManagement.clear();
            createTerminal();
            blockInput = false;
        }

    }

    public void createTerminal() {


        Skin colonySkin = assetManager.get("Colonize.json", Skin.class);
        Skin closeSkin = assetManager.get("CloseButton.json", Skin.class);
        Skin launchSkin = assetManager.get("LaunchButton.json", Skin.class);
        Skin halogen = assetManager.get("Halogen.json", Skin.class);
        double amount = board.getBoardTile(systemX, systemY).getSystemMaxDistrict();
        int displayed = (int) amount;
        String districtAmount = String.valueOf(displayed);


        systemManagement.addActor(terminal.systemTerminal(playerNation, board, systemX, systemY, districtAmount, colonySkin, closeSkin, launchSkin, labelStyle));


    }

    public void setTerminalActivation(boolean terminalActivation) {
        this.terminalActivation = terminalActivation;
    }

    public void setChooseLimit(int chooseLimit) {
        this.chooseLimit = chooseLimit;
    }

    public void setSystemDisplay(Boolean systemDisplay) {
        this.systemDisplay = systemDisplay;
    }

}


