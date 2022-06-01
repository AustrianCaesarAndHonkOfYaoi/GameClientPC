package Logic.Objects;

import java.util.ArrayList;

public class Fleet {
    private ArrayList<Ships> shipsInFleet = new ArrayList<>();
    private int xCoordinate;
    private int yCoordinate;

    private int newX;
    private  int newY;

    public Fleet(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    public ArrayList<Ships> getShipsInFleet() {
        return shipsInFleet;
    }
    public int getNewX() {return newX;}

    public void setNewX(int newX) {this.newX = newX;}

    public int getNewY() {
        return newY;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public void addShip(Ships ship){
        shipsInFleet.add(ship);
    }
    public int getMaxPower(){
        int ges = 0;
        for (int i = 0; i < shipsInFleet.size(); i++) {
            ges += shipsInFleet.get(i).getFirepower();
        }
        return ges;
    }
}
