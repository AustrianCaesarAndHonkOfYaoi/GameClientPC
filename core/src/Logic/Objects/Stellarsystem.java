package Logic.Objects;


import java.util.ArrayList;


public class Stellarsystem {

    private int pixelX;
    private int pixelY;
    private ArrayList<Fleet> systemsFleet = new ArrayList<>();
    private ArrayList<Planets> systemsPlanets = new ArrayList<>();
    private String nationality = "Uncolonised";
    private Boolean exploit = false;



    public Stellarsystem(int pixelX, int pixelY, Integer amountSystemsPlanets) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        if (amountSystemsPlanets>0) {
            for (int i = 0; i < amountSystemsPlanets; i++) {
                systemsPlanets.add(new Planets());
            }
        }
    }

    public void addFleet(Fleet fleet) {
        systemsFleet.add(fleet);
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
           amount+= systemsPlanets.get(i).getMaxDistricts();
        }
        System.out.println(amount);
        return amount;
    }

    public Boolean getExploit() {return exploit;}

    public void setExploit(Boolean exploit) {this.exploit = exploit;}
}
