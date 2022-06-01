package Logic.UI;

import Logic.Objects.Fleet;
import Logic.Objects.GameBoard;
import Logic.Objects.Nation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.interstellar.client.MainGame;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyInputListener implements Input.TextInputListener {
    private Fleet choosenFleet;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private GameBoard board;
    private Nation nation;
    private int fleetInNation;


    @Override
    public void input(String text) {
        String number1, number2;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ',') {
                number1 = text.substring(0, i);
                number2 = text.substring(i + 1);
                choosenFleet.setNewX(Integer.parseInt(number1)-1);
                choosenFleet.setNewY(11 - (Integer.parseInt(number2)-1));
            }
            int j = 1;

            Runnable moveTo = () -> {
                board.getBoardTile(choosenFleet.getxCoordinate(), choosenFleet.getyCoordinate()).removeFleet();
                Fleet nationFleet = nation.getFleet().get(fleetInNation);
                if (choosenFleet.getNewX() > choosenFleet.getxCoordinate()) {
                    choosenFleet.setxCoordinate(choosenFleet.getxCoordinate() + 1);
                    nationFleet.setxCoordinate(nationFleet.getxCoordinate() + 1);
                }
                if (choosenFleet.getNewX() < choosenFleet.getxCoordinate()) {
                    choosenFleet.setxCoordinate(choosenFleet.getxCoordinate() - 1);
                    nationFleet.setxCoordinate(nationFleet.getxCoordinate() - 1);
                }
                if (choosenFleet.getNewY() > choosenFleet.getyCoordinate()) {
                    choosenFleet.setyCoordinate(choosenFleet.getyCoordinate() + 1);
                    nationFleet.setyCoordinate(nationFleet.getyCoordinate() + 1);
                }
                if (choosenFleet.getNewY() < choosenFleet.getyCoordinate()) {
                    choosenFleet.setyCoordinate(choosenFleet.getyCoordinate() - 1);
                    nationFleet.setyCoordinate(nationFleet.getyCoordinate() - 1);
                }
                board.getBoardTile(choosenFleet.getxCoordinate(), choosenFleet.getyCoordinate()).addFleet(choosenFleet);


            };
            while (choosenFleet.getyCoordinate() != choosenFleet.getNewY() && choosenFleet.getxCoordinate() != choosenFleet.getNewX()) {
                scheduler.schedule(moveTo, j, TimeUnit.SECONDS);
                j++;
            }


        }


    }

    @Override
    public void canceled() {
    }

    public void setChoosenFleet(Fleet choosenFleet) {
        this.choosenFleet = choosenFleet;
    }

    public void setBoard(GameBoard b) {
        board = b;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public void setFleetInNation(int fleetInNation) {
        this.fleetInNation = fleetInNation;
    }
}
