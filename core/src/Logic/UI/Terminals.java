package Logic.UI;

import Logic.Objects.Fleet;
import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.Stellarsystem;
import Logic.shiptypes.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.interstellar.client.MainGame;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Terminals {

    private MainGame mainGame;
    private Label property;
    private Label planetInformation;
    private Label disctrictsPrice;
    private Label infoPrice;
    private Label infoDist;
    private Stellarsystem choosen;
    private Fleet choosenFleet;
    private MyInputListener inputListener = new MyInputListener();
    private Skin corvette = null;
    private Skin destroyer = null;
    private Skin cruiser = null;
    private Skin battleship = null;
    private Skin titan = null;
    private Skin carrier = null;
    private int fleetInNationPosition;

    public Terminals() {
    }

    public Table systemTerminal(Nation nation, GameBoard board, int x, int y, String districtAmount, Skin colonySkin, Skin closeSkin, Skin launchSkin, com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle halogen) {

        Table terminal = new Table();
        terminal.setFillParent(true);
        //  terminal.setDebug(true, true);
        choosen = board.getBoardTile(x, y);
        choosenFleet = choosen.getSystemsFleet();
        ImageButton planetColonisation = new ImageButton(colonySkin);
        ImageButton close = new ImageButton(closeSkin);
        ImageButton fleetInSystem = new ImageButton(launchSkin);

        ImageButton corvetteB = new ImageButton(corvette);
        ImageButton destroyerB = new ImageButton(destroyer);
        ImageButton cruiserB = new ImageButton(cruiser);
        ImageButton battleshipB = new ImageButton(battleship);
        ImageButton titanB = new ImageButton(titan);
        ImageButton carrierB = new ImageButton(carrier);

        property = new Label(choosen.getNationality(), halogen);
        planetInformation = new Label(null, halogen);
        planetInformation.setText(districtAmount);
        infoDist = new Label(null, halogen);
        infoDist.setText("Amount of Districts");
        infoPrice = new Label(null, halogen);
        infoPrice.setText("Price");
        disctrictsPrice = new Label(null, halogen);
        disctrictsPrice.setText(choosen.getPrice() / 1000 + "k");


        if (choosenFleet != null) {
            for (int i = 0; i < nation.getFleet().size(); i++) {
                if (nation.getFleet().get(i).getxCoordinate() == choosenFleet.getxCoordinate() && nation.getFleet().get(i).getyCoordinate() == choosenFleet.getyCoordinate()) {
                    fleetInNationPosition = i;
                }
            }
        }
        inputListener.setBoard(board);
        inputListener.setNation(nation);
        inputListener.setFleetInNation(fleetInNationPosition);
        ImageButton systemColonisation = new ImageButton(colonySkin);
        addListeners(x, y, nation, board, close, systemColonisation, planetColonisation, fleetInSystem, corvetteB, destroyerB, cruiserB, battleshipB, titanB, carrierB);


        terminal.moveBy(720, 280);
        terminal.add();
        terminal.add(close);
        terminal.add(property);

        terminal.row();
        if (choosen.getNationality().equals("Uncolonised")) {
            terminal.row();
            terminal.add();
            terminal.add(systemColonisation);
            terminal.row();
        }
        terminal.add(infoDist);
        terminal.add(infoPrice);
        terminal.row();
        terminal.add(planetInformation);
        terminal.add(disctrictsPrice);
        if (!choosen.getExploit()) {
            terminal.add(planetColonisation);
        }
//ToDO Schiffe und Launch
        if (choosen.getNationality().equals(nation.getName())) {
            if (choosen.getSystemsFleet() != null) {
                terminal.row();
                terminal.add(fleetInSystem);
            }
            terminal.row();
            terminal.add(corvetteB);
            terminal.row();
            terminal.add(destroyerB);
            terminal.row();
            terminal.add(cruiserB);
            terminal.row();
            terminal.add(battleshipB);
            terminal.row();
            terminal.add(titanB);
            terminal.row();
            terminal.add(carrierB);
        }

        return terminal;
    }

    public void addListeners(int gX, int gY, Nation nation, GameBoard board, ImageButton close, ImageButton systemColonisation, ImageButton planetColonisation, ImageButton fleetControl, ImageButton corvetteB, ImageButton destroyerB, ImageButton cruiserB, ImageButton battleshipB, ImageButton titanB, ImageButton carrierB) {
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitTerminal();
            }
        });
        systemColonisation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int alloycost = 200;
                int yaoicost = 150;
                int foodcost = 1000;
                if (board.getBoardTile(gX, gY).getEnemy()) {
                    alloycost = alloycost * 2;
                    yaoicost = yaoicost * 2;
                    foodcost = foodcost * 2;
                }
                if (choosen.getEnemy()) {
                    if (choosen.getSystemsFleet() != null && choosen.getSystemsFleet().getMaxPower() > choosen.getEnemyPower() && nation.getAmountAlloy() >= alloycost && nation.getAmountYaoi() >= yaoicost && nation.getAmountFood() >= foodcost) {
                        coloniseSystem(nation, alloycost, yaoicost, foodcost);
                    }
                } else if (nation.getAmountAlloy() >= alloycost && nation.getAmountYaoi() >= yaoicost && nation.getAmountFood() >= foodcost) {
                    coloniseSystem(nation, alloycost, yaoicost, foodcost);
                }
                exitTerminal();
            }
        });
        planetColonisation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                if (nation.getAmountMinerals() >= choosen.getPrice() && choosen.getNationality().equals(nation.getName())) {
                    nation.setAmountMinerals(-1 * choosen.getPrice());
                    choosen.setEnemyPower(0);
                    for (int j = 0; j < choosen.getPlanetAmount(); j++) {
                        nation.addPlanet(choosen.getSystemsPlanets(j));
                    }
                    for (int i = 0; i < nation.getNationPlanets().size(); i++) {
                        for (int j = 0; j < nation.getNationPlanets().get(i).getMaxDistricts(); j++) {
                            nation.setIncomeEnergy(nation.getNationPlanets().get(i).getDistrict(j).getEnergyProduction());
                            nation.setIncomeAlloy(nation.getNationPlanets().get(i).getDistrict(j).getAlloyProduction());
                            nation.setIncomeFood(nation.getNationPlanets().get(i).getDistrict(j).getFoodProduction());
                            nation.setIncomeMinerals(nation.getNationPlanets().get(i).getDistrict(j).getMineralProduction());
                            nation.setIncomeYaoi(nation.getNationPlanets().get(i).getDistrict(j).getYaoiProduction());


                        }
                        for (int l = 0; l < 6; l++) {
                            nation.setIncomeEnergy(nation.getNationPlanets().get(i).getDistrict(l).getEnergyProduction());
                            nation.setIncomeAlloy(nation.getNationPlanets().get(i).getBuildings(l).getAlloyProduction());
                            nation.setIncomeFood(nation.getNationPlanets().get(i).getBuildings(l).getFoodProduction());
                            nation.setIncomeMinerals(nation.getNationPlanets().get(i).getBuildings(l).getMineralProduction());
                            nation.setIncomeYaoi(nation.getNationPlanets().get(i).getBuildings(l).getYaoiProduction());
                            nation.setFleetCapacity(nation.getNationPlanets().get(i).getBuildings(l).getFleetProduction());

                        }

                    }
                    choosen.setExploit(true);
                }
                exitTerminal();
            }
        });
        fleetControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inputListener.setChoosenFleet(choosen.getSystemsFleet());
                Gdx.input.getTextInput(inputListener, null, null, "x,y");
                exitTerminal();


            }
        });
        destroyerB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Destroyer().getCost()) {
                    nation.setAmountAlloy(-1 * new Destroyer().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Destroyer());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Destroyer());
                    } else {
                        choosen.getSystemsFleet().addShip(new Destroyer());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Destroyer());
                    }
                }

            }
        });
        cruiserB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Cruiser().getCost()) {
                    nation.setAmountAlloy(-1 * new Cruiser().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Cruiser());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Cruiser());
                    } else {
                        choosen.getSystemsFleet().addShip(new Cruiser());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Cruiser());
                    }
                }

            }
        });
        battleshipB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Battleship().getCost()) {
                    nation.setAmountAlloy(-1 * new Battleship().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Battleship());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Battleship());
                    } else {
                        choosen.getSystemsFleet().addShip(new Battleship());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Battleship());
                    }
                }

            }
        });
        carrierB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Carrier().getCost()) {
                    nation.setAmountAlloy(-1 * new Carrier().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Carrier());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Carrier());
                    } else {
                        choosen.getSystemsFleet().addShip(new Carrier());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Carrier());
                    }
                }

            }
        });
        titanB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Titan().getCost()) {
                    nation.setAmountAlloy(-1 * new Titan().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Titan());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Titan());
                    } else {
                        choosen.getSystemsFleet().addShip(new Titan());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Titan());
                    }
                }

            }
        });
        corvetteB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= new Corvette().getCost()) {
                    nation.setAmountAlloy(-1 * new Corvette().getCost());
                    if (choosen.getSystemsFleet() == null) {
                        choosen.addFleet(new Fleet(gX, gY));
                        nation.getFleet().add(new Fleet(gX, gY));
                        choosen.getSystemsFleet().addShip(new Corvette());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Corvette());
                    } else {
                        choosen.getSystemsFleet().addShip(new Corvette());
                        nation.getFleet().get(fleetInNationPosition).addShip(new Corvette());
                    }
                }

            }
        });
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public void exitTerminal() {
        mainGame.setTerminalActivation(false);
        mainGame.setSystemDisplay(false);
        mainGame.setChooseLimit(11);
    }

    public void setShipSkins(Skin corvette, Skin destroyer, Skin cruiser, Skin battleship, Skin carrier, Skin titan) {
        this.corvette = corvette;
        this.destroyer = destroyer;
        this.cruiser = cruiser;
        this.battleship = battleship;
        this.carrier = carrier;
        this.titan = titan;
    }

    public void coloniseSystem(Nation nation, int alloycost, int yaoicost, int foodcost) {

        nation.setAmountAlloy(-alloycost);
        nation.setAmountYaoi(-yaoicost);
        nation.setAmountFood(-foodcost);
        choosen.setNationality(nation.getName());
        property.setText(nation.getName());
        for (int i = 0; i < choosen.getPlanetAmount(); i++) {
            nation.addPlanet(choosen.getSystemsPlanets(i));
        }
        choosen.setEnemy(false);

    }

}
