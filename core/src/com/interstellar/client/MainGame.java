package com.interstellar.client;

import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.Stellarsystem;
import Logic.UI.MyInputListener;
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
    private Texture energyTexture;
    private Texture mineralTexture;
    private Texture alloyTexture;
    private Texture foodTexture;
    private Texture yaoiTexture;
    private Texture fleetTexture;
    private Texture enemyTerritory;
    private Texture ownTerritory;
    private Texture fleetSymbol;
    //ToDO Bitmaps
    private Label energyDisplay;
    private Label mineralDisplay;
    private Label alloyDisplay;
    private Label yaoiDisplay;
    private Label foodDisplay;
    private Label fleetDisplay;
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
        systemManagement = new Stage(new ExtendViewport(1920, 1080, 1920, 1080));

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(systemManagement);
        Gdx.input.setInputProcessor(multiplexer);
        systemManagement.draw();

        board.initBoard(playerNation);
        activateRessourceTimer();
        loadAssets();
        loadTexture();

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
            shapeRenderer.rect(1410, 0, 700, 1040);
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
            for (int j = 11; j >= 0; j--) {
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

                    if (boardTile.getNationality().equals("Uncolonised")) {
                        batch.draw(systemSkin, boardX, pixelY, 50, 50);
                    } else if (boardTile.getNationality().equals(playerNation.getName())) {
                        batch.draw(ownTerritory, boardX, pixelY, 50, 50);
                    }
                    if(boardTile.getEnemy()){
                        batch.draw(enemyTerritory,boardX,pixelY,50,50);
                    }
                    batch.end();

                    for (int k = 0; k < boardTile.getPlanetAmount(); k++) {
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(Color.valueOf("c7d2dc"));
                        shapeRenderer.circle(boardX + 50 + 20 * k, pixelY + 25, 7);
                        shapeRenderer.end();
                    }
                    if (boardTile.getSystemsFleet() != null) {
                        batch.begin();
                        batch.draw(fleetSymbol, boardTile.getPixelX() - 50, boardTile.getPixelY());
                        batch.end();
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
        if (playerNation.getAmountEnergy() < 1000) {
            energyDisplay.setText("      " + playerNation.getAmountEnergy() + " / " + playerNation.getIncomeEnergy());
        } else {
            energyDisplay.setText("      " + playerNation.getAmountEnergy() / 1000 + "k / " + playerNation.getIncomeEnergy());

        }
        mineralDisplay = new Label(null, displays);
        if (playerNation.getAmountMinerals() < 1000) {
            mineralDisplay.setText("      " + playerNation.getAmountMinerals() + " / " + playerNation.getIncomeMinerals());
        } else {
            mineralDisplay.setText("      " + playerNation.getAmountMinerals() / 1000 + "k / " + playerNation.getIncomeMinerals());
        }
        alloyDisplay = new Label(null, displays);
        if (playerNation.getAmountAlloy() < 1000) {
            alloyDisplay.setText("      " + playerNation.getAmountAlloy() + " / " + playerNation.getIncomeAlloy());
        } else {
            alloyDisplay.setText("      " + playerNation.getAmountAlloy() / 1000 + "k / " + playerNation.getIncomeAlloy());

        }
        yaoiDisplay = new Label(null, displays);
        if (playerNation.getAmountYaoi() < 1000) {
            yaoiDisplay.setText("      " + playerNation.getAmountYaoi() + " / " + playerNation.getIncomeYaoi());
        } else {
            yaoiDisplay.setText("      " + playerNation.getAmountYaoi() / 1000 + "k / " + playerNation.getIncomeYaoi());

        }
        foodDisplay = new Label(null, displays);
        if (playerNation.getAmountFood() < 1000) {
            foodDisplay.setText("      " + playerNation.getAmountFood() + " / " + playerNation.getIncomeFood());
        } else {
            foodDisplay.setText("      " + playerNation.getAmountFood() / 1000 + "k / " + playerNation.getIncomeFood());

        }
        fleetDisplay = new Label(null, displays);
        if (playerNation.getUsedFleetSpace() < 1000) {
            fleetDisplay.setText("      " + playerNation.getUsedFleetSpace() + " / " + playerNation.getFleetCapacity());
        } else {
            fleetDisplay.setText("      " + playerNation.getUsedFleetSpace() / 1000 + "k / " + playerNation.getFleetCapacity());

        }
        //ToDo Texturen
        batch.begin();
        batch.draw(energyTexture, 195, 1045, 50, 50);
        batch.draw(mineralTexture, 370, 1045, 50, 30);
        batch.draw(alloyTexture, 550, 1045, 50, 30);
        batch.draw(foodTexture, 730, 1045, 50, 30);
        batch.draw(yaoiTexture, 900, 1045, 40, 30);
        batch.draw(fleetTexture, 1080, 1045, 50, 30);
        batch.end();
        ressources.add();
        ressources.add(energyDisplay);
        ressources.add();
        ressources.add(mineralDisplay);
        ressources.add();
        ressources.add(alloyDisplay);
        ressources.add();
        ressources.add(foodDisplay);
        ressources.add();
        ressources.add(yaoiDisplay);
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
                        systemY = 11 - j;
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

        Skin corvetteSkin = assetManager.get("Corvette.json", Skin.class);
        Skin destroyerSkin = assetManager.get("Destroyer.json", Skin.class);
        Skin cruiserSkin = assetManager.get("Cruiser.json", Skin.class);
        Skin battleshipSkin = assetManager.get("Battleship.json", Skin.class);
        Skin titanSkin = assetManager.get("Titan.json", Skin.class);
        Skin carrierSkin = assetManager.get("Carrier.json", Skin.class);

        terminal.setShipSkins(corvetteSkin, destroyerSkin, cruiserSkin, battleshipSkin, carrierSkin, titanSkin);
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

    public void activateRessourceTimer() {
        Runnable updateIncomes = () -> {
            try {
                if (playerNation.getAmountEnergy() < 99999) {
                    playerNation.setAmountEnergy(playerNation.getIncomeEnergy());
                }
                if (playerNation.getAmountAlloy() < 99999) {
                    playerNation.setAmountAlloy(playerNation.getIncomeAlloy());
                }
                if (playerNation.getAmountFood() < 99999) {
                    playerNation.setAmountFood(playerNation.getIncomeFood());
                }
                if (playerNation.getAmountMinerals() < 99999) {
                    playerNation.setAmountMinerals(playerNation.getIncomeMinerals());
                }
                if (playerNation.getAmountYaoi() < 99999) {
                    playerNation.setAmountYaoi(playerNation.getIncomeYaoi());
                }


            } catch (IndexOutOfBoundsException ignored) {

            }

        };

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(updateIncomes, 0, 1, TimeUnit.MILLISECONDS);
    }

    public void loadAssets() {
        assetManager.load("Colonize.json", Skin.class);
        assetManager.load("CloseButton.json", Skin.class);
        assetManager.load("LaunchButton.json", Skin.class);
        assetManager.load("Halogen.json", Skin.class);

        assetManager.load("Battleship.json", Skin.class);
        assetManager.load("Corvette.json", Skin.class);
        assetManager.load("Cruiser.json", Skin.class);
        assetManager.load("Carrier.json", Skin.class);
        assetManager.load("Titan.json", Skin.class);
        assetManager.load("Destroyer.json", Skin.class);

        FileHandle fontFile = Gdx.files.internal("Halogen.ttf");
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 24;

        BitmapFont font = freeTypeFontGenerator.generateFont(parameter);
        labelStyle = new Label.LabelStyle(font, Color.BLACK);
        displays = new Label.LabelStyle(font, Color.WHITE);


        assetManager.finishLoading();
    }
    public void loadTexture() {
        bg = new Texture("bg.jpg");
        systemSkin = new Texture("Symbol_System_01.png");
        incomeDisplay = new Texture("Entwurf02_Incomes.png");
        energyTexture = new Texture("Energy.png");
        mineralTexture = new Texture("Mineral.png");
        alloyTexture = new Texture("Alloy.png");
        foodTexture = new Texture("Food.png");
        yaoiTexture = new Texture("Yaoi.png");
        fleetTexture = new Texture("FleetSpace.png");
        enemyTerritory = new Texture("Symbol_System_Enemy.png");
        ownTerritory = new Texture("Symbol_System_Own.png");
        fleetSymbol = new Texture("Symbol_Fleet_06.png");
        systemDisplay = false;
        ressourceDisplay = new Stage();
    }
}


