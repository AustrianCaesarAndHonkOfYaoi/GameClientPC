package com.interstellar.client;

import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.System;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import okhttp3.OkHttpClient;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainGame extends ApplicationAdapter {
    private Random randomCalls = new Random();


    OkHttpClient client = new OkHttpClient();
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private AssetManager assetManager;

    private ShapeRenderer shapeRenderer;
    private Texture bg;
    private Texture systemSkin;
    private Texture incomeDisplay;

    private BitmapFont energyDisplay;
    private BitmapFont mineralDisplay;
    private BitmapFont alloyDisplay;
    private BitmapFont yaoiDisplay;
    private BitmapFont foodDisplay;
    private BitmapFont fleetDisplay;


    private GameBoard board;
    private Nation playerNation;

    public MainGame() {
    }

    //Texturen laden, Vorbereiten zum Rendern, Sounds, bearbeiten der Musiklautstärke
    @Override
    public void create() {
        //Initializing

        assetManager = new AssetManager();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        board = new GameBoard();
        playerNation = new Nation();

        bg = new Texture("bg.jpg");
        systemSkin = new Texture("Symbol_System_01.png");
        incomeDisplay = new Texture("Entwurf02_Incomes.png");

        energyDisplay = new BitmapFont();
        mineralDisplay = new BitmapFont();
        alloyDisplay = new BitmapFont();
        yaoiDisplay = new BitmapFont();
        foodDisplay = new BitmapFont();
        fleetDisplay = new BitmapFont();

        board.initBoard();

        Runnable updateIncomes = () -> {
            playerNation.setAmountEnergy(playerNation.getIncomeEnergy());
            playerNation.setAmountAlloy(playerNation.getIncomeAlloy());
            playerNation.setAmountFood(playerNation.getIncomeFood());
            playerNation.setAmountMinerals(playerNation.getIncomeMinerals());
            playerNation.setAmountYaoi(playerNation.getIncomeYaoi());
        };

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(updateIncomes,0,20,TimeUnit.MILLISECONDS);


    }



    //Jeder Frame wird aufgerufen, z.B Spielfeld visuell generieren, Visuelle steuern; Main die immer wieder aufgerufen wird
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


        shapeRenderer.end();

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
                    System boardTile = board.getBoardTile(i, j);
                    if (boardTile == null) {
                        continue;
                    }

                    int boardX = boardTile.getPixelX();
                    int pixelY = boardTile.getPixelY();


                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.WHITE);
                    System xAxis = board.getBoardTile(i + 1, j);
                    if (xAxis != null) {
                        shapeRenderer.line(boardX + 25, pixelY + 25, xAxis.getPixelX() + 25, xAxis.getPixelY() + 25);
                    }
                    System yAxis = board.getBoardTile(i, j + 1);
                    if (yAxis != null) {
                        shapeRenderer.line(boardX + 25, pixelY + 25, yAxis.getPixelX() + 25, yAxis.getPixelY() + 25);
                    }
                    shapeRenderer.end();

                    batch.begin();
                    batch.draw(systemSkin, boardX, pixelY);
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
        // playerNation.setAmountEnergy(1000);
        foodDisplay.draw(batch, playerNation.getAmountFood() + " / " + playerNation.getIncomeFood(), 1100, 1055);
        alloyDisplay.draw(batch, playerNation.getAmountAlloy() + " / " + playerNation.getIncomeAlloy(), 1180, 1055);
        yaoiDisplay.draw(batch, playerNation.getAmountYaoi() + " / " + playerNation.getIncomeYaoi(), 1255, 1055);
        fleetDisplay.draw(batch, playerNation.getUsedFleetSpace() + " / " + playerNation.getFleetCapacity(), 1320, 1055);

        batch.end();

    }


}
