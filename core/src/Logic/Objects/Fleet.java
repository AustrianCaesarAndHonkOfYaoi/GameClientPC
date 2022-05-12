package Logic.Objects;

import java.util.ArrayList;

public class Fleet {
    private ArrayList<Ships> shipsInFleet = new ArrayList<>();
    private int FleetLimit = 40;
    private int xCoordinate;
    private int yCoordinate;

    public ArrayList<Ships> getShipsInFleet() {
        return shipsInFleet;
    }
}
