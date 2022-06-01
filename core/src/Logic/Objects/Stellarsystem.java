package Logic.Objects;


import java.util.ArrayList;
import java.util.Random;


public class Stellarsystem {

    private int pixelX;
    private int pixelY;
    private Fleet systemsFleet;
    private ArrayList<Planets> systemsPlanets = new ArrayList<>();
    private String nationality = "Uncolonised";
    private Boolean exploit = false;
    private Boolean enemy;
    private int enemyPower;
    private int price;
    private Random randomCalls = new Random();


    public Stellarsystem(int pixelX, int pixelY, Integer amountSystemsPlanets) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        int random = randomCalls.nextInt(2);

        if (random == 1) {
            enemy = false;
        } else {
            enemy = true;
            enemyPower = randomCalls.nextInt(32000);
        }
        if (amountSystemsPlanets > 0) {
            for (int i = 0; i < amountSystemsPlanets; i++) {
                systemsPlanets.add(new Planets());
            }
            for (int i = 0; i < amountSystemsPlanets; i++) {
                price += getSystemsPlanets(i).getMaxDistricts() * 300;
                if (enemy) {
                    price += enemyPower;
                }
            }
        }

    }

    public void addFleet(Fleet fleet) {
        systemsFleet = fleet;
    }

    public void removeFleet() {
        systemsFleet = null;
    }

    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public int getPlanetAmount() {
        return systemsPlanets.size();
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Planets getSystemsPlanets(int i) {
        return systemsPlanets.get(i);
    }

    public double getSystemMaxDistrict() {
        double amount = 0;
        for (int i = 0; i < systemsPlanets.size(); i++) {
            amount += systemsPlanets.get(i).getMaxDistricts();
        }
        System.out.println(amount);
        return amount;
    }

    public Boolean getExploit() {
        return exploit;
    }

    public void setExploit(Boolean exploit) {
        this.exploit = exploit;
    }

    public int getPrice() {
        return price;
    }

    public Fleet getSystemsFleet() {
        return systemsFleet;
    }

    public Boolean getEnemy() {
        return enemy;
    }
public void setEnemy(Boolean enemy){this.enemy=enemy;}
    public int getEnemyPower() {
        return enemyPower;
    }

    public void setEnemyPower(int enemyPower) {
        this.enemyPower = enemyPower;
    }

}
