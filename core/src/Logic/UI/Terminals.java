package Logic.UI;

import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import Logic.Objects.Stellarsystem;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.interstellar.client.MainGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Terminals {

    private MainGame mainGame;
    private Label property;
    private Label planetInformation;
    private Label disctrictsPrice;
    private Label infoPrice;
    private Label infoDist;
    private Stellarsystem choosen;
    private int price = 0;

    public Table systemTerminal(Nation nation, GameBoard board, int x, int y, String districtAmount, Skin colonySkin, Skin closeSkin, Skin launchSkin, com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle halogen) {
        Table terminal = new Table();
        terminal.setFillParent(true);
        terminal.setDebug(true, true);
        choosen = board.getBoardTile(x, y);
        ImageButton planetColonisation = new ImageButton(colonySkin);
        ImageButton close = new ImageButton(closeSkin);
        System.out.println(choosen.getNationality());
        property = new Label(choosen.getNationality(), halogen);
        planetInformation = new Label(null, halogen);
        planetInformation.setText(districtAmount);
        infoDist = new Label(null, halogen);
        infoDist.setText("Amount of Districts");
        infoPrice = new Label(null, halogen);
        infoPrice.setText("Price");
        disctrictsPrice = new Label(null, halogen);
        disctrictsPrice.setText(price / 1000 + "k");

        ImageButton systemColonisation = new ImageButton(colonySkin);
        addListeners(nation, board, close, systemColonisation, planetColonisation);

        for (int i = 0; i < choosen.getPlanetAmount(); i++) {
            price += choosen.getSystemsPlanets(i).getMaxDistricts() * 300;
        }

        terminal.moveBy(790, 420);
        terminal.add();
        terminal.add(close);
        terminal.add();
        //terminal.right();
        terminal.row();
        terminal.add();
        terminal.add(property);

        terminal.row();
        if (choosen.getNationality().equals("Uncolonised")) {
            terminal.row();
            terminal.add();
            terminal.add(systemColonisation);
            terminal.row();
        }

        //ToDO Planeten Textur
        terminal.add(infoDist);
        terminal.add(infoPrice);
        terminal.row();
        terminal.add(planetInformation);
        terminal.add(disctrictsPrice);
        if (!choosen.getExploit()) {
            terminal.add(planetColonisation);
        }
        //ToDo Planeten
//ToDO Schiffe und Launch

        return terminal;
    }

    public void addListeners(Nation nation, GameBoard board, ImageButton close, ImageButton systemColonisation, ImageButton planetColonisation) {
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitTerminal();
            }
        });

        systemColonisation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nation.getAmountAlloy() >= 200 && nation.getAmountYaoi() >= 75) {
                    nation.setAmountAlloy(-200);
                    nation.setAmountYaoi(-75);
                    choosen.setNationality(nation.getName());
                    property.setText(nation.getName());
                    for (int i = 0; i < choosen.getPlanetAmount(); i++) {
                        nation.addPlanet(choosen.getSystemsPlanets(i));
                    }

                } else {
                    System.out.println("Keine Ressourcen");
                }
                exitTerminal();
            }
        });
        planetColonisation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                if (nation.getAmountMinerals() >= price && choosen.getNationality().equals(nation.getName())) {
                    for (int j = 0; j < choosen.getPlanetAmount(); j++) {
                        nation.addPlanet(choosen.getSystemsPlanets(j));
                    }
                    for (int i = 0; i < nation.getNationPlanets().size(); i++) {
                        for (int j = 0; j < nation.getNationPlanets().get(i).getMaxDistricts(); j++) {
                            nation.setIncomeEnergy(nation.getNationPlanets().get(i).getDistrict(j).getEnergyProduction());
                            System.out.println(nation.getIncomeEnergy());
                            nation.setIncomeAlloy(nation.getNationPlanets().get(i).getDistrict(j).getAlloyProduction());
                            nation.setIncomeFood(nation.getNationPlanets().get(i).getDistrict(j).getFoodProduction());
                            nation.setIncomeMinerals(nation.getNationPlanets().get(i).getDistrict(j).getMineralProduction());
                            nation.setIncomeYaoi(nation.getNationPlanets().get(i).getDistrict(j).getYaoiProduction());


                        }
                        for (int l = 0; l < 6; l++) {
                            nation.setIncomeEnergy(nation.getNationPlanets().get(i).getDistrict(l).getEnergyProduction());
                            System.out.println(nation.getIncomeEnergy());
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
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public void exitTerminal() {
        mainGame.setTerminalActivation(false);
        mainGame.setSystemDisplay(false);
        mainGame.setChooseLimit(11);
    }
}
